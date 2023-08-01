package com.topjohnwu.liorsmagic.dialog

import android.net.Uri
import com.topjohnwu.liorsmagic.MainDirections
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.Const
import com.topjohnwu.liorsmagic.events.DialogBuilder
import com.topjohnwu.liorsmagic.ui.module.ModuleViewModel
import com.topjohnwu.liorsmagic.view.LiorsmagicDialog

class LocalModuleInstallDialog(
    private val viewModel: ModuleViewModel,
    private val uri: Uri,
    private val displayName: String
) : DialogBuilder {
    override fun build(dialog: LiorsmagicDialog) {
        dialog.apply {
            setTitle(R.string.confirm_install_title)
            setMessage(context.getString(R.string.confirm_install, displayName))
            setButton(LiorsmagicDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
                onClick {
                    viewModel.apply {
                        MainDirections.actionFlashFragment(Const.Value.FLASH_ZIP, uri).navigate()
                    }
                }
            }
            setButton(LiorsmagicDialog.ButtonType.NEGATIVE) {
                text = android.R.string.cancel
            }
        }
    }
}
