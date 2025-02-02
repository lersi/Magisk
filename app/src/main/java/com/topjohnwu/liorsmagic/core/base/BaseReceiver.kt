package com.topjohnwu.liorsmagic.core.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import com.topjohnwu.liorsmagic.core.patch

abstract class BaseReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent?) {
        context.patch()
    }
}
