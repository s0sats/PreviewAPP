package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act055_IO_Move_Order_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

//    private final boolean serial_jump;
    private List<IO_Move_Search_Record> mValues;
    private List<IO_Move_Search_Record> mFilteredValues;
    private IOMoveOrderFilter valueFilter;
    private Context context;
    private Act055ListListener mListener;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act055_io_move_order_list_adapter";

    public Act055_IO_Move_Order_List_Adapter(Context context, List<IO_Move_Search_Record> mValues, Act055ListListener mListener, HMAux hmAux_Trans) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
        this.hmAux_Trans = new HMAux();
        this.mValues = mValues;
        this.mFilteredValues = (mValues);

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("serial_code_lbl");
        transList.add("move_order_lbl");
        transList.add("inbound_lbl");
        transList.add("inbound_not_found");
        transList.add("current_position_lbl");
        transList.add("suggested_position_lbl");
        transList.add(ConstantBaseApp.IO_INBOUND);
        transList.add(ConstantBaseApp.IO_OUTBOUND);
        transList.add(ConstantBaseApp.IO_PROCESS_MOVE_PLANNED);
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.act055_io_move_cell, viewGroup, false);
        return new ListItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ListItemViewHolder vh = (ListItemViewHolder) viewHolder;
        final IO_Move_Search_Record record = mFilteredValues.get(position);
        vh.bindData(record);
        vh.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleListItemClick(record);

            }
        });
//        if (serial_jump) {
//            handleListItemClick(record);
//        }

    }

    private void handleListItemClick(IO_Move_Search_Record record) {
        if (!record.getSite_code().equals(ToolBox_Con.getPreference_Site_Code(context))) {
            mListener.showAlertSerialOut(hmAux_Trans.get("alert_serial_out_site_title"), hmAux_Trans.get("alert_serial_out_site_msg"));
        } else {
            mListener.onClickListItem(record);
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new IOMoveOrderFilter();
        }
        return valueFilter;
    }

    @Override
    public int getItemCount() {
        return mFilteredValues.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        protected final ConstraintLayout main_cl_background;
        protected final TextView tv_io_process_type;
        protected final ImageView iv_io_process_type;
        protected final TextView tv_io_product_lbl;
        protected final TextView tv_io_product_ext_code_val;
        protected final TextView tv_io_product_desc_val;
        protected final TextView tv_io_serial_lbl;
        protected final TextView tv_io_serial_ext_code_lbl;
        protected final TextView tv_io_serial_ext_code_val;
        protected final TextView tv_io_serial_desc;
        protected final TextView tv_io_move_order_list_position;
        protected final TextView tv_io_move_order_lbl;
        protected final TextView tv_io_move_order_val;
        protected final TextView tv_io_inbound_lbl;
        protected final TextView tv_io_inbound_val;
        protected final TextView tv_io_current_position_lbl;
        protected final TextView tv_io_current_position_zone_local_val;
        protected final TextView tv_io_suggested_position_lbl;
        protected final TextView tv_io_suggested_position_zone_local_val;
        private final View itemVIew;


        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemVIew = itemView;
            main_cl_background = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_product_lbl = itemView.findViewById(R.id.act055_tv_io_product_lbl);
            tv_io_process_type = itemView.findViewById(R.id.act055_tv_io_process_type);
            iv_io_process_type = itemView.findViewById(R.id.act055_iv_io_process_type);
            tv_io_product_ext_code_val = itemView.findViewById(R.id.act055_tv_io_product_ext_code_val);
            tv_io_product_desc_val = itemView.findViewById(R.id.act055_tv_io_product_desc_val);
            tv_io_serial_lbl = itemView.findViewById(R.id.act055_tv_io_serial_lbl);
            tv_io_serial_ext_code_lbl = itemView.findViewById(R.id.act055_tv_io_serial_ext_code_lbl);
            tv_io_serial_ext_code_val = itemView.findViewById(R.id.act055_tv_io_serial_ext_code_val);
            tv_io_serial_desc = itemView.findViewById(R.id.act055_tv_io_serial_desc);
            tv_io_move_order_list_position = itemView.findViewById(R.id.act055_tv_io_move_order_list_position);
            tv_io_move_order_lbl = itemView.findViewById(R.id.act055_tv_io_move_order_lbl);
            tv_io_move_order_val = itemView.findViewById(R.id.act055_tv_io_move_order_val);
            tv_io_inbound_lbl = itemView.findViewById(R.id.act055_tv_io_inbound_lbl);
            tv_io_inbound_val = itemView.findViewById(R.id.act055_tv_io_inbound_val);
            tv_io_current_position_lbl = itemView.findViewById(R.id.act055_tv_io_current_position_lbl);
            tv_io_current_position_zone_local_val = itemView.findViewById(R.id.act055_tv_io_current_position_zone_local_val);
            tv_io_suggested_position_lbl = itemView.findViewById(R.id.act055_tv_io_suggested_position_lbl);
            tv_io_suggested_position_zone_local_val = itemView.findViewById(R.id.act055_tv_io_suggested_position_zone_local_val);
        }

        public View getItemView() {
            return itemVIew;
        }

        public void bindData(IO_Move_Search_Record data) {

            tv_io_product_lbl.setText(hmAux_Trans.get("product_lbl"));
            tv_io_product_ext_code_val.setText(data.getProduct_id());
            tv_io_product_desc_val.setText(data.getProduct_desc());

            tv_io_serial_lbl.setText(hmAux_Trans.get("serial_lbl"));
            tv_io_serial_ext_code_lbl.setText(hmAux_Trans.get("serial_code_lbl"));
            tv_io_serial_ext_code_val.setText(String.valueOf(data.getSerial_code()));
            tv_io_serial_desc.setText(formatSerialBrandModelColor(data));
            if(formatSerialBrandModelColor(data).isEmpty()){
                tv_io_serial_desc.setVisibility(View.GONE);
            }
            tv_io_move_order_list_position.setText(String.valueOf(getAdapterPosition() + 1));
            tv_io_move_order_lbl.setText(hmAux_Trans.get("move_order_lbl"));
            tv_io_move_order_val.setText(formatPrefixSufix(data.getMove_prefix(),data.getMove_code()));

            setProcessStatus(data.getMove_type());

            if(data.getInbound_prefix() == null || data.getInbound_code() == null) {
                tv_io_inbound_val.setVisibility(View.GONE);
                tv_io_inbound_lbl.setVisibility(View.GONE);
            }else{
                tv_io_inbound_val.setVisibility(View.VISIBLE);
                tv_io_inbound_lbl.setVisibility(View.VISIBLE);
                tv_io_inbound_val.setText(formatPrefixSufix(data.getInbound_prefix(), data.getInbound_code()));
                tv_io_inbound_lbl.setText(hmAux_Trans.get("inbound_lbl"));
            }

            if(data.getZone_id() == null || data.getZone_id().isEmpty()){
                tv_io_current_position_lbl.setVisibility(View.GONE);
                tv_io_current_position_zone_local_val.setVisibility(View.GONE);
            }else{
                tv_io_current_position_lbl.setVisibility(View.VISIBLE);
                tv_io_current_position_zone_local_val.setVisibility(View.VISIBLE);
                formatZoneLocal(data.getZone_id(), data.getLocal_id(),tv_io_current_position_zone_local_val );
                tv_io_current_position_lbl.setText(hmAux_Trans.get("current_position_lbl"));
            }

            if(data.getPlanned_zone_id() == null ||data.getPlanned_zone_id().isEmpty()){
                tv_io_suggested_position_lbl.setVisibility(View.GONE);
                tv_io_suggested_position_zone_local_val.setVisibility(View.GONE);
            }else{
                tv_io_current_position_lbl.setVisibility(View.VISIBLE);
                tv_io_current_position_zone_local_val.setVisibility(View.VISIBLE);
                formatZoneLocal(data.getPlanned_zone_id(), data.getPlanned_local_id(),tv_io_suggested_position_zone_local_val );
                tv_io_suggested_position_lbl.setText(hmAux_Trans.get("suggested_position_lbl"));
            }
        }

        private void formatZoneLocal(String zone, String local, TextView tv_zone_local) {
            String zoneLocal = zone;
            if(local!=null && !local.isEmpty()){
                zoneLocal = zoneLocal.concat(" | " + local);
            }
            tv_zone_local.setText(zoneLocal);
        }

        @NonNull
        private String formatPrefixSufix(int prefix, int sufix) {
            return prefix+"."+sufix;
        }

        private String formatSerialBrandModelColor(IO_Move_Search_Record data) {
            String serialBrandModelColor = data.getBrand_desc() == null ? "" : data.getBrand_desc();
            serialBrandModelColor = serialBrandModelColor + (data.getModel_desc() == null ? "" : " | " + data.getModel_desc());
            serialBrandModelColor = serialBrandModelColor + (data.getColor_desc() == null ? "" : " | " + data.getColor_desc());
            return serialBrandModelColor;
        }
        private void setProcessStatus(String processType) {
            if(processType == null){
                processType = "";
            }

            switch (processType) {
                case ConstantBaseApp.IO_INBOUND:
                    iv_io_process_type.setBackground(context.getResources().getDrawable(R.drawable.forward_gre));
                    break;
                case ConstantBaseApp.IO_OUTBOUND:
                    iv_io_process_type.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_left_thick));
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                    iv_io_process_type.setBackground(context.getResources().getDrawable(R.drawable.ic_swap_horiz_black_24dp));
                    break;
                default:
                    iv_io_process_type.setVisibility(View.GONE);
            }
            tv_io_process_type.setText(hmAux_Trans.get(processType));
        }

    }
    public interface Act055ListListener {

        void showAlertSerialOut(String title, String msg);

        void onClickListItem(IO_Move_Search_Record record);
    }

    private class IOMoveOrderFilter extends Filter{
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            if (charString.isEmpty()) {
                mFilteredValues = mValues;
            } else {
                List<IO_Move_Search_Record> filteredList = new ArrayList<>();
                for (IO_Move_Search_Record row : mValues) {
                    if (row.getAllFieldForFilter().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row);
                    }
                }
                mFilteredValues = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mFilteredValues;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            mFilteredValues = (ArrayList<IO_Move_Search_Record>) results.values;
            notifyDataSetChanged();
        }
    }
}
