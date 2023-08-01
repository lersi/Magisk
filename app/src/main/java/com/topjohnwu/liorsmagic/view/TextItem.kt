package com.topjohnwu.liorsmagic.view

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.databinding.DiffItem
import com.topjohnwu.liorsmagic.databinding.ItemWrapper
import com.topjohnwu.liorsmagic.databinding.RvItem

class TextItem(override val item: Int) : RvItem(), DiffItem<TextItem>, ItemWrapper<Int> {
    override val layoutRes = R.layout.item_text
}
