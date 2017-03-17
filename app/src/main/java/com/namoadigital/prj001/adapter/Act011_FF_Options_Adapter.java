package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neonhugo on 9/12/2016.
 */

public class Act011_FF_Options_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> dados;
    private HMAux restabs;
    private List<HMAux> pdfs;

    private List<HMAux> dados_final = new ArrayList<>();

    private int idselected = -1;

    public void setIdselected(int idselected) {
        this.idselected = idselected;
        //
        notifyDataSetChanged();
    }

    public void setTabsColors(HMAux aux) {
        for (HMAux auxTT : dados_final) {

            if (aux.get(auxTT.get("page")) != null){
                auxTT.put("link", aux.get(auxTT.get("page")));
            }
        }
        //
        notifyDataSetChanged();
    }

    public Act011_FF_Options_Adapter(Context context, int resource, List<HMAux> dados, HMAux restabs, List<HMAux> pdfs) {
        this.context = context;
        this.resource = resource;
        this.dados = dados;
        this.restabs = restabs;
        this.pdfs = pdfs;
        //
        feedDados_final();
    }

    private void feedDados_final() {
        for (HMAux aux : dados) {
            HMAux item = new HMAux();

            item.put("name", aux.get("custom_form_field_desc"));
            item.put("page", aux.get("page"));
            item.put("link", restabs.get(aux.get("page")) == null ? "PENDING" : restabs.get(aux.get("page")));
            item.put("count", aux.get("pagecount"));

            dados_final.add(item);
        }

//        for (HMAux aux : pdfs) {
//            HMAux item = new HMAux();
//
//            item.put("name", aux.get("blob_name"));
//            item.put("page", aux.get("page"));
//            item.put("link", aux.get("blob_url_local"));
//            item.put("count", "");
//
//            dados_final.add(item);
//        }

    }

    @Override
    public int getCount() {
        return dados_final.size();
    }

    @Override
    public Object getItem(int position) {
        return dados_final.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return Long.parseLong((dados.get(position).get(HMAux.TEXTO_01)));
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //String[] from = {HMAux.TEXTO_15}; nome TEXTO_11 id
        //int[] to = {R.id.f_ge_cf_form_exe_options_cell_tv_nome};
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }

        LinearLayout ll_back_b = (LinearLayout)
                convertView.findViewById(R.id.act011_ff_options_cell_ll_a_b);

        LinearLayout ll_back = (LinearLayout)
                convertView.findViewById(R.id.act011_ff_options_cell_ll_a);

        TextView tv_page = (TextView)
                convertView.findViewById(R.id.act011_ff_options_cell_tv_number);

        TextView tv_name = (TextView)
                convertView.findViewById(R.id.act011_ff_options_cell_tv_name);

        TextView tv_count = (TextView)
                convertView.findViewById(R.id.act011_ff_options_cell_tv_count);


        HMAux item = dados_final.get(position);

        tv_page.setText(item.get("page") + "." );
        tv_name.setText(item.get("name"));
        tv_count.setText(item.get("count"));

        if (item.get("link").equals("OK")) {
            ll_back.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_light_green4));
            ll_back_b.setBackground(null);
        } else if (item.get("link").equals("ERROR")) {
            //ll_back.setBackgroundColor(0xffff9900);
            //ll_back_b.setBackground(context.getDrawable(R.drawable.border_orange));
            ll_back.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_orange_light2));
        } else {
            ll_back_b.setBackground(null);
            ll_back.setBackgroundColor(0x00000000);
        }

        if (item.get("page").equalsIgnoreCase(String.valueOf(idselected))) {
            //tv_name.setTextColor(context.getResources().getColor(R.color.text_red));
            ll_back.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_light_blue3));
            //
            //ll_back.setBackground(context.getResources().getDrawable(R.drawable.btn_primary_layout));
        } else {
            //tv_name.setTextColor(context.getResources().getColor(R.color.text_black));
            //
            //ll_back.setBackground(null);
        }
        //
        return convertView;
    }
}
