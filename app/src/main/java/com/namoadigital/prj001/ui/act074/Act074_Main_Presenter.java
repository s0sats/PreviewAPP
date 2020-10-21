package com.namoadigital.prj001.ui.act074;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_BriefDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Next_Ticket;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Next_Ticket;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.sql.Sql_Act076_002;
import com.namoadigital.prj001.sql.TK_Ticket_Brief_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Brief_Sql_003;
import com.namoadigital.prj001.sql.TK_Ticket_Brief_Sql_004;
import com.namoadigital.prj001.sql.TK_Ticket_Brief_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Act074_Main_Presenter implements Act074_Main_Contract.I_Presenter {
    private Context context;
    private Act074_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    private final TK_Ticket_BriefDao ticketBriefdao;
    private List<Act074_TicketVH> focusList = new ArrayList<>();
    private List<Act074_TicketVH> unfocusList = new ArrayList<>();
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
        this.ticketBriefdao = new TK_Ticket_BriefDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void getMyTicketsList() {
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

    private void generateTicketVhList(List<HMAux> auxTickets) {

        if (auxTickets != null && auxTickets.size() > 0) {
            try {
                for (HMAux aux : auxTickets) {
                    //
                    setTicketLists(aux);
                }
                sortTicketsList();
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
                mView.showMsg(
                        hmAux_Trans.get("alert_error_on_generate_list_ttl"),
                        hmAux_Trans.get("alert_error_on_generate_list_msg")
                );
            }
        }
    }

    private void sortTicketsList() {

        Collections.sort(focusList, new Comparator<Act074_TicketVH>() {
            @Override
            public int compare(Act074_TicketVH o1, Act074_TicketVH o2) {
                long mDate = ToolBox_Inf.dateToMilliseconds(o1.getStep_forecast_start_date(), "");
                long cDate = ToolBox_Inf.dateToMilliseconds(o2.getStep_forecast_start_date(), "");
                int dateCriteria = Long.compare(mDate, cDate);
                if (dateCriteria == 0) {
                    int prefixCriteria = Integer.compare(o1.getTicket_prefix(), o2.getTicket_prefix());
                    if (prefixCriteria == 0) {
                        return Integer.compare(o1.getTicket_code(), o2.getTicket_code());
                    }
                    return prefixCriteria;
                }
                return dateCriteria;
            }
        });

        Collections.sort(unfocusList, new Comparator<Act074_TicketVH>() {
            @Override
            public int compare(Act074_TicketVH o1, Act074_TicketVH o2) {
                long mDate = ToolBox_Inf.dateToMilliseconds(o1.getTicket_forecast_date(), "");
                long cDate = ToolBox_Inf.dateToMilliseconds(o2.getTicket_forecast_date(), "");
                int dateCriteria = Long.compare(mDate, cDate);
                if (dateCriteria == 0) {
                    int prefixCriteria = Integer.compare(o1.getTicket_prefix(), o2.getTicket_prefix());
                    if (prefixCriteria == 0) {
                        return Integer.compare(o1.getTicket_code(), o2.getTicket_code());
                    }
                    return prefixCriteria;
                }

                return dateCriteria;
            }
        });
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
        bundle.putString(TK_TicketDao.TICKET_PREFIX, item.getTicket_pk());
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct) {
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
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, item.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, item.getTicket_code());
        return bundle;
    }

    @Override
    public void setTicketVH() {
        //Handler utilizado para soltar o prgress quando há um processamento de ticket grande.
        Thread mThread = new Thread( new Runnable() {
            @Override
            public void run() {
                List<HMAux> tickets = getTk_next_tickets();
                if (tickets != null && tickets.size() > 0) {
                    try {
                        for (HMAux aux : tickets) {
                            //
                            setTicketLists(aux);
                        }
                        sortTicketsList();
                        ((Act074_Main) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.loadTicketList(focusList, true);
                            }
                        });
                    } catch (Exception e) {
                        ToolBox_Inf.registerException(getClass().getName(), e);
                        mView.showEmptyListMsg(
                                hmAux_Trans.get("alert_error_on_generate_list_ttl"),
                                hmAux_Trans.get("alert_error_on_generate_list_msg")
                        );
                    }
                } else {
                    mView.showEmptyListMsg(hmAux_Trans.get("alert_no_tickets_ttl"), hmAux_Trans.get("alert_no_tickets_msg"));
                }
            }
        });
        mThread.start();
    }

    private void setTicketLists(HMAux aux) {
        boolean hasFocus = false;
        if (aux.hasConsistentValue(TK_TicketDao.USER_FOCUS)) {
            hasFocus = aux.get(TK_TicketDao.USER_FOCUS).equals("1");
        }
        if (hasFocus) {
            focusList.add(
                    Act074_TicketVH.getTicketVHObj(aux)
            );
        } else {
            unfocusList.add(
                    Act074_TicketVH.getTicketVHObj(aux)
            );
        }
    }

    private List<HMAux> getTk_next_tickets() {
        List<HMAux> tickets = new ArrayList<>();

        List<HMAux> nextTickets = ticketBriefdao.query_HM(new TK_Ticket_Brief_Sql_004(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context)
        ).toSqlQuery());
        tickets.addAll(nextTickets);

        List<HMAux> mergeTickets = ticketBriefdao.query_HM(new TK_Ticket_Brief_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context)
        ).toSqlQuery());
        tickets.addAll(mergeTickets);

        List<HMAux> localTickets = ticketBriefdao.query_HM(new TK_Ticket_Brief_Sql_005(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context)
        ).toSqlQuery());
        tickets.addAll(localTickets);

        return tickets;
    }

    @Override
    public boolean verifyProductForForm(HMAux ticketPrefixCode) {
        int ticketPrefix = ticketPrefixCode.hasConsistentValue(TK_TicketDao.TICKET_PREFIX) ? Integer.valueOf(ticketPrefixCode.get(TK_TicketDao.TICKET_PREFIX)) : -1;
        int ticketCode = ticketPrefixCode.hasConsistentValue(TK_TicketDao.TICKET_CODE) ? Integer.valueOf(ticketPrefixCode.get(TK_TicketDao.TICKET_CODE)) : -1;
        if (ToolBox_Inf.hasFormProductOutdate(context, ticketPrefix, ticketCode)) {
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
        } else {
            return false;
        }
    }

    //
    @Override
    public void getOfflineTicketsList(final boolean hasUserFocus) {
        Thread mThread = new Thread( new Runnable() {
            @Override
            public void run() {
                List<HMAux> auxTickets = getOffline_tickets();
                if (auxTickets != null && auxTickets.size() > 0) {


                    generateTicketVhList(auxTickets);

                    ((Act074_Main) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (hasUserFocus) {
                                mView.loadTicketList(focusList, hasUserFocus);
                            } else {
                                mView.loadTicketList(unfocusList, hasUserFocus);
                            }
                        }
                    });
                }else{
                    mView.showEmptyListMsg(hmAux_Trans.get("alert_no_tickets_ttl"), hmAux_Trans.get("alert_no_tickets_msg"));
                }
            }
        });
        mThread.start();
    }

    private List<HMAux> getOffline_tickets() {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
                new Sql_Act076_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        return auxTickets;
    }

    //
    @Override
    public List<Act074_TicketVH> getFocusList() {
        return focusList;
    }

    //
    @Override
    public List<Act074_TicketVH> getUnfocusList() {
        return unfocusList;
    }

    @Override
    public void deleteNextTickets() {
        ticketBriefdao.remove(new TK_Ticket_Brief_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context)
        ).toSqlQuery());
    }
}
