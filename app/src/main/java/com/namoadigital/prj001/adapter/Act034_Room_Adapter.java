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
import com.namoadigital.prj001.sql.Sql_Act034_004;
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
    private OnIvRoomClickListner OnIvRoomClickListner;

    public interface OnIvRoomClickListner{
        void onIvRoomClick(String room_code, String room_type, String room_desc, String image_path);
    }

    public void setOnIvRoomClickListner(Act034_Room_Adapter.OnIvRoomClickListner onIvRoomClickListner) {
        OnIvRoomClickListner = onIvRoomClickListner;
    }

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

    public int getRoomPosition(String room_code){
        for (int i = 0; i < source.size() ; i++) {
            if(source.get(i).get(CH_RoomDao.ROOM_CODE).equals(room_code)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }
        //
        final HMAux item = source.get(position);
        //
        ImageView iv_room_image = (ImageView) convertView.findViewById(R.id.act034_room_cell_iv_image);
        ImageView iv_room_icon = (ImageView) convertView.findViewById(R.id.act034_room_cell_iv_icon);
        TextView tv_room_desc = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_room_desc);
        TextView tv_msg_date = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_msg_date);
        TextView tv_msg = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_msg);
        TextView tv_badge = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_badge);
        //
        Bitmap imgBitmap = null;
        if(!item.get(CH_RoomDao.ROOM_IMAGE_LOCAL).equalsIgnoreCase("")) {
            imgBitmap = BitmapFactory.decodeFile(getImageThumbnail(item.get(CH_RoomDao.ROOM_IMAGE_LOCAL)));
        }else{
            imgBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_namoa);
        }
        /*
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), imgBitmap);
        drawable.setCircular(true);
        iv_room_image.setImageDrawable(drawable);
        */
        iv_room_image.setImageBitmap(imgBitmap);
        //
        iv_room_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnIvRoomClickListner != null){
                    OnIvRoomClickListner.onIvRoomClick(
                            item.get(CH_RoomDao.ROOM_CODE),
                            item.get(CH_RoomDao.ROOM_TYPE),
                            item.get(CH_RoomDao.ROOM_DESC),
                            item.get(CH_RoomDao.ROOM_IMAGE_LOCAL)
                    );
                }
            }
        });
        //
        switch (item.get(CH_RoomDao.ROOM_TYPE)) {

            case Constant.CHAT_ROOM_TYPE_WORKGROUP:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_group));
                break;
            case Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_private));
                break;
            case Constant.CHAT_ROOM_TYPE_SO:
            default:
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_others_type));
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
            tv_msg.setText(ToolBox_Inf.getSafeSubstring(msg, 45));
        }else{
            tv_msg.setText(type);
        }
        //
        int i = ToolBox_Inf.convertStringToInt(item.get(Sql_Act034_004.BADGE));
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

    private String getImageThumbnail(String image_path){
        return Constant.THU_PATH +"/"+ image_path.replace(".jpg","_thumb.jpg");
    }
}
