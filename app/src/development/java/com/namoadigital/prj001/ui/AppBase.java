package com.namoadigital.prj001.ui;

import android.app.Application;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.BuildConfig;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.receiver.WBR_Connections_Change;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import static com.namoa_digital.namoa_library.util.ConstantBase.PKG_CLEAN;
import static com.namoadigital.prj001.util.Constant.CACHE_PATH;
import static com.namoadigital.prj001.util.Constant.CACHE_PATH_PHOTO;
import static com.namoadigital.prj001.util.Constant.CACHE_PDF;
import static com.namoadigital.prj001.util.Constant.DB_FULL_BASE;
import static com.namoadigital.prj001.util.Constant.DB_FULL_CHAT;
import static com.namoadigital.prj001.util.Constant.DB_FULL_CUSTOM;
import static com.namoadigital.prj001.util.Constant.DB_NAME_BASE;
import static com.namoadigital.prj001.util.Constant.DB_NAME_CHAT;
import static com.namoadigital.prj001.util.Constant.DB_NAME_CUSTOM;
import static com.namoadigital.prj001.util.Constant.DB_PATH;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_BASE;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_CHAT;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_CUSTOM;
import static com.namoadigital.prj001.util.Constant.IMG_PATH;
import static com.namoadigital.prj001.util.Constant.SUPPORT_PATH;
import static com.namoadigital.prj001.util.Constant.THU_PATH;
import static com.namoadigital.prj001.util.Constant.ZIP_NAME;
import static com.namoadigital.prj001.util.Constant.ZIP_NAME_FULL;
import static com.namoadigital.prj001.util.Constant.ZIP_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.CACHE_CHAT_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_PREFIX;
import static com.namoadigital.prj001.util.ConstantBaseApp.GENERIC_CHANNEL_ID;
import static com.namoadigital.prj001.util.ConstantBaseApp.PENDENCY_CHANNEL_ID;
import static com.namoadigital.prj001.util.ConstantBaseApp.SUPPORT_NAME;
import static com.namoadigital.prj001.util.ConstantBaseApp.SUPPORT_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SERIAL_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SERIAL_PREFIX;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SO_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SO_PREFIX;
import static com.namoadigital.prj001.util.ConstantBaseApp.UNSENT_IMG_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.TICKET_JSON_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.CUSTOMER_SITE_LICENSE_JSON_PATH;


/**
 * Created by neomatrix on 09/01/17.
 */

public class AppBase extends Application {

    public static final String NAMOA_DIR = "/namoa";
    public static final String ZIPS_DIR = "/zips";
    public static final String IMGS_DIR = "/imgs";
    public static final String THUMBNAIL_DIR = "/thumbnail";
    public static final String SUPPORT_DIR = "/support";
    public static final String TOKEN_DIR = "/token";
    public static final String CHAT_DIR = "/chat";
    public static final String UNSENT_IMGS_DIR = "/unsentImgs";
    public static final String TICKET_DIR = "/ticket";
    public static final String CUSTOMER_SITE_LICENSE_JSON_DIR = "/customerSiteLicense";
    public static final String NAMOA_PEND_INFO = "Namoa Pend. Info.";
    public static final String NAMOA_NOTIF_INFO = "Namoa Notif. Info.";
    private static Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * Tratativa do Callbackl de exceptions não tratadas
         */
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ToolBox_Inf.registerFatalException(ex);
                //
                mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);

            }
        });
        //
        // Infra PATH
        DB_PATH = getApplicationContext().getExternalFilesDir(null) + NAMOA_DIR;
        ZIP_PATH = DB_PATH + ZIPS_DIR;
        IMG_PATH = DB_PATH + IMGS_DIR;
        THU_PATH = DB_PATH + THUMBNAIL_DIR;
        SUPPORT_PATH = DB_PATH + SUPPORT_DIR;
        TOKEN_PATH = DB_PATH + TOKEN_DIR;
        CHAT_PATH = DB_PATH + CHAT_DIR;
        UNSENT_IMG_PATH = IMG_PATH + UNSENT_IMGS_DIR;
        TICKET_JSON_PATH = DB_PATH + TICKET_DIR;
        CUSTOMER_SITE_LICENSE_JSON_PATH = DB_PATH + CUSTOMER_SITE_LICENSE_JSON_DIR;

        ZIP_NAME = "namoa_sms.zip";
        ZIP_NAME_FULL = ZIP_PATH + "/" + ZIP_NAME;

        SUPPORT_NAME = "support.zip";
        SUPPORT_NAME_FULL = SUPPORT_PATH + "/" + SUPPORT_NAME;
        //Add preenchimento do path do arquivo de support na lib
        ConstantBase.LIB_SUPPORT_PATH = SUPPORT_PATH;

        TOKEN_SO_PREFIX = "so_token_";
        TOKEN_SERIAL_PREFIX = "serial_token_";

        TOKEN_SO_NAME_FULL = TOKEN_PATH + "/" + TOKEN_SO_PREFIX;
        TOKEN_SERIAL_NAME_FULL = TOKEN_PATH + "/" + TOKEN_SERIAL_PREFIX;

        CHAT_PREFIX = "chat_";
        CHAT_NAME_FULL = CHAT_PATH + "/" + CHAT_PREFIX;

        CACHE_PATH = getApplicationContext().getExternalFilesDir(null) + "/CC_CACHE";
        CACHE_PATH_PHOTO = getApplicationContext().getExternalFilesDir(null) + "/CC_CACHE_PHOTO";
        CACHE_CHAT_PATH = getApplicationContext().getExternalFilesDir(null) + "/CC_CACHE_CHAT";
        CACHE_PDF = getApplicationContext().getExternalFilesDir(null) + "/CC_CACHE_PDF";
        ConstantBaseApp.APK_PATH = getApplicationContext().getExternalFilesDir(null) + "/namoa_apk";
        ConstantBaseApp.CAM_TEST_PATH = getApplicationContext().getExternalFilesDir(null) + "/camtest";

        DB_NAME_BASE = "namoa_sms.db3";
        DB_VERSION_BASE = 11;
        DB_FULL_BASE = DB_PATH + "/" + DB_NAME_BASE;

        DB_NAME_CUSTOM = "cc.db3";
        DB_VERSION_CUSTOM = 58;
        DB_FULL_CUSTOM = DB_PATH + "/" + DB_NAME_CUSTOM;

        //DB_NAME_CHAT = "namoa_chat.db3";
        DB_VERSION_CHAT = 6;
        DB_FULL_CHAT = DB_PATH + "/" + DB_NAME_CHAT;

        Constant.PRJ001_VERSION_CODE = BuildConfig.VERSION_CODE;
        Constant.PRJ001_VERSION = BuildConfig.VERSION_NAME;

        PKG_CLEAN = String.valueOf(Constant.PRJ001_VERSION);

        String PGK_CLEAN_P = ToolBox_Con.getPreference_PKG_CLEAN(getApplicationContext());
        /**
         * 22/01/2019 - LUCHE
         *
         * Criado var PGK_APP_TYPE_P que identifica qual o "tipo" do app.
         * Por hora, esse tipo, identifica se é flavor com ou sem ocr.
         * Caso esse o tipo mude, significa que o as preferencias devem ser zeradas enviado o user
         * para a tela de login.
         * OBS: OS FLAVOR COM OCR DEVEM TER SETADO COMO PREFERENCIA  PKG_APP_TYPE O VALOR PKG_APP_TYPE_MICROBLINK_OCR_VIN
         * ENQUANTO AS SEM OCR VALOR PKG_APP_TYPE_STANDARD
         */
        Constant.PKG_APP_TYPE_DEFAULT = Constant.PKG_APP_TYPE_STANDARD;
        String PGK_APP_TYPE_P = ToolBox_Con.getPreference_PKG_APP_TYPE(getApplicationContext());
        //
        if (!PKG_CLEAN.equals(PGK_CLEAN_P) || !Constant.PKG_APP_TYPE_DEFAULT.equals(PGK_APP_TYPE_P)) {
            ToolBox_Con.cleanPreferences(getApplicationContext());
            if(!PKG_CLEAN.equals(PGK_CLEAN_P)) {
                ToolBox_Con.setPreference_PKG_CLEAN(getApplicationContext(), PKG_CLEAN);
            }
            //
            if(!Constant.PKG_APP_TYPE_DEFAULT.equals(PGK_APP_TYPE_P)) {
                ToolBox_Con.setPreference_PKG_APP_TYPE(getApplicationContext(), Constant.PKG_APP_TYPE_DEFAULT);
            }
        }

        ToolBox_Inf.libTranslation(getApplicationContext());

        Constant.DEVELOPMENT_BASE = ToolBox_Inf.isDevelopmentBase();

        Constant.HM_ICON_NAMOA = R.mipmap.ic_namoa;
        Constant.HM_ICON_NAMOA_GO_ACT021 = "com.namoadigital.prj001.ui.act021.Act021_Main";
        Constant.HM_ICON_NAMOA_SERVICES = R.drawable.ic_n_service2_24x24;
        ConstantBase.AUTHORITIES_FOR_PROVIDER = BuildConfig.APPLICATION_ID + ".fileprovider";
        /**
         * Migração target28+
         */
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        ToolBox_Inf.createChannelNotification(getApplicationContext(), notificationManager, NAMOA_PEND_INFO, NotificationManager.IMPORTANCE_LOW, PENDENCY_CHANNEL_ID);
        ToolBox_Inf.createChannelNotification(getApplicationContext(), notificationManager, NAMOA_NOTIF_INFO, NotificationManager.IMPORTANCE_DEFAULT, GENERIC_CHANNEL_ID);
        registerConnectionsChanges();
    }

    private void registerConnectionsChanges() {
        WBR_Connections_Change connectionsChange = new WBR_Connections_Change();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionsChange,intentFilter);
    }

}
