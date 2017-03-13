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

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 13/03/2017.
 */

public class Namoa_Custom_Cell_2_Adapter extends BaseAdapter {

    //CONSTANTES
    public static final String CFG_ID_DESC = "ID_DESC";
    public static final String CFG_DESC = "DESC";
    public static final String CFG_DESC_QTY = "DESC_QTY";

    private Context context;
    private int resource;
    private List<HMAux> source;
    private String config;
    private String key_id;
    private String key_text;

    public Namoa_Custom_Cell_2_Adapter(Context context, int resource, List<HMAux> source, String config, String key_text) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_text = key_text;
    }

    public Namoa_Custom_Cell_2_Adapter(Context context, int resource, List<HMAux> source, String config, String key_text, String key_id) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_text = key_text;
        this.key_id = key_id;

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
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.namoa_custom_cell_2_ll_background);
        //
        TextView tvItem = (TextView) convertView.findViewById(R.id.namoa_custom_cell_2_tv_item);
        //
        TextView tvSubItem = (TextView) convertView.findViewById(R.id.namoa_custom_cell_2_tv_sub_item);
        //
        //Resgata HmAux com as informações
        HMAux item = source.get(position);
        //
        Drawable llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_4_states);
        String itemText = "";

        switch (config) {
            case CFG_ID_DESC:
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
                //
                try {
                    if (item.get(key_id).trim().length() > 0) {
                        itemText = item.get(key_id) + " - ";
                    }
                } catch (Exception e) {
                }
                //
                itemText += item.get(key_text);
                tvItem.setText(itemText);
                break;
            case CFG_DESC_QTY:
                llBackground.setBackground(llDrawable);
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
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
                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
                tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
                //
                itemText = item.get(key_text);
                tvItem.setText(itemText);
                break;

        }


        return convertView;
    }
}
