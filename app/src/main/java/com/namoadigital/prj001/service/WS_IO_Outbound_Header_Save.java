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
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.IO_Outbound_Item;
import com.namoadigital.prj001.model.IO_Outbound_Save_Return;
import com.namoadigital.prj001.model.T_IO_Outbound_Header_Env;
import com.namoadigital.prj001.model.T_IO_Outbound_Header_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Header_Save;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Outbound_Header_Save extends IntentService {


    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_outbound_header_save";
    private IO_OutboundDao outboundDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String token;
    private IO_Outbound ioOutbound;
    private boolean newProcess;

    public WS_IO_Outbound_Header_Save() {
        super("ws_io_outbound_header_save");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            outboundDao = new IO_OutboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            ioOutbound = (IO_Outbound) bundle.getSerializable(ConstantBaseApp.IO_OBJ_KEY);
            newProcess = bundle.getBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY,false);
            //
            processWsOutboundSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Outbound_Header_Save.completeWakefulIntent(intent);
        }
    }

    private void processWsOutboundSave() throws Exception {
        token = ToolBox_Inf.getToken(getApplicationContext());
        ArrayList<IO_Outbound> outbounds = new ArrayList<>();
        //
        loadTranslation();
        //
        if(ioOutbound == null || ioOutbound.getToken() == null || ioOutbound.getToken().trim().length() == 0){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1",hmAux_Trans.get("msg_empty_outbound") , "", "0");
            return;
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_outbound_data"), "", "0");
        //
        T_IO_Outbound_Header_Env env = new T_IO_Outbound_Header_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(token);
        outbounds.add(ioOutbound);
        env.setOutbound(outbounds);
        //
        callWSOutboundSave(env);
    }

    private void callWSOutboundSave(T_IO_Outbound_Header_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_OUTBOUND_HEADER_SAVE,
                gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        T_IO_Outbound_Header_Rec rec = gsonRec.fromJson(
                resultado,
                T_IO_Outbound_Header_Rec.class
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
        processOutboundSaveRet(rec);
    }


    private void processOutboundSaveRet(T_IO_Outbound_Header_Rec rec) throws Exception {
        String outbound_list_ret = "";
        //Apesar de ser um loop como o cabeçalho é somente online,
        //somente um item DEVE ser retornado.
        //
        for(IO_Outbound_Save_Return outboundRet : rec.getResult()){
            OutboundHeaderSaveActReturn actReturn = new OutboundHeaderSaveActReturn(
                    (int) outboundRet.getCustomer_code(),
                    outboundRet.getOutbound_prefix(),
                    outboundRet.getOutbound_code(),
                    outboundRet.getRet_status().equalsIgnoreCase("OK"),
                    outboundRet.getRet_msg(),
                    newProcess,
                    outboundRet.getOutbound().size() > 0

            );
            //
            if(actReturn.isRetStatusOk()){
                //Se ret OK, verifica se outbound existe e se sim, atualiza no banco
                //e seta como "outbound full", passando o parametro 1 na chave da pk.
                if(outboundRet.getOutbound() != null && outboundRet.getOutbound().size() > 0){
                    //Seta pk no itens caso existam
                    for(IO_Outbound_Item outboundItem : outboundRet.getOutbound().get(0).getItems()){
                        outboundItem.setPK(outboundRet.getOutbound().get(0));
                    }
                    //
                    outboundDao.addUpdate(outboundRet.getOutbound().get(0));
                }else{
                    if(newProcess){
                        //LANÇAR EXCPETION LISTA VAZIA??!?!?!?!?
                    }else{
                        ioOutbound.setToken(null);
                        ioOutbound.setScn(outboundRet.getScn());
                        ioOutbound.setUpdate_required(0);
                        outboundDao.addUpdate(ioOutbound);
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
        translist.add("msg_empty_outbound");
        translist.add("msg_preparing_outbound_data");


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

    public class OutboundHeaderSaveActReturn{
        private int customer_code;
        private int outbound_prefix;
        private int outbound_code;
        private boolean retStatus;
        private String msg;
        private boolean newProcess;
        private boolean outboundFull;


        public OutboundHeaderSaveActReturn(int customer_code, int outbound_prefix, int outbound_code, boolean retStatus, String msg, boolean newProcess, boolean outboundFull) {
            this.customer_code = customer_code;
            this.outbound_prefix = outbound_prefix;
            this.outbound_code = outbound_code;
            this.retStatus = retStatus;
            this.msg = msg;
            this.newProcess = newProcess;
            this.outboundFull = outboundFull;
        }

        public int getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(int customer_code) {
            this.customer_code = customer_code;
        }

        public int getOutbound_prefix() {
            return outbound_prefix;
        }

        public void setOutbound_prefix(int outbound_prefix) {
            this.outbound_prefix = outbound_prefix;
        }

        public int getOutbound_code() {
            return outbound_code;
        }

        public void setOutbound_code(int outbound_code) {
            this.outbound_code = outbound_code;
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

        public boolean isOutboundFull() {
            return outboundFull;
        }

        public void setOutboundFull(boolean outboundFull) {
            this.outboundFull = outboundFull;
        }
    }
}
