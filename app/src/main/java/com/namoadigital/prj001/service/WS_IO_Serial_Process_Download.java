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
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.T_IO_Serial_Process_Download_Env;
import com.namoadigital.prj001.model.T_IO_Serial_Process_Download_Move;
import com.namoadigital.prj001.model.T_IO_Serial_Process_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Serial_Process_Download;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Serial_Process_Download extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_serial_process_download";
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private MD_Product_SerialDao serialDao;


    public WS_IO_Serial_Process_Download() {
        super("WS_IO_Serial_Process_Download");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            serialDao = new MD_Product_SerialDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            String product_code = bundle.getString(MD_Product_SerialDao.PRODUCT_CODE);
            String serial_code = bundle.getString(MD_Product_SerialDao.SERIAL_CODE);

            processWsIoSerialProcessDownload(product_code, serial_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Serial_Process_Download.completeWakefulIntent(intent);
        }
    }

    private void processWsIoSerialProcessDownload(String product_code, String serial_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        T_IO_Serial_Process_Download_Env env = new T_IO_Serial_Process_Download_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setSerial_code(serial_code);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_SERIAL_PROCESS_DOWNLOAD,
                gson.toJson(env)
        );
        //
        T_IO_Serial_Process_Download_Rec rec = gson.fromJson(
                resultado,
                T_IO_Serial_Process_Download_Rec.class
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
        processResponse(rec);
    }

    private void processResponse(T_IO_Serial_Process_Download_Rec rec) {
        //Apagar a var hmAuxRet após todos os process serem implementados.
        HMAux hmAuxRet = new HMAux();

        if(rec.getProcess_type() != null && !rec.getProcess_type().isEmpty()) {
            switch (rec.getProcess_type()) {
                case ConstantBaseApp.IO_PROCESS_IN_CONF:
                    if(rec.getInbound() != null && rec.getInbound().size() > 0){
                        processInConfResponse(rec.getProcess_type(), rec.getInbound());
                    }else{
                        ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_empty_list"), "", "0");
                    }
                    break;
                case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
                    hmAuxRet.put(Constant.HMAUX_PROCESS_KEY,rec.getProcess_type());
                    sendCloseAct(hmAuxRet);
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
                    if(rec.getMove() != null && rec.getMove().size() > 0){
                        processMovePlannedResponse(rec.getProcess_type(), rec.getMove());
                    }else{
                        ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_empty_list"), "", "0");
                    }
                    break;
                case ConstantBaseApp.IO_PROCESS_MOVE:
                    if(rec.getMove() != null && rec.getMove().size() > 0){
                        processMoveResponse(rec.getProcess_type(),rec.getMove());
                    }else{
                        ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_empty_list"), "", "0");
                    }
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                    hmAuxRet.put(Constant.HMAUX_PROCESS_KEY,rec.getProcess_type());
                    sendCloseAct(hmAuxRet);
                    break;
                case ConstantBaseApp.IO_PROCESS_OUT_CONF:
                    if(rec.getOutbound() != null && rec.getOutbound().size() > 0){
                        processOutConfResponse(rec.getProcess_type(),rec.getOutbound());
                    }else{
                        ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_empty_list"), "", "0");
                    }
                    break;
            }
        }
    }

    private void processInConfResponse(String process_type, ArrayList<IO_Inbound> inbound) {
        HMAux hmAuxRet = new HMAux();
        hmAuxRet.put(Constant.HMAUX_PROCESS_KEY,process_type);
        //
        IO_InboundDao inboundDao = new IO_InboundDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        );
        //
        if(inbound.get(0) != null){
            //Seta pk nos itens da outbound
            for(IO_Inbound aux : inbound){
                aux.setPK();
            }
            //Insere outbound no banco.
            DaoObjReturn daoReturn = inboundDao.addUpdate(inbound.get(0));
            if (!daoReturn.hasError()) {
                if(inbound.get(0).getSerial() != null && inbound.get(0).getSerial().size() > 0) {
                    serialDao.addUpdateTmp(inbound.get(0).getSerial().get(0));
                }
                //
                hmAuxRet.put(Constant.HMAUX_PREFIX_KEY, String.valueOf(inbound.get(0).getInbound_prefix()));
                hmAuxRet.put(Constant.HMAUX_CODE_KEY, String.valueOf(inbound.get(0).getInbound_code()));
                //
                sendCloseAct(hmAuxRet);
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_in_conf"), "", "0");
            }

        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_in_conf"), "", "0");
        }

    }

    private void processOutConfResponse(String process_type, ArrayList<IO_Outbound> outbound) {
        HMAux hmAuxRet = new HMAux();
        hmAuxRet.put(Constant.HMAUX_PROCESS_KEY,process_type);
        //
        IO_OutboundDao outboundDao = new IO_OutboundDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        );
        //
        if(outbound.get(0) != null){
                //Seta pk nos itens da outbound
                for(IO_Outbound aux : outbound){
                    aux.setPK();
                }
                //Insere outbound no banco.
                DaoObjReturn daoReturn = outboundDao.addUpdate(outbound.get(0));
                if (!daoReturn.hasError()) {
                    if(outbound.get(0).getSerial() != null && outbound.get(0).getSerial().size() > 0) {
                        serialDao.addUpdateTmp(outbound.get(0).getSerial().get(0));
                    }
                    //
                    hmAuxRet.put(Constant.HMAUX_PREFIX_KEY, String.valueOf(outbound.get(0).getOutbound_prefix()));
                    hmAuxRet.put(Constant.HMAUX_CODE_KEY, String.valueOf(outbound.get(0).getOutbound_code()));
                    //
                    sendCloseAct(hmAuxRet);
                } else {
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_move_planned"), "", "0");
                }

        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_move_planned"), "", "0");
        }

    }

    /**
     * Processa response tipo MOVE_PLANNED
     *
     * Salva o retorno no banco de dados, pois já existe pk
     *
     * @param process_type
     * @param move - Array com movimentações planejadas
     */
    private void processMovePlannedResponse(String process_type, ArrayList<T_IO_Serial_Process_Download_Move> move) {
        HMAux hmAuxRet = new HMAux();
        hmAuxRet.put(Constant.HMAUX_PROCESS_KEY,process_type);
        //
        IO_MoveDao ioMoveDao = new IO_MoveDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        );
        //
        IO_Move io_move = T_IO_Serial_Process_Download_Move.getIO_MoveObj(move.get(0));
        //
        if(io_move != null){
            if(move.get(0).getSerial() != null && move.get(0).getSerial().size() > 0) {
                DaoObjReturn daoReturn = ioMoveDao.addUpdate(io_move);
                if (!daoReturn.hasError()) {
                    serialDao.addUpdateTmp(move.get(0).getSerial().get(0));
                    //
                    hmAuxRet.put(Constant.HMAUX_PREFIX_KEY, String.valueOf(io_move.getMove_prefix()));
                    hmAuxRet.put(Constant.HMAUX_CODE_KEY, String.valueOf(io_move.getMove_code()));
                    //
                    sendCloseAct(hmAuxRet);
                } else {
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_move_planned"), "", "0");
                }
            }
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_move_planned"), "", "0");
        }
    }

    /**
     * Processa response tipo MOVE
     * @param process_type
     * @param move - Array com movimentações
     */
    private void processMoveResponse(String process_type, ArrayList<T_IO_Serial_Process_Download_Move> move) {
        HMAux hmAuxRet = new HMAux();
        hmAuxRet.put(Constant.HMAUX_PROCESS_KEY, process_type);

        IO_Blind_MoveDao io_blind_moveDao = new IO_Blind_MoveDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        );

        //
        if(move.get(0).getSerial() != null && move.get(0).getSerial().size() > 0){

            IO_Blind_Move io_blind_move = T_IO_Serial_Process_Download_Move.getIO_Blind_MoveObj(move.get(0));

            io_blind_move.setBlind_tmp(getBlindTmp(io_blind_moveDao));
            io_blind_move.setSerial_id(move.get(0).getSerial().get(0).getSerial_id());

            serialDao.addUpdateTmp(move.get(0).getSerial().get(0));
            //
            hmAuxRet.put(Constant.HMAUX_BLIND_TMP_KEY, String.valueOf(io_blind_move.getBlind_tmp()));
            hmAuxRet.put(Constant.GS_PRODUCT_CODE, String.valueOf(io_blind_move.getProduct_code()));
            hmAuxRet.put(ConstantBaseApp.GS_SERIAL_ID, String.valueOf(io_blind_move.getSerial_id()));
            //
            sendCloseAct(hmAuxRet);
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_processing_move"), "", "0");
        }
    }

    private int getBlindTmp(IO_Blind_MoveDao io_blind_moveDao) {
        IO_Blind_Move blind_move = io_blind_moveDao.getByString(new IO_Blind_Move_Sql_002().toSqlQuery());
        if(blind_move == null){
            return 1;
        }
        return blind_move.getBlind_tmp() +1;
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
        translist.add("msg_error_processing_move_planned");
        translist.add("msg_error_processing_move");
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
