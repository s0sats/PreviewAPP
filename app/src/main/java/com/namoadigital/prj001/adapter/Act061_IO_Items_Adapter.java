package com.namoadigital.prj001.adapter;

import android.content.Context;
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
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act061_IO_Items_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private int resource;
    private List<HMAux> mValues;
    private List<HMAux> mFilteredValues;
    private IoItemFilter valueFilter;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String mResource_Name = "act061_io_items_adapter";
    private OnIoItemClickListener mOnIoItemClickListener;

    public interface OnIoItemClickListener{

        void onSerialClick(HMAux item);

        void onConfClick(HMAux item);

        void onPutAwayClick(HMAux item);
    }

    public OnIoItemClickListener getOnIoItemClickListener() {
        return mOnIoItemClickListener;
    }

    public void setOnIoItemClickListener(OnIoItemClickListener mOnIoItemClickListener) {
        this.mOnIoItemClickListener = mOnIoItemClickListener;
    }

    public Act061_IO_Items_Adapter(Context context, int resource, List<HMAux> mValues) {
        this.context = context;
        this.resource = resource;
        this.mValues = mValues;
        this.mFilteredValues = mValues;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
            .inflate(resource, viewGroup, false);
        return new IOInboundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        IOInboundViewHolder holder = (IOInboundViewHolder) viewHolder;
        holder.bindData(mFilteredValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilteredValues.size();
    }

    public class IOInboundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            tv_realized_lbl.setText(hmAux_Trans.get("suggestion_lbl"));
            tv_conf_dt_lbl.setText(hmAux_Trans.get("conf_date_lbl"));
            //
            iv_serial.setOnClickListener(this);
            iv_conf.setOnClickListener(this);
            iv_put_away.setOnClickListener(this);

        }

        public View getItemView() {
            return itemView;
        }

        public void bindData(HMAux item){
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
            if(!brandModelColor.isEmpty()) {
                tv_brand_model_color.setText(brandModelColor);
                tv_brand_model_color.setVisibility(View.VISIBLE);
            }
            //
            if(
                (item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_ZONE_DESC) && !item.get(IO_Inbound_ItemDao.PLANNED_ZONE_DESC).isEmpty())
                || (item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_LOCAL_ID) && !item.get(IO_Inbound_ItemDao.PLANNED_LOCAL_ID).isEmpty())
            ){
                String suggestedPosition = item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_ZONE_DESC) && !item.get(IO_Inbound_ItemDao.PLANNED_ZONE_DESC).isEmpty() ? item.get(IO_Inbound_ItemDao.PLANNED_ZONE_DESC) : "";
                suggestedPosition += item.hasConsistentValue(IO_Inbound_ItemDao.PLANNED_LOCAL_ID) && !item.get(IO_Inbound_ItemDao.PLANNED_LOCAL_ID).isEmpty() ? (!suggestedPosition.isEmpty() ? " | " : "" ) + item.get(IO_Inbound_ItemDao.PLANNED_LOCAL_ID) : "";
                tv_suggestion_val.setText(suggestedPosition);
            }
            //
            if(item.hasConsistentValue(IO_Inbound_ItemDao.CONF_DATE) && !item.get(IO_Inbound_ItemDao.CONF_DATE).isEmpty()){
                tv_realized_val.setText(item.get(IO_Inbound_ItemDao.ZONE_ID) +" | "+ item.get(IO_Inbound_ItemDao.LOCAL_ID) );
                tv_realized_lbl.setVisibility(View.VISIBLE);
                tv_realized_val.setVisibility(View.VISIBLE);
                //
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
            if(item.hasConsistentValue(IO_InboundDao.PUT_AWAY_PROCESS) && !item.get(IO_InboundDao.PUT_AWAY_PROCESS).isEmpty()){
                if(item.get(IO_InboundDao.PUT_AWAY_PROCESS).equals("0")){
                    iv_conf.setVisibility(View.VISIBLE);
                    iv_put_away.setVisibility(View.GONE);
                }else{
                    iv_conf.setVisibility(View.GONE);
                    iv_put_away.setVisibility(View.VISIBLE);
                }
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
            iv_conf.setVisibility(View.GONE);
            iv_put_away.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            if(mOnIoItemClickListener != null) {
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
            serialBrandModelColor +=  (data.get(MD_Product_SerialDao.MODEL_DESC) == null || data.get(MD_Product_SerialDao.MODEL_DESC).isEmpty() ? "" : " | " + data.get(MD_Product_SerialDao.MODEL_DESC));
            serialBrandModelColor +=  (data.get(MD_Product_SerialDao.COLOR_DESC) == null || data.get(MD_Product_SerialDao.COLOR_DESC).isEmpty() ? "" : " | " + data.get(MD_Product_SerialDao.COLOR_DESC));
            return serialBrandModelColor;
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new IoItemFilter();
        }
        return valueFilter;
    }

    private class IoItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }
}
