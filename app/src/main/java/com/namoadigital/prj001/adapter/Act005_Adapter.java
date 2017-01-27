package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class Act005_Adapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<HashMap<String,String>> source;

    public Act005_Adapter(Context context, int resource, List<HMAux> source) {
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
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);

        }

        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.menu_llChecklist);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.menu_ivChecklist);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.menu_tvChecklist);

        HashMap<String,String> item = source.get(position);

        ivIcon.setImageDrawable(context.getResources().getDrawable(Integer.valueOf(item.get("icon"))));
        tvTitle.setText(item.get("title"));

        return convertView;
    }
}
