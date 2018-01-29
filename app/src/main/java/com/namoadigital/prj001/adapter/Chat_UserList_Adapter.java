package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_UserList_Adapter extends BaseAdapter {

    public static final String USER_NICK = "user_nick";
    public static final String USER_CODE = "user_code";
    public static final String ROOM_CODE = "room_code";
    public static final String ON_LINE = "on_line";
    public static final String SYS_USER_IMAGE = "sys_user_image";
    public static final String SYS_USER_IMAGE_NAME = "sys_user_image_name";

    private Context context;
    private ArrayList<HMAux> source;
    private int resource;

    public Chat_UserList_Adapter(Context context, ArrayList<HMAux> source, int resource) {
        this.context = context;
        this.source = source;
        this.resource = resource;
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
        return 0l;
    }

    public void updateMemberImage(String userCode, String image_path) {
        String user_code = userCode;
        for (HMAux item : source) {
            if (item.get(USER_CODE).equalsIgnoreCase(user_code)) {
                item.put(SYS_USER_IMAGE, image_path);
                break;
            }
        }
        //
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }
        //
        HMAux item = source.get(position);
        //
        ImageView iv_member_img = (ImageView) convertView.findViewById(R.id.act034_room_info_cell_iv_member);
        TextView tv_member = (TextView) convertView.findViewById(R.id.act034_room_info_cell_tv_member);
        ImageView iv_online_icon = (ImageView) convertView.findViewById(R.id.act034_room_info_cell_iv_online);
        ImageView iv_delivered = (ImageView) convertView.findViewById(R.id.act034_room_info_cell_iv_delivered);
        TextView tv_delivered = (TextView) convertView.findViewById(R.id.act034_room_info_cell_tv_delivered);
        ImageView iv_read = (ImageView) convertView.findViewById(R.id.act034_room_info_cell_iv_read);
        TextView tv_read = (TextView) convertView.findViewById(R.id.act034_room_info_cell_tv_read);
        //
        Glide.with(context)
                .load(item.get(SYS_USER_IMAGE))
                .into(iv_member_img);
        //
        tv_member.setText(item.get(USER_NICK));

        return convertView;
    }
}
