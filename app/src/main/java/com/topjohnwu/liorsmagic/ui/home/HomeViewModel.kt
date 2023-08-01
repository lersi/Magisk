package com.topjohnwu.liorsmagic.ui.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.Bindable
import com.topjohnwu.liorsmagic.BR
import com.topjohnwu.liorsmagic.BuildConfig
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.ActivityExecutor
import com.topjohnwu.liorsmagic.arch.AsyncLoadViewModel
import com.topjohnwu.liorsmagic.arch.ContextExecutor
import com.topjohnwu.liorsmagic.arch.UIActivity
import com.topjohnwu.liorsmagic.arch.ViewEvent
import com.topjohnwu.liorsmagic.core.Config
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.download.Subject
import com.topjohnwu.liorsmagic.core.download.Subject.App
import com.topjohnwu.liorsmagic.core.ktx.await
import com.topjohnwu.liorsmagic.core.ktx.toast
import com.topjohnwu.liorsmagic.core.repository.NetworkService
import com.topjohnwu.liorsmagic.databinding.bindExtra
import com.topjohnwu.liorsmagic.databinding.set
import com.topjohnwu.liorsmagic.dialog.EnvFixDialog
import com.topjohnwu.liorsmagic.dialog.ManagerInstallDialog
import com.topjohnwu.liorsmagic.dialog.UninstallDialog
import com.topjohnwu.liorsmagic.events.SnackbarEvent
import com.topjohnwu.liorsmagic.utils.asText
import com.topjohnwu.superuser.Shell
import kotlin.math.roundToInt

class HomeViewModel(
    private val svc: NetworkService
) : AsyncLoadViewModel() {

    enum class State {
        LOADING, INVALID, OUTDATED, UP_TO_DATE
    }

    val liorsmagicTitleBarrierIds =
        intArrayOf(R.id.home_liorsmagic_icon, R.id.home_liorsmagic_title, R.id.home_liorsmagic_button)
    val appTitleBarrierIds =
        intArrayOf(R.id.home_manager_icon, R.id.home_manager_title, R.id.home_manager_button)

    @get:Bindable
    var isNoticeVisible = Config.safetyNotice
        set(value) = set(value, field, { field = it }, BR.noticeVisible)

    val liorsmagicState
        get() = when {
            Info.isRooted && Info.env.isUnsupported -> State.OUTDATED
            !Info.env.isActive -> State.INVALID
            Info.env.versionCode < BuildConfig.VERSION_CODE -> State.OUTDATED
            else -> State.UP_TO_DATE
        }

    @get:Bindable
    var appState = State.LOADING
        set(value) = set(value, field, { field = it }, BR.appState)

    val liorsmagicInstalledVersion
        get() = Info.env.run {
            if (isActive)
                ("$versionString ($versionCode)" + if (isDebug) " (D)" else "").asText()
            else
                R.string.not_available.asText()
        }

    @get:Bindable
    var managerRemoteVersion = R.string.loading.asText()
        set(value) = set(value, field, { field = it }, BR.managerRemoteVersion)

    val managerInstalledVersion
        get() = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})" +
            if (BuildConfig.DEBUG) " (D)" else ""

    @get:Bindable
    var stateManagerProgress = 0
        set(value) = set(value, field, { field = it }, BR.stateManagerProgress)

    val extraBindings = bindExtra {
        it.put(BR.viewModel, this)
    }

    companion object {
        private var checkedEnv = false
    }

    override suspend fun doLoadWork() {
        appState = State.LOADING
        Info.getRemote(svc)?.apply {
            appState = when {
                BuildConfig.VERSION_CODE < liorsmagic.versionCode -> State.OUTDATED
                else -> State.UP_TO_DATE
            }

            val isDebug = Config.updateChannel == Config.Value.DEBUG_CHANNEL
            managerRemoteVersion =
                ("${liorsmagic.version} (${liorsmagic.versionCode})" +
                    if (isDebug) " (D)" else "").asText()
        } ?: run {
            appState = State.INVALID
            managerRemoteVersion = R.string.not_available.asText()
        }
        ensureEnv()
    }

    override fun onNetworkChanged(network: Boolean) = startLoading()

    fun onProgressUpdate(progress: Float, subject: Subject) {
        if (subject is App)
            stateManagerProgress = progress.times(100f).roundToInt()
    }

    fun onLinkPressed(link: String) = object : ViewEvent(), ContextExecutor {
        override fun invoke(context: Context) {
            val intent = Intent(Intent.ACTION_VIEW, link.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context.toast(R.string.open_link_failed_toast, Toast.LENGTH_SHORT)
            }
        }
    }.publish()

    fun onDeletePressed() = UninstallDialog().show()

    fun onManagerPressed() = when (appState) {
        State.LOADING -> SnackbarEvent(R.string.loading).publish()
        State.INVALID -> SnackbarEvent(R.string.no_connection).publish()
        else -> withExternalRW {
            withInstallPermission {
                ManagerInstallDialog().show()
            }
        }
    }

    fun onLiorsmagicPressed() = withExternalRW {
        HomeFragmentDirections.actionHomeFragmentToInstallFragment().navigate()
    }

    fun hideNotice() {
        Config.safetyNotice = false
        isNoticeVisible = false
    }

    private suspend fun ensureEnv() {
        if (liorsmagicState == State.INVALID || checkedEnv) return
        val cmd = "env_check ${Info.env.versionString} ${Info.env.versionCode}"
        val code = Shell.cmd(cmd).await().code
        if (code != 0) {
            EnvFixDialog(this, code).show()
        }
        checkedEnv = true
    }

    val showTest = false
    fun onTestPressed() = object : ViewEvent(), ActivityExecutor {
        override fun invoke(activity: UIActivity<*>) {
            /* Entry point to trigger test events within the app */
        }
    }.publish()
}
