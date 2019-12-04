package com.namoadigital.prj001.ui.act069;

import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;

import java.util.ArrayList;

public interface Act069_Main_Contract {

    interface I_View{

        void loadTicketList(ArrayList<Act069_TicketVH> tickets);

        void showMsg(String ttl, String msg);

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);
    }

    interface I_Presenter{

        void getTicketList(boolean statusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile);

        int checkTicketToSync();

        String getBtnSyncText(int qtyToSync);

        boolean hasTicketInUpdateRequired();

        void executeTicketSync();
    }
}
