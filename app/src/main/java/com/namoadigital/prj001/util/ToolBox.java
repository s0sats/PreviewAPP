package com.namoadigital.prj001.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.WSValidationResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by neomatrix on 09/01/17.
 */

public class ToolBox {

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

    /**
     * Analyse WS returns and return a list of validationStatus.
     *
     * @param version_returns The ws version status returns
     * @param login_returns   The ws login status returns
     * @param licence_returns The ws licence status returns
     * @return
     */
    private static List<WSValidationResult> checkWsValidation(String version_returns, String login_returns, String licence_returns) {
        List<WSValidationResult> validationResultList = new ArrayList<>();
        WSValidationResult validationVersion;
        WSValidationResult validationLogin;
        WSValidationResult validationLicence;

        switch (version_returns) {
            case "STABLE":
                validationVersion = new WSValidationResult("OK", "OK", "0", true);
                break;
            case "UPDATE_REQUIRED":
                validationVersion = new WSValidationResult("VERSION", version_returns, "1", true);
                break;
            case "VERSION_ERRO":
                validationVersion = new WSValidationResult("VERSION", version_returns, "2", false);
                break;
            case "VERSION_INVALID":
                validationVersion = new WSValidationResult("VERSION", version_returns, "3", false);
                break;
            case "VERSION_EXPIRED":
                validationVersion = new WSValidationResult("VERSION", version_returns, "4", false);
                break;
            default:
                validationVersion = new WSValidationResult("NOK", "NOK", "NOK", false);
        }
        //Add Version obj validatation into the list
        validationResultList.add(validationVersion);
        //
        switch (login_returns) {
            case "OK":
                validationLogin = new WSValidationResult("OK", "OK", "5", true);
                break;
            case "LOGIN_ERRO":
                validationLogin = new WSValidationResult("LOGIN", login_returns, "6", false);
                break;
            case "USER_INVALID":
                validationLogin = new WSValidationResult("LOGIN", login_returns, "7", false);
                break;
            case "USER_BLOCKED":
                validationLogin = new WSValidationResult("LOGIN", login_returns, "8", false);
                break;
            case "USER_CANCELLED":
                validationLogin = new WSValidationResult("LOGIN", login_returns, "9", false);
                break;
            case "USER_OTHER_DEVICE":
                validationLogin = new WSValidationResult("LOGIN", login_returns, "10", true);
                break;
            default:
                validationLogin = new WSValidationResult("NOK", "NOK", "NOK", false);
        }
        //Add Login obj validatation into the list
        validationResultList.add(validationLogin);

        switch (licence_returns) {
            case "OK":
                NULL:
                validationLicence = new WSValidationResult("OK", "OK", "OK", true);
                break;
            case "NOK":
                validationLicence = new WSValidationResult("LICENCE", licence_returns, "NOK", false);
                break;
            default:
                validationLicence = new WSValidationResult("NOK", "NOK", "NOK", false);
        }
        //Add Licence obj validatation into the list
        validationResultList.add(validationLicence);

        return validationResultList;
    }

    /**
     * Process the WS validation returns
     *
     * @param ignoreVersion   Set 1 if the loop needs to ignore version verification
     * @param version_returns The ws version status returns
     * @param login_returns   The ws login status returns
     * @param licence_returns The ws licence status returns
     * @return A HmAux with 2 values:
     * Texto_1 : Validation status 0, 1, 2, 3;
     * Texto_2 : Validation mensage , when exists;
     */
    public static HMAux processWsValidation(int ignoreVersion, String version_returns, String login_returns, String licence_returns) {
        HMAux hmAux = new HMAux();
        List<WSValidationResult> validationList = checkWsValidation(version_returns, login_returns, licence_returns);
        //Loop through validation list.The ignoreVersion var, define where the loop starts
        for (int i = ignoreVersion; i < validationList.size(); i++) {
            WSValidationResult objAux = validationList.get(i);
            //If type is ok, updates HmAux and jump to next item
            if (objAux.getType().equals("OK")) {
                hmAux.put(HMAux.TEXTO_01, "0");
                hmAux.put(HMAux.TEXTO_02, "OK");
                continue;
            }
            //If type isn't ok, but the item is valid,
            //so it's a situation we have to treat specifically.
            //Those situation are:
            //App Version requires update;
            //The user login is already logged in other device.
            if (objAux.isValid()) {
                hmAux.put(HMAux.TEXTO_01, (objAux.getType().equals("VERSION") ? "1" : "2"));
                hmAux.put(HMAux.TEXTO_02, objAux.getMsg());
                return hmAux;
            } else {
                //Any other situation means an "abort" error.
                hmAux.put(HMAux.TEXTO_01, "3");
                hmAux.put(HMAux.TEXTO_02, objAux.getMsg());
                return hmAux;
            }
        }
        return hmAux;
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
}