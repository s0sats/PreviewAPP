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
    public void executeLoginProcess(String user, String password, String nfc, int status, int status_jump) {
        Intent mIntent = new Intent(context, WBR_Access.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GC_USER_CODE, user);
        bundle.putString(Constant.GC_PWD, password);
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putInt(Constant.GC_STATUS, status);
        bundle.putInt(Constant.GC_STATUS_JUMP, status_jump);
        bundle.putInt(Constant.USER_CUSTOMER_CODE, 0);
        bundle.putInt(Constant.USER_TYPE, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void validateLogin(String login, String password, String nfc_code) {

        if (nfc_code.trim().length() == 0) {

            if (login.trim().length() == 0) {
                mView.showAlertMsg("Form", "Error: Email is Required!!!");
                mView.fieldFocus(Act001_Main.ET_LOGIN);
                return;
            }
            //
            if (password.trim().length() == 0) {
                mView.showAlertMsg("Form", "Error: PassWord is Required!!!");
                mView.fieldFocus(Act001_Main.ET_PASSWORD);
                return;
            }
        }
        //Verifica se exite conexao antes de chamar WS
        //if(ToolBox_Inf.isOnline){
        executeLoginProcess(login, password, nfc_code, 0, 0);
        //}

    }
}
