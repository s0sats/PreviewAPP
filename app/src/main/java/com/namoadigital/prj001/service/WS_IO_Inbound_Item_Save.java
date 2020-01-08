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
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Save;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WS_IO_Inbound_Item_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_inbound_item_save";
    private IO_InboundDao inboundDao;
    private IO_Inbound_ItemDao inboundItemDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String token;
    private String file_to_del = "";
    private boolean reSend = false;
    private boolean menuSendProcess;
    private ArrayList<InboundItemSaveActReturn> actReturnList = new ArrayList<>();
    private ArrayList<T_IO_Inbound_Item_Env.IO_Inbound_Header> headerList = new ArrayList<>();

    public WS_IO_Inbound_Item_Save() {
        super("WS_IO_Inbound_Item_Save");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            inboundDao = new IO_InboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            inboundItemDao = new IO_Inbound_ItemDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            moveDao = new IO_MoveDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            menuSendProcess = bundle.getBoolean(ConstantBaseApp.PROCESS_MENU_SEND,false);
            //
            processWsInboundItemSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Inbound_Item_Save.completeWakefulIntent(intent);
        }
    }

    private void processWsInboundItemSave() throws Exception {
        //
        loadTranslation();
        //
        //
        //LUCHE - 08/01/2020
        //Unificado metodos do processo de envio com token no toolbox_inf após a adição do customer_code
        //no nome do arquivo
        //Lista arquivos de token de INBOUND
        File[] files = ToolBox_Inf.checkTokenToSend(
            getApplicationContext(),
            ConstantBaseApp.TOKEN_PATH,
            ConstantBaseApp.TOKEN_INBOUND_PREFIX
        );
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_itens_from_token"), "", "0");
            //
            file_to_del = files[0].getAbsolutePath();
            //
            reSend = true;
            //
            T_IO_Inbound_Item_Env env =
                gsonRec.fromJson(
                    ToolBox_Inf.getContents(files[0]),
                    T_IO_Inbound_Item_Env.class
                );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setReprocess(1);
            headerList = env.getInbound();
            //
            callWsInboundItemSave(env);

        } else {
            reSend = false;
            //
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_items_data"), "", "0");
            //Gera token
            String token = ToolBox_Inf.getToken(getApplicationContext());

            headerList = getItemToSave();

            //Se lista vazia, dispara msg de erro.
            if (headerList == null || headerList.size() == 0) {
                String json =  actReturnList != null ? gsonRec.toJson(actReturnList) : gsonRec.toJson(new ArrayList<>()) ;
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), new HMAux(), json  , "0");
                return;
            }
            //
            T_IO_Inbound_Item_Env env = new T_IO_Inbound_Item_Env();
            //
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setInbound(headerList);
            env.setReprocess(0);
            //
            String json_token_content = gsonRec.toJson(env);
            //
            String jsonFileName =
                ToolBox_Inf.buildTokenFileAbsPath(
                    getApplicationContext(),
                    ConstantBaseApp.TOKEN_INBOUND_PREFIX,
                    token
                );
            File jsonToken = ToolBox_Inf.saveTokenAsFile(jsonFileName, json_token_content);
            file_to_del = jsonToken.getAbsolutePath();
            //
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!ToolBox_Inf.checksumJsonToken(json_token_content, jsonToken)) {
                ToolBox_Inf.deleteFileWithRet(file_to_del);
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_token_file_error"), "", "0");
                return;
            }
            //Com arquivo token criado, seta update required para 0
            for (int i = 0; i < headerList.size(); i++) {
                //Atualiza header da Inbound para update required 0
                inboundDao.addUpdate(
                    new IO_Inbound_Sql_004(
                        headerList.get(i).getCustomer_code(),
                        headerList.get(i).getInbound_prefix(),
                        headerList.get(i).getInbound_code(),
                        0
                    ).toSqlQuery()

                );
                //Atualiza todos os itens da Inbound para update required 0
                inboundItemDao.addUpdate(new IO_Inbound_Item_Sql_005(
                        headerList.get(i).getCustomer_code(),
                        headerList.get(i).getInbound_prefix(),
                        headerList.get(i).getInbound_code(),
                        0
                    ).toSqlQuery()
                );
                //Atualiza update required das moves
                if (headerList.get(i).getMove() != null && headerList.get(i).getMove().size() > 0) {
                    for (IO_Move io_move : headerList.get(i).getMove()) {
                        //Atualiza header da Inbound para update required 0
                        moveDao.addUpdate(
                            new IO_Move_Order_Item_Sql_008(
                                io_move.getCustomer_code(),
                                io_move.getMove_prefix(),
                                io_move.getMove_code(),
                                0
                            ).toSqlQuery()
                        );
                    }
                }
            }
            //
            callWsInboundItemSave(env);
        }
    }

    private ArrayList<T_IO_Inbound_Item_Env.IO_Inbound_Header> getItemToSave() throws Exception {
        ArrayList<HMAux> inboundAux = new ArrayList<>();

        //Selecnio Inbound update_required
        inboundAux = (ArrayList<HMAux>) inboundDao.query_HM(
            new IO_Inbound_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
            ).toSqlQuery()
        );
        //Se tem inbonds, faz loop para selecionar dados.
        if (inboundAux != null && inboundAux.size() > 0) {
            for (HMAux hmAux : inboundAux) {
                if (hmAux == null
                    || hmAux.size() == 0
                    || !hmAux.hasConsistentValue(IO_InboundDao.CUSTOMER_CODE)
                    || !hmAux.hasConsistentValue(IO_InboundDao.INBOUND_PREFIX)
                    || !hmAux.hasConsistentValue(IO_InboundDao.INBOUND_CODE)
                    || !hmAux.hasConsistentValue(IO_InboundDao.SCN)
                ) {
                    //Gravar um exception ?!
                    //throw new Exception("Origin inbound not found");
                    break;
                } else {
                    T_IO_Inbound_Item_Env.IO_Inbound_Header header = new T_IO_Inbound_Item_Env.IO_Inbound_Header();
                    //
                    header.setCustomer_code(Long.parseLong(hmAux.get(IO_InboundDao.CUSTOMER_CODE)));
                    header.setInbound_prefix(Integer.parseInt(hmAux.get(IO_InboundDao.INBOUND_PREFIX)));
                    header.setInbound_code(Integer.parseInt(hmAux.get(IO_InboundDao.INBOUND_CODE)));
                    header.setScn(Integer.parseInt(hmAux.get(IO_InboundDao.SCN)));
                    //
                    header.getItems().addAll(
                        inboundItemDao.query(
                            new IO_Inbound_Item_Sql_008(
                                header.getCustomer_code(),
                                header.getInbound_prefix(),
                                header.getInbound_code()
                            ).toSqlQuery()
                        )
                    );
                    //
                    header.getMove().addAll(
                        moveDao.query(
                            new IO_Move_Order_Item_Sql_007(
                                header.getCustomer_code(),
                                header.getInbound_prefix(),
                                header.getInbound_code()
                            ).toSqlQuery()
                        )
                    );
                    //
                    headerList.add(header);
                }
            }
        }
        //
        return headerList;
    }

    private void callWsInboundItemSave(T_IO_Inbound_Item_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_IO_INBOUND_ITEM_SAVE,
            gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        T_IO_Inbound_Item_Rec rec = gsonRec.fromJson(
            resultado,
            T_IO_Inbound_Item_Rec.class
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

    private void processInboundSaveRet(T_IO_Inbound_Item_Rec rec) throws Exception {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        //Executa for no nivel de retorno da inbound.
        for (T_IO_Inbound_Item_Rec.IO_Inbound_Item_Save_Return saveReturn : rec.getResult()) {
            //Busca por filhos com status diferente de OK e se houver adiciona como item.
            for (T_IO_Inbound_Item_Rec.IO_Inbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
                InboundItemSaveActReturn actReturn = new InboundItemSaveActReturn();
                //Atualiza obj com dados do retorno master da inbound.
                //Por padrão seta o prefix e code comoo da inbound.Ficará assim se for in_conf
                actReturn.setCustomer_code((int) saveReturn.getCustomer_code());
                actReturn.setPrefix(saveReturn.getInbound_prefix());
                actReturn.setCode(saveReturn.getInbound_code());
                actReturn.setItem(saveReturnItem.getInbound_item());
                actReturn.setFromTokenProcess(reSend);
                //RetStatus nunca deveria ser null, mas se fora, considerar como OK
                actReturn.setRetStatus(saveReturnItem.getRet_status() == null ? "OK" : saveReturnItem.getRet_status());
                //Se  status diferente de OK, coloca msg, se não branco.
                actReturn.setMsg(
                    !actReturn.getRetStatus().equals("OK") ? saveReturnItem.getRet_msg() : null
                );
                //Se uma movimentação, seta o prefx e code da move e seta isMove como true.
                if(saveReturnItem.getMove_prefix() != null &&  saveReturnItem.getMove_code() != null){
                    actReturn.setPrefix(saveReturnItem.getMove_prefix());
                    actReturn.setCode(saveReturnItem.getMove_code());
                    actReturn.setMove(true);
                }
                //add o item na lista.
                actReturnList.add(actReturn);
            }
            //Nada será executado aqui, ja que somente rodará o inboundFull
            if (saveReturn.getRet_status().equals("OK")) {
//                for (T_IO_Inbound_Item_Rec.IO_Inbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
//                    //
//                    if (saveReturnItem.getRet_status().equals("OK")) {
//                        actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
//                        actReturn.setMsg(saveReturn.getRet_msg());
//                        actReturn.setInbound_item(saveReturnItem.getInbound_item());
//                        actReturn.setMove_prefix(saveReturnItem.getMove_prefix());
//                        actReturn.setMove_code(saveReturnItem.getMove_code());
//                        actReturnList.add(actReturn);
//                    }
//                }
            } else {
                //Se erro ao processar inbound, pega os itens e retorna ao update_required para 1.
                if (saveReturn.getRet_status().equals(ConstantBaseApp.SYS_STATUS_ERROR)) {
                    //busca cabeçalho para atualiza os itens
                    for (T_IO_Inbound_Item_Env.IO_Inbound_Header headerItem : headerList) {
                        //Se encontrou atualiza.
                        if (
                            headerItem.getCustomer_code() == saveReturn.getCustomer_code()
                                && headerItem.getInbound_prefix() == saveReturn.getInbound_prefix()
                                && headerItem.getInbound_code() == saveReturn.getInbound_code()
                        ) {
                            if (headerItem.getItems() != null && headerItem.getItems().size() > 0) {
//                                daoObjReturn = inboundItemDao.addUpdate(headerItem.getItems(), false);
//                                if (daoObjReturn.hasError()) {
//                                    throw new Exception(daoObjReturn.getErrorMsg());
//                                }
                                for (IO_Inbound_Item inboundItem : headerItem.getItems()) {
                                    inboundItemDao.addUpdate(
                                        new IO_Inbound_Item_Sql_009(
                                            inboundItem.getCustomer_code(),
                                            inboundItem.getInbound_prefix(),
                                            inboundItem.getInbound_code(),
                                            inboundItem.getInbound_item(),
                                            1
                                        ).toSqlQuery()
                                    );
                                }
                            }
                            //
                            if (headerItem.getMove() != null && headerItem.getMove().size() > 0) {
//                                daoObjReturn = moveDao.addUpdate(headerItem.getMove(), false);
//                                if (daoObjReturn.hasError()) {
//                                    throw new Exception(daoObjReturn.getErrorMsg());
//                                }
                                for (IO_Move io_move : headerItem.getMove()) {
                                    moveDao.addUpdate(
                                        new IO_Move_Order_Item_Sql_008(
                                            io_move.getCustomer_code(),
                                            io_move.getMove_prefix(),
                                            io_move.getMove_code(),
                                            1
                                        ).toSqlQuery()
                                    );
                                }
                            }
                            //Após processarsai do loop
                            break;
                        }
                    }
                }

            }
        }
        //Reseta daoObject
        daoObjReturn.clearError();
        //Processa as inbounds Full
        if (rec.getInbound() != null && rec.getInbound().size() > 0) {
            //Se envio do token, só pode executar inboundFull se não existir mais dados para serem enviados.
            if (reSend) {
                //Lista que sera passa para ser processada.
                ArrayList<IO_Inbound> inboundToProcess = new ArrayList<>();
                for (IO_Inbound inbound : rec.getInbound()) {
                    //Busca inbound no banco e somente se update_required = 0
                    //add na lista de processFull
                   HMAux auxIo =
                        inboundDao.getByStringHM(
                            new IO_Inbound_Sql_010(
                                inbound.getCustomer_code(),
                                inbound.getInbound_prefix(),
                                inbound.getInbound_code()
                            ).toSqlQuery()
                        );
                    //Verifica se inbound existe e se esta como update required
                    if (
                        auxIo != null
                        && auxIo.hasConsistentValue(IO_Inbound_Sql_010.HAS_UPDATE_TO_DO)
                        && auxIo.get(IO_Inbound_Sql_010.HAS_UPDATE_TO_DO).equalsIgnoreCase("0")
                    ) {
                        inboundToProcess.add(inbound);
                    }
                }
                daoObjReturn = inboundDao.processFull(inboundToProcess);
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getErrorMsg());
                }

            } else {
                daoObjReturn = inboundDao.processFull(rec.getInbound());
                //
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getErrorMsg());
                }
            }

        }
        //
        if (rec.getMove() != null && rec.getMove().size() > 0) {
            ArrayList<MD_Product_Serial> moveSerialList = new ArrayList<>();
            //
            for (IO_Move ioMove : rec.getMove()) {
                if (ioMove.getSerial() != null && ioMove.getSerial().size() > 0) {
                    moveSerialList.add(ioMove.getSerial().get(0));
                }
            }
            //
            daoObjReturn = moveDao.addUpdate(rec.getMove(), false);
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getErrorMsg());
            }
            serialDao.addUpdateTmp(moveSerialList, false);
            //
        }
        //
        if (ToolBox_Inf.deleteFileWithRet(file_to_del)) {
            if (reSend) {
                ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_items_data"), "", "0");
                //Reseta var de re transmissão.
                reSend = false;
                //Reseta lista de cab após processo de token
                headerList.clear();
                //
                processWsInboundItemSave();
            } else {
                String jsonActReturn = gsonRec.toJson(actReturnList);
                //
                callFinishProcessing(jsonActReturn);
            }
        } else {
            //VERIFICAR O QUYE FAZER NESSE CASO.
        }
    }

    private void callFinishProcessing(String actReturn) {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), new HMAux(), actReturn, "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_loading_itens_from_token");
        translist.add("msg_preparing_items_data");
        translist.add("msg_token_file_error");
        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_data");
        translist.add("msg_re_processing_items_data");
        translist.add("msg_save_ok");

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

    public static class InboundItemSaveActReturn {
        private int customer_code = -1;
        private int prefix = -1;
        private int code = -1;
        private Integer item = -1;
        private String retStatus ="";
        private boolean fromTokenProcess = false;
        private boolean isMove = false;
        private String msg = "";

        public InboundItemSaveActReturn() {
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

        public Integer getItem() {
            return item;
        }

        public void setItem(Integer item) {
            this.item = item;
        }

        public String getRetStatus() {
            return retStatus;
        }

        public void setRetStatus(String retStatus) {
            this.retStatus = retStatus;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isMove() {
            return isMove;
        }

        public void setMove(boolean move) {
            isMove = move;
        }

        public boolean isFromTokenProcess() {
            return fromTokenProcess;
        }

        public void setFromTokenProcess(boolean fromTokenProcess) {
            this.fromTokenProcess = fromTokenProcess;
        }

    }
}
