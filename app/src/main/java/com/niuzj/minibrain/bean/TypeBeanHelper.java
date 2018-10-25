package com.niuzj.minibrain.bean;

import android.util.Log;

import com.niuzj.minibrain.MineApplication;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;
import io.objectbox.reactive.DataTransformer;
import io.objectbox.reactive.Scheduler;

public class TypeBeanHelper {

    private static TypeBeanHelper sInstance;

    private TypeBeanHelper() {
    }

    public static TypeBeanHelper getInstance() {
        if (sInstance == null) {
            sInstance = new TypeBeanHelper();
        }
        return sInstance;
    }

    public void printLn(List<TypeBean> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Log.e("TypeBean", list.get(i).toString());
            }
        }
    }


    public void addType(String type) {
        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        Box<TypeBean> typeBeanBox = boxStore.boxFor(TypeBean.class);
        QueryBuilder<TypeBean> queryBuilder = typeBeanBox.query();
        Query<TypeBean> query = queryBuilder.equal(TypeBean_.type, type).build();
        TypeBean unique = query.findUnique();
        if (unique == null) {
            unique = new TypeBean();
            unique.setCount(1);
            unique.setType(type);
            unique.createTime = System.currentTimeMillis();
            unique.updateTime = System.currentTimeMillis();
        } else {
            unique.setCount(unique.getCount() + 1);
            unique.updateTime = System.currentTimeMillis();
        }
        typeBeanBox.put(unique);
    }

    /**
     * 减少该类型下的数量
     *
     * @param type
     */
    public void reduceType(String type) {
        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        Box<TypeBean> typeBeanBox = boxStore.boxFor(TypeBean.class);
        QueryBuilder<TypeBean> queryBuilder = typeBeanBox.query();
        Query<TypeBean> query = queryBuilder.equal(TypeBean_.type, type).build();
        TypeBean unique = query.findUnique();
        unique.setCount(unique.getCount() - 1);
        unique.updateTime = System.currentTimeMillis();
        typeBeanBox.put(unique);
    }

    /**
     * 删除该类型
     *
     * @param type
     */
    public void deleteType(String type) {
        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        final Box<TypeBean> typeBeanBox = boxStore.boxFor(TypeBean.class);
        QueryBuilder<TypeBean> queryBuilder = typeBeanBox.query();
        Query<TypeBean> query = queryBuilder.equal(TypeBean_.type, type).build();
        TypeBean unique = query.findUnique();
        typeBeanBox.remove(unique);
    }


}
