package com.example.asus.weclass;

/**
 * Created by ASUS on 2018/6/10.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public abstract class CommonAdapter<RequList> extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private List<RequList> mDatas;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    public CommonAdapter(Context context, int layoutId, List datas){
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = MyViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    public void onBindViewHolder(final MyViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
        if(mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
        }
    }


    public int getItemCount() {
        return mDatas.size();
    }
    public void convert(MyViewHolder holder, RequList reqlist) {}

    public interface OnItemClickListener {
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public void removeItem(int i) {
        mDatas.remove(i);
        notifyDataSetChanged();
    }
}

