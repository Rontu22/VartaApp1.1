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

package varta.cdac.app.features.call.add_participants

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.extensions.orFalse
import org.matrix.android.sdk.api.session.call.CallState
import org.matrix.android.sdk.api.session.call.MxCall
import varta.cdac.app.core.extensions.exhaustive
import varta.cdac.app.core.platform.VectorViewModel
import varta.cdac.app.features.call.dialpad.DialPadLookup
import varta.cdac.app.features.call.transfer.CallTransferViewEvents
import varta.cdac.app.features.call.webrtc.WebRtcCall
import varta.cdac.app.features.call.webrtc.WebRtcCallManager
import varta.cdac.app.features.createdirect.DirectRoomHelper

class AddParticipantViewModel @AssistedInject constructor(@Assisted initialState: AddParticipantViewState,
                                    private val dialPadLookup: DialPadLookup,
                                    private val directRoomHelper: DirectRoomHelper,
                                    private val callManager: WebRtcCallManager)
    : VectorViewModel<AddParticipantViewState,AddParticipantAction,AddParticipantViewEvents>(initialState){

        @AssistedFactory
        interface Factory{
            fun create(initialState: AddParticipantViewState):AddParticipantViewModel
        }
    companion object : MvRxViewModelFactory<AddParticipantViewModel,AddParticipantViewState>{
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: AddParticipantViewState): AddParticipantViewModel? {
          //  val activity : AddParticipantActivity = (viewModelContext as ActivityViewModelContext).activity()
            return super.create(viewModelContext, state)
        }
    }

    private val call = callManager.getCallById(initialState.callId)
    private val callListener = object : WebRtcCall.Listener{
        override fun onStateUpdate(call: MxCall) {
            if(call.state == CallState.Terminated)
            {
                _viewEvents.post(AddParticipantViewEvents.Dismiss)
            }
        }
    }
    init {
        if(call == null){
            _viewEvents.post(AddParticipantViewEvents.Dismiss)
        }else{
            call.addListener(callListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        call?.removeListener(callListener)
    }

    override fun handle(action: AddParticipantAction) {
        when(action){
            is AddParticipantAction.ConnectWithUserId -> connectWithUserId(action)
            is AddParticipantAction.ConnectWithPhoneNumber -> connectWithPhoneNumber(action)
        }.exhaustive
    }
    private fun connectWithUserId(action: AddParticipantAction.ConnectWithUserId)
    {
        viewModelScope.launch {
            try {
                if(action.consultFirst){
                    val dmRoomId = directRoomHelper.ensureDMExists(action.selectedUserId)
                    callManager.startOutgoingCall(
                            nativeRoomId = dmRoomId,
                            otherUserId = action.selectedUserId,
                            isVideoCall = call?.mxCall?.isVideoCall.orFalse(),
                            transferee = call
                    )
                }else{
                    call?.transferToUser(action.selectedUserId,null)
                }
                _viewEvents.post(AddParticipantViewEvents.Dismiss)

            }catch (failure: Throwable){
                _viewEvents.post(AddParticipantViewEvents.FailToTransfer)
            }
        }
    }

    private fun connectWithPhoneNumber(action: AddParticipantAction.ConnectWithPhoneNumber)
    {
        viewModelScope.launch {
            try {
                _viewEvents.post(AddParticipantViewEvents.Loading)
                val result = dialPadLookup.lookupPhoneNumber(action.phoneNumber)
                if(action.consultFirst)
                {
                    callManager.startOutgoingCall(
                            nativeRoomId = result.roomId,
                            otherUserId = result.userId,
                            isVideoCall = call?.mxCall?.isVideoCall.orFalse(),
                            transferee = call
                    )
                }else{
                    call?.transferToUser(result.userId,result.roomId)
                }
                _viewEvents.post(AddParticipantViewEvents.Dismiss)
            }catch (failure:Throwable)
            {
                _viewEvents.post(AddParticipantViewEvents.FailToTransfer)
            }
        }
    }
}
