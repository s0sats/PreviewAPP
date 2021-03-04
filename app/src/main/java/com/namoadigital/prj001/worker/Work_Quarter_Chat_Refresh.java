package com.namoadigital.prj001.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.util.ToolBox_Inf;

import static com.namoadigital.prj001.util.ConstantBaseApp.CHAT_SERVICE_MODE_SCHEDULED;

public class Work_Quarter_Chat_Refresh extends Worker {
    public static final String WORKER_TAG = "Work_Quarter_Chat_Refresh";

    public Work_Quarter_Chat_Refresh(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("ChatEvent", WORKER_TAG+" :doWork");
        try {
            Log.d("ChatEvent"," doWork \n");
            if (!ToolBox_Inf.isUsrAppLogged(getApplicationContext())) {
                Log.d("ChatEvent"," notLogged \n");
                return Result.success();
            }

            if(!AppBackgroundService.isRunning){
                ToolBox_Inf.callChatService(getApplicationContext(), CHAT_SERVICE_MODE_SCHEDULED);
            }
            Log.d("ChatEvent"," AppBackgroundService.isRunning: " + AppBackgroundService.isRunning);
            Log.d("ChatEvent"," success ");

            return Result.success();
        } catch (Exception e) {
            Log.d("ChatEvent", WORKER_TAG + " : Exception\n" + e.getMessage());
            ToolBox_Inf.registerException(getClass().getName(),e);
            /*
                Barrionuevo 18-02-2021
                Sucesso para evitar loop de erros com retry.
             */
            return Result.success();
        }
    }

}
