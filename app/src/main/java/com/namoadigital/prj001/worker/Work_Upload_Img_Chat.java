package com.namoadigital.prj001.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.CH_FileDao;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_File;
import com.namoadigital.prj001.model.TUploadImg_Chat_Env;
import com.namoadigital.prj001.model.TUploadImg_Chat_Rec;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_File_Sql_001;
import com.namoadigital.prj001.sql.CH_Message_Sql_024;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Work_Upload_Img_Chat extends Worker {
    public static final String WORKER_TAG = "Work_Upload_Img_Chat";
    public static boolean IS_RUNNING = false;

    public Work_Upload_Img_Chat(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG + ": doWork");
        try {
            Gson gson = new Gson();
            TUploadImg_Chat_Env env = new TUploadImg_Chat_Env();
            //
            ArrayList<CH_File> chFiles;
            //
            CH_FileDao chFileDao = new CH_FileDao(
                getApplicationContext()
            );
            //
            CH_MessageDao chMessageDao = new CH_MessageDao(
                getApplicationContext()
            );
            //
            chFiles = (ArrayList<CH_File>) chFileDao.query(
                new CH_File_Sql_001().toSqlQuery()
            );
            //Se lista vazia, não verifica necessidade de notificação de upload
            if(isStopped() || chFiles.size() == 0){
                Log.d("workerTsts", WORKER_TAG + ": Nothing to send");
                return Result.success();
            }
            //
            if (!ToolBox_Inf.isUploadRunning()) {
                //Chama notificação.
                ToolBox_Inf.showNotification(
                    getApplicationContext(),
                    Constant.NOTIFICATION_UPLOAD
                );
            }
            //
            IS_RUNNING = true;
            //
            env.setApp_code(Constant.PRJ001_CODE);
            //
            for (CH_File chFile : chFiles) {
                if(isStopped()){
                    break;
                }
                String val = chFile.getFile_path().replace(".", "_");
                //String[] vals = chFile.getFile_path().split("_");
                String[] vals = val.split("_");
                //
                env.setMsg_prefix(Long.parseLong(vals[0]));
                env.setMsg_code(Long.parseLong(vals[1]));
                env.setSocket_id(SingletonWebSocket.getmSocket_ID());
                //
                String sResults = ToolBox_Inf.uploadFileChat(
                    gson.toJson(env),
                    chFile.getFile_path(),
                    chFile.getFile_path_new()
                );

                TUploadImg_Chat_Rec rec = gson.fromJson(
                    sResults,
                    TUploadImg_Chat_Rec.class
                );

                if (rec.getResult().toUpperCase().contains(("OK"))) {
                    chFile.setFile_status("SENT");
                    chFileDao.addUpdate(chFile);
                    //
                    String pk[] = chFile.getFile_code().replace(".", "#").split("#");

                    chMessageDao.addUpdate(
                        new CH_Message_Sql_024(
                            Integer.parseInt(pk[0]),
                            Long.parseLong(pk[1])
                        ).toSqlQuery()
                    );
                }
                //
                ToolBox_Inf.sendBRChat(getApplicationContext(), Constant.CHAT_BR_TYPE_MSG_IMAGE_ME);
                //Log.d("CHFILE", rec.getResult());
            }
            //
            return Result.success();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
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

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("workerTsts", WORKER_TAG+" : onStopped");
    }
}
