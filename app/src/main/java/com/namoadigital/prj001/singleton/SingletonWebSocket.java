package com.namoadigital.prj001.singleton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_C_Message;
import com.namoadigital.prj001.model.Chat_Login_Env;
import com.namoadigital.prj001.model.Chat_Ref_Json;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.model.Chat_S_Pending_Message;
import com.namoadigital.prj001.receiver_chat.WBR_C_Add_Room;
import com.namoadigital.prj001.receiver_chat.WBR_C_All_Delivered;
import com.namoadigital.prj001.receiver_chat.WBR_C_All_Read;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message_Tmp;
import com.namoadigital.prj001.receiver_chat.WBR_C_Remove_Room;
import com.namoadigital.prj001.receiver_chat.WBR_C_Room;
import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.sql.CH_Message_Sql_011;
import com.namoadigital.prj001.sql.CH_Message_Sql_014;
import com.namoadigital.prj001.sql.CH_Message_Sql_018;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_007;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by neomatrix on 27/11/17.
 */

public class SingletonWebSocket {
    public static String mRoom_private = "";

    public static final String CHAT_TYPE_FILE_TMP = "tmp_";
    private final String WEB_SOCKET_SERVER_DISCONNECT = "io server disconnect";

    private static volatile SingletonWebSocket sSoleInstance;

    private static String mSocket_ID = "";

    public static String getmSocket_ID() {
        return mSocket_ID;
    }

    public PowerManager pm = null;
    public PowerManager.WakeLock wl = null;

    private Context context;
    private File log_file = null;
    private long total_msg = 0;
    private long count_msg = 0;
    private boolean show_notification = false;

    public long getTotal_msg() {
        return total_msg;
    }

    public long getCount_msg() {
        return count_msg;
    }

    public boolean isShow_notification() {
        return show_notification;
    }

    public void setShow_notification(boolean show_notification) {
        this.show_notification = show_notification;
    }

    /*
    Indica se é necessário refazer a conexao em caso de queda do servico
     */
    private boolean mSocketReconnect = true;

    /*
    Indica se o conexao socket está funcionando.
     */
    private boolean mSocketRunning = false;
    private static boolean mSocketLogged = false;

    public static Socket mSocket = null;

    public interface ISingletonWebSocket {
        void chat(String user, String message);
    }

    private ISingletonWebSocket delegate;

    public void setOnISWS(ISingletonWebSocket delegate) {
        this.delegate = delegate;
    }

    public void setmSocketReconnect(boolean mSocketReconnect) {
        this.mSocketReconnect = mSocketReconnect;
    }

    public static boolean isSingletonWebSocketSetted() {
        return sSoleInstance != null;
    }

    public static boolean isSocketSetted() {
        return mSocket != null;
    }

    public boolean ismSocketRunning() {
        return mSocketRunning;
    }

    public static boolean ismSocketLogged() {
        return mSocketLogged;
    }

    private SingletonWebSocket() {
        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public void initConnection() {
        Log.d("ChatEvent", "initConnection");
        //
        if (log_file == null) {
            log_file = new File(Constant.SUPPORT_PATH, "webSocket_log.txt");

        }

        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - initConnection\n", log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        IO.Options options = new IO.Options();
        options.timeout = 1000;
        //options.query = "transport=websocket";
        options.reconnection = true;
        options.reconnectionDelay = 2000;
        options.reconnectionDelayMax = 2000;
        options.randomizationFactor = 0.1;
        //options.forceNew = true;
        options.reconnectionAttempts = 20;

        try {
            mSocket = IO.socket(Constant.WEB_SOCKET_CHAT, options);

            mSocket.on(Constant.CHAT_EVENT_C_LOGIN, onLoginReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ERROR_LOGIN, onErrorLoginReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ERROR, onErrorReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ROOM, onRoomReturn);
            mSocket.on(Constant.CHAT_EVENT_C_PENDING_MESSAGES, onPendingMessagesReturn);
            mSocket.on(Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES, onHistoricalMessagesReturn);
            mSocket.on(Constant.CHAT_EVENT_C_MESSAGE, onMessagesReturn);
            mSocket.on(Constant.CHAT_EVENT_C_MESSAGE_TMP, onMessagesTmpReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ADD_ROOM, onAddRoom);
            mSocket.on(Constant.CHAT_EVENT_C_REMOVE_ROOM, onRemoveRoom);
            mSocket.on(Constant.CHAT_EVENT_C_ALL_DELIVERED, onAllDelivered);
            mSocket.on(Constant.CHAT_EVENT_C_ALL_READ, onAllRead);

            mSocket.on(Constant.CHAT_EVENT_C_ROOM_PRIVATE, onRoomPrivate);

            mSocket.on(Socket.EVENT_RECONNECT, onReconnectReturn);
            mSocket.on(Socket.EVENT_RECONNECTING, onReconnectingReturn);
            mSocket.on(Socket.EVENT_RECONNECT_ERROR, onReconnectError);
            mSocket.on(Socket.EVENT_RECONNECT_FAILED, onReconnectFailed);
            //Ping Pong
            //mSocket.on(Socket.EVENT_PING, onPing);
            mSocket.on("nping", onPing);

            mSocket.on(Socket.EVENT_CONNECT, onConnectReturn);

            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectReturn);

            mSocket.connect();
            //NUNCA INICIALIZAR O mSocket_ID AQUI!!!!
            //mSocket_ID = mSocket.id();
            //Log.d("ChatEvent","Pos connect -> mSocket_ID: " +mSocket_ID);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        Log.d("ChatEvent", "Reconect");
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Reconect   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        disconnect();
        //
        if (mSocketReconnect) {
            Log.d("ChatEvent", "Reconect -> initConnection");
            try {
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - Reconect -> initConnection \n", log_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initConnection();
        }
    }

    public void disconnect() {
        Log.d("ChatEvent", "disconnect");
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocketRunning = false;
            //Pega manager do socket e seta reconnectin para false;
            Manager socketManager = mSocket.io();
            socketManager.reconnection(false);
            mSocket.off();
            mSocket.disconnect();
            mSocket = null;
            String tst = "";
        }
    }

    private void attemptSendPong() {
        //   mSocket.emit(Socket.EVENT_PONG,"APP_PONG" );
        mSocket.emit("npong", "APP_PONG");
    }

    public void attemptSendLogin() {
        Log.d("ChatEvent", "attemptSendLogin");
        try {
            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -- attemptSendLogin ->  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + " - customer: " + ToolBox_Con.getPreference_Customer_Code(context) + "\n", log_file);
            if (mSocket.id() != null) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                Chat_Login_Env env = new Chat_Login_Env();
                EV_User_CustomerDao customerDao = new EV_User_CustomerDao(context);
                String customerList = "";
                String sessionList = "";
                //Seleciona todos customers con sessão ativa e que tem acesso ao chat.
                ArrayList<HMAux> chatSessionCustomers = (ArrayList<HMAux>) customerDao.query_HM(
                        new EV_User_Customer_Sql_007(
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );
                //
                if (chatSessionCustomers != null && chatSessionCustomers.size() > 0) {
                    for (int i = 0; i < chatSessionCustomers.size(); i++) {
                        customerList += chatSessionCustomers.get(i).get(EV_User_CustomerDao.CUSTOMER_CODE) + "|";
                        sessionList += chatSessionCustomers.get(i).get(EV_User_CustomerDao.SESSION_APP) + "|";
                    }
                    customerList = customerList.substring(0, customerList.length() - 1);
                    sessionList = sessionList.substring(0, sessionList.length() - 1);
                }
                //
                env.setUser_code(ToolBox_Con.getPreference_User_Code(context));
                env.setCustomer_code(customerList);
                //env.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
                env.setSession_id(sessionList);
                //env.setSession_id(ToolBox_Con.getPreference_Session_App(context));
                env.setSession_type("APP");
                env.setTranslate_code(ToolBox_Con.getPreference_Translate_Code(context));
                env.setForce(1);
                env.setDevice_code(ToolBox_Inf.uniqueID(context));
                //
                if (mSocket != null) {

                    mSocket.emit(Constant.CHAT_EVENT_S_LOGIN, gson.toJson(env));
                    Log.d("ChatEvent", "sLogin");
                    try {
                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " --- emit sLogin ->  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attemptSendRoom(String message) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_ROOM, message);
            try {
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " ----- sRoom ->  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("ChatEvent", "sRoom");
        }
    }

    public void attemptonRoomPrivate(String deliveryObj) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_ROOM_PRIVATE, deliveryObj);
            Log.d("ChatEvent", "sRoomPrivate");
        }
    }

    public void attemptonLeaveRoom(String deliveryObj) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_LEAVEROOM, deliveryObj);
            Log.d("ChatEvent", "sLeaveRoom");
        }
    }

    public void attemptSendPendingMessages(String room_code) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            CH_MessageDao messageDao = new CH_MessageDao(context);
            //
            ArrayList<HMAux> refJsonAux = (ArrayList<HMAux>) messageDao.query_HM(
                    new CH_Message_Sql_018(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            ToolBox_Con.getPreference_User_Code(context),
                            room_code
                    ).toSqlQuery()
            );
            ArrayList<Chat_Ref_Json> ref_json = new ArrayList<>();
            if (refJsonAux != null && refJsonAux.size() > 0) {
                for (HMAux hmAux : refJsonAux) {
                    if (hmAux.get(CH_MessageDao.MSG_PREFIX) != null && hmAux.get(CH_MessageDao.MSG_CODE) != null) {
                        Chat_Ref_Json refAux = new Chat_Ref_Json();
                        refAux.setMsg_prefix(
                                Integer.valueOf(hmAux.get(CH_MessageDao.MSG_PREFIX))
                        );
                        refAux.setMsg_code(
                                Integer.valueOf(hmAux.get(CH_MessageDao.MSG_CODE))
                        );
                        //
                        ref_json.add(refAux);
                    }
                }
            }
            //
            Chat_S_Pending_Message sPendingMessage = new Chat_S_Pending_Message();
            sPendingMessage.setRoom_code(room_code);
            sPendingMessage.setRef_json(ref_json);
            //
            String message = ToolBox_Inf.setWebSocketJsonParam(sPendingMessage);
            //
            mSocket.emit(Constant.CHAT_EVENT_S_PENDING_MESSAGES, message);
            Log.d("ChatEvent", "sPendingMessages");
        }
    }

    public void attemptSendHistoricalMessages(String message) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_HISTORICAL_MESSAGES, message);
            Log.d("ChatEvent", "sHistoricalMessages");
            try {
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " ------- sHistoricalMessages \n", log_file);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void attemptSendMessages(String message) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            //
            mSocket.emit(Constant.CHAT_EVENT_S_MESSAGE, message);
        }
    }

    public void attemptSendMessageTmp(String message) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_MESSAGE_TMP, message);
        }
    }

    public void attemptToDeliveryMessage(String deliveryObj) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_DELIVERED, deliveryObj);
            try {
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -    sDeliveryMessage ->  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("ChatEvent", "sDeliveryMessage");
        }
    }

    public void attemptToReadMessage(String readObj) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Constant.CHAT_EVENT_S_READ, readObj);
            Log.d("ChatEvent", "sReadMessage");
        }
    }

    public void attemptDisconnect(String message) {
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit(Socket.EVENT_DISCONNECT, message);
        }
    }

    public void attemptNamoa() {
        String message = ToolBox_Inf.uniqueID(context);
        if (mSocket != null && sSoleInstance.mSocketRunning) {
            mSocket.emit("sNamoa", message);
        }
    }

    private Emitter.Listener onLoginReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Inicializa socket_id local
            //Esse é o primeiro momento em que temos o socket_id retornado
            //do server.
            mSocket_ID = mSocket.id();
            //
            changeLoggedStatus(true);
            //
            Log.d("ChatEvent", "---- cLogin -> mSocket_ID: " + mSocket_ID);
            //
            attemptSendRoom("");
        }
    };
    //region CONNECT OR DISCONNECT
    private Emitter.Listener onConnectReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "onConnect   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null "));
            try {
                //Chama metodo que verifica se precisa
                checkForNewLogin();
                //
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - onConnect   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);
                //
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocketRunning = true;
        }
    };
    //
    private Emitter.Listener onDisconnectReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "onDisconect   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + " - origin: " + String.valueOf(args[0]));
            try {
                //mSocketRunning = false;
                changeLoggedStatus(false);
                //
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - onDisconect - Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + " - origin: " + String.valueOf(args[0]) + "\n", log_file);
                //
                if(ToolBox_Inf.isScreenOn(context)) {
                    //
                    if (ToolBox_Inf.isUsrAppLogged(context)) {
                        String param = String.valueOf(args[0]);
                        //Se desconect partiu do server, força initConnection
                        if (param.equals(WEB_SOCKET_SERVER_DISCONNECT)) {
                            //disconnect();
                            if (AppBackgroundService.isRunning) {
                                initConnection();
                            } else {
                                startChatService();
                                //initConnection();
                            }
                        }
                    } else {
                        Log.d("ChatEvent", "onDisconect   - Not Logged , destroySingleton");
                        stopChatService();
                    }
                }else{
                    Log.d("ChatEvent", "onDisconect   - Tela apagada, destroySingleton");
                    stopChatService();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // reconnect();
        }
    };

    //endregion

    //region RECONNECT EVENTS
    private Emitter.Listener onReconnectReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Cancela notificação de reconnectando
            //ToolBox_Inf.cancelChatNotification(context);
            try {
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - RECONNECT  \n", log_file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            if(ToolBox_Inf.isUsrAppLogged(context)) {
                if (mSocket != null || AppBackgroundService.isRunning) {
                    if (mSocket != null && AppBackgroundService.isRunning) {
                        Log.d("ChatEvent", "EVENT_RECONNECT   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null "));
                        //Chama metodo que verifica se precisa
                        checkForNewLogin();
                    } else if (!AppBackgroundService.isRunning) {
                        Log.d("ChatEvent", "EVENT_RECONNECT  -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "  Serviço estava para vai ser iniciado");
                        //
                        startChatService();
                    }
                    //
                    ToolBox_Inf.sendBRChat(context, Constant.CHAT_BR_TYPE_RECONNECTED);
                } else {
                    startChatService();
                    ToolBox_Inf.sendBRChat(context, Constant.CHAT_BR_TYPE_RECONNECTED);
                    Log.d("ChatEvent", "EVENT_RECONNECT");
                }
            }else{
                Log.d("ChatEvent", "EVENT_RECONNECTING - NOT LOGGED");
            }
        }
    };

    private Emitter.Listener onReconnectingReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "EVENT_RECONNECTING - teste");
            try {
                if(ToolBox_Inf.isUsrAppLogged(context)) {

                    if (mSocket != null) {
                        Log.d("ChatEvent", "EVENT_RECONNECTING   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "Tentativa :  " + String.valueOf(args[0]));
                    } else {
                        Log.d("ChatEvent", "EVENT_RECONNECTING - Tentativa :  " + String.valueOf(args[0]));
                    }
                    try {
                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - EVENT_RECONNECTING. Tentativa :  " + String.valueOf(args[0]) + "  \n", log_file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - EVENT_RECONNECTING - NOT LOGGED. Tentativa :  " + String.valueOf(args[0]) + "  \n", log_file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("ChatEvent", "EVENT_RECONNECTING - NOT LOGGED");
                }
                //
                HMAux hmAux = new HMAux();
                hmAux.put(Constant.CHAT_BR_PARAM_RECONNECTING_QTD, String.valueOf(args[0]));
                ToolBox_Inf.sendBRChat(context, Constant.CHAT_BR_TYPE_RECONNECTING, hmAux);
                //ToolBox_Inf.showChatNotification(context, Constant.CHAT_NOTIFICATION_TYPE_RECONNECTING, String.valueOf(args[0]));
                //
                changeLoggedStatus(false);
                //
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener onReconnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocket != null) {
                Log.d("ChatEvent", "EVENT_RECONNECT_ERROR   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ")
                        + " - Error:  " + String.valueOf(args[0]));
            } else {
                Log.d("ChatEvent", "EVENT_RECONNECT_ERROR");
            }
        }
    };

    private Emitter.Listener onReconnectFailed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - EVENT_RECONNECT_FAILED  \n", log_file);
                Log.d("ChatEvent", "EVENT_RECONNECT_FAILED");
                if(ToolBox_Inf.isUsrAppLogged(context)) {
                    if (mSocket != null) {
                        if (ToolBox_Inf.isScreenOn(context)) {
                            if (AppBackgroundService.isRunning) {
                                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - EVENT_RECONNECT_FAILED - Screen ON Força iniConnect  \n", log_file);
                                initConnection();
                                Log.d("ChatEvent", "EVENT_RECONNECT_FAILED   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + " Screen ON Força iniConnect. ");
                            } else {
                                ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - EVENT_RECONNECT_FAILED - Screen ON Restarta Serviço  \n", log_file);
                                Log.d("ChatEvent", "EVENT_RECONNECT_FAILED   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + " Screen ON Restarta Serviço. ");
                                startChatService();
                            }

                        } else {
                            ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - EVENT_RECONNECT_FAILED - Screen OFF. Para Serviço  \n", log_file);
                            stopChatService();
                            Log.d("ChatEvent", "EVENT_RECONNECT_FAILED   -  Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + " Parou serviço. ");
                        }

                    } else {
                        Log.d("ChatEvent", "EVENT_RECONNECT_FAILED - Socket Null");
                    }
                }else{
                    stopChatService();
                    Log.d("ChatEvent", "EVENT_RECONNECT_FAILED - NOT LOGGED");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //endregion

    //region PING/PONG  onPing
    private Emitter.Listener onPing = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocket != null) {
                Log.d("ChatEvent", "onPing -  Socket_id: " + (mSocket != null ? mSocket.id() : " null "));

                attemptSendPong();
            }
        }
    };
    //endregion

    //region ROOM EVENTS
    private Emitter.Listener onRoomReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cRoom Socket_Id: " + (mSocket != null ? mSocket.id() : ""));
            //retorna rooms
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cRoomIntent = new Intent(context, WBR_C_Room.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    cRoomIntent.putExtras(bundle);
                    context.sendBroadcast(cRoomIntent);
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
            //
        }
    };

    private Emitter.Listener onAddRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cAddRoom");
            //retorna rooms
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cRoomIntent = new Intent(context, WBR_C_Add_Room.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_ADD_ROOM);
                    cRoomIntent.putExtras(bundle);
                    context.sendBroadcast(cRoomIntent);
                    // Hugo Verificar
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };

    private Emitter.Listener onRemoveRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cRemoveRoom");
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cRoomIntent = new Intent(context, WBR_C_Remove_Room.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    cRoomIntent.putExtras(bundle);
                    context.sendBroadcast(cRoomIntent);
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };
    //endregion

    //region MSGs EVENTS
    private Emitter.Listener onPendingMessagesReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cPendingMessages");
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    processMessages(param);
                    //
                   /* Intent cMessageIntent = new Intent(context, WBR_C_Message.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    cMessageIntent.putExtras(bundle);
                    context.sendBroadcast(cMessageIntent);*/
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };

    private Emitter.Listener onHistoricalMessagesReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                Log.d("ChatEvent", "cHistoricalMessages");
                try {
                    ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -------- cHistoricalMessages \n", log_file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (args != null && args.length > 0) {
                    if (args[0] instanceof String) {
                        String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                        processMessages(param);
                    } else {
                        String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
    };

    private Emitter.Listener onMessagesReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cMessageIntent = new Intent(context, WBR_C_Message.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_MESSAGE);
                    cMessageIntent.putExtras(bundle);
                    context.sendBroadcast(cMessageIntent);
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };

    private Emitter.Listener onMessagesTmpReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cMessageIntent = new Intent(context, WBR_C_Message_Tmp.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    cMessageIntent.putExtras(bundle);
                    context.sendBroadcast(cMessageIntent);
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };
    //endregion

    //region DELIVERY AND READ EVENTS
    private Emitter.Listener onAllDelivered = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cAllDelivery");
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cMessageIntent = new Intent(context, WBR_C_All_Delivered.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    cMessageIntent.putExtras(bundle);
                    context.sendBroadcast(cMessageIntent);
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };

    private Emitter.Listener onAllRead = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cAllRead");
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cMessageIntent = new Intent(context, WBR_C_All_Read.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, param);
                    cMessageIntent.putExtras(bundle);
                    context.sendBroadcast(cMessageIntent);
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };
    //endregion

    private Emitter.Listener onRoomPrivate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Log.d("ChatEvent", "cRoomPrivateCustomer");
//
//            mRoom_private = "";
//
//            if (args != null && args.length > 0) {
//                if (args[0] instanceof String) {
//                    try {
//                        String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
//                        //
//                        Intent cRoomPrivateIntent = new Intent(Constant.CHAT_EVENT_C_ROOM_PRIVATE);
//                        cRoomPrivateIntent.addCategory(Intent.CATEGORY_DEFAULT);
//
//                        JSONObject jsonObject = new JSONObject(param);
//
//                        if (jsonObject.getInt("active") == 1) {
//                            mRoom_private = jsonObject.getString("room_code");
//                            //
//                            Bundle bundle = new Bundle();
//                            bundle.putString(Constant.CHAT_WS_JSON_PARAM, mRoom_private);
//                            cRoomPrivateIntent.putExtras(bundle);
//                            LocalBroadcastManager.getInstance(context).sendBroadcast(cRoomPrivateIntent);
//                        }
//                    } catch (Exception e) {
//                    }
//
//                } else {
//                    String tst = "No Json";
//                    /*
//                    * Verificar como proceder caso o retorno não seja uma string
//                    *
//                    * */
//                }
//            }
        }
    };


    //region ERROR EVENTS
    private Emitter.Listener onErrorLoginReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cErrorLogin  -> " + String.valueOf(args[0]));
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    //
                    try {
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                        //
                        Chat_C_Error cError =
                                gson.fromJson(
                                        param,
                                        Chat_C_Error.class
                                );

                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " ---- cLoginError -> " + (cError != null && cError.getError_msg() != null ? cError.getError_msg() : " null  ") + " Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);

                        //
                        if (cError != null && cError.getError_msg() != null) {
                            switch (cError.getError_msg()) {
                                case Constant.CHAT_ERROR_SESSION_NOT_FOUND:
                                    stopChatService();
                                    break;
                                case Constant.CHAT_ERROR_CHAT_SESSION_NOT_FOUND:
                                    initConnection();
                                    break;
                                case Constant.CHAT_ERROR_CUSTOMER_NOT_ACCESS_CHAT:
                                default:
                                    String tst = cError.getError_msg();
                                    String tst2 = tst + ";";
                                    break;
                            }

                        } else {
                            //Como tratar
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        ToolBox_Inf.registerException(getClass().getName(), e);
                    }
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };

    private Emitter.Listener onErrorReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ChatEvent", "cError  -> " + String.valueOf(args[0]));
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    //
                    try {
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                        //
                        Chat_C_Error cError =
                                gson.fromJson(
                                        param,
                                        Chat_C_Error.class
                                );

                        ToolBox_Inf.writeIn(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " - cError -> " + (cError != null && cError.getError_msg() != null ? cError.getError_msg() : " null  ") + " Socket_id: " + (mSocket != null ? mSocket.id() : " null ") + "\n", log_file);
                        //
                        if (cError != null && cError.getError_msg() != null) {
                            switch (cError.getError_msg()) {
                                case Constant.CHAT_ERROR_SESSION_NOT_FOUND:
                                    stopChatService();
                                    break;
                                case Constant.CHAT_ERROR_CHAT_SESSION_NOT_FOUND:
                                    initConnection();
                                    break;
                                case Constant.CHAT_ERROR_CUSTOMER_NOT_ACCESS_CHAT:
                                default:
                                    String tst = cError.getError_msg();
                                    //Erro de complicação do oracle, desce o serviço e depois reinicia
                                    if (cError.getError_msg().contains("ORA-04068")) {
                                        //AppBackgroundService.restartSingleton(context);
                                        //mSocket.emit(Socket.EVENT_DISCONNECT,"App Restart");
                                        disconnect();
                                        //
                                        initConnection();
                                        //
                                    }else{
                                        Exception e = new Exception(cError.getError_msg());
                                        //
                                        ToolBox_Inf.registerException(getClass().getName(),e);
                                        //
                                        disconnect();
                                        //
                                        initConnection();
                                    }
                                    break;
                            }

                        } else {
                            //Como tratar
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        ToolBox_Inf.registerException(getClass().getName(), e);
                    }
                } else {
                    String tst = "No Json";
                    /*
                    * Verificar como proceder caso o retorno não seja uma string
                    *
                    * */
                }
            }
        }
    };
    //endregion

    //region AUX METHODS
    private void checkForNewLogin() {
        if (mSocket != null && mSocket.id() != null && mSocket_ID != null && !mSocket_ID.equalsIgnoreCase(mSocket.id())) {
            mSocket_ID = mSocket.id();
            //Reseta variaveis do cHistoricalMessage
            resetProcessMsgCounter();
            //
            attemptSendLogin();
        }
    }

    private void startChatService() {
        Intent chatService = new Intent(context, AppBackgroundService.class);
        chatService.putExtra(Constant.CHAT_START_SERVICE_CALLER, getClass().getName());
        context.startService(chatService);
    }

    private void stopChatService() {
        destroySingletonWebSocket();
        Intent chatService = new Intent(context, AppBackgroundService.class);
        context.stopService(chatService);
    }

    public void attempSendOfflineMessages() {
        CH_MessageDao messageDao = new CH_MessageDao(context);
        //
        ArrayList<CH_Message> offlineMsgs =
                (ArrayList<CH_Message>) messageDao.query(
                        new CH_Message_Sql_011().toSqlQuery()
                );
        //
        if (offlineMsgs != null && offlineMsgs.size() > 0) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            for (CH_Message chMessage : offlineMsgs) {

                JSONObject msg_obj = null;
                try {
                    msg_obj = new JSONObject(chMessage.getMsg_obj());

                    JSONObject msg_obj_content = (JSONObject) msg_obj.get("message");
                    String msg_obj_type = (String) msg_obj_content.get("type");
                    String msg_obj_data = (String) msg_obj_content.get("data");

                    Chat_S_Message sMessage = new Chat_S_Message();
                    //
                    sMessage.setRoom_code(chMessage.getRoom_code());
                    sMessage.setTmp(chMessage.getTmp());
                    sMessage.setType(msg_obj_type);
                    sMessage.setData(msg_obj_data);
                    //
                    attemptSendMessages(gson.toJson(sMessage));
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToolBox_Inf.registerException(getClass().getName(), e);
                }
            }
        }

    }

    private String createMsgsFile(String param, String type) {
        String fileName = Constant.CHAT_PREFIX +
                (type != null ? type : "") +
                ToolBox_Inf.getToken(context) +
                "_" + UUID.randomUUID().toString() +
                ".txt";
        //
        File msgListFile = new File(Constant.CHAT_PATH, fileName);
        try {
            ToolBox_Inf.writeIn(param, msgListFile);
            return msgListFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(), e);
            return null;
        }
    }

    private void processMessages(String param) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        CH_MessageDao messageDao = new CH_MessageDao(context);

        JSONArray cMessagesTmp = new JSONArray();
        JSONArray cMessagesNew = new JSONArray();
        ArrayList<Chat_C_Message> messagesMineToInsert = new ArrayList<>();
        //
        String cMessageFilePath = "";
        String cMessageTmpFilePath = "";
        //
        try {
            ArrayList<Chat_C_Message> messages = gson.fromJson(
                    param,
                    new TypeToken<ArrayList<Chat_C_Message>>() {
                    }.getType());
            //Se não houver msg, envia broadcast de Scrool_Up
            if (messages.size() == 0) {
                ToolBox_Inf.sendBRChat(context, Constant.CHAT_BR_TYPE_MSG_SCROLL_UP);
                return;
            }
            /*
            * Se Ação do cHistoricalMessage é SCROLL_UP, pula processamento das listas
            * e direciona msgs para o serviço.
            */
            if (messages.get(0).getAction().equalsIgnoreCase(Constant.CHAT_HISTORICAL_MSG_ACTION_SCROLL_UP)) {
                //
                cMessageFilePath = createMsgsFile(param, null);
                //
                Intent cMessageIntent = new Intent(context, WBR_C_Message.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CHAT_WS_JSON_PARAM, cMessageFilePath);
                bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES);
                bundle.putString(Constant.CHAT_WS_HISTORICAL_ACTION_PARAM, Constant.CHAT_HISTORICAL_MSG_ACTION_SCROLL_UP);
                //
                cMessageIntent.putExtras(bundle);
                context.sendBroadcast(cMessageIntent);

            } else {
                //Atualiza total de msg e contador de msg
                total_msg = total_msg == 0 ? messages.get(0).getMsg_count() : total_msg;

                for (Chat_C_Message chatCMessage : messages) {
                    //Analise da lista de de - para
                    if (ToolBox_Con.getPreference_User_Code(context).equals(
                            String.valueOf(chatCMessage.getUser_code()))
                            && chatCMessage.getDelivered() == 0
                            ) {
                        CH_Message localMessage =
                                messageDao.getByString(
                                        new CH_Message_Sql_014(
                                                chatCMessage.getMsg_tmp(),
                                                ToolBox_Con.getPreference_User_Code(context)
                                        ).toSqlQuery()
                                );
                        if (
                                chatCMessage.getMsg_tmp() > 0 &&
                                        chatCMessage.getMsg_code() > 0 &&
                                        localMessage != null &&
                                        localMessage.getTmp() > -1 &&
                                        localMessage.getMsg_code() == 0 &&
                                        chatCMessage.getMsg_origin().equalsIgnoreCase("APP")

                                ) {
                            cMessagesTmp.put(new JSONObject(gson.toJson(chatCMessage)));

                            //} else if (
                        } else {
                               /* chatCMessage.getMsg_tmp() > 0 &&
                                        chatCMessage.getMsg_code() > 0 &&
                                        localMessage != null &&
                                        localMessage.getTmp() == -1
                                ) {
                            //Se msg é minha, ja teve de para processado em outro device,
                            //zera o tmp vindo do server e para gerar um novo.
                            chatCMessage.setMsg_tmp(0);
                            messagesMineToInsert.add(chatCMessage);*/
                            //
                            cMessagesNew.put(new JSONObject(gson.toJson(chatCMessage)));
                        }
                    } else {
                        cMessagesNew.put(new JSONObject(gson.toJson(chatCMessage)));
                    }
                }
                //Se existir, insere msg minhas que não estão locais.
                //Esse caso só acontece com msg que eu criei em outro device e
                //recebe ela no cMessage em outro device.
                if (messagesMineToInsert.size() > 0) {
                    ArrayList<CH_Message> chMessages = Chat_C_Message.toCH_MessageList(messagesMineToInsert);
                    //
                    messageDao.addUpdate(chMessages, false);
                }
                //Se existe alguma msg minha que recebeu msg_code, mas aind anão processei de-para
                //Gera arquivo texto com as msg
                if (cMessagesTmp.length() > 0) {
                    cMessageTmpFilePath = createMsgsFile(cMessagesTmp.toString(), CHAT_TYPE_FILE_TMP);
                }

                if (cMessagesNew.length() > 0) {
                    cMessageFilePath = createMsgsFile(cMessagesNew.toString(), null);
                    //
                    Intent cMessageIntent = new Intent(context, WBR_C_Message.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM, cMessageFilePath);
                    bundle.putString(Constant.CHAT_WS_EVENT_PARAM, Constant.CHAT_EVENT_C_HISTORICAL_MESSAGES);
                    bundle.putString(Constant.CHAT_WS_HISTORICAL_ACTION_PARAM, Constant.CHAT_HISTORICAL_MSG_ACTION_LOGIN);
                    bundle.putString(Constant.CHAT_WS_MSG_TMP_PARAM, cMessageTmpFilePath);
                    bundle.putLong(Constant.CHAT_WS_MSG_COUNTER_PARAM, messages.size());

                    cMessageIntent.putExtras(bundle);
                    context.sendBroadcast(cMessageIntent);
                    //
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean areAllMsgProcessed() {
        return count_msg == total_msg;
    }

    public void resetProcessMsgCounter() {
        total_msg = count_msg = 0;
        show_notification = false;
    }

    public long updateCounterMsg(long increment) {
        count_msg += increment;
        return count_msg;
    }

    private void changeLoggedStatus(boolean logged) {
        if (mSocketLogged != logged) {
            mSocketLogged = logged;
            ToolBox_Inf.sendBRChat(context, Constant.CHAT_BR_TYPE_CHAT_LOGGED_STATUS_CHANGE);
        }
    }

    //endregion

    /**
     * OnConnection Changed precisa chamar esse método para reiniciar a conexao em caso de falha.
     *
     * @return
     */
    public static SingletonWebSocket getInstance(Context context) {
        Log.d("ChatEvent", " SingletonWebSocket.getInstance , chamado por: " + context.getClass().getSimpleName() );
        Log.d("ChatEvent", " SingletonWebSocket.getInstance = " + (sSoleInstance == null ?" Null" :" Ja iniciado"));
        //Double check locking pattern
        if (sSoleInstance == null) { //Check for the first time

            synchronized (SingletonWebSocket.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sSoleInstance == null) {
                    sSoleInstance = new SingletonWebSocket();
                    sSoleInstance.context = context;
                    //
                    sSoleInstance.pm = (PowerManager) sSoleInstance.context.getSystemService(Context.POWER_SERVICE);
                    sSoleInstance.wl = sSoleInstance.pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PM_SingletonWebSocket");
                    //
                    sSoleInstance.initConnection();
                }
            }
        }

        return sSoleInstance;
    }

    public void destroySingletonWebSocket() {
        if(mSocket != null){
            //Pega manager do socket e seta reconnectin para false;
            Manager socketManager = mSocket.io();
            socketManager.reconnection(false);
            //
            mSocket.off();
            mSocket.disconnect();
            mSocket = null;
        }
        //
        if(sSoleInstance != null) {
            sSoleInstance.mSocketRunning = false;
            //sSoleInstance.mSocket = null;
            //
            //sSoleInstance.context = null;
            //
            sSoleInstance.pm = null;
            sSoleInstance.wl = null;
            sSoleInstance = null;
        }
        Log.d("ChatEvent", " destroySingletonWebSocketV\n");
    }

}

