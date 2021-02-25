package com.namoadigital.prj001.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.ui.act052.OnRecyclerViewClickListener;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act052_IO_Serial_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_FOOTER = 1;
    public static final int VIEW_TYPE_ITEM = 0;

    private final boolean isOnline;
    private final boolean serial_jump;
    private List<IO_Serial_Process_Record> mValues;
    private Context context;
    private OnRecyclerViewClickListener mListener;
    private HMAux hmAux_Trans;
    private String mResource_Code;
    private String mResource_Name = "act052_io_serial_list_adapter";
    private String blindSerial;

    public Act052_IO_Serial_List_Adapter(Context context, List<IO_Serial_Process_Record> mValues, OnRecyclerViewClickListener mListener, boolean isOnline, boolean serial_jump, String blindSerial) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
        this.hmAux_Trans = new HMAux();
        this.isOnline = isOnline;
        this.serial_jump = serial_jump;
        this.blindSerial = blindSerial;
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
        transList.add("alert_serial_out_site_title");
        transList.add("alert_serial_out_site_msg");
        transList.add("serial_transp_order_lbl");
        transList.add("btn_blind_serial_move");
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
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.act052_main_serial_list_item, viewGroup, false);
//        return new ListItemViewHolder(view);
        View view;
        //
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.act052_main_serial_list_item, viewGroup, false);
            return new ListItemViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.act052_footer_blind_item, viewGroup, false);
            return new BlindMoveViewHolder(view);
        }

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
                        handleListItemClick(record);

                    }
                });
                if (serial_jump) {
                    handleListItemClick(record);
                }
            }else if(viewHolder instanceof BlindMoveViewHolder){
                BlindMoveViewHolder vh = (BlindMoveViewHolder) viewHolder;
                vh.setFullSizeLayout();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void handleListItemClick(IO_Serial_Process_Record record) {
        mListener.onClickListItem(record);
    }

    @Override
    public int getItemCount() {
        if(mValues == null){
            return 0;
        }
        //
        //Se não foi passado serial, retorna tam da lista, se não add blind
        if(blindSerial == null || blindSerial.isEmpty()){
            return mValues.size();
        }
        //
        return mValues.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (mValues != null) {
            if (position == mValues.size()) {
                return VIEW_TYPE_FOOTER;
            } else {
                return VIEW_TYPE_ITEM;
            }
        } else {
            return VIEW_TYPE_FOOTER;
        }
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        protected final ConstraintLayout clBackground;
        protected final TextView tvIoProductLbl;
        protected final TextView tvIoTranspOrderLbl;
        protected final TextView tvIoTranspOrderVal;
        protected final TextView tvIoSerialLbl;
        protected final TextView tvIoSerialExtCodeLbl;
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
            clBackground = itemView.findViewById(R.id.act052_main_cl_background);
            tvIoProductLbl = itemView.findViewById(R.id.act052_tv_io_product_lbl);
            tvIoTranspOrderLbl = itemView.findViewById(R.id.act052_tv_io_transp_order_lbl);
            tvIoTranspOrderVal = itemView.findViewById(R.id.act052_tv_io_transp_order_val);
            tvIoSerialLbl = itemView.findViewById(R.id.act052_tv_io_serial_lbl);
            tvIoSerialExtCodeLbl = itemView.findViewById(R.id.act052_tv_io_serial_ext_code_lbl);
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

        public void bindData(IO_Serial_Process_Record data) {
            String processType = data.getProcess_type();
            setProcessStatus(processType);

            tvIoProductLbl.setText(hmAux_Trans.get("product_lbl"));
            tvIoSerialLbl.setText(hmAux_Trans.get("serial_lbl"));
            tvIoSerialExtCodeLbl.setText(hmAux_Trans.get("serial_code_lbl"));
            tvIoTranspOrderLbl.setText(hmAux_Trans.get("serial_transp_order_lbl"));

            tvProductExtCodeVal.setText(data.getProduct_id());
            tvProductDescVal.setText(data.getProduct_desc());
            tvSerialExtCodeVal.setText(data.getSerial_id());
            tvIoTranspOrderVal.setVisibility(View.VISIBLE);
            tvIoTranspOrderLbl.setVisibility(View.VISIBLE);
            if(data.getTransport_order() == null || data.getTransport_order().isEmpty()){
                tvIoTranspOrderVal.setVisibility(View.GONE);
                tvIoTranspOrderLbl.setVisibility(View.GONE);
            }
            tvIoTranspOrderVal.setText(data.getTransport_order());
            tvSerialZone.setText(data.getZone_desc());
            tvSerialPosition.setText(data.getLocal_id());
            setTvSerialBrandModelColor(data);

            int pos = getAdapterPosition() + 1;
            tvSerialListPosition.setText(String.valueOf(pos));
            if (isOnline) {
                ivOfflineMode.setVisibility(View.INVISIBLE);
            } else {
                ivOfflineMode.setVisibility(View.VISIBLE);
            }

            if (data.getSite_code() == null && data.getProcess_type() != null && data.getProcess_type().equalsIgnoreCase(ConstantBaseApp.IO_PROCESS_IN_CONF)) {
                clBackground.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
            }else if (data.getSite_code() != null && data.getSite_code() == Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context))) {
                clBackground.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
            } else {
                clBackground.setBackground(context.getDrawable(R.drawable.act013_cell_in_processing_states));
            }
        }

        private void setTvSerialBrandModelColor(IO_Serial_Process_Record data) {
            String serialBrandModelColor = formatSerialBrandModelColor(data);
            if (serialBrandModelColor == null || serialBrandModelColor.isEmpty()) {
                tvSerialBrandModelColor.setVisibility(View.GONE);
            } else {
                tvSerialBrandModelColor.setVisibility(View.VISIBLE);
                tvSerialBrandModelColor.setText(serialBrandModelColor);
            }
        }

        private String formatSerialBrandModelColor(IO_Serial_Process_Record data) {
            String serialBrandModelColor = data.getBrand_desc() == null || data.getBrand_desc().isEmpty()? "" : data.getBrand_desc();
            serialBrandModelColor = serialBrandModelColor + (data.getModel_desc() == null || data.getModel_desc().isEmpty()? "" : " | " + data.getModel_desc());
            serialBrandModelColor = serialBrandModelColor + (data.getColor_desc() == null || data.getColor_desc().isEmpty()? "" : " | " + data.getColor_desc());
            return serialBrandModelColor;
        }

        private void setProcessStatus(String processType) {
            if(processType == null){
                processType = "";
            }
            switch (processType) {
                case ConstantBaseApp.IO_PROCESS_IN_CONF:
                    ivStatusIcon.setVisibility(View.VISIBLE);
                    ivStatusIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_right_bold_black_24dp));
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                    ivStatusIcon.setVisibility(View.VISIBLE);
                    ivStatusIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_left_bold_black_24dp));
                    break;
                case ConstantBaseApp.IO_INBOUND:
                case ConstantBaseApp.IO_OUTBOUND:
                case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                case ConstantBaseApp.IO_PROCESS_MOVE:
                case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
                case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                case ConstantBaseApp.SYS_STATUS_PUT_AWAY:
                case ConstantBaseApp.SYS_STATUS_PICKING:
                    ivStatusIcon.setVisibility(View.VISIBLE);
                    ivStatusIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_swap_horiz_black_24dp));
                    break;
                default:
                    ivStatusIcon.setVisibility(View.GONE);
            }
            tvStatusDesc.setText(hmAux_Trans.get(processType));
        }
    }

    public class BlindMoveViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout clMain;
        private Button btnBlindMove;
        private View itemView;

        public BlindMoveViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            this.itemView = itemView;
            clMain = itemView.findViewById(R.id.act052_blind_item_cl_main);
            btnBlindMove = itemView.findViewById(R.id.act052_blind_item_btn_blind);
            btnBlindMove.setText(hmAux_Trans.get("btn_blind_serial_move") + " (" + blindSerial + ")");
            btnBlindMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onBlindMoveClick();
                    }
                }
            });
        }
        //

        public void setFullSizeLayout() {
            //Se unico item na lista, reseta o layout para pegar o tam do recycle
            if(getItemCount() == 1) {
                ConstraintLayout.LayoutParams params =
                        new ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        );
                //
                clMain.setLayoutParams(params);
            }
        }
    }

}
