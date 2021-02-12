package com.namoadigital.prj001.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Work_Firebase_Registration  extends Worker {
    public static final String WORKER_TAG = "Work_Firebase_Registration";

    public Work_Firebase_Registration(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG+" :doWork");
        try {
            FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            ToolBox_Inf.registerException(getClass().getName(),task.getException());
                            Log.d("ID_GOOGLE", "getInstanceId failed", task.getException());
                            return;
                        }
                        String sToken = task.getResult().getToken();
                        //LUCHE - 16/09/2020
                        //Com a nova logica de sempre chamar o getInstanceId() na act005, a
                        //atualização da preferencia e chama da do Ws deve ser feita apenas
                        //se o token retornado for diferente da preferencia, indicando que
                        //houve a troca.
                        if(!ToolBox_Con.getPreference_Google_ID(getApplicationContext()).equalsIgnoreCase(sToken)) {
                            //
                            ToolBox_Con.setPreference_Google_ID(
                                getApplicationContext(),
                                sToken);
                            //Log.d("ID_GOOGLE", sToken);
                            ToolBox_Inf.scheduleFirebaseID_ReportWork();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToolBox_Inf.registerException(getClass().getName(),e);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        ToolBox_Inf.registerException(getClass().getName(), new Exception("addOnCanceledListener"));
                    }
                });
            //}
            return Result.success();
        } catch (Exception e) {
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
