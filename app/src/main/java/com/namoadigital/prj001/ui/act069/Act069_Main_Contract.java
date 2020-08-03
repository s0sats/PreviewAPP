package com.namoadigital.prj001.ui.act069;

import android.os.Bundle;

import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;

import java.util.ArrayList;

public interface Act069_Main_Contract {

    interface I_View{

        void loadTicketList(ArrayList<Act074_TicketVH> tickets);

        void showMsg(String ttl, String msg);

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void callAct005();

        void callAct012();

        void callAct014();

        void callAct068();

        void callAct070(Bundle bundle);

        void callAct071(Bundle bundle);
    }

    interface I_Presenter{

        void getTicketList(boolean isHistoricalShown, boolean statusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile, long ticketProductCode, long ticketSerialCode, boolean bStatusNotExecuted, boolean bStatusIgnored, boolean bStatusCanceled, boolean bStatusRejected, boolean bParterNoProfile);

        int checkTicketToSync();

        String getBtnSyncText(int qtyToSync);

        boolean hasTicketInUpdateRequired();

        void executeTicketSync();

        void onBackPressedClicked(String requestingAct);

        void checkTicketFlow(Act074_TicketVH item);
    }
}
