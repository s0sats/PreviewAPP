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
import android.widget.TextView;

import com.namoadigital.prj001.R;

import java.util.ArrayList;
import java.util.List;

public class Act052_IO_Serial_List_Adapter extends RecyclerView.Adapter<Act052_IO_Serial_List_Adapter.ViewHolder> implements Filterable {

    private List<Object> mValues;
    private List<Object> mFilteredValues;
    private Context context;

    public Act052_IO_Serial_List_Adapter(Context context) {
        this.context = context;

        mValues = new ArrayList<>();
        mFilteredValues = new ArrayList<>();
    }

    @NonNull
    @Override
    public Act052_IO_Serial_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.act052_main_serial_list_item, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected final TextView tvStatusDesc;
        protected final TextView tvProductExtCodeVal;
        protected final TextView tvProductDescVal;
        protected final TextView tvSerialExtCodeVal;
        protected final TextView tvSerialLocation;
        protected final ImageView ivOfflineMode;
        protected final ImageView ivStatusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatusDesc = itemView.findViewById(R.id.act052_tv_io_status_desc);
            ivStatusIcon = itemView.findViewById(R.id.act052_tv_io_status_icon);
            tvProductExtCodeVal = itemView.findViewById(R.id.act052_tv_io_product_ext_code_val);
            tvProductDescVal = itemView.findViewById(R.id.act052_tv_io_product_desc_val);
            tvSerialExtCodeVal = itemView.findViewById(R.id.act052_tv_io_serial_ext_code_val);
            tvSerialLocation = itemView.findViewById(R.id.act052_tv_io_serial_location);
            ivOfflineMode = itemView.findViewById(R.id.act052_main_iv_offline_mode);
        }
    }
}
