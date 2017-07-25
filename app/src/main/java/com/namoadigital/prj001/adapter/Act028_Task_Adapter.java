package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act028_Task_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Act028_Task_Adapter(Context context, int resource, List<HMAux> source) {
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
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);

            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = source.get(position);

        TextView tv_task_label = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_task_tmp_label);
        TextView tv_task_value = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_task_tmp_value);
        TextView tv_task_status = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_task_status);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_ll_iv_icon);

        tv_task_label.setText("Task TMP");
        tv_task_value.setText(item.get("task_tmp"));
        tv_task_status.setText(item.get("task_status"));
        //
        if (item.get("task_user").equalsIgnoreCase(String.valueOf(ToolBox_Con.getPreference_User_Code(context)))){
            iv_icon.setVisibility(View.VISIBLE);
        } else {
            iv_icon.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
