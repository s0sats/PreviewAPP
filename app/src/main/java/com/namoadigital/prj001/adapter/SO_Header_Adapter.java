package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class SO_Header_Adapter extends BaseAdapter {
    public static final String CONFIG_TYPE_DOWNLOAD ="download";
    public static final String CONFIG_TYPE_EXIBITION ="exibition";
    //
    private Context context;
    private int resource;
    //private List<SM_SO> source;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String config_type;
    private boolean[] checkedStatus;
    private ISO_Header_Adapter delegate;

    public SO_Header_Adapter(Context context, int resource, List<HMAux> source,String config_type) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "so_header_adapter"
        );
        this.config_type = config_type;
        this.checkedStatus = new boolean[source.size()];
        for (int i = 0; i < checkedStatus.length; i++) {
            checkedStatus[i] = false;
        }
        loadTranslation();
    }

    public interface ISO_Header_Adapter{
        void downloadBtnClicked(HMAux so);

        void refreshSelectedQty(int qty_selected);
    }

    public void setOnDownloadBtnClicked(ISO_Header_Adapter delegate){
        this.delegate = delegate;
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

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }
        //Resgata item do list view.
        HMAux so = source.get(position);
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
        LinearLayout ll_download_optc = (LinearLayout) convertView.findViewById(R.id.act024_content_cell_ll_download_opt);
        //
        ImageView btn_download = (ImageView) convertView.findViewById(R.id.act024_content_cell_btn_single_download);
        //
        CheckBox chk_download = (CheckBox) convertView.findViewById(R.id.act024_content_cell_chk_download);
        //
        //Montagem dos dados na tela.
        //
        tv_so_ttl.setText(hmAux_Trans.get("so_main_title"));
        //
        tv_so_code.setText(hmAux_Trans.get("so_code_lbl") + ": " + so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE));
        //
        tv_so_id.setText(hmAux_Trans.get("so_id_lbl") + ": " + so.get(SM_SODao.SO_ID));
        //
        tv_so_desc.setText(hmAux_Trans.get("so_desc_lbl") + ": " + so.get(SM_SODao.SO_DESC));
        //
        tv_priority.setText(hmAux_Trans.get("priority_lbl") + ": " + so.get(SM_SODao.PRIORITY_DESC));
        //
        tv_status.setText(hmAux_Trans.get("status_lbl") + ": " + so.get(SM_SODao.STATUS));
        //
        if(so.get(SM_SODao.DEADLINE) != null && so.get(SM_SODao.DEADLINE).length() > 0){
            tv_deadline.setVisibility(View.VISIBLE);
            tv_deadline.setText(hmAux_Trans.get("deadline_lbl") + ": " + so.get(SM_SODao.DEADLINE));
        }else{
            tv_deadline.setVisibility(View.GONE);
            tv_deadline.setText("");
        }
        //
        tv_site.setText(hmAux_Trans.get("site_lbl") + ": " +so.get(SM_SODao.SITE_ID) + " - " + so.get(SM_SODao.SITE_DESC));
        //
        tv_operation.setText(hmAux_Trans.get("operation_lbl") + ": " + so.get(SM_SODao.OPERATION_ID) + " - " + so.get(SM_SODao.OPERATION_DESC));
        //
        tv_contract.setText(hmAux_Trans.get("contract_lbl") + ": " + so.get(SM_SODao.CONTRACT_CODE)+ " - " + so.get(SM_SODao.CONTRACT_DESC));
        //
        tv_client.setText(hmAux_Trans.get("client_lbl") + ": " + so.get(SM_SODao.CLIENT_NAME));
        //
        tv_serial_ttl.setText(hmAux_Trans.get("serial_main_title"));
        //
        tv_product.setText(hmAux_Trans.get("product_lbl") + ": " + so.get(SM_SODao.PRODUCT_ID) + " - " + so.get(SM_SODao.PRODUCT_DESC));
        //
        tv_serial.setText(hmAux_Trans.get("serial_lbl") + ": " + so.get(SM_SODao.SERIAL_ID));
        //
        tv_segment.setText(hmAux_Trans.get("segment_lbl") + ": " + so.get(SM_SODao.SEGMENT_ID) + " - " + so.get(SM_SODao.SEGMENT_DESC));
        //
        tv_category_price.setText(hmAux_Trans.get("category_price_lbl") + ": " + so.get(SM_SODao.CATEGORY_PRICE_ID) + " - " + so.get(SM_SODao.CATEGORY_PRICE_DESC));
        //
        //Checkbox
        chk_download.setTag(position);
        //
        chk_download.setOnCheckedChangeListener(chkListner);
        //
        chk_download.setChecked(checkedStatus[position]);
        //
        btn_download.setTag(position);
        //
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.downloadBtnClicked(source.get((Integer) v.getTag()));
                }
            }
        });
        //
        /*
        * Tratativas
        *
        */
        if(config_type.equals(CONFIG_TYPE_DOWNLOAD)) {
            //Se status da OS for edit ou stop, não exibe opções de download.
            if (so.get(SM_SODao.STATUS).equals(Constant.SO_STATUS_EDIT) || so.get(SM_SODao.STATUS).equals(Constant.SO_STATUS_STOP)) {
                ll_download_optc.setVisibility(View.GONE);
            } else {
                ll_download_optc.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    public ArrayList<HMAux> getSoToDownload() {
        ArrayList<HMAux> downloadList = new ArrayList<>();
        //
        for (int i = 0; i < checkedStatus.length; i++) {
            if (checkedStatus[i]) {
                downloadList.add(source.get(i));
            }
        }
        //
        return downloadList;
    }
    //
    private CompoundButton.OnCheckedChangeListener chkListner = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkedStatus[(int) buttonView.getTag()] = isChecked;
            //Ao clicar no checkbox, devolve a qtd de chk marcados.
            int qtySelected = 0;
            if (delegate != null) {
                for (int i = 0; i < checkedStatus.length; i++) {
                    if(checkedStatus[i]){
                        qtySelected++;
                    }
                }
                delegate.refreshSelectedQty(qtySelected);
            }
        }
    };

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
