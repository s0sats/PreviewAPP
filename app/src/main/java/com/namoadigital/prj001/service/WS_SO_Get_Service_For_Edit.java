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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.TSO_Get_Service_Edit_Env;
import com.namoadigital.prj001.model.TSO_Get_Service_Edit_Rec;
import com.namoadigital.prj001.model.TSO_Service_Search_Env;
import com.namoadigital.prj001.receiver.WBR_SO_Get_Service_For_Edit;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Get_Service_For_Edit extends IntentService {
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Get_Service_For_Edit";
    private Gson gson;

    public WS_SO_Get_Service_For_Edit() {
        super("WS_SO_Get_Service_For_Edit");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            int site_code = bundle.getInt(SM_SODao.SITE_CODE,0);
            int product_code = bundle.getInt(SM_SODao.PRODUCT_CODE,0);
            int serial_code = bundle.getInt(SM_SODao.SERIAL_CODE,0);
            int service_code = bundle.getInt(SM_SO_ServiceDao.SERVICE_CODE,0);
            //
            processSOSearchServiceForEdit(
                    product_code,
                    serial_code,
                    service_code,
                    site_code
            );
            //
        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Get_Service_For_Edit.completeWakefulIntent(intent);
        }
    }

    private void processSOSearchServiceForEdit(int product_code, int serial_code, int service_code, int site_code) throws Exception {


        loadTranslation();

        gson = new GsonBuilder().serializeNulls().create();
        //
        TSO_Get_Service_Edit_Env env = new TSO_Get_Service_Edit_Env();
        //header
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //body
        env.setProduct_code(product_code);
        env.setSerial_code(serial_code);
        env.setSite_code(site_code);
        env.setService_code(service_code);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_searching_services"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SERVICE_EDIT_GET,
                gson.toJson(env)
        );

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_services"), "", "0");
        //
        TSO_Get_Service_Edit_Rec rec = gson.fromJson(
                resultado,
                TSO_Get_Service_Edit_Rec.class
        );

        if (
                !ToolBox_Inf.processWSCheckValidation(
                        getApplicationContext(),
                        rec.getValidation(),
                        rec.getError_msg(),
                        rec.getLink_url(),
                        1,
                        1)
                        ||
                        !ToolBox_Inf.processoOthersError(
                                getApplicationContext(),
                                getResources().getString(R.string.generic_error_lbl),
                                rec.getError_msg())
        ) {
            return;
        }
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_list"), "", "0");
        //
        processSOGetServiceEditReturn(rec);
        /*
        Tratamento de sucesso e erro
        if (true) {
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
        } else {
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_save_ok"), hmAux, "", "0");
        }
        */
    }

    private void processSOGetServiceEditReturn(TSO_Get_Service_Edit_Rec rec) {

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_sending_so_data");
        translist.add("msg_receiving_so_data");
        translist.add("msg_processing_from_to_data");
        translist.add("msg_re_processing_so_data");
        translist.add("msg_error_on_save_serial");
        translist.add("msg_save_ok");
        translist.add("msg_updating_serial");
        translist.add("error_from_to_processing");


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
