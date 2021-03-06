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

package varta.cdac.app.features.settings.devtools

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import varta.cdac.app.core.ui.list.GenericItem_
import varta.cdac.app.core.utils.createUIHandler
import me.gujun.android.span.span
import org.matrix.android.sdk.internal.crypto.OutgoingRoomKeyRequest
import javax.inject.Inject

class OutgoingKeyRequestPagedController @Inject constructor() : PagedListEpoxyController<OutgoingRoomKeyRequest>(
        // Important it must match the PageList builder notify Looper
        modelBuildingHandler = createUIHandler()
) {

    interface InteractionListener {
        // fun didTap(data: UserAccountData)
    }

    var interactionListener: InteractionListener? = null

    override fun buildItemModel(currentPosition: Int, item: OutgoingRoomKeyRequest?): EpoxyModel<*> {
        val roomKeyRequest = item ?: return GenericItem_().apply { id(currentPosition) }

        return GenericItem_().apply {
            id(roomKeyRequest.requestId)
            title(roomKeyRequest.requestId)
            description(
                    span {
                        span("roomId: ") {
                            textStyle = "bold"
                        }
                        +"${roomKeyRequest.roomId}"

                        span("\nsessionId: ") {
                            textStyle = "bold"
                        }
                        +"${roomKeyRequest.sessionId}"
                        span("\nstate: ") {
                            textStyle = "bold"
                        }
                        +roomKeyRequest.state.name
                    }
            )
        }
    }
}
