package com.namoadigital.prj001.adapter;

import android.content.Context;
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
 * Created by neomatrix on 28/11/17.
 */

public class Act035_Adapter_Messages extends BaseAdapter {

    private Context context;
    //
    private int resource_01;
    private int resource_02;
    //
    private List<HMAux> data;

    public Act035_Adapter_Messages(Context context, int resource_01, int resource_02, List<HMAux> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.data = data;
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
        return Long.parseLong(data.get(position).get(HMAux.ID));
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(data.get(position).get(HMAux.TEXTO_02));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;
            }
        }
        //
        HMAux hmAux = data.get(position);
        //
        View v_space_left;
        ImageView iv_other_img;
        LinearLayout ll_item;
        TextView tv_name;
        ImageView iv_foto;
        TextView tv_message;
        TextView tv_hour;
        ImageView iv_me_img;
        View v_space_right;

        switch (getItemViewType(position)) {
            case 0:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
                iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);

                v_space_left.setVisibility(View.INVISIBLE);
                iv_other_img.setVisibility(View.VISIBLE);
                ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_from));
                tv_name.setText("Nome");
                iv_foto.setImageResource(R.mipmap.ic_namoa);
                tv_message.setText("Message");
                tv_hour.setText("11:25");
                iv_me_img.setVisibility(View.INVISIBLE);
                v_space_right.setVisibility(View.VISIBLE);

                break;
            case 1:

                v_space_left = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_left);
                iv_other_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_other_img);
                ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
                tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
                iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
                tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                iv_me_img = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_me_img);
                v_space_right = convertView.findViewById(R.id.act035_main_content_cell_whats_v_space_right);

                v_space_left.setVisibility(View.VISIBLE);
                iv_other_img.setVisibility(View.INVISIBLE);
                ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_me));
                tv_name.setVisibility(View.GONE);
                iv_foto.setImageResource(R.mipmap.ic_namoa);
                tv_message.setText("Message");
                tv_hour.setText("11:25");
                iv_me_img.setVisibility(View.VISIBLE);
                v_space_right.setVisibility(View.INVISIBLE);

                break;
        }
        //
        return convertView;
    }
}