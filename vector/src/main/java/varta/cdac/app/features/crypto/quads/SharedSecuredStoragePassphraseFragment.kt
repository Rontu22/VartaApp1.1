/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package varta.cdac.app.features.crypto.quads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.text.toSpannable
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.widget.editorActionEvents
import com.jakewharton.rxbinding3.widget.textChanges
import varta.cdac.app.R
import varta.cdac.app.core.extensions.showPassword
import varta.cdac.app.core.platform.VectorBaseFragment
import varta.cdac.app.core.resources.ColorProvider
import varta.cdac.app.databinding.FragmentSsssAccessFromPassphraseBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SharedSecuredStoragePassphraseFragment @Inject constructor(
        private val colorProvider: ColorProvider
) : VectorBaseFragment<FragmentSsssAccessFromPassphraseBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSsssAccessFromPassphraseBinding {
        return FragmentSsssAccessFromPassphraseBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: SharedSecureStorageViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If has passphrase
        val pass = getString(R.string.recovery_passphrase)
        val key = getString(R.string.recovery_key)
        views.ssssRestoreWithPassphraseWarningText.text = getString(
                R.string.enter_secret_storage_passphrase_or_key,
                pass,
                key
        )
                .toSpannable()
        // TODO Restore coloration when we will have a FAQ to open with those terms
        // .colorizeMatchingText(pass, colorProvider.getColorFromAttribute(android.R.attr.textColorLink))
        // .colorizeMatchingText(key, colorProvider.getColorFromAttribute(android.R.attr.textColorLink))

        views.ssssPassphraseEnterEdittext.editorActionEvents()
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.actionId == EditorInfo.IME_ACTION_DONE) {
                        submit()
                    }
                }
                .disposeOnDestroyView()

        views.ssssPassphraseEnterEdittext.textChanges()
                .subscribe {
                    views.ssssPassphraseEnterTil.error = null
                    views.ssssPassphraseSubmit.isEnabled = it.isNotBlank()
                }
                .disposeOnDestroyView()

        views.ssssPassphraseReset.views.bottomSheetActionClickableZone.debouncedClicks {
            sharedViewModel.handle(SharedSecureStorageAction.ForgotResetAll)
        }

        sharedViewModel.observeViewEvents {
            when (it) {
                is SharedSecureStorageViewEvent.InlineError -> {
                    views.ssssPassphraseEnterTil.error = it.message
                }
            }
        }

        views.ssssPassphraseSubmit.debouncedClicks { submit() }
        views.ssssPassphraseUseKey.debouncedClicks { sharedViewModel.handle(SharedSecureStorageAction.UseKey) }
        views.ssssViewShowPassword.debouncedClicks { sharedViewModel.handle(SharedSecureStorageAction.TogglePasswordVisibility) }
    }

    fun submit() {
        val text = views.ssssPassphraseEnterEdittext.text.toString()
        if (text.isBlank()) return // Should not reach this point as button disabled
        views.ssssPassphraseSubmit.isEnabled = false
        sharedViewModel.handle(SharedSecureStorageAction.SubmitPassphrase(text))
    }

    override fun invalidate() = withState(sharedViewModel) { state ->
        val shouldBeVisible = state.passphraseVisible
        views.ssssPassphraseEnterEdittext.showPassword(shouldBeVisible)
        views.ssssViewShowPassword.render(shouldBeVisible)
    }
}
