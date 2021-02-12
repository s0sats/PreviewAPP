package com.namoadigital.prj001.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.model.TGoogle_Env;
import com.namoadigital.prj001.model.TGoogle_Rec;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Work_Firebase_ID_Report extends Worker {
    public static final String WORKER_TAG = "Work_Firebase_ID_Report";

    public Work_Firebase_ID_Report(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG+" :doWork");

        try {
            if (!ToolBox_Con.getPreference_Session_App(getApplicationContext()).equalsIgnoreCase("")
                && !ToolBox_Con.getPreference_Google_ID(getApplicationContext()).equalsIgnoreCase("")
            ) {
                TGoogle_Env env = new TGoogle_Env();
                env.setApp_code(Constant.PRJ001_CODE);
                env.setApp_version(Constant.PRJ001_VERSION);

                env.setSession_app(
                    ToolBox_Con.getPreference_Session_App(getApplicationContext())
                );
                env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
                env.setGcm_id(
                    ToolBox_Con.getPreference_Google_ID(getApplicationContext())
                );

                Gson gson = new GsonBuilder().serializeNulls().create();

                String sResults = ToolBox_Con.connWebService(
                    Constant.WS_GOOGLE,
                    gson.toJson(env)
                );

                TGoogle_Rec rec = gson.fromJson(
                    sResults,
                    TGoogle_Rec.class
                );

                if (rec.getRet() != null && ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(rec.getRet())) {
                    ToolBox_Con.setPreference_Google_ID_OK(
                        getApplicationContext(),
                        ConstantBaseApp.MAIN_RESULT_OK
                    );
                    return Result.success();
                } else {
                    return Result.retry();
                }
            } else {
                return Result.retry();
            }
        }catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            Log.d("workerTsts", WORKER_TAG+" : Exception\n" + e.getMessage());
            return Result.retry();
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("workerTsts", WORKER_TAG+" : onStopped");
    }
}
