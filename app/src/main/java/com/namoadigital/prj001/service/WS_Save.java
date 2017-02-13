package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;
import com.namoadigital.prj001.model.TSave_Env;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class WS_Save extends IntentService {

    private StringBuilder sResult;

    private GE_Custom_Form_DataDao formDataDao =
            new GE_Custom_Form_DataDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );

    private GE_Custom_Form_Data_FieldDao formDataFieldDao =
            new GE_Custom_Form_Data_FieldDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
    String token = ToolBox_Inf.getToken(getApplicationContext());
    List<GE_Custom_Form_Data> form_datas = new ArrayList<>();
    List<GE_Custom_Form_Data_Field> form_data_fields = new ArrayList<>();


    public WS_Save() {
        super("WS_Save");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            int statusjump = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);

            sResult = new StringBuilder();

            processWS_Save(statusjump, jumpOD);

        } catch (Exception e) {

            String results = "ERROR: ";

            if (e.toString().contains("JsonSyntaxException")) {
                results += "JsonParse - " + sResult.toString();
                sb.append(results);

            } else if(e.toString().contains("ORA-")) {
                results += "Oracle - " + sResult.toString();
                sb.append(results);

            }else{
                sb.append(results)
                        .append(e.toString());
            }

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Save.completeWakefulIntent(intent);

        }

    }

    private void processWS_Save(int statusjump, int jumpOD) {

        Gson gson = new GsonBuilder().serializeNulls().create();

        if(processPendingToken(1) == 0){
            processNewToken(0);
        }

        TSave_Env env =  new TSave_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setSite_code(Long.parseLong(ToolBox_Con.getPreference_Site_Code(getApplicationContext())));
        env.setOperation_code(ToolBox_Con.getPreference_Operation_Code(getApplicationContext()));
        env.setForm_datas(form_datas);
        env.setForm_data_fields(form_data_fields);



        if(checkSaveReturn()){

        }
    }

    private int processPendingToken(int pending) {
        form_datas =
                    formDataDao.query(
                            new GE_Custom_Form_Data_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    pending
                            ).toSqlQuery()
                    );

        if(form_datas.size() > 0){

            form_data_fields =
                    formDataFieldDao.query(
                            new GE_Custom_Form_Data_Field_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                            ).toSqlQuery()
                    );

            //Atualiza token para o que esta pendente de envio
            token = form_datas.get(0).getToken();
        }

        return form_datas.size();
    }

    private int processNewToken(int pending) {
        form_datas =
                formDataDao.query(
                        new GE_Custom_Form_Data_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                pending
                        ).toSqlQuery()
                );

        if(form_datas.size() > 0){
            //Atualiza valor do token em todos os cabeçalhos
            for ( GE_Custom_Form_Data form_data:form_datas ) {
                 form_data.setToken(token);
            }

            formDataDao.addUpdate(form_datas,false);

            form_data_fields =
                    formDataFieldDao.query(
                            new GE_Custom_Form_Data_Field_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                            ).toSqlQuery()
                    );

        }

        return form_datas.size();

    }

    private boolean checkSaveReturn() {



        return false;
    }
}
