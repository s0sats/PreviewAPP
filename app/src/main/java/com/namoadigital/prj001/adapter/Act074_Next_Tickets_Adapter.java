package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act074_Next_Tickets_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private int resource;
    private List<Act074_TicketVH> mValues = new ArrayList<>();
    private List<Act074_TicketVH> mFilteredValues = new ArrayList<>();
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "Tickets_List_Adapter";
    private Act074_Next_Tickets_Adapter.TicketFilter valueFilter;
    private Act074_Next_Tickets_Adapter.OnTicketClickListener onTicketClickListener;
    private boolean isOnlineProcess;

    public Act074_Next_Tickets_Adapter(Context context, boolean isOnlineProcess, int resource, ArrayList<Act074_TicketVH> mValues) {
        this.context = context;
        this.resource = resource;
        this.mValues = mValues;
        this.mFilteredValues = mValues;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
        //
        this.isOnlineProcess = isOnlineProcess;
    }

    public Act074_Next_Tickets_Adapter(Context context, int resource, boolean isOnlineProcess) {
        this.context = context;
        this.resource = resource;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Name
        );
        //
        loadTranslation();
    }

    public void setDataset(List<Act074_TicketVH> mValues, boolean isOnlineProcess) {
        this.mValues = mValues;
        this.mFilteredValues = mValues;
        this.isOnlineProcess = isOnlineProcess;
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("other_steps_available_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                ConstantBaseApp.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    public interface OnTicketClickListener {
        void onTicketClickListener(Act074_TicketVH item);
    }

    public void setOnTicketClickListener(Act074_Next_Tickets_Adapter.OnTicketClickListener onTicketClickListener) {
        this.onTicketClickListener = onTicketClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(resource, viewGroup, false);
        return new Act074_Next_Tickets_Adapter.TicketVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Act074_Next_Tickets_Adapter.TicketVH holder = (Act074_Next_Tickets_Adapter.TicketVH) viewHolder;
        //
       holder.bindData(mFilteredValues.get(position));

    }

    @Override
    public int getItemCount() {
        return mFilteredValues.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new Act074_Next_Tickets_Adapter.TicketFilter();
        }
        return valueFilter;
    }

    private class TicketVH extends RecyclerView.ViewHolder {
        View itemView;
        private ConstraintLayout cl_background;
        private TextView tv_ticket_id;
        private TextView tv_status;
        private TextView tv_prod_desc;
        private TextView tv_site_desc;
        private TextView tv_serial;
        private TextView tv_desc_origin;
        private TextView tv_step_desc;
        private TextView tv_planned_date_val;
        private TextView tv_step_id;
        private ImageView iv_schedule_icon;
        private ImageView iv_refresh_icon;
        private ImageView iv_offline_icon;
        private Group gp_step;

        public TicketVH(View itemView) {
            super(itemView);
            this.itemView = itemView;
            //
            cl_background = itemView.findViewById(R.id.act074_ticket_cell_cl_background);
            tv_ticket_id = itemView.findViewById(R.id.act074_ticket_cell_tv_ticket_id);
            tv_status = itemView.findViewById(R.id.act074_ticket_cell_tv_status);
            tv_prod_desc = itemView.findViewById(R.id.act074_ticket_cell_tv_prod_desc);
            tv_site_desc = itemView.findViewById(R.id.act074_ticket_cell_tv_site_desc);
            tv_serial = itemView.findViewById(R.id.act074_ticket_cell_tv_serial);
            tv_desc_origin = itemView.findViewById(R.id.act074_ticket_cell_tv_desc_origin);
            tv_step_desc = itemView.findViewById(R.id.act074_ticket_cell_tv_step_desc);
            tv_planned_date_val = itemView.findViewById(R.id.act074_ticket_cell_tv_planned_date_val);
            tv_step_id = itemView.findViewById(R.id.act074_ticket_cell_tv_step_id);
            iv_schedule_icon = itemView.findViewById(R.id.act074_ticket_cell_iv_schedule_icon);
            iv_refresh_icon = itemView.findViewById(R.id.act074_ticket_cell_iv_refresh_icon);
            iv_offline_icon = itemView.findViewById(R.id.act074_ticket_cell_iv_offline_icon);
            gp_step = itemView.findViewById(R.id.act074_ticket_cell_gp_step);
            //
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTicketClickListener != null) {
                        onTicketClickListener.onTicketClickListener(mFilteredValues.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void bindData(Act074_TicketVH item) {
            resetVisibility();
            //
            if(item.getSync_required() == 1){
                iv_refresh_icon.setVisibility(View.VISIBLE);
                iv_refresh_icon.setImageTintList(context.getResources().getColorStateList(R.color.namoa_pipeline_sync_icon));
                iv_refresh_icon.setImageResource(R.drawable.ic_baseline_sync_24);
            }else{
                if(item.getLocal_ticket() == 0){
                    iv_refresh_icon.setVisibility(View.VISIBLE);
                    iv_refresh_icon.setImageTintList(context.getResources().getColorStateList(R.color.namoa_color_black));
                    iv_refresh_icon.setImageResource(R.drawable.ic_file_download_black_24dp);
                }
            }
            //
            tv_ticket_id.setText(getFormattedTicketID(item));
            try {
                tv_status.setTextColor(ToolBox_Inf.getStatusColorV2(context, item.getTicket_status()));
            }catch (NullPointerException e){
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_status_stop));
            }
            //
            setVisibilityByContent(tv_status, hmAux_Trans.get(item.getTicket_status()));
            setVisibilityByContent(tv_ticket_id, item.getTicket_id());

            String step_order = item.getTicket_current_step_order();
            if(item.getStep_order_seq() != null
            && !item.getStep_order_seq().isEmpty() ){
                step_order += "." + item.getStep_order_seq();
            }
            setVisibilityByContent(tv_step_id, step_order);
            //
            if(item.getTicket_step_desc() != null
                    && !item.getTicket_step_desc().isEmpty()) {
                setVisibilityByContent(tv_step_desc, item.getTicket_step_desc());
            } else {
                setVisibilityByContent(tv_step_desc, hmAux_Trans.get("other_steps_available_lbl"));
            }
            //
            setVisibilityByContent(tv_prod_desc, item.getTicket_prod_desc());
            setSiteVisibility(tv_site_desc, item.getTicket_site_desc());
            setVisibilityByContent(tv_serial, item.getTicket_serial());
            setVisibilityByContent(tv_desc_origin, item.getTicket_origin_desc());
            //
            String formatted_planned_date = getFormatted_planned_date(item);

            setVisibilityByContent(tv_planned_date_val,
                    formatted_planned_date
            );
            if(item.getUser_focus() == 0){
                gp_step.setVisibility(View.GONE);
                setVisibilityByContent(tv_planned_date_val,
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.getTicket_forecast_date()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
            }
            if(!isOnlineProcess){
                iv_offline_icon.setVisibility(View.VISIBLE);
            }
            //
        }

        private void setVisibilityByContent(TextView tv_field, String tv_content) {
            if (tv_content != null && !tv_content.isEmpty()) {
                tv_field.setVisibility(View.VISIBLE);
                tv_field.setText(tv_content);
            }
        }

        /**
         * LUCHE - 18/03/2020
         * <p></p>
         * Metodo que define exibição da informação de ticket id.
         * Caso sea agendamento, exibe primeiro a pk do agendamento.*
         *
         * @param item - Obj View holder
         */
        private String getFormattedTicketID(Act074_TicketVH item) {
            String id = item.getTicket_id();
            if (item.getSchedulePk() != null && !item.getSchedulePk().isEmpty()) {
                id = ToolBox_Inf.getFormattedTicketSeqExec(
                        item.getSchedulePk(),
                        String.valueOf(item.getTicket_prefix()),
                        String.valueOf(item.getTicket_code())
                );
            }
            return id;
        }

        private void resetVisibility() {
            tv_ticket_id.setVisibility(View.GONE);
            tv_status.setVisibility(View.GONE);
            tv_prod_desc.setVisibility(View.GONE);
            tv_site_desc.setVisibility(View.GONE);
            tv_serial.setVisibility(View.GONE);
            tv_desc_origin.setVisibility(View.GONE);
            tv_step_desc.setVisibility(View.GONE);
            tv_planned_date_val.setVisibility(View.GONE);
            tv_step_id.setVisibility(View.GONE);
            iv_schedule_icon.setVisibility(View.VISIBLE);
            iv_refresh_icon.setVisibility(View.GONE);
            iv_offline_icon.setVisibility(View.GONE);
            gp_step.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    private String getFormatted_planned_date(Act074_TicketVH item) {
        //
        return ToolBox_Inf.getStepStartEndDateFormated(context, item.getStep_forecast_start_date(), item.getStep_forecast_end_date());
    }

    private void setSiteVisibility(TextView tv_site_desc, String ticket_site_desc) {
        if(ticket_site_desc!=null && !ticket_site_desc.isEmpty()){
            tv_site_desc.setVisibility(View.VISIBLE);
            tv_site_desc.setText(ticket_site_desc);
        }else{
            tv_site_desc.setVisibility(View.GONE);
        }
    }

    private class TicketFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
            if (charString.isEmpty()) {
                mFilteredValues = mValues;
            } else {
                ArrayList<Act074_TicketVH> filteredList = new ArrayList<>();
                for (Act074_TicketVH row : mValues) {
                    //Resgata todos os campos concatenado e com remoção de acentuacao
                    String rowFields = ToolBox.AccentMapper(row.getAllFieldForFilter().toLowerCase());
                    if (rowFields.contains(charString)) {
                        filteredList.add(row);
                    }
                }
                mFilteredValues = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = mFilteredValues.size();
            filterResults.values = mFilteredValues;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredValues = (ArrayList<Act074_TicketVH>) results.values;
            notifyDataSetChanged();
        }
    }
}
