package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.TGC_Env;
import com.namoadigital.prj001.model.TGC_Rec;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_Truncate;
import com.namoadigital.prj001.sql.EV_User_Sql_Truncate;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

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

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_customer), "", "0");

        Gson gson = new GsonBuilder().serializeNulls().create();

        TGC_Env env = new TGC_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        //
        env.setEmail_p(user);
        env.setPassword(password);
        env.setNfc_code(nfc);

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_GETCUSTOMERS,
                gson.toJson(env)
        );

        TGC_Rec rec = gson.fromJson(
                resultado,
                TGC_Rec.class
        );

        if (!ToolBox_Inf.processWSCheck_GC(
                getApplicationContext(),
                rec.getVersion(),
                rec.getLogin(),
                (rec.getLink_url() != null) ? rec.getLink_url() : "",
                statusjump,
                1
        )) {
            return;
        }

        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);

        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_ev_user), "", "0");

        //Apaga dados da tabela
        ev_userDao.remove(new EV_User_Sql_Truncate().toSqlQuery() );

        File[] files_Users = ToolBox_Inf.getListOfFiles_v2("ev_user-");

        EV_User userInfo = null;

        for (File _file : files_Users) {

            ArrayList<EV_User> users = gson.fromJson(
                    ToolBox.jsonFromOracle(
                        ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_User>>() {
                    }.getType()
            );
            userInfo = users.get(0);
            ev_userDao.addUpdate(users, true);
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_ev_user_customer), "", "0");

        //Apaga dados da tabela
        ev_user_customerDao.remove(new EV_User_Customer_Sql_Truncate().toSqlQuery());

        File[] files_Customers = ToolBox_Inf.getListOfFiles_v2("ev_user_customer-");

        for (File _file : files_Customers) {

            ArrayList<EV_User_Customer> customers = gson.fromJson(
                    ToolBox.jsonFromOracle(
                        ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_User_Customer>>() {
                    }.getType()
            );
            ev_user_customerDao.addUpdate(customers, true);
        }

        //Verifica se novo usr igual ao ultimo logado
        //Se for diferente apaga os bancos mult
        if(userInfo.getUser_code() != Long.parseLong(ToolBox_Con.getPreference_Last_User_Logged(getApplicationContext()))){
            boolean del;
            File[] files_db = getListDB("C_");

            for (File _file : files_db) {
               del = _file.delete();
            }
        }

        ToolBox_Con.setPreference_User_Code(getApplicationContext(), String.valueOf(userInfo.getUser_code()));
        ToolBox_Con.setPreference_User_Code_Nick(getApplicationContext(), String.valueOf(userInfo.getUser_nick()));
        ToolBox_Con.setPreference_User_Email(getApplicationContext(), userInfo.getEmail_p());
        ToolBox_Con.setPreference_User_Pwd(getApplicationContext(), password);
        ToolBox_Con.setPreference_User_NFC(getApplicationContext(), String.valueOf(nfc));
        ToolBox_Con.setPreference_Last_User_Logged(getApplicationContext(), String.valueOf(userInfo.getUser_code()));

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", getString(R.string.msg_finishing_processsing), "", "0");

        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);

    }

    public static File[] getListDB(final String prefix) {
        File fileList = new File(Constant.DB_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix)) {
                    return true;
                }
                return false;
            }
        });
        //
        if (files != null) {
            Arrays.sort(files);
        }
        //
        return files;
    }
}
