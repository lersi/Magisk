package com.topjohnwu.liorsmagic.dialog

import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.lifecycle.lifecycleScope
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.di.ServiceLocator
import com.topjohnwu.liorsmagic.events.DialogBuilder
import com.topjohnwu.liorsmagic.view.MagiskDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

abstract class MarkDownDialog : DialogBuilder {

    abstract suspend fun getMarkdownText(): String

    @CallSuper
    override fun build(dialog: MagiskDialog) {
        with(dialog) {
            val view = LayoutInflater.from(context).inflate(R.layout.markdown_window_md2, null)
            setView(view)
            val tv = view.findViewById<TextView>(R.id.md_txt)
            activity.lifecycleScope.launch {
                try {
                    val text = withContext(Dispatchers.IO) { getMarkdownText() }
                    ServiceLocator.markwon.setMarkdown(tv, text)
                } catch (e: IOException) {
                    Timber.e(e)
                    tv.setText(R.string.download_file_error)
                }
            }
        }
    }
}
