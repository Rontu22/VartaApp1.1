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

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.viewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.parcelize.Parcelize
import varta.cdac.app.R
import varta.cdac.app.core.di.ScreenComponent
import varta.cdac.app.core.error.ErrorFormatter
import varta.cdac.app.core.platform.VectorBaseActivity
import varta.cdac.app.databinding.ActivityAddParticipantBinding
import varta.cdac.app.features.contactsbook.ContactsBookViewModel
import varta.cdac.app.features.contactsbook.ContactsBookViewState
import varta.cdac.app.features.userdirectory.UserListViewModel
import varta.cdac.app.features.userdirectory.UserListViewState
import javax.inject.Inject

@Parcelize
data class AddParticipantArgs(val callId : String) : Parcelable

private const val USER_LIST_FRAGMENT = "USER_LIST_FRAGMENT"

class AddParticipantActivity : VectorBaseActivity<ActivityAddParticipantBinding>() ,
AddParticipantViewModel.Factory,
UserListViewModel.Factory,
ContactsBookViewModel.Factory{

    @Inject lateinit var userListViewModelFactory: UserListViewModel.Factory
    @Inject lateinit var addParticipantViewModelFactory: AddParticipantViewModel.Factory
    @Inject lateinit var contactsBookViewModelFactory: ContactsBookViewModel.Factory
    @Inject lateinit var errorFormatter: ErrorFormatter

    private lateinit var sectionsPagerAdapter: AddParticipantPagerAdapter
    private val addParticipantViewModel : AddParticipantViewModel by viewModel()

    override fun getBinding() = ActivityAddParticipantBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.vectorCoordinatorLayoutAddParticipant

//    override fun injectWith(injector: ScreenComponent) {
//        injector.inject(this)
//    }

//    override fun injectWith(injector: ScreenComponent) {
//        injector.inject(this)
//    }

    override fun create(initialState: UserListViewState):UserListViewModel{
        return userListViewModelFactory.create(initialState)
    }

    override fun create(initialState: AddParticipantViewState): AddParticipantViewModel {
        return addParticipantViewModelFactory.create(initialState)
    }
    override fun create(initialState: ContactsBookViewState): ContactsBookViewModel {
        return contactsBookViewModelFactory.create(initialState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        waitingView = views.waitingView.waitingView

        addParticipantViewModel.observeViewEvents {
            when(it){
                is AddParticipantViewEvents.Dismiss -> finish()
                AddParticipantViewEvents.Loading -> showWaitingView()
                is AddParticipantViewEvents.FailToTransfer -> showSnackbar(getString(R.string.call_transfer_failure))
            }
        }

        sectionsPagerAdapter = AddParticipantPagerAdapter(this)
        views.addParticipantViewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(views.addParticipantTabLayout,views.addParticipantViewPager){tab,position ->
            when(position){
                AddParticipantPagerAdapter.USER_LIST_INDEX -> tab.text = "USERS"
                AddParticipantPagerAdapter.DIAL_PAD_INDEX -> tab.text = "CALL USERS"
            }
        }.attach()

        configureToolbar(views.addParticipantToolbar)
        views.addParticipantToolbar.title = "Add Participant"
        setupConnectAction()


       // setContentView(R.layout.activity_add_participant)
    }
    private fun setupConnectAction(){
        views.addParticipantConnectAction.debouncedClicks {
            when(views.addParticipantTabLayout.selectedTabPosition)
            {
                AddParticipantPagerAdapter.USER_LIST_INDEX ->{
                    val selectedUser = sectionsPagerAdapter.userListFragment?.getCurrentState()?.getSelectedMatrixId()?.firstOrNull() ?:return@debouncedClicks
                    val action = AddParticipantAction.ConnectWithUserId(views.addParticipantConsultCheckBox.isChecked,selectedUser)
                    addParticipantViewModel.handle(action)
                }
                AddParticipantPagerAdapter.DIAL_PAD_INDEX ->{
                    val phoneNumber = sectionsPagerAdapter.dialPadFragment?.getRawInput() ?: return@debouncedClicks
                    val action = AddParticipantAction.ConnectWithPhoneNumber(views.addParticipantConsultCheckBox.isChecked,phoneNumber)
                    addParticipantViewModel.handle(action)
                }
            }
        }
    }
    companion object{
        fun newIntent(context: Context,callId: String): Intent {
            return Intent(context,AddParticipantActivity::class.java).also {
                it.putExtra(MvRx.KEY_ARG,AddParticipantArgs(callId))
            }
        }
    }
}
