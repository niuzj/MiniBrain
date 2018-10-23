package com.niuzj.minibrain.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.niuzj.corelibrary.base.BaseActivity;
import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.MineApplication;
import com.niuzj.minibrain.R;
import com.niuzj.minibrain.bean.TypeBean;
import com.niuzj.minibrain.bean.TypeBeanHelper;
import com.niuzj.minibrain.bean.TypeBean_;
import com.niuzj.minibrain.bean.UrlBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

public class AddActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypeBean typeBean = getIntent().getParcelableExtra("type_bean");
        initView(typeBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add;
    }

    private void initView(TypeBean typeBean) {
        if (typeBean != null) {
            etType.setText(typeBean.getType());
        }
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


    @OnClick({R.id.bt_add, R.id.bt_history})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:

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

                UrlBean urlBean = new UrlBean();
                urlBean.type = type;
                urlBean.title = title;
                urlBean.content = content;
                urlBean.url = url;
                urlBean.createTime = System.currentTimeMillis();
                urlBean.updateTime = System.currentTimeMillis();

                BoxStore boxStore = MineApplication.getInstance().getBoxStore();
                Box<UrlBean> urlBeanBox = boxStore.boxFor(UrlBean.class);
                urlBeanBox.put(urlBean);

                TypeBeanHelper.getInstance().addType(type);

                Toast.makeText(this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AddActivity.this.finish();
                    }
                }, 1000);

                break;

            case R.id.bt_history:

                showTypeListDialog();

                break;
        }
    }

    private void showTypeListDialog() {

        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        Box<TypeBean> typeBeanBox = boxStore.boxFor(TypeBean.class);
        QueryBuilder<TypeBean> queryBuilder = typeBeanBox.query();
        Query<TypeBean> typeBeanQuery = queryBuilder.orderDesc(TypeBean_.updateTime).build();
        final String[] strings = typeBeanQuery.property(TypeBean_.type)
                .findStrings();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_list_dialog_item, 0, strings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etType.setText(strings[which]);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

}
