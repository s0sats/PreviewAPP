package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act074_Next_Tickets_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private int resource;
    private ArrayList<Act069_TicketVH> mValues;
    private ArrayList<Act069_TicketVH> mFilteredValues;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "Act074_Next_Tickets_Adapter";
    private Act074_Next_Tickets_Adapter.TicketFilter valueFilter;
    private Act074_Next_Tickets_Adapter.OnTicketClickListener onTicketClickListener;
    private Act074_Next_Tickets_Adapter.OnScheduleWarningClickListener onScheduleWarningClickListener;

    public Act074_Next_Tickets_Adapter(Context context, int resource, ArrayList<Act069_TicketVH> mValues) {
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
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("open_date_lbl");
        transList.add("forecast_date_lbl");
        transList.add("site_lbl");
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
        void onTicketClickListner(Act069_TicketVH item);
    }

    public interface OnScheduleWarningClickListener {
        void onScheduleWarningClick(String fcm_new_status, String fcm_user_nick, String schedule_erro_msg);
    }

    public void setOnTicketClickListener(Act074_Next_Tickets_Adapter.OnTicketClickListener onTicketClickListener) {
        this.onTicketClickListener = onTicketClickListener;
    }

    public void setOnScheduleWarningClickListener(Act074_Next_Tickets_Adapter.OnScheduleWarningClickListener onScheduleWarningClickListener) {
        this.onScheduleWarningClickListener = onScheduleWarningClickListener;
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
        private View itemView;
        private ConstraintLayout cl_background;
        private ConstraintLayout cl_ticket_id;
        private TextView tv_ticket_id;
        private TextView tv_status;
        private TextView tv_prod_desc;
        private TextView tv_site_desc;
        private TextView tv_serial;
        private TextView tv_open_comment;
        private TextView tv_step_desc;
        private TextView tv_open_date_val;
        private ImageView iv_step_id;
        private ImageView iv_schedule_icon;
        private TextView tv_other_steps_available;

        public TicketVH(View itemView) {
            super(itemView);
            this.itemView = itemView;
            //
            cl_background = itemView.findViewById(R.id.act074_ticket_cell_cl_background);
            cl_ticket_id = itemView.findViewById(R.id.act074_ticket_cell_cl_ticket_id);
            tv_ticket_id = itemView.findViewById(R.id.act074_ticket_cell_tv_ticket_id);
            tv_status = itemView.findViewById(R.id.act074_ticket_cell_tv_status);
            tv_prod_desc = itemView.findViewById(R.id.act074_ticket_cell_tv_prod_desc);
            tv_site_desc = itemView.findViewById(R.id.act074_ticket_cell_tv_site_desc);
            tv_serial = itemView.findViewById(R.id.act074_ticket_cell_tv_serial);
            tv_open_comment = itemView.findViewById(R.id.act074_ticket_cell_tv_desc_origin);
            tv_step_desc = itemView.findViewById(R.id.act074_ticket_cell_tv_step_desc);
            tv_open_date_val = itemView.findViewById(R.id.act074_ticket_cell_tv_open_date_val);
            iv_step_id = itemView.findViewById(R.id.act074_ticket_cell_iv_step_id);
            iv_schedule_icon = itemView.findViewById(R.id.act074_ticket_cell_iv_schedule_icon);
            //
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTicketClickListener != null) {
                        onTicketClickListener.onTicketClickListner(mFilteredValues.get(getAdapterPosition()));
                    }
                }
            });
            //
            setLabels();
        }

        private void setLabels() {
            tv_other_steps_available.setText(hmAux_Trans.get("other_steps_available_lbl"));
        }

        public void bindData(Act069_TicketVH item) {
            resetVisibility();
            //
            tv_ticket_id.setText(getFormattedTicketID(item));
            setSyncIcon(item.getSync_required());
            tv_status.setText(hmAux_Trans.get(item.getTicket_status()));
            tv_status.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(item.getTicket_status())));
            //
//            if (item.getType_path() != null && !item.getType_path().isEmpty()) {
//                tvType_path.setVisibility(View.VISIBLE);
//                tvType_path.setText(item.getType_path());
//            }
//            //
//            if (item.getType_desc() != null && !item.getType_desc().isEmpty()) {
//                tvType_desc.setVisibility(View.VISIBLE);
//                tvType_desc.setText(item.getType_desc());
//            }
//            //
//            if (item.getOpen_comments() != null && !item.getOpen_comments().isEmpty()) {
//                tvOpen_comment.setVisibility(View.VISIBLE);
//                tvOpen_comment.setText(item.getOpen_comments());
//            }
//            //
//            if (item.getOpen_date() != null && !item.getOpen_date().isEmpty()) {
//                tvOpen_date.setVisibility(View.VISIBLE);
//                tvOpen_date_val.setVisibility(View.VISIBLE);
//                tvOpen_date_val.setText(
//                        ToolBox_Inf.millisecondsToString(
//                                ToolBox_Inf.dateToMilliseconds(item.getOpen_date()),
//                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
//                        )
//                );
//            }
//            //
//            if (item.getForecast_date() != null && !item.getForecast_date().isEmpty()) {
//                tvForecast_date.setVisibility(View.VISIBLE);
//                tvForecast_date_val.setVisibility(View.VISIBLE);
//                tvForecast_date_val.setText(
//                        ToolBox_Inf.millisecondsToString(
//                                ToolBox_Inf.dateToMilliseconds(item.getForecast_date()),
//                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
//                        )
//                );
//            }
//            if (item.getCurrent_site_desc() != null && !item.getCurrent_site_desc().isEmpty()) {
//                tv_site_desc.setVisibility(View.VISIBLE);
//                tv_site_desc.setVisibility(View.VISIBLE);
//                tv_site_desc.setText(
//                        item.getCurrent_site_desc()
//                );
//            }
//            if (item.getCurrent_product_desc() != null && !item.getCurrent_product_desc().isEmpty()) {
//                tvProduct.setVisibility(View.VISIBLE);
//                tvProduct.setText(
//                        item.getCurrent_product_desc()
//                );
//            }
//            if (item.getCurrent_serial_id() != null && !item.getCurrent_serial_id().isEmpty()) {
//                tvSerial.setVisibility(View.VISIBLE);
//                tvSerial.setText(
//                        item.getCurrent_serial_id()
//                );
//            }
            setIvScheduleWarning(item);
        }

        private void setIvScheduleWarning(final Act069_TicketVH item) {
            if ((item.getFcm_new_status() != null && !item.getFcm_new_status().isEmpty())
                    || (item.getFcm_user_nick() != null && !item.getFcm_user_nick().isEmpty())
                    || (item.getSchedule_erro_msg() != null && !item.getSchedule_erro_msg().isEmpty())
            ) {
                int color = item.getSchedule_erro_msg() != null && !item.getSchedule_erro_msg().isEmpty()
                        ? R.color.namoa_color_danger_red
                        : R.color.light_to_dark_blue_color;
                //
//                ivScheduleWarning.setVisibility(View.VISIBLE);
//                ivScheduleWarning.setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
//                ivScheduleWarning.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onScheduleWarningClickListener != null) {
//                            onScheduleWarningClickListener.onScheduleWarningClick(
//                                    item.getFcm_new_status(),
//                                    item.getFcm_user_nick(),
//                                    item.getSchedule_erro_msg()
//                            );
//                        }
//                    }
//                });
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
        private String getFormattedTicketID(Act069_TicketVH item) {
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

        private void setSyncIcon(int sync_required) {
            Drawable rightDraw = null;
            if (sync_required == 1) {
                rightDraw = context.getResources().getDrawable(R.drawable.ic_sync_black_24dp);
                rightDraw.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
            }
            //
            tv_ticket_id.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDraw, null);
        }

        private void resetVisibility() {
            tv_ticket_id.setVisibility(View.GONE);
            tv_status.setVisibility(View.GONE);
            tv_prod_desc.setVisibility(View.GONE);
            tv_site_desc.setVisibility(View.GONE);
            tv_serial.setVisibility(View.GONE);
            tv_open_comment.setVisibility(View.GONE);
            tv_step_desc.setVisibility(View.GONE);
            tv_open_date_val.setVisibility(View.GONE);
            iv_step_id.setVisibility(View.GONE);
            iv_schedule_icon.setVisibility(View.GONE);
            tv_other_steps_available.setVisibility(View.GONE);
        }
    }


    private class TicketFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
//            if (charString.isEmpty()) {
//                mFilteredValues = mValues;
//            } else {
            ArrayList<Act069_TicketVH> filteredList = new ArrayList<>();
            for (Act069_TicketVH row : mValues) {
                //Resgata todos os campos concatenado e com remoção de acentuacao
                String rowFields = ToolBox.AccentMapper(row.getAllFieldForFilter().toLowerCase());
                if (rowFields.contains(charString)) {
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
            mFilteredValues = (ArrayList<Act069_TicketVH>) results.values;
            notifyDataSetChanged();
        }
    }
}
