package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Member_Adapter extends BaseAdapter {

    public static final String USER_CODE = "USER_CODE";
    public static final String SYS_USER_IMAGE = "sys_user_image";
    public static final String USER_NICK = "user_nick";
    public static final String IS_ONLINE = "is_online";

    private Context context;
    private ArrayList<HMAux> source;
    private int resource;

    public Chat_Member_Adapter(Context context, ArrayList<HMAux> source, int resource) {
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

    public void updateMemberImage(String userCode, String image_path){
        String user_code = userCode;
        for (HMAux item : source) {
            if(item.get(USER_CODE).equalsIgnoreCase(user_code)){
                item.put(SYS_USER_IMAGE,image_path);
                break;
            }
        }
        //
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
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
        //
        if(item.get(SYS_USER_IMAGE) != null){
            iv_member_img.setImageBitmap(BitmapFactory.decodeFile(Constant.CACHE_CHAT_PATH+"/"+item.get(SYS_USER_IMAGE)));
        }else{
            iv_member_img.setImageDrawable(context.getDrawable(R.drawable.ic_person_black_24dp));
        }
        //
        tv_member.setText(item.get(USER_NICK));
        //
        if(item.get(IS_ONLINE).equalsIgnoreCase("1")){
            iv_online_icon.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green));
        }else{
            iv_online_icon.setColorFilter(context.getResources().getColor(R.color.namoa_color_danger_red));
        }

        return convertView;
    }
}
