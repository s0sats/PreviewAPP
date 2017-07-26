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
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act027_Services_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Act027_Services_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act027_service_adapter"
        );

        loadTranslation();
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

        TextView tv_service_label = (TextView) convertView.findViewById(R.id.act027_services_content_cell_tv_service_ttl);

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

        tv_service_label.setText(hmAux_Trans.get("service_lbl"));

        tv_plc_pc_ps_sc_label.setText(hmAux_Trans.get("plc_pc_ps_sc_lbl"));
        tv_plc_pc_ps_sc_value.setText(item.get(SM_SO_ServiceDao.PRICE_LIST_CODE) + "." + item.get(SM_SO_ServiceDao.PACK_CODE) + "." + item.get(SM_SO_ServiceDao.PACK_SEQ) + "/" + item.get(SM_SO_ServiceDao.SERVICE_CODE));

        tv_price_list_id_label.setText(hmAux_Trans.get("price_list_id_lbl"));
        tv_price_list_id_value.setText(item.get(SM_SO_PackDao.PRICE_LIST_ID));

        tv_price_list_desc_value.setText(item.get(SM_SO_PackDao.PRICE_LIST_DESC));

        tv_pack_id_label.setText(hmAux_Trans.get("pack_id_lbl"));
        tv_pack_id_value.setText(item.get(SM_SO_PackDao.PACK_ID));

        tv_pack_desc_value.setText(item.get(SM_SO_PackDao.PACK_DESC));

        service_desc_value.setText(item.get(SM_SO_ServiceDao.SERVICE_DESC));

        tv_status_value.setText(item.get(SM_SO_ServiceDao.STATUS));
        tv_qty_value.setText(item.get(SM_SO_ServiceDao.QTY));

        return convertView;
    }

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("service_lbl");
        translateList.add("plc_pc_ps_sc_lbl");
        translateList.add("price_list_id_lbl");
        translateList.add("pack_id_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }


}
