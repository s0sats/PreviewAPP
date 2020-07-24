package com.namoadigital.prj001.ui.act070;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.T_TK_Ticket_Checkin_Obj_Env;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Checkin;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_TK_Ticket_Checkin;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.Sql_Act070_001;
import com.namoadigital.prj001.sql.Sql_Act070_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Action_V;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Generic;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Measure_V;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Super;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Act070_Main_Presenter implements Act070_Main_Contract.I_Presenter {
    private Context context;
    private Act070_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    private TK_Ticket_StepDao ticketStepDao;
    private TK_Ticket_CtrlDao ticketCtrlDao;
    private MD_PartnerDao mdPartnerDao;

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
        //
        this.mdPartnerDao = new MD_PartnerDao(
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
        //Inverte a ordenação dos controles
        Collections.reverse(ticket.getCtrl());
        //
        return ticket;
    }

    @Override
    public boolean validateBundleParams(int mTkPrefix, int mTkCode) {
        return mTkPrefix > 0 && mTkCode > 0;
    }

    @Override
    public boolean getReadOnlyDefinition(TK_Ticket mTicket) {
        return isOtherCheckIn(mTicket.getCheckin_user())
            || isReadOnlyStatus(mTicket.getTicket_status())
            || missingProfile();
    }

    private boolean isOtherCheckIn(Integer checkin_user) {
        if (checkin_user == null) {
            return false;
        } else {
            return !ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(checkin_user));
        }
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
    public boolean hideCancelCheckin(TK_Ticket mTicket) {
        return isReadOnlyStatus(mTicket.getTicket_status()) || missingProfile();
    }

    @Override
    public String getFormattedCheckinInfo(String checkin_date, String checkin_user_name) {
        String sFormatted = ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(checkin_date),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );
        //
        sFormatted += " (" + checkin_user_name + ")";
        return sFormatted;
    }

    @Override
    public String getFormattedDoneInfo(String close_date, String close_user_name) {
        return getFormattedCheckinInfo(close_date, close_user_name);
    }

    @Override
    public boolean checkFilterDisable(ArrayList<TK_Ticket_Ctrl> ctrls) {
        //Se vazio ou apénas um controle, esconde filtro
        if (ctrls == null || ctrls.size() <= 1) {
            return true;
        }
        //Se algum ctrl status diferente de DONE, OU WAITING_SYNC NAO esconde filtro
        for (TK_Ticket_Ctrl ctrl : ctrls) {
            if (isReadOnlyStatus(ctrl.getCtrl_status())
            ) {
                return false;
            }
        }
        //Se chegou aqui, esconde filtro
        return true;
    }

    @Override
    public ArrayList<TK_Ticket_Ctrl_Super> generateCtrlActions(TK_Ticket mTicket, LinearLayout llActions, boolean filterOn) {
        ArrayList<TK_Ticket_Ctrl_Super> ctrlSupers = new ArrayList<>();
        if (mTicket != null && mTicket.getCtrl() != null && mTicket.getCtrl().size() > 0) {
            for (TK_Ticket_Ctrl ctrl : mTicket.getCtrl()) {
                TK_Ticket_Ctrl_Super auxCtrl = null;
                switch (ctrl.getCtrl_type()) {
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                        auxCtrl = configMeasureView(mTicket, ctrl);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                        auxCtrl = configActionView(mTicket, ctrl);
                        break;
                    default:
                        auxCtrl = configSuperView(mTicket, ctrl);
                }
                //Aplica filtro se ativo.
                if (filterOn) {
                    auxCtrl.applyFilterVisibility();
                }
                //
                ctrlSupers.add(auxCtrl);
                llActions.addView(auxCtrl);
            }
        }
        //
        return ctrlSupers;
    }

    private TK_Ticket_Ctrl_Measure_V configMeasureView(TK_Ticket mTicket, TK_Ticket_Ctrl ctrl) {
        //
        TK_Ticket_Ctrl_Measure_V  measureCtrlView = new TK_Ticket_Ctrl_Measure_V(
            context,
            mTicket.getOpen_product_code(),
            mTicket.getOpen_serial_code(),
            ctrl,
            hmAux_Trans,
            null
        );
        //
        return measureCtrlView;
    }

    private TK_Ticket_Ctrl_Generic configSuperView(TK_Ticket mTicket, TK_Ticket_Ctrl ctrl) {
        TK_Ticket_Ctrl_Generic ctrlGeneric = new TK_Ticket_Ctrl_Generic(
            context,
            mTicket.getOpen_product_code(),
            mTicket.getOpen_serial_code(),
            ctrl,
            hmAux_Trans,
            null
        );
        //
        return ctrlGeneric;
    }

    private TK_Ticket_Ctrl_Action_V configActionView(final TK_Ticket mTicket, final TK_Ticket_Ctrl ctrl) {
        TK_Ticket_Ctrl_Action_V actionCtrlView = new TK_Ticket_Ctrl_Action_V(
            context,
            mTicket.getOpen_product_code(),
            mTicket.getOpen_serial_code(),
            ctrl,
            hmAux_Trans,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = getAct071CtrlBundleInfo(mTicket, ctrl);
                    mView.callAct071(bundle);
                }
            },
            new TK_Ticket_Ctrl_Action_V.TK_Ticket_Ctrl_Action_I() {
                @Override
                public boolean checkPartnerProfile(Integer partnerCode) {
                    return hasPartnerProfile(partnerCode);
                }
            }
        );
        //
        return actionCtrlView;
    }


    private boolean hasPartnerProfile(Integer partner_code) {
        if (partner_code == null) {
            return true;
        }
        //
        MD_Partner partner = mdPartnerDao.getByString(
            new MD_Partner_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                partner_code
            ).toSqlQuery()
        );
        //
        if (partner != null && partner.getCustomer_code() > 0) {
            return true;
        }
        //
        return false;
    }

    @Override
    public void prepareSyncProcess(TK_Ticket mTicket) {
        //Verifica se há necessidade de envidar dados para o server.
        if(checkUpdateRequiredNeeds(mTicket)){
            executeTicketSaveProcess();
        }else{
            executeSyncProcess(mTicket.getTicket_prefix(), mTicket.getTicket_code());
        }
    }

    private void executeSyncProcess(int ticket_prefix, int ticket_code) {
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
            bundle.putString(TK_TicketDao.TICKET_PREFIX, getTicketSyncPkFormat(ticket_prefix, ticket_code));
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);

        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private String getTicketSyncPkFormat(int ticket_prefix, int ticket_code) {
        return ToolBox_Con.getPreference_Customer_Code(context) + "|" + ticket_prefix + "|" + ticket_code;
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
        //return mTicket != null && mTicket.getUpdate_required() == 0 && mTicket.getSync_required() == 1;
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
    public boolean setCheckInData(TK_Ticket tkTicket) {
        DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
        //
        return !daoObjReturn.hasError();
    }

    @Override
    public void executeCheckin(TK_Ticket tkTicket, boolean checkIn) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Checkin.class.getName());
            //
            mView.showPD(
                checkIn ? hmAux_Trans.get("dialog_ticket_checkin_ttl") : hmAux_Trans.get("dialog_ticket_checkin_cancel_ttl"),
                checkIn ? hmAux_Trans.get("dialog_ticket_checkin_start") : hmAux_Trans.get("dialog_ticket_checkin_cancel_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Checkin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(WS_TK_Ticket_Checkin.WS_PARAM_TICKET_CHECKIN_LIST, getCheckInList(tkTicket, checkIn));
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            if (checkIn) {
                //informar checkInOffiline
                mView.showAlert(
                    hmAux_Trans.get("alert_ticket_checkin_offline_ttl"),
                    hmAux_Trans.get("alert_ticket_checkin_offline_msg")
                );
                //
                mView.callRefreshUi();
            } else {
                ToolBox_Inf.showNoConnectionDialog(context);
            }
        }
    }

    private ArrayList<T_TK_Ticket_Checkin_Obj_Env> getCheckInList(TK_Ticket tkTicket, boolean checkIn) {
        ArrayList<T_TK_Ticket_Checkin_Obj_Env> checkIns = new ArrayList<>();
        //
        T_TK_Ticket_Checkin_Obj_Env checkInAux = new T_TK_Ticket_Checkin_Obj_Env();
        //
        checkInAux.setCustomer_code(tkTicket.getCustomer_code());
        checkInAux.setTicket_prefix(tkTicket.getTicket_prefix());
        checkInAux.setTicket_code(tkTicket.getTicket_code());
        checkInAux.setTicket_scn(tkTicket.getScn());
        checkInAux.setCheckin(checkIn ? 1 : 0);
        //
        checkIns.add(checkInAux);
        //
        return checkIns;
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
            //mView.showAlert();
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
                isCurrentStep(ticketStep.getStep_order(),current_step_order)
            );
            //
            baseSteps.add(stepMain);
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
            addSelectedStepProcessToSourcer(source,mainPosition,stepsCtrls);
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

    private void addSelectedStepProcessToSourcer(ArrayList<BaseStep> source, int mainPosition, ArrayList<BaseStep> stepsCtrls) {
        int targetIdx = mainPosition + 1;
        try{
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
                        //
                        stepsCtrls.add(stepAction);
                        break;
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
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
