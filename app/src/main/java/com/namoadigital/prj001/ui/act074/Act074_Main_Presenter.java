package com.namoadigital.prj001.ui.act074;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.TK_Next_Ticket;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Next_Ticket;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Next_Ticket;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act074_Main_Presenter implements Act074_Main_Contract.I_Presenter {
    private Context context;
    private Act074_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;

    public Act074_Main_Presenter(Context context, Act074_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void getTicketList() {
        mView.setWsProcess(WS_TK_Next_Ticket.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("progress_next_tickets_ttl"),
                hmAux_Trans.get("progress_next_tickets_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_TK_Next_Ticket.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private ArrayList<Act074_TicketVH> generateTicketVhList(ArrayList<HMAux> auxTickets) {
        ArrayList<Act074_TicketVH> tickets = new ArrayList<>();
        if (auxTickets != null && auxTickets.size() > 0) {
            try {
                for (HMAux aux : auxTickets) {
                    //
                    tickets.add(
                            Act074_TicketVH.getTicketVHObj(aux)
                    );
                }
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(),e);
                tickets = new ArrayList<>();
                mView.showMsg(
                        hmAux_Trans.get("alert_error_on_generate_list_ttl"),
                        hmAux_Trans.get("alert_error_on_generate_list_msg")
                );
            }
        }
        return tickets;
    }

    @Override
    public int checkTicketToSync() {
        return 0;
    }

    @Override
    public String getBtnSyncText(int qtyToSync) {
        return null;
    }

    @Override
    public boolean hasTicketInUpdateRequired() {
        return false;
    }

    @Override
    public void executeTicketSync(Act074_TicketVH item) {
        mView.setWsProcess(WS_TK_Ticket_Download.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_download_ticket_ttl"),
                hmAux_Trans.get("dialog_download_ticket_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_TK_Ticket_Download.class);
        Bundle bundle = new Bundle();
        bundle.putString(TK_TicketDao.TICKET_PREFIX,item.getTicket_pk());
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct){
            default:
                mView.callAct068();
                break;
        }
    }

    @Override
    public void checkTicketFlow(Act074_TicketVH item) {
        mView.callAct070(generateAct070Bundle(item));
    }

    private Bundle generateAct070Bundle(Act074_TicketVH item) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX,item.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE,item.getTicket_code());
        return bundle;
    }

    @Override
    public void setTicketVH(List<TK_Next_Ticket> tickets) {
        ArrayList<Act074_TicketVH> ticketsVH = new ArrayList<>();
        if (tickets != null && tickets.size() > 0) {
            try {
                for (TK_Next_Ticket aux : tickets) {
                    //
                    if(ToolBox_Con.getPreference_Site_Code(context).equals(String.valueOf(aux.getOpenSiteCode()))){
                        aux.setOpenSiteDesc("");
                    }
                    ticketsVH.add(
                            Act074_TicketVH.getTicketVHObj(aux)
                    );
                }
                mView.loadTicketList(ticketsVH);
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(),e);

                mView.showEmptyListMsg(
                        hmAux_Trans.get("alert_error_on_generate_list_ttl"),
                        hmAux_Trans.get("alert_error_on_generate_list_msg")
                );
            }
        }else{
            mView.showEmptyListMsg(hmAux_Trans.get("alert_no_next_tickets_ttl"), hmAux_Trans.get("alert_no_next_tickets_msg"));
        }
    }

    @Override
    public boolean verifyProductForForm() {
        if(ToolBox_Inf.hasFormProductOutdate(context)){
            if (ToolBox_Con.isOnline(context)) {
                mView.setWsProcess(WS_Sync.class.getName());
                //
                mView.showPD(
                        hmAux_Trans.get("progress_sync_ttl"),
                        hmAux_Trans.get("progress_sync_msg")
                );
                //
                ArrayList<String> data_package = new ArrayList<>();
                data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
                //
                Intent mIntent = new Intent(context, WBR_Sync.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
                bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
                bundle.putLong(Constant.GS_PRODUCT_CODE, 0);
                bundle.putInt(Constant.GC_STATUS_JUMP, 1);
                bundle.putInt(Constant.GC_STATUS, 1);
                //
                mIntent.putExtras(bundle);
                //
                context.sendBroadcast(mIntent);
                return true;
            }
            return false;
        }else{
            return false;
        }
    }


}
