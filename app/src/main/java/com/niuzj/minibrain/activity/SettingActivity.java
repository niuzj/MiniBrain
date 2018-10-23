package com.niuzj.minibrain.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioGroup;

import com.niuzj.corelibrary.base.BaseActivity;
import com.niuzj.corelibrary.utils.SpUtil;
import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.R;
import com.niuzj.minibrain.constant.OpenType;
import com.niuzj.minibrain.constant.SPConstant;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

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
