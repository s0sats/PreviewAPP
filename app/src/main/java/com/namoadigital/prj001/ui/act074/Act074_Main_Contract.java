package com.namoadigital.prj001.ui.act074;

import android.os.Bundle;

import com.namoadigital.prj001.model.TK_Next_Ticket;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;

import java.util.ArrayList;
import java.util.List;

public interface Act074_Main_Contract {

    interface I_Presenter{

        void getTicketList();

        int checkTicketToSync();

        String getBtnSyncText(int qtyToSync);

        boolean hasTicketInUpdateRequired();

        void executeTicketSync(Act074_TicketVH item);

        void onBackPressedClicked(String requestingAct);

        void checkTicketFlow(Act074_TicketVH item);

        void setTicketVH(List<TK_Next_Ticket> tickets);
    }

    interface I_View {

        void setWsProcess(String wsProcess);

        void showPD(String ttl,String msg);

        void loadTicketList(ArrayList<Act074_TicketVH> tickets);

        void showMsg(String title, String msg);

        void showEmptyListMsg(String title, String msg);

        void callAct070(Bundle bundle);

        void callAct068();
    }
}
