package com.topjohnwu.liorsmagic.ui.theme

import com.topjohnwu.liorsmagic.arch.BaseViewModel
import com.topjohnwu.liorsmagic.core.Config
import com.topjohnwu.liorsmagic.dialog.DarkThemeDialog
import com.topjohnwu.liorsmagic.events.RecreateEvent
import com.topjohnwu.liorsmagic.view.TappableHeadlineItem

class ThemeViewModel : BaseViewModel(), TappableHeadlineItem.Listener {

    val themeHeadline = TappableHeadlineItem.ThemeMode

    override fun onItemPressed(item: TappableHeadlineItem) = when (item) {
        is TappableHeadlineItem.ThemeMode -> DarkThemeDialog().show()
    }

    fun saveTheme(theme: Theme) {
        if (!theme.isSelected) {
            Config.themeOrdinal = theme.ordinal
            RecreateEvent().publish()
        }
    }
}
