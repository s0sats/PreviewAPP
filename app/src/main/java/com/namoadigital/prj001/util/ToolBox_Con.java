package com.namoadigital.prj001.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.model.DaoObjReturn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by neomatrix on 09/01/17.
 */

public class ToolBox_Con {

    private static final String CLASS_NAME = "com.namoadigital.prj001.util.ToolBox_Con";

    /**
     * LUCHE - 07/06/2019
     *
     * Assinatura original do metodo.
     * @param urlEnd - Url
     * @param params - Json ja pronto para envio
     * @return
     * @throws Exception
     */
    public static String connWebService(String urlEnd, String params) throws Exception {
        return connWebService(urlEnd, params, 60000);
    }

    /**
     * LUCHE - 07/06/2019
     *
     * Nova assinatura do metodo connWebService, que recebe como ultimo parametro o timeout a ser
     * considerado.
     *
     * Metodo criado para possibilitar timeout diferenciado em algumas chamadas de WS mais pesadas,
     * como o Sincronismo
     *
     * @param urlEnd - Url
     * @param params - Json ja pronto para envio
     * @param timeout - Tempo  de Timeout
     * @return - Json retornado pelo server
     * @throws Exception
     */
    public static String connWebService(String urlEnd, String params,Integer timeout) throws Exception {
        StringBuilder sb = new StringBuilder();
        URL url;
        HttpsURLConnection conn = null;
        timeout = timeout != null ? timeout : 60000 ;

        url = new URL(urlEnd);

        SSLContext contextS = SSLContext.getInstance("TLS");
        TrustManager[] tmlist = {new MyTrustManager()};

        contextS.init(null, tmlist, null);
        conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(contextS.getSocketFactory());
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

        conn.setReadTimeout(timeout);
        conn.setConnectTimeout(timeout);

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        writer.write(params.toCharArray());
        writer.flush();
        writer.close();
        os.close();

        int httpStatus = conn.getResponseCode();
        if (httpStatus == HttpURLConnection.HTTP_OK) {
            sb.append(readStreamAux(conn.getInputStream()));
        } else {
            throw new Exception(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR);
            //sb.append("Error: " + "HTTP_STATUS " + httpStatus);
        }

        if (conn != null) {
            conn.disconnect();
        }

        return sb.toString();
    }
    //Teste de chama GET "https://chat.namoadigital.com/messageDist?msg_prefix=201712&msg_code=2267"
    public static String connHttpGet(String urlEnd, String params) throws Exception {
        StringBuilder sb = new StringBuilder();

        URL url;
        HttpsURLConnection conn = null;

        url = new URL(urlEnd);

        SSLContext contextS = SSLContext.getInstance("TLS");
        TrustManager[] tmlist = {new MyTrustManager()};

        contextS.init(null, tmlist, null);
        conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(contextS.getSocketFactory());
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

        conn.setReadTimeout(60000);
        conn.setConnectTimeout(60000);

        conn.setRequestMethod("GET");

        int httpStatus = conn.getResponseCode();
        if (httpStatus == HttpURLConnection.HTTP_OK) {
            sb.append(readStreamAux(conn.getInputStream()));
        } else {
            throw new Exception(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR);
            //sb.append("Error: " + "HTTP_STATUS " + httpStatus);
        }

        if (conn != null) {
            conn.disconnect();
        }

        return sb.toString();
    }

    private static String readStreamAux(InputStream inputStream) {
        Reader reader = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {

            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;

            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    ToolBox_Inf.registerException(CLASS_NAME, e);
                    e.printStackTrace();
                }
            }
        }

        return writer.toString();
    }

    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

            try {

                chain[0].checkValidity();
                chain[0].getIssuerUniqueID();
                chain[0].getSubjectDN();

            } catch (Exception e) {

                throw new CertificateException("Certificate not valid or trusted.");

            }

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

    /**
     * LUCHE - 10/01/2020
     *
     * Metodo que remove do arquivo de preferencia a chave passada.
     *
     * @param context
     * @param preferenceKey - Chave da preferencia
     */
    public static void removePreference(Context context,String preferenceKey){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        //
        sharedPreferences.edit().remove(
                preferenceKey
        ).apply();
    }

    //region PKG_CLEAN
    public static void setPreference_PKG_CLEAN(Context context, String PKG_CLEAN) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.PKG_CLEAN_KEY,
                PKG_CLEAN
        ).apply();
    }

    public static String getPreference_PKG_CLEAN(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.PKG_CLEAN_KEY,
                ""
        );
    }
    //endregion

    //region Act054
    //
    public static void setBooleanPreference(Context context, String pref_key, boolean pref_value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(
                pref_key,
                pref_value
        ).apply();
    }

    public static boolean getBooleanPreferencesByKey(Context context, String pref_key, boolean default_value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getBoolean(
                pref_key,
                default_value
        );
    }
    //endregion
    //region String
    //
    public static void setStringPreference(Context context, String pref_key, String pref_value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(
                pref_key,
                pref_value
        ).apply();
    }

    public static String getStringPreferencesByKey(Context context, String pref_key, String default_value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                pref_key,
                default_value
        );
    }
    //endregion


    //region Long
    //
    public static void setLongPreference(Context context, String pref_key, long pref_value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                pref_key,
                pref_value
        ).apply();
    }
    public static long getLongPreferencesByKey(Context context, String pref_key, long default_value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return  sharedPreferences.getLong(
                pref_key,
                default_value
        );
    }
    //endregion

    /**
     * LUCHE - 10/01/2020
     */
    public static boolean getPreference_HideSerialInfo(Context context) {
        //
        return getBooleanPreferencesByKey(
                context,
                ConstantBaseApp.PREFERENCE_HIDE_SERIAL_INFO,
                false
        );
    }

    public static void setPreference_HideSerialInfo(Context context, boolean isChecked) {
        setBooleanPreference(context,ConstantBaseApp.PREFERENCE_HIDE_SERIAL_INFO,isChecked);
    }

    //region PKG_APK_TYPE
    public static void setPreference_PKG_APP_TYPE(Context context, String PKG_APP_TYPE) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.PKG_APP_TYPE_KEY,
                PKG_APP_TYPE
        ).apply();
    }

    public static String getPreference_PKG_APP_TYPE(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.PKG_APP_TYPE_KEY,
                ""
        );
    }
    //endregion

    //region CLEAN_TOKEN_FILE_KEY

    /**
     * Preferencia para identificar se os arquivos de token devem ou não ser apagados
     * após atualização de software com troca de versão de banco de dados e dados pendentes
     * de envio.
     * @param context
     * @param CLEAN_TOKEN_FILE_KEY
     */
    public static void setPreference_CleanTokenFiles(Context context, int CLEAN_TOKEN_FILE_KEY) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putInt(
                Constant.CLEAN_TOKEN_FILE_KEY,
                CLEAN_TOKEN_FILE_KEY
        ).apply();
    }

    public static int getPreference_CleanTokenFiles(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getInt(
                Constant.CLEAN_TOKEN_FILE_KEY,
                -1
        );
    }
    //endregion

    //region CLEAN_TOKEN_FILE_KEY

    /**
     * LUCHE - 13/05/2019
     * Preferencia para identificar se as imagens não enviadas devem ser copiadas para o diretorio
     * de "unsent"após atualização de software com troca de versão de banco de dados ou troca de usuario.
     * @param context
     * @param unsentImg
     */
    public static void setPreference_BkpUnsentImg(Context context, boolean unsentImg) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putBoolean(
                Constant.BACKUP_UNSENT_IMG_KEY,
                unsentImg
        ).apply();
    }

    public static boolean getPreference_BkpUnsentImg(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getBoolean(
                Constant.BACKUP_UNSENT_IMG_KEY,
                false
        );
    }
    //endregion

    //region User_Code
    public static void setPreference_User_Code(Context context, String user_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE,
                user_code
        ).apply();
    }

    public static String getPreference_User_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_CODE,
                ""
        );
    }
    //endregion

    //region User_Pwd
    public static void setPreference_User_Pwd(Context context, String pwd) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_PWD,
                pwd
        ).apply();
    }

    //
    public static String getPreference_User_Pwd(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_PWD,
                ""
        );
    }
    //endregion

    //region User_Email
    public static void setPreference_User_Email(Context context, String email) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_EMAIL,
                email
        ).apply();
    }

    public static String getPreference_User_Email(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_EMAIL,
                ""
        );
    }
    //endregion

    //region Status_Login
    public static void setPreference_Status_Login(Context context, String status) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_STATUS,
                status
        ).apply();
    }

    public static String getPreference_Status_Login(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_STATUS,
                ""
        );
    }
    //endregion

    //region User_NFC
    public static void setPreference_User_NFC(Context context, String nfc) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_NFC,
                nfc
        ).apply();
    }

    public static String getPreference_User_NFC(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_NFC,
                ""
        );
    }
    //endregion

    //region Last_User_Logged
    public static void setPreference_Last_User_Logged(Context context, String user_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_LAST_USER_CODE_LOGGED,
                user_code
        ).apply();
    }

    public static String getPreference_Last_User_Logged(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_LAST_USER_CODE_LOGGED,
                "-1"
        );
    }
    //endregion

    //region Customer_Code
    public static void setPreference_Customer_Code(Context context, long customer_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.LOGIN_CUSTOMER_CODE,
                customer_code
        ).apply();
    }

    public static long getPreference_Customer_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.LOGIN_CUSTOMER_CODE,
                -1L
        );
    }

    //endregion

    //region Customer_Code_Name
    public static void setPreference_Customer_Code_Name(Context context, String customer_code_name) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_CUSTOMER_CODE_NAME,
                customer_code_name
        ).apply();
    }

    public static String getPreference_Customer_Code_NAME(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_CUSTOMER_CODE_NAME,
                ""
        );
    }

    //endregion

    //region Tracking
    public static void setPreference_Customer_Uses_Tracking(Context context, int tracking) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putInt(
                Constant.LOGIN_CUSTOMER_USES_TRACKING,
                tracking
        ).apply();
    }

    public static int getPreference_Customer_Uses_Tracking(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getInt(
                Constant.LOGIN_CUSTOMER_USES_TRACKING,
                -1
        );
    }

    //endregion

    //region CUSTOMER_TIMEZONE
    public static void setPreference_Customer_TMZ(Context context, String timezone) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_CUSTOMER_TMZ,
                timezone
        ).apply();
    }

    public static String getPreference_Customer_TMZ(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_CUSTOMER_TMZ,
                ""
        );
    }
    //endregion

    //region Chat_Msg_Code
    public static void setPreference_Chat_Msg_Code(Context context, long chat_msg_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.CHAT_PREFERENCE_MSG_CODE,
                chat_msg_code
        ).apply();
    }

    public static long getPreference_Chat_Msg_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.CHAT_PREFERENCE_MSG_CODE,
                100L
        );
    }
    //endregion

    //region Chat_Msg_Token
    public static void setPreference_Chat_Msg_Token(Context context, long chat_msg_token) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.CHAT_PREFERENCE_MSG_TOKEN,
                chat_msg_token
        ).apply();
    }

    public static long getPreference_Chat_Msg_Token(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.CHAT_PREFERENCE_MSG_TOKEN,
                1L
        );
    }
    //endregion


    //region Chat_Msg_Prefix
    public static void setPreference_Chat_Msg_Prefix(Context context, String chat_msg_prefix) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.CHAT_PREFERENCE_MSG_PREFIX,
                chat_msg_prefix
        ).apply();
    }

    public static String getPreference_Chat_Msg_Prefix(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.CHAT_PREFERENCE_MSG_PREFIX,
                ""
        );
    }
    //endregion


    //region Translate_Code
    public static void setPreference_Translate_Code(Context context, String translate_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.USER_CUSTOMER_TRANSLATE_CODE,
                translate_code
        ).apply();
    }

    public static String getPreference_Translate_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.USER_CUSTOMER_TRANSLATE_CODE,
                ""
        );
    }
    //endregion

    //region User_Code_Nick
    public static void setPreference_User_Code_Nick(Context context, String user_code_nick) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE_NICK,
                user_code_nick
        ).apply();
    }

    public static String getPreference_User_Code_Nick(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_CODE_NICK,
                ""
        );
    }

    //endregion

    //region Site_Code
    public static void setPreference_Site_Code(Context context, String site_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_SITE_CODE,
                site_code
        ).apply();
    }

    public static String getPreference_Site_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_SITE_CODE,
                "-1"
        );
    }
    //endregion

    //region Zone_Code
    public static void setPreference_Zone_Code(Context context, int zone_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putInt(
                Constant.LOGIN_ZONE_CODE,
                zone_code
        ).apply();
    }

    public static int getPreference_Zone_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getInt(
                Constant.LOGIN_ZONE_CODE,
                -1
        );
    }
    //endregion

    //region Operation_Code
    public static void setPreference_Operation_Code(Context context, long operation_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.LOGIN_OPERATION_CODE,
                operation_code
        ).apply();
    }

    public static long getPreference_Operation_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.LOGIN_OPERATION_CODE,
                -1L
        );
    }

    //endregion

    //region Session_app
    public static void setPreference_Session_App(Context context, String session_app) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.SESSION_APP,
                session_app
        ).apply();
    }

    public static String getPreference_Session_App(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.SESSION_APP,
                ""
        );
    }

    //endregion

    //region google_id
    public static void setPreference_Google_ID(Context context, String google_id) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.GOOGLE_ID,
                google_id
        ).apply();
    }

    public static String getPreference_Google_ID(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.GOOGLE_ID,
                ""
        );
    }

    public static void setPreference_Google_ID_OK(Context context, String sOk) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.GOOGLE_ID_OK,
                sOk
        ).apply();
    }

    public static String getPreference_Google_ID_OK(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.GOOGLE_ID_OK,
                ""
        );
    }

    public static void setPreference_Google_ID_DT(Context context, long sDT) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.GOOGLE_ID_DT,
                sDT
        ).apply();
    }

    public static Long getPreference_Google_ID_DT(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.GOOGLE_ID_DT,
                0L
        );
    }

    public static void setPreference_AL_DT(Context context, long sDT) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.AL_DT,
                sDT
        ).apply();
    }

    public static Long getPreference_AL_DT(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.AL_DT,
                0L
        );
    }

    public static void setPreference_SYNC_REQUIRED(Context context, String sRequired) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.SYNC_REQUIRED,
                sRequired
        ).apply();
    }

    public static String getPreference_SYNC_REQUIRED(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.SYNC_REQUIRED,
                ""
        );
    }

    public static void setPreference_MessageClear(Context context, String sMessageClear) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.MESSAGE_CLEAR,
                sMessageClear
        ).apply();
    }

    public static String getPreference_MessageClear(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.MESSAGE_CLEAR,
                ""
        );
    }


    //endregion

    //region Customer_Code_TMP
    public static void setPreference_Customer_Code_TMP(Context context, long customer_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.LOGIN_CUSTOMER_CODE_TMP,
                customer_code
        ).apply();
    }

    public static Long getPreference_Customer_Code_TMP(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.LOGIN_CUSTOMER_CODE_TMP,
                -1L
        );
    }
    //endregion

    //region User_NFC_TMP
    public static void setPreference_User_NFC_TMP(Context context, String nfc) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_NFC_TMP,
                nfc
        ).apply();
    }

    public static String getPreference_User_NFC_TMP(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_NFC_TMP,
                ""
        );
    }
    //endregion

    //region Translate_Code_TMP
    public static void setPreference_Translate_Code_TMP(Context context, String translate_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.USER_CUSTOMER_TRANSLATE_CODE_TMP,
                translate_code
        ).apply();
    }

    public static String getPreference_Translate_Code_TMP(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.USER_CUSTOMER_TRANSLATE_CODE_TMP,
                ""
        );
    }
    //endregion

    //region PHONE_UNIQUE_ID
    public static void setPreference_PHONE_UNIQUE_ID(Context context, String phone_uuid_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.PHONE_UNIQUE_ID,
                phone_uuid_code
        ).apply();
    }

    public static String getPreference_PHONE_UNIQUE_ID(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.PHONE_UNIQUE_ID,
                ""
        );
    }
    //endregion

    //region Nls_date_Format
    public static void setPreference_Customer_nls_date_format(Context context, String nls_date_format) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.NLS_DATE_FORMAT,
                nls_date_format
        ).apply();

        Constant.DATEFORMATDT = nls_date_format.toLowerCase().replace("m", "M").replace("R", "y");
    }

    public static String getPreference_Customer_nls_date_format(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.NLS_DATE_FORMAT,
                "dd/MM/yyyy"
        );
    }

    public static void setPreference_Service(Context context, String service) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                "SERVICE",
                service
        ).apply();
    }


    public static String getPreference_Service(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                "SERVICE",
                "NO_SERVICE"
        );
    }

    //endregion

    //region Approval Type
    public static void setApproval_Type(Context context, String approval_type) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.APPROVAL_TYPE,
                approval_type
        ).apply();
    }

    public static String getApproval_Type(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.APPROVAL_TYPE,
                ""
        );
    }

    //endregion


    public static void cleanPreferences(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE,
                String.valueOf("")
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_PWD,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_EMAIL,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_STATUS,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_NFC,
                ""
        ).apply();
        //
        sharedPreferences.edit().putLong(
                Constant.LOGIN_CUSTOMER_CODE,
                -1
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_CUSTOMER_CODE_NAME,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.USER_CUSTOMER_TRANSLATE_CODE,
                ""
        ).apply();
        //
        sharedPreferences.edit().putInt(
                Constant.LOGIN_CUSTOMER_USES_TRACKING,
                -1
        );
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE_NICK,
                String.valueOf("")
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_SITE_CODE,
                "-1"
        ).apply();
        //
        sharedPreferences.edit().putInt(
                Constant.LOGIN_ZONE_CODE,
                -1
        ).apply();
        //
        sharedPreferences.edit().putLong(
                Constant.LOGIN_OPERATION_CODE,
                -1
        ).apply();
        //
        //
        sharedPreferences.edit().putString(
                Constant.SESSION_APP,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_CUSTOMER_CODE_TMP,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_NFC_TMP,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.USER_CUSTOMER_TRANSLATE_CODE_TMP,
                ""
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.NLS_DATE_FORMAT,
                ""
        ).apply();
        //
        sharedPreferences.edit().putBoolean(
                Constant.PREFERENCES_OFFLINE_MODE,
                false
        ).apply();
        //
        sharedPreferences.edit().putBoolean(
                Constant.PREFERENCE_HIDE_SERIAL_INFO,
                false
        ).apply();
        //Adicionar reset das preferencias da act054?
        //
        sharedPreferences.edit().putString(
                "SERVICE",
                "NO_SERVICE"
        ).apply();
    }

    public static void resetCustomerSiteOperationPreferences(Context context) {
        ToolBox_Con.setPreference_Customer_Code(context, -1);
        ToolBox_Con.setPreference_Translate_Code(context, "");
        ToolBox_Con.setPreference_Site_Code(context, "-1");
        ToolBox_Con.setPreference_Zone_Code(context, -1);
        ToolBox_Con.setPreference_Operation_Code(context, -1);
        ToolBox_Con.setPreference_Status_Login(context, "");
        ToolBox_Con.setPreference_HideSerialInfo(context,false);
    }

    public static String customDBPath(long customer_code) {
        return (Constant.DB_PATH + "/C_" + customer_code + "_" + Constant.DB_VERSION_CUSTOM + ".db3");
    }

    public static boolean isOnline(Context context) {
        return isOnline(context,false);
    }

    /**
     * LUCHE - 31/07/2019
     *
     * Nova implementação do metodo isOnline que agora também validará se o
     * @param context
     * @param ignoreOfflineMode
     * @return
     */
    public static boolean isOnline(Context context, boolean ignoreOfflineMode) {
        if(!ignoreOfflineMode){
            if(ToolBox.getPreference_Offline_Mode(context)){
                return false;
            }
        }
        //
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        int netType = 0;
        if (netInfo != null) {
            netType = netInfo.getType();
            if (netInfo.isAvailable() && netInfo.isConnected()) {
                if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        }
        ToolBox_Inf.callPendencyNotification(context);
        return false;

    }

    public static String checkConStatus(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //
        if (wifi != null && wifi.isAvailable() && wifi.isConnected()) {
            return "WIFI";
        }
        //
        if (mobile != null && mobile.isAvailable() && mobile.isConnected()) {
            return "MOBILE";
        }
        //
        return "NO_SERVICE";
    }

    public static boolean isHostAvailable() {
        return isHostAvailable(
                Constant.WS_HOST,
                Constant.WS_PORT,
                Constant.WS_TIMEOUT
        );
    }

    public static boolean isHostAvailable(final String host, final int port, final int timeout) {
        try (final Socket socket = new Socket()) {
            final InetAddress inetAddress = InetAddress.getByName(host);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);

            socket.connect(inetSocketAddress, timeout);

            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDBHelperName(String mDBName, int mDBVersion) {
        try {
            if (mDBName == null || mDBName.isEmpty()) {
                return "";
            }

            return mDBName.replace(".db3", "_") + String.valueOf(mDBVersion) + ".db3";

        } catch (Exception e) {
            return "";
        }
    }

    public static DaoObjReturn getSQLiteErrorCodeDescription(String errorMessage) {
        final String codeStr = "(code ";
        DaoObjReturn mErrorDao = new DaoObjReturn();
        //
        mErrorDao.setError(true);
        mErrorDao.setRawMessage(errorMessage);
        //
        if(errorMessage.contains(codeStr)){
            int maxErroCodeSize = 4;//Tamanho maximo do "result_code" do sqlite
            int codeIdxStart = errorMessage.indexOf(codeStr) + codeStr.length();
            int codeIdxEnd = 0;
            //Faz loop para identificar se a substring é um "numero"
            //pois existem "result_code" de 1 a 4 "digitos"
            for( ; maxErroCodeSize  > 0; maxErroCodeSize --){
                codeIdxEnd = codeIdxStart + maxErroCodeSize;
                try{
                    String s = errorMessage.substring(codeIdxStart, codeIdxEnd);
                    int i = Integer.parseInt(s);
                    break;
                }catch (Exception e){
                }
            }
            //
            if(codeIdxStart < errorMessage.length() && codeIdxEnd <= errorMessage.length()) {
                String sqliteErroCode = errorMessage.substring(codeIdxStart, codeIdxEnd);
                //
                mErrorDao.setCode(sqliteErroCode);
                mErrorDao.setDescription(sqliteErrorDescMapper(sqliteErroCode));
            }
        }
        //
        return mErrorDao;
    }

    /**
     * Metodo que retorna o "result code desc" da sqlite exception
     * fonte: https://www.sqlite.org/rescode.html
     * @param code
     * @return
     */
    public static String sqliteErrorDescMapper(String code){
        HMAux sqliteMap = new HMAux();
        //
        sqliteMap.put("0","SQLITE_OK");
        sqliteMap.put("1","SQLITE_ERROR");
        sqliteMap.put("2","SQLITE_INTERNAL");
        sqliteMap.put("3","SQLITE_PERM");
        sqliteMap.put("4","SQLITE_ABORT");
        sqliteMap.put("5","SQLITE_BUSY");
        sqliteMap.put("6","SQLITE_LOCKED");
        sqliteMap.put("7","SQLITE_NOMEM");
        sqliteMap.put("8","SQLITE_READONLY");
        sqliteMap.put("9","SQLITE_INTERRUPT");
        sqliteMap.put("10","SQLITE_IOERR");
        sqliteMap.put("11","SQLITE_CORRUPT");
        sqliteMap.put("12","SQLITE_NOTFOUND");
        sqliteMap.put("13","SQLITE_FULL");
        sqliteMap.put("14","SQLITE_CANTOPEN");
        sqliteMap.put("15","SQLITE_PROTOCOL");
        sqliteMap.put("16","SQLITE_EMPTY");
        sqliteMap.put("17","SQLITE_SCHEMA");
        sqliteMap.put("18","SQLITE_TOOBIG");
        sqliteMap.put("19","SQLITE_CONSTRAINT");
        sqliteMap.put("20","SQLITE_MISMATCH");
        sqliteMap.put("21","SQLITE_MISUSE");
        sqliteMap.put("22","SQLITE_NOLFS");
        sqliteMap.put("23","SQLITE_AUTH");
        sqliteMap.put("24","SQLITE_FORMAT");
        sqliteMap.put("25","SQLITE_RANGE");
        sqliteMap.put("26","SQLITE_NOTADB");
        sqliteMap.put("27","SQLITE_NOTICE");
        sqliteMap.put("28","SQLITE_WARNING");
        sqliteMap.put("100","SQLITE_ROW");
        sqliteMap.put("101","SQLITE_DONE");
        sqliteMap.put("256","SQLITE_OK_LOAD_PERMANENTLY");
        sqliteMap.put("257","SQLITE_ERROR_MISSING_COLLSEQ");
        sqliteMap.put("261","SQLITE_BUSY_RECOVERY");
        sqliteMap.put("262","SQLITE_LOCKED_SHAREDCACHE");
        sqliteMap.put("264","SQLITE_READONLY_RECOVERY");
        sqliteMap.put("266","SQLITE_IOERR_READ");
        sqliteMap.put("267","SQLITE_CORRUPT_VTAB");
        sqliteMap.put("270","SQLITE_CANTOPEN_NOTEMPDIR");
        sqliteMap.put("275","SQLITE_CONSTRAINT_CHECK");
        sqliteMap.put("283","SQLITE_NOTICE_RECOVER_WAL");
        sqliteMap.put("284","SQLITE_WARNING_AUTOINDEX");
        sqliteMap.put("513","SQLITE_ERROR_RETRY");
        sqliteMap.put("516","SQLITE_ABORT_ROLLBACK");
        sqliteMap.put("517","SQLITE_BUSY_SNAPSHOT");
        sqliteMap.put("520","SQLITE_READONLY_CANTLOCK");
        sqliteMap.put("522","SQLITE_IOERR_SHORT_READ");
        sqliteMap.put("523","SQLITE_CORRUPT_SEQUENCE");
        sqliteMap.put("526","SQLITE_CANTOPEN_ISDIR");
        sqliteMap.put("531","SQLITE_CONSTRAINT_COMMITHOOK");
        sqliteMap.put("539","SQLITE_NOTICE_RECOVER_ROLLBACK");
        sqliteMap.put("769","SQLITE_ERROR_SNAPSHOT");
        sqliteMap.put("776","SQLITE_READONLY_ROLLBACK");
        sqliteMap.put("778","SQLITE_IOERR_WRITE");
        sqliteMap.put("782","SQLITE_CANTOPEN_FULLPATH");
        sqliteMap.put("787","SQLITE_CONSTRAINT_FOREIGNKEY");
        sqliteMap.put("1032","SQLITE_READONLY_DBMOVED");
        sqliteMap.put("1034","SQLITE_IOERR_FSYNC");
        sqliteMap.put("1038","SQLITE_CANTOPEN_CONVPATH");
        sqliteMap.put("1043","SQLITE_CONSTRAINT_FUNCTION");
        sqliteMap.put("1288","SQLITE_READONLY_CANTINIT");
        sqliteMap.put("1290","SQLITE_IOERR_DIR_FSYNC");
        sqliteMap.put("1294","SQLITE_CANTOPEN_DIRTYWAL");
        sqliteMap.put("1299","SQLITE_CONSTRAINT_NOTNULL");
        sqliteMap.put("1544","SQLITE_READONLY_DIRECTORY");
        sqliteMap.put("1546","SQLITE_IOERR_TRUNCATE");
        sqliteMap.put("1555","SQLITE_CONSTRAINT_PRIMARYKEY");
        sqliteMap.put("1802","SQLITE_IOERR_FSTAT");
        sqliteMap.put("1811","SQLITE_CONSTRAINT_TRIGGER");
        sqliteMap.put("2058","SQLITE_IOERR_UNLOCK");
        sqliteMap.put("2067","SQLITE_CONSTRAINT_UNIQUE");
        sqliteMap.put("2314","SQLITE_IOERR_RDLOCK");
        sqliteMap.put("2323","SQLITE_CONSTRAINT_VTAB");
        sqliteMap.put("2570","SQLITE_IOERR_DELETE");
        sqliteMap.put("2579","SQLITE_CONSTRAINT_ROWID");
        sqliteMap.put("2826","SQLITE_IOERR_BLOCKED");
        sqliteMap.put("3082","SQLITE_IOERR_NOMEM");
        sqliteMap.put("3338","SQLITE_IOERR_ACCESS");
        sqliteMap.put("3594","SQLITE_IOERR_CHECKRESERVEDLOCK");
        sqliteMap.put("3850","SQLITE_IOERR_LOCK");
        sqliteMap.put("4106","SQLITE_IOERR_CLOSE");
        sqliteMap.put("4362","SQLITE_IOERR_DIR_CLOSE");
        sqliteMap.put("4618","SQLITE_IOERR_SHMOPEN");
        sqliteMap.put("4874","SQLITE_IOERR_SHMSIZE");
        sqliteMap.put("5130","SQLITE_IOERR_SHMLOCK");
        sqliteMap.put("5386","SQLITE_IOERR_SHMMAP");
        sqliteMap.put("5642","SQLITE_IOERR_SEEK");
        sqliteMap.put("5898","SQLITE_IOERR_DELETE_NOENT");
        sqliteMap.put("6154","SQLITE_IOERR_MMAP");
        sqliteMap.put("6410","SQLITE_IOERR_GETTEMPPATH");
        sqliteMap.put("6666","SQLITE_IOERR_CONVPATH");
        //
        String s = "UNKNOW_ERROR";
        //
        return sqliteMap.containsKey(code) ? sqliteMap.get(code) : s;
    }


    public static boolean hasGPSResourceActive(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (lm.isLocationEnabled()) {
                return true;
            }
        }else {
            String provider = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null && provider.length() > 0) {
                return true;
            }
        }
        return false;
    }

}
