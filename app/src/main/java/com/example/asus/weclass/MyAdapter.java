package com.example.asus.weclass;

/**
 * Created by ASUS on 2018/6/10.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<AppointmentUsrData> mData;

    public MyAdapter() {}

    public MyAdapter(LinkedList<AppointmentUsrData> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return  null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.applist_model,parent,false);
            holder = new ViewHolder1();
            holder.status_img = (ImageView) convertView.findViewById(R.id.status);
            holder.classroom_txt = (TextView) convertView.findViewById(R.id.classroom);
            holder.application_date_txt = (TextView) convertView.findViewById(R.id.application_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder1) convertView.getTag();
        }
        holder.status_img.setImageResource(mData.get(position).getStatusImg());
        holder.classroom_txt.setText(mData.get(position).getRoomName());
        holder.application_date_txt.setText(mData.get(position).getBookDate());
        return convertView;
    }

    private class ViewHolder1 {
        ImageView status_img;
        TextView classroom_txt;
        TextView application_date_txt;
    }

    public void add(AppointmentUsrData data) {
        if (mData == null) {
            mData = new LinkedList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    public void remove(AppointmentUsrData data) {
        if (mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mData != null) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void update(int position, AppointmentUsrData data) {
        if (mData != null) {
            mData.set(position,data);
        }
        notifyDataSetChanged();
    }

    public AppointmentUsrData getData(int position) {
        if (mData != null) {
            return mData.get(position);
        }
        return null;
    }
}
