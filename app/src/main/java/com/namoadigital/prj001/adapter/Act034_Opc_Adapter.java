package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.sql.Sql_Act034_001;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Opc_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<HMAux> source;
    private String selected_customer;
    private int resource;


    public Act034_Opc_Adapter(Context context, ArrayList<HMAux> source, long selected_customer, int resource) {
        this.context = context;
        this.source = source;
        this.selected_customer = String.valueOf(selected_customer);
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

    public void setSelected(String selected_customer){
        this.selected_customer = selected_customer;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource,parent,false);
        }
        //
        HMAux item = source.get(position);
        LinearLayout ll_background = (LinearLayout) convertView.findViewById(R.id.act034_opc_cell_ll_background);
        TextView tv_customer_desc = (TextView) convertView.findViewById(R.id.act034_opc_cell_tv_customer_desc);
        TextView tv_badge = (TextView) convertView.findViewById(R.id.act034_opc_cell_tv_badge);
        ImageView iv_selected = (ImageView) convertView.findViewById(R.id.act034_opc_cell_iv_selected);
        //
        tv_customer_desc.setText(item.get(EV_User_CustomerDao.CUSTOMER_NAME));
        //
        if(item.get(CH_RoomDao.CUSTOMER_CODE).equalsIgnoreCase(selected_customer)){
            ll_background.setBackground(context.getDrawable(R.drawable.chat_customer_drawer_selected));
            tv_customer_desc.setTextColor(context.getResources().getColor(R.color.namoa_color_light_green3));
            tv_customer_desc.setTypeface(tv_customer_desc.getTypeface(), Typeface.BOLD);
        }else{
            ll_background.setBackground(context.getDrawable(R.drawable.lib_custom_cell_bg));
            tv_customer_desc.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue));
            tv_customer_desc.setTypeface(tv_customer_desc.getTypeface(), Typeface.NORMAL);
        }
        //
        if(item.get(Sql_Act034_001.MSG_QTY).equalsIgnoreCase("0")){
            tv_badge.setVisibility(View.INVISIBLE);
            tv_badge.setText("");
        }else{
            String qty_msg = item.get(Sql_Act034_001.MSG_QTY);
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
