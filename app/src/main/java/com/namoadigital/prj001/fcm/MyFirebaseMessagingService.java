package com.namoadigital.prj001.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.Chat_C_Remove_Room;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.model.FCM_Schedule;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.receiver_chat.WBR_C_Remove_Room;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_007;
import com.namoadigital.prj001.sql.FCMMessage_Sql_002;
import com.namoadigital.prj001.sql.FCMMessage_Sql_003;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_012;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_012;
import com.namoadigital.prj001.sql.SM_SO_Sql_018;
import com.namoadigital.prj001.sql.Sql_Schedule_FCM_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_003;
import com.namoadigital.prj001.ui.act018.Act018_Main;
import com.namoadigital.prj001.ui.act019.Act019_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        FCMMessageDao fcmMessageDao = new FCMMessageDao(
                getApplicationContext(),
                Constant.DB_FULL_BASE,
                Constant.DB_VERSION_BASE
        );

        /*
        * LOG PARA IDENTIFICAR FALHA OU NÃO DO FCM - APAGAR APOS TESTES
        * */
        /*try {
            File log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + "Recebeu fcm - Tamanho getData() =   " + (remoteMessage.getData() == null ? "nullo":remoteMessage.getData().size()) +"\n" , log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        if (remoteMessage.getData().size() > 0) {

            StringBuilder sb = new StringBuilder();

            sb.append("Resposta\n");
            sb.append("\ncustomer: ");
            sb.append(remoteMessage.getData().get("customer"));
            sb.append("\ntype: ");
            sb.append(remoteMessage.getData().get("type"));
            sb.append("\ntitle: ");
            sb.append(remoteMessage.getData().get("title"));
            sb.append("\nmsg_short: ");
            sb.append(remoteMessage.getData().get("msg_short"));
            sb.append("\nmsg_long: ");
            sb.append(remoteMessage.getData().get("msg_long"));
            sb.append("\nmodule: ");
            sb.append(remoteMessage.getData().get("module"));
            sb.append("\nsender: ");
            sb.append(remoteMessage.getData().get("sender"));
            sb.append("\nsync: ");
            sb.append(remoteMessage.getData().get("sync"));
            sb.append("\ncancellable: ");
            sb.append(remoteMessage.getData().get("cancellable"));

            FCMMessage fcmMessage = new FCMMessage();

            fcmMessage.setCustomer(remoteMessage.getData().get("customer"));
            fcmMessage.setType(remoteMessage.getData().get("type"));
            fcmMessage.setTitle(remoteMessage.getData().get("title"));
            fcmMessage.setMsg_short(remoteMessage.getData().get("msg_short"));
            fcmMessage.setMsg_long(remoteMessage.getData().get("msg_long"));
            fcmMessage.setModule(remoteMessage.getData().get("module"));
            fcmMessage.setSender(remoteMessage.getData().get("sender"));
            fcmMessage.setReceiver(remoteMessage.getData().get("receiver"));
            fcmMessage.setSync(remoteMessage.getData().get("sync"));
            fcmMessage.setStatus("0");
            String sDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
            fcmMessage.setDate_create(sDate);
            fcmMessage.setDate_create_ms(ToolBox.dateToMilliseconds(sDate));
            fcmMessage.setCancellable(remoteMessage.getData().get("cancellable"));

            //Se FCM não é o que esta usr logado, aborta FCM
            if(!fcmMessage.getReceiver().equals(ToolBox_Con.getPreference_User_Code(getApplicationContext()))){
                return;
            }
            /*
            * Valida se o customer do FCM esta logado
            * */
            EV_User_CustomerDao customerDao = new EV_User_CustomerDao(getApplicationContext());
            boolean loggedCustomer = false;
            //Seleciona todos customers con sessão ativa e que tem acesso ao chat.
            ArrayList<HMAux> chatSessionCustomers = (ArrayList<HMAux>) customerDao.query_HM(
                    new EV_User_Customer_Sql_007(
                            ToolBox_Con.getPreference_User_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //
            if (chatSessionCustomers != null && chatSessionCustomers.size() > 0) {
                for (int i = 0; i < chatSessionCustomers.size(); i++) {
                    if(fcmMessage.getCustomer().equals(chatSessionCustomers.get(i).get(EV_User_CustomerDao.CUSTOMER_CODE))){
                        loggedCustomer = true;
                        break;
                    }
                }

            }else{
                return;
            }
            //Se o customer do FCM não tem cessão no app, aborta
            if(!loggedCustomer){
                return;
            }
            //
            if (fcmMessage.getModule().trim().equalsIgnoreCase(Constant.CHAT_NOTIFICATION_TYPE_CHAT)) {
                if (  /*ToolBox_Inf.parameterExists(getApplicationContext(),Constant.PARAM_CHAT)
                      &&*/ ToolBox_Con.getPreference_Status_Login(getApplicationContext()).equals(Constant.LOGIN_STATUS_OK)
                      && ToolBox_Inf.isUsrAppLogged(getApplicationContext())
                    ) {
                    String param = "";
                    switch (fcmMessage.getTitle()){
                        case Constant.CHAT_NOTIFICATION_FCM_MSG:
                            param = ToolBox_Inf.getWebSocketJsonParam(fcmMessage.getMsg_long().trim());
                            //
                            Intent cMessageIntent = new Intent(getApplicationContext(), WBR_C_Message.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                            bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_MESSAGE_FCM);
                            cMessageIntent.putExtras(bundle);
                            getApplicationContext().sendBroadcast(cMessageIntent);
                            break;
                        case Constant.CHAT_NOTIFICATION_FCM_ADD_ROOM:
                            ToolBox_Inf.showChatRoomNotification(getApplicationContext());
                            break;
                        case Constant.CHAT_NOTIFICATION_FCM_REMOVE_ROOM:
                            Gson gson = new GsonBuilder().serializeNulls().create();
                            Chat_C_Remove_Room chatCRemoveRoom = new Chat_C_Remove_Room();
                            chatCRemoveRoom.setRoom_code(fcmMessage.getMsg_short());
                            //
                            Intent cRemoveRoomIntent = new Intent(getApplicationContext(), WBR_C_Remove_Room.class);
                            Bundle removeBundle = new Bundle();
                            removeBundle.putString(Constant.CHAT_WS_JSON_PARAM, gson.toJson(chatCRemoveRoom) );
                            removeBundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_NOTIFICATION_FCM_REMOVE_ROOM);
                            cRemoveRoomIntent.putExtras(removeBundle);
                            getApplicationContext().sendBroadcast(cRemoveRoomIntent);
                            break;
                        default:
                            ToolBox_Inf.showChatNotification(
                                    getApplicationContext(),
                                    fcmMessage.getModule().toUpperCase(),
                                    "0",
                                    "",//fcmMessage.getTitle().trim(),
                                    "",//fcmMessage.getMsg_short().trim()
                                    true);

                    }

                }
                return;
            }
            //
            if (fcmMessage.getTitle().trim().equalsIgnoreCase(ConstantBaseApp.FCM_ACTION_SM_SO_UPDATE) &&
                    fcmMessage.getModule().trim().equalsIgnoreCase(ConstantBaseApp.FCM_MODULE_SO)) {

                checkNService_SO_Status(fcmMessage);

            }
            //LUCHE - 02/05/2019
            //ADD TRATATIVA MODULO IO   IO_
            else if(fcmMessage.getModule().trim().equalsIgnoreCase(ConstantBaseApp.FCM_MODULE_IO)) {
                handleIoFCM(fcmMessage);
            }
            //LUCHE - 03/12/2019
            //ADD TRATATIVA MODULO Ticket
            else if( fcmMessage.getModule().trim().equalsIgnoreCase(ConstantBaseApp.FCM_MODULE_TICKET) &&
                     fcmMessage.getTitle().trim().equalsIgnoreCase(ConstantBaseApp.FCM_ACTION_TK_TICKET_UPDATE)
            ) {
                handleTkFMC(fcmMessage);

            } else if( fcmMessage.getModule().trim().equalsIgnoreCase(ConstantBaseApp.FCM_MODULE_SCHEDULE)) {
                //24/03/2020 - Todas as msg de scheduel, devem ser SILENT
                if(fcmMessage.getType().equals(ConstantBaseApp.FCM_TYPE_SILENT)) {
                    handleScheduleFCM(fcmMessage);
                }
            } else {
                //
                //LUCHE - 07/08/2019
                //Após reclamação de usr verzani de que tinha muitas msg de Atualizar o app namoa no menu notificação
                //foi solicitado o "ajuste"(GAB***R*a) para inserir as msgs desse tipo com o status de "já lida"
                //Se FCM for modulo CHECKLIST e tipo WARNING, DEVE ser a msg de atualização deversão.
                if(!fcmMessage.getModule().isEmpty()
                    && fcmMessage.getModule().equals(ConstantBaseApp.FCM_MODULE_CHECKLIST)
                    && !fcmMessage.getType().isEmpty()
                    && fcmMessage.getType().equals(ConstantBaseApp.FCM_TYPE_WARNING)
                ){
                    //Seta mensagem como lida
                    fcmMessage.setStatus("1");
                }
                //
                fcmMessageDao.addUpdate(fcmMessage);
                int fcmmessage_code = Integer.parseInt(
                        fcmMessageDao.getByStringHM(
                                new FCMMessage_Sql_002().toSqlQuery()
                        ).get(FCMMessage_Sql_002.FCMMESSAGE_CODE)
                );

                int fcmmessage_qty = Integer.parseInt(
                        fcmMessageDao.getByStringHM(
                                new FCMMessage_Sql_003().toSqlQuery()
                        ).get(FCMMessage_Sql_003.BADGE_MESSAGES_QTY)
                );

                Log.d("msg", "Message data payload: " + sb.toString());

                makeNF(
                        getApplicationContext(),
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("msg_short"),
                        fcmmessage_code,
                        fcmmessage_qty,
                        fcmMessage.getCancellable()
                );

                if (fcmMessage.getSync().equalsIgnoreCase("1")) {
                    ToolBox_Con.setPreference_SYNC_REQUIRED(getApplicationContext(), "1");
                    ToolBox_Inf.call_Notification_Sync(getApplicationContext(), 11);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("msg", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleScheduleFCM(FCMMessage fcmMessage) {
        MD_Schedule_ExecDao scheduleExecDao = new MD_Schedule_ExecDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        GE_Custom_Form_DataDao formDataDao = new GE_Custom_Form_DataDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        TK_TicketDao ticketDao = new TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        //
        try {
            FCM_Schedule fcmSchedule = new FCM_Schedule(fcmMessage);
            if(fcmSchedule.isValid()){
                FCM_Schedule.FCM_Schedule_Msg_long scheduleMsgLong = fcmSchedule.getSchedule_msg_long();
               /* ArrayList<GE_Custom_Form_Local> formLocalsToUpdate = new ArrayList<>();
                ArrayList<GE_Custom_Form_Data> formDatasToUpdate = new ArrayList<>();
                ArrayList<TK_Ticket> ticketsToUpdate = new ArrayList<>();*/
                //
                ArrayList<MD_Schedule_Exec> schedulesToUpdate = (ArrayList<MD_Schedule_Exec>) scheduleExecDao.query(
                    new Sql_Schedule_FCM_001(
                        fcmSchedule.getCustomer_code(),
                        fcmSchedule.getSchedule_prefix(),
                        fcmSchedule.getSchedule_code(),
                        fcmSchedule.getSchedule_exec()
                    ).toSqlQuery()
                );
                int dumbDebugger = 0;
                //Lista Seleciona, executa loop atualizando apenas as informações que vieram
                for (MD_Schedule_Exec scheduleExec : schedulesToUpdate) {
                    if( scheduleExec.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_SCHEDULE)
                        || fcmSchedule.getStatus() == null
                      /* TODO analisar com comercial, pois no caso do ticket, ele pode estar no arquivo token e não adiantaria remover o update required
                        || (scheduleExec.getSchedule_type().equalsIgnoreCase(ConstantBaseApp.MD_SCHEDULE_TYPE_FORM) && scheduleExec.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_DONE))
                        || (scheduleExec.getSchedule_type().equalsIgnoreCase(ConstantBaseApp.MD_SCHEDULE_TYPE_TICKET) && scheduleExec.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_WAITING_SYNC))*/
                    ) {
                        /*if(scheduleExec.getSchedule_type().equalsIgnoreCase(ConstantBaseApp.MD_SCHEDULE_TYPE_FORM) && scheduleExec.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_DONE)){
                            GE_Custom_Form_Local formLocal = getFormLocal(formLocalDao,scheduleExec);
                            GE_Custom_Form_Data formData = getFormData(formDataDao,scheduleExec);
                        }*/
                        //Descrição do Agendamento
                        if (scheduleMsgLong.getSchedule_desc() != null) {
                            scheduleExec.setSchedule_desc(scheduleMsgLong.getSchedule_desc());
                            dumbDebugger++;
                        }
                        //Data de inicio da exec
                        if (scheduleMsgLong.getDate_start() != null) {
                            scheduleExec.setDate_start(scheduleMsgLong.getDate_start());
                            dumbDebugger++;
                        }
                        //Data Fim da exec
                        if (scheduleMsgLong.getDate_end() != null) {
                            scheduleExec.setDate_end(scheduleMsgLong.getDate_end());
                            dumbDebugger++;
                        }
                        //Status
                        if (fcmSchedule.getStatus() != null) {
                            //Se houve troca de status ants da execução, sempre exibir como cancelado, pois não será exibido.
                            //scheduleExec.setStatus(fcmSchedule.getStatus());
                            scheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_CANCELLED);
                            scheduleExec.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                            dumbDebugger++;
                        }
                        //Comments da exec
                        if (scheduleMsgLong.getComments() != null) {
                            scheduleExec.setComments(scheduleMsgLong.getComments());
                            dumbDebugger++;
                        }
                        //Nick do user que fez a alteração.
                        if (scheduleMsgLong.getUser_nick() != null) {
                            scheduleExec.setFcm_user_nick(scheduleMsgLong.getUser_nick());
                            dumbDebugger++;
                        }
                    }else{
                        //Status
                        if (fcmSchedule.getStatus() != null) {
                            scheduleExec.setFcm_new_status(fcmSchedule.getStatus());
                            scheduleExec.setClose_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                            dumbDebugger++;
                        }
                        //Nick do user que fez a alteração.
                        if (scheduleMsgLong.getUser_nick() != null) {
                            scheduleExec.setFcm_user_nick(scheduleMsgLong.getUser_nick());
                            dumbDebugger++;
                        }
                        String notificationDesc =
                            scheduleExec.getSchedule_type().equals(ConstantBaseApp.MD_SCHEDULE_TYPE_FORM)
                                ? scheduleExec.getCustom_form_desc()
                                : scheduleExec.getTicket_type_desc();
                        //
                        //Chama notificação.
                        ToolBox_Inf.showScheduleNotification(
                            getApplicationContext(),
                            scheduleExec.getDate_start(),
                            notificationDesc,
                            scheduleExec.getStatus(),
                            scheduleExec.getFcm_new_status(),
                            scheduleExec.getFcm_user_nick()
                        );
                    }
                }
                //
                if(schedulesToUpdate.size() > 0) {
                    DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(schedulesToUpdate, false);
                    if (daoObjReturn.hasError()) {
                        throw new Exception(daoObjReturn.getErrorMsg());
                    }
                }
            }
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    /*
    private GE_Custom_Form_Data getFormData(GE_Custom_Form_DataDao formDataDao, MD_Schedule_Exec scheduleExec) {
         return formDataDao.getByString(
            new MD_Schedule_Exec_Sql_007(
                String.valueOf(scheduleExec.getCustomer_code()),
                String.valueOf(scheduleExec.getSchedule_prefix()),
                String.valueOf(scheduleExec.getSchedule_code()),
                String.valueOf(scheduleExec.getSchedule_exec())
            ).toSqlQuery()
        );
    }

    private GE_Custom_Form_Local getFormLocal(GE_Custom_Form_LocalDao formLocalDao, MD_Schedule_Exec scheduleExec) {
        return formLocalDao.getByString(
            new MD_Schedule_Exec_Sql_006(
                    String.valueOf(scheduleExec.getCustomer_code()),
                    String.valueOf(scheduleExec.getSchedule_prefix()),
                    String.valueOf(scheduleExec.getSchedule_code()),
                    String.valueOf(scheduleExec.getSchedule_exec())
                ).toSqlQuery()
            );
    }

     */

    private void checkNService_SO_Status(FCMMessage fcmMessage) {

        // Update So
        try {

            SM_SODao sm_soDao = new SM_SODao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            JSONObject jsonObjectRoot = new JSONObject(fcmMessage.getMsg_long());
            JSONObject jsonObject = jsonObjectRoot.getJSONObject("so_update");

            String customer_code = fcmMessage.getCustomer();
            String so_prefix = jsonObject.getString("so_prefix");
            String so_code = jsonObject.getString("so_code");
            String so_scn = jsonObject.getString("so_scn");
            String status = jsonObject.getString("status");

            // Update S.O.
            sm_soDao.addUpdate(
                    new SM_SO_Sql_018(
                            Long.parseLong(customer_code),
                            Integer.parseInt(so_prefix),
                            Integer.parseInt(so_code),
                            Integer.parseInt(so_scn)
                    ).toSqlQuery()
            );


        } catch (JSONException e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        }

        sendFCMStatus(fcmMessage.getTitle());
    }

    private void handleIoFCM(FCMMessage fcmMessage) {
        if(fcmMessage.getTitle().equals(ConstantBaseApp.FCM_ACTION_IO_INBOUND_UPDATE)) {
            handleInboundFCM(fcmMessage);
        }else {
            handleOutboundFCM(fcmMessage);
        }
        //
        sendFCMStatus(fcmMessage.getTitle());
    }

    private void handleInboundFCM(FCMMessage fcmMessage) {
        try{
            IO_InboundDao ioInboundDao = new IO_InboundDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
            );
            JSONObject jsonObjectRoot = new JSONObject(fcmMessage.getMsg_long());
            JSONObject jsonObject = jsonObjectRoot.getJSONObject("inbound");

            String customer_code = fcmMessage.getCustomer();
            String inbound_prefix = jsonObject.getString(IO_InboundDao.INBOUND_PREFIX);
            String inbound_code = jsonObject.getString(IO_InboundDao.INBOUND_CODE);
            String inbound_scn = jsonObject.getString(IO_InboundDao.SCN);
            String status = jsonObject.getString(IO_InboundDao.STATUS);

            // Update S.O.
            ioInboundDao.addUpdate(
                new IO_Inbound_Sql_012(
                    Long.parseLong(customer_code),
                    Integer.parseInt(inbound_prefix),
                    Integer.parseInt(inbound_code),
                    Integer.parseInt(inbound_scn)
                ).toSqlQuery()
            );

        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    private void handleOutboundFCM(FCMMessage fcmMessage) {
        try{
            IO_OutboundDao ioOutboundDao = new IO_OutboundDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );
            JSONObject jsonObjectRoot = new JSONObject(fcmMessage.getMsg_long());
            JSONObject jsonObject = jsonObjectRoot.getJSONObject("outbound");

            String customer_code = fcmMessage.getCustomer();
            String outbound_prefix = jsonObject.getString(IO_OutboundDao.OUTBOUND_PREFIX);
            String outbound_code = jsonObject.getString(IO_OutboundDao.OUTBOUND_CODE);
            String outbound_scn = jsonObject.getString(IO_OutboundDao.SCN);
            String status = jsonObject.getString(IO_OutboundDao.STATUS);

            // Update S.O.
            ioOutboundDao.addUpdate(
                    new IO_Outbound_Sql_012(
                            Long.parseLong(customer_code),
                            Integer.parseInt(outbound_prefix),
                            Integer.parseInt(outbound_code),
                            Integer.parseInt(outbound_scn)
                    ).toSqlQuery()
            );
            //
            sendFCMStatus(fcmMessage.getTitle());
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }


    private void handleTkFMC(FCMMessage fcmMessage) {
        try{
            TK_TicketDao ticketDao = new TK_TicketDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
            );
            JSONObject jsonObjectRoot = new JSONObject(fcmMessage.getMsg_long());
            JSONObject jsonObject = jsonObjectRoot.getJSONObject("ticket");

            String customer_code = fcmMessage.getCustomer();
            String ticket_prefix = jsonObject.getString(TK_TicketDao.TICKET_PREFIX);
            String ticket_code = jsonObject.getString(TK_TicketDao.TICKET_CODE);
            String ticket_scn = jsonObject.getString(TK_TicketDao.SCN);
            //String status = jsonObject.getString("status");
            // Update Ticket
            ticketDao.addUpdate(
                new TK_Ticket_Sql_003(
                    Long.parseLong(customer_code),
                    Integer.parseInt(ticket_prefix),
                    Integer.parseInt(ticket_code),
                    Integer.parseInt(ticket_scn)
                ).toSqlQuery()
            );
            //
            sendFCMStatus(fcmMessage.getTitle());
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    private void sendFCMStatus(String module_type) {
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.WS_FCM);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(ConstantBaseApp.SW_TYPE,module_type);
        //
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mIntent);
    }


    private void makeNF(Context context, String title, String message, long fcmmessage_code, int fcmmessage_qty, String cancellable) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        Intent mIntent = null;

        if (fcmmessage_qty > 1) {
            Bundle bundle = new Bundle();
            bundle.putString("action", "NOTIFICATION");
            mIntent = new Intent(getBaseContext(), Act018_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("action", "NOTIFICATION");
            bundle.putLong("fcmmessage_code", fcmmessage_code);
            mIntent = new Intent(getBaseContext(), Act019_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundle);
        }
        //
        PendingIntent pi = PendingIntent.getActivity(this, 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        //
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_namoa);
        builder.setAutoCancel(false);
        builder.setContentTitle(title);
        builder.setContentIntent(pi);
        if (fcmmessage_qty > 1) {
            builder.setContentText("(" + String.valueOf(fcmmessage_qty) + ") " + context.getResources().getString(R.string.message_received_notification_sync));
        } else {
            builder.setContentText(message);
        }
        /**
         *  BARRIONUEVO 03-04-2020
         *  Tratativa para determinar se notification eh cancelavel.
         */
        if("1".equalsIgnoreCase(cancellable)){
            builder.setOngoing(false);
        }else {
            builder.setOngoing(true);
        }
        //
        long dt_last = ToolBox_Con.getPreference_Google_ID_DT(getApplicationContext());
        Calendar cal_now = Calendar.getInstance();
        long dt_now = cal_now.getTimeInMillis();
        //
        if ((dt_last == 0) || ((dt_now - dt_last) > (120 * 1000))) {
            builder.setDefaults(
                    Notification.DEFAULT_SOUND |
                            Notification.DEFAULT_VIBRATE);
        } else {
        }
        //
        ToolBox_Con.setPreference_Google_ID_DT(getApplicationContext(), dt_now);
        //
        // Devolve a versao do Android instalado no equipamento
        int versao = Build.VERSION.SDK_INT;
        //
        if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nm.notify(10, builder.build());
        } else {
            nm.notify(10, builder.getNotification());
        }
    }

}
