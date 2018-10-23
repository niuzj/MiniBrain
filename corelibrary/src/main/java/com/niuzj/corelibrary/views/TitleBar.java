package com.niuzj.corelibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuzj.corelibrary.R;

public class TitleBar extends FrameLayout implements View.OnClickListener {

    private Context mContext;

    private ImageView mIvLeft;
    private TextView mTvTitle;
    private TextView mTvRight;

    private OnTitleBarClickListener mOnTitleBarClickListener;

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initAttr(attrs);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.core_library_title_bar_layout, this);
        mIvLeft = view.findViewById(R.id.iv_left);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvRight = view.findViewById(R.id.tv_right);

        mIvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);

    }

    private void initAttr(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.core_library_TitleBar);
        String title = typedArray.getString(R.styleable.core_library_TitleBar_core_library_title);
        String rightTitle = typedArray.getString(R.styleable.core_library_TitleBar_core_library_right_title);
        boolean visiable = typedArray.getBoolean(R.styleable.core_library_TitleBar_core_library_left_visiable, true);
        setTitle(title);
        setRightTitle(rightTitle);
        setLeftVisiable(visiable);
    }

    public void setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
            if (TextUtils.isEmpty(title)) {
                mTvTitle.setVisibility(GONE);
            } else {
                mTvTitle.setVisibility(VISIBLE);
            }
        }
    }

    public void setRightTitle(String rightTitle) {
        if (mTvRight != null) {
            mTvRight.setText(rightTitle);
            if (TextUtils.isEmpty(rightTitle)) {
                mTvRight.setVisibility(GONE);
            } else {
                mTvRight.setVisibility(VISIBLE);
            }
        }
    }

    public void setLeftVisiable(boolean visiable) {
        if (mIvLeft != null) {
            mIvLeft.setVisibility(visiable ? VISIBLE : GONE);
        }
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        mOnTitleBarClickListener = onTitleBarClickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_left) {
            if (mOnTitleBarClickListener != null) {
                mOnTitleBarClickListener.clickLeft();
            }
        } else if (i == R.id.tv_right) {
            if (mOnTitleBarClickListener != null) {
                mOnTitleBarClickListener.clickRight();
            }
        }
    }

    public interface OnTitleBarClickListener {
        void clickLeft();

        void clickRight();
    }

}
