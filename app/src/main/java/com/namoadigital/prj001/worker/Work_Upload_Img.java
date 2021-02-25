package com.namoadigital.prj001.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.TUploadImg_Env;
import com.namoadigital.prj001.model.TUploadImg_Rec;
import com.namoadigital.prj001.sql.GE_File_Sql_001;
import com.namoadigital.prj001.sql.GE_File_Sql_007;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Work_Upload_Img extends Worker {
    public static final String WORKER_TAG = "Work_Upload_Img";
    public static boolean IS_RUNNING = false;

    public Work_Upload_Img(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG + ": doWork");
        long customer_code = -1L;
        try {
            customer_code = getInputData().getLong(Constant.LOGIN_CUSTOMER_CODE,-1);
            if(customer_code == -1){
                return  Result.success();
            }
            Gson gson = new Gson();
            TUploadImg_Env env = new TUploadImg_Env();
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            //
            ArrayList<GE_File> geFiles;
            //
            GE_FileDao geFileDao = new GE_FileDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
            //
            geFiles = (ArrayList<GE_File>) geFileDao.query(
                new GE_File_Sql_001().toSqlQuery()
            );
            //SE NENHUM ITEM A ENVIAR, SAI DO SERVIÇO SEM CHAMAR NOTIFICAÇÃO
            if (geFiles.size() == 0) {
                Log.d("workerTsts", WORKER_TAG + ": Nothing to send");
                return Result.success();
            }
            //Verifica necessida de notificação de upload
            if (!ToolBox_Inf.isUploadRunning()) {
                //WBR_Upload_Img.IS_RUNNING = true;
                //Chama notificação.
                ToolBox_Inf.showNotification(
                    getApplicationContext(),
                    Constant.NOTIFICATION_UPLOAD
                );
            }

            IS_RUNNING = true;
            //
            for (GE_File geFile : geFiles) {
                GE_File curGeFile = getCurrentFileReg(geFileDao,geFile);
                //
                if(curGeFile == null){
                    curGeFile = geFile;
                }
                String sRealFileName = curGeFile.getFile_path_new() != null ? curGeFile.getFile_path_new() : curGeFile.getFile_path();
                //
                if (ToolBox_Inf.verifyFileExists(sRealFileName)) {
                    env.setFile_path(curGeFile.getFile_path());
                    //
                    String sResults = ToolBox_Inf.uploadFile(
                        gson.toJson(env),
                        curGeFile.getFile_path(),
                        curGeFile.getFile_path_new()
                    );

                    TUploadImg_Rec rec = gson.fromJson(
                        sResults,
                        TUploadImg_Rec.class
                    );

                    if (rec.getSave().equalsIgnoreCase("OK")) {
                        curGeFile.setFile_status("SENT");
                        geFileDao.addUpdate(curGeFile);
                    }
                } else {
                    curGeFile.setFile_status("FILE_NOT_FOUND");
                    geFileDao.addUpdate(curGeFile);
                }
            }
            //
            Log.d("workerTsts", WORKER_TAG + ": Img sent");
            return Result.success();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            Log.d("workerTsts", WORKER_TAG + ": Error retry");
            return Result.retry();
        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext());
            IS_RUNNING = false;
            //
            if (!ToolBox_Inf.isUploadRunning()) {
                ToolBox_Inf.cancelNotification(getApplicationContext(), Constant.NOTIFICATION_UPLOAD);
            }
        }

    }

    private GE_File getCurrentFileReg(GE_FileDao geFileDao, GE_File geFile) {
        return geFileDao.getByString(
            new GE_File_Sql_007(
                geFile.getFile_code()
            ).toSqlQuery()
        );
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("workerTsts", WORKER_TAG + ": onStopped");
    }
}
