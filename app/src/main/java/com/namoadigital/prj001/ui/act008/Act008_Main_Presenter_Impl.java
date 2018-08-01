package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act008_002;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act008_Main_Presenter_Impl implements Act008_Main_Presenter {

    private Context context;
    private Act008_Main_View mView;
    private Sync_ChecklistDao syncChecklistDao;
    private MD_ProductDao mdProductDao;
    private Long product_code;
    private boolean downloadStarted = false;
    private HMAux hmAux_Trans;
    private GE_Custom_Form_OperationDao formOperationDao;
    private boolean isSchedule;
    private String requesting_process;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;

    public Act008_Main_Presenter_Impl(Context context, Act008_Main_View mView, Sync_ChecklistDao syncChecklistDao, MD_ProductDao mdProductDao, Long product_code, HMAux hmAux_Trans, GE_Custom_Form_OperationDao formOperationDao, boolean isSchedule, String requesting_process, MD_Product_SerialDao serialDao, MD_Product_Serial_TrackingDao trackingDao) {
        this.context = context;
        this.mView = mView;
        this.syncChecklistDao = syncChecklistDao;
        this.mdProductDao = mdProductDao;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.formOperationDao = formOperationDao;
        this.isSchedule = isSchedule;
        this.requesting_process = requesting_process;
        this.serialDao = serialDao;
        this.trackingDao = trackingDao;
    }

    @Override
    public void getProductInfo(Bundle bundle) {
        MD_Product md_product = null;
        //Se for um agendamento, busca dados da custom_form_local
        //se não do MD
        if (isSchedule) {
            String serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");
            //se não veio serial, seta serial_id GAMBIS
            serial_id = !serial_id.equals("") ? serial_id :Constant.KEY_NO_SERIAL;
            //
            md_product =
                    mdProductDao.getByString(
                            new Sql_Act008_002(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                    bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE),
                                    bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE),
                                    bundle.getString(Constant.ACT010_CUSTOM_FORM_VERSION),
                                    bundle.getString(Constant.ACT013_CUSTOM_FORM_DATA)
                            ).toSqlQuery()
                    );
            //
            if(ToolBox_Inf.isValidProduct(md_product)){
                MD_Product_Serial scheduledSerial = getSerialInfo(
                                                        product_code,
                                                        serial_id
                                                    );
                //
                if(scheduledSerial != null){
                    mView.setMdProductSerial(scheduledSerial);
                }else{
                    mView.setMdProductSerial(md_product.createNewSerialForThisProduct(serial_id));
                }
            }

        } else {
            md_product =
                    mdProductDao.getByString(
                            new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    product_code
                            ).toSqlQuery()
                    );

        }
        //
        if (ToolBox_Inf.isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            mView.setProductValues(null);
            // A MSG FOI MOVIDA PARA O FRAGMENTO.
//            mView.showAlertDialog(
//                    hmAux_Trans.get("alert_product_not_found_title"),
//                    hmAux_Trans.get("alert_product_not_found_msg")
//            );
        }
    }

    @Override
    public void validateSerial(String serial, int required, int allow_new) {
        serial = serial.trim();
        //Verifica se Serial foi preenchido
        if (serial.length() == 0 && required == 1) {
            //Se não foi e serial requerido, dispara alert de erro.
            //mView.fieldFocus();
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_no_serial_typed_title"),
                    hmAux_Trans.get("alert_no_serial_typed_msg")
            );
        } else {
            checkConnectionV2(serial, required, allow_new);
        }
    }

    //region Fluxo pós modificação Serial
    @Override
    public void checkFlow() {
        if (checkSyncChecklistV2()) {
            checkNextStepV2();
        } else {
            if(isSchedule) {
                checkNextStepV2();
            }else {
                if (ToolBox_Con.isOnline(context)) {
                    executeSyncProcess();
                }else {
                    mView.showAlertDialog(
                            hmAux_Trans.get("alert_no_form_found_ttl"),
                            hmAux_Trans.get("alert_no_form_found_msg")
                    );
                }
            }
        }

    }

    public void checkNextStepV2() {
        updateSyncChecklist();
        //
        defineFlow();
    }

    //endregion

    //region  NOVO_FLUXO
    private void checkConnectionV2(String serial, int required, int allow_new) {
        if (checkSyncChecklistV2()) {
            checkNextStep(serial, required, allow_new);
        } else {
            if (!isSchedule) {
                if (ToolBox_Con.isOnline(context)) {
                    executeSyncProcess();
                } else {
                    mView.continueOfflineV2(false);
                }
            } else {
                checkNextStep(serial, required, allow_new);
            }
        }
    }

    private boolean checkSyncChecklistV2() {
        List<HMAux> hmAuxList =
                syncChecklistDao.query_HM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code
                        ).toSqlQuery()
                );

        if (hmAuxList.size() > 0) {
            return true;
        }
        //
        return false;
    }

    public void checkNextStep(String serial, int required, int allow_new) {
        if (serial.length() > 0) {
            if (checkForLocalSerial(product_code, serial)) {
                //Atualiza data na tabela de produtos local
                updateSyncChecklist();
                //
                defineFlow();
            } else {
                if (ToolBox_Con.isOnline(context)) {
                    //executeSerialProcess(serial);
                } else {
                    mView.continueOfflineV2(checkForLocalSerial(product_code, serial));
                }
            }

        } else {
            if (required == 0) {
                //mView.callAct009(context);
                defineFlow();
                //Atualiza data na tabela de produtos local
                updateSyncChecklist();
            } else {
                alertSerialEmpty();
            }
        }

    }

    private boolean checkForLocalSerial(Long product_code, String serial) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        //
        MD_Product_Serial objSerial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial
                ).toSqlQuery()
        );
        //
        if (objSerial != null && objSerial.getSerial_code() > 0) {
            return true;
        }
        //
        return false;
    }
    //endregion


    @Override
    public void searchLocalSerial(long product_code, String serial_id) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        //
        MD_Product_Serial objSerial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        if (objSerial != null && objSerial.getSerial_code() > -1) {
            mView.applyReceivedSerialToFrag(objSerial);
        }else{
            mView.reApplySerialIdToFrag();
        }
    }

    @Override
    public void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
                result,
                TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        if(serial_list != null){
            if(serial_list.size() == 0){
                mView.reApplySerialIdToFrag();
            }else if(serial_list.size() == 1){
                mView.applyReceivedSerialToFrag(serial_list.get(0));
            }else{
                //FUDEU
            }
        }else{
            //FUDEU 2
        }
        //
    }

    @Override
    public void checkSyncChecklist(String serial, int required, int allow_new) {
        List<HMAux> hmAuxList =
                syncChecklistDao.query_HM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code
                        ).toSqlQuery()
                );

        if (hmAuxList.size() == 0 && !isSchedule) {
            executeSyncProcess();
        } else {
            if (serial.length() > 0) {
                // executeSerialProcess(serial);
            } else {
                if (required == 0) {
                    //mView.callAct009(context);
                    defineFlow();
                    //Atualiza data na tabela de produtos local
                    updateSyncChecklist();
                } else {
                    alertSerialEmpty();
                }
            }
        }
    }

    private void executeSyncProcess() {
        mView.setWsProcess(WS_Sync.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("alert_start_sync_title"),
                hmAux_Trans.get("alert_start_sync_msg")
        );
        //
        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
        bundle.putLong(Constant.GS_PRODUCT_CODE, product_code);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

//    @Override
//    public void executeSerialProcess(String serial) {
//        mView.setWsProcess(WS_Serial.class.getName());
//
//        mView.showPD(
//                hmAux_Trans.get("alert_start_serial_title"),
//                hmAux_Trans.get("alert_start_serial_msg")
//                );
//
//        MD_Product md_product =
//                mdProductDao.getByString(
//                        new MD_Product_Sql_001(
//                                ToolBox_Con.getPreference_Customer_Code(context),
//                                product_code
//                        ).toSqlQuery()
//                );
//
//        Intent mIntent = new Intent(context, WBR_Serial.class);
//        Bundle bundle = new Bundle();
//        bundle.putLong(Constant.GS_SERIAL_PRODUCT_CODE, product_code);
//        bundle.putInt(Constant.GS_SERIAL_REQUIRED, md_product.getRequire_serial());
//        bundle.putInt(Constant.GS_SERIAL_ALLOW_NEW, md_product.getAllow_new_serial_cl());
//        bundle.putString(Constant.GS_SERIAL_ID, serial);
//        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
//        bundle.putInt(Constant.GC_STATUS, 1);
//        //
//        mIntent.putExtras(bundle);
//        //
//        context.sendBroadcast(mIntent);
//        //
//
//    }

    @Override
    public void updateSyncChecklist() {
        //Pega data atual
        Calendar cDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist syncChecklist = new Sync_Checklist();

        syncChecklist.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        syncChecklist.setProduct_code(product_code);
        syncChecklist.setLast_update(last_update);

        syncChecklistDao.addUpdate(syncChecklist);
        //
        startDownloadServices();
    }

    @Override
    public void proceedToSerialProcess(String serial, int serial_required) {

        if (serial.length() > 0) {
            //executeSerialProcess(serial);
        } else {
            if (serial.length() == 0 && serial_required == 0) {
                //mView.callAct009(context);
                defineFlow();
                //Atualiza data na tabela de produtos local
                updateSyncChecklist();
            } else {
                alertSerialEmpty();
            }
        }
    }

    private void alertSerialEmpty() {
        //Se serial requerido, dispara alert de erro.
        //mView.fieldFocus();
        mView.showAlertDialog(
                hmAux_Trans.get("alert_no_serial_typed_title"),
                hmAux_Trans.get("alert_no_serial_typed_msg")
        );
    }

    @Override
    public void startDownloadServices() {

        if (!downloadStarted) {
            Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
            Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
            Intent mIntentLogo = new Intent(context, WBR_DownLoad_Customer_Logo.class);
            Bundle bundle = new Bundle();
            //
            bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
            //
            mIntentPDF.putExtras(bundle);
            mIntentPIC.putExtras(bundle);
            mIntentLogo.putExtras(bundle);
            //
            context.sendBroadcast(mIntentPDF);
            context.sendBroadcast(mIntentPIC);
            context.sendBroadcast(mIntentLogo);
            //Atualiza var e impede que os serviços sejam chamados 2 vezes seguidas
            downloadStarted = true;
        }
    }

    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code) {
        mView.setWsProcess(WS_Serial_Tracking_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_tracking_search_ttl"),
                hmAux_Trans.get("progress_tracking_search_msg")
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

    @Override
    public void executeSerialSearch(String product_id, String serial_id) {
        mView.setWsProcess(WS_Serial_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, "");
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSerialSave() {
        mView.setWsProcess(WS_Serial_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_serial_save_ttl"),
                hmAux_Trans.get("progress_serial_save_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult) {
        if (hmSaveResult.size() > 0) {
            ArrayList<HMAux> returnList = new ArrayList<>();
            String ttl = "";
            String msg = "";
            //
            for (Map.Entry<String, String> item : hmSaveResult.entrySet()) {
                HMAux aux = new HMAux();
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();

                MD_Product mdProduct = mdProductDao.getByString(
                        new MD_Product_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                Long.parseLong(pk[0])
                        ).toSqlQuery()
                );
                //
                if (mdProduct != null) {
                    aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mdProduct.getProduct_code() + " - " + mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
                }
                aux.put(Generic_Results_Adapter.VALUE_ITEM_2, pk[1]);
                aux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);
                returnList.add(aux);
                //
                if (product_code == Long.parseLong(pk[0])
                        && serial_id.equals(pk[1])
                        ) {

                    if (status.equals("OK")) {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_ok_msg");
                    } else {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_error_msg") + "\n" + status;

                    }
                }
            }
            //Atualiza dados dos serial na tela e spinners
            refreshMdProductSerialReference(product_code, serial_id);
            //
            //if(returnList.size() == 1){
            if (returnList.size() == 1) {
                mView.showSingleResultMsg(ttl, msg);
            } else {
                mView.showSerialResults(returnList);
            }
        } else {
            mView.showSingleResultMsg(
                    hmAux_Trans.get("alert_save_serial_return_ttl"),
                    hmAux_Trans.get("alert_no_serial_return_msg")
            );
        }
    }

    private void refreshMdProductSerialReference(long product_code, String serial_id) {
        mView.updateProductSerialValues(
                getSerialInfo(
                        product_code,
                        serial_id
                )
        );
        //
        mView.refreshUI();
    }

    public MD_Product_Serial getSerialInfo(Long product_code, String serial_id) {
        MD_Product_Serial serialObjDb = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        return serialObjDb;
    }

    @Override
    public void updateSerialData(MD_Product_Serial mdProductSerial) {
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
                        MD_Product_Serial_Tracking_Sql_002(
                        mdProductSerial.getCustomer_code(),
                        mdProductSerial.getProduct_code(),
                        mdProductSerial.getSerial_tmp()
                ).toSqlQuery()
        );
        //Salva dados alterados do S.O
        serialDao.addUpdateTmp(mdProductSerial);
    }

    @Override
    public void defineFlow() {
        if (isSchedule) {
            mView.callAct011(context);
        } else {
            mView.callAct009(context);
        }
    }

    @Override
    public void onBackPressedClicked() {
        if (isSchedule) {
            mView.callAct017(context);
        } else {
            //mView.callAct007(context);
            mView.callAct006(context);
        }

    }
}
