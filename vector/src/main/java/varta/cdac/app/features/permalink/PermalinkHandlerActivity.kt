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

package varta.cdac.app.features.permalink

import android.content.Intent
import android.os.Bundle
import varta.cdac.app.R
import varta.cdac.app.core.di.ActiveSessionHolder
import varta.cdac.app.core.di.ScreenComponent
import varta.cdac.app.core.extensions.replaceFragment
import varta.cdac.app.core.platform.VectorBaseActivity
import varta.cdac.app.databinding.FragmentProgressBinding
import varta.cdac.app.features.home.HomeActivity
import varta.cdac.app.features.home.LoadingFragment
import javax.inject.Inject

class PermalinkHandlerActivity : VectorBaseActivity<FragmentProgressBinding>() {

    @Inject lateinit var permalinkHandler: PermalinkHandler
    @Inject lateinit var sessionHolder: ActiveSessionHolder

    override fun getBinding() = FragmentProgressBinding.inflate(layoutInflater)

    override fun injectWith(injector: ScreenComponent) {
        injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        if (isFirstCreation()) {
            replaceFragment(R.id.simpleFragmentContainer, LoadingFragment::class.java)
        }
        handleIntent()
    }

    private fun handleIntent() {
        // If we are not logged in, open login screen.
        // In the future, we might want to relaunch the process after login.
        if (!sessionHolder.hasActiveSession()) {
            startLoginActivity()
            return
        }
        // We forward intent to HomeActivity (singleTask) to avoid the dueling app problem
        // https://stackoverflow.com/questions/25884954/deep-linking-and-multiple-app-instances
        intent.setClass(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)

        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent()
    }

    private fun startLoginActivity() {
        navigator.openLogin(
                context = this,
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        )
        finish()
    }
}
