package com.namoadigital.prj001.view.act.test2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

public class Teste2 extends Base_Activity {

    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;

    private HMAux hmAux_Trans_frg_serial_edit;
    private String wsProcess = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste2);
        initVars();
        initActions();
    }

    private void initVars() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT031
        );
        loadTranslationFrg_Serial_Search();
    }

    private void loadTranslationFrg_Serial_Search() {
        List<String> transList = new ArrayList<String>();
        transList.add("act031_title");
        transList.add("alert_no_connection_title");
        transList.add("alert_no_connection_msg");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_start_sync_title");
        transList.add("alert_start_sync_msg");
        transList.add("alert_start_serial_title");
        transList.add("alert_start_serial_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_no_serial_typed_title");
        transList.add("alert_no_serial_typed_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("product_ttl");
        transList.add("mket_search_hint");
        transList.add("product_label");
        transList.add("product_id_label");
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("btn_create");
        //
        transList.add("serial_ttl");
        transList.add("serial_location_ttl");
        transList.add("site_lbl");
        transList.add("site_zone_lbl");
        transList.add("site_zone_local_lbl");
        transList.add("serial_add_info_ttl");
        transList.add("add_info1_lbl");
        transList.add("add_info2_lbl");
        transList.add("add_info3_lbl");
        transList.add("serial_properties_ttl");
        transList.add("brand_lbl");
        transList.add("brand_model_lbl");
        transList.add("brand_color_lbl");
        transList.add("segment_lbl");
        transList.add("category_price_lbl");
        transList.add("site_owner_lbl");
        transList.add("btn_serial_search");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_save_serial_ok_msg");
        transList.add("alert_invalid_serial_local_ttl");
        transList.add("alert_invalid_serial_local_msg");
        transList.add("alert_no_data_changes_ttl");
        transList.add("alert_no_data_changes_msg");
        transList.add("progress_serial_save_ttl");
        transList.add("progress_serial_save_msg");
        transList.add("dialog_results_ttl");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        transList.add("alert_no_serial_return_msg");
        //
        transList.add("tracking_ttl");
        transList.add("dialog_tracking_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_tracking_unavailable_ttl");
        transList.add("alert_tracking_unavailable_msg");
        transList.add("alert_tracking_already_listed_ttl");
        transList.add("alert_tracking_already_listed_msg");
        transList.add("alert_no_site_selected_ttl");
        transList.add("alert_no_site_selected_msg");
        transList.add("alert_keep_tracking_list_ttl");
        transList.add("alert_keep_tracking_list_msg");
        transList.add("alert_clear_tracking_list_ttl");
        transList.add("alert_clear_tracking_list_msg");
        transList.add("alert_save_serial_offline_msg");
        transList.add("new_serial_data_lost_ttl");
        transList.add("new_serial_data_lost_msg");
        transList.add("serial_data_lost_ttl");
        transList.add("serial_data_lost_msg");
        //
        transList.add("alert_offline_data_not_saved_ttl");
        transList.add("alert_offline_data_not_saved_msg");
        //
        //NOVAS TRADUÇÕES ADD NO RECURSO \/
        transList.add("btn_check_exists");
        transList.add("alert_serial_exists_ttl");
        transList.add("alert_serial_exists_msg");
        transList.add("alert_serial_not_exists_ttl");
        transList.add("alert_serial_not_exists_msg");
        transList.add("alert_serial_not_exists_msg");
        transList.add("dialog_serial_inbound_lbl");
        transList.add("dialog_serial_inbound_date_lbl");
        transList.add("dialog_serial_move_lbl");
        transList.add("dialog_serial_move_group_lbl");
        transList.add("dialog_serial_outbound_lbl");
        transList.add("alert_serial_validation_ttl");
        transList.add("alert_invalid_site_change_msg");
        //
        hmAux_Trans_frg_serial_edit = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        long product_code = 53;
        String serial_di = "s3";
        MD_ProductDao productDao = new MD_ProductDao(context);

        MD_Product mdProduct = productDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            product_code
                    ) .toSqlQuery()
        );
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);

        MD_Product_Serial mdProductSerial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_di
                ) .toSqlQuery()
        );
        //
        frgSerialEdit = (Frg_Serial_Edit) fm.findFragmentById(R.id.act023_frg_serial_edit);
        frgSerialEdit.setMdProduct(mdProduct);
        frgSerialEdit.setMdProductSerial(mdProductSerial);
        frgSerialEdit.setmModule_Code(mModule_Code);
        frgSerialEdit.setmResource_Code(mResource_Code);
        frgSerialEdit.setHmAux_Trans(hmAux_Trans_frg_serial_edit);
        frgSerialEdit.setNew_serial(true);
        controls_sta.addAll(frgSerialEdit.getControlsSta());
        frgSerialEdit.setBtnActionLabel("TEste");
        frgSerialEdit.setViewMode(Frg_Serial_Edit.VIEW_FULL_EDIT);
        frgSerialEdit.setShowCategorySegmentoInfo(true);
        frgSerialEdit.setDelegate(new Frg_Serial_Edit.I_Frg_Serial_Edit() {

            @Override
            public void onCheckButtonClick(long product_code, String product_id, String serial_id, String tracking) {
                executeSerialSearch(
                        product_id,
                        serial_id,
                        tracking
                );
            }

            @Override
            public void onSaveNoChangesClick(MD_Product_Serial md_product_serial, boolean serial_id_changes) {
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_no_data_changes_ttl"),
                        hmAux_Trans.get("alert_no_data_changes_msg"),
                        null,
                        0

                );
            }

            @Override
            public void onSaveWithChangesClick(MD_Product_Serial mdProductSerial, boolean serial_id_changes) {
                saveSerialInDb(mdProductSerial);
                //
                if(ToolBox_Con.isOnline(context)) {
                    executeSerialSave();
                }else{
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }

            @Override
            public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
                executeTrackingSearch(product_code,serial_code,tracking,site_code);
            }


            @Override
            public void onProductOrSerialNull() {
               onBackPressed();
            }

            @Override
            public void onFragIsReady() {

            }

            @Override
            public void abortFragLoad() {

            }
        });

    }

    private void saveSerialInDb(MD_Product_Serial mdProductSerial ) {
        MD_Product_Serial_TrackingDao trackingDao = new MD_Product_Serial_TrackingDao(context);
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
                MD_Product_Serial_Tracking_Sql_002(
                        mdProductSerial.getCustomer_code(),
                        mdProductSerial.getProduct_code(),
                        mdProductSerial.getSerial_tmp()
                ).toSqlQuery()
        );
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        //Salva dados alterados do S.O
        serialDao.addUpdateTmp(mdProductSerial);
        //
    }

    public void executeSerialSave() {
        wsProcess = WS_Serial_Save.class.getName();
        //
        showPD(
                "Title - Trad",//hmAux_Trans.get("progress_serial_save_ttl"),
                "Msg - Trad"//hmAux_Trans.get("progress_serial_save_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
//        bundle.putLong(Constant.WS_SO_SEARCH_PRODUCT_CODE,product_code);
//        bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID,serial_id);
//        bundle.putBoolean(Constant.WS_SO_SEARCH_SAVE_SERIAL,save_serial);
//        bundle.putBoolean(Constant.WS_SO_SEARCH_CREATE_SERIAL,true);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);

    }

    private void initActions() {

    }
    private void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    private void executeSerialSearch( String product_id,String serial_id, String tracking){
        if (ToolBox_Con.isOnline(context)) {
            wsProcess = WS_Serial_Search.class.getName();
            showPD(
                   "Search - Trad",// hmAux_Trans.get("dialog_serial_search_ttl"),
                    "Search ms - Trad"//hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, "");
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_search"), "", "0");
        } else {
            /*ArrayList<MD_Product_Serial> serial_list = hasLocalSerial(product_id, serial_id, tracking);
            //
            if (serial_list.size() > 0) {
                defineSearchResultFlow(serial_list);
            } else {
                if (mdProduct == null || mdProduct.getAllow_new_serial_cl() == 0) {
                    // mudar mensagem
                    ToolBox_Inf.showNoConnectionDialog(context);
                } else {
                    defineSearchResultFlow(serial_list);
                }
            }*/
            ToolBox_Inf.showNoConnectionDialog(context);
        }

    }

    private void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        wsProcess = WS_Serial_Tracking_Search.class.getName();
        //
        showPD(
                "Title -trad",//hmAux_Trans.get("progress_tracking_search_ttl"),
                "Msg - Trad"//hmAux_Trans.get("progress_tracking_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Tracking_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SERIAL_CODE, String.valueOf(serial_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_TRACKING, tracking);
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SITE_CODE, site_code);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
                result,
                TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        if(serial_list != null){
            if(serial_list.size() == 0){
                frgSerialEdit.reApplySerialId();
            }else if(serial_list.size() == 1){
                frgSerialEdit.applyReceivedSerial(serial_list.get(0));
            }else{
                //FUDEU
            }
        }else{
            //FUDEU 2
        }
        //
        //defineSearchResultFlow(serial_list);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(wsProcess.equals(WS_Serial_Tracking_Search.class.getName())){
            frgSerialEdit.processTrackingResult(hmAux);
            disableProgressDialog();
        }else if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            //frgSerialEdit.processTrackingResult(hmAux);
            disableProgressDialog();
        }else if(wsProcess.equalsIgnoreCase(WS_Serial_Save.class.getName())){
            frgSerialEdit.setNew_serial(false);
            frgSerialEdit.refreshUi();
            disableProgressDialog();
        }
    }

    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        //
        if(wsProcess.equals(WS_Serial_Tracking_Search.class.getName())){
           // frgSerialEdit.processTrackingResult(hmAux);
            disableProgressDialog();
        }else if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            extractSearchResult(result);
            //
            disableProgressDialog();
        }
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        if(wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())){
            //frgSerialEdit.processTrackingResult(hmAux);
            disableProgressDialog();
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        progressDialog.dismiss();
    }
    //Tratativa SESSION NOT FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        callAct003(context);
    }

    public void callAct003(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }
}
