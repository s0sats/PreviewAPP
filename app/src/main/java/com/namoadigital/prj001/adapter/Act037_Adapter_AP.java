package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 2/26/18.
 */

public class Act037_Adapter_AP extends BaseAdapter implements Filterable {

    private Context context;
    private int resource_01;
    //
    private ArrayList<HMAux> data;
    private ArrayList<HMAux> data_filtered;
    private String mResource_Code;
    private String mResource_Name = "act037_adapter_ap";
    private HMAux hmAux_Trans;
    private ValueFilter valueFilter;

    public Act037_Adapter_AP(Context context, int resource_01, ArrayList<HMAux> data, String mket_filter ) {
        this.context = context;
        this.resource_01 = resource_01;
        this.data = data;
        this.hmAux_Trans = new HMAux();
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
        //
        this.data_filtered = data;
        getFilter().filter(mket_filter);
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
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        HMAux item = data.get(position);

        try {

        } catch (Exception e) {
        }

        return 0;
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
        // Data Acess
        HMAux item = data.get(position);
        // View Settring
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {

                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;
                default:
                    break;
            }
        }
        //
        switch (getItemViewType(position)) {
            // Normal AP
            case 0:
                processNormalAP(item, convertView);
                break;
            default:
                break;
        }

        return convertView;
    }

    /*
    private void processNormalAP(HMAux item, View convertView) {
        TextView tv_cf_desc = (TextView) convertView.findViewById(R.id.act037_main_content_cell_ap_normal_tv_cf_desc);
        TextView tv_cf_type_desc = (TextView) convertView.findViewById(R.id.act037_main_content_cell_ap_normal_tv_cf_type_desc);
        //
        tv_cf_desc.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC));
        tv_cf_type_desc.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC));
    }
    */
    private void processNormalAP(HMAux item, View convertView) {
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.namoa_ap_tv_form_ttl);
        TextView tv_type = (TextView) convertView.findViewById(R.id.namoa_ap_tv_type_label);
        TextView tv_form_label = (TextView) convertView.findViewById(R.id.namoa_ap_tv_form_label);
        TextView tv_data_serv = (TextView) convertView.findViewById(R.id.namoa_ap_tv_data_serv_lbl);
        TextView tv_product = (TextView) convertView.findViewById(R.id.namoa_ap_tv_product_lbl);
        TextView tv_serial = (TextView) convertView.findViewById(R.id.namoa_ap_tv_serial_lbl);
        TextView tv_ap_ttl = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_ttl);
        TextView tv_ap_code = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_code_lbl);
        TextView tv_ap_status = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_status_lbl);
        TextView tv_ap_what = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_what_lbl);
        TextView tv_ap_who = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_who_lbl);
        TextView tv_ap_when = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_when_lbl);
        //
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_type_val);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_form_val);
        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_data_serv_val);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_product_val);
        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_serial_val);
        TextView tv_ap_code_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_code_val);
        TextView tv_ap_status_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_status_val);
        TextView tv_ap_what_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_what_val);
        TextView tv_ap_when_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_when_val);
        TextView tv_ap_who_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_who_val);
        LinearLayout ll_action_btn = (LinearLayout) convertView.findViewById(R.id.namoa_ap_ll_action_btn);
        //
        ll_action_btn.setVisibility(View.GONE);
        //
        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
        tv_type.setText(hmAux_Trans.get("form_type_lbl"));
        tv_type_val.setText(
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE) + " - " +
                        item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC)
        );
        //
        tv_form_label.setText(hmAux_Trans.get("form_code_lbl"));
        tv_form_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE) + " - " +
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION) + " - " +
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC)
        );
        //
        tv_data_serv.setText(hmAux_Trans.get("form_data_lbl"));
        tv_data_serv_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        //
        tv_product.setText(hmAux_Trans.get("product_code_lbl"));
        tv_product_val.setText(
                item.get(GE_Custom_Form_ApDao.PRODUCT_ID) + " - " +
                item.get(GE_Custom_Form_ApDao.PRODUCT_DESC)
        );
        //
        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
        tv_serial_val.setText(item.get(GE_Custom_Form_ApDao.SERIAL_ID));
        //
        tv_ap_ttl.setText(hmAux_Trans.get("ap_ttl"));
        //
        tv_ap_code.setText(hmAux_Trans.get("ap_code_lbl"));
        tv_ap_code_val.setText(
                item.get(GE_Custom_Form_ApDao.AP_CODE) + " - " +
                item.get(GE_Custom_Form_ApDao.AP_DESCRIPTION)
        );
        tv_ap_status.setText(hmAux_Trans.get("ap_status_lbl"));
        tv_ap_status_val.setText(
                hmAux_Trans.get(item.get(
                        GE_Custom_Form_ApDao.AP_STATUS
                        )
                )
        );
        ToolBox_Inf.setAPStatusColor(
                context,
                tv_ap_status_val,
                item.get(GE_Custom_Form_ApDao.AP_STATUS)
        );
        tv_ap_what.setText(hmAux_Trans.get("ap_what_lbl"));
        tv_ap_what_val.setText(
                        ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(item.get(GE_Custom_Form_ApDao.AP_WHAT)), 45)
        );
        tv_ap_who.setText(hmAux_Trans.get("ap_who_lbl"));
        tv_ap_who_val.setText(
                        item.get(GE_Custom_Form_ApDao.AP_WHO_NICK)
        );
        tv_ap_when.setText( hmAux_Trans.get("ap_when_lbl"));
        tv_ap_when_val.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
        );
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("form_ttl");
        translateList.add("form_type_lbl");
        translateList.add("form_code_lbl");
        translateList.add("form_data_lbl");
        translateList.add("product_code_lbl");
        translateList.add("serial_lbl");
        translateList.add("ap_ttl");
        translateList.add("ap_code_lbl");
        translateList.add("ap_status_lbl");
        translateList.add("ap_what_lbl");
        translateList.add("ap_who_lbl");
        translateList.add("ap_when_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            constraint = ToolBox_Inf.AccentMapper(constraint.toString().toLowerCase());

            if (constraint != null && constraint.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<HMAux>();
                for (HMAux hmAux : data_filtered) {
                    String ap_desc = ToolBox_Inf.AccentMapper(hmAux.get(GE_Custom_Form_ApDao.AP_DESCRIPTION).toLowerCase());
                    if (ap_desc.contains(constraint.toString().toLowerCase())) {
                        filterList.add(hmAux);
                    }
                }
                //
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
            //
            notifyDataSetChanged();
        }
    }
}
