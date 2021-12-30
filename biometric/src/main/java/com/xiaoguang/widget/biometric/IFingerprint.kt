package com.xiaoguang.widget.biometric

import android.content.Context


interface IFingerprint {
    /**
     * 初始化并调起指纹验证
     *
     * @param context
     * @param verificationDialogStyleBean
     * @param callback
     */
    fun authenticate(
        context: Context,
        verificationDialogStyleBean: BiometricConfig,
        callback: FingerprintCallback?
    )
}