package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_003;
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
                WBR_DownLoad_PDF.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }


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
                item.put("custom_name", hmAux.get("custom_name"));
                item.put("blob_url", hmAux.get("blob_url"));
                //
                dados.add(item);
            }
            //
            for (HMAux hmAux : dados) {
                //
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get("custom_name") + ".pdf")) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get("custom_name") + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            hmAux.get("blob_url"),
                            Constant.CACHE_PATH + "/" + hmAux.get("custom_name") + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get("custom_name"), ".pdf");
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

        } catch (Exception e) {
            String results = e.toString();
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
