package com.namoadigital.prj001.ui;

import android.app.Application;

import com.microblink.MicroblinkSDK;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.BuildConfig;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
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
import static com.namoadigital.prj001.util.ConstantBaseApp.SUPPORT_NAME;
import static com.namoadigital.prj001.util.ConstantBaseApp.SUPPORT_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SERIAL_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SERIAL_PREFIX;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SO_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SO_PREFIX;
import static com.namoadigital.prj001.util.ConstantBaseApp.UNSENT_IMG_PATH;


/**
 * Created by neomatrix on 09/01/17.
 */

public class AppBase extends Application {

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
                ToolBox_Inf.registerException(ex);
                //
                mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);

            }
        });
        //
        // Infra PATH
        DB_PATH = getFilesDir().getPath();

        ZIP_PATH = DB_PATH + "/zips";
        IMG_PATH = DB_PATH + "/imgs";
        THU_PATH = DB_PATH + "/thumbnail";
        SUPPORT_PATH = DB_PATH + "/support";
        TOKEN_PATH = DB_PATH + "/token";
        CHAT_PATH = DB_PATH + "/chat";
        UNSENT_IMG_PATH = IMG_PATH + "/unsentImgs";

        ZIP_NAME = "namoa_sms.zip";
        ZIP_NAME_FULL = ZIP_PATH + "/" + ZIP_NAME;

        SUPPORT_NAME = "support.zip";
        SUPPORT_NAME_FULL = SUPPORT_PATH + "/" + SUPPORT_NAME;
        //Add preenchimento do path do arquivo de support na lib
        ConstantBase.LIB_SUPPORT_PATH = SUPPORT_PATH;

        TOKEN_SO_PREFIX = "so_token_";
        TOKEN_SERIAL_PREFIX = "serial_token_";

        TOKEN_SO_NAME_FULL = TOKEN_PATH +"/" + TOKEN_SO_PREFIX;
        TOKEN_SERIAL_NAME_FULL = TOKEN_PATH +"/" + TOKEN_SERIAL_PREFIX;

        CHAT_PREFIX = "chat_";
        CHAT_NAME_FULL = CHAT_PATH + "/" + CHAT_PREFIX;

        CACHE_PATH = DB_PATH + "/CC_CACHE";
        CACHE_CHAT_PATH = DB_PATH + "/CC_CACHE_CHAT";
        CACHE_PATH_PHOTO = DB_PATH + "/CC_CACHE_PHOTO";
        CACHE_PDF = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE_PDF";

        DB_NAME_BASE = "namoa_sms.db3";
        DB_VERSION_BASE = 9;
        DB_FULL_BASE = DB_PATH + "/" + DB_NAME_BASE;

        DB_NAME_CUSTOM = "cc.db3";
        DB_VERSION_CUSTOM = 37;
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
        Constant.PKG_APP_TYPE_DEFAULT = Constant.PKG_APP_TYPE_MICROBLINK_OCR_VIN;
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

        if (!PKG_CLEAN.equals(PGK_CLEAN_P)) {
            ToolBox_Con.cleanPreferences(getApplicationContext());
            ToolBox_Con.setPreference_PKG_CLEAN(getApplicationContext(), PKG_CLEAN);
        }

        ToolBox_Inf.libTranslation(getApplicationContext());

        Constant.DEVELOPMENT_BASE = ToolBox_Inf.isDevelopmentBase();

        Constant.HM_ICON_NAMOA = R.mipmap.ic_namoa;
        Constant.HM_ICON_NAMOA_GO_ACT021 = "com.namoadigital.prj001.ui.act021.Act021_Main";
        //
        try {
            //Comando para não exibir msg de licence limited time.
            MicroblinkSDK.setShowTimeLimitedLicenseWarning(false);
            MicroblinkSDK.setLicenseFile("MB_com.namoadigital.prj001.production_BlinkID_Android_2020-06-18.mblic", this);
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }

    }
}
