package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_DownLoad_Picture extends IntentService {

    public WS_DownLoad_Picture() {
        super("WS_DownLoad_Picture");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            //
            ArrayList<HMAux> dados = new ArrayList<>();
            ArrayList<HMAux> dados_geral;
            //
            GE_Custom_Form_FieldDao form_fieldDao = new GE_Custom_Form_FieldDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            GE_Custom_Form_Field_LocalDao form_fieldLocalDao = new GE_Custom_Form_Field_LocalDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            dados_geral = (ArrayList<HMAux>) form_fieldDao.query_HM(
                    new GE_Custom_Form_Field_Sql_001().toSqlQuery().toLowerCase()
            );
            //
            dados_geral.addAll(
                    (ArrayList<HMAux>) form_fieldLocalDao.query_HM(
                            new GE_Custom_Form_Field_Local_Sql_001().toSqlQuery().toLowerCase()
                    )
            );
            //
            for (HMAux hmAux : dados_geral) {
                HMAux item = new HMAux();
                item.put("custom_name", hmAux.get("custom_name"));
                item.put("custom_form_data_content", hmAux.get("custom_form_data_content"));
                //
                dados.add(item);
            }
            //
            for (HMAux hmAux : dados) {
                //
                String value = "";

                try {
                    JSONObject jsonObject = new JSONObject(hmAux.get("custom_form_data_content"));
                    JSONArray jsonArray = jsonObject.getJSONArray("CONTENT");
                    //
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        //
                        value = jo.getString("VALUE");
                    }

                } catch (JSONException e) {
                }
                //
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get("custom_name") + ".jpg")) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get("custom_name") + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            value,
                            Constant.CACHE_PATH + "/" + hmAux.get("custom_name") + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get("custom_name"), ".jpg");
                }
                //
                String nome_parte[] = hmAux.get("custom_name").split("_");
                //
                form_fieldDao.addUpdate(
                        new GE_Custom_Form_Field_Sql_002(
                                nome_parte[1],
                                nome_parte[2],
                                nome_parte[3],
                                nome_parte[4],
                                nome_parte[5],
                                hmAux.get("custom_name") + ".jpg"
                        ).toSqlQuery().toLowerCase()
                );

                form_fieldLocalDao.addUpdate(
                        new GE_Custom_Form_Field_Local_Sql_002(
                                nome_parte[1],
                                nome_parte[2],
                                nome_parte[3],
                                nome_parte[4],
                                nome_parte[5],
                                hmAux.get("custom_name") + ".jpg"
                        ).toSqlQuery().toLowerCase()
                );

            }

        } catch (Exception e) {
            String results = e.toString();
        } finally {
            WBR_DownLoad_Picture.completeWakefulIntent(intent);
        }
    }
}
