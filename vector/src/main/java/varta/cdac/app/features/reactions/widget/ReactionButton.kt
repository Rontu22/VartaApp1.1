/*
 * Copyright 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package varta.cdac.app.features.reactions.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import varta.cdac.app.EmojiCompatWrapper
import varta.cdac.app.R
import varta.cdac.app.core.di.HasScreenInjector
import varta.cdac.app.core.utils.TextUtils
import varta.cdac.app.databinding.ReactionButtonBinding
import javax.inject.Inject

/**
 * An animated reaction button.
 * Displays a String reaction (emoji), with a count, and that can be selected or not (toggle)
 */
class ReactionButton @JvmOverloads constructor(context: Context,
                                               attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnLongClickListener {

    init {
        if (context is HasScreenInjector) {
            context.injector().inject(this)
        }
    }

    companion object {
        private val DECCELERATE_INTERPOLATOR = DecelerateInterpolator()
        private val ACCELERATE_DECELERATE_INTERPOLATOR = AccelerateDecelerateInterpolator()
        private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)
    }

    @Inject lateinit var emojiCompatWrapper: EmojiCompatWrapper

    private val views: ReactionButtonBinding

    var reactedListener: ReactedListener? = null
    private var dotPrimaryColor: Int = 0
    private var dotSecondaryColor: Int = 0
    private var circleStartColor: Int = 0
    private var circleEndColor: Int = 0

    var reactionCount = 11
        set(value) {
            field = value
            views.reactionCount.text = TextUtils.formatCountToShortDecimal(value)
        }

    var reactionString = "😀"
        set(value) {
            field = value
            // maybe cache this for performances?
            val emojiSpanned = emojiCompatWrapper.safeEmojiSpanify(value)
            views.reactionText.text = emojiSpanned
        }

    private var animationScaleFactor: Float = 0.toFloat()

    private var isChecked: Boolean = false

    private var animatorSet: AnimatorSet? = null

    private var onDrawable: Drawable? = null
    private var offDrawable: Drawable? = null

    init {
        inflate(context, R.layout.reaction_button, this)
        views = ReactionButtonBinding.bind(this)
        views.reactionCount.text = TextUtils.formatCountToShortDecimal(reactionCount)

//        emojiView?.typeface = this.emojiTypeFace ?: Typeface.DEFAULT
        context.withStyledAttributes(attrs, R.styleable.ReactionButton, defStyleAttr) {
            onDrawable = ContextCompat.getDrawable(context, R.drawable.reaction_rounded_rect_shape)
            offDrawable = ContextCompat.getDrawable(context, R.drawable.reaction_rounded_rect_shape_off)

            circleStartColor = getColor(R.styleable.ReactionButton_circle_start_color, 0)

            if (circleStartColor != 0) {
                views.circle.startColor = circleStartColor
            }

            circleEndColor = getColor(R.styleable.ReactionButton_circle_end_color, 0)

            if (circleEndColor != 0) {
                views.circle.endColor = circleEndColor
            }

            dotPrimaryColor = getColor(R.styleable.ReactionButton_dots_primary_color, 0)
            dotSecondaryColor = getColor(R.styleable.ReactionButton_dots_secondary_color, 0)

            if (dotPrimaryColor != 0 && dotSecondaryColor != 0) {
                views.dots.setColors(dotPrimaryColor, dotSecondaryColor)
            }

            getString(R.styleable.ReactionButton_emoji)?.let {
                reactionString = it
            }

            reactionCount = getInt(R.styleable.ReactionButton_reaction_count, 0)

            val status = getBoolean(R.styleable.ReactionButton_toggled, false)
            setChecked(status)
        }

        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    private fun getDrawableFromResource(array: TypedArray, styleableIndexId: Int): Drawable? {
        val id = array.getResourceId(styleableIndexId, -1)

        return if (-1 != id) ContextCompat.getDrawable(context, id) else null
    }

    /**
     * This triggers the entire functionality of the button such as icon changes,
     * animations, listeners etc.
     *
     * @param v
     */
    override fun onClick(v: View) {
        if (!isEnabled) {
            return
        }

        isChecked = !isChecked

        // icon!!.setImageDrawable(if (isChecked) likeDrawable else unLikeDrawable)
        background = if (isChecked) onDrawable else offDrawable

        if (isChecked) {
            reactedListener?.onReacted(this)
        } else {
            reactedListener?.onUnReacted(this)
        }

        if (animatorSet != null) {
            animatorSet!!.cancel()
        }

        if (isChecked) {
            views.reactionText.animate().cancel()
            views.reactionText.scaleX = 0f
            views.reactionText.scaleY = 0f

            views.circle.innerCircleRadiusProgress = 0f
            views.circle.outerCircleRadiusProgress = 0f
            views.dots.currentProgress = 0f

            animatorSet = AnimatorSet()

            val outerCircleAnimator = ObjectAnimator.ofFloat(views.circle, CircleView.OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f)
            outerCircleAnimator.duration = 250
            outerCircleAnimator.interpolator = DECCELERATE_INTERPOLATOR

            val innerCircleAnimator = ObjectAnimator.ofFloat(views.circle, CircleView.INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f)
            innerCircleAnimator.duration = 200
            innerCircleAnimator.startDelay = 200
            innerCircleAnimator.interpolator = DECCELERATE_INTERPOLATOR

            val starScaleYAnimator = ObjectAnimator.ofFloat(views.reactionText, ImageView.SCALE_Y, 0.2f, 1f)
            starScaleYAnimator.duration = 350
            starScaleYAnimator.startDelay = 250
            starScaleYAnimator.interpolator = OVERSHOOT_INTERPOLATOR

            val starScaleXAnimator = ObjectAnimator.ofFloat(views.reactionText, ImageView.SCALE_X, 0.2f, 1f)
            starScaleXAnimator.duration = 350
            starScaleXAnimator.startDelay = 250
            starScaleXAnimator.interpolator = OVERSHOOT_INTERPOLATOR

            val dotsAnimator = ObjectAnimator.ofFloat(views.dots, DotsView.DOTS_PROGRESS, 0f, 1f)
            // .ofFloat<DotsView>(views.dots, DotsView.DOTS_PROGRESS, 0, 1f)
            dotsAnimator.duration = 900
            dotsAnimator.startDelay = 50
            dotsAnimator.interpolator = ACCELERATE_DECELERATE_INTERPOLATOR

            animatorSet!!.playTogether(
                    outerCircleAnimator,
                    innerCircleAnimator,
                    starScaleYAnimator,
                    starScaleXAnimator,
                    dotsAnimator
            )

            animatorSet!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    views.circle.innerCircleRadiusProgress = 0f
                    views.circle.outerCircleRadiusProgress = 0f
                    views.dots.currentProgress = 0f
                    views.reactionText.scaleX = 1f
                    views.reactionText.scaleY = 1f
                }

                override fun onAnimationEnd(animation: Animator) {
//                    if (animationEndListener != null) {
//                        //  animationEndListener!!.onAnimationEnd(this@ReactionButton)
//                    }
                }
            })

            animatorSet!!.start()
        }
    }

    /**
     * Used to trigger the scale animation that takes places on the
     * icon when the button is touched.
     *
     * @param event
     * @return
     */
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        if (!isEnabled)
//            return true
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN   ->
//                /*
//                Commented out this line and moved the animation effect to the action up event due to
//                conflicts that were occurring when library is used in sliding type views.
//
//                icon.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator(DECCELERATE_INTERPOLATOR);
//                */
//                isPressed = true
//
//            MotionEvent.ACTION_MOVE   -> {
//                val x = event.x
//                val y = event.y
//                val isInside = x > 0 && x < width && y > 0 && y < height
//                if (isPressed != isInside) {
//                    isPressed = isInside
//                }
//            }
//
//            MotionEvent.ACTION_UP     -> {
//                views.reactionText!!.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).interpolator = DECCELERATE_INTERPOLATOR
//                views.reactionText!!.animate().scaleX(1f).scaleY(1f).interpolator = DECCELERATE_INTERPOLATOR
//                if (isPressed) {
//                    performClick()
//                    isPressed = false
//                }
//            }
//            MotionEvent.ACTION_CANCEL -> isPressed = false
//        }
//        return true
//    }

    override fun onLongClick(v: View?): Boolean {
        reactedListener?.onLongClick(this)
        return reactedListener != null
    }

    /**
     * This set sets the colours that are used for the little dots
     * that will be exploding once the like button is clicked.
     *
     * @param primaryColor
     * @param secondaryColor
     */
    fun setExplodingDotColorsRes(@ColorRes primaryColor: Int, @ColorRes secondaryColor: Int) {
        views.dots.setColors(ContextCompat.getColor(context, primaryColor), ContextCompat.getColor(context, secondaryColor))
    }

    fun setExplodingDotColorsInt(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        views.dots.setColors(primaryColor, secondaryColor)
    }

    fun setCircleStartColorRes(@ColorRes circleStartColor: Int) {
        this.circleStartColor = ContextCompat.getColor(context, circleStartColor)
        views.circle.startColor = this.circleStartColor
    }

    fun setCircleStartColorInt(@ColorInt circleStartColor: Int) {
        this.circleStartColor = circleStartColor
        views.circle.startColor = circleStartColor
    }

    fun setCircleEndColorRes(@ColorRes circleEndColor: Int) {
        this.circleEndColor = ContextCompat.getColor(context, circleEndColor)
        views.circle.endColor = this.circleEndColor
    }

    /**
     * Sets the initial state of the button to liked
     * or unliked.
     *
     * @param status
     */
    fun setChecked(status: Boolean?) {
        if (status!!) {
            isChecked = true
            background = onDrawable
        } else {
            isChecked = false
            background = offDrawable
        }
    }

    /**
     * Sets the factor by which the dots should be sized.
     */
    fun setAnimationScaleFactor(animationScaleFactor: Float) {
        this.animationScaleFactor = animationScaleFactor
    }

    interface ReactedListener {
        fun onReacted(reactionButton: ReactionButton)
        fun onUnReacted(reactionButton: ReactionButton)
        fun onLongClick(reactionButton: ReactionButton)
    }
}
