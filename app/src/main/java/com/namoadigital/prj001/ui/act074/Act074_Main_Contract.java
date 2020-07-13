package com.namoadigital.prj001.ui.act074;

import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;

import java.util.ArrayList;

public interface Act074_Main_Contract {


    interface I_Presenter{

        void getTicketList(boolean isHistoricalShown, boolean statusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile, long ticketProductCode, long ticketSerialCode, boolean bStatusNotExecuted, boolean bStatusIgnored, boolean bStatusCanceled, boolean bStatusRejected, boolean bParterNoProfile);

        int checkTicketToSync();

        String getBtnSyncText(int qtyToSync);

        boolean hasTicketInUpdateRequired();

        void executeTicketSync();

        void onBackPressedClicked(String requestingAct);

        void checkTicketFlow(Act069_TicketVH item);
    }

    interface I_View {
        void loadTicketList(ArrayList<Act069_TicketVH> tickets);
    }
}
