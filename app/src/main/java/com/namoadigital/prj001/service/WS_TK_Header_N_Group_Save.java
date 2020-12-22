package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.T_TK_Header_N_Group_Save_WG_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Header_Group_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_Result;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_Result_Step;
import com.namoadigital.prj001.model.WS_TK_Ticket_Ctrl_Obj;
import com.namoadigital.prj001.model.WS_TK_Ticket_Obj;
import com.namoadigital.prj001.model.WS_TK_Ticket_Product_Obj;
import com.namoadigital.prj001.model.WS_TK_Ticket_Step_Obj;
import com.namoadigital.prj001.receiver.WBR_TK_Header_N_Group_Save;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Header_N_Group_Save extends IntentService {

    public static final String TIME_ACTION = "TIME_ACTION";
    public static final String MOVE_OTHER_DATE = "MOVE_OTHER_DATE";
    public static final String MOVE_STEPS = "MOVE_STEPS";
    public static final String IS_HEADER_DATETIME_CHANGES = "IS_HEADER_DATETIME_CHANGES";
    public static final String WORKGROUP_JSON_PARAM = "WORKGROUP_JSON_PARAM";
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_header_n_group_save";
    //private String token;
    private String file_to_del = "";
    private boolean reSend = false;
    private boolean menuSendProcess;
    private Gson gsonEnv;
    private Gson gsonRec;
    //private ArrayList<WS_TK_Ticket_Obj> ticketToSend = new ArrayList<>();
    private ArrayList<Object> actReturnList = new ArrayList<>();
    private TK_TicketDao ticketDao;
    private TK_Ticket_CtrlDao ticketCtrlDao;
    private MD_Schedule_ExecDao scheduleExecDao;
    private int scn;
    private int ticketPrefix;
    private int ticketCode;
    private int mainUser;
    private int move_other_date;
    private int move_steps;
    private boolean is_header_datetime_changes;
    private String mainUserName;
    private String mainUserNick;
    private String timeAction;
    private String forecast_time;
    private String start_date;
    private String forecast_date;
    private String internalComments;
    private List<TK_Ticket> ticketToSend = new ArrayList<>();
    private T_TK_Header_N_Group_Save_WG_Env.T_TK_Header_N_Group_Save_WG_Ticket wgTicket;

    public final static String PRODUCT_SAVE_RETURN_KEY = "PRODUCT_SAVE_RETURN_KEY";


    public WS_TK_Header_N_Group_Save() {
        super("WS_TK_Header_N_Group_Save");
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
            scheduleExecDao = new MD_Schedule_ExecDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            scn = bundle.getInt(TK_TicketDao.SCN, -1);
            ticketPrefix = bundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            ticketCode = bundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            mainUser = bundle.getInt(TK_TicketDao.MAIN_USER, -1);
            mainUserName = bundle.getString(TK_TicketDao.MAIN_USER_NAME);
            mainUserNick = bundle.getString(TK_TicketDao.MAIN_USER_NICK);
            forecast_time = bundle.getString(TK_TicketDao.FORECAST_TIME);
            start_date = bundle.getString(TK_TicketDao.START_DATE);
            forecast_date = bundle.getString(TK_TicketDao.FORECAST_DATE);
            timeAction = bundle.getString(TIME_ACTION, "");
            internalComments = bundle.getString(TK_TicketDao.INTERNAL_COMMENTS);
            move_other_date = bundle.getInt(MOVE_OTHER_DATE, 0);
            move_steps = bundle.getInt(MOVE_STEPS, 0);
            is_header_datetime_changes = bundle.getBoolean(IS_HEADER_DATETIME_CHANGES);
            wgTicket = (T_TK_Header_N_Group_Save_WG_Env.T_TK_Header_N_Group_Save_WG_Ticket) bundle.getSerializable(WORKGROUP_JSON_PARAM);
            //
            processTicketSave();
            //
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_TK_Header_N_Group_Save.completeWakefulIntent(intent);
        }

    }

    private void processTicketSave() throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_items_data"), "", "0");
        //
        String token = ToolBox_Inf.getToken(getApplicationContext());
        /*
            Metodo que configura campos de envio oriundos da tela de cabeçalho.
         */
        if(is_header_datetime_changes) {
            TK_Ticket ticket = getTicketDB(ticketPrefix, ticketCode);
            //
            setHeaderChanges(ticket);
            //
            ticketToSend.add(
                ticket
            );
            //Se lista vazia, dispara msg de erro.
            if (ticketToSend == null || ticketToSend.size() == 0) {
                String json = actReturnList != null ? gsonRec.toJson(actReturnList) : gsonRec.toJson(new ArrayList<>());
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), json, "0");
                return;
            }
            //
            T_TK_Ticket_Header_Group_Env env = new T_TK_Ticket_Header_Group_Env();
            //
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setTicket(ticketToSend);
            env.setToken(token);
            //
            callWsTicketSave(gsonEnv.toJson(env));
        }else{
            //todo tratar workgroup.
            T_TK_Header_N_Group_Save_WG_Env env = new T_TK_Header_N_Group_Save_WG_Env(
                Constant.PRJ001_CODE,
                Constant.PRJ001_VERSION,
                Constant.PKG_APP_TYPE_DEFAULT,
                ToolBox_Con.getPreference_Session_App(getApplicationContext()),
                token,
                wgTicket
            );
            //
            callWsTicketSave(gsonRec.toJson(env));
        }
    }

    private void setHeaderChanges(TK_Ticket ticket) {
        String formatted_apply_perc_steps = null;

        if (!"".equals(timeAction)) {
            //
            ticket.setTime_action(timeAction);
            switch(timeAction) {
                case ConstantBaseApp.TK_TICKET_START_DATE_AND_HEADER:
                case ConstantBaseApp.TK_TICKET_FORECAST_DATE_AND_HEADER:
                case ConstantBaseApp.TK_TICKET_FORECAST_TIME_AND_HEADER:
                case ConstantBaseApp.TK_TICKET_EDIT_HEADER:
                    //
                    if(internalComments != null) {
                        ticket.setInternal_comments(internalComments);
                    }
                    //
                    if (mainUser != -1) {
                        if(mainUser==0){
                            ticket.setMain_user(null);
                            ticket.setMain_user_name(null);
                            ticket.setMain_user_nick(null);
                        }else{
                            ticket.setMain_user(mainUser);
                            ticket.setMain_user_name(mainUserName);
                            ticket.setMain_user_nick(mainUserNick);
                        }
                    }
            }
            //
            switch(timeAction) {
                case ConstantBaseApp.TK_TICKET_START_DATE_AND_HEADER:
                case ConstantBaseApp.TK_TICKET_START_DATE:
                    ticket.setChange_date(start_date);
                    break;
                case ConstantBaseApp.TK_TICKET_FORECAST_DATE_AND_HEADER:
                case ConstantBaseApp.TK_TICKET_FORECAST_DATE:
                    ticket.setChange_date(forecast_date);
                    break;
                case ConstantBaseApp.TK_TICKET_FORECAST_TIME_AND_HEADER:
                case ConstantBaseApp.TK_TICKET_FORECAST_TIME:
                    long calculatted_forecast_time_edit=0;
                    //
                    if(forecast_time != null
                            && !forecast_time.isEmpty()) {

                        calculatted_forecast_time_edit = formatForecastTime(forecast_time);
                        long calculatted_forecast_time_old = formatForecastTime(ticket.getForecast_time());
                        if (move_steps == 1) {
                            if(calculatted_forecast_time_old != 0){
                                formatted_apply_perc_steps = String.valueOf(calculatted_forecast_time_edit / calculatted_forecast_time_old).replace(".", ",");
                            }else{
                                formatted_apply_perc_steps = "0";
                            }
                        }
                        ticket.setForecast_time(String.valueOf(calculatted_forecast_time_edit - calculatted_forecast_time_old));
                    }
                    break;
            }
            //
            ticket.setMove_steps(move_steps);
            ticket.setMove_other_date(move_other_date);
            //
            ticket.setApply_perc_steps(formatted_apply_perc_steps);
        }
    }

    /**
     * BARRRIONUEVO 09-12-2020
     * Metodo que calcula o forecast_time
     * O atributo vem como D HH:MM porem, deve ser enviado como minutos.
     * Estranho como a vida deve ser.
     *
     * @param forecast_time
     * @return
     */
    private long formatForecastTime(String forecast_time) {
        if(forecast_time != null) {
            String[] dayTimeSplit = forecast_time.split(" ");
            long timeSplit = 0;

            timeSplit = dayTimeSplit.length > 1 ? 24 * 60 * Integer.valueOf(dayTimeSplit[0]) : 0;
            int firstIdx = dayTimeSplit.length > 1 ? 1 : 0;
            String[] aux = dayTimeSplit[firstIdx].split(":");
            timeSplit += Integer.parseInt(aux[0]) * 60;
            timeSplit += Integer.parseInt(aux[1]);
            return timeSplit;
        }
        return 0;
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

    private TK_Ticket getTicketDB(int ticketPrefix, int ticketCode) {
        TK_Ticket ticket;

        ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        ticketPrefix,
                        ticketCode
                ).toSqlQuery()
        );
        ticket.setStep(new ArrayList<TK_Ticket_Step>());
        ticket.setProduct(new ArrayList<TK_Ticket_Product>());
        return ticket;
    }

    private void callWsTicketSave(String jsonEnv) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_SAVE,
            jsonEnv
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

//    private void callWsTicketSave(T_TK_Ticket_Header_Group_Env env) throws Exception {
//        //
//        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
//        //
//        String jsonEnv = gsonEnv.toJson(env);
//        String resultado = ToolBox_Con.connWebService(
//                Constant.WS_TICKET_SAVE,
//                jsonEnv
//        );
//        //
//        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
//        //
//        T_TK_Ticket_Save_Rec rec = gsonRec.fromJson(
//                resultado,
//                T_TK_Ticket_Save_Rec.class
//        );
//        //
//        if (
//                !ToolBox_Inf.processWSCheckValidation(
//                        getApplicationContext(),
//                        rec.getValidation(),
//                        rec.getError_msg(),
//                        rec.getLink_url(),
//                        1,
//                        1)
//                        ||
//                        !ToolBox_Inf.processoOthersError(
//                                getApplicationContext(),
//                                getResources().getString(R.string.generic_error_lbl),
//                                rec.getError_msg())
//        ) {
//            return;
//        }
//        //
//        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_processing_data"), "", "0");
//        //
//        processTicketSaveReturn(rec);
//    }

    private void processTicketSaveReturn(T_TK_Ticket_Save_Rec rec) throws Exception {
        if (ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(rec.getSave())
                || ConstantBaseApp.MAIN_RESULT_OK_DUP.equalsIgnoreCase(rec.getSave())
        ) {
            if (rec.getTicket_return() != null && rec.getTicket_return().size() > 0) {
                processActFeedback(rec);
            }
            //
            if (rec.getTicket() != null && rec.getTicket().size() > 0) {
                processTicketFull(rec.getTicket());
            }
            return;
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

    private void processActFeedback(T_TK_Ticket_Save_Rec rec) throws Exception {
        for (T_TK_Ticket_Save_Rec_Result recResult : rec.getTicket_return()) {
            WS_TK_Ticket_Save.TicketSaveActReturn actReturn = getActReturn(recResult);
            if (recResult.getStep() != null && recResult.getStep().size() > 0) {
                for (T_TK_Ticket_Save_Rec_Result_Step resultStep : recResult.getStep()) {
                    //if(ConstantBaseApp.MAIN_RESULT_OK.equals(resultStep.getRet_status())) {
                        actReturn.setRetMsg(
                            getFormattedRetMsg(actReturn, resultStep)
                        );
                    //}
                }
            }
            //
            actReturnList.add(actReturn);
        }
    }

    @NonNull
    private String getFormattedRetMsg(WS_TK_Ticket_Save.TicketSaveActReturn actReturn, T_TK_Ticket_Save_Rec_Result_Step resultStep) {
        String stepErroMsg = "";
        //
        boolean hasError = resultStep.getRet_msg() != null && !resultStep.getRet_msg().isEmpty();
        //
        if(hasError){
            if(!actReturn.isProcessError()) {
                actReturn.setProcessError(hasError);
            }
            if (resultStep.getStep_desc() != null) {
                stepErroMsg = "- " + resultStep.getStep_desc() + " : ";
                stepErroMsg += resultStep.getRet_msg() != null && !resultStep.getRet_msg().isEmpty() ? "\n" + resultStep.getRet_msg() : resultStep.getRet_status();
            }else{
                if (actReturn.getRetMsg() == null || actReturn.getRetMsg().isEmpty()) {
                    stepErroMsg += resultStep.getRet_msg() != null && !resultStep.getRet_msg().isEmpty() ? resultStep.getRet_msg() : resultStep.getRet_status();
                }else{
                    stepErroMsg += "- " + (resultStep.getRet_msg() != null && !resultStep.getRet_msg().isEmpty() ? resultStep.getRet_msg() : resultStep.getRet_status());
                }
            }
        }
        //
        if (actReturn.getRetMsg() == null || actReturn.getRetMsg().isEmpty()) {
            return stepErroMsg + "\n";
        } else {
            return actReturn.getRetMsg() + stepErroMsg + "\n";
        }
    }
    private WS_TK_Ticket_Save.TicketSaveActReturn getActReturn(T_TK_Ticket_Save_Rec_Result retResult) {
        WS_TK_Ticket_Save.TicketSaveActReturn actReturn = null;
        //
        actReturn = new WS_TK_Ticket_Save.TicketSaveActReturn(
            retResult.getCustomer_code(),
            retResult.getTicket_prefix(),
            retResult.getTicket_code(),
            retResult.getScn(),
            retResult.getRet_status(),
            retResult.getRet_msg(),
            reSend
        );
        actReturn.setProcessError(retResult.getRet_msg() != null && !retResult.getRet_msg().isEmpty());
        //
        return actReturn;
    }

    private void processTicketFull(ArrayList<TK_Ticket> tickets) {
        for (TK_Ticket tk_ticket : tickets) {
            //Seta pk nos filhos
            tk_ticket.setPK();
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
            if(!daoObjReturn.hasError()){
                callFinishProcessing(gsonRec.toJson(actReturnList));
            }else{
                ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    hmAux_Trans.get("msg_error_on_ticket_update"),
                    new HMAux(),
                    "",
                    "0");
            }
        }

    }


    private void callFinishProcessing(String jsonActReturn) {
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), jsonActReturn, "0");
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
        translist.add("msg_data_returned_error");
        translist.add("msg_error_on_ticket_update");
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
    }

}
