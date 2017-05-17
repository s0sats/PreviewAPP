package com.namoadigital.prj001.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.Ev_User_Customer_ParameterDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.fcm.WS_Notification_Sync;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.Ev_User_Customer_Parameter;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.receiver.WBR_AL_Full;
import com.namoadigital.prj001.receiver.WBR_AL_Quarter;
import com.namoadigital.prj001.receiver.WBR_Cleanning;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_UpdateSoftware;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.receiver.WBR_Upload_Support;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Sql_002;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Trans_Sql_002;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_006;
import com.namoadigital.prj001.sql.Ev_User_Customer_Parameter_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_010;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_013;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_014;
import com.namoadigital.prj001.sql.MD_Operation_Sql_002;
import com.namoadigital.prj001.sql.MD_Site_Sql_001;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_003;
import com.namoadigital.prj001.ui.act001.Act001_Main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by neomatrix on 09/01/17.
 */

public class ToolBox_Inf {

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

        return carrierID;
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

    public static String uploadFile(String json, String sFile) {
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Constant.CACHE_PATH_PHOTO + "/" + sFile);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload(Constant.WS_UPLOAD, json);

            return hfu.Send_Now(fstrm, sFile);

        } catch (Exception e) {
            String error = e.toString();
            return "Error: " + e.toString();
        }
    }

    public static String uploadFileSupport(String ws_url, String json, String sPath, String sFile) {
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(sPath + "/" + sFile);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload(ws_url, json);

            return hfu.Send_Now(fstrm, sFile);

        } catch (Exception e) {
            String error = e.toString();
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
        File file = new File(Constant.CACHE_PATH + "/" + sName);

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

    public static boolean verifyDownloadFileInf(String sName) {
        File file = new File(Constant.CACHE_PATH + "/", sName);

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

        }

        return text.toString();
    }

    public static void sendBCStatus(Context context, String type, String value, String link, String required) {
        Intent mIntent = new Intent(Constant.SW_TYPE_BR);
        mIntent.addCategory(Intent.CATEGORY_DEFAULT);

        mIntent.putExtra(Constant.SW_TYPE, type);
        mIntent.putExtra(Constant.SW_VALUE, value);
        mIntent.putExtra(Constant.SW_LINK, link != null ? link : "");
        mIntent.putExtra(Constant.SW_REQUIRED, required != null ? required : 0);

        context.sendBroadcast(mIntent);
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
                case "OK":
                    break;
                case "NFC_BLOCKED":
                    sendBCStatus(context, "ERROR_1",context.getString(R.string.msg_nfc_card_blocked) /*context.getString(R.string.msg_user_canceled)*/, s_Link, "0");
                    return false;

                default:
                    sendBCStatus(context, "ERROR_1",context.getString(R.string.msg_unespected_error), s_Link, "0");
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
                sendBCStatus(context, "VERSION_ERRO", error_msg, s_Link, "1");

                return false;

            case "VERSION_INVALID":
                sendBCStatus(context, "VERSION_INVALID", error_msg, s_Link, "1");

                return false;

            case "EXPIRED":
                sendBCStatus(context, "EXPIRED", error_msg, s_Link, "1");

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

            default:
                break;
        }

        return true;
    }

    public static boolean processWSCheckValidationNFCAuth(Context context, String validation, String error_msg, String s_Link, String ret, String ret_error) {

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

            default:
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
        ;

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

    public static HMAux loadFooterDialogInfo(Context context) {
        HMAux hmAux = new HMAux();
        String customerDesc = "";
        String siteDesc = "";
        String operationDesc = "";

        List<String> transList = new ArrayList<>();
        transList.add("lbl_external_site");
        transList.add("footer_dialog_customer_lbl");
        transList.add("footer_dialog_site_lbl");
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
        hmAux.put(Constant.FOOTER_IMEI, ToolBox_Inf.uniqueID(context));

        return hmAux;

    }

    public static String getCustomerLogoPath(Context context) {

        return Constant.CACHE_PATH + "/logo_c_" + ToolBox_Con.getPreference_Customer_Code(context) + ".png";

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
                title = hmAux_Trans.get("notification_ttl_download");
                msg = hmAux_Trans.get("notification_msg_download");
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
            e.printStackTrace();
        }

        return calendar.getTimeInMillis();
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

    public static void writeIn(String data , File file) throws IOException {
        FileWriter writer =  new FileWriter(file,true);
        writer.append(data);
        writer.close();
    }

    public static String getDateHourStr(){
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

    public static void registerException(String local, String exception) {

        File exception_file = new File(Constant.SUPPORT_PATH,"excep_" + getDateHourStr() + ".txt");

        try {
            ToolBox_Inf.writeIn(local + ";\n" +exception,exception_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}