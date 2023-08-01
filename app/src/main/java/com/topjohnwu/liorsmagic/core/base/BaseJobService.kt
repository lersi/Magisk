package com.topjohnwu.liorsmagic.core.base

import android.app.job.JobService
import android.content.Context
import com.topjohnwu.liorsmagic.core.patch

abstract class BaseJobService : JobService() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.patch())
    }
}
