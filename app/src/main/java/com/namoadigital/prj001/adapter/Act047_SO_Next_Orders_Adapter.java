package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

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
    private boolean reservedUserFilter = false;

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

    public void changeListByFilter(ArrayList<SO_Next_Orders_Obj> list) {
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
        TextView tv_so_id_val = convertView.findViewById(R.id.so_so_id_val);
        TextView tv_status_val = convertView.findViewById(R.id.act047_cell_tv_status_val);
        ImageView iv_block = convertView.findViewById(R.id.so_block);
        TextView tv_priority_val = convertView.findViewById(R.id.so_priority_val);
        TextView tv_deadline_val = convertView.findViewById(R.id.so_deadline_val);
        TextView tv_serial_id = convertView.findViewById(R.id.so_serial_id_val);
        TextView tv_tracking_val = convertView.findViewById(R.id.so_tracking_val);
        TextView tv_brand = convertView.findViewById(R.id.so_brand_val);
        TextView tv_model = convertView.findViewById(R.id.so_model_val);
        TextView tv_color = convertView.findViewById(R.id.so_color_val);
        TextView tv_segment_category_val = convertView.findViewById(R.id.so_segment_category_val);
        TextView tv_pipeline_val = convertView.findViewById(R.id.so_pipeline_val);
        TextView create_date = convertView.findViewById(R.id.so_create_date_val);
        ImageView icon_schedule = convertView.findViewById(R.id.so_left_icon);
        ImageView icon_clouds = convertView.findViewById(R.id.so_right_icon);
        TextView tv_site = convertView.findViewById(R.id.so_site_val);
        LinearLayout layout_services = convertView.findViewById(R.id.so_service_layout);
        TextView tv_service = convertView.findViewById(R.id.so_service_first);
        TextView tv_is_reserved = convertView.findViewById(R.id.so_is_reserved_val);
        //
        //Seta Valores
        icon_clouds.setVisibility(View.GONE);
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
        //
        icon_schedule.setImageTintList(AppCompatResources.getColorStateList(context, R.color.m3_namoa_onSurfaceVariant));
        if (item.getDeadline() == null || item.getDeadline().isEmpty()) {
            icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.outline_calendar_month_24));
            icon_schedule.setImageTintList(AppCompatResources.getColorStateList(context, android.R.color.darker_gray));
        } else {
            if (item.getHas_client_deadline() == 1) {
                icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_calendar_clock));
            } else {
                icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.outline_calendar_month_24));
            }
        }
        //
        if (item.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)) {
            iv_block.setVisibility(View.VISIBLE);
        }
        //
        if (item.getPriority_desc() != null || !item.getPriority_desc().isEmpty()) {
            tv_priority_val.setText(item.getPriority_desc());
            tv_priority_val.setVisibility(View.VISIBLE);
            if (!item.getPriority_color().isEmpty()) {
                tv_priority_val.setTextColor(Color.parseColor(item.getPriority_color()));
            }
        } else {
            tv_priority_val.setVisibility(View.GONE);
        }
        //
        if (item.getDeadline() == null || item.getDeadline().isEmpty()) {
            tv_deadline_val.setText(hmAux_Trans.get("no_deadline_lbl"));
            tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
        } else {

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
        if (item.getSiteDesc() != null && !item.getSiteDesc().isEmpty()) {
            tv_site.setText(item.getSiteDesc());
            tv_site.setVisibility(View.VISIBLE);
        } else {
            tv_site.setVisibility(View.GONE);
        }

        tv_serial_id.setText(item.getSerial_id());
        //
        if (item.getTracking() == null || item.getTracking().isEmpty()) {
            tv_tracking_val.setVisibility(View.GONE);
        } else {
            tv_tracking_val.setVisibility(View.VISIBLE);
            tv_tracking_val.setText(item.getTracking());
        }
        //

        //


        tv_brand.setVisibility(item.getBrand_desc() == null || item.getBrand_desc().isEmpty() ? View.GONE : View.VISIBLE);
        tv_model.setVisibility(item.getModel_desc() == null || item.getModel_desc().isEmpty() ? View.GONE : View.VISIBLE);
        tv_color.setVisibility(item.getColor_desc() == null || item.getColor_desc().isEmpty() ? View.GONE : View.VISIBLE);

        tv_brand.setText(item.getBrand_desc() == null || item.getBrand_desc().isEmpty() ? "" : item.getBrand_desc());
        tv_model.setText(item.getModel_desc() == null || item.getModel_desc().isEmpty() ? "" : "| " + item.getModel_desc());
        tv_color.setText(item.getColor_desc() == null || item.getColor_desc().isEmpty() ? "" : "| " + item.getColor_desc());

        //Segment
        if (item.getSegment_category_price() != null && !item.getSegment_category_price().isEmpty()) {
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
        //
        layout_services.setVisibility(View.GONE);
        //
        if (item.getService() != null
                && !item.getService().isEmpty()) {
            String[] services = item.getService().split("\n");
            boolean isOneService = services.length == 1;
            if (isOneService) {
                layout_services.setVisibility(View.VISIBLE);
                tv_service.setText(services[0]);
            }
        }

        if (item.getReservedUser() == null) {
            tv_is_reserved.setVisibility(View.GONE);
        } else {
            tv_is_reserved.setVisibility(View.VISIBLE);
            if (item.getReservedUser().toString().equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(context))) {
                tv_is_reserved.setText(hmAux_Trans.get("so_is_reserved_lbl"));
                tv_is_reserved.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_primary));
            } else {
                tv_is_reserved.setText(hmAux_Trans.get("so_is_reserved_other_lbl"));
                tv_is_reserved.setTextColor(ContextCompat.getColor(context, R.color.m3_namoa_onSurfaceVariant));
            }
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
        translateList.add("so_is_reserved_lbl");
        translateList.add("so_is_reserved_other_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    public void applyFilter(boolean reservedUserFilter, String textFilter) {
        this.reservedUserFilter = reservedUserFilter;
        getFilter().filter(textFilter.trim());
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new NextOrdersFilter();
        }
        return mFilter;
    }

    private class NextOrdersFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SO_Next_Orders_Obj> temp;
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
            ArrayList<SO_Next_Orders_Obj> filteredList = new ArrayList<>();

            if (charString.isEmpty()) {
                for (SO_Next_Orders_Obj row : mValues) {
                    if (!reservedUserFilter || isUserReserved(row.getReservedUser())) {
                        filteredList.add(row);
                    }
                }
                temp = filteredList;
            } else {

                for (SO_Next_Orders_Obj row : mValues) {
                    //Resgata todos os campos concatenado e com remoção de acentuacao
                    String rowFields = ToolBox.AccentMapper(row.getAllFieldForFilter().toLowerCase());
                    if (rowFields.contains(charString)
                            && (!reservedUserFilter || isUserReserved(row.getReservedUser()))) {
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

        private boolean isUserReserved(Integer reservedUser) {
            if (reservedUser != null) {
                String preferenceUserCode = ToolBox_Con.getPreference_User_Code(context);
                if (!preferenceUserCode.trim().isEmpty()) {
                    return reservedUser.equals(Integer.parseInt(preferenceUserCode));
                }
            }
            return false;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            mFilteredValues = ((ArrayList<SO_Next_Orders_Obj>) results.values);
            rememberSO_next_listState.dataChanged(mFilteredValues);
            notifyDataSetChanged();
        }
    }


}
