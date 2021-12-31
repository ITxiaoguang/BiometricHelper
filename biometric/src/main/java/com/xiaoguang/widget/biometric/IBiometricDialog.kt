package com.xiaoguang.widget.biometric

import android.content.Context
import android.content.DialogInterface
import android.view.Window

/**
 * Xiaoguang 2021-12-31 10:15 qq:929842234.
 */
interface IBiometricDialog {

    /**
     * Show dialog
     */
    fun showDialog(
        context: Context,
        config: BiometricConfig,
        dismissListener: DialogInterface.OnDismissListener
    )

    /**
     * Dismiss dialog
     */
    fun dismiss()

    /**
     * setTitle
     */
    fun setTitle(title: String)

    /**
     * setContent
     */
    fun setContent(content: String?)

    /**
     * getWindow
     */
    fun getWindow(): Window

    /**
     * isShowing
     */
    fun isShowing(): Boolean


}