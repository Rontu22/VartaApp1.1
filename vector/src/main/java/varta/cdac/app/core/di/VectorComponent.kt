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

package varta.cdac.app.core.di

import android.content.Context
import android.content.res.Resources
import dagger.BindsInstance
import dagger.Component
import varta.cdac.app.ActiveSessionDataSource
import varta.cdac.app.AppStateHandler
import varta.cdac.app.EmojiCompatFontProvider
import varta.cdac.app.EmojiCompatWrapper
import varta.cdac.app.VectorApplication
import varta.cdac.app.core.dialogs.UnrecognizedCertificateDialog
import varta.cdac.app.core.error.ErrorFormatter
import varta.cdac.app.core.network.WifiDetector
import varta.cdac.app.core.pushers.PushersManager
import varta.cdac.app.core.utils.AssetReader
import varta.cdac.app.core.utils.DimensionConverter
import varta.cdac.app.features.call.webrtc.WebRtcCallManager
import varta.cdac.app.features.configuration.VectorConfiguration
import varta.cdac.app.features.crypto.keysrequest.KeyRequestHandler
import varta.cdac.app.features.crypto.verification.IncomingVerificationRequestHandler
import varta.cdac.app.features.home.AvatarRenderer
import varta.cdac.app.features.home.CurrentSpaceSuggestedRoomListDataSource
import varta.cdac.app.features.home.room.detail.RoomDetailPendingActionStore
import varta.cdac.app.features.home.room.detail.timeline.helper.MatrixItemColorProvider
import varta.cdac.app.features.home.room.detail.timeline.helper.RoomSummariesHolder
import varta.cdac.app.features.html.EventHtmlRenderer
import varta.cdac.app.features.html.VectorHtmlCompressor
import varta.cdac.app.features.login.ReAuthHelper
import varta.cdac.app.features.navigation.Navigator
import varta.cdac.app.features.notifications.NotifiableEventResolver
import varta.cdac.app.features.notifications.NotificationBroadcastReceiver
import varta.cdac.app.features.notifications.NotificationDrawerManager
import varta.cdac.app.features.notifications.NotificationUtils
import varta.cdac.app.features.notifications.PushRuleTriggerListener
import varta.cdac.app.features.pin.PinCodeStore
import varta.cdac.app.features.pin.PinLocker
import varta.cdac.app.features.popup.PopupAlertManager
import varta.cdac.app.features.rageshake.BugReporter
import varta.cdac.app.features.rageshake.VectorFileLogger
import varta.cdac.app.features.rageshake.VectorUncaughtExceptionHandler
import varta.cdac.app.features.reactions.data.EmojiDataSource
import varta.cdac.app.features.session.SessionListener
import varta.cdac.app.features.settings.VectorPreferences
import varta.cdac.app.features.ui.UiStateRepository
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.auth.AuthenticationService
import org.matrix.android.sdk.api.auth.HomeServerHistoryService
import org.matrix.android.sdk.api.raw.RawService
import org.matrix.android.sdk.api.session.Session
import javax.inject.Singleton

@Component(modules = [VectorModule::class])
@Singleton
interface VectorComponent {

    fun inject(notificationBroadcastReceiver: NotificationBroadcastReceiver)

    fun inject(vectorApplication: VectorApplication)

    fun matrix(): Matrix

    fun matrixItemColorProvider(): MatrixItemColorProvider

    fun sessionListener(): SessionListener

    fun currentSession(): Session

    fun notificationUtils(): NotificationUtils

    fun notificationDrawerManager(): NotificationDrawerManager

    fun appContext(): Context

    fun resources(): Resources

    fun assetReader(): AssetReader

    fun dimensionConverter(): DimensionConverter

    fun vectorConfiguration(): VectorConfiguration

    fun avatarRenderer(): AvatarRenderer

    fun activeSessionHolder(): ActiveSessionHolder

    fun unrecognizedCertificateDialog(): UnrecognizedCertificateDialog

    fun emojiCompatFontProvider(): EmojiCompatFontProvider

    fun emojiCompatWrapper(): EmojiCompatWrapper

    fun eventHtmlRenderer(): EventHtmlRenderer

    fun vectorHtmlCompressor(): VectorHtmlCompressor

    fun navigator(): Navigator

    fun errorFormatter(): ErrorFormatter

    fun appStateHandler(): AppStateHandler

    fun currentSpaceSuggestedRoomListDataSource(): CurrentSpaceSuggestedRoomListDataSource

    fun roomDetailPendingActionStore(): RoomDetailPendingActionStore

    fun activeSessionObservableStore(): ActiveSessionDataSource

    fun incomingVerificationRequestHandler(): IncomingVerificationRequestHandler

    fun incomingKeyRequestHandler(): KeyRequestHandler

    fun authenticationService(): AuthenticationService

    fun rawService(): RawService

    fun homeServerHistoryService(): HomeServerHistoryService

    fun bugReporter(): BugReporter

    fun vectorUncaughtExceptionHandler(): VectorUncaughtExceptionHandler

    fun pushRuleTriggerListener(): PushRuleTriggerListener

    fun pusherManager(): PushersManager

    fun notifiableEventResolver(): NotifiableEventResolver

    fun vectorPreferences(): VectorPreferences

    fun wifiDetector(): WifiDetector

    fun vectorFileLogger(): VectorFileLogger

    fun uiStateRepository(): UiStateRepository

    fun pinCodeStore(): PinCodeStore

    fun emojiDataSource(): EmojiDataSource

    fun alertManager(): PopupAlertManager

    fun reAuthHelper(): ReAuthHelper

    fun pinLocker(): PinLocker

    fun webRtcCallManager(): WebRtcCallManager

    fun roomSummaryHolder(): RoomSummariesHolder

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): VectorComponent
    }
}
