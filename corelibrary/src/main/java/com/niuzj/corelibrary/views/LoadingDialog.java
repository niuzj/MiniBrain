package com.niuzj.corelibrary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.niuzj.corelibrary.R;

public class LoadingDialog extends AlertDialog {
    private SpinView mBar;

    public LoadingDialog(Context context) {
        super(context, R.style.core_library_loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_library_dialog_loading);

        //点击imageview外侧区域，动画不会消失
        setCanceledOnTouchOutside(false);

        mBar = (SpinView) findViewById(R.id.loading_progress);
        mBar.setAnimationSpeed(1);
    }

}
