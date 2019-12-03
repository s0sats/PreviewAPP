package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class Act069_Tickets_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private int resource;
    private ArrayList<Act069_TicketVH> mValues;
    private ArrayList<Act069_TicketVH> mFilteredValues;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act069_tickets_adapter";
    private TicketFilter valueFilter;
    private OnTicketClickListener onTicketClickListener;

    public Act069_Tickets_Adapter(Context context, int resource, ArrayList<Act069_TicketVH> mValues) {
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

    public interface OnTicketClickListener{
        void onTicketClickListner(Act069_TicketVH item);
    }

    public void setOnTicketClickListener(OnTicketClickListener onTicketClickListener) {
        this.onTicketClickListener = onTicketClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(resource, viewGroup, false);
        return new TicketVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TicketVH holder = (TicketVH) viewHolder;
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
            valueFilter = new TicketFilter();
        }
        return valueFilter;
    }

    private class TicketVH extends RecyclerView.ViewHolder{
        private View itemView;
        private TextView tvTicketId;
        private TextView tvStatus;
        private TextView tvType_path;
        private TextView tvType_desc;
        private TextView tvOpen_comment;
        private TextView tvOpen_date;
        private TextView tvOpen_date_val;
        private TextView tvForecast_date;
        private TextView tvForecast_date_val;
        private TextView tvSite;
        private TextView tvSite_dec;
        private TextView tvProduct;
        private TextView tvSerial;

        public TicketVH(View itemView) {
            super(itemView);
            this.itemView = itemView;
            //
            tvTicketId = itemView.findViewById(R.id.act069_ticket_cell_tv_ticket_id);
            tvStatus = itemView.findViewById(R.id.act069_ticket_cell_tv_status);
            tvType_path = itemView.findViewById(R.id.act069_ticket_cell_tv_type_path);
            tvType_desc = itemView.findViewById(R.id.act069_ticket_cell_tv_type_desc);
            tvOpen_comment = itemView.findViewById(R.id.act069_ticket_cell_tv_open_comment);
            tvOpen_date = itemView.findViewById(R.id.act069_ticket_cell_tv_open_date);
            tvOpen_date_val = itemView.findViewById(R.id.act069_ticket_cell_tv_open_date_val);
            tvForecast_date = itemView.findViewById(R.id.act069_ticket_cell_tv_forecast_date);
            tvForecast_date_val = itemView.findViewById(R.id.act069_ticket_cell_tv_forecast_date_val);
            tvSite = itemView.findViewById(R.id.act069_ticket_cell_tv_site);
            tvSite_dec = itemView.findViewById(R.id.act069_ticket_cell_tv_site_dec);
            tvProduct = itemView.findViewById(R.id.act069_ticket_cell_tv_product);
            tvSerial = itemView.findViewById(R.id.act069_ticket_cell_tv_serial);
            //
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onTicketClickListener != null){
                        onTicketClickListener.onTicketClickListner(mFilteredValues.get(getAdapterPosition()));
                    }
                }
            });
            //
            setLabels();
        }

        private void setLabels() {
            tvOpen_date.setText(hmAux_Trans.get("open_date_lbl"));
            tvForecast_date.setText(hmAux_Trans.get("forecast_date_lbl"));
            tvSite.setText(hmAux_Trans.get("site_lbl"));
        }

        public void bindData(Act069_TicketVH item){
            resetVisibility();
            //
            tvTicketId.setText(item.getTicket_id());
            tvStatus.setText(hmAux_Trans.get(item.getTicket_status()));
            tvStatus.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(item.getTicket_status())));
            //
            if(item.getType_path() != null && !item.getType_path().isEmpty()) {
                tvType_path.setVisibility(View.VISIBLE);
                tvType_path.setText(item.getType_path());
            }
            //
            if(item.getType_desc() != null && !item.getType_desc().isEmpty()) {
                tvType_desc.setVisibility(View.VISIBLE);
                tvType_desc.setText(item.getType_desc());
            }
            //
            if(item.getOpen_comments() != null && !item.getOpen_comments().isEmpty()) {
                tvOpen_comment.setVisibility(View.VISIBLE);
                tvOpen_comment.setText(item.getOpen_comments());
            }
            //
            if(item.getOpen_date() != null && !item.getOpen_date().isEmpty()) {
                tvOpen_date.setVisibility(View.VISIBLE);
                tvOpen_date_val.setVisibility(View.VISIBLE);
                tvOpen_date_val.setText(
                    ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.getOpen_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                );
            }
            //
            if(item.getForecast_date() != null && !item.getForecast_date().isEmpty()) {
                tvForecast_date.setVisibility(View.VISIBLE);
                tvForecast_date_val.setVisibility(View.VISIBLE);
                tvForecast_date_val.setText(
                    ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.getForecast_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                );
            }
            if(item.getCurrent_site_desc() != null && !item.getCurrent_site_desc().isEmpty()) {
                tvSite.setVisibility(View.VISIBLE);
                tvSite_dec.setVisibility(View.VISIBLE);
                tvSite_dec.setText(
                    item.getCurrent_site_desc()
                );
            }
            if(item.getCurrent_product_desc() != null && !item.getCurrent_product_desc().isEmpty()) {
                tvProduct.setVisibility(View.VISIBLE);
                tvProduct.setText(
                    item.getCurrent_product_desc()
                );
            }
            if(item.getCurrent_serial_id() != null && !item.getCurrent_serial_id().isEmpty()) {
                tvSerial.setVisibility(View.VISIBLE);
                tvSerial.setText(
                    item.getCurrent_serial_id()
                );
            }
        }

        private void resetVisibility() {
            tvType_path.setVisibility(View.GONE);
            tvType_desc.setVisibility(View.GONE);
            tvOpen_comment.setVisibility(View.GONE);
            tvOpen_date.setVisibility(View.GONE);
            tvOpen_date_val.setVisibility(View.GONE);
            tvForecast_date.setVisibility(View.GONE);
            tvForecast_date_val.setVisibility(View.GONE);
            tvSite.setVisibility(View.GONE);
            tvSite_dec.setVisibility(View.GONE);
            tvProduct.setVisibility(View.GONE);
            tvSerial.setVisibility(View.GONE);
        }
    }


    private class TicketFilter extends Filter{
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
