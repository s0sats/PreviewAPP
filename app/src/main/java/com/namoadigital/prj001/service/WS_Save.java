package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSave_Env;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class WS_Save extends IntentService {

    private GE_Custom_Form_DataDao formDataDao;
    private GE_Custom_Form_Data_FieldDao formDataFieldDao;
    private GE_Custom_Form_LocalDao formLocalDao;
    private MD_Schedule_ExecDao scheduleExecDao;
    private TK_Ticket_StepDao ticketStepDao;
    //
    private String token;
    private List<GE_Custom_Form_Data> form_datas;
    private List<GE_Custom_Form_Data_Field> form_data_fields;
    //
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_Save";
    private String mSEND = "";
    private boolean mResend = false;
    private ArrayList<TSave_Rec.Error_Process> errorProcessList = new ArrayList<>();

    public WS_Save() {
        super("WS_Save");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            formDataDao = new GE_Custom_Form_DataDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );

            formDataFieldDao = new GE_Custom_Form_Data_FieldDao(
                            getApplicationContext(),
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                            Constant.DB_VERSION_CUSTOM
                    );
            //
            formLocalDao =
                new GE_Custom_Form_LocalDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
                );
            //
            scheduleExecDao = new MD_Schedule_ExecDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
            );
            //
            ticketStepDao = new TK_Ticket_StepDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
            );
            //
            form_datas = new ArrayList<>() ;
            form_data_fields = new ArrayList<>();
            //
            int jumpValidation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);
            mSEND = bundle.getString(Act005_Main.WS_PROCESS_SO_STATUS, "");

            processWS_Save(jumpValidation, jumpOD);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_Save.completeWakefulIntent(intent);

        }

    }

    private void processWS_Save(int jumpValidation, int jumpOD) throws Exception {

        //Seleciona traduções
        loadTranslation();

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_getting_finalized_forms"), "", "0");
        //Antigo não usar <- Até 09/08/18  esse comentario era valido.
        //Voltamos o esquema para excludeFieldsWithoutExposeAnnotation , para evitar campo duplicado no json.
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

        if(processPendingToken(1) == 0){
            processNewToken(0);
            mResend =false;
        }else{
            mResend =true;
        }
        //Verifica se existem dados a serem enviado
        //Se não existir, cancela a chamada do WS
        if(form_datas.size() == 0){
            if (mSEND.isEmpty()){
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_finalized_forms_found"), "", "0");
                return;
            } else {
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_finalized_forms_found"), hmAux_Trans.get("msg_no_finalized_forms_found"), "0");
                return;
            }
        }
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_forms"), "", "0");
        //
        TSave_Env env =  new TSave_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setSite_code(ToolBox_Con.getPreference_Site_Code(getApplicationContext()));
        env.setOperation_code(ToolBox_Con.getPreference_Operation_Code(getApplicationContext()));
        env.setForm_datas(form_datas);
        env.setForm_data_fields(form_data_fields);
        env.setToken(token);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SAVE,
                gson.toJson(env)
        );

        TSave_Rec rec = gson.fromJson(
                resultado,
                TSave_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                jumpValidation,
                jumpOD
                )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
            ) {
            return;
        }
        //Apos processar validation, processa o retorno do SAve
        checkSaveReturn(gson,rec.getSave(),rec.getError_msg(),rec.getError_process(), jumpValidation, jumpOD);

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_getting_finalized_forms");
        translist.add("msg_no_finalized_forms_found");
        translist.add("msg_sending_forms");
        translist.add("msg_forms_sent");
        translist.add("msg_error_token_excep");
        translist.add("msg_error_save");


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

    private int processPendingToken(int pending) {
        form_datas =
                    formDataDao.query(
                            new GE_Custom_Form_Data_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    pending
                            ).toSqlQuery()
                    );

        if(form_datas.size() > 0){

            form_data_fields =
                    formDataFieldDao.query(
                            new GE_Custom_Form_Data_Field_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                            ).toSqlQuery()
                    );

            //Atualiza token para o que esta pendente de envio
            token = form_datas.get(0).getToken();
        }

        return form_datas.size();
    }

    private int processNewToken(int pending) {
        form_datas =
                formDataDao.query(
                        new GE_Custom_Form_Data_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                pending
                        ).toSqlQuery()
                );
        token = ToolBox_Inf.getToken(getApplicationContext());
        if(form_datas.size() > 0){
            //Atualiza valor do token em todos os cabeçalhos
            for ( GE_Custom_Form_Data form_data:form_datas ) {
                 form_data.setToken(token);
            }

            formDataDao.addUpdate(form_datas,false);

            form_data_fields =
                    formDataFieldDao.query(
                            new GE_Custom_Form_Data_Field_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                            ).toSqlQuery()
                    );

        }

        return form_datas.size();

    }

    private boolean checkSaveReturn(Gson gson, String save, String error_msg, ArrayList<TSave_Rec.Error_Process> error_process, int jumpValidation, int jumpOD) throws Exception{
        HMAux hmAuxRet = new HMAux();
        switch (save){
            case "OK":
            case "OK_DUP":
                List<GE_Custom_Form_Local> formLocals = new ArrayList<>();
                List<MD_Schedule_Exec> formSchedules = new ArrayList<>();
                List<TK_Ticket_Ctrl> formTicketCtrl = new ArrayList<>();
                boolean isScheduleForm = false;
                boolean isTicketForm = false;
                //Se enviado com sucesso, atualiza Status para DONE
                for (GE_Custom_Form_Data form_data : form_datas){
                    //Se status DONE
                    form_data.setCustom_form_status(Constant.SYS_STATUS_DONE);
                    //Vars do novo agendamento
                    TSave_Rec.Error_Process errorProcess = null;
                    isScheduleForm = ToolBox_Inf.isScheduleForm(form_data);
                    isTicketForm = isFormCreateByTicket(form_data);
                    //LUCHE - 20/02/2020
                    //Tratativa pós novo agendamento que registra no banco e exibe o erro
                    //Resgata item com erro se houver.
                    if(isScheduleForm || isTicketForm) {
                        errorProcess = checkErrorProcess(
                            error_process,
                            form_data.getCustomer_code(),
                            form_data.getCustom_form_type(),
                            form_data.getCustom_form_code(),
                            form_data.getCustom_form_version(),
                            form_data.getCustom_form_data()
                        );
                        //Seta msg de erro no form_datase houver
                        form_data.setError_msg(errorProcess != null ? errorProcess.getError() : null);
                    }
                    //
                    try {
                        GE_Custom_Form_Local formLocal = processFormLocalSaveReturn(
                            form_data.getCustomer_code(),
                            form_data.getCustom_form_type(),
                            form_data.getCustom_form_code(),
                            form_data.getCustom_form_version(),
                            form_data.getCustom_form_data()
                        );
                        //Preenche dados no obj de erro.
                        if(errorProcess != null){
                            errorProcess.setCustom_form_type_desc(formLocal.getCustom_form_type_desc());
                            errorProcess.setCustom_form_desc(formLocal.getCustom_form_desc());
                            form_data.setCustom_form_status(ConstantBaseApp.SYS_STATUS_IGNORED);
                            formLocal.setCustom_form_status(ConstantBaseApp.SYS_STATUS_IGNORED);
                        }
                        //
                        formLocals.add(formLocal);
                    }catch (Exception e){
                        //TODO VERIFICAR SE DEVEMOS TRATAR AQUI O CASO DO FORM_DATA SEM FORM LOCAL
                        ToolBox_Inf.registerException(getClass().getName(),e);
                    }
                    if(isScheduleForm) {
                        //
                        MD_Schedule_Exec scheduleExec = processScheduleExecSaveReturn(
                            form_data.getCustomer_code(),
                            form_data.getSchedule_prefix(),
                            form_data.getSchedule_code(),
                            form_data.getSchedule_exec()
                        );
                        //Reseta informações de FCM
                        scheduleExec.setFcm_new_status(null);
                        scheduleExec.setFcm_user_nick(null);
                        //Seta msg de erro no agendamento, se houver
                        if(errorProcess != null && errorProcess.getError() != null ){
                            //Seta erro
                            scheduleExec.setSchedule_erro_msg(errorProcess.getError());
                            scheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_IGNORED);
                        }
                        scheduleExec.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                        //Add na lista
                        formSchedules.add(scheduleExec);
                        //Preenche dados no obj de erro.
                        if (errorProcess != null) {
                            errorProcess.setError_type(TSave_Rec.Error_Process.ERROR_TYPE_SCHEDULE);
                            errorProcess.setSchedule_pk(
                                ToolBox_Inf.formatSchedulePk(
                                    scheduleExec.getSchedule_prefix(),
                                    scheduleExec.getSchedule_code(),
                                    scheduleExec.getSchedule_exec()
                                )
                            );
                            //
                            errorProcess.setSchedule_desc(scheduleExec.getSchedule_desc());
                        }
                    }
                    //TODO Continuar daqui, salvar os dados no ctrl;
                    if(isTicketForm) {
                        //
                        TK_Ticket_Step ticketStep = processTicketStepSaveReturn(
                            form_data.getCustomer_code(),
                            form_data.getTicket_prefix(),
                            form_data.getTicket_code(),
                            form_data.getTicket_seq(),
                            form_data.getTicket_seq_tmp(),
                            form_data.getStep_code()
                        );
                        if (errorProcess != null) {
                            errorProcess.setError_type(TSave_Rec.Error_Process.ERROR_TYPE_TICKET);
                            //
                            errorProcess.setTicket_step_pk(
                                formatTicketStepPk(
                                    ticketStep.getTicket_prefix(), ticketStep.getTicket_code()
                                )
                            );
                            //
                            errorProcess.setTicket_step_desc(ticketStep.getStep_desc());
                        }
                    }
                    //
                    if(errorProcess != null) {
                        errorProcessList.add(errorProcess);
                    }
                }
                //Atualiza dados na tabela.
                formLocalDao.addUpdate(formLocals,false);
                scheduleExecDao.addUpdate(formSchedules,false);
                formDataDao.addUpdate(form_datas,false);
                /*27-08-2019 BARRIONUEVO
                   Controle de reprocessamento de n-form ao enviar registros com tokens
                 */
                if(mResend){
                    mResend = false;
                    processWS_Save(jumpValidation, jumpOD);
                    return  true;
                }else {
                    ToolBox_Inf.sendBCStatus(
                        getApplicationContext(),
                        "CLOSE_ACT", hmAux_Trans.get("msg_forms_sent"),
                        errorProcessList.size() > 0 ? gson.toJson(errorProcessList) : "",
                        "0"
                    );
                    //hmAuxRet.put(Constant.WS_SEND_RETURN,"OK");
                    //ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_forms_sent"),hmAuxRet, "", "0");
                    return true;
                }

            case "ERROR_TOKEN_EXCEPTION":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1",  hmAux_Trans.get("msg_error_token_excep"), "", "0");
                //hmAuxRet.put(Constant.WS_SEND_RETURN, hmAux_Trans.get("msg_error_token_excep"));
                //ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_error_token_excep"),hmAuxRet, "", "0");
                return false;
            default:
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_save") + " \n" + error_msg ,"" , "0");
                //hmAuxRet.put(Constant.WS_SEND_RETURN, hmAux_Trans.get("msg_error_save") + " \n" + error_msg);
                //ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_error_save"),hmAuxRet, "", "0");
                return false;
        }

    }

    private TK_Ticket_Step processTicketStepSaveReturn(long customer_code, Integer ticket_prefix, Integer ticket_code, Integer ticket_seq, Integer ticket_seq_tmp, Integer step_code) {
        TK_Ticket_Step auxStep =
            ticketStepDao.getByString(
                new TK_Ticket_Step_Sql_001(
                    customer_code,
                    ticket_prefix,
                    ticket_code,
                    step_code
                ).toSqlQuery()
            );
        //
        return auxStep;
    }

    public static String formatTicketStepPk(Integer ticket_prefix, Integer ticket_code) {
        if(ticket_prefix == null || ticket_code == null ){
            return "";
        }
        //
        return  ticket_prefix +"."+ ticket_code;
    }

    private MD_Schedule_Exec processScheduleExecSaveReturn(long customer_code, Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        //LUCHE - 14/02/2020
        MD_Schedule_Exec auxSchedule =
            scheduleExecDao.getByString(
                new MD_Schedule_Exec_Sql_001(
                    customer_code,
                    schedule_prefix,
                    schedule_code,
                    schedule_exec
                ).toSqlQuery()
            );
        //
        auxSchedule.setStatus(Constant.SYS_STATUS_DONE);
        //
        return auxSchedule;
    }

    private GE_Custom_Form_Local processFormLocalSaveReturn(long customer_code,
                                                            int custom_form_type,
                                                            int custom_form_code,
                                                            int custom_form_version,
                                                            long custom_form_data
                                                        ) throws Exception {
        GE_Custom_Form_Local aux =
            formLocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                    String.valueOf(customer_code),
                    String.valueOf(custom_form_type),
                    String.valueOf(custom_form_code),
                    String.valueOf(custom_form_version),
                    String.valueOf(custom_form_data)
                ).toSqlQuery()
            );
        //
        aux.setCustom_form_status(Constant.SYS_STATUS_DONE);
        //
        return aux;
    }

    public boolean isFormCreateByTicket(GE_Custom_Form_Data geCustomFormData) {
        return
            geCustomFormData.getTicket_prefix() != null && geCustomFormData.getTicket_prefix() > -1
                && geCustomFormData.getTicket_code() != null && geCustomFormData.getTicket_code() > -1
                && geCustomFormData.getTicket_seq() != null && geCustomFormData.getTicket_seq() > -1
                && geCustomFormData.getTicket_seq_tmp() != null && geCustomFormData.getTicket_seq_tmp()  > -1
                && geCustomFormData.getStep_code() != null && geCustomFormData.getStep_code() > -1;
    }

    private TSave_Rec.Error_Process checkErrorProcess(ArrayList<TSave_Rec.Error_Process> error_process_list, long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, long custom_form_data) {
        for (TSave_Rec.Error_Process error : error_process_list) {
            if(
                error.getCustomer_code() == customer_code
                && error.getCustom_form_type() == custom_form_type
                && error.getCustom_form_code() == custom_form_code
                && error.getCustom_form_version() == custom_form_version
                && error.getCustom_form_data() == custom_form_data
            ){
                return error;
            }
        }
        //
        return null;
    }
}
