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

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 */

public class Act013_Adapter_Pendencies extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Act013_Adapter_Pendencies(Context context, int resource, List<HMAux> source) {
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
        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.act013_main_content_cell_01_ll_bg);
        //
        TextView tvItem = (TextView) convertView.findViewById(R.id.act013_main_content_cell_01_tv_01);
        //
        TextView tvItem2 = (TextView) convertView.findViewById(R.id.act013_main_content_cell_01_tv_02);
        //
        TextView tvItem3 = (TextView) convertView.findViewById(R.id.act013_main_content_cell_01_tv_03);
        //Resgata HmAux com as informações
        HMAux item = source.get(position);

        Drawable llDrawable = null;

        tvItem.setText(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        tvItem2.setText(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE) + " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        tvItem3.setText(
                item.get(
                        GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE) +
                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION) +
                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC)
        );

        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)){

            case GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS_IN_PROCESSING:
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_states);
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.custom_yellow_dark));
                tvItem2.setTextColor(context.getResources().getColorStateList(R.color.custom_yellow_dark));
                tvItem3.setTextColor(context.getResources().getColorStateList(R.color.custom_yellow_dark));

                break;
            //
            case GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS_FINALIZED:
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_states);
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_font_color));
                tvItem2.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_font_color));
                tvItem3.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_font_color));
                break;

            default:
                llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));
                tvItem2.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));
                tvItem3.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));

                break;
        }

        return convertView;
    }
}
