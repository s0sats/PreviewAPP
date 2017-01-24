package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.receiver.WBR_Session;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_001;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

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
    public void executeSessionProcess(String email, String password, String nfc, HMAux customer,int forced_login, int jump_validation, int jump_od) {
        Intent mIntent = new Intent(context, WBR_Session.class);
        Bundle bundle = new Bundle();

        bundle.putString(Constant.GC_USER_CODE, email);
        bundle.putString(Constant.GC_PWD, password);
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putString(Constant.USER_CUSTOMER_CODE, customer.get(EV_User_CustomerDao.CUSTOMER_CODE));
        bundle.putString(Constant.USER_CUSTOMER_TRANSLATE_CODE, customer.get(EV_User_CustomerDao.TRANSLATE_CODE));
        bundle.putInt(Constant.FORCED_LOGIN, forced_login);
        bundle.putInt(Constant.GC_STATUS_JUMP, jump_validation);
        bundle.putInt(Constant.GC_STATUS, jump_od);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSyncProcess() {
        EV_User_Customer userCustomer;

        userCustomer =  ev_user_customerDao.getByString(
                            new EV_User_Customer_Sql_003(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                ToolBox_Con.getPreference_User_Code(context)
                            ).toSqlQuery()
                        );

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add("MAIN");
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP,userCustomer.getSession_app());
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE,data_package);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);


        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", "Starting to sync ...", "", "0");

    }

}
