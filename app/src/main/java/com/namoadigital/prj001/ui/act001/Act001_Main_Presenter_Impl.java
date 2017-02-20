package com.namoadigital.prj001.ui.act001;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
    public void executeLoginProcess(String user, String password, String nfc, int status_jump) {
        Intent mIntent = new Intent(context, WBR_GetCustomer.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GC_USER_CODE, user);
        bundle.putString(Constant.GC_PWD, ToolBox_Inf.md5(password).toUpperCase());
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putInt(Constant.GC_STATUS_JUMP, status_jump);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void validateLogin(String login, String password, String nfc_code) {

        if (nfc_code.trim().length() == 0) {

            if (login.trim().length() == 0) {
                mView.showAlertMsg(
                        context.getString(R.string.alert_title_login_fill_field),
                        context.getString(R.string.msg_login_required)
                );
                mView.fieldFocus(Act001_Main.ET_LOGIN);
                return;
            }
            //
            if (password.trim().length() == 0) {
                mView.showAlertMsg(
                        context.getString(R.string.alert_title_login_fill_field),
                        context.getString(R.string.msg_pwd_required)
                );
                mView.fieldFocus(Act001_Main.ET_PASSWORD);
                return;
            }
        }
        //Verifica se exite conexao antes de chamar WS
        if(ToolBox_Con.isOnline(context)){
            mView.showPD();
            //
            executeLoginProcess(login, password, nfc_code, 0);
        } else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }

    }

    @Override
    public void checkLogin() {
        String user_code = ToolBox_Con.getPreference_User_Code(context);
        long customer_code = ToolBox_Con.getPreference_Customer_Code(context);

        if (!user_code.equals("")) {
            if (customer_code != -1) {
                mView.call_Act003_Main(context);
            } else {
                mView.call_Act002_Main(context);
            }
        }
    }
}
