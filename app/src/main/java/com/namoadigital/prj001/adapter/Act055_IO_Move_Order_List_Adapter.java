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
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.List;

public class Act055_IO_Move_Order_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

//    private final boolean serial_jump;
    private List<IO_Move_Search_Record> mValues;
    private List<IO_Move_Search_Record> mFilteredValues;
    private IOMoveOrderFilter valueFilter;
    private Context context;
    private Act055ListListener mListener;
    private HMAux hmAux_Trans;

    public Act055_IO_Move_Order_List_Adapter(Context context, List<IO_Move_Search_Record> mValues, Act055ListListener mListener, HMAux hmAux_Trans) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
        this.hmAux_Trans = hmAux_Trans;
        this.mValues = mValues;
        this.mFilteredValues = (mValues);
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
            tv_io_product_lbl.setText("Produto - trad");
//            tv_io_product_lbl.setText(hmAux_Trans.get(""));
            tv_io_product_ext_code_val.setText(data.getProduct_id());
            tv_io_product_desc_val.setText(data.getProduct_desc());
            tv_io_serial_lbl.setText("Serial - trad");
//            tv_io_serial_lbl.setText(hmAux_Trans.get(""));
            tv_io_serial_ext_code_lbl.setText("Cod. Ext: - trad");
            tv_io_serial_ext_code_val.setText(String.valueOf(data.getSerial_code()));
            tv_io_serial_desc.setText(formatSerialBrandModelColor(data));
            tv_io_move_order_list_position.setText(String.valueOf(getAdapterPosition() + 1));
            tv_io_move_order_lbl.setText("Ordem De Mov - trad");
            tv_io_move_order_val.setText(formatPrefixSufix(data.getMove_prefix(),data.getMove_code()));
            tv_io_inbound_lbl.setText("Inbound - trad");
            if(data.getInbound_prefix() != null || data.getInbound_code() != null) {
                tv_io_inbound_val.setText(formatPrefixSufix(data.getInbound_prefix(), data.getInbound_code()));
            }else{
                tv_io_inbound_val.setText("Não possui inbound -trad");
            }
            tv_io_current_position_lbl.setText("Posição Atual - trad");
            tv_io_current_position_zone_val.setText(data.getZone_id());
            tv_io_current_position_local_val.setText(data.getLocal_id());
            tv_io_suggested_position_lbl.setText("Posição Sugerida - trad");
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

        void showAlertSerialOut(String alert_serial_out_site_title, String alert_serial_out_site_msg);

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

                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
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
