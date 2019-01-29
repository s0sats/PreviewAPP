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
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.SO_Favorite_Request;
import com.namoadigital.prj001.model.SO_Favorite_Response;
import com.namoadigital.prj001.receiver.WBR_SO_Favorite_List;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Favorite_List extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Favorite_List";
    private Gson gson;

    public WS_SO_Favorite_List() {
        super("WS_SO_Favorite_List");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            gson = new GsonBuilder().serializeNulls().create();
            String siteCode = bundle.getString(MD_SiteDao.SITE_CODE);
            long operationCode = bundle.getLong(Constant.LOGIN_OPERATION_CODE);
            long productCode = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE);
            long serialCode = bundle.getLong(MD_Product_SerialDao.SERIAL_CODE);
            int categoryPriceCode = bundle.getInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE);
            int segmentCode = bundle.getInt(MD_SegmentDao.SEGMENT_CODE);

            //
            processSO_Favorite_List(siteCode, operationCode, productCode, serialCode, categoryPriceCode, segmentCode);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Favorite_List.completeWakefulIntent(intent);
        }
    }

    private void processSO_Favorite_List(String siteCode, long operationCode, long productCode, long serialCode, int categoryPriceCode, int segmentCode) throws Exception {
        loadTranslation();
        //
        SO_Favorite_Request env = new SO_Favorite_Request(siteCode, operationCode, productCode, serialCode, categoryPriceCode, segmentCode);
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setProduct_code(productCode);
        env.setSerial_code(serialCode);
        env.setCategory_price_code(categoryPriceCode);
        env.setOperation_code(operationCode);
        env.setSegment_code(segmentCode);
        env.setSite_code(siteCode);

        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_searching_sos"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_FAVORITE_LIST,
                gson.toJson(env)
        );

        SO_Favorite_Response response = gson.fromJson(
                resultado,
                SO_Favorite_Response.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                response.getValidation(),
                response.getError_msg(),
                response.getLink_url(),
                1,
                1
        )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        response.getError_msg())
        ) {
            return;
        }
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_searching_sos"), resultado, "0");

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_processing_list");
        translist.add("msg_error_on_save_serial");
        translist.add("msg_searching_sos");
        translist.add("msg_send_serial_data");
        translist.add("msg_end_serial_save");
        translist.add("msg_end_so_download");


        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);
    }
}
