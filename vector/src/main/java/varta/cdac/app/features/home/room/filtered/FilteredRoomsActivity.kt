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

package varta.cdac.app.features.home.room.filtered

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import varta.cdac.app.R
import varta.cdac.app.core.di.ScreenComponent
import varta.cdac.app.core.extensions.replaceFragment
import varta.cdac.app.core.platform.VectorBaseActivity
import varta.cdac.app.databinding.ActivityFilteredRoomsBinding
import varta.cdac.app.features.home.RoomListDisplayMode
import varta.cdac.app.features.home.room.list.RoomListFragment
import varta.cdac.app.features.home.room.list.RoomListParams

class FilteredRoomsActivity : VectorBaseActivity<ActivityFilteredRoomsBinding>() {

    private val roomListFragment: RoomListFragment?
        get() {
            return supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as? RoomListFragment
        }

    override fun getBinding() = ActivityFilteredRoomsBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.coordinatorLayout

    override fun injectWith(injector: ScreenComponent) {
        injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureToolbar(views.filteredRoomsToolbar)
        if (isFirstCreation()) {
            val params = RoomListParams(RoomListDisplayMode.FILTERED)
            replaceFragment(R.id.filteredRoomsFragmentContainer, RoomListFragment::class.java, params, FRAGMENT_TAG)
        }
        views.filteredRoomsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                roomListFragment?.filterRoomsWith(newText)
                return true
            }
        })
        // Open the keyboard immediately
        views.filteredRoomsSearchView.requestFocus()
    }

    companion object {
        private const val FRAGMENT_TAG = "RoomListFragment"

        fun newIntent(context: Context): Intent {
            return Intent(context, FilteredRoomsActivity::class.java)
        }
    }
}
