package com.namoadigital.prj001.util;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by neomatrix on 09/01/17.
 */

public class ToolBox_Con {

    public static String connWebService(String urlEnd, String params) {
        StringBuilder sb = new StringBuilder();

        URL url;
        HttpsURLConnection conn = null;

        try {
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

            sb.append(readStreamAux(conn.getInputStream()));

        } catch (Exception e) {

            sb.append("Error: " + e.toString());

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
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
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
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

    public static String getPreference_User_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_CODE,
                ""
        );
    }

    // Hugo
    public static String getPreference_User_Code_Nick(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_USER_CODE_NICK,
                ""
        );
    }

    public static long getPreference_Customer_Code(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getLong(
                Constant.LOGIN_CUSTOMER_CODE,
                -1L
        );
    }

    public static String getPreference_Customer_nls_date_format(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.NLS_DATE_FORMAT,
                "dd/MM/yyyy"
        );
    }

    // Hugo
    public static String getPreference_Customer_Code_NAME(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                Constant.LOGIN_CUSTOMER_CODE_NAME,
                ""
        );
    }

    public static void setPreference_User_Code(Context context, String user_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE,
                user_code
        ).apply();
    }

    // Hugo Agora

    public static void setPreference_User_Code_Nick(Context context, String user_code_nick) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE_NICK,
                user_code_nick
        ).apply();
    }

    public static void setPreference_Customer_Code(Context context, long customer_code) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putLong(
                Constant.LOGIN_CUSTOMER_CODE,
                customer_code
        ).apply();
    }

    public static void setPreference_Customer_Code_Name(Context context, String customer_code_name) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_CUSTOMER_CODE_NAME,
                customer_code_name
        ).apply();
    }

    public static void setPreference_Customer_nls_date_format(Context context, String nls_date_format) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.NLS_DATE_FORMAT,
                nls_date_format
        ).apply();
    }


    public static void zerarPreferences(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE,
                String.valueOf("")
        ).apply();
        //
        sharedPreferences.edit().putString(
                Constant.LOGIN_USER_CODE_NICK,
                String.valueOf("")
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
                Constant.NLS_DATE_FORMAT,
                ""
        ).apply();

    }


}
