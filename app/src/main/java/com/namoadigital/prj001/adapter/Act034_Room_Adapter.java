package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Act034_Room_Adapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<HMAux> source;
    private int resource;
    private HMAux hmAux_Trans;
    private OnIvRoomClickListner OnIvRoomClickListner;
    //Filter implementation
    private ValueFilter valueFilter;
    private ArrayList<HMAux> source_filtered;

    public interface OnIvRoomClickListner {
        void onIvRoomClick(String room_code, String room_type, String room_desc, String image_path);
    }

    public void setOnIvRoomClickListner(Act034_Room_Adapter.OnIvRoomClickListner onIvRoomClickListner) {
        OnIvRoomClickListner = onIvRoomClickListner;
    }

    public Act034_Room_Adapter(Context context, ArrayList<HMAux> source, int resource, String mket_filter, HMAux hmAux_Trans) {
        this.context = context;
        this.source = source;
        this.resource = resource;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.source_filtered = source;
        getFilter().filter(mket_filter);
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

    public int getRoomPosition(String room_code) {
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).get(CH_RoomDao.ROOM_CODE).equals(room_code)) {
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
        View v_type_color = convertView.findViewById(R.id.act034_room_cell_v_color);
        ImageView iv_room_image = (ImageView) convertView.findViewById(R.id.act034_room_cell_iv_image);
        ImageView iv_room_icon = (ImageView) convertView.findViewById(R.id.act034_room_cell_iv_icon);
        TextView tv_room_desc = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_room_desc);
        LinearLayout ll_room_status = (LinearLayout) convertView.findViewById(R.id.act034_opc_cell_ll_room_status);
        TextView tv_room_status = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_room_status);
        TextView tv_msg_date = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_msg_date);
        TextView tv_msg = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_msg);
        TextView tv_badge = (TextView) convertView.findViewById(R.id.act034_room_cell_tv_badge);
        //
        Bitmap imgBitmap = null;
        if (!item.get(CH_RoomDao.ROOM_IMAGE_LOCAL).equalsIgnoreCase("")) {
            //imgBitmap = BitmapFactory.decodeFile(getImageThumbnail(item.get(CH_RoomDao.ROOM_IMAGE_LOCAL)));
            imgBitmap = BitmapFactory.decodeFile(Constant.CACHE_CHAT_PATH + "/" + item.get(CH_RoomDao.ROOM_IMAGE_LOCAL));
        } else {
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
                if (OnIvRoomClickListner != null) {
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
                v_type_color.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_light_blue));
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_group));
                ll_room_status.setVisibility(View.GONE);
                tv_room_status.setText("");
                break;
            case Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER:
                v_type_color.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_light_blue));
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_private));
                ll_room_status.setVisibility(View.GONE);
                tv_room_status.setText("");
                break;
            case Constant.CHAT_ROOM_TYPE_SO:
            case Constant.CHAT_ROOM_TYPE_PA:
                v_type_color.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_light_green3));
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_others_type));
                ll_room_status.setVisibility(View.VISIBLE);
                tv_room_status.setText(item.get(CH_RoomDao.ROOM_STATUS));
                break;
            case Constant.CHAT_ROOM_TYPE_SYS:
                v_type_color.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_black));
                ll_room_status.setVisibility(View.GONE);
                tv_room_status.setText("");
                break;
            default:
                v_type_color.setBackgroundColor(context.getResources().getColor(R.color.namoa_color_danger_red));
                iv_room_icon.setImageDrawable(context.getDrawable(R.drawable.ic_room_others_type));
                ll_room_status.setVisibility(View.GONE);
                tv_room_status.setText("");
        }
        //POR HORA ESCONDE OS ICONE ATE ENCONTRAR ALGO MELHOR 05/02/2018
        iv_room_icon.setVisibility(View.GONE);
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
        String msg = item.containsKey(CH_MessageDao.MSG_OBJ + "_data") ? item.get(CH_MessageDao.MSG_OBJ + "_data") : null;
        String type = item.containsKey(CH_MessageDao.MSG_OBJ + "_type") ? item.get(CH_MessageDao.MSG_OBJ + "_type") : null;

        if (type != null && type.equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_TEXT)) {
            tv_msg.setText(ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(msg), 45));
        } else if (type != null && type.equalsIgnoreCase(Constant.CHAT_MESSAGE_TYPE_TRANSLATE)) {
            tv_msg.setText(ToolBox_Inf.getSafeSubstring(getTranslateMsg(msg), 45));
        } else {
            tv_msg.setText(type);
        }
        //
        int i = ToolBox_Inf.convertStringToInt(item.get(Sql_Act034_004.BADGE));
        //
        if (i == 0) {
            tv_badge.setText("");
            tv_badge.setVisibility(View.INVISIBLE);
        } else if (i > 9) {
            tv_badge.setVisibility(View.VISIBLE);
            tv_badge.setText(String.valueOf(i));
        } else {
            tv_badge.setVisibility(View.VISIBLE);
            tv_badge.setText(" " + String.valueOf(i) + " ");
        }
        //
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            constraint = ToolBox_Inf.AccentMapper(constraint.toString().toLowerCase());

            if (constraint != null && constraint.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<HMAux>();
                for (HMAux hmAux : source_filtered) {
                    String room_desc = ToolBox_Inf.AccentMapper(hmAux.get(CH_RoomDao.ROOM_DESC).toLowerCase());
                    String room_status = ToolBox_Inf.AccentMapper(hmAux.get(CH_RoomDao.ROOM_STATUS).toLowerCase());
                    if (
                            room_desc.contains(constraint.toString().toLowerCase()) ||
                                    (room_status != null && room_status.contains(constraint.toString().toLowerCase()))
                            ) {
                        filterList.add(hmAux);
                    }
                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = source_filtered.size();
                results.values = source_filtered;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            source = (ArrayList<HMAux>) results.values;
            //
            notifyDataSetChanged();
        }
    }

    private String getTranslateMsg(String msg) {
        String resultTranslate = "";
        try {
            String msgParts[] = msg.replace("|", "#").split("#");
            resultTranslate = hmAux_Trans.get(msgParts[0]) + (msgParts.length > 1 ? msgParts[1] : "");
            //
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.CHAT_MESSAGE_TYPE_TRANSLATE;
        }
        return resultTranslate;
    }

    private String getImageThumbnail(String image_path) {
        return Constant.THU_PATH + "/" + image_path.replace(".jpg", "_thumb.jpg");
    }
}
