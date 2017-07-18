package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;

import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act028_Exec_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<SM_SO_Service_Exec> source;

    public Act028_Exec_Adapter(Context context, int resource, List<SM_SO_Service_Exec> source) {
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

        SM_SO_Service_Exec item = source.get(position);

        TextView tv_exec_tmp_label = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_tmp_label);
        TextView tv_exec_tmp_value = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_tmp_value);
        TextView tv_exec_status = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_status);

        tv_exec_tmp_label.setText("Exec TMP");
        tv_exec_tmp_value.setText(String.valueOf(item.getExec_tmp()));
        tv_exec_status.setText(item.getStatus());

        return convertView;
    }
}
