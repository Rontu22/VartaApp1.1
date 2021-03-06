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

package varta.cdac.app.features.sync.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import varta.cdac.app.R
import varta.cdac.app.core.utils.isAirplaneModeOn
import varta.cdac.app.databinding.ViewSyncStateBinding

import org.matrix.android.sdk.api.session.sync.SyncState

class SyncStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle) {

    private val views: ViewSyncStateBinding

    init {
        inflate(context, R.layout.view_sync_state, this)
        views = ViewSyncStateBinding.bind(this)
    }

    fun render(newState: SyncState) {
        views.syncStateProgressBar.isVisible = newState is SyncState.Running && newState.afterPause

        if (newState == SyncState.NoNetwork) {
            val isAirplaneModeOn = isAirplaneModeOn(context)
            views.syncStateNoNetwork.isVisible = isAirplaneModeOn.not()
            views.syncStateNoNetworkAirplane.isVisible = isAirplaneModeOn
        } else {
            views.syncStateNoNetwork.isVisible = false
            views.syncStateNoNetworkAirplane.isVisible = false
        }
    }
}
