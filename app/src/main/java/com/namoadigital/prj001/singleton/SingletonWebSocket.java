package com.namoadigital.prj001.singleton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.Chat_Login_Env;
import com.namoadigital.prj001.receiver_chat.WBR_C_Add_Room;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message;
import com.namoadigital.prj001.receiver_chat.WBR_C_Message_Tmp;
import com.namoadigital.prj001.receiver_chat.WBR_C_Remove_Room;
import com.namoadigital.prj001.receiver_chat.WBR_C_Room;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by neomatrix on 27/11/17.
 */

public class SingletonWebSocket {
    private static volatile SingletonWebSocket sSoleInstance;

    private static String mSocket_ID = "";

    public static String getmSocket_ID() {
        return mSocket_ID;
    }

    private Context context;
    private File log_file = null;

    /*
    Indica se é necessário refazer a conexao em caso de queda do servico
     */
    private boolean mSocketReconnect = true;

    /*
    Indica se o conexao socket está funcionando.
     */
    private boolean mSocketRunning = false;

    public Socket mSocket;

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

    private SingletonWebSocket() {
        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public void initConnection() {

        if(log_file == null){
            log_file = new File(Constant.SUPPORT_PATH,"webSocket_log.txt");

        }

        Log.d("Chat", "initConnection");

        try {
            ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - initConnection\n",log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IO.Options options = new IO.Options();
        options.timeout = 1000;
        options.query = "transport=websocket";
        options.reconnection = true;
        //options.reconnectionAttempts = 2;

        try {
            mSocket = IO.socket("https://chat.namoadigital.com", options);

            mSocket.on(Constant.CHAT_EVENT_C_LOGIN, onLoginReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ERROR_LOGIN, onErrorLoginReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ERROR, onErrorReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ROOM, onRoomReturn);
            mSocket.on(Constant.CHAT_EVENT_C_PENDING_MESSAGES, onPendingMessagesReturn);
            mSocket.on(Constant.CHAT_EVENT_C_MESSAGE, onMessagesReturn);
            mSocket.on(Constant.CHAT_EVENT_C_MESSAGE_TMP, onMessagesTmpReturn);
            mSocket.on(Constant.CHAT_EVENT_C_ADD_ROOM, onAddRoom);
            mSocket.on(Constant.CHAT_EVENT_C_REMOVE_ROOM, onRemoveRoom);

            mSocket.on(Socket.EVENT_RECONNECT, onReconnectReturn);
            mSocket.on(Socket.EVENT_RECONNECTING, onReconnectingReturn);
            mSocket.on(Socket.EVENT_RECONNECT_ERROR, onReconnectError);
            mSocket.on(Socket.EVENT_RECONNECT_ERROR, onReconnectError);
            mSocket.on(Socket.EVENT_RECONNECT_FAILED, onReconnectFailed);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("Chat", "onConnect   -  Socket_id: " + mSocket.id());
                    try {
                        ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - onConnect   -  Socket_id: " + mSocket.id()+"\n",log_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mSocketRunning = true;
                }
            });

            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("Chat", "onDisconect   -  Socket_id: " + mSocket.id());
                    try {
                        ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - onDisconect\nSocket_id: " + mSocket.id()+"\n",log_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // reconnect();
                }
            });

            mSocket.connect();

            attemptSendLogin();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        Log.d("Chat", "Reconect");
        try {
            ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - Reconect   -  Socket_id: " + mSocket.id()+"\n",log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        disconnect();
        //
        if (mSocketReconnect) {
            Log.d("Chat", "Reconect -> initConnection");
            try {
                ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - Reconect -> initConnection \n",log_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initConnection();
        }
    }

    public void disconnect() {
        mSocketRunning = false;
        Log.d("Chat", "disconnect");
        if (mSocket != null) {
            mSocket.off();
            mSocket.disconnect();
            mSocket = null;
        }
    }

    public void attemptSendLogin() {
        Log.d("Chat", "attemptSendLogin");
        try {
            ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - attemptSendLogin ->  Socket_id: " + mSocket.id()+"\n",log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Chat_Login_Env env = new Chat_Login_Env();
            //
            env.setUser_code(ToolBox_Con.getPreference_User_Code(context));
            env.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
            env.setSession_id(ToolBox_Con.getPreference_Session_App(context));
            env.setSession_type("APP");
            env.setTranslate_code(ToolBox_Con.getPreference_Translate_Code(context));
            //
            if (mSocket != null) {
                mSocket.emit(Constant.CHAT_EVENT_S_LOGIN, gson.toJson(env));
                Log.d("Chat", "sLogin");
                try {
                    ToolBox_Inf.writeIn(ToolBox_Inf.convertToDeviceTMZ("") + " - emit sLogin ->  Socket_id: " + mSocket.id()+"\n",log_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attemptSendRoom(String message) {
        if (mSocket != null) {
            mSocket.emit(Constant.CHAT_EVENT_S_ROOM, message);

        }
    }

    public void attemptSendPendingMessages(String message) {
        if (mSocket != null) {
            mSocket.emit(Constant.CHAT_EVENT_S_PENDING_MESSAGES, message);
        }
    }

    public void attemptSendMessages(String message) {
        if (mSocket != null) {
            mSocket.emit(Constant.CHAT_EVENT_S_MESSAGE, message);
        }
    }

    public void attemptSendMessageTmp(String message) {
        if (mSocket != null) {
            mSocket.emit(Constant.CHAT_EVENT_S_MESSAGE_TMP, message);
        }
    }

    public void attemptDisconnect(String message) {
        if (mSocket != null) {
            mSocket.emit(Socket.EVENT_DISCONNECT, message);
        }
    }


    private Emitter.Listener onLoginReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            attemptSendRoom("");
            //attemptSendRoom("");
        }
    };

    //region RECONNECT EVENTS
    private Emitter.Listener onReconnectReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(mSocket!=null) {
                Log.d("Chat", "EVENT_RECONNECT   -  Socket_id: " + mSocket.id());
                if(!mSocket_ID.equalsIgnoreCase(mSocket.id())){
                    mSocket_ID = mSocket.id();
                    //
                    attemptSendLogin();
                }
                ToolBox_Inf.sendBRChat(context,Constant.CHAT_BR_TYPE_RECONNECTED);
            }else{
                Log.d("Chat", "EVENT_RECONNECT");
            }
        }
    };

    private Emitter.Listener onReconnectingReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(mSocket!=null) {
                Log.d("Chat", "EVENT_RECONNECTING   -  Socket_id: " + mSocket.id());
            }else{
                Log.d("Chat", "EVENT_RECONNECTING");
            }
            //
            HMAux hmAux = new HMAux();
            hmAux.put(Constant.CHAT_BR_PARAM_RECONNECTING_QTD,String.valueOf(args[0]));
            ToolBox_Inf.sendBRChat(context,Constant.CHAT_BR_TYPE_RECONNECTING,hmAux);
        }
    };

    private Emitter.Listener onReconnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(mSocket!=null) {
                Log.d("Chat", "EVENT_RECONNECT_ERROR   -  Socket_id: " + mSocket.id());
            }else{
                Log.d("Chat", "EVENT_RECONNECT_ERROR");
            }
        }
    };

    private Emitter.Listener onReconnectFailed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(mSocket!=null) {
                Log.d("Chat", "EVENT_RECONNECT_FAILED   -  Socket_id: " + mSocket.id());
            }else{
                Log.d("Chat", "EVENT_RECONNECT_FAILED");
            }
        }
    };
    //endregion

    //region ROOM EVENTS
    private Emitter.Listener onRoomReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //retorna rooms
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cRoomIntent = new Intent(context,WBR_C_Room.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM,param);
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
            attemptSendPendingMessages("");
        }
    };

    private Emitter.Listener onAddRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //retorna rooms
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cRoomIntent = new Intent(context,WBR_C_Add_Room.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM,param);
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

    private Emitter.Listener onRemoveRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cRoomIntent = new Intent(context, WBR_C_Remove_Room.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM,param);
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
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cMessageIntent = new Intent(context,WBR_C_Message.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM,param);
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

    private Emitter.Listener onMessagesReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    String param = ToolBox_Inf.getWebSocketJsonParam(String.valueOf(args[0]));
                    //
                    Intent cMessageIntent = new Intent(context,WBR_C_Message.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM,param);
                    bundle.putString(Constant.CHAT_WS_EVENT_PARAM,Constant.CHAT_EVENT_C_MESSAGE);
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
                    Intent cMessageIntent = new Intent(context,WBR_C_Message_Tmp.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_JSON_PARAM,param);
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

    //region ERROR EVENTS
    private Emitter.Listener onErrorLoginReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            int i = 10;
        }
    };

    private Emitter.Listener onErrorReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            int i = 10;
        }
    };
    //endregion

    /**
     * OnConnection Changed precisa chamar esse método para reiniciar a conexao em caso de falha.
     *
     * @return
     */
    public static SingletonWebSocket getInstance(Context context) {
        //Double check locking pattern
        if (sSoleInstance == null) { //Check for the first time

            synchronized (SingletonWebSocket.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sSoleInstance == null) {
                    sSoleInstance = new SingletonWebSocket();
                    sSoleInstance.context = context;
                }
            }
        }

        if (!sSoleInstance.mSocketRunning) {
            sSoleInstance.initConnection();
        }

        return sSoleInstance;
    }
}

