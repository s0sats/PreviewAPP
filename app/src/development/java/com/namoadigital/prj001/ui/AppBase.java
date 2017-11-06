package com.namoadigital.prj001.ui;

import android.app.Application;
import android.os.Environment;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import static com.namoa_digital.namoa_library.util.ConstantBase.PKG_CLEAN;
import static com.namoadigital.prj001.util.Constant.CACHE_PATH;
import static com.namoadigital.prj001.util.Constant.CACHE_PATH_PHOTO;
import static com.namoadigital.prj001.util.Constant.CACHE_PDF;
import static com.namoadigital.prj001.util.Constant.DB_FULL_BASE;
import static com.namoadigital.prj001.util.Constant.DB_FULL_CUSTOM;
import static com.namoadigital.prj001.util.Constant.DB_NAME_BASE;
import static com.namoadigital.prj001.util.Constant.DB_NAME_CUSTOM;
import static com.namoadigital.prj001.util.Constant.DB_PATH;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_BASE;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_CUSTOM;
import static com.namoadigital.prj001.util.Constant.IMG_PATH;
import static com.namoadigital.prj001.util.Constant.SUPPORT_PATH;
import static com.namoadigital.prj001.util.Constant.THU_PATH;
import static com.namoadigital.prj001.util.Constant.ZIP_NAME;
import static com.namoadigital.prj001.util.Constant.ZIP_NAME_FULL;
import static com.namoadigital.prj001.util.Constant.ZIP_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.PKG_CLEAN_APP;
import static com.namoadigital.prj001.util.ConstantBaseApp.SUPPORT_NAME;
import static com.namoadigital.prj001.util.ConstantBaseApp.SUPPORT_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_PATH;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SERIAL_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SERIAL_PREFIX;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SO_NAME_FULL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TOKEN_SO_PREFIX;

/**
 * Created by neomatrix on 09/01/17.
 */

public class AppBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //
        // Infra PATH
        DB_PATH = Environment
                .getExternalStorageDirectory().getPath() + "/namoa";
        ZIP_PATH = DB_PATH + "/zips";
        IMG_PATH = DB_PATH + "/imgs";
        THU_PATH = DB_PATH + "/thumbnail";
        SUPPORT_PATH = DB_PATH + "/support";
        TOKEN_PATH = DB_PATH + "/token";

        ZIP_NAME = "namoa_sms.zip";
        ZIP_NAME_FULL = ZIP_PATH + "/" + ZIP_NAME;

        SUPPORT_NAME = "support.zip";
        SUPPORT_NAME_FULL = SUPPORT_PATH + "/" + SUPPORT_NAME;

        TOKEN_SO_PREFIX = "so_token_";
        TOKEN_SERIAL_PREFIX = "serial_token_";

        TOKEN_SO_NAME_FULL = TOKEN_PATH + "/" + TOKEN_SO_PREFIX;
        TOKEN_SERIAL_NAME_FULL = TOKEN_PATH + "/" + TOKEN_SERIAL_PREFIX;

        CACHE_PATH = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE";
        CACHE_PATH_PHOTO = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE_PHOTO";
        CACHE_PDF = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE_PDF";

        DB_NAME_BASE = "namoa_sms.db3";
        DB_VERSION_BASE = 7;
        DB_FULL_BASE = DB_PATH + "/" + DB_NAME_BASE;

        DB_NAME_CUSTOM = "cc.db3";
        DB_VERSION_CUSTOM = 18;
        DB_FULL_CUSTOM = DB_PATH + "/" + DB_NAME_CUSTOM;

        PKG_CLEAN = PKG_CLEAN_APP;

        String PGK_CLEAN_P = ToolBox_Con.getPreference_PKG_CLEAN(getApplicationContext());

        if (!PKG_CLEAN.equals(PGK_CLEAN_P)) {
            ToolBox_Con.cleanPreferences(getApplicationContext());
            ToolBox_Con.setPreference_PKG_CLEAN(getApplicationContext(), PKG_CLEAN);
        }

        ToolBox_Inf.libTranslation(getApplicationContext());

        Constant.DEVELOPMENT_BASE = ToolBox_Inf.isDevelopmentBase();

        Constant.HM_ICON_NAMOA = R.mipmap.ic_namoa;
        Constant.HM_ICON_NAMOA_GO_ACT021 = "com.namoadigital.prj001.ui.act021.Act021_Main";
        Constant.HM_ICON_NAMOA_SERVICES = R.drawable.ic_n_service2_24x24;

    }
}
