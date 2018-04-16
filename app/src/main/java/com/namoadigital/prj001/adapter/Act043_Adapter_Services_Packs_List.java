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

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act043_Adapter_Services_Packs_List extends BaseAdapter implements Filterable {

    private Context context;
    private ValueFilter valueFilter;
    //
    private int resource_01;
    private int resource_02;

    private ArrayList<HMAux> data;
    private ArrayList<HMAux> data_filtered;


    private HMAux hmAux_Trans;
    private String mResource_Code;

    public Act043_Adapter_Services_Packs_List(Context context, int resource_01, int resource_02, HMAux hmAux_Trans, ArrayList<HMAux> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.data = data;
        this.data_filtered = data;
        //
        getFilter();
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
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            HMAux item = data.get(position);

            if (item.get("type_ps").equalsIgnoreCase("S")) {
                return 0;
            } else if (item.get("type_ps").equalsIgnoreCase("P")) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        boolean results = false;

        switch (getItemViewType(position)) {

//            // Service
//            case 0:
//                results = true;
//                break;
//
//            // Pack
//            case 1:
//                results = true;
//                break;
//
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

                // Service
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;

                // Pack
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;

            }
        }

        HMAux hmAux = data.get(position);

        switch (getItemViewType(position)) {
            // Service
            case 0:
                processService(hmAux, convertView);
                break;

            // Pack
            case 1:
                processPack(hmAux, convertView);
                break;
        }
        return convertView;
    }

    private void processService(HMAux hmAux, View convertView) {
        LinearLayout ll_background = (LinearLayout) convertView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_ll_background);
        TextView tv_desc = (TextView) convertView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_desc);
        ImageView iv_foto = (ImageView) convertView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_iv_type);
        //
        try {
            int qtd = Integer.parseInt(hmAux.get("qtd"));
            //
            if (qtd > 0) {
                ll_background.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
            } else {
                ll_background.setBackground(null);
            }
        } catch (Exception e) {
            ll_background.setBackground(null);
        }
        //
        tv_desc.setText(hmAux.get("pack_service_desc"));
        iv_foto.setImageResource(R.drawable.ic_adicionar2_ns);
    }

    private void processPack(HMAux hmAux, View convertView) {
        LinearLayout ll_background = (LinearLayout) convertView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_ll_background);
        TextView tv_desc = (TextView) convertView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_desc);
        ImageView iv_foto = (ImageView) convertView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_iv_type);
        //
        try {
            int qtd = Integer.parseInt(hmAux.get("qtd"));
            //
            if (qtd > 0) {
                ll_background.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
            } else {
                ll_background.setBackground(null);
            }
        } catch (Exception e) {
            ll_background.setBackground(null);
        }
        //
        tv_desc.setText(hmAux.get("pack_service_desc"));
        iv_foto.setImageResource(R.drawable.ic_apagar);
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
                for (int i = 0; i < data_filtered.size(); i++) {
                    String user_nick = ToolBox.AccentMapper(data_filtered.get(i).get("pack_service_desc").toLowerCase());
                    String user_name = ToolBox.AccentMapper(data_filtered.get(i).get("pack_service_desc").toLowerCase());
                    if (user_nick.contains(constraint.toString().toLowerCase()) ||
                            user_name.contains(constraint.toString().toLowerCase())
                            ) {

                        HMAux hmAux = new HMAux();
                        hmAux.putAll(data_filtered.get(i));

                        filterList.add(hmAux);

                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = data_filtered.size();
                results.values = data_filtered;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<HMAux>) results.values;

            notifyDataSetChanged();
        }
    }


}