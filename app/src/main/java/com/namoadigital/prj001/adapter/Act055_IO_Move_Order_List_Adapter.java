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
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.util.Constant;
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
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

//    public Act055_IO_Move_Order_List_Adapter(Context context, List<IO_Move_Search_Record> mValues, Act055ListListener mListener, HMAux hmAux_Trans, boolean serial_jump) {
//        this.context = context;
//        this.mValues = mValues;
//        this.mListener = mListener;
//        this.hmAux_Trans = hmAux_Trans;
//        this.serial_jump = serial_jump;
//        this.mValues = mValues;
//        this.mFilteredValues = (mValues);
//    }

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
        final IO_Move_Search_Record record = mValues.get(position);
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
        return mValues.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        protected final ConstraintLayout main_cl_background;
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
        protected final TextView tv_io_current_position_zone_val;
        protected final TextView tv_io_current_position_local_val;
        protected final TextView tv_io_suggested_position_lbl;
        protected final TextView tv_io_suggested_position_zone_val;
        protected final TextView tv_io_suggested_position_local_val;
        private final View itemVIew;


        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemVIew = itemView;
            main_cl_background = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_product_lbl = itemView.findViewById(R.id.act055_tv_io_product_lbl);
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
            tv_io_current_position_zone_val = itemView.findViewById(R.id.act055_tv_io_current_position_zone_val);
            tv_io_current_position_local_val = itemView.findViewById(R.id.act055_tv_io_current_position_local_val);
            tv_io_suggested_position_lbl = itemView.findViewById(R.id.act055_tv_io_suggested_position_lbl);
            tv_io_suggested_position_zone_val = itemView.findViewById(R.id.act055_tv_io_suggested_position_zone_val);
            tv_io_suggested_position_local_val = itemView.findViewById(R.id.act055_tv_io_suggested_position_local_val);
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
            tv_io_move_order_list_position.setText(String.valueOf(getAdapterPosition() + 1));
            tv_io_move_order_lbl.setText(hmAux_Trans.get("move_order_lbl"));
            tv_io_move_order_val.setText(formatPrefixSufix(data.getMove_prefix(),data.getMove_code()));
            tv_io_inbound_lbl.setText(hmAux_Trans.get("inbound_lbl"));
            if(data.getInbound_prefix() == null || data.getInbound_code() == null) {
                tv_io_inbound_val.setVisibility(View.GONE);
            }else{
                tv_io_inbound_val.setVisibility(View.VISIBLE);
                tv_io_inbound_val.setText(formatPrefixSufix(data.getInbound_prefix(), data.getInbound_code()));
            }
            tv_io_current_position_lbl.setText(hmAux_Trans.get("current_position_lbl"));
            tv_io_current_position_zone_val.setText(data.getZone_id());
            tv_io_current_position_local_val.setText(data.getLocal_id());
            tv_io_suggested_position_lbl.setText(hmAux_Trans.get("suggested_position_lbl"));
            tv_io_suggested_position_zone_val.setText(data.getPlanned_zone_id());
            tv_io_suggested_position_local_val.setText(data.getPlanned_local_id());

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
