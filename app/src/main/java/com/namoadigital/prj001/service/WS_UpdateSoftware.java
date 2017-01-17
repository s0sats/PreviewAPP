package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.UserDao;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_UpdateSoftware;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_UpdateSoftware extends IntentService {

    private String l_version_link;
    private String l_version_required;

    private StringBuilder sResult;

    public WS_UpdateSoftware() {
        super("WS_UpdateSoftware");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        StringBuilder sb = new StringBuilder();

        try {

            if (bundle != null) {
                l_version_link = bundle.getString(Constant.SW_LINK);
                l_version_required = bundle.getString(Constant.SW_REQUIRED);

            } else {
                l_version_link = "";
                l_version_required = "";
            }

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Starting Update Process...", "", "0");

            if (!l_version_link.equalsIgnoreCase("")) {

                processUpdate();

                ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Ending Update Process...", "", "0");


            } else {

                if (l_version_required.equals("0")) {
                    //sendBroadCastStatus(Constantes.SWAKESERVICEVERSIONUPDATE_STATUS_ERROR_GO, "Process End " + " - " + SystemClock.elapsedRealtime() + "\n");
                } else {
                    //sendBroadCastStatus(Constantes.SWAKESERVICEVERSIONUPDATE_STATUS_ERROR_END, "Process End " + " - " + SystemClock.elapsedRealtime() + "\n");
                }
            }


        } catch (Exception e) {

            String results = "ERROR: ";

            if (e.toString().contains("JsonSyntaxException")) {
                results += "JsonParse - " + sResult.toString();
                sb.append(results);

            } else {
                sb.append(results)
                        .append(e.toString());
            }

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_UpdateSoftware.completeWakefulIntent(intent);

        }

    }

    private void processUpdate() throws Exception {

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Starting Download...", "", "0");

        String local_link = "/sdcard/download" + "/" + "namoa.apk";

        ToolBox_Inf.deletarDownloadFile(local_link);

        ToolBox_Inf.downloadNewVersion(
                l_version_link,
                local_link
        );

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE", "Close App for Update Process...", "", "0");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.fromFile(
                        new File(local_link)),
                "application/vnd.android.package-archive"
        );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }

}
