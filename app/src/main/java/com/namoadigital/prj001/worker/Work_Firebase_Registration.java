package com.namoadigital.prj001.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Work_Firebase_Registration  extends ListenableWorker {
    public static final String WORKER_TAG = "Work_Firebase_Registration";

    public Work_Firebase_Registration(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {

        return CallbackToFutureAdapter.getFuture(completer -> FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            ToolBox_Inf.registerException(getClass().getName(), task.getException());
                            return;
                        }
                        String sToken = task.getResult();
                        //LUCHE - 16/09/2020
                        //Com a nova logica de sempre chamar o getInstanceId() na act005, a
                        //atualização da preferencia e chama da do Ws deve ser feita apenas
                        //se o token retornado for diferente da preferencia, indicando que
                        //houve a troca.
                        if (
                                !ToolBox_Con.getPreference_Google_ID(getApplicationContext()).equalsIgnoreCase(sToken)
                                        || !ToolBox_Con.getPreference_Google_ID_OK(getApplicationContext()).equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_OK)
                        ) {
                            //
                            ToolBox_Con.setPreference_Google_ID(
                                    getApplicationContext(),
                                    sToken);
                            //
                            ToolBox_Con.setPreference_Google_ID_OK(
                                    getApplicationContext(),
                                    ""
                            );
                            //Log.d("ID_GOOGLE", sToken);
                            ToolBox_Inf.scheduleFirebaseID_ReportWork(getApplicationContext());
                            completer.set(Result.success());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToolBox_Inf.registerException(getClass().getName(), e);
                        completer.set(Result.retry());
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        ToolBox_Inf.registerException(getClass().getName(), new Exception("addOnCanceledListener"));
                        completer.set(Result.retry());
                    }
                }));
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }
}
