package com.namoadigital.prj001.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

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

    public static String connWebService(String urlEnd, String params) throws Exception {
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


    //region Chat_Msg_Prefix
    public static void setPreference_Chat_Msg_Prefix(Context context, String chat_msg_prefix ) {
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

    //region PHONE_UUID_CODE
    public static void setPreference_PHONE_UUID_CODE(Context context, String phone_uuid_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.PHONE_UUID_CODE,
                phone_uuid_code
        ).apply();
    }

    public static String getPreference_PHONE_UUID_CODE(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.PHONE_UUID_CODE,
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

    }

    public static String customDBPath(long customer_code) {
        return (Constant.DB_PATH + "/C_" + customer_code + "_" + Constant.DB_VERSION_CUSTOM + ".db3");
    }

    public static boolean isOnline(Context context) {
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
        return false;

    }

    public static String checkConStatus(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //
        if (wifi.isAvailable() && wifi.isConnected()) {
            return "WIFI";
        }
        //
        if (mobile.isAvailable() && mobile.isConnected()) {
            return "MOBILE";
        }
        //
        return "NO_SERVICE";
    }

}
