/*
 * Copyright (c) 2021 New Vector Ltd
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

package varta.cdac.app.features.spaces.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import varta.cdac.app.core.epoxy.onClick
import varta.cdac.app.core.platform.VectorBaseFragment
import varta.cdac.app.databinding.FragmentSpaceCreateChooseTypeBinding
import javax.inject.Inject

class ChooseSpaceTypeFragment @Inject constructor() : VectorBaseFragment<FragmentSpaceCreateChooseTypeBinding>() {

    private val sharedViewModel: CreateSpaceViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceCreateChooseTypeBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.publicButton.onClick {
            sharedViewModel.handle(CreateSpaceAction.SetRoomType(SpaceType.Public))
        }

        views.privateButton.onClick {
            sharedViewModel.handle(CreateSpaceAction.SetRoomType(SpaceType.Private))
        }
    }
}
