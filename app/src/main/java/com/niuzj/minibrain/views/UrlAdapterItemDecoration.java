package com.niuzj.minibrain.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.niuzj.corelibrary.utils.DensityUtil;

public class UrlAdapterItemDecoration extends RecyclerView.ItemDecoration {

    int top;
    int left;
    int right;
    int bottom;

    public UrlAdapterItemDecoration(Context context) {
        top = DensityUtil.dip2px(8, context);
        left = right = DensityUtil.dip2px(16, context);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int childAdapterPosition = parent.getChildAdapterPosition(view);

        if (childAdapterPosition != childCount - 1) {
            bottom = 0;
        } else {
            bottom = DensityUtil.dip2px(8, parent.getContext());
        }
        outRect.set(left, top, right, bottom);
    }
}
