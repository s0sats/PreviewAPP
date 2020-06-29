package com.namoadigital.prj001.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Work_DownLoad_Customer_Logo extends Worker {
    public static final String WORKER_TAG = "Work_DownLoad_Customer_Logo";
    public static boolean IS_RUNNING = false;

    public Work_DownLoad_Customer_Logo(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        long customer_code;
        String user_code;
        try {
            Log.d("workerTsts", WORKER_TAG+" :doWork");
            Data inputData = getInputData();
            //
            customer_code = inputData.getLong(Constant.LOGIN_CUSTOMER_CODE,-1);
            user_code = inputData.getString(Constant.LOGIN_USER_CODE);
            //Se parametro de customer não foi enviado, aborta chamada
            if (customer_code == -1L) {
                return Result.success();
            }

            EV_User_CustomerDao userCustomerDao =
                new EV_User_CustomerDao(
                    getApplicationContext(),
                    Constant.DB_FULL_BASE,
                    Constant.DB_VERSION_BASE
                );

            EV_User_Customer userCustomer =
                userCustomerDao.getByString(
                    new EV_User_Customer_Sql_002(
//                                    ToolBox_Con.getPreference_User_Code(getApplicationContext()),
//                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                        user_code,
                        String.valueOf(customer_code)
                    ).toSqlQuery()
                );
            String logo_prefix = "logo_c_" + userCustomer.getCustomer_code();
            //Se imagem existir, sai do serviço sem acionar necessidade de notificação
            if(ToolBox_Inf.verifyDownloadFileInf(logo_prefix + ".png",Constant.IMG_PATH)){
                return Result.success();
            }
            //Se não saiu, verifica necessidade de notificação.
            if (!ToolBox_Inf.isDownloadRunning()) {
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }
            IS_RUNNING = true;
            //
            if (!ToolBox_Inf.verifyDownloadFileInf(logo_prefix + ".png", Constant.IMG_PATH)) {

                ToolBox_Inf.deleteDownloadFileInf(logo_prefix + ".tmp", Constant.IMG_PATH);
                //
                ToolBox_Inf.downloadImagePDF(
                    userCustomer.getLogo_url(),
                    Constant.IMG_PATH + "/" + logo_prefix + ".tmp"
                );
                //Verfica se downlaod esta ok
                if (ToolBox_Inf.verifyImgIntegrity(Constant.IMG_PATH, logo_prefix + ".tmp")) {
                    //Extensão sempre .png ,
                    //pois no android le a imagens independente da extensão
                    ToolBox_Inf.renameDownloadFileInfV2(Constant.IMG_PATH, logo_prefix, "", ".png");
                } else {
                    throw new Exception("ImgIntegrity");
                }
            }
            return Result.success();
        } catch (Exception e) {
            Log.d("workerTsts", WORKER_TAG+" : Exception\n" + e.getMessage());
            ToolBox_Inf.registerException(getClass().getName(), e);
            return Result.failure();
        } finally {
            IS_RUNNING = false;
            //Log.v("WS_Customer_Logo","false");
            if (!ToolBox_Inf.isDownloadRunning()) {
                ToolBox_Inf.cancelNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }
        }
    }
}
