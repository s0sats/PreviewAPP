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
import com.namoadigital.prj001.model.Main_Header_Env;
import com.namoadigital.prj001.model.TK_Next_Ticket;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Brief;
import com.namoadigital.prj001.model.T_TK_Next_Ticket_WS_Response;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_002;
import com.namoadigital.prj001.sql.TK_Ticket_Brief_Sql_003;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Next_Ticket extends IntentService {
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_ticket_download";
    private Gson gson;
    private TK_TicketDao ticketDao;
    private TK_Ticket_BriefDao ticketBriefDao;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WS_TK_Next_Ticket() {
        super("WS_TK_Next_Ticket");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {

            gson = new GsonBuilder().serializeNulls().create();
            ticketDao = new TK_TicketDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            ticketBriefDao = new TK_Ticket_BriefDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            processNextTicket();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_TK_Ticket_Save.completeWakefulIntent(intent);
        }
    }

    private void processNextTicket() throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        deleteLocalNextTickets();
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        Main_Header_Env env = new Main_Header_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_NEXT_TICKET,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        T_TK_Next_Ticket_WS_Response rec = gson.fromJson(
                resultado,
                T_TK_Next_Ticket_WS_Response.class
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
        processTicketReturn(rec);

    }

    private void deleteLocalNextTickets() {
        ticketBriefDao.remove(new TK_Ticket_Brief_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
        ).toSqlQuery());
    }

    private void processTicketReturn(T_TK_Next_Ticket_WS_Response response) {
        //
        checkSyncRequired(response.getNext_tickets());
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux(), "", "0");
        //
    }

    private void checkSyncRequired(List<TK_Next_Ticket> next_tickets) {
        List<TK_Ticket_Brief> ticketBriefList = new ArrayList<>();
        if(next_tickets != null) {
            for (TK_Next_Ticket ticket : next_tickets) {

                TK_Ticket local_ticket = ticketDao.getByString(
                        new TK_Ticket_Sql_001(
                                ticket.getCustomerCode(),
                                ticket.getTicketPrefix(),
                                ticket.getTicketCode()
                        ).toSqlQuery()
                );
                //
                if (local_ticket != null) {
                    if (local_ticket.getScn() < ticket.getScn()) {
                        ticketDao.addUpdate(
                                new Sql_WS_TK_Ticket_Save_002(
                                        local_ticket.getCustomer_code(),
                                        local_ticket.getTicket_prefix(),
                                        local_ticket.getTicket_code(),
                                        local_ticket.getUpdate_required(),
                                        local_ticket.getUpdate_required_product(),
                                        1
                                ).toSqlQuery()
                        );
                    }
                }
                TK_Ticket_Brief tk_ticket_brief = getTicketBrief(ticket);
                ticketBriefList.add(tk_ticket_brief);

            }
        }
        if(!ticketBriefList.isEmpty()) {
            ticketBriefDao.addUpdate(ticketBriefList, false);
        }
    }

    private TK_Ticket_Brief getTicketBrief(TK_Next_Ticket ticket) {
        TK_Ticket_Brief tk_ticket_brief = new TK_Ticket_Brief();
        tk_ticket_brief.setCustomer_code(ticket.getCustomerCode());
        tk_ticket_brief.setTicket_prefix(ticket.getTicketPrefix());
        tk_ticket_brief.setTicket_code(ticket.getTicketCode());
        tk_ticket_brief.setTicket_id(ticket.getTicketId());
        tk_ticket_brief.setScn(ticket.getScn());
        tk_ticket_brief.setOpen_site_code(ticket.getOpenSiteCode());
        tk_ticket_brief.setOpen_site_desc(ticket.getOpenSiteDesc());
        tk_ticket_brief.setOpen_product_desc(ticket.getOpenProductDesc());
        tk_ticket_brief.setOpen_serial_id(ticket.getOpenSerialId());
        tk_ticket_brief.setCurrent_step_order(ticket.getCurrentStepOrder());
        tk_ticket_brief.setTicket_status(ticket.getTicketStatus());
        tk_ticket_brief.setOrigin_desc(ticket.getOriginDesc());
        tk_ticket_brief.setStep_desc(ticket.getStepDesc());
        tk_ticket_brief.setStep_order_seq(ticket.getStep_order_seq());
        tk_ticket_brief.setForecast_start(ticket.getForecastStart());
        tk_ticket_brief.setForecast_end(ticket.getForecastEnd());
        tk_ticket_brief.setStep_count(ticket.getStepCount());
        tk_ticket_brief.setFcm(0);
//        tk_ticket_brief.setLocal_ticket(has_local_ticket);
        tk_ticket_brief.setClient_code(ticket.getClient_code());
        tk_ticket_brief.setClient_name(ticket.getClient_name());
        tk_ticket_brief.setContract_code(ticket.getContract_code());
        tk_ticket_brief.setContract_desc(ticket.getContract_desc());
        return tk_ticket_brief;
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_processing_data");
        translist.add("generic_process_finalized_msg");
        translist.add("msg_error_on_insert_ticket");
        translist.add("msg_no_data_returned");
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
