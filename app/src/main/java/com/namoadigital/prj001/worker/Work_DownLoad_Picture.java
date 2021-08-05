package com.namoadigital.prj001.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
import com.namoadigital.prj001.dao.TkTicketOriginNcDao;
import com.namoadigital.prj001.model.TkTicketOriginNc;
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
import com.namoadigital.prj001.sql.TkTicketOriginNcDownloadSql001;
import com.namoadigital.prj001.sql.TkTicketOriginNcSql001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Work_DownLoad_Picture extends Worker {
    public static final String WORKER_TAG = "Work_DownLoad_Picture";
    public static boolean IS_RUNNING = false;
    private long customer_code = -1;
    //DAOS
    private MD_ProductDao productDao;
    private MD_All_ProductDao allProductDao;
    private GE_Custom_Form_FieldDao form_fieldDao;
    private GE_Custom_Form_Field_LocalDao form_fieldLocalDao;
    private GE_Custom_Form_LocalDao formLocalDao;
    //So
    private SM_SO_Service_Exec_Task_FileDao taskFileDao;
    private SM_SO_Product_EventDao eventDao;
    private SM_SO_Product_Event_FileDao eventFileDao;
    private SM_SODao smSoDao;
    //Chat
    private CH_RoomDao roomDao = null;
    private CH_MessageDao messageDao = null;
    //ticket
    private TK_TicketDao ticketDao;
    private TK_Ticket_ActionDao ticketActionDao;
    private TkTicketOriginNcDao tickeOriginNctDao;
    //LISTAS
    //N-Form
    private ArrayList<HMAux> dados = new ArrayList<>();
    private ArrayList<HMAux> dados_geral = new ArrayList<>();
    //Produtos
    private ArrayList<HMAux> product_sketch_list = new ArrayList<>();
    private ArrayList<HMAux> all_product_sketch_list = new ArrayList<>();
    private ArrayList<HMAux> product_icon_list = new ArrayList<>();
    //OS
    private ArrayList<HMAux> so_file_list = new ArrayList<>();
    private ArrayList<HMAux> event_sketch_list = new ArrayList<>();
    private ArrayList<HMAux> event_file_list = new ArrayList<>();
    private ArrayList<HMAux> so_client_approval_image = new ArrayList<>();
    //Chat
    private ArrayList<HMAux> roomImgList = new ArrayList<>();
    private ArrayList<HMAux> messageImgList = new ArrayList<>();
    //Ticket
    private ArrayList<HMAux> ticketImgList = new ArrayList<>();
    private ArrayList<HMAux> ticketActionImgList = new ArrayList<>();
    private ArrayList<HMAux> ticketNcImgList = new ArrayList<>();

    public Work_DownLoad_Picture(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG+" :doWork");
        try {
            Data inputData = getInputData();
            customer_code = inputData.getLong(Constant.LOGIN_CUSTOMER_CODE, -1);
            //Se parametro de customer não foi enviado, aborta chamada
            if (customer_code == -1L) {
                return Result.success();
            }
            //Inicia Daos
            initDaos();
            //Popula listas dos modulos
            getDownloadModulesList();
            //
            //Se nada para baixar, cancela chamas encadeadas
            if (areAllListEmpty()) {
                return Result.failure();
            }
            //Se parado, finaliza processamento
            if (isStopped()) {
                return Result.success();
            }
            //POSSUI ITEM NA LISTA, AI SIM VERIFICA NECESSIDADE DE NOTIFICAÇÃO E INICIA DOWNLOADS
            if (!ToolBox_Inf.isDownloadRunning()) {
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }
            IS_RUNNING = true;
            //
            // PROCESSAMENTO DAS LISTAS
            //
            /**Download de files do N-FORM*/
            processNFormDownloads();
            /** Download croquis de MD Produtos e MD ALL Products*/
            //
            processProductDownloads();
            /** Download de files do S.O*/
            if (ToolBox_Inf.profileExists(getApplicationContext(), Constant.PROFILE_PRJ001_SO, null)) {
                processSODownloads();
            }
            /**Download de files do CHAT*/
            processChatDownloads();
            /**Download de files do Ticket */
            processTicketDownloads();
            //
            //Verifica se ainda existens itens para serem baixados, se tiver, envia retry ao inves de success
//            if(hasMoreItensToDownload()){
//                Log.d("workerTsts", WORKER_TAG+" : New Itens toDownload\n");
//                return Result.retry();
//            }else{
//                return Result.success();
//            }
            ///
            return Result.success();
        } catch (Exception e) {
            Log.d("workerTsts", WORKER_TAG+" : Exception\n" + e.getMessage());
            ToolBox_Inf.registerException(getClass().getName(), e);
            return Result.retry();
        } finally {
            IS_RUNNING = false;
            if (!ToolBox_Inf.isDownloadRunning()) {
                ToolBox_Inf.cancelNotification(
                    getApplicationContext(),
                    Constant.NOTIFICATION_DOWNLOAD);
            }
        }
    }

    private boolean hasMoreItensToDownload() {
        getDownloadModulesList();
        //
        return !areAllListEmpty();
    }

    private boolean areAllListEmpty() {
        int i = dados_geral.size() +
             so_file_list.size() +
             so_client_approval_image.size() +
             product_sketch_list.size() +
             all_product_sketch_list.size() +
             event_sketch_list.size() +
             event_file_list.size() +
             roomImgList.size() +
             messageImgList.size() +
             product_icon_list.size() +
             ticketImgList.size() +
             ticketActionImgList.size() +
             ticketNcImgList.size() ;

        Log.d("workerTsts", WORKER_TAG+" : Itens to download = " + i);

        return dados_geral.size() == 0
            && so_file_list.size() == 0
            && so_client_approval_image.size() == 0
            && product_sketch_list.size() == 0
            && all_product_sketch_list.size() == 0
            && event_sketch_list.size() == 0
            && event_file_list.size() == 0
            && roomImgList.size() == 0
            && messageImgList.size() == 0
            && product_icon_list.size() == 0
            //&& schedule_product_icon_list.size() == 0
            && ticketImgList.size() == 0
            && ticketActionImgList.size() == 0
            && ticketNcImgList.size() == 0;
    }

    /**
     * Popula listas com os itens para download.
     */
    private void getDownloadModulesList() {
        /*
         * Download images do N-Form
         */
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
        product_sketch_list = (ArrayList<HMAux>) productDao.query_HM(
            new MD_Product_Sql_004(
                customer_code
            ).toSqlQuery()
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
        //LUCHE - 17/02/2020
        //Comentado lista, pois após analise concluimos que não faze sentido, ja que ela é gerada
        //porem não é considerada na lista de download
            /*schedule_product_icon_list = (ArrayList<HMAux>) formLocalDao.query_HM(
                new GE_Custom_Form_Local_Sql_017(
                    customer_code
                ).toSqlQuery()
            );*/

        /**
         *
         * LISTAS VINCULADAS AS.O
         *
         */
        // if (ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO/*, Constant.PARAM_SO_MOV*/})) {
        if (ToolBox_Inf.profileExists(getApplicationContext(), Constant.PROFILE_PRJ001_SO, null)) {
            //Adiciona lista de assinaturas da s.o
            so_client_approval_image.addAll(
                smSoDao.query_HM(
                    new SM_SO_Sql_021(customer_code).toSqlQuery()
                )
            );
            //Adiciona lista de task files para download
            so_file_list.addAll(taskFileDao.query_HM(
                new SM_SO_Service_Exec_Task_File_Sql_003(
                    customer_code
                ).toSqlQuery()
                )
            );
            //CROQUIS PRODUTO EVENTO
            event_sketch_list = (ArrayList<HMAux>) eventDao.query_HM(
                new SM_SO_Product_Event_Sql_004(
                    customer_code
                ).toSqlQuery()
            );
            /*
             * Download imagem dos PRODUTO EVENTO
             */
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

        //if (ToolBox_Inf.parameterExists(getApplicationContext(), Constant.PARAM_CHAT)) {
        //
        roomImgList.addAll(roomDao.query_HM(
            new CH_Room_Sql_002().toSqlQuery()
        ));

        //
        messageImgList.addAll(messageDao.query_HM(
            new CH_Message_Sql_006().toSqlQuery()
        ));
        /**
         * LISTA DE DOWNLOAD TICKET
         *
         */

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
        ticketNcImgList.addAll(
                tickeOriginNctDao.query_HM(
                    new TkTicketOriginNcDownloadSql001(customer_code).toSqlQuery()
            )
        );
    }

    private void initDaos() {
        form_fieldDao = new GE_Custom_Form_FieldDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        //
        form_fieldLocalDao = new GE_Custom_Form_Field_LocalDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        //
        formLocalDao = new GE_Custom_Form_LocalDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        //CROQUIS MD Products
        productDao = new MD_ProductDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        //All products
        allProductDao = new MD_All_ProductDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );

        if (ToolBox_Inf.profileExists(getApplicationContext(), Constant.PROFILE_PRJ001_SO, null)) {
            //
            smSoDao = new SM_SODao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
            //
            taskFileDao =
                new SM_SO_Service_Exec_Task_FileDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
                );

            //CROQUIS PRODUTO EVENTO
            eventDao = new SM_SO_Product_EventDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
            //
                      /*
             * Download imagem dos PRODUTO EVENTO
             */
            eventFileDao = new SM_SO_Product_Event_FileDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
        }
        // Room
        roomDao = new CH_RoomDao(getApplicationContext());
        // Messages
        messageDao = new CH_MessageDao(getApplicationContext());
        //TICKET
        ticketDao = new TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        ticketActionDao = new TK_Ticket_ActionDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        tickeOriginNctDao = new TkTicketOriginNcDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("workerTsts", WORKER_TAG+" : onStopped");
    }
    private void processTicketDownloads() throws Exception {
        for (HMAux hmAux : ticketImgList) {
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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

        for(HMAux item: ticketNcImgList){
            if(isStopped()){
                break;
            }
            try {
                String fileNameLocal = item.get(TkTicketOriginNcDownloadSql001.FILE_NAME_LOCAL).toLowerCase();
                if (!ToolBox_Inf.verifyDownloadFileInf( fileNameLocal + ".jpg", Constant.CACHE_PATH_PHOTO)) {

                    ToolBox_Inf.deleteDownloadFileInf(fileNameLocal + ".tmp", Constant.CACHE_PATH_PHOTO);
                    //
                    ToolBox_Inf.downloadImagePDF(
                            item.get(TkTicketOriginNcDownloadSql001.FILE_NAME_URL),
                            Constant.CACHE_PATH_PHOTO + "/" + fileNameLocal + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(fileNameLocal, ".jpg", Constant.CACHE_PATH_PHOTO);
                    //
                    //Atualiza campo com url local
                    updateFileName(item, fileNameLocal);
                } else {
                    //Atualiza campo com url local

                    updateFileName(item, fileNameLocal);
                }
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
    }

    private void updateFileName(HMAux item, String fileNameLocal) {
        TkTicketOriginNc originNc = tickeOriginNctDao.getByString(
                new TkTicketOriginNcSql001(
                        customer_code,
                        Integer.parseInt(item.get(TkTicketOriginNcDao.TICKET_PREFIX)),
                        Integer.parseInt(item.get(TkTicketOriginNcDao.TICKET_CODE)),
                        Integer.parseInt(item.get(TkTicketOriginNcDao.PAGE)),
                        Integer.parseInt(item.get(TkTicketOriginNcDao.CUSTOM_FORM_ORDER))
                ).toSqlQuery().toLowerCase()
        );
        //
        if(originNc != null) {
            switch (Objects.requireNonNull(item.get(TkTicketOriginNcDownloadSql001.TICKET_ORIGIN_FILE_NAME_ID))) {
                case "1":
                    originNc.setDataPhoto1UrlLocal(fileNameLocal + ".jpg");
                    break;
                case "2":
                    originNc.setDataPhoto2UrlLocal(fileNameLocal + ".jpg");
                    break;
                case "3":
                    originNc.setDataPhoto3UrlLocal(fileNameLocal + ".jpg");
                    break;
                case "4":
                    originNc.setDataPhoto4UrlLocal(fileNameLocal + ".jpg");
                    break;
                case "5":
                    originNc.setDataValueLocal(fileNameLocal + ".jpg");
                    break;
                case "6":
                    originNc.setPictureUrlLocal(fileNameLocal + ".jpg");
                    break;

            }
            //
            tickeOriginNctDao.addUpdate(originNc);
        }
    }

    private void processChatDownloads() throws Exception {
        for (HMAux hmAux : roomImgList) {
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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

    private void processSODownloads() throws Exception {
        for (HMAux hmAux : so_client_approval_image) {
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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


    private void processProductDownloads() throws Exception {
        for (HMAux hmAux : product_sketch_list) {
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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
            if(isStopped()){
                break;
            }
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
                    //
                    file_address = hmAux.get(MD_ProductDao.PRODUCT_ICON_NAME).toLowerCase();
                }else{
                    //LUCHE - 01/07/2020
                    //Add preenchimento do nome da foto caso ela já exista localmente.
                    //CORREÇÃO DE BUG.
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

    private void processNFormDownloads() throws Exception {
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
            if(isStopped()){
                break;
            }
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
                //
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
