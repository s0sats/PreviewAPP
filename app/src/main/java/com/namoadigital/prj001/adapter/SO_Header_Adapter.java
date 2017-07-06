package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class SO_Header_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<SM_SO> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;

    public SO_Header_Adapter(Context context, int resource, List<SM_SO> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "so_header_adapter"
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
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);
        }
        //Resgata item do list view.
        SM_SO so = source.get(position);
        //
        TextView tv_so_ttl = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_so_ttl);
        //SO Prefix.SO_code
        TextView tv_so_code = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_so_code);
        //
        TextView tv_so_id = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_so_id);
        //
        TextView tv_so_desc = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_so_desc);
        //
        TextView tv_site = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_site);
        //
        TextView tv_operation = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_operation);
        //
        TextView tv_contract = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_contract);
        //
        TextView tv_priority = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_priority);
        //
        TextView tv_status = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_status);
        //
        TextView tv_client = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_client);
        //
        TextView tv_deadline = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_deadline);
        //
        TextView tv_serial_ttl = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_serial_ttl);
        //
        TextView tv_product = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_product);
        //
        TextView tv_serial = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_serial);
        //
        TextView tv_segment = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_segment);
        //
        TextView tv_category_price = (TextView) convertView.findViewById(R.id.act024_content_cell_tv_category_price);
        //
        ImageView btn_download = (ImageView) convertView.findViewById(R.id.act024_content_cell_btn_single_download);
        //
        CheckBox chk_download = (CheckBox) convertView.findViewById(R.id.act024_content_cell_chk_download);
        //
        //Montagem dos dados na tela.
        //
        tv_so_ttl.setText(hmAux_Trans.get("so_main_title"));
        //
        tv_so_code.setText(hmAux_Trans.get("so_code_lbl")+ ": " + so.getSo_prefix() +"."+ so.getSo_code());
        //
        tv_so_id.setText(hmAux_Trans.get("so_id_lbl")+ ": " + so.getSo_id());
        //
        tv_so_desc.setText(hmAux_Trans.get("so_desc_lbl")+ ": " + so.getSo_desc());
        //
        tv_priority.setText(hmAux_Trans.get("priority_lbl")+ ": " + so.getPriority_desc() );
        //
        tv_status.setText(hmAux_Trans.get("status_lbl")+ ": " + so.getStatus() );
        //
        tv_deadline.setText(hmAux_Trans.get("deadline_lbl")+ ": " + so.getDeadline() );
        //
        tv_site.setText(hmAux_Trans.get("site_lbl")+ ": " + so.getSite_id() + " - " + so.getSite_desc() );
        //
        tv_operation.setText(hmAux_Trans.get("operation_lbl")+ ": " + so.getOperation_id() + " - " + so.getOperation_desc() );
        //
        tv_contract.setText(hmAux_Trans.get("contract_lbl")+ ": " + so.getContract_code() + " - " + so.getContract_desc());
        //
        tv_client.setText(hmAux_Trans.get("client_lbl")+ ": " + so.getClient_name());
        //
        tv_serial_ttl.setText(hmAux_Trans.get("serial_main_title"));
        //
        tv_product.setText(hmAux_Trans.get("product_lbl")+ ": " + so.getProduct_id() + " - " + so.getProduct_desc());
        //
        tv_serial.setText(hmAux_Trans.get("serial_lbl")+ ": " + so.getSerial_id());
        //
        tv_segment.setText(hmAux_Trans.get("segment_lbl")+ ": " + so.getSegment_id() + " - " + so.getSegment_desc());
        //
        tv_category_price.setText(hmAux_Trans.get("category_price_lbl")+ ": " + so.getCategory_price_id() + " - " + so.getCategory_price_desc());

        return convertView;
    }

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("so_main_title");
        translateList.add("so_code_lbl");
        translateList.add("so_id_lbl");
        translateList.add("so_desc_lbl");
        translateList.add("site_lbl");
        translateList.add("operation_lbl");
        translateList.add("contract_lbl");
        translateList.add("priority_lbl");
        translateList.add("status_lbl");
        translateList.add("client_lbl");
        translateList.add("deadline_lbl");
        translateList.add("serial_main_title");
        translateList.add("product_lbl");
        translateList.add("serial_lbl");
        translateList.add("segment_lbl");
        translateList.add("category_price_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }


}
