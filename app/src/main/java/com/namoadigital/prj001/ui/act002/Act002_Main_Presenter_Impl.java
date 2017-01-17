package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.receiver.WBR_Access;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main_Presenter_Impl implements Act002_Main_Presenter {

    private Context context;

    private Act002_Main_View mView;

    public Act002_Main_Presenter_Impl(Context context, Act002_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }


    @Override
    public void getAllCustomers() {

    }

    @Override
    public void executeSyncProcess(String user, String password, String nfc, long customer_code, int status) {
        Intent mIntent = new Intent(context, WBR_Access.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GC_USER_CODE, user);
        bundle.putString(Constant.GC_PWD, password);
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putInt(Constant.GC_STATUS, status);
        bundle.putLong(Constant.USER_CUSTOMER_CODE, customer_code);
        bundle.putInt(Constant.USER_TYPE, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
