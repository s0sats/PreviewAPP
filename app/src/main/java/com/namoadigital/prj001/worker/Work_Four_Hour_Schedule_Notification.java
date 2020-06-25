package com.namoadigital.prj001.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Work_Four_Hour_Schedule_Notification extends Worker {
    public static final String WORKER_TAG = "Work_Four_Hour_Schedule_Notification";

    public Work_Four_Hour_Schedule_Notification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG+" :doWork");
        try {
            long customer_code = -1L;
            customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());
            /*
                BARRIONUEVO - 05/12/2019
                Tratativa para nao executar servicos quando deslogado
             */
            //Se parametro de customer na preferencias estiver igual a -1 não gera notification.
            if (customer_code == -1L) {
                return Result.success();
            }
            ToolBox_Inf.generateNotification(getApplicationContext(),
                ConstantBaseApp.ALARM_REQUEST_CODE_WS_AL_FULL
            );
            return Result.success();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            return Result.retry();
        }
    }
}
