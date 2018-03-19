package com.namoadigital.prj001.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.ui.act034.Act034_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 1/16/18.
 */

public class NotificationReceiver extends BroadcastReceiver {


    public static final String NOTIFICATION = "notification";

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

            boolean mCam = mCamera != null && mCamera.get("camera") != null ? true : false;
            boolean mAct = mActivity != null && mActivity.get(ConstantBaseApp.ACT035) != null ? true : false;

            Intent mIntent = new Intent(context, Act034_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(intent.hasExtra(CH_RoomDao.CUSTOMER_CODE)){
                Bundle bundle = new Bundle();
                bundle.putLong(
                        CH_RoomDao.CUSTOMER_CODE,
                        intent.getLongExtra(
                                CH_RoomDao.CUSTOMER_CODE,
                                ToolBox_Con.getPreference_Customer_Code(context)
                        )
                );
                //
                mIntent.putExtras(bundle);
            }

            if (!mCam && !mAct) {
                mIntent.putExtra(NOTIFICATION, true);
                context.startActivity(mIntent);
            } else {
                mIntent.putExtra(NOTIFICATION, Act034_Main.bTT);
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

    }
}
