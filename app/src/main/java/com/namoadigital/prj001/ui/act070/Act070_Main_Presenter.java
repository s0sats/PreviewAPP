package com.namoadigital.prj001.ui.act070;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_TK_Ticket_Checkin;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.Sql_Act070_001;
import com.namoadigital.prj001.sql.Sql_Act070_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAbstractProcess;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
    public void prepareSyncProcess(TK_Ticket mTicket) {
        //Verifica se há necessidade de envidar dados para o server.
        if(checkUpdateRequiredNeeds(mTicket)){
            executeTicketSaveProcess();
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

    private void executeTicketSaveProcess() {
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
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public boolean checkOnlySyncNeeds(TK_Ticket mTicket) {
        return
                mTicket != null
                && (mTicket.getUpdate_required() == 1
                    || mTicket.getSync_required() == 1
                );
    }

    public boolean checkUpdateRequiredNeeds(TK_Ticket mTicket) {
        return mTicket != null
            && (mTicket.getUpdate_required() == 1
                || isTicketInTokenFile(mTicket.getTicket_prefix(),mTicket.getTicket_code())
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
                //
                for (WS_TK_Ticket_Save.TicketSaveActReturn actReturn : checkinReturns) {
                    String ticketCode = actReturn.getPrefix() + "." + actReturn.getCode();
                    //
                    if (!auxResult.containsKey(ticketCode)
                        || (auxResult.containsKey(ticketCode)
                        &&  !ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(actReturn.getRetStatus())
                            )
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        auxResult.put(ticketCode, getResultSaveMsgFormmated(actReturn));
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
                        resultList.add(ticketNextIdx, hmAux);
                        ticketNextIdx++;
                    } else {
                        resultList.add(hmAux);
                    }
                }
                //
                mView.showResult(resultList, ticketResult);
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
                        auxResult.put(ticketCode, getResultMsgFormmated(actReturn));
                        auxAction.put(ticketCode, String.valueOf(actReturn.getCheckinAction()));
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
                mView.showResult(resultList, ticketResult);
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

    @Override
    public boolean isTicketInTokenFile(int ticket_prefix, int ticket_code) {
        ArrayList<TK_Ticket> ticketInToken = ToolBox_Inf.getTicketsWithinToken(ToolBox_Con.getPreference_Customer_Code(context));
        if(ticketInToken != null && ticketInToken.size() > 0){
            for (TK_Ticket tkTicket : ticketInToken) {
                if( tkTicket.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                    && tkTicket.getTicket_prefix() == ticket_prefix
                    && tkTicket.getTicket_code() == ticket_code
                ){
                    return true;
                }
            }
        }
        //
        return false;
    }

    private Bundle getAct071CtrlBundleInfo(TK_Ticket mTicket, TK_Ticket_Ctrl ctrl) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ctrl.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, ctrl.getTicket_code());
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ, ctrl.getTicket_seq());
        bundle.putString(TK_TicketDao.TICKET_ID, mTicket.getTicket_id());
        bundle.putString(TK_TicketDao.TYPE_PATH, mTicket.getType_path());
        bundle.putString(TK_TicketDao.TYPE_DESC, mTicket.getType_desc());
        bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN, mTicket.getCheckin_user() == null || !ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(mTicket.getCheckin_user())));
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
            ArrayList<BaseStep> baseSteps = generateStepperSource(ticketsStep, mTicket.getCurrent_step_order(), mTicket.getForecast_date());
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

    private ArrayList<BaseStep> generateStepperSource(List<TK_Ticket_Step> ticketStepList, Integer current_step_order, String forecast_date) {
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
                isCurrentStep(ticketStep.getStep_order(),current_step_order),
                ticketStep.getScan_serial() == 1,
                ticketStep.getAllow_new_obj()== 1,
                ticketStep.getMove_next_step()== 1
            );
            //
            baseSteps.add(stepMain);
            //Seta indice onde adapter precisa ser posicionado.
            if(mView.getCurrentStepFirstPosition() == -1 && stepMain.isCurrentStep()){
                mView.setCurrentStepFirstPosition(baseSteps.indexOf(stepMain));
            }
        }
        //
        if(!footerExists(baseSteps)){
            baseSteps.add(
                new StepFooter(forecast_date)
            );
        }
        return baseSteps;
    }

    private boolean footerExists(ArrayList<BaseStep> baseSteps) {
        return baseSteps.size() > 1
            && (baseSteps.get(baseSteps.size() -1) instanceof StepFooter);
    }

    private boolean isCurrentStep(int step_order, Integer current_step_order) {
        return current_step_order != null && step_order == current_step_order;
    }

    private String getStepNum(int step_order, Integer step_order_seq) {
        return step_order + (step_order_seq == null ? "" : "." + step_order_seq);
    }

    @Override
    public void generateStepCtrlsContent(TK_Ticket mTicket, ArrayList<BaseStep> source , int mainPosition ) {
        ArrayList<BaseStep> stepsCtrls = generateStepCtrls(mTicket, (StepMain) source.get(mainPosition));
        if(stepsCtrls != null && stepsCtrls.size() > 0){
            addSelectedStepProcessToSource(source,mainPosition,stepsCtrls);
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

    private void addSelectedStepProcessToSource(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        int targetIdx = mainPosition + 1;
        try{
            //Adiciona btn de checkin se houver necessidade
            addCheckinCtrl(source,mainPosition,stepsCtrls);
            /*//Adiciona os obj / processos
            source.addAll(targetIdx,stepsCtrls);*/
            //Adiciona btn de add processo se houver necessidade
            addNewProcess(source,mainPosition,stepsCtrls);
            //Adiciona btn de checkout se houver necessidade
            addCheckOutCtrl(source,mainPosition,stepsCtrls);
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

    private void addCheckOutCtrl(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        StepMain stepMain = (StepMain) source.get(mainPosition);
        //
        if( !ConstantBaseApp.SYS_STATUS_DONE.equals(stepMain.getStepStatus())
            && ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepMain.getStepType())
        ){
            BaseStep firstPlannedObj = getFirstPlannedObj(stepsCtrls);
            if(firstPlannedObj != null){
                if(firstPlannedObj instanceof StepAbstractProcess){
                    String processStatus = ((StepAbstractProcess) firstPlannedObj).getProcessStatus();
                    if(ConstantBaseApp.SYS_STATUS_DONE.equals(processStatus)){
                        StepProcessBtn stepProcessBtn = new StepProcessBtn(
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

    @Nullable
    private BaseStep getFirstPlannedObj(ArrayList<BaseStep> stepsCtrls) {
        for (BaseStep stepsCtrl : stepsCtrls) {
            if(stepsCtrl instanceof StepAction){
                if(((StepAction) stepsCtrl).isProcessPlanned()){
                    return stepsCtrl;
                }
            }
        }
        return null;
    }

    private void addNewProcess(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        StepMain stepMain = (StepMain) source.get(mainPosition);
        if(stepMain.isCurrentStep() && stepMain.isAllow_new_obj()){
            StepProcessBtn stepNewProcess =
                new StepProcessBtn(
                    hmAux_Trans.get("process_add_new_btn"),
                    ConstantBaseApp.TK_PIPELINE_STEP_NEW_PROCESS_TYPE_ADD_NEW
                );
            //
            stepsCtrls.add(stepsCtrls.size(),stepNewProcess);
        }
    }

    private void addCheckinCtrl(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        StepMain stepMain = (StepMain) source.get(mainPosition);
        if(stepMain.isCurrentStep()
            && (ConstantBaseApp.SYS_STATUS_PENDING.equals(stepMain.getStepStatus()) || ConstantBaseApp.SYS_STATUS_PROCESS.equals(stepMain.getStepStatus()))
            && ConstantBaseApp.TK_PIPELINE_STEP_TYPE_START_END.equals(stepMain.getStepType())
            && !ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate())
        ){
            StepProcessBtn stepNewProcess =
                new StepProcessBtn(
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
                        StepAction stepAction = new StepAction();
                        stepAction.setStepCode(tkStepCtrl.getStep_code());
                        stepAction.setStepDescription(hmAux_Trans.get("process_action_tll"));
                        stepAction.setProductDesc(tkStepCtrl.getProduct_desc());
                        stepAction.setSerialId(tkStepCtrl.getSerial_id());
                        //stepAction.setSiteDesc(tkStepCtrl.getSite_desc());
                        stepAction.setStartDate(tkStepCtrl.getCtrl_start_date());
                        stepAction.setEndDate(tkStepCtrl.getCtrl_end_date());
                        stepAction.setEndUser(tkStepCtrl.getCtrl_end_user_name());
                        stepAction.setPartnerDesc(tkStepCtrl.getPartner_desc());
                        stepAction.setStepType(stepMain.getStepType());
                        stepAction.setProcessStatus(tkStepCtrl.getCtrl_status());
                        stepAction.setCurrentStep(stepMain.isCurrentStep());
                        stepAction.setStepAlreadyCheckedIn(ToolBox_Inf.hasConsistentValueString(stepMain.getCheckInDate()));
                        stepAction.setProcessPlanned(tkStepCtrl.getObj_planned() == 1);
                        //
                        stepsCtrls.add(stepAction);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                        StepApproval stepApproval = new StepApproval();
                        stepApproval.setStepCode(tkStepCtrl.getStep_code());
                        stepApproval.setStepType(stepMain.getStepType());
                        stepApproval.setProcessStatus(tkStepCtrl.getCtrl_status());
                        stepApproval.setApprovalType(tkStepCtrl.getApproval() != null ? tkStepCtrl.getApproval().getApproval_type() : null);
                        stepApproval.setApprovalQuestion(tkStepCtrl.getApproval()  != null ? tkStepCtrl.getApproval().getApproval_question() : null);
                        stepApproval.setApprovalStatus(tkStepCtrl.getApproval() != null ? tkStepCtrl.getApproval().getApproval_status() : null);
                        stepApproval.setApprovalComment(tkStepCtrl.getApproval() != null ? tkStepCtrl.getApproval().getApproval_comments() : null);
                        stepApproval.setPartnerDesc(tkStepCtrl.getPartner_desc());
                        stepApproval.setStartDate(tkStepCtrl.getCtrl_start_date());
                        stepApproval.setEndDate(tkStepCtrl.getCtrl_end_date());
                        stepApproval.setEndUser(tkStepCtrl.getCtrl_start_user_name());
                        stepApproval.setHasRejection(tkStepCtrl.getRejection() != null && tkStepCtrl.getRejection().size() > 0 );
                        stepsCtrls.add(stepApproval);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                    default:
                        break;
                }
            }
            //
        }
        return stepsCtrls;
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
            case ConstantBaseApp.ACT069:
            default:
                mView.callAct069();
        }
    }

}
