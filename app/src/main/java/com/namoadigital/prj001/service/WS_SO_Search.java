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
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.TSO_Search_Env;
import com.namoadigital.prj001.model.TSO_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/06/2017.
 */

public class WS_SO_Search extends IntentService {

    public static final String SO_PREFIX_CODE = "so_prefix_code";
    public static final String SO_LIST_QTY = "so_list_qty";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Search";
    private Gson gson;

    public WS_SO_Search() {
        super("WS_SO_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            gson = new GsonBuilder().serializeNulls().create();
            Long product_code = bundle.getLong(Constant.WS_SO_SEARCH_PRODUCT_CODE, -1L);
            String serial_id = bundle.getString(Constant.WS_SO_SEARCH_SERIAL_ID, "");
            String so_mult = bundle.getString(Constant.WS_SO_SEARCH_SO_MULT, "");//Lista de SO a Serem Baixados. Act024
            int profile_check = bundle.getInt(Constant.WS_SO_SEARCH_PROFILE_CHECK, 1);//Lista de SO a Serem Baixados. Act024
            //
            processSO_Search(product_code, serial_id, so_mult, profile_check);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Search.completeWakefulIntent(intent);
        }

    }

    private void processSO_Search(Long product_code, String serial_id, String so_mult, int profile_check) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        TSO_Search_Env env = new TSO_Search_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setProduct_code(product_code);
        env.setSerial_id(serial_id);
        env.setSo_mult(so_mult);
        env.setProfile_check(profile_check);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);

        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_searching_sos"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SEARCH,
                gson.toJson(env)
        );
        //
        TSO_Search_Rec rec = gson.fromJson(
                resultado,
                TSO_Search_Rec.class
        );
        //
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
        processSOSearchReturn(rec);
    }

    private void processSOSearchReturn(TSO_Search_Rec rec) {
        String so_prefix_code = "";
        //
        if(rec.getSo() != null && rec.getSo().size() > 0){
            so_prefix_code = rec.getSo().get(0).getSo_prefix()
                             + Constant.MAIN_CONCAT_STRING
                             + rec.getSo().get(0).getSo_code();
        }
        //
        HMAux hmAux = new HMAux();
        hmAux.put(SO_PREFIX_CODE, so_prefix_code);
        hmAux.put(SO_LIST_QTY, String.valueOf(rec.getSo().size()));
        //
        SM_SODao soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        //
        for (SM_SO sm_so : rec.getSo()) {
            //Apaga SO completa
            soDao.removeFull(sm_so);
            //
            sm_so.setPK();
            //
        }
        //
        soDao.addUpdate(rec.getSo(), false);
        //
        callFinishProcessing(hmAux);
    }

    private void callFinishProcessing(HMAux hmAux) {
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_end_so_download"), hmAux, "", "0");
        //
        startDownloadWorkers();
    }

    /**
     * LUCHE - 30/06/2020
     * Alterado metodo que chamada serviços de download de img e pdf, para chamar os respectivos
     * Workers
     */
    private void startDownloadWorkers() {
        ToolBox_Inf.scheduleDownloadPdfWork(getApplicationContext());
        ToolBox_Inf.scheduleDownloadPictureWork(getApplicationContext());
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
