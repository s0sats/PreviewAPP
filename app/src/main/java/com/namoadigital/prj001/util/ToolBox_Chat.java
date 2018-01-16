package com.namoadigital.prj001.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.net.URLEncoder;

/**
 * Created by neomatrix on 1/15/18.
 */

public class ToolBox_Chat {

    /**
     * @param urlEnd Constants http.....
     * @param params Json
     * @return resposta em texto JSON servidor
     */
    public static String comunicacao(String urlEnd, String params) {
        StringBuilder sb = new StringBuilder();

        URL url;
        HttpURLConnection conn = null;

        try {

            url = new URL(urlEnd);
            conn = (HttpURLConnection) url.openConnection();
            //
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //
            //conn.setConnectTimeout(60000);
            //conn.setReadTimeout(60000);
            //
            StringBuilder parametrosFormatados = new StringBuilder();
            parametrosFormatados.append(URLEncoder.encode("json", "UTF-8"));
            parametrosFormatados.append("=");
            parametrosFormatados.append(URLEncoder.encode(params, "UTF-8"));
            //
            // Envio de Parametros
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8")
            );
            writer.write(parametrosFormatados.toString());
            writer.flush();
            writer.close();
            //os.close();
            //
            // Ler as informacoes enviados pelo Servidor
            sb.append(readStream(conn.getInputStream()));
        } catch (Exception e) {
            sb.append(e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return sb.toString();
    }

    private static String readStream(InputStream inputStream) {
        Reader reader = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            reader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8")
            );

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
        //
        return writer.toString();
    }

    public static void gravarLog(String mensagem, String caminho, String arquivo) {
        File diretorio = new File(caminho);
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        //
        FileWriter f;
        //
        try {
            f = new FileWriter(
                    caminho + "/" + arquivo,
                    true
            );
            //
            f.write(mensagem);
            f.flush();
            f.close();
        } catch (Exception e) {
            Log.d("ERRO", e.toString());
        }
    }

    public static String LerLog(String caminho, String arquivo) {
        StringBuilder conteudo = new StringBuilder();
        //
        File diretorio = new File(caminho);
        if (!diretorio.exists()) {
            return "erro";
        }
        //
        File arquivoLocal = new File(caminho + "/" + arquivo);
        if (!arquivoLocal.exists()) {
            return "erro";
        }
        //
        try {
            BufferedReader input = new BufferedReader(new FileReader(arquivoLocal));
            String linha = null;
            //
            while ((linha = input.readLine()) != null) {
                conteudo.append(linha);
            }
            //
            input.close();

        } catch (Exception e) {
            Log.d("ERRO", e.toString());
        }
        //
        return conteudo.toString();
    }

    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }
}
