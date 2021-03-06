/*
 * Copyright 2019 New Vector Ltd
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

package varta.cdac.app.features.signout.soft.epoxy

import android.os.Build
import android.text.Editable
import android.widget.Button
import androidx.autofill.HintConstants
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import varta.cdac.app.R
import varta.cdac.app.core.epoxy.ClickListener
import varta.cdac.app.core.epoxy.TextListener
import varta.cdac.app.core.epoxy.VectorEpoxyHolder
import varta.cdac.app.core.epoxy.VectorEpoxyModel
import varta.cdac.app.core.epoxy.addTextChangedListenerOnce
import varta.cdac.app.core.epoxy.onClick
import varta.cdac.app.core.epoxy.setValueOnce
import varta.cdac.app.core.extensions.showPassword
import varta.cdac.app.core.platform.SimpleTextWatcher
import varta.cdac.app.core.resources.StringProvider
import varta.cdac.app.core.ui.views.RevealPasswordImageView

@EpoxyModelClass(layout = R.layout.item_login_password_form)
abstract class LoginPasswordFormItem : VectorEpoxyModel<LoginPasswordFormItem.Holder>() {

    @EpoxyAttribute var passwordValue: String = ""
    @EpoxyAttribute var passwordShown: Boolean = false
    @EpoxyAttribute var submitEnabled: Boolean = false
    @EpoxyAttribute var errorText: String? = null
    @EpoxyAttribute lateinit var stringProvider: StringProvider
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var passwordRevealClickListener: ClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var forgetPasswordClickListener: ClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var submitClickListener: ClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onPasswordEdited: TextListener? = null

    private val textChangeListener = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            onPasswordEdited?.invoke(s.toString())
        }
    }

    override fun bind(holder: Holder) {
        super.bind(holder)

        setupAutoFill(holder)
        holder.passwordFieldTil.error = errorText
        renderPasswordField(holder)
        holder.passwordReveal.onClick(passwordRevealClickListener)
        holder.forgetPassword.onClick(forgetPasswordClickListener)
        holder.submit.isEnabled = submitEnabled
        holder.submit.onClick(submitClickListener)
        holder.setValueOnce(holder.passwordField, passwordValue)
        holder.passwordField.addTextChangedListenerOnce(textChangeListener)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.passwordField.removeTextChangedListener(textChangeListener)
    }

    private fun setupAutoFill(holder: Holder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.passwordField.setAutofillHints(HintConstants.AUTOFILL_HINT_PASSWORD)
        }
    }

    private fun renderPasswordField(holder: Holder) {
        holder.passwordField.showPassword(passwordShown)
        holder.passwordReveal.render(passwordShown)
    }

    class Holder : VectorEpoxyHolder() {
        val passwordField by bind<TextInputEditText>(R.id.itemLoginPasswordFormPasswordField)
        val passwordFieldTil by bind<TextInputLayout>(R.id.itemLoginPasswordFormPasswordFieldTil)
        val passwordReveal by bind<RevealPasswordImageView>(R.id.itemLoginPasswordFormPasswordReveal)
        val forgetPassword by bind<Button>(R.id.itemLoginPasswordFormForgetPasswordButton)
        val submit by bind<Button>(R.id.itemLoginPasswordFormSubmit)
    }
}
