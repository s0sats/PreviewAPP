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
import com.namoadigital.prj001.ui.act006.Act006_Main;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 09/03/2017.
 */

public class Act006_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Act006_Adapter(Context context, int resource, List<HMAux> source) {
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
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.namoa_custom_cell_2_ll_background);
        //
        TextView tvItem = (TextView) convertView.findViewById(R.id.namoa_custom_cell_2_tv_item);
        //
        TextView tvSubItem = (TextView) convertView.findViewById(R.id.namoa_custom_cell_2_tv_sub_item);

        //Esconde elementos que  não serão mostrados
        tvSubItem.setVisibility(View.GONE);

        //Configura tvItem
        tvItem.setTextColor(context.getResources().getColor(R.color.padrao_WHITE));
        tvItem.setText(item.get(Act006_Main.LIST_LABEL));

        //Define cor do fundo
        if(item.get(Act006_Main.LIST_OPT).equals("new")){
            Drawable llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_3_states);
            llBackground.setBackground(llDrawable);

        }else{
            Drawable llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_2_states);
            llBackground.setBackground(llDrawable);
        }

        return convertView;
    }
}
