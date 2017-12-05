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
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

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
        Bitmap imgBitmap = BitmapFactory.decodeFile(Constant.THU_PATH +"/"+ item.get(CH_RoomDao.ROOM_IMAGE_LOCAL));
        iv_room_image.setImageBitmap(imgBitmap);
        //
        switch (item.get(CH_RoomDao.ROOM_TYPE)) {

            case Constant.CHAT_ROOM_TYPE_WORKGROUP:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_group_black_24dp));
                break;

            default:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_block_helper_black_48dp));

        }
        //
        tv_room_desc.setText(item.get(CH_RoomDao.ROOM_DESC));
        //
        tv_msg_date.setText(
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(CH_MessageDao.MSG_DATE)),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
        );
        //
        String msg = item.containsKey(CH_MessageDao.MSG_OBJ+"_data")?item.get(CH_MessageDao.MSG_OBJ+"_data"):null ;
        String type = item.containsKey(CH_MessageDao.MSG_OBJ+"_type") ? item.get(CH_MessageDao.MSG_OBJ+"_type") : null;

        if(type != null && type.equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_TEXT)){
            tv_msg.setText(msg);
        }else{
            tv_msg.setText(type);
        }
        //
        int i = ToolBox_Inf.convertStringToInt(item.get(CH_Room_Sql_001.BADGE));
        //
        if (i == 0) {
            tv_badge.setText("");
            tv_badge.setVisibility(View.INVISIBLE);
        } else if(i > 9){
            tv_badge.setVisibility(View.VISIBLE);
            tv_badge.setText(String.valueOf(i));
        }else {
            tv_badge.setVisibility(View.VISIBLE);
            tv_badge.setText(" " + String.valueOf(i) + " ");
        }
        //
        return convertView;
    }
}
