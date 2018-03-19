package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 11/04/2017.
 */

public class Module_Schedules_Adapter extends BaseAdapter {

    private Context context;
    private int resource_01;
    private int resource_02;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Module_Schedules_Adapter(Context context, int resource_01, int resource_02, List<HMAux> source) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.source = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                                    context,
                                    Constant.APP_MODULE,
                                    "module_schedules_adapter"
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        HMAux item = source.get(position);
        //
        switch (item.get(Act017_Main.ACT017_MODULE_KEY)){
            case Constant.MODULE_CHECKLIST:
                return 0;
            case Constant.MODULE_FORM_AP:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Resgata HmAux com as informações
        HMAux item = source.get(position);
        //
        if(convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(resource_01,parent,false);
                    processFormItem(item,convertView);
                    break;
                case 1:
                    convertView = mInflater.inflate(resource_02,parent,false);
                    processFormAPItem(item,convertView);
                    break;
            }
        }
        //
        return convertView;
    }

    private void processFormAPItem(HMAux item, View convertView) {
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.namoa_ap_tv_form_ttl);
        ImageView iv_sync_upload = (ImageView) convertView.findViewById(R.id.namoa_ap_iv_sync_upload);
        TextView tv_type = (TextView) convertView.findViewById(R.id.namoa_ap_tv_type_label);
        TextView tv_module_ttl = (TextView) convertView.findViewById(R.id.namoa_ap_tv_module_ttl);
        TextView tv_form_label = (TextView) convertView.findViewById(R.id.namoa_ap_tv_form_label);
        TextView tv_data_serv = (TextView) convertView.findViewById(R.id.namoa_ap_tv_data_serv_lbl);
        TextView tv_product = (TextView) convertView.findViewById(R.id.namoa_ap_tv_product_lbl);
        TextView tv_serial = (TextView) convertView.findViewById(R.id.namoa_ap_tv_serial_lbl);
        TextView tv_ap_ttl = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_ttl);
        TextView tv_ap_code = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_code_lbl);
        TextView tv_ap_status = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_status_lbl);
        TextView tv_ap_what = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_what_lbl);
        TextView tv_ap_who = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_who_lbl);
        TextView tv_ap_when = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_when_lbl);
        //
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_type_val);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_form_val);
        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_data_serv_val);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_product_val);
        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_serial_val);
        TextView tv_ap_code_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_code_val);
        TextView tv_ap_status_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_status_val);
        TextView tv_ap_what_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_what_val);
        TextView tv_ap_when_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_when_val);
        TextView tv_ap_who_val = (TextView) convertView.findViewById(R.id.namoa_ap_tv_ap_who_val);
        LinearLayout ll_action_btn = (LinearLayout) convertView.findViewById(R.id.namoa_ap_ll_action_btn);
        //
        tv_module_ttl.setText("N-Form AP -trad");
        tv_module_ttl.setVisibility(View.VISIBLE);
        //
        ll_action_btn.setVisibility(View.GONE);
        //
        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
        if(item.get(GE_Custom_Form_ApDao.SYNC_REQUIRED).equals("1")){
            iv_sync_upload.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down_thick_black_24dp));
            iv_sync_upload.setVisibility(View.VISIBLE);
        }else if(item.get(GE_Custom_Form_ApDao.UPLOAD_REQUIRED).equals("1")){
            iv_sync_upload.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_up_thick_black_24dp));
            iv_sync_upload.setVisibility(View.VISIBLE);
        }else{
            iv_sync_upload.setVisibility(View.GONE);
        }
        tv_type.setText(hmAux_Trans.get("form_type_lbl"));
        tv_type_val.setText(
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE) + " - " +
                        item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC)
        );
        //
        tv_form_label.setText(hmAux_Trans.get("form_code_lbl"));
        tv_form_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE) + " - " +
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION) + " - " +
                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC)
        );
        //
        tv_data_serv.setText(hmAux_Trans.get("form_data_lbl"));
        tv_data_serv_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        //
        tv_product.setText(hmAux_Trans.get("product_code_lbl"));
        tv_product_val.setText(
                item.get(GE_Custom_Form_ApDao.PRODUCT_ID) + " - " +
                        item.get(GE_Custom_Form_ApDao.PRODUCT_DESC)
        );
        //
        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
        tv_serial_val.setText(item.get(GE_Custom_Form_ApDao.SERIAL_ID));
        //
        tv_ap_ttl.setText(hmAux_Trans.get("ap_ttl"));
        //
        tv_ap_code.setText(hmAux_Trans.get("ap_code_lbl"));
        tv_ap_code_val.setText(
                item.get(GE_Custom_Form_ApDao.AP_CODE) + " - " +
                        item.get(GE_Custom_Form_ApDao.AP_DESCRIPTION)
        );
        tv_ap_status.setText(hmAux_Trans.get("ap_status_lbl"));
        tv_ap_status_val.setText(
                hmAux_Trans.get(item.get(
                        GE_Custom_Form_ApDao.AP_STATUS
                        )
                )
        );
        ToolBox_Inf.setAPStatusColor(
                context,
                tv_ap_status_val,
                item.get(GE_Custom_Form_ApDao.AP_STATUS)
        );
        tv_ap_what.setText(hmAux_Trans.get("ap_what_lbl"));
        tv_ap_what_val.setText(
                ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(item.get(GE_Custom_Form_ApDao.AP_WHAT)), 45)
        );
        tv_ap_who.setText(hmAux_Trans.get("ap_who_lbl"));
        tv_ap_who_val.setText(
                item.get(GE_Custom_Form_ApDao.AP_WHO_NICK)
        );
        tv_ap_when.setText( hmAux_Trans.get("ap_when_lbl"));
        if(!item.get(GE_Custom_Form_ApDao.AP_WHEN).isEmpty()){
            tv_ap_when_val.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN)),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
            );
        }else{
            tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHEN));
        }

    }

    private void processFormItem(HMAux item, View convertView) {
        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.module_schedules_cell_ll_bg);
        //
        ArrayList<TextView> tv_list = new ArrayList<>();
        //
        ImageView iv_main = (ImageView) convertView.findViewById(R.id.module_schedules_cell_iv_main);
        //
        TextView tv_main_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_main_lbl);
        TextView tv_main_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_main_val);

        tv_list.add(tv_main_lbl);
        tv_list.add(tv_main_val);

        //
        TextView tv_date_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_date_label);
        TextView tv_date_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_date_val);

        tv_list.add(tv_date_lbl);
        tv_list.add(tv_date_val);
        //
        TextView tv_ttl_001 = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_ttl_001);


        tv_list.add(tv_ttl_001);
        //
        TextView tv_item_01_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_01_lbl);
        TextView tv_item_01_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_01_val);

        tv_list.add(tv_item_01_lbl);
        tv_list.add(tv_item_01_val);
        //
        TextView tv_item_02_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_02_lbl);
        TextView tv_item_02_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_02_val);

        tv_list.add(tv_item_02_lbl);
        tv_list.add(tv_item_02_val);
        //
        TextView tv_item_03_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_03_lbl);
        TextView tv_item_03_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_03_val);

        tv_list.add(tv_item_03_lbl);
        tv_list.add(tv_item_03_val);
        //
        //
        TextView tv_item_04_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_04_lbl);
        TextView tv_item_04_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_04_val);

        tv_list.add(tv_item_04_lbl);
        tv_list.add(tv_item_04_val);
        //
        TextView tv_ttl_002 = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_ttl_002);

        tv_list.add(tv_ttl_002);
        //
        TextView tv_item_05_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_05_lbl);
        TextView tv_item_05_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_05_val);

        tv_list.add(tv_item_05_lbl);
        tv_list.add(tv_item_05_val);
        //
        TextView tv_item_06_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_06_lbl);
        TextView tv_item_06_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_06_val);

        tv_list.add(tv_item_06_lbl);
        tv_list.add(tv_item_06_val);
        //
        TextView tv_item_07_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_07_lbl);
        TextView tv_item_07_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_07_val);

        tv_list.add(tv_item_07_lbl);
        tv_list.add(tv_item_07_val);
        //
        TextView tv_item_08_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_08_lbl);
        TextView tv_item_08_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_08_val);

        tv_list.add(tv_item_08_lbl);
        tv_list.add(tv_item_08_val);
        //
        Drawable llDrawable = null;

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)){

            case Constant.MODULE_CHECKLIST:

                iv_main.setImageDrawable(context.getDrawable(R.drawable.ic_n_form));
                //APÓS IMPLANTAÇÃO DO FORM_AP, ICONE FOI ESCONDIDO
                iv_main.setVisibility(View.GONE);
                //tv_main_lbl.setText(hmAux_Trans.get("lbl_module")+" "+item.get(Act017_Main.ACT017_MODULE_KEY));
                tv_main_lbl.setText(hmAux_Trans.get("CHECKLIST"));

                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));
                tv_ttl_001.setText(hmAux_Trans.get("ttl_product"));
                tv_item_01_lbl.setText(hmAux_Trans.get("lbl_product_code")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
                tv_item_02_lbl.setText(hmAux_Trans.get("lbl_product_id")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));
                tv_item_03_lbl.setText(hmAux_Trans.get("lbl_product_desc")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
                tv_item_04_lbl.setText(hmAux_Trans.get("lbl_serial_id")+" "+item.get(GE_Custom_Form_LocalDao.SERIAL_ID));

                if(item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0){
                    tv_item_04_lbl.setVisibility(View.GONE);
                    tv_item_04_val.setVisibility(View.GONE);
                }

                tv_ttl_002.setText(hmAux_Trans.get("ttl_form"));
                tv_item_05_lbl.setText(hmAux_Trans.get("lbl_type")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
                tv_item_06_lbl.setText(hmAux_Trans.get("lbl_form")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));

                if(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA_SERV).trim().length() > 0){
                    tv_item_07_lbl.setVisibility(View.VISIBLE);
                    tv_item_07_lbl.setText(hmAux_Trans.get("lbl_data_serv")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA_SERV));

                    tv_item_08_lbl.setVisibility(View.VISIBLE);
                    tv_item_08_lbl.setText(hmAux_Trans.get("lbl_date_schedule_start")+" "+item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));
                }else{
                    tv_item_07_lbl.setVisibility(View.VISIBLE);
                    tv_item_08_lbl.setVisibility(View.VISIBLE);
                }
                break;

        }

        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)){

            case Constant.SYS_STATUS_IN_PROCESSING:
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_in_processing_states);
                llBackground.setBackground(llDrawable);
                break;
            //
            case Constant.SYS_STATUS_FINALIZED:
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_6_states);
                llBackground.setBackground(llDrawable);
                break;

            case Constant.SYS_STATUS_SENT:
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_sent_states);
                llBackground.setBackground(llDrawable);
                break;

            case Constant.SYS_STATUS_SCHEDULE:
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));
                //
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_7_states);
                llBackground.setBackground(llDrawable);
                break;

            default:
                llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
                llBackground.setBackground(llDrawable);
                break;
        }


    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("lbl_date");
        translateList.add("lbl_hour");
        translateList.add("ttl_product");
        translateList.add("lbl_product_code");
        translateList.add("lbl_product_id");
        translateList.add("lbl_product_desc");
        translateList.add("lbl_serial_id");
        translateList.add("ttl_form");
        translateList.add("lbl_type");
        translateList.add("lbl_form");
        translateList.add("CHECKLIST");
        translateList.add("lbl_data_serv");
        translateList.add("lbl_date_schedule_start");
        //Form_AP
        translateList.add("form_ttl");
        translateList.add("form_type_lbl");
        translateList.add("form_code_lbl");
        translateList.add("form_data_lbl");
        translateList.add("product_code_lbl");
        translateList.add("serial_lbl");
        translateList.add("ap_ttl");
        translateList.add("ap_code_lbl");
        translateList.add("ap_status_lbl");
        translateList.add("ap_what_lbl");
        translateList.add("ap_who_lbl");
        translateList.add("ap_when_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
