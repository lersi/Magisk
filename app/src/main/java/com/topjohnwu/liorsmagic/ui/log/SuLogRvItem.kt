package com.topjohnwu.liorsmagic.ui.log

import androidx.databinding.Bindable
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.ktx.timeDateFormat
import com.topjohnwu.liorsmagic.core.ktx.toTime
import com.topjohnwu.liorsmagic.core.model.su.SuLog
import com.topjohnwu.liorsmagic.databinding.DiffItem
import com.topjohnwu.liorsmagic.databinding.ObservableRvItem
import com.topjohnwu.liorsmagic.databinding.set

class SuLogRvItem(val log: SuLog) : ObservableRvItem(), DiffItem<SuLogRvItem> {

    override val layoutRes = R.layout.item_log_access_md2

    val date = log.time.toTime(timeDateFormat)

    @get:Bindable
    var isTop = false
        set(value) = set(value, field, { field = it }, BR.top)

    @get:Bindable
    var isBottom = false
        set(value) = set(value, field, { field = it }, BR.bottom)

    override fun itemSameAs(other: SuLogRvItem) = log.appName == other.log.appName
}
