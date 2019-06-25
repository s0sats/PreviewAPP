package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act067_IO_Items_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public static final int VIEW_TYPE_FOOTER = 1;
    public static final int VIEW_TYPE_ITEM = 0;

    private Context context;
    private int resource;
    private List<HMAux> mValues;
    private List<HMAux> mFilteredValues;
    private Act067_IO_Items_Adapter.IoItemFilter valueFilter;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act067_io_items_adapter";
    private Act067_IO_Items_Adapter.OnIoItemClickListener mOnIoItemClickListener;
    private boolean outboundAllowNewItem;
    private boolean filterActionPendencies;

    public interface OnIoItemClickListener {

        void onSerialClick(HMAux item);

        void onPickingDoneClick(HMAux item);

        void onPickingClick(HMAux item);

        void onAddItemClick();
    }

    public Act067_IO_Items_Adapter.OnIoItemClickListener getOnIoItemClickListener() {
        return mOnIoItemClickListener;
    }

    public void setOnIoItemClickListener(Act067_IO_Items_Adapter.OnIoItemClickListener mOnIoItemClickListener) {
        this.mOnIoItemClickListener = mOnIoItemClickListener;
    }

    public Act067_IO_Items_Adapter(Context context, int resource, List<HMAux> mValues, boolean outboundAllowNewItem, boolean filterActionPendencies) {
        this.context = context;
        this.resource = resource;
        this.mValues = mValues;
        this.mFilteredValues = mValues;
        this.outboundAllowNewItem = outboundAllowNewItem;
        this.filterActionPendencies = filterActionPendencies;
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
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("suggestion_lbl");
        transList.add("realized_lbl");
        transList.add("conf_date_lbl");
        transList.add("btn_add_item");
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
        View view;
        //
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(resource, viewGroup, false);
            return new Act067_IO_Items_Adapter.IOInboundViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.act067_frag_item_cell_footer, viewGroup, false);
            return new Act067_IO_Items_Adapter.FooterVH(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        try {
            if (viewHolder instanceof Act067_IO_Items_Adapter.IOInboundViewHolder) {
                Act067_IO_Items_Adapter.IOInboundViewHolder holder = (Act067_IO_Items_Adapter.IOInboundViewHolder) viewHolder;
                holder.bindData(mFilteredValues.get(position));
            } else if (viewHolder instanceof Act067_IO_Items_Adapter.FooterVH) {
                Act067_IO_Items_Adapter.FooterVH holder = (Act067_IO_Items_Adapter.FooterVH) viewHolder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mFilteredValues == null) {
            return 0;
        }
        //
        if (mFilteredValues.size() == 0) {
            return 1;
        }

        return mFilteredValues.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (mFilteredValues != null) {
            if (position == mFilteredValues.size()) {
                return VIEW_TYPE_FOOTER;
            } else {
                return VIEW_TYPE_ITEM;
            }
        } else {
            return VIEW_TYPE_FOOTER;
        }
    }

    public void updateFilterActionPendenciesStatus(boolean filterActionPendencies) {
        this.filterActionPendencies = filterActionPendencies;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new Act067_IO_Items_Adapter.IoItemFilter();
        }
        return valueFilter;
    }

    private class IoItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
            ArrayList<HMAux> filteredList = new ArrayList<>();
            //
            for (HMAux aux : mValues) {
                String serialId =
                        aux.hasConsistentValue(MD_Product_SerialDao.SERIAL_ID)
                                ? ToolBox.AccentMapper(aux.get(MD_Product_SerialDao.SERIAL_ID)).toLowerCase()
                                : "";
                if (!serialId.isEmpty()
                        && serialId.contains(charString)
                        && (filterStatus(aux))

                ) {
                    filteredList.add(aux);
                }
            }
            mFilteredValues = filteredList;
            FilterResults filterResults = new FilterResults();
            filterResults.count = mFilteredValues.size();
            filterResults.values = mFilteredValues;
            return filterResults;
        }

        /**
         * Verifica se não é para filtrar status ou
         * filtra por picking e picking_done
         *
         * @param aux
         * @return
         */
        private boolean filterStatus(HMAux aux) {
            if (aux.hasConsistentValue(IO_Outbound_ItemDao.STATUS)) {
                return
                        !filterActionPendencies
                                || (
                                filterActionPendencies
                                        &&
                                        (aux.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PICKING)
                                                || aux.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PICKING_DONE)
                                        )
                        );
            } else {
                //Não deveria acontecer.
                return false;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredValues = (ArrayList<HMAux>) results.values;
            notifyDataSetChanged();
        }
    }


    public class IOInboundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_row_id;
        private TextView tv_status;
        private ImageView iv_serial;
        private TextView tv_product_lbl;
        private TextView tv_product_val;
        private TextView tv_serial_lbl;
        private TextView tv_serial_val;
        private TextView tv_brand_model_color;
        private ImageView iv_picking;
        private ImageView iv_picking_done;
        private ConstraintLayout cl_suggestion;
        private TextView tv_sugestion_lbl;
        private TextView tv_suggestion_val;
        private ConstraintLayout cl_realized;
        private TextView tv_realized_lbl;
        private TextView tv_realized_val;
        private TextView tv_conf_dt_lbl;
        private TextView tv_conf_dt_val;
        private View itemView;

        public IOInboundViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            //
            tv_row_id = itemView.findViewById(R.id.act067_frag_item_cell_tv_row_id);
            tv_status = itemView.findViewById(R.id.act067_frag_item_cell_tv_status);
            iv_serial = itemView.findViewById(R.id.act067_frag_item_cell_iv_serial);//image
            tv_product_lbl = itemView.findViewById(R.id.act067_frag_item_cell_tv_product_lbl);
            tv_product_val = itemView.findViewById(R.id.act067_frag_item_cell_tv_product_val);
            tv_serial_lbl = itemView.findViewById(R.id.act067_frag_item_cell_tv_serial_lbl);
            tv_serial_val = itemView.findViewById(R.id.act067_frag_item_cell_tv_serial_val);
            tv_brand_model_color = itemView.findViewById(R.id.act067_frag_item_cell_tv_brand_model_color);
            iv_picking = itemView.findViewById(R.id.act067_frag_item_cell_iv_picking);
            iv_picking_done = itemView.findViewById(R.id.act067_act067_frag_item_cell_iv_picking_done);
            cl_suggestion = itemView.findViewById(R.id.act067_frag_item_cell_cl_suggestion);
            tv_sugestion_lbl = itemView.findViewById(R.id.act067_frag_item_cell_tv_sugestion_lbl);
            tv_suggestion_val = itemView.findViewById(R.id.act067_frag_item_cell_tv_suggestion_val);
            cl_realized = itemView.findViewById(R.id.act067_frag_item_cell_cl_realized);
            tv_realized_lbl = itemView.findViewById(R.id.act067_frag_item_cell_tv_realized_lbl);
            tv_realized_val = itemView.findViewById(R.id.act067_frag_item_cell_tv_realized_val);
            tv_conf_dt_lbl = itemView.findViewById(R.id.act067_frag_item_cell_tv_conf_dt_lbl);
            tv_conf_dt_val = itemView.findViewById(R.id.act067_frag_item_cell_tv_conf_dt_val);
            //translations
            tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
            tv_serial_lbl.setText(hmAux_Trans.get("serial_lbl"));
            tv_sugestion_lbl.setText(hmAux_Trans.get("suggestion_lbl"));
            tv_realized_lbl.setText(hmAux_Trans.get("realized_lbl"));
            tv_conf_dt_lbl.setText(hmAux_Trans.get("conf_date_lbl"));
            //
            iv_serial.setOnClickListener(this);
            iv_picking_done.setOnClickListener(this);
            iv_picking.setOnClickListener(this);

        }

        public View getItemView() {
            return itemView;
        }

        public void bindData(HMAux item) {
            //Esconde views
            resetVisibility();
            //
            tv_row_id.setText(item.get(IO_Outbound_ItemDao.OUTBOUND_ITEM));
            tv_status.setText(hmAux_Trans.get(item.get(IO_Outbound_ItemDao.STATUS)));
            tv_status.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(item.get(IO_Outbound_ItemDao.STATUS))));
            //iv_serial.setOnClickListener();
            tv_product_val.setText(item.get(MD_Product_SerialDao.PRODUCT_DESC));
            tv_serial_val.setText(item.get(MD_Product_SerialDao.SERIAL_ID));
            tv_brand_model_color.setVisibility(View.GONE);
            String brandModelColor = formatSerialBrandModelColor(item).trim();
            if (!brandModelColor.isEmpty()) {
                tv_brand_model_color.setText(brandModelColor);
                tv_brand_model_color.setVisibility(View.VISIBLE);
            }
            //
            if (
                    (item.hasConsistentValue(IO_OutboundDao.ZONE_ID_PICKING) && !item.get(IO_OutboundDao.ZONE_ID_PICKING).isEmpty())
                            || (item.hasConsistentValue(IO_OutboundDao.LOCAL_ID_PICKING) && !item.get(IO_OutboundDao.LOCAL_ID_PICKING).isEmpty())
            ) {
                String suggestedPosition = item.hasConsistentValue(IO_OutboundDao.ZONE_ID_PICKING) && !item.get(IO_OutboundDao.ZONE_ID_PICKING).isEmpty() ? item.get(IO_OutboundDao.ZONE_ID_PICKING) : "";
                suggestedPosition += item.hasConsistentValue(IO_OutboundDao.LOCAL_ID_PICKING) && !item.get(IO_OutboundDao.LOCAL_ID_PICKING).isEmpty() ? (!suggestedPosition.isEmpty() ? " | " : "") + item.get(IO_OutboundDao.LOCAL_ID_PICKING) : "";
                tv_suggestion_val.setText(suggestedPosition);
                tv_sugestion_lbl.setVisibility(View.VISIBLE);
                tv_suggestion_val.setVisibility(View.VISIBLE);
            }
            //Seta dados do realizado.Info depende do status do item e tipo de inbound
            setRealized(item);

            //
            if (item.hasConsistentValue(IO_Outbound_ItemDao.CONF_DATE) && !item.get(IO_Outbound_ItemDao.CONF_DATE).isEmpty()) {
                tv_conf_dt_val.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(IO_Outbound_ItemDao.CONF_DATE)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )

                );
                tv_conf_dt_lbl.setVisibility(View.VISIBLE);
                tv_conf_dt_val.setVisibility(View.VISIBLE);
            }
            //Define qual icone deve ser exibido.
            if (item.hasConsistentValue(IO_OutboundDao.PICKING_PROCESS) && !item.get(IO_OutboundDao.PICKING_PROCESS).isEmpty()) {
                if (item.get(IO_OutboundDao.PICKING_PROCESS).equals("0")) {
                    iv_picking_done.setVisibility(View.VISIBLE);
                    iv_picking.setVisibility(View.GONE);
                } else {
                    if (
                            item.hasConsistentValue(IO_Outbound_ItemDao.STATUS)
                                    && (item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_DONE)
                                    || item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PICKING_DONE)
                            )
                    ) {
                        iv_picking_done.setVisibility(View.VISIBLE);
                        iv_picking.setVisibility(View.GONE);
                    } else {
                        iv_picking_done.setVisibility(View.GONE);
                        iv_picking.setVisibility(View.VISIBLE);
                    }
                }
            }
            //Define layout dos botões
            defineButtonsLayout(item);
        }

        private void setRealized(HMAux item) {
            String realized = "";

            if (item.hasConsistentValue(IO_OutboundDao.PICKING_PROCESS)
                    && item.get(IO_OutboundDao.PICKING_PROCESS).equals("1")
                    && item.hasConsistentValue(IO_OutboundDao.STATUS)
                    && (item.get(IO_OutboundDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_DONE) || item.get(IO_OutboundDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC))
                    && item.hasConsistentValue(IO_MoveDao.TO_ZONE_DESC)
                    && item.hasConsistentValue(IO_MoveDao.TO_LOCAL_ID)
            ) {
                realized = item.hasConsistentValue(IO_MoveDao.TO_ZONE_DESC) && !item.get(IO_MoveDao.TO_ZONE_DESC).isEmpty() ? item.get(IO_MoveDao.TO_ZONE_DESC) : "";
                realized += item.hasConsistentValue(IO_MoveDao.TO_LOCAL_ID) && !item.get(IO_MoveDao.TO_LOCAL_ID).isEmpty() ? (!realized.isEmpty() ? " | " : "") + item.get(IO_MoveDao.TO_LOCAL_ID) : "";
                tv_realized_val.setText(realized);
                tv_realized_lbl.setVisibility(View.VISIBLE);
                tv_realized_val.setVisibility(View.VISIBLE);

            } else {
                if (item.hasConsistentValue(IO_OutboundDao.ZONE_CODE_PICKING) && !item.get(IO_OutboundDao.ZONE_CODE_PICKING).isEmpty()) {
                    realized = item.hasConsistentValue(IO_OutboundDao.ZONE_DESC_PICKING) && !item.get(IO_OutboundDao.ZONE_DESC_PICKING).isEmpty() ? item.get(IO_OutboundDao.ZONE_DESC_PICKING) : "";
                    realized += item.hasConsistentValue(IO_OutboundDao.LOCAL_ID_PICKING) && !item.get(IO_OutboundDao.LOCAL_ID_PICKING).isEmpty() ? (!realized.isEmpty() ? " | " : "") + item.get(IO_OutboundDao.LOCAL_ID_PICKING) : "";
                    tv_realized_val.setText(realized);
                    tv_realized_lbl.setVisibility(View.VISIBLE);
                    tv_realized_val.setVisibility(View.VISIBLE);
                }
            }
        }

        private void defineButtonsLayout(HMAux item) {
            if (item.hasConsistentValue(IO_Outbound_ItemDao.STATUS)) {

                if (item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PICKING_DONE)) {
                    iv_picking_done.setImageDrawable(
                            context.getDrawable(R.drawable.ic_ok_orange_ns_states)
                    );
                } else {
                    iv_picking_done.setImageDrawable(
                            context.getDrawable(R.drawable.ic_ok_ns_states)
                    );
                }
                //
                if (item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PICKING)) {
                    iv_picking.setImageDrawable(
                            context.getDrawable(R.drawable.io_bg_orange_states)
                    );
                } else {
                    iv_picking.setImageDrawable(
                            context.getDrawable(R.drawable.io_bg_green_states)
                    );
                }
                //Cancela ação do btn caso o status não permita uma ação.
                if (
                        item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_DONE)
                                || item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
                                || item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                                || item.get(IO_Outbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_INCONSISTENT)
                ) {
                    iv_picking_done.setEnabled(false);
                    iv_picking.setEnabled(false);
                } else {
                    iv_picking_done.setEnabled(true);
                    iv_picking.setEnabled(true);
                }
            } else {
                iv_picking_done.setEnabled(false);
                iv_picking_done.setVisibility(View.GONE);
                iv_picking.setEnabled(false);
                iv_picking.setVisibility(View.GONE);
            }
        }

        private void resetVisibility() {
            tv_brand_model_color.setVisibility(View.GONE);
            tv_brand_model_color.setVisibility(View.GONE);
            tv_brand_model_color.setVisibility(View.GONE);
            tv_sugestion_lbl.setVisibility(View.GONE);
            tv_suggestion_val.setVisibility(View.GONE);
            tv_realized_lbl.setVisibility(View.GONE);
            tv_realized_val.setVisibility(View.GONE);
            tv_conf_dt_lbl.setVisibility(View.INVISIBLE);
            tv_conf_dt_val.setVisibility(View.INVISIBLE);
            //
            iv_picking_done.setVisibility(View.GONE);
            iv_picking.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            if (mOnIoItemClickListener != null) {
                switch (v.getId()) {
                    case R.id.act067_frag_item_cell_iv_serial:
                        mOnIoItemClickListener.onSerialClick(mFilteredValues.get(getAdapterPosition()));
                        break;
                    case R.id.act067_act067_frag_item_cell_iv_picking_done:
                        mOnIoItemClickListener.onPickingDoneClick(mFilteredValues.get(getAdapterPosition()));
                        break;
                    case R.id.act067_frag_item_cell_iv_picking:
                        mOnIoItemClickListener.onPickingClick(mFilteredValues.get(getAdapterPosition()));
                        break;
                }
            }
        }

        private String formatSerialBrandModelColor(HMAux data) {
            String
                    serialBrandModelColor = data.get(MD_Product_SerialDao.BRAND_DESC) == null || data.get(MD_Product_SerialDao.BRAND_DESC).isEmpty() ? "" : data.get(MD_Product_SerialDao.BRAND_DESC);
            serialBrandModelColor += (data.get(MD_Product_SerialDao.MODEL_DESC) == null || data.get(MD_Product_SerialDao.MODEL_DESC).isEmpty() ? "" : " | " + data.get(MD_Product_SerialDao.MODEL_DESC));
            serialBrandModelColor += (data.get(MD_Product_SerialDao.COLOR_DESC) == null || data.get(MD_Product_SerialDao.COLOR_DESC).isEmpty() ? "" : " | " + data.get(MD_Product_SerialDao.COLOR_DESC));
            return serialBrandModelColor;
        }
    }

    public class FooterVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btnAddItem;

        public FooterVH(@NonNull View itemView) {
            super(itemView);
            //
            btnAddItem = itemView.findViewById(R.id.act067_frag_item_footer_btn_add);
            btnAddItem.setText(hmAux_Trans.get("btn_add_item"));
            //
            if (outboundAllowNewItem) {
                btnAddItem.setVisibility(View.VISIBLE);
                btnAddItem.setOnClickListener(this);
            } else {
                btnAddItem.setVisibility(View.GONE);
                btnAddItem.setOnClickListener(null);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnIoItemClickListener != null) {
                mOnIoItemClickListener.onAddItemClick();
            }
        }
    }


}
