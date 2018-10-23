package com.niuzj.corelibrary.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.niuzj.corelibrary.R;

import static android.content.Context.WINDOW_SERVICE;

public class DefaultDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    //文案
    private String title;
    private String content;
    private String leftTitle;
    private String rightTitle;
    //颜色
    private String titleColor;
    private String contentColor;
    private String leftColor;
    private String rightColor;
    //文字大小
    private int titleSize;
    private int contentSize;
    private int leftSize;
    private int rightSize;
    //背景
    private int resId = -1;
    //点击事件
    private OnDialogClickListener mOnDialogClickListener;
    //弹窗占屏幕宽度的比例
    private float widthRatio;


    public DefaultDialog(@NonNull Context context, Builder builder) {
        super(context, R.style.core_library_dialog);
        mContext = context;
        title = builder.title;
        content = builder.content;
        leftTitle = builder.leftTitle;
        rightTitle = builder.rightTitle;
        titleColor = builder.titleColor;
        contentColor = builder.contentColor;
        leftColor = builder.leftColor;
        rightColor = builder.rightColor;
        titleSize = builder.titleSize;
        contentSize = builder.contentSize;
        leftSize = builder.leftSize;
        rightSize = builder.rightSize;
        resId = builder.resId;
        mOnDialogClickListener = builder.mOnDialogClickListener;
        this.widthRatio = builder.widthRatio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initAttr();

    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.core_library_dialog_default_layout, null);
        setContentView(view);
        //标题
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        RelativeLayout rlTitle = (RelativeLayout) view.findViewById(R.id.rlTitle);
        if (TextUtils.isEmpty(title)) {
            rlTitle.setVisibility(View.GONE);
        } else {
            rlTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            setTextColor(titleColor, tvTitle);
            setTextSize(titleSize, tvTitle);
        }
        //内容
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(Html.fromHtml(content));
            setTextColor(contentColor, tvContent);
            setTextSize(contentSize, tvContent);
        }
        //底部按钮
        TextView tvLeft = (TextView) view.findViewById(R.id.tvLeft);
        View line = view.findViewById(R.id.line);
        TextView tvRight = (TextView) view.findViewById(R.id.tvRight);
        if (!TextUtils.isEmpty(leftTitle) && !TextUtils.isEmpty(rightTitle)) {
            tvLeft.setText(leftTitle);
            setTextColor(leftColor, tvLeft);
            setTextSize(leftSize, tvLeft);
            tvLeft.setOnClickListener(this);

            tvRight.setText(rightTitle);
            setTextColor(rightColor, tvRight);
            setTextSize(rightSize, tvRight);
            tvRight.setOnClickListener(this);
        } else {
            line.setVisibility(View.GONE);
            if (TextUtils.isEmpty(leftTitle)) {
                tvLeft.setVisibility(View.GONE);
            } else {
                tvLeft.setText(leftTitle);
                setTextColor(leftColor, tvLeft);
                setTextSize(leftSize, tvLeft);
                tvLeft.setOnClickListener(this);
            }
            if (TextUtils.isEmpty(rightTitle)) {
                tvRight.setVisibility(View.GONE);
            } else {
                tvRight.setText(rightTitle);
                setTextColor(rightColor, tvRight);
                setTextSize(rightSize, tvRight);
                tvRight.setOnClickListener(this);
            }
        }
    }

    private void initAttr() {
        if (widthRatio != 0.0f) {
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            attributes.width = (int) (defaultDisplay.getWidth() * widthRatio);
            window.setAttributes(attributes);
        }
    }

    private void setTextColor(String textColor, TextView textView) {
        if (!TextUtils.isEmpty(textColor)) {
            try {
                int color = Color.parseColor(textColor);
                textView.setTextColor(color);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void setTextSize(int textSize, TextView textView) {
        if (textSize > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvLeft) {
            if (mOnDialogClickListener != null) {
                mOnDialogClickListener.clickLeft();
            }

        } else if (i == R.id.tvRight) {
            if (mOnDialogClickListener != null) {
                mOnDialogClickListener.clickRight();
            }

        }
        dismiss();
    }

    public interface OnDialogClickListener {
        void clickLeft();

        void clickRight();
    }

    public static class Builder {
        private String title;
        private String content;
        private String leftTitle;
        private String rightTitle;

        private String titleColor;
        private String contentColor;
        private String leftColor;
        private String rightColor;

        private int titleSize;
        private int contentSize;
        private int leftSize;
        private int rightSize;

        private int resId = -1;

        private OnDialogClickListener mOnDialogClickListener;

        private float widthRatio;

        public Builder() {
            this.widthRatio = 0.8f;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder leftTitle(String leftTitle) {
            this.leftTitle = leftTitle;
            return this;
        }

        public Builder rightTitle(String rightTitle) {
            this.rightTitle = rightTitle;
            return this;
        }

        public Builder titleColor(String titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder contentColor(String contentColor) {
            this.contentColor = contentColor;
            return this;
        }

        public Builder leftColor(String leftColor) {
            this.leftColor = leftColor;
            return this;
        }

        public Builder rightColor(String rightColor) {
            this.rightColor = rightColor;
            return this;
        }

        public Builder titleSize(int titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public Builder contentSize(int contentSize) {
            this.contentSize = contentSize;
            return this;
        }

        public Builder leftSize(int leftSize) {
            this.leftSize = leftSize;
            return this;
        }

        public Builder rightSize(int rightSize) {
            this.rightSize = rightSize;
            return this;
        }

        public Builder resId(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder clickListener(OnDialogClickListener onDialogClickListener) {
            this.mOnDialogClickListener = onDialogClickListener;
            return this;
        }

        public Builder widthRatio(float widthRatio) {
            this.widthRatio = widthRatio;
            return this;
        }

        public DefaultDialog build(Context context) {
            return new DefaultDialog(context, this);
        }

    }

}
