package com.namoadigital.prj001.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.namoadigital.prj001.model.TUploadImg_Env;
import com.namoadigital.prj001.model.TUploadImg_Rec;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;

public class Work_Upload_Other_User_Img extends Worker {
    public static final String WORKER_TAG = "Work_Upload_Other_User_Img";
    public static boolean IS_RUNNING = false;

    public Work_Upload_Other_User_Img(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Gson gson = new Gson();
            TUploadImg_Env env = new TUploadImg_Env();
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            //
            File[] unsentImgs = new File(ConstantBaseApp.UNSENT_IMG_PATH).listFiles();
            //SE NENHUM ITEM A ENVIAR, SAI DO SERVIÇO SEM CHAMAR NOTIFICAÇÃO
            if (isStopped() || unsentImgs.length == 0) {
                return Result.success();
            }
            //Verifica necessida de notificação de upload
            if (!ToolBox_Inf.isUploadRunning()) {
                //Chama notificação.
                ToolBox_Inf.showNotification(
                    getApplicationContext(),
                    Constant.NOTIFICATION_UPLOAD
                );
            }

            IS_RUNNING = true;
            //
            for (File file : unsentImgs) {
                if(isStopped()){
                    break;
                }
                env.setFile_path(file.getName());
                //
                String sResults = ToolBox_Inf.uploadFileUnsentImg(
                    gson.toJson(env),
                    ConstantBaseApp.UNSENT_IMG_PATH,
                    file.getName(),
                    null
                );

                TUploadImg_Rec rec = gson.fromJson(
                    sResults,
                    TUploadImg_Rec.class
                );

                if (rec.getSave().equalsIgnoreCase("OK")) {
                    //Não há necessidade de verificar o delete.
                    file.delete();
                }
            }
            //
            return Result.success();
        } catch (Exception e) {
            Log.d("workerTsts", WORKER_TAG+" : Exception\n" + e.getMessage());
            ToolBox_Inf.registerException(getClass().getName(), e);
            return  Result.retry();
        } finally {
            IS_RUNNING = false;
            if (!ToolBox_Inf.isUploadRunning()) {
                ToolBox_Inf.cancelNotification(getApplicationContext(), Constant.NOTIFICATION_UPLOAD);
            }
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("workerTsts", WORKER_TAG+" : onStopped");
    }
}
