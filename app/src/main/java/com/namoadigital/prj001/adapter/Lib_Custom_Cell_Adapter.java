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
import com.namoadigital.prj001.dao.MD_SiteDao;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class Lib_Custom_Cell_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source) {
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

        Drawable llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
        llBackground.setBackground(llDrawable);
        tvItem.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));
        tvSubItem.setTextColor(context.getResources().getColorStateList(R.color.lib_custom_cell_font_color));
        //
        String siteDesc = item.get(MD_SiteDao.SITE_ID) +" - " + item.get(MD_SiteDao.SITE_DESC);
        tvItem.setText(siteDesc);

        return convertView;
    }
}
