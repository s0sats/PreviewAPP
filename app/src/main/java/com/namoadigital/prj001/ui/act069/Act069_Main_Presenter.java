package com.namoadigital.prj001.ui.act069;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.sql.Sql_Act069_001;
import com.namoadigital.prj001.sql.Sql_Act069_002;
import com.namoadigital.prj001.sql.Sql_Act069_003;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

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
    public void getTicketList(boolean isHistoricalShown, boolean bStatusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile, long ticketProductCode, long ticketSerialCode, boolean bStatusNotExecuted, boolean bStatusIgnored, boolean bStatusCanceled, boolean bStatusRejected, boolean bParterNoProfile) {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
            new Sql_Act069_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context),
                isHistoricalShown,
                bStatusPending,
                bStatusProcess,
                bStatusWaitingSync,
                bStatusDone,
                ticketProductCode,
                ticketSerialCode,
                bStatusNotExecuted,
                bStatusIgnored,
                bStatusCanceled,
                bStatusRejected,
                bParterEmpty,
                bParterProfile,
                bParterNoProfile
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
                    getCtrlsSerialsList(aux);
                    //
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

    private void getCtrlsSerialsList(HMAux auxTicket) {
        List<HMAux> serialList = ticketDao.query_HM(
            new Sql_Act069_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                auxTicket.get(TK_TicketDao.TICKET_PREFIX),
                auxTicket.get(TK_TicketDao.TICKET_CODE)
            ).toSqlQuery()
        );
        //
        String seriais = "";
        if(serialList != null && serialList.size() > 0){
            for (HMAux auxSerial : serialList) {
                seriais = seriais.isEmpty()? auxSerial.get(TK_Ticket_CtrlDao.SERIAL_ID) :  seriais +"|" + auxSerial.get(TK_Ticket_CtrlDao.SERIAL_ID);
            }
            //
        }
        //
        auxTicket.put(Act069_TicketVH.CTRLS_SERIAL_LIST,seriais);
    }

    /**
     * LUCHE - 18/03/2020
     * <p></p>
     * Metodo que define proxima ação do fluxo.
     * Se for uma execução de ticket agendado, navega para act071, se não para act070
     * @param item Ticket Clicado
     */
    @Override
    public void checkTicketFlow(Act069_TicketVH item) {
        if(isScheduledTicketExecution(item)){
            mView.callAct071(generateAct071Bundle(item));
        }else{
            mView.callAct070(generateAct070Bundle(item));
        }
    }

    /**
     * LUCHE - 18/03/2020
     * <P></P>
     * Metodo que verifica se o item seleciona é um agendamento em andamento
     * @param item Item clicado
     * @return - Verdadeiro se scheduelPK existir e ticket_prefix == 0  e ticket_code > 0
     */
    private boolean isScheduledTicketExecution(Act069_TicketVH item) {
        return item.getSchedulePk() != null && !item.getSchedulePk().isEmpty()
        && item.getTicket_prefix() == 0
        && item.getTicket_code() > 0;
    }

    /**
     * LUCHE - 18/03/2020
     * <P></P>
     * Criad bundle com dado do ticket para seusado na chamada da act070
     * @param item Ticket clicado
     * @return - Bundle com pk do ticket
     */
    private Bundle generateAct070Bundle(Act069_TicketVH item) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX,item.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE,item.getTicket_code());
        return bundle;
    }

    /**
     * LUCHE - 18/03/2020
     * <P></P>
     * Criad bundle com dado do ticket para ser usado na chamada da act071.
     * Adiciona pk o ticket e dados do agendamento.
     * @param item Ticket clicado
     * @return - Bundle com dados do ticket agendamento
     */
    private Bundle generateAct071Bundle(Act069_TicketVH item) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX,item.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE,item.getTicket_code());
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX, item.getSchedule_prefix() != null ? item.getSchedule_prefix() : -1);
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_CODE, item.getSchedule_code()!= null ? item.getSchedule_code() : -1);
        bundle.putInt(MD_Schedule_ExecDao.SCHEDULE_EXEC, item.getSchedule_exec()!= null ? item.getSchedule_exec() : -1);
        //EM 13/03/2020, a aexecução do ticket agendado sempre gerar um ticket finalizado, sendo assim, como essa será a unica ação,
        //é possivel chumbar o valor de ticket_seq como 1, pois sempre será a primeira e unica ação deste ticket.
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, 1);
        bundle.putString(TK_TicketDao.TICKET_ID, ToolBox_Inf.getFormattedTicketSeqExec(
            item.getSchedulePk(),
            String.valueOf(item.getTicket_prefix()),
            String.valueOf(item.getTicket_code())
            )
        );
        bundle.putString(TK_TicketDao.TYPE_DESC, item.getType_desc());
        bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN,false);
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, item.getSchedulePk());
        //
        return bundle;
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
        try{
            //Chama metodo do toolbox_inf que verificar pendencia de envio no banco e no arquivo token
            int sendPendecies = Integer.valueOf(
                ToolBox_Inf.handleTicketUpdateRequired(context, ToolBox_Con.getPreference_Customer_Code(context))
            );
            //Se maior que 0, tem ticket pendente de envio
            return sendPendecies > 0;
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            //Retorno é True, pois o true impede de proseguir
            return true;
        }

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
            case ConstantBaseApp.ACT068:
            case ConstantBaseApp.ACT073:
                mView.callAct068();
                break;
            case ConstantBaseApp.ACT005:
            default:
                mView.callAct005();
        }
    }
}
