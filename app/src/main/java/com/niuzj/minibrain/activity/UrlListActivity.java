package com.niuzj.minibrain.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.niuzj.corelibrary.base.BaseActivity;
import com.niuzj.corelibrary.utils.JsonUtil;
import com.niuzj.corelibrary.utils.SpUtil;
import com.niuzj.corelibrary.views.DefaultDialog;
import com.niuzj.corelibrary.views.LoadingDialog;
import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.MineApplication;
import com.niuzj.minibrain.R;
import com.niuzj.minibrain.adapter.UrlListAdapter;
import com.niuzj.minibrain.bean.TypeBean;
import com.niuzj.minibrain.bean.TypeBeanHelper;
import com.niuzj.minibrain.bean.TypeBean_;
import com.niuzj.minibrain.bean.UrlBean;
import com.niuzj.minibrain.bean.UrlBeanHelper;
import com.niuzj.minibrain.bean.UrlBean_;
import com.niuzj.minibrain.constant.OpenType;
import com.niuzj.minibrain.constant.SPConstant;
import com.niuzj.minibrain.interfaces.OnItemClickListener;
import com.niuzj.minibrain.interfaces.OnItemLongClickListener;
import com.niuzj.minibrain.views.UrlAdapterItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;
import io.objectbox.reactive.ErrorObserver;

public class UrlListActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;

    TypeBean mTypeBean;
    UrlListAdapter mUrlListAdapter;
    List<UrlBean> mUrlBeanList;
    int mEditPosition;

    DataSubscriptionList mDataSubscriptionList = new DataSubscriptionList();
    AlertDialog mAlertDialog;

    private static final int EDIT_REQUEST_CODE = 1;
    public static final int EDIT_RESULT_TYPE_CHANGED = 2;
    public static final int EDIT_RESULT_OK = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        getListData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_url_list;
    }

    private void initData() {
        mTypeBean = getIntent().getParcelableExtra("type_bean");
        mUrlBeanList = new ArrayList<>();
    }

    private void initView() {

        mTitleBar.setTitle(mTypeBean.getType());
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {
                Intent intent = new Intent(UrlListActivity.this, AddActivity.class);
                intent.putExtra("type_bean", mTypeBean);
                startActivity(intent);
            }
        });
        mUrlListAdapter = new UrlListAdapter(mUrlBeanList, this);
        mUrlListAdapter.setOnItemClickListener(this);
        mUrlListAdapter.setOnItemLongClickListener(this);
        mRecyclerView.addItemDecoration(new UrlAdapterItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mUrlListAdapter);

    }

    private void getListData() {
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        Box<UrlBean> urlBeanBox = boxStore.boxFor(UrlBean.class);
        QueryBuilder<UrlBean> queryBuilder = urlBeanBox.query();
        Query<UrlBean> query = queryBuilder
                .equal(UrlBean_.type, mTypeBean.getType())
                .orderDesc(UrlBean_.updateTime)
                .build();
        query.subscribe(mDataSubscriptionList)
                .on(AndroidScheduler.mainThread())
                .onError(new ErrorObserver() {
                    @Override
                    public void onError(Throwable th) {
                        loadingDialog.dismiss();
                    }
                })
                .observer(new DataObserver<List<UrlBean>>() {
                    @Override
                    public void onData(List<UrlBean> data) {
                        loadingDialog.dismiss();
                        mUrlBeanList.clear();
                        mUrlBeanList.addAll(data);
                        mUrlListAdapter.notifyDataSetChanged();

                        if (mUrlBeanList.size() <= 0) {
                            finish();
                        }

                        UrlBeanHelper.getInstance().printLn(mUrlBeanList);

                    }
                });

    }

    @Override
    protected void onDestroy() {
        mDataSubscriptionList.cancel();
        super.onDestroy();
    }

    @Override
    public void onItemClick(int position) {
        UrlBean urlBean = mUrlBeanList.get(position);
        if (TextUtils.isEmpty(urlBean.url)) {

            new DefaultDialog.Builder()
                    .content(urlBean.content)
                    .build(this)
                    .show();

        } else {
            boolean hasOpen = SpUtil.getBoolean(this, SPConstant.HAS_OPEN, false);
            if (hasOpen) {
                int openType = SpUtil.getInt(this, SPConstant.OPEN_TYPE, OpenType.INNER);
                if (openType == OpenType.INNER) {

                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url_bean", urlBean);
                    startActivity(intent);

                } else if (openType == OpenType.BROWSER) {

                    Uri uri = Uri.parse(urlBean.url);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);

                } else if (openType == OpenType.TIP) {

                    showTipDialog(urlBean);

                }

            } else {
                showTipDialog(urlBean);
            }
        }
    }

    /**
     * 显示弹窗，提示用户选择打开方式
     *
     * @param urlBean
     */
    private void showTipDialog(final UrlBean urlBean) {
        new DefaultDialog.Builder()
                .content(getString(R.string.performance_better_tip))
                .leftTitle(getString(R.string.cancel))
                .rightTitle(getString(R.string.confirm))
                .clickListener(new DefaultDialog.OnDialogClickListener() {
                    @Override
                    public void clickLeft() {
                        SpUtil.saveBoolean(UrlListActivity.this, SPConstant.HAS_OPEN, true);
                        Intent intent = new Intent(UrlListActivity.this, WebViewActivity.class);
                        intent.putExtra("url_bean", urlBean);
                        startActivity(intent);
                    }

                    @Override
                    public void clickRight() {
                        SpUtil.saveBoolean(UrlListActivity.this, SPConstant.HAS_OPEN, true);
                        //跳转到系统浏览器
                        Uri uri = Uri.parse(urlBean.url);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .build(this).show();
    }

    @Override
    public void onItemLongClick(final int position) {
        final UrlBean urlBean = mUrlBeanList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_list_dialog_item, 0, Arrays.asList(getString(R.string.edit), getString(R.string.delete), getString(R.string.cancel))), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        //编辑
                        mEditPosition = position;
                        Intent intent = new Intent(UrlListActivity.this, EditActivity.class);
                        intent.putExtra("url_bean", urlBean);
                        startActivity(intent);
                        break;

                    case 1:
                        //删除
                        Box<UrlBean> urlBeanBox = MineApplication.getInstance().getBoxStore().boxFor(UrlBean.class);
                        urlBeanBox.remove(urlBean);

                        //修改该类型数量
                        TypeBeanHelper.getInstance().reduceType(urlBean.type);

                        //从列表中删除
                        mUrlBeanList.remove(urlBean);

                        if (mUrlBeanList.size() <= 0) {
                            UrlListActivity.this.finish();
                        } else {
                            mUrlListAdapter.notifyDataSetChanged();
                        }

                        break;

                    case 2:
                        //删除
                        break;

                }

                mAlertDialog.dismiss();
            }
        });

        mAlertDialog = builder.create();
        ListView listView = mAlertDialog.getListView();
        listView.setDivider(new ColorDrawable(ContextCompat.getColor(this, R.color.core_library_default_line_color)));
        mAlertDialog.show();
    }
}
