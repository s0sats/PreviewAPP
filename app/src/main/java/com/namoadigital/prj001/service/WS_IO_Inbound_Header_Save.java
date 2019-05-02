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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Header_Save;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Inbound_Header_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_inbound_header_save";
    private IO_InboundDao inboundDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String token;
    private IO_Inbound ioInbound;
    private boolean newProcess;

    public WS_IO_Inbound_Header_Save() {
        super("ws_io_inbound_header_save");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            inboundDao = new IO_InboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            ioInbound = (IO_Inbound) bundle.getSerializable(ConstantBaseApp.IO_OBJ_KEY);
            newProcess = bundle.getBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY,false);
            //
            processWsInboundSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Inbound_Header_Save.completeWakefulIntent(intent);
        }
    }

    private void processWsInboundSave() throws Exception {
        token = ToolBox_Inf.getToken(getApplicationContext());
        ArrayList<IO_Inbound> inbounds = new ArrayList<>();
        //
        loadTranslation();
        //
        if(ioInbound == null || ioInbound.getToken() == null || ioInbound.getToken().trim().length() == 0){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1",hmAux_Trans.get("msg_empty_inbound") , "", "0");
            return;
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_inbound_data"), "", "0");
        //
        T_IO_Inbound_Header_Env env = new T_IO_Inbound_Header_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(token);
        inbounds.add(ioInbound);
        env.setInbound(inbounds);
        //
        callWSInboundSave(env);
    }

    private void callWSInboundSave(T_IO_Inbound_Header_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_IO_INBOUND_HEADER_SAVE,
            gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        T_IO_Inbound_Header_Rec rec = gsonRec.fromJson(
            resultado,
            T_IO_Inbound_Header_Rec.class
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
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_data"), "", "0");
        //
        processInboundSaveRet(rec);
    }


    private void processInboundSaveRet(T_IO_Inbound_Header_Rec rec) throws Exception {
        String inbound_list_ret = "";
        //Apesar de ser um loop como o cabeçalho é somente online,
        //somente um item DEVE ser retornado.
        //
        for(IO_Inbound_Save_Return inboundRet : rec.getResult()){
            InboundHeaderSaveActReturn actReturn = new InboundHeaderSaveActReturn(
                (int) inboundRet.getCustomer_code(),
                inboundRet.getInbound_prefix(),
                inboundRet.getInbound_code(),
                inboundRet.getRet_status().equalsIgnoreCase("OK"),
                inboundRet.getRet_status(),
                newProcess,
                inboundRet.getInbound().size() > 0

            );
            //
            if(actReturn.isRetStatusOk()){
                //Se ret OK, verifica se inbound existe e se sim, atualiza no banco
                //e seta como "inbound full", passando o parametro 1 na chave da pk.
                if(inboundRet.getInbound() != null && inboundRet.getInbound().size() > 0){
                    //Seta pk no itens caso existam
                    for(IO_Inbound_Item inboundItem : inboundRet.getInbound().get(0).getItems()){
                        inboundItem.setPK(inboundRet.getInbound().get(0));
                    }
                    //
                    inboundDao.addUpdate(inboundRet.getInbound().get(0));
                }else{
                    if(newProcess){
                        //LANÇAR EXCPETION LISTA VAZIA??!?!?!?!?
                    }else{
                        ioInbound.setToken(null);
                        ioInbound.setScn(inboundRet.getScn());
                        ioInbound.setUpdate_required(0);
                        inboundDao.addUpdate(ioInbound);
                    }
                }
            }
            //
            ToolBox.sendBCStatus(
                getApplicationContext(),
                "CLOSE_ACT",
                hmAux_Trans.get("msg_process_finalized"),
                gsonRec.toJson(actReturn),
                "0");
        }

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_data");
        translist.add("msg_process_finalized");
        translist.add("msg_empty_inbound");
        translist.add("msg_preparing_inbound_data");


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

    public class InboundHeaderSaveActReturn{
        private int customer_code;
        private int inbound_prefix;
        private int inbound_code;
        private boolean retStatus;
        private String msg;
        private boolean newProcess;
        private boolean inboundFull;


        public InboundHeaderSaveActReturn(int customer_code, int inbound_prefix, int inbound_code, boolean retStatus, String msg, boolean newProcess, boolean inboundFull) {
            this.customer_code = customer_code;
            this.inbound_prefix = inbound_prefix;
            this.inbound_code = inbound_code;
            this.retStatus = retStatus;
            this.msg = msg;
            this.newProcess = newProcess;
            this.inboundFull = inboundFull;
        }

        public int getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(int customer_code) {
            this.customer_code = customer_code;
        }

        public int getInbound_prefix() {
            return inbound_prefix;
        }

        public void setInbound_prefix(int inbound_prefix) {
            this.inbound_prefix = inbound_prefix;
        }

        public int getInbound_code() {
            return inbound_code;
        }

        public void setInbound_code(int inbound_code) {
            this.inbound_code = inbound_code;
        }

        public boolean isRetStatusOk() {
            return retStatus;
        }

        public void setRetStatus(boolean retStatus) {
            this.retStatus = retStatus;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isNewProcess() {
            return newProcess;
        }

        public void setNewProcess(boolean newProcess) {
            this.newProcess = newProcess;
        }

        public boolean isInboundFull() {
            return inboundFull;
        }

        public void setInboundFull(boolean inboundFull) {
            this.inboundFull = inboundFull;
        }
    }
}
