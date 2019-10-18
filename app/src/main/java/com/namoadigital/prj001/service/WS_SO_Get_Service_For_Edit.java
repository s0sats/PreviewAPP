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
            int category_price_code = bundle.getInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE, -1);
            int pack_code = bundle.getInt(SM_SO_ServiceDao.PACK_CODE, -1);
            int pack_seq = bundle.getInt(SM_SO_ServiceDao.PACK_SEQ, -1);
            int price_list_code = bundle.getInt(SM_SO_ServiceDao.PRICE_LIST_CODE, -1);
            int so_prefix = bundle.getInt(SM_SO_ServiceDao.SO_PREFIX, -1);
            int so_code = bundle.getInt(SM_SO_ServiceDao.SO_CODE, -1);
            int service_seq = bundle.getInt(SM_SO_ServiceDao.SERVICE_SEQ, -1);
            //
            processSOSearchServiceForEdit(
                    product_code,
                    serial_code,
                    service_code,
                    site_code,
                    category_price_code,
                    pack_code,
                    pack_seq,
                    price_list_code,
                    so_prefix,
                    so_code,
                    service_code,
                    service_seq
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

    private void processSOSearchServiceForEdit(int product_code, int serial_code, int service_code, int site_code, int category_price_code, int pack_code, int pack_seq, int price_list_code, int so_prefix, int so_code, int serviceCode, int service_seq) throws Exception {


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
        env.setCategory_price_code(category_price_code);
        env.setPack_code(pack_code);
        env.setPack_seq(pack_seq);
        env.setPrice_list_code(price_list_code);
        env.setSo_prefix(so_prefix);
        env.setSo_code(so_code);
        env.setService_code(service_code);
        env.setService_seq(service_seq);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_searching_service_info_list"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SERVICE_EDIT_GET,
                gson.toJson(env)
        );

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_service_info_list"), "", "0");
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

    }

    private void processSOGetServiceEditReturn(TSO_Get_Service_Edit_Rec rec) {
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_proccess"), new HMAux(), gson.toJson(rec) , "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_searching_service_info_list");
        translist.add("msg_receiving_service_info_list");
        translist.add("msg_re_processing_so_data");
        translist.add("msg_end_proccess");


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
