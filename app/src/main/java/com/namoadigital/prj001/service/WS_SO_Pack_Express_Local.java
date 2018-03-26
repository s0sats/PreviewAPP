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
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_005;
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
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            //
            processSO_Pack_Express();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Pack_Express_Local.completeWakefulIntent(intent);
        }
    }

    private void processSO_Pack_Express() throws Exception {
        loadTranslation();

        Gson gsonEnv = new GsonBuilder().serializeNulls().create();
        Gson gsonRec = new GsonBuilder().serializeNulls().create();

        ArrayList<SO_Pack_Express_Local> so_pack_express_List = (ArrayList<SO_Pack_Express_Local>) soPackExpressLocalDao.query(
                new SO_Pack_Express_Local_Sql_005(
                ).toSqlQuery()
        );
        //
        if (so_pack_express_List == null || so_pack_express_List.size() == 0) {
//            if (!menu_send_process) {
//                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_ap_to_save"), "", "0");
//                return;
//            } else {
            HMAux auxApReturned = new HMAux();
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_no_so_pack_save"), auxApReturned, "", "0");
            return;
//            }
        }
        //
        TSO_Pack_Express_Env env = new TSO_Pack_Express_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));
        env.setPack_express(so_pack_express_List);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_ap_data"), "", "0");
        //
        String json = gsonEnv.toJson(env);
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_PACK_EXPRESSION,
                json
        );
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_data_returned"), "", "0");
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

    private void processSO_Pack_Express_Local_Return(TSO_Pack_Express_Rec rec, ArrayList<SO_Pack_Express_Local> so_pack_express_List) {
        switch (rec.getSave()) {
            case "OK":
                HMAux auxApReturned = new HMAux();
                //
                for (SO_Pack_Express_Local so_pack_express_local : rec.getPack_express()) {
                    String hmAuxPKKey =
                            so_pack_express_local.getCustomer_code() + "." +
                                    so_pack_express_local.getSite_code() + "." +
                                    so_pack_express_local.getOperation_code() + "." +
                                    so_pack_express_local.getProduct_code() + "." +
                                    so_pack_express_local.getExpress_code() + "." +
                                    so_pack_express_local.getExpress_tmp();

                    auxApReturned.put(
                            hmAuxPKKey,
                            (!so_pack_express_local.getRet_code().equalsIgnoreCase("OK") ? so_pack_express_local.getRet_msg() : "OK")
                    );

                    moveData(so_pack_express_local, so_pack_express_List);

                    // save so_pack_express
                }
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_ap_save"), auxApReturned, "", "0");
                break;
            case "ERROR":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", rec.getError_msg(), "", "0");
                break;
            default:
        }

    }

    private void moveData(SO_Pack_Express_Local so_pack_express_local, ArrayList<SO_Pack_Express_Local> so_pack_express_list) {

        String keyL = so_pack_express_local.getCustomer_code() + "." +
                so_pack_express_local.getSite_code() + "." +
                so_pack_express_local.getOperation_code() + "." +
                so_pack_express_local.getProduct_code() + "." +
                so_pack_express_local.getExpress_code() + "." +
                so_pack_express_local.getExpress_tmp();

        for (SO_Pack_Express_Local mSo_pack_express_local : so_pack_express_list) {
            String keyML = mSo_pack_express_local.getCustomer_code() + "." +
                    mSo_pack_express_local.getSite_code() + "." +
                    mSo_pack_express_local.getOperation_code() + "." +
                    mSo_pack_express_local.getProduct_code() + "." +
                    mSo_pack_express_local.getExpress_tmp();

            if (keyL.equalsIgnoreCase(keyML)){
                so_pack_express_local.setPartner_code(mSo_pack_express_local.getPartner_code());
                so_pack_express_local.setExpress_code(mSo_pack_express_local.getExpress_code());
                //
                break;
            }
        }

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_no_ap_to_save");
        translist.add("msg_sending_ap_data");
        translist.add("msg_processing_data_returned");
        translist.add("msg_end_ap_save");
        translist.add("msg_ap_removed");
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
