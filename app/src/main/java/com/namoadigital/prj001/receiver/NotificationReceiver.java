package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 1/16/18.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(
            !ToolBox_Con.getPreference_User_Code(context).equals("") &&
            ToolBox_Con.getPreference_Customer_Code(context) != -1 &&
            !ToolBox_Con.getPreference_Site_Code(context).equals("-1") &&
            ToolBox_Con.getPreference_Operation_Code(context) != -1
        ) {
//            Intent mIntent = new Intent(context, Act034_Main.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //
//            context.startActivity(mIntent);
        }
    }
}
