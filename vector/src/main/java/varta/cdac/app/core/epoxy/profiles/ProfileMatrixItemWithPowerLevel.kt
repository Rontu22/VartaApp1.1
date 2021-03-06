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
 *
 */

package varta.cdac.app.core.epoxy.profiles

import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import varta.cdac.app.R
import varta.cdac.app.core.extensions.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_profile_matrix_item)
abstract class ProfileMatrixItemWithPowerLevel : BaseProfileMatrixItem<ProfileMatrixItemWithPowerLevel.Holder>() {

    @EpoxyAttribute var powerLevelLabel: CharSequence? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.editableView.isVisible = false
        holder.powerLabel.setTextOrHide(powerLevelLabel)
    }

    class Holder : ProfileMatrixItem.Holder() {
        val powerLabel by bind<TextView>(R.id.matrixItemPowerLevelLabel)
    }
}
