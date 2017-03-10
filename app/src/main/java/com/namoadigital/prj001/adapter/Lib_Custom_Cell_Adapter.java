package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class Lib_Custom_Cell_Adapter extends BaseAdapter {

    //CONSTANTES
    public static final String CFG_ID_DESC = "ID_DESC";
    public static final String CFG_ID_DESC_DESC2 = "ID_DESC1_DESC2";
    public static final String CFG_DESC = "DESC";
    public static final String CFG_DESC_QTY = "DESC_QTY";

    private Context context;
    private int resource;
    private List<HMAux> source;
    private String config;
    private String key_id;
    private String key_text;
    private String key_text2;

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source, String config,  String key_text) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_text = key_text;
    }

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source, String config,  String key_text, String key_id) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_text = key_text;
        this.key_id = key_id;
    }

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source, String config, String key_text, String key_id, String key_text2) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_id = key_id;
        this.key_text = key_text;
        this.key_text2 = key_text2;
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

        if (convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);
        }
        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.lib_custom_cell_ll_background);
        //
        TextView tvItem = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_item);
        //
        TextView tvSubItem = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_sub_item);
        //
        ImageView iv001 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_001);
        //
        ImageView iv002 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_002);
        //
        TextView  tvTopQty = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_top);
        //Resgata HmAux com as informações
        HMAux item = source.get(position);

        //Inicia configuraçõa dos elementos
        tvSubItem.setVisibility(View.GONE);
        iv001.setVisibility(View.GONE);
        iv002.setVisibility(View.GONE);

        Drawable llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_1_states);
        String itemText = "";

        switch (config){
            case CFG_ID_DESC:
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                //
                try {
                    if (item.get(key_id).trim().length() > 0){
                        itemText = item.get(key_id) + " - ";
                    }
                } catch (Exception e) {
                }
                //
                itemText += item.get(key_text);
                tvItem.setText(itemText);
                break;
            case CFG_ID_DESC_DESC2:
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                //
                try {
                    if (item.get(key_id).trim().length() > 0){
                        itemText = item.get(key_id) + " - ";
                    }
                } catch (Exception e) {
                }
                //
                try {
                    itemText += item.get(key_text);
                } catch (Exception e) {
                }
                //
                try {
                    itemText += " - " + item.get(key_text2);
                } catch (Exception e) {
                }
                tvItem.setText(itemText);

                break;
            case CFG_DESC_QTY:
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                //
                itemText = item.get(key_text);
                //
                try {
                    if (item.get(key_id).trim().length() > 0){
                        itemText += " (" + item.get(key_id) + ")";
                    }
                } catch (Exception e) {
                    itemText += " ( - )";
                }
                //
                //
                tvItem.setText(itemText);
                break;

            case CFG_DESC:default:
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_cell_1_font_color));
                //
                itemText = item.get(key_text);
                tvItem.setText(itemText);
                break;
        }

        return convertView;
    }
}
