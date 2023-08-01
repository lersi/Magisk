package com.topjohnwu.liorsmagic.ui.module

import android.net.Uri
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.AsyncLoadViewModel
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.base.ContentResultCallback
import com.topjohnwu.liorsmagic.core.model.module.LocalModule
import com.topjohnwu.liorsmagic.core.model.module.OnlineModule
import com.topjohnwu.liorsmagic.databinding.MergeObservableList
import com.topjohnwu.liorsmagic.databinding.RvItem
import com.topjohnwu.liorsmagic.databinding.bindExtra
import com.topjohnwu.liorsmagic.databinding.diffList
import com.topjohnwu.liorsmagic.databinding.set
import com.topjohnwu.liorsmagic.dialog.LocalModuleInstallDialog
import com.topjohnwu.liorsmagic.dialog.OnlineModuleInstallDialog
import com.topjohnwu.liorsmagic.events.GetContentEvent
import com.topjohnwu.liorsmagic.events.SnackbarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize

class ModuleViewModel : AsyncLoadViewModel() {

    val bottomBarBarrierIds = intArrayOf(R.id.module_update, R.id.module_remove)

    private val itemsInstalled = diffList<LocalModuleRvItem>()

    val items = MergeObservableList<RvItem>()
    val extraBindings = bindExtra {
        it.put(BR.viewModel, this)
    }

    val data get() = uri

    @get:Bindable
    var loading = true
        private set(value) = set(value, field, { field = it }, BR.loading)

    override suspend fun doLoadWork() {
        loading = true
        val moduleLoaded = Info.env.isActive &&
                withContext(Dispatchers.IO) { LocalModule.loaded() }
        if (moduleLoaded) {
            loadInstalled()
            if (items.isEmpty()) {
                items.insertItem(InstallModule)
                    .insertList(itemsInstalled)
            }
        }
        loading = false
        loadUpdateInfo()
    }

    override fun onNetworkChanged(network: Boolean) = startLoading()

    private suspend fun loadInstalled() {
        withContext(Dispatchers.Default) {
            val installed = LocalModule.installed().map { LocalModuleRvItem(it) }
            itemsInstalled.update(installed)
        }
    }

    private suspend fun loadUpdateInfo() {
        withContext(Dispatchers.IO) {
            itemsInstalled.forEach {
                if (it.item.fetch())
                    it.fetchedUpdateInfo()
            }
        }
    }

    fun downloadPressed(item: OnlineModule?) =
        if (item != null && Info.isConnected.value == true) {
            withExternalRW { OnlineModuleInstallDialog(item).show() }
        } else {
            SnackbarEvent(R.string.no_connection).publish()
        }

    fun installPressed() = withExternalRW {
        GetContentEvent("application/zip", UriCallback()).publish()
    }

    fun requestInstallLocalModule(uri: Uri, displayName: String) {
        LocalModuleInstallDialog(this, uri, displayName).show()
    }

    @Parcelize
    class UriCallback : ContentResultCallback {
        override fun onActivityResult(result: Uri) {
            uri.value = result
        }
    }

    companion object {
        private val uri = MutableLiveData<Uri?>()
    }
}
