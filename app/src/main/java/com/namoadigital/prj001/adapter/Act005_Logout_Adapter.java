package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;

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

        CheckBox chk_001 = (CheckBox) convertView.findViewById(R.id.act005_dialog_logout_chk_customer);

        chk_001.setTag(item.get(EV_User_CustomerDao.CUSTOMER_CODE));
        chk_001.setText(item.get(EV_User_CustomerDao.CUSTOMER_NAME));

        chk_001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }
}
