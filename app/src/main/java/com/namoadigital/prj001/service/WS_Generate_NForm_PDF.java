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
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.TNForm_PDF_Env;
import com.namoadigital.prj001.model.TNForm_PDF_Rec;
import com.namoadigital.prj001.receiver.WBR_Generate_NForm_PDF;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_Generate_NForm_PDF extends IntentService {
    public static final String NFORM_PK_KEY = "NFORM_PK_KEY";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_generate_nform_pdf";

    public WS_Generate_NForm_PDF() {
        super("WS_Generate_NForm_PDF");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            long customer_code = bundle.getLong(MD_Product_SerialDao.CUSTOMER_CODE);
            String sys_pk = bundle.getString(NFORM_PK_KEY);


            processGenerateNFormPDF(customer_code,sys_pk);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Generate_NForm_PDF.completeWakefulIntent(intent);
        }
    }

    private void processGenerateNFormPDF(long customer_code, String sys_pk) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        TNForm_PDF_Env env = new TNForm_PDF_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSys_pk(sys_pk);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_generating_pdf"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_GENERATE_FORM_PDF,
                gson.toJson(env)
        );
        //
        TNForm_PDF_Rec rec = gson.fromJson(
                resultado,
                TNForm_PDF_Rec.class
        );
        //
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
        processReturn(sys_pk,rec);

    }

    private void processReturn(String sys_pk, TNForm_PDF_Rec rec) {
        if(rec.getUrl() != null && !rec.getUrl().isEmpty()){
            HMAux aux  = new HMAux();
            aux.put(NFORM_PK_KEY,sys_pk);
            aux.put(GE_Custom_Form_BlobDao.BLOB_URL,rec.getUrl());
            //
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "CLOSE_ACT",
                    hmAux_Trans.get("msg_process_finalized"),
                    aux,
                    rec.getLink_url(),
                    "0"
            );
        }else{
            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    hmAux_Trans.get("msg_pdf_generation_error"),
                    "",
                    "0"
            );
        }
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_sending_data");
        translist.add("msg_generating_pdf");
        translist.add("msg_pdf_generation_error");
        translist.add("msg_process_finalized");
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
