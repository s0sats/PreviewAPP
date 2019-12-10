package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.sql.CH_Message_Sql_006;
import com.namoadigital.prj001.sql.CH_Message_Sql_007;
import com.namoadigital.prj001.sql.CH_Room_Sql_002;
import com.namoadigital.prj001.sql.CH_Room_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_017;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_004;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_005;
import com.namoadigital.prj001.sql.MD_Product_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Sql_005;
import com.namoadigital.prj001.sql.MD_Product_Sql_007;
import com.namoadigital.prj001.sql.MD_Product_Sql_008;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Sql_021;
import com.namoadigital.prj001.sql.SM_SO_Sql_022;
import com.namoadigital.prj001.sql.TK_Ticket_Action_Sql_Img_Download_001;
import com.namoadigital.prj001.sql.TK_Ticket_Action_Sql_Img_Download_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_Img_Download_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_Img_Download_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_DownLoad_Picture extends IntentService {

    private long customer_code;

    public WS_DownLoad_Picture() {
        super("WS_DownLoad_Picture");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            Bundle bundle = intent.getExtras();
            //
            customer_code = bundle.getLong(Constant.LOGIN_CUSTOMER_CODE, -1);
            //Se parametro de customer não foi enviado, aborta chamada
            if (customer_code == -1L) {
                return;
            }

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
            ArrayList<HMAux> product_icon_list = new ArrayList<>();
            ArrayList<HMAux> schedule_product_icon_list = new ArrayList<>();
            //
            GE_Custom_Form_FieldDao form_fieldDao = new GE_Custom_Form_FieldDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            GE_Custom_Form_Field_LocalDao form_fieldLocalDao = new GE_Custom_Form_Field_LocalDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
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
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            product_sketch_list = (ArrayList<HMAux>) productDao.query_HM(
                    new MD_Product_Sql_004(
                            customer_code
                    ).toSqlQuery()
            );
            //CROQUIS MD ALL Products
            //allProductDao = new MD_All_ProductDao(getApplicationContext());
            allProductDao = new MD_All_ProductDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            all_product_sketch_list = (ArrayList<HMAux>) allProductDao.query_HM(
                    new MD_All_Product_Sql_004(
                            customer_code
                    ).toSqlQuery()
            );
            //
            product_icon_list = (ArrayList<HMAux>) productDao.query_HM(
                    new MD_Product_Sql_007(
                        customer_code
                    ).toSqlQuery()
            );
            //
            schedule_product_icon_list = (ArrayList<HMAux>) formLocalDao.query_HM(
                new GE_Custom_Form_Local_Sql_017(
                    customer_code
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
            SM_SODao smSoDao = null;
            ArrayList<HMAux> so_client_approval_image = new ArrayList<>();

            // if (ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO/*, Constant.PARAM_SO_MOV*/})) {
            if (ToolBox_Inf.profileExists(getApplicationContext(), Constant.PROFILE_PRJ001_SO, null)) {
                //
                smSoDao = new SM_SODao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
                //Adiciona lista de assinaturas da s.o
                so_client_approval_image.addAll(
                        smSoDao.query_HM(
                                new SM_SO_Sql_021(customer_code).toSqlQuery()
                        )
                );
                //
                taskFileDao =
                        new SM_SO_Service_Exec_Task_FileDao(
                                getApplicationContext(),
                                ToolBox_Con.customDBPath(customer_code),
                                Constant.DB_VERSION_CUSTOM
                        );

                //Adiciona lista de task files para download
                so_file_list.addAll(taskFileDao.query_HM(
                        new SM_SO_Service_Exec_Task_File_Sql_003(
                                customer_code
                        ).toSqlQuery()
                        )
                );
                //CROQUIS PRODUTO EVENTO
                eventDao = new SM_SO_Product_EventDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                event_sketch_list = (ArrayList<HMAux>) eventDao.query_HM(
                        new SM_SO_Product_Event_Sql_004(
                                customer_code
                        ).toSqlQuery()
                );
                /*
                 * Download imagem dos PRODUTO EVENTO
                 */
                eventFileDao = new SM_SO_Product_Event_FileDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                event_file_list = (ArrayList<HMAux>) eventFileDao.query_HM(
                        new SM_SO_Product_Event_File_Sql_004(
                                customer_code
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
            /**
             * LISTA DE DOWNLOAD TICKET
             *
             */
            TK_TicketDao ticketDao = new TK_TicketDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
            TK_Ticket_ActionDao ticketActionDao = new TK_Ticket_ActionDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
            ArrayList<HMAux> ticketImgList = new ArrayList<>();
            ArrayList<HMAux> ticketActionImgList = new ArrayList<>();
            //
            ticketImgList.addAll(
                ticketDao.query_HM(
                    new TK_Ticket_Sql_Img_Download_001(customer_code).toSqlQuery()
                )
            );
            //
            ticketActionImgList.addAll(
                ticketActionDao.query_HM(
                    new TK_Ticket_Action_Sql_Img_Download_001(customer_code).toSqlQuery()
                )
            );
            //
            //}
            //APÓS GERAR TODAS AS LISTA , SE NÃO HOUVER REGISTROS PARA DOWNLOAD
            //SAI DO SERVIÇO SEM EXIBIR NOTIFICAÇÃO DE DOWNLOAD.
            if (dados_geral.size() == 0
                    && so_file_list.size() == 0
                    && so_client_approval_image.size() == 0
                    && product_sketch_list.size() == 0
                    && all_product_sketch_list.size() == 0
                    && event_sketch_list.size() == 0
                    && event_file_list.size() == 0
                    && roomImgList.size() == 0
                    && messageImgList.size() == 0
                    && product_icon_list.size() == 0
                    && schedule_product_icon_list.size() == 0
                    && ticketImgList.size() == 0
                    && ticketActionImgList.size() == 0
                    ) {
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
            /**Download de files do N-FORM*/
            processNFormDownloads(form_fieldDao, form_fieldLocalDao ,dados_geral,dados);

            /** Download croquis de MD Produtos e MD ALL Products*/
            //
            processProductDownloads(productDao,allProductDao,product_sketch_list,all_product_sketch_list,product_icon_list);

            /** Download de files do S.O*/
            if (ToolBox_Inf.profileExists(getApplicationContext(), Constant.PROFILE_PRJ001_SO, null)) {
                processSODownloads(
                    smSoDao,
                    taskFileDao,
                    eventDao,
                    productDao,
                    so_client_approval_image,
                    so_file_list,
                    event_sketch_list,
                    event_file_list
                );
                //
            }
            /**Download de files do CHAT*/
            processChatDownloads(
                roomDao,
                messageDao,
                roomImgList,
                messageImgList
            );
            /**Download de files do Ticket */
            processTicketDownloads(
                ticketDao,
                ticketActionDao,
                ticketImgList,
                ticketActionImgList
            );
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

    private void processTicketDownloads(TK_TicketDao ticketDao, TK_Ticket_ActionDao ticketActionDao, ArrayList<HMAux> ticketImgList, ArrayList<HMAux> ticketActionImgList) throws Exception {
        for (HMAux hmAux : ticketImgList) {
            try {
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(TK_Ticket_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase() + ".jpg", Constant.CACHE_PATH_PHOTO)) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get(TK_Ticket_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase() + ".tmp", Constant.CACHE_PATH_PHOTO);
                    //
                    ToolBox_Inf.downloadImagePDF(
                        hmAux.get(TK_TicketDao.OPEN_PHOTO),
                        Constant.CACHE_PATH_PHOTO + "/" + hmAux.get(TK_Ticket_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get(TK_Ticket_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase(), ".jpg", Constant.CACHE_PATH_PHOTO);
                    //
                    //Atualiza campo com url local
                    ticketDao.addUpdate(
                        new TK_Ticket_Sql_Img_Download_002(
                            customer_code,
                            hmAux.get(TK_TicketDao.TICKET_PREFIX),
                            hmAux.get(TK_TicketDao.TICKET_CODE),
                            hmAux.get(TK_Ticket_Sql_Img_Download_001.FILE_LOCAL_NAME) + ".jpg"
                        ).toSqlQuery().toLowerCase()
                    );

                } else {
                    //Atualiza campo com url local
                    ticketDao.addUpdate(
                        new TK_Ticket_Sql_Img_Download_002(
                            customer_code,
                            hmAux.get(TK_TicketDao.TICKET_PREFIX),
                            hmAux.get(TK_TicketDao.TICKET_CODE),
                            hmAux.get(TK_Ticket_Sql_Img_Download_001.FILE_LOCAL_NAME) + ".jpg"
                        ).toSqlQuery().toLowerCase()
                    );
                }
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
        //
        for (HMAux hmAux : ticketActionImgList) {
            try {
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(TK_Ticket_Action_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase() + ".png", Constant.CACHE_PATH_PHOTO)) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get(TK_Ticket_Action_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase() + ".tmp", Constant.CACHE_PATH_PHOTO);
                    //
                    ToolBox_Inf.downloadImagePDF(
                        hmAux.get(TK_Ticket_ActionDao.ACTION_PHOTO),
                        Constant.CACHE_PATH_PHOTO + "/" + hmAux.get(TK_Ticket_Action_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get(TK_Ticket_Action_Sql_Img_Download_001.FILE_LOCAL_NAME).toLowerCase(), ".png", Constant.CACHE_PATH_PHOTO);
                    //
                    //Atualiza campo com url local
                    ticketActionDao.addUpdate(
                        new TK_Ticket_Action_Sql_Img_Download_002(
                            customer_code,
                            hmAux.get(TK_Ticket_ActionDao.TICKET_PREFIX),
                            hmAux.get(TK_Ticket_ActionDao.TICKET_CODE),
                            hmAux.get(TK_Ticket_ActionDao.TICKET_SEQ),
                            hmAux.get(TK_Ticket_Action_Sql_Img_Download_001.FILE_LOCAL_NAME) + ".png"
                        ).toSqlQuery().toLowerCase()
                    );

                } else {
                    //Atualiza campo com url local
                    ticketActionDao.addUpdate(
                        new TK_Ticket_Action_Sql_Img_Download_002(
                            customer_code,
                            hmAux.get(TK_Ticket_ActionDao.TICKET_PREFIX),
                            hmAux.get(TK_Ticket_ActionDao.TICKET_CODE),
                            hmAux.get(TK_Ticket_ActionDao.TICKET_SEQ),
                            hmAux.get(TK_Ticket_Action_Sql_Img_Download_001.FILE_LOCAL_NAME) + ".png"
                        ).toSqlQuery().toLowerCase()
                    );
                }
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
    }

    private void processChatDownloads(CH_RoomDao roomDao, CH_MessageDao messageDao, ArrayList<HMAux> roomImgList, ArrayList<HMAux> messageImgList) throws Exception {
        for (HMAux hmAux : roomImgList) {
            try {
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
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }

        // Messages
        for (HMAux hmAux : messageImgList) {
            try {
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
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
    }

    private void processSODownloads(SM_SODao smSoDao, SM_SO_Service_Exec_Task_FileDao taskFileDao, SM_SO_Product_EventDao eventDao, MD_ProductDao productDao, ArrayList<HMAux> so_client_approval_image, ArrayList<HMAux> so_file_list, ArrayList<HMAux> event_sketch_list, ArrayList<HMAux> event_file_list) throws Exception {
        for (HMAux hmAux : so_client_approval_image) {
            try {
                if (!ToolBox_Inf.verifyDownloadFileInfV2(hmAux.get(SM_SODao.CLIENT_APPROVAL_IMAGE_NAME).toLowerCase() + ".png")) {

                    ToolBox_Inf.deleteDownloadFileInfV2(hmAux.get(SM_SODao.CLIENT_APPROVAL_IMAGE_NAME).toLowerCase() + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                        hmAux.get(SM_SODao.CLIENT_APPROVAL_IMAGE_URL),
                        Constant.CACHE_PATH_PHOTO + "/" + hmAux.get(SM_SODao.CLIENT_APPROVAL_IMAGE_NAME).toLowerCase() + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInfPHOTO(hmAux.get(SM_SODao.CLIENT_APPROVAL_IMAGE_NAME).toLowerCase(), ".png");
                }
                //Atualiza campo com url local
                smSoDao.addUpdate(
                    new SM_SO_Sql_022(
                        hmAux.get(SM_SODao.CUSTOMER_CODE),
                        hmAux.get(SM_SODao.SO_PREFIX),
                        hmAux.get(SM_SODao.SO_CODE),
                        hmAux.get(SM_SODao.CLIENT_APPROVAL_IMAGE_NAME)
                    ).toSqlQuery().toLowerCase()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
        //
        for (HMAux hmAux : so_file_list) {
            try {
                if (!ToolBox_Inf.verifyDownloadFileInfV2(hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".jpg")) {

                    ToolBox_Inf.deleteDownloadFileInfV2(hmAux.get(SM_SO_Service_Exec_Task_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ".tmp");
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
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
        //
        /*
         * Download croqui dos eventos(croquis da sm_so_product_events)
         */
        //
        for (HMAux hmAux : event_sketch_list) {
            try {
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
                        customer_code,
                        hmAux.get(SM_SO_Product_EventDao.SO_PREFIX),
                        hmAux.get(SM_SO_Product_EventDao.SO_CODE),
                        hmAux.get(SM_SO_Product_EventDao.SEQ),
                        fileName + ".jpg"
                    ).toSqlQuery()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }//FIM Event File
        /*
         * Download da foto tirada no produto evento
         */
        //
        for (HMAux hmAux : event_file_list) {
            try {
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
                        customer_code,
                        hmAux.get(SM_SO_Product_Event_FileDao.SO_PREFIX),
                        hmAux.get(SM_SO_Product_Event_FileDao.SO_CODE),
                        hmAux.get(SM_SO_Product_Event_FileDao.SEQ),
                        hmAux.get(SM_SO_Product_Event_FileDao.FILE_CODE),
                        fileName + ".jpg"
                    ).toSqlQuery()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }//FIM Event File
    }


    private void processProductDownloads(MD_ProductDao productDao, MD_All_ProductDao allProductDao, ArrayList<HMAux> product_sketch_list, ArrayList<HMAux> all_product_sketch_list, ArrayList<HMAux> product_icon_list) throws Exception {
        for (HMAux hmAux : product_sketch_list) {
            try {
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
                        customer_code,
                        hmAux.get(MD_ProductDao.PRODUCT_CODE),
                        hmAux.get(MD_Product_Sql_004.PROD_FILE_LOCAL_NAME) + ".jpg"
                    ).toSqlQuery()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }

        for (HMAux hmAux : all_product_sketch_list) {
            try {
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
                        customer_code,
                        hmAux.get(MD_All_ProductDao.PRODUCT_CODE),
                        hmAux.get(MD_All_Product_Sql_004.PROD_FILE_LOCAL_NAME) + ".jpg"
                    ).toSqlQuery()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }//FIM CROQUI

        for (HMAux hmAux : product_icon_list) {
            String file_address = "";
            try {
                if (!ToolBox_Inf.verifyDownloadFileInf(hmAux.get(MD_ProductDao.PRODUCT_ICON_NAME).toLowerCase(), Constant.CACHE_PATH)) {

                    ToolBox_Inf.deleteDownloadFileInf(hmAux.get(MD_ProductDao.PRODUCT_ICON_NAME).toLowerCase() + ".tmp", Constant.CACHE_PATH);
                    //
                    file_address = Constant.CACHE_PATH + "/" + hmAux.get(MD_ProductDao.PRODUCT_ICON_NAME).toLowerCase() + ".tmp";
                    ToolBox_Inf.downloadImagePDF(
                        hmAux.get(MD_ProductDao.PRODUCT_ICON_URL),
                        file_address
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(hmAux.get(MD_ProductDao.PRODUCT_ICON_NAME).toLowerCase(), "");

                    file_address = hmAux.get(MD_ProductDao.PRODUCT_ICON_NAME).toLowerCase();
                }

                //Atualiza campo com url local
                productDao.addUpdate(
                    new MD_Product_Sql_008(
                        customer_code,
                        hmAux.get(MD_ProductDao.PRODUCT_CODE),
                        file_address
                    ).toSqlQuery()
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
    }

    private void processNFormDownloads(GE_Custom_Form_FieldDao form_fieldDao, GE_Custom_Form_Field_LocalDao form_fieldLocalDao, ArrayList<HMAux> dados_geral, ArrayList<HMAux> dados) throws Exception {
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
            try {
                String value = "";

                JSONObject jsonObject = new JSONObject(hmAux.get("custom_form_data_content"));
                JSONArray jsonArray = jsonObject.getJSONArray("CONTENT");
                //
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    //
                    value = jo.getString("VALUE");
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
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }

        }
    }
}
