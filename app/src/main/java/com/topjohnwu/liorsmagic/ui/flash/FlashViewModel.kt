package com.topjohnwu.liorsmagic.ui.flash

import android.view.MenuItem
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.BaseViewModel
import com.topjohnwu.liorsmagic.core.Const
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.ktx.reboot
import com.topjohnwu.liorsmagic.core.ktx.synchronized
import com.topjohnwu.liorsmagic.core.ktx.timeFormatStandard
import com.topjohnwu.liorsmagic.core.ktx.toTime
import com.topjohnwu.liorsmagic.core.tasks.FlashZip
import com.topjohnwu.liorsmagic.core.tasks.LiorsmagicInstaller
import com.topjohnwu.liorsmagic.core.utils.MediaStoreUtils
import com.topjohnwu.liorsmagic.core.utils.MediaStoreUtils.outputStream
import com.topjohnwu.liorsmagic.databinding.set
import com.topjohnwu.liorsmagic.events.SnackbarEvent
import com.topjohnwu.superuser.CallbackList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlashViewModel : BaseViewModel() {

    enum class State {
        FLASHING, SUCCESS, FAILED
    }

    private val _state = MutableLiveData(State.FLASHING)
    val state: LiveData<State> get() = _state
    val flashing = state.map { it == State.FLASHING }

    @get:Bindable
    var showReboot = Info.isRooted
        set(value) = set(value, field, { field = it }, BR.showReboot)

    val items = ObservableArrayList<ConsoleItem>()
    lateinit var args: FlashFragmentArgs

    private val logItems = mutableListOf<String>().synchronized()
    private val outItems = object : CallbackList<String>() {
        override fun onAddElement(e: String?) {
            e ?: return
            items.add(ConsoleItem(e))
            logItems.add(e)
        }
    }

    fun startFlashing() {
        val (action, uri) = args

        viewModelScope.launch {
            val result = when (action) {
                Const.Value.FLASH_ZIP -> {
                    uri ?: return@launch
                    FlashZip(uri, outItems, logItems).exec()
                }
                Const.Value.UNINSTALL -> {
                    showReboot = false
                    LiorsmagicInstaller.Uninstall(outItems, logItems).exec()
                }
                Const.Value.FLASH_LIORSMAGIC -> {
                    if (Info.isEmulator)
                        LiorsmagicInstaller.Emulator(outItems, logItems).exec()
                    else
                        LiorsmagicInstaller.Direct(outItems, logItems).exec()
                }
                Const.Value.FLASH_INACTIVE_SLOT -> {
                    LiorsmagicInstaller.SecondSlot(outItems, logItems).exec()
                }
                Const.Value.PATCH_FILE -> {
                    uri ?: return@launch
                    showReboot = false
                    LiorsmagicInstaller.Patch(uri, outItems, logItems).exec()
                }
                else -> {
                    back()
                    return@launch
                }
            }
            onResult(result)
        }
    }

    private fun onResult(success: Boolean) {
        _state.value = if (success) State.SUCCESS else State.FAILED
    }

    fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> savePressed()
        }
        return true
    }

    private fun savePressed() = withExternalRW {
        viewModelScope.launch(Dispatchers.IO) {
            val name = "liorsmagic_install_log_%s.log".format(
                System.currentTimeMillis().toTime(timeFormatStandard)
            )
            val file = MediaStoreUtils.getFile(name, true)
            file.uri.outputStream().bufferedWriter().use { writer ->
                synchronized(logItems) {
                    logItems.forEach {
                        writer.write(it)
                        writer.newLine()
                    }
                }
            }
            SnackbarEvent(file.toString()).publish()
        }
    }

    fun restartPressed() = reboot()
}
