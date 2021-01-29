package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.SiteLicense;
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
            //LUCHE - 06/01/2021 - Somente quando licença for por site
            //LUCHE - 12/01/2021 - Como tudo sempre muda, esse obj serializado perdeu seu sentido,
            //mas foi mantido pois o tempo de refatoração não valeria a pena
            @Nullable
            SiteLicense siteLicense = (SiteLicense) bundle.getSerializable(SiteLicense.class.getName());
            //
            processWS_Session(user, password, nfc, customer_code, translate_code,forced_login,jump_validation,jump_od,siteLicense);

        } catch (Exception e) {
            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            WBR_Session.completeWakefulIntent(intent);
        }

    }

    private void processWS_Session(String user, String password, String nfc, String customer_code, String translate_code, int forced_login, int jump_validation, int jump_od,@Nullable SiteLicense siteLicense) throws Exception {
        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
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
        env.setVersion_os(ToolBox_Inf.sVersionDesc(Build.VERSION.RELEASE, String.valueOf(Build.VERSION.SDK_INT)));
        env.setForce_login(String.valueOf(forced_login));
        env.setCustomer_code(customer_code);
        env.setTranslate_code(Integer.parseInt(translate_code));
        env.setGcm_id(ToolBox_Con.getPreference_Google_ID(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSite_code(siteLicense != null ? siteLicense.getSite_code() : null);
        env.setUser_level_code(siteLicense != null ? siteLicense.getUser_level_code() : null);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_sending_data_msg), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SESSION,
                gson.toJson(env)
        );
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_receiving_data_msg), "", "0");

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
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
            ) {
            return;
        }
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_saving_data_msg), "", "0");

        EV_User_Customer userCustomer = ev_user_customerDao.getByString(
                    new EV_User_Customer_Sql_002(
                            ToolBox_Con.getPreference_User_Code(getApplicationContext()),
                            customer_code
                            ).toSqlQuery()
                    );

        //Seta propriedade do customer que serão atualizadas
        userCustomer.setBlocked(0);
        userCustomer.setSession_app(rec.getSession_app());
        //LUCHE - 07/01/2020 - Seta preferencias da licença escolhida
        //if(siteLicense != null) {
        //LUCHE
        if(rec.getSession_options() != null) {
            TSession_Rec.Session_Options session_options = rec.getSession_options();
            userCustomer.setLicense_site_code(session_options.getSite_code());
            userCustomer.setLicense_site_desc(session_options.getSite_desc());
            userCustomer.setLicense_user_level_code(session_options.getUser_level_code());
            userCustomer.setLicense_user_level_id(session_options.getUser_level_id());
            userCustomer.setLicense_user_level_value(session_options.getUser_level_value());
            userCustomer.setLicense_user_level_changed(session_options.getUser_level_changed());
        }
        //Chama metodo para atualizar dados
        ev_user_customerDao.addUpdate(userCustomer);
        //Seta preferecia de customer
        ToolBox_Con.setPreference_Customer_Code(getApplicationContext(), userCustomer.getCustomer_code());
        ToolBox_Con.setPreference_Customer_Code_Name(getApplicationContext(), userCustomer.getCustomer_name());
        ToolBox_Con.setPreference_Customer_nls_date_format (getApplicationContext(), userCustomer.getNls_date_format());
        ToolBox_Con.setPreference_Translate_Code(getApplicationContext(), String.valueOf(userCustomer.getTranslate_code()));
        ToolBox_Con.setPreference_Session_App(getApplicationContext(),rec.getSession_app());
        ToolBox_Con.setPreference_Status_Login(getApplicationContext(),Constant.LOGIN_STATUS_OK);
        ToolBox_Con.setPreference_Customer_Uses_Tracking(getApplicationContext(), userCustomer.getTracking());
        ToolBox_Con.setPreference_Customer_TMZ(getApplicationContext(), userCustomer.getTimezone());
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS_GO", getString(R.string.msg_getting_master_data), "", "0");
    }
}
