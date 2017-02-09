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

import java.util.List;

/**
 * Created by neonhugo on 9/12/2016.
 */

public class Act011_FF_Options_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> dados;

    private int idselected = -1;

    public void setIdselected(int idselected) {
        this.idselected = idselected;
        //
        notifyDataSetChanged();
    }

    public Act011_FF_Options_Adapter(Context context, int resource, List<HMAux> dados) {
        this.context = context;
        this.resource = resource;
        this.dados = dados;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Object getItem(int position) {
        return dados.get(position);
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

        LinearLayout ll_back = (LinearLayout)
                convertView.findViewById(R.id.act011_ff_options_cell_ll_a);

        TextView tv_name = (TextView)
                convertView.findViewById(R.id.act011_ff_options_cell_tv_name);

        HMAux item = dados.get(position);

        tv_name.setText(item.get("custom_form_field_desc"));

        if (item.get("page").equalsIgnoreCase(String.valueOf(idselected))) {
            tv_name.setTextColor(context.getResources().getColor(R.color.text_red));
            //
            //ll_back.setBackground(context.getResources().getDrawable(R.drawable.btn_primary_layout));
        } else {
            tv_name.setTextColor(context.getResources().getColor(R.color.text_black));
            //
            //ll_back.setBackground(null);
        }
        //
        return convertView;
    }
}
