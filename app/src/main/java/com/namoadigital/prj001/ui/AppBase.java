package com.namoadigital.prj001.ui;

import android.app.Application;
import android.os.Environment;

import static com.namoadigital.prj001.util.Constant.CACHE_PATH;
import static com.namoadigital.prj001.util.Constant.CACHE_PATH_PHOTO;
import static com.namoadigital.prj001.util.Constant.DB_NAME;
import static com.namoadigital.prj001.util.Constant.DB_PATH;
import static com.namoadigital.prj001.util.Constant.DB_PATH_ZIP;
import static com.namoadigital.prj001.util.Constant.DB_VERSION;
import static com.namoadigital.prj001.util.Constant.DB_ZIP;
import static com.namoadigital.prj001.util.Constant.IMG_PATH;
import static com.namoadigital.prj001.util.Constant.THU_PATH;
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

        DB_PATH_ZIP = DB_PATH + "/zips";
        ZIP_PATH = DB_PATH_ZIP;
        IMG_PATH = DB_PATH + "/imgs";
        THU_PATH = DB_PATH + "/thumbnail";

        DB_NAME = Environment
                .getExternalStorageDirectory().getPath() + "/namoa"
                + "/namoa_sms.db3";
        //DB_NAME = "namoa_sms.db3";

        DB_VERSION = 1;

        DB_ZIP = DB_PATH_ZIP + "/namoa_sms.zip";
        //
        CACHE_PATH = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE";
        //CACHE_PATH = DB_PATH + "/CC_CACHE";

        CACHE_PATH_PHOTO = System.getenv("EXTERNAL_STORAGE") + "/CC_CACHE_PHOTO";
    }
}
