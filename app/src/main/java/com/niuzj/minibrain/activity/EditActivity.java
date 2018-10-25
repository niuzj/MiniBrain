package com.niuzj.minibrain.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.niuzj.corelibrary.base.BaseActivity;
import com.niuzj.corelibrary.views.DefaultDialog;
import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.MineApplication;
import com.niuzj.minibrain.R;
import com.niuzj.minibrain.bean.TypeBeanHelper;
import com.niuzj.minibrain.bean.UrlBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class EditActivity extends BaseActivity {

    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;

    //要编辑的对象
    UrlBean mUrlBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit;
    }

    private void initData() {
        mUrlBean = getIntent().getParcelableExtra("url_bean");

        etType.setText(mUrlBean.type);
        etTitle.setText(mUrlBean.title);
        etContent.setText(mUrlBean.content);
        etUrl.setText(mUrlBean.url);
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {

            }
        });

    }

    @OnClick(R.id.bt_edit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_edit:

                String type = etType.getText().toString();
                if (TextUtils.isEmpty(type)) {
                    Toast.makeText(this, getString(R.string.type_hint), Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = etTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(this, getString(R.string.title_hint), Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = etContent.getText().toString();
                String url = etUrl.getText().toString();
                if (TextUtils.isEmpty(content) && TextUtils.isEmpty(url)) {
                    Toast.makeText(this, getString(R.string.content_url_cannot_null), Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean typeChanged = false;
                if (!TextUtils.equals(mUrlBean.type, type)) {
                    //如果类型发生变化，需要操作类型数据库
                    TypeBeanHelper.getInstance().reduceType(mUrlBean.type);
                    TypeBeanHelper.getInstance().addType(type);
                    typeChanged = true;
                }

                String oldType = mUrlBean.type;
                mUrlBean.type = type;
                mUrlBean.title = title;
                mUrlBean.content = content;
                mUrlBean.url = url;
                mUrlBean.updateTime = System.currentTimeMillis();

                BoxStore boxStore = MineApplication.getInstance().getBoxStore();
                Box<UrlBean> urlBeanBox = boxStore.boxFor(UrlBean.class);
                urlBeanBox.put(mUrlBean);

                if (typeChanged) {

                    new DefaultDialog.Builder()
                            .title(getString(R.string.edit_success))
                            .rightTitle(getString(R.string.confirm))
                            .content(getString(R.string.type_change_tip, type, oldType))
                            .clickListener(new DefaultDialog.OnDialogClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {
                                    EditActivity.this.finish();
                                }
                            })
                            .build(this)
                            .show();

                } else {

                    Toast.makeText(this, getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EditActivity.this.finish();
                        }
                    }, 500);

                }


                break;
        }
    }

}
