package com.xiaoguang.widget.biometric

import androidx.core.hardware.fingerprint.FingerprintManagerCompat

/**
 * 验证结果回调，供使用者调用
 */
interface FingerprintCallback {

    /**
     * 验证成功
     */
    fun onSucceeded23(result: FingerprintManagerCompat.AuthenticationResult?)

    /**
     * 验证成功
     */
    fun onSucceeded28(result: androidx.biometric.BiometricPrompt.AuthenticationResult?)

    /**
     * 验证失败
     */
    fun onFailed()

    /**
     * 验证失败
     */
    fun onError(errString: CharSequence?)

    /**
     * 取消验证
     */
    fun onCancel()
}