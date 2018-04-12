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
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act043_Adapter_Services_Preview extends BaseAdapter {

    private Context context;
    private int resource_01;
    private ArrayList<HMAux> data;
    private HMAux hmAux_Trans;
    private OnInfoClickListner onInfoClickListner;

    public interface OnInfoClickListner{
        void OnInfoClick(HMAux service);
    }

    public void setOnInfoClickListner(OnInfoClickListner onInfoClickListner) {
        this.onInfoClickListner = onInfoClickListner;
    }

    public Act043_Adapter_Services_Preview(Context context, int resource_01, ArrayList<HMAux> data, HMAux hmAux_Trans) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource_01, parent, false);
        }

        final HMAux hmAux = data.get(position);
        //
        TextView tv_service_desc = (TextView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_tv_service_desc);
        //
        TextView tv_service_price = (TextView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_tv_service_price);
        //
        ImageView iv_info = (ImageView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_iv_info);
        //
        tv_service_desc.setText(hmAux.get(SM_SO_ServiceDao.SERVICE_ID)+" - "+hmAux.get(SM_SO_ServiceDao.SERVICE_DESC));
        //
        tv_service_price.setText(hmAux.get(SM_SO_ServiceDao.PRICE));
        //
        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onInfoClickListner != null){
                    onInfoClickListner.OnInfoClick(hmAux);
                }
            }
        });

        return convertView;
    }


}