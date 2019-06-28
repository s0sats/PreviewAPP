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
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.IO_Outbound_Item;
import com.namoadigital.prj001.model.T_IO_Outbound_Item_Env;
import com.namoadigital.prj001.model.T_IO_Outbound_Item_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Item_Add;
import com.namoadigital.prj001.sql.IO_Outbound_Item_Sql_006;
import com.namoadigital.prj001.sql.IO_Outbound_Item_Sql_007;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_002;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_005;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_006;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_007;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_008;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Outbound_Item_Add extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_outbound_item_add";
    private IO_OutboundDao outboundDao;
    private IO_Outbound_ItemDao outboundItemDao;
    private MD_Product_SerialDao serialDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String token;
    private ArrayList<IO_Outbound> outbounds = new ArrayList<>();
    private ArrayList<T_IO_Outbound_Item_Env.IO_Outbound_Header> outboundHeaders = new ArrayList<>();
    private boolean menuSendProcess = false;


    public WS_IO_Outbound_Item_Add() {
        super("WS_IO_Outbound_Item_Add");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            outboundDao = new IO_OutboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            outboundItemDao = new IO_Outbound_ItemDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            //
            processWsOutboundItemSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Outbound_Item_Add.completeWakefulIntent(intent);
        }
    }

    private void processWsOutboundItemSave() throws Exception {
        //
        loadTranslation();
        //Lista arquivos de token de outbound
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_data"), "", "0");
        //
        if (!processToken(1)) {
            processToken(0);
        }
        //
        //Se lista vazia, dispara msg de erro.
        if (outboundHeaders == null || outboundHeaders.size() == 0) {
            if (menuSendProcess) {
                HMAux auxRet = new HMAux();
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), auxRet, "", "0");
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_item_to_add"), "", "0");
            }
            return;
        }
        //
        T_IO_Outbound_Item_Env env = new T_IO_Outbound_Item_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(token);
        env.setOutbound(outboundHeaders);
        env.setReprocess(0);
        //
        callWsOutboundItemSave(env, menuSendProcess);

    }

    private boolean processToken(int pending) throws Exception {
        //Selecnio Outbound update_required
        outbounds = (ArrayList<IO_Outbound>) outboundDao.query(
                new IO_Outbound_Sql_005(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        pending
                ).toSqlQuery()
        );
        //
        if (outbounds != null && outbounds.size() > 0) {
            //Se é processo de busca de token pendente, não gera novo token
            token = pending == 1 ? outbounds.get(0).getToken() : ToolBox_Inf.getToken(getApplicationContext());
            //
            for (IO_Outbound ioOutbound : outbounds) {
                //
                IO_Outbound_Item outboundItem = outboundItemDao.getByString(
                        new IO_Outbound_Item_Sql_006(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                ioOutbound.getOutbound_prefix(),
                                ioOutbound.getOutbound_code(),
                                0
                        ).toSqlQuery()
                );
                ///Gera item de cabeçaho de envio
                if (outboundItem != null && outboundItem.getCustomer_code() > 0) {
                    T_IO_Outbound_Item_Env.IO_Outbound_Header outboundHeader = new T_IO_Outbound_Item_Env.IO_Outbound_Header();
                    //Seta dados do cabeçalho
                    outboundHeader.setCustomer_code(ioOutbound.getCustomer_code());
                    outboundHeader.setOutbound_prefix(ioOutbound.getOutbound_prefix());
                    outboundHeader.setOutbound_code(ioOutbound.getOutbound_code());

                    outboundHeader.setScn(ioOutbound.getScn());
                    //Pega apenas os itens com update required
                    outboundHeader.getItems().add(outboundItem);
                    //
                    outboundHeaders.add(outboundHeader);
                    //Se geração de token, atualiza o token no cabeçalho da outbound.
                    if (pending == 0) {
                        outboundDao.addUpdate(
                                new IO_Outbound_Sql_007(
                                        ioOutbound.getCustomer_code(),
                                        ioOutbound.getOutbound_prefix(),
                                        ioOutbound.getOutbound_code(),
                                        token
                                ).toSqlQuery()
                        );
                    }
                }
            }
        }
        //
        return outboundHeaders.size() > 0;
    }

    private ArrayList<T_IO_Outbound_Item_Env.IO_Outbound_Header> getOutboundItensHeader(ArrayList<IO_Outbound_Item> ioOutboundItems) throws Exception {
        ArrayList<T_IO_Outbound_Item_Env.IO_Outbound_Header> headers = new ArrayList<>();
        for (IO_Outbound_Item outboundItem : ioOutboundItems) {
            T_IO_Outbound_Item_Env.IO_Outbound_Header header = new T_IO_Outbound_Item_Env.IO_Outbound_Header();

            //
            IO_Outbound ioOutbound = outboundDao.getByString(
                    new IO_Outbound_Sql_002(
                            outboundItem.getCustomer_code(),
                            outboundItem.getOutbound_prefix(),
                            outboundItem.getOutbound_code()
                    ).toSqlQuery()
            );
            //
            if (ioOutbound == null) {
                throw new Exception("Origin outbound not found");
            }
            //
            header.setCustomer_code(outboundItem.getCustomer_code());
            header.setOutbound_prefix(outboundItem.getOutbound_prefix());
            header.setOutbound_code(outboundItem.getOutbound_code());
            header.setScn(ioOutbound.getScn());
            header.getItems().add(outboundItem);
            //
            headers.add(header);
        }

        return headers;
    }

    private void callWsOutboundItemSave(T_IO_Outbound_Item_Env env, boolean menuSendProcess) throws Exception {
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
        if (!ToolBox_Inf.processWSCheckValidation(
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
        DaoObjReturn daoObjReturn;
        //if (rec.getSave().equals("OK")) {
        ArrayList<OutboundItemSaveActReturn> actReturnList = new ArrayList<>();
        for (T_IO_Outbound_Item_Rec.IO_Outbound_Item_Save_Return saveReturn : rec.getResult()) {
            OutboundItemSaveActReturn actReturn = new OutboundItemSaveActReturn();
            //Atualiza obj que contera o retorno.
            actReturn.setCustomer_code((int) saveReturn.getCustomer_code());
            actReturn.setOutbound_prefix(saveReturn.getOutbound_prefix());
            actReturn.setOutbound_code(saveReturn.getOutbound_code());
            actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
            actReturn.setMsg(saveReturn.getRet_msg());
            //
            if (saveReturn.getRet_status().equals("OK")) {
                //Atualiza SCN e seta update required para 0
                outboundDao.addUpdate(
                        new IO_Outbound_Sql_006(
                                saveReturn.getCustomer_code(),
                                saveReturn.getOutbound_prefix(),
                                saveReturn.getOutbound_code(),
                                0,
                                saveReturn.getScn()
                        ).toSqlQuery()
                );
                //
                for (T_IO_Outbound_Item_Rec.IO_Outbound_Item_Save_Return_Item saveReturnItem : saveReturn.getItems()) {
                    IO_Outbound_Item outboundItem = outboundItemDao.getByString(
                            new IO_Outbound_Item_Sql_006(
                                    saveReturn.getCustomer_code(),
                                    saveReturn.getOutbound_prefix(),
                                    saveReturn.getOutbound_code(),
                                    0
                            ).toSqlQuery()
                    );
                    //
                    if (saveReturnItem.getRet_status().equals("OK")) {
                        actReturn.setRetStatus(saveReturn.getRet_status().equals("OK"));
                        actReturn.setMsg(saveReturn.getRet_msg());
                        actReturn.setOutbound_item(saveReturnItem.getOutbound_item());
                        //

                        if (outboundItem != null) {
                            outboundItem.setOutbound_item(saveReturnItem.getOutbound_item());
                            outboundItem.setSave_date(null);
                            outboundItem.setUpdate_required(0);
                            //
                            daoObjReturn = outboundItemDao.addUpdate(outboundItem);
                            if (!daoObjReturn.hasError()) {
                                //Apaga registro com item code 0
                                outboundItem.setOutbound_item(0);
                                daoObjReturn = outboundItemDao.remove(outboundItem, null);
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
                            throw new Exception("Outbound Item not found");
                        }


                    } else {
                        //Se erro O QUE FAZER?
//                            daoObjReturn = outboundItemDao.delete(inboundItem, null);
//                            if (daoObjReturn.hasError()) {
//                                throw new Exception(daoObjReturn.getErrorMsg());
//                            }
                    }
                }
            }else{
                //tudo via query sem verificação......
                outboundDao.addUpdate(
                        new IO_Outbound_Sql_008(
                                saveReturn.getCustomer_code(),
                                saveReturn.getOutbound_prefix(),
                                saveReturn.getOutbound_code()
                        ).toSqlQuery()

                );
                //
                outboundItemDao.remove(
                        new IO_Outbound_Item_Sql_007(
                                saveReturn.getCustomer_code(),
                                saveReturn.getOutbound_prefix(),
                                saveReturn.getOutbound_code(),
                                0
                        ).toSqlQuery()
                );
            }
            //
            actReturnList.add(actReturn);
        }
        //
        //Se tem inboud, significa Outbound Full e atualizará td
        if (rec.getOutbound() != null && rec.getOutbound().size() > 0) {
            daoObjReturn = outboundDao.processFull(rec.getOutbound());
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

    public class OutboundItemSaveActReturn {
        private int customer_code;
        private int outbound_prefix;
        private int outbound_code;
        private Integer outbound_item;
        private boolean retStatus;
        private String msg;
        private boolean outboundFull;

        public OutboundItemSaveActReturn() {
        }

        public OutboundItemSaveActReturn(int customer_code, int outbound_prefix, int outbound_code, Integer outbound_item, boolean retStatus, String msg, boolean hasMove, boolean outboundFull, Integer move_prefix, Integer move_code) {
            this.customer_code = customer_code;
            this.outbound_prefix = outbound_prefix;
            this.outbound_code = outbound_code;
            this.outbound_item = outbound_item;
            this.retStatus = retStatus;
            this.msg = msg;
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

        public Integer getOutbound_item() {
            return outbound_item;
        }

        public void setOutbound_item(Integer outbound_item) {
            this.outbound_item = outbound_item;
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

        public boolean isOutboundFull() {
            return outboundFull;
        }

        public void setOutboundFull(boolean outboundFull) {
            this.outboundFull = outboundFull;
        }

    }
}
