package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSO_Serial_Save_Env;
import com.namoadigital.prj001.model.TSO_Serial_Save_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Serial_Save;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/06/2017.
 */

public class WS_SO_Serial_Save extends IntentService {

    public static final String SERIAL_SAVE = "serial_save";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Serial_Save";
    private MD_Product_SerialDao serialDao;

    public WS_SO_Serial_Save() {
        super("WS_SO_Serial_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);

            Long product_code = bundle.getLong(Constant.WS_SO_SERIAL_SAVE_PRODUCT_CODE, -1L);
            String serial_id = bundle.getString(Constant.WS_SO_SERIAL_SAVE_SERIAL_ID, "");
            //
            processSO_Serial_Save(product_code, serial_id);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Serial_Save.completeWakefulIntent(intent);
        }
    }

    private void processSO_Serial_Save(Long product_code, String serial_id) {
        ArrayList<MD_Product_Serial> serialList = new ArrayList<>();

        MD_Product_Serial serial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        serial.setOnly_position(1);
        serialList.add(serial);

        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        TSO_Serial_Save_Env env = new TSO_Serial_Save_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken("");

        //env.setSo(new ArrayList<SM_SO>());
        env.setSerial(serialList);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_updating_serial"), "", "0");
        //
        String teste = gson.toJson(env).toString();


        String url = Constant.WS_SO_SERIAL_SAVE;
        //
        String resultado = ToolBox_Con.connWebService(
                "https://dev.namoadigital.com/ws/prj001/server_save_so.ws",
                gson.toJson(env)
        );
        //
        TSO_Serial_Save_Rec rec = gson.fromJson(
                resultado,
                TSO_Serial_Save_Rec.class
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
                ) {
            return;
        }

        HMAux hmAux = new HMAux();

        processSerialSaveRet(rec.getSerial_return().get(0), serialList.get(0), hmAux);

        if (hmAux.get(SERIAL_SAVE).equalsIgnoreCase("OK")) {
            ToolBox.sendBCStatus(getApplicationContext(), "SAVE_OK", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
        } else {
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
        }

    }

    private void processSerialSaveRet(TSO_Serial_Save_Rec.Serial_Save_Return serial_return, MD_Product_Serial serial, HMAux hmAux) {

        if (serial_return.getRet_status().toUpperCase().equals("OK")) {
            serial.setUpdate_required(0);
            serialDao.addUpdate(serial);
            hmAux.put(SERIAL_SAVE, "OK");
        } else {
            hmAux.put(SERIAL_SAVE, serial_return.getRet_msg() == null ? hmAux_Trans.get("msg_error_on_save_serial") : serial_return.getRet_msg());
        }

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_processing_list");
        translist.add("msg_error_on_save_serial");
        translist.add("msg_updating_serial");

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
