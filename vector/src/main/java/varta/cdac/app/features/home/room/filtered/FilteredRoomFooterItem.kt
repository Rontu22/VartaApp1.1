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

package varta.cdac.app.features.home.room.filtered

import android.widget.Button
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import varta.cdac.app.R
import varta.cdac.app.core.epoxy.VectorEpoxyHolder
import varta.cdac.app.core.epoxy.VectorEpoxyModel
import varta.cdac.app.core.epoxy.onClick
import varta.cdac.app.features.home.room.list.widget.NotifsFabMenuView

@EpoxyModelClass(layout = R.layout.item_room_filter_footer)
abstract class FilteredRoomFooterItem : VectorEpoxyModel<FilteredRoomFooterItem.Holder>() {

    @EpoxyAttribute
    var listener: FilteredRoomFooterItemListener? = null

    @EpoxyAttribute
    var currentFilter: String = ""

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.createRoomButton.onClick { listener?.createRoom(currentFilter) }
        holder.createDirectChat.onClick { listener?.createDirectChat() }
        holder.openRoomDirectory.onClick { listener?.openRoomDirectory(currentFilter) }
    }

    class Holder : VectorEpoxyHolder() {
        val createRoomButton by bind<Button>(R.id.roomFilterFooterCreateRoom)
        val createDirectChat by bind<Button>(R.id.roomFilterFooterCreateDirect)
        val openRoomDirectory by bind<Button>(R.id.roomFilterFooterOpenRoomDirectory)
    }

    interface FilteredRoomFooterItemListener : NotifsFabMenuView.Listener {
        fun createRoom(initialName: String)
    }
}
