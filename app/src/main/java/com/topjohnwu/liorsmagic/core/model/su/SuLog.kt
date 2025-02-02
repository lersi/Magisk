package com.topjohnwu.liorsmagic.core.model.su

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.topjohnwu.liorsmagic.core.ktx.getLabel

@Entity(tableName = "logs")
class SuLog(
    val fromUid: Int,
    val toUid: Int,
    val fromPid: Int,
    val packageName: String,
    val appName: String,
    val command: String,
    val action: Boolean,
    val time: Long = System.currentTimeMillis()
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

fun PackageManager.createSuLog(
    info: PackageInfo,
    toUid: Int,
    fromPid: Int,
    command: String,
    policy: Int
): SuLog {
    val appInfo = info.applicationInfo
    return SuLog(
        fromUid = appInfo.uid,
        toUid = toUid,
        fromPid = fromPid,
        packageName = getNameForUid(appInfo.uid)!!,
        appName = appInfo.getLabel(this),
        command = command,
        action = policy == SuPolicy.ALLOW
    )
}

fun createSuLog(
    fromUid: Int,
    toUid: Int,
    fromPid: Int,
    command: String,
    policy: Int
): SuLog {
    return SuLog(
        fromUid = fromUid,
        toUid = toUid,
        fromPid = fromPid,
        packageName = "[UID] $fromUid",
        appName = "[UID] $fromUid",
        command = command,
        action = policy == SuPolicy.ALLOW
    )
}
