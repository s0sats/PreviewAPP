package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.ui.act043.Act043_Main;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Act043_Adapter_Services_Packs_List_RV extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private ValueFilter valueFilter;
    private int resource_01;
    private ArrayList<TSO_Service_Search_Obj> data;
    private ArrayList<TSO_Service_Search_Obj> data_filtered;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick(TSO_Service_Search_Obj item);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public Act043_Adapter_Services_Packs_List_RV(Context context, int resource_01, ArrayList<TSO_Service_Search_Obj> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.data = data;
        this.data_filtered = new ArrayList<>(data);
        //
        getFilter();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource_01, viewGroup, false);
        return new Pack_ServiceVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Pack_ServiceVH holder = (Pack_ServiceVH) viewHolder;
        holder.bindData(data_filtered.get(position));
    }

    @Override
    public int getItemCount() {
        return data_filtered.size();
    }

    public class Pack_ServiceVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout ll_background;
        private TextView tv_desc;
        private ImageView iv_foto;
        private ProgressBar pb_rating;
        private TextView tv_price;
        private View itemView;

        public Pack_ServiceVH(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            //
            ll_background = itemView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_ll_background);
            tv_desc = itemView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_desc);
            iv_foto =  itemView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_iv_type);
            pb_rating = itemView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_pb_rating);
            tv_price =  itemView.findViewById(R.id.act043_adapter_services_pack_list_content_cell_tv_price);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onClick(data_filtered.get(getAdapterPosition()));
            }
        }

        public void bindData(TSO_Service_Search_Obj data){
            resetViews();
            //
            setViewBackground(data.isSelected());
            setIcon(data.getType_ps());
            tv_desc.setText(data.getPack_service_desc_full());
            setPrice(data.getPrice(),data.hasNullPrice());
            try {
                pb_rating.setProgress(data.getRating_ref().intValue());
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
                pb_rating.setProgress(0);
            }
        }

        private void setPrice(Double price, boolean hasNullPrice) {
            //tv_price.setText(price == null ? null : new DecimalFormat("#,##0.00").format(price));
            tv_price.setText(price == null ? null : (new DecimalFormat("###0.00").format(price)).replace(",","."));
            if(hasNullPrice) {
                tv_price.setTextColor(context.getResources().getColor(R.color.namoa_color_danger_red));
            } else{
                tv_price.setTextColor(context.getResources().getColor(R.color.namoa_color_dark_blue));
            }
        }

        private void setViewBackground(boolean isSelected) {
            if(isSelected){
                ll_background.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
            }else{
                ll_background.setBackground(context.getDrawable(R.drawable.namoa_cell_9_states));
            }
        }

        private void resetViews() {
            ll_background.setBackground(null);
            iv_foto.setImageDrawable(null);
            tv_desc.setText("");
            tv_price.setText(null);
            tv_price.setTextColor(context.getResources().getColor(R.color.namoa_color_dark_blue));
            pb_rating.setProgress(0);
        }

        private void setIcon(String type_ps) {
            iv_foto.setImageDrawable(null);
            //
            if(Act043_Main.TYPE_PS_PACK.equals(type_ps)){
                iv_foto.setImageResource(R.drawable.ic_archive_material_black_24dp);
            }else{
                iv_foto.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        //
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<TSO_Service_Search_Obj> filterList = new ArrayList<TSO_Service_Search_Obj>();
                constraint = ToolBox.AccentMapper(constraint.toString());
                //
                for (int i = 0; i < data.size(); i++) {
                    String service_desc = ToolBox.AccentMapper(data.get(i).getPack_service_desc().toLowerCase());
                    String service_desc_full = ToolBox.AccentMapper(data.get(i).getPack_service_desc_full().toLowerCase());
                    if (service_desc.contains(constraint.toString().toLowerCase()) ||
                        service_desc_full.contains(constraint.toString().toLowerCase())
                    ) {
                        filterList.add(data.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = data.size();
                results.values = data;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data_filtered = (ArrayList<TSO_Service_Search_Obj>) results.values;
            //
            notifyDataSetChanged();
        }
    }
}
