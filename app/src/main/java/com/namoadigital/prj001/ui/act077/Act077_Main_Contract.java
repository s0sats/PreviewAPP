package com.namoadigital.prj001.ui.act077;

import android.os.Bundle;

import com.namoadigital.prj001.model.TK_Ticket;

public interface Act077_Main_Contract {
    interface I_View {
        void loadTicketOrigin(TK_Ticket tkTicket);

        void showMsg(String ttl, String msg);

        void showPD(String ttl, String msg);

        void callAct070(Bundle bundle);
    }

    interface I_Presenter{

        void getStepOrigin(int ticketPrefix, int ticketCode);

    }
}
