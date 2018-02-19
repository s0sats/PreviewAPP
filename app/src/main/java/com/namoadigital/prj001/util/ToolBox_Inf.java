package com.namoadigital.prj001.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_ProfileDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.Ev_User_Customer_ParameterDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.fcm.WS_Notification_Sync;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_Obj;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.EV_Profile;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.Ev_User_Customer_Parameter;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.receiver.NotificationReceiver;
import com.namoadigital.prj001.receiver.WBR_AL_Full;
import com.namoadigital.prj001.receiver.WBR_AL_Quarter;
import com.namoadigital.prj001.receiver.WBR_Cleanning;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_UpdateSoftware;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.receiver.WBR_Upload_Support;
import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_020;
import com.namoadigital.prj001.sql.CH_Message_Sql_022;
import com.namoadigital.prj001.sql.CH_Message_Sql_023;
import com.namoadigital.prj001.sql.CH_Room_Sql_004;
import com.namoadigital.prj001.sql.CH_Room_Sql_007;
import com.namoadigital.prj001.sql.CH_Room_Sql_008;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Sql_002;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Trans_Sql_002;
import com.namoadigital.prj001.sql.EV_Profile_Sql_001;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_006;
import com.namoadigital.prj001.sql.EV_User_Sql_001;
import com.namoadigital.prj001.sql.Ev_User_Customer_Parameter_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_010;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_013;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_014;
import com.namoadigital.prj001.sql.MD_Operation_Sql_002;
import com.namoadigital.prj001.sql.MD_Site_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_014;
import com.namoadigital.prj001.sql.Sql_Chat_Notification_001;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_003;
import com.namoadigital.prj001.ui.act001.Act001_Main;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_IMEI;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_VERSION_LBL;

/**
 * Created by neomatrix on 09/01/17.
 */

public class ToolBox_Inf {

    private static final String CLASS_NAME = "com.namoadigital.prj001.util.ToolBox_Inf";
    
    private static final Map<Character, Character> ACCENT_MAP = initAccentMap();

    private static Map<Character,Character> initAccentMap() {
        Map<Character,Character> map = new HashMap<Character, Character>();
        //
        map.put('à','a');
        map.put('á','a');
        map.put('â','a');
        map.put('ã','a');
        map.put('ä','a');
        map.put('å','a');
        map.put('ç','c');
        map.put('č','c');
        map.put('ć','c');
        map.put('è','e');
        map.put('é','e');
        map.put('ê','e');
        map.put('ë','e');
        map.put('ì','i');
        map.put('í','i');
        map.put('î','i');
        map.put('ï','i');
        map.put('ñ','n');
        map.put('ò','o');
        map.put('ó','o');
        map.put('ô','o');
        map.put('õ','o');
        map.put('ö','o');
        map.put('ø','o');
        map.put('ß','s');
        map.put('§','s');
        map.put('ù','u');
        map.put('ú','u');
        map.put('û','u');
        map.put('ü','u');
        map.put('ÿ','y');
        //
        return map;
    } 
    
    public static void mkDirectory() {
        File dirDB = new File(Constant.DB_PATH);
        if (!dirDB.exists()) {
            dirDB.mkdir();
        }
        //
        File dirImg = new File(Constant.IMG_PATH);
        if (!dirImg.exists()) {
            dirImg.mkdir();
        }
        //
        File dirThu = new File(Constant.THU_PATH);
        if (!dirThu.exists()) {
            dirThu.mkdir();
        }
        //
        File dirZip = new File(Constant.ZIP_PATH);
        if (!dirZip.exists()) {
            dirZip.mkdir();
        }
        //
        File dirCache = new File(Constant.CACHE_PATH);
        if (!dirCache.exists()) {
            dirCache.mkdir();
        }
        //
        File dirCachePhoto = new File(Constant.CACHE_PATH_PHOTO);
        if (!dirCachePhoto.exists()) {
            dirCachePhoto.mkdir();
        }
        //
        File dirCachePDF = new File(Constant.CACHE_PDF);
        if (!dirCachePDF.exists()) {
            dirCachePDF.mkdir();
        }

        File dirSupport = new File(Constant.SUPPORT_PATH);
        if (!dirSupport.exists()) {
            dirSupport.mkdir();
        }

        File dirToken = new File(Constant.TOKEN_PATH);
        if (!dirToken.exists()) {
            dirToken.mkdir();
        }

        File dirChat = new File(Constant.CHAT_PATH);
        if (!dirChat.exists()) {
            dirChat.mkdir();
        }

        File dirChatImage = new File(Constant.CACHE_CHAT_PATH);
        if (!dirChatImage.exists()) {
            dirChatImage.mkdir();
        }

        File dirCamTest = new File(System.getenv("EXTERNAL_STORAGE") + "/camtest");
        if (!dirCamTest.exists()) {
            dirCamTest.mkdir();
        }

    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String CarrierInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        //
        return tm.getDeviceId();
    }

    public static String get_phone_uuid(Context context) throws IOException {
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        UUID androidId_UUID = UUID
                .nameUUIDFromBytes(androidId.getBytes("utf8"));

        String phone_uuid_code = ToolBox_Con.getPreference_PHONE_UUID_CODE(context);

        if (phone_uuid_code.trim().length() == 0) {
            phone_uuid_code = androidId_UUID.toString();

            ToolBox_Con.setPreference_PHONE_UUID_CODE(context, phone_uuid_code);

        }

        return phone_uuid_code;
    }

    public static String uniqueID(Context context) {
        String carrierID = null;
        String nocarrierID = null;

        try {
            nocarrierID = get_phone_uuid(context);
            carrierID = CarrierInfo(context);
        } catch (Exception e) {
            return nocarrierID;
        }

        if (carrierID != null && !carrierID.trim().isEmpty()) {
            return carrierID;
        } else {
            return nocarrierID;
        }
    }

    public static String getToken(Context context) {
        String IMEI = uniqueID(context);
        //
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        String curr_date = formater.format(new Date());
        //
        formater = new SimpleDateFormat("HHmmss");
        String curr_time = formater.format(new Date());
        //
        String key = IMEI + "_" + curr_date + "_" + curr_time;
        //
        return key;
    }

    public static String getPrefix(Context context) {
        return getToken(context) + "_";
    }

    public static void downloadZip(String urlPath, String localPath) throws Exception {

        URL url = new URL(urlPath);
        URLConnection connection = url.openConnection();

        File file = new File(localPath);
        if (file.exists()) {
            file.delete();
        }

        connection.setReadTimeout(60000);
        connection.setConnectTimeout(60000);

        FileOutputStream outputStream = new FileOutputStream(localPath, true);

        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        byte[] data = new byte[1024];

        int n;

        while ((n = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, n);
        }

        outputStream.flush();
        outputStream.close();

        inputStream.close();
    }

    public static void downloadNewVersion(String urlPath, String localPath) throws Exception {

        URL url = new URL(urlPath);
        //
        URLConnection connection = url.openConnection();
        //
        File file = new File(localPath);
        if (file.exists()) {
            file.delete();
        }
        //
        connection.setReadTimeout(60000);
        connection.setConnectTimeout(60000);
        //
        FileOutputStream outputStream = new FileOutputStream(localPath, true);
        //
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        byte[] data = new byte[1024];
        //
        int n;
        //
        while ((n = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, n);
        }
        //
        outputStream.flush();
        outputStream.close();
        //
        inputStream.close();
    }

    public static void downloadImagePDF(String urlPath, String localPath) throws Exception {

        URL url = new URL(urlPath);
        //
        URLConnection connection = url.openConnection();
        //
        File file = new File(localPath);
        if (file.exists()) {
            file.delete();
        }
        //
        connection.setReadTimeout(60000);
        connection.setConnectTimeout(60000);
        //
        FileOutputStream outputStream = new FileOutputStream(localPath, true);
        //
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        byte[] data = new byte[1024];
        //
        int n;
        //
        while ((n = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, n);
        }
        //
        outputStream.flush();
        outputStream.close();
        //
        inputStream.close();
    }

    public static String uploadFile(String json, String sFile, String sNewName) {
        try {
            //Como no processo de SO a foto pode mudar de nome,
            //Verifica qual o nome o arquivo esta no momento.
            String sRealFileName = sNewName != null ? sNewName : sFile;
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Constant.CACHE_PATH_PHOTO + "/" + sRealFileName);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload(Constant.WS_UPLOAD, json);

            return hfu.Send_Now(fstrm, sFile);

        } catch (Exception e) {
            String error = e.toString();
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return "Error: " + e.toString();
        }
    }

    public static String uploadFileChat(String json, String sFile, String sNewName) {
        try {
            //Como no processo de SO a foto pode mudar de nome,
            //Verifica qual o nome o arquivo esta no momento.
            String sRealFileName = sNewName != null ? sNewName : sFile;
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Constant.CACHE_PATH_PHOTO + "/" + sRealFileName);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload(
                    //Constant.WS_UPLOAD_CHAT
                    Constant.WS_UPLOAD_NODE_CHAT
                    , json);

            return hfu.Send_Now(fstrm, sFile);

        } catch (Exception e) {
            String error = e.toString();
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return "Error: " + e.toString();
        }
    }

    // Testar como substituto
    public static String multiPartRequest(String urlTo, String post, String filepath, String filefield) throws ParseException, IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            //outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            String[] posts = post.split("&");
            int max = posts.length;
            for (int i = 0; i < max; i++) {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                String[] kv = posts[i].split("=");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(kv[1]);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            inputStream = connection.getInputStream();
            result = convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            Log.e("MultipartRequest", "Multipart Form Upload Error");
            e.printStackTrace();
            return "error";
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
// see http://www.androidsnippets.com/multipart-http-requests


    public static String uploadFileSupport(String ws_url, String json, String sPath, String sFile) {
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(sPath + "/" + sFile);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload(ws_url, json);
            return hfu.Send_Now(fstrm, sFile);

        } catch (Exception e) {
            String error = e.toString();
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return "Error: " + e.toString();
        }
    }

    public static void deleteLocalImage(String sFile) {
        File file = new File(Constant.CACHE_PATH_PHOTO + "/" + sFile);

        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean unpackZip(String path, String zipname) {

        String stp = Constant.ZIP_PATH + "/";

        if (!path.trim().equals("")) {
            stp = path.trim();
        }

        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(stp + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(stp + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(stp + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();

        } catch (IOException e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return false;
        }

        return true;
    }

    public static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("ZIP", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("ZIP", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            ToolBox_Inf.registerException(CLASS_NAME, ioe);
            Log.e("ZIP", ioe.getMessage());
        }
    }

    public static File[] getListOfFiles_v2(final String prefix) {
        File fileList = new File(Constant.ZIP_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix)) {
                    return true;
                }

                return false;
            }
        });
        //
        if (files != null) {
            Arrays.sort(files);
        }
        //
        return files;
    }

    public static File[] getListOfFiles_v5(String path, final String prefix) {
        File fileList = new File(path);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix)) {
                    return true;
                }
                return false;
            }
        });
        //
        if (files != null) {
            Arrays.sort(files);
        }
        //
        return files;
    }

    public static String getContents(File aFile) {
        StringBuilder contents = new StringBuilder();

        try {
            BufferedReader input = new BufferedReader(new FileReader(aFile));

            try {

                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                }

            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ToolBox_Inf.registerException(CLASS_NAME, ex);
            ex.printStackTrace();
        }

        return contents.toString();
    }

    public static void deleteAllFOD(String sDir) {
        File dir = new File(sDir);
        //
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static void deleteDownloadFile(String sName) {
        File file = new File(sName);

        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteDownloadFileInf(String sName) {
        deleteDownloadFileInf(sName, Constant.CACHE_PATH);
    }

    public static void deleteDownloadFileInfV2(String sName) {
        deleteDownloadFileInf(sName, Constant.CACHE_PATH_PHOTO);
    }


    public static void deleteDownloadFileInf(String sName, String path) {
        File file = new File(path + "/" + sName);

        if (file.exists()) {
            file.delete();
        }
    }

    public static void renameDownloadFileInf(String sName, String ext) {
        File from = new File(Constant.CACHE_PATH + "/", sName + ".tmp");
        File to = new File(Constant.CACHE_PATH + "/", sName + ext);
        //
        from.renameTo(to);
    }

    public static void renameDownloadFileInf(String sName, String ext, String path) {
        File from = new File(path + "/", sName + ".tmp");
        File to = new File(path + "/", sName + ext);
        //
        from.renameTo(to);
    }

    public static void renameDownloadFileInfPHOTO(String sName, String ext) {
        File from = new File(Constant.CACHE_PATH_PHOTO + "/", sName + ".tmp");
        File to = new File(Constant.CACHE_PATH_PHOTO + "/", sName + ext);
        //
        from.renameTo(to);
    }

    public static void renameDownloadFileInfSig(String sName, String ext) {
        File from = new File(Constant.CACHE_PATH_PHOTO + "/", sName + ".tmps");
        File to = new File(Constant.CACHE_PATH_PHOTO + "/", sName + ext);
        //
        from.renameTo(to);
    }

    public static void renameDownloadFileInfV2(String path, String sName, String extOri, String extDest) {
        if (extOri == null || extOri.trim().length() == 0) {
            extOri = ".tmp";
        }

        File from = new File(path + "/", sName + extOri);
        File to = new File(path + "/", sName + extDest);
        //
        from.renameTo(to);
    }

    public static boolean verifyDownloadFileInf(String sName) {
        return verifyDownloadFileInf(sName, Constant.CACHE_PATH);
    }

    public static boolean verifyDownloadFileInfV2(String sName) {
        return verifyDownloadFileInf(sName, Constant.CACHE_PATH_PHOTO);
    }

    public static boolean verifyFileExists(String sName) {
        return verifyDownloadFileInf(sName, Constant.CACHE_PATH_PHOTO);
    }

    public static boolean verifyDownloadFileInf(String sName, String path) {
        File file = new File(path + "/", sName);

        return file.exists();
    }

    public static String sFileContent(String sPath, String sFile) {

        StringBuilder text = new StringBuilder();

        File file = new File(sPath, sFile);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }

            br.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);

        }

        return text.toString();
    }

    /**
     * Usar o mesmo metodo do que do  Toolbox
     *
     * @param context
     * @param type
     * @param value
     * @param link
     * @param required
     */
    @Deprecated
    public static void sendBCStatus(Context context, String type, String value, String link, String required) {
        Intent mIntent = new Intent(Constant.SW_TYPE_BR);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);

        mIntent.putExtra(Constant.SW_TYPE, type);
        mIntent.putExtra(Constant.SW_VALUE, value);
        mIntent.putExtra(Constant.SW_LINK, link != null ? link : "");
        mIntent.putExtra(Constant.SW_REQUIRED, required != null ? required : 0);


        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
    }

    public static void executeUpdSW(Context context, String link, String required) {
        Intent mIntent = new Intent(context, WBR_UpdateSoftware.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.SW_LINK, link);
        bundle.putString(Constant.SW_REQUIRED, required);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    public static boolean processWSCheck_GC(Context context, String sVersion, String sLogin, String s_Link, int iStatus, int iStatus_OD) {
        if (sVersion != null) {
            switch (sVersion) {
                case "VERSION_INVALID":
                    sendBCStatus(context, "VERSION_INVALID", context.getString(R.string.msg_version_invalid), s_Link, "1");
                    return false;

                case "EXPIRED":
                    sendBCStatus(context, "EXPIRED", context.getString(R.string.msg_version_expired), s_Link, "1");
                    return false;

                case "UPDATE_REQUIRED":
                    if (iStatus == 0) {
                        sendBCStatus(context, "UPDATE_REQUIRED", context.getString(R.string.msg_update_required), s_Link, "0");
                        return false;
                    } else {
                        break;
                    }
                case "STABLE":
                    break;

                default:
                    break;
            }
        }

        if (sLogin != null) {
            switch (sLogin) {
                case "USER_INVALID":
                    sendBCStatus(context, "USER_INVALID", context.getString(R.string.msg_user_invalid), s_Link, "0");
                    return false;

                case "USER_CANCELLED":
                    sendBCStatus(context, "USER_CANCELLED", context.getString(R.string.msg_user_canceled), s_Link, "0");
                    return false;

                case "USER_OTHER_DEVICE":
                    if (iStatus_OD == 0) {
                        sendBCStatus(context, "USER_OTHER_DEVICE", context.getString(R.string.msg_user_other_device), s_Link, "0");
                        return false;
                    } else {
                        return true;
                    }

                case "NFC_BLOCKED":
                    sendBCStatus(context, "ERROR_1", context.getString(R.string.msg_nfc_card_blocked) /*context.getString(R.string.msg_user_canceled)*/, s_Link, "0");
                    return false;

                case "DEVICE_CODE_REQUIRED":
                    sendBCStatus(context, "ERROR_1", context.getString(R.string.msg_device_code_not_found), s_Link, "0");
                    return false;


                case "OK":
                    break;
                default:
                    sendBCStatus(context, "ERROR_1", context.getString(R.string.msg_unespected_error), s_Link, "0");
                    return false;
            }
        }

        return true;
    }

    /**
     * @param context
     * @param validation
     * @param error_msg
     * @param s_Link
     * @param iStatus    - Se deve validar update_required.0 valida , 1 não valida
     * @param iStatus_OD - Se deve validar forced_login.0 valida , 1 não valida
     * @return
     */
    public static boolean processWSCheckValidation(Context context, String validation, String error_msg, String s_Link, int iStatus, int iStatus_OD) {
        validation = validation == null ? "" : validation;

        switch (validation) {
            case "OK":
                break;

            case "UPDATE_REQUIRED":
                if (iStatus == 0) {
                    sendBCStatus(context, "UPDATE_REQUIRED", context.getString(R.string.msg_update_required), s_Link, "0");

                    return false;
                } else {
                    return true;
                }

            case "VERSION_ERRO":
                sendBCStatus(context, "VERSION_ERRO", context.getString(R.string.msg_version_invalid), s_Link, "1");

                return false;

            case "VERSION_INVALID":
                sendBCStatus(context, "VERSION_INVALID", context.getString(R.string.msg_version_invalid), s_Link, "1");

                return false;

            case "EXPIRED":
                sendBCStatus(context, "EXPIRED", context.getString(R.string.msg_version_expired), s_Link, "1");

                return false;

            case "LOGIN_ERRO":
                sendBCStatus(context, "LOGIN_ERRO", error_msg, s_Link, "0");

                return false;

            case "USER_INVALID":
                sendBCStatus(context, "USER_INVALID", error_msg, s_Link, "0");

                return false;

            case "USER_BLOCKED":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");

                return false;

            case "USER_CANCELLED":
                sendBCStatus(context, "USER_CANCELLED", error_msg, s_Link, "0");

                return false;

            case "USER_OTHER_DEVICE":
                if (iStatus_OD == 0) {
                    sendBCStatus(context, "USER_OTHER_DEVICE", error_msg, s_Link, "0");
                    return false;
                } else {
                    return true;
                }

            case "SESSION_NOT_FOUND":
                sendBCStatus(context, "ERROR_3", error_msg, s_Link, "0");
                return false;

            case "CREATE_SESSION_ABORT":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;

            case "LICENSE_QTY_INVALID":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;
            case "PARAMETERS_ERROR":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;
            case "CUSTOMER_IP_REQUIRED":
                ToolBox.sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;
            case "CUSTOMER_IP_RESTRICTION":
                ToolBox.sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;
            default:
                if (validation.trim().length() == 0) {
                    return processoOthersError(context, context.getResources().getString(R.string.generic_error_lbl), error_msg);
                }
                break;
        }

        return true;
    }

    public static boolean processWSCheckValidationNFCAuth(Context context, String validation, String error_msg, String s_Link, String ret, String ret_error) {
        validation = validation == null ? "" : validation;

        switch (validation) {
            case "OK":
                break;

            case "UPDATE_REQUIRED":
                break;

            case "VERSION_ERRO":
                sendBCStatus(context, "VERSION_ERRO", error_msg, s_Link, "1");

                return false;

            case "VERSION_INVALID":
                sendBCStatus(context, "VERSION_INVALID", error_msg, s_Link, "1");

                return false;

            case "EXPIRED":
                sendBCStatus(context, "EXPIRED", error_msg, s_Link, "1");

                return false;

            case "USER_BLOCKED":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");

                return false;

            case "SESSION_NOT_FOUND":
                sendBCStatus(context, "ERROR_3", error_msg, s_Link, "0");
                return false;

            case "CREATE_SESSION_ABORT":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;

            case "LICENSE_QTY_INVALID":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;
            case "PARAMETERS_ERROR":
                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;

            case "CUSTOMER_IP_REQUIRED":
                ToolBox.sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;

            case "CUSTOMER_IP_RESTRICTION":
                ToolBox.sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
                return false;

            default:
                if (validation.trim().length() == 0) {
                    return processoOthersError(context, context.getResources().getString(R.string.generic_error_lbl), error_msg);
                }
                break;
        }

        if (ret_error != null) {
            sendBCStatus(context, "ERROR_1", ret_error, s_Link, "0");
            return false;
        }

        return true;
    }

    public static String BitMapToBase64(Bitmap bm) {
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //
            byte[] b = baos.toByteArray();
            //
            return Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    public static Bitmap Base64ToBitMap(String base64_img) {
        if (base64_img != null && !base64_img.equals("")) {
            byte[] a = Base64.decode(base64_img, Base64.DEFAULT);
            //
            return BitmapFactory.decodeByteArray(a, 0, a.length);
        } else {
            return null;
        }
    }

    public static void call_Act001_Main(Context context) {
        Intent mIntent = new Intent(context, Act001_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }


    public static void call_Notification_Sync(Context context, int id) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        Intent mIntent = new Intent(context, WS_Notification_Sync.class);

        PendingIntent pi = PendingIntent.getService(
                context,
                0,
                mIntent,
                0
        );
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.sync_notification_animation);
        builder.setAutoCancel(false);
        builder.setContentTitle(context.getString(R.string.title_notification_generic));
        //builder.setContentIntent(pi);
        builder.setContentText(context.getString(R.string.message_notification_sync));
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        //
        int versao = Build.VERSION.SDK_INT;
        //
        if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nm.notify(id, builder.build());
        } else {
            nm.notify(id, builder.getNotification());
        }
    }

    /**
     * Metodo que retorna o Resource_code, baseado no Resource_name
     *
     * @param context
     * @param module_code
     * @param resource_name
     * @return
     */
    public static String getResourceCode(Context context, String module_code, String resource_name) {
        //Dao para buscar codigo do recurso
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        //Usa Sql 002 para selecionar obj com o resource_code
        EV_Module_Res evModuleRes = moduleResDao.getByString(
                new EV_Module_Res_Txt_Sql_002(
                        module_code,
                        resource_name
                ).toSqlQuery()
        );

        try {
            return String.valueOf(evModuleRes.getResource_code());
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Melhoria do metodo setLanguage sem translation_list
     * Quando houver tempo, fazer taskforce para substituir esse
     * metodo nas acts que usam o antigo.
     *
     * @param context
     * @param module_code
     * @param resource_code
     * @param translate_code
     * @param translation_list
     * @return
     */
    public static HMAux setLanguage(Context context, String module_code, String resource_code, String translate_code, List<String> translation_list) {

        EV_Module_Res_Txt_TransDao transDao = new EV_Module_Res_Txt_TransDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        List<EV_Module_Res_Txt_Trans> module_res_txt_transes = transDao.query(
                new EV_Module_Res_Txt_Trans_Sql_002(
                        module_code,
                        resource_code,
                        translate_code
                ).toSqlQuery()
        );

        HMAux item = new HMAux();
        //
        for (EV_Module_Res_Txt_Trans module_res_txt_trans : module_res_txt_transes) {
            item.put(module_res_txt_trans.getTxt_code(), module_res_txt_trans.getTxt_value());
        }

        item = ToolBox_Inf.getTranslationList(item, module_code, resource_code, translation_list);

        //
        return item;
    }

    @Deprecated
    public static HMAux setLanguage(Context context, String module_code, String resource_code, String translate_code) {

        EV_Module_Res_Txt_TransDao transDao = new EV_Module_Res_Txt_TransDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        List<EV_Module_Res_Txt_Trans> module_res_txt_transes = transDao.query(
                new EV_Module_Res_Txt_Trans_Sql_002(
                        module_code,
                        resource_code,
                        translate_code
                ).toSqlQuery()
        );

        HMAux item = new HMAux();
        //
        for (EV_Module_Res_Txt_Trans module_res_txt_trans : module_res_txt_transes) {
            item.put(module_res_txt_trans.getTxt_code(), module_res_txt_trans.getTxt_value());
        }
        //
        return item;
    }

    public static String sVersionDesc(String Build_RELEASE, String Build_SDK_INT) {
        return Build_RELEASE + " (" + Build_SDK_INT + ")";
    }

    public static HMAux getTranslationList(HMAux hmAux_Trans, String mModule_Code, String mResource_Code, List<String> translate_list) {

        for (String txt : translate_list) {

            if (hmAux_Trans.get(txt) != null) {
                hmAux_Trans.put(txt, hmAux_Trans.get(txt));
            } else {
                hmAux_Trans.put(txt, ToolBox.setNoTrans(mModule_Code, mResource_Code, txt));
            }
        }
        return hmAux_Trans;
    }

    public static boolean checkFormIsReady(Context context, long customer_code, int custom_form_type, int custom_form_code, int custom_form_version) {
        GE_Custom_Form_Blob_LocalDao blobLocalDao =
                new GE_Custom_Form_Blob_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
        List<GE_Custom_Form_Blob_Local> pendingBlobs = blobLocalDao.query(
                new GE_Custom_Form_Blob_Local_Sql_004(
                        customer_code,
                        custom_form_type,
                        custom_form_code,
                        custom_form_version
                ).toSqlQuery()
        );
        //Se exitem blobs pendentes, retorna false
        if (pendingBlobs.size() > 0) {
            return false;
        } else {
            GE_Custom_Form_Field_LocalDao fieldLocalDao =
                    new GE_Custom_Form_Field_LocalDao(
                            context,
                            ToolBox_Con.customDBPath(customer_code),
                            Constant.DB_VERSION_CUSTOM
                    );
            List<HMAux> pendingPictures = fieldLocalDao.query_HM(
                    new GE_Custom_Form_Field_Local_Sql_003(
                            customer_code,
                            custom_form_type,
                            custom_form_code,
                            custom_form_version
                    ).toSqlQuery()
            );

            if (pendingPictures.size() > 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Verifica se existe o arquivo de banco de dados daquele customer
     *
     * @param customer_code
     * @return True or false
     */
    public static boolean checkCustomerDBExists(final long customer_code) {
        File customerDB = new File(Constant.DB_PATH + "/C_" + customer_code + "_" + Constant.DB_VERSION_CUSTOM + ".db3");
        //
        return customerDB.exists();
    }

    public static void showNoConnectionDialog(Context act_context) {
        String title = "";
        String msg = "";

        //Se possui variavel translate code, busca tradução
        if (!ToolBox_Con.getPreference_Translate_Code(act_context).equals("")) {
            String mModule = "SYS";
            String mResource_name = "SYS_APP";
            //
            List<String> transList = new ArrayList<>();
            transList.add("alert_no_conection_ttl");
            transList.add("alert_no_conection_msg");

            HMAux hmTrans = setLanguage(
                    act_context,
                    mModule,
                    getResourceCode(
                            act_context,
                            mModule,
                            mResource_name
                    ),
                    ToolBox_Con.getPreference_Translate_Code(act_context),
                    transList
            );

            title = hmTrans.get("alert_no_conection_ttl");
            msg = hmTrans.get("alert_no_conection_msg");
        } else {
            //Se não busca do arquivo de Strings
            title = act_context.getString(R.string.generic_no_connection_ttl);
            msg = act_context.getString(R.string.generic_no_connection_msg);
        }

        //Chama caixa de dialogo
        ToolBox.alertMSG(
                act_context,
                title,
                msg,
                null,
                0
        );
    }

    public static void buildFooterDialog(Context context) {

        HMAux hmDialogInfo = loadFooterDialogInfo(context);

        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.footer_dialog_info_app, null);
        LinearLayout ll_footer_close = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll);

        ImageView iv_customer = (ImageView) customView.findViewById(R.id.footer_dialog_app_iv_customer);
        //
        LinearLayout ll_customer = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_customer);
        TextView tv_customer_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_customer_lbl);
        TextView tv_customer_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_customer_value);
        //
        LinearLayout ll_site = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_site);
        TextView tv_site_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_site_lbl);
        TextView tv_site_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_site_value);
        //
        LinearLayout ll_zone = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_zone);
        TextView tv_zone_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_zone_lbl);
        TextView tv_zone_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_zone_value);
        //
        LinearLayout ll_operation = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_operation);
        TextView tv_operation_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_operation_lbl);
        TextView tv_operation_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_operation_value);
        //
        LinearLayout ll_imei = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_imei);
        TextView tv_imei_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_imei_lbl);
        TextView tv_imei_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_imei_value);
        //
        TextView tv_version_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_version_lbl);
        TextView tv_version_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_version_number);
        //
        Bitmap customer_img = getCustomerImage(ToolBox_Inf.getCustomerLogoPath(context));

        if (customer_img != null) {
            iv_customer.setImageBitmap(customer_img);
        } else {
            iv_customer.setImageBitmap(null);
            // Fazer Analise
        }
        //
        tv_customer_lbl.setText(hmDialogInfo.get(Constant.FOOTER_CUSTOMER_LBL));
        tv_customer_value.setText(hmDialogInfo.get(Constant.FOOTER_CUSTOMER));

        tv_site_lbl.setText(hmDialogInfo.get(Constant.FOOTER_SITE_LBL));
        tv_site_value.setText(hmDialogInfo.get(Constant.FOOTER_SITE));
        ll_site.setVisibility(hmDialogInfo.get(Constant.FOOTER_SITE) == null || hmDialogInfo.get(Constant.FOOTER_SITE).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_zone_lbl.setText(hmDialogInfo.get(Constant.FOOTER_ZONE_LBL));
        tv_zone_value.setText(hmDialogInfo.get(Constant.FOOTER_ZONE));
        ll_zone.setVisibility(hmDialogInfo.get(Constant.FOOTER_ZONE) == null || hmDialogInfo.get(Constant.FOOTER_ZONE).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_operation_lbl.setText(hmDialogInfo.get(Constant.FOOTER_OPERATION_LBL));
        tv_operation_value.setText(hmDialogInfo.get(Constant.FOOTER_OPERATION));
        ll_operation.setVisibility(hmDialogInfo.get(Constant.FOOTER_OPERATION) == null || hmDialogInfo.get(Constant.FOOTER_OPERATION).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_imei_lbl.setText(hmDialogInfo.get(Constant.FOOTER_IMEI_LBL));
        tv_imei_value.setText(hmDialogInfo.get(FOOTER_IMEI));
        ll_imei.setVisibility(hmDialogInfo.get(FOOTER_IMEI) == null || hmDialogInfo.get(FOOTER_IMEI).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_version_lbl.setText(hmDialogInfo.get(FOOTER_VERSION_LBL));
        tv_version_value.setText(Constant.PRJ001_VERSION);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dmW = (float) dm.widthPixels * 0.9f;
        float dmH = (float) dm.heightPixels * 0.9f;

        //customDialog = new Dialog(context, R.style.MyDialogTheme);
        final Dialog customDialog = new Dialog(context);
        customDialog.setContentView(customView);
        customDialog.getWindow().setLayout((int) dmW, (int) dmH);
        customDialog.show();

        ll_footer_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

    }

    private static Bitmap getCustomerImage(String path) {
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        return bitmap;
    }

    public static HMAux loadFooterDialogInfo(Context context) {
        HMAux hmAux = new HMAux();
        String customerDesc = "";
        String siteDesc = "";
        String zoneDesc = "";
        String operationDesc = "";

        List<String> transList = new ArrayList<>();
        transList.add("lbl_external_site");
        transList.add("footer_dialog_customer_lbl");
        transList.add("footer_dialog_site_lbl");
        transList.add("footer_dialog_zone_lbl");
        transList.add("footer_dialog_operation_lbl");
        transList.add("footer_dialog_btn_ok");
        transList.add("footer_dialog_btn_ok");
        transList.add("footer_dialog_imei");
        //
        HMAux HmTrans = setLanguage(
                context,
                Constant.APP_MODULE,
                getResourceCode(context, Constant.APP_MODULE, Constant.ACT003),
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        MD_Site site =
                new MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).getByString(
                        new MD_Site_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context)
                        ).toSqlQuery()
                );

        MD_Operation operation =
                new MD_OperationDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).getByString(
                        new MD_Operation_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Operation_Code(context)
                        ).toSqlQuery()
                );


        customerDesc = ToolBox_Con.getPreference_Customer_Code(context) + " - " + ToolBox_Con.getPreference_Customer_Code_NAME(context);
        operationDesc = operation.getOperation_code() + " - " + operation.getOperation_desc();

        siteDesc = site.getSite_code() + " - " + site.getSite_desc();

        hmAux.put(Constant.FOOTER_CUSTOMER_LBL, HmTrans.get("footer_dialog_customer_lbl"));
        hmAux.put(Constant.FOOTER_CUSTOMER, customerDesc);
        hmAux.put(Constant.FOOTER_SITE_LBL, HmTrans.get("footer_dialog_site_lbl"));
        hmAux.put(Constant.FOOTER_SITE, siteDesc);
        hmAux.put(Constant.FOOTER_OPERATION_LBL, HmTrans.get("footer_dialog_operation_lbl"));
        hmAux.put(Constant.FOOTER_OPERATION, operationDesc);
        hmAux.put(Constant.FOOTER_BTN_OK, HmTrans.get("footer_dialog_btn_ok"));
        hmAux.put(Constant.FOOTER_VERSION_LBL, HmTrans.get("footer_version_lbl"));
        hmAux.put(Constant.FOOTER_IMEI_LBL, HmTrans.get("footer_dialog_imei"));
        hmAux.put(FOOTER_IMEI, ToolBox_Inf.uniqueID(context));
        //
        hmAux.put(Constant.FOOTER_ZONE_LBL, "");
        hmAux.put(Constant.FOOTER_ZONE, "");

        if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})) {
            MD_Site_Zone zone =
                    new MD_Site_ZoneDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    ).getByString(
                            new MD_Site_Zone_Sql_003(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)),
                                    ToolBox_Con.getPreference_Zone_Code(context)

                            ).toSqlQuery()
                    );
            zoneDesc = "";
            if (zone != null) {
                zoneDesc = zone.getZone_code() + " - " + zone.getZone_desc();
            }
            //
            hmAux.put(Constant.FOOTER_ZONE_LBL, HmTrans.get("footer_dialog_zone_lbl"));
            hmAux.put(Constant.FOOTER_ZONE, zoneDesc);

        }
        return hmAux;

    }

    public static String getCustomerLogoPath(Context context) {

        return Constant.IMG_PATH + "/logo_c_" + ToolBox_Con.getPreference_Customer_Code(context) + ".png";

    }

    public static void cleanOldSyncChecklistData(Context context) {
        Sync_ChecklistDao syncChecklistDao =
                new Sync_ChecklistDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        syncChecklistDao.remove(
                new Sync_Checklist_Sql_003(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );

    }

    public static void reprogramAlarms(Context context) {

        AlarmManager am = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        Intent mIntent = new Intent(context,
                WBR_Cleanning.class
        );
        //
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                0
        );
        //
        am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (5 * 60 * 1000),
                (43200000),
                pi
        );
    }

    public static void reprogramAlarms_Full_Quarter(Context context) {
        AlarmManager am = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendarAux = Calendar.getInstance();
        //
        calendarAux.set(
                Calendar.HOUR,
                calendarAux.get(Calendar.HOUR) + 1
        );
        //
        calendarAux.set(
                Calendar.MINUTE,
                0
        );
        //
        calendarAux.set(
                Calendar.SECOND,
                0
        );
//        // Tirar
//        calendarAux.set(
//                Calendar.SECOND,
//                calendarAux.get(Calendar.SECOND) + 15
//        );

        /**
         * Alarme a cada 4 horas
         */
        Intent mIntent_Full = new Intent(context,
                WBR_AL_Full.class
        );
        //
        boolean isWorking = (PendingIntent.getBroadcast(
                context, 100, mIntent_Full, PendingIntent.FLAG_NO_CREATE) != null);

        Log.d("ALARM", String.valueOf(isWorking));

        if (!isWorking) {

            PendingIntent pi_full = PendingIntent.getBroadcast(
                    context,
                    100,
                    mIntent_Full,
                    0
            );
            //
            am.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendarAux.getTimeInMillis(),
                    (1000 * 60 * 60 * 4),
                    //(1000 * 60 * 5),
                    pi_full
            );
        }

        calendarAux = Calendar.getInstance();

        calendarAux.set(
                Calendar.SECOND,
                calendarAux.get(Calendar.SECOND) + 15
        );

        /**
         * Alarme a cada 15 minutos
         */
        Intent mIntent_Quarter = new Intent(context,
                WBR_AL_Quarter.class
        );
        //
        isWorking = (PendingIntent.getBroadcast(
                context, 200, mIntent_Quarter, PendingIntent.FLAG_NO_CREATE) != null);

        Log.d("ALARM", String.valueOf(isWorking));

        if (!isWorking) {
            PendingIntent pi_Quarter = PendingIntent.getBroadcast(
                    context,
                    200,
                    mIntent_Quarter,
                    0
            );
            //
            am.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendarAux.getTimeInMillis(),
                    (1000 * 60 * 15),
                    //(1000 * 60 * 2),
                    pi_Quarter
            );
        }
        //
        // generateNotification(context, 300);
    }

    public static void generateNotification(Context context, int parameter) {

        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        EV_User_CustomerDao ev_user_customerDao = new EV_User_CustomerDao(
                context,
                Constant.DB_FULL_BASE,
                Constant.DB_VERSION_BASE
        );

        GE_Custom_Form_LocalDao formLocalDao;

        ArrayList<HMAux> customers_vs_total = new ArrayList<>();

        ArrayList<HMAux> totals;

        ArrayList<HMAux> customers = (ArrayList<HMAux>) ev_user_customerDao.query_HM(
                new EV_User_Customer_Sql_006()
                        .toSqlQuery()
        );

        for (HMAux hmAux : customers) {
            formLocalDao =
                    new GE_Custom_Form_LocalDao(
                            context,
                            ToolBox_Con.customDBPath(Long.parseLong(hmAux.get("customer_code"))),
                            Constant.DB_VERSION_CUSTOM
                    );

            HMAux auxCT = new HMAux();
            auxCT.put("customer_code", hmAux.get("customer_code"));
            auxCT.put("customer_name", hmAux.get("customer_name"));
            auxCT.put("translate_code", hmAux.get("translate_code"));
            //
            boolean bInclude = false;
            //
            if (parameter == 100 || parameter == 300) {
                totals = (ArrayList<HMAux>) formLocalDao.query_HM(
                        new GE_Custom_Form_Local_Sql_013(
                                Calendar.getInstance().getTimeInMillis()
                        ).toSqlQuery()
                );
            } else {
                totals = (ArrayList<HMAux>) formLocalDao.query_HM(
                        new GE_Custom_Form_Local_Sql_014(
                                Calendar.getInstance().getTimeInMillis()
                        ).toSqlQuery()
                );
            }
            //
            for (HMAux hmAuxTotal : totals) {
                bInclude = true;

                if (parameter == 100 || parameter == 300) {
                    if (hmAuxTotal.get("type").equalsIgnoreCase("future")) {
                        auxCT.put("future", hmAuxTotal.get("total"));
                    } else {
                        auxCT.put("late", hmAuxTotal.get("total"));
                    }
                } else {
                    if (hmAuxTotal.get("type").equalsIgnoreCase("future")) {
                        auxCT.put("future", hmAuxTotal.get("total"));
                    } else {
                    }
                }
            }
            //
            if (bInclude) {
                HMAux hmTr = translationCustomerSys(context, auxCT.get("translate_code"));
                auxCT.put("future_text", hmTr.get("message_full_quarter_notification_future"));
                auxCT.put("late_text", hmTr.get("message_full_quarter_notification_late"));
                customers_vs_total.add(auxCT);
            }

        }

        int tamanho_final = customers_vs_total.size();


        for (HMAux cust : customers_vs_total) {

            StringBuilder sbFinal = new StringBuilder();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_calendario);
            builder.setAutoCancel(true);

            if (parameter == 100 || parameter == 300) {
                if (!cust.get("future").equals("0") || !cust.get("late").equals("0")) {
                    sbFinal
                            .append(cust.get("future_text") + "(")
                            .append(cust.get("future") + ")  ")
                            .append(cust.get("late_text") + "(")
                            .append(cust.get("late") + ")\n");
                }
            } else {
                if (!cust.get("future").equals("0")) {
                    sbFinal
                            .append(cust.get("future_text") + "(")
                            .append(cust.get("future") + ")\n");
                }
            }

            int parameter_unified = 500;

            if (sbFinal.toString().length() != 0) {

                //builder.setContentTitle(context.getString(R.string.title_notification_generic));
                //builder.setContentText(context.getString(R.string.message_notification_sync));
                builder.setContentTitle(cust.get("customer_name"));
                builder.setContentText(sbFinal.toString());
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                //
                int versao = Build.VERSION.SDK_INT;
                //
                if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    nm.notify(parameter_unified + Integer.parseInt(cust.get("customer_code")), builder.build());
                } else {
                    nm.notify(parameter_unified + Integer.parseInt(cust.get("customer_code")), builder.getNotification());
                }

            }
        }
    }

    public static void libTranslation(Context context) {
        Constant.HMAUX_TRANS_LIB = new HMAux();

        if (!ToolBox_Con.getPreference_Translate_Code(context).equals("")) {
            List<String> transList = new ArrayList<>();
            Constant.HMAUX_TRANS_LIB = setLanguage(
                    context,
                    Constant.APP_MODULE,
                    getResourceCode(
                            context,
                            Constant.APP_MODULE,
                            Constant.LIB_RESOURCE_NAME
                    ),
                    ToolBox_Con.getPreference_Translate_Code(context),
                    transList);
        }


        Constant.HMAUX_TRANS_LIB.put("nfc_activity_title", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_activity_title") || Constant.HMAUX_TRANS_LIB.get("nfc_activity_title").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_activity_title) : Constant.HMAUX_TRANS_LIB.get("nfc_activity_title")));
        Constant.HMAUX_TRANS_LIB.put("nfc_activity_desc", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_activity_desc") || Constant.HMAUX_TRANS_LIB.get("nfc_activity_desc").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_activity_desc) : Constant.HMAUX_TRANS_LIB.get("nfc_activity_desc")));


        Constant.HMAUX_TRANS_LIB.put("nfc_no_support", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_no_support") || Constant.HMAUX_TRANS_LIB.get("nfc_no_support").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_no_support) : Constant.HMAUX_TRANS_LIB.get("nfc_no_support")));
        Constant.HMAUX_TRANS_LIB.put("nfc_no_data", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_no_data") || Constant.HMAUX_TRANS_LIB.get("nfc_no_data").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_no_data) : Constant.HMAUX_TRANS_LIB.get("nfc_no_data")));
        Constant.HMAUX_TRANS_LIB.put("nfc_invalid_tag", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_invalid_tag") || Constant.HMAUX_TRANS_LIB.get("nfc_invalid_tag").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_invalid_tag) : Constant.HMAUX_TRANS_LIB.get("nfc_no_support")));
        Constant.HMAUX_TRANS_LIB.put("nfc_invalid_type", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_invalid_type") || Constant.HMAUX_TRANS_LIB.get("nfc_invalid_type").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_invalid_type) : Constant.HMAUX_TRANS_LIB.get("nfc_invalid_type")));
        Constant.HMAUX_TRANS_LIB.put("nfc_security_violation", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_security_violation") || Constant.HMAUX_TRANS_LIB.get("nfc_security_violation").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_security_violation) : Constant.HMAUX_TRANS_LIB.get("nfc_security_violation")));
        Constant.HMAUX_TRANS_LIB.put("nfc_security_crypto_no_support", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_security_crypto_no_support") || Constant.HMAUX_TRANS_LIB.get("nfc_security_crypto_no_support").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_security_crypto_no_support) : Constant.HMAUX_TRANS_LIB.get("nfc_security_crypto_no_support")));
        Constant.HMAUX_TRANS_LIB.put("nfc_security_geral", (!Constant.HMAUX_TRANS_LIB.containsKey("nfc_security_geral") || Constant.HMAUX_TRANS_LIB.get("nfc_security_geral").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.nfc_security_geral) : Constant.HMAUX_TRANS_LIB.get("nfc_security_geral")));
        Constant.HMAUX_TRANS_LIB.put("alert_title_signature_validation", (!Constant.HMAUX_TRANS_LIB.containsKey("alert_title_signature_validation") || Constant.HMAUX_TRANS_LIB.get("alert_title_signature_validation").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.alert_title_signature_validation) : Constant.HMAUX_TRANS_LIB.get("alert_title_signature_validation")));
        Constant.HMAUX_TRANS_LIB.put("alert_msg_signature_validation", (!Constant.HMAUX_TRANS_LIB.containsKey("alert_msg_signature_validation") || Constant.HMAUX_TRANS_LIB.get("alert_msg_signature_validation").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.alert_msg_signature_validation) : Constant.HMAUX_TRANS_LIB.get("alert_msg_signature_validation")));

        Constant.HMAUX_TRANS_LIB.put("signature_hint", (!Constant.HMAUX_TRANS_LIB.containsKey("signature_hint") || Constant.HMAUX_TRANS_LIB.get("signature_hint").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.signature_hint) : Constant.HMAUX_TRANS_LIB.get("signature_hint")));

        Constant.HMAUX_TRANS_LIB.put("mdots_server_title", (!Constant.HMAUX_TRANS_LIB.containsKey("mdots_server_title") || Constant.HMAUX_TRANS_LIB.get("mdots_server_title").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.mdots_server_title) : Constant.HMAUX_TRANS_LIB.get("mdots_server_title")));
        Constant.HMAUX_TRANS_LIB.put("mdots_user_title", (!Constant.HMAUX_TRANS_LIB.containsKey("mdots_user_title") || Constant.HMAUX_TRANS_LIB.get("mdots_user_title").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.mdots_user_title) : Constant.HMAUX_TRANS_LIB.get("mdots_user_title")));
        Constant.HMAUX_TRANS_LIB.put("mdots_non_compliance", (!Constant.HMAUX_TRANS_LIB.containsKey("mdots_non_compliance") || Constant.HMAUX_TRANS_LIB.get("mdots_non_compliance").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.mdots_non_compliance) : Constant.HMAUX_TRANS_LIB.get("mdots_non_compliance")));
        Constant.HMAUX_TRANS_LIB.put("sys_alert_btn_ok", (!Constant.HMAUX_TRANS_LIB.containsKey("sys_alert_btn_ok") || Constant.HMAUX_TRANS_LIB.get("sys_alert_btn_ok").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.sys_alert_btn_ok) : Constant.HMAUX_TRANS_LIB.get("sys_alert_btn_ok")));
        Constant.HMAUX_TRANS_LIB.put("footer_label", (!Constant.HMAUX_TRANS_LIB.containsKey("footer_label") || Constant.HMAUX_TRANS_LIB.get("footer_label").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.footer_label) : Constant.HMAUX_TRANS_LIB.get("footer_label")));
    }

    public static HMAux translationCustomerSys(Context context, String translate_code) {
        HMAux hmAux = new HMAux();

        if (!translate_code.equals("")) {
            List<String> transList = new ArrayList<>();
            hmAux = setLanguage(
                    context,
                    Constant.APP_MODULE,
                    getResourceCode(
                            context,
                            Constant.APP_MODULE,
                            Constant.LIB_RESOURCE_NAME
                    ),
                    translate_code,
                    transList);
        }

        hmAux.put("message_full_quarter_notification_future", (!hmAux.containsKey("message_full_quarter_notification_future") || hmAux.get("message_full_quarter_notification_future").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.message_full_quarter_notification_future) : hmAux.get("message_full_quarter_notification_future")));
        hmAux.put("message_full_quarter_notification_late", (!hmAux.containsKey("message_full_quarter_notification_late") || hmAux.get("message_full_quarter_notification_late").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.message_full_quarter_notification_late) : hmAux.get("message_full_quarter_notification_late")));

        return hmAux;
    }


    public static boolean hasNFC(Context context) {
        if (NfcAdapter.getDefaultAdapter(context) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String nlsDate2SqliteDate(Context context) {
        String sqlite_format = ToolBox_Con.getPreference_Customer_nls_date_format(context);
        return sqlite_format
                .replace("DD", "%d")
                .replace("MM", "%m")
                .replace("RRRR", "%Y");
    }

    public static String nlsDateFormat(Context context) {
        String sqlite_format = ToolBox_Con.getPreference_Customer_nls_date_format(context);
        return sqlite_format
                .replace("DD", "dd")
                .replace("MM", "MM")
                .replace("RRRR", "yyyy");
    }

    public static void cleanningFormLocal(Context context) {
        GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        formLocalDao.remove(
                new GE_Custom_Form_Local_Sql_010().toSqlQuery()
        );

    }

    public static boolean isDevelopmentBase() {
        return Constant.WS_GETCUSTOMERS.contains("https://dev.");
    }

    public static boolean isDownloadRunning() {
        if (WBR_DownLoad_Customer_Logo.IS_RUNNING
                || WBR_DownLoad_PDF.IS_RUNNING
                || WBR_DownLoad_Picture.IS_RUNNING
                || WBR_UpdateSoftware.IS_RUNNING
                ) {
            return true;
        }
        return false;
    }

    public static boolean isUploadRunning() {
        if (WBR_Upload_Img.IS_RUNNING || WBR_Upload_Support.IS_RUNNING) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param notification_id Constant com o id da notificação;
     */
    public static void showNotification(Context context, int notification_id) {
        List<String> translist = new ArrayList<>();
        int animation = -1;
        String title = "";
        String msg = "";

        HMAux hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                "",
                "0",
                ToolBox_Con.getPreference_Translate_Code(context),
                translist
        );

        switch (notification_id) {

            case Constant.NOTIFICATION_UPLOAD:
                animation = R.drawable.upload_animation;
                title = hmAux_Trans.get("notification_ttl_upload");
                msg = hmAux_Trans.get("notification_msg_upload");
                break;

            case Constant.NOTIFICATION_DOWNLOAD:
                animation = R.drawable.download_animation;
                title = context.getString(R.string.notification_ttl_download);
                if (hmAux_Trans.containsKey("notification_ttl_download")) {
                    title = hmAux_Trans.get("notification_ttl_download");
                }
                msg = context.getString(R.string.notification_msg_download);
                if (hmAux_Trans.containsKey("notification_msg_download")) {
                    msg = hmAux_Trans.get("notification_msg_download");
                }
                break;
        }
        //Se Id encontrado gera notificação, se não, não.
        if (animation != -1) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(animation)
                            .setContentTitle(title)
                            .setContentText(msg)
                            .setTicker("");

            mBuilder.setAutoCancel(true);

            NotificationManager mNotifyManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            mNotifyManager.notify(notification_id, mBuilder.build());

        }

    }

    public static void cancelNotification(Context context, int notification_id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
    }

    public static StringBuilder wsExceptionTreatment(Context context, Exception e) {
        StringBuilder sb = new StringBuilder();
        String results = "";

        HMAux hmAux_Trans = new HMAux();

        if (!ToolBox_Con.getPreference_Translate_Code(context).equals("")) {
            List<String> transList = new ArrayList<>();
            transList.add("ws_exception_contact_admin_json_syntax");
            transList.add("ws_exception_contact_admin_oracle");
            transList.add("ws_exception_contact_admin_timeout");
            transList.add("ws_exception_server_connection_failed");
            transList.add("generic_error_lbl");

            hmAux_Trans = setLanguage(
                    context,
                    Constant.APP_MODULE,
                    getResourceCode(
                            context,
                            Constant.APP_MODULE,
                            "0"
                    ),
                    ToolBox_Con.getPreference_Translate_Code(context),
                    transList);
        }

        hmAux_Trans.put("ws_exception_contact_admin_json_syntax",
                (!hmAux_Trans.containsKey("ws_exception_contact_admin_json_syntax") || hmAux_Trans.get("ws_exception_contact_admin_json_syntax").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_contact_admin_json_syntax) : hmAux_Trans.get("ws_exception_contact_admin_json_syntax"))
        );

        hmAux_Trans.put("ws_exception_contact_admin_oracle",
                (!hmAux_Trans.containsKey("ws_exception_contact_admin_oracle") || hmAux_Trans.get("ws_exception_contact_admin_oracle").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_contact_admin_oracle) : hmAux_Trans.get("ws_exception_contact_admin_oracle"))
        );

        hmAux_Trans.put("ws_exception_contact_admin_timeout",
                (!hmAux_Trans.containsKey("ws_exception_contact_admin_timeout") || hmAux_Trans.get("ws_exception_contact_admin_timeout").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_contact_admin_timeout) : hmAux_Trans.get("ws_exception_contact_admin_timeout"))
        );

        hmAux_Trans.put("ws_exception_server_connection_failed",
                (!hmAux_Trans.containsKey("ws_exception_server_connection_failed") || hmAux_Trans.get("ws_exception_server_connection_failed").contains(Constant.APP_MODULE + "/") ? context.getString(R.string.ws_exception_server_connection_failed) : hmAux_Trans.get("ws_exception_server_connection_failed"))
        );

        results = (!hmAux_Trans.containsKey("generic_error_lbl") || hmAux_Trans.get("generic_error_lbl").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.generic_error_lbl) : hmAux_Trans.get("generic_error_lbl")).toUpperCase();

        if (e.toString().contains("JsonSyntaxException")) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_contact_admin_json_syntax"))
                    .append("\n")
                    .append("\n")
                    .append("JsonParse!\n")
                    .append(e.toString()
                    );

        } else if (e.toString().contains("ORA-")) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_contact_admin_oracle"))
                    .append("\n")
                    .append("\n")
                    .append("ORACLE!\n")
                    .append(e.toString()
                    );

        } else if (e.toString().toLowerCase().contains("timeout")) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_contact_admin_timeout"))
                    .append("\n")
                    .append("\n")
                    .append("Timeout!\n ")
                    .append(e.toString()
                    );
        } else if (e.toString().contains(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR)) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_server_connection_failed"))
                    .append("\n")
                    .append("\n")
                    .append(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR + "!\n ")
            //.append(e.toString())//Como exception na mão, não tem toString
            ;

        } else {
            sb.append(results)
                    .append("\n")
                    .append("\n")
                    .append(e.toString());
        }
        return sb;
    }

    /**
     * Verifica se o parametro passado existe para aquele customer
     *
     * @param context
     * @param param   Constante do parametro a ser buscado
     * @return true or false;
     */
    public static boolean parameterExists(Context context, String param) {
        try {
            Ev_User_Customer_ParameterDao parameterDao
                    = new Ev_User_Customer_ParameterDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
            //
            Ev_User_Customer_Parameter parameter
                    = parameterDao.getByString(
                    new Ev_User_Customer_Parameter_Sql_002(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            param
                    ).toSqlQuery()
            );

            if (parameter != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * Verifica se conjunto de parametros passados existe para aquele customer
     *
     * @param context
     * @param param   Array com constante dos parametros a serem buscados
     * @return true or false;
     */
    public static boolean parameterExists(Context context, String[] param) {
        try {
            Ev_User_Customer_ParameterDao parameterDao
                    = new Ev_User_Customer_ParameterDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);

            for (int i = 0; i < param.length; i++) {
                Ev_User_Customer_Parameter parameter
                        = parameterDao.getByString(
                        new Ev_User_Customer_Parameter_Sql_002(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                param[i]
                        ).toSqlQuery()
                );

                if (parameter != null) {
                    return true;
                }
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * Converte data enviada para o timezone do aparelho
     *
     * @param
     * @return data convertida
     */
    public static String convertToDeviceTMZ(String date_tmz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        Calendar calendar = Calendar.getInstance();
        try {
            // calendar.setTime(sdf.parse("2017-04-13 12:24:46 +0000"));
            calendar.setTime(sdf.parse(date_tmz));
        } catch (ParseException e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            e.printStackTrace();
        }

        return sdf.format(calendar.getTime());
    }

    /**
     * Devolve milisegundos de uma data
     *
     * @param date_tmz
     * @return
     */
    public static long dateToMilliseconds(String date_tmz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date_tmz));
        } catch (ParseException e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            e.printStackTrace();
        }

        return calendar.getTimeInMillis();
    }

    public static long dateToMilliseconds(String date_tmz, String type) {
        String sFormat = "";

        if (date_tmz.isEmpty()) {
            return 0L;
        }

        if (type.equalsIgnoreCase("SECOND")) {
            sFormat = "yyyy-MM-dd HH:mm Z";
        } else {
            sFormat = "yyyy-MM-dd HH:mm:ss Z";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date_tmz));
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return 0L;
        }
    }

    public static String millisecondsToString(long mils, String format) {

        String sResults = "";

        if (mils == 0L) {
            return "";
        }

        Calendar ca1 = Calendar.getInstance();
        ca1.setTimeInMillis(mils);
        if (format == null || format.equalsIgnoreCase("")) {
            format = "dd-MM-yyyy";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            sResults = sdf.format(ca1.getTime());
        } catch (Exception var7) {
            sResults = "00:00 01-01-1900";
        }

        return sResults;
    }

    public static void copyFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            //file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }

    public static File[] getListOfFiles_v4(String path, String... sufix) {

        File fileList = new File(path);
        File[] files = fileList.listFiles(new GenericExtFilter(sufix));
        //
        if (files != null) {
            Arrays.sort(files);
        }
        //
        return files;
    }

    private static class GenericExtFilter implements FilenameFilter {
        private String[] exts;

        public GenericExtFilter(String... exts) {
            this.exts = exts;
        }

        @Override
        public boolean accept(File dir, String name) {
            for (String ext : exts) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static void call_Location_Tracker(Context context) {
        if (!SV_LocationTracker.status) {
            Intent mIntent = new Intent(context, SV_LocationTracker.class);
            context.startService(mIntent);
        }
    }

    public static void stop_Location_Tracker(Context context) {
        if (SV_LocationTracker.status) {
            Intent mIntent = new Intent(context, SV_LocationTracker.class);
            context.stopService(mIntent);
        }
    }

    public static void writeIn(String data, File file) throws IOException {
        FileWriter writer = new FileWriter(file, true);
        writer.append(data);
        writer.close();
    }

    public static String getDateHourStr() {
        //
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        String curr_date = formater.format(new Date());
        //
        formater = new SimpleDateFormat("HHmmss");
        String curr_time = formater.format(new Date());
        //
        String dateHour = curr_date + "_" + curr_time;

        return dateHour;
    }

    public static void registerException(String local, Exception exception) {

        File exception_file = new File(Constant.SUPPORT_PATH, "excep_" + getDateHourStr() + ".txt");

        try {

            StackTraceElement[] stackTrace = exception.getStackTrace();
            String traceString = "";
            String erro = "";

            for (StackTraceElement trace : stackTrace) {
                traceString += trace.toString() + "\n ";
            }

            erro = "Local:\n " + local + ";\nException:\n " + exception.toString() + ";\nTrace:\n" + traceString + ";";

            ToolBox_Inf.writeIn(erro, exception_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Metodo que esconde teclado quando o foco esta no campo passado como parametro.
     *
     * @param c           - Contexto da tela
     * @param windowToken - EditText.getWindowToken()
     */
    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void clearFilesByPrefix(String path, String prefix) {
        //File fileList = new File(Constant.CACHE_PATH);
        File fileList = new File(path);
        //
        if (fileList.isDirectory()) {
            String[] children = fileList.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].startsWith(prefix)) {
                    try {
                        new File(fileList, children[i]).delete();
                    } catch (Exception e) {
                        ToolBox_Inf.registerException(CLASS_NAME, e);
                    }
                }
            }
        }
    }

    public static boolean verifyImgIntegrity(String path, String file_name) {

        File file = new File(path + "/" + file_name);

        if (file.isFile()) {
            try {
                Bitmap tmpImg = BitmapFactory.decodeFile(path + "/" + file_name);
                if (tmpImg != null) {
                    return true;
                }
            } catch (Exception e) {
                registerException(CLASS_NAME, e);
                return false;
            }
        }

        return false;
    }

    public static Integer mIntegerParse(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (Exception e) {
            return null;
        }
    }

    public static void alertBundleNotFound(final Base_Activity act, HMAux hmAux_Trans) {
        //
        Exception e = new Exception("Bundle parameters not found.");
        //
        ToolBox_Inf.registerException(act.getClass().getName(), e);
        //
        ToolBox.alertMSG(
                act,
                hmAux_Trans.get("alert_bundle_not_found_ttl"),
                hmAux_Trans.get("alert_bundle_not_found_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent(act, Act005_Main.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        act.startActivity(mIntent);
                        act.finish();
                    }
                },
                0
        );
    }

    public static void alertBundleNotFound(final Base_Activity_Frag act, HMAux hmAux_Trans) {
        //
        Exception e = new Exception("Bundle parameters not found.");
        //
        ToolBox_Inf.registerException(act.getClass().getName(), e);
        //
        ToolBox.alertMSG(
                act,
                hmAux_Trans.get("alert_bundle_not_found_ttl"),
                hmAux_Trans.get("alert_bundle_not_found_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent(act, Act005_Main.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        act.startActivity(mIntent);
                        act.finish();
                    }
                },
                0
        );
    }


    public static void setSSmValue(SearchableSpinner ss_component, String code, String desc, boolean source_val) {
        HMAux hmAux = new HMAux();
        hmAux.put(SearchableSpinner.ID, code);
        hmAux.put(SearchableSpinner.DESCRIPTION, desc);
        ss_component.setmValue(hmAux);
        if (source_val) {
            ss_component.setTag(code);
        }
    }

    public static boolean processoOthersError(Context context, String error_msg_header, String error_msg) {
        if (error_msg != null && error_msg.length() > 0) {
            //ToolBox.sendBCStatus(context, "ERROR_1", error_msg_header + "\n" + error_msg, "", "0");
            ToolBox.sendBCStatus(context, "CUSTOM_ERROR", error_msg_header + "\n" + error_msg, "", "0");
            return false;
        }
        return true;
    }

    /**
     * @param ss_component
     * @param code         - Codigo interno do server(code)
     * @param desc         - Descrição do item
     * @param source_val   - Seta esse code, como valor default no sppiner
     * @param acceptNull   - Seta tag indicanda se spinner aceita valor null
     */
    public static void setSSmValue(SearchableSpinner ss_component, String code, String desc, boolean source_val, boolean acceptNull) {
        try {
            HMAux hmAux = new HMAux();
            if (code != null && code != "null") {
                hmAux.put(SearchableSpinner.ID, code);
                hmAux.put(SearchableSpinner.DESCRIPTION, desc);
            }
            //ss_component.setmValue(hmAux);
            ss_component.setmValue(hmAux, source_val);
            //
            if (source_val) {
                ss_component.setTag(code);
            }
            //
            ss_component.setTag(R.id.SS_NULLS_ACCEPT, String.valueOf(acceptNull));
            //
        } catch (Exception e) {
            registerException(CLASS_NAME, e);
            e.printStackTrace();
        }
    }

    /**
     * Verifica se o item de profile passado existe
     *
     * @param context
     * @param menu_code
     * @param param_code
     * @return
     */
    public static boolean profileExists(Context context, String menu_code, String param_code) {
        try {
            EV_ProfileDao evProfileDao
                    = new EV_ProfileDao(context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM);
            //
            EV_Profile profile
                    = evProfileDao.getByString(
                    new EV_Profile_Sql_001(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            menu_code,
                            param_code
                    ).toSqlQuery()
            );

            if (profile != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static String getColumnsToHmAux(String[] columns) {
        if (columns.length > 0) {
            return Arrays.toString(columns).replace("[", "").replace("]", "").replace(",", "#").replace(" ", "");
        } else {
            return "";
        }
    }

    public static void setServiceStatusColor(Context context, TextView tv_status, String status) {
        switch (status) {
            case Constant.SO_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SO_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SO_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            default:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
        }
    }


    public static void setTaskStatusColor(Context context, TextView tv_status, String status) {
        switch (status) {
            case Constant.SO_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SO_STATUS_PROCESS:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SO_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SO_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SO_STATUS_NOT_EXECUTED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
                break;
            case Constant.SO_STATUS_INCONSISTENT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
                break;
            default:
                break;
        }
    }


    public static void setExecStatusColor(Context context, TextView tv_status, String status) {
                /*
        * Tratativa de cor por Status
        * */
        switch (status) {
            case Constant.SO_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SO_STATUS_PROCESS:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SO_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SO_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SO_STATUS_NOT_EXECUTED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
                break;
            case Constant.SO_STATUS_INCONSISTENT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
                break;
            default:
                break;
        }
    }

    public static void setSOStatusColor(Context context, TextView tv_status, String status) {

        switch (status) {
            case Constant.SO_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SO_STATUS_PROCESS:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SO_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SO_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SO_STATUS_BLOCKED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_black));
                break;
            case Constant.SO_STATUS_WAITING_BUDGET:
            case Constant.SO_STATUS_WAITING_QUALITY:
            case Constant.SO_STATUS_WAITING_CLIENT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_brown));
                break;
            case Constant.SO_STATUS_EDIT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_pink_1));
                break;
            case Constant.SO_STATUS_WAITING_SYNC:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_dark_blue));
                break;
            default:
                break;

        }

    }

    public static int convertStringToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void cleanUpApproval(Context context, SM_SODao sm_soDao) {
        String approval_type = ToolBox_Con.getApproval_Type(context);
        //
        sm_soDao.remove(
                new SM_SO_Sql_014(approval_type).toSqlQuery()
        );
        //
        ToolBox_Con.setApproval_Type(context, "");
    }

    public static void showDialogInfo(Context context, SM_SO_Service sm_so_service, View viewInclude) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toolbox_dialog_info, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.toolbox_dialog_info_tv_title);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.toolbox_dialog_info_ll);
        Button btn_ok = (Button) view.findViewById(R.id.toolbox_dialog_info_btn_ok);

        if (viewInclude != null) {
            ll.addView(viewInclude);
        }

        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }

    public static void deleteFileListExceptionSafe(ArrayList<File> filesToDeleteList) {
        for (File file : filesToDeleteList) {
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception e) {
                    ToolBox_Inf.registerException(CLASS_NAME, e);
                    continue;
                }
            }
        }
    }

    public static void deleteFileListExceptionSafe(File[] filesToDeleteList) {
        for (File file : filesToDeleteList) {
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception e) {
                    ToolBox_Inf.registerException(CLASS_NAME, e);
                    continue;
                }
            }
        }
    }

    public static void deleteFileListExceptionSafe(String path, String prefix) {
        File[] filesToDeleteList = getListOfFiles_v5(path, prefix);

        for (File file : filesToDeleteList) {
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception e) {
                    ToolBox_Inf.registerException(CLASS_NAME, e);
                    continue;
                }
            }
        }
    }

    public static void deleteFileListExceptionSafe(String path, String sFiles, String separatorChar) {
        if (sFiles == null || sFiles.isEmpty()) {
            return;
        }

        String _path = path != null ? path : Constant.CACHE_PATH_PHOTO;
        String[] filesToDeleteList = sFiles.split(separatorChar != null ? separatorChar : "#");

        File file;

        for (String _file : filesToDeleteList) {
            file = new File(_path + "/" + _file);
            //
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception e) {
                    ToolBox_Inf.registerException(CLASS_NAME, e);
                    continue;
                }
            }
        }
    }

    public static String getWebSocketJsonParam(String socket_arg) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            Chat_Obj obj = gson.fromJson(socket_arg, Chat_Obj.class);
            //
            if (obj != null) {
                return obj.getObj().toString();
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return null;
        }
        return null;
    }

    public static String setWebSocketJsonParam(Object emit_param) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();

            Chat_Obj chatObj = new Chat_Obj();

            chatObj.setObj(gson.toJsonTree(emit_param));

            if (chatObj.getObj() != null) {
                return gson.toJson(chatObj);
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return null;
        }
        return null;
    }
    public static String getRoomObjJsonParam(String socket_arg) {
        try {
            JSONObject jsonObject = new JSONObject(socket_arg);
           /* Iterator<String> key = jsonObject.keys();
            String key_name = key.next();
            JSONObject obj = jsonObject.getJSONObject(key_name);*/
            JSONObject obj = jsonObject.getJSONObject(jsonObject.keys().next());
            //
            if (obj != null) {
                return obj.toString();
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return null;
        }
        return null;
    }

    public static boolean addJsonObjAsHmAuxKey(List<HMAux> hmAuxList, String key) {
        boolean ret = true;
        //
        for (HMAux hmAux : hmAuxList) {
            boolean hasError = addJsonObjAsHmAuxKey(hmAux, key);
            if (ret) {
                ret = hasError;
            }
        }
        return ret;
    }

    public static boolean addJsonObjAsHmAuxKey(HMAux hmAux, String key) {
        JSONObject json = null;
        //
        try {
            json = new JSONObject(String.valueOf(hmAux.get(key)));
            //
            if (json.length() > 0) {
                Iterator<String> root = json.keys();

                if (root.hasNext()) {
                    JSONObject innerJson = json.getJSONObject(root.next());

                    for (Iterator<String> iter = innerJson.keys(); iter.hasNext(); ) {
                        String json_key = iter.next();

                        String json_new_key = key + "_" + json_key;

                        hmAux.put(json_new_key, innerJson.getString(json_key));
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (JSONException e) {
            //registerException(CLASS_NAME,e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addJsonStringAsHmAux(HMAux hmAux, String value) {
        JSONObject json = null;
        //
        try {
            json = new JSONObject(value);
            //
            if (json.length() > 0) {

                for (Iterator<String> iter = json.keys(); iter.hasNext(); ) {
                    String json_key = iter.next();
                    hmAux.put(json_key, json.getString(json_key));
                }
                return true;

            } else {
                return false;
            }
        } catch (JSONException e) {
            //registerException(CLASS_NAME,e);
            e.printStackTrace();
            return false;
        }
    }


    public static boolean createThumbNail_Images(String path, String original) {

        try {
            File image = new File(path + "/" + original);

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getPath(), bounds);
            if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
                return false;

            int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                    : bounds.outWidth;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = originalSize / 512;

            Bitmap imgFinal = BitmapFactory.decodeFile(image.getPath(), opts);

            File file = new File(Constant.THU_PATH + "/" + original.replace(".jpg", "") + "_thumb.jpg");

            if (file.exists()) {
                file.delete();
            }

            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

            imgFinal.compress(Bitmap.CompressFormat.PNG, 25, os);

            os.flush();
            os.close();

            return true;

        } catch (Exception e) {
            registerException(CLASS_NAME, e);
            return false;
        }

    }

    synchronized public static long chatNextMSGCode(Context context) {

        long nextID = ToolBox_Con.getPreference_Chat_Msg_Code(context);
        //
        /*String mPrefix = yearMonthPrefix();
        //
        if (mPrefix.equalsIgnoreCase(ToolBox_Con.getPreference_Chat_Msg_Prefix(context))) {
            ToolBox_Con.setPreference_Chat_Msg_Code(context, ++nextID);
            //Log.d("Chat","NEXT_TMP ->" + String.valueOf(nextID));
            return nextID;
        } else {
            ToolBox_Con.setPreference_Chat_Msg_Prefix(context, mPrefix);
            Log.d("Chat", "NEXT_TMP ->" + String.valueOf(nextID));
            return 101L;
        }*/
        ToolBox_Con.setPreference_Chat_Msg_Code(context, ++nextID);
        return nextID;
    }

    synchronized public static long chatNextMSGToken(Context context) {
        long nextToken = ToolBox_Con.getPreference_Chat_Msg_Token(context);
        //
        ToolBox_Con.setPreference_Chat_Msg_Token(context, ++nextToken);
        return nextToken;
    }

    public static String yearMonthPrefix() {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        //
        Calendar cAux = Calendar.getInstance();
        //
        return sdf.format(cAux.getTime());*/
        //Como não existirá mais prefix temporario, retorna 0
        //Remover a chamada desse metodo após modificação da pk
        return "0";
    }

    public static void sendBRChat(Context context, String type) {
        Intent mIntent = new Intent(Constant.CHAT_BR_FILTER);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(Constant.CHAT_BR_TYPE, type);

        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
    }

    public static void sendBRChat(Context context, String type, HMAux param) {
        Intent mIntent = new Intent(Constant.CHAT_BR_FILTER);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(Constant.CHAT_BR_TYPE, type);
        mIntent.putExtra(Constant.CHAT_BR_PARAM, (Serializable) param);

        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
    }

    public static void sendBRChatDownloadUpdate(Context context, HMAux param) {
        Intent mIntent = new Intent(Constant.CHAT_BR_FILTER_DOWNLOAD);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
        mIntent.putExtra(Constant.CHAT_BR_PARAM, (Serializable) param);

        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
    }

    public static String lPad(int qtd, int msg) {
        return String.format("%0" + qtd + "d", msg);

    }


    public static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static void showChatNotification(Context context, String type, String attempt) {
        showChatNotification(context, type, attempt, "", "");
    }

    //
//    public static void showChatNotification(Context context, String type, String attempt, String title, String message) {
//        //
//        HMAux hmAux_trans = null;
//
//        List<String> translateList = new ArrayList<>();
//        //
//        hmAux_trans = ToolBox_Inf.setLanguage(
//                context,
//                Constant.APP_MODULE,
//                ToolBox_Inf.getResourceCode(
//                        context,
//                        Constant.APP_MODULE,
//                        "sys"
//                ),
//                ToolBox_Con.getPreference_Translate_Code(context),
//                translateList
//        );
//        //
//        if (hmAux_trans == null || hmAux_trans.size() == 0) {
//            //Necessidade de incluir arquivo de String ?!
//        }
//        //
//        NotificationManager nm = (NotificationManager)
//                context.getSystemService(NOTIFICATION_SERVICE);
//        //
//        RemoteViews view =
//                new RemoteViews(context.getPackageName(), R.layout.notification_chat_msg);
//        //
//        Intent mIntent = new Intent(context, NotificationReceiver.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent pi = PendingIntent.getBroadcast(
//                context,
//                0,
//                mIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );
//
//        try {
//            switch (type) {
//                case Constant.CHAT_NOTIFICATION_TYPE_MESSAGE:
//                    CH_RoomDao roomDao = new CH_RoomDao(context);
//                    //
//                    HMAux msgInfo =
//                            roomDao.getByStringHM(
//                                    new Sql_Chat_Notification_001(
//                                            ToolBox_Con.getPreference_User_Code(context)
//                                    ).toSqlQuery()
//                            );
//                    //
//                    if (msgInfo != null && msgInfo.size() > 0) {
//                        view.setImageViewResource(R.id.notification_chat_msg_iv_icon, R.drawable.ic_chat_24x24);
//                        if (
//                                msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM).equals("1") &&
//                                        msgInfo.get(Sql_Chat_Notification_001.QTY_MSG).equals("1")
//                                ) {
//                            view.setTextViewText(
//                                    R.id.notification_chat_msg_tv_msg_1,
//                                    msgInfo.get(Sql_Chat_Notification_001.LAST_ROOM) + " " + hmAux_trans.get("notification_user_says_lbl")
//                            );
//                            HMAux msgAux = getChatMsgContent(msgInfo.get(Sql_Chat_Notification_001.LAST_MSG));
//                            //
//                            view.setTextViewText(
//                                    R.id.notification_chat_msg_tv_msg_2,
//                                    msgAux.get("type").equals("TEXT") ? msgAux.get("data") : msgAux.get("type")
//                            );
//
//                        } else {
//                            view.setTextViewText(
//                                    R.id.notification_chat_msg_tv_msg_1,
//                                    msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM) + " " + hmAux_trans.get("notification_rooms_lbl")
//                            );
//                            view.setTextViewText(
//                                    R.id.notification_chat_msg_tv_msg_2,
//                                    msgInfo.get(Sql_Chat_Notification_001.QTY_MSG) + " " + hmAux_trans.get("notification_messages_lbl")
//                            );
//                        }
//
//                    }
//                    break;
//                case Constant.CHAT_NOTIFICATION_TYPE_RECONNECTING:
//                    view.setImageViewResource(R.id.notification_chat_msg_iv_icon, R.drawable.sync_notification_animation);
//                    view.setTextViewText(
//                            R.id.notification_chat_msg_tv_msg_1,
//                            hmAux_trans.get("chat_no_connecton_lbl")
//                    );
//                    view.setTextViewText(
//                            R.id.notification_chat_msg_tv_msg_2,
//                            hmAux_trans.get("chat_reconnection_attempt") + "  " + attempt
//                    );
//                    break;
//
//                case Constant.CHAT_NOTIFICATION_TYPE_CHAT:
//                    view.setImageViewResource(R.id.notification_chat_msg_iv_icon, R.drawable.ic_chat_24x24);
//                    view.setTextViewText(
//                            R.id.notification_chat_msg_tv_msg_1,
//                            //title
//                            hmAux_trans.get("chat_fcm_offline_ttl")
//                    );
//                    view.setTextViewText(
//                            R.id.notification_chat_msg_tv_msg_2,
//                            //message,
//                            hmAux_trans.get("chat_fcm_offline_msg")
//                    );
//
//                    break;
//            }
//
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//            //builder.setSmallIcon(type.equals(Constant.CHAT_NOTIFICATION_TYPE_MESSAGE) ? R.mipmap.ic_namoa : R.drawable.sync_notification_animation);
//            builder.setAutoCancel(true);
//            builder.setContent(view);
//            //builder.setCustomBigContentView(view);
//
//            if (type.equals(Constant.CHAT_NOTIFICATION_TYPE_MESSAGE) || type.equals(Constant.CHAT_NOTIFICATION_TYPE_CHAT)) {
//                // builder.setSound(alarmSound);
//                builder.setSound(Uri.parse("android.resource://"
//                        + context.getPackageName() + "/" + R.raw.morfador));
//
//                builder.setContentIntent(pi);
//
//                builder.setSmallIcon(R.drawable.ic_chat_24x24);
//            } else {
//                builder.setSmallIcon(R.drawable.sync_notification_animation);
//            }
//            Notification notification = builder.build();
//            //
//            nm.notify(Constant.NOTIFICATION_CHAT_MSG, notification);
//
//        } catch (Exception e) {
//            registerException(CLASS_NAME, e);
//        }
//
//    }
    public static void showChatRoomNotification(Context context) {
        HMAux hmAux_trans = null;
        List<String> translateList = new ArrayList<>();
        //
        hmAux_trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                ToolBox_Inf.getResourceCode(
                        context,
                        Constant.APP_MODULE,
                        "sys"
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
        if (hmAux_trans == null || hmAux_trans.size() == 0) {
            //Necessidade de incluir arquivo de String ?!
        }
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_chat_24x24);
        builder.setColor(context.getResources().getColor(R.color.namoa_color_success_green));
        // builder.setSound(alarmSound);
        builder.setSound(Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.morfador));
        builder.setContentTitle(hmAux_trans.get("notification_add_room_ttl"));
        builder.setContentText(hmAux_trans.get("notification_add_room_msg"));

        Notification notification = builder.build();
        //
        nm.notify(Constant.NOTIFICATION_CHAT_ROOM, notification);
    }

    public static void cancelChatRoomNotification(Context context) {
        cancelNotification(context, Constant.NOTIFICATION_CHAT_ROOM);
    }

    public static void showChatNotification(Context context, String type, String attempt, String title, String message) {
        //
        boolean show_notification = false;
        HMAux hmAux_trans = null;
        List<String> translateList = new ArrayList<>();
        //
        hmAux_trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                ToolBox_Inf.getResourceCode(
                        context,
                        Constant.APP_MODULE,
                        "sys"
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
        //
        if (hmAux_trans == null || hmAux_trans.size() == 0) {
            //Necessidade de incluir arquivo de String ?!
        }
        //
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent mIntent = new Intent(context, NotificationReceiver.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_chat_24x24);
        builder.setColor(context.getResources().getColor(R.color.namoa_color_success_green));
        // builder.setSound(alarmSound);
        builder.setSound(Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.morfador));
        builder.setContentIntent(pi);
        try {
            switch (type) {
                case Constant.CHAT_NOTIFICATION_TYPE_MESSAGE:
                    CH_RoomDao roomDao = new CH_RoomDao(context);
                    //
                    HMAux msgInfo =
                            roomDao.getByStringHM(
                                    new Sql_Chat_Notification_001(
                                            ToolBox_Con.getPreference_User_Code(context)
                                    ).toSqlQuery()
                            );
                    //
                    if (msgInfo != null && msgInfo.size() > 0) {
                        if (msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM).equals("0") ||
                                msgInfo.get(Sql_Chat_Notification_001.QTY_MSG).equals("0")) {
                            show_notification = false;
                        } else {
                            show_notification = true;

                            if (Act035_Main.mRoom_code.equalsIgnoreCase(msgInfo.get("room_code"))){
                                return;
                            }

                            if (msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM).equals("1")) {
                                mIntent = new Intent(context, NotificationReceiver.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mIntent.putExtra("room_code", msgInfo.get("room_code"));

                                pi = PendingIntent.getBroadcast(
                                        context,
                                        0,
                                        mIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );

                                builder.setContentIntent(pi);
                            } else {
                            }

                            if (
                                    msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM).equals("1") &&
                                            msgInfo.get(Sql_Chat_Notification_001.QTY_MSG).equals("1")
                                    ) {
                                builder.setContentTitle(
                                        msgInfo.get(Sql_Chat_Notification_001.LAST_ROOM) + " " + hmAux_trans.get("notification_user_says_lbl")
                                );
                                HMAux msgAux = getChatMsgContent(msgInfo.get(Sql_Chat_Notification_001.LAST_MSG));
                                //
                                switch (msgAux.get("type")) {
                                    case Constant.CHAT_MESSAGE_TYPE_TEXT:
                                        builder.setContentText(
                                                msgAux.get("data")
                                        );
                                        break;
                                    case Constant.CHAT_MESSAGE_TYPE_TRANSLATE:
                                        String transMsg = "";
                                        if (msgAux.get(Constant.CHAT_MESSAGE_TYPE_TRANSLATE) != null) {
                                            transMsg =
                                                    msgAux.get("data").replace(
                                                            msgAux.get(Constant.CHAT_MESSAGE_TYPE_TRANSLATE) + "|",
                                                            hmAux_trans.get(msgAux.get(Constant.CHAT_MESSAGE_TYPE_TRANSLATE))
                                                    );
                                        } else {
                                            transMsg = Constant.CHAT_MESSAGE_TYPE_TRANSLATE;
                                        }
                                        //
                                        builder.setContentText(transMsg);
                                        //
                                        break;
                                    default:
                                        builder.setContentText(
                                                hmAux_trans.get(
                                                        msgAux.get("type")
                                                )
                                        );
                                }

                            } else {
                                builder.setContentTitle(
                                        msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM) + " " + hmAux_trans.get("notification_rooms_lbl")
                                );

                                builder.setContentText(
                                        msgInfo.get(Sql_Chat_Notification_001.QTY_MSG) + " " + hmAux_trans.get("notification_messages_lbl")
                                );
                            }
                        }
                    }
                    break;
                case Constant.CHAT_NOTIFICATION_TYPE_CHAT:
                    show_notification = true;
                    //view.setImageViewResource(R.id.notification_chat_msg_iv_icon, R.drawable.ic_chat_24x24);
                    builder.setContentTitle(
                            hmAux_trans.get("chat_fcm_offline_ttl")
                    );
                    builder.setContentText(
                            hmAux_trans.get("chat_fcm_offline_msg")
                    );

                    break;
                default:
                    show_notification = false;
            }
            if (show_notification) {
                Notification notification = builder.build();
                //
                nm.notify(Constant.NOTIFICATION_CHAT_MSG, notification);
            }

        } catch (Exception e) {
            registerException(CLASS_NAME, e);
        }

    }

    public static void cancelChatNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(Constant.NOTIFICATION_CHAT_MSG);
    }

    public static HMAux getChatMsgContent(String msg_obj) {
        HMAux hmAux = new HMAux();
        //
        try {
            JSONObject jsonObj = new JSONObject(msg_obj);
            JSONObject jsonMsg = jsonObj.getJSONObject("message");
            hmAux.put("type", String.valueOf(jsonMsg.getString("type")));
            hmAux.put("data", String.valueOf(jsonMsg.getString("data")));
            if (String.valueOf(jsonMsg.getString("type")).equals(Constant.CHAT_MESSAGE_TYPE_TRANSLATE)) {
                try {
                    String[] translation =
                            String.valueOf(jsonMsg.getString("data"))
                                    .replace("|", Constant.MAIN_CONCAT_STRING)
                                    .split(Constant.MAIN_CONCAT_STRING);
                    hmAux.put(Constant.CHAT_MESSAGE_TYPE_TRANSLATE, translation[0]);
                } catch (Exception e) {
                    hmAux.put(Constant.CHAT_MESSAGE_TYPE_TRANSLATE, Constant.CHAT_MESSAGE_TYPE_TRANSLATE);
                }
            }
            return hmAux;
        } catch (Exception e) {
            registerException(CLASS_NAME, e);
            return hmAux;
        }
    }

    /**
     * Valida se usr tem as preferencias de user, customer e session preenchidas
     *
     * @param context
     * @return
     */
    public static boolean isUsrAppLogged(Context context) {
        boolean logged =
                !ToolBox_Con.getPreference_User_Code(context).equals("")
                        && ToolBox_Con.getPreference_Customer_Code(context) != -1
                        && !ToolBox_Con.getPreference_Session_App(context).equals("");
        return logged;
    }

    public static boolean equalDate(String dtStart, String dtEnd) {
        try {
            String sDtStart[] = dtStart.split(" ");
            String sDtEnd[] = dtEnd.split(" ");

            if (sDtStart[0].equalsIgnoreCase(sDtEnd[0])) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isToday_Yesterday(long date, boolean today) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        if (today) {
            now.add(Calendar.DATE, 0);
        } else {
            now.add(Calendar.DATE, -1);
        }

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static String getSafeSubstring(String s, int maxLength) {
        if (!TextUtils.isEmpty(s)) {
            if (s.length() >= maxLength) {
                return s.substring(0, maxLength) + " ...";
            }
        }
        return s;
    }

    public static String getBreakNewLine(String s) {
        String[] lines = s.split("\\r?\\n");
        //
        if (lines != null && lines.length > 0) {
            return lines[0];
        } else {
            return "";
        }
    }

    public static void cleanRoom_RoomMessages(Context context) {
        CH_RoomDao mRoomDao = new CH_RoomDao(context);

        ArrayList<File> imagesList = new ArrayList<>();
        //
        ArrayList<HMAux> mRooms = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Room_Sql_008().toSqlQuery()
        );

        for (HMAux aux : mRooms) {
            imagesList.add(new File(Constant.CACHE_CHAT_PATH + "/" + aux.get(CH_RoomDao.ROOM_IMAGE_LOCAL)));
            imagesList.add(new File(Constant.THU_PATH + "/" +
                    aux.get(CH_RoomDao.ROOM_IMAGE_LOCAL).substring(0, aux.get(CH_RoomDao.ROOM_IMAGE_LOCAL).length() - 4) +
                    Constant.THUMB_SUFFIX + ".jpg"));
        }

        ArrayList<HMAux> mRoomsMessagesImages = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Message_Sql_023().toSqlQuery()
        );

        for (HMAux aux : mRoomsMessagesImages) {
            imagesList.add(new File(Constant.CACHE_PATH_PHOTO + "/" + aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL)));
            imagesList.add(new File(Constant.THU_PATH + "/" +
                    aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).substring(0, aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).length() - 4) +
                    Constant.THUMB_SUFFIX + ".jpg"));
        }

        // Delete Images from Room and Messages of this Room
        boolean detect = true;
        int i = 0;
        try {
            while (detect) {
                if (imagesList.get(i).getName().contains("ch_user.jpg") ||
                        imagesList.get(i).getName().contains("ch_user_thumb.jpg") ||
                        imagesList.get(i).getName().contains("ch_image-so.jpg") ||
                        imagesList.get(i).getName().contains("ch_image-so_thumb.jpg")
                        ) {
                    imagesList.remove(i);
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
        }

        deleteFileListExceptionSafe(imagesList);

        for (HMAux aux : mRooms) {
            // Remove Messages of this Room
            mRoomDao.remove(
                    new CH_Message_Sql_022(
                            aux.get(CH_RoomDao.ROOM_CODE)
                    ).toSqlQuery()
            );
            // Remove Room
            mRoomDao.remove(new CH_Room_Sql_004(
                            aux.get(CH_RoomDao.ROOM_CODE)
                    ).toSqlQuery()
            );
        }

        mRoomDao.addUpdate(new CH_Room_Sql_007(
                ).toSqlQuery()
        );
    }

    public static void cleanRoom_RoomMessages(Context context, CH_Room ch_room) {
        CH_RoomDao mRoomDao = new CH_RoomDao(context);

        ArrayList<File> imagesList = new ArrayList<>();
        //
        imagesList.add(new File(Constant.CACHE_CHAT_PATH + "/" + ch_room.getRoom_image_local()));
//        imagesList.add(new File(Constant.THU_PATH + "/" +
//                ch_room.getRoom_image_local().substring(0, ch_room.getRoom_image_local().length() - 4) +
//                Constant.THUMB_SUFFIX + ".jpg"));
        //
        ArrayList<HMAux> msgImages = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Message_Sql_020(
                        ch_room.getRoom_code()
                ).toSqlQuery()
        );
        //
        for (HMAux aux : msgImages) {
            imagesList.add(new File(Constant.CACHE_PATH_PHOTO + "/" + aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL)));
//            imagesList.add(new File(Constant.THU_PATH + "/" +
//                    aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).substring(0, aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).length() - 4) +
//                    Constant.THUMB_SUFFIX + ".jpg"));
        }

        // Delete Images from Room and Messages of this Room
        boolean detect = true;
        int i = 0;
        try {
            while (detect) {
                if (imagesList.get(i).getName().contains("ch_user.jpg") ||
                        imagesList.get(i).getName().contains("ch_user_thumb.jpg") ||
                        imagesList.get(i).getName().contains("ch_image-so.jpg") ||
                        imagesList.get(i).getName().contains("ch_image-so_thumb.jpg")
                        ) {
                    imagesList.remove(i);
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
        }

        deleteFileListExceptionSafe(imagesList);

        // Remove Messages of this Room
        mRoomDao.remove(
                new CH_Message_Sql_022(
                        ch_room.getRoom_code()
                ).toSqlQuery()
        );
        // Remove Room
        mRoomDao.remove(new CH_Room_Sql_004(
                        ch_room.getRoom_code()
                ).toSqlQuery()
        );
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isOn = pm.isScreenOn();

        return isOn;
    }

    public static boolean isUsrAdmin(Context context) {
        EV_UserDao userDao = new EV_UserDao(context);
        //
        EV_User ev_user = userDao.getByString(
                new EV_User_Sql_001(
                        ToolBox_Con.getPreference_User_Code(context)
                ).toSqlQuery()
        );
        //
        return ev_user != null && ev_user.getAdmin() == 1;
    }

    public static void showChatAdminInfo(Context context, HMAux hmAuxTrans) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_admin_dialog, null);
        //
        TextView tv_service_status = (TextView) view.findViewById(R.id.chat_admin_dialog_tv_service_status);
        TextView tv_socket_status = (TextView) view.findViewById(R.id.chat_admin_dialog_tv_socket_status);
        TextView tv_socket_logged = (TextView) view.findViewById(R.id.chat_admin_dialog_tv_socket_logged);
        TextView tv_socket_id = (TextView) view.findViewById(R.id.chat_admin_dialog_tv_socket_id);
        //
        tv_service_status.setText(AppBackgroundService.isRunning ? "Rodando" : "Parado");
        tv_socket_status.setText(SingletonWebSocket.isSocketSetted() ? "Setado" : "Nullo");
        tv_socket_logged.setText(SingletonWebSocket.ismSocketLogged() ? "Logado" : "Deslogado");
        tv_socket_id.setText(SingletonWebSocket.isSocketSetted() ? SingletonWebSocket.mSocket.id() : "Socket não setado");
        //
        builder
                .setTitle("Debug Info Chat")
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static int colorIndex = 0;

    private static int userColors[] = {
            0xFFFF0000,
            0xFF868A08,
            0xFF40FF00,
            0xFF01DFA5,
            0xFF01A9DB,
            0xFF0101DF,
            0xFFA901DB,
            0xFFDF01A5,
            0xFFFF0040,
            0xFFFF8000
    };

    public static int userColor() {
        if (colorIndex >= userColors.length) {
            colorIndex = 0;
        }
        //
        return userColors[colorIndex++];
    }

    public static String AccentMapper(String string){
        //
        if (string == null) {
            return "";
        }
        //
        StringBuilder sb = new StringBuilder(string);
        //
        for(int i = 0; i < string.length(); i++) {
            Character c = ACCENT_MAP.get(sb.charAt(i));
            if(c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }

        return sb.toString();
    }

}