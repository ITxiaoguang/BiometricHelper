package com.xiaoguang.widget.biometric

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.biometric.BiometricManager

/**
 * Created by Xiaoguang on 2021/11/18.
 */
class BiometricPromptManager(builder: Builder) {
    private var mImpl: IFingerprint? = null

    /**
     * Dismiss dialog.
     */
    fun onActivityPause() {
        if (mImpl is FingerprintApi23 && isAboveApi23) {
            (mImpl as FingerprintApi23).onActivityPause()
        }
    }

    /**
     * Activity destroy.
     */
    fun onActivityDestroy() {
        if (mImpl is FingerprintApi23 && isAboveApi23) {
            (mImpl as FingerprintApi23).onActivityDestroy()
        }
    }

    /**
     * UpdateAppManager的构建器
     */
    class Builder
    /**
     * 构建器
     *
     * @param context
     */(  /*必选字段*/
        val context: Context
    ) {
        var callback: FingerprintCallback? = null

        //在Android 9.0系统上，是否使用系统验证框
        var enableAndroidP = true
        @DrawableRes
        var imgRes = 0
        var title: String = ""
        var failTitle: String = ""
        var failContent: String = ""
        var cancelText: String = ""//取消按钮文字
        var iBiometricDialog: IBiometricDialog? = null//弹窗

        /**
         * 指纹识别回调
         *
         * @param callback
         */
        fun setCallback(callback: FingerprintCallback): Builder {
            this.callback = callback
            return this
        }

        /**
         * 在 >= Android 9.0 系统上，是否开启google提供的验证方式及验证框
         *
         * @param enableAndroidP
         */
        fun enableAndroidP(enableAndroidP: Boolean): Builder {
            this.enableAndroidP = enableAndroidP
            return this
        }

        /**
         * @param imgRes The resource of image.
         */
        fun setImgRes(@DrawableRes imgRes: Int): Builder {
            this.imgRes = imgRes
            return this
        }

        /**
         * >= Android 9.0 的验证框的主标题
         *
         * @param title
         */
        fun title(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * >= Android 9.0 的验证框的描述内容
         *
         * @param failTitle
         */
        fun failTitle(failTitle: String): Builder {
            this.failTitle = failTitle
            return this
        }

        /**
         * >= Android 9.0 的验证框的取消按钮的文字
         *
         * @param failContent
         */
        fun failContent(failContent: String): Builder {
            this.failContent = failContent
            return this
        }

        /**
         * >= Android 9.0 的验证框的取消按钮的文字
         *
         * @param cancelText
         */
        fun cancelText(cancelText: String): Builder {
            this.cancelText = cancelText
            return this
        }

        /**
         * 弹窗
         *
         * @param iBiometricDialog
         */
        fun setCustomDialog(iBiometricDialog: IBiometricDialog): Builder {
            this.iBiometricDialog = iBiometricDialog
            return this
        }

        /**
         * 开始构建
         *
         * @return
         */
        fun build(): BiometricPromptManager {
            return BiometricPromptManager(this)
        }
    }

    companion object {
        val isAboveApi28: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
        val isAboveApi23: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        /**
         * Return whether the device support biometric.
         *
         * @param context context
         * @return {@code: true}:yes<br></br>{@code: false}:no
         */
        fun isBiometricPromptEnable(context: Context?): Boolean {
            val biometricManager = BiometricManager.from(
                context!!
            )
            return when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.e("FingerprintUtils", "应用可以进行生物识别技术进行身份验证。")
                    true
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.e("FingerprintUtils", "该设备上没有搭载可用的生物特征功能。")
                    false
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.e("FingerprintUtils", "生物识别功能当前不可用。")
                    false
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.e("FingerprintUtils", "用户没有录入生物识别数据。")
                    false
                }
                else -> {
                    Log.e("FingerprintUtils", "un know")
                    false
                }
            }
        }
    }

    init {
        if (isAboveApi28) {
            mImpl = if (builder.enableAndroidP) {
                BiometricApi28()
            } else {
                FingerprintApi23()
            }
        } else if (isAboveApi23) {
            mImpl = FingerprintApi23()
        }

        // API >= Android 6.0
        val config = BiometricConfig()
        // API  >= Android 9.0
        // 设定指纹验证框的样式
        config.imgRes = builder.imgRes
        config.title = builder.title
        config.failTitle = builder.failTitle
        config.failContent = builder.failContent
        config.cancelText = builder.cancelText
        if(null == builder.iBiometricDialog){
            config.iBiometricDialog = BiometricDialogImpl()
        }else{
            config.iBiometricDialog = builder.iBiometricDialog
        }
        mImpl!!.authenticate(builder.context, config, builder.callback)
    }
}