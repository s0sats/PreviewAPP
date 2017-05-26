package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 */

public class Local_Data_List_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Local_Data_List_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                                        context,
                                        Constant.APP_MODULE,
                                        "local_data_list_adapter"
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

        if(convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);

        }

        //Resgata HmAux com as informações
        HMAux item = source.get(position);

        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.local_data_list_cell_01_ll_bg);
        //
        ArrayList<TextView> tv_list = new ArrayList<>();
        //
        TextView tv_date_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_label);
        TextView tv_date_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date);

        tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));

        tv_list.add(tv_date_lbl);
        tv_list.add(tv_date_val);
        //
        TextView tv_hour_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_hour_label);
        TextView tv_hour_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_hour);

        tv_hour_lbl.setVisibility(View.GONE);
        tv_hour_val.setVisibility(View.GONE);

        tv_hour_lbl.setText(hmAux_Trans.get("lbl_hour")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));

        tv_list.add(tv_hour_lbl);
        tv_list.add(tv_hour_val);
        //
        TextView tv_product_ttl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_ttl);

        tv_product_ttl.setText(hmAux_Trans.get("ttl_product"));

        tv_list.add(tv_product_ttl);
        //
        TextView tv_code_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_code_label);
        TextView tv_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_code_val);

        tv_code_lbl.setText(hmAux_Trans.get("lbl_product_code")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));

        tv_list.add(tv_code_lbl);
        tv_list.add(tv_code_val);
        //
        TextView tv_id_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_label);
        TextView tv_id_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_val);

        tv_id_lbl.setText(hmAux_Trans.get("lbl_product_id")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));

        tv_list.add(tv_id_lbl);
        tv_list.add(tv_id_val);
        //
        TextView tv_product_desc_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_desc_lbl);
        TextView tv_product_desc_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_desc_val);

        tv_product_desc_lbl.setText(hmAux_Trans.get("lbl_product_desc")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));

        tv_list.add(tv_product_desc_lbl);
        tv_list.add(tv_product_desc_val);
        //
        //
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_serial_label);
        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_serial_val);

        tv_serial_lbl.setText(hmAux_Trans.get("lbl_serial_id")+" "+item.get(GE_Custom_Form_LocalDao.SERIAL_ID));

        if(item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0){
            tv_serial_lbl.setVisibility(View.GONE);
            tv_serial_val.setVisibility(View.GONE);
        }

        tv_list.add(tv_code_lbl);
        tv_list.add(tv_code_val);
        //
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_ttl);

        tv_form_ttl.setText(hmAux_Trans.get("ttl_form"));

        tv_list.add(tv_form_ttl);
        //
        TextView tv_type_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_type_label);
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_type_val);

        tv_type_lbl.setText(hmAux_Trans.get("lbl_type")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));

        tv_list.add(tv_type_lbl);
        tv_list.add(tv_type_val);
        //
        TextView tv_form_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_label);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_val);

        tv_form_lbl.setText(hmAux_Trans.get("lbl_form")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));

        tv_list.add(tv_form_lbl);
        tv_list.add(tv_form_val);
        //
        TextView tv_data_serv_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_data_serv_lbl);
        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_data_serv_val);

        tv_list.add(tv_data_serv_lbl);
        tv_list.add(tv_data_serv_val);

        TextView tv_date_schedule_start_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_schedule_start_lbl);
        TextView tv_date_schedule_start_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_schedule_start_val);

        tv_list.add(tv_date_schedule_start_lbl);
        tv_list.add(tv_date_schedule_start_val);

        if(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA_SERV).trim().length() > 0){
            tv_data_serv_lbl.setVisibility(View.VISIBLE);
            tv_data_serv_lbl.setText(hmAux_Trans.get("lbl_data_serv")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA_SERV));
            //
            tv_date_schedule_start_lbl.setVisibility(View.VISIBLE);
            tv_date_schedule_start_lbl.setText(hmAux_Trans.get("lbl_date_schedule_start")+" "+item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));

        }else{
            tv_data_serv_lbl.setVisibility(View.GONE);
            tv_data_serv_lbl.setText("");
            //
            tv_date_schedule_start_lbl.setVisibility(View.GONE);
            tv_date_schedule_start_lbl.setText("");
        }


        //
        Drawable llDrawable = null;

//        tvItem.setText(
//                item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE) +
//                " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
//        tvItem2.setText(
//                item.get(
//                        GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC)
//        );
//        tvItem3.setText("# " +
//                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA)
//        );

        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)){

            case Constant.CUSTOM_FORM_STATUS_IN_PROCESSING:
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_in_processing_states);
                llBackground.setBackground(llDrawable);
                break;
            //
            case Constant.CUSTOM_FORM_STATUS_FINALIZED:
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_6_states);
                llBackground.setBackground(llDrawable);
//                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
//                tvItem2.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
//                tvItem3.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));

                break;
            case Constant.CUSTOM_FORM_STATUS_SENT:
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_sent_states);
                llBackground.setBackground(llDrawable);
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_END));
//                tvItem.setTextColor(context.getResources().getColorStateList(namoa_color_dark_blue));
//                tvItem2.setTextColor(context.getResources().getColorStateList(namoa_color_dark_blue));
//                tvItem3.setTextColor(context.getResources().getColorStateList(namoa_color_dark_blue));

                break;
            case Constant.CUSTOM_FORM_STATUS_SCHEDULED:
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_7_states);
                llBackground.setBackground(llDrawable);
//                tvItem.setTextColor(context.getResources().getColorStateList(namoa_color_dark_blue));
//                tvItem2.setTextColor(context.getResources().getColorStateList(namoa_color_dark_blue));
//                tvItem3.setTextColor(context.getResources().getColorStateList(namoa_color_dark_blue));

                break;

            default:
                llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
                llBackground.setBackground(llDrawable);
 //               tvItem.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));
 //               tvItem2.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));
 //               tvItem3.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));

                break;
        }

        return convertView;
    }


    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("lbl_date");
        translateList.add("lbl_hour");
        translateList.add("ttl_product");
        translateList.add("lbl_product_code");
        translateList.add("lbl_product_id");
        translateList.add("lbl_product_desc");
        translateList.add("ttl_form");
        translateList.add("lbl_type");
        translateList.add("lbl_form");
        translateList.add("lbl_data_serv");
        translateList.add("lbl_date_schedule_start");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
