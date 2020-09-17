package com.namoadigital.prj001.ui.act070;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Form;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Checkin;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act070_001;
import com.namoadigital.prj001.sql.Sql_Act070_002;
import com.namoadigital.prj001.sql.Sql_Act070_003;
import com.namoadigital.prj001.sql.Sql_Act070_004;
import com.namoadigital.prj001.sql.Sql_Act070_006;
import com.namoadigital.prj001.sql.Sql_Act070_007;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAbstractProcess;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.ui.act070.model.StepForm;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.ui.act070.model.StepNone;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.PipelineNewProcessDialog;
import com.namoadigital.prj001.view.dialog.PipelineRejectionListDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Act070_Main_Presenter implements Act070_Main_Contract.I_Presenter {
    private Context context;
    private Act070_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    private TK_Ticket_StepDao ticketStepDao;
    private TK_Ticket_CtrlDao ticketCtrlDao;
    private GE_Custom_FormDao geCustomFormDao;
    private GE_Custom_Form_DataDao formDataDao;

    public Act070_Main_Presenter(Context context, Act070_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.ticketDao = new TK_TicketDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        ticketStepDao = new TK_Ticket_StepDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        ticketCtrlDao = new TK_Ticket_CtrlDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.geCustomFormDao = new GE_Custom_FormDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.formDataDao = new GE_Custom_Form_DataDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    @Nullable
    public TK_Ticket getTicketObj(int mTkPrefix, int mTkCode) {
        TK_Ticket ticket = null;
        //
        ticket = ticketDao.getByString(
            new TK_Ticket_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mTkPrefix,
                mTkCode
            ).toSqlQuery()
        );
        //
        return ticket;
    }

    @Override
    public boolean validateBundleParams(int mTkPrefix, int mTkCode) {
        return mTkPrefix > 0 && mTkCode > 0;
    }

    @Override
    public boolean getReadOnlyDefinition(TK_Ticket mTicket) {
        return isReadOnlyStatus(mTicket.getTicket_status()) || missingProfile();
    }

    private boolean missingProfile() {
        return !ToolBox_Inf.profileExists(
            context,
            ConstantBaseApp.PROFILE_MENU_TICKET,
            ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_ACTION_EXEC
        );
    }

    @Override
    public boolean isReadOnlyStatus(String ticketStatus) {
        return !ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(ticketStatus)
            && !ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(ticketStatus);
    }

    @Override
    public void prepareSyncProcess(TK_Ticket mTicket, boolean allowOfflineSave) {
        //Verifica se há necessidade de envidar dados para o server.
        if(checkUpdateRequiredNeeds(mTicket)){
            if(ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTicket.getTicket_prefix(), mTicket.getTicket_code())){
                //callWsSave();
                defineFormWaitingSyncFlow(mTicket.getTicket_prefix(), mTicket.getTicket_code(), allowOfflineSave);
            }else {
                executeTicketSaveProcess(false);
            }
        }else{
            executeSyncProcess(mTicket.getTicket_prefix(), mTicket.getTicket_code(),mTicket.getScn());
        }
    }

    private void executeSyncProcess(int ticket_prefix, int ticket_code, int scn) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Download.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_download_ticket_ttl"),
                hmAux_Trans.get("dialog_download_ticket_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(TK_TicketDao.TICKET_PREFIX, getTicketSyncPkFormat(ticket_prefix, ticket_code,scn));
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);

        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private String getTicketSyncPkFormat(int ticket_prefix, int ticket_code, int scn) {
        return ToolBox_Con.getPreference_Customer_Code(context) + "|" + ticket_prefix + "|" + ticket_code +"|" + scn;
    }

    private void executeTicketSaveProcess(boolean allowOffline) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Save.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_ticket_save_ttl"),
                hmAux_Trans.get("dialog_ticket_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Save.class);
            Bundle bundle = new Bundle();
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            //SE FOR SAVE, EXIBE MSG , SE FOR SYNC, NÃO EXIBE
            if(allowOffline) {
                mView.showAlert(
                    hmAux_Trans.get("alert_offline_save_ttl"),
                    hmAux_Trans.get("alert_offline_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.callRefreshUi();
                        }
                    },
                    false
                );
            }else{
                ToolBox_Inf.showNoConnectionDialog(context);
            }
        }
    }

    /**
     * LUCHE - 10/09/2020
     * DEVE SEMPRE SER PRECEDIDO DA CHAMADA DO hasFormWaitingSyncWithinTicket
     * Metodo que verifica se deve chamar o Ws de save do form ou exibir msg de que existe form com
     * pendencia de GPS.
     *
     * @param ticket_prefix
     * @param ticket_code
     */
    private void defineFormWaitingSyncFlow(int ticket_prefix, int ticket_code, boolean allowOfflineSave){
        if(ToolBox_Inf.hasFormGpsPendencyWithinTicket(context,ticket_prefix,ticket_code)){
            mView.showAlert(
                hmAux_Trans.get("alert_form_location_pendency_ttl"),
                hmAux_Trans.get("alert_form_location_pendency_msg")
            );
        }else{
            callWsSave(allowOfflineSave);
        }
    }

    private void callWsSave(boolean allowOfflineSave) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_ticket_form_save_ttl"),
                    hmAux_Trans.get("dialog_ticket_form_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Save.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
            bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device
            bundle.putString(Act005_Main.WS_PROCESS_SO_STATUS, "SEND");

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            if(allowOfflineSave){
                mView.showAlert(
                        hmAux_Trans.get("alert_offline_save_ttl"),
                        hmAux_Trans.get("alert_offline_save_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.callRefreshUi();
                            }
                        },
                        false
                );
            }else{
                ToolBox_Inf.showNoConnectionDialog(context);
            }
        }
    }

//    /**
//     * BARRIONUEVO 01-09-2020
//     * Metodo que verifica forms de ctrl que estão em waiting sync
//     * LUCHE - 10/09/2020
//     * Comentado pois será usado a versão do toolbox_inf, ja que esse metodo pe chamado em mais telas
//     * @param ticket_prefix
//     * @param ticket_code
//     * @return
//     */
//    private boolean hasFormWaitingSync(int ticket_prefix, int ticket_code) {
//
//        GE_Custom_Form_Data formData = formDataDao.getByString(
//                new Sql_Act070_005(
//                        ToolBox_Con.getPreference_Customer_Code(context),
//                        ticket_prefix,
//                        ticket_code
//                ).toSqlQuery()
//        );
//        return formData != null;
//    }

    @Override
    public boolean checkOnlySyncNeeds(TK_Ticket mTicket) {
        return
                mTicket != null
                && (mTicket.getSync_required() == 1);
    }

    public boolean checkUpdateRequiredNeeds(TK_Ticket mTicket) {
        return mTicket != null
            && (mTicket.getUpdate_required() == 1
                || mTicket.getUpdate_required_product() == 1
                || ToolBox_Inf.isTicketInTokenFile(context, mTicket.getTicket_prefix(),mTicket.getTicket_code())
                || ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTicket.getTicket_prefix(), mTicket.getTicket_code())
                );
    }

    @Override
    public void processSaveReturn(int mPrefix, int mCode, String jsonRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn> checkinReturns = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        //
        if (jsonRet != null && !jsonRet.isEmpty()) {
            try {
                checkinReturns = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn>>() {
                    }.getType());

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
            //
            if (checkinReturns != null && checkinReturns.size() > 0) {
                boolean ticketResult = true;
                int ticketNextIdx = 0;
                HMAux auxResult = new HMAux();
                String ticketPk = mPrefix + "." + mCode;
                //
                for (WS_TK_Ticket_Save.TicketSaveActReturn actReturn : checkinReturns) {
                    String ticketCode = actReturn.getPrefix() + "." + actReturn.getCode();
                    //
                    if (!auxResult.containsKey(ticketCode)
                        || (auxResult.containsKey(ticketCode)
                        && !actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK))
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        //auxResult.put(ticketCode, getResultMsgFormmated(actReturn));

                        if(actReturn.isProcessError()){
                            ticketResult = !actReturn.isProcessError();
                            auxResult.put(ticketCode, actReturn.getRetMsg());
                        }
                    }
                }
                //For no resumido por ticket montando msg a ser exibida
                for (Map.Entry<String, String> item : auxResult.entrySet()) {
                    HMAux hmAux = new HMAux();
                    //
                    //Monta HmAux
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("ticket_lbl"));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.getValue());
                    //
                    if (item.getKey().equals(ticketPk)) {
                        //05/08/2020 - Modificado o set para ser feito no primeiro loop
                        //ticketResult = item.getValue().equals(ConstantBaseApp.MAIN_RESULT_OK);
                        resultList.add(ticketNextIdx, hmAux);
                        ticketNextIdx++;
                    } else {
                        resultList.add(hmAux);
                    }
                }
                //
                mView.addResultList(resultList);
                mView.showResult(ticketResult);
            } else {
                mView.showAlert(
                    hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                    hmAux_Trans.get("alert_none_ticket_returned_msg")
                );
            }
        } else {
            mView.showAlert(
                hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                hmAux_Trans.get("alert_none_ticket_returned_msg")
            );
        }
    }

    @Override
    public void processCheckinReturn(int mPrefix, int mCode, String jsonRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_TK_Ticket_Checkin.TicketCheckinActReturn> checkinReturns = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        //
        if (jsonRet != null && !jsonRet.isEmpty()) {
            try {
                checkinReturns = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_TK_Ticket_Checkin.TicketCheckinActReturn>>() {
                    }.getType());

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
            //
            if (checkinReturns != null && checkinReturns.size() > 0) {
                boolean ticketResult = true;
                int ticketNextIdx = 0;
                HMAux auxResult = new HMAux();
                HMAux auxAction = new HMAux();
                //
                for (WS_TK_Ticket_Checkin.TicketCheckinActReturn actReturn : checkinReturns) {
                    String ticketCode = actReturn.getPrefix() + "." + actReturn.getCode();
                    //
                    if (!auxResult.containsKey(ticketCode)
                        || (auxResult.containsKey(ticketCode)
                        && (!actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)
                        || actReturn.isProcessError()
                    )
                    )
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        if(!ConstantBaseApp.MAIN_RESULT_OK.equals(actReturn.getRetStatus())){
                            ticketResult = ConstantBaseApp.MAIN_RESULT_OK.equals(actReturn.getRetStatus());
                            auxResult.put(ticketCode, getResultMsgFormmated(actReturn));
                            auxAction.put(ticketCode, String.valueOf(actReturn.getCheckinAction()));
                        }
                    }
                }
                //For no resumido por ticket montando msg a ser exibida
                for (Map.Entry<String, String> item : auxResult.entrySet()) {
                    String ticketPk = mPrefix + "." + mCode;
                    HMAux hmAux = new HMAux();
                    //
                    //Monta HmAux
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("ticket_lbl"));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.getValue());
                    //
                    if (item.getKey().equals(ticketPk)) {
                        ticketResult = item.getValue().equals(ConstantBaseApp.MAIN_RESULT_OK);
                        //
                        hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, setCheckinActionResultLbl(item.getValue(), auxAction.get(ticketPk)));
                        //
                        resultList.add(ticketNextIdx, hmAux);
                        ticketNextIdx++;
                    } else {
                        hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, setCheckinActionResultLbl(item.getValue(), auxAction.get(ticketPk)));
                        resultList.add(hmAux);
                    }
                }
                //
                mView.addResultList(resultList);
                mView.showResult(ticketResult);
            } else {
                mView.showAlert(
                    hmAux_Trans.get("alert_checkin_not_returned_ttl"),
                    hmAux_Trans.get("alert_checkin_not_returned_msg")
                );
            }
        } else {
            mView.showAlert(
                hmAux_Trans.get("alert_checkin_not_returned_ttl"),
                hmAux_Trans.get("alert_checkin_not_returned_msg")
            );
        }
    }



    private String setCheckinActionResultLbl(String status, String action) {
        if ("1".equals(action)) {
            return hmAux_Trans.get("result_checkin_lbl") + " " + status;
        } else if ("0".equals(action)) {
            return hmAux_Trans.get("result_checkin_cancel_lbl") + " " + status;
        }
        //Não deve acontecer
        return status;
    }

    private String getResultMsgFormmated(WS_TK_Ticket_Checkin.TicketCheckinActReturn actReturn) {
        if (actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)) {
            return actReturn.getRetStatus();
        } else {
            return actReturn.isProcessError() ? actReturn.getProcessStatus() + "\n" + actReturn.getProcessMsg() : actReturn.getRetStatus() + "\n" + actReturn.getRetMsg();
        }
    }

    private String getResultSaveMsgFormmated(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        if (actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)) {
            return actReturn.getRetStatus();
        } else {
            return actReturn.isProcessError() ? actReturn.getProcessStatus() + "\n" + actReturn.getProcessMsg() : actReturn.getRetStatus() + "\n" + actReturn.getRetMsg();
        }
    }
    @Override
    public boolean checkSyncRequireNeedsChange(int ticket_prefix, int ticket_code) {
        TK_Ticket aux = getTicketObj(ticket_prefix, ticket_code);
        if (aux != null
            && aux.getTicket_prefix() == ticket_prefix
            && aux.getTicket_code() == ticket_code
            && aux.getSync_required() == 1
        ) {
            return true;
        }
        return false;
    }

    public Bundle getAct071ActionBundle(TK_Ticket mTicket, int stepCode, int processTkSeq, int processTkSeqTmp, boolean currentStep, boolean actionCreation) {
        return getAct071Bundle(mTicket, stepCode, processTkSeq, processTkSeqTmp, currentStep, false, actionCreation);
    }

    @Override
    public Bundle getAct071Bundle(TK_Ticket mTicket, int stepCode, int processTkSeq, int processTkSeqTmp, boolean currentStep, boolean ctrlCreation, boolean actionCreation) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, mTicket.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, mTicket.getTicket_code());
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ, processTkSeq);
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ_TMP, processTkSeqTmp);
        bundle.putInt(TK_Ticket_ActionDao.STEP_CODE, stepCode);
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
        bundle.putBoolean(TK_TicketDao.CURRENT_STEP_ORDER, currentStep);
        bundle.putBoolean(Act070_Main.PARAM_CTRL_CREATION, ctrlCreation);
        bundle.putBoolean(Act070_Main.PARAM_ACTION_CREATION, actionCreation);
        return bundle;
    }

    @Override
    public void defineProcessBtnFlow(final TK_Ticket mTicket, final StepProcessBtn stepProcessBtn) {
        switch (stepProcessBtn.getProcessType()){
            case ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_CHECKIN:
                mView.showAlert(
                    hmAux_Trans.get("alert_checkin_confirm_ttl"),
                    hmAux_Trans.get("alert_checkin_confirm_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setCheckinToStep(mTicket,stepProcessBtn.getStepCode());
                        }
                    },
                    true
                );
                break;
            case ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_CHECKOUT:
                mView.showAlert(
                    hmAux_Trans.get("alert_checkout_confirm_ttl"),
                    hmAux_Trans.get("alert_checkout_confirm_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setCheckOutToStep(mTicket,stepProcessBtn.getStepCode());
                        }
                    },
                    true
                );
                break;
            case ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_ADD_NEW:
                showNewProcessDialog(mTicket, hmAux_Trans,stepProcessBtn);
                break;
        }

    }

    private void showNewProcessDialog(final TK_Ticket mTicket, HMAux hmAux_trans, StepProcessBtn stepProcessBtn) {
        PipelineNewProcessDialog newProcessDialog =
            new PipelineNewProcessDialog(
                context,
                hmAux_trans,
                stepProcessBtn,
                new PipelineNewProcessDialog.PipelineProcessDialogClickListener() {
                    @Override
                    public void onProcessActionClick(StepProcessBtn processBtn) {
                        mView.callAct071(
                            getAct071NewProcessoBundle(
                                mTicket,
                                processBtn.getStepCode()
                            )
                        );
                    }

                    @Override
                    public void onCancelClick() {

                    }
                }
            );
        //
        newProcessDialog.show();
    }

    @Override
    public void defineNoneFlow(final TK_Ticket mTicket, StepNone stepNone) {
        final TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(),mTicket.getTicket_code(), stepNone.getStepCode());
        final TK_Ticket_Ctrl ticketCtrl = getSelectedCtrlFromDb(mTicket.getTicket_prefix(),mTicket.getTicket_code(),stepNone.getProcessTkSeq(),stepNone.getStepCode());
        //
        if(ticketStep != null && ticketCtrl != null){
            if(!isDoneOrWaitingSync(ticketStep.getStep_status())){
                if(stepNone.isCurrentStep()){
                    if( isStartEndActionExecution(ticketStep, ticketCtrl)
                        || isOneTouchActionExecution(ticketStep, ticketCtrl)
                    ){
                        mView.showAlert(
                            hmAux_Trans.get("alert_start_process_ttl"),
                            hmAux_Trans.get("alert_start_process_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startNoneProcess(mTicket,ticketStep,ticketCtrl);
                                }
                            },
                            true
                        );
                    } else if (ConstantBaseApp.SYS_STATUS_PROCESS.equals(ticketCtrl.getCtrl_status())){
                        mView.showAlert(
                            hmAux_Trans.get("alert_process_access_denied_ttl"),
                            hmAux_Trans.get("alert_process_started_in_server_msg")
                        );
                    } else{
                        //NÃO MAPEADO.
                    }
                }
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_step_or_ctrl_not_found_ttl"),
                hmAux_Trans.get("alert_step_or_ctrl_not_found_msg")
            );
        }

    }

    @Override
    public void defineFormFlow(final TK_Ticket mTicket, StepForm stepForm) {
        final TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(),mTicket.getTicket_code(), stepForm.getStepCode());
        final TK_Ticket_Ctrl ticketCtrl = getSelectedCtrlFromDb(mTicket.getTicket_prefix(),mTicket.getTicket_code(),stepForm.getProcessTkSeq(),stepForm.getStepCode());
        //
        if(ticketStep != null && ticketCtrl != null && ticketCtrl.getForm() != null){
            if(!isDoneOrWaitingSync(ticketStep.getStep_status())){
                if(stepForm.isCurrentStep()){
                    if (isStartEndActionExecution(ticketStep, ticketCtrl)
                        || isOneTouchActionExecution(ticketStep, ticketCtrl)
                    ) {
                        /**
                         * BARRIONUEVO 08-09-2020
                         * Metodo que verifica acesso ao produto.
                         */
                        if(userHasProductAccess(ticketCtrl.getProduct_code())) {
                            if (checkFormMasterDataExists(ticketCtrl.getForm())) {
                                showConfirmStartFormDialog(mTicket, ticketStep, ticketCtrl);
                            } else {
                                mView.showAlert(
                                    hmAux_Trans.get("alert_form_master_data_not_found_ttl"),
                                    hmAux_Trans.get("alert_form_master_data_not_found_msg"),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            if(ToolBox_Con.isOnline(context)) {
                                                ToolBox_Inf.hasFormProductOutdate(context, mTicket.getTicket_prefix(), mTicket.getTicket_code());
                                                callWsSync();
                                            }else{
                                                ToolBox_Inf.showNoConnectionDialog(context);
                                            }
                                        }
                                    },
                                    false
                                );
                            }
                        }else{
                            mView.showAlert(
                                    hmAux_Trans.get("alert_product_form_access_denied_ttl"),
                                    hmAux_Trans.get("alert_product_form_access_denied_msg")
                            );
                        }
                    } else if (ConstantBaseApp.SYS_STATUS_PROCESS.equals(ticketCtrl.getCtrl_status())) {
                        if (formDataAlreadyExists(ticketCtrl.getForm())) {
                            startFormProcess(mTicket, ticketStep, ticketCtrl);
                        } else if(formCtrlCreatedBySameUsr(ticketCtrl)) {
                            showConfirmStartFormDialog(mTicket, ticketStep, ticketCtrl);
                        } else {
                            mView.showAlert(
                                hmAux_Trans.get("alert_process_access_denied_ttl"),
                                hmAux_Trans.get("alert_process_started_in_server_msg")
                            );
                        }
                    } else if (isDoneOrWaitingSync(ticketCtrl.getCtrl_status())) {
                        navegateToFormOrPDF(mTicket, ticketStep, ticketCtrl);
                    }
                }else{
                    if(isDoneOrWaitingSync(ticketCtrl.getCtrl_status())){
                        navegateToFormOrPDF(mTicket, ticketStep, ticketCtrl);
                    }
                }
//
//                if(ConstantBaseApp.SYS_STATUS_PENDING.equals(ticketCtrl.getCtrl_status()) && stepForm.isCurrentStep()){
//                    mView.showAlert(
//                        hmAux_Trans.get("alert_start_process_ttl"),
//                        hmAux_Trans.get("alert_start_process_msg"),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                startFormProcess(mTicket,ticketStep,ticketCtrl);
//                            }
//                        },
//                        true
//                    );
//                }else if (ConstantBaseApp.SYS_STATUS_PROCESS.equals(ticketCtrl.getCtrl_status())){
//                    if(ticketCtrl.getForm() != null && formAlreadyExists(ticketCtrl.getForm())){
//                        startFormProcess(mTicket,ticketStep,ticketCtrl);
//                    }else {
//                        mView.showAlert(
//                            hmAux_Trans.get("alert_process_access_denied_ttl"),
//                            hmAux_Trans.get("alert_process_started_in_server_msg")
//                        );
//                    }
//                }//não faz nada, pois não tem ação
            }else{
                navegateToFormOrPDF(mTicket, ticketStep, ticketCtrl);
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_step_or_ctrl_not_found_ttl"),
                hmAux_Trans.get("alert_step_or_ctrl_not_found_msg")
            );
        }
    }

    @Override
    public void processWS_SaveReturn(String mLink) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
            ArrayList<TSave_Rec.Error_Process> errorProcesses = null;
            try {
                errorProcesses = gson.fromJson(
                        mLink,
                        new TypeToken<ArrayList<TSave_Rec.Error_Process>>() {
                        }.getType()
                );
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
            }
            //
            if(errorProcesses != null && errorProcesses.size() > 0){
                ArrayList<HMAux> auxResults = new ArrayList<>();
                for (TSave_Rec.Error_Process error_process : errorProcesses) {
                    //
                    HMAux mHmAux = ToolBox_Inf.getWsSaveErrorProcessAuxResult(error_process);
                    //
                    HMAux aux = new HMAux();
                    switch (mHmAux.get("type")) {
                       case ConstantBaseApp.SYS_STATUS_SCHEDULE:
                            aux.put(Generic_Results_Adapter.LABEL_TTL, mHmAux.get("label"));
                            aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mHmAux.get("final_status")+"\n"+mHmAux.get("status"));
                            break;
                        case TSave_Rec.Error_Process.ERROR_TYPE_TICKET:
                            aux.put(Generic_Results_Adapter.LABEL_TTL, mHmAux.get("label"));
                            aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mHmAux.get("final_status")+"\n"+mHmAux.get("status"));
                            break;
                    }
                    //
                    auxResults.add(aux);
                }
                //
                mView.addResultList(auxResults);
            }

    }

    private void showConfirmStartFormDialog(final TK_Ticket mTicket, final TK_Ticket_Step ticketStep, final TK_Ticket_Ctrl ticketCtrl) {
        mView.showAlert(
            hmAux_Trans.get("alert_start_process_ttl"),
            hmAux_Trans.get("alert_start_process_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startFormProcess(mTicket, ticketStep, ticketCtrl);
                }
            },
            true
        );
    }

    private boolean formCtrlCreatedBySameUsr(TK_Ticket_Ctrl ticketCtrl) {
        return
            ticketCtrl.getCtrl_start_user() != null
            && ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(ticketCtrl.getCtrl_start_user()))
            && ticketCtrl.getForm() != null
            && ticketCtrl.getForm().getCustom_form_data() == null
            ;
    }

    private void navegateToFormOrPDF(TK_Ticket mTicket, TK_Ticket_Step ticketStep, TK_Ticket_Ctrl ticketCtrl) {
        if(ticketCtrl.getForm() != null){
            if (formDataAlreadyExists(ticketCtrl.getForm())) {
                startFormProcess(mTicket, ticketStep, ticketCtrl);
            } else {
                tryOpenFormPDF(ticketCtrl.getForm());
            }
        } else{
            mView.showAlert(
                hmAux_Trans.get("alert_form_obj_not_found_ttl"),
                hmAux_Trans.get("alert_form_obj_not_found_msg")
            );
        }
    }

    private void tryOpenFormPDF(TK_Ticket_Form form) {
        if(form.getPdf_url() == null || form.getPdf_url().isEmpty()){
            mView.showAlert(
                hmAux_Trans.get("alert_form_pdf_not_generated_ttl"),
                hmAux_Trans.get("alert_form_pdf_not_generated_msg")
            );
        }else{
            if(form.getPdf_url_local() == null || form.getPdf_url().isEmpty()){
                mView.showAlert(
                    hmAux_Trans.get("alert_form_pdf_not_downloaded_ttl"),
                    hmAux_Trans.get("alert_form_pdf_not_downloaded_ttl")
                );
            }else{
               openFormPDF(form.getPdf_url_local());
            }
        }
    }

    private void openFormPDF(String pdf_url_local) {
        File pdfFile = new File(ConstantBaseApp.CACHE_PATH + "/" + pdf_url_local);
        copyPdfToPdfFolder(pdfFile);
        Intent pdfIntent = ToolBox_Inf.getOpenPdfIntent(context,ConstantBaseApp.CACHE_PDF + "/" + pdf_url_local);
        //
        try {
            context.startActivity(pdfIntent);
        }catch (ActivityNotFoundException e){
            ToolBox_Inf.registerException(e);
            //
            ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_starting_pdf_not_supported_ttl"),
                hmAux_Trans.get("alert_starting_pdf_not_supported_msg"),
                null,
                0
            );
        }
    }

    private void copyPdfToPdfFolder(File pdfFile) {
        try {
            ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);
            ToolBox_Inf.copyFile(
                pdfFile,
                new File(Constant.CACHE_PDF)
            );
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
    }

    //todo VERIFICAR COM O CESINHA COMO VAI FICAR O MODELO DE CTRL COM FORM
    private void startFormProcess(TK_Ticket mTicket, TK_Ticket_Step ticketStep, TK_Ticket_Ctrl ticketCtrl) {
        if(ticketCtrl.getForm() != null){
            if(ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(ticketCtrl.getForm().getForm_status())){
                //LUCHE - 31/08/2020
                //Pela definição de hoje, uma vez finalizado será exibido somente o PDF.
//                if(formDataAlreadyExists(ticketCtrl.getForm())) {
//                    mView.callAct011(getAct011Bundle(ticketCtrl));
//                }else{
//                    tryOpenFormPDF(ticketCtrl.getForm());
//                }
                tryOpenFormPDF(ticketCtrl.getForm());
            }else{
                mView.callAct011(getAct011Bundle(ticketCtrl));
            }
        }
    }

    private boolean userHasProductAccess(int open_product_code) {
        MD_ProductDao md_productDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(
                        ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        MD_Product md_product = null;
        md_product = md_productDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        open_product_code
                ).toSqlQuery()
        );
        //
        if (ToolBox_Inf.isValidProduct(md_product)) {
            return true;
        }
        return false;
    }

    /**
     * LUCHE - 24/08/2020
     * <p></p>
     * Metodo que verifica se o form existe localmente.
     * @param form Obj Tk_Ticket_form
     * @return
     */
    private boolean formDataAlreadyExists(TK_Ticket_Form form) {
        GE_Custom_Form_Data formData = formDataDao.getByString(
            new Sql_Act070_003(
                form.getCustomer_code(),
                form.getTicket_prefix(),
                form.getTicket_code(),
                form.getTicket_seq(),
                form.getTicket_seq_tmp(),
                form.getStep_code()
            ).toSqlQuery()
        );
        //
        return formData != null && formData.getCustomer_code() > 0;
    }

    private boolean checkFormMasterDataExists(TK_Ticket_Form form) {
        GE_Custom_Form customForm = geCustomFormDao.getByString(
            new Sql_Act070_004(
                form.getCustomer_code(),
                form.getCustom_form_type(),
                form.getCustom_form_code()
            ).toSqlQuery()
        );
        //
        return customForm != null && customForm.getCustomer_code() > 0;
    }

    /**
     * LUCHE - 24/08/2020
     * <P></P>
     * Metodo que gera o bundle para Act011. Bundle serve tanto para criar o form quando para navegar
     * para um form ja iniciado.
     *
     * @param ticketCtrl
     * @return
     */
    private Bundle getAct011Bundle(TK_Ticket_Ctrl ticketCtrl) {
        GE_Custom_Form customForm = geCustomFormDao.getByString(
                new Sql_Act070_006(
                        ticketCtrl.getCustomer_code(),
                        ticketCtrl.getForm().getCustom_form_type(),
                        ticketCtrl.getForm().getCustom_form_code()
                ).toSqlQuery()
        );

        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(ticketCtrl.getProduct_code()));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, ticketCtrl.getProduct_desc());
        bundle.putString(MD_ProductDao.PRODUCT_ID, ticketCtrl.getProduct_id());
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, ticketCtrl.getSerial_id());
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, String.valueOf(ticketCtrl.getForm().getCustom_form_type()));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, ticketCtrl.getForm().getCustom_form_type_desc());
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, String.valueOf(ticketCtrl.getForm().getCustom_form_code()));
        /*
            Barrionuevo - 02-09-2020
            Parametro com a versão mais atual do form.
         */
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, String.valueOf(customForm.getCustom_form_version()));
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, ticketCtrl.getForm().getCustom_form_desc());
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
            getCustomFormDataOrNew(ticketCtrl)
        );
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_PREFIX,ticketCtrl.getTicket_prefix());
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_CODE,ticketCtrl.getTicket_code());
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ,ticketCtrl.getTicket_seq());
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP,ticketCtrl.getTicket_seq_tmp());
        bundle.putInt(TK_Ticket_CtrlDao.STEP_CODE,ticketCtrl.getStep_code());
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT070);
        return bundle;
    }

    /**
     * LUCHE - 04/09/2020
     * <p></p>
     * Metodo que retorno o custom_form_data_tmp ou novo custom_form_data, caso seja criação de form.
     * @param ticketCtrl
     * @return
     */
    @NonNull
    private String getCustomFormDataOrNew(@NonNull TK_Ticket_Ctrl ticketCtrl) {
        if(ticketCtrl != null && ticketCtrl.getForm() != null){
            return
                ticketCtrl.getForm().getCustom_form_data_tmp() != null
                    ? String.valueOf(ticketCtrl.getForm().getCustom_form_data_tmp())
                    : getNextCustomFormData(ticketCtrl.getForm());
        }else{
            return "0";
        }
    }

    /**
     * LUCHE - 04/09/2020
     * Metodo que retorno proximo custom_form_data para ser usado na criação do form do ticket.
     * @param form
     * @return
     */
    private String getNextCustomFormData(TK_Ticket_Form form) {
        HMAux nextFormData = formDataDao.getByStringHM(
            new GE_Custom_Form_Local_Sql_002(
                String.valueOf(form.getCustomer_code()),
                String.valueOf(form.getCustom_form_type()),
                String.valueOf(form.getCustom_form_code()),
                String.valueOf(form.getCustom_form_version())
            ).toSqlQuery().toLowerCase()
        );
        //
        if (nextFormData != null && nextFormData.size() > 0 && nextFormData.hasConsistentValue("id")) {
            return nextFormData.get("id");
        }
        //
        return "0";
    }

    private void startNoneProcess(TK_Ticket mTicket, TK_Ticket_Step ticketStep, TK_Ticket_Ctrl ticketCtrl) {
        int ctrlIdx = getCtrlIdx(ticketCtrl, ticketStep);
        int stepIdx = getStepIdx(ticketStep,mTicket);
        //
        setDataIntoCtrl(ticketCtrl);
        setCheckInIntoStepWhenOneTouchStep(ticketStep,ticketCtrl);
        //
        ticketStep.getCtrl().set(ctrlIdx,ticketCtrl);
        //
        checkCloseStepForWaitingSync(ticketStep, ticketCtrl);

        //
        ticketCtrl.setUpdate_required(1);
        ticketStep.setUpdate_required(1);
        mTicket.setUpdate_required(1);
        //
        mTicket.getStep().set(stepIdx,ticketStep);
        //
        DaoObjReturn daoObjReturn  = ticketDao.addUpdate(mTicket);
        if(!daoObjReturn.hasError()){
            if(ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTicket.getTicket_prefix(), mTicket.getTicket_code())){
                //callWsSave();
                defineFormWaitingSyncFlow(mTicket.getTicket_prefix(), mTicket.getTicket_code(), true);
            }else {
                executeTicketSaveProcess(true);
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_error_on_save_none_ttl"),
                hmAux_Trans.get("alert_error_on_save_none_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressedClicked(mView.getRequestingAct());
                    }
                },
                false
            );
        }

    }

    private void setDataIntoCtrl(TK_Ticket_Ctrl ticketCtrl) {
        int userCode = ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context));
        String userNick = ToolBox_Con.getPreference_User_Code_Nick(context);
        String ctrlStartEndDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
        //
        ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
        ticketCtrl.setCtrl_start_user(userCode);
        ticketCtrl.setCtrl_start_user_name(userNick);
        ticketCtrl.setCtrl_start_date(ctrlStartEndDate);
        ticketCtrl.setCtrl_end_user(userCode);
        ticketCtrl.setCtrl_end_user_name(userNick);
        ticketCtrl.setCtrl_end_date(ctrlStartEndDate);
    }

    private void setCheckInIntoStepWhenOneTouchStep(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        if(ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(ticketStep.getExec_type())
            && !ToolBox_Inf.hasConsistentValueString(ticketStep.getStep_start_date())
        ) {
            ticketStep.setStep_start_date(mTicketCtrl.getCtrl_start_date());
            ticketStep.setStep_start_user(mTicketCtrl.getCtrl_start_user());
            ticketStep.setStep_start_user_nick(mTicketCtrl.getCtrl_start_user_name());
        }
    }

    private void checkCloseStepForWaitingSync(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        int stepCtrlsFinalizedCounter = 0;
        for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
            if(isDoneOrWaitingSync(ticketCtrl.getCtrl_status())){
                stepCtrlsFinalizedCounter++;
            }
        }
        //Se todos os ctrl estão finalizado e o step é one_touch ou for start_end com move_next_step,
        //faz checkout
        if( stepCtrlsFinalizedCounter == ticketStep.getCtrl().size()
            && ticketStep.getMove_next_step() == 1
        ){
            ticketStep.setStep_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            ticketStep.setStep_end_date(mTicketCtrl.getCtrl_end_date());
            ticketStep.setStep_end_user(mTicketCtrl.getCtrl_end_user());
            ticketStep.setStep_end_user_nick(mTicketCtrl.getCtrl_end_user_name());
        }
    }

    /**
     * LUCHE - 23/03/2020
     * <p></p>
     * Metodo que retorna o indice do control que esta sendo alterado.
     * Na teoria, sempre retornará um valor, por o crl sempre existe.
     * @param mTicketCtrl Obj controle alterado pelo usr
     * @param tkTicketStep Obj Ticket Step ao qual o controle pertence
     * @return - Idx do ctrl ou -1 caso não encontre.
     */
    private int getCtrlIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket_Step tkTicketStep) {
        for (int i = 0; i < tkTicketStep.getCtrl().size(); i++) {
            if(
                tkTicketStep.getCtrl().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                    && tkTicketStep.getCtrl().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                    && tkTicketStep.getCtrl().get(i).getTicket_seq() == mTicketCtrl.getTicket_seq()
                    && tkTicketStep.getCtrl().get(i).getStep_code() == mTicketCtrl.getStep_code()
            ){
                return i;
            }
        }
        //
        return -1;
    }

    private Bundle getAct071NewProcessoBundle(TK_Ticket mTicket, int stepCode) {
        return getAct071Bundle(mTicket,stepCode,0,0,true,true,true);
    }

    private void setCheckinToStep(TK_Ticket mTicket, int stepCode) {
        TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(), mTicket.getTicket_code(), stepCode);
        //
        ticketStep.setStep_start_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        ticketStep.setStep_start_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        ticketStep.setStep_start_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        ticketStep.setUpdate_required(1);
        ticketStep.setStep_status(ConstantBaseApp.SYS_STATUS_PROCESS);
        mTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_PROCESS);
        //
        int stepIdx = getStepIdx(ticketStep,mTicket);
        mTicket.setUpdate_required(1);
        mTicket.getStep().set(stepIdx,ticketStep);
        DaoObjReturn daoObjReturn  = ticketDao.addUpdate(mTicket);
        if(!daoObjReturn.hasError()){
            if(ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTicket.getTicket_prefix(), mTicket.getTicket_code())){
                //callWsSave();
                defineFormWaitingSyncFlow(mTicket.getTicket_prefix(), mTicket.getTicket_code(), true);
            }else {
                executeTicketSaveProcess(true);
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_error_on_set_checkin_ttl"),
                hmAux_Trans.get("alert_error_on_set_checkin_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressedClicked(mView.getRequestingAct());
                    }
                },
                false
            );
        }
    }

    private void setCheckOutToStep(TK_Ticket mTicket, int stepCode) {
        TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(), mTicket.getTicket_code(), stepCode);
        //
        ticketStep.setStep_end_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        ticketStep.setStep_end_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        ticketStep.setStep_end_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        ticketStep.setUpdate_required(1);
        ticketStep.setStep_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
        //
        int stepIdx = getStepIdx(ticketStep,mTicket);
        mTicket.setUpdate_required(1);
        mTicket.getStep().set(stepIdx,ticketStep);
        DaoObjReturn daoObjReturn  = ticketDao.addUpdate(mTicket);
        if(!daoObjReturn.hasError()){
            if(ToolBox_Inf.hasFormWaitingSyncWithinTicket(context,mTicket.getTicket_prefix(), mTicket.getTicket_code())){
                //callWsSave();
                defineFormWaitingSyncFlow(mTicket.getTicket_prefix(), mTicket.getTicket_code(), true);
            }else {
                executeTicketSaveProcess(true);
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_error_on_set_checkout_ttl"),
                hmAux_Trans.get("alert_error_on_set_checkout_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressedClicked(mView.getRequestingAct());
                    }
                },
                false
            );
        }
    }

    private int getStepIdx(TK_Ticket_Step ticketStep, TK_Ticket tkTicket) {
        for (int i = 0; i < tkTicket.getStep().size(); i++) {
            if(tkTicket.getStep().get(i).getTicket_prefix() == ticketStep.getTicket_prefix()
               && tkTicket.getStep().get(i).getTicket_code() == ticketStep.getTicket_code()
               && tkTicket.getStep().get(i).getStep_code() == ticketStep.getStep_code()
            ){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void defineActionFlow(final TK_Ticket mTicket, final StepAction stepAction) {
        TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(),mTicket.getTicket_code(), stepAction.getStepCode());
        TK_Ticket_Ctrl ticketCtrl = getSelectedCtrlFromDb(mTicket.getTicket_prefix(),mTicket.getTicket_code(),stepAction.getProcessTkSeq(),stepAction.getStepCode());
        if(ticketStep != null && ticketCtrl != null){
            if(isDoneOrWaitingSync(ticketStep.getStep_status())){
                mView.callAct071(
                    getAct071ActionBundle(mTicket,stepAction.getStepCode(),stepAction.getProcessTkSeq(), stepAction.getProcessTkSeqTmp(), stepAction.isCurrentStep(), false)
                );
            }else{
                if(isDoneOrWaitingSync(ticketCtrl.getCtrl_status())){
                    mView.callAct071(
                        getAct071ActionBundle(mTicket, stepAction.getStepCode(), stepAction.getProcessTkSeq(),stepAction.getProcessTkSeqTmp(), stepAction.isCurrentStep(), false
                        )
                    );
                }else if(stepAction.isCurrentStep()) {
                    if( isStartEndActionExecution(ticketStep, ticketCtrl)
                        || isOneTouchActionExecution(ticketStep, ticketCtrl)
                    ){
                        mView.showAlert(
                            hmAux_Trans.get("alert_start_action_ttl"),
                            hmAux_Trans.get("alert_start_action_confirm"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mView.callAct071(
                                        getAct071ActionBundle(mTicket, stepAction.getStepCode(), stepAction.getProcessTkSeq(), stepAction.getProcessTkSeqTmp(), stepAction.isCurrentStep(), true )
                                    );
                                }
                            },
                            true
                        );
                    }else if (ConstantBaseApp.SYS_STATUS_PROCESS.equals(ticketCtrl.getCtrl_status())){
                        mView.showAlert(
                            hmAux_Trans.get("alert_process_access_denied_ttl"),
                            hmAux_Trans.get("alert_process_started_in_server_msg")
                        );
                    } else{
                        //NÃO MAPEADO.
                    }
                }// //não faz nada, pois não tem ação
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_step_or_ctrl_not_found_ttl"),
                hmAux_Trans.get("alert_step_or_ctrl_not_found_msg")
            );
        }
    }

    private boolean isOneTouchActionExecution(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl ticketCtrl) {
        return  ConstantBaseApp.SYS_STATUS_PENDING.equals(ticketStep.getStep_status())
                && ConstantBaseApp.SYS_STATUS_PENDING.equals(ticketCtrl.getCtrl_status())
                && ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(ticketStep.getExec_type());
    }

    private boolean isStartEndActionExecution(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl ticketCtrl) {
        return ConstantBaseApp.SYS_STATUS_PROCESS.equals(ticketStep.getStep_status())
            && ConstantBaseApp.SYS_STATUS_PENDING.equals(ticketCtrl.getCtrl_status())
            && ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(ticketStep.getExec_type());
    }

    @Override
    public void defineApprovalFlow(final TK_Ticket mTicket, final StepApproval stepApproval) {
        final TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(),mTicket.getTicket_code(), stepApproval.getStepCode());
        final TK_Ticket_Ctrl ticketCtrl = getSelectedCtrlFromDb(mTicket.getTicket_prefix(),mTicket.getTicket_code(),stepApproval.getProcessTkSeq(),stepApproval.getStepCode());
        //
        if (ticketStep != null && ticketCtrl != null) {
            if (isDoneOrWaitingSync(ticketStep.getStep_status())) {
                defineApprovalMode(stepApproval, ticketCtrl);
            } else {
                if (isDoneOrWaitingSync(ticketCtrl.getCtrl_status())) {
                    defineApprovalMode(stepApproval, ticketCtrl);
                } else if (stepApproval.isCurrentStep()) {
                    if (isStartEndActionExecution(ticketStep, ticketCtrl)
                            || isOneTouchActionExecution(ticketStep, ticketCtrl)) {
                        mView.showAlert(
                                hmAux_Trans.get("alert_start_approval_ttl"),
                                hmAux_Trans.get("alert_start_approval_confirm"),
                                new DialogInterface.OnClickListener() {
                                    @ Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        defineApprovalMode(stepApproval, ticketCtrl);
                                    }
                                },
                                true);
                    } else if (ConstantBaseApp.SYS_STATUS_PROCESS.equals(ticketCtrl.getCtrl_status())) {
                        defineApprovalMode(stepApproval, ticketCtrl);
                    }
                }
            }
        } else {
            mView.showAlert(
                    hmAux_Trans.get("alert_step_or_ctrl_not_found_ttl"),
                    hmAux_Trans.get("alert_step_or_ctrl_not_found_msg"));
        }


    }

    private void defineApprovalMode(StepApproval stepApproval, TK_Ticket_Ctrl ticketCtrl) {
        switch (stepApproval.getApprovalType()){
            case ConstantBaseApp.TK_PIPELINE_APPROVAL_GET_MATERIAL:
            case ConstantBaseApp.TK_PIPELINE_APPROVAL_RETURN_MATERIAL:
                mView.callact075ForApproval(ticketCtrl.getStep_code(), ticketCtrl.getTicket_seq(), stepApproval.isCurrentStep(), false);
                break;
            case ConstantBaseApp.TK_PIPELINE_APPROVAL_OPERATIONAL:
                mView.callact075ForApproval(ticketCtrl.getStep_code(), ticketCtrl.getTicket_seq(), stepApproval.isCurrentStep(), true);
                break;
        }
    }

    @Override
    public void prepareRejectionDialog(TK_Ticket mTicket, StepApproval stepApproval) {
        TK_Ticket_Step ticketStep = getSelectedStep(mTicket.getTicket_prefix(),mTicket.getTicket_code(), stepApproval.getStepCode());
        TK_Ticket_Ctrl ticketCtrl = getSelectedCtrlFromDb(mTicket.getTicket_prefix(),mTicket.getTicket_code(),stepApproval.getProcessTkSeq(),stepApproval.getStepCode());
        //
        if(ticketCtrl != null && ticketCtrl.getApproval() != null) {
            showRejectionList(ticketCtrl);
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_step_or_ctrl_not_found_ttl"),
                hmAux_Trans.get("alert_step_or_ctrl_not_found_msg")
            );
        }
    }

    private void showRejectionList(TK_Ticket_Ctrl ctrl) {
        PipelineRejectionListDialog rejectionListDialog =
            new PipelineRejectionListDialog(
                context,
                hmAux_Trans,
                ctrl.getApproval().getApproval_question(),
                hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_REJECTED),
                ctrl.getRejection()
            );
        //
        rejectionListDialog.show();
    }

    private boolean isDoneOrWaitingSync(String status) {
        return ConstantBaseApp.SYS_STATUS_DONE.equals(status)
                || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(status) ;
    }

    private TK_Ticket_Step getSelectedStep(int ticketPrefix, int ticketCode, int stepCode) {
        return ticketStepDao.getByString(
            new TK_Ticket_Step_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticketPrefix,
                ticketCode,
                stepCode
            ).toSqlQuery()
        );
    }

    @Nullable
    private TK_Ticket_Ctrl getSelectedCtrlFromStep(ArrayList<TK_Ticket_Ctrl> ctrls, int ticketPrefix, int ticketCode, int ticketSeq, int stepCode) {
        for (TK_Ticket_Ctrl ctrl : ctrls) {
            if( ctrl.getTicket_prefix() == ticketPrefix
                && ctrl.getTicket_code() == ticketCode
                && ctrl.getTicket_seq() == ticketSeq
                && ctrl.getStep_code() == stepCode
            ){
                return ctrl;
            }
        }
        return null;
    }
    private TK_Ticket_Ctrl getSelectedCtrlFromDb(int ticketPrefix, int ticketCode, int ticketSeq, int stepCode) {
        return ticketCtrlDao.getByString(
            new TK_Ticket_Ctrl_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticketPrefix,
                ticketCode,
                ticketSeq,
                stepCode
            ).toSqlQuery()
        );
    }

    private Bundle getAct071CtrlBundleInfo(TK_Ticket mTicket, TK_Ticket_Ctrl ctrl) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ctrl.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, ctrl.getTicket_code());
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ, ctrl.getTicket_seq());
        bundle.putString(TK_TicketDao.TICKET_ID, mTicket.getTicket_id());
        bundle.putString(TK_TicketDao.TYPE_PATH, mTicket.getType_path());
        bundle.putString(TK_TicketDao.TYPE_DESC, mTicket.getType_desc());
        //TODO REVE SE NECESSARIO AINDA
        //bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN, mTicket.getCheckin_user() == null || !ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(mTicket.getCheckin_user())));
        return bundle;
    }

    //region 22/07/2020 - NOVO_TICKET
    @Override
    public void getStepsList(TK_Ticket mTicket) {
        List<TK_Ticket_Step> ticketsStep = ticketStepDao.query(
            new Sql_Act070_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mTicket.getTicket_prefix(),
                mTicket.getTicket_code()
            ).toSqlQuery()
        );
        //
        if(ticketsStep != null){
            ArrayList<BaseStep> baseSteps = generateStepperSource(mTicket,ticketsStep);
            if(baseSteps != null){
                mView.setStepperSource(baseSteps);
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_no_step_found_ttl"),
                hmAux_Trans.get("alert_no_step_found_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressedClicked(mView.getRequestingAct());
                    }
                },
                false
            );
        }
    }

    private ArrayList<BaseStep> generateStepperSource(TK_Ticket mTicket, List<TK_Ticket_Step> ticketStepList) {
        ArrayList<BaseStep> baseSteps = new ArrayList<>();
        for (TK_Ticket_Step ticketStep : ticketStepList) {
            StepMain stepMain = new StepMain(
                ticketStep.getStep_code(),
                ticketStep.getStep_desc(),
                getStepNum(ticketStep.getStep_order(),ticketStep.getStep_order_seq()),
                ticketStep.getForecast_start(),
                ticketStep.getForecast_end(),
                ticketStep.getStep_start_date(),
                ticketStep.getStep_end_date(),
                ticketStep.getExec_type(),
                ticketStep.getStep_status(),
                isCurrentStep(ticketStep.getStep_order(),mTicket.getCurrent_step_order()),
                ticketStep.getScan_serial() == 1,
                ticketStep.getAllow_new_obj()== 1,
                ticketStep.getMove_next_step()== 1
            );
            //
            baseSteps.add(stepMain);
            //Seta indice onde adapter precisa ser posicionado.
            if( mView.getCurrentStepFirstPosition() == -1
                && stepMain.isCurrentStep()
                && !ConstantBaseApp.SYS_STATUS_DONE.equals(stepMain.getStepStatus())
            ){
                mView.setCurrentStepFirstPosition(baseSteps.indexOf(stepMain));
            }
        }
        //
        if(!footerExists(baseSteps)){
            baseSteps.add(
                new StepFooter(
                    mTicket.getTicket_status(),
                    getFooterDate(mTicket)
                )
            );
        }
        return baseSteps;
    }

    /**
     * Metodo que retona forecast_date ou close_date baseado no status do ticket.
     * @param mTicket
     * @return
     */
    private String getFooterDate(TK_Ticket mTicket) {
        return ConstantBaseApp.SYS_STATUS_DONE.equals(mTicket.getTicket_status())
            ? mTicket.getClose_date()
            : mTicket.getForecast_date();
    }

    private boolean footerExists(ArrayList<BaseStep> baseSteps) {
        return baseSteps.size() > 1
            && (baseSteps.get(baseSteps.size() -1) instanceof StepFooter);
    }

    private boolean isCurrentStep(int step_order, Integer current_step_order) {
        return current_step_order != null && step_order == current_step_order;
    }

    private String getStepNum(int step_order, Integer step_order_seq) {
       // return step_order + (step_order_seq == null ? "" : "." + step_order_seq);
        return TK_Ticket_Step.getStepNumFormatted(step_order,step_order_seq);
    }

    @Override
    public void generateStepCtrlsContent(TK_Ticket mTicket, ArrayList<BaseStep> source , int mainPosition ) {
        ArrayList<BaseStep> stepsCtrls = generateStepCtrls(mTicket, (StepMain) source.get(mainPosition));
        if(stepsCtrls != null && stepsCtrls.size() > 0){
            addSelectedStepProcessToSource(mTicket,source,mainPosition,stepsCtrls);
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_update_stepper_error_ttl"),
                hmAux_Trans.get("alert_no_items_to_add_msg")
            );
        }
    }

    @Override
    public void updateStepOpenStates(ArrayList<BaseStep> sources, int mainPosition, boolean isShown) {
        if(sources.get(mainPosition) instanceof StepMain) {
            ((StepMain) sources.get(mainPosition)).setStepOpen(!isShown);
        }
    }

    private void addSelectedStepProcessToSource(TK_Ticket tkTicket, ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        String ticket_status = tkTicket.getTicket_status();
        int targetIdx = mainPosition + 1;
        //
        try{
            //Se status do ticket for cancelado ou rejeitado, nem processa botões de ação
            if(!isBadStatus(ticket_status)) {
                //Adiciona btn de checkin se houver necessidade
                addCheckinCtrl(source, mainPosition, stepsCtrls);
                /*//Adiciona os obj / processos
                source.addAll(targetIdx,stepsCtrls);*/
                //Adiciona btn de add processo se houver necessidade
                addNewProcess(source, mainPosition, stepsCtrls);
                //Adiciona btn de checkout se houver necessidade
                addCheckOutCtrl(tkTicket,source, mainPosition, stepsCtrls);
                //
            }
            //
            if(source.addAll(targetIdx,stepsCtrls)){
                mView.informAdapterInsertRange(targetIdx,stepsCtrls.size());
            }
        }catch (Exception e){
            mView.showAlert(
                hmAux_Trans.get("alert_update_stepper_error_ttl"),
                e.getMessage()
            );
            e.printStackTrace();
        }
    }

    private void addCheckOutCtrl(TK_Ticket tkTicket, ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        StepMain stepMain = (StepMain) source.get(mainPosition);
        //
        if( !isDoneOrWaitingSync(stepMain.getStepStatus())
            && !isBadStatus(stepMain.getStepStatus())
            //&& ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepMain.getStepType())
            && !stepMain.isMove_next_step()
        ){
            BaseStep firstPlannedObj = getFirstPlannedObj(stepsCtrls);
            if(firstPlannedObj != null){
                if(firstPlannedObj instanceof StepAbstractProcess){
                    String processStatus = ((StepAbstractProcess) firstPlannedObj).getProcessStatus();
                    if(isDoneOrWaitingSync(processStatus)){
                        if(isNotFormProcessOrFormNoPendencie(tkTicket, (StepAbstractProcess)firstPlannedObj)){
                            StepProcessBtn stepProcessBtn = new StepProcessBtn(
                                stepMain.getStepCode(),
                                hmAux_Trans.get("process_check_out_btn"),
                                ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_CHECKOUT
                            );
                            //
                            stepsCtrls.add(stepsCtrls.size(),stepProcessBtn);
                        }
                    }
                }
            }
        }
    }

    private boolean isNotFormProcessOrFormNoPendencie(TK_Ticket tkTicket, StepAbstractProcess firstPlannedObj) {
        if(!(firstPlannedObj instanceof StepForm)){
            return true;
        }
        //
        return !hasFormWithGpsPendency(tkTicket,firstPlannedObj);
    }

    /**
     * LUCHE - 09/09/2020
     * <p></p>
     * Metodo que verifica se existe algum form com pendencia de GPS no step.
     * @param tkTicket Ticket
     * @param firstPlannedObj Obj planeajdo
     * @return - Verdadeiro houver alguma form do step aguardando pendencia GPS.
     */
    private boolean hasFormWithGpsPendency(TK_Ticket tkTicket, StepAbstractProcess firstPlannedObj) {
        List<GE_Custom_Form_Data> formWithGpsPendency = formDataDao.query(
            new Sql_Act070_007(
                tkTicket.getCustomer_code(),
                tkTicket.getTicket_prefix(),
                tkTicket.getTicket_code(),
                firstPlannedObj.getStepCode()
            ).toSqlQuery()
        );
        //
        return formWithGpsPendency != null && formWithGpsPendency.size() > 0;
    }

    @Nullable
    private BaseStep getFirstPlannedObj(ArrayList<BaseStep> stepsCtrls) {
        for (BaseStep stepsCtrl : stepsCtrls) {
            if(stepsCtrl instanceof StepAction){
                if(((StepAction) stepsCtrl).isProcessPlanned()){
                    return stepsCtrl;
                }
            }else if(stepsCtrl instanceof StepApproval){
                if(((StepApproval) stepsCtrl).isProcessPlanned()){
                    return stepsCtrl;
                }
            }else if(stepsCtrl instanceof StepNone){
                if(((StepNone) stepsCtrl).isProcessPlanned()){
                    return stepsCtrl;
                }
            }else if(stepsCtrl instanceof StepForm){
                if(((StepForm) stepsCtrl).isProcessPlanned()){
                    return stepsCtrl;
                }
            }
        }
        return null;
    }
    //TODO VERICAR NA WEB SOBRE QUANDO LIBERAR O BOTÃO NO CASO DO ONE_TOUCH
    private void addNewProcess(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        StepMain stepMain = (StepMain) source.get(mainPosition);
        if(
            /*!ConstantBaseApp.SYS_STATUS_DONE.equals(stepMain.getStepStatus())
             && !ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(stepMain.getStepStatus())*/
           !isDoneOrWaitingSync(stepMain.getStepStatus())
           && !isBadStatus(stepMain.getStepStatus())
           && ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate())
           && stepMain.isCurrentStep()
           && stepMain.isAllow_new_obj()
        ){
            StepProcessBtn stepNewProcess =
                new StepProcessBtn(
                    stepMain.getStepCode(),
                    hmAux_Trans.get("process_add_new_btn"),
                    ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_ADD_NEW
                );
            //
            stepsCtrls.add(stepsCtrls.size(),stepNewProcess);
        }
    }

    private void addCheckinCtrl(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        StepMain stepMain = (StepMain) source.get(mainPosition);
        if( !isBadStatus(stepMain.getStepStatus())
            && stepMain.isCurrentStep()
            && (ConstantBaseApp.SYS_STATUS_PENDING.equals(stepMain.getStepStatus()) || ConstantBaseApp.SYS_STATUS_PROCESS.equals(stepMain.getStepStatus()))
            && ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepMain.getStepType())
            && !ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate())
        ){
            StepProcessBtn stepNewProcess =
                new StepProcessBtn(
                    stepMain.getStepCode(),
                    hmAux_Trans.get("process_check_in_btn"),
                    ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_CHECKIN
            );
            //
            stepsCtrls.add(0,stepNewProcess);
        }
    }

    @Override
    public void removeStepCtrlsContent(ArrayList<BaseStep> sources, int mainPosition) {
        ArrayList<BaseStep> removeCtrlList = new ArrayList<>();
        int targetIdx = mainPosition + 1;
        for (int i = targetIdx; i < sources.size(); i++) {
            BaseStep auxStep = sources.get(i);
            if(isNotMainOrFooterStep(auxStep)){
                removeCtrlList.add(auxStep);
            }else{
                break;
            }
        }
        if(removeCtrlList != null && removeCtrlList.size() > 0){
            try {
                if (sources.removeAll(removeCtrlList)) {
                    mView.informAdapterRemoveRange(targetIdx, removeCtrlList.size());
                } else {
                    mView.showAlert(
                        hmAux_Trans.get("alert_update_stepper_error_ttl"),
                        hmAux_Trans.get("alert_error_on_remove_items_msg")
                    );
                }
            }catch (Exception e){
                mView.showAlert(
                    hmAux_Trans.get("alert_update_stepper_error_ttl"),
                    e.getMessage()
                );
                e.printStackTrace();
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_update_stepper_error_ttl"),
                hmAux_Trans.get("alert_no_items_to_remove_msg")
            );
        }
        //

    }

    private boolean isNotMainOrFooterStep(BaseStep auxStep) {
        return !(auxStep instanceof StepMain) && !(auxStep instanceof StepFooter);
    }

    private ArrayList<BaseStep> generateStepCtrls(TK_Ticket mTicket, StepMain stepMain) {
        ArrayList<BaseStep> stepsCtrls = new ArrayList<>();
        //
        ArrayList<TK_Ticket_Ctrl> tkStepCtrls =
            (ArrayList<TK_Ticket_Ctrl>) ticketCtrlDao.query(
                    new Sql_Act070_002(
                        mTicket.getCustomer_code(),
                        mTicket.getTicket_prefix(),
                        mTicket.getTicket_code(),
                        stepMain.getStepCode()
                    ).toSqlQuery()
        );
        //
        if(tkStepCtrls == null || tkStepCtrls.size() == 0) {
            mView.showAlert(
                hmAux_Trans.get("alert_no_process_found_ttl"),
                hmAux_Trans.get("alert_no_process_found_msg")
            );
        }else{
            for (TK_Ticket_Ctrl tkStepCtrl : tkStepCtrls) {
                //
                switch (tkStepCtrl.getCtrl_type()){
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                        StepAction stepAction = createStepAction(mTicket,stepMain, tkStepCtrl);
                        stepsCtrls.add(stepAction);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                        StepApproval stepApproval = createStepApproval(mTicket, stepMain, tkStepCtrl);
                        stepsCtrls.add(stepApproval);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_NONE:
                        StepNone stepNone = createStepNone(mTicket,stepMain, tkStepCtrl);
                        stepsCtrls.add(stepNone);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                        StepForm stepChecklist = createStepForm(mTicket,stepMain, tkStepCtrl);
                        stepsCtrls.add(stepChecklist);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                    default:
                        break;
                }
            }
            //
        }
        return stepsCtrls;
    }

    private StepForm createStepForm(TK_Ticket mTicket, StepMain stepMain, TK_Ticket_Ctrl tkStepCtrl) {
        StepForm stepForm = new StepForm();
        stepForm.setStepCode(tkStepCtrl.getStep_code());
        stepForm.setStepDescription(
            getStepFormDescription(tkStepCtrl)
        );
        stepForm.setStepType(stepMain.getStepType());
        stepForm.setProcessTkSeq(tkStepCtrl.getTicket_seq());
        stepForm.setProcessStatus(tkStepCtrl.getCtrl_status());
        stepForm.setCurrentStep(stepMain.isCurrentStep());
        stepForm.setStepAlreadyCheckedIn(ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate()));
        stepForm.setProcessPlanned(tkStepCtrl.getObj_planned() == 1);
        //PK DO APP
        stepForm.setProcessTkSeqTmp(tkStepCtrl.getTicket_seq_tmp());
        stepForm.setProductDesc(tkStepCtrl.getProduct_desc());
        stepForm.setSerialId(tkStepCtrl.getSerial_id());
        stepForm.setStartDate(tkStepCtrl.getCtrl_start_date());
        stepForm.setEndDate(tkStepCtrl.getCtrl_end_date());
        stepForm.setEndUser(tkStepCtrl.getCtrl_end_user_name());
        stepForm.setPartnerDesc(tkStepCtrl.getPartner_desc());
        stepForm.setProductDifferentThanTicket(tkStepCtrl.getProduct_code() != null && mTicket.getOpen_product_code() != tkStepCtrl.getProduct_code());
        stepForm.setSerialDifferentThanTicket(tkStepCtrl.getSerial_code() != null && mTicket.getOpen_serial_code() != tkStepCtrl.getSerial_code());
        return stepForm;
    }

    private String getStepFormDescription(TK_Ticket_Ctrl tkStepCtrl) {
        return
            tkStepCtrl.getForm() != null && tkStepCtrl.getForm().getCustom_form_desc() != null
                ? tkStepCtrl.getForm().getCustom_form_desc()
                : hmAux_Trans.get("process_checklist_tll");
    }

    @NonNull
    private StepApproval createStepApproval(TK_Ticket mTicket, StepMain stepMain, TK_Ticket_Ctrl tkStepCtrl) {
        StepApproval stepApproval = new StepApproval();
        stepApproval.setStepCode(tkStepCtrl.getStep_code());
        stepApproval.setStepType(stepMain.getStepType());
        stepApproval.setProcessTkSeq(tkStepCtrl.getTicket_seq());
        stepApproval.setProcessStatus(tkStepCtrl.getCtrl_status());
        stepApproval.setApprovalType(tkStepCtrl.getApproval() != null ? tkStepCtrl.getApproval().getApproval_type() : null);
        stepApproval.setApprovalQuestion(tkStepCtrl.getApproval()  != null ? tkStepCtrl.getApproval().getApproval_question() : null);
        stepApproval.setApprovalStatus(tkStepCtrl.getApproval() != null ? tkStepCtrl.getApproval().getApproval_status() : null);
        stepApproval.setApprovalComment(tkStepCtrl.getApproval() != null ? tkStepCtrl.getApproval().getApproval_comments() : null);
        stepApproval.setPartnerDesc(tkStepCtrl.getPartner_desc());
        stepApproval.setStartDate(tkStepCtrl.getCtrl_start_date());
        stepApproval.setEndDate(tkStepCtrl.getCtrl_end_date());
        stepApproval.setEndUser(tkStepCtrl.getCtrl_end_user_name());
        stepApproval.setHasRejection(tkStepCtrl.getRejection() != null && tkStepCtrl.getRejection().size() > 0 );
        stepApproval.setCurrentStep(stepMain.isCurrentStep());
        stepApproval.setStepAlreadyCheckedIn(ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate()));
        stepApproval.setProcessPlanned(tkStepCtrl.getObj_planned() == 1);
        //
        stepApproval.setProductDifferentThanTicket(tkStepCtrl.getProduct_code() != null && mTicket.getOpen_product_code() != tkStepCtrl.getProduct_code());
        stepApproval.setProductDifferentThanTicket(tkStepCtrl.getSerial_code() != null && mTicket.getOpen_serial_code() != tkStepCtrl.getSerial_code());
        return stepApproval;
    }

    @NonNull
    private StepAction createStepAction(TK_Ticket mTicket, StepMain stepMain, TK_Ticket_Ctrl tkStepCtrl) {
        StepAction stepAction = new StepAction();
        stepAction.setStepCode(tkStepCtrl.getStep_code());
        stepAction.setStepDescription(hmAux_Trans.get("process_action_tll"));
        stepAction.setStepType(stepMain.getStepType());
        stepAction.setProcessTkSeq(tkStepCtrl.getTicket_seq());
        stepAction.setProcessStatus(tkStepCtrl.getCtrl_status());
        stepAction.setCurrentStep(stepMain.isCurrentStep());
        stepAction.setStepAlreadyCheckedIn(ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate()));
        stepAction.setProcessPlanned(tkStepCtrl.getObj_planned() == 1);
        //PK DO APP
        stepAction.setProcessTkSeqTmp(tkStepCtrl.getTicket_seq_tmp());
        stepAction.setProductDesc(tkStepCtrl.getProduct_desc());
        stepAction.setSerialId(tkStepCtrl.getSerial_id());
        stepAction.setStartDate(tkStepCtrl.getCtrl_start_date());
        stepAction.setEndDate(tkStepCtrl.getCtrl_end_date());
        stepAction.setEndUser(tkStepCtrl.getCtrl_end_user_name());
        stepAction.setPartnerDesc(tkStepCtrl.getPartner_desc());
        stepAction.setProductDifferentThanTicket(tkStepCtrl.getProduct_code() != null && mTicket.getOpen_product_code() != tkStepCtrl.getProduct_code());
        stepAction.setSerialDifferentThanTicket(tkStepCtrl.getSerial_code() != null && mTicket.getOpen_serial_code() != tkStepCtrl.getSerial_code());
        return stepAction;
    }

    @NonNull
    private StepNone createStepNone(TK_Ticket mTicket, StepMain stepMain, TK_Ticket_Ctrl tkStepCtrl) {
        StepNone stepNone = new StepNone();
        //Dados do StepAbs
        stepNone.setStepCode(tkStepCtrl.getStep_code());
        stepNone.setStepDescription(hmAux_Trans.get("process_none_tll"));
        stepNone.setStepType(stepMain.getStepType());
        stepNone.setProcessStatus(tkStepCtrl.getCtrl_status());
        stepNone.setCurrentStep(stepMain.isCurrentStep());
        stepNone.setStepAlreadyCheckedIn(ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate()));
        stepNone.setProcessPlanned(tkStepCtrl.getObj_planned() == 1);
        stepNone.setProcessTkSeq(tkStepCtrl.getTicket_seq());
        //Dados do proprio componente
        stepNone.setProductDesc(tkStepCtrl.getProduct_desc());
        stepNone.setSerialId(tkStepCtrl.getSerial_id());
        stepNone.setStartDate(tkStepCtrl.getCtrl_start_date());
        stepNone.setEndDate(tkStepCtrl.getCtrl_end_date());
        stepNone.setEndUser(tkStepCtrl.getCtrl_end_user_name());
        stepNone.setPartnerDesc(tkStepCtrl.getPartner_desc());
        stepNone.setProductDifferentThanTicket(tkStepCtrl.getProduct_code() != null && mTicket.getOpen_product_code() != tkStepCtrl.getProduct_code());
        stepNone.setSerialDifferentThanTicket(tkStepCtrl.getSerial_code() != null && mTicket.getOpen_serial_code() != tkStepCtrl.getSerial_code());
        return stepNone;
    }

    /**
     * Verifica se é um dos status que para o ticket do jeito que esta, cancelled e rejeited
     * @param status
     * @return
     */
    public boolean isBadStatus(String status){
        return
            ConstantBaseApp.SYS_STATUS_CANCELLED.equals(status)
            || ConstantBaseApp.SYS_STATUS_REJECTED.equals(status);
    }

    //endregion

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct) {
            case ConstantBaseApp.ACT017:
                mView.callAct017();
                break;
            case ConstantBaseApp.ACT035:
                mView.callAct035();
                break;
            case ConstantBaseApp.ACT014:
            case ConstantBaseApp.ACT069:
                mView.callAct069();
                break;
            case ConstantBaseApp.ACT076:
                mView.callAct076();
                break;
            default:
                mView.callAct068();
        }
    }

    @Override
    public boolean verifyProductForForm() {
        if(ToolBox_Inf.hasFormProductOutdate(context)){
            if (ToolBox_Con.isOnline(context)) {
                callWsSync();
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    private void callWsSync() {
        mView.setWsProcess(WS_Sync.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_sync_ttl"),
                hmAux_Trans.get("progress_sync_msg")
        );
        //
        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
        bundle.putLong(Constant.GS_PRODUCT_CODE, 0);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
