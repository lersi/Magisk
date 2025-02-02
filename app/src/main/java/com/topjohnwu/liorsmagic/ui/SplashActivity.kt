package com.topjohnwu.liorsmagic.ui

import android.Manifest.permission.REQUEST_INSTALL_PACKAGES
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.topjohnwu.liorsmagic.BuildConfig.APPLICATION_ID
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.StubApk
import com.topjohnwu.liorsmagic.arch.NavigationActivity
import com.topjohnwu.liorsmagic.core.Config
import com.topjohnwu.liorsmagic.core.Const
import com.topjohnwu.liorsmagic.core.JobService
import com.topjohnwu.liorsmagic.core.di.ServiceLocator
import com.topjohnwu.liorsmagic.core.isRunningAsStub
import com.topjohnwu.liorsmagic.core.ktx.toast
import com.topjohnwu.liorsmagic.core.tasks.HideAPK
import com.topjohnwu.liorsmagic.core.utils.RootUtils
import com.topjohnwu.liorsmagic.ui.theme.Theme
import com.topjohnwu.liorsmagic.view.LiorsmagicDialog
import com.topjohnwu.liorsmagic.view.Shortcuts
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
abstract class SplashActivity<Binding : ViewDataBinding> : NavigationActivity<Binding>() {

    companion object {
        private var splashShown = false
    }

    private var needShowMainUI = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Theme.selected.themeRes)

        if (isRunningAsStub && !splashShown) {
            // Manually apply splash theme for stub
            theme.applyStyle(R.style.StubSplashTheme, true)
        }

        super.onCreate(savedInstanceState)

        if (!isRunningAsStub) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { !splashShown }
        }

        if (splashShown) {
            doShowMainUI(savedInstanceState)
        } else {
            Shell.getShell(Shell.EXECUTOR) {
                if (isRunningAsStub && !it.isRoot) {
                    showInvalidStateMessage()
                    return@getShell
                }
                preLoad(savedInstanceState)
            }
        }
    }

    private fun doShowMainUI(savedInstanceState: Bundle?) {
        needShowMainUI = false
        showMainUI(savedInstanceState)
    }

    abstract fun showMainUI(savedInstanceState: Bundle?)

    @SuppressLint("InlinedApi")
    private fun showInvalidStateMessage(): Unit = runOnUiThread {
        LiorsmagicDialog(this).apply {
            setTitle(R.string.unsupport_nonroot_stub_title)
            setMessage(R.string.unsupport_nonroot_stub_msg)
            setButton(LiorsmagicDialog.ButtonType.POSITIVE) {
                text = R.string.install
                onClick {
                    withPermission(REQUEST_INSTALL_PACKAGES) {
                        if (!it) {
                            toast(R.string.install_unknown_denied, Toast.LENGTH_SHORT)
                            showInvalidStateMessage()
                        } else {
                            lifecycleScope.launch {
                                HideAPK.restore(this@SplashActivity)
                            }
                        }
                    }
                }
            }
            setCancelable(false)
            show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (needShowMainUI) {
            doShowMainUI(null)
        }
    }

    private fun preLoad(savedState: Bundle?) {
        val prevPkg = intent.getStringExtra(Const.Key.PREV_PKG)?.let {
            // Make sure the calling package matches (prevent DoS)
            if (it == realCallingPackage)
                it
            else
                null
        }

        Config.load(prevPkg)
        handleRepackage(prevPkg)
        if (prevPkg != null) {
            runOnUiThread {
                // Relaunch the process after package migration
                StubApk.restartProcess(this)
            }
            return
        }

        JobService.schedule(this)
        Shortcuts.setupDynamic(this)

        // Pre-fetch network services
        ServiceLocator.networkService

        // Wait for root service
        RootUtils.Connection.await()

        runOnUiThread {
            splashShown = true
            if (isRunningAsStub) {
                // Re-launch main activity without splash theme
                relaunch()
            } else {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    doShowMainUI(savedState)
                } else {
                    needShowMainUI = true
                }
            }
        }
    }

    private fun handleRepackage(pkg: String?) {
        if (packageName != APPLICATION_ID) {
            runCatching {
                // Hidden, remove com.topjohnwu.liorsmagic if exist as it could be malware
                packageManager.getApplicationInfo(APPLICATION_ID, 0)
                Shell.cmd("(pm uninstall $APPLICATION_ID)& >/dev/null 2>&1").exec()
            }
        } else {
            if (Config.suManager.isNotEmpty())
                Config.suManager = ""
            pkg ?: return
            Shell.cmd("(pm uninstall $pkg)& >/dev/null 2>&1").exec()
        }
    }
}
