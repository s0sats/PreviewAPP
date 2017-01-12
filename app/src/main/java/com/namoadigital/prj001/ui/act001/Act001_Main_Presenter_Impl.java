package com.namoadigital.prj001.ui.act001;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.receiver.WBR_Access;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Act001_Main_Presenter_Impl implements Act001_Main_Presenter {

    private Context context;

    private Act001_Main_View mView;

    public Act001_Main_Presenter_Impl(Context context, Act001_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void executeLoginProcess(String user, String password, String nfc, int status) {
        Intent mIntent = new Intent(context, WBR_Access.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, user);
        bundle.putString(Constant.USER_PWD, password);
        bundle.putString(Constant.USER_NFC, nfc);
        bundle.putInt(Constant.USER_STATUS, status);
        bundle.putInt(Constant.USER_TYPE, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
