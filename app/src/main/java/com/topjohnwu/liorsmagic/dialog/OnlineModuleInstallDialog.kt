package com.topjohnwu.liorsmagic.dialog

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.di.ServiceLocator
import com.topjohnwu.liorsmagic.core.download.Action
import com.topjohnwu.liorsmagic.core.download.DownloadService
import com.topjohnwu.liorsmagic.core.download.Subject
import com.topjohnwu.liorsmagic.core.model.module.OnlineModule
import com.topjohnwu.liorsmagic.view.LiorsmagicDialog

class OnlineModuleInstallDialog(private val item: OnlineModule) : MarkDownDialog() {

    private val svc get() = ServiceLocator.networkService

    override suspend fun getMarkdownText(): String {
        val str = svc.fetchString(item.changelog)
        return if (str.length > 1000) str.substring(0, 1000) else str
    }

    override fun build(dialog: LiorsmagicDialog) {
        super.build(dialog)
        dialog.apply {

            fun download(install: Boolean) {
                val action = if (install) Action.Flash else Action.Download
                val subject = Subject.Module(item, action)
                DownloadService.start(activity, subject)
            }

            val title = context.getString(R.string.repo_install_title,
                item.name, item.version, item.versionCode)

            setTitle(title)
            setCancelable(true)
            setButton(LiorsmagicDialog.ButtonType.NEGATIVE) {
                text = R.string.download
                onClick { download(false) }
            }
            setButton(LiorsmagicDialog.ButtonType.POSITIVE) {
                text = R.string.install
                onClick { download(true) }
            }
            setButton(LiorsmagicDialog.ButtonType.NEUTRAL) {
                text = android.R.string.cancel
            }
        }
    }

}
