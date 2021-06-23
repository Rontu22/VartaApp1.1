/*
 * Copyright 2018 New Vector Ltd
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
package varta.cdac.app.push.fcm

import androidx.fragment.app.Fragment
import varta.cdac.app.fdroid.features.settings.troubleshoot.TestAutoStartBoot
import varta.cdac.app.fdroid.features.settings.troubleshoot.TestBackgroundRestrictions
import varta.cdac.app.fdroid.features.settings.troubleshoot.TestBatteryOptimization
import varta.cdac.app.features.settings.troubleshoot.NotificationTroubleshootTestManager
import varta.cdac.app.features.settings.troubleshoot.TestAccountSettings
import varta.cdac.app.features.settings.troubleshoot.TestDeviceSettings
import varta.cdac.app.features.settings.troubleshoot.TestNotification
import varta.cdac.app.features.settings.troubleshoot.TestPushRulesSettings
import varta.cdac.app.features.settings.troubleshoot.TestSystemSettings
import javax.inject.Inject

class NotificationTroubleshootTestManagerFactory @Inject constructor(
        private val testSystemSettings: TestSystemSettings,
        private val testAccountSettings: TestAccountSettings,
        private val testDeviceSettings: TestDeviceSettings,
        private val testPushRulesSettings: TestPushRulesSettings,
        private val testAutoStartBoot: TestAutoStartBoot,
        private val testBackgroundRestrictions: TestBackgroundRestrictions,
        private val testBatteryOptimization: TestBatteryOptimization,
        private val testNotification: TestNotification
) {

    fun create(fragment: Fragment): NotificationTroubleshootTestManager {
        val mgr = NotificationTroubleshootTestManager(fragment)
        mgr.addTest(testSystemSettings)
        mgr.addTest(testAccountSettings)
        mgr.addTest(testDeviceSettings)
        mgr.addTest(testPushRulesSettings)
        mgr.addTest(testAutoStartBoot)
        mgr.addTest(testBackgroundRestrictions)
        mgr.addTest(testBatteryOptimization)
        mgr.addTest(testNotification)
        return mgr
    }
}
