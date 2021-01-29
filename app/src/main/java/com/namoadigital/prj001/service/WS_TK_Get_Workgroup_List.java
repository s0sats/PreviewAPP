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
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_BriefDao;
import com.namoadigital.prj001.model.T_TK_Get_Workgroup_List_Env;
import com.namoadigital.prj001.model.T_TK_Get_Workgroup_List_Rec;
import com.namoadigital.prj001.receiver.WBR_TK_Get_Workgroup_List;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WS_TK_Get_Workgroup_List extends IntentService {
    public static final String TICKET_WORKGROUP_LIST_FILE =  "TICKET_WORKGROUP_LIST_FILE";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_get_workgroup_list";
    private Gson gson;
    private TK_TicketDao ticketDao;
    private TK_Ticket_BriefDao ticketBriefDao;

    public WS_TK_Get_Workgroup_List() {
        super("WS_TK_Get_Workgroup_List");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            gson = new GsonBuilder().serializeNulls().create();
            int ticket_prefix = bundle.getInt(TK_TicketDao.TICKET_PREFIX,-1);
            int ticket_code = bundle.getInt(TK_TicketDao.TICKET_CODE,-1);
            int ticket_scn = bundle.getInt(TK_TicketDao.SCN,-1);
            //
            processGetWorkgrouList(ticket_prefix,ticket_code,ticket_scn);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            WBR_TK_Get_Workgroup_List.completeWakefulIntent(intent);
        }
    }

    private void processGetWorkgrouList(int ticket_prefix, int ticket_code, int ticket_scn) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        T_TK_Get_Workgroup_List_Env env =
            new T_TK_Get_Workgroup_List_Env(
                Constant.PRJ001_CODE,
                Constant.PRJ001_VERSION,
                Constant.PKG_APP_TYPE_DEFAULT,
                ToolBox_Con.getPreference_Session_App(getApplicationContext()),
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                ticket_prefix,
                ticket_code,
                ticket_scn
            );
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_TICKET_GET_WORKGROUP_LIST,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        T_TK_Get_Workgroup_List_Rec rec = gson.fromJson(
                resultado,
            T_TK_Get_Workgroup_List_Rec.class
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
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_processing_data"), "", "0");
        //
        processWorkgroupListReturn(rec);
    }

    private void processWorkgroupListReturn(T_TK_Get_Workgroup_List_Rec response) throws IOException {
        if(response.getScn_valid() == 1) {
            createWorkgroupListJsonFile(ConstantBaseApp.TICKET_WORKGROUP_LIST_JSON_FILE, gson.toJson(response.getData()));
            //
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), "", "0");
        } else{
            ToolBox.sendBCStatus(getApplicationContext(), Constant.PD_TYPE_ERROR_1, hmAux_Trans.get("scn_out_of_date_msg"), "", "0");
        }
    }

    private File createWorkgroupListJsonFile(String fileName, String workGroupList) throws IOException {
        File json_file = new File(ConstantBaseApp.TICKET_JSON_PATH, fileName);
        ToolBox_Inf.writeIn(workGroupList, json_file);
        return json_file;
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_processing_data");
        translist.add("generic_process_finalized_msg");
        translist.add("scn_out_of_date_msg");
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
