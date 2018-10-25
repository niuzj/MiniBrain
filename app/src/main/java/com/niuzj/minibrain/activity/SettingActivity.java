package com.niuzj.minibrain.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.niuzj.corelibrary.base.BaseActivity;
import com.niuzj.corelibrary.utils.AppUtil;
import com.niuzj.corelibrary.utils.SpUtil;
import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.R;
import com.niuzj.minibrain.constant.OpenType;
import com.niuzj.minibrain.constant.SPConstant;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.ll_permission)
    LinearLayout mLlPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setDefaultOpenType();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    private void initView() {
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {

            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_inner:
                        SpUtil.saveInt(SettingActivity.this, SPConstant.OPEN_TYPE, OpenType.INNER);
                        break;

                    case R.id.radio_browser:
                        SpUtil.saveInt(SettingActivity.this, SPConstant.OPEN_TYPE, OpenType.BROWSER);
                        break;

                    case R.id.radio_tip:
                        SpUtil.saveInt(SettingActivity.this, SPConstant.OPEN_TYPE, OpenType.TIP);
                        break;
                }
            }
        });

        //版本号
        mTvVersion.setText(getString(R.string.version_info, AppUtil.getVersionName(this)));

        //权限
        List<String> permissions = Arrays.asList(getString(R.string.permission_internet));
        for (int i = 0; i < permissions.size(); i++) {
            String permission = permissions.get(i);
            TextView textView = new TextView(this);
            textView.setText(permission);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            textView.setTextColor(ContextCompat.getColor(this, R.color.core_library_default_text_color_grey));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            mLlPermission.addView(textView);
        }

    }

    private void setDefaultOpenType() {
        int openType = SpUtil.getInt(this, SPConstant.OPEN_TYPE, OpenType.INNER);
        switch (openType) {
            case OpenType.INNER:
                mRadioGroup.check(R.id.radio_inner);
                break;

            case OpenType.BROWSER:
                mRadioGroup.check(R.id.radio_browser);
                break;

            case OpenType.TIP:
                mRadioGroup.check(R.id.radio_tip);
                break;
        }
    }


}
