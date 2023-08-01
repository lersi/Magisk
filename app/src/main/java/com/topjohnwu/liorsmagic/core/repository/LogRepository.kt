package com.topjohnwu.liorsmagic.core.repository

import com.topjohnwu.liorsmagic.core.Const
import com.topjohnwu.liorsmagic.core.Info
import com.topjohnwu.liorsmagic.core.data.SuLogDao
import com.topjohnwu.liorsmagic.core.ktx.await
import com.topjohnwu.liorsmagic.core.model.su.SuLog
import com.topjohnwu.superuser.Shell


class LogRepository(
    private val logDao: SuLogDao
) {

    suspend fun fetchSuLogs() = logDao.fetchAll()

    suspend fun fetchLiorsmagicLogs(): String {
        val list = object : AbstractMutableList<String>() {
            val buf = StringBuilder()
            override val size get() = 0
            override fun get(index: Int): String = ""
            override fun removeAt(index: Int): String = ""
            override fun set(index: Int, element: String): String = ""
            override fun add(index: Int, element: String) {
                if (element.isNotEmpty()) {
                    buf.append(element)
                    buf.append('\n')
                }
            }
        }
        if (Info.env.isActive) {
            Shell.cmd("cat ${Const.LIORSMAGIC_LOG} || logcat -d -s Liorsmagic").to(list).await()
        } else {
            Shell.cmd("logcat -d").to(list).await()
        }
        return list.buf.toString()
    }

    suspend fun clearLogs() = logDao.deleteAll()

    fun clearLiorsmagicLogs(cb: (Shell.Result) -> Unit) =
        Shell.cmd("echo -n > ${Const.LIORSMAGIC_LOG}").submit(cb)

    suspend fun insert(log: SuLog) = logDao.insert(log)

}
