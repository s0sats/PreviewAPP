package com.namoadigital.prj001.ui;

import android.app.Application;
import android.os.Environment;

import static com.namoadigital.prj001.util.Constant.CACHE_PATH;
import static com.namoadigital.prj001.util.Constant.CACHE_PATH_PHOTO;
import static com.namoadigital.prj001.util.Constant.DB_FULL_BASE;
import static com.namoadigital.prj001.util.Constant.DB_FULL_CUSTOM;
import static com.namoadigital.prj001.util.Constant.DB_NAME_BASE;
import static com.namoadigital.prj001.util.Constant.DB_NAME_CUSTOM;
import static com.namoadigital.prj001.util.Constant.DB_PATH;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_BASE;
import static com.namoadigital.prj001.util.Constant.DB_VERSION_CUSTOM;
import static com.namoadigital.prj001.util.Constant.IMG_PATH;
import static com.namoadigital.prj001.util.Constant.THU_PATH;
import static com.namoadigital.prj001.util.Constant.ZIP_NAME;
import static com.namoadigital.prj001.util.Constant.ZIP_NAME_FULL;
import static com.namoadigital.prj001.util.Constant.ZIP_PATH;

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

        //DB_PATH = getFilesDir().getPath();

        ZIP_PATH = DB_PATH + "/zips";;
        IMG_PATH = DB_PATH + "/imgs";
        THU_PATH = DB_PATH + "/thumbnail";

        ZIP_NAME = "namoa_sms.zip";
        ZIP_NAME_FULL = ZIP_PATH + "/" + ZIP_NAME;

        CACHE_PATH = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE";
        //CACHE_PATH = DB_PATH + "/CC_CACHE";

        CACHE_PATH_PHOTO = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE_PHOTO";

        DB_NAME_BASE = "namoa_sms.db3";
        DB_VERSION_BASE = 2;
        DB_FULL_BASE = DB_PATH + "/" + DB_NAME_BASE;

        DB_NAME_CUSTOM = "cc.db3";
        DB_VERSION_CUSTOM = 3;
        DB_FULL_CUSTOM = DB_PATH + "/" + DB_NAME_CUSTOM;

    }
}
