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

package varta.cdac.app.core.ui.bottomsheet

import com.airbnb.mvrx.MvRxState
import varta.cdac.app.core.platform.EmptyAction
import varta.cdac.app.core.platform.EmptyViewEvents
import varta.cdac.app.core.platform.VectorViewModel

abstract class BottomSheetGenericViewModel<State : MvRxState>(initialState: State) :
        VectorViewModel<State, EmptyAction, EmptyViewEvents>(initialState) {

    override fun handle(action: EmptyAction) {
        // No op
    }
}
