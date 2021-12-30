package com.xiaoguang.widget.biometric

import android.content.Context
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

/**
 * Android P == 9.0
 */
@RequiresApi(api = Build.VERSION_CODES.P)
class BiometricApi28 : IFingerprint {
    private val handler = Handler()
    private val executor = Executor { command -> handler.post(command) }
    private var biometricPrompt: BiometricPrompt? = null

    override fun authenticate(
        context: Context,
        config: BiometricConfig,
        callback: FingerprintCallback?
    ) {
        val promptInfo = PromptInfo.Builder()
            .setTitle(config.title) //设置大标题
            .setNegativeButtonText(config.cancelText) //设置取消按钮
            .build()

        //需要提供的参数callback
        biometricPrompt = BiometricPrompt(
            (context as FragmentActivity),
            executor, object : BiometricPrompt.AuthenticationCallback() {
                //各种异常的回调
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    if (callback != null) {
                        if (errorCode == 13) {
                            callback.onCancel()
                        } else {
                            callback.onError(errString)
                        }
                    }
                }

                //认证成功的回调
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    if (callback != null) {
                        callback.onSucceeded28(result)
                    }
                    //                BiometricPrompt.CryptoObject authenticatedCryptoObject =
//                        result.getCryptoObject();
                    // User has verified the signature, cipher, or message
                    // authentication code (MAC) associated with the crypto object,
                    // so you can use it in your app's crypto-driven workflows.
                }

                //认证失败的回调
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    if (callback != null) {
                        callback.onFailed()
                    }
                }
            })

        // 显示认证对话框
        biometricPrompt!!.authenticate(promptInfo)
    }

}