package com.namoadigital.prj001.ui.act074;

import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;

import java.util.ArrayList;

public interface Act074_Main_Contract {

    interface I_Presenter{

        void getTicketList(boolean isHistoricalShown, boolean statusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile, long ticketProductCode, long ticketSerialCode, boolean bStatusNotExecuted, boolean bStatusIgnored, boolean bStatusCanceled, boolean bStatusRejected, boolean bParterNoProfile);

        int checkTicketToSync();

        String getBtnSyncText(int qtyToSync);

        boolean hasTicketInUpdateRequired();

        void executeTicketSync();

        void onBackPressedClicked(String requestingAct);

        void checkTicketFlow(Act074_TicketVH item);
    }

    interface I_View {
        void loadTicketList(ArrayList<Act074_TicketVH> tickets);

        void showMsg(String alert_error_on_generate_list_ttl, String alert_error_on_generate_list_msg);
    }
}
