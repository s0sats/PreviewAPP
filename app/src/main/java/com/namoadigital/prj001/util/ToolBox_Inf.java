package com.namoadigital.prj001.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Field_Local;
import com.namoadigital.prj001.receiver.WBR_UpdateSoftware;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Sql_002;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Trans_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_003;
import com.namoadigital.prj001.ui.act001.Act001_Main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

        return androidId_UUID.toString();
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
            FileInputStream fstrm = new FileInputStream(Constant.CACHE_PATH + "/" + sFile);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload(Constant.WS_UPLOAD, json);

            return hfu.Send_Now(fstrm, sFile);

        } catch (Exception e) {
            String error = e.toString();
            return "Error: " + e.toString();
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

                default:
                    break;
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
            List<GE_Custom_Form_Field_Local> pendingPictures = fieldLocalDao.query(
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

}