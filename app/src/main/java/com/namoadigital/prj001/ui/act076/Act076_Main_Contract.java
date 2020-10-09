package com.namoadigital.prj001.ui.act076;

import android.os.Bundle;

import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;

import java.util.ArrayList;

public interface Act076_Main_Contract {
    interface I_View {
        void loadTicketList(ArrayList<Act074_TicketVH> tickets);

        void showMsg(String ttl, String msg);

        void showPD(String ttl, String msg);

        void callAct005();

        void callAct068();

        void callAct070(Bundle bundle);

        void callAct012();
    }

    interface I_Presenter{

        void onBackPressedClicked(String requestingAct);

        void checkTicketFlow(Act074_TicketVH item);

        void getTicketListBySerial(long ticketProductCode, long ticketSerialCode);
    }
}
