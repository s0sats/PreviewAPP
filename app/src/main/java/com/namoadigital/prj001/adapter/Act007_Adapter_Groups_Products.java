package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.List;

/**
 * Created by neomatrix on 02/02/17.
 */

public class Act007_Adapter_Groups_Products extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> data;

    public Act007_Adapter_Groups_Products(Context context, int resource, List<HMAux> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong((data.get(position).get("code")));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }

        TextView tv_texto_01 = (TextView)
                convertView.findViewById(R.id.act007_main_content_cell_01_tv_texto_01);

        TextView tv_texto_02 = (TextView)
                convertView.findViewById(R.id.act007_main_content_cell_01_tv_texto_02);

        LinearLayout ll_fundo = (LinearLayout)
                convertView.findViewById(R.id.act007_main_content_cell_01_ll_fundo);

        HMAux item = data.get(position);

        if (item.get("type").equalsIgnoreCase("group")) {
            tv_texto_01.setText(item.get("full_desc"));
            tv_texto_01.setTextColor(context.getResources().getColor(R.color.padrao_WHITE));
            tv_texto_02.setVisibility(View.GONE);
            //
            ll_fundo.setBackground(context.getResources().getDrawable(R.drawable.btn_primary_layout));
        } else {
            tv_texto_01.setText(item.get("full_desc"));
            tv_texto_01.setTextColor(context.getResources().getColor(R.color.padrao_BACK));
            tv_texto_02.setText(item.get("desc"));
            tv_texto_02.setVisibility(View.VISIBLE);
            //
            ll_fundo.setBackground(null);
        }

        return convertView;
    }
}
