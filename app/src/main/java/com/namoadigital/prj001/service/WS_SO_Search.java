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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSO_Search_Env;
import com.namoadigital.prj001.model.TSO_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/06/2017.
 */

public class WS_SO_Search extends IntentService {

    public static final String SERIAL_SAVE = "serial_save";
    public static final String SO_LIST  = "so_list";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Search";
    private MD_Product_SerialDao serialDao;

    public WS_SO_Search() {
        super("WS_SO_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            Long product_code = bundle.getLong(Constant.WS_SO_SEARCH_PRODUCT_CODE, -1L);
            String serial_id = bundle.getString(Constant.WS_SO_SEARCH_SERIAL_ID, "");
            String so_mult = bundle.getString(Constant.WS_SO_SEARCH_SO_MULT, "");
            boolean save_serial = bundle.getBoolean(Constant.WS_SO_SEARCH_SAVE_SERIAL, false);

            processSO_Search(product_code, serial_id, so_mult, save_serial);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Serial.completeWakefulIntent(intent);
        }

    }

    private void processSO_Search(Long product_code, String serial_id, String so_mult, boolean save_serial) {
        ArrayList<MD_Product_Serial> serialList = new ArrayList<>();

        if(save_serial) {
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
        }
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        TSO_Search_Env env = new TSO_Search_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setSerial_id(serial_id);
        env.setSo_mult(so_mult);
        env.setSerial(serialList);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_checking_serial"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SEARCH,
                gson.toJson(env)
        );
        //
        TSO_Search_Rec rec = gson.fromJson(
                resultado,
                TSO_Search_Rec.class
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
        //Se tentou salvar serial, valida retorno
        if(save_serial){
            processSerialSaveRet(rec.getSerial_return().get(0),serialList.get(0),hmAux);
        }else{
            hmAux.put(SERIAL_SAVE,"OK");
        }
        //Tratativas especificas
        //mudar tratativa pra so_mult.split(?) > 0
        if (so_mult.length() == 0) {
            hmAux.put(SO_LIST,gson.toJson(rec.getSo()));
        } else {
            //
            SM_SODao soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            //
            soDao.addUpdate(rec.getSo(), false);
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_processing_list"),hmAux, "", "0");

    }

    private void processSerialSaveRet(TSO_Search_Rec.Serial_Save_Return serial_return, MD_Product_Serial serial, HMAux hmAux) {

        if(serial_return.getRet_status().toUpperCase().equals("OK")){
            serial.setUpdate_required(0);
            serialDao.addUpdate(serial);
            hmAux.put(SERIAL_SAVE,"OK");
        }else{
            hmAux.put(SERIAL_SAVE,serial_return.getRet_msg() == null ? hmAux_Trans.get("msg_error_on_save_serial"):serial_return.getRet_msg());
        }

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_processing_list");
        translist.add("msg_error_on_save_serial");
        translist.add("msg_checking_serial");

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
