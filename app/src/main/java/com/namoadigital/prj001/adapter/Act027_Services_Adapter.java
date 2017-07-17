package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;

import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act027_Services_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Act027_Services_Adapter(Context context, int resource, List<HMAux> source) {
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

        TextView tv_plc_pc_ps_sc_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_plc_pc_ps_sc_label);
        TextView tv_plc_pc_ps_sc_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_plc_pc_ps_sc_value);

        TextView tv_price_list_id_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_price_list_id_label);
        TextView tv_price_list_id_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_price_list_id_value);

        //TextView tv_price_list_desc_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_price_list_desc_label);
        TextView tv_price_list_desc_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_price_list_desc_value);

        TextView tv_pack_id_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_id_label);
        TextView tv_pack_id_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_id_value);

        //TextView tv_pack_desc_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_desc_label);
        TextView tv_pack_desc_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_pack_desc_value);

        //TextView tv_service_desc_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_desc_label);
        TextView service_desc_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_desc_value);

        //TextView tv_status_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_status_label);
        //TextView tv_qty_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_qty_label);

        TextView tv_status_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_status_value);
        TextView tv_qty_value = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_qty_value);

        //tv_plc_pc_ps_sc_label.setText("");
        tv_plc_pc_ps_sc_value.setText(item.get(SM_SO_ServiceDao.PRICE_LIST_CODE) + "." + item.get(SM_SO_ServiceDao.PACK_CODE) + "." + item.get(SM_SO_ServiceDao.PACK_SEQ) + "/" + item.get(SM_SO_ServiceDao.SERVICE_CODE));

        //tv_price_list_id_label.setText("");
        tv_price_list_id_value.setText(item.get(SM_SO_PackDao.PRICE_LIST_ID));

        //tv_price_list_desc_label.setText("");
        tv_price_list_desc_value.setText(item.get(SM_SO_PackDao.PRICE_LIST_DESC));

        //tv_pack_id_label.setText("");
        tv_pack_id_value.setText(item.get(SM_SO_PackDao.PACK_ID));

        //tv_pack_desc_label.setText("");
        tv_pack_desc_value.setText(item.get(SM_SO_PackDao.PACK_DESC));

        //tv_service_desc_label.setText("");
        service_desc_value.setText(item.get(SM_SO_ServiceDao.SERVICE_DESC));

        //tv_status_label.setText("");
        //tv_qty_label.setText("");

        tv_status_value.setText(item.get(SM_SO_ServiceDao.STATUS));
        tv_qty_value.setText(item.get(SM_SO_ServiceDao.QTY));

        return convertView;
    }
}
