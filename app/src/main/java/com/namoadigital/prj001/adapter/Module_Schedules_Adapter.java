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
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 11/04/2017.
 *
 * Modificado by DANIEL.LUCHE on 16/08/2018.
 *
 * Adicionado variavel do comentario do agendamento e interface para o clique.
 *
 */

public class Module_Schedules_Adapter extends BaseAdapter {

    private Context context;
    private int resource_01;
    private int resource_02;
    private int resource_03;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private OnIvCommentClickListner onIvCommentClickListner;

    public interface OnIvCommentClickListner{
        void OnIvCommentClick(HMAux item);
    }

    public void setOnIvCommentClickListner(OnIvCommentClickListner onIvCommentClickListner) {
        this.onIvCommentClickListner = onIvCommentClickListner;
    }

    private long site_id_preference = -1L;

    public long getSite_id_preference() {
        return site_id_preference;
    }

    public void setSite_id_preference(String site_id_preference) {
        try {
            this.site_id_preference = Long.parseLong(site_id_preference);
        } catch (Exception e) {
            this.site_id_preference = -1L;
        }
        //
        notifyDataSetChanged();
    }

    public Module_Schedules_Adapter(Context context,List<HMAux> source, int resource_01, int resource_02, int resource_03) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.resource_03 = resource_03;
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
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        HMAux item = source.get(position);
        //
        switch (item.get(Act017_Main.ACT017_MODULE_KEY)) {
            case Constant.MODULE_CHECKLIST:
                return 0;
            case Constant.MODULE_FORM_AP:
                return 1;
            case Act017_Main.MODULE_SCHEDULE_DATE_REF:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Resgata HmAux com as informações
        HMAux item = source.get(position);
        //
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;
                case 2:
                    convertView = mInflater.inflate(resource_03, parent, false);
                    break;
            }
        }
        //
        switch (getItemViewType(position)) {
            case 0:
                processFormItem(item, convertView);
                break;
            case 1:
                processFormAPItem(item, convertView);
                break;
            case 2:
                processDateItem(item, convertView);
                break;
        }
        //
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        //return super.isEnabled(position);
        boolean isEnabled = false;
        switch (getItemViewType(position)){
            case 0:
            case 1:
                isEnabled = true;
                break;
            case 2:
            default:
                isEnabled =  false;
                break;
        }
        //
        return isEnabled;
    }

    private void processDateItem(HMAux item, View convertView) {
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.module_schedules_date_cell_iv_calendar);
        TextView tv_date = (TextView) convertView.findViewById(R.id.module_schedules_date_cell_tv_date);
        //
        iv_icon.setImageDrawable(context.getDrawable(R.drawable.ic_calendario));
        //
        tv_date.setText(item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
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
        tv_module_ttl.setText(hmAux_Trans.get("lbl_form_ap"));
        tv_module_ttl.setVisibility(View.VISIBLE);
        //
        ll_action_btn.setVisibility(View.GONE);
        //
        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
        if (item.get(GE_Custom_Form_ApDao.SYNC_REQUIRED).equals("1")) {
            iv_sync_upload.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down_thick_black_24dp));
            iv_sync_upload.setVisibility(View.VISIBLE);
        } else if (item.get(GE_Custom_Form_ApDao.UPLOAD_REQUIRED).equals("1")) {
            iv_sync_upload.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_up_thick_black_24dp));
            iv_sync_upload.setVisibility(View.VISIBLE);
        } else {
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
        tv_ap_when.setText(hmAux_Trans.get("ap_when_lbl"));
        if (!item.get(GE_Custom_Form_ApDao.AP_WHEN).isEmpty()) {
            tv_ap_when_val.setText(
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN)),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
            );
        } else {
            tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHEN));
        }

    }

    private void processFormItem(final HMAux item, View convertView) {
        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.module_schedules_cell_ll_bg);
        //
        ImageView iv_form_info = convertView.findViewById(R.id.module_schedules_cell_iv_form_info);
        TextView tv_item_product = convertView.findViewById(R.id.module_schedules_cell_tv_item_product);
        TextView tv_item_serial_id = convertView.findViewById(R.id.module_schedules_cell_tv_item_serial_id);
        TextView tv_item_form_desc = convertView.findViewById(R.id.module_schedules_cell_tv_item_form_desc);
        TextView tv_item_seq_exec = convertView.findViewById(R.id.module_schedules_cell_tv_item_seq_exec);
        TextView tv_item_site = convertView.findViewById(R.id.module_schedules_cell_tv_item_site);
        TextView tv_item_date = convertView.findViewById(R.id.module_schedules_cell_tv_item_date);
        TextView tv_item_comment = convertView.findViewById(R.id.module_schedules_cell_tv_item_comment);
        TextView tv_ttl = convertView.findViewById(R.id.module_schedules_cell_tv_ttl);
        TextView tv_status_val = convertView.findViewById(R.id.module_schedules_cell_tv_status_val);
        // Aguardando definicao
        if (item.get("site_code") != null && !item.get("site_code").equalsIgnoreCase("null") && Long.parseLong(item.get("site_code")) == site_id_preference) {
            llBackground.setBackground(context.getDrawable(R.drawable.namoa_cell_8_states));
        } else {
            llBackground.setBackground(context.getDrawable(R.drawable.act013_cell_in_processing_states));
        }
        //
        Drawable llDrawable = null;

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)) {

            case Constant.MODULE_CHECKLIST:
                //16/08/2018 - Add icone de comentario quando houver
                if(!item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE).isEmpty()){
                    iv_form_info.setVisibility(View.VISIBLE);
                }else{
                    iv_form_info.setVisibility(View.GONE);
                }
                //
                iv_form_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onIvCommentClickListner != null){
                            onIvCommentClickListner.OnIvCommentClick(item);
                        }
                    }
                });
                //
                if (item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0) {
                    tv_item_serial_id.setVisibility(View.GONE);
                }

                tv_ttl.setText(hmAux_Trans.get("ttl_form"));
                tv_item_form_desc.setText(item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
                if(item.hasConsistentValue(MD_Schedule_ExecDao.COMMENTS)
                && !item.get(MD_Schedule_ExecDao.COMMENTS).isEmpty()) {
                    tv_item_comment.setVisibility(View.VISIBLE);
                    tv_item_comment.setText(item.get(MD_Schedule_ExecDao.COMMENTS));
                }else{
                    tv_item_comment.setVisibility(View.GONE);
                }
                tv_item_product.setText(item.get(MD_Schedule_ExecDao.PRODUCT_ID) + " - " + item.get(MD_Schedule_ExecDao.PRODUCT_DESC) );
                tv_item_serial_id.setText(item.get(MD_Schedule_ExecDao.SERIAL_ID));
                if (item.get("schedule_pk").trim().length() > 0) {
                    tv_item_seq_exec.setVisibility(View.VISIBLE);
                    tv_item_seq_exec.setText(item.get("schedule_pk"));
                    if(item.hasConsistentValue(MD_Schedule_ExecDao.SITE_ID)
                    && item.get(MD_Schedule_ExecDao.SITE_CODE).equals(String.valueOf(site_id_preference))){
                        tv_item_site.setVisibility(View.GONE);
                    }else {
                        tv_item_site.setVisibility(View.VISIBLE);
                        tv_item_site.setText(item.get(MD_Schedule_ExecDao.SITE_ID) + " - " + item.get(MD_Schedule_ExecDao.SITE_DESC));
                    }
                    //
                    tv_item_date.setVisibility(View.VISIBLE);

                    setIntervalScheduled(item, tv_item_date);
                } else {
                    tv_item_seq_exec.setVisibility(View.VISIBLE);
                    tv_item_site.setVisibility(View.VISIBLE);
                    tv_item_date.setVisibility(View.VISIBLE);
                }

                break;
        }

        switch (item.get(MD_Schedule_ExecDao.STATUS)) {

            case Constant.SYS_STATUS_IN_PROCESSING:
                tv_item_date.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                                //item.get(GE_Custom_Form_DataDao.DATE_START)
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )


                );
//                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_in_processing_states);
//                llBackground.setBackground(llDrawable);
                //
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_PROCESS))
                );
                break;
            //
            case Constant.SYS_STATUS_FINALIZED:
//                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_6_states);
//                llBackground.setBackground(llDrawable);
                //
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_DONE));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_DONE))
                );
                break;

            case Constant.SYS_STATUS_SENT:
//                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_sent_states);
//                llBackground.setBackground(llDrawable);
                //
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_SENT));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_SENT))
                );
                break;

            case Constant.SYS_STATUS_SCHEDULE:
                setIntervalScheduled(item, tv_item_date);

//                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_7_states);
//                llBackground.setBackground(llDrawable);

                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_SCHEDULE));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_SCHEDULE))
                );
                break;

            default:
                llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
                llBackground.setBackground(llDrawable);
                break;
        }
    }

    private void setIntervalScheduled(HMAux item, TextView tv_item_date) {
        String dateStart = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT) ? item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT) : "", ""),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );

        String dateEnd = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT) ? item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT) : "", ""),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );

        tv_item_date.setText(ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd));
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
        translateList.add("lbl_site_id");
        translateList.add("lbl_date_schedule_start");
        translateList.add("lbl_date_schedule_end");
        translateList.add("lbl_status");
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
        //
        List<String> transeList005 = new ArrayList<>();
        transeList005.add("lbl_form_ap");
        //
        hmAux_Trans.putAll(ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                ToolBox_Inf.getResourceCode(
                        context,
                        Constant.APP_MODULE,
                        "act005"
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transeList005
                )
        );
    }
}
