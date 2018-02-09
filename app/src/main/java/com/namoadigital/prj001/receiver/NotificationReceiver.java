package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.ui.act034.Act034_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 1/16/18.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (
                !ToolBox_Con.getPreference_User_Code(context).equals("") &&
                        ToolBox_Con.getPreference_Customer_Code(context) != -1 &&
                        !ToolBox_Con.getPreference_Site_Code(context).equals("-1") &&
                        ToolBox_Con.getPreference_Operation_Code(context) != -1
                ) {


            HMAux mCamera = Camera_Activity.hmActivityStatus;
            HMAux mActivity = Base_Activity.hmActivityStatus;

            boolean mCam = mCamera.get("camera") != null ? true : false;
            boolean mAct = mActivity.get(ConstantBaseApp.ACT035) != null ? true : false;

            Intent mIntent = new Intent(context, Act034_Main.class);

            if (!mCam && !mAct) {
                mIntent.putExtra("gg", true);
                context.startActivity(mIntent);
            } else {
                context.startActivity(mIntent);
                //
                ToolBox.sendBCSpecialStatus(
                        context,
                        ConstantBase.MSG_SPECIAL_NOTIFICATION_CLOSE,
                        "",
                        "camera" + "#" + Constant.ACT035
                );
            }


        }

//        HMAux mCamera = Camera_Activity.hmActivityStatus;
//        HMAux mActivity = Base_Activity.hmActivityStatus;
//
//        boolean mCam = mCamera.get("camera") != null ? true : false;
//        boolean mAct = mActivity.get(ConstantBaseApp.ACT035) != null ? true : false;
//
//        Intent mIntent = new Intent(context, Act034_Main.class);
//
//        if (!mCam && !mAct) {
//            mIntent.putExtra("gg", true);
//            context.startActivity(mIntent);
//        } else {
//            context.startActivity(mIntent);
//            //
//            ToolBox.sendBCSpecialStatus(
//                    context,
//                    ConstantBase.MSG_SPECIAL_NOTIFICATION_CLOSE,
//                    "",
//                    "camera" + "#" + Constant.ACT035
//            );
//        }
    }
}
