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
package varta.cdac.app.features.roomprofile.alias.detail

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.assisted.AssistedFactory
import varta.cdac.app.core.platform.EmptyAction
import varta.cdac.app.core.platform.EmptyViewEvents
import varta.cdac.app.core.platform.VectorViewModel
import org.matrix.android.sdk.api.session.Session

class RoomAliasBottomSheetViewModel @AssistedInject constructor(
        @Assisted initialState: RoomAliasBottomSheetState,
        session: Session
) : VectorViewModel<RoomAliasBottomSheetState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory {
        fun create(initialState: RoomAliasBottomSheetState): RoomAliasBottomSheetViewModel
    }

    companion object : MvRxViewModelFactory<RoomAliasBottomSheetViewModel, RoomAliasBottomSheetState> {

        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: RoomAliasBottomSheetState): RoomAliasBottomSheetViewModel? {
            val fragment: RoomAliasBottomSheet = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.roomAliasBottomSheetViewModelFactory.create(state)
        }
    }

    init {
        setState {
            copy(
                    matrixToLink = session.permalinkService().createPermalink(alias)
            )
        }
    }

    override fun handle(action: EmptyAction) {
        // No op
    }
}
