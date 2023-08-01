package com.topjohnwu.liorsmagic.ui.module

import androidx.databinding.Bindable
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.model.module.LocalModule
import com.topjohnwu.liorsmagic.databinding.DiffItem
import com.topjohnwu.liorsmagic.databinding.ItemWrapper
import com.topjohnwu.liorsmagic.databinding.ObservableRvItem
import com.topjohnwu.liorsmagic.databinding.RvItem
import com.topjohnwu.liorsmagic.databinding.set
import com.topjohnwu.liorsmagic.utils.TextHolder
import com.topjohnwu.liorsmagic.utils.asText

object InstallModule : RvItem(), DiffItem<InstallModule> {
    override val layoutRes = R.layout.item_module_download
}

class LocalModuleRvItem(
    override val item: LocalModule
) : ObservableRvItem(), DiffItem<LocalModuleRvItem>, ItemWrapper<LocalModule> {

    override val layoutRes = R.layout.item_module_md2

    val showNotice: Boolean
    val noticeText: TextHolder

    init {
        val isZygisk = item.isZygisk
        val isRiru = item.isRiru
        val zygiskUnloaded = isZygisk && item.zygiskUnloaded

        showNotice = zygiskUnloaded ||
            (Info.isZygiskEnabled && isRiru) ||
            (!Info.isZygiskEnabled && isZygisk)
        noticeText =
            when {
                zygiskUnloaded -> R.string.zygisk_module_unloaded.asText()
                isRiru -> R.string.suspend_text_riru.asText(R.string.zygisk.asText())
                else -> R.string.suspend_text_zygisk.asText(R.string.zygisk.asText())
            }
    }

    @get:Bindable
    var isEnabled = item.enable
        set(value) = set(value, field, { field = it }, BR.enabled, BR.updateReady) {
            item.enable = value
        }

    @get:Bindable
    var isRemoved = item.remove
        set(value) = set(value, field, { field = it }, BR.removed, BR.updateReady) {
            item.remove = value
        }

    @get:Bindable
    val showUpdate get() = item.updateInfo != null

    @get:Bindable
    val updateReady get() = item.outdated && !isRemoved && isEnabled

    val isUpdated = item.updated

    fun fetchedUpdateInfo() {
        notifyPropertyChanged(BR.showUpdate)
        notifyPropertyChanged(BR.updateReady)
    }

    fun delete() {
        isRemoved = !isRemoved
    }

    override fun itemSameAs(other: LocalModuleRvItem): Boolean = item.id == other.item.id
}
