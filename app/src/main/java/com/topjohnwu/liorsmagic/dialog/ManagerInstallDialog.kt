package com.topjohnwu.liorsmagic.dialog

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.di.AppContext
import com.topjohnwu.liorsmagic.core.di.ServiceLocator
import com.topjohnwu.liorsmagic.core.download.DownloadService
import com.topjohnwu.liorsmagic.core.download.Subject
import com.topjohnwu.liorsmagic.view.MagiskDialog
import java.io.File

class ManagerInstallDialog : MarkDownDialog() {

    private val svc get() = ServiceLocator.networkService

    override suspend fun getMarkdownText(): String {
        val text = svc.fetchString(Info.remote.liorsmagic.note)
        // Cache the changelog
        AppContext.cacheDir.listFiles { _, name -> name.endsWith(".md") }.orEmpty().forEach {
            it.delete()
        }
        File(AppContext.cacheDir, "${Info.remote.liorsmagic.versionCode}.md").writeText(text)
        return text
    }

    override fun build(dialog: MagiskDialog) {
        super.build(dialog)
        dialog.apply {
            setCancelable(true)
            setButton(MagiskDialog.ButtonType.POSITIVE) {
                text = R.string.install
                onClick { DownloadService.start(activity, Subject.App()) }
            }
            setButton(MagiskDialog.ButtonType.NEGATIVE) {
                text = android.R.string.cancel
            }
        }
    }

}
