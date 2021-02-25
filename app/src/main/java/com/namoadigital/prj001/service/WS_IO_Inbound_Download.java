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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.T_IO_Inbound_Download_Env;
import com.namoadigital.prj001.model.T_IO_Inbound_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Download;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Inbound_Download extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_inbound_download";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_IO_Inbound_Download() {
        super("WS_IO_Inbound_Download");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            //
            String inboundPk = bundle.getString(IO_InboundDao.INBOUND_CODE);
            //
            processWsIoInboundDownload(inboundPk);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Inbound_Download.completeWakefulIntent(intent);
        }
    }

    private void processWsIoInboundDownload(String inboundPk)  throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        T_IO_Inbound_Download_Env env = new T_IO_Inbound_Download_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setInbound(inboundPk);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_INBOUND_DOWNLOAD,
                gson.toJson(env)
        );
        //
        T_IO_Inbound_Download_Rec rec = gson.fromJson(
                resultado,
                T_IO_Inbound_Download_Rec.class
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
        processResponse(rec.getInbound());
    }

    /**
     * Processa response tipo INBOUND
     * @param inbounds_list
     */
    private void processResponse(ArrayList<IO_Inbound> inbounds_list) {
        if(inbounds_list != null && inbounds_list.size() > 0 ) {
            //Set<MD_Product_Serial> serialHashList = new HashSet<>();
            HMAux hmAuxRet = new HMAux();
            hmAuxRet.put(Constant.HMAUX_PROCESS_KEY, Constant.IO_INBOUND);
            //
            IO_InboundDao inboundDao = new IO_InboundDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
//            MD_Product_SerialDao serialDao = new MD_Product_SerialDao(
//                    getApplicationContext(),
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
//                    Constant.DB_VERSION_CUSTOM
//            );
//            //Faz loop na lista retornada setando pk nos itens e adicionando seriais a serem salvos
//            //na lista de seriais.
//            for(IO_Inbound io_inbound: inbounds_list){
//                io_inbound.setPK();
//                serialHashList.addAll(io_inbound.getSerial());
//            }
//            //Inserte/Atualiza seriais
//            serialDao.addUpdateTmp(serialHashList,false);
//            //Insere / Atualiza lista de inbound
//            DaoObjReturn daoReturn = inboundDao.addUpdate(inbounds_list,false);
            DaoObjReturn daoReturn = inboundDao.processFull(inbounds_list);
            //Caso sucesso ao inserir inbound, envia retorno com a pk do item selecionado
            //ou sem pk se mais de um item for selecionado.
            if (!daoReturn.hasError()) {
                if(inbounds_list.size() == 1) {
                    hmAuxRet.put(Constant.HMAUX_PREFIX_KEY, String.valueOf(inbounds_list.get(0).getInbound_prefix()));
                    hmAuxRet.put(Constant.HMAUX_CODE_KEY, String.valueOf(inbounds_list.get(0).getInbound_code()));
                }
                //
                sendCloseAct(hmAuxRet);
            }else{
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_inbound"), "", "0");
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
        translist.add("msg_error_processing_inbound");
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
