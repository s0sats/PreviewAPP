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
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Download extends IntentService {

    public static final String TICKET_PREFIX = "TICKET_PREFIX";
    public static final String TICKET_CODE = "TICKET_CODE";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_SO_Search";
    private Gson gson;

    public WS_TK_Ticket_Download() { super("WS_TK_Ticket_Download");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            gson = new GsonBuilder().serializeNulls().create();
            String ticket_pks = bundle.getString(TICKET_PREFIX, "");
            //
            processTicketDownload(ticket_pks);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_TK_Ticket_Download.completeWakefulIntent(intent);
        }

    }

    private void processTicketDownload(String ticketPkList) throws Exception {
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //Seleciona traduções
        loadTranslation();
        //
        T_TK_Ticket_Download_Env env = new T_TK_Ticket_Download_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.getTicket().addAll(
            getTicketPkList(ticketPkList)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_searching_sos"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_DOWNLOAD,
            gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        T_TK_Ticket_Download_Rec rec = gson.fromJson(
            resultado,
            T_TK_Ticket_Download_Rec.class
        );
        //
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
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_list"), "", "0");
        //
        processTicketReturn(rec.getTicket());

    }

    private void processTicketReturn(ArrayList<TK_Ticket> ticketList) {
        if(ticketList != null && ticketList.size() > 0){
            for (TK_Ticket tkTicket : ticketList) {
                tkTicket.setPK();
            }
            //
            TK_TicketDao ticketDao = new TK_TicketDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
            );
            //
            ticketDao.addUpdate(ticketList,false);
            //
            startDownloadServices();
            //
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), "", "0");
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), "", "0");
        }
    }

    private void startDownloadServices() {
        //Como será possivel baixar ticket do customer logado, pode ser chamada a rotina de download.
        //Esse as definição mudar, rever, pois seria necessario chamar essa serviço para cada customer code diferente.
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        mIntentPIC.putExtras(bundle);
        getApplicationContext().sendBroadcast(mIntentPIC);
    }

    private ArrayList<T_TK_Ticket_Download_PK_Env> getTicketPkList(String ticketPkList) throws Exception {
        ArrayList<T_TK_Ticket_Download_PK_Env> objPkList = new ArrayList<>();
        String[] sPkList = ticketPkList.split(ConstantBaseApp.MAIN_CONCAT_STRING);
        //
        for (String sPk : sPkList) {
            String[] pk = sPk.replace("|","#").split("#");
            T_TK_Ticket_Download_PK_Env pkAux = new T_TK_Ticket_Download_PK_Env();
            //
            pkAux.setCustomer_code(pk[0]);
            pkAux.setTicket_prefix(pk[1]);
            pkAux.setTicket_code(pk[2]);
            //
            objPkList.add(pkAux);
        }
        //
        return objPkList;
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_process_finalized_msg");
        //
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
