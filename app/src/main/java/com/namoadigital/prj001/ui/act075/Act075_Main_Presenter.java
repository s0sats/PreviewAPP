package com.namoadigital.prj001.ui.act075;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao;
import com.namoadigital.prj001.dao.TK_Ticket_Approval_RejectionDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_ProductDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Approval_Rejection;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Product_Save;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_TK_Ticket_Product_Save;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Rejection_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Product_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_GET_MATERIAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_OPERATIONAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_RETURN_MATERIAL;

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
    public void saveproduct(int scn, ArrayList<TK_Ticket_Product> tk_ticket_products) {
        mView.setWsProcess(WS_TK_Ticket_Product_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_product_save_ticket_ttl"),
                hmAux_Trans.get("dialog_product_save_ticket_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_TK_Ticket_Product_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.SCN, scn);
        bundle.putSerializable(TK_Ticket_ProductDao.TABLE, tk_ticket_products);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public boolean getWithdrawStatus(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = ticketApprovalDao.query(
                new TK_Ticket_Approval_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticket.getTicket_prefix(),
                        ticket.getTicket_code()
                ).toSqlQuery());

        if (approvals != null) {
            for (TK_Ticket_Approval approval :
                    approvals) {
                if (APPROVAL_GET_MATERIAL.equalsIgnoreCase(approval.getApproval_type())) {
                    if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(approval.getApproval_status())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean getAppliedStatus(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = ticketApprovalDao.query(
                new TK_Ticket_Approval_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticket.getTicket_prefix(),
                        ticket.getTicket_code()
                ).toSqlQuery());

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
    public void saveApproval(TK_Ticket_Approval ticketApproval, boolean isApproved, String approveComments) {
        if (isApproved) {
            ticketApproval.setApproval_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            ticketApproval.setApproval_comments(approveComments);
            TK_Ticket selectedTicket = getTicket(ticketApproval.getCustomer_code(), ticketApproval.getTicket_prefix(), ticketApproval.getTicket_code());
            selectedTicket.setUpdate_required(1);
            TK_Ticket_Step selectedStep = getSelectedStep(ticketApproval.getTicket_prefix(), ticketApproval.getTicket_code(), ticketApproval.getStep_code());
            selectedStep.setUpdate_required(1);
            selectedStep.setStep_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            TK_Ticket_Ctrl selectedCtrl = getSelectedCtrl(ticketApproval.getTicket_prefix(), ticketApproval.getTicket_code(), ticketApproval.getTicket_seq(), ticketApproval.getStep_code());
            selectedCtrl.setUpdate_required(1);
            selectedCtrl.setCtrl_end_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            selectedCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            DaoObjReturn daoTicketReturn = ticketDao.addUpdate(selectedTicket);
            DaoObjReturn daoStepReturn = ticketStepDao.addUpdate(selectedStep);
            DaoObjReturn daoCtrlReturn = tkTicketCtrlDao.addUpdate(selectedCtrl);
            DaoObjReturn daoApprovalReturn = ticketApprovalDao.addUpdate(ticketApproval);
            if (!daoTicketReturn.hasError()
                    &&  !daoStepReturn.hasError()
                    &&  !daoCtrlReturn.hasError()
                    &&  !daoApprovalReturn.hasError()
            ) {
                executeTicketSaveProcess();
            } else {
                mView.showMsg(
                        hmAux_Trans.get("alert_error_on_approve_ttl"),
                        hmAux_Trans.get("alert_error_on_approve_msg")
                );
            }
        } else {
            TK_Ticket_Approval_Rejection rejection = getRejectionPKObj(ticketApproval);
            rejection.setRejection_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
            rejection.setRejection_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
            rejection.setRejection_date(ToolBox_Inf.getDateHourStr());

            List<TK_Ticket_Approval_Rejection> query = ticketApprovalRejectionDao.query(new TK_Ticket_Approval_Rejection_Sql_001(
                    rejection.getCustomer_code(),
                    rejection.getTicket_prefix(),
                    rejection.getTicket_code(),
                    rejection.getTicket_seq(),
                    rejection.getStep_code()).toSqlQuery()
            );
            rejection.setSeq(query.size() + 1);
            rejection.setRejection_comments(approveComments);
            DaoObjReturn daoObjReturn = ticketApprovalRejectionDao.addUpdate(rejection);
            if (!daoObjReturn.hasError()) {
                executeTicketSaveProcess();
            } else {
                mView.showMsg(
                        hmAux_Trans.get("alert_error_on_reject_ttl"),
                        hmAux_Trans.get("alert_error_on_reject_msg")
                );
            }
        }
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
        TK_Ticket_Approval approval = getTicketApproval(customer_code, mTkPrefix, mTkCode, mTkSeq, stepCode);
        if (approval != null && APPROVAL_OPERATIONAL.equalsIgnoreCase(approval.getApproval_type())) {
            return userHasOperationalApprovalAccess(mTkPrefix, mTkCode);
        } else {
            return userHasMaterialApprovalAccess(ticket.getMain_user());
        }
    }

    public boolean userHasMaterialApprovalAccess(Integer main_user) {
        if (main_user != null) {
            if (ToolBox_Con.getPreference_User_Code(context).equalsIgnoreCase(main_user.toString())) {
                return true;
            }
        }

        return false;
    }

    public boolean userHasOperationalApprovalAccess(int mTkPrefix, int mTkCode) {
        return false;
    }

    private String getResultSaveMsgFormmated(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        if (actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)) {
            return actReturn.getRetStatus();
        } else {
            return actReturn.isProcessError() ? actReturn.getProcessStatus() + "\n" + actReturn.getProcessMsg() : actReturn.getRetStatus() + "\n" + actReturn.getRetMsg();
        }
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
}
