package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
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
 * Created by neomatrix on 02/02/17.
 */

public class Act007_Adapter_Groups_Products extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> data;
    private HMAux hmAux_Trans;

    public Act007_Adapter_Groups_Products(Context context, int resource, List<HMAux> data ,HMAux hmAux_Trans) {
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong((data.get(position).get("code")));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = data.get(position);

        LinearLayout ll_fundo = (LinearLayout)
                convertView.findViewById(R.id.act007_main_content_cell_ll_background);

        ImageView iv_001 = (ImageView)
                convertView.findViewById(R.id.act007_main_content_cell_iv_001);

        TextView tv_code = (TextView)
                convertView.findViewById(R.id.act007_main_content_cell_tv_code);

        TextView tv_id = (TextView)
                convertView.findViewById(R.id.act007_main_content_cell_tv_id);

        TextView tv_desc = (TextView)
                convertView.findViewById(R.id.act007_main_content_cell_tv_desc);

        ll_fundo.setBackground(null);

        String codeText = hmAux_Trans.get("lbl_code") + " " ;
        String idText =  hmAux_Trans.get("lbl_id") + " " ;
        String descText =  /*hmAux_Trans.get("lbl_desc") + " "*/ "" ;

        ColorStateList filterColor =  context.getResources().getColorStateList(R.color.namoa_color_light_blue7);
        //
        tv_code.setTextColor(filterColor);
        tv_id.setTextColor(filterColor);
        tv_desc.setTextColor(filterColor);
        //
        tv_desc.setTypeface(tv_desc.getTypeface(), Typeface.BOLD);

        if (item.get("type").equalsIgnoreCase("group")) {
            iv_001.setVisibility(View.VISIBLE);
            iv_001.setImageResource(R.drawable.ic_pasta);
            iv_001.setColorFilter(context.getResources().getColor(R.color.namoa_color_light_blue7));
            //
            codeText += item.get("code");
            idText += item.get("id");
            descText += item.get("desc");
            //
            tv_code.setText(codeText);
            tv_id.setText(idText);
            tv_desc.setText(descText);

        } else {
            iv_001.setVisibility(View.VISIBLE);
            iv_001.setImageResource(R.drawable.ic_produto);
            iv_001.setColorFilter(context.getResources().getColor(R.color.namoa_color_light_green5));
            //
            codeText += item.get("code");
            idText += item.get("id");
            descText += item.get("desc");
            //
            tv_code.setText(codeText);
            tv_id.setText(idText);
            tv_desc.setText(descText);

        }

        return convertView;
    }
}
