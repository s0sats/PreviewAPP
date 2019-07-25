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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act061_IO_Items_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public static final int VIEW_TYPE_FOOTER = 1;
    public static final int VIEW_TYPE_ITEM = 0;

    private Context context;
    private int resource;
    private List<HMAux> mValues;
    private List<HMAux> mFilteredValues;
    private IoItemFilter valueFilter;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act061_io_items_adapter";
    private OnIoItemClickListener mOnIoItemClickListener;
    private boolean inboundAllowNewItem;
    private boolean filterActionPendencies;
    private String productCode;
    private String serialCode;

    public interface OnIoItemClickListener {

        void onSerialClick(HMAux item);

        void onConfClick(HMAux item);

        void onPutAwayClick(HMAux item);

        void onAddItemClick();

    }

    public OnIoItemClickListener getOnIoItemClickListener() {
        return mOnIoItemClickListener;
    }

    public void setOnIoItemClickListener(OnIoItemClickListener mOnIoItemClickListener) {
        this.mOnIoItemClickListener = mOnIoItemClickListener;
    }

    public Act061_IO_Items_Adapter(Context context, int resource, List<HMAux> mValues, boolean inboundAllowNewItem, boolean filterActionPendencies, String productCode, String serialCode) {
        this.context = context;
        this.resource = resource;
        this.mValues = mValues;
        this.mFilteredValues = mValues;
        this.inboundAllowNewItem = inboundAllowNewItem;
        this.filterActionPendencies = filterActionPendencies;
        this.productCode = productCode;
        this.serialCode = serialCode;
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
            return new IOInboundViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.act061_frag_item_cell_footer, viewGroup, false);
            return new FooterVH(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        try {
            if (viewHolder instanceof IOInboundViewHolder) {
                IOInboundViewHolder holder = (IOInboundViewHolder) viewHolder;
                holder.bindData(mFilteredValues.get(position));
                HMAux aux = mFilteredValues.get(position);
                //
                if(
                    aux.hasConsistentValue(IO_Inbound_ItemDao.PRODUCT_CODE)
                    && aux.hasConsistentValue(IO_Inbound_ItemDao.SERIAL_CODE)
                    && aux.get(IO_Inbound_ItemDao.PRODUCT_CODE).equals(productCode)
                    && aux.get(IO_Inbound_ItemDao.SERIAL_CODE).equals(serialCode)
                ){
                    holder.highlightCell();
                }

            } else if (viewHolder instanceof FooterVH) {
                FooterVH holder = (FooterVH) viewHolder;
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
            valueFilter = new IoItemFilter();
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
         * filtra por pendind e put_away
         *
         * @param aux
         * @return
         */
        private boolean filterStatus(HMAux aux) {
            if (aux.hasConsistentValue(IO_Inbound_ItemDao.STATUS)) {
                return
                    !filterActionPendencies
                        || (
                        filterActionPendencies
                            &&
                            (aux.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PENDING)
                                || aux.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PUT_AWAY)
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
        private ConstraintLayout cl_main;
        private TextView tv_row_id;
        private TextView tv_status;
        private ImageView iv_serial;//image
        private TextView tv_product_lbl;
        private TextView tv_product_val;
        private TextView tv_serial_lbl;
        private TextView tv_serial_val;
        private TextView tv_brand_model_color;
        private ImageView iv_put_away;
        private ImageView iv_conf;
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
            cl_main = itemView.findViewById(R.id.act061_frag_item_cell_cl_main);
            tv_row_id = itemView.findViewById(R.id.act061_frag_item_cell_tv_row_id);
            tv_status = itemView.findViewById(R.id.act061_frag_item_cell_tv_status);
            iv_serial = itemView.findViewById(R.id.act061_frag_item_cell_iv_serial);//image
            tv_product_lbl = itemView.findViewById(R.id.act061_frag_item_cell_tv_product_lbl);
            tv_product_val = itemView.findViewById(R.id.act061_frag_item_cell_tv_product_val);
            tv_serial_lbl = itemView.findViewById(R.id.act061_frag_item_cell_tv_serial_lbl);
            tv_serial_val = itemView.findViewById(R.id.act061_frag_item_cell_tv_serial_val);
            tv_brand_model_color = itemView.findViewById(R.id.act061_frag_item_cell_tv_brand_model_color);
            iv_put_away = itemView.findViewById(R.id.act061_frag_item_cell_iv_put_away);
            iv_conf = itemView.findViewById(R.id.act061_frag_item_cell_iv_conf);
            cl_suggestion = itemView.findViewById(R.id.act061_frag_item_cell_cl_suggestion);
            tv_sugestion_lbl = itemView.findViewById(R.id.act061_frag_item_cell_tv_sugestion_lbl);
            tv_suggestion_val = itemView.findViewById(R.id.act061_frag_item_cell_tv_suggestion_val);
            cl_realized = itemView.findViewById(R.id.act061_frag_item_cell_cl_realized);
            tv_realized_lbl = itemView.findViewById(R.id.act061_frag_item_cell_tv_realized_lbl);
            tv_realized_val = itemView.findViewById(R.id.act061_frag_item_cell_tv_realized_val);
            tv_conf_dt_lbl = itemView.findViewById(R.id.act061_frag_item_cell_tv_conf_dt_lbl);
            tv_conf_dt_val = itemView.findViewById(R.id.act061_frag_item_cell_tv_conf_dt_val);
            //translations
            tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
            tv_serial_lbl.setText(hmAux_Trans.get("serial_lbl"));
            tv_sugestion_lbl.setText(hmAux_Trans.get("suggestion_lbl"));
            tv_realized_lbl.setText(hmAux_Trans.get("realized_lbl"));
            tv_conf_dt_lbl.setText(hmAux_Trans.get("conf_date_lbl"));
            //
            iv_serial.setOnClickListener(this);
            iv_conf.setOnClickListener(this);
            iv_put_away.setOnClickListener(this);
        }

        public View getItemView() {
            return itemView;
        }

        public void bindData(HMAux item) {
            //Esconde views
            resetVisibility();
            //
            tv_row_id.setText(item.get(IO_Inbound_ItemDao.INBOUND_ITEM));
            tv_status.setText(hmAux_Trans.get(item.get(IO_Inbound_ItemDao.STATUS)));
            tv_status.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(item.get(IO_Inbound_ItemDao.STATUS))));
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
                (item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_ZONE_ID) && !item.get(IO_Inbound_ItemDao.PLANNED_ZONE_ID).isEmpty())
                    || (item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_LOCAL_ID) && !item.get(IO_Inbound_ItemDao.PLANNED_LOCAL_ID).isEmpty())
            ) {
                String suggestedPosition = item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_ZONE_ID) && !item.get(IO_Inbound_ItemDao.PLANNED_ZONE_ID).isEmpty() ? item.get(IO_Inbound_ItemDao.PLANNED_ZONE_ID) : "";
                suggestedPosition += item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_LOCAL_ID) && !item.get(IO_Inbound_ItemDao.PLANNED_LOCAL_ID).isEmpty() ? (!suggestedPosition.isEmpty() ? " | " : "") + item.get(IO_Inbound_ItemDao.PLANNED_LOCAL_ID) : "";
                tv_suggestion_val.setText(suggestedPosition);
                tv_sugestion_lbl.setVisibility(View.VISIBLE);
                tv_suggestion_val.setVisibility(View.VISIBLE);
            }
            //Seta dados do realizado.Info depende do status do item e tipo de inbound
            setRealized(item);

            //
            if (item.hasConsistentValue(IO_Inbound_ItemDao.CONF_DATE) && !item.get(IO_Inbound_ItemDao.CONF_DATE).isEmpty()) {
                tv_conf_dt_val.setText(
                    ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(IO_Inbound_ItemDao.CONF_DATE)),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )

                );
                tv_conf_dt_lbl.setVisibility(View.VISIBLE);
                tv_conf_dt_val.setVisibility(View.VISIBLE);
            }
            //Define qual icone deve ser exibido.
            if (item.hasConsistentValue(IO_InboundDao.PUT_AWAY_PROCESS) && !item.get(IO_InboundDao.PUT_AWAY_PROCESS).isEmpty()) {
                if (item.get(IO_InboundDao.PUT_AWAY_PROCESS).equals("0")) {
                    iv_conf.setVisibility(View.VISIBLE);
                    iv_put_away.setVisibility(View.GONE);
                } else {
                    if (
                        item.hasConsistentValue(IO_Inbound_ItemDao.STATUS)
                            && (item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_DONE)
                            || item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PUT_AWAY)
                        )
                    ) {
                        iv_conf.setVisibility(View.GONE);
                        iv_put_away.setVisibility(View.VISIBLE);
                    } else {
                        iv_conf.setVisibility(View.VISIBLE);
                        iv_put_away.setVisibility(View.GONE);
                    }
                }
            }
            //Define layout dos botões
            defineButtonsLayout(item);
        }

        private void setRealized(HMAux item) {
            String realized = "";

            if (item.hasConsistentValue(IO_InboundDao.PUT_AWAY_PROCESS)
                && item.get(IO_InboundDao.PUT_AWAY_PROCESS).equals("1")
                && item.hasConsistentValue(IO_InboundDao.STATUS)
                && (item.get(IO_InboundDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_DONE) || item.get(IO_InboundDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC))
                && item.hasConsistentValue(IO_MoveDao.TO_ZONE_DESC)
                && item.hasConsistentValue(IO_MoveDao.TO_LOCAL_ID)
            ) {
                realized = item.hasConsistentValue(IO_MoveDao.TO_ZONE_DESC) && !item.get(IO_MoveDao.TO_ZONE_DESC).isEmpty() ? item.get(IO_MoveDao.TO_ZONE_DESC) : "";
                realized += item.hasConsistentValue(IO_MoveDao.TO_LOCAL_ID) && !item.get(IO_MoveDao.TO_LOCAL_ID).isEmpty() ? (!realized.isEmpty() ? " | " : "") + item.get(IO_MoveDao.TO_LOCAL_ID) : "";
                tv_realized_val.setText(realized);
                tv_realized_lbl.setVisibility(View.VISIBLE);
                tv_realized_val.setVisibility(View.VISIBLE);

            } else {
                if (item.hasConsistentValue(IO_Inbound_ItemDao.ZONE_CODE) && !item.get(IO_Inbound_ItemDao.ZONE_CODE).isEmpty()) {
                    realized = item.hasConsistentValue(IO_Inbound_ItemDao.ZONE_DESC) && !item.get(IO_Inbound_ItemDao.ZONE_DESC).isEmpty() ? item.get(IO_Inbound_ItemDao.ZONE_DESC) : "";
                    realized += item.hasConsistentValue(IO_Inbound_ItemDao.LOCAL_ID) && !item.get(IO_Inbound_ItemDao.LOCAL_ID).isEmpty() ? (!realized.isEmpty() ? " | " : "") + item.get(IO_Inbound_ItemDao.LOCAL_ID) : "";
                    tv_realized_val.setText(realized);
                    tv_realized_lbl.setVisibility(View.VISIBLE);
                    tv_realized_val.setVisibility(View.VISIBLE);
                }
            }
        }

        private void defineButtonsLayout(HMAux item) {
            if (item.hasConsistentValue(IO_Inbound_ItemDao.STATUS)) {

                if (item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PENDING)) {
                    iv_conf.setImageDrawable(
                        context.getDrawable(R.drawable.ic_ok_orange_ns_states)
                    );
                } else {
                    iv_conf.setImageDrawable(
                        context.getDrawable(R.drawable.ic_ok_ns_states)
                    );
                }
                //
                if (item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PUT_AWAY)) {
                    iv_put_away.setImageDrawable(
                        context.getDrawable(R.drawable.io_bg_orange_states)
                    );
                } else {
                    iv_put_away.setImageDrawable(
                        context.getDrawable(R.drawable.io_bg_green_states)
                    );
                }
                //Cancela ação do btn caso o status não permita uma ação.
                if (
                    item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_DONE)
                        || item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
                        || item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                        || item.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_INCONSISTENT)
                ) {
                    iv_conf.setEnabled(false);
                    iv_put_away.setEnabled(false);
                } else {
                    iv_conf.setEnabled(true);
                    iv_put_away.setEnabled(true);
                }
            } else {
                iv_conf.setEnabled(false);
                iv_conf.setVisibility(View.GONE);
                iv_put_away.setEnabled(false);
                iv_put_away.setVisibility(View.GONE);
            }
        }

        private void resetVisibility() {
            cl_main.setBackground(
                context.getDrawable(R.drawable.namoa_cell_8)
            );
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
            iv_conf.setVisibility(View.GONE);
            iv_put_away.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            if (mOnIoItemClickListener != null) {
                switch (v.getId()) {
                    case R.id.act061_frag_item_cell_iv_serial:
                        mOnIoItemClickListener.onSerialClick(mFilteredValues.get(getAdapterPosition()));
                        break;
                    case R.id.act061_frag_item_cell_iv_conf:
                        mOnIoItemClickListener.onConfClick(mFilteredValues.get(getAdapterPosition()));
                        break;
                    case R.id.act061_frag_item_cell_iv_put_away:
                        mOnIoItemClickListener.onPutAwayClick(mFilteredValues.get(getAdapterPosition()));
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

        public void highlightCell(){
            cl_main.setBackground(
                context.getDrawable(R.drawable.lib_custom_cell_bg_pressed)
            );
        }
    }

    public class FooterVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btnAddItem;

        public FooterVH(@NonNull View itemView) {
            super(itemView);
            //
            btnAddItem = itemView.findViewById(R.id.act061_frag_item_footer_btn_add);
            btnAddItem.setText(hmAux_Trans.get("btn_add_item"));
            //
            if (inboundAllowNewItem) {
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
