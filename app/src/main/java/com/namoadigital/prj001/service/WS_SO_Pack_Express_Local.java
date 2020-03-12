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
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.model.TSO_Pack_Express_Env;
import com.namoadigital.prj001.model.TSO_Pack_Express_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Pack_Express_Local;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_007;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_008;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/06/2017.
 */

public class WS_SO_Pack_Express_Local extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_pack_express_local";
    //
    private SO_Pack_Express_LocalDao soPackExpressLocalDao;

    private String token;
    private ArrayList<SO_Pack_Express_Local> so_pack_express_List;
    private HMAux auxApReturned;
    private int repeting;

    //Gson de envio exclui td que não tiver a tag @Expose para diminuir pacote de envio
    private Gson gsonEnv;
    //Gson de Retorno com inicilização padrão.
    private Gson gsonRec;

    public WS_SO_Pack_Express_Local() {
        super("WS_SO_Pack_Express_Local");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            // token = ToolBox_Inf.getToken(getApplicationContext());
            soPackExpressLocalDao = new SO_Pack_Express_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            //
            token = ToolBox_Inf.getToken(getApplicationContext());
            so_pack_express_List = new ArrayList<>();
            auxApReturned = new HMAux();
            //
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            //
            processSO_Pack_Express();

        } catch (Exception e) {
            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);
            ToolBox_Inf.registerException(getClass().getName(), e);
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");
        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_SO_Pack_Express_Local.completeWakefulIntent(intent);
        }
    }

    private void processSO_Pack_Express() throws Exception {
        loadTranslation();

        Gson gsonEnv = new GsonBuilder().serializeNulls().create();
        Gson gsonRec = new GsonBuilder().serializeNulls().create();

        repeting = 0;

        while (++repeting < 2) {

            if (processPendingToken() == 0) {
                processNewToken();
            }
            if (so_pack_express_List == null || so_pack_express_List.size() == 0) {
                //HMAux auxApReturned = new HMAux();
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_express_save"), auxApReturned, "", "0");
                return;
            }
            //
            TSO_Pack_Express_Env env = new TSO_Pack_Express_Env();
            //
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setPack_express(so_pack_express_List);
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            //
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_express_data"), "", "0");
            //
            String json = gsonEnv.toJson(env);
            //
            String resultado = ToolBox_Con.connWebService(
                    Constant.WS_SO_PACK_EXPRESSION,
                    json
            );

            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_express_data_returned"), "", "0");
            //
            TSO_Pack_Express_Rec rec = gsonRec.fromJson(
                    resultado,
                    TSO_Pack_Express_Rec.class
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
            processSO_Pack_Express_Local_Return(rec, so_pack_express_List);
        }

        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_express_save"), auxApReturned, "", "0");
    }

    private int processPendingToken() {
        so_pack_express_List = (ArrayList<SO_Pack_Express_Local>) soPackExpressLocalDao.query(
                new SO_Pack_Express_Local_Sql_007(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        1
                ).toSqlQuery()
        );

        if (so_pack_express_List.size() > 0) {
            token = so_pack_express_List.get(0).getToken();
        }

        return so_pack_express_List.size();
    }

    private int processNewToken() {
        soPackExpressLocalDao.query(
                new SO_Pack_Express_Local_Sql_008(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        token
                ).toSqlQuery()
        );

        so_pack_express_List = (ArrayList<SO_Pack_Express_Local>) soPackExpressLocalDao.query(
                new SO_Pack_Express_Local_Sql_007(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        1
                ).toSqlQuery()
        );

        return so_pack_express_List.size();
    }


    private void processSO_Pack_Express_Local_Return(TSO_Pack_Express_Rec rec, ArrayList<SO_Pack_Express_Local> so_pack_express_List) {
        switch (rec.getSave()) {
            case "OK_DUP":
            case "OK":
                //HMAux auxApReturned = new HMAux();
                //
                for (SO_Pack_Express_Local so_pack_express_local : rec.getPack_express()) {
                    String hmAuxPKKey;

                    moveData(so_pack_express_local, so_pack_express_List);

                    if (so_pack_express_local.getRet_code().equalsIgnoreCase("OK")) {
                        hmAuxPKKey = String.valueOf(so_pack_express_local.getCustomer_code()) + "." +
                                String.valueOf(so_pack_express_local.getSite_code()) + "." +
                                String.valueOf(so_pack_express_local.getOperation_code()) + "." +
                                String.valueOf(so_pack_express_local.getProduct_code()) + "." +
                                String.valueOf(so_pack_express_local.getExpress_tmp()) +
                                Constant.MAIN_CONCAT_STRING +
                                so_pack_express_local.getSo_desc() +
                                Constant.MAIN_CONCAT_STRING +
                                so_pack_express_local.getSerial_id();

                        ;
                    } else {
                        hmAuxPKKey = String.valueOf(so_pack_express_local.getCustomer_code()) + "." +
                                String.valueOf(so_pack_express_local.getExpress_tmp()) +
                                Constant.MAIN_CONCAT_STRING +
                                so_pack_express_local.getSo_desc() +
                                Constant.MAIN_CONCAT_STRING +
                                so_pack_express_local.getSerial_id();
                    }

                    auxApReturned.put(
                            hmAuxPKKey,
                            (!so_pack_express_local.getRet_code().equalsIgnoreCase("OK") ? so_pack_express_local.getRet_msg() : "OK")
                    );

                    // moveData(so_pack_express_local, so_pack_express_List);

                    soPackExpressLocalDao.addUpdate(so_pack_express_local);
                }
                //
                break;
            case "ERROR":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", rec.getError_msg(), "", "0");
                break;
            default:
        }

    }

    private void moveData(SO_Pack_Express_Local so_pack_express_local, ArrayList<SO_Pack_Express_Local> so_pack_express_list) {
        if (so_pack_express_local.getRet_code().equalsIgnoreCase("OK")) {

            String keyL = String.valueOf(so_pack_express_local.getCustomer_code()) + "." +
                    String.valueOf(so_pack_express_local.getSite_code()) + "." +
                    String.valueOf(so_pack_express_local.getOperation_code()) + "." +
                    String.valueOf(so_pack_express_local.getProduct_code()) + "." +
                    String.valueOf(so_pack_express_local.getExpress_tmp());

            for (SO_Pack_Express_Local mSo_pack_express_local : so_pack_express_list) {
                String keyML = String.valueOf(mSo_pack_express_local.getCustomer_code()) + "." +
                        String.valueOf(mSo_pack_express_local.getSite_code()) + "." +
                        String.valueOf(mSo_pack_express_local.getOperation_code()) + "." +
                        String.valueOf(mSo_pack_express_local.getProduct_code()) + "." +
                        String.valueOf(mSo_pack_express_local.getExpress_tmp());

                if (keyL.equalsIgnoreCase(keyML)) {
                    so_pack_express_local.setPartner_code(mSo_pack_express_local.getPartner_code());
                    so_pack_express_local.setExpress_code(mSo_pack_express_local.getExpress_code());
                    so_pack_express_local.setStatus(Constant.SYS_STATUS_SENT);
                    so_pack_express_local.setLog_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    //
                    break;
                }
            }
        } else {
            String keyL = String.valueOf(so_pack_express_local.getCustomer_code()) + "." +
                    String.valueOf(so_pack_express_local.getExpress_tmp());

            for (SO_Pack_Express_Local mSo_pack_express_local : so_pack_express_list) {
                String keyML = String.valueOf(mSo_pack_express_local.getCustomer_code()) + "." +
                        String.valueOf(mSo_pack_express_local.getExpress_tmp());

                if (keyL.equalsIgnoreCase(keyML)) {
                    so_pack_express_local.setSite_code(mSo_pack_express_local.getSite_code());
                    so_pack_express_local.setOperation_code(mSo_pack_express_local.getOperation_code());
                    so_pack_express_local.setProduct_code(mSo_pack_express_local.getProduct_code());
                    so_pack_express_local.setExpress_code(mSo_pack_express_local.getExpress_code());
                    so_pack_express_local.setSerial_id(mSo_pack_express_local.getSerial_id());
                    so_pack_express_local.setPartner_code(mSo_pack_express_local.getPartner_code());
                    so_pack_express_local.setSo_desc(mSo_pack_express_local.getSo_desc());
                    //
                    if (so_pack_express_local.getRet_code().equalsIgnoreCase("ERROR")) {
                        so_pack_express_local.setToken("");
                        so_pack_express_local.setSo_status(Constant.SYS_STATUS_ERROR);
                    } else {
                        so_pack_express_local.setStatus(Constant.SYS_STATUS_SENT);
                        so_pack_express_local.setSo_status(Constant.SYS_STATUS_DENIED);
                    }
                    //
                    so_pack_express_local.setLog_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    //
                    break;
                }
            }
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_no_express_save");
        translist.add("msg_sending_express_data");
        translist.add("msg_processing_express_data_returned");

        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);
    }

}
