package com.topjohnwu.liorsmagic.ui.install

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Spanned
import android.text.SpannedString
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.BuildConfig
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.BaseViewModel
import com.topjohnwu.liorsmagic.core.Config
import com.topjohnwu.liorsmagic.core.Const
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.base.ContentResultCallback
import com.topjohnwu.liorsmagic.core.di.AppContext
import com.topjohnwu.liorsmagic.core.ktx.toast
import com.topjohnwu.liorsmagic.core.repository.NetworkService
import com.topjohnwu.liorsmagic.databinding.set
import com.topjohnwu.liorsmagic.dialog.SecondSlotWarningDialog
import com.topjohnwu.liorsmagic.events.GetContentEvent
import com.topjohnwu.liorsmagic.ui.flash.FlashFragment
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.io.File
import java.io.IOException

class InstallViewModel(svc: NetworkService, markwon: Markwon) : BaseViewModel() {

    val isRooted get() = Info.isRooted
    val hideVbmeta = Info.vbmeta || Info.isSamsung || Info.isAB
    val skipOptions = Info.isEmulator || (Info.isSAR && !Info.isFDE && hideVbmeta && Info.ramdisk)
    val noSecondSlot = !isRooted || !Info.isAB || Info.isEmulator

    @get:Bindable
    var step = if (skipOptions) 1 else 0
        set(value) = set(value, field, { field = it }, BR.step)

    private var methodId = -1

    @get:Bindable
    var method
        get() = methodId
        set(value) = set(value, methodId, { methodId = it }, BR.method) {
            when (it) {
                R.id.method_patch -> {
                    GetContentEvent("*/*", UriCallback()).publish()
                }
                R.id.method_inactive_slot -> {
                    SecondSlotWarningDialog().show()
                }
            }
        }

    val data: LiveData<Uri?> get() = uri

    @get:Bindable
    var notes: Spanned = SpannedString("")
        set(value) = set(value, field, { field = it }, BR.notes)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File(AppContext.cacheDir, "${BuildConfig.VERSION_CODE}.md")
                val text = when {
                    file.exists() -> file.readText()
                    Const.Url.CHANGELOG_URL.isEmpty() -> ""
                    else -> {
                        val str = svc.fetchString(Const.Url.CHANGELOG_URL)
                        file.writeText(str)
                        str
                    }
                }
                val spanned = markwon.toMarkdown(text)
                withContext(Dispatchers.Main) {
                    notes = spanned
                }
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    fun install() {
        when (method) {
            R.id.method_patch -> FlashFragment.patch(data.value!!).navigate(true)
            R.id.method_direct -> FlashFragment.flash(false).navigate(true)
            R.id.method_inactive_slot -> FlashFragment.flash(true).navigate(true)
            else -> error("Unknown value")
        }
    }

    override fun onSaveState(state: Bundle) {
        state.putParcelable(INSTALL_STATE_KEY, InstallState(
            methodId,
            step,
            Config.keepVerity,
            Config.keepEnc,
            Config.patchVbmeta,
            Config.recovery
        ))
    }

    override fun onRestoreState(state: Bundle) {
        state.getParcelable<InstallState>(INSTALL_STATE_KEY)?.let {
            methodId = it.method
            step = it.step
            Config.keepVerity = it.keepVerity
            Config.keepEnc = it.keepEnc
            Config.patchVbmeta = it.patchVbmeta
            Config.recovery = it.recovery
        }
    }

    @Parcelize
    class UriCallback : ContentResultCallback {
        override fun onActivityLaunch() {
            AppContext.toast(R.string.patch_file_msg, Toast.LENGTH_LONG)
        }
        override fun onActivityResult(result: Uri) {
            uri.value = result
        }
    }

    @Parcelize
    class InstallState(
        val method: Int,
        val step: Int,
        val keepVerity: Boolean,
        val keepEnc: Boolean,
        val patchVbmeta: Boolean,
        val recovery: Boolean,
    ) : Parcelable

    companion object {
        private const val INSTALL_STATE_KEY = "install_state"
        private val uri = MutableLiveData<Uri?>()
    }
}
