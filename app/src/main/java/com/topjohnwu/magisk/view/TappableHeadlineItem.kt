package com.topjohnwu.liorsmagic.view

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.databinding.DiffItem
import com.topjohnwu.liorsmagic.databinding.RvItem

sealed class TappableHeadlineItem : RvItem(), DiffItem<TappableHeadlineItem> {

    abstract val title: Int
    abstract val icon: Int

    override val layoutRes = R.layout.item_tappable_headline

    // --- listener

    interface Listener {

        fun onItemPressed(item: TappableHeadlineItem)

    }

    // --- objects

    object ThemeMode : TappableHeadlineItem() {
        override val title = R.string.settings_dark_mode_title
        override val icon = R.drawable.ic_day_night
    }

}
