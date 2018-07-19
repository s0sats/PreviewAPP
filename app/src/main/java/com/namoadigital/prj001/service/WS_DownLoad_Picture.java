package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.sql.CH_Message_Sql_006;
import com.namoadigital.prj001.sql.CH_Message_Sql_007;
import com.namoadigital.prj001.sql.CH_Room_Sql_002;
import com.namoadigital.prj001.sql.CH_Room_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_002;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_004;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_005;
import com.namoadigital.prj001.sql.MD_Product_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.namoadigital.prj001.util.ToolBox_Con.isHostAvailable;

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

            if (!isHostAvailable()) {
                //ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", getString(R.string.msg_no_server_found), "", "0");
                //
                return;
            }

            Bundle bundle = intent.getExtras();

            /*
            * Download images do N-Form
            * */
            //
            ArrayList<HMAux> dados = new ArrayList<>();
            ArrayList<HMAux> dados_geral = new ArrayList<>();
            MD_ProductDao productDao = null;
            ArrayList<HMAux> product_sketch_list = new ArrayList<>();
            MD_All_ProductDao allProductDao = null;
            ArrayList<HMAux> all_product_sketch_list = new ArrayList<>();
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
            //CROQUIS MD Products
            productDao = new MD_ProductDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            product_sketch_list = (ArrayList<HMAux>) productDao.query_HM(
                    new MD_Product_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //CROQUIS MD ALL Products
            allProductDao = new MD_All_ProductDao(getApplicationContext());
            //
            all_product_sketch_list = (ArrayList<HMAux>) allProductDao.query_HM(
                    new MD_All_Product_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            /**
             *
             * LISTAS VINCULADAS AS.O
             *
             */
            SM_SO_Service_Exec_Task_FileDao taskFileDao = null;
            ArrayList<HMAux> so_file_list = new ArrayList<>();
            SM_SO_Product_EventDao eventDao = null;
            ArrayList<HMAux> event_sketch_list = new ArrayList<>();
            SM_SO_Product_Event_FileDao eventFileDao = null;
            ArrayList<HMAux> event_file_list = new ArrayList<>();

            if (ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO/*, Constant.PARAM_SO_MOV*/})) {
                //
                taskFileDao =
                        new SM_SO_Service_Exec_Task_FileDao(
                                getApplicationContext(),
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                                Constant.DB_VERSION_CUSTOM
                        );

                //Adiciona lista de task files para download
                so_file_list.addAll(taskFileDao.query_HM(
                        new SM_SO_Service_Exec_Task_File_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                        ).toSqlQuery()
                        )
                );
                //CROQUIS PRODUTO EVENTO
                eventDao = new SM_SO_Product_EventDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                event_sketch_list = (ArrayList<HMAux>) eventDao.query_HM(
                        new SM_SO_Product_Event_Sql_004(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                        ).toSqlQuery()
                );
                /*
                 * Download imagem dos PRODUTO EVENTO
                 */
                eventFileDao = new SM_SO_Product_Event_FileDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                event_file_list = (ArrayList<HMAux>) eventFileDao.query_HM(
                        new SM_SO_Product_Event_File_Sql_004(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                        ).toSqlQuery()
                );
                //
            }
            /**
             *
             *
             * Lista de download Chat
             *
             */
            CH_RoomDao roomDao = null;
            ArrayList<HMAux> roomImgList = new ArrayList<>();
            CH_MessageDao messageDao = null;
            ArrayList<HMAux> messageImgList = new ArrayList<>();
            //if (ToolBox_Inf.parameterExists(getApplicationContext(), Constant.PARAM_CHAT)) {
                // Room
                roomDao = new CH_RoomDao(getApplicationContext());
                //
                roomImgList.addAll(roomDao.query_HM(
                        new CH_Room_Sql_002().toSqlQuery()
                ));
                // Messages
                messageDao = new CH_MessageDao(getApplicationContext());
                //
                messageImgList.addAll(messageDao.query_HM(
                        new CH_Message_Sql_006().toSqlQuery()
                ));
                //
            //}
            //APÓS GERAR TODAS AS LISTA , SE NÃO HOUVER REGISTROS PARA DOWNLOAD
            //SAI DO SERVIÇO SEM EXIBIR NOTIFICAÇÃO DE DOWNLOAD.
            if(  dados_geral.size() == 0
                 && so_file_list.size() == 0
                 && product_sketch_list.size() == 0
                 && all_product_sketch_list.size() == 0
                 && event_sketch_list.size() == 0
                 && event_file_list.size() == 0
                 && roomImgList.size() == 0
                 && messageImgList.size() == 0
            ){
                return;
            }
            //POSSUI ITEM NA LISTA, AI SIM VERIFICA NECESSIDADE DE NOTIFICAÇÃO E INICIA DOWNLOADS
            if (!ToolBox_Inf.isDownloadRunning()) {
                //Log.v("WS_DownLoad_Picture","true");
                //WBR_DownLoad_Picture.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }
            WBR_DownLoad_Picture.IS_RUNNING = true;
            //
            // PROCESSAMENTO DAS LISTAS
            //
            /**
             * Download de files do N-FORM
             */
            //region FORM
            //BAIXANDO ITENS DO FORM
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
            //endregion

            //region MD Product
            /*
             * Download croquis de MD Produtos e MD ALL Products
             */
            //
            for (HMAux hmAux : product_sketch_list) {
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".jpg", Constant.CACHE_PATH)) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".tmp", Constant.CACHE_PATH);
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
                                hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME) + ".jpg"
                        ).toSqlQuery()
                );
            }

            for (HMAux hmAux : all_product_sketch_list) {
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(MD_All_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".jpg", Constant.CACHE_PATH)) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get(MD_All_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".tmp", Constant.CACHE_PATH);
                    //
                    ToolBox_Inf.downloadImagePDF(
                            hmAux.get(MD_All_ProductDao.SKETCH_URL),
                            Constant.CACHE_PATH + "/" + hmAux.get(MD_All_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get(MD_All_Product_Sql_004.PROD_FILE_LOCAL_NAME).toLowerCase(), ".jpg");
                }
                //Atualiza campo com url local
                allProductDao.addUpdate(
                        new MD_All_Product_Sql_005(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                hmAux.get(MD_All_ProductDao.PRODUCT_CODE),
                                hmAux.get(MD_All_Product_Sql_004.PROD_FILE_LOCAL_NAME) + ".jpg"
                        ).toSqlQuery()
                );
            }//FIM CROQUI
            //endregion  MD Product

            /**
             * Download de files do S.O
             */
            //region S.O
            //Download de files do S.O
            if (ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO/*, Constant.PARAM_SO_MOV*/})) {
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
                * Download croqui dos eventos(croquis da sm_so_product_events)
                */
                //
                for (HMAux hmAux : event_sketch_list) {
                    String fileName = hmAux.get(SM_SO_Product_EventDao.SKETCH_NAME).toLowerCase().substring(0, hmAux.get(SM_SO_Product_EventDao.SKETCH_NAME).length() - 4);
                    if (!ToolBox_Inf.verifyDownloadFileInf(fileName + ".jpg", Constant.CACHE_PATH)) {

                        ToolBox_Inf.deleteDownloadFileInf(fileName + ".tmp", Constant.CACHE_PATH);
                        //
                        ToolBox_Inf.downloadImagePDF(
                                hmAux.get(SM_SO_Product_EventDao.SKETCH_URL),
                                Constant.CACHE_PATH + "/" + fileName + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInf(fileName, ".jpg");
                    }
                    //Atualiza campo com url local
                    eventDao.addUpdate(
                            new SM_SO_Product_Event_Sql_005(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    hmAux.get(SM_SO_Product_EventDao.SO_PREFIX),
                                    hmAux.get(SM_SO_Product_EventDao.SO_CODE),
                                    hmAux.get(SM_SO_Product_EventDao.SEQ),
                                    fileName + ".jpg"
                            ).toSqlQuery()
                    );
                }//FIM Event File
                /*
                 * Download da foto tirada no produto evento
                 */
                //
                for (HMAux hmAux : event_file_list) {
                    String fileName = hmAux.get(SM_SO_Product_Event_FileDao.FILE_NAME).toLowerCase().substring(0, hmAux.get(SM_SO_Product_Event_FileDao.FILE_NAME).length() - 4);
                    if (!ToolBox_Inf.verifyDownloadFileInf(fileName + ".jpg", Constant.CACHE_PATH_PHOTO)) {

                        ToolBox_Inf.deleteDownloadFileInf(fileName + ".tmp", Constant.CACHE_PATH_PHOTO);
                        //
                        ToolBox_Inf.downloadImagePDF(
                                hmAux.get(SM_SO_Product_Event_FileDao.FILE_URL),
                                Constant.CACHE_PATH_PHOTO + "/" + fileName + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInfPHOTO(fileName, ".jpg");
                    }
                    //Atualiza campo com url local
                    productDao.addUpdate(
                            new SM_SO_Product_Event_File_Sql_005(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    hmAux.get(SM_SO_Product_Event_FileDao.SO_PREFIX),
                                    hmAux.get(SM_SO_Product_Event_FileDao.SO_CODE),
                                    hmAux.get(SM_SO_Product_Event_FileDao.SEQ),
                                    hmAux.get(SM_SO_Product_Event_FileDao.FILE_CODE),
                                    fileName + ".jpg"
                            ).toSqlQuery()
                    );
                }//FIM Event File
            }
            //fim SO
            //endregion
            /**
             * Download de files do CHAT
             */
            //region CHAT
            //if (ToolBox_Inf.parameterExists(getApplicationContext(), Constant.PARAM_CHAT)) {
                //
                for (HMAux hmAux : roomImgList) {
                    if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(CH_Room_Sql_002.FILE_LOCAL_NAME).toLowerCase() + ".jpg", Constant.CACHE_CHAT_PATH)) {

                        ToolBox_Inf.deleteDownloadFileInf(hmAux.get(CH_Room_Sql_002.FILE_LOCAL_NAME).toLowerCase() + ".tmp", Constant.CACHE_CHAT_PATH);
                        //
                        ToolBox_Inf.downloadImagePDF(
                                hmAux.get(CH_RoomDao.ROOM_IMAGE),
                                Constant.CACHE_CHAT_PATH + "/" + hmAux.get(CH_Room_Sql_002.FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInf(hmAux.get(CH_Room_Sql_002.FILE_LOCAL_NAME).toLowerCase(), ".jpg", Constant.CACHE_CHAT_PATH);
                        //
                        //Atualiza campo com url local
                        roomDao.addUpdate(
                                new CH_Room_Sql_003(
                                        hmAux.get(CH_RoomDao.ROOM_CODE),
                                        hmAux.get(CH_Room_Sql_002.FILE_LOCAL_NAME) + ".jpg"
                                ).toSqlQuery().toLowerCase()
                        );

                    } else {
                        //Atualiza campo com url local
                        roomDao.addUpdate(
                                new CH_Room_Sql_003(
                                        hmAux.get(CH_RoomDao.ROOM_CODE),
                                        hmAux.get(CH_Room_Sql_002.FILE_LOCAL_NAME) + ".jpg"
                                ).toSqlQuery().toLowerCase()
                        );
                    }
                    //
                    ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_ROOM);
                }

                // Messages
                for (HMAux hmAux : messageImgList) {
                    JSONObject jsonObject = new JSONObject(hmAux.get("msg_obj"));
                    JSONObject jsonObject1 = jsonObject.getJSONObject("message");

                    String sFileD = jsonObject1.getString("data");
                    // String arr[] = sFileD.split("/");
                    //String sFile = arr[arr.length - 1].replace(".jpg", "").replace(".png", "");
                    String sFile = hmAux.get(CH_MessageDao.MSG_PREFIX) + "." + hmAux.get(CH_MessageDao.MSG_CODE);

                    if (!ToolBox_Inf.verifyDownloadFileInfV2(sFile + ".jpg")) {

                        ToolBox_Inf.deleteDownloadFileInfV2(sFile + ".tmp");
                        //
                        ToolBox_Inf.downloadImagePDF(
                                sFileD,
                                Constant.CACHE_PATH_PHOTO + "/" + sFile + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInfPHOTO(sFile, ".jpg");
                        //
                        ToolBox_Inf.createThumbNail_Images(Constant.CACHE_PATH_PHOTO, sFile + ".jpg");
                    }
                    //Atualiza campo com url local
                    messageDao.addUpdate(
                            new CH_Message_Sql_007(
                                    Integer.parseInt(hmAux.get(CH_MessageDao.MSG_PREFIX)),
                                    Long.parseLong(hmAux.get(CH_MessageDao.MSG_CODE)),
                                    sFile + ".jpg"
                            ).toSqlQuery().toLowerCase()
                    );
                    //
                    hmAux.put(CH_MessageDao.MESSAGE_IMAGE_LOCAL, sFile + ".jpg");
                    ToolBox_Inf.sendBRChatDownloadUpdate(getApplicationContext(), hmAux);
                }

            //}//FIM chat
            //endregion

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
