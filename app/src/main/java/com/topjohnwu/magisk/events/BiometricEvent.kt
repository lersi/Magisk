package com.topjohnwu.liorsmagic.events

import com.topjohnwu.liorsmagic.arch.ActivityExecutor
import com.topjohnwu.liorsmagic.arch.UIActivity
import com.topjohnwu.liorsmagic.arch.ViewEvent
import com.topjohnwu.liorsmagic.core.di.ServiceLocator
import com.topjohnwu.liorsmagic.core.utils.BiometricHelper

class BiometricEvent(
    builder: Builder.() -> Unit
) : ViewEvent(), ActivityExecutor {

    private var listenerOnFailure: () -> Unit = {}
    private var listenerOnSuccess: () -> Unit = {}

    init {
        builder(Builder())
    }

    override fun invoke(activity: UIActivity<*>) {
        ServiceLocator.biometrics.authenticate(
            activity,
            onError = listenerOnFailure,
            onSuccess = listenerOnSuccess
        )
    }

    inner class Builder internal constructor() {

        fun onFailure(listener: () -> Unit) {
            listenerOnFailure = listener
        }

        fun onSuccess(listener: () -> Unit) {
            listenerOnSuccess = listener
        }
    }

}
