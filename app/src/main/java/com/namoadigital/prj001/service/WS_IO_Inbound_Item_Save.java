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
    private String mModule_Code = Constant.APP_MODULE;
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
        super("ws_io_inbound_item_save");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            inboundDao = new IO_InboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            inboundItemDao = new IO_Inbound_ItemDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            moveDao = new IO_MoveDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
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
        //Lista arquivos de token de SO
        File[] files = checkInboundTokenToSend();
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_serial_from_token"), "", "0");
            //
            file_to_del = files[0].getName();
            //
            reSend = true;
            //
            T_IO_Inbound_Item_Env env =
                gsonRec.fromJson(
                    ToolBox_Inf.getContents(files[0]),
                    T_IO_Inbound_Item_Env.class
                );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            env.setReprocess(1);
            headerList = env.getInbound();
            //
            callWsInboundItemSave(env);

        } else {
            reSend = false;
            //
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_serial_data"), "", "0");
            //Gera token
            String token = ToolBox_Inf.getToken(getApplicationContext());

            headerList = getItemToSave();

            //Se lista vazia, dispara msg de erro.
            if (headerList == null || headerList.size() == 0) {
                if (menuSendProcess) {
                    HMAux auxApReturned = new HMAux();
                    ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), auxApReturned, "", "0");
                } else {
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_serial_to_update"), "", "0");
                }
                return;
            }
            //
            T_IO_Inbound_Item_Env env = new T_IO_Inbound_Item_Env();
            //
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setInbound(headerList);
            env.setReprocess(0);
            //
            String json_token_content = gsonRec.toJson(env);
            File jsonToken = saveTokenAsFile(token, json_token_content);
            //
            file_to_del = jsonToken.getName();
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!checksumJsonToken(json_token_content, jsonToken)) {
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
                        io_move.setUpdate_required(0);
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
            InboundItemSaveActReturn actReturn = new InboundItemSaveActReturn();
            //Atualiza obj com dados do retorno master da inbound.
            actReturn.setCustomer_code((int) saveReturn.getCustomer_code());
            actReturn.setInbound_prefix(saveReturn.getInbound_prefix());
            actReturn.setInbound_code(saveReturn.getInbound_code());
            actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
            actReturn.setMsg(saveReturn.getRet_msg());
            //Busca por filhos com status diferente de OK e se houver adiciona como item.
            for (T_IO_Inbound_Item_Rec.IO_Inbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
                //
                if (!saveReturnItem.getRet_status().equals("OK")) {
                    InboundItemSaveActReturn.InboundItemSaveInfo itemInfos = new InboundItemSaveActReturn.InboundItemSaveInfo();
                    itemInfos.setInbound_item(saveReturnItem.getInbound_item());
                    itemInfos.setMove_prefix(saveReturnItem.getMove_prefix());
                    itemInfos.setMove_code(saveReturnItem.getMove_code());
                    itemInfos.setMsg(saveReturnItem.getRet_msg());
                    //
                    actReturn.getItems().add(itemInfos);
                }
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
            //
            actReturnList.add(actReturn);
            //
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
                    IO_Inbound auxIo =
                        inboundDao.getByString(
                            new IO_Inbound_Sql_002(
                                inbound.getCustomer_code(),
                                inbound.getInbound_prefix(),
                                inbound.getInbound_code()
                            ).toSqlQuery()
                        );
                    //Verifica se inbound existe e se esta como update required
                    if (auxIo != null && auxIo.getCustomer_code() > 0) {
                        if (auxIo.getUpdate_required() == 0) {
                            inboundToProcess.add(auxIo);
                        }
                    } else {
                        //iSSO NÃO DEVERIA ACONTECER... MAS O QUE FAZER?
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
        if (deleteFile(Constant.TOKEN_PATH, file_to_del)) {
            if (reSend) {
                ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_so_data"), "", "0");
                //Reseta var de re transmissão.
                reSend = false;
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
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), actReturn, "0");
    }

    //region Token Methods
    private boolean checksumJsonToken(String json_token_content, File jsonToken) {
        String md5Content = ToolBox_Inf.md5(json_token_content);
        //
        String md5File = ToolBox_Inf.md5(ToolBox_Inf.getContents(jsonToken));
        //
        return md5Content.equals(md5File);
    }

    private File saveTokenAsFile(String token, String json_token_content) throws Exception {
        File json_token = new File(Constant.TOKEN_PATH, Constant.TOKEN_INBOUND_PREFIX + token + ".json");
        ToolBox_Inf.writeIn(json_token_content, json_token);
        return json_token;
    }

    private File[] checkInboundTokenToSend() {
        return ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_INBOUND_PREFIX);
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

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_data");
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

    public static class InboundItemSaveActReturn {
        private int customer_code = -1;
        private int inbound_prefix = -1;
        private int inbound_code = -1;
        private boolean retStatus = false;
        private String msg = "";
        private boolean fromTokenProcess = false;
        private ArrayList<InboundItemSaveInfo> items = new ArrayList<>();

        public InboundItemSaveActReturn() {
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

        public boolean isRetStatus() {
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

        public ArrayList<InboundItemSaveInfo> getItems() {
            return items;
        }

        public void setItems(ArrayList<InboundItemSaveInfo> items) {
            this.items = items;
        }

        public boolean isFromTokenProcess() {
            return fromTokenProcess;
        }

        public void setFromTokenProcess(boolean fromTokenProcess) {
            this.fromTokenProcess = fromTokenProcess;
        }

        public static class InboundItemSaveInfo {
            private Integer inbound_item;
            private Integer move_prefix;
            private Integer move_code;
            private String msg;

            public Integer getMove_prefix() {
                return move_prefix;
            }

            public void setMove_prefix(Integer move_prefix) {
                this.move_prefix = move_prefix;
            }

            public Integer getMove_code() {
                return move_code;
            }

            public void setMove_code(Integer move_code) {
                this.move_code = move_code;
            }

            public Integer getInbound_item() {
                return inbound_item;
            }

            public void setInbound_item(Integer inbound_item) {
                this.inbound_item = inbound_item;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
    }
}
