package com.topjohnwu.liorsmagic.dialog

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.events.DialogBuilder
import com.topjohnwu.liorsmagic.view.LiorsmagicDialog

class SuperuserRevokeDialog(
    private val appName: String,
    private val onSuccess: () -> Unit
) : DialogBuilder {

    override fun build(dialog: LiorsmagicDialog) {
        dialog.apply {
            setTitle(R.string.su_revoke_title)
            setMessage(R.string.su_revoke_msg, appName)
            setButton(LiorsmagicDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
                onClick { onSuccess() }
            }
            setButton(LiorsmagicDialog.ButtonType.NEGATIVE) {
                text = android.R.string.cancel
            }
        }
    }
}
