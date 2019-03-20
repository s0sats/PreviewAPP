package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.ui.act052.OnRecyclerViewClickListener;

import java.util.List;

public class Act052_IO_Serial_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final boolean isOnline;
    private List<IO_Serial_Process_Record> mValues;
    private Context context;
    private OnRecyclerViewClickListener mListener;
    private HMAux hmAux_Trans;

    private static final String IN_CONF  = "IN_CONF";
    private static final String IN_PUT_AWAY  = "IN_PUT_AWAY";
    private static final String MOVE_PLANNED  = "MOVE_PLANNED";
    private static final String MOVE  = "MOVE";
    private static final String OUT_PICKING = "OUT_PICKING";
    private static final String OUT_CONF  = "OUT_CONF";

    public Act052_IO_Serial_List_Adapter(Context context,List<IO_Serial_Process_Record> mValues, OnRecyclerViewClickListener mListener, HMAux hmAux_Trans, boolean isOnline) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
        this.hmAux_Trans = hmAux_Trans;
        this.isOnline = isOnline;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == R.layout.act052_rv_blind_move_btn) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.act052_rv_blind_move_btn, viewGroup, false);
            return new FooterViewHolder(view);
        }
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.act052_main_serial_list_itemv2, viewGroup, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        try {
            if (viewHolder instanceof ListItemViewHolder) {
                ListItemViewHolder vh = (ListItemViewHolder) viewHolder;
                final IO_Serial_Process_Record record = mValues.get(position);
                vh.bindData(record);
                vh.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClickListItem(record);
                    }
                });
            } else if (viewHolder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) viewHolder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        //Tratamento para visualização de botão de movimento offline
        if (mValues == null) {
            return 0;
        }

        if (mValues.size() == 0) {
            return 1;
        }

        return mValues.size() + 1;

    }

    @Override
    public int getItemViewType(int position) {
        return (position == mValues.size()) ? R.layout.act052_rv_blind_move_btn : R.layout.act052_main_serial_list_itemv2;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        protected final TextView tvStatusDesc;
        protected final TextView tvProductExtCodeVal;
        protected final TextView tvProductDescVal;
        protected final TextView tvSerialExtCodeVal;
        protected final TextView tvSerialBrandModelColor;
        protected final TextView tvSerialZone;
        protected final TextView tvSerialPosition;
        protected final TextView tvSerialListPosition;
        protected final ImageView ivOfflineMode;
        protected final ImageView ivStatusIcon;
        private final View itemVIew;


        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemVIew = itemView;
            tvStatusDesc = itemView.findViewById(R.id.act052_tv_io_status_desc);
            ivStatusIcon = itemView.findViewById(R.id.act052_tv_io_status_icon);
            tvProductExtCodeVal = itemView.findViewById(R.id.act052_tv_io_product_ext_code_val);
            tvProductDescVal = itemView.findViewById(R.id.act052_tv_io_product_desc_val);
            tvSerialExtCodeVal = itemView.findViewById(R.id.act052_tv_io_serial_ext_code_val);
            tvSerialBrandModelColor = itemView.findViewById(R.id.act052_tv_io_serial_desc);
            ivOfflineMode = itemView.findViewById(R.id.act052_main_iv_offline_mode);
            tvSerialListPosition = itemView.findViewById(R.id.act052_tv_serial_list_position);
            tvSerialZone = itemView.findViewById(R.id.act052_tv_io_serial_zone);
            tvSerialPosition = itemView.findViewById(R.id.act052_tv_io_serial_position);
        }

        public View getItemView() {
            return itemVIew;
        }

        public void bindData(IO_Serial_Process_Record data){
            String processType = data.getProcess_type();
            setProcessStatus(processType);

            tvProductExtCodeVal.setText(data.getProduct_id());
            tvProductDescVal.setText(data.getProduct_desc());
            tvSerialExtCodeVal.setText(data.getSerial_id());
            tvSerialZone.setText(data.getZone_desc());
            tvSerialPosition.setText(data.getLocal_id());
            setTvSerialBrandModelColor(data);

            int pos = getAdapterPosition() +1;
            tvSerialListPosition.setText(String.valueOf(pos));
            if(isOnline){
                ivOfflineMode.setVisibility(View.INVISIBLE);
            }else{
                ivOfflineMode.setVisibility(View.VISIBLE);
            }

        }

        private void setTvSerialBrandModelColor(IO_Serial_Process_Record data) {
            String serialBrandModelColor = formatSerialBrandModelColor(data);
            if(serialBrandModelColor == null || serialBrandModelColor.isEmpty()){
                tvSerialBrandModelColor.setVisibility(View.GONE);
            }else{
                tvSerialBrandModelColor.setText(serialBrandModelColor);
            }
        }

        private String formatSerialBrandModelColor(IO_Serial_Process_Record data) {
            String serialBrandModelColor = data.getBrand_desc() == null ? "": data.getBrand_desc() + " | " ;
            serialBrandModelColor = serialBrandModelColor + data.getModel_desc() == null ? "": data.getModel_desc() + " | ";
            serialBrandModelColor = serialBrandModelColor + data.getColor_desc() == null ? "": data.getColor_desc();
            return serialBrandModelColor;
        }

        private void setProcessStatus(String processType) {
            switch (processType){
                case IN_CONF:
                    ivStatusIcon.setBackground(context.getResources().getDrawable(R.drawable.forward_gre));
                    break;
                case OUT_CONF:
                    ivStatusIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_left_thick));
                    break;
                default:
                    ivStatusIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_swap_horiz_black_24dp));
            }
            tvStatusDesc.setText(hmAux_Trans.get(processType));
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private final Button btnBlindMove;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            btnBlindMove = itemView.findViewById(R.id.act052_btn_blind_move);
            btnBlindMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickListButton();
                }
            });
        }

    }
}
