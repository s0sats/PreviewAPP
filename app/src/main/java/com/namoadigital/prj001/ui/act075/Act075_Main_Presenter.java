package com.namoadigital.prj001.ui.act075;

import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_GET_MATERIAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_OPERATIONAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_RETURN_MATERIAL;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao;
import com.namoadigital.prj001.dao.TK_Ticket_Approval_RejectionDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_ProductDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Approval_Rejection;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act075_001;
import com.namoadigital.prj001.sql.Sql_Act075_002;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Rejection_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Product_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Product_Sql_003;
import com.namoadigital.prj001.sql.TK_Ticket_Product_Sql_004;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Act075_Main_Presenter implements Act075_Main_Contract.I_Presenter {
    private final TK_Ticket_StepDao ticketStepDao;
    private Context context;
    private Act075_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    private TK_Ticket_ProductDao ticketProductDao;
    private TK_Ticket_CtrlDao tkTicketCtrlDao;
    private TK_Ticket_ApprovalDao ticketApprovalDao;
    private TK_Ticket_Approval_RejectionDao ticketApprovalRejectionDao;
    private String ctrlStartDate = "";
    private Integer ctrlStartUser = -1;
    private String ctrlStartUserName= "" ;

    public Act075_Main_Presenter(Context context, Act075_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        this.ticketApprovalDao = new TK_Ticket_ApprovalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        this.ticketProductDao = new TK_Ticket_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        this.ticketStepDao = new TK_Ticket_StepDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        this.ticketApprovalRejectionDao = new TK_Ticket_Approval_RejectionDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        this.tkTicketCtrlDao = new TK_Ticket_CtrlDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    public TK_Ticket getTicket(long customer_code, int ticket_prefix, int ticket_code) {
        //
        return ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        customer_code,
                        ticket_prefix,
                        ticket_code
                ).toSqlQuery()
        );
    }

    @Override
    public TK_Ticket_Approval getTicketApproval(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq, int step_code) {
        return ticketApprovalDao.getByString(
                new TK_Ticket_Approval_Sql_001(
                        customer_code,
                        ticket_prefix,
                        ticket_code,
                        ticket_seq,
                        step_code
                ).toSqlQuery()
        );
    }

    @Override
    public TK_Ticket_Product getTicketProduct(long customer_code, int ticket_prefix, int ticket_code, int product_code) {
        //
        return ticketProductDao.getByString(
                new TK_Ticket_Product_Sql_001(
                        customer_code,
                        ticket_prefix,
                        ticket_code,
                        product_code
                ).toSqlQuery()
        );
    }

    @Override
    public boolean getWithdrawStatus(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = getTicketApprovals(ticket);

        if (approvals != null) {
            for (TK_Ticket_Approval approval :
                    approvals) {
                if (APPROVAL_GET_MATERIAL.equalsIgnoreCase(approval.getApproval_type())) {
                    TK_Ticket_Ctrl ctrl = tkTicketCtrlDao.getByString(
                            new TK_Ticket_Ctrl_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    approval.getTicket_prefix(),
                                    approval.getTicket_code(),
                                    approval.getTicket_seq(),
                                    approval.getStep_code()
                            ).toSqlQuery());
                    if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(ctrl.getCtrl_status())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<TK_Ticket_Approval> getTicketApprovals(TK_Ticket ticket) {
        return ticketApprovalDao.query(
                new TK_Ticket_Approval_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticket.getTicket_prefix(),
                        ticket.getTicket_code()
                ).toSqlQuery());
    }

    @Override
    public boolean getAppliedStatus(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = getTicketApprovals(ticket);

        if (approvals != null) {
            for (TK_Ticket_Approval approval :
                    approvals) {
                if (APPROVAL_RETURN_MATERIAL.equalsIgnoreCase(approval.getApproval_type())) {
                    if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(approval.getApproval_status())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public TK_Ticket_Step getSelectedStep(int mTkPrefix, int mTkCode, int mStepCode) {
        return ticketStepDao.getByString(
                new TK_Ticket_Step_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTkPrefix,
                        mTkCode,
                        mStepCode
                ).toSqlQuery()
        );
    }

    @Override
    public TK_Ticket_Ctrl getSelectedCtrl(int mTkPrefix, int mTkCode, int mTkSeq, int mStepCode) {
        return tkTicketCtrlDao.getByString(
                new TK_Ticket_Ctrl_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTkPrefix,
                        mTkCode,
                        mTkSeq,
                        mStepCode
                ).toSqlQuery()
        );
    }

    @Override
    public String getSelectedCtrlStatus(int mTkPrefix, int mTkCode, int mTkSeq, int mStepCode) {
        TK_Ticket_Ctrl ticketCtrl = tkTicketCtrlDao.getByString(
                new TK_Ticket_Ctrl_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTkPrefix,
                        mTkCode,
                        mTkSeq,
                        mStepCode
                ).toSqlQuery()
        );
        return ticketCtrl.getCtrl_status();
    }

    @Override
    public int getStepColor(TK_Ticket_Step ticketStep, boolean IsCurrentStep) {
        int stepColor = ContextCompat.getColor(context, R.color.namoa_color_pipeline_next_step);
        if (ConstantBaseApp.SYS_STATUS_DONE.equals(ticketStep.getStep_status())
                || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(ticketStep.getStep_status())
        ) {
            stepColor = ToolBox_Inf.getStatusColorV2(context, ticketStep.getStep_status());
        } else if (IsCurrentStep) {
            stepColor = ContextCompat.getColor(context, R.color.namoa_status_process);
        }
        return stepColor;
    }

    @Override
    public String getStepNumFormatted(TK_Ticket_Step ticketStep) {
        return
                ticketStep != null
                        ? TK_Ticket_Step.getStepNumFormatted(ticketStep.getStep_order(), ticketStep.getStep_order_seq())
                        : "";
    }

    @Override
    public String getStepDesc(TK_Ticket_Step ticketStep) {
        return ticketStep != null
                ? ticketStep.getStep_desc()
                : "";
    }

    @Override
    public boolean saveApproval(TK_Ticket_Approval ticketApproval, boolean isApproved, String approveComments) {
        if (!isApproved) {
            ticketApproval.setApproval_status(ConstantBaseApp.SYS_STATUS_REJECTED);
            TK_Ticket_Approval_Rejection rejection = getRejectionPKObj(ticketApproval);
            rejection.setRejection_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
            rejection.setRejection_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
            rejection.setRejection_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            //
            List<TK_Ticket_Approval_Rejection> query = ticketApprovalRejectionDao.query(new TK_Ticket_Approval_Rejection_Sql_001(
                    rejection.getCustomer_code(),
                    rejection.getTicket_prefix(),
                    rejection.getTicket_code(),
                    rejection.getTicket_seq(),
                    rejection.getStep_code()).toSqlQuery()
            );
            //
            rejection.setSeq(query.size() + 1);
            rejection.setRejection_comments(approveComments);
            ticketApprovalRejectionDao.addUpdate(rejection);
        } else {
            ticketApproval.setApproval_status(ConstantBaseApp.SYS_STATUS_DONE);
        }

        ticketApproval.setApproval_comments(approveComments);
        //
        TK_Ticket tkTicket = getTicket(ticketApproval.getCustomer_code(), ticketApproval.getTicket_prefix(), ticketApproval.getTicket_code());
        TK_Ticket_Ctrl mTicketCtrl = getSelectedCtrl(ticketApproval.getTicket_prefix(), ticketApproval.getTicket_code(), ticketApproval.getTicket_seq(), ticketApproval.getStep_code());

        int stepIdx = getStepIdx(mTicketCtrl, tkTicket);
        if (tkTicket != null
                && stepIdx > -1 && stepIdx <= tkTicket.getStep().size()
                && tkTicket.getStep().get(stepIdx) != null
        ) {
            int ctrlIdx = getCtrlIdx(mTicketCtrl, tkTicket.getStep().get(stepIdx));
            if (ctrlIdx > -1) {
                TK_Ticket_Step ticketStep = tkTicket.getStep().get(stepIdx);
                ticketStep.getCtrl().set(ctrlIdx, mTicketCtrl);
                //
                if (mTicketCtrl.getCtrl_start_date() == null
                        || mTicketCtrl.getCtrl_start_date().isEmpty()) {
                    mTicketCtrl.setCtrl_start_date(ctrlStartDate);
                }
                //
                if (mTicketCtrl.getCtrl_start_user() == null
                        || mTicketCtrl.getCtrl_start_user() < 0) {
                    mTicketCtrl.setCtrl_start_user(ctrlStartUser);
                }
                //
                if (mTicketCtrl.getCtrl_start_user_name() == null
                        || mTicketCtrl.getCtrl_start_user_name().isEmpty()) {
                    mTicketCtrl.setCtrl_start_user_name(ctrlStartUserName);
                }
                if(isApproved) {
                    mTicketCtrl.setCtrl_end_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    mTicketCtrl.setCtrl_end_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
                    mTicketCtrl.setCtrl_end_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
                }
                mTicketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
                setCheckInOutWhenOneTouchStep(ticketStep, mTicketCtrl);
                //
                checkCloseStepForWaitingSync(ticketStep, mTicketCtrl);
                mTicketCtrl.setApproval(ticketApproval);
                mTicketCtrl.setUpdate_required(1);
                ticketStep.setUpdate_required(1);
                tkTicket.setUpdate_required(1);
                DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
                return !daoObjReturn.hasError();
            }
        }
        return false;
    }

    @Override
    public void setStartCtrl() {
        ctrlStartDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
        ctrlStartUser = Integer.valueOf(ToolBox_Con.getPreference_User_Code(context));
        ctrlStartUserName = ToolBox_Con.getPreference_User_Code_Nick(context);
    }

    private int getStepIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket tkTicket) {
        for (int i = 0; i < tkTicket.getStep().size(); i++) {
            if (
                    tkTicket.getStep().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                            && tkTicket.getStep().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                            && tkTicket.getStep().get(i).getStep_code() == mTicketCtrl.getStep_code()
            ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * LUCHE - 23/03/2020
     * <p></p>
     * Metodo que retorna o indice do control que esta sendo alterado.
     * Na teoria, sempre retornará um valor, por o crl sempre existe.
     *
     * @param mTicketCtrl  Obj controle alterado pelo usr
     * @param tkTicketStep Obj Ticket Step ao qual o controle pertence
     * @return - Idx do ctrl ou -1 caso não encontre.
     */
    private int getCtrlIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket_Step tkTicketStep) {
        for (int i = 0; i < tkTicketStep.getCtrl().size(); i++) {
            if (
                    tkTicketStep.getCtrl().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                            && tkTicketStep.getCtrl().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                            && tkTicketStep.getCtrl().get(i).getTicket_seq() == mTicketCtrl.getTicket_seq()
                            && tkTicketStep.getCtrl().get(i).getStep_code() == mTicketCtrl.getStep_code()
            ) {
                return i;
            }
        }
        //
        return -1;
    }

    @Override
    public void executeTicketSaveProcess() {
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
            mView.showAlert(
                    hmAux_Trans.get("alert_offline_save_ttl"),
                    hmAux_Trans.get("alert_offline_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.callMoveOn();
                        }
                    },
                    false
            );
        }
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
                            && !ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(actReturn.getRetStatus())
                    )
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        if(actReturn.isProcessError()){
                            ticketResult = !actReturn.isProcessError();
                            auxResult.put(ticketCode, actReturn.getRetMsg());
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
                mView.showMsg(
                        hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                        hmAux_Trans.get("alert_none_ticket_returned_msg")
                );
            }
        } else {
            mView.showMsg(
                    hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                    hmAux_Trans.get("alert_none_ticket_returned_msg")
            );
        }
    }

    @Override
    public boolean hasApproveProfile(int mTkPrefix, int mTkCode, int mTkSeq, int stepCode) {
        long customer_code = ToolBox_Con.getPreference_Customer_Code(context);
        TK_Ticket ticket = getTicket(customer_code, mTkPrefix, mTkCode);
        //TK_Ticket_Approval approval = getTicketApproval(customer_code, mTkPrefix, mTkCode, mTkSeq, stepCode);
        if (ticket != null ) {
            return ticket.getAllow_step_approval() == 1 ? true : false;
        }else{
            return false;
        }
    }

    @Override
    public List<TK_Ticket_Product> getTicketProductListForApproval(TK_Ticket_Approval ticketApproval) {
        if(APPROVAL_GET_MATERIAL.equalsIgnoreCase(ticketApproval.getApproval_type())) {
            return ticketProductDao.query(
                    new TK_Ticket_Product_Sql_003(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            ticketApproval.getTicket_prefix(),
                            ticketApproval.getTicket_code()
                    ).toSqlQuery()
            );
        }
        return ticketProductDao.query(
                new TK_Ticket_Product_Sql_004(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticketApproval.getTicket_prefix(),
                        ticketApproval.getTicket_code()
                ).toSqlQuery()
        );
    }

    @Override
    public boolean saveAppliedProduct(TK_Ticket ticket, ArrayList<TK_Ticket_Product> getmValues) {
        ticketDao.query(new Sql_Act075_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticket.getTicket_prefix(),
                ticket.getTicket_code()
        ).toSqlQuery());

        DaoObjReturn daoObjReturn = ticketProductDao.addUpdate(getmValues, false);
        return !daoObjReturn.hasError();
    }


    @Override
    public void callWsSave() {
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
            mView.showAlert(
                    hmAux_Trans.get("alert_form_pendency_please_sync_ttl"),
                    hmAux_Trans.get("alert_form_pendency_please_sync_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.callMoveOn();
                        }
                    },
                    false
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

    @Override
    public List<TK_Ticket_Product> getTicketProductList(TK_Ticket tkTicket) {
        List<TK_Ticket_Product> productList = new ArrayList<>();
        if (getWithdrawStatus(tkTicket)) {
            for (TK_Ticket_Product product : tkTicket.getProduct()) {

                if (product.getQty()!= null && product.getQty() > 0
                || (product.getQty_planned()!= null && product.getQty_planned() > 0) ) {
                    productList.add(product);
                }

            }
            return productList;
        } else {
            return tkTicket.getProduct();
        }

    }

    @Override
    public void executeTicketSaveSyncFormProcess() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_ticket_update_ttl"),
                    hmAux_Trans.get("dialog_ticket_update_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Save.class);
            Bundle bundle = new Bundle();
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public boolean hasUpdatePendency(TK_Ticket tkTicket) {
        List<TK_Ticket> ticketList = ticketDao.query(new Sql_Act075_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                tkTicket.getTicket_prefix(),
                tkTicket.getTicket_code()
        ).toSqlQuery());
        //
        boolean hasPendencies;
        if(ticketList == null || ticketList.size() == 0){
            hasPendencies = false;
        }else{
            hasPendencies = true;
        }
        //
        return hasPendencies
                || ToolBox_Inf.hasFormGpsPendencyWithinTicket(context, tkTicket.getTicket_prefix(),tkTicket.getTicket_code())
                || ToolBox_Inf.isTicketInTokenFile(context, tkTicket.getTicket_prefix(),tkTicket.getTicket_code());
    }

    /**
     * BARRIONUEVO  30-11-2020
     *     Verifica se ha aprovacao de retirada de materiais para desabilitar a adicao/edicao
     * de materais
     * @param ticket
     * @return
     */
    @Override
    public boolean hasApprovalPendency(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = getTicketApprovals(ticket);

        if (approvals != null) {
            for (TK_Ticket_Approval approval :
                    approvals) {
                if (APPROVAL_GET_MATERIAL.equalsIgnoreCase(approval.getApproval_type())) {
                    if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(approval.getApproval_status())) {
                        TK_Ticket_Ctrl ctrl = tkTicketCtrlDao.getByString(
                                new TK_Ticket_Ctrl_Sql_001(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        approval.getTicket_prefix(),
                                        approval.getTicket_code(),
                                        approval.getTicket_seq(),
                                        approval.getStep_code()
                                ).toSqlQuery());
                        if (ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(ctrl.getCtrl_status())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    @Deprecated
    public boolean userHasMaterialWithdrawApprovalAccess(Integer main_user) {
        if (main_user != null) {
            if (ToolBox_Con.getPreference_User_Code(context).equalsIgnoreCase(main_user.toString())) {
                return true;
            }
        }
        return false;
    }

    private String getResultSaveMsgFormmated(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
//        if (actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)) {
//            return actReturn.getRetStatus();
//        } else {
        String retMsg = actReturn.getRetMsg();
        if(retMsg.contains("null")){
            retMsg = "";
        }
        return actReturn.isProcessError() ? actReturn.getProcessStatus() + "\n" + actReturn.getProcessMsg() : actReturn.getRetStatus() + "\n" + retMsg;
//        }
    }

    private TK_Ticket_Approval_Rejection getRejectionPKObj(TK_Ticket_Approval ticketApproval) {
        TK_Ticket_Approval_Rejection aux = new TK_Ticket_Approval_Rejection();
        aux.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        aux.setTicket_prefix(ticketApproval.getTicket_prefix());
        aux.setTicket_code(ticketApproval.getTicket_code());
        aux.setTicket_seq(ticketApproval.getTicket_seq());
        aux.setStep_code(ticketApproval.getStep_code());
        return aux;

    }

    private void setCheckInOutWhenOneTouchStep(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        if (ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(ticketStep.getExec_type())) {
            ticketStep.setStep_start_date(mTicketCtrl.getCtrl_start_date());
            ticketStep.setStep_start_user(mTicketCtrl.getCtrl_start_user());
            ticketStep.setStep_start_user_nick(mTicketCtrl.getCtrl_start_user_name());
        }
    }

    private void checkCloseStepForWaitingSync(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        int stepCtrlsFinalizedCounter = 0;
        for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
            if (ConstantBaseApp.SYS_STATUS_DONE.equals(ticketCtrl.getCtrl_status())
                    || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(ticketCtrl.getCtrl_status())
            ) {
                stepCtrlsFinalizedCounter++;
            }
        }
        //Se todos os ctrl estão finalizado e o step é one_touch ou for start_end com move_next_step,
        //faz checkout
        if (stepCtrlsFinalizedCounter == ticketStep.getCtrl().size()
                && ticketStep.getMove_next_step() == 1
        ) {
            ticketStep.setStep_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            ticketStep.setStep_end_date(mTicketCtrl.getCtrl_end_date());
            ticketStep.setStep_end_user(mTicketCtrl.getCtrl_end_user());
            ticketStep.setStep_end_user_nick(mTicketCtrl.getCtrl_end_user_name());
        }
    }

    /**
     * LUCHE - 11/09/2020
     * Metodo que define o titulo da tela baseado no act_profile ou tipo da aprovação
     *  act_profile
     *      1 = Produto
     *      2 = Aprovação
     * @param act_profile
     * @param mTkPrefix
     * @param mTkCode
     * @param mTkSeq
     * @param mStepCode
     * @return - Retorna a CHAVE a ser buscada no hmAux, ou seja, o txt_code.
     */
    @Override
    public String defineActTitle(int act_profile, int mTkPrefix, int mTkCode, int mTkSeq, int mStepCode) {
        if(act_profile == 1){
            return "act_ticket_product_ttl";
        }else{
            TK_Ticket_Approval ticketApproval = getTicketApproval(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix, mTkCode, mTkSeq, mStepCode);
            //
            if(ticketApproval != null && ticketApproval.getApproval_type() != null){
                switch (ticketApproval.getApproval_type()){
                    case APPROVAL_GET_MATERIAL:
                        return "act_ticket_get_material_approval_ttl";
                    case APPROVAL_RETURN_MATERIAL:
                        return "act_ticket_return_material_approval_ttl";
                    case APPROVAL_OPERATIONAL:
                        return "act_ticket_operational_approval_ttl";
                    default:
                        return "act_ticket_approval_ttl";
                }
            }
            //Se algo der erro poe label generico
            return"act_ticket_approval_ttl";
        }
    }

    @Override
    public boolean verifyProductForForm(int ticket_prefix, int ticket_code) {
        if(ToolBox_Inf.hasFormProductOutdate(context, ticket_prefix, ticket_code)){
            if (ToolBox_Con.isOnline(context)) {
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
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    @Override
    public void executeSerialSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_serial_save_ttl"),
                    hmAux_Trans.get("progress_serial_save_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Save.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showAlert(
                    hmAux_Trans.get("alert_offline_save_ttl"),
                        hmAux_Trans.get("alert_offline_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.callMoveOn();
                        }
                    },
                    false
            );
        }
    }

    @Override
    public void processWsSerialSavelReturn(HMAux hmAux) {
        if (!hmAux.isEmpty() && hmAux.size() > 0) {
            ArrayList<HMAux> hmAuxList = new ArrayList<>();
            for (Map.Entry<String, String> item : hmAux.entrySet()) {
                HMAux aux = new HMAux();
                /**
                 * [0] - Product_code
                 * [1] - Serial ID
                 */
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();
                String productInfo = getFormatedProductInfo(getMdProduct(ToolBox_Inf.convertStringToInt(pk[0])));
                //O mHmAux abaixo é copia da act011, e foi modificado pra essa tela.
//                HMAux mHmAux = new HMAux();
//                mHmAux.put("label", "" + productInfo + " - " + pk[1]);
//                mHmAux.put("type", "SERIAL");
//                mHmAux.put("status", status);
//                mHmAux.put("final_status", productInfo + " - " + pk[1] + " / " + status);
                //
                aux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("serial_lbl"));
                aux.put(Generic_Results_Adapter.LABEL_ITEM_1, productInfo + " - " + pk[1] );
                aux.put(Generic_Results_Adapter.VALUE_ITEM_1, status);
                //
                if (!ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(status)) {
                    //Só colocado dentro da lista pois o metodo addResultList requer uma lista.
                    hmAuxList.add(aux);
                }
            }
            if(hmAuxList.size() > 0){
                mView.addResultList(hmAuxList);
            }
        }
    }

    @Override
    /**
     * LUCHE - 01/07/2021
     * <p></p>
     * Modificado metodo para valida somente os status editaveis. O requisito de bloquear via
     * usr focus foi definido erroneamente.
     */
    public boolean isEditable(String status) {
        return ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(status)
               ||  ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(status);
    }

    private String getFormatedProductInfo(MD_Product mdProduct) {
        if (mdProduct != null) {
            return mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc();
        } else {
            return "";
        }
    }


    private MD_Product getMdProduct(int product_code) {
        MD_Product md_product;
        MD_ProductDao md_productDao = new MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(
                        ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        md_product = md_productDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
        return md_product;
    }

    @Override
    public ArrayList<Integer> getProductCodeList(List<TK_Ticket_Product> tkTicketProducts) {
        ArrayList<Integer> productCodeList = new ArrayList<>();
        //
        for (TK_Ticket_Product tkTicketProduct : tkTicketProducts) {
            productCodeList.add(tkTicketProduct.getProduct_code());
        }
        return productCodeList;
    }
}
