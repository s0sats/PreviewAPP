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
import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class SO_Header_Adapter extends BaseAdapter {
    public static final String CONFIG_TYPE_DOWNLOAD = "download";
    public static final String CONFIG_TYPE_EXIBITION_FULL = "CONFIG_TYPE_EXIBITION_FULL";
    public static final String CONFIG_TYPE_EXIBITION_SO = "CONFIG_TYPE_EXIBITION_SO";
    //
    private Context context;
    private int resource_01;
    private int resource_02;
    //private List<SM_SO> source;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String config_type;
    private boolean[] checkedStatus;
    private ISO_Header_Adapter delegate;

//    public SO_Header_Adapter(Context context, int resource_01, List<HMAux> source, String config_type) {
//        this.context = context;
//        this.resource_01 = resource_01;
//        this.source = source;
//        this.mResource_Code = ToolBox_Inf.getResourceCode(
//                context,
//                Constant.APP_MODULE,
//                "so_header_adapter"
//        );
//        this.config_type = config_type;
//        this.checkedStatus = new boolean[source.size()];
//        for (int i = 0; i < checkedStatus.length; i++) {
//            checkedStatus[i] = false;
//        }
//        loadTranslation();
//    }

    public SO_Header_Adapter(Context context, List<HMAux> source, String config_type, int resource_01, int resource_02) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
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

    public interface ISO_Header_Adapter {
        void downloadBtnClicked(HMAux so);

        void refreshSelectedQty(int qty_selected);
    }

    public void setOnDownloadBtnClicked(ISO_Header_Adapter delegate) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        HMAux so = source.get(position);
        //
        switch (so.get(Constant.PARAM_KEY_TYPE)) {
            case Constant.PARAM_KEY_TYPE_SO_EXPRESS:
                return 1;
            case Constant.PARAM_KEY_TYPE_SO:
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                //SO_EXPRESS
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;
                case 0:
                default:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;

            }
            //
        }
        //Define qual metodo tratará o layout
        switch (getItemViewType(position)) {
            //SO_EXPRESS
            case 1:
                processSOExpress(convertView, position);
                break;
            case 0:
            default:
                processSO(convertView, position);
                break;
        }
        //
        return convertView;
    }

    private void processSO(View convertView, int position) {
        //Resgata item do list view.
        HMAux so = source.get(position);
        //
        TextView tv_so_ttl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_ttl);
        //SO Prefix.SO_code
        LinearLayout ll_prefix_code = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_prefix_code);
        TextView tv_so_prefix_code = (TextView) convertView.findViewById(R.id.so_header_cell_tv_prefix_code);

        LinearLayout ll_so_id = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_so_id);
        TextView tv_so_id_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_id);
        TextView tv_so_id_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_id_val);
        //
        LinearLayout ll_so_desc = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_so_desc);
        TextView tv_so_desc_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_desc);
        //
        LinearLayout ll_priority = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_priority);
        TextView tv_priority_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_priority_lbl);
        TextView tv_priority_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_priority_val);
        //
        LinearLayout ll_status = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_status);
        TextView tv_status_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_status_lbl);
        TextView tv_status_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_status_val);
        //
        LinearLayout ll_express_so = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_express_ret);
        TextView tv_express_ret_msg = (TextView) convertView.findViewById(R.id.so_header_cell_tv_express_ret_msg);
        //
        LinearLayout ll_deadline = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_deadline);
        TextView tv_deadline_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_deadline_lbl);
        TextView tv_deadline_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_deadline_val);
        //
        LinearLayout ll_site = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_site);
        TextView tv_site_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_site_lbl);
        TextView tv_site_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_site_val);
        //
        LinearLayout ll_operation = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_operation);
        TextView tv_operation_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_operation_lbl);
        TextView tv_operation_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_operation_val);
        //
        LinearLayout ll_contract = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_contract);
        TextView tv_contract_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_contract_lbl);
        TextView tv_contract_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_contract_val);
        //
        LinearLayout ll_client = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_client);
        TextView tv_client_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_client_lbl);
        TextView tv_client_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_client_val);
        //
        LinearLayout ll_serial_info = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_serial_info);
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_serial_lbl);
        TextView tv_serial_id = (TextView) convertView.findViewById(R.id.so_header_cell_tv_serial_id);
        //
        LinearLayout ll_product = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_product);
        TextView tv_product_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_product_lbl);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_product_val);
        //
        LinearLayout ll_segment = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_segment);
        TextView tv_segment_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_segment_lbl);
        TextView tv_segment_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_segment_val);
        //
        LinearLayout ll_category_price = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_category_price);
        TextView tv_category_price_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_category_price_lbl);
        TextView tv_category_price_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_category_price_val);
        //
        LinearLayout ll_brand_model_color = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_brand_model_color);
        TextView tv_brand_model_color_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_brand_model_color_val);
        //
        LinearLayout ll_download_optc = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_download_opt);
        //
        ImageView btn_download = (ImageView) convertView.findViewById(R.id.so_header_cell_btn_single_download);
        //
        CheckBox chk_download = (CheckBox) convertView.findViewById(R.id.so_header_cell_chk_download);
        //SO EXPRESS
        LinearLayout ll_so_express = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_express_icon);
        //
        ImageView iv_express_so = (ImageView) convertView.findViewById(R.id.so_header_cell_iv_express_so);
        //
        //Montagem dos dados na tela.
        //
        tv_so_ttl.setText(hmAux_Trans.get("so_main_title"));
        //
        tv_so_prefix_code.setText(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE));
        //
        tv_so_id_lbl.setText(hmAux_Trans.get("so_id_lbl"));
        tv_so_id_val.setText(so.get(SM_SODao.SO_ID));
        if (so.get(SM_SODao.SO_ID).equals(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE))) {
            ll_so_id.setVisibility(View.GONE);
        } else {
            ll_so_id.setVisibility(View.VISIBLE);
        }
        //
        tv_so_desc_val.setText(hmAux_Trans.get("so_desc_lbl") + ": " + so.get(SM_SODao.SO_DESC));
        if (so.get(SM_SODao.SO_DESC) != null && so.get(SM_SODao.SO_DESC).length() > 0) {
            ll_so_desc.setVisibility(View.VISIBLE);
        } else {
            ll_so_desc.setVisibility(View.GONE);
        }
        //
        tv_priority_lbl.setText(hmAux_Trans.get("priority_lbl"));
        tv_priority_val.setText(so.get(SM_SODao.PRIORITY_DESC));
        //
        tv_status_lbl.setText(hmAux_Trans.get("status_lbl"));
        tv_status_val.setText(hmAux_Trans.get(so.get(SM_SODao.STATUS)));
        ToolBox_Inf.setSOStatusColor(context, tv_status_val, so.get(SM_SODao.STATUS));
        //Só exibir se for Express
        ll_express_so.setVisibility(View.GONE);
        //
        tv_deadline_lbl.setText(hmAux_Trans.get("deadline_lbl"));
        if (so.get(SM_SODao.DEADLINE) != null && so.get(SM_SODao.DEADLINE).length() > 0) {
            ll_deadline.setVisibility(View.VISIBLE);
            tv_deadline_val.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(so.get(SM_SODao.DEADLINE)),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )

            );
        } else {
            ll_deadline.setVisibility(View.GONE);
            tv_deadline_val.setText("");
        }
        //
        tv_site_lbl.setText(hmAux_Trans.get("site_lbl"));
        tv_site_val.setText(so.get(SM_SODao.SITE_ID) + " - " + so.get(SM_SODao.SITE_DESC));
        //
        tv_operation_lbl.setText(hmAux_Trans.get("operation_lbl"));
        tv_operation_val.setText(so.get(SM_SODao.OPERATION_ID) + " - " + so.get(SM_SODao.OPERATION_DESC));
        //
        tv_contract_lbl.setText(hmAux_Trans.get("contract_lbl"));
        tv_contract_val.setText(so.get(SM_SODao.CONTRACT_CODE) + " - " + so.get(SM_SODao.CONTRACT_DESC));
        //
        tv_client_lbl.setText(hmAux_Trans.get("client_lbl"));
        tv_client_val.setText(so.get(SM_SODao.CLIENT_NAME));
        //
        if (so.get(SM_SODao.CLIENT_TYPE).equals(Constant.CLIENT_TYPE_CLIENT)) {
            ll_client.setVisibility(View.VISIBLE);
        } else {
            ll_client.setVisibility(View.GONE);
        }
        //
        tv_serial_lbl.setText(hmAux_Trans.get("serial_main_title"));
        tv_serial_id.setText(so.get(SM_SODao.SERIAL_ID));
        //
        tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
        tv_product_val.setText(so.get(SM_SODao.PRODUCT_ID) + " - " + so.get(SM_SODao.PRODUCT_DESC));
        //
        tv_segment_lbl.setText(hmAux_Trans.get("segment_lbl"));
        tv_segment_val.setText(so.get(SM_SODao.SEGMENT_ID) + " - " + so.get(SM_SODao.SEGMENT_DESC));
        //
        tv_category_price_lbl.setText(hmAux_Trans.get("category_price_lbl"));
        tv_category_price_val.setText(so.get(SM_SODao.CATEGORY_PRICE_ID) + " - " + so.get(SM_SODao.CATEGORY_PRICE_DESC));
        //
        String brand_model_color = "";
        if ((so.containsKey(MD_BrandDao.BRAND_DESC)
                || so.containsKey(MD_Brand_ModelDao.MODEL_DESC)
                || so.containsKey(MD_Brand_ColorDao.COLOR_DESC)
        )
                ) {
            brand_model_color += so.get(MD_BrandDao.BRAND_DESC).length() == 0 ? "" : "| " + so.get(MD_BrandDao.BRAND_DESC) + " ";
            brand_model_color += so.get(MD_Brand_ModelDao.MODEL_DESC).length() == 0 ? "" : "| " + so.get(MD_Brand_ModelDao.MODEL_DESC) + " ";
            brand_model_color += so.get(MD_Brand_ColorDao.COLOR_DESC).length() == 0 ? "" : "| " + so.get(MD_Brand_ColorDao.COLOR_DESC) + " ";
        }
        //
        if (brand_model_color.length() > 0) {
            brand_model_color = brand_model_color.substring(1, brand_model_color.length());
            tv_brand_model_color_val.setText(brand_model_color);
            ll_brand_model_color.setVisibility(View.VISIBLE);
        } else {
            tv_brand_model_color_val.setText("");
            ll_brand_model_color.setVisibility(View.GONE);
        }
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
        //so exibir se So Express
        ll_so_express.setVisibility(View.GONE);
        //
        /*
         * Tratativas
         *
         */

//        if (config_type.equals(CONFIG_TYPE_DOWNLOAD)) {
//            //Se status da OS for edit ou stop, não exibe opções de download.
//            if (so.get(SM_SODao.STATUS).equals(Constant.SYS_STATUS_EDIT) || so.get(SM_SODao.STATUS).equals(Constant.SYS_STATUS_STOP)) {
//                ll_download_optc.setVisibility(View.GONE);
//            } else {
//                ll_download_optc.setVisibility(View.VISIBLE);
//            }
//        }
        ll_download_optc.setVisibility(View.GONE);

        if (config_type.equals(CONFIG_TYPE_EXIBITION_SO)) {
            ll_serial_info.setVisibility(View.GONE);
        } else {
            ll_serial_info.setVisibility(View.VISIBLE);
        }
    }

    private void processSOExpress(View convertView, int position) {
        //Resgata item do list view.
        HMAux so_express = source.get(position);
        //
        TextView tv_so_ttl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_ttl);
        //SO Prefix.SO_code
        LinearLayout ll_prefix_code = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_prefix_code);
        TextView tv_so_prefix_code = (TextView) convertView.findViewById(R.id.so_header_cell_tv_prefix_code);

        LinearLayout ll_so_id = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_so_id);
        TextView tv_so_id_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_id);
        TextView tv_so_id_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_id_val);
        //
        LinearLayout ll_so_desc = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_so_desc);
        TextView tv_so_desc_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_so_desc);
        //
        LinearLayout ll_priority = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_priority);
        TextView tv_priority_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_priority_lbl);
        TextView tv_priority_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_priority_val);
        //
        LinearLayout ll_status = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_status);
        TextView tv_status_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_status_lbl);
        TextView tv_status_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_status_val);
        //
        LinearLayout ll_express_ret = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_express_ret);
        TextView tv_express_ret_msg = (TextView) convertView.findViewById(R.id.so_header_cell_tv_express_ret_msg);
        //
        LinearLayout ll_deadline = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_deadline);
        TextView tv_deadline_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_deadline_lbl);
        TextView tv_deadline_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_deadline_val);
        //
        LinearLayout ll_site = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_site);
        TextView tv_site_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_site_lbl);
        TextView tv_site_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_site_val);
        //
        LinearLayout ll_operation = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_operation);
        TextView tv_operation_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_operation_lbl);
        TextView tv_operation_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_operation_val);
        //
        LinearLayout ll_contract = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_contract);
        TextView tv_contract_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_contract_lbl);
        TextView tv_contract_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_contract_val);
        //
        LinearLayout ll_client = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_client);
        TextView tv_client_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_client_lbl);
        TextView tv_client_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_client_val);
        //
        LinearLayout ll_serial_info = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_serial_info);
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_serial_lbl);
        TextView tv_serial_id = (TextView) convertView.findViewById(R.id.so_header_cell_tv_serial_id);
        //
        LinearLayout ll_product = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_product);
        TextView tv_product_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_product_lbl);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_product_val);
        //
        LinearLayout ll_segment = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_segment);
        TextView tv_segment_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_segment_lbl);
        TextView tv_segment_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_segment_val);
        //
        LinearLayout ll_category_price = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_category_price);
        TextView tv_category_price_lbl = (TextView) convertView.findViewById(R.id.so_header_cell_tv_category_price_lbl);
        TextView tv_category_price_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_category_price_val);
        //} so_header_cell_ll_brand_model_color
        LinearLayout ll_brand_model_color = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_brand_model_color);
        TextView tv_brand_model_color_val = (TextView) convertView.findViewById(R.id.so_header_cell_tv_brand_model_color_val);
        //
        LinearLayout ll_download_optc = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_download_opt);
        //SO EXPRESS
        LinearLayout ll_so_express = (LinearLayout) convertView.findViewById(R.id.so_header_cell_ll_express_icon);
        //
        ImageView iv_express_so = (ImageView) convertView.findViewById(R.id.so_header_cell_iv_express_so);
        //
        //Montagem dos dados na tela.
        //
        tv_so_ttl.setText(hmAux_Trans.get("so_main_title"));
        //
        if (!so_express.get(SM_SODao.SO_PREFIX).isEmpty()) {
            ll_prefix_code.setVisibility(View.VISIBLE);
            tv_so_prefix_code.setText(so_express.get(SM_SODao.SO_PREFIX) + "." + so_express.get(SM_SODao.SO_CODE));
        } else {
            tv_so_prefix_code.setText("");
            ll_prefix_code.setVisibility(View.INVISIBLE);
        }
        //
        tv_so_id_lbl.setText(hmAux_Trans.get("so_id_lbl"));
        tv_so_id_val.setText(so_express.get(SM_SODao.SO_ID));
        if (so_express.get(SM_SODao.SO_ID).equals(so_express.get(SM_SODao.SO_PREFIX) + "." + so_express.get(SM_SODao.SO_CODE))) {
            ll_so_id.setVisibility(View.GONE);
        } else {
            ll_so_id.setVisibility(View.VISIBLE);
        }
        //
        tv_so_desc_val.setText(hmAux_Trans.get("so_desc_lbl") + ": " + so_express.get(SM_SODao.SO_DESC));
        if (so_express.get(SM_SODao.SO_DESC) != null && so_express.get(SM_SODao.SO_DESC).length() > 0) {
            ll_so_desc.setVisibility(View.VISIBLE);
        } else {
            ll_so_desc.setVisibility(View.GONE);
        }
        //
        tv_priority_lbl.setText(hmAux_Trans.get("priority_lbl"));
        tv_priority_val.setText(so_express.get(SM_SODao.PRIORITY_DESC));
        //
        tv_status_lbl.setText(hmAux_Trans.get("status_lbl"));
        tv_status_val.setText(hmAux_Trans.get(so_express.get(SO_Pack_Express_LocalDao.SO_STATUS)));
        ToolBox_Inf.setSOStatusColor(context, tv_status_val, so_express.get(SO_Pack_Express_LocalDao.SO_STATUS));
        //
        if(so_express.get(SO_Pack_Express_LocalDao.SO_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_DENIED)
           || so_express.get(SO_Pack_Express_LocalDao.SO_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_ERROR)
        ){
            ll_express_ret.setVisibility(View.VISIBLE);
            tv_express_ret_msg.setText(so_express.get(SO_Pack_Express_LocalDao.RET_MSG));
            ToolBox_Inf.setSOStatusColor(context, tv_express_ret_msg, so_express.get(SO_Pack_Express_LocalDao.SO_STATUS));
        }else{
            ll_express_ret.setVisibility(View.GONE);
            tv_express_ret_msg.setText(so_express.get(SO_Pack_Express_LocalDao.RET_MSG));

        }
        //
        tv_deadline_lbl.setText(hmAux_Trans.get("deadline_lbl"));
            ll_deadline.setVisibility(View.GONE);
            tv_deadline_val.setText("");
        //
        tv_site_lbl.setText(hmAux_Trans.get("site_lbl"));
        tv_site_val.setText(so_express.get(SM_SODao.SITE_ID) + " - " + so_express.get(SM_SODao.SITE_DESC));
        //
        tv_operation_lbl.setText(hmAux_Trans.get("operation_lbl"));
        tv_operation_val.setText(so_express.get(SM_SODao.OPERATION_ID) + " - " + so_express.get(SM_SODao.OPERATION_DESC));
        //
        tv_contract_lbl.setText(hmAux_Trans.get("contract_lbl"));
        tv_contract_val.setText(so_express.get(SM_SODao.CONTRACT_CODE) + " - " + so_express.get(SM_SODao.CONTRACT_DESC));
        //
        tv_client_lbl.setText(hmAux_Trans.get("client_lbl"));
        tv_client_val.setText(so_express.get(SM_SODao.CLIENT_NAME));
        //
        ll_client.setVisibility(View.GONE);
        //
        tv_serial_lbl.setText(hmAux_Trans.get("serial_main_title"));
        tv_serial_id.setText(so_express.get(SM_SODao.SERIAL_ID));
        //
        tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
        tv_product_val.setText(so_express.get(SM_SODao.PRODUCT_ID) + " - " + so_express.get(SM_SODao.PRODUCT_DESC));
        //
        tv_segment_lbl.setText(hmAux_Trans.get("segment_lbl"));
        tv_segment_val.setText(so_express.get(SM_SODao.SEGMENT_ID) + " - " + so_express.get(SM_SODao.SEGMENT_DESC));
        //
        tv_category_price_lbl.setText(hmAux_Trans.get("category_price_lbl"));
        tv_category_price_val.setText(so_express.get(SM_SODao.CATEGORY_PRICE_ID) + " - " + so_express.get(SM_SODao.CATEGORY_PRICE_DESC));
        //
        String brand_model_color = "";
        if ((so_express.containsKey(MD_BrandDao.BRAND_DESC)
                || so_express.containsKey(MD_Brand_ModelDao.MODEL_DESC)
                || so_express.containsKey(MD_Brand_ColorDao.COLOR_DESC)
        )
                ) {
            brand_model_color += so_express.get(MD_BrandDao.BRAND_DESC).length() == 0 ? "" : "| " + so_express.get(MD_BrandDao.BRAND_DESC) + " ";
            brand_model_color += so_express.get(MD_Brand_ModelDao.MODEL_DESC).length() == 0 ? "" : "| " + so_express.get(MD_Brand_ModelDao.MODEL_DESC) + " ";
            brand_model_color += so_express.get(MD_Brand_ColorDao.COLOR_DESC).length() == 0 ? "" : "| " + so_express.get(MD_Brand_ColorDao.COLOR_DESC) + " ";
        }
        //
        if (brand_model_color.length() > 0) {
            brand_model_color = brand_model_color.substring(1, brand_model_color.length());
            tv_brand_model_color_val.setText(brand_model_color);
            ll_brand_model_color.setVisibility(View.VISIBLE);
        } else {
            tv_brand_model_color_val.setText("");
            ll_brand_model_color.setVisibility(View.GONE);
        }
        //
        ll_so_express.setVisibility(View.VISIBLE);

        //
        /*
         * Tratativas
         *
         */

        ll_serial_info.setVisibility(View.VISIBLE);
        ll_so_id.setVisibility(View.GONE);
        ll_priority.setVisibility(View.GONE);
        ll_deadline.setVisibility(View.GONE);
        ll_contract.setVisibility(View.GONE);
        ll_client.setVisibility(View.GONE);
        ll_category_price.setVisibility(View.GONE);
        ll_segment.setVisibility(View.GONE);
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
                    if (checkedStatus[i]) {
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
