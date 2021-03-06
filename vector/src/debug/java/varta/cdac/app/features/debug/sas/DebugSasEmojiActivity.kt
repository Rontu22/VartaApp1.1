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

package varta.cdac.app.features.debug.sas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import varta.cdac.app.core.extensions.cleanup
import varta.cdac.app.core.extensions.configureWith
import varta.cdac.app.databinding.FragmentGenericRecyclerBinding
import org.matrix.android.sdk.api.crypto.getAllVerificationEmojis

class DebugSasEmojiActivity : AppCompatActivity() {

    private lateinit var views: FragmentGenericRecyclerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = FragmentGenericRecyclerBinding.inflate(layoutInflater)
        setContentView(views.root)
        val controller = SasEmojiController()
        views.genericRecyclerView.configureWith(controller)
        controller.setData(SasState(getAllVerificationEmojis()))
    }

    override fun onDestroy() {
        views.genericRecyclerView.cleanup()
        super.onDestroy()
    }
}
