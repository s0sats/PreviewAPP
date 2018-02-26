package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;

import java.util.ArrayList;

/**
 * Created by neomatrix on 2/26/18.
 */

public class Act037_Adapter_ap extends BaseAdapter {

    private Context context;
    private int resource_01;
    //
    private ArrayList<HMAux> data;
    private HMAux hmAux_Trans;

    public Act037_Adapter_ap(Context context, int resource_01, ArrayList<HMAux> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.data = data;
        this.hmAux_Trans = new HMAux();
    }

    public Act037_Adapter_ap(Context context, int resource_01, ArrayList<HMAux> data, HMAux hmAux_Trans) {
        this.context = context;
        this.resource_01 = resource_01;
        this.data = data;
        this.hmAux_Trans = hmAux_Trans;
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
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        HMAux item = data.get(position);

        try {

        } catch (Exception e) {
        }

        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        boolean results = false;

        switch (getItemViewType(position)) {

            default:
                results = true;
                break;
        }

        return results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Data Acess
        HMAux item = data.get(position);

        // View Settring
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {

                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;
                default:
                    break;
            }
        }
        //
        switch (getItemViewType(position)) {
            // Normal AP
            case 0:
                processNormalAP(item, convertView);
                break;
            default:
                break;
        }

        return convertView;
    }

    private void processNormalAP(HMAux item, View convertView) {
        TextView tv_cf_desc = (TextView) convertView.findViewById(R.id.act037_main_content_cell_ap_normal_tv_cf_desc);
        TextView tv_cf_type_desc = (TextView) convertView.findViewById(R.id.act037_main_content_cell_ap_normal_tv_cf_type_desc);
        //
        tv_cf_desc.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC));
        tv_cf_type_desc.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC));
    }
}
