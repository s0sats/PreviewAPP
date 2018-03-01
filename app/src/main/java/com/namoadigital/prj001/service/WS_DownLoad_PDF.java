package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_007;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_008;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_File_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_File_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_DownLoad_PDF extends IntentService {

    public WS_DownLoad_PDF() {
        super("WS_DownLoad_PDF");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            if (!ToolBox_Inf.isDownloadRunning()) {
                //Log.v("WS_DownLoad_PDF","true");
                //WBR_DownLoad_PDF.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }

            WBR_DownLoad_PDF.IS_RUNNING = true;

            Bundle bundle = intent.getExtras();
            //
            ArrayList<HMAux> dados = new ArrayList<>();
            ArrayList<HMAux> dados_geral;
            //
            GE_Custom_Form_BlobDao form_blobDao = new GE_Custom_Form_BlobDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            GE_Custom_Form_Blob_LocalDao form_blob_localDao = new GE_Custom_Form_Blob_LocalDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            dados_geral = (ArrayList<HMAux>) form_blobDao.query_HM(
                    new GE_Custom_Form_Blob_Sql_002().toSqlQuery().toLowerCase()
            );
            //
            dados_geral.addAll(
                    (ArrayList<HMAux>) form_blob_localDao.query_HM(
                            new GE_Custom_Form_Blob_Local_Sql_002().toSqlQuery().toLowerCase()
                    )
            );
            //
            for (HMAux hmAux : dados_geral) {
                HMAux item = new HMAux();
                item.put("custom_name", hmAux.get("custom_name").toLowerCase());
                item.put("blob_url", hmAux.get("blob_url"));
                //
                dados.add(item);
            }
            //
            for (HMAux hmAux : dados) {
                //
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get("custom_name").toLowerCase() + ".pdf")) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get("custom_name").toLowerCase() + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            hmAux.get("blob_url"),
                            Constant.CACHE_PATH + "/" + hmAux.get("custom_name").toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get("custom_name").toLowerCase(), ".pdf");
                }
                //
                String nome_parte[] = hmAux.get("custom_name").split("_");
                form_blobDao.addUpdate(
                        new GE_Custom_Form_Blob_Sql_003(
                                nome_parte[1],
                                nome_parte[2],
                                nome_parte[3],
                                nome_parte[4],
                                nome_parte[5],
                                hmAux.get("custom_name") + ".pdf"
                        ).toSqlQuery().toLowerCase()
                );

                form_blob_localDao.addUpdate(
                        new GE_Custom_Form_Blob_Local_Sql_003(
                                nome_parte[1],
                                nome_parte[2],
                                nome_parte[3],
                                nome_parte[4],
                                nome_parte[5],
                                hmAux.get("custom_name") + ".pdf"
                        ).toSqlQuery().toLowerCase()
                );

            }
            /*
            *  Action Plan 01/03/2018
            */
            GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
            //
            ArrayList<HMAux> formAplist = new ArrayList<>();
            //
            formAplist.addAll(
              formApDao.query_HM(
                      new GE_Custom_Form_Ap_Sql_007(
                              ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                      ).toSqlQuery()

              )
            );
            //
            for (HMAux hmAux: formAplist) {
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(GE_Custom_Form_Ap_Sql_007.FILE_LOCAL_NAME).toLowerCase() + ".pdf")) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get(GE_Custom_Form_Ap_Sql_007.FILE_LOCAL_NAME).toLowerCase() + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_URL),
                            Constant.CACHE_PATH + "/" + hmAux.get(GE_Custom_Form_Ap_Sql_007.FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get(GE_Custom_Form_Ap_Sql_007.FILE_LOCAL_NAME).toLowerCase(), ".pdf");
                }
                //
                formApDao.addUpdate(
                        new GE_Custom_Form_Ap_Sql_008(
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA),
                                hmAux.get(GE_Custom_Form_Ap_Sql_007.FILE_LOCAL_NAME) + ".pdf"
                        ).toSqlQuery().toLowerCase()
                );
            }
            /**
             *
             * Download de files do Cabeçalho do S.O
             *
             */

            if (ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})) {

                SM_SO_FileDao soFileDao =
                        new SM_SO_FileDao(
                                getApplicationContext(),
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                                Constant.DB_VERSION_CUSTOM
                        );
                ArrayList<HMAux> so_file_list = new ArrayList<>();
                //Carrega lista de files do cabealho da SO
                so_file_list.addAll(
                        soFileDao.query_HM(
                                new SM_SO_File_Sql_003(
                                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                                ).toSqlQuery()
                        )
                );
                //
                String splitKey = "@#My#@Key#@";
                for (HMAux hmAux : so_file_list) {
                    String fileName = hmAux.get(SM_SO_FileDao.FILE_NAME).replace(".", splitKey);
                    String[] nameSplited = fileName.split(splitKey);
                    String ext = "."+ nameSplited[nameSplited.length -1];

                    if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ext)) {

                        ToolBox_Inf.deleteDownloadFileInf(hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".tmp");
                        //
                        ToolBox_Inf.downloadImagePDF(
                                hmAux.get(SM_SO_FileDao.FILE_URL),
                                Constant.CACHE_PATH + "/" + hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInf(hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME).toLowerCase(), ext);
                    }

                    soFileDao.addUpdate(
                            new SM_SO_File_Sql_004(
                                    hmAux.get(SM_SO_FileDao.CUSTOMER_CODE),
                                    hmAux.get(SM_SO_FileDao.SO_PREFIX),
                                    hmAux.get(SM_SO_FileDao.SO_CODE),
                                    hmAux.get(SM_SO_FileDao.FILE_CODE),
                                    hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME) + ext
                            ).toSqlQuery().toLowerCase()
                    );
                }
            }

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(), e);

        } finally {
            WBR_DownLoad_PDF.IS_RUNNING = false;
            WBR_DownLoad_PDF.completeWakefulIntent(intent);
            if (!ToolBox_Inf.isDownloadRunning()) {
                //Log.v("WS_DownLoad_PDF","false");
                ToolBox_Inf.cancelNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }

        }
    }
}
