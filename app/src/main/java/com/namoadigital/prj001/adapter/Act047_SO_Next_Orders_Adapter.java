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

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.design.list.OnRememberListState;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act047_SO_Next_Orders_Adapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<SO_Next_Orders_Obj> mValues;
    private OnRememberListState<SO_Next_Orders_Obj> rememberSO_next_listState;
    private int resource;
    private String mResource_Code;
    private String mResource_Name = "act047_next_orders_adapter";
    private HMAux hmAux_Trans;
    private ArrayList<SO_Next_Orders_Obj> mFilteredValues;
    private NextOrdersFilter mFilter;

    public Act047_SO_Next_Orders_Adapter(Context context, ArrayList<SO_Next_Orders_Obj> mValues, int resource, OnRememberListState<SO_Next_Orders_Obj> listState) {
        this.context = context;
        this.mValues = mValues;
        this.resource = resource;
        this.rememberSO_next_listState = listState;
        //
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        //
        this.mFilteredValues = mValues;
        loadTranslation();
    }

    public void changeListByFilter(ArrayList<SO_Next_Orders_Obj> list){
        this.mValues.clear();
        this.mFilteredValues.clear();
        this.mValues = list;
        this.mFilteredValues = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFilteredValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mFilteredValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }
        //
        final SO_Next_Orders_Obj item = mFilteredValues.get(position);
        //IniVars
        TextView tv_prefix_code = convertView.findViewById(R.id.act047_cell_tv_prefix_code);
        TextView tv_so_id_val = convertView.findViewById(R.id.act047_cell_tv_so_id_val);
        TextView tv_status_val = convertView.findViewById(R.id.act047_cell_tv_status_val);
        ImageView iv_block = convertView.findViewById(R.id.act047_cell_iv_block);
        TextView tv_priority_val = convertView.findViewById(R.id.act047_cell_tv_priority_val);
        TextView tv_deadline_val = convertView.findViewById(R.id.act047_cell_tv_deadline_val);
        TextView tv_serial_id = convertView.findViewById(R.id.act047_cell_tv_serial_id);
        TextView tv_tracking_val = convertView.findViewById(R.id.act047_cell_tv_tracking_val);
        TextView tv_brand_model_color = convertView.findViewById(R.id.act047_cell_tv_brand_model_color_val);
        TextView tv_segment_category_val = convertView.findViewById(R.id.act047_cell_tv_segment_category_val);
        TextView tv_pipeline_val = convertView.findViewById(R.id.act047_cell_tv_pipeline_val);
        TextView tv_client_so_id_val = convertView.findViewById(R.id.act047_cell_tv_client_so_id_val);
        TextView create_date = convertView.findViewById(R.id.act047_cell_tv_create_date_val);
        //
        //Seta Valores
        tv_prefix_code.setText(item.getSo_prefix() + "." + item.getSo_code());
        //
        StringBuilder value = new StringBuilder();
        if (!item.getSo_id().equals(item.getSo_prefix() + "." + item.getSo_code())) {

            value.append(item.getSo_id());

            if (item.getClient_so_id() != null && !item.getClient_so_id().isEmpty()) {
                value.append(" | ").append(item.getClient_so_id());
            }

            tv_so_id_val.setVisibility(View.VISIBLE);
        } else {
            if (item.getClient_so_id() != null && !item.getClient_so_id().isEmpty()) {
                value.append(item.getClient_so_id());
                tv_so_id_val.setVisibility(View.VISIBLE);
            } else {
                tv_so_id_val.setVisibility(View.GONE);
            }

        }
        tv_so_id_val.setText(value.toString());
        //
        tv_status_val.setText((hmAux_Trans.get(item.getStatus())));
        tv_status_val.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(item.getStatus())));
        iv_block.setVisibility(View.GONE);
        if(item.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)){
            iv_block.setVisibility(View.VISIBLE);
        }
        //
        if (item.getPriority_desc() != null || !item.getPriority_desc().isEmpty()) {
            tv_priority_val.setText(item.getPriority_desc());
            tv_priority_val.setVisibility(View.VISIBLE);
        } else {
            tv_priority_val.setVisibility(View.GONE);
        }
        //
        if (item.getDeadline() == null || item.getDeadline().isEmpty()) {
            tv_deadline_val.setText(hmAux_Trans.get("no_deadline_lbl"));
            tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
        } else {

            String customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context);
            String deadlineTime = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(item.getDeadline()),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );

            if (ToolBox_Inf.isItemLate(item.getDeadline())) {
                tv_deadline_val.setTextColor(context.getResources().getColor(R.color.text_red));
            } else {
                tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
            }
            tv_deadline_val.setText(deadlineTime);

        }
        //
        tv_serial_id.setText(item.getSerial_id());
        //
        if(item.getTracking() == null || item.getTracking().isEmpty()) {
            tv_tracking_val.setVisibility(View.GONE);
        }else{
            tv_tracking_val.setVisibility(View.VISIBLE);
            tv_tracking_val.setText(item.getTracking());
        }
        //
        if(item.getBrand_model_color() == null || item.getBrand_model_color().isEmpty()){
            tv_brand_model_color.setVisibility(View.GONE);
        }else{
            tv_brand_model_color.setVisibility(View.VISIBLE);
            tv_brand_model_color.setText(item.getBrand_model_color());
        }
        //Segment
        if(item.getSegment_category_price() != null && !item.getSegment_category_price().isEmpty()){
            tv_segment_category_val.setText(item.getSegment_category_price());
            tv_segment_category_val.setVisibility(View.VISIBLE);
        } else {
            tv_segment_category_val.setVisibility(View.GONE);
        }
        //Pipeline
        if (item.getPipeline_desc() != null && !item.getPipeline_desc().isEmpty()) {
            tv_pipeline_val.setText(item.getPipeline_desc());
            tv_pipeline_val.setVisibility(View.VISIBLE);
        } else {
            tv_pipeline_val.setVisibility(View.GONE);
        }

        create_date.setText(hmAux_Trans.get("create_date_lbl") + " " + ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.getCreateDate()),
                ToolBox_Inf.nlsDateFormat(context)));

        return convertView;
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("so_main_title");
        translateList.add("so_id_lbl");
        translateList.add("so_desc_lbl");
        translateList.add("operation_lbl");
        translateList.add("status_lbl");
        translateList.add("priority_lbl");
        translateList.add("deadline_lbl");
        translateList.add("serial_main_title");
        translateList.add("product_lbl");
        translateList.add("tracking_lbl");
        translateList.add("dialog_so_desc_lbl");
        translateList.add("dialog_services_lbl");
        translateList.add("dialog_so_comment_lbl");
        translateList.add("dialog_so_details_ttl");
        //
        translateList.add("segment_category_price_lbl");
        translateList.add("pipeline_lbl");
        translateList.add("client_so_id");
        translateList.add("no_deadline_lbl");
        translateList.add("create_date_lbl");

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
        if(mFilter == null){
            mFilter = new NextOrdersFilter();
        }
        return mFilter;
    }

    private class NextOrdersFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SO_Next_Orders_Obj> temp;
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
            if (charString.isEmpty()) {
                temp = mValues;
            } else {
                ArrayList<SO_Next_Orders_Obj> filteredList = new ArrayList<>();
                for (SO_Next_Orders_Obj row : mValues) {
                    //Resgata todos os campos concatenado e com remoção de acentuacao
                    String rowFields = ToolBox.AccentMapper(row.getAllFieldForFilter().toLowerCase());if (rowFields.contains(charString)) {
                        filteredList.add(row);
                    }
                }
                temp = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = temp.size();
            filterResults.values = temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            mFilteredValues = ((ArrayList<SO_Next_Orders_Obj>) results.values);
            rememberSO_next_listState.dataChanged(mFilteredValues);
            notifyDataSetChanged();
        }
    }





}
