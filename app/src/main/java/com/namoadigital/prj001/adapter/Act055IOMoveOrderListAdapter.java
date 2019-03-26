package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

public class Act055IOMoveOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final boolean isOnline;
    private final boolean serial_jump;
    private List<IO_Move_Search_Record> mValues;
    private Context context;
    private Act055ListListener mListener;
    private HMAux hmAux_Trans;

    public Act055IOMoveOrderListAdapter(Context context, List<IO_Move_Search_Record> mValues, Act055ListListener mListener, HMAux hmAux_Trans, boolean isOnline, boolean serial_jump) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
        this.hmAux_Trans = hmAux_Trans;
        this.isOnline = isOnline;
        this.serial_jump = serial_jump;

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
        final IO_Move_Search_Record record = mValues.get(position);
        vh.bindData(record);
        vh.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleListItemClick(record);

            }
        });
        if (serial_jump) {
            handleListItemClick(record);
        }

    }

    private void handleListItemClick(IO_Move_Search_Record record) {
        if (!record.getSite_code().equals(ToolBox_Con.getPreference_Site_Code(context))) {
            mListener.showAlertSerialOut(hmAux_Trans.get("alert_serial_out_site_title"), hmAux_Trans.get("alert_serial_out_site_msg"));
        } else {
            mListener.onClickListItem(record);
        }
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
            tv_io_product_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_product_ext_code_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_product_desc_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_serial_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_serial_ext_code_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_serial_ext_code_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_serial_desc = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_move_order_list_position = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_move_order_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_move_order_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_inbound_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_inbound_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_current_position_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_current_position_zone_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_current_position_local_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_suggested_position_lbl = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_suggested_position_zone_val = itemView.findViewById(R.id.act055_main_cl_background);
            tv_io_suggested_position_local_val = itemView.findViewById(R.id.act055_main_cl_background);
        }

        public View getItemView() {
            return itemVIew;
        }

        public void bindData(IO_Move_Search_Record data) {
            tv_io_product_lbl.setText(hmAux_Trans.get(""));
            tv_io_product_ext_code_val.setText(data.getProduct_id());
            tv_io_product_desc_val.setText(data.getProduct_desc());
            tv_io_serial_lbl.setText(hmAux_Trans.get(""));
            tv_io_serial_ext_code_lbl.setText(hmAux_Trans.get(""));
            tv_io_serial_ext_code_val.setText(data.getSerial_code());
            tv_io_serial_desc.setText(formatSerialBrandModelColor(data));
            tv_io_move_order_list_position.setText(getAdapterPosition() + 1);
            tv_io_move_order_lbl.setText(hmAux_Trans.get(""));
            tv_io_move_order_val.setText(formatPrefixSufix(data.getMove_prefix(),data.getMove_code()));
            tv_io_inbound_lbl.setText(hmAux_Trans.get(""));
            tv_io_inbound_val.setText(formatPrefixSufix(data.getInbound_prefix(), data.getInbound_code()));
            tv_io_current_position_lbl.setText(hmAux_Trans.get(""));
            tv_io_current_position_zone_val.setText(data.getZone_id());
            tv_io_current_position_local_val.setText(data.getLocal_id());
            tv_io_suggested_position_lbl.setText(hmAux_Trans.get(""));
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
}
