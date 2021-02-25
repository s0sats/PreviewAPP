package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Ticket_Checkin_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Checkin_Obj_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Checkin_Rec;
import com.namoadigital.prj001.model.T_TK_Ticket_WS_Return;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Checkin;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Checkin extends IntentService {

    public static final String WS_PARAM_TICKET_CHECKIN_LIST = "WS_TICKET_CHECKIN_LIST";
    public static final String ERROR_MSG_TICKET_NOT_FOUND = "TICKET_NOT_FOUND";
    public static final String ERROR_MSG_INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String ERROR_MSG_INVALID_STATUS_TO_CHECKIN = "INVALID_STATUS_TO_CHECKIN";
    public static final String ERROR_MSG_INVALID_STATUS_TO_CANCEL_CHECKIN = "INVALID_STATUS_TO_CANCEL_CHECKIN";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_ticket_checkin";
    private Gson gson;
    private TK_TicketDao ticketDao;
    private ArrayList<T_TK_Ticket_Checkin_Obj_Env> ticketsToCheckin;

    public WS_TK_Ticket_Checkin() {
        super("WS_TK_Ticket_Checkin");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            gson = new GsonBuilder().serializeNulls().create();
            //
            ticketDao = new TK_TicketDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
            );
            //
            ticketsToCheckin = (ArrayList<T_TK_Ticket_Checkin_Obj_Env>) bundle.getSerializable(WS_PARAM_TICKET_CHECKIN_LIST);
            //
            processTicketCheckin();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_TK_Ticket_Checkin.completeWakefulIntent(intent);
        }

    }

    private void processTicketCheckin() throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        if(ticketsToCheckin == null || ticketsToCheckin.size() == 0){
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_ticket_to_chekin"), "", "0");
        }
        //
        T_TK_Ticket_Checkin_Env env = new T_TK_Ticket_Checkin_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));
        env.setTicket(ticketsToCheckin);
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_CHECKIN,
            gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        T_TK_Ticket_Checkin_Rec rec = gson.fromJson(
            resultado,
            T_TK_Ticket_Checkin_Rec.class
        );
        //
        if (
            !ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                1,
                1)
                ||
                !ToolBox_Inf.processoOthersError(
                    getApplicationContext(),
                    getResources().getString(R.string.generic_error_lbl),
                    rec.getError_msg())
        ) {
            return;
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_processing_data"), "", "0");
        //
        processTicketCheckinReturn(rec);

    }

    private void processTicketCheckinReturn(T_TK_Ticket_Checkin_Rec rec) {
        if (ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(rec.getSave())
            || ConstantBaseApp.MAIN_RESULT_OK_DUP.equalsIgnoreCase(rec.getSave())
        ) {
            if (rec.getTicket() != null && rec.getTicket().size() > 0) {
                ArrayList<TicketCheckinActReturn> actReturns = new ArrayList<>();
                //
                for (T_TK_Ticket_WS_Return ticketReturn : rec.getTicket()) {
                    TicketCheckinActReturn actReturn = getActReturn(ticketReturn);
                    updateTicketReturned(ticketReturn, actReturn);
                    actReturns.add(actReturn);
                }
                //
                ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "CLOSE_ACT",
                    hmAux_Trans.get("generic_process_finalized_msg"),
                    new HMAux(),
                    gson.toJson(actReturns),
                    "0");
            } else {
                ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    hmAux_Trans.get("msg_no_ticket_data_returned"),
                    new HMAux(),
                    "",
                    "0");
            }
        } else {
            //COMO TRATAR, SERÁ QUE EXISTE ESSE OUTRO STATUS
            ToolBox.sendBCStatus(
                getApplicationContext(),
                "ERROR_1",
                hmAux_Trans.get("msg_ticket_checkin_error") + rec.getSave(),
                new HMAux(),
                "",
                "0");
        }

    }

    private TicketCheckinActReturn getActReturn(T_TK_Ticket_WS_Return ticketReturn) {
        TicketCheckinActReturn aReturn = new TicketCheckinActReturn(
            ticketReturn.getCustomer_code(),
            ticketReturn.getTicket_prefix(),
            ticketReturn.getTicket_code(),
            ticketReturn.getScn(),
            getCheckinAction(ticketReturn),
            ticketReturn.getRet_status(),
            ticketReturn.getRet_msg()
        );
        //
        return aReturn;
    }

    private int getCheckinAction(T_TK_Ticket_WS_Return ticketReturn) {
        for (T_TK_Ticket_Checkin_Obj_Env checkinObjEnv : ticketsToCheckin) {
            if (
                checkinObjEnv.getCustomer_code() == ticketReturn.getCustomer_code()
                    && checkinObjEnv.getTicket_prefix() == ticketReturn.getTicket_prefix()
                    && checkinObjEnv.getTicket_code() == ticketReturn.getTicket_code()
            ) {
                return checkinObjEnv.getCheckin();
            }
        }
        //Se -1 ferrou, o item retornado não estava no envio.
        return -1;
    }

    private void updateTicketReturned(T_TK_Ticket_WS_Return ticketReturn, TicketCheckinActReturn actReturn) {
        TK_TicketDao ticketDao = new TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        if (ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(ticketReturn.getRet_status())) {
            if (ticketReturn.getTicket() != null) {
                TK_Ticket tkTicket = ticketReturn.getTicket();
                tkTicket.setPK();
                //TODO REVE SE MOVER PARA O STEP
                //tkTicket.updateLocalImagesPathIfExists();
                DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
                //oq fazer no erro?
                if (daoObjReturn.hasError()) {
                    actReturn.processError = true;
                    actReturn.processStatus = ConstantBaseApp.SYS_STATUS_ERROR;
                    actReturn.processMsg = hmAux_Trans.get("msg_error_on_update_ticket");
                }
            }
        }
        //ELSE DEVERIA SER ERRO, SE HOUVER OUTRO STATUS DEFINIR COMO TRATAR.
        else {
            //Seta tradução da msg de error
            //actReturn.retMsg = hmAux_Trans.get(ticketReturn.getError_msg());
            actReturn.retMsg = ticketReturn.getRet_msg();
            //
            switch (ticketReturn.getRet_msg()) {
                case ERROR_MSG_TICKET_NOT_FOUND:
                    break;
                case ERROR_MSG_INVALID_STATUS_TO_CHECKIN:
                case ERROR_MSG_INVALID_STATUS_TO_CANCEL_CHECKIN:
                    TK_Ticket tkTicket = ticketReturn.getTicket();
                    if (ticketReturn.getTicket() != null) {
                        tkTicket.setPK();
                        //TODO REVE SE MOVER PARA O STEP
                        //tkTicket.updateLocalImagesPathIfExists();
                    } else {
                        tkTicket = ticketDao.getByString(
                            new TK_Ticket_Sql_001(
                                ticketReturn.getCustomer_code(),
                                ticketReturn.getTicket_prefix(),
                                ticketReturn.getTicket_code()
                            ).toSqlQuery()
                        );
                        //
                        if (tkTicket != null) {
                            //Desfaz a ação feita
                            if (actReturn.checkinAction == 1) {
                                tkTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_PENDING);
                            } else {
                                tkTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_PROCESS);
                            }
                            tkTicket.setToken(null);
                            tkTicket.setUpdate_required(0);
                        } else {
                            actReturn.processError = true;
                            actReturn.processStatus = ConstantBaseApp.SYS_STATUS_ERROR;
                            actReturn.processMsg = hmAux_Trans.get("msg_error_on_update_ticket");
                        }
                    }
                    //
                    DaoObjReturn daoObjReturn = ticketDao.addUpdate(tkTicket);
                    //
                    if (daoObjReturn.hasError()) {
                        actReturn.processError = true;
                        actReturn.processStatus = ConstantBaseApp.SYS_STATUS_ERROR;
                        actReturn.processMsg = hmAux_Trans.get("msg_error_on_update_ticket");
                    }
                    break;
            }
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_process_finalized_msg");
        translist.add("msg_no_ticket_to_chekin");
        translist.add("msg_error_on_update_ticket");
        translist.add("msg_ticket_checkin_error");
        translist.add("msg_no_ticket_data_returned");
        translist.add("generic_processing_data");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            getApplicationContext(),
            mModule_Code,
            mResource_Name
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
            getApplicationContext(),
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
            translist);
    }

    public static class TicketCheckinActReturn {
        private int customer_code = -1;
        private int prefix = -1;
        private int code = -1;
        private Integer scn = -1;
        private String retStatus = "";
        private String retMsg = "";
        private boolean processError = false;
        private String processStatus = "";
        private String processMsg = "";
        private int checkinAction = -1;

        public TicketCheckinActReturn() {
        }

        public TicketCheckinActReturn(int customer_code, int prefix, int code, Integer scn, int checkinAction, String retStatus, String retMsg) {
            this.customer_code = customer_code;
            this.prefix = prefix;
            this.code = code;
            this.scn = scn;
            this.checkinAction = checkinAction;
            this.retStatus = retStatus;
            this.retMsg = retMsg;
        }

        public int getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(int customer_code) {
            this.customer_code = customer_code;
        }

        public int getPrefix() {
            return prefix;
        }

        public void setPrefix(int prefix) {
            this.prefix = prefix;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Integer getScn() {
            return scn;
        }

        public void setScn(Integer scn) {
            this.scn = scn;
        }

        public String getRetStatus() {
            return retStatus;
        }

        public void setRetStatus(String retStatus) {
            this.retStatus = retStatus;
        }

        public String getRetMsg() {
            return retMsg;
        }

        public void setRetMsg(String retMsg) {
            this.retMsg = retMsg;
        }

        public boolean isProcessError() {
            return processError;
        }

        public void setProcessError(boolean processError) {
            this.processError = processError;
        }

        public String getProcessStatus() {
            return processStatus;
        }

        public void setProcessStatus(String processStatus) {
            this.processStatus = processStatus;
        }

        public String getProcessMsg() {
            return processMsg;
        }

        public void setProcessMsg(String processMsg) {
            this.processMsg = processMsg;
        }

        public int getCheckinAction() {
            return checkinAction;
        }

        public void setCheckinAction(int checkinAction) {
            this.checkinAction = checkinAction;
        }
    }

}
