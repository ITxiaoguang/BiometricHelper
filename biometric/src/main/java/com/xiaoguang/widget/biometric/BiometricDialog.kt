package com.xiaoguang.widget.biometric

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class BiometricDialog private constructor(builder: Builder) :
    AlertDialog(builder.context, builder.style) {

    interface ButtonListener {
        fun onClick(d: BiometricDialog?, v: View?)
    }

    enum class ViewType {
        VERTICAL,  // 纵向排版
    }

    private val viewType: ViewType
    private val canExit: Boolean

    @DrawableRes
    private val imgRes: Int
    private var title: String?
    private var content: String?
    private val contentBuilder: SpannableStringBuilder?
    private val dismissListener: DialogInterface.OnDismissListener?
    fun setTitle(title: String?) {
        this.title = title
        initData()
    }

    fun setContent(content: String?) {
        this.content = content
        initData()
    }

    private var iv_bg: ImageView? = null
    private var tv_title: TextView? = null
    private var tv_content: TextView? = null

    // VERTICAL
    private val top: String?
    private var topListener: ButtonListener?

    // VERTICAL
    private var ll_vertical: LinearLayout? = null
    var tv_top: TextView? = null
        private set

    init {
        viewType = builder.viewType
        canExit = builder.canExit
        title = builder.title
        content = builder.content
        contentBuilder = builder.contentBuilder
        dismissListener = builder.dismissListener
        imgRes = builder.imgRes
        top = builder.top
        topListener = builder.topListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_biometric)
        window!!.setBackgroundDrawableResource(R.color.transparent)
        setCanceledOnTouchOutside(canExit)
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        iv_bg = findViewById(R.id.iv_bg)
        tv_title = findViewById(R.id.tv_title)
        tv_content = findViewById(R.id.tv_content)
        // VERTICAL
        ll_vertical = findViewById(R.id.ll_vertical)
        tv_top = findViewById(R.id.tv_top)
    }

    private fun initListener() {
        if (null != topListener) {
            tv_top!!.setOnClickListener { v: View? ->
                if (null != topListener) {
                    topListener!!.onClick(this@BiometricDialog, v)
                }
            }
        }
        dismissListener?.let { setOnDismissListener(it) }
    }

    private fun initData() {
        if (0 != imgRes) {
            iv_bg!!.visibility = View.VISIBLE
            iv_bg!!.setBackgroundResource(imgRes)
        } else {
            iv_bg!!.visibility = View.GONE
        }
        setView(tv_title, title)
        setView(tv_content, content, contentBuilder)
        // VERTICAL
        setView(tv_top, top)
    }

    private fun setView(textView: TextView?, content: String?) {
        if (null == textView) {
            return
        }
        if (!TextUtils.isEmpty(content)) {
            textView.visibility = View.VISIBLE
            textView.text = content
        } else {
            textView.visibility = View.GONE
        }
    }

    private fun setView(
        textView: TextView?,
        content: String?,
        contentBuilder: SpannableStringBuilder?
    ) {
        if (null == textView) {
            return
        }
        if (!TextUtils.isEmpty(content)) {
            textView.visibility = View.VISIBLE
            textView.text = content
        } else if (!TextUtils.isEmpty(contentBuilder)) {
            textView.visibility = View.VISIBLE
            textView.text = contentBuilder
            textView.movementMethod = LinkMovementMethod.getInstance()
        } else {
            textView.visibility = View.GONE
        }
    }

    override fun cancel() {
        if (canExit) {
            super.cancel()
        }
    }

    override fun dismiss() {
        super.dismiss()
        topListener = null
    }

    class Builder {
        var context: Context
        var viewType = ViewType.VERTICAL
        var style = R.style.BiometricDialogStyle
        var canExit = true
        var title: String? = null
        var content: String? = null
        var contentBuilder: SpannableStringBuilder? = null
        var dismissListener: DialogInterface.OnDismissListener? = null

        @DrawableRes
        var imgRes = 0
        var top: String? = null
        var topListener: ButtonListener? = null

        constructor(context: Context) {
            this.context = context
        }

        constructor(context: Context, viewType: ViewType) {
            this.context = context
            this.viewType = viewType
        }

        /**
         * @param style The style for dialog.
         */
        fun setStyle(style: Int): Builder {
            this.style = style
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
         * @param titleId The title for dialog.
         */
        fun setTitle(@StringRes titleId: Int): Builder {
            title = context.getString(titleId)
            return this
        }

        /**
         * @param title The title for dialog.
         */
        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        /**
         * @param canExit Whether the dialog can exit.
         */
        fun setCanExit(canExit: Boolean): Builder {
            this.canExit = canExit
            return this
        }

        /**
         * @param contentId The content for dialog.
         */
        fun setContent(@StringRes contentId: Int): Builder {
            content = context.getString(contentId)
            return this
        }

        /**
         * @param content The content for dialog.
         */
        fun setContent(content: String?): Builder {
            this.content = content
            return this
        }

        /**
         * @param contentBuilder The content for dialog.
         */
        fun setContent(contentBuilder: SpannableStringBuilder?): Builder {
            this.contentBuilder = contentBuilder
            return this
        }

        /**
         * @param dismissListener Interface used to allow the creator of a dialog to
         * run some code when the dialog is dismissed.
         */
        fun setOnDismissListener(dismissListener: DialogInterface.OnDismissListener?): Builder {
            this.dismissListener = dismissListener
            return this
        }

        /**
         * @param topId The top button.
         */
        fun setTop(@StringRes topId: Int): Builder {
            top = context.getString(topId)
            return this
        }

        /**
         * @param top The top button.
         */
        fun setTop(top: String?): Builder {
            this.top = top
            return this
        }

        /**
         * @param topId       The top button.
         * @param topListener The top button's listener.
         */
        fun setTop(@StringRes topId: Int, topListener: ButtonListener?): Builder {
            top = context.getString(topId)
            this.topListener = topListener
            return this
        }

        /**
         * @param top         The top button.
         * @param topListener The top button's listener.
         */
        fun setTop(top: String, topListener: ButtonListener?): Builder {
            this.top = top
            this.topListener = topListener
            return this
        }

        fun create(): BiometricDialog {
            return BiometricDialog(this)
        }

    }

}