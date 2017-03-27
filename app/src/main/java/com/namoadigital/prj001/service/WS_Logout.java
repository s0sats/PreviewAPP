package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.TLogout_Env;
import com.namoadigital.prj001.model.TLogout_Rec;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 */

public class WS_Logout extends IntentService {

    private StringBuilder sResult;
    private EV_User_CustomerDao customerDao;

    public WS_Logout() {
        super("WS_Logout");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        sResult = new StringBuilder();

        try {

            String customer_list = bundle.getString(Constant.WS_LOGOUT_CUSTOMER_LIST);
            processWS_Logout(customer_list);

        }catch (Exception e) {

            String results = "ERROR: ";

            if (e.toString().contains("JsonSyntaxException")) {
                results += "JsonParse - " + sResult.toString();
                sb.append(results);

            } else if(e.toString().contains("ORA-")) {
                results += "Oracle - " + sResult.toString();
                sb.append(results);

            }else{
                sb.append(results)
                        .append(e.toString());
            }

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            WBR_Logout.completeWakefulIntent(intent);
        }
    }

    private void processWS_Logout(String customer_list) {

        Gson gson = new GsonBuilder().serializeNulls().create();

        TLogout_Env env =  new TLogout_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setUser_code(ToolBox_Con.getPreference_User_Code(getApplicationContext()));
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        env.setCustomer_code(customer_list);

       // ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Solicitando logout", "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_LOGOUT,
                gson.toJson(env)
        );

        TLogout_Rec rec = gson.fromJson(
                resultado,
                TLogout_Rec.class
        );

        checkLogoutReturn(rec.getLogout(),customer_list);

    }

    private void checkLogoutReturn(String logout_return, String customer_list) {
        customerDao = new EV_User_CustomerDao(
                getApplicationContext(),
                Constant.DB_FULL_BASE,
                Constant.DB_VERSION_BASE
        );
        switch (logout_return.toUpperCase()){
            case "OK":
            case "ERROR":
            default:
                customer_list = customer_list.replace("|","','");

                customerDao.addUpdate(
                        new EV_User_Customer_Sql_005(
                            ToolBox_Con.getPreference_User_Code(getApplicationContext()),
                            customer_list
                        ).toSqlQuery()
                );

                ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "", "", "0");
                break;
        }
    }
}
