package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Opc_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<HMAux> source;
    private int resource;


    public Act034_Opc_Adapter(Context context, ArrayList<HMAux> source, int resource) {
        this.context = context;
        this.source = source;
        this.resource = resource;
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource,parent,false);
        }
        //
        HMAux item = source.get(position);

        TextView tv_customer_desc = (TextView) convertView.findViewById(R.id.act034_opc_cell_tv_customer_desc);
        TextView tv_badge = (TextView) convertView.findViewById(R.id.act034_opc_cell_tv_badge);
        //
        tv_customer_desc.setText(item.get(EV_User_CustomerDao.CUSTOMER_NAME));
        //
        if(item.get("badge").equalsIgnoreCase("0")){
            tv_badge.setVisibility(View.INVISIBLE);
            tv_badge.setText("");
        }else{
            String qty_msg = item.get("badge");
            //
            if(qty_msg.length() == 1){
                qty_msg = " "+qty_msg +" ";
            }

            tv_badge.setText(qty_msg);
            tv_badge.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
