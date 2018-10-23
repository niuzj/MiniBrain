package com.niuzj.minibrain.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.niuzj.corelibrary.utils.DensityUtil;
import com.niuzj.minibrain.R;

public class TypeAdapterItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount = 2;
    private Context mContext;

    public TypeAdapterItemDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int left;
        int right;
        if (childAdapterPosition % spanCount == 0) {
            //左边
            left = DensityUtil.dip2px(16, mContext);
            right = DensityUtil.dip2px(8, mContext);
        } else {
            //右边
            left = DensityUtil.dip2px(8, mContext);
            right = DensityUtil.dip2px(16, mContext);
        }
        outRect.set(left, DensityUtil.dip2px(8, mContext), right, 0);
    }
}
