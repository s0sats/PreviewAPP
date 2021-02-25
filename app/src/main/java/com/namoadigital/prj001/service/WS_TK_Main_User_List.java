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
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.T_TK_Main_User_Rec;
import com.namoadigital.prj001.model.T_TK_Ticket_Main_User_List_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Main_User_List_Rec;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WS_TK_Main_User_List extends IntentService {
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_main_user_list";
    public static final String EDIT_HEADER = "EDIT_HEADER";
    private Gson gson;
    private TK_TicketDao ticketDao;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WS_TK_Main_User_List() {
        super("WS_TK_Main_User_List");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        gson = new GsonBuilder().serializeNulls().create();
        ticketDao = new TK_TicketDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
        try {
            int ticketPrefix = bundle.getInt(TK_TicketDao.TICKET_PREFIX);
            int ticketCode = bundle.getInt(TK_TicketDao.TICKET_CODE);
            int scn = bundle.getInt(TK_TicketDao.SCN);
            int edit_header = bundle.getInt(WS_TK_Main_User_List.EDIT_HEADER);
            //
            processMainUserList(ticketPrefix, ticketCode, scn, edit_header);
            //
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_TK_Ticket_Save.completeWakefulIntent(intent);
        }
    }

    private void processMainUserList(int ticketPrefix, int ticketCode, int scn, int edit_header) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();

        T_TK_Ticket_Main_User_List_Env env =  new T_TK_Ticket_Main_User_List_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setTicket_prefix(ticketPrefix);
        env.setTicket_code(ticketCode);
        env.setScn(scn);
        env.setEdit_header(edit_header);

        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_TICKET_SERVER_MAIN_USER,
                gson.toJson(env)
        );

        T_TK_Ticket_Main_User_List_Rec rec = gson.fromJson(
                resultado,
                T_TK_Ticket_Main_User_List_Rec.class
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
        checkMainUserListReturn(rec.getScn_valid(), rec.getData());
        //
    }

    private void checkMainUserListReturn(int scn_valid, List<T_TK_Main_User_Rec> data) throws IOException {
        if(scn_valid == 0){
            ToolBox.sendBCStatus(getApplicationContext(), "CUSTOM_ERROR",  hmAux_Trans.get("alert_invalid_scn_msg"), "INVALID_SCN", "0");
        }else{
            createMainUserListJsonFile(ConstantBaseApp.TICKET_MAIN_USER_LIST_JSON_FILE, gson.toJson(data));
            //
            ToolBox.sendBCStatus(getApplicationContext(),"CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), gson.toJson(data),"0");
        }
    }

    private File createMainUserListJsonFile(String fileName, String workGroupList) throws IOException {
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
