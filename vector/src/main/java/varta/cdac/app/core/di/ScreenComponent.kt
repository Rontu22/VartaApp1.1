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

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import varta.cdac.app.core.dialogs.UnrecognizedCertificateDialog
import varta.cdac.app.core.error.ErrorFormatter
import varta.cdac.app.core.preference.UserAvatarPreference
import varta.cdac.app.features.MainActivity
import varta.cdac.app.features.auth.ReAuthActivity
import varta.cdac.app.features.call.CallControlsBottomSheet
import varta.cdac.app.features.call.VectorCallActivity
import varta.cdac.app.features.call.conference.VectorJitsiActivity
import varta.cdac.app.features.call.transfer.CallTransferActivity
import varta.cdac.app.features.createdirect.CreateDirectRoomActivity
import varta.cdac.app.features.crypto.keysbackup.settings.KeysBackupManageActivity
import varta.cdac.app.features.crypto.keysbackup.setup.KeysBackupSetupActivity
import varta.cdac.app.features.crypto.quads.SharedSecureStorageActivity
import varta.cdac.app.features.crypto.recover.BootstrapBottomSheet
import varta.cdac.app.features.crypto.verification.VerificationBottomSheet
import varta.cdac.app.features.debug.DebugMenuActivity
import varta.cdac.app.features.devtools.RoomDevToolActivity
import varta.cdac.app.features.home.HomeActivity
import varta.cdac.app.features.home.HomeModule
import varta.cdac.app.features.home.room.detail.RoomDetailActivity
import varta.cdac.app.features.home.room.detail.readreceipts.DisplayReadReceiptsBottomSheet
import varta.cdac.app.features.home.room.detail.search.SearchActivity
import varta.cdac.app.features.home.room.detail.timeline.action.MessageActionsBottomSheet
import varta.cdac.app.features.home.room.detail.timeline.edithistory.ViewEditHistoryBottomSheet
import varta.cdac.app.features.home.room.detail.timeline.reactions.ViewReactionsBottomSheet
import varta.cdac.app.features.home.room.detail.widget.RoomWidgetsBottomSheet
import varta.cdac.app.features.home.room.filtered.FilteredRoomsActivity
import varta.cdac.app.features.home.room.list.RoomListModule
import varta.cdac.app.features.home.room.list.actions.RoomListQuickActionsBottomSheet
import varta.cdac.app.features.invite.InviteUsersToRoomActivity
import varta.cdac.app.features.invite.VectorInviteView
import varta.cdac.app.features.link.LinkHandlerActivity
import varta.cdac.app.features.login.LoginActivity
import varta.cdac.app.features.login2.LoginActivity2
import varta.cdac.app.features.matrixto.MatrixToBottomSheet
import varta.cdac.app.features.media.BigImageViewerActivity
import varta.cdac.app.features.media.VectorAttachmentViewerActivity
import varta.cdac.app.features.navigation.Navigator
import varta.cdac.app.features.permalink.PermalinkHandlerActivity
import varta.cdac.app.features.pin.PinLocker
import varta.cdac.app.features.qrcode.QrCodeScannerActivity
import varta.cdac.app.features.rageshake.BugReportActivity
import varta.cdac.app.features.rageshake.BugReporter
import varta.cdac.app.features.rageshake.RageShake
import varta.cdac.app.features.reactions.EmojiReactionPickerActivity
import varta.cdac.app.features.reactions.widget.ReactionButton
import varta.cdac.app.features.roomdirectory.RoomDirectoryActivity
import varta.cdac.app.features.roomdirectory.createroom.CreateRoomActivity
import varta.cdac.app.features.roommemberprofile.RoomMemberProfileActivity
import varta.cdac.app.features.roommemberprofile.devices.DeviceListBottomSheet
import varta.cdac.app.features.roomprofile.RoomProfileActivity
import varta.cdac.app.features.roomprofile.alias.detail.RoomAliasBottomSheet
import varta.cdac.app.features.roomprofile.settings.historyvisibility.RoomHistoryVisibilityBottomSheet
import varta.cdac.app.features.roomprofile.settings.joinrule.RoomJoinRuleBottomSheet
import varta.cdac.app.features.settings.VectorSettingsActivity
import varta.cdac.app.features.settings.devices.DeviceVerificationInfoBottomSheet
import varta.cdac.app.features.share.IncomingShareActivity
import varta.cdac.app.features.signout.soft.SoftLogoutActivity
import varta.cdac.app.features.spaces.InviteRoomSpaceChooserBottomSheet
import varta.cdac.app.features.spaces.SpaceCreationActivity
import varta.cdac.app.features.spaces.SpaceExploreActivity
import varta.cdac.app.features.spaces.SpaceSettingsMenuBottomSheet
import varta.cdac.app.features.spaces.invite.SpaceInviteBottomSheet
import varta.cdac.app.features.spaces.manage.SpaceManageActivity
import varta.cdac.app.features.spaces.share.ShareSpaceBottomSheet
import varta.cdac.app.features.terms.ReviewTermsActivity
import varta.cdac.app.features.ui.UiStateRepository
import varta.cdac.app.features.usercode.UserCodeActivity
import varta.cdac.app.features.widgets.WidgetActivity
import varta.cdac.app.features.widgets.permissions.RoomWidgetPermissionBottomSheet
import varta.cdac.app.features.workers.signout.SignOutBottomSheetDialogFragment

@Component(
        dependencies = [
            VectorComponent::class
        ],
        modules = [
            ViewModelModule::class,
            FragmentModule::class,
            HomeModule::class,
            RoomListModule::class,
            ScreenModule::class
        ]
)
@ScreenScope
interface ScreenComponent {

    /* ==========================================================================================
     * Shortcut to VectorComponent elements
     * ========================================================================================== */

    fun activeSessionHolder(): ActiveSessionHolder
    fun fragmentFactory(): FragmentFactory
    fun viewModelFactory(): ViewModelProvider.Factory
    fun bugReporter(): BugReporter
    fun rageShake(): RageShake
    fun navigator(): Navigator
    fun pinLocker(): PinLocker
    fun errorFormatter(): ErrorFormatter
    fun uiStateRepository(): UiStateRepository
    fun unrecognizedCertificateDialog(): UnrecognizedCertificateDialog

    /* ==========================================================================================
     * Activities
     * ========================================================================================== */

    fun inject(activity: HomeActivity)
    fun inject(activity: RoomDetailActivity)
    fun inject(activity: RoomProfileActivity)
    fun inject(activity: RoomMemberProfileActivity)
    fun inject(activity: VectorSettingsActivity)
    fun inject(activity: KeysBackupManageActivity)
    fun inject(activity: EmojiReactionPickerActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: LoginActivity2)
    fun inject(activity: LinkHandlerActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: RoomDirectoryActivity)
    fun inject(activity: KeysBackupSetupActivity)
    fun inject(activity: BugReportActivity)
    fun inject(activity: FilteredRoomsActivity)
    fun inject(activity: CreateRoomActivity)
    fun inject(activity: CreateDirectRoomActivity)
    fun inject(activity: IncomingShareActivity)
    fun inject(activity: SoftLogoutActivity)
    fun inject(activity: PermalinkHandlerActivity)
    fun inject(activity: QrCodeScannerActivity)
    fun inject(activity: DebugMenuActivity)
    fun inject(activity: SharedSecureStorageActivity)
    fun inject(activity: BigImageViewerActivity)
    fun inject(activity: InviteUsersToRoomActivity)
    fun inject(activity: ReviewTermsActivity)
    fun inject(activity: WidgetActivity)
    fun inject(activity: VectorCallActivity)
    fun inject(activity: VectorAttachmentViewerActivity)
    fun inject(activity: VectorJitsiActivity)
    fun inject(activity: SearchActivity)
    fun inject(activity: UserCodeActivity)
    fun inject(activity: CallTransferActivity)
    fun inject(activity: ReAuthActivity)
    fun inject(activity: RoomDevToolActivity)
    fun inject(activity: SpaceCreationActivity)
    fun inject(activity: SpaceExploreActivity)
    fun inject(activity: SpaceManageActivity)

    /* ==========================================================================================
     * BottomSheets
     * ========================================================================================== */

    fun inject(bottomSheet: MessageActionsBottomSheet)
    fun inject(bottomSheet: ViewReactionsBottomSheet)
    fun inject(bottomSheet: ViewEditHistoryBottomSheet)
    fun inject(bottomSheet: DisplayReadReceiptsBottomSheet)
    fun inject(bottomSheet: RoomListQuickActionsBottomSheet)
    fun inject(bottomSheet: RoomAliasBottomSheet)
    fun inject(bottomSheet: RoomHistoryVisibilityBottomSheet)
    fun inject(bottomSheet: RoomJoinRuleBottomSheet)
    fun inject(bottomSheet: VerificationBottomSheet)
    fun inject(bottomSheet: DeviceVerificationInfoBottomSheet)
    fun inject(bottomSheet: DeviceListBottomSheet)
    fun inject(bottomSheet: BootstrapBottomSheet)
    fun inject(bottomSheet: RoomWidgetPermissionBottomSheet)
    fun inject(bottomSheet: RoomWidgetsBottomSheet)
    fun inject(bottomSheet: CallControlsBottomSheet)
    fun inject(bottomSheet: SignOutBottomSheetDialogFragment)
    fun inject(bottomSheet: MatrixToBottomSheet)
    fun inject(bottomSheet: ShareSpaceBottomSheet)
    fun inject(bottomSheet: SpaceSettingsMenuBottomSheet)
    fun inject(bottomSheet: InviteRoomSpaceChooserBottomSheet)
    fun inject(bottomSheet: SpaceInviteBottomSheet)

    /* ==========================================================================================
     * Others
     * ========================================================================================== */

    fun inject(view: VectorInviteView)
    fun inject(preference: UserAvatarPreference)
    fun inject(button: ReactionButton)

    /* ==========================================================================================
     * Factory
     * ========================================================================================== */

    @Component.Factory
    interface Factory {
        fun create(vectorComponent: VectorComponent,
                   @BindsInstance context: AppCompatActivity
        ): ScreenComponent
    }
}
