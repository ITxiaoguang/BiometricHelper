package com.xiaoguang.widget.biometric

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.regex.Pattern

class CommDialog private constructor(builder: Builder) :
    AlertDialog(builder.context, builder.style) {
    /**
     * 图片滑动后回调
     * hxg 2021/9/28 16:00 qq:929842234
     */
    interface ButtonListener {
        fun onClick(d: CommDialog?, v: View?)
    }

    enum class ViewType {
        HORIZONTAL,  // 横向排版
        VERTICAL,  // 纵向排版
        TIP // 提示类型
    }

    //    private InterHandler mHandler;
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
        //        fitText();
    }

    fun setContent(content: String?) {
        this.content = content
        initData()
        //        fitText();
    }

    // HORIZONTAL
    private val left: String?
    private var leftListener: ButtonListener?
    private val right: String?
    private var rightListener: ButtonListener?

    // VERTICAL
    private val top: String?
    private var topListener: ButtonListener?
    private val bottom: String?
    private var bottomListener: ButtonListener?

    // TIP
    private val tipEnter: String?
    private val tipMsg: String?
    private val tipMsgBuilder: SpannableStringBuilder?

    //    private int count;
    private val tipEnterListener: ButtonListener?
    private var iv_bg: ImageView? = null
    private var tv_title: TextView? = null
    private var tv_content: TextView? = null

    // HORIZONTAL
    private var ll_horizontal: LinearLayout? = null
    var tv_left: TextView? = null
        private set
    var tv_right: TextView? = null
        private set
    private var v_weight: View? = null

    // VERTICAL
    private var ll_vertical: LinearLayout? = null
    var tv_top: TextView? = null
        private set
    var tv_bottom: TextView? = null
        private set

    // TIP
    private var ll_tip: LinearLayout? = null
    private var tv_tip_enter: TextView? = null
    private var tv_tip_msg: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_comm)
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
        // HORIZONTAL
        ll_horizontal = findViewById(R.id.ll_horizontal)
        tv_left = findViewById(R.id.tv_left)
        v_weight = findViewById(R.id.v_weight)
        tv_right = findViewById(R.id.tv_right)
        // VERTICAL
        ll_vertical = findViewById(R.id.ll_vertical)
        tv_top = findViewById(R.id.tv_top)
        tv_bottom = findViewById(R.id.tv_bottom)
        // TIP
        ll_tip = findViewById(R.id.ll_tip)
        tv_tip_enter = findViewById(R.id.tv_tip_enter)
        tv_tip_msg = findViewById(R.id.tv_tip_msg)
    }

    private fun initListener() {
        if (null != leftListener) {
            tv_left!!.setOnClickListener { v: View? ->
                if (null != leftListener) {
                    leftListener!!.onClick(this@CommDialog, v)
                }
            }
        }
        if (null != rightListener) {
            tv_right!!.setOnClickListener { v: View? ->
                if (null != rightListener) {
                    rightListener!!.onClick(this@CommDialog, v)
                }
            }
        }
        if (null != topListener) {
            tv_top!!.setOnClickListener { v: View? ->
                if (null != topListener) {
                    topListener!!.onClick(this@CommDialog, v)
                }
            }
        }
        if (null != bottomListener) {
            tv_bottom!!.setOnClickListener { v: View? ->
                if (null != bottomListener) {
                    bottomListener!!.onClick(this@CommDialog, v)
                }
            }
        }
        if (null != tipEnterListener) {
            tv_tip_enter!!.setOnClickListener { v: View? ->
                if (null != tipEnterListener) {
//                    cancelTimer();
                    tipEnterListener.onClick(this@CommDialog, v)
                }
            }
        }
        dismissListener?.let { setOnDismissListener(it) }
    }

    private fun initData() {
        when (viewType) {
            ViewType.HORIZONTAL -> {
                ll_vertical!!.visibility = View.GONE
                ll_tip!!.visibility = View.GONE
            }
            ViewType.VERTICAL -> {
                ll_horizontal!!.visibility = View.GONE
                ll_tip!!.visibility = View.GONE
            }
            ViewType.TIP -> {
                ll_horizontal!!.visibility = View.GONE
                ll_vertical!!.visibility = View.GONE
            }
        }
        if (0 != imgRes) {
            iv_bg!!.visibility = View.VISIBLE
            iv_bg!!.setBackgroundResource(imgRes)
        } else {
            iv_bg!!.visibility = View.GONE
        }
        setView(tv_title, title)
        setView(tv_content, content, contentBuilder)
        // HORIZONTAL
        if (!TextUtils.isEmpty(left)) {
            v_weight!!.visibility = View.VISIBLE
            tv_left!!.visibility = View.VISIBLE
            tv_left!!.text = left
        } else {
            v_weight!!.visibility = View.GONE
            tv_left!!.visibility = View.GONE
        }
        setView(tv_right, right)
        // VERTICAL
        setView(tv_top, top)
        setView(tv_bottom, bottom)
        // TIP
        setView(tv_tip_enter, tipEnter)
        setView(tv_tip_msg, tipMsg, tipMsgBuilder)
        //        setTipCount();
    }

    //    private void setTipCount() {
    //        if (null != tipMsgBuilder && 0 != count) {
    //            startTimer();
    //        }
    //    }
    //    private Timer timer;
    //    private TimerTask timerTask;
    //
    //    /**
    //     * 倒计时
    //     */
    //    private void startTimer() {
    //        cancelTimer();
    //        if (null == mHandler) {
    //            mHandler = new InterHandler(this);
    //        }
    //        timer = new Timer();
    //        timerTask = new TimerTask() {
    //            @Override
    //            public void run() {
    //                mHandler.sendEmptyMessage(1);
    //            }
    //        };
    //        timer.schedule(timerTask, 1000, 1000);
    //    }
    //
    //    private void cancelTimer() {
    //        if (null != timer) {
    //            timer.cancel();
    //            timer = null;
    //        }
    //        if (null != timerTask) {
    //            timerTask.cancel();
    //            timerTask = null;
    //        }
    //    }
    //    private void setTipMsgBuilder() {
    //        --count;
    //        if (count <= 0) {
    //            if (null != tipEnterListener) {
    //                cancelTimer();
    //                tipEnterListener.onClick(this, tv_tip_msg);
    //            }
    //            return;
    //        }
    //        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getRichText(
    //                StringUtils.getString(R.string.enter_tips, count + "s"),
    //                count + "s",
    //                ColorUtils.getColor(R.color.colorAccent)));
    //        setView(tv_tip_msg, tipMsg, stringBuilder);
    ////        fitText();
    //    }
    //    private static class InterHandler extends Handler {
    //        private final WeakReference<CommDialog> mActivity;
    //
    //        InterHandler(CommDialog activity) {
    //            mActivity = new WeakReference<>(activity);
    //        }
    //
    //        @Override
    //        public void handleMessage(Message msg) {
    //            CommDialog activity = mActivity.get();
    //            if (activity != null) {
    //                if (msg.what == 1) {
    //                    activity.setTipMsgBuilder();
    //                }
    //            }
    //        }
    //    }
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

    //    public void fitText() {
    //        View view = null != tv_top ? tv_top
    //                : null != tv_content ? tv_content
    //                : tv_tip_msg;
    //        if (null == view) {
    //            return;
    //        }
    //        view.postDelayed(() -> {
    //            if (null != tv_top) {
    //                if (tv_top.getLineCount() > 1) {
    //                    tv_top.setGravity(Gravity.START);
    //                } else {
    //                    tv_top.setGravity(Gravity.CENTER);
    //                }
    //            }
    //            if (null != tv_content) {
    //                if (tv_content.getLineCount() > 1) {
    //                    tv_content.setGravity(Gravity.START);
    //                } else {
    //                    tv_content.setGravity(Gravity.CENTER);
    //                }
    //            }
    //            if (null != tv_tip_msg) {
    //                if (tv_tip_msg.getLineCount() > 1) {
    //                    tv_tip_msg.setGravity(Gravity.START);
    //                } else {
    //                    tv_tip_msg.setGravity(Gravity.CENTER);
    //                }
    //            }
    //        }, 10);
    //    }
    override fun cancel() {
        if (canExit) {
            super.cancel()
        }
    }

    override fun dismiss() {
        super.dismiss()
        leftListener = null
        rightListener = null
        topListener = null
        bottomListener = null
        //        if (null != mHandler) {
//            mHandler.removeCallbacksAndMessages(null);
//        }
    }

    class Builder {
        var context: Context
        var viewType = ViewType.HORIZONTAL
        var style = R.style.CommonDialogStyle
        var canExit = true
        var title: String? = null
        var content: String? = null
        var contentBuilder: SpannableStringBuilder? = null
        var dismissListener: DialogInterface.OnDismissListener? = null
        var left: String? = null
        var leftListener: ButtonListener? = null
        var right: String? = null
        var rightListener: ButtonListener? = null

        @DrawableRes
        var imgRes = 0
        var top: String? = null
        var topListener: ButtonListener? = null
        var bottom: String? = null
        var bottomListener: ButtonListener? = null
        val tipEnter: String? = null
        val tipMsg: String? = null
        val tipMsgBuilder: SpannableStringBuilder? = null

        //        private int count;
        val tipEnterListener: ButtonListener? = null

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
         * @param leftId The left button.
         */
        fun setLeft(@StringRes leftId: Int): Builder {
            left = context.getString(leftId)
            return this
        }

        /**
         * @param left The left button.
         */
        fun setLeft(left: String?): Builder {
            this.left = left
            return this
        }

        /**
         * @param leftId       The left button.
         * @param leftListener The left button's listener.
         */
        fun setLeft(@StringRes leftId: Int, leftListener: ButtonListener?): Builder {
            left = context.getString(leftId)
            this.leftListener = leftListener
            return this
        }

        /**
         * @param left         The left button.
         * @param leftListener The left button's listener.
         */
        fun setLeft(left: String?, leftListener: ButtonListener?): Builder {
            this.left = left
            this.leftListener = leftListener
            return this
        }

        /**
         * @param rightId The right button.
         */
        fun setRight(@StringRes rightId: Int): Builder {
            right = context.getString(rightId)
            return this
        }

        /**
         * @param right The right button.
         */
        fun setRight(right: String?): Builder {
            this.right = right
            return this
        }

        /**
         * @param rightId       The right button.
         * @param rightListener The right button's listener.
         */
        fun setRight(@StringRes rightId: Int, rightListener: ButtonListener?): Builder {
            right = context.getString(rightId)
            this.rightListener = rightListener
            return this
        }

        /**
         * @param right         The right button.
         * @param rightListener The right button's listener.
         */
        fun setRight(right: String?, rightListener: ButtonListener?): Builder {
            this.right = right
            this.rightListener = rightListener
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

        /**
         * @param bottomId The bottom button.
         */
        fun setBottom(@StringRes bottomId: Int): Builder {
            bottom = context.getString(bottomId)
            return this
        }

        /**
         * @param bottom The bottom button.
         */
        fun setBottom(bottom: String?): Builder {
            this.bottom = bottom
            return this
        }

        /**
         * @param bottomId       The bottom button.
         * @param bottomListener The bottom button's listener.
         */
        fun setBottom(@StringRes bottomId: Int, bottomListener: ButtonListener?): Builder {
            bottom = context.getString(bottomId)
            this.bottomListener = bottomListener
            return this
        }

        /**
         * @param bottom         The bottom button.
         * @param bottomListener The bottom button's listener.
         */
        fun setBottom(bottom: String?, bottomListener: ButtonListener?): Builder {
            this.bottom = bottom
            this.bottomListener = bottomListener
            return this
        }

        /**
         * 去除定时器模块
         */
        //        /**
        //         * @param tipEnterId The enter bottom.
        //         * @param tipMsgId   The Tip message.
        //         * @param count      The tip can count down.
        //         */
        //        public Builder setTip(@StringRes int tipEnterId, @StringRes int tipMsgId, int count, ButtonListener tipEnterListener) {
        //            this.tipEnter = context.getString(tipEnterId);
        //            this.tipMsg = context.getString(tipMsgId);
        //            this.count = count;
        //            this.tipEnterListener = tipEnterListener;
        //            return this;
        //        }
        //        /**
        //         * @param tipEnter The enter bottom.
        //         * @param tipMsg   The Tip message.
        //         * @param count    The tip can count down.
        //         */
        //        public Builder setTip(String tipEnter, String tipMsg, int count, ButtonListener tipEnterListener) {
        //            this.tipEnter = tipEnter;
        //            this.tipMsg = tipMsg;
        //            this.count = count;
        //            this.tipEnterListener = tipEnterListener;
        //            return this;
        //        }
        //        /**
        //         * @param tipEnterId    The enter bottom.
        //         * @param tipMsgBuilder The Tip message.
        //         * @param count         The tip can count down.
        //         */
        //        public Builder setTip(@StringRes int tipEnterId, SpannableStringBuilder tipMsgBuilder, int count, ButtonListener tipEnterListener) {
        //            this.tipEnter = context.getString(tipEnterId);
        //            this.tipMsgBuilder = tipMsgBuilder;
        //            this.count = count;
        //            this.tipEnterListener = tipEnterListener;
        //            return this;
        //        }
        //        /**
        //         * @param tipEnter      The enter bottom.
        //         * @param tipMsgBuilder The Tip message.
        //         * @param count         The tip can count down.
        //         */
        //        public Builder setTip(String tipEnter, SpannableStringBuilder tipMsgBuilder, int count, ButtonListener tipEnterListener) {
        //            this.tipEnter = tipEnter;
        //            this.tipMsgBuilder = tipMsgBuilder;
        //            this.count = count;
        //            this.tipEnterListener = tipEnterListener;
        //            return this;
        //        }
        fun create(): CommDialog {
            return CommDialog(this)
        }


    }

    companion object {
        /**
         * 获取高亮字符串
         *
         * @param content 内容
         * @param target  高亮的部分
         * @param color   高亮的颜色
         */
        fun getRichText(content: String?, target: String?, color: Int): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val matcher = Pattern.compile(target).matcher(content)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                stringBuilder.setSpan(
                    ForegroundColorSpan(color),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return stringBuilder
        }
    }

    init {
        viewType = builder.viewType
        canExit = builder.canExit
        title = builder.title
        content = builder.content
        contentBuilder = builder.contentBuilder
        dismissListener = builder.dismissListener
        left = builder.left
        leftListener = builder.leftListener
        right = builder.right
        rightListener = builder.rightListener
        imgRes = builder.imgRes
        top = builder.top
        topListener = builder.topListener
        bottom = builder.bottom
        bottomListener = builder.bottomListener
        tipEnter = builder.tipEnter
        tipMsg = builder.tipMsg
        tipMsgBuilder = builder.tipMsgBuilder
        //        count = builder.count;
        tipEnterListener = builder.tipEnterListener
    }
}