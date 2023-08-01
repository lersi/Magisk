package com.topjohnwu.liorsmagic.dialog

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.events.DialogBuilder
import com.topjohnwu.liorsmagic.view.LiorsmagicDialog

class SecondSlotWarningDialog : DialogBuilder {

    override fun build(dialog: LiorsmagicDialog) {
        dialog.apply {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(R.string.install_inactive_slot_msg)
            setButton(LiorsmagicDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
            }
            setCancelable(true)
        }
    }
}
