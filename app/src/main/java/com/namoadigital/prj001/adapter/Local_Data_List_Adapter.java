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
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 */

public class Local_Data_List_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Local_Data_List_Adapter(Context context, int resource, List<HMAux> source) {
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
        TextView tv_date_lvl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_label);
        TextView tv_date_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date);
        tv_list.add(tv_date_lvl);
        tv_list.add(tv_date_val);
        //
        TextView tv_hour_lvl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_hour_label);
        TextView tv_hour_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_hour);
        tv_list.add(tv_hour_lvl);
        tv_list.add(tv_hour_val);
        //
        TextView tv_product_ttl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_ttl);
        tv_list.add(tv_product_ttl);
        //
        TextView tv_code_lvl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_code_label);
        TextView tv_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_code_val);
        tv_list.add(tv_code_lvl);
        tv_list.add(tv_code_val);
        //
        TextView tv_id_lvl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_label);
        TextView tv_id_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_val);
        tv_list.add(tv_id_lvl);
        tv_list.add(tv_id_val);
        //
        TextView tv_product_desc_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_desc_lbl);
        TextView tv_product_desc_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_desc_val);
        tv_list.add(tv_product_desc_lbl);
        tv_list.add(tv_product_desc_val);
        //
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_ttl);
        tv_list.add(tv_form_ttl);
        //
        TextView tv_type_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_type_label);
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_type_val);
        tv_list.add(tv_type_lbl);
        tv_list.add(tv_type_val);
        //
        TextView tv_form_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_label);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_val);
        tv_list.add(tv_form_lbl);
        tv_list.add(tv_form_val);
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
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_in_processing_states);
                llBackground.setBackground(llDrawable);
                break;
            //
            case Constant.CUSTOM_FORM_STATUS_FINALIZED:
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_4_states);
                llBackground.setBackground(llDrawable);
//                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
//                tvItem2.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
//                tvItem3.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));

                break;
            case Constant.CUSTOM_FORM_STATUS_SENT:
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_sent_states);
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
}
