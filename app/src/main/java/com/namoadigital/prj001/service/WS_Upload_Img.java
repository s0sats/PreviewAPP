package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;

/**
 * Created by neomatrix on 20/01/17.
 */

public class WS_Upload_Img extends IntentService {

    public WS_Upload_Img() {
        super("WS_Upload_Img");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
//            Bundle bundle = intent.getExtras();
//            //
//            ArrayList<HMAux> dados = new ArrayList<>();
//            ArrayList<HMAux> dados_geral;
//            //
//            GE_Custom_Form_BlobDao form_blobDao = new GE_Custom_Form_BlobDao(
//                    getApplicationContext(),
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
//                    Constant.DB_VERSION_CUSTOM
//            );
//            //
//            GE_Custom_Form_Blob_LocalDao form_blob_localDao = new GE_Custom_Form_Blob_LocalDao(
//                    getApplicationContext(),
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
//                    Constant.DB_VERSION_CUSTOM
//            );
//            //
//            dados_geral = (ArrayList<HMAux>) form_blobDao.query_HM(
//                    new GE_Custom_Form_Blob_Sql_002().toSqlQuery().toLowerCase()
//            );
//            //
//            dados_geral.addAll(
//                    (ArrayList<HMAux>) form_blob_localDao.query_HM(
//                            new GE_Custom_Form_Blob_Local_Sql_002().toSqlQuery().toLowerCase()
//                    )
//            );
//            //
//            for (HMAux hmAux : dados_geral) {
//                HMAux item = new HMAux();
//                item.put("custom_name", hmAux.get("custom_name"));
//                item.put("blob_url", hmAux.get("blob_url"));
//                //
//                dados.add(item);
//            }
//            //
//            for (HMAux hmAux : dados) {
//                //
//                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get("custom_name") + ".pdf")) {
//
//                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get("custom_name") + ".tmp");
//                    //
//                    ToolBox_Inf.downloadImagePDF(
//                            hmAux.get("blob_url"),
//                            Constant.CACHE_PATH + "/" + hmAux.get("custom_name") + ".tmp"
//                    );
//                    //
//                    ToolBox_Inf.renameDownloadFileInf(hmAux.get("custom_name"), ".pdf");
//                }
//                //
//                String nome_parte[] = hmAux.get("custom_name").split("_");
//                form_blobDao.addUpdate(
//                        new GE_Custom_Form_Blob_Sql_003(
//                                nome_parte[1],
//                                nome_parte[2],
//                                nome_parte[3],
//                                nome_parte[4],
//                                nome_parte[5],
//                                hmAux.get("custom_name") + ".pdf"
//                        ).toSqlQuery().toLowerCase()
//                );
//
//                form_blob_localDao.addUpdate(
//                        new GE_Custom_Form_Blob_Local_Sql_003(
//                                nome_parte[1],
//                                nome_parte[2],
//                                nome_parte[3],
//                                nome_parte[4],
//                                nome_parte[5],
//                                hmAux.get("custom_name") + ".pdf"
//                        ).toSqlQuery().toLowerCase()
//                );
//
//            }

            int i = 10;

        } catch (Exception e) {
            String results = e.toString();
        } finally {
            WBR_Upload_Img.completeWakefulIntent(intent);
        }
    }
}
