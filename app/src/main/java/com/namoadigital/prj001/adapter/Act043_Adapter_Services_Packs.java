package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.namoa_digital.namoa_library.util.HMAux;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act043_Adapter_Services_Packs extends BaseAdapter {

    private Context context;
    //
    private int resource_01;
    private int resource_02;
    private int resource_03;
    private int resource_04;

    private ArrayList<HMAux> data;

    private HMAux hmAux_Trans;
    private String mResource_Code;

    public Act043_Adapter_Services_Packs(Context context, int resource_01, int resource_02, int resource_03, int resource_04, ArrayList<HMAux> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.resource_03 = resource_03;
        this.resource_04 = resource_04;

        this.data = data;
        this.hmAux_Trans = hmAux_Trans;

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();//
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 11;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            HMAux item = data.get(position);

            if (item.get("tmp") == null) {
                if (item.get("type").equalsIgnoreCase("DATE")) {
                    return 4;
                } else if (item.get("type").equalsIgnoreCase("END")) {
                    return 5;
                } else if (item.get("type").equalsIgnoreCase("NO_READ")) {
                    return 9;
                } else {
                }
            }


        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        boolean results = false;

        switch (getItemViewType(position)) {

            // Date
            case 4:
                results = false;
                break;

            // No More
            case 5:
                results = false;
                break;

            // Translate
            case 6:
                results = false;
                break;

            // FORM_AP
            /*case 7:
                results = false;
                break;*/

            // SO
            case 8:
                results = false;
                break;

            // NO READ
            case 9:
                results = false;
                break;

            // 0 - Other IMG / 1 - Me IMG / 2 - Other TXT / 3 - Me TXT / 7 - FORM_AP
            default:
                results = true;
                break;
        }

        return results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {

                // Other IMG
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;

                // Me IMG
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;

                // Other TXT
                case 2:
                    convertView = mInflater.inflate(resource_03, parent, false);
                    break;

                // Me TXT
                case 3:
                    convertView = mInflater.inflate(resource_04, parent, false);
                    break;

            }
        }

        JSONObject message = null;
        HMAux hmAux = data.get(position);

        if (hmAux.get("tmp") != null) {

            try {
                JSONObject msg_obj = new JSONObject(hmAux.get("msg_obj"));
                message = msg_obj.getJSONObject("message");
            } catch (JSONException e) {
                message = new JSONObject();
            }

        }

        switch (getItemViewType(position)) {
//            // tipo 1
//            case 0:
//                processImageOther(message, hmAux, convertView);
//                break;
//
//            // tipo 2
//            case 1:
//                processImageMe(message, hmAux, convertView);
//                break;
//
//            // tipo 3
//            case 2:
//                processTxTOther(message, hmAux, convertView);
//                break;
//
//            // tipo 4
//            case 3:
//                processTxTMe(message, hmAux, convertView);
//                break;

        }
        return convertView;
    }

//    private void processImageOther(JSONObject message, HMAux hmAux, View convertView) {
//        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
//        ImageView iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
//        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
//
//        tv_name.setText(hmAux.get("user_nick"));
//
//        if (hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL) == null || hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).isEmpty()) {
//            iv_foto.setImageResource(R.drawable.sand_watch_transp);
//        } else {
//            iv_foto.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
//                    hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).replace(".jpg", "") + "_thumb.jpg"
//            ));
//        }
//
//        tv_hour.setText(
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                        " HH:mm"
//                )
//        );
//
//        if (hmAuxColors.get(hmAux.get("user_code")) == null) {
//            hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
//        }
//
//        tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
//    }
//
//    private void processImageMe(JSONObject message, HMAux hmAux, View convertView) {
//        ImageView iv_foto = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
//        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
//        ImageView iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);
//
//        if (hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL) == null || hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).isEmpty()) {
//            iv_foto.setImageResource(R.drawable.sand_watch_transp);
//        } else {
//            iv_foto.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
//                    hmAux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).replace(".jpg", "") + "_thumb.jpg"
//            ));
//        }
//
//        tv_hour.setText(
//
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                        " HH:mm"
//
//                )
//        );
//
//        iv_badge.setImageDrawable(processBadgeImage(hmAux));
//    }
//
//    private void processTxTOther(JSONObject message, HMAux hmAux, View convertView) {
//        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
//        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
//        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
//        //
//        try {
//
//            if (hmAuxColors.get(hmAux.get("user_code")) == null) {
//                hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
//            }
//
//            tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
//
//
//            tv_message.setText(message.getString("data").trim());
//
//        } catch (JSONException e) {
//            tv_message.setText("Error data");
//        }
//
//        tv_name.setText(hmAux.get("user_nick"));
//
//        tv_hour.setText(
//
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                        " HH:mm"
//
//                )
//        );
//    }
//
//    private void processTxTMe(JSONObject message, HMAux hmAux, View convertView) {
//        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
//        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
//        ImageView iv_badge = (ImageView) convertView.findViewById(R.id.act035_main_content_cell_iv_badge);
//
//        try {
//            tv_message.setText(message.getString("data").trim());
//        } catch (JSONException e) {
//            tv_message.setText("Error data");
//        }
//
//        tv_hour.setText(
//
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                        " HH:mm"
//
//                )
//        );
//
//        iv_badge.setImageDrawable(processBadge(hmAux));
//    }
//
//    private void processDate(JSONObject message, HMAux hmAux, View convertView) {
//        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
//        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
//
//        String resultado = "";
//
//        if (ToolBox_Inf.isToday_Yesterday(
//                ToolBox_Inf.dateToMillisecondsChat(hmAux.get("msg_date_zone"), ""),
//                true
//        )) {
//
//            resultado = hmAux_Trans.get("TODAY");
//
//        }
//        //
//        if (ToolBox_Inf.isToday_Yesterday(
//                ToolBox_Inf.dateToMillisecondsChat(hmAux.get("msg_date_zone"), ""),
//                false
//        )) {
//
//            resultado = hmAux_Trans.get("YESTERDAY");
//
//        }
//        //
//        if (resultado.equalsIgnoreCase("")) {
//            tv_message.setText(
//                    ToolBox_Inf.millisecondsToString(
//                            ToolBox_Inf.dateToMillisecondsChat(hmAux.get("msg_date_zone"), ""),
////                            ""
//                            ToolBox_Inf.nlsDateFormat(context)
//                    )
//            );
//
//        } else {
//            tv_message.setText(resultado);
//        }
//    }
//
//    private void processNoMore(View convertView) {
//        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
//        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
//        tv_message.setText(hmAux_Trans.get("NO_MORE_MESSAGES"));
//
//    }
//
//    private void processTranslate(JSONObject message, HMAux hmAux, View convertView) {
//        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
//        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
//        String resultTranslate = "";
//
//        try {
//
//            String msgParts[] = message.getString("data").replace("|", "#").split("#");
//
//            msgParts = message.getString("data").replace("|", "#").split("#");
//            resultTranslate = hmAux_Trans.get(msgParts[0]) + (msgParts.length > 1 ? msgParts[1] : "");
//            //
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        tv_message.setText(resultTranslate);
//    }
//
//    private void processForm_APOther(JSONObject message, HMAux hmAux, View convertView) {
//        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
//        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_ttl);
//        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_hour_ttl);
//        TextView tv_type = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_label);
//        TextView tv_form_label = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_label);
//        TextView tv_data_serv = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_lbl);
//        TextView tv_product = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_lbl);
//        TextView tv_serial = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_lbl);
//        TextView tv_ap_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_ttl);
//        TextView tv_ap_code = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_lbl);
//        TextView tv_ap_status = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_lbl);
//        TextView tv_ap_what = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_lbl);
//        TextView tv_ap_who = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_lbl);
//        TextView tv_ap_when = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_lbl);
//        //
//        TextView tv_type_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_val);
//        TextView tv_form_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_val);
//        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_val);
//        TextView tv_product_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_val);
//        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_val);
//        TextView tv_ap_code_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_val);
//        TextView tv_ap_status_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_val);
//        TextView tv_ap_what_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_val);
//        TextView tv_ap_when_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_val);
//        TextView tv_ap_who_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_val);
//        LinearLayout ll_action_btn = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_ll_action_btn);
//        //
//        Button iv_download_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_download_ap);
//        Button iv_join_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_join_ap);
//        //
//        ll_action_btn.setVisibility(View.VISIBLE);
//        //
//        HashMap<String, String> item = new HashMap<>();
//
//        try {
//            item = ToolBox_Inf.JsonToHashMap(
//                    message.getJSONObject("data"),
//                    "form_ap"
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //
//        if (hmAuxColors.get(hmAux.get("user_code")) == null) {
//            hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
//        }
//        tv_name.setText(hmAux.get("user_nick"));
//        tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
//        //
//        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
//        //
//        tv_hour.setText(
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                        " HH:mm"
//                )
//        );
//        //
//        tv_type.setText(hmAux_Trans.get("form_type_lbl"));
//        tv_type_val.setText(
//                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE) + " - " +
//                        item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC)
//        );
//        //
//        tv_form_label.setText(hmAux_Trans.get("form_code_lbl"));
//        tv_form_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE) + " - " +
//                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION) + " - " +
//                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC)
//        );
//        //
//        tv_data_serv.setText(hmAux_Trans.get("form_data_lbl"));
//        tv_data_serv_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
//        //
//        tv_product.setText(hmAux_Trans.get("product_code_lbl"));
//        tv_product_val.setText(
//                item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_ID) + " - " +
//                        item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_DESC)
//        );
//        //
//        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
//        tv_serial_val.setText(item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) != "null" ? item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) : "");
//        //
//        tv_ap_ttl.setText(hmAux_Trans.get("ap_ttl"));
//        //
//        tv_ap_code.setText(hmAux_Trans.get("ap_code_lbl"));
//        tv_ap_code_val.setText(
//                item.get(GE_Custom_Form_ApDao.AP_CODE) + " - " +
//                        item.get(GE_Custom_Form_ApDao.AP_DESCRIPTION)
//        );
//        tv_ap_status.setText(hmAux_Trans.get("ap_status_lbl"));
//        tv_ap_status_val.setText(
//                hmAux_Trans.get(item.get(
//                        GE_Custom_Form_ApDao.AP_STATUS
//                        )
//                )
//        );
//        ToolBox_Inf.setAPStatusColor(
//                context,
//                tv_ap_status_val,
//                item.get(GE_Custom_Form_ApDao.AP_STATUS)
//        );
//        tv_ap_what.setText(hmAux_Trans.get("ap_what_lbl"));
//
//        if (item.get(GE_Custom_Form_ApDao.AP_WHAT) != "null" && !item.get(GE_Custom_Form_ApDao.AP_WHAT).isEmpty()) {
//            tv_ap_what_val.setText(
//                    ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(item.get(GE_Custom_Form_ApDao.AP_WHAT)), 45)
//            );
//        } else {
//            tv_ap_what_val.setText("");
//        }
//        tv_ap_who.setText(hmAux_Trans.get("ap_who_lbl"));
//        tv_ap_who_val.setText(
//                item.get("ap_who_name") != "null" ? item.get("ap_who_name") : ""
//        );
//        tv_ap_when.setText(hmAux_Trans.get("ap_when_lbl"));
//        tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) != null ? item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) : "");
//
//        if (item.get(GE_Custom_Form_ApDao.AP_WHEN) != null &&
//                !item.get(GE_Custom_Form_ApDao.AP_WHEN).equalsIgnoreCase("null") &&
//                !item.get(GE_Custom_Form_ApDao.AP_WHEN).isEmpty()) {
//            tv_ap_when_val.setText(
//                    ToolBox_Inf.millisecondsToString(
//                            ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN), ""),
//                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
//                    )
//            );
//        } else {
//            tv_ap_when_val.setText("");
//            //tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHEN));
//        }
//
//        iv_download_ap.setTag(item);
//        iv_download_ap.setText(hmAux_Trans.get("lbl_checklist"));
//        iv_download_ap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();
//
//                if (delegate != null) {
//                    delegate.download_AP(itemAux.get("pk"), itemAux.get("custom_form_url"));
//                }
//            }
//        });
//
//        iv_join_ap.setTag(item);
//        iv_join_ap.setText(hmAux_Trans.get("lbl_form_ap"));
//        iv_join_ap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();
//
//                if (delegate != null) {
//                    delegate.join_AP(itemAux.get("pk"));
//                }
//            }
//        });
//        //Seta visibilidade do botão de pendend do perfil do user
//        iv_join_ap.setVisibility(profile_ap ? View.VISIBLE: View.INVISIBLE);
//    }
//
//    private void processForm_APMe(JSONObject message, HMAux hmAux, View convertView) {
//        TextView tv_name = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
//        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_ttl);
//        TextView tv_hour = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_hour_ttl);
//        TextView tv_type = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_label);
//        TextView tv_form_label = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_label);
//        TextView tv_data_serv = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_lbl);
//        TextView tv_product = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_lbl);
//        TextView tv_serial = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_lbl);
//        TextView tv_ap_ttl = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_ttl);
//        TextView tv_ap_code = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_lbl);
//        TextView tv_ap_status = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_lbl);
//        TextView tv_ap_what = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_lbl);
//        TextView tv_ap_who = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_lbl);
//        TextView tv_ap_when = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_lbl);
//        //
//        TextView tv_type_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_type_val);
//        TextView tv_form_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_form_val);
//        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_data_serv_val);
//        TextView tv_product_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_product_val);
//        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_serial_val);
//        TextView tv_ap_code_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_code_val);
//        TextView tv_ap_status_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_status_val);
//        TextView tv_ap_what_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_what_val);
//        TextView tv_ap_when_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_when_val);
//        TextView tv_ap_who_val = (TextView) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_tv_ap_who_val);
//        LinearLayout ll_action_btn = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_ll_action_btn);
//        //
//        Button iv_download_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_download_ap);
//        Button iv_join_ap = (Button) convertView.findViewById(R.id.act035_main_content_cell_namoa_ap_iv_join_ap);
//        //
//        ll_action_btn.setVisibility(View.VISIBLE);
//        //
//        HashMap<String, String> item = new HashMap<>();
//
//        try {
//            item = ToolBox_Inf.JsonToHashMap(
//                    message.getJSONObject("data"),
//                    "form_ap"
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //
//        if (hmAuxColors.get(hmAux.get("user_code")) == null) {
//            hmAuxColors.put(hmAux.get("user_code"), String.valueOf(ToolBox_Inf.userColor()));
//        }
//
//        tv_name.setText(hmAux.get("user_nick"));
//        tv_name.setTextColor(Integer.parseInt(hmAuxColors.get(hmAux.get("user_code"))));
//        tv_name.setVisibility(View.GONE);
//        //
//        tv_form_ttl.setText(hmAux_Trans.get("form_ttl"));
//        //
//        tv_hour.setText(
//                ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(hmAux.get("msg_date"), ""),
//                        " HH:mm"
//                )
//        );
//        //
//        tv_type.setText(hmAux_Trans.get("form_type_lbl"));
//        tv_type_val.setText(
//                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE) + " - " +
//                        item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE_DESC)
//        );
//        //
//        tv_form_label.setText(hmAux_Trans.get("form_code_lbl"));
//        tv_form_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE) + " - " +
//                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION) + " - " +
//                item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DESC)
//        );
//        //
//        tv_data_serv.setText(hmAux_Trans.get("form_data_lbl"));
//        tv_data_serv_val.setText(item.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
//        //
//        tv_product.setText(hmAux_Trans.get("product_code_lbl"));
//        tv_product_val.setText(
//                item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_ID) + " - " +
//                        item.get("ap_" + GE_Custom_Form_ApDao.PRODUCT_DESC)
//        );
//        //
//        tv_serial.setText(hmAux_Trans.get("serial_lbl"));
//        tv_serial_val.setText(item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) != "null" ? item.get("ap_" + GE_Custom_Form_ApDao.SERIAL_ID) : "");
//        //
//        tv_ap_ttl.setText(hmAux_Trans.get("ap_ttl"));
//        //
//        tv_ap_code.setText(hmAux_Trans.get("ap_code_lbl"));
//        tv_ap_code_val.setText(
//                item.get(GE_Custom_Form_ApDao.AP_CODE) + " - " +
//                        item.get(GE_Custom_Form_ApDao.AP_DESCRIPTION)
//        );
//        tv_ap_status.setText(hmAux_Trans.get("ap_status_lbl"));
//        tv_ap_status_val.setText(
//                hmAux_Trans.get(item.get(
//                        GE_Custom_Form_ApDao.AP_STATUS
//                        )
//                )
//        );
//        ToolBox_Inf.setAPStatusColor(
//                context,
//                tv_ap_status_val,
//                item.get(GE_Custom_Form_ApDao.AP_STATUS)
//        );
//        tv_ap_what.setText(hmAux_Trans.get("ap_what_lbl"));
//
//        if (item.get(GE_Custom_Form_ApDao.AP_WHAT) != "null" && !item.get(GE_Custom_Form_ApDao.AP_WHAT).isEmpty()) {
//            tv_ap_what_val.setText(
//                    ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(item.get(GE_Custom_Form_ApDao.AP_WHAT)), 45)
//            );
//        } else {
//            tv_ap_what_val.setText("");
//        }
//        tv_ap_who.setText(hmAux_Trans.get("ap_who_lbl"));
//        tv_ap_who_val.setText(
//                item.get("ap_who_name") != "null" ? item.get("ap_who_name") : ""
//        );
//        tv_ap_when.setText(hmAux_Trans.get("ap_when_lbl"));
//        tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) != null ? item.get(GE_Custom_Form_ApDao.AP_WHO_NICK) : "");
//
//        if (item.get(GE_Custom_Form_ApDao.AP_WHEN) != null &&
//                !item.get(GE_Custom_Form_ApDao.AP_WHEN).equalsIgnoreCase("null") &&
//                !item.get(GE_Custom_Form_ApDao.AP_WHEN).isEmpty()) {
//            tv_ap_when_val.setText(
//                    ToolBox_Inf.millisecondsToString(
//                            ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_ApDao.AP_WHEN), ""),
//                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
//                    )
//            );
//        } else {
//            tv_ap_when_val.setText("");
//            //tv_ap_when_val.setText(item.get(GE_Custom_Form_ApDao.AP_WHEN));
//        }
//
//        iv_download_ap.setTag(item);
//        iv_download_ap.setText(hmAux_Trans.get("lbl_checklist"));
//        iv_download_ap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();
//
//                if (delegate != null) {
//                    delegate.download_AP(itemAux.get("pk"), itemAux.get("custom_form_url"));
//                }
//            }
//        });
//
//        iv_join_ap.setTag(item);
//        iv_join_ap.setText(hmAux_Trans.get("lbl_form_ap"));
//        iv_join_ap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String, String> itemAux = (HashMap<String, String>) v.getTag();
//
//                if (delegate != null) {
//                    delegate.join_AP(itemAux.get("pk"));
//                }
//            }
//        });
//        //Seta visibilidade do botão de pendend do perfil do user
//        iv_join_ap.setVisibility(profile_ap ? View.VISIBLE: View.INVISIBLE);
//
//    }
//
//    private void processSo(JSONObject message, HMAux hmAux, View convertView) {
//
//    }
//
//    private void processNoRead(HMAux hmAux, View convertView) {
//        LinearLayout ll_item = (LinearLayout) convertView.findViewById(R.id.act035_main_content_cell_whats_ll_item);
//        TextView tv_message = (TextView) convertView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
//        tv_message.setText("(" + hmAux.get("count") + ") " + "mensagens nao lidas ");//hmAux_Trans.get("NO_READ_MESSAGES"));
//
//    }
//
//    private Drawable processBadge(HMAux hmAux) {
//        // Badge Status for All
//        if (hmAux.get(CH_MessageDao.ALL_READ).equalsIgnoreCase("1")) {
//            return context.getResources().getDrawable(R.drawable.ic_done_all_green_24dp);
//        } else if (hmAux.get(CH_MessageDao.ALL_DELIVERED).equalsIgnoreCase("1")) {
//            return context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
//        } else if (!hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")) {
//            return context.getResources().getDrawable(R.drawable.ic_done_black_24dp);
//        } else {
//            return context.getResources().getDrawable(R.drawable.ic_clock_chat);
//        }
//    }
//
//    private Drawable processBadgeImage(HMAux hmAux) {
//        // Badge Status for All
//        if (hmAux.get(CH_MessageDao.ALL_READ).equalsIgnoreCase("1")) {
//            return context.getResources().getDrawable(R.drawable.ic_done_all_green_24dp);
//        } else if (hmAux.get(CH_MessageDao.ALL_DELIVERED).equalsIgnoreCase("1")) {
//            return context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
//        } else if (!hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")
//                && hmAux.get(CH_MessageDao.FILE_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SENT)) {
//            return context.getResources().getDrawable(R.drawable.ic_done_black_24dp);
//        } else {
//            return context.getResources().getDrawable(R.drawable.ic_clock_chat);
//        }
//    }

}