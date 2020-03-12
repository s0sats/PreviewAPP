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
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.IO_Outbound_Item;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.T_IO_Outbound_Item_Env;
import com.namoadigital.prj001.model.T_IO_Outbound_Item_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Item_Save;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_008;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_013;
import com.namoadigital.prj001.sql.IO_Outbound_Item_Sql_005;
import com.namoadigital.prj001.sql.IO_Outbound_Item_Sql_008;
import com.namoadigital.prj001.sql.IO_Outbound_Item_Sql_009;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_004;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_009;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_010;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WS_IO_Outbound_Item_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_outbound_item_save";
    private IO_OutboundDao outboundDao;
    private IO_Outbound_ItemDao outboundItemDao;
    private MD_Product_SerialDao serialDao;
    private IO_MoveDao moveDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String token;
    private String file_to_del = "";
    private boolean reSend = false;
    private boolean menuSendProcess;
    private ArrayList<OutboundItemSaveActReturn> actReturnList = new ArrayList<>();
    private ArrayList<T_IO_Outbound_Item_Env.IO_Outbound_Header> headerList = new ArrayList<>();

    public WS_IO_Outbound_Item_Save() {
        super("WS_IO_Outbound_Item_Save");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            outboundDao = new IO_OutboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            outboundItemDao = new IO_Outbound_ItemDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            moveDao = new IO_MoveDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            menuSendProcess = bundle.getBoolean(ConstantBaseApp.PROCESS_MENU_SEND,false);
            //
            processWsOutboundItemSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_IO_Outbound_Item_Save.completeWakefulIntent(intent);
        }
    }

    private void processWsOutboundItemSave() throws Exception {
        //
        loadTranslation();
        //
        //LUCHE - 08/01/2020
        //Unificado metodos do processo de envio com token no toolbox_inf após a adição do customer_code
        //no nome do arquivo
        //Lista arquivos de token de OUTBOUND
        File[] files = ToolBox_Inf.checkTokenToSend(
            getApplicationContext(),
            ConstantBaseApp.TOKEN_PATH,
            ConstantBaseApp.TOKEN_OUTBOUND_PREFIX
        );
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_itens_from_token"), "", "0");
            //
            file_to_del = files[0].getAbsolutePath();
            //
            reSend = true;
            //
            T_IO_Outbound_Item_Env env =
                    gsonRec.fromJson(
                            ToolBox_Inf.getContents(files[0]),
                            T_IO_Outbound_Item_Env.class
                    );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setReprocess(1);
            headerList = env.getOutbound();
            //
            callWsOutboundItemSave(env);

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
            T_IO_Outbound_Item_Env env = new T_IO_Outbound_Item_Env();
            //
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setOutbound(headerList);
            env.setReprocess(0);
            //
            String json_token_content = gsonRec.toJson(env);
            //
            String jsonFileName =
                ToolBox_Inf.buildTokenFileAbsPath(
                    getApplicationContext(),
                    ConstantBaseApp.TOKEN_OUTBOUND_PREFIX,
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
                //Atualiza header da Outbound para update required 0
                outboundDao.addUpdate(
                        new IO_Outbound_Sql_004(
                                headerList.get(i).getCustomer_code(),
                                headerList.get(i).getOutbound_prefix(),
                                headerList.get(i).getOutbound_code(),
                                0
                        ).toSqlQuery()

                );
                //Atualiza todos os itens da Outbound para update required 0
                outboundItemDao.addUpdate(new IO_Outbound_Item_Sql_005(
                                headerList.get(i).getCustomer_code(),
                                headerList.get(i).getOutbound_prefix(),
                                headerList.get(i).getOutbound_code(),
                                0
                        ).toSqlQuery()
                );
                //Atualiza update required das moves
                if (headerList.get(i).getMove() != null && headerList.get(i).getMove().size() > 0) {
                    for (IO_Move io_move : headerList.get(i).getMove()) {
                        //Atualiza header da Outbound para update required 0
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
            callWsOutboundItemSave(env);
        }
    }

    private ArrayList<T_IO_Outbound_Item_Env.IO_Outbound_Header> getItemToSave() throws Exception {
        ArrayList<HMAux> outboundAux = new ArrayList<>();

        //Selecnio Outbound update_required
        outboundAux = (ArrayList<HMAux>) outboundDao.query_HM(
                new IO_Outbound_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
        );
        //Se tem inbonds, faz loop para selecionar dados.
        if (outboundAux != null && outboundAux.size() > 0) {
            for (HMAux hmAux : outboundAux) {
                if (hmAux == null
                        || hmAux.size() == 0
                        || !hmAux.hasConsistentValue(IO_OutboundDao.CUSTOMER_CODE)
                        || !hmAux.hasConsistentValue(IO_OutboundDao.OUTBOUND_PREFIX)
                        || !hmAux.hasConsistentValue(IO_OutboundDao.OUTBOUND_CODE)
                        || !hmAux.hasConsistentValue(IO_OutboundDao.SCN)
                ) {
                    //Gravar um exception ?!
                    //throw new Exception("Origin outbound not found");
                    break;
                } else {
                    T_IO_Outbound_Item_Env.IO_Outbound_Header header = new T_IO_Outbound_Item_Env.IO_Outbound_Header();
                    //
                    header.setCustomer_code(Long.parseLong(hmAux.get(IO_OutboundDao.CUSTOMER_CODE)));
                    header.setOutbound_prefix(Integer.parseInt(hmAux.get(IO_OutboundDao.OUTBOUND_PREFIX)));
                    header.setOutbound_code(Integer.parseInt(hmAux.get(IO_OutboundDao.OUTBOUND_CODE)));
                    header.setScn(Integer.parseInt(hmAux.get(IO_OutboundDao.SCN)));
                    //
                    header.getItems().addAll(
                            outboundItemDao.query(
                                    new IO_Outbound_Item_Sql_008(
                                            header.getCustomer_code(),
                                            header.getOutbound_prefix(),
                                            header.getOutbound_code()
                                    ).toSqlQuery()
                            )
                    );
                    //
                    header.getMove().addAll(
                            moveDao.query(
                                    new IO_Move_Order_Item_Sql_013(
                                            header.getCustomer_code(),
                                            header.getOutbound_prefix(),
                                            header.getOutbound_code()
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

    private void callWsOutboundItemSave(T_IO_Outbound_Item_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_OUTBOUND_ITEM_SAVE,
                gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        T_IO_Outbound_Item_Rec rec = gsonRec.fromJson(
                resultado,
                T_IO_Outbound_Item_Rec.class
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

    private void processOutboundSaveRet(T_IO_Outbound_Item_Rec rec) throws Exception {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        //Executa for no nivel de retorno da outbound.
        for (T_IO_Outbound_Item_Rec.IO_Outbound_Item_Save_Return saveReturn : rec.getResult()) {
            //Busca por filhos com status diferente de OK e se houver adiciona como item.
            for (T_IO_Outbound_Item_Rec.IO_Outbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
                OutboundItemSaveActReturn actReturn = new OutboundItemSaveActReturn();
                //Atualiza obj com dados do retorno master da outbound.
                //Por padrão seta o prefix e code comoo da outbound.Ficará assim se for in_conf
                actReturn.setCustomer_code((int) saveReturn.getCustomer_code());
                actReturn.setPrefix(saveReturn.getOutbound_prefix());
                actReturn.setCode(saveReturn.getOutbound_code());
                actReturn.setItem(saveReturnItem.getOutbound_item());
                actReturn.setFromTokenProcess(reSend);
                //RetStatus nunca deveria ser null, mas se fora, considerar como OK
                actReturn.setRetStatus(saveReturnItem.getRet_status() == null ? "OK" : saveReturnItem.getRet_status());
                //Se  status diferente de OK, coloca msg, se não branco.
                actReturn.setMsg(
                        !actReturn.getMsg().equals("OK") ? saveReturnItem.getRet_msg() : null
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
            //Nada será executado aqui, ja que somente rodará o outboundFull
            if (saveReturn.getRet_status().equals("OK")) {
//                for (T_IO_Outbound_Item_Rec.IO_Outbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
//                    //
//                    if (saveReturnItem.getRet_status().equals("OK")) {
//                        actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
//                        actReturn.setMsg(saveReturn.getRet_msg());
//                        actReturn.setOutbound_item(saveReturnItem.getOutbound_item());
//                        actReturn.setMove_prefix(saveReturnItem.getMove_prefix());
//                        actReturn.setMove_code(saveReturnItem.getMove_code());
//                        actReturnList.add(actReturn);
//                    }
//                }
            } else {
                //Se erro ao processar outbound, pega os itens e retorna ao update_required para 1.
                if (saveReturn.getRet_status().equals(ConstantBaseApp.SYS_STATUS_ERROR)) {
                    //busca cabeçalho para atualiza os itens
                    for (T_IO_Outbound_Item_Env.IO_Outbound_Header headerItem : headerList) {
                        //Se encontrou atualiza.
                        if (
                                headerItem.getCustomer_code() == saveReturn.getCustomer_code()
                                        && headerItem.getOutbound_prefix() == saveReturn.getOutbound_prefix()
                                        && headerItem.getOutbound_code() == saveReturn.getOutbound_code()
                        ) {
                            if (headerItem.getItems() != null && headerItem.getItems().size() > 0) {
//                                daoObjReturn = outboundItemDao.addUpdate(headerItem.getItems(), false);
//                                if (daoObjReturn.hasError()) {
//                                    throw new Exception(daoObjReturn.getErrorMsg());
//                                }
                                for (IO_Outbound_Item outboundItem : headerItem.getItems()) {
                                    outboundItemDao.addUpdate(
                                            new IO_Outbound_Item_Sql_009(
                                                    outboundItem.getCustomer_code(),
                                                    outboundItem.getOutbound_prefix(),
                                                    outboundItem.getOutbound_code(),
                                                    outboundItem.getOutbound_item(),
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
        //Processa as outbounds Full
        if (rec.getOutbound() != null && rec.getOutbound().size() > 0) {
            //Se envio do token, só pode executar outboundFull se não existir mais dados para serem enviados.
            if (reSend) {
                //Lista que sera passa para ser processada.
                ArrayList<IO_Outbound> outboundToProcess = new ArrayList<>();
                for (IO_Outbound outbound : rec.getOutbound()) {
                    //Busca outbound no banco e somente se update_required = 0
                    //add na lista de processFull
                    HMAux auxIo =
                            outboundDao.getByStringHM(
                                    new IO_Outbound_Sql_010(
                                            outbound.getCustomer_code(),
                                            outbound.getOutbound_prefix(),
                                            outbound.getOutbound_code()
                                    ).toSqlQuery()
                            );
                    //Verifica se outbound existe e se esta como update required
                    if (
                            auxIo != null
                                    && auxIo.hasConsistentValue(IO_Outbound_Sql_010.HAS_UPDATE_TO_DO)
                                    && auxIo.get(IO_Outbound_Sql_010.HAS_UPDATE_TO_DO).equalsIgnoreCase("0")
                    ) {
                        outboundToProcess.add(outbound);
                    }
                }
                daoObjReturn = outboundDao.processFull(outboundToProcess);
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getErrorMsg());
                }

            } else {
                daoObjReturn = outboundDao.processFull(rec.getOutbound());
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
                processWsOutboundItemSave();
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

    public static class OutboundItemSaveActReturn {
        private int customer_code = -1;
        private int prefix = -1;
        private int code = -1;
        private Integer item = -1;
        private String retStatus ="";
        private boolean fromTokenProcess = false;
        private boolean isMove = false;
        private String msg = "";

        public OutboundItemSaveActReturn() {
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
