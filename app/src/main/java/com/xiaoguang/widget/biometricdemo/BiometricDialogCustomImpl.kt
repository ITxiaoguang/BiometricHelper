package com.xiaoguang.widget.biometricdemo

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.Window
import com.xiaoguang.widget.biometric.BiometricConfig
import com.xiaoguang.widget.biometric.BiometricDialog
import com.xiaoguang.widget.biometric.IBiometricDialog

/**
 * 自定义弹窗
 * 工厂模式
 * 继承IBiometricDialog
 * Xiaoguang 2021-12-31 10:29 qq:929842234.
 */
class BiometricDialogCustomImpl : IBiometricDialog {
    private var biometricDialog: BiometricDialog? = null

    override fun showDialog(
        context: Context,
        config: BiometricConfig,
        dismissListener: DialogInterface.OnDismissListener
    ) {
        biometricDialog = BiometricDialog.Builder(context)
            .setCanExit(true)
            .setImgRes(config.imgRes)
            .setTitle(config.title)
            .setTop(config.cancelText, object : BiometricDialog.ButtonListener {
                override fun onClick(d: BiometricDialog?, v: View?) {
                    d?.dismiss()
                }
            })
            .setOnDismissListener(dismissListener)
            .create()
        biometricDialog!!.show()
    }

    override fun dismiss() {
        biometricDialog!!.dismiss()
    }

    override fun setTitle(title: String) {
        biometricDialog!!.setTitle(title)
    }

    override fun setContent(content: String?) {
        biometricDialog!!.setContent(content)
    }

    override fun getWindow(): Window {
        return biometricDialog!!.window!!
    }

    override fun isShowing(): Boolean {
        return biometricDialog!!.isShowing
    }
}