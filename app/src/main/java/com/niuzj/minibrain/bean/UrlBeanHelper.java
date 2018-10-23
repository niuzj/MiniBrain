package com.niuzj.minibrain.bean;

import android.util.Log;

import com.niuzj.minibrain.MineApplication;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataTransformer;

public class UrlBeanHelper {

    private static UrlBeanHelper sInstance;

    private UrlBeanHelper() {

    }

    public static UrlBeanHelper getInstance() {
        if (sInstance == null) {
            sInstance = new UrlBeanHelper();
        }
        return sInstance;
    }

    public void printLn(List<UrlBean> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Log.e("UrlBean", list.get(i).toString());
            }
        }
    }

    public void deleteUrlByType(String type) {
        BoxStore boxStore = MineApplication.getInstance().getBoxStore();
        final Box<UrlBean> urlBeanBox = boxStore.boxFor(UrlBean.class);
        QueryBuilder<UrlBean> queryBuilder = urlBeanBox.query();
        Query<UrlBean> urlBeanQuery = queryBuilder.equal(UrlBean_.type, type).build();
        urlBeanQuery.subscribe()
                .single()
                .transform(new DataTransformer<List<UrlBean>, List<Long>>() {
                    @Override
                    public List<Long> transform(List<UrlBean> source) throws Exception {
                        List<Long> ids = null;
                        if (source != null && source.size() > 0) {
                            ids = new ArrayList<>();
                            for (int i = 0; i < source.size(); i++) {
                                ids.add(source.get(i).id);
                            }
                        }
                        return ids;
                    }
                })
                .observer(new DataObserver<List<Long>>() {
                    @Override
                    public void onData(List<Long> data) {
                        urlBeanBox.removeByKeys(data);
                    }
                });
    }

}
