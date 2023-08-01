package com.topjohnwu.liorsmagic.core.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.Config

class BiometricHelper(context: Context) {

    private val mgr = BiometricManager.from(context)

    val isSupported get() = when (mgr.canAuthenticate(Authenticators.BIOMETRIC_WEAK)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }

    val isEnabled: Boolean get() {
        val enabled = Config.suBiometric
        if (enabled && !isSupported) {
            Config.suBiometric = false
            return false
        }
        return enabled
    }

    fun authenticate(
        activity: FragmentActivity,
        onError: () -> Unit = {},
        onSuccess: () -> Unit): BiometricPrompt {
        val prompt = BiometricPrompt(activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError()
                }

                override fun onAuthenticationFailed() {
                    onError()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }
            }
        )
        val info = BiometricPrompt.PromptInfo.Builder()
            .setConfirmationRequired(true)
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_WEAK)
            .setTitle(activity.getString(R.string.authenticate))
            .setNegativeButtonText(activity.getString(android.R.string.cancel))
            .build()
        prompt.authenticate(info)
        return prompt
    }

}
