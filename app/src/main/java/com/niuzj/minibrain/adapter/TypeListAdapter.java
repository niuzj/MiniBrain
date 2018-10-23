package com.niuzj.minibrain.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuzj.minibrain.R;
import com.niuzj.minibrain.bean.TypeBean;
import com.niuzj.minibrain.interfaces.OnItemClickListener;
import com.niuzj.minibrain.interfaces.OnItemLongClickListener;

import java.util.List;

public class TypeListAdapter extends RecyclerView.Adapter<TypeListAdapter.TypeViewHolder> {

    private Context mContext;
    private List<TypeBean> mTypeBeanList;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public TypeListAdapter(Context context, List<TypeBean> typeBeanList) {
        mContext = context;
        mTypeBeanList = typeBeanList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_type_list, null);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder typeViewHolder, final int position) {
        TypeBean typeBean = mTypeBeanList.get(position);
        if (typeBean.isAddFlag()) {
            typeViewHolder.mTvType.setText(mContext.getString(R.string.add));
            typeViewHolder.mTvCount.setVisibility(View.GONE);
        } else {
            typeViewHolder.mTvType.setText(typeBean.getType());
            typeViewHolder.mTvCount.setVisibility(View.VISIBLE);
            typeViewHolder.mTvCount.setText(String.valueOf(typeBean.getCount()));
        }
        typeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        typeViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTypeBeanList == null ? 0 : mTypeBeanList.size();
    }

    public class TypeViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvType;
        public TextView mTvCount;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvType = itemView.findViewById(R.id.tv_type);
            mTvCount = itemView.findViewById(R.id.tv_count);
        }
    }

}
