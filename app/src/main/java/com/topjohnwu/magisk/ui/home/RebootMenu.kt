package com.topjohnwu.liorsmagic.ui.home

import android.os.Build
import android.os.PowerManager
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.content.getSystemService
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.base.BaseActivity
import com.topjohnwu.liorsmagic.core.ktx.reboot as systemReboot

object RebootMenu {

    private fun reboot(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reboot_normal -> systemReboot()
            R.id.action_reboot_userspace -> systemReboot("userspace")
            R.id.action_reboot_bootloader -> systemReboot("bootloader")
            R.id.action_reboot_download -> systemReboot("download")
            R.id.action_reboot_edl -> systemReboot("edl")
            R.id.action_reboot_recovery -> systemReboot("recovery")
            else -> Unit
        }
        return true
    }

    fun inflate(activity: BaseActivity): PopupMenu {
        val themeWrapper = ContextThemeWrapper(activity, R.style.Foundation_PopupMenu)
        val menu = PopupMenu(themeWrapper, activity.findViewById(R.id.action_reboot))
        activity.menuInflater.inflate(R.menu.menu_reboot, menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            activity.getSystemService<PowerManager>()?.isRebootingUserspaceSupported == true)
            menu.menu.findItem(R.id.action_reboot_userspace).isVisible = true
        menu.setOnMenuItemClickListener(RebootMenu::reboot)
        return menu
    }

}
