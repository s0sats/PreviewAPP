package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Rec_Result;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_005;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_006;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_ticket_save";
    //private String token;
    private String file_to_del = "";
    private boolean reSend = false;
    private boolean menuSendProcess;
    private Gson gsonEnv;
    private Gson gsonRec;
    private ArrayList<TK_Ticket> ticketToSend = new ArrayList<>();
    private ArrayList<Object> actReturnList = new ArrayList<>();
    private TK_TicketDao ticketDao;

    public WS_TK_Ticket_Save() { super("WS_TK_Ticket_Save");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            ticketDao = new TK_TicketDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            menuSendProcess = bundle.getBoolean(ConstantBaseApp.PROCESS_MENU_SEND,false);
            processTicketSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_TK_Ticket_Save.completeWakefulIntent(intent);
        }

    }

    private void processTicketSave() throws Exception {
        //Seleciona traduções
        loadTranslation();
        //Lista arquivos de token de Ticket
        File[] files = checkTicketTokenToSend();
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_loading_data_from_token"), "", "0");
            //
            file_to_del = files[0].getName();
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
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            //
            callWsTicketSave(env);
        }else{
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
            //Set update required do banco para 0
            for (TK_Ticket ticket : ticketToSend) {
                ticket.setToken(token);
            }
            //
            T_TK_Ticket_Save_Env env = new T_TK_Ticket_Save_Env();
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            env.setTicket(ticketToSend);
            env.setToken(token);
            //
            String json_token_content = gsonRec.toJson(env);
            File jsonToken = saveTokenAsFile(token, json_token_content);
            //
            file_to_del = jsonToken.getName();
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!checksumJsonToken(json_token_content, jsonToken)) {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("generic_token_file_creation_error"), "", "0");
                return;
            }
            //Set update required do banco para 0
            for (TK_Ticket ticket : ticketToSend) {
                ticketDao.addUpdate(
                    new TK_Ticket_Sql_005(
                        ticket.getCustomer_code(),
                        ticket.getTicket_prefix(),
                        ticket.getTicket_code(),
                        0
                    ).toSqlQuery()
                );
                //
            }
            //
            callWsTicketSave(env);
        }
    }

    private ArrayList<TK_Ticket> getTicketsToSend() {
        return (ArrayList<TK_Ticket>) ticketDao.query(
            new TK_Ticket_Sql_006(
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
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        //
        if( ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(rec.getSave())
            || ConstantBaseApp.MAIN_RESULT_OK_DUP.equalsIgnoreCase(rec.getSave()))
        {
            if(rec.getResult() != null && rec.getResult().size() > 0){
                for (T_TK_Ticket_Save_Rec_Result retResult : rec.getResult()) {
                    TicketSaveActReturn actReturn = getActReturn(retResult);
                    actReturnList.add(actReturn);
                    //
                    processTicketRet(retResult.getTicket(),actReturn);
                }
                //
                if (deleteFile(Constant.TOKEN_PATH, file_to_del)) {
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
                }
            }else{
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_data_returned_error"), "", "0");
            }
        }else{
            //COMO TRATAR, SERÁ QUE EXISTE ESSE OUTRO STATUS
            ToolBox.sendBCStatus(
                getApplicationContext(),
                "ERROR_1",
                hmAux_Trans.get("msg_data_returned_error") +":\n"+ rec.getSave(),
                new HMAux(),
                "",
                "0");
        }
    }

    private void callFinishProcessing(String jsonActReturn) {
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), jsonActReturn, "0");
    }

    private void processTicketRet(TK_Ticket retTicket, TicketSaveActReturn actReturn) throws Exception {
        if(retTicket != null){
            //Só atualizará o obj ticket se não for processamento de token
            //ou for processamento de token, mas o ticket nõ posusi mais dados a serem enviados
            if(!reSend || noMoreUpdate(retTicket) ) {
                //Seta PKs nos objs filhos
                retTicket.setPK();
                //Verifica se imagens já foram baixadas e atualiza campo com o local_path
                retTicket.updateLocalImagesPathIfExists();
                //Salva obj
                DaoObjReturn daoObjReturn = ticketDao.addUpdate(retTicket);
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getErrorMsg());
                }//
            }else{
                //Remove dados do processamento da lista, pois haverá um segundo processamento pro mesmo item
                actReturnList.remove(actReturn);
            }
        }
    }

    private boolean noMoreUpdate(TK_Ticket retTicket) {
        TK_Ticket dbTicket = ticketDao.getByString(
            new TK_Ticket_Sql_001(
                retTicket.getCustomer_code(),
                retTicket.getTicket_prefix(),
                retTicket.getTicket_code()
            ).toSqlQuery()
        );
        //
        return dbTicket != null && dbTicket.getUpdate_required() == 0;
    }

    private TicketSaveActReturn getActReturn(T_TK_Ticket_Save_Rec_Result retResult) {
        TicketSaveActReturn actReturn = new TicketSaveActReturn(
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

    //region Token Methods
    private boolean checksumJsonToken(String json_token_content, File jsonToken) {
        String md5Content = ToolBox_Inf.md5(json_token_content);
        //
        String md5File = ToolBox_Inf.md5(ToolBox_Inf.getContents(jsonToken));
        //
        return md5Content.equals(md5File);
    }

    private File saveTokenAsFile(String token, String json_token_content) throws IOException {
        File json_token = new File(Constant.TOKEN_PATH, Constant.TOKEN_TICKET_PREFIX + token + ".json");
        ToolBox_Inf.writeIn(json_token_content, json_token);
        return json_token;
    }

    private File[] checkTicketTokenToSend() {
        return ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_TICKET_PREFIX);
    }

    private boolean deleteFile(String path, String name) {
        File file = new File(path + "/" + name);

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
    //endregion

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
        private boolean fromTokenProcess =false;
        private boolean processError = false;
        private String processStatus = "";
        private String processMsg = "";
        //
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
    }

}
