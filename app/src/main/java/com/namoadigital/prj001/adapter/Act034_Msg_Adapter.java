package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Msg_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<HMAux> source;

    public Act034_Msg_Adapter(Context context, ArrayList<HMAux> source) {
        this.context = context;
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
        int resource = -1;
        HMAux item = source.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //
            switch (item.get("type")){
                case "others":
                    resource = R.layout.act034_main_msg_others_cell;
                    break;
                case "mine":
                    resource = R.layout.act034_main_msg_mine_cell;
                    break;
                case "alert":default:
                    resource = R.layout.act034_main_msg_alert_cell;
                    break;
            }

            convertView = inflater.inflate(resource,parent,false);
        }
        //
        TextView  tv_user;
        TextView  tv_date;
        ImageView iv_msg_states;
        TextView tv_msg;
        TextView tv_alert;
        TextView tv_alert_msg;
        //
        switch (item.get("type")){
            case "others":
                tv_user = (TextView) convertView.findViewById(R.id.act034_main_msg_others_cell_tv_user);
                tv_date = (TextView) convertView.findViewById(R.id.act034_main_msg_others_cell_tv_date);
                iv_msg_states = (ImageView) convertView.findViewById(R.id.act034_main_msg_others_cell_iv_msg_state);
                tv_msg  = (TextView) convertView.findViewById(R.id.act034_main_msg_others_cell_tv_msg);
                //
                tv_user.setVisibility(View.VISIBLE);
                tv_user.setText(item.get("user"));
                tv_date.setText(item.get("date"));
                iv_msg_states.setVisibility(View.GONE);
                tv_msg.setText(item.get("msg"));

                break;
            case "mine":
                tv_user = (TextView) convertView.findViewById(R.id.act034_main_msg_mine_cell_tv_user);
                tv_date = (TextView) convertView.findViewById(R.id.act034_main_msg_mine_cell_tv_date);
                iv_msg_states = (ImageView) convertView.findViewById(R.id.act034_main_msg_mine_cell_iv_msg_state);
                tv_msg  = (TextView) convertView.findViewById(R.id.act034_main_msg_mine_cell_tv_msg);
                //
                tv_user.setVisibility(View.GONE);
                tv_date.setText(item.get("date"));
                iv_msg_states.setVisibility(View.VISIBLE);
                switch (item.get("status")){
                    case "0":
                        iv_msg_states.setImageDrawable(context.getDrawable(R.drawable.sand_watch_transp));
                        break;
                    case "1":
                        iv_msg_states.setImageDrawable(context.getDrawable(R.drawable.ic_done_black_24dp));
                        break;
                    case "2":
                        iv_msg_states.setImageDrawable(context.getDrawable(R.drawable.ic_done_all_black_24dp));
                        break;
                    default:
                        break;

                }
                tv_msg.setText(item.get("msg"));
                break;

            case "alert":default:
                tv_alert =(TextView) convertView.findViewById(R.id.act034_main_msg_alert_cell_tv_alert);
                tv_alert_msg =(TextView) convertView.findViewById(R.id.act034_main_msg_alert_cell_tv_alert_msg);
                //
                tv_alert.setText(item.get("alert"));
                tv_alert_msg.setText(item.get("alert_msg"));
                break;
        }

        return convertView;
    }
}
