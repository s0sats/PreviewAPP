package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;

import com.namoadigital.prj001.BuildConfig;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.receiver.WBR_UpdateSoftware;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_UpdateSoftware extends IntentService {

    private String l_version_link;
    private String l_version_required;

    public WS_UpdateSoftware() {
        super("WS_UpdateSoftware");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        StringBuilder sb = new StringBuilder();

        try {

            if(!ToolBox_Inf.isDownloadRunning()){
                //Log.v("WS_Customer_Logo","true");
                 WBR_UpdateSoftware.IS_RUNNING = true;
                 ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_DOWNLOAD);
            }

            if (bundle != null) {
                l_version_link = bundle.getString(Constant.SW_LINK);
                l_version_required = bundle.getString(Constant.SW_REQUIRED);

            } else {
                l_version_link = "";
                l_version_required = "";
            }

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_start_update_process_msg), "", "0");

            if (!l_version_link.equalsIgnoreCase("")) {

                processUpdate();

                //ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Update Process...", "", "0");

            } else {

                if (l_version_required.equals("0")) {
                    ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", getString(R.string.generic_error_update_empty_link), "", "0");
                } else {
                    ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_2", getString(R.string.generic_error_update_empty_link), "", "0");
                }
            }


        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            WBR_UpdateSoftware.IS_RUNNING = false;
            WBR_UpdateSoftware.completeWakefulIntent(intent);

            //Log.v("WS_Customer_Logo","false");
            if(!ToolBox_Inf.isDownloadRunning()){
                ToolBox_Inf.cancelNotification(getApplicationContext(),Constant.NOTIFICATION_DOWNLOAD);
            }

        }

    }

    private void processUpdate() throws Exception {

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_starting_download_msg), "", "0");

        //String local_link = "/sdcard/download" + "/" + "namoa.apk";
        //23/08/2018 - Modificado local de download da apk. Teste para resolver problema Zenfone3
        String local_link = Constant.APK_PATH+"/"+Constant.APK_FILE_NAME;

        ToolBox_Inf.deleteDownloadFile(local_link);

        ToolBox_Inf.downloadNewVersion(
                l_version_link,
                local_link
        );
        //Var para chamar intent de atualização
        Intent intent;
        Uri uri;
        //
        try {
            //LUCHE - 26/12/2019
            //Após lançamento do Android 10, o esquema de atualização com Type application/vnd.android.package-archive parou de funcionar.
            //Para corrigir o problema, foi necessario adicionar a verificação da versão API do device
            //adicionando atualização via file provider nas API 24+(7.0+)
            //Adicionado try / catch com registro de exception e msg de erro para o user.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", new File(local_link));
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                uri = Uri.fromFile(new File(local_link));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //
            startActivity(intent);
            //
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_APP", getString(R.string.close_app_update_process_msg), "", "0");
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            //
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", getString(R.string.install_app_error_msg), "", "0");
        }
    }
}
