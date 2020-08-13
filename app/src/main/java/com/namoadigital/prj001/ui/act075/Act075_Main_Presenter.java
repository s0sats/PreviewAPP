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
import com.namoadigital.prj001.sql.TK_Ticket_Product_Sql_003;
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
                mTicketCtrl.setCtrl_end_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                mTicketCtrl.setCtrl_end_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
                mTicketCtrl.setCtrl_end_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
                mTicketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
                setCheckInOutWhenOneTouchStep(ticketStep, mTicketCtrl);
                //
                checkCloseStepForWaitingSync(ticketStep, mTicketCtrl);
                mTicketCtrl.setApproval(ticketApproval);
                mTicketCtrl.setUpdate_required(1);
                ticketStep.setUpdate_required(1);
                tkTicket.setUpdate_required(1);
                DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
                if (!daoObjReturn.hasError()) {
                    executeTicketSaveProcess();
                } else {
                    mView.showMsg(
                            hmAux_Trans.get("alert_error_on_approve_ttl"),
                            hmAux_Trans.get("alert_error_on_approve_msg")
                    );
                }
            }
        }
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
            return ticket.getAllow_step_approval() == 1 ? true : false;
        } else {
            if (!getWithdrawStatus(ticket)) {
                return userHasMaterialWithdrawApprovalAccess(ticket.getMain_user());
            } else {
                return ticket.getAllow_step_approval() == 1 ? true : false;
            }
        }
    }

    @Override
    public List<TK_Ticket_Product> getTicketProductListForApproval(int mTkPrefix, int mTkCode) {
        return ticketProductDao.query(
                new TK_Ticket_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTkPrefix,
                        mTkCode
                ).toSqlQuery()
        );

    }

    @Override
    public void saveAppliedProduct(TK_Ticket ticket, ArrayList<TK_Ticket_Product> getmValues) {
        ticket.setUpdate_required_product(1);
        ticketDao.addUpdate(ticket);
        updateTicketProducts(getmValues);
        executeTicketSaveProcess();
    }

    @Override
    public List<TK_Ticket_Product> getTicketProductList(TK_Ticket tkTicket) {
        List<TK_Ticket_Product> productList = new ArrayList<>();
        if (getWithdrawStatus(tkTicket)) {
            for (TK_Ticket_Product product : tkTicket.getProduct()) {

                if (product.getQty() > 0) {
                    productList.add(product);
                }

            }
            return productList;
        } else {
            return tkTicket.getProduct();
        }

    }

    private void updateTicketProducts(ArrayList<TK_Ticket_Product> ticket_products) {
        for (TK_Ticket_Product ticket_product : ticket_products) {
            ticketProductDao.addUpdate(ticket_product);
        }
    }


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
}
