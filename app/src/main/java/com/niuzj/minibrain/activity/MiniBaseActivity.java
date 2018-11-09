package com.niuzj.minibrain.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.niuzj.corelibrary.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class MiniBaseActivity extends BaseActivity {

    Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
