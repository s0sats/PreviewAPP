package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.SiteLicense;
import com.namoadigital.prj001.model.T_EV_Get_Customer_Site_License_Env;
import com.namoadigital.prj001.model.T_EV_Get_Customer_Site_License_Rec;
import com.namoadigital.prj001.receiver.WBR_Get_Customer_Site_License;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WS_Get_Customer_Site_License  extends IntentService {
    public static final String KEY_CODE_ID = "KEY_CODE_ID";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_get_customer_site_license";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_Get_Customer_Site_License() {
        super("WS_Get_Customer_Site_License");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String customer_code = bundle.getString(EV_User_CustomerDao.CUSTOMER_CODE,"-1");
            //
            processGetCustomerSiteLicense(customer_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Get_Customer_Site_License.completeWakefulIntent(intent);
        }
    }

    private void processGetCustomerSiteLicense(String customer_code) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();

        T_EV_Get_Customer_Site_License_Env env =  new T_EV_Get_Customer_Site_License_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        env.setCustomer_code(customer_code);
        env.setEmail_p(ToolBox_Con.getPreference_User_Code_Nick(getApplicationContext()));
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        env.setPassword(ToolBox_Con.getPreference_User_Pwd(getApplicationContext()));
        env.setNfc_code(ToolBox_Con.getPreference_User_NFC(getApplicationContext()));
        //Default pois foi usado uma copia do servico Get_Customer
        env.setStatus_jump("1");
        env.setCurrent_time(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_GET_CUSTOMERS_SITE_LICENSE,
                gson.toJson(env)
        );

        T_EV_Get_Customer_Site_License_Rec rec = gson.fromJson(
                resultado,
                T_EV_Get_Customer_Site_License_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                1,
                1
        )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
        ) {
            return;
        }
        //
        checkSiteLicenseListReturn(rec.getSite_license());
        //
    }

    private void checkSiteLicenseListReturn(ArrayList<SiteLicense> data) throws IOException {
        createLicenseSiteListJsonFile(ConstantBaseApp.ENV_SITE_LICENSE_JSON_FILE, gson.toJson(data));
        //
        ToolBox.sendBCStatus(getApplicationContext(),"CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), gson.toJson(data),"0");
    }

    private File createLicenseSiteListJsonFile(String fileName, String workGroupList) throws IOException {
        File json_file = new File(ConstantBaseApp.CUSTOMER_SITE_LICENSE_JSON_PATH, fileName);
        ToolBox_Inf.writeIn(workGroupList, json_file);
        return json_file;
    }


    //
    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_processing_data");
        translist.add("generic_process_finalized_msg");
        translist.add("alert_invalid_scn_msg");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);
    }
}
