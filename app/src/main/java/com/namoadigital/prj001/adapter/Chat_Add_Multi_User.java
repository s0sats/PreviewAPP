package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

import static com.namoadigital.prj001.adapter.Chat_UserList_Adapter.SYS_USER_IMAGE;
import static com.namoadigital.prj001.adapter.Chat_UserList_Adapter.USER_NAME;
import static com.namoadigital.prj001.adapter.Chat_UserList_Adapter.USER_NICK;
import static com.namoadigital.prj001.adapter.Chat_UserList_Adapter.USER_SELECTED;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Chat_Add_Multi_User extends BaseAdapter implements Filterable {

    private Context context;
    private ValueFilter valueFilter;
    //
    private int resource;

    private ArrayList<HMAux> data;
    private ArrayList<HMAux> data_filtered;


    private HMAux hmAux_Trans;
    private String mResource_Code;

    public Chat_Add_Multi_User(Context context, int resource, HMAux hmAux_Trans, ArrayList<HMAux> data) {
        this.context = context;
        this.resource = resource;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.data = data;
        this.data_filtered = new ArrayList<>();
        this.data_filtered.addAll(data);
        //
        getFilter();
    }

    @Override
    public int getCount() {
        return data_filtered.size();
    }

    @Override
    public Object getItem(int position) {
        return data_filtered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        boolean results = false;

        switch (getItemViewType(position)) {
            default:
                results = true;
                break;
        }
        return results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {

                case 0:
                    convertView = mInflater.inflate(resource, parent, false);
                    break;

                case 1:
                    break;
            }
        }

        HMAux hmAux = data_filtered.get(position);

        switch (getItemViewType(position)) {
            // Service
            case 0:
                processUser(hmAux, convertView);
                break;

            case 1:
                break;
        }
        return convertView;
    }

    private void processUser(final HMAux hmAux, View convertView) {
        LinearLayout ll_background = (LinearLayout) convertView.findViewById(R.id.chat_add_multi_user_cell_ll_background);
        ImageView iv_member_img = (ImageView) convertView.findViewById(R.id.chat_add_multi_user_cell_iv_member);
        TextView tv_member = (TextView) convertView.findViewById(R.id.chat_add_multi_user_cell_tv_member);
        TextView tv_member_name = (TextView) convertView.findViewById(R.id.chat_add_multi_user_cell_tv_member_name);
        //
        Glide.with(context)
                .load(hmAux.get(SYS_USER_IMAGE))
                .into(iv_member_img);
        //
        if (hmAux.get(USER_SELECTED).equalsIgnoreCase("1")){
            ll_background.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
        } else {
            ll_background.setBackground(null);
        }
        //
        ll_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hmAux.get(USER_SELECTED).equalsIgnoreCase("1")){
                    hmAux.put(USER_SELECTED, "0");
                } else {
                    hmAux.put(USER_SELECTED, "1");
                }

                notifyDataSetChanged();
            }
        });
        //
        tv_member.setText(hmAux.get(USER_NICK));
        tv_member_name.setText(hmAux.get(USER_NAME));
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
                constraint = ToolBox.AccentMapper(constraint.toString());
                //
                for (int i = 0; i < data.size(); i++) {
                    String user_nick = ToolBox.AccentMapper(data.get(i).get(USER_NICK).toLowerCase());
                    String user_name = ToolBox.AccentMapper(data.get(i).get(USER_NAME).toLowerCase());
                    if (user_nick.contains(constraint.toString().toLowerCase()) ||
                            user_name.contains(constraint.toString().toLowerCase())
                            ) {

                        filterList.add(data.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = data.size();
                results.values = data;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data_filtered = (ArrayList<HMAux>) results.values;

            notifyDataSetChanged();
        }
    }

}