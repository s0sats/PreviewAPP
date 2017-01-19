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
import com.namoadigital.prj001.dao.EV_User_CustomerDao;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 19/01/2017.
 */

public class Lib_Custom_Cell_Adapter extends BaseAdapter  {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source) {
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

        if (convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);
        }

        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.lib_custom_cell_ll_background);
        //
        TextView tvItem = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_item);
        //
        TextView tvSubItem = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_sub_item);
        //
        ImageView iv001 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_001);
        //
        ImageView iv002 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_002);

        HMAux item = source.get(position);

        tvItem.setText(item.get(EV_User_CustomerDao.CUSTOMER_NAME));

        tvSubItem.setVisibility(View.GONE);

        if(item.get(EV_User_CustomerDao.BLOCKED).equals(0)){
            llBackground.setBackgroundColor(context.getResources().getColor(R.color.bootstrap_brand_warning));
        }

        if(!item.get(EV_User_CustomerDao.PENDING).equals(0)){
            iv001.setVisibility(View.VISIBLE);
            iv001.setColorFilter(R.color.bootstrap_brand_primary);
        }

        if(item.get(EV_User_CustomerDao.SESSION_APP).trim().length() > 0){
            iv002.setVisibility(View.VISIBLE);
            iv002.setColorFilter(R.color.bootstrap_brand_success);
        }  else {
            iv002.setVisibility(View.VISIBLE);
            iv002.setColorFilter(R.color.bootstrap_brand_danger);
        }

        return convertView;
    }
}
