package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_UserList_Adapter extends BaseAdapter implements Filterable {

    public static final String USER_NICK = "user_nick";
    public static final String USER_CODE = "user_code";
    public static final String ROOM_CODE = "room_code";
    public static final String USER_NAME = "user_name";
    public static final String ON_LINE = "on_line";
    public static final String SYS_USER_IMAGE = "sys_user_image";
    public static final String SYS_USER_IMAGE_NAME = "sys_user_image_name";

    private Context context;
    private ValueFilter valueFilter;
    private ArrayList<HMAux> source;
    private ArrayList<HMAux> source_filtered;
    private int resource;

    public Chat_UserList_Adapter(Context context, ArrayList<HMAux> source, int resource) {
        this.context = context;
        this.source = source;
        this.source_filtered = source;
        this.resource = resource;
        //
        getFilter();
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
        TextView tv_member_name = (TextView) convertView.findViewById(R.id.act034_room_info_cell_tv_member_name);
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
        tv_member_name.setText(item.get(USER_NAME));

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
            if (constraint != null && constraint.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<HMAux>();
                constraint = ToolBox_Inf.AccentMapper(constraint.toString());
                //
                for (int i = 0; i < source_filtered.size(); i++) {
                    String user_nick = ToolBox.AccentMapper(source_filtered.get(i).get(USER_NICK).toLowerCase());
                    String user_name = ToolBox.AccentMapper(source_filtered.get(i).get(USER_NAME).toLowerCase());
                    if ( user_nick.contains(constraint.toString().toLowerCase()) ||
                         user_name.contains(constraint.toString().toLowerCase())
                    ) {

                        HMAux hmAux = new HMAux();
                        hmAux.putAll(source_filtered.get(i));

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

            notifyDataSetChanged();
        }
    }
}
