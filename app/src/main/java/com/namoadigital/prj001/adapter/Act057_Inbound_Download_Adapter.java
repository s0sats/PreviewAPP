package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import az.plainpie.PieView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Inbound_Search_Record;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act057_Inbound_Download_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private int resource;
    private List<IO_Inbound_Search_Record> mValues;
    private List<IO_Inbound_Search_Record> mFilteredValues;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act057_inbound_download_adapter";
    private InboundDownloadFilter valueFilter;
    private boolean pendingFilter;
    private boolean processFilter;
    private int downloadCounter;
    private boolean isOnline;
    private boolean pendencies;
    private OnItemClickListner mOnItemClickListner;
    private OnItemCheckedChangeListener mOnItemCheckedChangeListener;

    public interface OnItemClickListner{
        void onItemClick(IO_Inbound_Search_Record item);
    }

    public interface OnItemCheckedChangeListener{
        void onItemCheckedChange(int downloadCounter);
    }

    public OnItemClickListner getOnItemClickListner() {
        return mOnItemClickListner;
    }

    public void setOnItemClickListner(OnItemClickListner mOnItemClickListner) {
        this.mOnItemClickListner = mOnItemClickListner;
    }

    public OnItemCheckedChangeListener getmOnItemCheckedChangeListener() {
        return mOnItemCheckedChangeListener;
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener mOnItemCheckedChangeListener) {
        this.mOnItemCheckedChangeListener = mOnItemCheckedChangeListener;
    }

    public Act057_Inbound_Download_Adapter(Context context, List<IO_Inbound_Search_Record> mValues, boolean pendingFilter, boolean processFilter, boolean isOnline , boolean pendencies) {
        this.context = context;
        this.mValues = mValues;
        this.resource = R.layout.act057_io_inbound_cell;
        this.pendingFilter = pendingFilter;
        this.processFilter = processFilter;
        this.mFilteredValues = mValues;
        this.downloadCounter = 0;
        this.isOnline = isOnline;
        this.pendencies = pendencies;
        //
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("inbound_id_lbl");
        transList.add("inbound_desc_lbl");
        transList.add("create_date_lbl");
        transList.add("eta_date_lbl");
        transList.add("invoice_lbl");
        transList.add("from_lbl");
        transList.add("modal_lbl");
        transList.add("comment_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(resource, viewGroup, false);
        return new InboundDownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        InboundDownloadViewHolder holder = (InboundDownloadViewHolder) viewHolder;
        //
        holder.bindData(mFilteredValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilteredValues.size();
    }

    public void updateStatusFilter(boolean pendingFilter,boolean processFilter){
        this.pendingFilter = pendingFilter;
        this.processFilter = processFilter;

    }

    public String getInboundsToDownload(){
        String inboundToDownload = "";
        //
        for(int i = 0;i < mFilteredValues.size(); i++){
            if(mFilteredValues.get(i).isToDownload()){
                inboundToDownload += "|"+
                            mFilteredValues.get(i).getInbound_prefix()+"."+
                            mFilteredValues.get(i).getInbound_code();
            }
        }
        //
        return inboundToDownload.substring(1);
    }

    public class InboundDownloadViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout cl_main_bg;
        private CheckBox chkDownload;
        private ImageView iv_offline;
        private TextView tv_status;
        private PieView pv_done;
        private TextView tv_inbound_id;
        private TextView tv_inbound_id_val;
        private TextView tv_inbound_desc;
        private TextView tv_inbound_desc_val;
        private TextView tv_create_dt;
        private TextView tv_create_dt_val;
        private TextView tv_eta_dt;
        private TextView tv_eta_dt_val;
        private TextView tv_invoice;
        private TextView tv_invoice_val;
        private TextView tv_from;
        private TextView tv_from_val;
        private TextView tv_modal;
        private TextView tv_modal_val;
        private TextView tv_comment;
        private TextView tv_comment_val;
        private View itemView;

        public InboundDownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            //
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListner != null) {
                        if (isOnline) {
                            chkDownload.performClick();
                        } else {
                            mOnItemClickListner.onItemClick(mFilteredValues.get(getAdapterPosition()));
                        }
                    }
                }
            });
            //
            cl_main_bg = itemView.findViewById(R.id.act057_main_cl_background);
            chkDownload = itemView.findViewById(R.id.act057_io_inbound_cell_chk_download);
            chkDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilteredValues.get(getAdapterPosition()).setToDownload(((CheckBox) v).isChecked());
                    //
                    if(((CheckBox) v).isChecked()){
                        downloadCounter++;
                    }else{
                        downloadCounter--;
                    }
                    //
                    if(mOnItemCheckedChangeListener != null){
                        mOnItemCheckedChangeListener.onItemCheckedChange(downloadCounter);
                    }
                }
            });
            iv_offline = itemView.findViewById(R.id.act057_io_inbound_cell_iv_offline);
            tv_status = itemView.findViewById(R.id.act057_io_inbound_cell_tv_status);
            pv_done = itemView.findViewById(R.id.act057_io_inbound_cell_pv_done);
            configPvUI(pv_done);
            tv_inbound_id = itemView.findViewById(R.id.act057_io_inbound_cell_tv_id);
            tv_inbound_id_val= itemView.findViewById(R.id.act057_io_inbound_cell_tv_id_val);
            tv_inbound_desc = itemView.findViewById(R.id.act057_io_inbound_cell_tv_desc);
            tv_inbound_desc_val= itemView.findViewById(R.id.act057_io_inbound_cell_tv_desc_val);
            tv_create_dt = itemView.findViewById(R.id.act057_io_inbound_cell_tv_create_date);
            tv_create_dt_val = itemView.findViewById(R.id.act057_io_inbound_cell_tv_create_date_val);
            tv_eta_dt  = itemView.findViewById(R.id.act057_io_inbound_cell_tv_eta_date);
            tv_eta_dt_val = itemView.findViewById(R.id.act057_io_inbound_cell_tv_eta_date_val);
            tv_invoice = itemView.findViewById(R.id.act057_io_inbound_cell_tv_invoice);
            tv_invoice_val = itemView.findViewById(R.id.act057_io_inbound_cell_tv_invoice_val);
            tv_from = itemView.findViewById(R.id.act057_io_inbound_cell_tv_from);
            tv_from_val = itemView.findViewById(R.id.act057_io_inbound_cell_tv_from_val);
            tv_modal = itemView.findViewById(R.id.act057_io_inbound_cell_tv_modal);
            tv_modal_val = itemView.findViewById(R.id.act057_io_inbound_cell_tv_modal_val);
            tv_comment = itemView.findViewById(R.id.act057_io_inbound_cell_tv_comment);
            tv_comment_val = itemView.findViewById(R.id.act057_io_inbound_cell_tv_comment_val);
        }

        private void configPvUI(PieView pv_done) {
            pv_done.setInnerText("\u2713");
            //pv_done.setTextColor(context.getResources().getColor(R.color.namoa_status_done));
            pv_done.setTextColor(context.getResources().getColor(R.color.font_normal));
            pv_done.setPercentageBackgroundColor(context.getResources().getColor(R.color.namoa_status_done));
            //pv_done.setMainBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            pv_done.setMainBackgroundColor(context.getResources().getColor(R.color.namoa_icon_pressed_color));
            pv_done.setInnerBackgroundColor(context.getResources().getColor(R.color.namoa_color_gray));
        }

        public View getItemView() {
            return itemView;
        }

        public void bindData(IO_Inbound_Search_Record data){
            //Esconde views
            resetVisibility();
            //
            if(!isOnline && !data.isSameSiteAsLoggedOrFree()){
                cl_main_bg.setBackground(context.getDrawable(R.drawable.act013_cell_in_processing_states));
            }
            chkDownload.setChecked(data.isToDownload());
            tv_status.setText(hmAux_Trans.get(data.getStatus()));
            tv_status.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(data.getStatus())));
            pv_done.setPercentage(data.getPerc_done() != null ?  data.getPerc_done() : 0.0f);
            //pv_done.setInnerText("✓");
            tv_inbound_id.setVisibility(View.GONE);
            tv_inbound_id_val.setVisibility(View.GONE);
            if(data.getInbound_id() != null && data.getInbound_id().trim().length() > 0){
                tv_inbound_id.setText(hmAux_Trans.get("inbound_id_lbl"));
                tv_inbound_id_val.setText(data.getInbound_id());
                tv_inbound_id.setVisibility(View.VISIBLE);
                tv_inbound_id_val.setVisibility(View.VISIBLE);
            }
            tv_inbound_desc.setVisibility(View.GONE);
            tv_inbound_desc_val.setVisibility(View.GONE);
            if(data.getInbound_desc() != null && data.getInbound_desc().trim().length() > 0){
                tv_inbound_desc.setText(hmAux_Trans.get("inbound_desc_lbl"));
                tv_inbound_desc_val.setText(data.getInbound_desc());
                tv_inbound_desc.setVisibility(View.VISIBLE);
                tv_inbound_desc_val.setVisibility(View.VISIBLE);
            }
            tv_create_dt.setVisibility(View.GONE);
            tv_create_dt_val.setVisibility(View.GONE);
            if(data.getCreate_date() != null && data.getCreate_date().trim().length() > 0){
                tv_create_dt.setText(hmAux_Trans.get("create_date_lbl"));
                tv_create_dt_val.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(data.getCreate_date()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        ));
                tv_create_dt.setVisibility(View.VISIBLE);
                tv_create_dt_val.setVisibility(View.VISIBLE);
            }
            //
            tv_eta_dt.setVisibility(View.GONE);
            tv_eta_dt_val.setVisibility(View.GONE);
            if(data.getEta_date() != null && data.getEta_date().trim().length() > 0) {
                tv_eta_dt.setText(hmAux_Trans.get("eta_date_lbl"));
                tv_eta_dt_val.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(data.getEta_date()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        ));
                //
                tv_eta_dt.setVisibility(View.VISIBLE);
                tv_eta_dt_val.setVisibility(View.VISIBLE);
            }
            //
            tv_invoice.setVisibility(View.GONE);
            tv_invoice_val.setVisibility(View.GONE);
            if(data.getInvoice_number() != null && data.getInvoice_number().trim().length() > 0) {
                tv_invoice.setText(hmAux_Trans.get("invoice_lbl"));
                tv_invoice_val.setText(data.getInvoice_number());
                //
                tv_invoice.setVisibility(View.VISIBLE);
                tv_invoice_val.setVisibility(View.VISIBLE);
            }
            //
            tv_from.setVisibility(View.GONE);
            tv_from_val.setVisibility(View.GONE);
            if(data.getFrom() != null && data.getFrom().trim().length() > 0) {
                tv_from.setText(hmAux_Trans.get("from_lbl"));
                tv_from_val.setText(data.getFrom());
                //
                tv_from.setVisibility(View.VISIBLE);
                tv_from_val.setVisibility(View.VISIBLE);
            }
            //
            if(data.getModal() != null && data.getModal().trim().length() > 0) {
                tv_modal.setText(hmAux_Trans.get("modal_lbl"));
                tv_modal_val.setText(data.getModal());
                tv_modal.setVisibility(View.VISIBLE);
                tv_modal_val.setVisibility(View.VISIBLE);
            }

            if(data.getComments() != null && data.getComments().trim().length() > 0) {
                tv_comment.setText(hmAux_Trans.get("comment_lbl"));
                tv_comment_val.setText(data.getComments());
                tv_comment.setVisibility(View.VISIBLE);
                tv_comment_val.setVisibility(View.VISIBLE);
            }
            //Se lista de pendents, não exibe icone nem checkbox
            if(pendencies){
                chkDownload.setVisibility(View.GONE);
                chkDownload.setOnClickListener(null);
                iv_offline.setVisibility(View.GONE);
            }
        }

        private void resetVisibility() {
            cl_main_bg.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
            chkDownload.setVisibility(isOnline ? View.VISIBLE : View.GONE);
            iv_offline.setVisibility(isOnline ? View.GONE :View.VISIBLE);
            tv_modal.setVisibility(View.GONE);
            tv_modal_val.setVisibility(View.GONE);
            tv_comment.setVisibility(View.GONE);
            tv_comment_val.setVisibility(View.GONE);
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new InboundDownloadFilter();
        }
        return valueFilter;
    }

    private class InboundDownloadFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
//            if (charString.isEmpty()) {
//                mFilteredValues = mValues;
//            } else {
                List<IO_Inbound_Search_Record> filteredList = new ArrayList<>();
                for (IO_Inbound_Search_Record row : mValues) {
                    //Resgata todos os campos concatenado e com remoção de acentuacao
                    String rowFields = ToolBox.AccentMapper(row.getAllFieldForFilter().toLowerCase());
                    if (rowFields.contains(charString)
                        && (
                            (pendingFilter && processFilter)
                            || (!pendingFilter || (pendingFilter && row.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PENDING)))
                            && (!processFilter || (processFilter && row.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PROCESS)))
                        )
                    ) {
                        filteredList.add(row);
                    }
                }
                mFilteredValues = filteredList;
            //}
            FilterResults filterResults = new FilterResults();
            filterResults.count = mFilteredValues.size();
            filterResults.values = mFilteredValues;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredValues = (ArrayList<IO_Inbound_Search_Record>) results.values;
            notifyDataSetChanged();
        }
    }
}
