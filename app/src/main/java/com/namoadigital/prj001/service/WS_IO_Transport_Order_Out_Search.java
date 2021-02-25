package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.T_IO_Transport_Order_Out_Search_Env;
import com.namoadigital.prj001.model.T_IO_Transport_Order_Out_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Transport_Order_Out_Search extends IntentService {

    public static final String KEY_CODE_ID = "KEY_CODE_ID";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_transport_order_out_search";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_IO_Transport_Order_Out_Search() {
        super("WS_IO_Transport_Order_Out_Search");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            String transport_order = bundle.getString(IO_InboundDao.TRANSPORT_ORDER,"");
            //
            processWsOutTransportOrderSearch(transport_order);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Outbound_Search.completeWakefulIntent(intent);
        }
    }

    private void processWsOutTransportOrderSearch(String transport_order) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        T_IO_Transport_Order_Out_Search_Env env = new T_IO_Transport_Order_Out_Search_Env();
        //
        env.setApp_code(ConstantBaseApp.PRJ001_CODE);
        env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
        env.setTransport_order(transport_order);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_TRANSPORT_ORDER_OUT_SEARCH,
                gson.toJson(env)
        );
        //
        T_IO_Transport_Order_Out_Search_Rec rec = gson.fromJson(
                resultado,
                T_IO_Transport_Order_Out_Search_Rec.class
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
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_processing_list"), gson.toJson(rec.getOutbound()), "0");
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_processing_list");

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
