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
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.T_IO_Outbound_Download_Env;
import com.namoadigital.prj001.model.T_IO_Outbound_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Download;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WS_IO_Outbound_Download extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_outbound_download";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_IO_Outbound_Download() {
        super("WS_IO_Outbound_Download");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            //
            String outboundPk = bundle.getString(IO_OutboundDao.OUTBOUND_CODE);
            //
            processWsIoOutboundDownload(outboundPk);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Outbound_Download.completeWakefulIntent(intent);
        }
    }

    private void processWsIoOutboundDownload(String outboundPk) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        T_IO_Outbound_Download_Env env = new T_IO_Outbound_Download_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setOutbound(outboundPk);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_OUTBOUND_DOWNLOAD,
                gson.toJson(env)
        );
        //
        T_IO_Outbound_Download_Rec rec = gson.fromJson(
                resultado,
                T_IO_Outbound_Download_Rec.class
        );
        //
        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                1,
                1
        )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
        ) {
            return;
        }
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_data"), "", "0");
        //
        processResponse(rec.getOutbound());
    }

    /**
     * Processa response tipo OUTBOUND
     * @param outbounds_list
     */
    private void processResponse(ArrayList<IO_Outbound> outbounds_list) {
        if(outbounds_list != null && outbounds_list.size() > 0 ) {

            HMAux hmAuxRet = new HMAux();
            hmAuxRet.put(Constant.HMAUX_PROCESS_KEY, Constant.IO_OUTBOUND);
            //
            IO_OutboundDao outboundDao = new IO_OutboundDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            MD_Product_SerialDao serialDao = new MD_Product_SerialDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //Inserte/Atualiza seriais

            //Insere / Atualiza lista de outbound
            DaoObjReturn daoReturn = outboundDao.processFull(outbounds_list);
            //Caso sucesso ao inserir outbound, envia retorno com a pk do item selecionado
            //ou sem pk se mais de um item for selecionado.
            if (!daoReturn.hasError()) {
                if(outbounds_list.size() == 1) {
                    hmAuxRet.put(Constant.HMAUX_PREFIX_KEY, String.valueOf(outbounds_list.get(0).getOutbound_prefix()));
                    hmAuxRet.put(Constant.HMAUX_CODE_KEY, String.valueOf(outbounds_list.get(0).getOutbound_code()));
                }
                //
                sendCloseAct(hmAuxRet);
            }else{
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_outbound"), "", "0");
            }
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_empty_list"), "", "0");
        }

    }

    /**
     * Envia close act com HmAux.
     * @param hmAuxRet
     */
    private void sendCloseAct(HMAux hmAuxRet) {
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "CLOSE_ACT",
                hmAux_Trans.get("msg_process_finalized"),
                hmAuxRet,
                "",
                "0");

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_data");
        translist.add("msg_empty_list");
        translist.add("msg_error_processing_outbound");
        translist.add("msg_process_finalized");

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
}
