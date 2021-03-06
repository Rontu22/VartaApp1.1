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

@file:Suppress("DEPRECATION")

package varta.cdac.app.core.hardware

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.core.content.getSystemService
import javax.inject.Inject

class HardwareInfo @Inject constructor(
        private val context: Context
) {
    /**
     * Tell if the device has a back (or external) camera
     */
    fun hasBackCamera(): Boolean {
        val manager = context.getSystemService<CameraManager>() ?: return Camera.getNumberOfCameras() > 0

        return manager.cameraIdList.any {
            val lensFacing = manager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING)
            lensFacing == CameraCharacteristics.LENS_FACING_BACK || lensFacing == CameraCharacteristics.LENS_FACING_EXTERNAL
        }
    }
}
