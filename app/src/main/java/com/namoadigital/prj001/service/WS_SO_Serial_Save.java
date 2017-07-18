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
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.TSO_Serial_Save_Env;
import com.namoadigital.prj001.model.TSO_Serial_Save_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Serial_Save;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Sql_005;
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
    public static final String SO_ACTION_EXECUTION = "EXECUTION";
    public static final String SO_ORIGIN_CHANGE_APP = "APP";
    public static final String SO_RETURN_LIST = "SO_RETURN_LIST";
    public static final String SO_RETURN_STATUS = "SO_RETURN_STATUS";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Serial_Save";
    private MD_Product_SerialDao serialDao;
    private SM_SODao soDao;
    private String token;

    public WS_SO_Serial_Save() {
        super("WS_SO_Serial_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            token = ToolBox_Inf.getToken(getApplicationContext());
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);

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
        ArrayList<SM_SO> sos = new ArrayList<>();

        MD_Product_Serial serial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        sos = (ArrayList<SM_SO>) soDao.query(
                new SM_SO_Sql_005(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
        );
        /*
        * TESTE PARA SAVE DA S.O
        *
        * */
        for (int i = 0; i < sos.size(); i++) {
            sos.get(i).setAction(SO_ACTION_EXECUTION);
            sos.get(i).setToken(token);
            sos.get(i).setOrigin_change(SO_ORIGIN_CHANGE_APP);
        }

        //
        //serial.setOnly_position(1);
       // serialList.add(serial);
        //
        loadTranslation();
        //
        Gson gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        Gson gsonRec = new GsonBuilder().serializeNulls().create();
        //
        TSO_Serial_Save_Env env = new TSO_Serial_Save_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(token);
        env.setSo(sos);
        env.setSerial(serialList);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_updating_serial"), "", "0");
        //
        String teste = gsonEnv.toJson(env).toString();
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SERIAL_SAVE,
                gsonEnv.toJson(env)
        );
        //
        TSO_Serial_Save_Rec rec = gsonRec.fromJson(
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
        if(product_code != -1L) {
            processSerialSaveRet(rec.getSerial_return().get(0), serialList.get(0), hmAux);

            if (hmAux.get(SERIAL_SAVE).equalsIgnoreCase("OK")) {
                ToolBox.sendBCStatus(getApplicationContext(), "SAVE_OK", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
            }
        }
        //
        if(sos.size() > 0){
            processSOSaveRet(rec,hmAux);
        }

    }

    private void processSOSaveRet(TSO_Serial_Save_Rec ret, HMAux hmAux) {
        String so_list_ret = "";
        String so_list_status = "";
        //Processa de-para de task
        if(ret.getSo_from_to() != null){
           if(processFromTo(ret.getSo_from_to())){
              int i = 0;

            }else{

           }

        }







        for (TSO_Serial_Save_Rec.So_Save_Return so_ret : ret.getSo_return()) {
            so_list_ret += "#"+ so_ret.getSo_prefix()+"."+so_ret.getSo_code();
            so_list_status += "#" + so_ret.getRet_status();
        }

        hmAux.put(SO_RETURN_LIST,so_list_ret.substring(1,so_list_ret.length()));
        hmAux.put(SO_RETURN_STATUS,so_list_status.substring(1,so_list_status.length()));

        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
        
    }

    private boolean processFromTo(TSO_Serial_Save_Rec.So_From_To so_from_to) {
        SM_SO_Service_ExecDao execDao = new SM_SO_Service_ExecDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        SM_SO_Service_Exec_TaskDao taskDao = new SM_SO_Service_Exec_TaskDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        try {
            //
            for (SM_SO_Service_Exec_Task task : so_from_to.getTask()) {
                SM_SO_Service_Exec exec = new SM_SO_Service_Exec();
                exec.setCustomer_code(task.getCustomer_code());
                exec.setSo_prefix(task.getSo_prefix());
                exec.setSo_code(task.getSo_code());
                exec.setPrice_list_code(task.getPrice_list_code());
                exec.setPack_code(task.getPack_code());
                exec.setPack_seq(task.getPack_seq());
                exec.setCategory_price_code(task.getCategory_price_code());
                exec.setService_code(task.getService_code());
                exec.setService_seq(task.getService_seq());
                exec.setExec_code(task.getExec_code());
                exec.setExec_tmp(task.getExec_tmp());
                //
                execDao.addUpdateTmp(exec);
                taskDao.addUpdateTmp(task);
            }
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            e.printStackTrace();
            return false;
        }
        return true;
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
