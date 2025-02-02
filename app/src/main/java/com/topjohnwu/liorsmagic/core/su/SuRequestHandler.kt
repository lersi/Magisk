package com.topjohnwu.liorsmagic.core.su

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.topjohnwu.liorsmagic.BuildConfig
import com.topjohnwu.liorsmagic.core.Config
import com.topjohnwu.liorsmagic.core.data.liorsmagicdb.PolicyDao
import com.topjohnwu.liorsmagic.core.ktx.getPackageInfo
import com.topjohnwu.liorsmagic.core.model.su.SuPolicy
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class SuRequestHandler(
    val pm: PackageManager,
    private val policyDB: PolicyDao
) {

    private lateinit var output: File
    private lateinit var policy: SuPolicy
    lateinit var pkgInfo: PackageInfo
        private set

    // Return true to indicate undetermined policy, require user interaction
    suspend fun start(intent: Intent): Boolean {
        if (!init(intent))
            return false

        // Never allow com.topjohnwu.liorsmagic (could be malware)
        if (pkgInfo.packageName == BuildConfig.APPLICATION_ID) {
            Shell.cmd("(pm uninstall ${BuildConfig.APPLICATION_ID} >/dev/null 2>&1)&").exec()
            return false
        }

        when (Config.suAutoResponse) {
            Config.Value.SU_AUTO_DENY -> {
                respond(SuPolicy.DENY, 0)
                return false
            }
            Config.Value.SU_AUTO_ALLOW -> {
                respond(SuPolicy.ALLOW, 0)
                return false
            }
        }

        return true
    }

    private suspend fun init(intent: Intent): Boolean {
        val uid = intent.getIntExtra("uid", -1)
        val pid = intent.getIntExtra("pid", -1)
        val fifo = intent.getStringExtra("fifo")
        if (uid <= 0 || pid <= 0 || fifo == null) {
            return false
        }
        output = File(fifo)
        policy = SuPolicy(uid)
        try {
            pkgInfo = pm.getPackageInfo(uid, pid) ?: PackageInfo().apply {
                val name = pm.getNameForUid(uid) ?: throw PackageManager.NameNotFoundException()
                // We only fill in sharedUserId and leave other fields uninitialized
                sharedUserId = name.split(":")[0]
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
            respond(SuPolicy.DENY, -1)
            return false
        }
        return output.canWrite()
    }

    suspend fun respond(action: Int, time: Int) {
        val until = if (time > 0)
            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) +
                TimeUnit.MINUTES.toSeconds(time.toLong())
        else
            time.toLong()

        policy.policy = action
        policy.until = until

        withContext(Dispatchers.IO) {
            try {
                DataOutputStream(FileOutputStream(output)).use {
                    it.writeInt(policy.policy)
                    it.flush()
                }
            } catch (e: IOException) {
                Timber.e(e)
            }
            if (until >= 0) {
                policyDB.update(policy)
            }
        }
    }
}
