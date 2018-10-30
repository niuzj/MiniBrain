package com.niuzj.minibrain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.niuzj.corelibrary.base.BaseActivity;
import com.niuzj.corelibrary.utils.JsonUtil;
import com.niuzj.corelibrary.utils.SpUtil;
import com.niuzj.corelibrary.views.DefaultDialog;
import com.niuzj.corelibrary.views.LoadingDialog;
import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.activity.AddActivity;
import com.niuzj.minibrain.activity.SettingActivity;
import com.niuzj.minibrain.activity.UrlListActivity;
import com.niuzj.minibrain.adapter.TypeListAdapter;
import com.niuzj.minibrain.bean.TypeBean;
import com.niuzj.minibrain.bean.TypeBeanHelper;
import com.niuzj.minibrain.bean.TypeBean_;
import com.niuzj.minibrain.bean.UrlBean;
import com.niuzj.minibrain.bean.UrlBeanHelper;
import com.niuzj.minibrain.constant.SPConstant;
import com.niuzj.minibrain.interfaces.OnItemClickListener;
import com.niuzj.minibrain.interfaces.OnItemLongClickListener;
import com.niuzj.minibrain.views.TypeAdapterItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;
import io.objectbox.reactive.ErrorObserver;

public class MainActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener {

    RecyclerView mRecyclerView;
    TitleBar mTitleBar;
    TypeListAdapter mAdapter;
    List<TypeBean> mList;
    DataSubscriptionList mDataSubscriptionList = new DataSubscriptionList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataAndView();
        refreshList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initDataAndView() {

        mList = new ArrayList<>();
        mAdapter = new TypeListAdapter(this, mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new TypeAdapterItemDecoration(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);

        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void refreshList() {

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        Box<TypeBean> typeBeanBox = boxStore.boxFor(TypeBean.class);
        QueryBuilder<TypeBean> queryBuilder = typeBeanBox.query();
        Query<TypeBean> query = queryBuilder.greater(TypeBean_.count, 0)
                .orderDesc(TypeBean_.updateTime)
                .greater(TypeBean_.count, 0)
                .build();
        query.subscribe(mDataSubscriptionList)
                .on(AndroidScheduler.mainThread())
                .onError(new ErrorObserver() {
                    @Override
                    public void onError(Throwable th) {
                        loadingDialog.dismiss();
                        TypeBean addType = new TypeBean();
                        addType.setType(getString(R.string.add));
                        addType.setAddFlag(true);
                        mList.clear();
                        mList.add(addType);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .observer(new DataObserver<List<TypeBean>>() {
                    @Override
                    public void onData(List<TypeBean> data) {
                        loadingDialog.dismiss();
                        mList.clear();
                        TypeBean addType = new TypeBean();
                        addType.setAddFlag(true);
                        addType.setType(getString(R.string.add));
                        if (data != null && data.size() > 0) {
                            mList.addAll(data);
                        }
                        mList.add(addType);
                        mAdapter.notifyDataSetChanged();

                        TypeBeanHelper.getInstance().printLn(mList);

                    }
                });

    }

    @Override
    public void onItemClick(int position) {
        TypeBean typeBean = mList.get(position);
        if (typeBean.isAddFlag()) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, UrlListActivity.class);
            intent.putExtra("type_bean", typeBean);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        final TypeBean typeBean = mList.get(position);
        if (!typeBean.isAddFlag()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_list_dialog_item, 0, Arrays.asList(getString(R.string.add), getString(R.string.delete), getString(R.string.cancel))), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //添加
                            Intent intent = new Intent(MainActivity.this, AddActivity.class);
                            UrlBean urlBean = new UrlBean();
                            urlBean.type = typeBean.getType();
                            intent.putExtra("url_bean", urlBean);
                            startActivity(intent);
                            break;

                        case 1:
                            //删除
                            showDeleteDialog(typeBean);
                            break;

                        case 2:
                            break;
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * 删除确认弹窗
     *
     * @param typeBean
     */
    private void showDeleteDialog(final TypeBean typeBean) {
        new DefaultDialog.Builder()
                .content(getString(R.string.delete_type_tip, typeBean.getType(), typeBean.getCount()))
                .leftTitle(getString(R.string.cancel))
                .rightTitle(getString(R.string.confirm))
                .clickListener(new DefaultDialog.OnDialogClickListener() {
                    @Override
                    public void clickLeft() {

                    }

                    @Override
                    public void clickRight() {
                        TypeBeanHelper.getInstance().deleteType(typeBean.getType());
                        UrlBeanHelper.getInstance().deleteUrlByType(typeBean.getType());
                    }
                })
                .build(this)
                .show();

    }

    @Override
    protected void onDestroy() {
        mDataSubscriptionList.cancel();
        super.onDestroy();
    }
}
