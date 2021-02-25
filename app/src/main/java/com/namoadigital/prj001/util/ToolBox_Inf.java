package com.namoadigital.prj001.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.ctls.FabMenuItem;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.BuildConfig;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_ProfileDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.Ev_User_Customer_ParameterDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_SiteDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.fcm.WS_Notification_Sync;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_Obj;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.EV_Profile;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.Ev_User_Customer_Parameter;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSO_Save_Env;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.model.TSerial_Save_Env;
import com.namoadigital.prj001.model.T_IO_Inbound_Item_Env;
import com.namoadigital.prj001.model.T_IO_Outbound_Item_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Save_Env;
import com.namoadigital.prj001.receiver.NotificationReceiver;
import com.namoadigital.prj001.receiver.WBR_UpdateSoftware;
import com.namoadigital.prj001.receiver.WBR_Upload_Support;
import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_020;
import com.namoadigital.prj001.sql.CH_Message_Sql_022;
import com.namoadigital.prj001.sql.CH_Message_Sql_023;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.CH_Room_Sql_004;
import com.namoadigital.prj001.sql.CH_Room_Sql_007;
import com.namoadigital.prj001.sql.CH_Room_Sql_008;
import com.namoadigital.prj001.sql.CH_Room_Sql_010;
import com.namoadigital.prj001.sql.CH_Room_Sql_011;
import com.namoadigital.prj001.sql.CH_Room_Sql_012;
import com.namoadigital.prj001.sql.CH_Room_Sql_014;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Sql_002;
import com.namoadigital.prj001.sql.EV_Module_Res_Txt_Trans_Sql_002;
import com.namoadigital.prj001.sql.EV_Profile_Sql_001;
import com.namoadigital.prj001.sql.EV_Profile_Sql_002;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_006;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_007;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_008;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_010;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_011;
import com.namoadigital.prj001.sql.EV_User_Sql_001;
import com.namoadigital.prj001.sql.Ev_User_Customer_Parameter_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_010;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_011;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_006;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_010;
import com.namoadigital.prj001.sql.GE_File_Sql_001;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_006;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_013;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_005;
import com.namoadigital.prj001.sql.IO_Outbound_Sql_013;
import com.namoadigital.prj001.sql.MD_Operation_Sql_002;
import com.namoadigital.prj001.sql.MD_Operation_Sql_SS;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_015;
import com.namoadigital.prj001.sql.MD_Product_Serial_x_TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Sql_Footer;
import com.namoadigital.prj001.sql.MD_Site_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.sql.SM_SO_Sql_014;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_010;
import com.namoadigital.prj001.sql.Sql_Act002_001;
import com.namoadigital.prj001.sql.Sql_Act005_007;
import com.namoadigital.prj001.sql.Sql_Act005_008;
import com.namoadigital.prj001.sql.Sql_Act021_003;
import com.namoadigital.prj001.sql.Sql_Act070_005;
import com.namoadigital.prj001.sql.Sql_Act070_008;
import com.namoadigital.prj001.sql.Sql_Chat_Notification_001;
import com.namoadigital.prj001.sql.Sql_Form_x_Operation;
import com.namoadigital.prj001.sql.Sql_Form_x_Product;
import com.namoadigital.prj001.sql.Sql_Form_x_Site;
import com.namoadigital.prj001.sql.Sql_Notification_Schedule_001;
import com.namoadigital.prj001.sql.Sql_Notification_Schedule_002;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_001;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_003;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_004;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_007;
import com.namoadigital.prj001.ui.AppBase;
import com.namoadigital.prj001.ui.act001.Act001_Main;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act077.Act077_Main;
import com.namoadigital.prj001.ui.act078.Act078_Main;
import com.namoadigital.prj001.ui.act079.Act079_Main;
import com.namoadigital.prj001.ui.act080.Act080_Main;
import com.namoadigital.prj001.worker.Work_Chat_Refresh;
import com.namoadigital.prj001.worker.Work_Cleanning_Data;
import com.namoadigital.prj001.worker.Work_DownLoad_Customer_Logo;
import com.namoadigital.prj001.worker.Work_DownLoad_PDF;
import com.namoadigital.prj001.worker.Work_DownLoad_Picture;
import com.namoadigital.prj001.worker.Work_Firebase_ID_Report;
import com.namoadigital.prj001.worker.Work_Firebase_Registration;
import com.namoadigital.prj001.worker.Work_Four_Hour_Schedule_Notification;
import com.namoadigital.prj001.worker.Work_Quarter_Chat_Refresh;
import com.namoadigital.prj001.worker.Work_Quarter_Schedule_Notification;
import com.namoadigital.prj001.worker.Work_Upload_Img;
import com.namoadigital.prj001.worker.Work_Upload_Img_Chat;
import com.namoadigital.prj001.worker.Work_Upload_Other_User_Img;

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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.namoadigital.prj001.dao.EV_User_CustomerDao.LICENSE_CONTROL_TYPE_CONCURRENT_GLOBAL_LEVEL;
import static com.namoadigital.prj001.ui.AppBase.NAMOA_NOTIF_INFO;
import static com.namoadigital.prj001.ui.AppBase.NAMOA_PEND_INFO;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_SERVICE_MODE;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_SERVICE_MODE_ACTIVED;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_CANCEL;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_IMEI;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_OK;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_USER_LEVEL_LBL;
import static com.namoadigital.prj001.util.ConstantBaseApp.FOOTER_VERSION_LBL;
import static com.namoadigital.prj001.util.ConstantBaseApp.GENERIC_CHANNEL_ID;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_NC;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_SCORE;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MEASURE;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE;
import static com.namoadigital.prj001.util.ToolBox_Con.isHostAvailable;

/**
 * Created by neomatrix on 09/01/17.
 */

public class ToolBox_Inf {

    private static final String CLASS_NAME = "com.namoadigital.prj001.util.ToolBox_Inf";
    public static final String RANGE_RED = "RANGE_RED";
    public static final String RANGE_YELLOW = "RANGE_YELLOW";
    public static final String RANGE_GREEN = "RANGE_GREEN";


//    private static final Map<Character, Character> ACCENT_MAP = initAccentMap();
//
//    private static Map<Character, Character> initAccentMap() {
//        Map<Character, Character> map = new HashMap<Character, Character>();
//        //
//        map.put('à', 'a');
//        map.put('á', 'a');
//        map.put('â', 'a');
//        map.put('ã', 'a');
//        map.put('ä', 'a');
//        map.put('å', 'a');
//        map.put('ç', 'c');
//        map.put('č', 'c');
//        map.put('ć', 'c');
//        map.put('è', 'e');
//        map.put('é', 'e');
//        map.put('ê', 'e');
//        map.put('ë', 'e');
//        map.put('ì', 'i');
//        map.put('í', 'i');
//        map.put('î', 'i');
//        map.put('ï', 'i');
//        map.put('ñ', 'n');
//        map.put('ò', 'o');
//        map.put('ó', 'o');
//        map.put('ô', 'o');
//        map.put('õ', 'o');
//        map.put('ö', 'o');
//        map.put('ø', 'o');
//        map.put('ß', 's');
//        map.put('§', 's');
//        map.put('ù', 'u');
//        map.put('ú', 'u');
//        map.put('û', 'u');
//        map.put('ü', 'u');
//        map.put('ÿ', 'y');
//        //
//        return map;
//    }

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

        File dirCamTest = new File(Constant.CAM_TEST_PATH);
        if (!dirCamTest.exists()) {
            dirCamTest.mkdir();
        }
        //23/08/2018 - Diretorio da apk baixada.
        File dirApk = new File(Constant.APK_PATH);
        if (!dirApk.exists()) {
            dirApk.mkdir();
        }
        //10/05/2019 - Diretorio da apk baixada.
        File dirUnsetImg = new File(Constant.UNSENT_IMG_PATH);
        if (!dirUnsetImg.exists()) {
            dirUnsetImg.mkdir();
        }
        //LUCHE - 04/12/2020 - Dir com os arquivos json de edição do ticket
        File dirTicketJsonFile = new File(Constant.TICKET_JSON_PATH);
        if (!dirTicketJsonFile.exists()) {
            dirTicketJsonFile.mkdir();
        }
        //BARRIONUEVO - 06/01/2020 - Dir com os arquivos json de lista de site licença do customer
        File dirCustomerSiteLicenseJsonFile = new File(Constant.CUSTOMER_SITE_LICENSE_JSON_PATH);
        if (!dirCustomerSiteLicenseJsonFile.exists()) {
            dirCustomerSiteLicenseJsonFile.mkdir();
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

    /**
     * LUCHE - 06/02/2020
     *
     * Modificado metodo que pega IMEI, adicionando try /catch e retornando NULL em caso de exception
     *
     * @param context
     * @return - String com IMEI ou NULL em caso de exception
     */
      /*
                BARRIONUEVO  29-06-2020
                Projeto de atualizacao do app para target 10, devido a qtde de pontos onde esta per
                missao deve ser tratada, foi decidido nao usar mais o IMEI.
             */
//    @Nullable
//    private static String CarrierInfo(Context context) {
//        try {
//            TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//            //
//
//            return tm.getDeviceId();
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
    /**
     * LUCHE - 06/02/2020
     *
     * Novo metodo para geração do UUID.
     * Gera UUID baseado no Settings.Secure.ANDROID_ID (SSAI)
     *
     * @param context
     * @return - String com UUID ou NULL em caso de exception
     */
    @Nullable
    private static String getPhoneUUID(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
            //
            UUID androidId_UUID = UUID
                .nameUUIDFromBytes(androidId.getBytes(StandardCharsets.UTF_8));
            //
            return androidId_UUID.toString();
        }catch (Exception e){
            /**
             *O QUE FAZER SE EXCEPTION AQUI ?!
             */
            e.printStackTrace();
            registerException(CLASS_NAME, e);
            return null;
        }

    }

    /**
     * LUCHE - 06/02/2020
     *
     * Nova implementação do metodo uniqueID que retorna a preferencia PHONE_UNIQUE_ID caso "exista"
     * Se não houver valor no PHONE_UNIQUE_ID, tenta pegar IMEI e , em caso de falha, gera UUID.
     * O valor gerado é salvo na preferencia PHONE_UNIQUE_ID.
     *
     * @param context
     * @return - Preferencia PHONE_UNIQUE_ID
     */
    @Nullable
    public static String uniqueIDv2(Context context){
        Log.d("IMEI", "uniqueIDv2");
        String phone_uuid_code = ToolBox_Con.getPreference_PHONE_UNIQUE_ID(context);
        //Se preferencia setada, a retorna
        Log.d("IMEI", "getPreference_PHONE_UNIQUE_ID phone_uuid_code: " + phone_uuid_code);
        if(phone_uuid_code.trim().length() != 0){
            return phone_uuid_code;
        }else{
            /*
                BARRIONUEVO  29-06-2020
                Projeto de atualizacao do app para target 10, devido a qtde de pontos onde esta per
                missao deve ser tratada, foi decidido nao usar mais o IMEI.
             */
            //Se não tem preferencia, pegar IMEI
//            phone_uuid_code = CarrierInfo(context);
            //Se não IMEI não retornado, tenta gerar UUID
//            if(phone_uuid_code == null || phone_uuid_code.trim().isEmpty() ){
            phone_uuid_code = getPhoneUUID(context);
//            }
            Log.d("IMEI", " getPhoneUUID phone_uuid_code: " + phone_uuid_code);
            //Grava valor na preferencia
            ToolBox_Con.setPreference_PHONE_UNIQUE_ID(context, phone_uuid_code != null ? phone_uuid_code : "");
            //Retorna preferencia.
            return ToolBox_Con.getPreference_PHONE_UNIQUE_ID(context);
        }
    }

    /**
     * LUCHE - 06/02/2020
     *
     * Modificado metodo , comentando a implementação original e retornando a implementação o valor
     * de uniqueIDv2.
     *
     * @param context
     * @return - Retorna valor da preferencia PHONE_UNIQUE_ID OU null.
     */
    @Nullable
    public static String uniqueID(Context context) {
        /*String carrierID = null;
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
        }*/
        return uniqueIDv2(context);
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

    /**
     * Luche - 10/05/2019
     *
     * Metodo para upload de imagem que foram movidas para pasta UnsentImgs
     *
     * @param json - Json do Envio
     * @param filePath - String com o caminho do diretorio pasta onde o arquivo esta localizado
     * @param fileName - String com o nome do arquivo
     * @param sNewName - Novo nome, quando houver
     * @return
     */
    public static String uploadFileUnsentImg(String json, String filePath, String fileName,@Nullable String sNewName) {
        try {
            //Como no processo de SO a foto pode mudar de nome,
            //Verifica qual o nome o arquivo esta no momento.
            String sRealFileName = sNewName != null ? sNewName : fileName;
            FileInputStream fstrm = new FileInputStream(filePath + "/" + sRealFileName);
            HttpFileUpload hfu = new HttpFileUpload(Constant.WS_UPLOAD, json);
            return hfu.Send_Now(fstrm, fileName);

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
        try {
            File dir = new File(sDir);
            //
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }catch (Exception e){
            registerException(CLASS_NAME,e);
        }
    }

    public static void deleteDownloadFile(String sName) {
        try {
            File file = new File(sName);

            if (file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            registerException(CLASS_NAME, e);
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
        //Alterado 13/11/2018 - Luche
        //Add try catch pois se path ou sName for null gera exception
        try {
            File file = new File(path + "/", sName);

            return file.exists();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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

    public static boolean processWSCheck_GC(Context context, String sVersion, String sLogin, String s_Link, int iStatus, int iStatus_OD,Integer db_version) {
        if (sVersion != null) {
            switch (sVersion) {
                case "VERSION_INVALID":
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_VERSION_INVALID, context.getString(R.string.msg_version_invalid), s_Link, "1");
                    return false;

                case "EXPIRED":
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_EXPIRED, context.getString(R.string.msg_version_expired), s_Link, "1");
                    return false;

                case ConstantBaseApp.MAIN_RESULT_UPDATE_REQUIRED:
                    if (iStatus == 0) {
                        //sendBCStatus(context, "UPDATE_REQUIRED", context.getString(R.string.msg_update_required), s_Link, "0");
                        checkNewDbVersion(context,db_version);
                        //
                        ToolBox.sendBCStatus(
                            context,
                            ConstantBase.PD_TYPE_UPDATE_REQUIRED,
                            context.getString(R.string.msg_update_required),
                            s_Link,
                            "0"
                        );
                        return false;
                    } else {
                        break;
                    }
                case ConstantBaseApp.MAIN_RESULT_UPDATE_REQUIRED_WARNING:
                    if (iStatus == 0) {
                        //
                        HMAux hmAux = checkNewDbVersionV2(context, db_version);
                        //
                        if(hmAux == null || hmAux.size() == 0) {
                            ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_UPDATE_REQUIRED, context.getString(R.string.msg_update_required), s_Link, "0");
                        }else {
                            ToolBox
                                .sendBCStatus(context,
                                    ConstantBase.PD_TYPE_UPDATE_REQUIRED_WARNING,
                                    context.getString(R.string.msg_update_required_warning),
                                    hmAux,
                                    s_Link,
                                    "0"
                                );
                        }
                        //
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
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_USER_INVALID, context.getString(R.string.msg_user_invalid), s_Link, "0");
                    return false;

                case "USER_CANCELLED":
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_USER_CANCELLED, context.getString(R.string.msg_user_canceled), s_Link, "0");
                    return false;

                case "USER_OTHER_DEVICE":
                    if (iStatus_OD == 0) {
                        ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_USER_OTHER_DEVICE, context.getString(R.string.msg_user_other_device), s_Link, "0");
                        return false;
                    } else {
                        return true;
                    }

                case "NFC_BLOCKED":
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_1, context.getString(R.string.msg_nfc_card_blocked) /*context.getString(R.string.msg_user_canceled)*/, s_Link, "0");
                    return false;

                case "DEVICE_CODE_REQUIRED":
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_1, context.getString(R.string.msg_device_code_not_found), s_Link, "0");
                    return false;

                case "OK":
                    break;
                default:
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_1, context.getString(R.string.msg_unespected_error), s_Link, "0");
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
        /**
         * LUCHE - 18/12/2018
         * Para implementar a nova funcionalidade que informa o usr quando esse irá de fato perder os dados
         * devido a atualização da versão do banco de dados, foi criada uma segunda assinatura desse metodo
         * adicionando como ultimo parametro a versão do banco de dados da versão stable.
         */
        return processWSCheckValidation(context, validation, error_msg, s_Link,iStatus, iStatus_OD,0);
//        validation = validation == null ? "" : validation;
//
//        switch (validation) {
//            case "OK":
//                break;
//
//            case "UPDATE_REQUIRED":
//                if (iStatus == 0) {
//                    sendBCStatus(context, "UPDATE_REQUIRED", context.getString(R.string.msg_update_required), s_Link, "0");
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case "VERSION_ERRO":
//                sendBCStatus(context, "VERSION_ERRO", context.getString(R.string.msg_version_invalid), s_Link, "1");
//
//                return false;
//
//            case "VERSION_INVALID":
//                sendBCStatus(context, "VERSION_INVALID", context.getString(R.string.msg_version_invalid), s_Link, "1");
//
//                return false;
//
//            case "EXPIRED":
//                sendBCStatus(context, "EXPIRED", context.getString(R.string.msg_version_expired), s_Link, "1");
//
//                return false;
//
//            case "LOGIN_ERRO":
//                sendBCStatus(context, "LOGIN_ERRO", error_msg, s_Link, "0");
//
//                return false;
//
//            case "USER_INVALID":
//                sendBCStatus(context, "USER_INVALID", error_msg, s_Link, "0");
//
//                return false;
//
//            case "USER_BLOCKED":
//                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
//
//                return false;
//
//            case "USER_CANCELLED":
//                sendBCStatus(context, "USER_CANCELLED", error_msg, s_Link, "0");
//
//                return false;
//
//            case "USER_OTHER_DEVICE":
//                if (iStatus_OD == 0) {
//                    sendBCStatus(context, "USER_OTHER_DEVICE", error_msg, s_Link, "0");
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case "SESSION_NOT_FOUND":
//                sendBCStatus(context, "ERROR_3", error_msg, s_Link, "0");
//                return false;
//
//            case "CREATE_SESSION_ABORT":
//                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
//                return false;
//
//            case "LICENSE_QTY_INVALID":
//                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
//                return false;
//            case "PARAMETERS_ERROR":
//                sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
//                return false;
//            case "CUSTOMER_IP_REQUIRED":
//                ToolBox.sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
//                return false;
//            case "CUSTOMER_IP_RESTRICTION":
//                ToolBox.sendBCStatus(context, "ERROR_1", error_msg, s_Link, "0");
//                return false;
//            default:
//                if (validation.trim().length() == 0) {
//                    return processoOthersError(context, context.getResources().getString(R.string.generic_error_lbl), error_msg);
//                }
//                break;
//        }
//
//        return true;
    }

    /**
     * LUCHE - 18/12/2018
     *
     * Criado nova assinatura do metodo para receber parametro adicional db_version e verificar
     * a necessidade de exibir ou não a msg de que dados serão perdidos.
     *
     * @param context
     * @param validation
     * @param error_msg
     * @param s_Link
     * @param iStatus    - Se deve validar update_required.0 valida , 1 não valida
     * @param iStatus_OD - Se deve validar forced_login.0 valida , 1 não valida
     * @param db_version - Versão do banco de dados do app Stable. Usada pra gerar ou não msg de perda de dados.
     * @return
     */

    public static boolean processWSCheckValidation(Context context, String validation, String error_msg, String s_Link, int iStatus, int iStatus_OD, Integer db_version) {
        validation = validation == null ? "" : validation;

        switch (validation) {
            case "OK":
                break;

            case "UPDATE_REQUIRED":
                if (iStatus == 0) {
                    //sendBCStatus(context, "UPDATE_REQUIRED", context.getString(R.string.msg_update_required), s_Link, "0");
                    checkNewDbVersion(context,db_version);
                    //
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_UPDATE_REQUIRED, context.getString(R.string.msg_update_required), s_Link, "0");
                    return false;
                } else {
                    return true;
                }

            case "VERSION_ERRO":
            case "VERSION_INVALID":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_VERSION_INVALID, context.getString(R.string.msg_version_invalid), s_Link, "1");
                return false;

            case "EXPIRED":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_EXPIRED, context.getString(R.string.msg_version_expired), s_Link, "1");
                return false;

            case "LOGIN_ERRO":
            case "USER_INVALID":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_USER_INVALID, error_msg, s_Link, "0");
                return false;

            case "USER_CANCELLED":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_USER_CANCELLED, error_msg, s_Link, "0");
                return false;

            case "USER_OTHER_DEVICE":
                if (iStatus_OD == 0) {
                    ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_USER_OTHER_DEVICE, error_msg, s_Link, "0");
                    return false;
                } else {
                    return true;
                }

            case "SESSION_NOT_FOUND":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_3, error_msg, s_Link, "0");
                return false;

            case "USER_BLOCKED":
            case "CREATE_SESSION_ABORT":
            case "LICENSE_QTY_INVALID":
            case "PARAMETERS_ERROR":
            case "CUSTOMER_IP_REQUIRED":
            case "CUSTOMER_IP_RESTRICTION":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_1, error_msg, s_Link, "0");
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
            case "VERSION_INVALID":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_VERSION_INVALID, error_msg, s_Link, "1");
                return false;

            case "EXPIRED":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_EXPIRED, error_msg, s_Link, "1");
                return false;

            case "SESSION_NOT_FOUND":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_3, error_msg, s_Link, "0");
                return false;

            case "USER_BLOCKED":
            case "CREATE_SESSION_ABORT":
            case "LICENSE_QTY_INVALID":
            case "PARAMETERS_ERROR":
            case "CUSTOMER_IP_REQUIRED":
            case "CUSTOMER_IP_RESTRICTION":
                ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_1, error_msg, s_Link, "0");
                return false;

            default:
                if (validation.trim().length() == 0) {
                    return processoOthersError(context, context.getResources().getString(R.string.generic_error_lbl), error_msg);
                }
                break;
        }

        if (ret_error != null) {
            ToolBox.sendBCStatus(context, ConstantBase.PD_TYPE_ERROR_1, ret_error, s_Link, "0");
            return false;
        }

        return true;
    }

    /**
     * LUCHE - 26/02/2019 - Alteração
     *  Metodo que verifica se existe um troca de versão do db local e se existe dados pendentens de envio.
     *  Caso as duas condições sejam satisfeitas, adiciona msg de PERDA DE DADOS e seta preferencia do
     *  processo de apagar dados de token para 1
     *
     * LUCHE - 07/01/2020
     *  Após revisão do update_requied com troca de versão de banco e com dados pendentes,
     *  não será mais exibida msg em vermelho. Caso seja o msm usr, ele será avisado da nova versão
     *  e dos dados pendentes e não poderá atualizar o app.
     *  Esse metodo, agora, serve apenas para identificar a mudança de versão de banco e, nesse caso,
     *  verificar a necessidade de copiar imagens não enviadas para o dir de unsent imgs
     *
     * @param context - Contexto
     * @param db_version - Versão do banco de dados enviada pelo server.*
     */
    private static void checkNewDbVersion(Context context, Integer db_version){
        if(db_version != null && db_version > Constant.DB_VERSION_CUSTOM){
            //
            if(hasUnsentImgs(context)){
                //Se preferencia para checkar backup de imagens pra verdadeiro.
                ToolBox_Con.setPreference_BkpUnsentImg(context,true);
            }
        }
    }

    /**
     * LUCHE - 06/01/2020
     *
     * Criado nova versão do metodo checkNewDbVersion.
     * Após revisão no processo de update_requied, caso o usuario tenha dados pendentes
     * de envio ele NÃO PODERÁ ATUALIZAR O APP, no login.
     * Foi solicitado a exibição de uma nova mensagem informando que atualização só poderá ser feita
     * após os dados serem enviados e exbição de em quais customers existem dados pendentes.
     *
     * LUCHE - 22/01/2020
     *
     * Modificado chamada do metodo hasPendingDataV2, passando como segundo parametro lista de bancos
     * de dados locais referente a mesma versão do app instalado.
     * Em resumo, sera retornada a lista de banco dos customer que terminam com a versão atual do banco,
     * Constant.DB_VERSION_CUSTOM. Dessa forma, os bancos antigos com pendencia de envio,
     * não serão listados
     *
     *
     * @param context - Contexto
     * @param db_version - Versão do banco de dados enviada pelo server.
     * @return - HmAux com msg e lista de customer.
     */
    private static HMAux checkNewDbVersionV2(Context context, Integer db_version){
        HMAux aux = new HMAux();
        if(db_version != null && db_version > Constant.DB_VERSION_CUSTOM){
            //
            String customerPendencieList = hasPendingDataV2(context,getCurrentDbVersionDbList("C_"));
            //
            if( customerPendencieList != null) {
                //aux.put(Constant.LIB_DB_VERSION_MSG, context.getString(R.string.msg_not_sent_data_will_be_lost));
                aux.put(
                    Constant.LIB_DB_VERSION_MSG,
                    context.getString(R.string.msg_customer_pendencies_list) + customerPendencieList
                );
                //
                ToolBox_Con.setPreference_CleanTokenFiles(context, 1);
            }
            //
            if(hasUnsentImgs(context)){
                //Se preferencia para checkar backup de imagens pra verdadeiro.
                ToolBox_Con.setPreference_BkpUnsentImg(context,true);
            }
        }
        return aux;
    }

    /**
     * Verifica se existem imagens não enviadas em TODOS os bancos.
     * @param context
     * @return
     */
    public static boolean hasUnsentImgs(Context context){
        ArrayList<GE_File> geFiles = getUnsentGeFiles(context);
        return geFiles != null && geFiles.size() > 0;
    }

    /**
     * Busca em todos os bancos existentes se existens imagens pendentes
     * de envio e retorna lista as pendentes.
     * @param context
     * @return
     */
    private static ArrayList<GE_File> getUnsentGeFiles(Context context) {
        File[] dbList = getListDB("C_", true);
        ArrayList<GE_File> geFiles = new ArrayList<>();
        GE_FileDao geFileDao = null;
        //Gera lista de GeFiles
        for(File dbFile : dbList){
            String[] db_full_name = dbFile.getName().contains("_") ? dbFile.getName().split("_") : new String[]{};
            //Valida customer_code,pois o code esta sendo gerado pelo nome do arquivo.
            Long customer_code = db_full_name.length == 3 && db_full_name[1] != null && mLongParse(db_full_name[1]) != null ? mLongParse(db_full_name[1]) : -1L;
            //
            if(customer_code != null && customer_code != -1){
                geFileDao = new GE_FileDao(
                    context,
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                geFiles.addAll(geFileDao.query(
                    new GE_File_Sql_001().toSqlQuery()
                    )
                );
            }
        }
        //
        return geFiles;
    }

    /**
     * LUCHE - 13/05/2019
     * Metodo que move as imagens pendentes de envio para pasta de UnsentImgs.
     * @param context - Contexto
     * @return false se erro ao copiar alguma foto
     */
    public static boolean moveUnsentImgs(Context context) {
        ArrayList<GE_File> geFiles = new ArrayList<>();
        int errorCount = 0;
        //Recebe lista de imagens a serem enviadas.
        geFiles = getUnsentGeFiles(context);
        //CopiaArquivos
        for(GE_File geFile : geFiles){
            File fromFile = new File(ConstantBaseApp.CACHE_PATH_PHOTO,geFile.getFile_path());
            File toFile = new File(ConstantBaseApp.UNSENT_IMG_PATH);
            try {
                //Verifica se arquivo EXISTE na pasta origem e NÃO EXISTE na destino
                if( verifyDownloadFileInf(geFile.getFile_path(),fromFile.getParent())
                    && !verifyDownloadFileInf(geFile.getFile_path(),toFile.getParent())
                ) {
                    copyFile(fromFile, toFile);
//                    if(!fromFile.renameTo(toFile)){
//                        errorCount++;
//                    }
                }
            }catch (Exception e){
                registerException(CLASS_NAME,e);
                errorCount++;
            }
        }
        //Se itens na lista, chama serviço de envio
        if(geFiles.size() > 0){
//            Intent mIntent = new Intent(context,WBR_Upload_Other_User_Img.class);
//            context.sendBroadcast(mIntent);
            scheduleUploadOtherUserImgWork();
        }
        //Se contador de erro 0 , então sucesso.
        return errorCount == 0;
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
        NotificationCompat.Builder builder = getNotificationBuilder(context, nm);
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

    @NonNull
    public static NotificationCompat.Builder getNotificationBuilder(Context context, NotificationManager notificationManager) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(GENERIC_CHANNEL_ID);
            if(notificationChannel == null) {
                createChannelNotification(context, notificationManager, NAMOA_NOTIF_INFO, NotificationManager.IMPORTANCE_DEFAULT, GENERIC_CHANNEL_ID);
            }
            builder = new NotificationCompat.Builder(context, GENERIC_CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    public static void createChannelNotification(Context context, NotificationManager notificationManager, String channelName, int importanceDefault, String genericChannelId) {
        CharSequence name = channelName;
        int importance = importanceDefault;
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(genericChannelId, name, importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    public static NotificationCompat.Builder getLowImportanceBuilder(Context context, NotificationManager notificationManager) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(ConstantBaseApp.GENERIC_CHANNEL_ID);
            if (notificationChannel == null) {
                createChannelNotification(context, notificationManager, NAMOA_PEND_INFO, NotificationManager.IMPORTANCE_LOW, ConstantBaseApp.PENDENCY_CHANNEL_ID);
            }
            builder = new NotificationCompat.Builder(context, ConstantBaseApp.PENDENCY_CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    public static String getResourceCode(Context context, String module_code, String resource_name) {
        return getResourceCode(context, module_code, resource_name, ToolBox_Con.getPreference_Customer_Code(context));
    }

    /**
     * Metodo que retorna o Resource_code, baseado no Resource_name
     * Problema gerado quando notificação do chat era ativado sem o usuario estar com customer logado
     *
     * @param context
     * @param module_code
     * @param resource_name
     * @return
     */
    public static String getResourceCode(Context context, String module_code, String resource_name, long customer_code) {
        //Dao para buscar codigo do recurso
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(
                context,
                ToolBox_Con.customDBPath(customer_code),
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
        return setLanguage(context, module_code, resource_code, translate_code, translation_list, ToolBox_Con.getPreference_Customer_Code(context));
    }

    /**
     * Segunda assinatura com parametro de customer para evitar customer -1
     * Problema gerado quando notificação do chat era ativado sem o usuario estar com customer logado
     *
     * @param context
     * @param module_code
     * @param resource_code
     * @param translate_code
     * @param translation_list
     * @param customer_code
     * @return
     */
    public static HMAux setLanguage(Context context, String module_code, String resource_code, String translate_code, List<String> translation_list, long customer_code) {

        EV_Module_Res_Txt_TransDao transDao = new EV_Module_Res_Txt_TransDao(
                context,
                ToolBox_Con.customDBPath(customer_code),
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

    public static void showNoConnectionDialogWithInteraction(Context act_context, DialogInterface.OnClickListener listener) {
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
                listener,
                0
        );
    }

    /**
     * Metodo retorna o site do serviço tem restrição de execução.
     * A restrição de execução se dá caso o serviço possua um site e esse site seja diferente do
     * site logado.
     *
     * @param context
     * @param site_code   - Site do Serviço
     * @param hmAux_Trans - HmAux com as traduções
     * @return - Caso falso, exibe dialog informando que não é possivel executar a ação.
     */
    public static boolean hasServiceSiteRestriction(Context context, String site_code, HMAux hmAux_Trans) {
        boolean results = false;
        if (site_code == null || site_code.equalsIgnoreCase("null") || site_code.isEmpty()) {
            results = false;
        } else if (site_code.equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(context))) {
            results = false;
        } else {
            results = true;
        }
        //
        if (results) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("so_service_site_restriction_ttl"),
                    hmAux_Trans.get("so_service_site_restriction_msg"),
                    null,
                    0
            );
        }
        return results;
    }

    public static void buildFooterDialog(final Context context) {
        buildFooterDialog(context, false);
    }

    /**
     * LUCHE - 07/01/2021
     * Modificado metodo para adicionar info de site_licença selecionado
     * @param context
     * @param editMode
     */
    public static void buildFooterDialog(final Context context, boolean editMode) {

        HMAux hmDialogInfo = loadFooterDialogInfo(context);

        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.footer_dialog_info_app, null);
        LinearLayout ll_footer_close = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll);

        ImageView iv_customer = (ImageView) customView.findViewById(R.id.footer_dialog_app_iv_customer);
        //
        TextView tv_offline_lbl = (TextView) customView.findViewById(R.id.footer_dialog_tv_offline_lbl);
        Switch sw_offline = (Switch) customView.findViewById(R.id.footer_dialog_sw_offline);
        LinearLayout ll_offline_mode = (LinearLayout) customView.findViewById(R.id.footer_dialog_ll_offline_mode);
        //
        LinearLayout ll_customer = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_customer);
//        TextView tv_customer_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_customer_lbl);
        TextView tv_customer_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_customer_value);
        //LUCHE - 07/01/2021
        LinearLayout ll_site_license = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_site_license);
        TextView tv_site_license_desc = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_site_license_desc);
        //
        //BARRIONUEVO - 19/01/2021
        LinearLayout ll_global_level = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_global_level);
        TextView tv_user_global_level = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_user_global_level);
        //
        LinearLayout ll_site = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_site);
        TextView tv_site_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_site_lbl);
        TextView tv_site_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_site_value);
        final SearchableSpinner ss_site = customView.findViewById(R.id.footer_dialog_app_ss_site);
        //
        LinearLayout ll_zone = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_zone);
        TextView tv_zone_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_zone_lbl);
        TextView tv_zone_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_zone_value);
        final SearchableSpinner ss_zone = customView.findViewById(R.id.footer_dialog_app_ss_zone);
        //
        LinearLayout ll_operation = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_operation);
        TextView tv_operation_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_operation_lbl);
        TextView tv_operation_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_operation_value);
        final SearchableSpinner ss_operation = customView.findViewById(R.id.footer_dialog_app_ss_operation);
        //
        LinearLayout ll_imei = (LinearLayout) customView.findViewById(R.id.footer_dialog_app_ll_imei);
        TextView tv_imei_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_imei_lbl);
        TextView tv_imei_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_imei_value);
        //
        TextView tv_version_lbl = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_version_lbl);
        TextView tv_version_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_version_number);
        TextView tv_version_code_value = (TextView) customView.findViewById(R.id.footer_dialog_app_tv_version_number_code);
        //
        Button footer_dialog_app_action_cancel = customView.findViewById(R.id.footer_dialog_app_action_cancel);
        Button footer_dialog_app_action_ok = customView.findViewById(R.id.footer_dialog_app_action_ok);
        //
        Bitmap customer_img = getCustomerImage(ToolBox_Inf.getCustomerLogoPath(context));
        final Dialog customDialog = new Dialog(context);
        if(editMode){
            ll_site.setVisibility(View.GONE);
            ll_zone.setVisibility(View.GONE);
            ll_operation.setVisibility(View.GONE);
            ss_site.setVisibility(View.VISIBLE);
            ss_zone.setVisibility(View.VISIBLE);
            ss_operation.setVisibility(View.VISIBLE);
            footer_dialog_app_action_ok.setVisibility(View.VISIBLE);
            footer_dialog_app_action_cancel.setVisibility(View.VISIBLE);
            footer_dialog_app_action_ok.setText(hmDialogInfo.get(FOOTER_OK));
            footer_dialog_app_action_cancel.setText(hmDialogInfo.get(FOOTER_CANCEL));

            footer_dialog_app_action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                }
            });

            final String original_site = hmDialogInfo.get(Constant.FOOTER_SITE);
            final String original_zone = hmDialogInfo.get(Constant.FOOTER_ZONE);
            final String original_operation = hmDialogInfo.get(Constant.FOOTER_OPERATION);

            footer_dialog_app_action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result_ok = true;
                    boolean has_changes = false;
                    if(ss_site.getmValue()!= null && ss_site.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                        ss_site.setBackground(context.getResources().getDrawable(R.drawable.shape_ok));
                        has_changes = checkForChange(has_changes, original_site, ss_site);
                    }else{
                        result_ok = false;
                        ss_site.setBackground(context.getResources().getDrawable(R.drawable.shape_error));
                    }
                    //
                    if(ss_zone.getVisibility() == View.VISIBLE){
                        if (ss_zone.getmValue() != null && ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                            ss_zone.setBackground(context.getResources().getDrawable(R.drawable.shape_ok));
                            has_changes = checkForChange(has_changes, original_zone, ss_zone);
                        } else {
                            result_ok = false;
                            ss_zone.setBackground(context.getResources().getDrawable(R.drawable.shape_error));
                        }
                    }
                    //
                    if(ss_operation.getmValue()!= null && ss_operation.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                        ss_operation.setBackground(context.getResources().getDrawable(R.drawable.shape_ok));
                        has_changes = checkForChange(has_changes, original_operation, ss_operation);
                    }else{
                        ss_operation.setBackground(context.getResources().getDrawable(R.drawable.shape_error));
                        result_ok = false;
                    }

                    if(!has_changes){
                        customDialog.dismiss();
                    }else if(result_ok) {
                        ToolBox_Con.setPreference_Site_Code(context, ss_site.getmValue().get(SearchableSpinner.CODE));
                        if(ss_zone.getVisibility() == View.VISIBLE
                        && ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                            ToolBox_Con.setPreference_Zone_Code(context, Integer.parseInt(ss_zone.getmValue().get(SearchableSpinner.CODE)));
                        }
                        ToolBox_Con.setPreference_Operation_Code(context, Long.parseLong(ss_operation.getmValue().get(SearchableSpinner.CODE)));
                        Intent mIntent = new Intent(ToolBox.SW_TYPE_BR_REFRESH);
                        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        //
                        Bundle bundle = new Bundle();
                        bundle.putString(ConstantBaseApp.SW_TYPE, ConstantBase.MSG_SPECIAL_NOTIFICATION);
                        /*
                           BARRIONUEVO 17-04-2020
                           Parametriza a chamada do localbroadcast
                        */
                        if (context != null)
                        {
                            if (context instanceof Base_Activity_Frag)
                            {
                                bundle.putString(ToolBox.SW_ACTIVITY, "Base_Activity_Frag");
                            }else{
                                bundle.putString(ToolBox.SW_ACTIVITY, "Base_Activity");
                            }
                        }

                        mIntent.putExtras(bundle);
                        //
                        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
                        customDialog.dismiss();
                    }
                }
            });
        }else{
            footer_dialog_app_action_ok.setVisibility(View.GONE);
            footer_dialog_app_action_cancel.setVisibility(View.GONE);
            ll_site.setVisibility(View.VISIBLE);
            ll_zone.setVisibility(View.VISIBLE);
            ll_operation.setVisibility(View.VISIBLE);
            ss_site.setVisibility(View.GONE);
            ss_zone.setVisibility(View.GONE);
            ss_operation.setVisibility(View.GONE);
        }

        if (customer_img != null) {
            iv_customer.setImageBitmap(customer_img);
        } else {
            iv_customer.setImageBitmap(null);
            // Fazer Analise
        }
        //
        tv_offline_lbl.setText(hmDialogInfo.get(Constant.FOOTER_OFFLINE_MODE_LBL));

//        tv_customer_lbl.setText(hmDialogInfo.get(Constant.FOOTER_CUSTOMER_LBL));
        tv_customer_value.setText(hmDialogInfo.get(Constant.FOOTER_CUSTOMER));
        //region Licença por Site
        //LUCHE - 07/01/2021 - Add informação de licença por site quando customer usar essa configuração
        EV_User_Customer evUsrCustomer = getCurrentEvUsrCustomerInfo(context);
        //
        ll_site_license.setVisibility(View.GONE);
        ll_global_level.setVisibility(View.GONE);
        if(evUsrCustomer != null){
          if (evUsrCustomer.getLicense_site_code() != null && evUsrCustomer.getLicense_site_code() > 0){
                ll_site_license.setVisibility(View.VISIBLE);
                tv_site_license_desc.setText(
                        getSiteLicenseDescFormmated(context,evUsrCustomer.getLicense_site_desc(),evUsrCustomer.getLicense_user_level_id(),evUsrCustomer.getLicense_user_level_changed())
                );
          }
        //endregion
            //region  Global por nivel
            if (evUsrCustomer.getLicense_control_type() != null && evUsrCustomer.getLicense_control_type().equals(LICENSE_CONTROL_TYPE_CONCURRENT_GLOBAL_LEVEL)) {
                ll_global_level.setVisibility(View.VISIBLE);
                int textColor;
                if(evUsrCustomer.getLicense_user_level_changed() != null && evUsrCustomer.getLicense_user_level_changed() == 1){
                    textColor = R.color.namoa_color_danger_red;
                }else{
                    textColor = R.color.namoa_color_light_blue_lib;
                }

                tv_user_global_level.setText(getLabelValueColorFormmated(
                        context,
                        hmDialogInfo.get(FOOTER_USER_LEVEL_LBL),
                        evUsrCustomer.getLicense_user_level_id(),
                        ": ",
                        true,
                        textColor
                        )
                );
            }
            //endregion
        }

        if(editMode){
            setEnableUserInfo(context, hmDialogInfo, ss_site, ss_zone, ss_operation);
        }else {
            setDisableUserInfo(hmDialogInfo, ll_site, tv_site_lbl, tv_site_value, ll_zone, tv_zone_lbl, tv_zone_value, ll_operation, tv_operation_lbl, tv_operation_value);
        }

        tv_imei_lbl.setText(hmDialogInfo.get(Constant.FOOTER_IMEI_LBL));
        tv_imei_value.setText(hmDialogInfo.get(FOOTER_IMEI));
        ll_imei.setVisibility(hmDialogInfo.get(FOOTER_IMEI) == null || hmDialogInfo.get(FOOTER_IMEI).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_version_lbl.setText(hmDialogInfo.get(FOOTER_VERSION_LBL));
        tv_version_value.setText(Constant.PRJ001_VERSION);
        tv_version_code_value.setText("(" + Constant.PRJ001_VERSION_CODE + ")");

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dmW = (float) dm.widthPixels * 0.9f;
        float dmH = (float) dm.heightPixels * 0.9f;

        //customDialog = new Dialog(context, R.style.MyDialogTheme);

        customDialog.setContentView(customView);
        customDialog.getWindow().setLayout((int) dmW, (int) dmH);
        customDialog.show();

        ll_footer_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });


//        LUCHE - 12/08/2019
//        Comentado função de modo offline até que seja definido em conjunto como ele deverá funcionar

//        sw_offline.setChecked(ToolBox.getPreference_Offline_Mode(context));
//
//        sw_offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ToolBox.setPreference_Offline_Mode(context, isChecked);
//                if(context instanceof Base_Activity){
//                    ((Base_Activity) context).refreshOfflineModeUI();
//                } else if(context instanceof Base_Activity_Frag){
//                    ((Base_Activity_Frag) context).refreshOfflineModeUI();
//                }
//            }
//        });

    }

    public static EV_User_CustomerDao getEv_user_customerDao(Context context) {
        return new EV_User_CustomerDao(
                context,
                Constant.DB_FULL_BASE,
                Constant.DB_VERSION_BASE
        );
    }

    private static SpannableString getSiteLicenseDescFormmated(Context context, String license_site_desc, String license_user_level_id, Integer license_user_level_changed) {
        String siteDescInfo = license_site_desc + " / " + license_user_level_id;
        SpannableString spannableString = new SpannableString(siteDescInfo);
        //
        if(license_user_level_changed != null && license_user_level_changed == 1){
            spannableString.setSpan(
                new ForegroundColorSpan(context.getResources().getColor(R.color.namoa_color_danger_red)),
                (siteDescInfo.indexOf(" / " + license_user_level_id) + 2),
                siteDescInfo.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            );
        }
        //
        return spannableString;
    }

    private static SpannableString getLabelValueColorFormmated(Context context, String label, String value, String separator, boolean applyColor, int color) {
        String siteDescInfo = label + separator + value;
        SpannableString spannableString = new SpannableString(siteDescInfo);
        //
        if(applyColor){
            spannableString.setSpan(
                    new ForegroundColorSpan(context.getResources().getColor(color)),
                (siteDescInfo.indexOf(separator + value) + separator.length()),
                    siteDescInfo.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
            );
        }
        //
        return spannableString;
    }


    private static boolean checkForChange(boolean has_changes, String original_code, SearchableSpinner searchableSpinner) {
        String original_code_split[] = original_code.split(" - ");
        if (!searchableSpinner.getmValue().get(SearchableSpinner.CODE).equals(original_code_split[0])) {
            has_changes = true;
        }
        return has_changes;
    }

    private static void setEnableUserInfo(Context context, HMAux hmDialogInfo,SearchableSpinner ss_site,SearchableSpinner ss_zone, SearchableSpinner ss_operation) {
        setmCanClean(ss_site, ss_zone, ss_operation);
        setmSSAction(context, ss_site, ss_zone, ss_operation);
        //
        MD_SiteDao siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        ArrayList<HMAux> ssSiteOption = (ArrayList<HMAux>) siteDao.query_HM(
                new MD_Site_Sql_SS_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
        HMAux ssSiteValue = getCurrentmValue(hmDialogInfo.get(Constant.FOOTER_SITE), ssSiteOption);

        if(isConcurrentBySiteLicense(context)) {
            ssSiteOption = (ArrayList<HMAux>) getSiteLicenseAvailability(ssSiteOption, SearchableSpinner.RIGHT_ICON);
        }

        setSSs(ss_site, hmDialogInfo.get(Constant.FOOTER_SITE_LBL), ssSiteValue, ssSiteOption);
        //
        ss_site.setVisibility(hmDialogInfo.get(Constant.FOOTER_SITE) == null || hmDialogInfo.get(Constant.FOOTER_SITE).length() <= 0 ? View.GONE : View.VISIBLE);
        //
        ArrayList<HMAux> ssZoneOption = getSiteZoneOption(context, ssSiteValue);
        HMAux ssZoneValue = getCurrentmValue(hmDialogInfo.get(Constant.FOOTER_ZONE), ssZoneOption);

        String zone_lbl = hmDialogInfo.get(Constant.FOOTER_ZONE);
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
                || ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)) {
            ss_zone.setVisibility(View.VISIBLE);
            if (!zone_lbl.isEmpty()) {
                setSSs(ss_zone, hmDialogInfo.get(Constant.FOOTER_ZONE_LBL), ssZoneValue, ssZoneOption);
            } else {
                ss_zone.setmOption(ssZoneOption);
            }
        } else {
            ss_zone.setVisibility(View.GONE);
        }
        //
        MD_OperationDao operationDao = new MD_OperationDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        ArrayList<HMAux> ssOperationOption = (ArrayList<HMAux>) operationDao.query_HM(
                new MD_Operation_Sql_SS(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        HMAux ssOperationValue = getCurrentmValue(hmDialogInfo.get(Constant.FOOTER_OPERATION), ssOperationOption);
        setSSs(ss_operation, hmDialogInfo.get(Constant.FOOTER_OPERATION_LBL), ssOperationValue, ssOperationOption);
        ss_operation.setVisibility(hmDialogInfo.get(Constant.FOOTER_OPERATION) == null || hmDialogInfo.get(Constant.FOOTER_OPERATION).length() <= 0 ? View.GONE : View.VISIBLE);
        //
    }

    public static List<HMAux> getSiteLicenseAvailability(List<HMAux> ssSiteOption, String iconKey) {

        for (HMAux hmAux : ssSiteOption) {
            if(hmAux.hasConsistentValue(MD_SiteDao.LICENSE_ENABLED)){
                if (hmAux.get(MD_SiteDao.LICENSE_ENABLED).equals("0")) {
                    if (hmAux.hasConsistentValue(MD_SiteDao.LICENSE_BLOCKED)) {
                        if ("1".equals(hmAux.get(MD_SiteDao.LICENSE_BLOCKED))){
                            hmAux.put(iconKey, String.valueOf(R.drawable.ic_site_license_disable_unavailable));
                        }else{
                            int free_executions_max, free_executions_count, app_executions_count;
                            //
                            free_executions_max = getIntFromHmAux(hmAux, MD_SiteDao.FREE_EXECUTIONS_MAX);
                            free_executions_count = getIntFromHmAux(hmAux, MD_SiteDao.FREE_EXECUTIONS_COUNT);
                            app_executions_count = getIntFromHmAux(hmAux, MD_SiteDao.APP_EXECUTIONS_COUNT);
                            //
                            int totalExecution = free_executions_count + app_executions_count;
                            if(totalExecution < free_executions_max){
                                hmAux.put(iconKey, String.valueOf(R.drawable.ic_site_license_disable_available));
                            }else {
                                hmAux.put(iconKey, String.valueOf(R.drawable.ic_site_license_disable_unavailable));
                            }
                        }
                    }
                }else{
                    hmAux.put(iconKey, String.valueOf(R.drawable.ic_site_license_enable));
                }
            }
        }
        return ssSiteOption;
    }

    private static int getIntFromHmAux(HMAux hmAux, String hmAuxKey){
        if(hmAux.hasConsistentValue(hmAuxKey)){
            return Integer.parseInt(hmAux.get(hmAuxKey));
        }
        return 0;
    }

    private static void setDisableUserInfo(HMAux hmDialogInfo, LinearLayout ll_site, TextView tv_site_lbl, TextView tv_site_value, LinearLayout ll_zone, TextView tv_zone_lbl, TextView tv_zone_value, LinearLayout ll_operation, TextView tv_operation_lbl, TextView tv_operation_value) {
        tv_site_lbl.setText(hmDialogInfo.get(Constant.FOOTER_SITE_LBL));
        tv_site_value.setText(hmDialogInfo.get(Constant.FOOTER_SITE));
        ll_site.setVisibility(hmDialogInfo.get(Constant.FOOTER_SITE) == null || hmDialogInfo.get(Constant.FOOTER_SITE).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_zone_lbl.setText(hmDialogInfo.get(Constant.FOOTER_ZONE_LBL));
        tv_zone_value.setText(hmDialogInfo.get(Constant.FOOTER_ZONE));
        ll_zone.setVisibility(hmDialogInfo.get(Constant.FOOTER_ZONE) == null || hmDialogInfo.get(Constant.FOOTER_ZONE).length() <= 0 ? View.GONE : View.VISIBLE);

        tv_operation_lbl.setText(hmDialogInfo.get(Constant.FOOTER_OPERATION_LBL));
        tv_operation_value.setText(hmDialogInfo.get(Constant.FOOTER_OPERATION));
        ll_operation.setVisibility(hmDialogInfo.get(Constant.FOOTER_OPERATION) == null || hmDialogInfo.get(Constant.FOOTER_OPERATION).length() <= 0 ? View.GONE : View.VISIBLE);
    }


    private static ArrayList<HMAux> getSiteZoneOption(Context context, HMAux ssSiteValue) {
        MD_Site_ZoneDao siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        return (ArrayList<HMAux>) siteZoneDao.query_HM(
                new MD_Site_Zone_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ssSiteValue.get(SearchableSpinner.CODE)
                ).toSqlQuery()
        );
    }

    private static void setmSSAction(final Context context, SearchableSpinner ss_site, final SearchableSpinner ss_zone, SearchableSpinner ss_operation) {
        ss_site.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                ArrayList<HMAux> ssZoneOption = getSiteZoneOption(context, hmAux);
                ToolBox_Inf.setSSmValue(ss_zone, null, null, null, false, true);
                ss_zone.setmOption(ssZoneOption);
                if(ssZoneOption != null && ssZoneOption.size() == 1){
                    ss_zone.setmValue(ssZoneOption.get(0));
                }
            }
        });
    }

    private static HMAux getCurrentmValue(String current_value, ArrayList<HMAux> ssOption) {
        String value_code[] = current_value.split(" - ");
        for (HMAux value:ssOption) {
            if(value.get(SearchableSpinner.CODE).equals(value_code[0])){
                return value;
            }
        }
        return null;
    }

    private static void setmCanClean(SearchableSpinner ss_site, SearchableSpinner ss_zone, SearchableSpinner ss_operation) {
        ss_site.setmCanClean(false);
        ss_zone.setmCanClean(false);
        ss_operation.setmCanClean(false);
    }

    private static void setSSs(SearchableSpinner searchableSpinner, String label, HMAux mValue, ArrayList<HMAux> mOption) {
        searchableSpinner.setmTitle(label);
        searchableSpinner.setmLabel(label);
        searchableSpinner.setmValue(mValue);
        searchableSpinner.setmOption(mOption);
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
        transList.add("footer_dialog_offline_mode_lbl");
        transList.add("footer_dialog_customer_lbl");
        transList.add("footer_dialog_site_lbl");
        transList.add("footer_dialog_zone_lbl");
        transList.add("footer_dialog_operation_lbl");
        transList.add("footer_dialog_btn_ok");
        transList.add("footer_dialog_btn_ok");
        transList.add("footer_dialog_imei");
        transList.add("footer_dialog_user_level");
        transList.add("sys_not_found_lbl");
        transList.add("sys_site_or_operation_not_found_error");
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
                        new MD_Site_Sql_Footer(
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
        //
        if(operation != null && site != null ){
            operationDesc = operation.getOperation_code() + " - " + operation.getOperation_desc();
            //LUCHE - 11/03/2018
            //Modificado query de site para trazer todos os dados e modificado a construção do texto site desc
            //para a linha abaixo.
            siteDesc = site.getSite_code() + " - " + site.getSite_id() + " - " + site.getSite_desc();
        }else{
            //String sError = "Site ou Operação do footer não encontrado:\n";
            String sError = HmTrans.get("sys_site_or_operation_not_found_error") +"\n";
            //
            if(site == null){
                //Atualiza var de erro
                sError += Constant.FOOTER_SITE_LBL +" -> " +HmTrans.get("sys_not_found_lbl") +"\n";
                //Define descricao
                siteDesc = HmTrans.get("sys_not_found_lbl");
            }else{
                siteDesc = site.getSite_code() + " - " + site.getSite_desc();
            }
            //
            if(operation == null){
                //Atualiza var de erro
                sError += Constant.FOOTER_OPERATION_LBL +" -> " +HmTrans.get("sys_not_found_lbl") +"\n";
                //Define descricao
                operationDesc = HmTrans.get("sys_not_found_lbl");
            }else{
                operationDesc  = operation.getOperation_code() + " - " + operation.getOperation_desc();
            }
            //Gera arquivo de exception
            registerException(CLASS_NAME,new Exception(sError));
        }
        //
        hmAux.put(Constant.FOOTER_OFFLINE_MODE_LBL, HmTrans.get("footer_dialog_offline_mode_lbl"));
        hmAux.put(Constant.FOOTER_CUSTOMER_LBL, HmTrans.get("footer_dialog_customer_lbl"));
        hmAux.put(Constant.FOOTER_CUSTOMER, customerDesc);
        hmAux.put(Constant.FOOTER_SITE_LBL, HmTrans.get("footer_dialog_site_lbl"));
        hmAux.put(Constant.FOOTER_SITE, siteDesc);
        hmAux.put(Constant.FOOTER_OPERATION_LBL, HmTrans.get("footer_dialog_operation_lbl"));
        hmAux.put(Constant.FOOTER_OPERATION, operationDesc);
        hmAux.put(Constant.FOOTER_BTN_OK, HmTrans.get("footer_dialog_btn_ok"));
        hmAux.put(Constant.FOOTER_VERSION_LBL, HmTrans.get("footer_version_lbl"));
        hmAux.put(Constant.FOOTER_IMEI_LBL, HmTrans.get("footer_dialog_imei"));
        hmAux.put(FOOTER_USER_LEVEL_LBL, HmTrans.get("footer_dialog_user_level"));
        hmAux.put(FOOTER_IMEI, ToolBox_Inf.uniqueID(context));
        hmAux.put(FOOTER_OK, HmTrans.get("sys_alert_btn_ok"));
        hmAux.put(FOOTER_CANCEL, HmTrans.get("sys_alert_btn_cancel"));
        //
        hmAux.put(Constant.FOOTER_ZONE_LBL, "");
        hmAux.put(Constant.FOOTER_ZONE, "");

        //if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO/*, Constant.PARAM_SO_MOV*/})) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO,null)
            || ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)
        ) {
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

    public static HMAux loadFooterSiteOperationInfo(Context context) {
        HMAux hmAux = new HMAux();
        String siteDesc;
        String zoneDesc;
        String operationDesc;
        //
        List<String> transList = new ArrayList<>();
        transList.add("sys_not_found_lbl");
        //
        HMAux HmTrans = setLanguage(
                context,
                Constant.APP_MODULE,
                ToolBox_Inf.getResourceCode(
                        context,
                        Constant.APP_MODULE,
                        "sys"
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        //
        MD_Site site =
                new MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).getByString(
                        new MD_Site_Sql_Footer(
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
        //
        if(operation != null && site != null ){
            operationDesc = operation.getOperation_desc().replace(operation.getOperation_id() + " - ", "").trim();
            //siteDesc = site.getSite_desc().replace(site.getSite_id() + " - ", "").trim();
            //LUCHE - 11/03/2019
            //Comentado a linha a cima e modificado query usada no carregamento do obj site.
            siteDesc = site.getSite_desc();
        }else{
            //String sError = "Site ou Operação do footer não encontrado:\n";
            //LUCHE - 11/03/2019
            //Comentado a linha a cima e add tradução
            String sError = HmTrans.get("sys_site_or_operation_not_found_error") +"\n";
             //
            if(site == null){
                //Add chave que indica erro no carregamento do site.
                hmAux.put(Constant.FOOTER_SITE_NOT_FOUND, HmTrans.get("footer_dialog_site_lbl") +" "+ HmTrans.get("sys_not_found_lbl"));
                //Atualiza var de erro
                sError += Constant.FOOTER_SITE_LBL +" -> " +HmTrans.get("sys_not_found_lbl") +"\n";
                //Define descricao
                siteDesc = HmTrans.get("sys_not_found_lbl");
            }else{
                siteDesc = site.getSite_desc().replace(site.getSite_id() + " - ", "").trim();
            }

            //
            if(operation == null){
                //Add chave que indica erro no carregamento dA operação.
                hmAux.put(Constant.FOOTER_OPERATION_NOT_FOUND,HmTrans.get("footer_dialog_operation_lbl") +" "+ HmTrans.get("sys_not_found_lbl"));
                //Atualiza var de erro
                sError += Constant.FOOTER_OPERATION_LBL +" -> " +HmTrans.get("sys_not_found_lbl") +"\n";
                //Define descricao
                operationDesc = HmTrans.get("sys_not_found_lbl");
            }else{
                operationDesc  = operation.getOperation_desc().replace(operation.getOperation_id() + " - ", "").trim();
            }
            //Gera arquivo de exception
            registerException(CLASS_NAME,new Exception(sError));
        }

        hmAux.put(Constant.FOOTER_SITE, siteDesc);
        hmAux.put(Constant.FOOTER_OPERATION, operationDesc);

        //if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO})) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO,null)
            || ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)
        ) {
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
                //zoneDesc = zone.getZone_code() + " - " + zone.getZone_desc();
                zoneDesc = zone.getZone_desc();
            }
            //
            hmAux.put(Constant.FOOTER_ZONE, zoneDesc);

        }
        //
        return hmAux;
    }

    public static String getCustomerLogoPath(Context context) {

        return Constant.IMG_PATH + "/logo_c_" + ToolBox_Con.getPreference_Customer_Code(context) + ".png";

    }

    /**
     * Essa rotina de deleção visa apenas limpar a tabela sync_checklist
     * que indica que os forms daquele produto foram sincronzados e quando.
     * Uma vez removido esse vinculo, o produto não será mais envia no CHECKLIST
     * do sincronismo e seus forms não serão mais enviado.
     * Sendo assim, os forms só serão "deletados" no proximo sincronismo.
     * Luche 26/03/2018
     *
     * @param context
     */
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

    /**
     * Modificado em 19/03/2020 - LUCHE
     * <p></p>
     * Metodo ancestral que gera a notificação de agendados e atrasados.
     * Foi modificado em 19/03/2020 substituindo os textos chumbados por constantes e
     * foram adiconado comentarios para facilitar o entendimento do codigo
     * @param context Contexto
     * @param parameter Request Code da pedingIntent
     */
    public static void generateNotification(Context context, int parameter) {
        MD_Schedule_ExecDao scheduleDao;
        ArrayList<HMAux> customers_vs_total = new ArrayList<>();
        ArrayList<HMAux> totals;
        //
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        EV_User_CustomerDao ev_user_customerDao = getEv_user_customerDao(context);
        //Seleciona lista de customers com sessão.
        ArrayList<HMAux> customers =  (ArrayList<HMAux>) ev_user_customerDao.query_HM(new EV_User_Customer_Sql_006().toSqlQuery());
        //Loop na lista de customer para exibir msg de agendamentos para cada um.
        for (HMAux hmAux : customers) {
            long customerCode = Long.parseLong(hmAux.get(EV_User_CustomerDao.CUSTOMER_CODE));
            //
            scheduleDao =
                    new MD_Schedule_ExecDao(
                            context,
                            ToolBox_Con.customDBPath(customerCode),
                            Constant.DB_VERSION_CUSTOM
                    );
            //
            HMAux auxCT = (HMAux) hmAux.clone();
            //
            boolean bInclude = false;
            // parameter:
            //  100 - Notificação alarm full a cada 60 minutos.
            //  200 - Notificação alarm quarter a cada 15 minutos.
            if (parameter == ConstantBaseApp.ALARM_REQUEST_CODE_WS_AL_FULL) {
                //Gera "lista" com a qtd de itens agendados para a proxima 1 horas(future) e os atrasados(late).
                //A parte da lista é apenas para não retornar null, pois o resultado sempre será apenas um registro
                totals = (ArrayList<HMAux>) scheduleDao.query_HM(
                        new Sql_Notification_Schedule_001(
                                context,
                                customerCode
                        ).toSqlQuery()
                );
            } else {
                //Gera lista com a qtd de itens agendados para a proxima 1 horas(future)
                totals = (ArrayList<HMAux>) scheduleDao.query_HM(
                        new Sql_Notification_Schedule_002(
                            context,
                            customerCode
                        ).toSqlQuery()
                );
            }
            //loop na lista dos dados retornados.
            for (HMAux hmAuxTotal : totals) {
                bInclude = true;
                if (parameter == ConstantBaseApp.ALARM_REQUEST_CODE_WS_AL_FULL ) {
                    if (hmAuxTotal.get(ConstantBaseApp.MD_SCHEDULE_KEY_TYPE).equalsIgnoreCase(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE)) {
                        auxCT.put(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE, hmAuxTotal.get(ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL));
                    } else {
                        auxCT.put(ConstantBaseApp.MD_SCHEDULE_KEY_LATE, hmAuxTotal.get(ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL));
                    }
                } else {
                    if (hmAuxTotal.get(ConstantBaseApp.MD_SCHEDULE_KEY_TYPE).equalsIgnoreCase(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE)) {
                        auxCT.put(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE, hmAuxTotal.get(ConstantBaseApp.MD_SCHEDULE_KEY_TOTAL));
                    } else {
                    }
                }
            }
            //Se houve resultado na query, insere tradução no proximo aux o.O
            if (bInclude) {
                HMAux hmTr = translationCustomerSys(context, auxCT.get(EV_User_CustomerDao.TRANSLATE_CODE));
                auxCT.put(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE_TEXT, hmTr.get(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_FUTURE));
                auxCT.put(ConstantBaseApp.MD_SCHEDULE_KEY_LATE_TEXT, hmTr.get(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_LATE));
                customers_vs_total.add(auxCT);
            }

        }
        //Executa loop no HmAux com a msg de cada customer e monta a notificação
        for (HMAux cust : customers_vs_total) {
            StringBuilder sbFinal = new StringBuilder();
            //
            NotificationCompat.Builder builder = getNotificationBuilder(context, nm);
            builder.setSmallIcon(R.drawable.ic_calendario);
            builder.setAutoCancel(true);
            //
            if (parameter == ConstantBaseApp.ALARM_REQUEST_CODE_WS_AL_FULL) {
                if (!cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE).equals("0") || !cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_LATE).equals("0")) {
                    sbFinal
                            .append(cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE_TEXT) + "(")
                            .append(cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE) + ")  ")
                            .append(cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_LATE_TEXT) + "(")
                            .append(cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_LATE) + ")\n");
                }
            } else {
                if (!cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE).equals("0")) {
                    sbFinal
                            .append(cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE_TEXT) + "(")
                            .append(cust.get(ConstantBaseApp.MD_SCHEDULE_KEY_FUTURE) + ")\n");
                }
            }
            //Só deus o sabe o pq, mas é um "prefixo" para gerar o "id" da notificação.
            //
            int parameter_unified = 500;
            //Se tradução OK, monta builder da notificação e exibe notificação.
            if (sbFinal.toString().length() != 0) {
                builder.setContentTitle(cust.get(EV_User_CustomerDao.CUSTOMER_NAME));
                builder.setContentText(sbFinal.toString());
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                //
                int versao = Build.VERSION.SDK_INT;
                //
                if (versao >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    nm.notify(parameter_unified + Integer.parseInt(cust.get(EV_User_CustomerDao.CUSTOMER_CODE)), builder.build());
                } else {
                    nm.notify(parameter_unified + Integer.parseInt(cust.get(EV_User_CustomerDao.CUSTOMER_CODE)), builder.getNotification());
                }
            }
        }
    }

    public static void showScheduleNotification(Context context, String scheduleDateStart, String scheduleItemDesc, String currentStatus, String newStatus, String userNick) {
        String mModule = "SYS";
        String mResource_name = "SYS_APP";
        //Formata Data para forao de exbição.
        scheduleDateStart =   millisecondsToString(
            dateToMilliseconds(scheduleDateStart +" "+ToolBox_Con.getPreference_Customer_TMZ(context)),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );
        //Tratativa para status que só existe no Checklist.
        if (ConstantBaseApp.SYS_STATUS_IN_PROCESSING.equals(currentStatus)) {
            currentStatus = ConstantBaseApp.SYS_STATUS_PROCESS;
        }
        //
        List<String> transList = new ArrayList<>();
        //
        transList.add("schedule_notification_schedule_pk_lbl");
        transList.add("schedule_notification_current_status_lbl");
        transList.add("schedule_notification_new_status_lbl");
        transList.add("schedule_notification_user_nick_lbl");
        //
        HMAux hmAuxTrans = setLanguage(
                            context,
                            mModule,
                            getResourceCode(
                                context,
                                mModule,
                                mResource_name
                            ),
                            ToolBox_Con.getPreference_Translate_Code(context),
                            transList
                        );


        /**
         * A PORRA DO REMOTEVIEWS NÃO SUPORTA A PORRA DO CONSTRAINT LAYOUT COMO RAIZ
         * https://stackoverflow.com/questions/45396426/crash-when-using-constraintlayout-in-notification
         */
        //Notificação Small, monsta icone de warning, data de incio e descrição do form,caso seja N-Form, ou desc do tipo de ticket, caos ticket.
        RemoteViews notificationLayoutSmall = new RemoteViews(context.getPackageName(), R.layout.schedule_notification_small);
        notificationLayoutSmall.setTextViewText(R.id.schedule_notification_small_date_start,scheduleDateStart);
        notificationLayoutSmall.setTextViewText(R.id.schedule_notification_small_desc,scheduleItemDesc);
        //Notificação Big, exibe os dados da Small, mas a troca de Status e user que gerou a ação.
        RemoteViews notificationLayoutBig = new RemoteViews(context.getPackageName(), R.layout.schedule_notification_big);
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_date_start,scheduleDateStart);
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_desc,scheduleItemDesc);
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_cur_status_lbl, hmAuxTrans.get("schedule_notification_current_status_lbl"));
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_cur_status, hmAuxTrans.get(currentStatus));
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_new_status_lbl, hmAuxTrans.get("schedule_notification_new_status_lbl"));
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_s_new_status_val, hmAuxTrans.get(newStatus));
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_user_nick_lbl, hmAuxTrans.get("schedule_notification_user_nick_lbl"));
        notificationLayoutBig.setTextViewText(R.id.schedule_notification_big_user_nick_val, userNick);
        //
        NotificationManager nm = (NotificationManager)
            context.getSystemService(NOTIFICATION_SERVICE);
        //
        NotificationCompat.Builder builder = getNotificationBuilder(context, nm);
        builder.setSmallIcon(R.drawable.ic_calendario);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setCustomContentView(notificationLayoutSmall);
        builder.setCustomBigContentView(notificationLayoutBig);
        builder.setAutoCancel(true);
        //Tentativa de unique
        int id = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nm.notify(id, builder.build());
        }else {
            nm.notify(id, builder.getNotification());
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


            Constant.HMAUX_TRANS_LIB.putAll(setLanguage(
                    context,
                    Constant.APP_MODULE,
                    getResourceCode(
                            context,
                            Constant.APP_MODULE,
                            "SYS_WEB_STATUS"
                    ),
                    ToolBox_Con.getPreference_Translate_Code(context),
                    transList));
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
        Constant.HMAUX_TRANS_LIB.put("sys_alert_btn_cancel", (!Constant.HMAUX_TRANS_LIB.containsKey("sys_alert_btn_cancel") || Constant.HMAUX_TRANS_LIB.get("sys_alert_btn_cancel").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.sys_alert_btn_cancel) : Constant.HMAUX_TRANS_LIB.get("sys_alert_btn_cancel")));
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

        hmAux.put(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_FUTURE, (!hmAux.containsKey(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_FUTURE) || hmAux.get(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_FUTURE).contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.message_full_quarter_notification_future) : hmAux.get(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_FUTURE)));
        hmAux.put(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_LATE, (!hmAux.containsKey(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_LATE) || hmAux.get(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_LATE).contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.message_full_quarter_notification_late) : hmAux.get(ConstantBaseApp.MD_SCHEDULE_MESSAGE_FULL_QUARTER_NOTIFICATION_LATE)));

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
        if (   Work_DownLoad_Customer_Logo.IS_RUNNING
                || Work_DownLoad_PDF.IS_RUNNING
                || Work_DownLoad_Picture.IS_RUNNING
                || WBR_UpdateSoftware.IS_RUNNING
        ) {
            return true;
        }
        return false;
    }

    public static boolean isUploadRunning() {
      //  if (WBR_Upload_Img.IS_RUNNING || WBR_Upload_Support.IS_RUNNING || WBR_Upload_Img_Chat.IS_RUNNING || WBR_Upload_Other_User_Img.IS_RUNNING) {
        if ( Work_Upload_Img.IS_RUNNING
            || WBR_Upload_Support.IS_RUNNING
            || Work_Upload_Img_Chat.IS_RUNNING
            || Work_Upload_Other_User_Img.IS_RUNNING
        ) {
            return true;
        }
        return false;
    }

    /**
     * Assinatura orignal
     *
     * @param context
     * @param notification_id
     */
    public static void showNotification(Context context, int notification_id) {
        showNotification(context, notification_id, ToolBox_Con.getPreference_Customer_Code(context));
    }

    /**
     * Assinatura com parametro de customer para evitar banco -1
     * Problema gerado quando notificação do chat era ativado sem o usuario estar com customer logado
     *
     * @param context
     * @param notification_id
     * @param customer_code
     */
    public static void showNotification(Context context, int notification_id, long customer_code) {
        List<String> translist = new ArrayList<>();
        int animation = -1;
        String title = "";
        String msg = "";
        HMAux hmAux_Trans = new HMAux();
        //
        Log.d("ShowNotif", "Customer: " + String.valueOf(customer_code));

        //
        if (customer_code != -1) {
            //
            hmAux_Trans = ToolBox_Inf.setLanguage(
                    context,
                    "",
                    "0",
                    ToolBox_Con.getPreference_Translate_Code(context),
                    translist,
                    customer_code
            );
        }

        switch (notification_id) {

            case Constant.NOTIFICATION_UPLOAD:
                animation = R.drawable.upload_animation;
                title = context.getString(R.string.notification_ttl_upload);
                if (hmAux_Trans.containsKey("notification_ttl_upload")) {
                    title = hmAux_Trans.get("notification_ttl_upload");
                }
                msg = context.getString(R.string.notification_msg_upload);
                if (hmAux_Trans.containsKey("notification_msg_upload")) {
                    msg = hmAux_Trans.get("notification_msg_upload");
                }
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
        NotificationManager mNotifyManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //Se Id encontrado gera notificação, se não, não.
        if (animation != -1) {
            NotificationCompat.Builder mBuilder = getNotificationBuilder(context, mNotifyManager);
            //
            mBuilder.setSmallIcon(animation)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setTicker("");
            //
            mBuilder.setAutoCancel(true);
            //18/07/2018
            //A notificação de upload agoranão é mais cancelavel
            if (notification_id == Constant.NOTIFICATION_UPLOAD) {
                mBuilder.setAutoCancel(false);
                mBuilder.setOngoing(true);
            }

            mNotifyManager.notify(notification_id, mBuilder.build());

        }

    }

    public static void cancelNotification(Context context, int notification_id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
    }

    public static void callPendencyNotification(Context context) {
        if(ToolBox_Con.getPreference_Customer_Code(context) > 0) {
            HMAux hmAux_trans = new HMAux();
            List<String> translateList = new ArrayList<>();
            translateList.add("sys_notification_pendency_form_lbl");
            translateList.add("sys_notification_pendency_form_ap_lbl");
            translateList.add("sys_notification_pendency_serial_lbl");
            translateList.add("sys_notification_pendency_assets_lbl");
            translateList.add("sys_notification_pendency_services_lbl");
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
                if (hmAux_trans == null) {
                    hmAux_trans = new HMAux();
                }
            }

            callPendencyNotification(context, hmAux_trans);
        }
    }

    /**
     * BARRIONUEVO - 12/03/2020
     * <p></p>
     * Chamada de notificacao com resumo de pendencia.
     * @param context Application Context
     * @param hmAux_Trans Traducao utilizada na Notification eh do Sys
     */
    @NonNull
    public static void callPendencyNotification(Context context, HMAux hmAux_Trans){
        if(ToolBox_Con.getPreference_Customer_Code(context) > 0) {
            NotificationHelper notificationHelper = new NotificationHelper(context, hmAux_Trans);
            notificationHelper.call_Notification();
        }
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
            transList.add("ws_exception_connection_error");
            transList.add("ws_exception_server_not_found");

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

        hmAux_Trans.put("ws_exception_connection_error",
                (!hmAux_Trans.containsKey("ws_exception_connection_error") || hmAux_Trans.get("ws_exception_connection_error").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_connection_error) : hmAux_Trans.get("ws_exception_connection_error"))
        );

        hmAux_Trans.put("ws_exception_unexpected_error",
                (!hmAux_Trans.containsKey("ws_exception_unexpected_error") || hmAux_Trans.get("ws_exception_unexpected_error").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_unexpected_error) : hmAux_Trans.get("ws_exception_unexpected_error"))
        );

//        hmAux_Trans.put("ws_exception_contact_admin_json_syntax",
//                (!hmAux_Trans.containsKey("ws_exception_contact_admin_json_syntax") || hmAux_Trans.get("ws_exception_contact_admin_json_syntax").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_contact_admin_json_syntax) : hmAux_Trans.get("ws_exception_contact_admin_json_syntax"))
//        );

        hmAux_Trans.put("ws_exception_contact_admin_oracle",
                (!hmAux_Trans.containsKey("ws_exception_contact_admin_oracle") || hmAux_Trans.get("ws_exception_contact_admin_oracle").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_contact_admin_oracle) : hmAux_Trans.get("ws_exception_contact_admin_oracle"))
        );

        hmAux_Trans.put("ws_exception_contact_admin_timeout",
                (!hmAux_Trans.containsKey("ws_exception_contact_admin_timeout") || hmAux_Trans.get("ws_exception_contact_admin_timeout").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.ws_exception_contact_admin_timeout) : hmAux_Trans.get("ws_exception_contact_admin_timeout"))
        );

        hmAux_Trans.put("ws_exception_server_connection_failed",
                (!hmAux_Trans.containsKey("ws_exception_server_connection_failed") || hmAux_Trans.get("ws_exception_server_connection_failed").contains(Constant.APP_MODULE + "/") ? context.getString(R.string.ws_exception_server_connection_failed) : hmAux_Trans.get("ws_exception_server_connection_failed"))
        );

        hmAux_Trans.put("ws_exception_server_not_found",
            (!hmAux_Trans.containsKey("ws_exception_server_not_found") || hmAux_Trans.get("ws_exception_server_not_found").contains(Constant.APP_MODULE + "/") ? context.getString(R.string.ws_exception_server_not_found) : hmAux_Trans.get("ws_exception_server_not_found"))
        );

        results = (!hmAux_Trans.containsKey("generic_error_lbl") || hmAux_Trans.get("generic_error_lbl").contains(Constant.APP_MODULE + "/") ? context.getResources().getString(R.string.generic_error_lbl) : hmAux_Trans.get("generic_error_lbl")).toUpperCase();

//        if (e.toString().contains("JsonSyntaxException")) {
//            sb.append(results).append(" \n")
//                    .append(hmAux_Trans.get("ws_exception_contact_admin_json_syntax"))
//                    .append("\n")
//                    .append("\n")
//                    .append("JsonParse!\n")
//                    .append(e.toString()
//                    );
//
        if (e.toString().contains("JsonSyntaxException")) {
            if (!isHostAvailable()) {
                sb.append(results)
                        .append("\n")
                        .append("\n")
                        .append(hmAux_Trans.get("ws_exception_server_not_found"));
            } else {
                sb.append(results).append(" \n")
                        .append(hmAux_Trans.get("ws_exception_connection_error"))
                        .append("\n")
                        .append("\n");
                //.append("JsonParse!\n")
                //.append(e.toString()
            }
        } else if (e.toString().contains("ORA-")) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_contact_admin_oracle"))
                    .append("\n")
                    .append("\n")
                    .append("ORACLE!\n");
                    //.append(e.toString()

        } else if (e.toString().toLowerCase().contains("timeout")) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_contact_admin_timeout"))
                    .append("\n")
                    .append("\n")
                    .append("Timeout!\n ");
                    //.append(e.toString()
        } else if (e.toString().contains(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR)) {
            sb.append(results).append(" \n")
                    .append(hmAux_Trans.get("ws_exception_server_connection_failed"))
                    .append("\n")
                    .append("\n")
                    .append(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR + "!\n ");
            //.append(e.toString());//Como exception na mão, não tem toString
        } else {
            sb.append(results)
                    .append("\n")
                    .append("\n")
                    .append(hmAux_Trans.get("ws_exception_server_connection_failed"));
                    //.append(e.toString());
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
     * 27/11/18 - LUCHE
     *
     * Esse metodo possui um problema quando o timezone do user
     * entra no horario de verão: Quando o timezone entra no horario de verão,
     * o timezone usado continua sendo o padrão.
     * EX:
     *  Padrão Sp = GMT -03:00, Horario de Verão SP = GMT -02:00.
     * Independentemente de estar no horario de verão ou não, o GMT usado é o -03:00
     *
     * É possivel corrigi-lo usando o metodo getDeviceGMT e codigo similar ao
     * millisecondsToString()
     * @param date_tmz
     * @return
     */
    public static String convertDBToDeviceTMZ(String date_tmz) {
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");
        SimpleDateFormat sdfS = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");
        Calendar calendar = Calendar.getInstance();
        StringBuilder sbFinal = new StringBuilder();

        try {
            String argOriginal[] = date_tmz.split(" ");
            calendar.setTime(sdfD.parse(argOriginal[0] + " " + argOriginal[1] + " GMT" + argOriginal[2]));
            String[] args = sdfS.format(calendar.getTime()).split(" ");
            sbFinal.append(args[0]).append(" ").append(args[1]).append(" ");

            for (int i = 0; i < args[2].length(); ++i) {
                sbFinal.append(args[2].charAt(i));
                if (i == 2) {
                    sbFinal.append(":");
                }
            }
        } catch (Exception e) {
            return "";
        }

        return sbFinal.toString();
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
        //LUCHE - 18/02/2020
        //Add if verificando data, pois gerava muitas arquivos de exception desnecessariamente quando
        //data vazia ou null
        if(date_tmz != null && !date_tmz.isEmpty() && !date_tmz.equalsIgnoreCase("null")) {
            try {
                calendar.setTime(sdf.parse(date_tmz));
            } catch (ParseException e) {
                ToolBox_Inf.registerException(CLASS_NAME, e);
                e.printStackTrace();
            }
        }
        //
        return calendar.getTimeInMillis();
    }

    public static long dateToMilliseconds(String date_tmz, String type) {
        String sFormat = "";

        if (date_tmz == null ||  date_tmz.isEmpty()) {
            return 0L;
        }

        if (type.equalsIgnoreCase(Constant.DATE_TO_MILLISECOND_TYPE_IGNORE_SECOND)) {
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

    public static long dateToMillisecondsChat(String date_tmz, String type) {
        String sFormat = "";

        if (date_tmz == null || date_tmz.isEmpty()) {
            return 0L;
        }

        if (type.equalsIgnoreCase(Constant.DATE_TO_MILLISECOND_TYPE_IGNORE_SECOND)) {
            sFormat = "yyyy-MM-dd HH:mm";
        } else {
            sFormat = "yyyy-MM-dd HH:mm:ss";
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

//  VERSÃO USADA ATÉ 26/11/18
//  public static String millisecondsToString(long mils, String format) {
//
//        String sResults = "";
//
//        if (mils == 0L) {
//            return "";
//        }
//
//        Calendar ca1 = Calendar.getInstance();
//        ca1.setTimeInMillis(mils);
//        if (format == null || format.equalsIgnoreCase("")) {
//            format = "dd-MM-yyyy";
//        }
//
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//
//        try {
//            sResults = sdf.format(ca1.getTime());
//        } catch (Exception var7) {
//            sResults = "00:00 01-01-1900";
//        }
//
//        return sResults;
//    }

    /**
     * 26/11/18 - LUCHE
     * Metodo que retorna data formatada ja convertida pro timezone do device.
     *
     * Criado nova versão do metodo millisecondsToString , forçando o timezone do Device
     * com o de GMT retornado pelo metodo getDeviceGMT().
     * Por mais louco que pareça, essa mudança foi necessaria pois, as vezes, no horario de verão,
     * a conversão gerava data no timezone sem horario de verão o.O
     * @param mils - Millisegundas da data a ser formatada
     * @param format - Formato da data.
     * @return - Data formatada conforme format e convertida no GMT do device.
     */
    public static String millisecondsToString(long mils, String format) {

        String sResults = "";

        TimeZone curTmz = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone(ToolBox.getDeviceGMT(true)));
        Calendar ca1 = Calendar.getInstance();
        //
        if (mils == 0L) {
            return "";
        }

        ca1.setTimeInMillis(mils);
        if (format == null || format.equalsIgnoreCase("")) {
            format = "dd-MM-yyyy";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            Date dt = ca1.getTime();
            sResults = sdf.format(dt);
        } catch (Exception var7) {
            sResults = "00:00 01-01-1900";
        } finally {
            TimeZone.setDefault(curTmz);
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

    public static SpannableString getFormattedScheduleWarningInfo(String fcmNewStatusLbl,String fcmNewStatus, String fcmUserNickLbl,String fcmUserNick,String errorMsgLbl, String errorMsg, String initialText) {
        StringBuilder sbString = new StringBuilder(initialText != null ? initialText : "");
        SpannableString finalString = null;
        //
        if(fcmNewStatus != null && !fcmNewStatus.isEmpty()){
            sbString.append(fcmNewStatusLbl).append("\n").append(fcmNewStatus).append("\n");
        }
        if(fcmUserNick != null && !fcmUserNick.isEmpty()){
            sbString.append(fcmUserNickLbl).append("\n").append(fcmUserNick).append("\n");
        }
        if(errorMsg != null && !errorMsg.isEmpty()){
            sbString.append(errorMsgLbl).append("\n").append(errorMsg).append("\n");
        }
        //
        finalString = new SpannableString(sbString.toString());
        try{
            if(fcmNewStatus != null && !fcmNewStatus.isEmpty()) {
                finalString.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    sbString.indexOf(fcmNewStatusLbl),
                    sbString.indexOf(fcmNewStatus),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE

                );
            }
            if(fcmUserNick != null && !fcmUserNick.isEmpty()) {
                finalString.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    sbString.indexOf(fcmUserNickLbl),
                    sbString.indexOf(fcmUserNick),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE

                );
            }
            if(errorMsg != null && !errorMsg.isEmpty()) {
                finalString.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    sbString.indexOf(errorMsgLbl),
                    sbString.indexOf(errorMsg),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //5
        return finalString;
    }

    public static SpannableString getFormattedScheduleWarningInfo(String fcmNewStatusLbl,String fcmNewStatus, String fcmUserNickLbl,String fcmUserNick,String errorMsgLbl, String errorMsg) {
        return getFormattedScheduleWarningInfo(fcmNewStatusLbl,fcmNewStatus,fcmUserNickLbl,fcmUserNick,errorMsgLbl,errorMsg,null);
    }

    public static void showScheduleWarningDialog(Context context,String dialogTitle, String fcmNewStatusLbl,String fcmNewStatus, String fcmUserNickLbl, String fcmUserNick,String errorMsgLbl,String errorMsg){
        android.app.AlertDialog.Builder dialogScheduleWarning = new android.app.AlertDialog.Builder(context);
        dialogScheduleWarning.setTitle(dialogTitle);
        dialogScheduleWarning.setMessage(
            getFormattedScheduleWarningInfo(
                fcmNewStatusLbl,
                fcmNewStatus,
                fcmUserNickLbl,
                fcmUserNick,
                errorMsgLbl,
                errorMsg
            )
        );
        dialogScheduleWarning.setCancelable(true);
        dialogScheduleWarning.show();
    }

    /**
     * Metodo responsavel por verificar se há necessidade de sincronizacao
     * de formularios no fluxo de ticket
     * @param context - utilizado para instanciar os DAOs
     * @return
     */
    public static boolean hasFormProductOutdate(Context context, int ticketPrefix, int ticketCode) {
        long preference_customer_code = ToolBox_Con.getPreference_Customer_Code(context);
        Sync_ChecklistDao syncChecklistDao = new Sync_ChecklistDao(
                context,
                ToolBox_Con.customDBPath(preference_customer_code),
                Constant.DB_VERSION_CUSTOM
        );
        //
        List<HMAux> hmAuxList;
        if(ticketPrefix == -1 && ticketCode == -1) {
            hmAuxList =
                    syncChecklistDao.query_HM(
                            new Sync_Checklist_Sql_004(
                                    preference_customer_code
                            ).toSqlQuery()
                    );
        }else{
            hmAuxList =
                    syncChecklistDao.query_HM(
                            new Sync_Checklist_Sql_004(
                                    preference_customer_code,
                                    ticketPrefix,
                                    ticketCode
                            ).toSqlQuery()
                    );
        }
        if(hmAuxList != null
                && !hmAuxList.isEmpty()){
            for(HMAux aux: hmAuxList){
                if(aux.hasConsistentValue(Sync_ChecklistDao.PRODUCT_CODE)) {
                    setProductToSync(preference_customer_code, syncChecklistDao, aux);
                }
            }
            return true;
        }
        //
        return false;
    }

    public static boolean hasFormProductOutdate(Context context) {
        return hasFormProductOutdate(context, -1, -1);
    }


    public static boolean isTicketInTokenFile(Context context, int ticket_prefix, int ticket_code) {
        ArrayList<TK_Ticket> ticketInToken = ToolBox_Inf.getTicketsWithinToken(ToolBox_Con.getPreference_Customer_Code(context));
        if(ticketInToken != null && ticketInToken.size() > 0){
            for (TK_Ticket tkTicket : ticketInToken) {
                if( tkTicket.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                        && tkTicket.getTicket_prefix() == ticket_prefix
                        && tkTicket.getTicket_code() == ticket_code
                ){
                    return true;
                }
            }
        }
        //
        return false;
    }
    /**
     * BARRIONUEVO 01-09-2020
     * Metodo que verifica forms de ctrl que estão em waiting sync.
     * LUCHE - 10/09/2020
     * Modificado query do metodo para incluir tb o os forms com pendencia de GPS.
     * @param ticket_prefix
     * @param ticket_code
     * @return
     */
    public static boolean hasFormWaitingSyncWithinTicket(Context context, int ticket_prefix, int ticket_code) {
        GE_Custom_Form_DataDao formDataDao = new GE_Custom_Form_DataDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        GE_Custom_Form_Data formData = formDataDao.getByString(
                new Sql_Act070_005(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticket_prefix,
                        ticket_code
                ).toSqlQuery()
        );
        return formData != null;
    }

    /**
     * LUCHE - 10/09/2020
     * Verifica se existe alguma form com pendencia de GPS para o ticket passado.
     * @param context
     * @param ticket_prefix
     * @param ticket_code
     * @return
     */
    public static boolean hasFormGpsPendencyWithinTicket(Context context, int ticket_prefix, int ticket_code) {
        GE_Custom_Form_DataDao formDataDao = new GE_Custom_Form_DataDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        ArrayList<GE_Custom_Form_Data> formWithGpsPendency = (ArrayList<GE_Custom_Form_Data>) formDataDao.query(
            new Sql_Act070_008(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticket_prefix,
                ticket_code
            ).toSqlQuery()
        );
        //
        return formWithGpsPendency != null && formWithGpsPendency.size() > 0;
    }

    private static void setProductToSync(long preference_customer_code, Sync_ChecklistDao syncChecklistDao, HMAux aux) {
        Integer productCodeOutdate = Integer.parseInt(aux.get(Sync_ChecklistDao.PRODUCT_CODE));
        Calendar cDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist sync = new Sync_Checklist();
        sync.setCustomer_code(preference_customer_code);
        sync.setProduct_code(productCodeOutdate);
//        sync.setLast_update(ToolBox.sDTFormat_Agora("yyyy-MM-dd"));
        sync.setLast_update(last_update);
        syncChecklistDao.addUpdate(sync);
    }

    public static Drawable getNoPhotoDrawable(Context context) {
        Drawable placeHolder;
        placeHolder = context.getResources().getDrawable(R.drawable.ic_baseline_photo_camera_24);
        placeHolder.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
        return placeHolder;
    }


    public static boolean hasOffHandFormInProcess(Context context, int ticket_prefix, int ticket_code) {
        long preference_customer_code = ToolBox_Con.getPreference_Customer_Code(context);
        TK_Ticket_CtrlDao tkTicketCtrlDao = new TK_Ticket_CtrlDao(
                context,
                ToolBox_Con.customDBPath(preference_customer_code),
                Constant.DB_VERSION_CUSTOM
        );

        List<TK_Ticket_Ctrl> query = tkTicketCtrlDao.query(new TK_Ticket_Ctrl_Sql_007(
                preference_customer_code,
                ticket_prefix,
                ticket_code
        ).toSqlQuery());
        return query != null && query.size() > 0 ;
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

    public static void call_Location_Tracker_On_Background(Context context, int mode) {
        Intent mIntent = new Intent(context, SV_LocationTracker.class);
        mIntent.putExtra(SV_LocationTracker.ASYNC_GPS, mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(mIntent);
        }else {
            context.startService(mIntent);
        }
    }

    public static void call_Location_Tracker(Context context) {
        if (!SV_LocationTracker.status) {
            Intent mIntent = new Intent(context, SV_LocationTracker.class);
            context.startService(mIntent);
        }
    }

    public static void stop_Location_Tracker(Context context) {
        Intent mIntent = new Intent(context, SV_LocationTracker.class);
        context.stopService(mIntent);
    }

    public static void writeIn(String data, File file) throws IOException {
        FileWriter writer = new FileWriter(file, true);
        writer.append(data);
        writer.close();
    }

    /**
     * BARRIONUEVO 10-02-2021
     * Metodo responsavel
     * @param context
     */
    public static void stopChatService(Context context) {
        if(AppBackgroundService.isRunning) {
            Intent chatService = new Intent(context, AppBackgroundService.class);
            context.stopService(chatService);
        }
    }

    /**
     * BARRIONUEVO 10-02-2021
     * Metodo responsavel pela chamada do chat fora das acts de lista e conversas do chat.
     * @param context
     */
    public static void callChatService(Context context, String mode) {
        Log.d("ChatEvent"," callChatService mode : " + mode);
        Intent mIntent = new Intent(context, AppBackgroundService.class);
        mIntent.putExtra(CHAT_SERVICE_MODE, mode);
        AppBackgroundService.serviceChatMode = mode;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && !CHAT_SERVICE_MODE_ACTIVED.equals(mode)) {
            context.startForegroundService(mIntent);
        }else{
            context.startService(mIntent);
        }
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

    /**
     * Metodo que gera arquivo texto com registrando dados da exception e local onde ocorreu
     * 09/01/2019 - LUCHE
     *
     * Movido o metodo para biblioteca e adicionando chamada do metodo a lib dentro desse metodo.
     * @param local
     * @param exception
     */
    public static void registerException(String local, Exception exception) {

//        File exception_file = new File(Constant.SUPPORT_PATH, "excep_" + getDateHourStr() + ".txt");
//
//        try {
//
//            StackTraceElement[] stackTrace = exception.getStackTrace();
//            String traceString = "";
//            String erro = "";
//
//            for (StackTraceElement trace : stackTrace) {
//                traceString += trace.toString() + "\n ";
//            }
//
//            erro = "Local:\n " + local + ";\nException:\n " + exception.toString() + ";\nTrace:\n" + traceString + ";";
//
//            ToolBox_Inf.writeIn(erro, exception_file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ToolBox.registerException(local,exception);
    }

    /**
     * 04/12/2018 - LUCHE
     * Metodo chamado pelo Callback de exceptions não tratadas  gerandp arquvio de suport
     * @param exception
     */
    public static void registerException(Throwable exception) {
        String local = AppBase.class.getSimpleName();
        File exception_file = new File(Constant.SUPPORT_PATH, "fatal_excep_" + getDateHourStr() + ".txt");

        try {
            //Tenta Pega o primeiro item StackTrace elemente do "cause" e obtem o nome da classe onde
            //o erro se originou.
            //O metodo getCause pode retornar null , então testa o retorno antes de tentar pegar
            //nome do arquivo.
            //No futuro esse if pode ser melhorado sem usar attribuição de vars
            //porem, por hora, melhor deixar assim para debug.
            Throwable throwable = exception.getCause();
            if(throwable != null){
                StackTraceElement[] stack = throwable.getStackTrace();
                if(stack != null && stack.length > 0 && stack[0] != null){
                    StackTraceElement stakFirst = stack[0];
                    local = stakFirst.getFileName();
                }
                //local = (exception.getCause().getStackTrace())[0].getFileName();
            }else{
                if(exception.getStackTrace() != null
                    &&  exception.getStackTrace().length > 0
                    && (exception.getStackTrace())[0] != null )
                {
                    local = (exception.getStackTrace())[0].getFileName();
                }
            }
            //
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
    /*
        BARRIONUEVO - 22/10/2019
        Metodo para esconder keyboard no AlertDialog, inicialmente utilizado nos fragmentos da act043
     */
    public static void hideSoftKeyboard(Context context, View view) {
        if (view!= null && view.hasFocus()){
            if (context instanceof AppCompatActivity) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyBoard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

    public static Long mLongParse(String integer) {
        try {
            return Long.parseLong(integer);
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

    /**
     * Metodo usado no Action Plan para gerar a lista de status do Spinner
     * O metodo possui a lista de status geral como a array e EXCLUI DESSA LISTA
     * os status enviados como parametros...
     *
     * @param hmAux_trans - Traduções dos status
     * @param status - Lista de status que NÃO SERÃO EXIBIDOS.
     * @return - Lista de status QUE NÃO FORAM ENVIADOS.
     */
    public static ArrayList<HMAux> formApStatusList(HMAux hmAux_trans, String... status) {
        ArrayList<HMAux> statusList = new ArrayList<>();
        //
        String[] mStatus = {
                Constant.SYS_STATUS_EDIT,
                Constant.SYS_STATUS_PROCESS,
                Constant.SYS_STATUS_WAITING_ACTION,
                Constant.SYS_STATUS_DONE,
                Constant.SYS_STATUS_CANCELLED
        };

        for (int i = 0; i < mStatus.length; i++) {
            if (mStatus != null && mStatus.length > 0 && Arrays.asList(status).contains(mStatus[i])) {
                continue;
            } else {
                HMAux hmAux = new HMAux();
                hmAux.put(SearchableSpinner.CODE, mStatus[i]);
                hmAux.put(SearchableSpinner.ID, mStatus[i]);
                hmAux.put(SearchableSpinner.DESCRIPTION, hmAux_trans.get(mStatus[i]));
                //
                statusList.add(hmAux);
            }

        }
        //
        return statusList;
    }

    public static void setSSmValue(SearchableSpinner ss_component, String code, String id, String desc, boolean source_val, String... extra) {
        HMAux hmAux = new HMAux();
        hmAux.put(SearchableSpinner.CODE, code);
        hmAux.put(SearchableSpinner.ID, id);
        hmAux.put(SearchableSpinner.DESCRIPTION, desc);
        //
        if (extra.length > 0 && extra.length % 2 == 0) {
            for (int i = 0; i < extra.length; i += 2) {
                hmAux.put(extra[i], extra[i + 1]);
            }
        }
        //
        ss_component.setmValue(hmAux, source_val);
        //
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
     * @param id           - Codigo externo (id)
     * @param desc         - Descrição do item
     * @param source_val   - Seta esse code, como valor default no sppiner
     * @param acceptNull   - Seta tag indicanda se spinner aceita valor null
     */
    public static void setSSmValue(SearchableSpinner ss_component, String code, String id, String desc, boolean source_val, boolean acceptNull) {
        try {
            HMAux hmAux = new HMAux();
            if (code != null && code != "null") {
                hmAux.put(SearchableSpinner.CODE, code);
                hmAux.put(SearchableSpinner.ID, id);
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
       /* switch (status) {
            case Constant.SYS_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SYS_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SYS_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            default:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
        }*/
        tv_status.setTextColor(context.getResources().getColor(getStatusColor(status)));

    }


    public static void setTaskStatusColor(Context context, TextView tv_status, String status) {
        /*switch (status) {
            case Constant.SYS_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SYS_STATUS_PROCESS:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SYS_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SYS_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SYS_STATUS_NOT_EXECUTED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
                break;
            case Constant.SYS_STATUS_INCONSISTENT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
                break;
            default:
                break;
        }*/
        tv_status.setTextColor(context.getResources().getColor(getStatusColor(status)));
    }


    public static void setExecStatusColor(Context context, TextView tv_status, String status) {
        /*
         * Tratativa de cor por Status
         * */
//        switch (status) {
//            case Constant.SYS_STATUS_PENDING:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
//                break;
//            case Constant.SYS_STATUS_PROCESS:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
//                break;
//            case Constant.SYS_STATUS_DONE:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
//                break;
//            case Constant.SYS_STATUS_CANCELLED:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
//                break;
//            case Constant.SYS_STATUS_NOT_EXECUTED:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
//                break;
//            case Constant.SYS_STATUS_INCONSISTENT:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
//                break;
//            default:
//                break;
//        }
        tv_status.setTextColor(context.getResources().getColor(getStatusColor(status)));
    }

    public static void setSOStatusColor(Context context, TextView tv_status, String status) {

//        switch (status) {
//            case Constant.SYS_STATUS_PENDING:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
//                break;
//            case Constant.SYS_STATUS_PROCESS:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
//                break;
//            case Constant.SYS_STATUS_DONE:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
//                break;
//            case Constant.SYS_STATUS_CANCELLED:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
//                break;
//            case Constant.SYS_STATUS_STOP:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_black));
//                break;
//            case Constant.SYS_STATUS_WAITING_BUDGET:
//            case Constant.SYS_STATUS_WAITING_QUALITY:
//            case Constant.SYS_STATUS_WAITING_CLIENT:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_brown));
//                break;
//            case Constant.SYS_STATUS_EDIT:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_pink_1));
//                break;
//            case Constant.SYS_STATUS_WAITING_SYNC:
//                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_dark_blue));
//                break;
//            default:
//                break;
//
//        }
        tv_status.setTextColor(context.getResources().getColor(getStatusColor(status)));

    }

    public static Integer getIntOrNull(String value){
        try{
            return Integer.parseInt(value);
        }catch (Exception e){
            return null;
        }
    }

    public static int convertStringToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Double convertStringToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static String prepareForNull(String value) {
        try {
            if (value.trim().length() != 0) {
                return value.trim();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
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

            imgFinal.compress(Bitmap.CompressFormat.JPEG, 25, os);

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

    public static void showChatNotification(Context context, String type, String attempt, boolean showAnyway) {
        showChatNotification(context, type, attempt, "", "", showAnyway);
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
        NotificationCompat.Builder builder = getNotificationBuilder(context, nm);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_chat_24x24);
        builder.setColor(context.getResources().getColor(R.color.namoa_color_success_green));
        if (Constant.DEVELOPMENT_BASE) {
            builder.setSound(Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.morfador));
        } else {
            builder.setSound(alarmSound);
        }

        builder.setContentTitle(hmAux_trans.get("notification_add_room_ttl"));
        builder.setContentText(hmAux_trans.get("notification_add_room_msg"));

        Notification notification = builder.build();
        //
        nm.notify(Constant.NOTIFICATION_CHAT_ROOM, notification);
    }

    public static void cancelChatRoomNotification(Context context) {
        cancelNotification(context, Constant.NOTIFICATION_CHAT_ROOM);
    }

    public static void showChatNotification(Context context, String type, String attempt, String title, String message, boolean showAnyway) {
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
            if(hmAux_trans == null){
                hmAux_trans = new HMAux();
            }
            //
            hmAux_trans.put("notification_user_says_lbl",context.getString(R.string.notification_user_says_lbl));
            hmAux_trans.put("notification_rooms_lbl", context.getString(R.string.notification_rooms_lbl));
            hmAux_trans.put("notification_messages_lbl",context.getString(R.string.notification_messages_lbl));
            hmAux_trans.put("chat_fcm_offline_ttl",context.getString(R.string.chat_fcm_offline_ttl));
            hmAux_trans.put("chat_fcm_offline_msg",context.getString(R.string.chat_fcm_offline_msg));
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
        NotificationCompat.Builder builder = getNotificationBuilder(context, nm);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_chat_24x24);
        builder.setColor(context.getResources().getColor(R.color.namoa_color_success_green));
        if (Constant.DEVELOPMENT_BASE) {
            builder.setSound(Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.morfador));
        } else {
            builder.setSound(alarmSound);
        }
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

                            if (!showAnyway && Act035_Main.mRoom_code != null && Act035_Main.mRoom_code.equalsIgnoreCase(msgInfo.get("room_code"))) {
                                return;
                            }

                            /*if (msgInfo.get(Sql_Chat_Notification_001.QTY_ROOM).equals("1")) {
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
                            }*/
                            //Se msg são de apenas um customer, passa como parametro
                            if (msgInfo.get(Sql_Chat_Notification_001.QTY_CUSTOMER).equals("1")) {
                                mIntent = new Intent(context, NotificationReceiver.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mIntent.putExtra(CH_RoomDao.CUSTOMER_CODE, Long.parseLong(msgInfo.get(CH_RoomDao.CUSTOMER_CODE)));
                                //
                                pi = PendingIntent.getBroadcast(
                                        context,
                                        0,
                                        mIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );

                                builder.setContentIntent(pi);
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
                && now.get(Calendar.DAY_OF_MONTH) == cdate.get(Calendar.DAY_OF_MONTH);
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
        try {
            String[] lines = s.split("\\r?\\n");
            //
            if (lines != null && lines.length > 0) {
                return lines[0];
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void cleanRoom_RoomMessages(Context context) {
        CH_RoomDao mRoomDao = new CH_RoomDao(context);

        /**
         * Marcar os customer que estao locais mas que nao vieram da lista para serem ignorados no processo de limpeza
         */
        mRoomDao.addUpdate(new CH_Room_Sql_012(
                ).toSqlQuery()
        );

        ArrayList<File> imagesList = new ArrayList<>();
        //
        ArrayList<HMAux> mRooms = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Room_Sql_008().toSqlQuery()
        );

        ArrayList<HMAux> mRoomsImages = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Room_Sql_010().toSqlQuery()
        );

        try {
            for (HMAux aux : mRoomsImages) {
                imagesList.add(new File(Constant.CACHE_CHAT_PATH + "/" + aux.get(CH_RoomDao.ROOM_IMAGE_LOCAL)));
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
        }

        ArrayList<HMAux> mRoomsMessagesImages = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Message_Sql_023().toSqlQuery()
        );

        try {
            for (HMAux aux : mRoomsMessagesImages) {
                imagesList.add(new File(Constant.CACHE_PATH_PHOTO + "/" + aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL)));
                imagesList.add(new File(Constant.THU_PATH + "/" +
                        aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).substring(0, aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).length() - 4) +
                        Constant.THUMB_SUFFIX + ".jpg"));
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
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
        ArrayList<HMAux> mRoomsImages = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Room_Sql_011(ch_room.getRoom_code()).toSqlQuery()
        );
        //
        try {
            for (HMAux aux : mRoomsImages) {
                imagesList.add(new File(Constant.CACHE_CHAT_PATH + "/" + aux.get(CH_RoomDao.ROOM_IMAGE_LOCAL)));
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
        }
        //
        ArrayList<HMAux> msgImages = (ArrayList<HMAux>) mRoomDao.query_HM(
                new CH_Message_Sql_020(
                        ch_room.getRoom_code()
                ).toSqlQuery()
        );
        try {
            //
            for (HMAux aux : msgImages) {
                imagesList.add(new File(Constant.CACHE_PATH_PHOTO + "/" + aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL)));
                imagesList.add(new File(Constant.THU_PATH + "/" +
                        aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).substring(0, aux.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).length() - 4) +
                        Constant.THUMB_SUFFIX + ".jpg"));
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
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

//    public static String AccentMapper(String string) {
//        //
//        if (string == null) {
//            return "";
//        }
//        //
//        StringBuilder sb = new StringBuilder(string);
//        //
//        for (int i = 0; i < string.length(); i++) {
//            Character c = ACCENT_MAP.get(sb.charAt(i));
//            if (c != null) {
//                sb.setCharAt(i, c.charValue());
//            }
//        }
//
//        return sb.toString();
//    }

    public static String getCustomerSession(Context context, String user_code, long customer_code) {
        String session_app = null;
        EV_User_CustomerDao userCustomerDao = new EV_User_CustomerDao(context);
        //
        HMAux session_info = userCustomerDao.getByStringHM(
                new EV_User_Customer_Sql_008(
                        user_code,
                        customer_code
                ).toSqlQuery()
        );
        //
        if (session_info != null && session_info.size() > 0) {
            session_app = session_info.get(EV_User_CustomerDao.SESSION_APP);
        }
        //
        return session_app;
    }

    public static ArrayList<HMAux> getSessionCustomerChatList(Context context) {
        EV_User_CustomerDao userCustomerDao = new EV_User_CustomerDao(context);
        //
        ArrayList<HMAux> customer_list = (ArrayList<HMAux>) userCustomerDao.
                query_HM(
                        new EV_User_Customer_Sql_007(
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );
        //
        return customer_list;
    }

    public static ArrayList<HMAux> getActiveCustomerSession(Context context) {
        EV_User_CustomerDao userCustomerDao = new EV_User_CustomerDao(context);
        //
        ArrayList<HMAux> customer_list = (ArrayList<HMAux>) userCustomerDao.
                query_HM(
                        new EV_User_Customer_Sql_010(
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );
        //
        return customer_list;
    }

    public static String returnHmAuxListInString(ArrayList<HMAux> auxList, String key, String separator) {
        String hmAuxInLine = "";
        separator = separator == null ? "," : separator;
        //
        for (HMAux hmAux : auxList) {
            if (hmAux.containsKey(key)) {
                hmAuxInLine += hmAux.get(key) + separator;
            }
        }
        //
        hmAuxInLine = hmAuxInLine.length() > 0 ? hmAuxInLine.substring(0, hmAuxInLine.length() - 1) : "";
        //
        return hmAuxInLine;
    }

    public static JsonArray arrayListToJsonArray(ArrayList<?> arrayList) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        JsonArray jsonArray = new JsonArray();
        //
        for (int i = 0; i < arrayList.size(); i++) {
            jsonArray.add(gson.toJsonTree(arrayList.get(i)));
        }
        return jsonArray;
    }

    public static void setAPStatusColor(Context context, TextView tv_status, String status) {
        tv_status.setTextColor(context.getResources().getColor(getApStatusColor(status)));
    }

    public static int getApStatusColor(String status) {
        /*switch (status) {
            case Constant.SYS_STATUS_EDIT:
                return R.color.namoa_color_pink_1;
            case Constant.SYS_STATUS_PROCESS:
                return R.color.namoa_color_yellow_2;
            case Constant.SYS_STATUS_WAITING_ACTION:
                return R.color.namoa_color_brown;
            case Constant.SYS_STATUS_DONE:
                return R.color.namoa_color_green_2;
            case Constant.SYS_STATUS_CANCELLED:
                return R.color.namoa_color_gray_4;
            default:
                return R.color.namoa_color_gray_4;
        }*/
        return getStatusColor(status);
    }

    @Deprecated
    /**
     * <p></p>
     * Metodo que retorno do color referente ao status passado
     * <p></p>
     * LUCHE - 27/12/2019
     * O Metodo foi depreciado, pois foi criado o getStatusColorV2 que usa o mesmo switch deste metodo
     * mas ao inves de retornar o R.color, retorna o recurso da cor e , sendo assim, pode ser usado
     * diretamente no setTextColor();
     * <p></p>
     * LUCHE - 30/03/2020
     * <p></p>
     * Comentando os status do form que não existirão mais(Finalized e Sent) e modificando a cor dos
     * status schedule e waiting_sync
     *
     * @param status - Status
     * @return - R.color do status passado.
     */
    public static int getStatusColor(String status) {
        switch (status) {
            case Constant.SYS_STATUS_EDIT:
                return R.color.namoa_status_edit;
            case Constant.SYS_STATUS_STOP:
            case Constant.SYS_STATUS_REJECTED:
                return R.color.namoa_status_stop;
            case Constant.SYS_STATUS_PENDING:
                return R.color.namoa_status_pending;
            case Constant.SYS_STATUS_SCHEDULE:
                return R.color.namoa_status_scheduled;
            case Constant.SYS_STATUS_PROCESS:
            case Constant.SYS_STATUS_IN_PROCESSING:
            case Constant.SYS_STATUS_PUT_AWAY:
            case Constant.SYS_STATUS_PICKING:
                return R.color.namoa_status_process;
            case Constant.SYS_STATUS_PICKING_DONE:
            case Constant.SYS_STATUS_WAITING_APPROVAL:
                return R.color.namoa_status_waiting_approval;
            case Constant.SYS_STATUS_WAITING_BUDGET:
                return R.color.namoa_status_waiting_budget;
            case Constant.SYS_STATUS_WAITING_QUALITY:
                return R.color.namoa_status_waiting_quality;
            case Constant.SYS_STATUS_WAITING_CLIENT:
            case Constant.SYS_STATUS_WAITING_ACTION:
                return R.color.namoa_status_waiting_client;
            case Constant.SYS_STATUS_DONE:
            case Constant.SYS_STATUS_FINALIZED://Remover após certeza que não é mais usado
                return R.color.namoa_status_done;
            case Constant.SYS_STATUS_NOT_EXECUTED:
            case Constant.SYS_STATUS_SENT://30/03/2020 - Ainda usado na O.S express
                return R.color.namoa_status_not_executed;
            case Constant.SYS_STATUS_CANCELLED:
            case Constant.SYS_STATUS_DELETED:
                return R.color.namoa_status_cancelled;
            case Constant.SYS_STATUS_INCONSISTENT:
                return R.color.namoa_status_inconsistent;
            case Constant.SYS_STATUS_WAITING_SYNC:
                return R.color.namoa_status_waiting_sync;
            case Constant.SYS_STATUS_ERROR:
            case Constant.SYS_STATUS_IGNORED:
                return R.color.namoa_status_error;
            case Constant.SYS_STATUS_ACTIVE:
                return R.color.namoa_status_active;
            case Constant.SYS_STATUS_INACTIVE:
                return R.color.namoa_status_inactive;
            default:
                return R.color.namoa_color_gray_4;
        }
    }

    /**
     * Metodo é uma versão melhorada do getStatusColor, pois ja retorna resource do id da cor assim,
     * pode ser usado diretamente no setTextColor() se a necessidade de usar context.getResources().getColor()
     * @param context - contexto
     * @param status - status
     * @return retorna o resource da cor
     */
    public static int getStatusColorV2(Context context, String status) {
        return context.getResources().getColor(getStatusColor(status));
    }

    public static HashMap<String, String> JsonToHashMap(JSONObject jsonObject, String root) throws Exception {
        JSONObject mRoot = jsonObject.getJSONObject(root);
        HashMap<String, String> map = new HashMap<>();

        if (mRoot != null) {
            Iterator iter = mRoot.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = mRoot.getString(key);
                //
                map.put(key, value);
            }
        }

        return map;
    }

    public static int deleteUnnecessaryAP(Context context) {
        int deletedCounter = 0;
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(context);
        //
        ArrayList<GE_Custom_Form_Ap> formList = (ArrayList<GE_Custom_Form_Ap>)
                formApDao.query(
                        new GE_Custom_Form_Ap_Sql_011(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );
        //
        if (formList != null && formList.size() > 0) {
            for (GE_Custom_Form_Ap formAp : formList) {
                boolean deleteAP = true;
                //
                //if (ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT)) {
                CH_RoomDao roomDao = new CH_RoomDao(context);
                //Seleção antiga:
                // Selecionava somente via room_code
//                CH_Room chRoom = roomDao.getByString(
//                        new CH_Room_Sql_001(
//                                formAp.getRoom_code()
//                        ).toSqlQuery()
//                );
                //Seleção Nova(23/07/18):
                //Seleciona via room_code e pk concatenada no room_obj
                String pkConcat =
                        formAp.getCustomer_code() + "|" +
                        formAp.getCustom_form_type() + "|" +
                        formAp.getCustom_form_code() + "|" +
                        formAp.getCustom_form_version() + "|" +
                        formAp.getCustom_form_data() + "|" +
                        formAp.getAp_code()
                        ;
                //
                CH_Room chRoom = roomDao.getByString(
                        new CH_Room_Sql_014(
                                formAp.getRoom_code(),
                                pkConcat
                        ).toSqlQuery()
                );
                //
                if (chRoom != null && chRoom.getRoom_code().length() > 0) {
                    deleteAP = false;
                }
                //}
                //
                if (deleteAP) {
                    formApDao.remove(
                            new GE_Custom_Form_Ap_Sql_010(
                                    formAp.getCustomer_code(),
                                    formAp.getCustom_form_type(),
                                    formAp.getCustom_form_code(),
                                    formAp.getCustom_form_version(),
                                    formAp.getCustom_form_data(),
                                    formAp.getAp_code()
                            ).toSqlQuery()
                    );
                    deletedCounter++;
                }
            }
        }
        //
        return deletedCounter;
    }

    public static boolean checkForApExclusion(Context context, String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String ap_code) {
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(context);
        int user_code = ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context));
        boolean deleteAP = false;
        //
        GE_Custom_Form_Ap formAp = formApDao.getByString(
                new GE_Custom_Form_Ap_Sql_005(
                        customer_code,
                        custom_form_type,
                        custom_form_code,
                        custom_form_version,
                        custom_form_data,
                        ap_code,
                        GE_Custom_Form_Ap_Sql_005.RETURN_SQL_OBJ
                ).toSqlQuery()
        );
        if (formAp != null) {
            if (formAp.getAp_who() == null || formAp.getAp_who() != user_code) {
                if (!formAp.getAp_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
                        && !formAp.getAp_status().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED)
                        ) {
                    if (formAp.getRoom_code() == null) {
                        deleteAP = true;
                    } else {
                        //if (ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT)) {
                        CH_RoomDao roomDao = new CH_RoomDao(context);
                        //
                        CH_Room chRoom = roomDao.getByString(
                                new CH_Room_Sql_001(
                                        formAp.getRoom_code()
                                ).toSqlQuery()
                        );
                        //
                        if (chRoom == null) {
                            deleteAP = true;
                        }
//                        } else {
//                            deleteAP = true;
//                        }
                    }
                }
            }
        }
        //
        if (deleteAP) {
            formApDao.remove(
                    new GE_Custom_Form_Ap_Sql_010(
                            formAp.getCustomer_code(),
                            formAp.getCustom_form_type(),
                            formAp.getCustom_form_code(),
                            formAp.getCustom_form_version(),
                            formAp.getCustom_form_data(),
                            formAp.getAp_code()
                    ).toSqlQuery()
            );
            //
            return true;
        }
        //
        return false;
    }

    public static String getFullNick(String nick, int id) {
        return getFullNick(nick, String.valueOf(id));
    }

    public static String getFullNick(String nick, String id) {
        if (nick.contains(" (" + id + ")")) {
            return nick;
        } else {
            return nick + " (" + id + ")";
        }
    }

    public static long pkCustomerCode(String pk) {
        try {
            String[] pks = pk.replace("|", "#").replace(".", "#").split("#");
            //
            return Long.parseLong(pks[0]);
        } catch (Exception e) {
            return -1L;
        }
    }

    public static String removeAllLineBreaks(String text) {
        return text.trim().replaceAll("\\r|\\n", "");
    }

    public static String removeForbidenChars(String text) {
        text = ToolBox_Inf.removeAllLineBreaks(text);
        return text.trim().replaceAll("\\t|\'|\"", "");
    }



    public static ArrayList<HMAux> getMenuProfiles(Context context, String menu_code) {
        ArrayList<HMAux> profiles = new ArrayList<>();
        //
        EV_ProfileDao profileDao = new EV_ProfileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        profiles = (ArrayList<HMAux>) profileDao.query_HM(
                new EV_Profile_Sql_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        menu_code
                ).toSqlQuery()
        );
        //
        return profiles;
    }

    public static String getMenuProfilesAsStringConcat(Context context, String menu_code, String concat) {
        ArrayList<HMAux> profileList = ToolBox_Inf.getMenuProfiles(context, menu_code);
        String profile = "";
        concat = concat == null || concat.isEmpty() ? "|" : concat;
        //
        for (HMAux hmAux : profileList) {
            if (!hmAux.get(EV_ProfileDao.PARAMETER_CODE).isEmpty()) {
                profile += hmAux.get(EV_ProfileDao.PARAMETER_CODE) + concat;
            }
        }
        //Ajusta string removendo pipe no final
        try {
            profile = profile.substring(0, profile.length() - 1);
        } catch (Exception e) {
            profile = "";
        }
        return profile;

    }

    public static boolean checkFormXProductExists(Context context, long customer_code, long product_code) {
        GE_Custom_Form_ProductDao formProductDao =
                new GE_Custom_Form_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        HMAux aux =
                formProductDao.getByStringHM(
                        new Sql_Form_x_Product(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code
                        ).toSqlQuery()
                );
        String hasFormXProduct = aux == null ? "null" : aux.get(Sql_Form_x_Product.FORM_PRODUCT_PROFILE);
        if (hasFormXProduct.equals("0") || hasFormXProduct.equals("null")) {
            return false;
        }
        return true;
    }

    public static boolean checkFormXOperationExists(Context context, long customer_code, long operation_code) {
        GE_Custom_Form_OperationDao formOperationDao =
                new GE_Custom_Form_OperationDao(
                        context,
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        HMAux aux =
                formOperationDao.getByStringHM(
                        new Sql_Form_x_Operation(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                operation_code
                        ).toSqlQuery());
        String hasFormXOperation = aux == null ? "null" : aux.get(Sql_Form_x_Operation.FORM_OPERATION_PROFILE);
        if (hasFormXOperation.equals("0") || hasFormXOperation.equals("null")) {
            return false;
        }
        return true;
    }

    public static boolean checkFormXSiteExists(Context context, long customer_code, String site_code) {
        GE_Custom_Form_SiteDao formSiteDao =
                new GE_Custom_Form_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(customer_code),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        HMAux aux =
                formSiteDao.getByStringHM(
                        new Sql_Form_x_Site(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                site_code
                        ).toSqlQuery());
        String hasFormXSite = aux == null ? "null" : aux.get(Sql_Form_x_Site.FORM_SITE_PROFILE);
        if (hasFormXSite.equals("0") || hasFormXSite.equals("null")) {
            return false;
        }
        return true;
    }


    /**
     * Verifica se existem tokens de seriais ou registros no MD_Product_Serial com status de UPDATE_REQUIRED = 1
     *
     * @param context
     * @return existem arquivos ou registros
     */
    public static boolean checkSerialTokenURStatus(Context context) {
        MD_Product_SerialDao productSerialDao = new MD_Product_SerialDao(context);
        //
        int qty_file_token;
        int qty_UR;

        File[] files =
            getListOfFiles_v5(
                ConstantBaseApp.TOKEN_PATH,
                buildTokenPrefixWithCustomer(ToolBox_Con.getPreference_Customer_Code(context), ConstantBaseApp.TOKEN_SERIAL_PREFIX)
            );

        if (files != null && files.length > 0) {
            qty_file_token = files.length;
        } else {
            qty_file_token = 0;
        }

        try {
            qty_UR = Integer.parseInt(productSerialDao.getByStringHM(
                    new MD_Product_Serial_Sql_015(
                            ToolBox_Con.getPreference_Customer_Code(context)).toSqlQuery()
                    ).get(MD_Product_Serial_Sql_015.IN_TOKEN_UR_QTY)
            );
            //
        } catch (Exception e) {
            qty_UR = 0;
        }

        if ((qty_file_token + qty_UR) > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isValidProduct(MD_Product md_product) {
        //Erro, produto não encontrado
        if (md_product != null && md_product.getProduct_code() > 0) {
            return true;
        }
        return false;
    }

    public static String getDayTranslate(Date date) {
        String dayTrans = "";

        switch (date.getDay()) {
            case 0:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("daySunday");
                break;
            case 1:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayMonday");
                break;
            case 2:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayTuesday");
                break;
            case 3:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayWednesday");
                break;
            case 4:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayThursday");
                break;
            case 5:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("dayFriday");
                break;
            case 6:
                dayTrans = ConstantBase.HMAUX_TRANS_LIB.get("daySaturday");
                break;
            default:
                break;
        }

        return dayTrans;
    }

    public static String getMonthTranslate(Date date) {
        String monthTrans = "";

        switch (date.getMonth()) {
            case 0:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJanuary");
                break;
            case 1:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monFebruary");
                break;
            case 2:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monMarch");
                break;
            case 3:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monApril");
                break;
            case 4:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monMay");
                break;
            case 5:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJune");
                break;
            case 6:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monJuly");
                break;
            case 7:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monAugust");
                break;
            case 8:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monSeptember");
                break;
            case 9:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monOctober");
                break;
            case 10:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monNovember");
                break;
            case 11:
                monthTrans = ConstantBase.HMAUX_TRANS_LIB.get("monDecember");
                break;
            default:
                break;
        }

        return monthTrans;
    }

    public static String convertBytesToFormattedString(long size, boolean accurate_conversion) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;
        //
        if (!accurate_conversion) {
            Kb = 1 * 1000;
            Mb = Kb * 1000;
            Gb = Mb * 1000;
            Tb = Gb * 1000;
            Pb = Tb * 1000;
            Eb = Pb * 1000;
        }

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " PB";
        if (size >= Eb) return floatForm((double) size / Eb) + " EB";

        return "Erro...";
    }

    public static String floatForm(double d) {
        try {
            return new DecimalFormat("#.##").format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * LUCHE - 08/10/2019
     *
     * Formata o price em double do java para formato esperado pela tela
     *
     * @param vDouble - Valor double
     * @return  - Retorno string no formato esperado pela tela.
     */
    public static String formatDoublePriceToScreen(Double vDouble){
        try {
            return (new DecimalFormat("###0.00").format(vDouble)).replace(",", ".");
        } catch (Exception e){
            ToolBox_Inf.registerException(CLASS_NAME,e);
            return "0.00";
        }

    }

    public static File[] getListDB(final String prefix) {
        File fileList = new File(Constant.DB_PATH);
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

    public static File[] getListDB(final String prefix, final boolean excludeJournal) {
        final String suffix = ".db3";
        File fileList = new File(Constant.DB_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix) && (!excludeJournal || filename.endsWith(suffix))) {
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

    /**
     * LUCHE - 22/01/2020
     * Metodo que lista todos os banco na mesma versão de banco do app (DB_VERSION_CUSTOM).
     *
     * Metodo usado na identificação de itens pendentes no processo de atualização de App com troca
     * de banco de dados.
     * @param prefix - "c_"
     * @return - Lista de banco de dados com versão DB_VERSION_CUSTOM
     */
    public static File[] getCurrentDbVersionDbList(final String prefix) {
        final String suffix = Constant.DB_VERSION_CUSTOM+ ".db3";
        File fileList = new File(Constant.DB_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix) && filename.endsWith(suffix)) {
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

    /**
     * LUCHE - 20/12/18
     * Metodo que verifica se existem dados pendentes em uma lista de  banco de dados.
     * A informação de customer code extraida do nome do banco.
     * Se encontrar pendencia em algum customer, retorna true.
     * Metodo usado quando app recebe UPDATE_REQUIRED no retorno dos WS de Sync ou GetCustomer.
     * Metodo executa loop na lista de banco e chama a cada iteração do loop o mesmo metodo,
     * mas com segunda assinatura que verifica as pendencias em um customer especifico
     * @param context - Contexto
     * @param listDB - Lista de bancos de
     * @return
     */
    public static boolean hasPendingData(Context context, File[] listDB) {
        //
        if (listDB == null || listDB.length == 0) {
            return false;
        }
        //
        for (File db : listDB) {
            String[] db_full_name = db.getName().contains("_") ? db.getName().split("_") : new String[]{};
            Long customer_code = db_full_name.length == 3 && db_full_name[1] != null && mLongParse(db_full_name[1]) != null ? mLongParse(db_full_name[1]) : -1L;
            //
            if( customer_code != null && customer_code != -1 ){
                //
                if(hasPendingData(context,customer_code)){
                    return true;
                }
            }
        }
        //
        return false;
    }
    /**
    * LUCHE - 06/01/2020
    *
    * Criado nova versão do metodo hasPendingData, para ao inves de retornar um boolean, retornar
     *String com lista de customers com dados pendentes de envio.
    *
    * @param context - Contexto
    * @param listDB - Lista de bancos
    * @return
     */
    public static String hasPendingDataV2(Context context, File[] listDB) {
        String customer_list = "";
        //
        if (listDB == null || listDB.length == 0) {
            return null;
        }
        //
        for (File db : listDB) {
            String[] db_full_name = db.getName().contains("_") ? db.getName().split("_") : new String[]{};
            Long customer_code = db_full_name.length == 3 && db_full_name[1] != null && mLongParse(db_full_name[1]) != null ? mLongParse(db_full_name[1]) : -1L;
            //
            if( customer_code != null && customer_code != -1){
                //
                if(hasPendingData(context,customer_code)){
                     EV_User_CustomerDao customerDao = getEv_user_customerDao(context);
                    //
                    EV_User_Customer evUserCustomer = customerDao.getByString(
                        new EV_User_Customer_Sql_011(
                            customer_code
                        ).toSqlQuery()
                    );
                    //
                    if(evUserCustomer != null) {
                        customer_list += evUserCustomer.getCustomer_code()+" - "+evUserCustomer.getCustomer_name()+"\n";
                    }
                }
            }
        }
        //
        return !customer_list.equalsIgnoreCase("") ? customer_list.substring(0, customer_list.length() -1) : null ;
    }

    /**
     * LUCHE - 20/12/18
     * Metodo que verifica se existem dados pendentes no banco do customer especificado.
     * São considerados pendentes, itens pendentes de transmissão e N-Form no status IN_PROCESS.
     *
     * LUCHE - 06/01/2020
     * Revisado adicionando a contagem de itens pendentes dos modulos assets e ticket
     *
     * @param context - Context
     * @param customer_code - Codigo do customer
     * @return - Verdadeiro se encontrar algum item pendente de envio.
     */
    public static boolean hasPendingData(Context context, Long customer_code){
        if(customer_code != null && customer_code != -1) {
            /**
             * Pendencia de Serial
             * Banco e Token
             */
            //
            MD_Product_SerialDao mdProductDao = new MD_Product_SerialDao(
                    context,
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //

            HMAux pendencies = mdProductDao.getByStringHM(
                    new Sql_Act005_008(
                            customer_code
                    ).toSqlQuery()
            );

            if( (pendencies != null
                    && pendencies.hasConsistentValue(Sql_Act005_008.BADGE_TO_SEND_QTY)
                    && !pendencies.get(Sql_Act005_008.BADGE_TO_SEND_QTY).equalsIgnoreCase("0"))
                    || isSerialWithinTokenFile(customer_code) > 0
                    ){
                return true;
            }
            /**
             * Pendencias N-Form
             */
            pendencies.clear();
            //
            GE_Custom_Form_LocalDao customFormLocalDao = new GE_Custom_Form_LocalDao(
                    context,
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            pendencies =
                    customFormLocalDao.getByStringHM(
                            new Sql_Act002_001(
                                    String.valueOf(customer_code)
                            ).toSqlQuery()
                    );
            //
            if(pendencies != null
                    && pendencies.hasConsistentValue(Sql_Act002_001.QTY_CUSTOMER_PENDENCIES)
                    && !pendencies.get(Sql_Act002_001.QTY_CUSTOMER_PENDENCIES).equalsIgnoreCase("0")
                ){
                return true;
            }
            //
            /**
             * Pendencias S.O
             */
            pendencies.clear();
            //
            SM_SODao soDao = new SM_SODao(
                    context,
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            pendencies = soDao.getByStringHM(
                    new Sql_Act021_003(
                            customer_code
                    ).toSqlQuery()
            );
            //
            if((pendencies != null &&
                    pendencies.hasConsistentValue(Sql_Act021_003.UPDATE_APPROVAL_REQUIRED_QTY) &&
                    !pendencies.get(Sql_Act021_003.UPDATE_APPROVAL_REQUIRED_QTY).equalsIgnoreCase("0")
            ) || isSoWithinTokenFile(customer_code) > 0
                    ){
                return true;
            }
            /**
             * Pendencias Form AP
             */
            pendencies.clear();
            //
            GE_Custom_Form_ApDao customFormApDao = new GE_Custom_Form_ApDao(
                    context,
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            pendencies = customFormApDao.getByStringHM(
                    new Sql_Act005_007(
                            String.valueOf(customer_code)
                    ).toSqlQuery()
            );
            //
            if(pendencies != null
                    && pendencies.hasConsistentValue(Sql_Act005_007.BADGE_TO_SEND_QTY)
                    && !pendencies.get(Sql_Act005_007.BADGE_TO_SEND_QTY).equalsIgnoreCase("0")
                    ){
                return true;
            }

            /**
             * Pendencias So Pacote Expresso
             */
            pendencies.clear();
            //
            SO_Pack_Express_LocalDao soPackExpressLocalDao = new SO_Pack_Express_LocalDao(
                    context,
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            pendencies = soPackExpressLocalDao.getByStringHM(
                    new SO_Pack_Express_Local_Sql_010(
                            customer_code
                    ).toSqlQuery()
            );
            //
            if(pendencies != null
                    && pendencies.hasConsistentValue(SO_Pack_Express_Local_Sql_010.BADGE_IN_NEW_QTY)
                    && !pendencies.get(SO_Pack_Express_Local_Sql_010.BADGE_IN_NEW_QTY).equalsIgnoreCase("0")
                    ){
                return true;
            }
            /**
             * Pendencias Assets
             */
            pendencies.clear();
            //
            if(!handleAssetsWaitingSync(context,customer_code).equalsIgnoreCase("0")){
                return true;
            }
            //
            /**
             * Pendencias Ticket
             */
            pendencies.clear();
            //
            if(!handleTicketUpdateRequired(context,customer_code).equalsIgnoreCase("0")){
                return true;
            }

        }
        //
        return false;
    }
    /**
     * BARRIONUEVO - 30/04/2020
     * Metodo que contabiliza pendencias de localizacao no N-FORM.
     */
    public static int getLocationPendencies(Context context) {
        GE_Custom_Form_DataDao ge_custom_form_dataDao = new GE_Custom_Form_DataDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        List<GE_Custom_Form_Data> formDataList =  ge_custom_form_dataDao.query(
                new GE_Custom_Form_Data_Sql_006(ToolBox_Con.getPreference_Customer_Code(context)).toSqlQuery()
        );
        if (formDataList != null) {
            return formDataList.size();
        }
        return 0;
    }

    /**
     * Metodo que retorna a qtd de S.O dentro do arquivos token de so
     *
     * @return
     * @param customer_code
     */
    public static int isSoWithinTokenFile(long customer_code) {
        try {
            File[] soToken =
                ToolBox_Inf.getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_SO_PREFIX)
                );
            if (soToken.length > 0) {
                Gson gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
                //
                ArrayList<SM_SO> token_so_list =
                        gsonEnv.fromJson(
                                ToolBox_Inf.getContents(soToken[0]),
                                TSO_Save_Env.class
                        ).getSo();
                //
                return token_so_list.size();
            } else {
                return 0;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            //
            return 0;
        }
    }

    /**
     * Metodo que retorna a qtd de Seriais dentro do arquivos token de Serial
     *
     * @return
     */
    public static int isSerialWithinTokenFile(long customer_code) {
        int qty = 0;
        try {
            File[] serialToken =
                ToolBox_Inf.getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_SERIAL_PREFIX)
                );

            if (serialToken.length > 0) {
                Gson gsonEnv = new GsonBuilder().serializeNulls().create();
                //
                ArrayList<MD_Product_Serial> token_serial_list =
                        gsonEnv.fromJson(
                                ToolBox_Inf.getContents(serialToken[0]),
                                TSerial_Save_Env.class
                        ).getSerial();
                //
                return token_serial_list.size() + qty;
            } else {
                return qty;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            //
            return qty;
        }
    }

    /**
     * LUCHE - 06/01/2020
     *
     * Metodo que retorna a qtd de Tickets dentro do arquivos token de Ticket
     *
     * @return - Qtd de ticket existen no arquivo de token
     * @param customer_code
     */
    public static int getQtyTicketsWithinToken(long customer_code) {
        return getTicketsWithinToken(customer_code).size();
    }

    /**
     * LUCHE  - 06/01/2020
     *
     * Metodo retorna que a lista de tickets presentes no arquivo json ou lista vazio caso não existam
     * arquivos json.
     *
     * @return - Lista dos ticket presente no arquivo de token
     * @param customer_code
     */
    public static ArrayList<TK_Ticket> getTicketsWithinToken(long customer_code) {
        ArrayList<TK_Ticket> token_ticket_list = new ArrayList<>();
        try {
            File[] ticketToken =
                ToolBox_Inf.getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_TICKET_PREFIX)
                );
            if (ticketToken.length > 0) {
                Gson gsonEnv = new GsonBuilder().serializeNulls().create();
                //
                token_ticket_list =
                    gsonEnv.fromJson(
                        ToolBox_Inf.getContents(ticketToken[0]),
                        T_TK_Ticket_Save_Env.class
                    ).getTicket();
                //
                return token_ticket_list != null ? token_ticket_list : new ArrayList<TK_Ticket>();
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
        }
        //
        return token_ticket_list;
    }

    /**
     * LUCHE - 06/01/2020
     *
     * Metodo que trata o retorno  do metodo que conta a qtd de TICKETS pendentes de envio.
     *
     * @param context - Contexto
     * @param customer_code
     * @return - Qty de itens pendentes de envio do modulo N-Ticket
     */
    public static String handleTicketUpdateRequired(Context context, Long customer_code) {
        String qty;//tratar badges de pendentes.
        try {
            qty = getTicketUpdateRequiredCount(context,customer_code);
            //
        } catch (Exception e) {
            qty = "0";
        }
        return qty;
    }

    /**
     * LUCHE - 06/01/2020
     *
     * Metodo contabiliza qtd de tickets pendente de envio, incluindo os existentes no arquivo de token
     *
     * @param context - Contexto
     * @param customer_code
     * @return - Qty de itens pendentes de envio do modulo N-Ticket
     */
    private static String getTicketUpdateRequiredCount(Context context, Long customer_code) {
        TK_TicketDao tk_ticketDao = new TK_TicketDao(context,ToolBox_Con.customDBPath(customer_code), Constant.DB_VERSION_CUSTOM);
        int pendencies=0;

        ArrayList<TK_Ticket> ticketUpdateReq = (ArrayList<TK_Ticket>) tk_ticketDao.query((
                new Sql_WS_TK_Ticket_Save_001(
                    customer_code
                )
            ).toSqlQuery()
        );
        //
        if(ticketUpdateReq != null && ticketUpdateReq.size() > 0
        ){
            try {
                pendencies +=  ticketUpdateReq.size();
            } catch (Exception e) {
                pendencies = 0;
                registerException(CLASS_NAME,e);
            }

        }
        //Ticket do token
        pendencies += ToolBox_Inf.getQtyTicketsWithinToken(customer_code);
        //
        return String.valueOf(pendencies);
    }

    /**
     * LUCHE - 06/01/2020
     *
     * Metodo que trata o retorno  do metodo que conta a qtd de processo de Assets pendentes de envio.
     *
     * Movido originalmente criado na Act005 por Barrionuevo
     * @param context - Contexto
     * @param customer_code
     * @return - Qty de itens pendentes de envio do modulo N-Assets
     */
    public static String handleAssetsWaitingSync(Context context, Long customer_code) {
        String qty;//tratar badges de pendentes.
        try {
            qty = getAssetsWaitingSyncCount(context,customer_code);
            //
        } catch (Exception e) {
            qty = "0";
        }
        return qty;
    }

    /**
     * LUCHE - 06/01/2020
     *
     * Metodo busca em todos os processo do Assets itens pendentes de envio e retorno qtd desses itens.
     * O metodo também contempla itens presentes em arquivos de token
     *
     * Movido originalmente criado na Act005 por Barrionuevo
     *
     * @param context - Contexto
     * @param customer_code
     * @return Qty de itens pendentes de envio do modulo N-Assets
     */
    private static String getAssetsWaitingSyncCount(Context context, Long customer_code) {
        IO_MoveDao assetMoveDao = new IO_MoveDao(context, ToolBox_Con.customDBPath(customer_code), Constant.DB_VERSION_CUSTOM);
        IO_InboundDao assetInboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(customer_code), Constant.DB_VERSION_CUSTOM);
        IO_OutboundDao assetOutboundDao = new IO_OutboundDao(context, ToolBox_Con.customDBPath(customer_code), Constant.DB_VERSION_CUSTOM);
        //
        HMAux moveWaitingSync = assetMoveDao.getByStringHM((
                new IO_Move_Order_Item_Sql_005(
                    customer_code,
                    ConstantBaseApp.IO_PROCESS_MOVE_PLANNED,
                    0
                )
            ).toSqlQuery()
        );
        int pendencies=0;
        if (moveWaitingSync != null && moveWaitingSync.hasConsistentValue(IO_MoveDao.PENDING_QTY)) {
            try {
                pendencies = Integer.valueOf(moveWaitingSync.get(IO_MoveDao.PENDING_QTY));
            } catch (Exception e) {
                pendencies = 0;
                e.printStackTrace();
            }
        }
        //Blind Moves
        HMAux blindWaitingSync = assetMoveDao.getByStringHM((
                new IO_Blind_Move_Sql_006(
                    customer_code
                )
            ).toSqlQuery()
        );
        //
        if (blindWaitingSync != null && blindWaitingSync.hasConsistentValue(IO_Blind_MoveDao.PENDING_QTY)) {
            try {
                pendencies = pendencies + Integer.valueOf(blindWaitingSync.get(IO_Blind_MoveDao.PENDING_QTY));
            } catch (Exception e) {
                //Se exception não faz nada.
                e.printStackTrace();
            }
        }
        //
        ArrayList<HMAux> outboundWaitingSync = (ArrayList<HMAux>) assetOutboundDao.query_HM(
            new IO_Outbound_Sql_013(
                customer_code
            ).toSqlQuery()
        );

        ArrayList<HMAux> inboundWaitingSync = (ArrayList<HMAux>) assetInboundDao.query_HM(
            new IO_Inbound_Sql_013(
                customer_code
            ).toSqlQuery()
        );

        pendencies =
            pendencies
                + outboundWaitingSync.size()
                + inboundWaitingSync.size()
                + ToolBox_Inf.countInboundsInTokenFile(customer_code)
                + ToolBox_Inf.countOutboundsInTokenFile(customer_code)
        ;
        //
        return String.valueOf(pendencies);
    }

    /**
     * LUCHE - 30/04/2019
     *
     * Metodo que verifica se existe arquivo de token de inbound
     *
     * @return
     */
    public static boolean exitsInboundTokenFile(long customer_code) {
        try {
            File[] inboundToken =
                getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_INBOUND_PREFIX)
                );
            //
            return inboundToken.length > 0;
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return false;
        }
    }

    /**
     * LUCHE - 30/04/2019
     *
     * Metodo que verifica se existe arquivo de token de outbound
     *
     * @return
     */
    public static boolean exitsOutboundTokenFile(long customer_code) {
        try {
            File[] outboundToken =
                getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_OUTBOUND_PREFIX)
                );
           return outboundToken.length > 0;
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return false;
        }
    }

    /**
     * LUCHE - 30/07/2019
     *
     * Retorna qtd de inbounds no arquivo de token
     * @return
     */
    public static int countInboundsInTokenFile(long customer_code) {
        try {
            Gson gsonRec = new GsonBuilder().serializeNulls().create();
            File[] inboundToken =
                ToolBox_Inf.getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_INBOUND_PREFIX)
                );
            if(inboundToken.length > 0) {
                //
                T_IO_Inbound_Item_Env inboundList =
                    gsonRec.fromJson(
                        ToolBox_Inf.getContents(inboundToken[0]),
                        T_IO_Inbound_Item_Env.class
                    );
                return inboundList.getInbound().size();
            }else{
                return 0;
            }
            //
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return 0;
        }
    }
    /**
     * LUCHE - 30/07/2019
     *
     * Retorna qtd de outbound no arquivo de token
     * @return
     */
    public static int countOutboundsInTokenFile(long customer_code) {
        try {
            Gson gsonRec = new GsonBuilder().serializeNulls().create();
            File[] outboundToken =
                ToolBox_Inf.getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    buildTokenPrefixWithCustomer(customer_code,ConstantBaseApp.TOKEN_OUTBOUND_PREFIX)
                );
            if(outboundToken.length > 0) {
                T_IO_Outbound_Item_Env outboundList =
                    gsonRec.fromJson(
                        ToolBox_Inf.getContents(outboundToken[0]),
                        T_IO_Outbound_Item_Env.class
                    );
                //
                return outboundList.getOutbound().size();
            }else{
                return 0;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return 0;
        }
    }

    /**
     * LUCHE - 12/08/2019
     * @param brandDesc - Descrição da Marca
     * @param modelDesc - Descrição do Modelo
     * @param colorDesc - Descrição da Cor
     * @return - String formatada ou vazia, caso todos itens nulos
     */
    public static String formatSerialBrandModelColor(String brandDesc, String modelDesc, String colorDesc ) {
        String serialBrandModelColor = brandDesc == null || brandDesc.isEmpty()? "" : brandDesc;
        serialBrandModelColor = serialBrandModelColor + (modelDesc == null || modelDesc.isEmpty()? "" : " | " + modelDesc);
        serialBrandModelColor = serialBrandModelColor + (colorDesc == null || colorDesc.isEmpty()? "" : " | " + colorDesc);
        return serialBrandModelColor;
    }

    /**
     * LUCHE - 12/08/2019
     * @param mdProductSerial - Obj serial
     * @return String formatada ou vazia, caso todos itens nulos ou caso serial null.
     */
    public static String formatSerialBrandModelColor(MD_Product_Serial mdProductSerial) {
        try {
            String serialBrandModelColor = mdProductSerial.getBrand_desc() == null || mdProductSerial.getBrand_desc().isEmpty() ? "" : mdProductSerial.getBrand_desc();
            serialBrandModelColor = serialBrandModelColor + (mdProductSerial.getModel_desc() == null || mdProductSerial.getModel_desc().isEmpty() ? "" : " | " + mdProductSerial.getModel_desc());
            serialBrandModelColor = serialBrandModelColor + (mdProductSerial.getColor_desc() == null || mdProductSerial.getColor_desc().isEmpty() ? "" : " | " + mdProductSerial.getColor_desc());
            return serialBrandModelColor;
        }catch (Exception e){
            ToolBox_Inf.registerException(CLASS_NAME,e);
            return null;
        }
    }

    /**
     * L.BARRINUEVO - 13/08/2019
     * @param context - Context
     * @param serviceClass - Classe do serviço aser verificado.
     * @return - Verdadeiro se serviço estiver rodando.
     */
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static String buildTicketImgPath(TK_Ticket tkTicket) {
        try {
            return ConstantBaseApp.TK_TICKET_PREX_IMG + tkTicket.getCustomer_code() + "_" + tkTicket.getTicket_prefix() + "_" + tkTicket.getTicket_code() + ".jpg";
        }catch (Exception e){
            registerException(CLASS_NAME,e);
            return null;
        }
    }

    /**
     * LUCHE
     * <p>
     * Metodo que devolve nome da imagem baseado no obj de control do ticket
     * </p>
     * LUCHE - 13/03/2020
     * <p>
     * Modificado, extraindo o seu codigo para o novo metodo buildTicketActionImgPath
     * </p>
     * @param ctrl - Controle
     * @return - Retorna nome da imagem formatoda ou null se exception
     */
    @Nullable
    public static String buildTicketActionImgPath(TK_Ticket_Ctrl ctrl) {
//        try{
//            return ConstantBaseApp.TK_TICKET_PREX_IMG + ctrl.getCustomer_code() +"_"+ctrl.getTicket_prefix()+"_"+ctrl.getTicket_code()+"_"+ctrl.getTicket_seq()+ ".png";
//        }catch (Exception e){
//            registerException(CLASS_NAME,e);
//            return null;
//        }
        //LUCHE - 13/03/2020
        //Extraido o codigo acima para novo metodo
        try{
            return buildTicketActionImgPath(ctrl.getCustomer_code(),ctrl.getTicket_prefix(),ctrl.getTicket_code(),ctrl.getTicket_seq());
        }catch (Exception e){
            registerException(CLASS_NAME,e);
            return null;
        }
    }

    /**
     * LUCHE
     * <p>
     * Metodo que devolve nome da imagem baseado no obj de control do ticket
     * </p>
     * LUCHE - 13/03/2020
     * <p>
     * Modificado, extraindo o seu codigo para o novo metodo buildTicketActionImgPath
     * </p>
     * @param action
     * @return
     */
    @Nullable
    public static String buildTicketActionImgPath(TK_Ticket_Action action) {
//        try{
//            return ConstantBaseApp.TK_TICKET_PREX_IMG + action.getCustomer_code() +"_"+action.getTicket_prefix()+"_"+action.getTicket_code()+"_"+action.getTicket_seq()+ ".png";
//        }catch (Exception e){
//            registerException(CLASS_NAME,e);
//            return null;
//        }
        //LUCHE - 13/03/2020
        //Extraido o codigo acima para novo metodo
        try {
            return buildTicketActionImgPath(action.getCustomer_code(), action.getTicket_prefix(), action.getTicket_code(), action.getTicket_seq());
        }catch (Exception e){
            registerException(CLASS_NAME,e);
            return null;
        }
    }

    /**
     * LUCHE - 13/03/2020
     * <p>
     * Metodo que devolve nome da imagem baseado nos itens da pk do ticket_action
     * </p>
     * @param customerCode - Codigo do Customer
     * @param ticket_prefix - Preixo do ticket
     * @param ticket_code - Codigo do ticket
     * @param ticket_seq - Sequencia do Ctrl ou  action
     * @return
     */
    @Nullable
    public static String buildTicketActionImgPath(long customerCode, int ticket_prefix, int ticket_code, int ticket_seq){
        try{
            return ConstantBaseApp.TK_TICKET_PREX_IMG + customerCode +"_"+ticket_prefix+"_"+ticket_code+"_"+ticket_seq+ ".png";
        }catch (Exception e){
            registerException(CLASS_NAME,e);
            return null;
        }
    }

    /**
     * Retorna array de inteiros com a porcentagem width e height relativo ao tamanho da tela
     * Indices:
     *        0 -> Width
     *        1 -> Height
     * @param context - Context
     * @param wPercent - Percentual de largura
     * @param hPercent - Percentual de altura
     * @return - Array int de 2 posições sendo 0 -> Width e 1 -> Height
     */
    public static int[] getPercentageWidthAndHeight(Context context, double wPercent, double hPercent) {
        int[] percentMetrics = getScreenMetrics(context);
        try {
            percentMetrics[0] = (int) (percentMetrics[0] * wPercent);
            percentMetrics[1] = (int) (percentMetrics[1] * hPercent);
        }catch (Exception e){
            registerException(CLASS_NAME,e);
        }
        return percentMetrics;
    }
    /**
     * Retorna array de int com o Width e Height da tela do device
     * Indices:
     *       0 -> Width
     *       1 -> Height
     * @param context - Context
     * @return Array int[2] com valores da tela ou 0 se exception
     */
    public static int[] getScreenMetrics(Context context) {
        int[] metrics = new int[2];
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            metrics[0] = displayMetrics.widthPixels;
            metrics[1] = displayMetrics.heightPixels;
        }catch (Exception e){
            registerException(CLASS_NAME,e);
        }
        //
        return metrics;
    }

    /**
     * Metodo criado para avaliar se a imagem enviada é grande de mais para abrir.
     * Depois de tentar metodo baseado no tamanho maximo do canvas e OpenGL, sem sucesso
     * , foram executado testes em 4 devices com resolução e configurações distintas e todos
     * suportaram a resolução 4k.
     * Sendo assim, esse metodo avalia se o width ou height da imagem é maior que a da resolução
     * 4k(3840).
     *
     * @param path - Caminho para imagem
     * @return Verdadeiro se img menor ou igual a resolução 4k
     */
    public static boolean isImageUnder4kLimit(String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path,options);
            //
            return options.outWidth <= 3840 && options.outHeight <= 3840;
        } catch (Exception e) {
            ToolBox_Inf.registerException(CLASS_NAME, e);
            return false;
        }
    }

    /**
     * LUCHE - 03/01/2020
     * Após lançamento do Android 10, as ações que utilizam uma actvity/app externo para abrir um
     * arquivo do nosso app pararam de funcionar devido a nova mudança de permissões de acesso a arquivos.
     *
     * Esse metodo analisa a versão do Android do Device e retorna a implementação correta para abertura
     * do PDF via app externo.
     *
     * Para a implementação no Android 10+, foi necessario usar o FileProvider com configuração de xml
     * de acesso a diretorios externos e adicionar a flag FLAG_GRANT_READ_URI_PERMISSION
     *
     * LUCHE - 29/01/2021
     * Após mudana do diretorio do pdf para o SandBox em produção, foi removido o IF que condicionava
     * qual metodologia usar, agora todoas as versões usarão o fileprovider.
     *
     * @param context - Contexto
     * @param pathname - Caminho + /+ nome do arquivo
     * @return - Intent usando URI via FileProvider
     */
    public static Intent getOpenPdfIntent(Context context, String pathname){
        Intent intent;
        Uri uri;
        File pdfFile = new File(pathname);
        //
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(Intent.ACTION_VIEW);
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
//        } else {
//            intent = new Intent(Intent.ACTION_VIEW);
//            uri = Uri.fromFile(pdfFile);
//            intent.setDataAndType(uri, "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        }
        //
        return intent;
    }

    //region WS Token Process
    public static String buildTokenPrefixWithCustomer(Context context,String prefix){
        return prefix + ToolBox_Con.getPreference_Customer_Code(context) + "_";
    }

    public static String buildTokenPrefixWithCustomer(long customer_code,String prefix){
        return prefix + customer_code + "_";
    }

    public static String buildTokenFileName(Context context,String prefix,String token){
        return buildTokenPrefixWithCustomer(context,prefix) + token+ ".json";
    }

    public static String buildTokenFileAbsPath(Context context, String prefix,String token,@NonNull String dirPath){
        String tokenFileName = "";
        //
        if(dirPath != null && !dirPath.isEmpty()){
            tokenFileName = dirPath + "/"+ buildTokenFileName(context,prefix,token);
        }
        //
        return tokenFileName;
    }

    public static String buildTokenFileAbsPath(Context context, String prefix, String token){
        return buildTokenFileAbsPath(context,prefix,token,ConstantBaseApp.TOKEN_PATH);
    }

    public static boolean checksumJsonToken(String json_token_content, File jsonToken) {
        String md5Content = ToolBox_Inf.md5(json_token_content);
        //
        String md5File = ToolBox_Inf.md5(ToolBox_Inf.getContents(jsonToken));
        //
        return md5Content.equals(md5File);
    }

    public static File saveTokenAsFile(String tokenFileName, String json_token_content) throws IOException {
        File json_token = new File(tokenFileName);
        ToolBox_Inf.writeIn(json_token_content, json_token);
        return json_token;
    }

    public static File[] checkTokenToSend(Context context, @NonNull String dirPath, String tokenPrefix) {
        tokenPrefix += ToolBox_Con.getPreference_Customer_Code(context);
        return ToolBox_Inf.getListOfFiles_v5(dirPath, tokenPrefix);
    }

    public static boolean deleteFileWithRet(String absolutePath) {
        File file = new File(absolutePath);

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
    //endregion

    /**
     * LUCHE - 10/01/2020
     *
     * Metodo que retorna se o processo de hide serial deve ser usado.
     *
     * O metodo leva primeiramente a existencia do profile PROFILE_PRJ001_PRODUCT_SERIAL_FORCE_NOT_SHOW_SERIAL_INFO
     * caso ele exista, ignora o valor da preferencia.
     * Caso não exista, utilizar o valor da preferencia.
     * @param context
     * @return
     */
    public static boolean hasForceNotShowSerialInfo(Context context) {
        if(ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL, ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL_FORCE_NOT_SHOW_SERIAL_INFO)){
            return true;
        }
        //
        return ToolBox_Con.getPreference_HideSerialInfo(context);
    }

    public static String formatSchedulePk(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        return formatSchedulePk(
            String.valueOf(schedule_prefix),
            String.valueOf(schedule_code),
            String.valueOf(schedule_exec)
        );
    }

    public static String formatSchedulePk(String schedule_prefix, String schedule_code, String schedule_exec) {
        if(
            schedule_prefix == null || schedule_prefix.equalsIgnoreCase("null")
            || schedule_code == null || schedule_code.equalsIgnoreCase("null")
            || schedule_exec == null || schedule_exec.equalsIgnoreCase("null")
        ){
            return "";
        }
        //
        return  schedule_prefix +"."+
                schedule_code +"."+
                schedule_exec ;
    }

    /**
     * LUCHE - 17/02/2020
     *
     * Formata a data do agedamento SEM APLICAR GMT.(Pedido do backend)
     * @param context
     * @param date
     * @return
     */
    public static String formatScheduleDate(Context context, String date) {
        SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        SimpleDateFormat dateFormatOut;
        String format = ToolBox_Inf.nlsDateFormat(context);
        try{
            if( format == null || format.equalsIgnoreCase("")){
                format = "dd-MM-yyyy";
            }
            //
            format += " HH:mm";
            dateFormatOut = new SimpleDateFormat(format);
            //
            return dateFormatOut.format(dateFormatIn.parse(date));
        }catch (Exception e){
            ToolBox_Inf.registerException(CLASS_NAME,e);
            return "01-01-1900";
        }
    }

    /**
     * LUCHE - 17/02/2020
     *
     * Formata a apresentacao de um intervalo de datas:
     *  1) Para dias diferentes:
     *     - "yyyy-MM-dd HH:mm - yyyy-MM-dd HH:mm"
     *  2) Para dias iguais:
     *     - "yyyy-MM-dd HH:mm - HH:mm"
     * @param startDate
     * @param endDate
     * @return
     */
    public static String formatScheduleIntervalDateFormatted(Context context, String startDate, String endDate) {
        SimpleDateFormat dateFormatIn = new SimpleDateFormat(ToolBox_Inf.nlsDateFormat(context) + " HH:mm");
        SimpleDateFormat dateFormatStart = new SimpleDateFormat(ToolBox_Inf.nlsDateFormat(context) + " HH:mm");
        SimpleDateFormat dateFormatEnd = new SimpleDateFormat(ToolBox_Inf.nlsDateFormat(context) + " HH:mm");
        if(checkSameDayDate(startDate, endDate)){
            dateFormatEnd = new SimpleDateFormat("HH:mm");
        }
        try{
            return dateFormatStart.format(dateFormatIn.parse(startDate)) + " - " + dateFormatEnd.format(dateFormatIn.parse(endDate));
        }catch (Exception e){
            ToolBox_Inf.registerException(CLASS_NAME,e);
            return "01-01-1900";
        }
    }

    public static boolean checkSameDayDate(String startDate, String endDate) {
        return startDate.substring(0, 10).equals(endDate.substring(0,10));
    }


    /**
     * LUCHE - 17/02/2020
     *
     * Metodo que avalia se form é um agendado.Considera agendado se a pk do agendamento estiver preenchida
     * @param customFormLocal
     * @return
     */
    public static boolean isScheduleForm(GE_Custom_Form_Local customFormLocal) {
        return customFormLocal != null
            && customFormLocal.getSchedule_prefix() != null
            && customFormLocal.getSchedule_code() != null
            && customFormLocal.getSchedule_exec() != null
            && customFormLocal.getSchedule_prefix() > 0
            && customFormLocal.getSchedule_code() > 0
            && customFormLocal.getSchedule_exec() > 0;
    }

    /**
     * LUCHE - 17/02/2020
     *
     * Metodo que avalia se customFormData é um agendado.Considera agendado se a pk do agendamento estiver preenchida
     * @param customFormData
     * @return
     */
    public static boolean isScheduleForm(GE_Custom_Form_Data customFormData) {
        return customFormData != null
            && customFormData.getSchedule_prefix() != null
            && customFormData.getSchedule_code() != null
            && customFormData.getSchedule_exec() != null
            && customFormData.getSchedule_prefix() > 0
            && customFormData.getSchedule_code() > 0
            && customFormData.getSchedule_exec() > 0;
    }

    /**
     * LUCHE - 27/02/2020
     * Metodo que retorna String formatada com descrição do tipo e descrição do form
     * @param error_process - Obj de retorno do WS_Save quando há mensagem de retorno
     * @return - String formatada
     */
    public static String formatFormErrorDesc(TSave_Rec.Error_Process error_process) {
        return error_process.getCustom_form_type_desc() +"\n" + error_process.getCustom_form_desc();
    }

    /**
     * LUCHE - 27/02/2020
     * Metodo que retorna String formatada com PK + descrição do agendamento
     * @param error_process - Obj de retorno do WS_Save quando há mensagem de retorno
     * @return - String formatada
     */
    public static String formatScheduleErroLabel(TSave_Rec.Error_Process error_process) {
        return error_process.getSchedule_pk() +" - "+ error_process.getSchedule_desc();
    }

    /**
     * LUCHE - 28/02/2020
     * Metodo que retorna Id Descrição do produto formatada
     * @param productId - ID do produto
     * @param productDesc - Descrição do produto
     * @return
     */
    public static String getFormatedProductIdDesc(String productId, String productDesc) {
        if(productId != null && !productId.equalsIgnoreCase("null") && !productId.isEmpty()
            && productDesc != null && !productDesc.equalsIgnoreCase("null") && !productDesc.isEmpty()
        ){
            return productId +" "+ productDesc;
        }
        //
        return productId +" "+ productDesc;
    }

    /**
     * LUCHE - 11/03/2020
     * Metodo que retorna Id Descrição formatada
     * @param objId - ID do obj
     * @param objDesc - Descrição do obj
     * @return
     */
    public static String getFormattedGenericIdDesc(String objId, String objDesc) {
        if(objId != null && !objId.equalsIgnoreCase("null") && !objId.isEmpty()
            && objDesc != null && !objDesc.equalsIgnoreCase("null") && !objDesc.isEmpty()
        ){
            return objId +" - "+ objDesc;
        }
        //
        return objId +" " + objDesc;
    }

    /**
     * LUCHE - 06/03/2020
     *
     * Metodo que verifica se a configuração do produto permite avançar para a criação a tela de
     * seleção de serial.
     *
     * @param auxSchedule - Item regatado do dialog
     * @return - Verdadeiro se se serial não foi informado e se produto permitie criação de serial
     */

    public static boolean productConfigPreventToProceed(HMAux auxSchedule) {
        try {
            boolean followToAct020 =
                 auxSchedule.get(MD_Schedule_ExecDao.SERIAL_ID).isEmpty()
                 && auxSchedule.get(MD_Schedule_ExecDao.ALLOW_NEW_SERIAL_CL).equalsIgnoreCase("1");
            //
            return !followToAct020;
        }catch (Exception e){
            e.printStackTrace();
            registerException(CLASS_NAME,e);
            return false;
        }
    }

    /**
     * LUCHE - 17/03/2020
     * Metodo que formata a exibição de Schedule Pk + Ticket Pk.
     *
     * @param schedulePk - PK concatenada por .
     * @param ticketPrefix - Ticket Prefix
     * @param ticketCode - Ticket Code
     * @return - Dados formatado
     */
    public static String getFormattedTicketSeqExec(String schedulePk, String ticketPrefix, String ticketCode) {
        String formmattedTicketSeqExec =  schedulePk;
        if( ticketPrefix != null && !ticketPrefix.isEmpty()
            && ticketCode != null && !ticketCode.isEmpty()
        ){
            formmattedTicketSeqExec += " ["+ticketPrefix+"."+ticketCode+"]";
        }
        return formmattedTicketSeqExec;
    }

    /**
     * Metodo que faz o agendamento do Work de upload de Imgs
     * Configuração:
     *  - O agendamento deve ser imediato caso a constraint de CONNECTED seja atendida.
     *  - Em caso de falha, faz nova tentativa após 60 segundos.
     *  - Caso acontece de tentar agendar mais uma chamada sendo que ja existe uma pendente,
     *  a nova chamada substituirá a anterior. Nesse caso faz sentido a substituição pois, a troca de
     *  conexão pode fazer o agendamento e não faria sentido esperar pelo tempo de repescagem da
     *  chamada anterior
     */
    public static void scheduleUploadImgWork(Context context){
        Data inputData =
            new Data.Builder().putLong(
                Constant.LOGIN_CUSTOMER_CODE,
                ToolBox_Con.getPreference_Customer_Code(context)
            ).build();
        //
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workUploadImgRequest =
                new OneTimeWorkRequest.Builder(Work_Upload_Img.class)
                    .setInputData(inputData)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        10,
                        TimeUnit.SECONDS
                    )
                    .setConstraints(constraints)
                    .build();
        //
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_Upload_Img.WORKER_TAG,
                ExistingWorkPolicy.REPLACE,
                workUploadImgRequest
            );
    }

    /**
     * Metodo que faz o agendamento do Work de upload de Imgs do chat
     * Configuração:
     *  - O agendamento deve ser imediato caso a constraint de CONNECTED seja atendida.
     *  - Em caso de falha, faz nova tentativa após 60 segundos.
     *  - Caso acontece de tentar agendar mais uma chamada sendo que ja existe uma pendente,
     *  a nova chamada substituirá a anterior. Nesse caso faz sentido a substituição pois, a troca de
     *  conexão pode fazer o agendamento e não faria sentido esperar pelo tempo de repescagem da
     *  chamada anterior
     */
    public static void scheduleUploadOtherUserImgWork(){
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workUploadOtherUsrImgRequest =
            new OneTimeWorkRequest.Builder(Work_Upload_Other_User_Img.class)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    60,
                    TimeUnit.SECONDS
                )
                .setConstraints(constraints)
                .build();
        //
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_Upload_Other_User_Img.WORKER_TAG,
                ExistingWorkPolicy.REPLACE,
                workUploadOtherUsrImgRequest
            );
    }

    public static void scheduleUploadImgChat(){
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workUploadImgChatRequest =
            new OneTimeWorkRequest.Builder(Work_Upload_Img_Chat.class)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    60,
                    TimeUnit.SECONDS
                )
                .setConstraints(constraints)
                .build();
        //
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_Upload_Img_Chat.WORKER_TAG,
                ExistingWorkPolicy.REPLACE,
                workUploadImgChatRequest
            );
    }



    /**
     * Metodo que faz o agendamento do Work verifica os agendamentos a cada 15 min
     * Configuração:
     *  - O agendamento deve ser imediato
     *  - Execução recorrente a cada 15 min sem flexibilidade, pois 15 é o tempo minimo para execução
     *  recorrente
     *  - Em caso de erro, tenta executar novamente depois do intervalo minimo de 15s
     *  - Caso acontece de tentar agendar mais uma chamada sendo que ja existe uma pendente,
     *  a ultima chamada será ignora e a primeira será mantida.
     */
    public static void scheduleQuarterScheduleNotification(){
        //Periodicidade
        //Flexibilidade - "Janela" de permissão para executar mais cedo. Periodicidade - Flexibilidade.(15-5 = 10)A partir de
        PeriodicWorkRequest  workQuarterScheduleNotification =
             new PeriodicWorkRequest.Builder(
                 Work_Quarter_Schedule_Notification.class,
                 15 , TimeUnit.MINUTES //Periodicidade
                 //,5,  TimeUnit.MINUTES //Flexibilidade
             )
             .setBackoffCriteria(
                 BackoffPolicy.LINEAR,
                 PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                 TimeUnit.MILLISECONDS)
             .build();
        //
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                Work_Quarter_Schedule_Notification.WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                workQuarterScheduleNotification
            );
    }

    /**
     * Metodo que faz o agendamento do Work verifica os agendamentos a cada 4 hrs
     * Configuração:
     *  - O agendamento deve ser imediato
     *  - Execução recorrente a cada 4 horas com flexibilidade de 1 horas, ou seja, a partir de
     *  3 hrs pode ser rodado
     *  - Em caso de erro, tenta executar novamente depois do intervalo minimo de 15s
     *  - Caso acontece de tentar agendar mais uma chamada sendo que ja existe uma pendente,
     *  a ultima chamada será ignora e a primeira será mantida.
     */
    public static void schedule4HoursScheduleNotification(){
        //Periodicidade
        //Flexibilidade - "Janela" de permissão para executar mais cedo. Periodicidade - Flexibilidade.(15-5 = 10)A partir de
        PeriodicWorkRequest  work4HoursScheduleNotification =
            new PeriodicWorkRequest.Builder(
                Work_Four_Hour_Schedule_Notification.class,
                4 , TimeUnit.HOURS //Periodicidade
                ,1,  TimeUnit.HOURS //Flexibilidade
            )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build();
        //
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                Work_Four_Hour_Schedule_Notification.WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                work4HoursScheduleNotification
            );
    }

    /**
     * Metodo que faz o agendamento do Work da rotina de limpeza
     * Configuração:
     *  - O agendamento deve ser imediato
     *  - Execução recorrente a cada 12 horas com flexibilidade de 1 horas, ou seja, a partir de
     *  11 hrs pode ser rodado
     *  - Em caso de erro, tenta executar novamente depois de 1 hr
     *  - Caso acontece de tentar agendar mais uma chamada sendo que ja existe uma pendente,
     *  a ultima chamada será ignora e a primeira será mantida.
     *
     */
    public static void scheduleCleanningWork(){
        //Periodicidade
        //Flexibilidade - "Janela" de permissão para executar mais cedo. Periodicidade - Flexibilidade.(15-5 = 10)A partir de
        PeriodicWorkRequest  workCleanningRequest =
            new PeriodicWorkRequest.Builder(
                Work_Cleanning_Data.class,
                12 , TimeUnit.HOURS //Periodicidade
                ,1,  TimeUnit.HOURS //Flexibilidade
            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                1,
                TimeUnit.HOURS
            )
            .build();
        //
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                Work_Cleanning_Data.WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                workCleanningRequest
            );
    }

    /**
     * Metodo que faz o agendamento do Work de download de logo do customer
     * Configuração:
     *  - O agendamento deve ser imediato caso a constraint de CONNECTED seja atendida.
     *  - Em caso de falha, faz nova tentativa após 10 segundos.
     *  - Caso acontece de tentar agendar mais  uma chamada sendo que ja existe uma pendente,
     *  a ultima chamada será ignora e a primeira será mantida.
     * @param context
     */
    public static void scheduleDownloadCustomerLogoWork(Context context){
        Data inputData =
            new Data.Builder()
                .putLong(
                    Constant.LOGIN_CUSTOMER_CODE,
                    ToolBox_Con.getPreference_Customer_Code(context)
                )
                .putString(
                    Constant.LOGIN_USER_CODE,
                    ToolBox_Con.getPreference_User_Code(context)
                )
                .build();
        //
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workDownloadCustomerLogoRequest =
            new OneTimeWorkRequest.Builder(Work_DownLoad_Customer_Logo.class)
                .setInputData(inputData)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10,
                    TimeUnit.SECONDS
                )
                .setConstraints(constraints)
                .build();
        //Testei com ExistingWorkPolicy.REPLACE, mas pode acontecer de ter chamadas concorrente.
        //Apesar de a cada "replace" o worker anterior ser cancelado, o doWork não para de forma
        //instananea e o codigo continuará sendo executado, porem não será feito downlaod por a trativa
        //isStopped(), foi adicionado nos loop
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_DownLoad_Customer_Logo.WORKER_TAG,
                ExistingWorkPolicy.KEEP,
                workDownloadCustomerLogoRequest
            );
    }

    /**
     * Metodo que faz o agendamento do Work de download de PDF
     * Configuração:
     *  - O agendamento deve ser imediato caso a constraint de CONNECTED seja atendida.
     *  - Em caso de falha, faz nova tentativa após 10 segundos.
     *  - Caso acontece de tentar agendar mais  uma chamada sendo que ja existe uma pendente,
     *  a ultima chamada será ignora e a primeira será mantida.
     * @param context
     */

    public static void scheduleDownloadPdfWork(Context context){
        Data inputData =
            new Data.Builder()
                .putLong(
                    Constant.LOGIN_CUSTOMER_CODE,
                    ToolBox_Con.getPreference_Customer_Code(context)
                )
                .build();
        //Define constraint que precisa estar conectado.
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //Cria uma tarefa que não se repete, incluindo os dados de customer no inputData
        //E defindo uma nova tentativa em caso de falha para daqui a 10 segundos.
        OneTimeWorkRequest workDownloadPdfRequest =
            new OneTimeWorkRequest.Builder(Work_DownLoad_PDF.class)
                .setInputData(inputData)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10,
                    TimeUnit.SECONDS
                )
                .setConstraints(constraints)
                .build();
        //
        //Testei com ExistingWorkPolicy.REPLACE, mas pode acontecer de ter chamadas concorrente.
        //Apesar de a cada "replace" o worker anterior ser cancelado, o doWork não para de forma
        //instananea e o codigo continuará sendo executado, porem não será feito downlaod por a trativa
        //isStopped(), foi adicionado nos loop
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_DownLoad_PDF.WORKER_TAG,
                ExistingWorkPolicy.KEEP,
                workDownloadPdfRequest
            );
    }

    /**
     * Metodo que faz o agendamento do Work de download de Imgs
     * Configuração:
     *  - O agendamento deve ser imediato caso a constraint de CONNECTED seja atendida.
     *  - Em caso de falha, faz nova tentativa após 10 segundos.
     *  - Caso acontece de tentar agendar mais  uma chamada sendo que ja existe uma pendente,
     *  a ultima chamada será ignora e a primeira será mantida.
     * @param context
     */
    public static void scheduleDownloadPictureWork(Context context){
        Data inputData =
            new Data.Builder()
                .putLong(
                    Constant.LOGIN_CUSTOMER_CODE,
                    ToolBox_Con.getPreference_Customer_Code(context)
                )
                .build();
        //
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workDownloadPictureRequest =
            new OneTimeWorkRequest.Builder(Work_DownLoad_Picture.class)
                .setInputData(inputData)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10,
                    TimeUnit.SECONDS
                )
                .setConstraints(constraints)
                .build();
        //Testei com ExistingWorkPolicy.REPLACE, mas pode acontecer de ter chamadas concorrente.
        //Apesar de a cada "replace" o worker anterior ser cancelado, o doWork não para de forma
        //instananea e o codigo continuará sendo executado, porem não será feito downlaod por a trativa
        //isStopped(), foi adicionado nos loop
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_DownLoad_Picture.WORKER_TAG,
                ExistingWorkPolicy.KEEP,
                workDownloadPictureRequest
            );
    }
    //TODO Comentar os metodos que chamam os Workers

    /**
     * Metodo que faz o agendamento de todos os workers de downloa
     * @param context
     */
    public static void scheduleAllDownloadWorkers(Context context) {
        ToolBox_Inf.scheduleDownloadPdfWork(context);
        ToolBox_Inf.scheduleDownloadPictureWork(context);
        ToolBox_Inf.scheduleDownloadCustomerLogoWork(context);
    }

    /**
     * Metodo que faz o agendamento do Work verifica msg do chat a cada 15 min
     * Configuração:
     *  - Execução recorrente a cada 15 min sem flexibilidade, pois 15 é o tempo minimo para execução
     *  recorrente
     *  - Verifica se usuário está logado ou com customer válido.
     *  - Em caso de exception o worker tenta de novo.
     *
     */
    public static void scheduleWorkQuarterChatRefresh(){
        //
        Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -  scheduleWorkQuarterChatRefresh \n");
        //Periodicidade
        //
        Constraints networkConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        PeriodicWorkRequest  workQuarterChatRefresh =
                new PeriodicWorkRequest.Builder(
                        Work_Quarter_Chat_Refresh.class,
                        15 , TimeUnit.MINUTES //Periodicidade
                         ,5,  TimeUnit.MINUTES //Flexibilidade
                )
                        .setBackoffCriteria(
                                BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS)
                        .build();
        //
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                        Work_Quarter_Chat_Refresh.WORKER_TAG,
                        ExistingPeriodicWorkPolicy.KEEP,
                        workQuarterChatRefresh
                );
    }

    /**
     * LUCHE - 22/02/2021
     * <p></p>
     * Metodo que agenda o work que subirá o serviço de chat.
     */
    public static void scheduleWorkChatRefresh(){
        //
        Log.d("ChatEvent",ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z") + " -  scheduleWorkChatRefresh \n");
        //Cosntraint de conexão.
        Constraints networkConstraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();
        //
        OneTimeWorkRequest  workQuarterChatRefresh =
            new OneTimeWorkRequest.Builder(
                Work_Chat_Refresh.class
            )
            .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10,
                    TimeUnit.SECONDS
            )
            .setConstraints(networkConstraints).build();
        //Testei com ExistingWorkPolicy.REPLACE, mas pode acontecer de ter chamadas concorrente.
        //Apesar de a cada "replace" o worker anterior ser cancelado, o doWork não para de forma
        //instananea e o codigo continuará sendo executado, porem não será feito downlaod por a trativa
        //isStopped(), foi adicionado nos loop
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_Chat_Refresh.WORKER_TAG,
                ExistingWorkPolicy.KEEP,
                workQuarterChatRefresh
            );
    }

    /**
     * LUCHE
     * <P></P>
     * Metodo que agenda o work que chamará a instancia do firebase e tentará atualiza o firebase_id
     * do usr.
     */
    public static void scheduleFirebaseRegistrationWork() {
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workFirebaseResgistrationRequest =
            new OneTimeWorkRequest.Builder(Work_Firebase_Registration.class)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    5,
                    TimeUnit.MINUTES
                )
                .setConstraints(constraints)
                .build();
        //Testei com ExistingWorkPolicy.REPLACE, mas pode acontecer de ter chamadas concorrente.
        //Apesar de a cada "replace" o worker anterior ser cancelado, o doWork não para de forma
        //instananea e o codigo continuará sendo executado, porem não será feito downlaod por a trativa
        //isStopped(), foi adicionado nos loop
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_Firebase_Registration.WORKER_TAG,
                ExistingWorkPolicy.KEEP,
                workFirebaseResgistrationRequest
            );
    }

    /**
     * LUCHE
     * <P></P>
     * Metodo que agenda o work que do serviço que reporta para o servidor o novo firebaseId
     */
    public static void scheduleFirebaseID_ReportWork() {
        Constraints constraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //
        OneTimeWorkRequest workReportFirebasIdRequest =
            new OneTimeWorkRequest.Builder(Work_Firebase_ID_Report.class)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    5,
                    TimeUnit.MINUTES
                )
                .setConstraints(constraints)
                .build();
        //Testei com ExistingWorkPolicy.REPLACE, mas pode acontecer de ter chamadas concorrente.
        //Apesar de a cada "replace" o worker anterior ser cancelado, o doWork não para de forma
        //instananea e o codigo continuará sendo executado, porem não será feito downlaod por a trativa
        //isStopped(), foi adicionado nos loop
        WorkManager.getInstance()
            .enqueueUniqueWork(
                Work_Firebase_ID_Report.WORKER_TAG,
                ExistingWorkPolicy.KEEP,
                workReportFirebasIdRequest
            );
    }

    /**
     * LUCHE - 08/07/2020
     * <p></p>
     * Metodo que valida string tem um valor diferente de null ou vazia
     * @param value Texto
     * @return True se string != null e != de vazia
     */
    public static boolean hasConsistentValueString(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * LUCHE - 13/07/2020
     * <p></p>
     * Metodo que retorna se o site passado é o mesmo que o site logado.
     * @param context Contexto
     * @param siteCode SiteCode a ser comparado
     * @return Verdadeiro se siteCode != null ,!= vazio e igual ao site da preferencia.
     */
    public static boolean equalsToLoggedSite(Context context, String siteCode){
        return  hasConsistentValueString(siteCode)
                && siteCode.equals(ToolBox_Con.getPreference_Site_Code(context));
    }

    /**
     * LUCHE - 16/07/2020
     * <p></p>
     * Formata a data da seguinte maneira:
     *  - Se apenas data de inicio, data hora
     *  - Se inicio e fim no mesmo dia, exibe data hora_inicio - hora_fim
     *  - Se inicio e fim em dias diferente, exibe das duas datas com hora, data_inicio hora_inicio - data_fim hora_fim
     * @param context
     * @param startDate Data Inicio
     * @param endDate Data Fim
     * @return - Retorna data formata
     */
    public static String getStepStartEndDateFormated(Context context, String startDate, String endDate ){
        SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        SimpleDateFormat dateFormatStart = new SimpleDateFormat(ToolBox_Inf.nlsDateFormat(context) + " HH:mm");
        SimpleDateFormat dateFormatEnd = new SimpleDateFormat(ToolBox_Inf.nlsDateFormat(context) + " HH:mm");
        //
        try {
            if(ToolBox_Inf.hasConsistentValueString(startDate) && ToolBox_Inf.hasConsistentValueString(endDate)){
                if (ToolBox_Inf.checkSameDayDate(startDate, endDate)) {
                    dateFormatEnd = new SimpleDateFormat("HH:mm");
                }
                //
                return dateFormatStart.format(dateFormatIn.parse(startDate)) + " - " + dateFormatEnd.format(dateFormatIn.parse(endDate));
            }else{
                return dateFormatStart.format(dateFormatIn.parse(startDate));
            }
        }catch (Exception e){
            ToolBox_Inf.registerException(CLASS_NAME,e);
            return "01-01-1900";
        }
    }

    /**
     * LUCHE - 20/07/2020
     * <p></p>
     * Metodo que copia o arquivo passado em originalFile para o arquivo passado copyFile,
     * criando o segundo se ele não existir.
     * Copia o arquivo para qualquer diretorio existente.
     *
     * @param originalFile - Arquivo original
     * @param copyFile - Arquivo para qual será copiado.
     * @throws IOException
     * TODO MELHOR ADICIONANDO CRIACAO DE DIR DESTINO?
     */
    public static void copyAndRenameFile(File originalFile, File copyFile) throws IOException {
        if(!copyFile.exists()){
            copyFile.createNewFile();
        }
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(copyFile).getChannel();
            inputChannel = new FileInputStream(originalFile).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            //file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }
    }

    /**
     * LUCHE - 01/09/2020
     * Metodo que retorna String formatada com PK + descrição do agendamento
     * @param error_process - Obj de retorno do WS_Save quando há mensagem de retorno
     * @return - String formatada
     */
    public static String formatTicketErroLabel(TSave_Rec.Error_Process error_process) {
        return error_process.getTicket_step_pk() +" - "+ error_process.getTicket_step_desc();
    }

    /**
     * LUCHE - 01/09/2020
     * <p></p>
     * Gera o HmAux de error baseado no tipo do obj, atualmente, schedule e ticket.
     * @param error_process
     * @return HmAux a ser exibido.
     */
    public static HMAux getWsSaveErrorProcessAuxResult(TSave_Rec.Error_Process error_process) {
        HMAux mHmAux = new HMAux();
        String label = "";
        String type = "";
        String status = "";
        String final_status ="";
        //
        if(TSave_Rec.Error_Process.ERROR_TYPE_TICKET.equals(error_process.getError_type())){
            label = ToolBox_Inf.formatTicketErroLabel(error_process);
            type = TSave_Rec.Error_Process.ERROR_TYPE_TICKET;
        }else{
            label = ToolBox_Inf.formatScheduleErroLabel(error_process);
            type = ConstantBaseApp.SYS_STATUS_SCHEDULE;
        }
        status = error_process.getError();
        final_status = ToolBox_Inf.formatFormErrorDesc(error_process);
        //
        mHmAux.put("label", label);
        mHmAux.put("type", type);
        mHmAux.put("status", status);
        mHmAux.put("final_status", final_status);
        //
        return mHmAux;
    }

    /**
     * LUCHE - 11/09/2020
     * Metodo que define a exição da descrição da origem do ticket, baseada no tipo.
     * Até a presente data, só existe um que deve ser tratado diferente, ma sja deixei as constantes
     * e switch criados pro futuro.
     * @param ticketOriginType Tipo da Origem
     * @param ticketOriginDesc Descricao da origem
     * @return
     */
    @NonNull
    public static String getFormattedTicketOriginDesc(String ticketOriginType, String ticketOriginDesc) {
        if(ticketOriginType == null){
            return "\\" + ticketOriginDesc;
        }
        switch (ticketOriginType){
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE:
                return ticketOriginDesc;
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MEASURE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_SCORE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_NC:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_TRANSFER:
            default:
                return ticketOriginDesc;
        }
    }

    public static Intent getOriginIntent(Context context, String origin_type) {
        switch (origin_type){
            case TK_TICKET_ORIGIN_TYPE_MEASURE:
                return new Intent(context, Act077_Main.class);
            case TK_TICKET_ORIGIN_TYPE_BARCODE:
            case TK_TICKET_ORIGIN_TYPE_MANUAL:
                return new Intent(context, Act078_Main.class);
            case TK_TICKET_ORIGIN_TYPE_FORM:
            case TK_TICKET_ORIGIN_TYPE_FORM_NC:
            case TK_TICKET_ORIGIN_TYPE_FORM_SCORE:
                return new Intent(context, Act079_Main.class);
             case TK_TICKET_ORIGIN_TYPE_SCHEDULE:
                return new Intent(context, Act080_Main.class);
            default:
                return null;
        }
    }

    public static int getScoreFormColor(@NonNull String score_status){

        if(RANGE_RED.equalsIgnoreCase(score_status)){
            return R.color.namoa_color_danger_red;
        }
        if(RANGE_YELLOW.equalsIgnoreCase(score_status)){
            return R.color.namoa_color_yellow_2;
        }
        if(RANGE_GREEN.equalsIgnoreCase(score_status)){
            return R.color.namoa_color_success_green;
        }

        return R.color.namoa_color_gray_chat;

    }

    /**
     * LUCHE - 03/11/2020
     * Metodo que verifica se alguma serial do ticket esta marcado com update required.
     * @param context
     * @param ticket_prefix
     * @param ticket_code
     * @return True se encontrar alguma serial do ticket na condição de atualização
     */
    public static boolean hasSerialUpdateRequiredWithinTicket(Context context, int ticket_prefix, int ticket_code) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        //
        List<MD_Product_Serial> serialList = serialDao.query(
            new MD_Product_Serial_x_TK_Ticket_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticket_prefix,
                ticket_code
            ).toSqlQuery()
        );
        //
        return serialList != null && serialList.size() > 0;
    }

    /**
     * LUCHE - 09/11/2020
     * Metodo que executa obj none pendente ao executar check-in manual no step.
     * Regra solicita em 09/11/2020
     * @param ticketStep
     * @param setStepUpdateOnForceNone - Define se ao "finalizar" um none, deve setar o step como
     * update_required.(faz sentido na chamada da act011)
     */
    public static boolean forceNoneObjToWaitingSync(TK_Ticket_Step ticketStep, boolean setStepUpdateOnForceNone) {
        boolean anyNoneFinalizaed = false;
        if(ticketStep.getCtrl() != null) {
            for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
                if(ConstantBaseApp.TK_TICKET_CRTL_TYPE_NONE.equals(ticketCtrl.getCtrl_type())
                    && ConstantBaseApp.SYS_STATUS_PENDING.equals(ticketCtrl.getCtrl_status())
                ){
                    ticketCtrl.setCtrl_start_date(ticketStep.getStep_start_date());
                    ticketCtrl.setCtrl_start_user(ticketStep.getStep_start_user());
                    ticketCtrl.setCtrl_start_user_name(ticketStep.getStep_start_user_nick());
                    //Sim, o end do ctrl é o start do step, ja que a sua exicução é imediata
                    ticketCtrl.setCtrl_end_date(ticketStep.getStep_start_date());
                    ticketCtrl.setCtrl_end_user(ticketStep.getStep_start_user());
                    ticketCtrl.setCtrl_end_user_name(ticketStep.getStep_start_user_nick());
                    ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
                    ticketCtrl.setUpdate_required(1);
                    //Se flag true, seta o step tb para atualização.
                    if(setStepUpdateOnForceNone){
                        ticketStep.setUpdate_required(1);
                    }
                    //
                    anyNoneFinalizaed = true;
                }
            }
        }
        return anyNoneFinalizaed;
    }

    /**
     * BARRIONUEVO - 18-11-2020
     * Metodo responsavel por verificar a ultima data valida.
     */
    public static boolean isLocalDatetimeOk(Context context) {
        String sDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");
        long currentTimeMillis = ToolBox_Inf.dateToMilliseconds(sDate);
        boolean isDatetimeValid = ToolBox_Con.getBooleanPreferencesByKey(context, ConstantBaseApp.DATETIME_IS_VALID, true);
        long lastValidTime = ToolBox_Con.getLongPreferencesByKey(context, ConstantBaseApp.DATETIME_LAST_VALID_TIME, currentTimeMillis);
        long datetimeTolerance = ToolBox_Con.getLongPreferencesByKey(context, ConstantBaseApp.DATETIME_TOLERANCE, 4200000);

        if(isDatetimeValid) {
            if ((currentTimeMillis + datetimeTolerance) >= lastValidTime) {
                if(currentTimeMillis >= lastValidTime) {
                    ToolBox_Con.setLongPreference(context, ConstantBaseApp.DATETIME_LAST_VALID_TIME, currentTimeMillis);
                }
                return true;
            }
        }

        ToolBox_Con.setBooleanPreference(context, ConstantBaseApp.DATETIME_IS_VALID, false);
        return false;
    }

    /**
     * BARRIONUEVO - 03-12-2020
     * Encapsulamento de configuração de FABMenu do Pipeline.
     * @param context
     * @param fabMenu
     * @param hmAux_Trans
     * @param ticket
     * @param listener
     */
    public static void setPipelineFabMenu(Context context, FabMenu fabMenu, HMAux hmAux_Trans, TK_Ticket ticket, FabMenu.IFabMenu listener) {
        ArrayList<FabMenuItem> fabMenuItems  = initFabMenuItens(context, hmAux_Trans, ticket);
        //
        fabMenu.setFabMenuItens(fabMenuItems);
        fabMenu.setmIcons_Enabled(true);
        fabMenu.setOnFabClickListener(listener);
        fabMenu.refreshDrawableState();
    }

    private static ArrayList<FabMenuItem> initFabMenuItens(Context context, HMAux hmAux_Trans, TK_Ticket ticket) {
        FabMenuItem fabStep;
        FabMenuItem fabProduct;
        FabMenuItem fabOrigin;
        FabMenuItem fabEditHeader;

        ArrayList<FabMenuItem> fabMenuItems = new ArrayList<>();
        int lblBgColor = context.getResources().getColor(R.color.namoa_pipeline_background_icon);
        int lblColor = context.getResources().getColor(R.color.padrao_WHITE);
        int btnBgColor = context.getResources().getColor(R.color.namoa_sync_pipeline_background_btn);
        int iconColor = context.getResources().getColor(R.color.colorPrimary);
        fabMenuItems.clear();
        //atalho para edicao de cabecalho.
        fabEditHeader = new FabMenuItem(context);
        fabEditHeader.setTag(ConstantBaseApp.FAB_TO_HEADER_EDIT_LBL);
        fabEditHeader.setmLabel(hmAux_Trans.get("to_header_edit_lbl"));
        fabEditHeader.setmLabel_Back_Color(lblBgColor);
        fabEditHeader.setmLabel_Text_Color(lblColor);
        fabEditHeader.setmButton_Back_Color(btnBgColor);
        fabEditHeader.setmButton_Resource_Color(iconColor);
        fabEditHeader.setmButton_Resource(R.drawable.ic_baseline_pipeline_header_24);
        fabMenuItems.add(fabEditHeader);
        //atalho para edicao de grupo de trabalho.
        if(ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_CHANGE_WORKGROUP)
        && !ticket.isReadOnly(context)) {
            FabMenuItem fabEditWorkGroup;
            fabEditWorkGroup = new FabMenuItem(context);
            fabEditWorkGroup.setTag(ConstantBaseApp.FAB_TO_WORK_GROUP_EDIT_LBL);
            fabEditWorkGroup.setmLabel(hmAux_Trans.get("to_work_group_edit_lbl"));
            fabEditWorkGroup.setmLabel_Back_Color(lblBgColor);
            fabEditWorkGroup.setmLabel_Text_Color(lblColor);
            fabEditWorkGroup.setmButton_Back_Color(btnBgColor);
            fabEditWorkGroup.setmButton_Resource_Color(iconColor);
            fabEditWorkGroup.setmButton_Resource(R.drawable.ic_account_switch_24dp_black);
            fabMenuItems.add(fabEditWorkGroup);
        }
        //atalaho para origin.
        fabOrigin = new FabMenuItem(context);
        fabOrigin.setTag(ConstantBaseApp.FAB_TO_ORIGIN_LBL);
        fabOrigin.setmLabel(hmAux_Trans.get("to_origin_lbl"));
        fabOrigin.setmLabel_Back_Color(lblBgColor);
        fabOrigin.setmLabel_Text_Color(lblColor);
        fabOrigin.setmButton_Back_Color(btnBgColor);
        fabOrigin.setmButton_Resource_Color(iconColor);
        fabOrigin.setmButton_Resource(R.drawable.ic_baseline_error_outline_24dp_black);
        fabMenuItems.add(fabOrigin);
        //atalho para step
        fabStep = new FabMenuItem(context);
        fabStep.setTag(ConstantBaseApp.FAB_TO_STEP_LBL);
        fabStep.setmLabel(hmAux_Trans.get("to_step_lbl"));
        fabStep.setmLabel_Back_Color(lblBgColor);
        fabStep.setmLabel_Text_Color(lblColor);
        fabStep.setmButton_Back_Color(btnBgColor);
        fabStep.setmButton_Resource_Color(iconColor);
        fabStep.setmButton_Resource(R.drawable.ic_baseline_assignment_24);
        fabMenuItems.add(fabStep);
        //atalaho para produto.
        fabProduct = new FabMenuItem(context);
        fabProduct.setTag(ConstantBaseApp.FAB_TO_PRODUCT_LBL);
        fabProduct.setmLabel(hmAux_Trans.get("to_product_lbl"));
        fabProduct.setmLabel_Back_Color(lblBgColor);
        fabProduct.setmLabel_Text_Color(lblColor);
        fabProduct.setmButton_Back_Color(btnBgColor);
        fabProduct.setmButton_Resource_Color(iconColor);
        fabProduct.setmButton_Resource(R.drawable.ic_baseline_build_24);
        fabMenuItems.add(fabProduct);
        //
        return fabMenuItems;
    }

    /**
     * LUCHE - 13/01/2021
     * Metodo que retorna o obj Ev_user_customer do customer logado.
     * @param context
     * @return
     */
    public static EV_User_Customer getCurrentEvUsrCustomerInfo(Context context) {
        return new EV_User_CustomerDao(
            context,
            Constant.DB_FULL_BASE,
            Constant.DB_VERSION_BASE
        ).getByString(
            new EV_User_Customer_Sql_002(
                ToolBox_Con.getPreference_User_Code(context),
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
            ).toSqlQuery()
        );
    }

    /**
     * LUCHE - 13/01/2021
     * Metodo que retorna o obj site do site logado
     * @param context
     * @return
     */
    private static MD_Site getCurrentSiteObjInfo(Context context) {
        return new MD_SiteDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        ).getByString(
            new MD_Site_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context)
            ).toSqlQuery()
        );
    }

    /**
     * LUCHE - 13/01/2021
     * Metodo que retorna o obj site do site logado
     * @param context
     * @return
     */
    public static MD_Site getSiteObjInfo(Context context, String site_code) {
        return new MD_SiteDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        ).getByString(
            new MD_Site_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                    site_code
            ).toSqlQuery()
        );
    }

    /**
     * LUCHE - 13/01/2021
     * Metodo que verifica se o tipo de licença do customer logado é o tipo de licença por site.
     * @param context
     * @return Verdadeiro se o tipo de licença for LICENSE_CONTROL_TYPE_CONCURRENT_BY_SITE
     */
    public static boolean isConcurrentBySiteLicense(Context context){
        EV_User_Customer userCustomer = getCurrentEvUsrCustomerInfo(context);
        return userCustomer != null && EV_User_CustomerDao.LICENSE_CONTROL_TYPE_CONCURRENT_BY_SITE.equals(userCustomer.getLicense_control_type());
    }

    /**
     * LUCHE - 13/01/2021
     * Metodo que verifica se o site logado possui licença habilitada.
     * @param context
     * @return
     */
    public static boolean isSiteLicenseDisabled(Context context){
        return isSiteLicenseDisabled(context,ToolBox_Con.getPreference_Site_Code(context));
    }
    /**
     * LUCHE - 13/01/2021
     * Metodo que verifica se o site logado possui licença habilitada.
     * @param context
     * @return
     */
    public static boolean isSiteLicenseDisabled(Context context, String site_code){
        MD_Site mdSite = getSiteObjInfo(context, site_code);
        return mdSite != null && mdSite.getLicense_enabled() != null && mdSite.getLicense_enabled() == 0;
    }



    /**
     * LUCHE - 13/01/2021
     * Metodo que calcula se o limite de execuções gratuitas foi atingida
     * Os campos Free_executions_max e Free_executions_count são enviados pelo servidor, ja o campo
     * getApp_executions_count é contabilizado pelo app e para calcular o numero de execuções disponiveis,
     * é necessario somar os 2 campos.
     * @param context
     * @return
     */
    public static boolean hasFreeExecutionAvailable(Context context){
        return hasFreeExecutionAvailable(context, ToolBox_Con.getPreference_Site_Code(context));
    }

    private static boolean hasFreeExecutionAvailable(Context context, String site_code) {
        MD_Site mdSite = getSiteObjInfo(context, site_code);
        if(mdSite != null){
            //Em teste se site tem licença ativa não deveria ser usado esse metodo, uma vez que com
            // licença ativa, NÃO HÁ LIMITE DE EXECUÇÃO, mas fica a tratativa
            if(mdSite.getLicense_enabled() == 1){
                return true;
            }else{
                //Se um dos itens de calculo for null, ja deu algum b.o, retorna falso
                if( mdSite.getFree_executions_max() == null
                        || mdSite.getFree_executions_count() == null
                ){
                    return false;
                }else{
                    /*
                        Se todos campos preenchidos, faz o calculo
                        QtdMax - (QtdExecDoServer + QtdExecDoApp)
                        Se valor maior que zero, tem execução para fazer.
                     */
                    return (mdSite.getFree_executions_max() - (mdSite.getFree_executions_count() + mdSite.getApp_executions_count())) > 0;
                }
            }
        }
        return false;
    }

    /**
     * LUCHE - 14/01/2021
     * Metodo que verifica se o site logado esta bloquado analizando a proprieade License_blocked
     * @param context
     * @return
     */
    public static boolean isCurrentSiteBlockedByExecution(Context context){
        return isCurrentSiteBlockedByExecution(context, ToolBox_Con.getPreference_Site_Code(context));
    }

    private static boolean isCurrentSiteBlockedByExecution(Context context, String site_code) {
        MD_Site mdSite = getSiteObjInfo(context, site_code);
        if(mdSite != null){
            return mdSite.getLicense_blocked() == 1;
        }
        //
        return true;
    }

    /**
     * LUCHE - 14/01/2021
     * Metodo que verifica se site esta bloquado, verificando tando a propriedade License_blocked
     * quando o calculo de execuções disponiveis
     * @param context
     * @return
     */
    public static boolean isSiteBlockedOrLimitExecutionReached(Context context) {

        return isSiteBlockedOrLimitExecutionReached(context, ToolBox_Con.getPreference_Site_Code(context));
    }

    /**
     * LUCHE - 14/01/2021
     * Metodo que verifica se site esta bloquado, verificando tando a propriedade License_blocked
     * quando o calculo de execuções disponiveis
     * @param context
     * @return
     */
    public static boolean isSiteBlockedOrLimitExecutionReached(Context context, String site_code) {
        if(site_code == null){
            return false;
        }
        return
                ToolBox_Inf.isCurrentSiteBlockedByExecution(context, site_code)
                        ||( ToolBox_Inf.isConcurrentBySiteLicense(context)
                        && ToolBox_Inf.isSiteLicenseDisabled(context, site_code)
                        && !ToolBox_Inf.hasFreeExecutionAvailable(context, site_code)
                );
    }
}