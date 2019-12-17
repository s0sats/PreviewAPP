package com.namoadigital.prj001.ui.act069;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.sql.Sql_Act069_001;
import com.namoadigital.prj001.sql.Sql_Act069_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_To_Send;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act069_Main_Presenter implements Act069_Main_Contract.I_Presenter {

    private Context context;
    private Act069_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;

    public Act069_Main_Presenter(Context context, Act069_Main_Contract.I_View mView, HMAux hmAux_Trans) {
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
    public void getTicketList(boolean statusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile) {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
            new Sql_Act069_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context),
                statusPending,
                bStatusProcess,
                bStatusWaitingSync,
                bStatusDone,
                bParterEmpty,
                bParterProfile
            ).toSqlQuery()
        );
        //
        mView.loadTicketList(generateTicketVhList(auxTickets));
    }

    private ArrayList<Act069_TicketVH> generateTicketVhList(ArrayList<HMAux> auxTickets) {
        ArrayList<Act069_TicketVH> tickets = new ArrayList<>();
        if (auxTickets != null && auxTickets.size() > 0) {
            try {
                for (HMAux aux : auxTickets) {
                    tickets.add(
                        Act069_TicketVH.getTicketVHObj(aux)
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
        //
        return tickets;
    }

    @Override
    public int checkTicketToSync() {
        ArrayList<HMAux> auxTickets = getTicketToSync();
        //
        return auxTickets != null ?  auxTickets.size(): 0;
    }

    private ArrayList<HMAux> getTicketToSync() {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
            new Sql_Act069_002(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        );
        //
        return auxTickets;
    }

    private String getTicketConcatList() {
        ArrayList<HMAux> auxTickets = getTicketToSync();
        String ticketPKList = "";
        for (HMAux aux : auxTickets) {
            if(aux.hasConsistentValue(Sql_Act069_002.TICKET_PK)){
                ticketPKList += ConstantBaseApp.MAIN_CONCAT_STRING + aux.get(Sql_Act069_002.TICKET_PK);
            }
        }
        //
        return ticketPKList.contains(ConstantBaseApp.MAIN_CONCAT_STRING) ? ticketPKList.substring(ConstantBaseApp.MAIN_CONCAT_STRING.length()) : "";
    }

    @Override
    public String getBtnSyncText(int qtyToSync) {
        return hmAux_Trans.get("btn_sync_tickets") +" ("+qtyToSync+")";
    }

    @Override
    public boolean hasTicketInUpdateRequired() {
        HMAux auxTickets = ticketDao.getByStringHM(
            new TK_Ticket_Sql_To_Send(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        );
        //
        if( auxTickets != null
            && auxTickets.hasConsistentValue(TK_Ticket_Sql_To_Send.BADGE_TO_SEND_QTY)
        ){
            try{
                if(Integer.valueOf(auxTickets.get(TK_Ticket_Sql_To_Send.BADGE_TO_SEND_QTY)) == 0){
                    return false;
                }
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
                //Retorno é True, pois o true impede de proseguir
                return true;
            }
        }
        //
        return true;
    }

    @Override
    public void executeTicketSync() {
        if(ToolBox_Con.isOnline(context)){
            mView.setWsProcess(WS_TK_Ticket_Download.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_download_ticket_ttl"),
                hmAux_Trans.get("dialog_download_ticket_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(TK_TicketDao.TICKET_PREFIX, getTicketConcatList());
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);

        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct){
            case ConstantBaseApp.ACT012:
                mView.callAct012();
                break;
            case ConstantBaseApp.ACT014:
                mView.callAct014();
                break;
            case ConstantBaseApp.ACT005:
            default:
                mView.callAct005();
        }
    }
}
