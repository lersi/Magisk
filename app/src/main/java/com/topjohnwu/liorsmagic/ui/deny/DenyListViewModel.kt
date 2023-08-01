package com.topjohnwu.liorsmagic.ui.deny

import android.annotation.SuppressLint
import android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.arch.AsyncLoadViewModel
import com.topjohnwu.liorsmagic.core.di.AppContext
import com.topjohnwu.liorsmagic.core.ktx.concurrentMap
import com.topjohnwu.liorsmagic.databinding.bindExtra
import com.topjohnwu.liorsmagic.databinding.filterList
import com.topjohnwu.liorsmagic.databinding.set
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.withContext

class DenyListViewModel : AsyncLoadViewModel() {

    var isShowSystem = false
        set(value) {
            field = value
            doQuery(query)
        }

    var isShowOS = false
        set(value) {
            field = value
            doQuery(query)
        }

    var query = ""
        set(value) {
            field = value
            doQuery(value)
        }

    val items = filterList<DenyListRvItem>(viewModelScope)
    val extraBindings = bindExtra {
        it.put(BR.viewModel, this)
    }

    @get:Bindable
    var loading = true
        private set(value) = set(value, field, { field = it }, BR.loading)

    @SuppressLint("InlinedApi")
    override suspend fun doLoadWork() {
        loading = true
        val apps = withContext(Dispatchers.Default) {
            val pm = AppContext.packageManager
            val denyList = Shell.cmd("liorsmagic --denylist ls").exec().out
                .map { CmdlineListItem(it) }
            val apps = pm.getInstalledApplications(MATCH_UNINSTALLED_PACKAGES).run {
                asFlow()
                    .filter { AppContext.packageName != it.packageName }
                    .concurrentMap { AppProcessInfo(it, pm, denyList) }
                    .filter { it.processes.isNotEmpty() }
                    .concurrentMap { DenyListRvItem(it) }
                    .toCollection(ArrayList(size))
            }
            apps.sort()
            apps
        }
        items.set(apps)
        doQuery(query)
    }

    private fun doQuery(s: String) {
        items.filter {
            fun filterSystem() = isShowSystem || !it.info.isSystemApp()

            fun filterOS() = (isShowSystem && isShowOS) || it.info.isApp()

            fun filterQuery(): Boolean {
                fun inName() = it.info.label.contains(s, true)
                fun inPackage() = it.info.packageName.contains(s, true)
                fun inProcesses() = it.processes.any { p -> p.process.name.contains(s, true) }
                return inName() || inPackage() || inProcesses()
            }

            (it.isChecked || (filterSystem() && filterOS())) && filterQuery()
        }
        loading = false
    }
}
