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

package varta.cdac.app.features.home.room.detail.timeline.item

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import varta.cdac.app.R
import varta.cdac.app.core.epoxy.VectorEpoxyHolder
import varta.cdac.app.core.epoxy.VectorEpoxyModel
import me.saket.bettermovementmethod.BetterLinkMovementMethod

@EpoxyModelClass(layout = R.layout.item_timeline_event_create)
abstract class RoomCreateItem : VectorEpoxyModel<RoomCreateItem.Holder>() {

    @EpoxyAttribute lateinit var text: CharSequence

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.description.movementMethod = BetterLinkMovementMethod.getInstance()
        holder.description.text = text
    }

    class Holder : VectorEpoxyHolder() {
        val description by bind<TextView>(R.id.roomCreateItemDescription)
    }
}
