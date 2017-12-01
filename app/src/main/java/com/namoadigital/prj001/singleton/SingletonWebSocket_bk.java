package com.namoadigital.prj001.singleton;

import android.content.Context;

import com.namoadigital.prj001.util.ToolBox_Con;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by neomatrix on 27/11/17.
 */

public class SingletonWebSocket_bk {
    private static volatile SingletonWebSocket_bk sSoleInstance;

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

    private SingletonWebSocket_bk() {
        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public void initConnection() {

        IO.Options options = new IO.Options();
        options.timeout = 1000;

        try {
            mSocket = IO.socket("https://chat.namoadigital.com", options);

            mSocket.on("cLogin", onLoginReturn);
            mSocket.on("error", onErrorReturn);
            mSocket.on("cRoom", onRoomReturn);
            mSocket.on("cPendingMessages", onPendingMessagesReturn);
            mSocket.on("cMessages", onMessagesReturn);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mSocketRunning = true;
                }
            });

            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    reconnect();
                }
            });

            mSocket.connect();

            attemptSendLogin();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        disconnect();
        //
        if (mSocketReconnect) {
            initConnection();
        }
    }

    public void disconnect() {
        mSocketRunning = false;

        if (mSocket != null) {
            mSocket.off();
            mSocket.disconnect();
            mSocket = null;
        }
    }

    public void attemptSendLogin() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_code", ToolBox_Con.getPreference_User_Code(context));
            jsonObject.put("customer_code", String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
            jsonObject.put("session_id", ToolBox_Con.getPreference_Session_App(context));
            jsonObject.put("session_type", "APP");
            jsonObject.put("translate_code", ToolBox_Con.getPreference_Translate_Code(context));

            if (mSocket != null) {
                mSocket.emit("sLogin", jsonObject.toString());
            }
        } catch (Exception e){
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
            attemptSendPendingMessages("");
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
    public static SingletonWebSocket_bk getInstance(Context context) {
        //Double check locking pattern
        if (sSoleInstance == null) { //Check for the first time

            synchronized (SingletonWebSocket_bk.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sSoleInstance == null) {
                    sSoleInstance = new SingletonWebSocket_bk();
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

