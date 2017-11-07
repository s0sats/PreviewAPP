package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_004;
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
            if (!ToolBox_Inf.isDownloadRunning()) {
                //Log.v("WS_DownLoad_Picture","true");
                WBR_DownLoad_Picture.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }

            Bundle bundle = intent.getExtras();

            /*
            * Download images do N-Form
            * */
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
                item.put("custom_name", hmAux.get("custom_name").toLowerCase());
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
                    ToolBox_Inf.registerException(getClass().getName(), e);
                }
                //
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get("custom_name").toLowerCase() + ".jpg")) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get("custom_name").toLowerCase() + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            value,
                            Constant.CACHE_PATH + "/" + hmAux.get("custom_name").toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get("custom_name").toLowerCase(), ".jpg");
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

            /**
             *
             * Download de files do S.O
             *
             */

            if (ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})) {
                //
                SM_SO_Service_Exec_Task_FileDao taskFileDao =
                        new SM_SO_Service_Exec_Task_FileDao(
                                getApplicationContext(),
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                                Constant.DB_VERSION_CUSTOM
                        );
                ArrayList<HMAux> so_file_list = new ArrayList<>();
                //Adiciona lista de task files para download
                so_file_list.addAll(taskFileDao.query_HM(
                        new SM_SO_Service_Exec_Task_File_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                        ).toSqlQuery()
                        )
                );
                //
                for (HMAux hmAux : so_file_list) {
                    if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".jpg")) {

                        ToolBox_Inf.deleteDownloadFileInf(hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".tmp");
                        //
                        ToolBox_Inf.downloadImagePDF(
                                hmAux.get(SM_SO_FileDao.FILE_URL),
                                Constant.CACHE_PATH_PHOTO + "/" + hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInfPHOTO(hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME).toLowerCase(), ".jpg");
                    }
                    //Atualiza campo com url local
                    taskFileDao.addUpdate(
                            new SM_SO_Service_Exec_Task_File_Sql_004(
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.CUSTOMER_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.SO_PREFIX),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.SO_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.PRICE_LIST_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.PACK_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.PACK_SEQ),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.CATEGORY_PRICE_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.SERVICE_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.SERVICE_SEQ),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.EXEC_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.TASK_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_FileDao.FILE_CODE),
                                    hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME) + ".jpg"
                            ).toSqlQuery().toLowerCase()
                    );
                }
                //
                /*
                * Download croquis de Produtos
                */
                MD_ProductDao productDao = new MD_ProductDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                ArrayList<HMAux> product_sketch_list = new ArrayList<>();
                //
                product_sketch_list = (ArrayList<HMAux>) productDao.query_HM(
                        new MD_Product_Sql_004(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                        ).toSqlQuery()
                );
                //
                for (HMAux hmAux : product_sketch_list) {
                    if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".jpg",Constant.CACHE_PATH)) {

                        ToolBox_Inf.deleteDownloadFileInf(hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".tmp",Constant.CACHE_PATH);
                        //
                        ToolBox_Inf.downloadImagePDF(
                                hmAux.get(MD_ProductDao.SKETCH_URL),
                                Constant.CACHE_PATH + "/" + hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInf(hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase(), ".jpg");
                    }
                    //Atualiza campo com url local
                    productDao.addUpdate(
                            new MD_Product_Sql_005(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    hmAux.get(MD_ProductDao.PRODUCT_CODE),
                                    hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME) +".jpg"
                            ).toSqlQuery()
                    );
                }

            }
            //fim SO

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //Log.v("WS_DownLoad_Picture","false");
            WBR_DownLoad_Picture.IS_RUNNING = false;
            WBR_DownLoad_Picture.completeWakefulIntent(intent);
            if (!ToolBox_Inf.isDownloadRunning()) {
                ToolBox_Inf.cancelNotification(
                        getApplicationContext(),
                        Constant.NOTIFICATION_DOWNLOAD);
            }
        }
    }
}
