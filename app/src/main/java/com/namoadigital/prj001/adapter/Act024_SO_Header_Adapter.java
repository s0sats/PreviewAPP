package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.namoadigital.prj001.model.SM_SO;

import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class Act024_SO_Header_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<SM_SO> source;

    public Act024_SO_Header_Adapter(Context context, int resource, List<SM_SO> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);
        }



        return convertView;
    }
}
