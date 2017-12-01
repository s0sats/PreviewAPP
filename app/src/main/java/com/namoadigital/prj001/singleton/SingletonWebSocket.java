package com.namoadigital.prj001.singleton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.model.Chat_Login_Env;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.receiver_chat.WBR_C_Room;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by neomatrix on 27/11/17.
 */

public class SingletonWebSocket {
    private static volatile SingletonWebSocket sSoleInstance;

    private Context context;

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
        Log.d("Chat", "initConnection");

        IO.Options options = new IO.Options();
        options.timeout = 1000;

        try {
            mSocket = IO.socket("https://chat.namoadigital.com", options);

            mSocket.on("cLogin", onLoginReturn);
            mSocket.on("error", onErrorReturn);
            mSocket.on("cRoom", onRoomReturn);
            mSocket.on("cPendingMessages", onPendingMessagesReturn);
            mSocket.on("cMessages", onMessagesReturn);

            mSocket.connect();

            attemptSendLogin();

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("Chat", "onConnect");
                    mSocketRunning = true;
                }
            });

            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("Chat", "onDisconect");
                    reconnect();
                }
            });

            mSocket.connect();

                        /*
            *TESTE ENVIO DE MSG APAGAR DEPOIS
            */

            Chat_S_Message sMessage = new Chat_S_Message();
            sMessage.setRoom_code("2017.F");
            sMessage.setType(Constant.CHAT_MESSAGE_TYPE_TEXT);
            sMessage.setData("Msg teste padrão do app n:"+ new Random().nextInt(100));
            sMessage.setTmp(1);

            Gson gson = new GsonBuilder().serializeNulls().create();

            attemptSendMessages(gson.toJson(sMessage));
            //  attemptSendMessages(gson.toJson(chatObj));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        Log.d("Chat", "Reconect");
        disconnect();
        //
        if (mSocketReconnect) {
            Log.d("Chat", "Reconect -> initConnection");
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
                mSocket.emit("sLogin", gson.toJson(env));
                Log.d("Chat", "sLogin");
            }
        } catch (Exception e) {
        }
    }

    public void attemptSendRoom(String message) {
        if (mSocket != null) {
            mSocket.emit("sRoom", message);
        }
    }

    public void attemptSendPendingMessages(String message) {
        if (mSocket != null) {
            mSocket.emit("sPendingMessages", message);
        }
    }

    public void attemptSendMessages(String message) {
        if (mSocket != null) {
            mSocket.emit("sMessage", message);
        }
    }


    private Emitter.Listener onLoginReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            attemptSendRoom("");
        }
    };

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
            //attemptSendPendingMessages("");
        }
    };

    private Emitter.Listener onPendingMessagesReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //attemptSendMessages("");
            int i = 10;//mSocket.emit("sMessages", "");
        }
    };

    private Emitter.Listener onMessagesReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

    private Emitter.Listener onErrorReturn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            int i = 10;
        }
    };


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

