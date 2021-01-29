package com.namoadigital.prj001.ui.act074;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;

import java.util.List;

public interface Act074_Main_Contract {

    interface I_Presenter{

        void getMyTicketsList();

        int checkTicketToSync();

        String getBtnSyncText(int qtyToSync);

        boolean hasTicketInUpdateRequired();

        void executeTicketSync(Act074_TicketVH item);

        void onBackPressedClicked(String requestingAct);

        void checkTicketFlow(Act074_TicketVH item);

        void setTicketVH();

        boolean verifyProductForForm(HMAux ticketPrefixCode);

        void getOfflineTicketsList(boolean hasUserFocus);

        List<Act074_TicketVH> getFocusList();

        List<Act074_TicketVH> getUnfocusList();

        void deleteNextTickets();
    }

    interface I_View {

        void setWsProcess(String wsProcess);

        void showPD(String ttl,String msg);

        void loadTicketList(List<Act074_TicketVH> tickets, boolean userFocusOnly);

        void showMsg(String title, String msg);

        void showEmptyListMsg(String title, String msg);

        void callAct070(Bundle bundle);

        void callAct068();
    }
}
