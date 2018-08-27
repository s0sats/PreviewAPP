package com.namoadigital.prj001.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act047_SO_Next_Orders_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<SO_Next_Orders_Obj>source;
    private int resource;
    private String mResource_Code;
    private String mResource_Name = "act047_next_orders_adapter";
    private HMAux hmAux_Trans;

    public Act047_SO_Next_Orders_Adapter(Context context, ArrayList<SO_Next_Orders_Obj> source, int resource) {
        this.context = context;
        this.source = source;
        this.resource = resource;
        //
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                mResource_Name
        );
        loadTranslation();
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource,parent,false);
        }
        //
        final SO_Next_Orders_Obj item = source.get(position);
        //IniVars
        TextView tv_so_ttl = (TextView) convertView.findViewById(R.id.act047_cell_tv_so_ttl);
        TextView tv_prefix_code = (TextView) convertView.findViewById(R.id.act047_cell_tv_prefix_code);
        ImageView iv_so_details = (ImageView) convertView.findViewById(R.id.act047_cell_iv_so_details);
        LinearLayout ll_so_id = (LinearLayout) convertView.findViewById(R.id.act047_cell_ll_so_id);
        TextView tv_so_id = (TextView) convertView.findViewById(R.id.act047_cell_tv_so_id);
        TextView tv_so_id_val = (TextView) convertView.findViewById(R.id.act047_cell_tv_so_id_val);
        TextView tv_status_lbl = (TextView) convertView.findViewById(R.id.act047_cell_tv_status_lbl);
        TextView tv_status_val = (TextView) convertView.findViewById(R.id.act047_cell_tv_status_val);
        LinearLayout ll_deadline = (LinearLayout) convertView.findViewById(R.id.act047_cell_ll_deadline);
        TextView tv_deadline_lbl = (TextView) convertView.findViewById(R.id.act047_cell_tv_deadline_lbl);
        TextView tv_deadline_val = (TextView) convertView.findViewById(R.id.act047_cell_tv_deadline_val);
        LinearLayout ll_operation = (LinearLayout) convertView.findViewById(R.id.act047_cell_ll_operation);
        TextView tv_operation_lbl = (TextView) convertView.findViewById(R.id.act047_cell_tv_operation_lbl);
        TextView tv_operation_val = (TextView) convertView.findViewById(R.id.act047_cell_tv_operation_val);
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.act047_cell_tv_serial_lbl);
        TextView tv_serial_id = (TextView) convertView.findViewById(R.id.act047_cell_tv_serial_id);
        TextView tv_product_lbl = (TextView) convertView.findViewById(R.id.act047_cell_tv_product_lbl);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.act047_cell_tv_product_val);
        LinearLayout ll_tracking = (LinearLayout) convertView.findViewById(R.id.act047_cell_ll_tracking);
        TextView tv_tracking_lbl = (TextView) convertView.findViewById(R.id.act047_cell_tv_tracking_lbl);
        TextView tv_tracking_val = (TextView) convertView.findViewById(R.id.act047_cell_tv_tracking_val);
        LinearLayout ll_brand_model_color = (LinearLayout) convertView.findViewById(R.id.act047_cell_ll_brand_model_color);
        TextView tv_brand_model_color = (TextView) convertView.findViewById(R.id.act047_cell_tv_brand_model_color_val);
        //
        //Seta Valores
        tv_so_ttl.setText(hmAux_Trans.get("so_main_title"));
        tv_prefix_code.setText(item.getSo_prefix()+"."+item.getSo_code());
        //
        if(!item.getSo_id().equals(item.getSo_prefix()+"."+item.getSo_code())) {
            ll_so_id.setVisibility(View.VISIBLE);
            tv_so_id.setText(hmAux_Trans.get("so_id_lbl"));
            tv_so_id_val.setText(item.getSo_id());

        }else{
            ll_so_id.setVisibility(View.GONE);
            tv_so_id.setText(hmAux_Trans.get("so_id_lbl"));
            tv_so_id_val.setText("");
        }
        //
        tv_status_lbl.setText(hmAux_Trans.get("status_lbl"));
        tv_status_val.setText((hmAux_Trans.get(item.getStatus())));
        tv_status_val.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(item.getStatus())));
        //
        if(item.getDeadline() == null || item.getDeadline().isEmpty()){
            ll_deadline.setVisibility(View.GONE);
            tv_deadline_lbl.setText(hmAux_Trans.get("deadline_lbl"));
            tv_deadline_val.setText("");
        }else{
            ll_deadline.setVisibility(View.VISIBLE);
            tv_deadline_lbl.setText(hmAux_Trans.get("deadline_lbl"));
            tv_deadline_val.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(item.getDeadline()),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
            );
        }
        //
        tv_serial_lbl.setText(hmAux_Trans.get("serial_main_title"));
        tv_serial_id.setText(item.getSerial_id());
        //
        tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
        //tv_product_val.setText(item.getProduct_id()+" - "+item.getProduct_desc());
        tv_product_val.setText(item.getProduct_id());
        //
        if(item.getTracking() == null || item.getTracking().isEmpty()) {
            ll_tracking.setVisibility(View.GONE);
            tv_tracking_lbl.setText(hmAux_Trans.get("tracking_lbl"));
            tv_tracking_val.setText("");
        }else{
            ll_tracking.setVisibility(View.VISIBLE);
            tv_tracking_lbl.setText(hmAux_Trans.get("tracking_lbl"));
            tv_tracking_val.setText(item.getTracking());
        }
        //
        if(item.getBrand_model_color() == null || item.getBrand_model_color().isEmpty()){
            ll_brand_model_color.setVisibility(View.GONE);
            tv_brand_model_color.setText("");
        }else{
            ll_brand_model_color.setVisibility(View.VISIBLE);
            tv_brand_model_color.setText(item.getBrand_model_color());
        }
        //
        iv_so_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailsDialog(item);
            }
        });
        //
        return convertView;
    }

    private void showDetailsDialog(SO_Next_Orders_Obj item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.act047_so_next_orders_dialog,null);
        //IniVars
        LinearLayout ll_so_desc = (LinearLayout) view.findViewById(R.id.act047_so_next_orders_dialog_ll_so_desc);
        TextView tv_so_desc_lbl = (TextView) view.findViewById(R.id.act047_so_next_orders_dialog_tv_so_desc_lbl);
        TextView tv_so_desc_val = (TextView) view.findViewById(R.id.act047_so_next_orders_dialog_tv_so_desc_val);
        LinearLayout ll_services = (LinearLayout) view.findViewById(R.id.act047_so_next_orders_dialog_ll_services);
        TextView tv_services_lbl = (TextView) view.findViewById(R.id.act047_so_next_orders_dialog_tv_services_lbl);
        TextView tv_services_val = (TextView) view.findViewById(R.id.act047_so_next_orders_dialog_tv_services_val);
        LinearLayout ll_so_comments = (LinearLayout) view.findViewById(R.id.act047_so_next_orders_dialog_ll_so_comment);
        TextView tv_so_comment_lbl = (TextView) view.findViewById(R.id.act047_so_next_orders_dialog_tv_so_comment_lbl);
        TextView tv_so_comment_val = (TextView) view.findViewById(R.id.act047_so_next_orders_dialog_tv_so_comment_val);
        //Seta data
        tv_so_desc_lbl.setText(hmAux_Trans.get("dialog_so_desc_lbl"));
        tv_so_desc_val.setText(item.getSo_desc());
        tv_services_lbl.setText(hmAux_Trans.get("dialog_services_lbl"));
        tv_services_val.setText(item.getService());
        tv_so_comment_lbl.setText(hmAux_Trans.get("dialog_so_comment_lbl"));
        tv_so_comment_val.setText(item.getComments());
        //
        builder
                .setTitle(hmAux_Trans.get("dialog_so_details_ttl"))
                .setView(view)
                .setPositiveButton(
                        hmAux_Trans.get("sys_alert_btn_ok"),
                        null
                );
        //
        builder.create().show();
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("so_main_title");
        translateList.add("so_id_lbl");
        translateList.add("so_desc_lbl");
        translateList.add("operation_lbl");
        translateList.add("status_lbl");
        translateList.add("deadline_lbl");
        translateList.add("serial_main_title");
        translateList.add("product_lbl");
        translateList.add("tracking_lbl");
        translateList.add("dialog_so_desc_lbl");
        translateList.add("dialog_services_lbl");
        translateList.add("dialog_so_comment_lbl");
        translateList.add("dialog_so_details_ttl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

}
