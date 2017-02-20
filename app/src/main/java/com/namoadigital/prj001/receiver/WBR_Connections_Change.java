package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.namoadigital.prj001.service.WS_DownLoad_PDF;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 20/02/17.
 */

public class WBR_Connections_Change extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String status = ToolBox_Con.checkConStatus(context);

        if (!status.equalsIgnoreCase("NO_SERVICE")) {
            if (!ToolBox_Con.getPreference_Service(context).equals("NO_SERVICE")) {
                activateUpload(context);
            }
        }

        Toast.makeText(
                context,
                status,
                Toast.LENGTH_SHORT
        ).show();


    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
