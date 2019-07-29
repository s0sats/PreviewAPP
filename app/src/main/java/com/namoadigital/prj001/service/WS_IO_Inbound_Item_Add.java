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
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Item_Add;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Inbound_Item_Add extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_inbound_item_add";
    private IO_InboundDao inboundDao;
    private IO_Inbound_ItemDao inboundItemDao;
    private MD_Product_SerialDao serialDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String token;
    private ArrayList<IO_Inbound> inbounds = new ArrayList<>();
    private ArrayList<T_IO_Inbound_Item_Env.IO_Inbound_Header> inboundHeaders = new ArrayList<>();
    private boolean menuSendProcess = false;

    public WS_IO_Inbound_Item_Add() {
        super("WS_IO_Inbound_Item_Add");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            inboundDao = new IO_InboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            inboundItemDao = new IO_Inbound_ItemDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            //
            processWsInboundItemSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Inbound_Item_Add.completeWakefulIntent(intent);
        }
    }

    private void processWsInboundItemSave() throws Exception {
        //
        loadTranslation();
        //Lista arquivos de token de inbound
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_data"), "", "0");
        //
        if (!processToken(1)) {
            processToken(0);
        }
        //
        //Se lista vazia, dispara msg de erro.
        if (inboundHeaders == null || inboundHeaders.size() == 0) {
            if (menuSendProcess) {
                HMAux auxRet = new HMAux();
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), auxRet, "", "0");
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_item_to_add"), "", "0");
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
        env.setInbound(inboundHeaders);
        env.setReprocess(0);
        //
        callWsInboundItemSave(env, menuSendProcess);

    }

    private boolean processToken(int pending) throws Exception {
        //Selecnio Inbound update_required
        inbounds = (ArrayList<IO_Inbound>) inboundDao.query(
            new IO_Inbound_Sql_005(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                pending
            ).toSqlQuery()
        );
        //
        if (inbounds != null && inbounds.size() > 0) {
            //Se é processo de busca de token pendente, não gera novo token
            token = pending == 1 ? inbounds.get(0).getToken() : ToolBox_Inf.getToken(getApplicationContext());
            //
            for (IO_Inbound ioInbound : inbounds) {
                //
                IO_Inbound_Item inboundItem = inboundItemDao.getByString(
                    new IO_Inbound_Item_Sql_006(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        ioInbound.getInbound_prefix(),
                        ioInbound.getInbound_code(),
                        0
                    ).toSqlQuery()
                );
                ///Gera item de cabeçaho de envio
                if (inboundItem != null && inboundItem.getCustomer_code() > 0) {
                    T_IO_Inbound_Item_Env.IO_Inbound_Header inboundHeader = new T_IO_Inbound_Item_Env.IO_Inbound_Header();
                    //Seta dados do cabeçalho
                    inboundHeader.setCustomer_code(ioInbound.getCustomer_code());
                    inboundHeader.setInbound_prefix(ioInbound.getInbound_prefix());
                    inboundHeader.setInbound_code(ioInbound.getInbound_code());
                    inboundHeader.setScn(ioInbound.getScn());
                    //Pega apenas os itens com update required
                    inboundHeader.getItems().add(inboundItem);
                    //
                    inboundHeaders.add(inboundHeader);
                    //Se geração de token, atualiza o token no cabeçalho da inbound.
                    if (pending == 0) {
                        inboundDao.addUpdate(
                            new IO_Inbound_Sql_007(
                                ioInbound.getCustomer_code(),
                                ioInbound.getInbound_prefix(),
                                ioInbound.getInbound_code(),
                                token
                            ).toSqlQuery()
                        );
                    }
                }
            }
        }
        //
        return inboundHeaders.size() > 0;
    }

    private ArrayList<T_IO_Inbound_Item_Env.IO_Inbound_Header> getInboundItensHeader(ArrayList<IO_Inbound_Item> ioInboundItems) throws Exception {
        ArrayList<T_IO_Inbound_Item_Env.IO_Inbound_Header> headers = new ArrayList<>();
        for (IO_Inbound_Item inboundItem : ioInboundItems) {
            T_IO_Inbound_Item_Env.IO_Inbound_Header header = new T_IO_Inbound_Item_Env.IO_Inbound_Header();

            //
            IO_Inbound ioInbound = inboundDao.getByString(
                new IO_Inbound_Sql_002(
                    inboundItem.getCustomer_code(),
                    inboundItem.getInbound_prefix(),
                    inboundItem.getInbound_code()
                ).toSqlQuery()
            );
            //
            if (ioInbound == null) {
                throw new Exception(hmAux_Trans.get("msg_origin_inbound_not_found"));
            }
            //
            header.setCustomer_code(inboundItem.getCustomer_code());
            header.setInbound_prefix(inboundItem.getInbound_prefix());
            header.setInbound_code(inboundItem.getInbound_code());
            header.setScn(ioInbound.getScn());
            header.getItems().add(inboundItem);
            //
            headers.add(header);
        }

        return headers;
    }

    private void callWsInboundItemSave(T_IO_Inbound_Item_Env env, boolean menuSendProcess) throws Exception {
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
        DaoObjReturn daoObjReturn;
        //if (rec.getSave().equals("OK")) {
            ArrayList<InboundItemSaveActReturn> actReturnList = new ArrayList<>();
            for (T_IO_Inbound_Item_Rec.IO_Inbound_Item_Save_Return saveReturn : rec.getResult()) {
                InboundItemSaveActReturn actReturn = new InboundItemSaveActReturn();
                //Atualiza obj que contera o retorno.
                actReturn.setCustomer_code((int) saveReturn.getCustomer_code());
                actReturn.setInbound_prefix(saveReturn.getInbound_prefix());
                actReturn.setInbound_code(saveReturn.getInbound_code());
                actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
                actReturn.setMsg(saveReturn.getRet_msg());
                //
                if (saveReturn.getRet_status().equals("OK")) {
                    //Atualiza SCN e seta update required para 0
                    inboundDao.addUpdate(
                        new
                            IO_Inbound_Sql_006(
                            saveReturn.getCustomer_code(),
                            saveReturn.getInbound_prefix(),
                            saveReturn.getInbound_code(),
                            0,
                            saveReturn.getScn()
                        ).toSqlQuery()
                    );
                    //
                    for (T_IO_Inbound_Item_Rec.IO_Inbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
                        IO_Inbound_Item inboundItem = inboundItemDao.getByString(
                            new IO_Inbound_Item_Sql_006(
                                saveReturn.getCustomer_code(),
                                saveReturn.getInbound_prefix(),
                                saveReturn.getInbound_code(),
                                0
                            ).toSqlQuery()
                        );
                        //
                        if (saveReturnItem.getRet_status().equals("OK")) {
                            actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
                            actReturn.setMsg(saveReturn.getRet_msg());
                            actReturn.setInbound_item(saveReturnItem.getInbound_item());
                            //

                            if (inboundItem != null) {
                                inboundItem.setInbound_item(saveReturnItem.getInbound_item());
                                inboundItem.setSave_date(null);
                                inboundItem.setUpdate_required(0);
                                //
                                daoObjReturn = inboundItemDao.addUpdate(inboundItem);
                                if (!daoObjReturn.hasError()) {
                                    //Apaga registro com item code 0
                                    inboundItem.setInbound_item(0);
                                    daoObjReturn = inboundItemDao.delete(inboundItem, null);
                                    if (!daoObjReturn.hasError()) {
                                        if (saveReturnItem.getSerial() != null && saveReturnItem.getSerial().size() > 0) {
                                            serialDao.addUpdateTmp(saveReturnItem.getSerial().get(0));
                                        }
                                    } else {
                                        throw new Exception(daoObjReturn.getErrorMsg());
                                    }
                                } else {
                                    throw new Exception(daoObjReturn.getErrorMsg());
                                }
                            } else {
                                throw new Exception(hmAux_Trans.get("msg_inbound_item_not_found"));
                            }


                        } else {
                            //Se erro O QUE FAZER?
//                            daoObjReturn = inboundItemDao.delete(inboundItem, null);
//                            if (daoObjReturn.hasError()) {
//                                throw new Exception(daoObjReturn.getErrorMsg());
//                            }
                        }
                    }
                }else{
                    //tudo via query sem verificação......
                    inboundDao.addUpdate(
                        new IO_Inbound_Sql_008(
                            saveReturn.getCustomer_code(),
                            saveReturn.getInbound_prefix(),
                            saveReturn.getInbound_code()
                        ).toSqlQuery()

                    );
                    //
                    inboundItemDao.remove(
                        new IO_Inbound_Item_Sql_007(
                            saveReturn.getCustomer_code(),
                            saveReturn.getInbound_prefix(),
                            saveReturn.getInbound_code(),
                            0
                        ).toSqlQuery()
                    );
                }
                //
                actReturnList.add(actReturn);
            }
            //
            //Se tem inboud, significa Inbound Full e atualizará td
            if (rec.getInbound() != null && rec.getInbound().size() > 0) {
                daoObjReturn = inboundDao.processFull(rec.getInbound());
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getErrorMsg());
                }

            }
            //
            String jsonActReturn = gsonRec.toJson(actReturnList);
            //
            callFinishProcessing(jsonActReturn);
        //}

    }

    private void callFinishProcessing(String actReturn) {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), actReturn, "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_preparing_data");
        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_data");
        translist.add("msg_save_ok");
        translist.add("msg_no_item_to_add");
        translist.add("msg_inbound_item_not_found");
        translist.add("msg_origin_inbound_not_found");


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

    public class InboundItemSaveActReturn {
        private int customer_code;
        private int inbound_prefix;
        private int inbound_code;
        private Integer inbound_item;
        private boolean retStatus;
        private String msg;
        private boolean inboundFull;

        public InboundItemSaveActReturn() {
        }

        public InboundItemSaveActReturn(int customer_code, int inbound_prefix, int inbound_code, Integer inbound_item, boolean retStatus, String msg, boolean hasMove, boolean inboundFull, Integer move_prefix, Integer move_code) {
            this.customer_code = customer_code;
            this.inbound_prefix = inbound_prefix;
            this.inbound_code = inbound_code;
            this.inbound_item = inbound_item;
            this.retStatus = retStatus;
            this.msg = msg;
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

        public Integer getInbound_item() {
            return inbound_item;
        }

        public void setInbound_item(Integer inbound_item) {
            this.inbound_item = inbound_item;
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

        public boolean isInboundFull() {
            return inboundFull;
        }

        public void setInboundFull(boolean inboundFull) {
            this.inboundFull = inboundFull;
        }

    }
}
