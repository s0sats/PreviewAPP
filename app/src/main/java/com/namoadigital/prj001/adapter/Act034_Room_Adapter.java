package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Room_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<HMAux> source;
    private int resource;

    public Act034_Room_Adapter(Context context, ArrayList<HMAux> source, int resource) {
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
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }
        //
        HMAux item = source.get(position);
        //
        ImageView iv_room_image = (ImageView) convertView.findViewById(R.id.act034_room_cell_iv_image);
        ImageView iv_room_icon = (ImageView) convertView.findViewById(R.id.act034_room_cell_iv_icon);
        TextView tv_room_desc = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_room_desc);
        TextView tv_msg_date = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_msg_date);
        TextView tv_msg = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_msg);
        TextView tv_badge = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_badge);
        //
        Bitmap imgBitmap = BitmapFactory.decodeFile(item.get(CH_RoomDao.ROOM_IMAGE_LOCAL));
        iv_room_image.setImageBitmap(imgBitmap);
        //
        switch (item.get(CH_RoomDao.ROOM_TYPE)){

            case Constant.CHAT_ROOM_TYPE_WORKGROUP:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_group_black_24dp));
                break;

            default:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_block_helper_black_48dp));

        }
        //
        tv_room_desc.setText(item.get(CH_RoomDao.ROOM_DESC));
        //
        tv_msg_date.setText("01/01/1900 00:00:00");
        //
        tv_msg.setText("Last Msg ?!");
        //
        Random random = new Random();
        //
        int i = random.nextInt( 5);
        //
        if(i <=2){
            tv_badge.setText("");
            tv_badge.setVisibility(View.INVISIBLE);
        }else{
            tv_badge.setVisibility(View.VISIBLE);
            tv_badge.setText(" " +String.valueOf(i) +" ");
        }

        //
        return convertView;
    }
}
