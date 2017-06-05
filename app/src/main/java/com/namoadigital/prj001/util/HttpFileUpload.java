package com.namoadigital.prj001.util;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by neomatrix on 20/02/17.
 */

public class HttpFileUpload {

    URL connectURL;
    String responseString;
    String Json;
    byte[] dataToServer;
    FileInputStream fileInputStream = null;

    HttpFileUpload(String urlString, String vJson) {
        try {
            connectURL = new URL(urlString);
            Json = vJson;
        } catch (Exception ex) {
            ToolBox_Inf.registerException(getClass().getName(),ex);
            Log.i("HttpFileUpload", "URL Malformatted");
        }
    }

    String Send_Now(FileInputStream fStream, String sFile) {
        fileInputStream = fStream;
        return Sending(sFile);
    }

    String Sending(String sFile) {
        String sResults = "";

        //String  [] parts = sFile.split("/");

        String iFileName = sFile; //[parts.length-1];
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "fSnd";
        try {
            Log.e(Tag, "Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

            // Time Out configuration
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"json\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(Json);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"file_post\";filename=\"" + iFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag, "Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            Log.e(Tag, "File Sent, Response: " + String.valueOf(conn.getResponseCode()));

            InputStream is = conn.getInputStream();

            // retrieve the response from server
            int ch;

            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();

            sResults = s;

            Log.i("Response", s);
            dos.close();
        } catch (MalformedURLException ex) {
            ToolBox_Inf.registerException(getClass().getName(),ex);
            sResults = "URL error: " + ex.getMessage();
        } catch (IOException ioe) {
            ToolBox_Inf.registerException(getClass().getName(),ioe);
            sResults = "IO error: " + ioe.getMessage();
        } catch (Exception exg){
            ToolBox_Inf.registerException(getClass().getName(),exg);
            sResults = "Error: " + exg.getMessage();
        }

        return sResults;
    }
}
