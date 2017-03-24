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
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_004;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 */

public class Act005_Logout_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;


    public Act005_Logout_Adapter(Context context, List<HMAux> source) {
        this.context = context;
        this.resource = R.layout.act005_dialog_logout_cell;
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
        //Resgata HmAux com as informações
        HMAux item = source.get(position);

        ImageView iV001 = (ImageView) convertView.findViewById(R.id.act005_dialog_logout_iv_chk);
        iV001.setColorFilter(context.getResources().getColor(R.color.namoa_color_dark_blue));

        TextView textView = (TextView) convertView.findViewById(R.id.act005_dialog_logout_tv_customer);

        textView.setText(item.get(EV_User_CustomerDao.CUSTOMER_NAME));

        if (item.get(EV_User_Customer_Sql_004.KEY_LOGOUT).equals("1")){
            iV001.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
        }else{
            iV001.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp));
        }

        return convertView;
    }
}
