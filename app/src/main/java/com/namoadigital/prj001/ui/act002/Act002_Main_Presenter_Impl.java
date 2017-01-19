package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.receiver.WBR_Session;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main_Presenter_Impl implements Act002_Main_Presenter {

    private Context context;
    private Act002_Main_View mView;
    private EV_User_CustomerDao ev_user_customerDao;
    private HMAux HMCustomer;

    public Act002_Main_Presenter_Impl(Context context, Act002_Main_View mView) {
        this.context = context;
        this.mView = mView;
        this.ev_user_customerDao = new EV_User_CustomerDao(context,Constant.DB_FULL_BASE,Constant.DB_VERSION_BASE);
    }

    @Override
    public void getAllCustomers() {
        mView.loadCustomers(
                ev_user_customerDao.query_HM(
                            new EV_User_Customer_Sql_001(
                                    ToolBox_Con.getPreference_User_Code(context)
                            ).toSqlQuery()
                )
        );
    }

    @Override
    public void getToken(HMAux item) {
        /*this.HMCustomer = item;
        Intent intent =  new Intent(context, WBR_Login.class);
        Bundle bundle =  new Bundle();
        bundle.putString();*/


    }

    @Override
    public void executeSessionProcess(String user, String password, String nfc, long customer_code, int status) {
        Intent mIntent = new Intent(context, WBR_Session.class);
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
