
package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Search_Not_Focus;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Search_Not_Focus;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act008_003;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
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
    private GE_Custom_Form_LocalDao geCustomFormLocalDao;
    private Long product_code;
    private boolean downloadStarted = false;
    private HMAux hmAux_Trans;
    private GE_Custom_Form_OperationDao formOperationDao;
    private boolean isSchedule;
    private boolean isFinishPlusNew;
    private String requesting_process;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;
    private MD_Schedule_ExecDao scheduleExecDao;
    private int mTkPrefix;
    private int mTkCode;
    private int mStepCode;

    public Act008_Main_Presenter_Impl(Context context, Act008_Main_View mView, Sync_ChecklistDao syncChecklistDao, MD_ProductDao mdProductDao, GE_Custom_Form_LocalDao geCustomFormLocalDao, Long product_code, HMAux hmAux_Trans, GE_Custom_Form_OperationDao formOperationDao, boolean isSchedule, String requesting_process, MD_Product_SerialDao serialDao, MD_Product_Serial_TrackingDao trackingDao, boolean isFinishPlusNew,int mTkPrefix, int mTkCode, int mStepCode) {
        this.context = context;
        this.mView = mView;
        this.syncChecklistDao = syncChecklistDao;
        this.mdProductDao = mdProductDao;
        this.geCustomFormLocalDao = geCustomFormLocalDao;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.formOperationDao = formOperationDao;
        this.isSchedule = isSchedule;
        this.requesting_process = requesting_process;
        this.serialDao = serialDao;
        this.trackingDao = trackingDao;
        this.isFinishPlusNew = isFinishPlusNew;
        this.scheduleExecDao = new MD_Schedule_ExecDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.mTkPrefix = mTkPrefix;
        this.mTkCode = mTkCode;
        this.mStepCode = mStepCode;

    }

    @Override
    public void getProductInfo(Bundle bundle) {
        MD_Product md_product = null;
        //Se for um agendamento, busca dados da custom_form_local
        //se não do MD
        if (isSchedule) {
            String serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");
            //se não veio serial, seta serial_id GAMBIS
            serial_id = !serial_id.equals("") ? serial_id : Constant.KEY_NO_SERIAL;
            //LUCHE - 03/04/2020
            //Add tentativa de busca do produto antes de gerar o fake.Caso não encontre
            //Gera o fake.
            //Todo verificar a necessidade de colocar essas infos na md_schedule_exec
            md_product  = getMDProduct(product_code);
            if(md_product == null) {
                 /**
                 * LUCHE - 13/04/2020
                 * Devido a falta de tempo do hotfix do hotfix do hotfix para ser publicado ainda nesta data
                 * reaproveitei o que ja tinha.
                 * O ideial era fazer usando o bundle.getString(MD_Schedule_ExecDao.SCHEDULE_PK), porem como existem muitas
                 * validação pelo split , foi deixada esse esquema meio portugues:
                 * Primeiro seleciona o customForm para depois pegar o agendamento.
                 */
                //
                GE_Custom_Form_Local geCustomFormLocal =
                    geCustomFormLocalDao.getByString(
                        new Sql_Act008_003(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE),
                            bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE),
                            bundle.getString(Constant.ACT010_CUSTOM_FORM_VERSION),
                            bundle.getString(Constant.ACT013_CUSTOM_FORM_DATA)
                        ).toSqlQuery()
                    );
                //
                MD_Schedule_Exec scheduleExec = getMdScheduleExec(
                    geCustomFormLocal.getSchedule_prefix(),
                    geCustomFormLocal.getSchedule_code(),
                    geCustomFormLocal.getSchedule_exec()
                );
                //
                md_product = createMdProduct(scheduleExec);
            }
            if(ToolBox_Inf.isValidProduct(md_product)){
                 /*MD_Product_Serial scheduledSerial = getSerialInfo(
                                                        product_code,
                                                        serial_id
                                                    );*/
                //
                MD_Product_Serial scheduledSerial;
                if (bundle.containsKey(Constant.MAIN_MD_PRODUCT_SERIAL)) {
                    scheduledSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
                } else {
                    scheduledSerial = getSerialInfo(
                        product_code,
                        serial_id
                    );
                }
                //
                if(scheduledSerial != null){
                    mView.setMdProductSerial(scheduledSerial);
                }else{
                    mView.setMdProductSerial(md_product.createNewSerialForThisProduct(serial_id));
                }
            }

        } else {
            md_product =
                getMDProduct(product_code);
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

    private MD_Product createMdProduct(MD_Schedule_Exec scheduleExec) {
        MD_Product product = new MD_Product();
        product.setCustomer_code(scheduleExec.getCustomer_code());
        product.setProduct_code(scheduleExec.getProduct_code());
        product.setProduct_id(scheduleExec.getProduct_id());
        product.setProduct_desc(scheduleExec.getProduct_desc());
        product.setRequire_serial(scheduleExec.getRequire_serial());
        product.setAllow_new_serial_cl(scheduleExec.getAllow_new_serial_cl());
        product.setUn("TST");
        product.setSketch_code(0);
        product.setSketch_url("");
        product.setSketch_url_local("");
        product.setSketch_lines(0);
        product.setSketch_columns(0);
        product.setSketch_color("#FFFFFF");
        product.setFlag_offline(1);
        product.setLocal_control(scheduleExec.getLocal_control());
        product.setIo_control(scheduleExec.getIo_control());
        product.setSerial_rule(scheduleExec.getSerial_rule());
        product.setSerial_min_length(scheduleExec.getSerial_min_length());
        product.setSerial_max_length(scheduleExec.getSerial_max_length());
        product.setSite_restriction(scheduleExec.getSite_restriction());
        product.setProduct_icon_name(scheduleExec.getProduct_icon_name());
        product.setProduct_icon_url(scheduleExec.getProduct_icon_url());
        product.setSpare_part(0);
        return product;
    }

    private MD_Schedule_Exec getMdScheduleExec(int schedulePrefix, int scheduleCode, int scheduleExec) {
        return scheduleExecDao.getByString(
            new MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedulePrefix,
                scheduleCode,
                scheduleExec
            ).toSqlQuery()
        );
    }

    private MD_Product getMDProduct(Long product_code) {
        return mdProductDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
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
        if(mView.isHas_tk_ticket_is_form_off_hand() && !mView.isOffHandForm()){
            defineFlow();
        }else if (checkSyncChecklistV2()) {
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
        startDownloadWorkers();
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

    /**
     * LUCHE - 30/06/2020
     * Alterado metodo que chamava serviços de download para chamar os respectivos Workers
     */
    @Override
    public void startDownloadWorkers() {
        if (!downloadStarted) {
            ToolBox_Inf.scheduleAllDownloadWorkers(context);
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
    public void executeSerialSearch(String product_id, String serial_id, boolean scheduled_profile_check) {
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
        bundle.putBoolean(ConstantBaseApp.SCHEDULED_PROFILE_CHECK, scheduled_profile_check);
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
    public void executeUnfocusTicketDownload(int productCode, int serialCode) {
        mView.setWsProcess(WS_TK_Ticket_Search_Not_Focus.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_unfocus_ticket_download_ttl"),
                hmAux_Trans.get("progress_unfocus_ticket_download_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_TK_Ticket_Search_Not_Focus.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MD_Product_SerialDao.PRODUCT_CODE, productCode);
        bundle.putInt(MD_Product_SerialDao.SERIAL_CODE, serialCode);
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

                MD_Product mdProduct = getMDProduct(Long.parseLong(pk[0]));
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

            if (isSchedule || isFinishPlusNew) {
                if(ToolBox_Inf.isConcurrentBySiteLicense(context)
                        && isOutOfLicense()
                        && !mView.isHas_tk_ticket_is_form_off_hand()){
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_serial_site_out_of_license_tll"),
                            hmAux_Trans.get("alert_serial_site_out_of_license_msg"),
                            null,
                            0
                    );
                }else {
                    mView.callAct011(context);
                }
            } else {
                if (mView.isHas_tk_ticket_is_form_off_hand()) {
                    if (mView.isOffHandForm()) {
                        mView.callAct009(context);
                    } else {
                        mView.callAct071(context, getAct071Bundle());
                    }
                } else {
                    if(mView.isNewSerial()){
                        if(ToolBox_Inf.isConcurrentBySiteLicense(context)
                                && isOutOfLicense()
                                && !mView.isHas_tk_ticket_is_form_off_hand()){
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_serial_site_out_of_license_tll"),
                                    hmAux_Trans.get("alert_serial_site_out_of_license_msg"),
                                    null,
                                    0
                            );
                        }else {
                            mView.callAct009(context);
                        }
                    }else {
                        mView.callAct083(context);
                    }
                }
            }
    }
    /**
     * BARRIONUEVO - 19-01-2021
     *     Meotodo que verifica a condição das licenças para o site do serial.
    */
    private boolean isOutOfLicense() {
        String serial_site_code = mView.getmdProductSerialSiteCode();
        if(serial_site_code != null
        && serial_site_code.equals(ToolBox_Con.getPreference_Site_Code(context)) ){
            return false;
        }else{
            return ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context, serial_site_code);
        }
    }

    @Override
    public void onBackPressedClicked() {
        if (isSchedule) {
            if(requesting_process.equals(Constant.ACT013)){
                mView.callAct013(context);
            }else {
                mView.callAct017(context);
            }
        } else {
            if(mView.isHas_tk_ticket_is_form_off_hand()) {
                mView.callAct081(context);
            }else {
                mView.callAct006(context);
            }
        }
    }
    private Bundle getAct071Bundle() {
        Bundle bundle = new Bundle();
        TK_Ticket mTicket = getTicketObj();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, mTicket.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, mTicket.getTicket_code());
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ, 0);
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ_TMP, 0);
        bundle.putInt(TK_Ticket_ActionDao.STEP_CODE, mStepCode);
        bundle.putString(TK_TicketDao.TICKET_ID, mTicket.getTicket_id());
        bundle.putString(TK_TicketDao.TYPE_PATH, mTicket.getType_path());
        bundle.putString(TK_TicketDao.TYPE_DESC, mTicket.getType_desc());
        //params header
        bundle.putString(TK_TicketDao.OPEN_DATE, mTicket.getOpen_date());
        bundle.putInt(TK_TicketDao.OPEN_SITE_CODE, mTicket.getOpen_site_code());
        bundle.putString(TK_TicketDao.OPEN_SITE_DESC, mTicket.getOpen_site_desc());
        bundle.putString(TK_TicketDao.OPEN_SERIAL_ID, mTicket.getOpen_serial_id());
        bundle.putString(TK_TicketDao.OPEN_PRODUCT_DESC, mTicket.getOpen_product_desc());
        bundle.putString(TK_TicketDao.ORIGIN_DESC, mTicket.getOrigin_desc());
        bundle.putBoolean(TK_TicketDao.CURRENT_STEP_ORDER, true);
        bundle.putBoolean(Act070_Main.PARAM_CTRL_CREATION, true);
        bundle.putBoolean(Act070_Main.PARAM_ACTION_CREATION, true);
        return bundle;
    }

    public TK_Ticket getTicketObj() {
        TK_TicketDao ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        TK_Ticket ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTkPrefix,
                        mTkCode
                ).toSqlQuery()
        );
        //
        return ticket;
    }

    /**
     * LUCHE - 06/11/2020
     * Metodo que retorna ticket id + step desc formatada.
     * @param act081Bundle
     * @return
     */
    @Override
    public String getFormattedTicketInfo(Bundle act081Bundle) {
        if(act081Bundle == null) {
            return "";
        }
        return  act081Bundle.getString(TK_TicketDao.TICKET_ID, "")
                +" - "+ act081Bundle.getString(TK_Ticket_StepDao.STEP_DESC, "");
    }
}
