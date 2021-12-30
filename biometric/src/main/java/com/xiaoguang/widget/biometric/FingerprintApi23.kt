package com.xiaoguang.widget.biometric

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.xiaoguang.widget.biometric.CommDialog.ButtonListener
import com.xiaoguang.widget.biometric.CommDialog.ViewType

/**
 * Android M == 6.0
 */
@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintApi23 : IFingerprint {
    private var context: Context? = null
    private var config: BiometricConfig? = null

    //指向调用者的指纹回调
    private var fingerprintCallback: FingerprintCallback? = null

    //用于取消扫描器的扫描动作
    private var cancellationSignal: CancellationSignal? = null

    //Android 6.0 指纹管理
    private var fingerprintManagerCompat: FingerprintManagerCompat? = null
    override fun authenticate(
        context: Context,
        config: BiometricConfig,
        callback: FingerprintCallback?
    ) {
        this.context = context
        this.config = config
        fingerprintCallback = callback
        //Android 6.0 指纹管理 实例化
        fingerprintManagerCompat = FingerprintManagerCompat.from(context)

        //取消扫描，每次取消后需要重新创建新示例
        cancellationSignal = CancellationSignal()
        cancellationSignal!!.setOnCancelListener {
            if (null != cancellationSignal && commDialog!!.isShowing) {
                commDialog!!.dismiss()
            }
        }

        //调起指纹验证
        fingerprintManagerCompat!!.authenticate(
            cryptoObject,
            0,
            cancellationSignal,
            authenticationCallback,
            null
        )
        //指纹验证框
        //指纹验证框
        commDialog = CommDialog.Builder(context, ViewType.VERTICAL)
            .setCanExit(false)
            .setImgRes(config.imgRes)
            .setTitle(config.title)
            .setTop(config.cancelText, object : ButtonListener {
                override fun onClick(d: CommDialog?, v: View?) {
                    d?.dismiss()
                }
            })
            .setOnDismissListener {
                if (cancellationSignal != null && !cancellationSignal!!.isCanceled) {
                    cancellationSignal!!.cancel()
                }
            }
            .create()
        commDialog!!.show()

    }

    /**
     * 指纹验证结果回调
     */
    private val authenticationCallback: FingerprintManagerCompat.AuthenticationCallback =
        object : FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
                super.onAuthenticationError(errMsgId, errString)
                //errMsgId==5时，在OnDialogActionListener的onCancle回调中处理；！=5的报错，才需要显示在指纹验证框中。
                if (errMsgId != 5) {
                    commDialog!!.setTitle(errString.toString())
                    commDialog!!.setContent(null)
                    commDialog!!.window!!.decorView.removeCallbacks(runnable)
                    commDialog!!.window!!
                        .decorView.postDelayed(runnable, 800)
                    if (fingerprintCallback != null) {
                        fingerprintCallback!!.onCancel()
                    }
                }
            }

            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
                super.onAuthenticationHelp(helpMsgId, helpString)
                commDialog!!.setTitle(helpString.toString())
                commDialog!!.setContent(null)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                fingerprintCallback!!.onSucceeded23(result)
                commDialog!!.dismiss()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                commDialog!!.setTitle(config!!.failTitle)
                commDialog!!.setContent(config!!.failContent)
                fingerprintCallback!!.onFailed()
            }
        }
    var runnable = Runnable {
        if (null != commDialog) {
            commDialog!!.dismiss()
        }
    }

    fun onActivityPause() {
        if (null != commDialog) {
            if (commDialog!!.isShowing) {
                commDialog!!.dismiss()
                if (fingerprintCallback != null) {
                    fingerprintCallback!!.onCancel()
                }
            }
        }
    }

    fun onActivityDestroy() {
        if (null != commDialog) {
            if (commDialog!!.isShowing) {
                commDialog!!.dismiss()
                if (fingerprintCallback != null) {
                    fingerprintCallback!!.onCancel()
                }
            } else {
                commDialog = null
            }
        }
    }

    companion object {
        //指纹验证框
        private var commDialog: CommDialog? = null

        //指纹加密
        private var cryptoObject: FingerprintManagerCompat.CryptoObject? = null
    }

    init {
        //指纹加密，提前进行Cipher初始化，防止指纹认证时还没有初始化完成
        try {
            cryptoObject = FingerprintManagerCompat.CryptoObject(CipherHelper().createCipher()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}