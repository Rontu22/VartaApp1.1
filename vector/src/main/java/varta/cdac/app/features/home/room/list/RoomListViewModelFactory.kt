/*
 * Copyright 2019 New Vector Ltd
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

package varta.cdac.app.features.home.room.list

import varta.cdac.app.AppStateHandler
import varta.cdac.app.core.resources.StringProvider
import varta.cdac.app.features.settings.VectorPreferences
import org.matrix.android.sdk.api.session.Session
import javax.inject.Inject
import javax.inject.Provider

class RoomListViewModelFactory @Inject constructor(private val session: Provider<Session>,
                                                   private val appStateHandler: AppStateHandler,
                                                   private val stringProvider: StringProvider,
                                                   private val vectorPreferences: VectorPreferences)
    : RoomListViewModel.Factory {

    override fun create(initialState: RoomListViewState): RoomListViewModel {
        return RoomListViewModel(
                initialState,
                session.get(),
                stringProvider,
                appStateHandler,
                vectorPreferences
        )
    }
}
