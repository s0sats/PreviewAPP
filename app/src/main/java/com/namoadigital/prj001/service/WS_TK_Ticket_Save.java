package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_From_To;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_Result;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_Result_Step;
import com.namoadigital.prj001.model.WS_TK_Ticket_Ctrl_Obj;
import com.namoadigital.prj001.model.WS_TK_Ticket_Obj;
import com.namoadigital.prj001.model.WS_TK_Ticket_Product_Obj;
import com.namoadigital.prj001.model.WS_TK_Ticket_Step_Obj;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.sql.GE_File_Sql_006;
import com.namoadigital.prj001.sql.GE_File_Sql_007;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_009;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_001;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_002;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_003;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_004;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_005;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_006;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_007;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_004;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_009;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_ticket_save";
    //private String token;
    private String file_to_del = "";
    private boolean reSend = false;
    private boolean menuSendProcess;
    private Gson gsonEnv;
    private Gson gsonRec;
    //private ArrayList<WS_TK_Ticket_Obj> ticketToSend = new ArrayList<>();
    private ArrayList<TK_Ticket> ticketToSend = new ArrayList<>();
    private ArrayList<TicketSaveActReturn> actReturnList = new ArrayList<>();
    private TK_TicketDao ticketDao;
    private TK_Ticket_CtrlDao ticketCtrlDao;
    private TK_Ticket_StepDao ticketStepDao;
    private TK_Ticket_ActionDao ticketActionDao;
    private TK_Ticket_ApprovalDao ticketApprovalDao;
    private MD_Schedule_ExecDao scheduleExecDao;
    private GE_FileDao geFileDao;

    public WS_TK_Ticket_Save() {
        super("WS_TK_Ticket_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            ticketDao = new TK_TicketDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            ticketCtrlDao = new TK_Ticket_CtrlDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            ticketStepDao = new TK_Ticket_StepDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            ticketActionDao = new TK_Ticket_ActionDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            ticketApprovalDao = new TK_Ticket_ApprovalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            scheduleExecDao = new MD_Schedule_ExecDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            geFileDao = new GE_FileDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            menuSendProcess = bundle.getBoolean(ConstantBaseApp.PROCESS_MENU_SEND, false);
            processTicketSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_TK_Ticket_Save.completeWakefulIntent(intent);
        }

    }

    private void processTicketSave() throws Exception {
        //Seleciona traduções
        loadTranslation();
        //LUCHE - 08/01/2020
        //Unificado metodos do processo de envio com token no toolbox_inf após a adição do customer_code
        //no nome do arquivo
        //Lista arquivos de token de Ticket
        File[] files = ToolBox_Inf.checkTokenToSend(
                getApplicationContext(),
                ConstantBaseApp.TOKEN_PATH,
                ConstantBaseApp.TOKEN_TICKET_PREFIX
        );
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_loading_data_from_token"), "", "0");
            //
            file_to_del = files[0].getAbsolutePath();
            //
            reSend = true;
            //
            T_TK_Ticket_Save_Env env =
                    gsonRec.fromJson(
                            ToolBox_Inf.getContents(files[0]),
                            T_TK_Ticket_Save_Env.class
                    );
            //
            ticketToSend = env.getTicket();
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setReprocess(1);
            //
            callWsTicketSave(env);
        } else {
            reSend = false;
            //
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_items_data"), "", "0");
            //Gera token
            String token = ToolBox_Inf.getToken(getApplicationContext());
            //
            ticketToSend = getTicketsToSend();
            //Se lista vazia, dispara msg de erro.
            if (ticketToSend == null || ticketToSend.size() == 0) {
                String json = actReturnList != null ? gsonRec.toJson(actReturnList) : gsonRec.toJson(new ArrayList<>());
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), json, "0");
                return;
            }
            //
            //Seta token nos itens da lista.Util apenas na geração do arquivo de token.
            for (TK_Ticket ticket : ticketToSend) {
                ticket.setToken(token);
            }
            //
            T_TK_Ticket_Save_Env env = new T_TK_Ticket_Save_Env();
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setTicket(ticketToSend);
            env.setReprocess(0);
            env.setToken(token);
            //
            String json_token_content = gsonRec.toJson(env);
            String jsonFileName =
                    ToolBox_Inf.buildTokenFileAbsPath(
                            getApplicationContext(),
                            ConstantBaseApp.TOKEN_TICKET_PREFIX,
                            token
                    );
            File jsonToken = ToolBox_Inf.saveTokenAsFile(jsonFileName, json_token_content);
            file_to_del = jsonToken.getAbsolutePath();
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            //  if (!checksumJsonToken(json_token_content, jsonToken)) {
            if (!ToolBox_Inf.checksumJsonToken(json_token_content, jsonToken)) {
                ToolBox_Inf.deleteFileWithRet(file_to_del);
                //
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("generic_token_file_creation_error"), "", "0");
                return;
            }
            //Reseta update required das tabelas
            for (TK_Ticket ticket : ticketToSend) {
                //TK_Ticket
                ticketDao.addUpdate(
                        new Sql_WS_TK_Ticket_Save_002(
                                ticket.getCustomer_code(),
                                ticket.getTicket_prefix(),
                                ticket.getTicket_code(),
                                0,
                                0,
                                0//Como retorno sempre full, reseta o sync_required
                        ).toSqlQuery()
                );
                //TK_Ticket_Step
                ticketDao.addUpdate(
                        new Sql_WS_TK_Ticket_Save_003(
                                ticket.getCustomer_code(),
                                ticket.getTicket_prefix(),
                                ticket.getTicket_code()
                        ).toSqlQuery()
                );
                //TK_Ticket_Ctrl
                ticketDao.addUpdate(
                        new Sql_WS_TK_Ticket_Save_004(
                                ticket.getCustomer_code(),
                                ticket.getTicket_prefix(),
                                ticket.getTicket_code()
                        ).toSqlQuery()
                );
            }
            //
            callWsTicketSave(env);
        }
    }

//    private ArrayList<WS_TK_Ticket_Obj> getTicketsToSend() {
//        ArrayList<TK_Ticket> rawTickets = getTicketsDB();
//        return getTicketSendFormat(rawTickets);
//        //
//    }

    private ArrayList<TK_Ticket> getTicketsToSend() {
        return keepOnlyUpdateRequiredData(getTicketsDB());
    }

    private ArrayList<TK_Ticket> keepOnlyUpdateRequiredData(ArrayList<TK_Ticket> ticketList) {
        for (TK_Ticket tk_ticket : ticketList) {
            if (tk_ticket.getUpdate_required_product() != 1) {
                tk_ticket.setProduct(new ArrayList<TK_Ticket_Product>());
            }
            //
            if (tk_ticket.getStep() != null && tk_ticket.getStep().size() > 0) {
                ArrayList<TK_Ticket_Step> stepsToUpdate = new ArrayList<>();
                for (TK_Ticket_Step tk_ticket_step : tk_ticket.getStep()) {
                    if (tk_ticket_step.getUpdate_required() == 1) {
                        if (tk_ticket_step.getCtrl() != null && tk_ticket_step.getCtrl().size() > 0) {
                            ArrayList<TK_Ticket_Ctrl> ctrlsToUpdate = new ArrayList<>();
                            for (TK_Ticket_Ctrl ticketCtrl : tk_ticket_step.getCtrl()) {
                                //Se ctrl update_required, poe na  nova lista
                                if (ticketCtrl.getUpdate_required() == 1) {
                                    ctrlsToUpdate.add(ticketCtrl);
                                }
                            }
                            //Atualiza lista de ctrls da step.
                            tk_ticket_step.setCtrl(ctrlsToUpdate);
                        }
                        //Se step update_required, poe na  nova lista
                        stepsToUpdate.add(tk_ticket_step);
                    }
                }
                //Atualiza lista de steps no ticket
                tk_ticket.setStep(stepsToUpdate);
            }
        }
        //
        return ticketList;
    }

    private ArrayList<WS_TK_Ticket_Obj> getTicketSendFormat(ArrayList<TK_Ticket> rawTickets) {
        ArrayList<WS_TK_Ticket_Obj> ticketsToSend = new ArrayList<>();
        for (TK_Ticket rawTicket : rawTickets) {
            WS_TK_Ticket_Obj ticketObj = new WS_TK_Ticket_Obj();
            ticketObj.setCustomer_code(rawTicket.getCustomer_code());
            ticketObj.setTicket_prefix(rawTicket.getTicket_prefix());
            ticketObj.setTicket_code(rawTicket.getTicket_code());
            ticketObj.setScn(rawTicket.getScn());
            //Busca por produtos a serem enviados.
            if (rawTicket.getProduct() != null && rawTicket.getProduct().size() > 0 && rawTicket.getUpdate_required_product() == 1) {
                ArrayList<WS_TK_Ticket_Product_Obj> ticketProductObjs = new ArrayList<>();
                for (TK_Ticket_Product tk_ticket_product : rawTicket.getProduct()) {
                    WS_TK_Ticket_Product_Obj productObj = new WS_TK_Ticket_Product_Obj();
                    //
                    productObj.setProduct_code(tk_ticket_product.getProduct_code());
                    productObj.setQty(tk_ticket_product.getQty());
                    productObj.setQty_used(tk_ticket_product.getQty_used());
                    productObj.setQty_returned(tk_ticket_product.getQty_returned());
                    //
                    ticketProductObjs.add(productObj);
                }
                ticketObj.setProduct(ticketProductObjs);
            }
            //
            if (rawTicket.getStep() != null && rawTicket.getStep().size() > 0) {
                ArrayList<WS_TK_Ticket_Step_Obj> ticketStepObjs = new ArrayList<>();
                for (TK_Ticket_Step tk_ticket_step : rawTicket.getStep()) {
                    WS_TK_Ticket_Step_Obj stepObj = new WS_TK_Ticket_Step_Obj();
                    if (tk_ticket_step.getUpdate_required() == 1) {
                        stepObj.setStep_code(tk_ticket_step.getStep_code());
                        stepObj.setStep_start_date(tk_ticket_step.getStep_start_date());
                        stepObj.setStep_end_date(tk_ticket_step.getStep_end_date());
                        //
                        ticketStepObjs.add(stepObj);
                    }
                    //
                    for (TK_Ticket_Ctrl ticketCtrl : tk_ticket_step.getCtrl()) {
                        if (ticketCtrl.getUpdate_required() == 1) {
                            WS_TK_Ticket_Ctrl_Obj ctrlObj = new WS_TK_Ticket_Ctrl_Obj();
                            ctrlObj.setTicket_seq(ticketCtrl.getTicket_seq());
                            ctrlObj.setTicket_seq_tmp(ticketCtrl.getTicket_seq_tmp());
                            ctrlObj.setCtrl_start_date(ticketCtrl.getCtrl_start_date());
                            ctrlObj.setCtrl_end_date(ticketCtrl.getCtrl_end_date());
                            ctrlObj.setCtrl_type(ticketCtrl.getCtrl_type());
                            switch (ticketCtrl.getCtrl_type()) {
                                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                                    ctrlObj.setAction(ticketCtrl.getAction());
                                    break;
                                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                                    ctrlObj.setApproval(ticketCtrl.getApproval());
                            }
                            //
                            stepObj.getCtrl().add(ctrlObj);
                        }
                    }
                    //
                    if (!ticketStepObjs.contains(stepObj) && stepObj.getCtrl().size() > 0) {
                        ticketStepObjs.add(stepObj);
                    }
                }
                //
                ticketObj.setStep(ticketStepObjs);
            }
            //
            ticketsToSend.add(ticketObj);
        }
        //
        return ticketsToSend;
    }

    private ArrayList<TK_Ticket> getTicketsDB() {
        return (ArrayList<TK_Ticket>) ticketDao.query(
                new Sql_WS_TK_Ticket_Save_001(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
        );
    }

    private void callWsTicketSave(T_TK_Ticket_Save_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_TICKET_SAVE,
                gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        T_TK_Ticket_Save_Rec rec = gsonRec.fromJson(
                resultado,
                T_TK_Ticket_Save_Rec.class
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
        processTicketSaveReturn(rec);
    }

    private void processTicketSaveReturn(T_TK_Ticket_Save_Rec rec) throws Exception {
        //
        if (ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(rec.getSave())
                || ConstantBaseApp.MAIN_RESULT_OK_DUP.equalsIgnoreCase(rec.getSave())) {
            if (rec.getTicket_return() != null && rec.getTicket_return().size() > 0) {
                //
                processActFeedback(rec);
                //
            }
            if (rec.getFrom_to() != null && rec.getFrom_to().size() > 0) {
                if (processFromTo(rec.getFrom_to(), rec.getTicket_return())) {
                    processTicketFull(rec);
                } else {
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("error_from_to_processing"), "", "0");
                }
            } else {
                processTicketFull(rec);
            }
        } else {
            //COMO TRATAR, SERÁ QUE EXISTE ESSE OUTRO STATUS
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    hmAux_Trans.get("msg_data_returned_error") + ":\n" + rec.getSave(),
                    new HMAux(),
                    "",
                    "0");
        }
    }


    private TK_Ticket_Action getOldAction(T_TK_Ticket_Save_Rec rec) {
        for (T_TK_Ticket_Save_Rec_Result rec_result : rec.getTicket_return()) {
            if (rec_result.getSchedule_prefix() != null
                    && rec_result.getSchedule_code() != null
                    && rec_result.getSchedule_exec() != null) {
                //
                TK_Ticket scheduledTicket = getScheduledTicket(
                        rec_result.getSchedule_prefix(),
                        rec_result.getSchedule_code(),
                        rec_result.getSchedule_exec()
                );
                for (TK_Ticket_Step scheduleStep : scheduledTicket.getStep()) {
                    for (TK_Ticket_Ctrl tk_ticket_ctrl : scheduleStep.getCtrl()) {
                        return tk_ticket_ctrl.getAction();
                    }
                }
            }
        }
        return null;
    }

    private void processActFeedback(T_TK_Ticket_Save_Rec rec) throws Exception {
        for (T_TK_Ticket_Save_Rec_Result recResult : rec.getTicket_return()) {
            TicketSaveActReturn actReturn = getActReturn(recResult);
            if (recResult.getStep() != null && recResult.getStep().size() > 0) {
                for (T_TK_Ticket_Save_Rec_Result_Step resultStep : recResult.getStep()) {
                    //if(!ConstantBaseApp.MAIN_RESULT_OK.equals(resultStep.getRet_status())){
                    actReturn.setRetMsg(
                            getFormattedRetMsg(actReturn, resultStep)
                    );
                    //}
                    if (resultStep.getCtrl() != null) {
                        ArrayList<TK_Ticket_Ctrl> ctrlsToUpdate = new ArrayList<>();
                        for (T_TK_Ticket_Save_Rec_Result_Step.T_TK_Ticket_Save_Rec_Result_Ctrl resultCtrl : resultStep.getCtrl()) {
                            String newStatus =
                                    ConstantBaseApp.MAIN_RESULT_OK.equals(resultStep.getRet_status())
                                            ? ConstantBaseApp.SYS_STATUS_DONE
                                            : ConstantBaseApp.SYS_STATUS_ERROR;
                            //
                            TK_Ticket_Ctrl dbTicketCtrl = getDbTicketCtrl(
                                    resultStep.getCustomer_code(),
                                    resultStep.getTicket_prefix(),
                                    resultStep.getTicket_code(),
                                    resultStep.getStep_code(),
                                    resultCtrl.getTicket_seq_tmp()
                            );
                            //
                            switch (dbTicketCtrl.getCtrl_type()) {
                                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                                    if (handleApprovalObject(dbTicketCtrl.getApproval())) {
                                        dbTicketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_DONE);
                                    } else {
                                        dbTicketCtrl.removeEndInfo();
                                        resetStepDueToRejection(resultStep);
                                        dbTicketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_PROCESS);
                                    }
                                    //Não ha necessidade de mexer no rejeitdo, pois ele tem o proprio status que sempre é rejeitado.
                                    break;
                                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                                    dbTicketCtrl.setCtrl_status(newStatus);
                                    dbTicketCtrl.copyCtrlStatusForInnerProcess();
                                    break;
                            }
                            //
                            ctrlsToUpdate.add(dbTicketCtrl);
                        }
                        //
                        ticketCtrlDao.addUpdate(ctrlsToUpdate, false);
                    }
                    //
                    if (ConstantBaseApp.MAIN_RESULT_OK.equals(resultStep.getRet_status()) && recResult.getTicket_update() == 0) {
                        TK_Ticket_Step ticketStepFromDB = getTicketStepFromDB(resultStep);
                        //Se não tem uma nova atualização, atualiza.
                        if (ticketStepFromDB.getUpdate_required() == 0) {
                            //Verifica se é um checkout para setar o Step como Done
                            if (ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(ticketStepFromDB.getStep_status())
                                    && stepWithCheckIn(ticketStepFromDB)
                                    && stepWithCheckout(ticketStepFromDB)
                            ) {
                                ticketStepFromDB.setStep_status(ConstantBaseApp.SYS_STATUS_DONE);
                                ticketStepDao.addUpdate(ticketStepFromDB);
                            } else {
                                //Se não é checkout, verifica se é um checkin e muda o status para PROCESS
                                if (ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(ticketStepFromDB.getStep_status())
                                        && stepWithCheckIn(ticketStepFromDB)
                                ) {
                                    ticketStepFromDB.setStep_status(ConstantBaseApp.SYS_STATUS_PROCESS);
                                    ticketStepDao.addUpdate(ticketStepFromDB);
                                }
                            }
                        }
                    }
                }
            } else {
                boolean createdBySchedule = false;
                createdBySchedule = isTicketCreatedBySchedule(recResult);
                //
                if (createdBySchedule) {
                    //
                    TK_Ticket ticket = getScheduledTicket(recResult.getSchedule_prefix(),
                            recResult.getSchedule_code(),
                            recResult.getSchedule_exec());

                    MD_Schedule_Exec scheduleExec = getSchedule(recResult.getSchedule_prefix(),
                            recResult.getSchedule_code(),
                            recResult.getSchedule_exec());

                    if (recResult.getRet_status().equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_OK)) {
                        TK_Ticket ticketBackup = ticket.clone();
                        ticketDao.removeFullV2(ticket);
                        //
                        ticket.setTicket_prefix(recResult.getTicket_prefix());
                        ticket.setTicket_code(recResult.getTicket_code());
                        ticket.setTicket_status(ConstantBase.SYS_STATUS_DONE);
                        ticket.setPK();
                        //
                        scheduleExec.setStatus(recResult.getSchedule_status());
                        scheduleExec.setFcm_new_status(null);
                        scheduleExec.setFcm_user_nick(null);
                        scheduleExec.setSchedule_erro_msg(null);
                        scheduleExec.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

                        DaoObjReturn daoObjReturn = updateTicketAndScheduleReg(ticket, scheduleExec);
                        if (daoObjReturn.hasError()) {
                            ticketDao.addUpdate(ticketBackup);
                            throw new Exception(daoObjReturn.getErrorMsg());
                        }
                        //
                        actReturn.setRetMsg(
                                getFormattedScheduleRetMsg(actReturn, recResult)
                        );
                    } else {
                    /*
                        1) Avaliar status do Schedule
                            - Caso seja ERROR
                                1) manter registro para proxima execução.
                            - Caso seja DONE/PROCESS/CANCELLED
                                1) Mudar status de ticket para cancelled.
                                2) Apagar registro da tabela de Schedule.
                    */
                        if(ConstantBase.SYS_STATUS_ERROR.equalsIgnoreCase(recResult.getSchedule_status())){

                        }else{
                            if(ConstantBase.SYS_STATUS_CANCELLED.equalsIgnoreCase(recResult.getSchedule_status())
                            || ConstantBase.SYS_STATUS_PROCESS.equalsIgnoreCase(recResult.getSchedule_status())
                            || ConstantBase.SYS_STATUS_DONE.equalsIgnoreCase(recResult.getSchedule_status())) {
                                ticket.setTicket_status(ConstantBase.SYS_STATUS_CANCELLED);
                                ticket.setUpdate_required(0);
                                ticket.setUpdate_required_product(0);

                                ticketDao.addUpdate(ticket);

                                scheduleExecDao.remove(new MD_Schedule_Exec_Sql_009(
                                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                        scheduleExec.getSchedule_prefix(),
                                        scheduleExec.getSchedule_code(),
                                        scheduleExec.getSchedule_exec()
                                ).toSqlQuery());

                                String stepErroMsg = recResult.getSchedule_prefix() + "." +
                                        recResult.getSchedule_code() + "." +
                                        recResult.getSchedule_exec() + ": ";

                                stepErroMsg += recResult.getRet_msg() != null && !recResult.getRet_msg().isEmpty() ? "\n" + recResult.getRet_msg() : recResult.getRet_status();
                                actReturn.setRetMsg(
                                        stepErroMsg
                                );
                            }
                        }

                    }
                }
            }
            //
            if (recResult.getProduct() != null && recResult.getProduct().size() > 0) {
                for (T_TK_Ticket_Save_Rec_Result_Step resultProduct : recResult.getProduct()) {
                    //if(!ConstantBaseApp.MAIN_RESULT_OK.equals(resultProduct.getRet_status())){
                    actReturn.setRetMsg(
                            getFormattedRetMsg(actReturn, resultProduct)
                    );
                    //}
                }
            }
            //
            if (recResult.getTicket_update() == 0 && ConstantBaseApp.MAIN_RESULT_OK.equals(recResult.getRet_status())) {
                //atualiza SCN do ticket.
                ticketDao.addUpdate(
                        new Sql_WS_TK_Ticket_Save_007(
                                recResult.getCustomer_code(),
                                recResult.getTicket_prefix(),
                                recResult.getTicket_code(),
                                recResult.getScn()
                        ).toSqlQuery()
                );
            } else if (recResult.getTicket_update() == 1) {
                ticketDao.addUpdate(
                        new Sql_WS_TK_Ticket_Save_002(
                                recResult.getCustomer_code(),
                                recResult.getTicket_prefix(),
                                recResult.getTicket_code(),
                                1,
                                0
                        ).toSqlQuery()
                );
            }
            //
            actReturnList.add(actReturn);
        }
    }

    private void resetStepDueToRejection(T_TK_Ticket_Save_Rec_Result_Step resultStep) {
        TK_Ticket_Step ticketStepFromDB = getTicketStepFromDB(resultStep);
        ticketStepFromDB.setStep_end_user_nick(null);
        ticketStepFromDB.setStep_end_user(null);
        ticketStepFromDB.setStep_end_date(null);
        ticketStepDao.addUpdate(ticketStepFromDB);
    }

    private boolean handleApprovalObject(TK_Ticket_Approval approval) {

        if (ConstantBase.SYS_STATUS_REJECTED.equalsIgnoreCase(approval.getApproval_status())) {
            approval.setApproval_status(ConstantBase.SYS_STATUS_PENDING);
            approval.setApproval_comments(null);
            ticketApprovalDao.addUpdate(approval);
            return false;
        }
        return true;
    }

    private boolean stepWithCheckout(TK_Ticket_Step ticketStepFromDB) {
        return ToolBox_Inf.hasConsistentValueString(ticketStepFromDB.getStep_end_date())
                && ticketStepFromDB.getStep_end_user() != null && ticketStepFromDB.getStep_end_user() > 0;
    }

    private boolean stepWithCheckIn(TK_Ticket_Step ticketStepFromDB) {
        return ToolBox_Inf.hasConsistentValueString(ticketStepFromDB.getStep_start_date())
                && ticketStepFromDB.getStep_start_user() != null && ticketStepFromDB.getStep_start_user() > 0;
    }

    private TK_Ticket_Step getTicketStepFromDB(T_TK_Ticket_Save_Rec_Result_Step resultStep) {
        return ticketStepDao.getByString(
                new TK_Ticket_Step_Sql_001(
                        resultStep.getCustomer_code(),
                        resultStep.getTicket_prefix(),
                        resultStep.getTicket_code(),
                        resultStep.getStep_code()
                ).toSqlQuery()
        );
    }

    @NonNull
    private String getFormattedRetMsg(TicketSaveActReturn actReturn, T_TK_Ticket_Save_Rec_Result_Step resultStep) {
        String stepErroMsg = resultStep.getStep_desc() + " : ";
        stepErroMsg += resultStep.getRet_msg() != null && !resultStep.getRet_msg().isEmpty() ? "\n" + resultStep.getRet_msg() : resultStep.getRet_status();
        //
        if (actReturn.getRetMsg() == null || actReturn.getRetMsg().isEmpty()) {
            return stepErroMsg + "\n";
        } else {
            return actReturn.getRetMsg() + stepErroMsg + "\n";
        }
    }

    @NonNull
    private String getFormattedScheduleRetMsg(TicketSaveActReturn actReturn, T_TK_Ticket_Save_Rec_Result recResult) {
        String stepErroMsg = recResult.getSchedule_prefix() + "." +
                recResult.getSchedule_code() + "." +
                recResult.getSchedule_exec() + ": ";
        stepErroMsg += recResult.getRet_msg() != null && !recResult.getRet_msg().isEmpty() ? "\n" + recResult.getRet_msg() : recResult.getRet_status();
        //
        if (actReturn.getRetMsg() == null || actReturn.getRetMsg().isEmpty()) {
            return stepErroMsg + "\n";
        } else {
            return actReturn.getRetMsg() + stepErroMsg + "\n";
        }
    }


    private void processTicketFull(T_TK_Ticket_Save_Rec rec) throws Exception {
        //Executa loop nos tickets caso exista
        if (rec.getTicket() != null && rec.getTicket().size() > 0) {
            for (TK_Ticket tk_ticket : rec.getTicket()) {
                //Seta pk nos filhos
                tk_ticket.setPK();

                for (T_TK_Ticket_Save_Rec_Result rec_result : rec.getTicket_return()) {
                    if (rec_result.getSchedule_prefix() != null
                            && rec_result.getSchedule_code() != null
                            && rec_result.getSchedule_exec() != null) {
                        //
                        TK_Ticket scheduledTicket = getScheduledTicket(
                                rec_result.getSchedule_prefix(),
                                rec_result.getSchedule_code(),
                                rec_result.getSchedule_exec()
                        );
                        //
                        DaoObjReturn daoObjReturn = ticketDao.removeFullV2(scheduledTicket);
                        if (!daoObjReturn.hasError()) {
                            //todo o que fazer?
                        }
                    }
                }

                //Varre todas as imagens verificando se existe imagem local para cada item que pode ter foto
                tk_ticket.updateLocalImagesPathIfExists();
                //Verifica se precisa resetar alguma foto. Isso deve ser feito se o "file_code" da foto
                //for alterado, o que significa que mudaram a foto no server...
                TK_Ticket.checkActionPhotoResetNeeds(
                        getDbTicket(tk_ticket, false),
                        tk_ticket
                );
                //Remove o ticket do banco de dados
                ticketDao.removeFullV2(tk_ticket);
                //Tenta o insert do ticket
                DaoObjReturn daoObjReturn = ticketDao.addUpdate(tk_ticket);
                //Se não houve erro , chama metodo define proximo passo.
                if (!daoObjReturn.hasError()) {
                    prepareEndOrResendProcess();
                }
            }
        } else {
            prepareEndOrResendProcess();
        }
    }

    private TK_Ticket getScheduledTicket(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        return ticketDao.getByString(new TK_Ticket_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                schedule_prefix,
                schedule_code,
                schedule_exec
        ).toSqlQuery());
    }

    private void prepareEndOrResendProcess() throws Exception {
        //Após processamento , apaga arquivo de token
        if (ToolBox_Inf.deleteFileWithRet(file_to_del)) {
            if (reSend) {
                ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_re_processing_data"), "", "0");
                //Reseta var de re transmissão.
                reSend = false;
                //Reseta lista de cab após processo de token
                ticketToSend.clear();
                //
                processTicketSave();
            } else {
                String jsonActReturn = gsonRec.toJson(actReturnList);
                //
                callFinishProcessing(jsonActReturn);
            }
        } else {
            //VERIFICAR O QUYE FAZER NESSE CASO.
            ToolBox_Inf.registerException(
                    getClass().getName(),
                    new Exception("WS_TICKET_SAVE_ERROR_ON_DELETE_TOKEN_FILE")
            );
        }
    }

    private boolean processFromTo(ArrayList<T_TK_Ticket_Save_Rec_From_To> from_to, ArrayList<T_TK_Ticket_Save_Rec_Result> recTicketResult) {
        try {
            for (T_TK_Ticket_Save_Rec_From_To recFromTo : from_to) {
                T_TK_Ticket_Save_Rec_Result ticketProcessResult = getTicketProcessResult(recTicketResult, recFromTo.getCustomer_code(), recFromTo.getTicket_prefix(), recFromTo.getTicket_code());
                //
                if (ticketProcessResult != null && ConstantBaseApp.MAIN_RESULT_OK.equals(ticketProcessResult.getRet_status())) {
                    TK_Ticket_Ctrl ticketCtrl = getDbTicketCtrl(
                            recFromTo.getCustomer_code(),
                            recFromTo.getTicket_prefix(),
                            recFromTo.getTicket_code(),
                            recFromTo.getStep_code(),
                            recFromTo.getTicket_seq_tmp()
                    );
                    //Seta o seq retornando pelo servido no ctrl
                    ticketCtrl.setTicket_seq(recFromTo.getTicket_seq());
                    //Seta o seq retornado nos processos filhos
                    ticketCtrl.setPKIntoProcess();
                    //Atualiza status no ctrl para done
                    ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_DONE);
                    //Atualiza status dos processos filhos
                    ticketCtrl.copyCtrlStatusForInnerProcess();
                    //Se for action, faz processo de copia da foto
                    if (ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION.equals(ticketCtrl.getCtrl_type())
                            && ticketCtrl.getAction() != null
                            && ticketCtrl.getAction().getAction_photo_local() != null
                            && !ticketCtrl.getAction().getAction_photo_local().isEmpty()
                    ) {
                        String originalName = ticketCtrl.getAction().getAction_photo_local();
                        String newName = ToolBox_Inf.buildTicketActionImgPath(ticketCtrl);
                        if (!fromToImgProcess(geFileDao, originalName, newName)) {
                            return false;
                        }
                        //
                        ticketCtrl.setFinalNameIntoActionPhoto(newName);
                    }
                    //Atualiza ctrl
                    ticketCtrlDao.addUpdateTmp(ticketCtrl, null);
                    //Se processo de token e servidor informa que ainda tem atualização,
                    //Seta update_Required pra 1
                    if (reSend && ticketProcessResult.getTicket_update() == 1) {
                        ticketDao.addUpdate(
                                new Sql_WS_TK_Ticket_Save_002(
                                        ticketProcessResult.getCustomer_code(),
                                        ticketProcessResult.getTicket_prefix(),
                                        ticketProcessResult.getTicket_code(),
                                        1,
                                        0
                                ).toSqlQuery()
                        );
                    } else {
                        //Se processo de token, necessidade de outra atualização
                        //atualiza somente o SCN
                        ticketDao.addUpdate(
                                new Sql_WS_TK_Ticket_Save_007(
                                        ticketProcessResult.getCustomer_code(),
                                        ticketProcessResult.getTicket_prefix(),
                                        ticketProcessResult.getTicket_code(),
                                        ticketProcessResult.getScn()
                                ).toSqlQuery()
                        );
                    }
                } else {
                    TK_Ticket ticketSent = getTicketFromSentList(recFromTo.getCustomer_code(), recFromTo.getTicket_prefix(), recFromTo.getTicket_code());
                    //ATUALIZAR TICKET PARA UPDATE REQUIRED
                    ticketDao.addUpdate(
                            new Sql_WS_TK_Ticket_Save_002(
                                    ticketSent.getCustomer_code(),
                                    ticketSent.getTicket_prefix(),
                                    ticketSent.getTicket_code(),
                                    ticketSent.getUpdate_required(),
                                    ticketSent.getUpdate_required_product() //Como retorno sempre full, reseta o sync_required
                            ).toSqlQuery()
                    );
                    //
                    if (ticketSent.getStep() != null && ticketSent.getStep().size() > 0) {
                        //TK_Ticket_Step
                        for (TK_Ticket_Step ticketStep : ticketSent.getStep()) {
                            ticketDao.addUpdate(
                                    new Sql_WS_TK_Ticket_Save_005(
                                            ticketStep.getCustomer_code(),
                                            ticketStep.getTicket_prefix(),
                                            ticketStep.getTicket_code(),
                                            ticketStep.getStep_code(),
                                            1
                                    ).toSqlQuery()
                            );
                            //TK_ticket_Ctrl
                            if (ticketStep.getCtrl() != null && ticketStep.getCtrl().size() > 0) {
                                for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
                                    //TK_Ticket_Ctrl
                                    ticketDao.addUpdate(
                                            new Sql_WS_TK_Ticket_Save_006(
                                                    ticketCtrl.getCustomer_code(),
                                                    ticketCtrl.getTicket_prefix(),
                                                    ticketCtrl.getTicket_code(),
                                                    ticketCtrl.getStep_code(),
                                                    ticketCtrl.getTicket_seq_tmp(),
                                                    1
                                            ).toSqlQuery()
                                    );
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private TK_Ticket getTicketFromSentList(int customer_code, int ticket_prefix, int ticket_code) {
        for (TK_Ticket tk_ticket : ticketToSend) {
            if (tk_ticket.getCustomer_code() == customer_code
                    && tk_ticket.getTicket_prefix() == ticket_prefix
                    && tk_ticket.getTicket_code() == ticket_code
            ) {
                return tk_ticket;
            }
        }
        return null;
    }

    private T_TK_Ticket_Save_Rec_Result getTicketProcessResult(ArrayList<T_TK_Ticket_Save_Rec_Result> recTicketResult, int customer_code, int ticket_prefix, int ticket_code) {
        for (T_TK_Ticket_Save_Rec_Result recResult : recTicketResult) {
            if (
                    recResult.getCustomer_code() == customer_code
                            && recResult.getTicket_prefix() == ticket_prefix
                            && recResult.getTicket_code() == ticket_code
            ) {
                return recResult;
            }
        }
        return null;
    }

    private TK_Ticket_Ctrl getDbTicketCtrl(int customer_code, int ticket_prefix, int ticket_code, int step_code, int ticket_seq_tmp) {
        return ticketCtrlDao.getByString(
                new TK_Ticket_Ctrl_Sql_004(
                        customer_code,
                        ticket_prefix,
                        ticket_code,
                        ticket_seq_tmp
                ).toSqlQuery()
        );
    }

    private void callFinishProcessing(String jsonActReturn) {
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), jsonActReturn, "0");
    }

//    private void processTicketRet(T_TK_Ticket_Save_Rec_Result retResult, TicketSaveActReturn actReturn) throws Exception {
//        boolean createdBySchedule = isTicketCreatedBySchedule(retResult);
//        TK_Ticket retTicket = retResult.getTicket();
//        DaoObjReturn daoObjReturn = new DaoObjReturn();
//        if(retTicket != null){
//            //Só atualizará o obj ticket se não for processamento de token
//            //ou for processamento de token, mas o ticket nõ posusi mais dados a serem enviados
//            TK_Ticket dbTicket = getDbTicket(retTicket,createdBySchedule);
//            //
//            if(!reSend || noMoreUpdate(dbTicket)) {
//                //Seta PKs nos objs filhos
//                retTicket.setPK();
//                //Se agendamento, seta dados do ag no obj de retorno e renomeia foto
//                if(createdBySchedule){
//                    actReturn.setSchedulePrefix(retTicket.getSchedule_prefix());
//                    actReturn.setScheduleCode(retTicket.getSchedule_code());
//                    actReturn.setScheduleExec(retTicket.getSchedule_exec());
//                    //
//                    updateActionsFileNames(retResult);
//                }
//                //REGION VERIFICAR TODO REVE SE MOVER PARA O STEP
//                //Verifica a necessidade de resetar a foto das action
////                TK_Ticket.checkActionPhotoResetNeeds(dbTicket,retTicket);
////                //Verifica se imagens já foram baixadas e atualiza campo com o local_path
////                retTicket.updateLocalImagesPathIfExists();
//                //ENDREGION
//                //Salva obj
//                if(createdBySchedule){
//                    MD_Schedule_Exec schedule = getSchedule(
//                        retTicket.getSchedule_prefix(),
//                        retTicket.getSchedule_code(),
//                        retTicket.getSchedule_exec()
//                    );
//                    //
//                    schedule.setStatus(retTicket.getTicket_status());
//                    schedule.setFcm_new_status(null);
//                    schedule.setFcm_user_nick(null);
//                    schedule.setSchedule_erro_msg(null);
//                    schedule.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
//                    //
//                    daoObjReturn = updateTicketAndScheduleReg(retTicket,schedule);
//                }else {
//                    daoObjReturn = ticketDao.addUpdate(retTicket);
//                }
//                if (daoObjReturn.hasError()) {
//                    throw new Exception(daoObjReturn.getErrorMsg());
//                }//
//            }else{
//                //Remove dados do processamento da lista, pois haverá um segundo processamento pro mesmo item
//                actReturnList.remove(actReturn);
//            }
//        }else{
//            if(createdBySchedule){
//                //Resgata ticket origim
//                TK_Ticket dbTicket = execGetDbTicketQuery(
//                    new TK_Ticket_Sql_001(
//                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
//                        retResult.getOld_ticket_prefix(),
//                        retResult.getOld_ticket_code()
//                    ).toSqlQuery()
//                );
//                //Se exisir, o que deve sempre acontecer
//                if(dbTicket != null){
//                    //Atualiza no obj retornado a tela a pk do agendamento
//                    actReturn.setSchedulePrefix(dbTicket.getSchedule_prefix());
//                    actReturn.setScheduleCode(dbTicket.getSchedule_code());
//                    actReturn.setScheduleExec(dbTicket.getSchedule_exec());
//                    //Seta status rejeitado no ticket, ctrls e ações
//                    dbTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_IGNORED);
//                    dbTicket.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
//                    //TODO REVE SE MOVER PARA O STEP
////                    if(dbTicket.getCtrl() != null && dbTicket.getCtrl().size() > 0) {
////                        for (TK_Ticket_Ctrl ticketCtrl : dbTicket.getCtrl()) {
////                            ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_IGNORED);
////                            if(ticketCtrl.getAction() != null){
////                                ticketCtrl.getAction().setAction_status(ConstantBaseApp.SYS_STATUS_IGNORED);
////                            }
////                        }
////                    }
//                    MD_Schedule_Exec schedule = getSchedule(
//                        dbTicket.getSchedule_prefix(),
//                        dbTicket.getSchedule_code(),
//                        dbTicket.getSchedule_exec()
//                    );
//                    //
//                    schedule.setStatus(dbTicket.getTicket_status());
//                    schedule.setFcm_new_status(null);
//                    schedule.setFcm_user_nick(null);
//                    schedule.setSchedule_erro_msg(actReturn.getRetMsg());
//                    schedule.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
//                    //
//                    //Atualiza ticket e agendamento
//                    daoObjReturn =  updateTicketAndScheduleReg(dbTicket,schedule);
//                    if (daoObjReturn.hasError()) {
//                        throw new Exception(daoObjReturn.getErrorMsg());
//                    }//
//                }else{
//                    throw new Exception("Original ticket not found !!!");
//                }
//            }
//        }
//    }

    @NonNull
    private DaoObjReturn updateTicketAndScheduleReg(TK_Ticket ticket, MD_Schedule_Exec scheduleExec) {
        DaoObjReturn daoObjReturn;
        daoObjReturn = ticketDao.addUpdateBySchedulePk(ticket, null);
        if (!daoObjReturn.hasError()) {
            updateSchedule(scheduleExec);
        }
        //
        return daoObjReturn;
    }


    /**
     * LUCHE - 14/02/2020
     * <p>
     * Atualiza status da tabela de agendamentos.
     *
     * @param scheduleExec - Agendamento com dados a serem atualizados.
     * @return - Verdadeiro se sucesso
     */
    private boolean updateSchedule(MD_Schedule_Exec scheduleExec) {
        DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
        //Retorna verdadeiro se não teve erro.
        return !daoObjReturn.hasError();
    }

    private MD_Schedule_Exec getSchedule(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        return scheduleExecDao.getByString(
                new MD_Schedule_Exec_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        );
    }

//    private void updateActionsFileNames(T_TK_Ticket_Save_Rec_Result retResult) throws Exception {
//        TK_Ticket tkTicket = retResult.getTicket();
//        //TODO REVE SE MOVER PARA O STEP
//        if(tkTicket.getCtrl() != null) {
//            for (TK_Ticket_Ctrl ticketCtrl : tkTicket.getCtrl()) {
//                if(ticketCtrl.getAction() != null){
//                    if(ticketCtrl.getAction().getAction_photo_name() != null) {
//                        String oldName = ToolBox_Inf.buildTicketActionImgPath(ticketCtrl.getCustomer_code(), retResult.getOld_ticket_prefix(), retResult.getOld_ticket_code(), ticketCtrl.getTicket_seq());
//                        if (oldName != null) {
//                            renameFile(oldName, ticketCtrl.getAction().getAction_photo_name());
//                        } else {
//                            //Ok Fazer.... Lançar exception
//                            throw new Exception("Error on update tickt photos name !!!");
//                        }
//                    }//Senão tem foto, não tem que fazer de para de foto
//                }else{
//                    //Ok Fazer.... Lançar exception
//                    throw new Exception("Ticket action not found !!!");
//                }
//            }
//        }else{
//            //Ok Fazer.... Lançar exception
//            throw new Exception("Ticket controls not found !!!");
//        }
    //}

    private void renameFile(String oldName, String action_photo_name) {
        File from = new File(Constant.CACHE_PATH_PHOTO + "/", oldName);
        File to = new File(Constant.CACHE_PATH_PHOTO + "/", action_photo_name);
        from.renameTo(to);
    }

    private boolean isTicketCreatedBySchedule(T_TK_Ticket_Save_Rec_Result retResult) {
        return retResult.getSchedule_prefix() != null && retResult.getSchedule_prefix() > 0
                && retResult.getSchedule_code() != null && retResult.getSchedule_code() > 0
                && retResult.getSchedule_exec() != null && retResult.getSchedule_exec() > 0;
    }

    private boolean noMoreUpdate(TK_Ticket dbTicket) {
       /* TK_Ticket dbTicket = ticketDao.getByString(
            new TK_Ticket_Sql_001(
                retTicket.getCustomer_code(),
                retTicket.getTicket_prefix(),
                retTicket.getTicket_code()
            ).toSqlQuery()
        );*/

        //
        return dbTicket != null && dbTicket.getUpdate_required() == 0;
    }

    private TK_Ticket getDbTicket(TK_Ticket retTicket, boolean createdBySchedule) {
        String selectionQuery = "";
        //
        if (createdBySchedule) {
            selectionQuery = new TK_Ticket_Sql_009(
                    retTicket.getCustomer_code(),
                    retTicket.getSchedule_prefix(),
                    retTicket.getSchedule_code(),
                    retTicket.getSchedule_exec()
            ).toSqlQuery();
        } else {
            selectionQuery = new TK_Ticket_Sql_001(
                    retTicket.getCustomer_code(),
                    retTicket.getTicket_prefix(),
                    retTicket.getTicket_code()
            ).toSqlQuery();
        }
        //
        return execGetDbTicketQuery(selectionQuery);
    }

    private TK_Ticket execGetDbTicketQuery(String selectionQuery) {
        return ticketDao.getByString(selectionQuery);
    }

    private TicketSaveActReturn getActReturn(T_TK_Ticket_Save_Rec_Result retResult) {
        TicketSaveActReturn actReturn = null;
        //busca se ja existe retorno para o ticket
        for (TicketSaveActReturn saveActReturn : actReturnList) {
            if (saveActReturn.getPrefix() == retResult.getTicket_prefix()
                    && saveActReturn.getCode() == retResult.getTicket_code()
            ) {
                actReturn = saveActReturn;
                break;
            }
        }
        //REMOVE O RETORNO DO MESMO TICKET
        if (actReturn != null) {
            actReturnList.remove(actReturn);
        }
        //
        actReturn = new TicketSaveActReturn(
                retResult.getCustomer_code(),
                retResult.getTicket_prefix(),
                retResult.getTicket_code(),
                retResult.getScn(),
                retResult.getRet_status(),
                retResult.getRet_msg(),
                reSend
        );
        //
        return actReturn;
    }

    /**
     * LUCHE - 20/07/2020
     * <p></p>
     * Após usr da Mosolf ganhar 2 vezes na loteria conseguindo sincornizar o
     * rename da foto com a rotina de upload, foi criado novo metodo para tratar
     * o upload das imagens.
     * Será gerada uma copia da imagem no de-para, na sequencia será avaliado o
     * status da foto na GE_File, se OPEN, atualiza registro com o segundo nome
     * se SENT, apaga a foto original.
     *
     * @param geFileDao
     * @param originalFileName
     * @param new_name
     * @return
     */
    private boolean fromToImgProcess(GE_FileDao geFileDao, String originalFileName, String new_name) {
        if (copyAndCheckFile(originalFileName, new_name)) {
            String fileCode = originalFileName.replace(".jpg", "").replace(".png", "");
            GE_File geFile = getCurrentFileReg(geFileDao, fileCode);
            if (ConstantBaseApp.GE_FILE_STATUS_OPENED.equals(geFile.getFile_status())) {
                //Atualiza path da imagem na lista de upload
                geFileDao.addUpdate(
                        new GE_File_Sql_006(
                                fileCode,
                                new_name
                        ).toSqlQuery()
                );
            } else if (ConstantBaseApp.GE_FILE_STATUS_SENT.equals(geFile.getFile_status())) {
                //Log.d("del-PHOTO", "WsTicketSave :" + geFile.getFile_path());
                ToolBox_Inf.deleteFileWithRet(
                        imgFileAbsolutePath(originalFileName)
                );
            }
            //
            return true;
        }
        return false;
    }

    private String imgFileAbsolutePath(String file_name) {
        File file = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", file_name);
        try {
            return file.getAbsolutePath();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            return "";
        }
    }

    /**
     * LUCHE - 20/07/2020
     * <p></p>
     * Cria copia da foto com o nome definitivo.
     *
     * @param originalFileName - Nome da foto original
     * @param copiedFileName   - Nome definitivo para criação da copia
     * @return Verdadeiro se foto copiada com sucesso.
     */
    private boolean copyAndCheckFile(String originalFileName, String copiedFileName) {
        try {
            File originalFile = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", originalFileName);
            File copiedFile = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", copiedFileName);
            //
            ToolBox_Inf.copyAndRenameFile(originalFile, copiedFile);
            //
            return ToolBox_Inf.verifyDownloadFileInf(copiedFileName, ConstantBaseApp.CACHE_PATH_PHOTO);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
            return false;
        }
    }

    private GE_File getCurrentFileReg(GE_FileDao geFileDao, String fileCode) {
        return geFileDao.getByString(
                new GE_File_Sql_007(
                        fileCode
                ).toSqlQuery()
        );
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_process_finalized_msg");
        translist.add("generic_loading_data_from_token");
        translist.add("generic_token_file_creation_error");
        translist.add("generic_processing_data");
        translist.add("generic_re_processing_data");
        translist.add("msg_preparing_items_data");
        translist.add("msg_no_data_returned_error");
        translist.add("msg_data_returned_error");
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

    public static class TicketSaveActReturn {
        private int customer_code = -1;
        private int prefix = -1;
        private int code = -1;
        private Integer scn = -1;
        private String retStatus = "";
        private String retMsg = "";
        private boolean fromTokenProcess = false;
        private boolean processError = false;
        private String processStatus = "";
        private String processMsg = "";
        private int oldPrefix = -1;
        private int oldCode = -1;
        private int schedulePrefix = -1;
        private int scheduleCode = -1;
        private int scheduleExec = -1;

        //
        public TicketSaveActReturn(int customer_code, int prefix, int code, Integer scn, String retStatus, String retMsg, boolean fromTokenProcess, int oldPrefix, int oldCode) {
            this.customer_code = customer_code;
            this.prefix = prefix;
            this.code = code;
            this.scn = scn;
            this.retStatus = retStatus;
            this.retMsg = retMsg;
            this.fromTokenProcess = fromTokenProcess;
            this.oldPrefix = oldPrefix;
            this.oldCode = oldCode;
        }

        public TicketSaveActReturn(int customer_code, int prefix, int code, Integer scn, String retStatus, String retMsg, boolean fromTokenProcess) {
            this.customer_code = customer_code;
            this.prefix = prefix;
            this.code = code;
            this.scn = scn;
            this.retStatus = retStatus;
            this.retMsg = retMsg;
            this.fromTokenProcess = fromTokenProcess;
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

        public boolean isFromTokenProcess() {
            return fromTokenProcess;
        }

        public void setFromTokenProcess(boolean fromTokenProcess) {
            this.fromTokenProcess = fromTokenProcess;
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

        public int getOldPrefix() {
            return oldPrefix;
        }

        public void setOldPrefix(int oldPrefix) {
            this.oldPrefix = oldPrefix;
        }

        public int getOldCode() {
            return oldCode;
        }

        public void setOldCode(int oldCode) {
            this.oldCode = oldCode;
        }

        public int getSchedulePrefix() {
            return schedulePrefix;
        }

        public void setSchedulePrefix(int schedulePrefix) {
            this.schedulePrefix = schedulePrefix;
        }

        public int getScheduleCode() {
            return scheduleCode;
        }

        public void setScheduleCode(int scheduleCode) {
            this.scheduleCode = scheduleCode;
        }

        public int getScheduleExec() {
            return scheduleExec;
        }

        public void setScheduleExec(int scheduleExec) {
            this.scheduleExec = scheduleExec;
        }

        public boolean isSameTicket(TicketSaveActReturn actReturn) {
            return this.getPrefix() == actReturn.getPrefix()
                    && this.getCode() == actReturn.getCode();
        }

    }
}
