package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.TSession_Env;
import com.namoadigital.prj001.model.TSession_Rec;
import com.namoadigital.prj001.receiver.WBR_Session;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_Session extends IntentService {

    private EV_UserDao userDao;
    private EV_User_CustomerDao ev_user_customerDao;

    private StringBuilder sResult;


    public WS_Session() {
        super("WS_Session");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String user = bundle.getString(Constant.GC_USER_CODE);
            String password = bundle.getString(Constant.GC_PWD);
            String nfc = bundle.getString(Constant.GC_NFC);
            String customer_code = bundle.getString(Constant.USER_CUSTOMER_CODE);
            String translate_code = bundle.getString(Constant.USER_CUSTOMER_TRANSLATE_CODE);
            int forced_login = bundle.getInt(Constant.FORCED_LOGIN);
            int jump_validation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jump_od = bundle.getInt(Constant.GC_STATUS);

           sResult = new StringBuilder();

           processWS_Session(user, password, nfc, customer_code, translate_code,forced_login,jump_validation,jump_od);

        } catch (Exception e) {

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
            WBR_Session.completeWakefulIntent(intent);
        }

    }

    private void processWS_Session(String user, String password, String nfc, String customer_code, String translate_code, int forced_login, int jump_validation, int jump_od) {
        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        //
        Gson gson = new Gson();
        //
        TSession_Env env =  new TSession_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        //
        env.setEmail_p(user);
        env.setPassword(password);
        env.setNfc_code(nfc);
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        env.setManufacturer(Build.MANUFACTURER);
        env.setModel(Build.MODEL);
        env.setOs("ANDROID");
        env.setVersion_os(String.valueOf(Build.VERSION.SDK_INT));
        env.setForce_login(String.valueOf(forced_login));
        env.setCustomer_code(customer_code);
        env.setTranslate_code(Integer.parseInt(translate_code));

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SESSION,
                gson.toJson(env)
        );

        TSession_Rec rec = gson.fromJson(
                resultado,
                TSession_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                jump_validation,
                jump_od
                )
            ) {
            return;
        }
        EV_User_Customer userCustomer = ev_user_customerDao.getByString(
                    new EV_User_Customer_Sql_002(
                            ToolBox_Con.getPreference_User_Code(getApplicationContext()),
                            customer_code
                            ).toSqlQuery()
                    );

        //Seta propriedade do customer que serão atualizadas
        userCustomer.setBlocked(0);
        userCustomer.setSession_app(rec.getSession_app());

        //Chama metodo para atualizar dados
        ev_user_customerDao.addUpdate(userCustomer);

        //Seta preferecia de customer
        ToolBox_Con.setPreference_Customer_Code(getApplicationContext(), userCustomer.getCustomer_code());
        ToolBox_Con.setPreference_Customer_Code_Name(getApplicationContext(), userCustomer.getCustomer_name());
        ToolBox_Con.setPreference_Customer_nls_date_format (getApplicationContext(), userCustomer.getNls_date_format());

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Processing...", "", "0");

    }
}
