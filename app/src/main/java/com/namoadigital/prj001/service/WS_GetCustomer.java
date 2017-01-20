package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.TGC_Env;
import com.namoadigital.prj001.model.TGC_Rec;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_GetCustomer extends IntentService {

    private EV_UserDao ev_userDao;
    private EV_User_CustomerDao ev_user_customerDao;

    private StringBuilder sResult;


    public WS_GetCustomer() {
        super("WS_GetCustomer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String user = bundle.getString(Constant.GC_USER_CODE);
            String password = bundle.getString(Constant.GC_PWD);
            String nfc = bundle.getString(Constant.GC_NFC);
            int statusjump = bundle.getInt(Constant.GC_STATUS_JUMP);

            sResult = new StringBuilder();

            processWS_GC(user, password, nfc, statusjump);

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

            WBR_GetCustomer.completeWakefulIntent(intent);

        }

    }

    private void processWS_GC(String user, String password, String nfc, int statusjump) throws Exception {

        ev_userDao = new EV_UserDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);

        Gson gson = new Gson();

        TGC_Env env = new TGC_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        //
        env.setEmail_p(user);
        env.setPassword(ToolBox_Inf.md5(password).toUpperCase());
        env.setNfc_code(nfc);

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_GETCUSTOMERS,
                gson.toJson(env)
        );

        TGC_Rec rec = gson.fromJson(
                resultado,
                TGC_Rec.class
        );

        if (!ToolBox_Inf.processWSCheck(
                getApplicationContext(),
                rec.getVersion(),
                rec.getLogin(),
                "OK",
                (rec.getLink_url() != null) ? rec.getLink_url() : "",
                statusjump,
                1
        )) {
            return;
        }

        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);

        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing EV_User...", "", "0");

        File[] files_Users = ToolBox_Inf.getListOfFiles_v2("ev_user-");
        //Recebe dados do usuário para inserir nas preferencias
        EV_User userInfo = null;

        for (File _file : files_Users) {

            ArrayList<EV_User> users = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_User>>() {
                    }.getType()
            );
            userInfo = users.get(0);
            ev_userDao.addUpdate(users, false);
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing EV_User_Customer...", "", "0");

        File[] files_Customers = ToolBox_Inf.getListOfFiles_v2("ev_user_customer-");

        for (File _file : files_Customers) {

            ArrayList<EV_User_Customer> customers = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_User_Customer>>() {
                    }.getType()
            );
            ev_user_customerDao.addUpdate(customers, false);
        }
        //Seta preferencias do user
        ToolBox_Con.setPreference_User_Code(getApplicationContext(),String.valueOf(userInfo.getUser_code()));
        ToolBox_Con.setPreference_User_Code_Nick(getApplicationContext(),String.valueOf(userInfo.getUser_code()));
        ToolBox_Con.setPreference_User_Email(getApplicationContext(),userInfo.getEmail_p());
        ToolBox_Con.setPreference_User_Pwd(getApplicationContext(),ToolBox_Inf.md5(password).toUpperCase());
        ToolBox_Con.setPreference_User_NFC(getApplicationContext(),String.valueOf(nfc));

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Processing...", "", "0");

        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);

    }
}
