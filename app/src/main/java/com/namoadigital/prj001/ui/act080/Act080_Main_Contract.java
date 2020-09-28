package com.namoadigital.prj001.ui.act080;

import com.namoadigital.prj001.model.TK_Ticket;

public interface Act080_Main_Contract {
    interface I_View {

        void loadTicketOrigin(TK_Ticket ticket);

        void showAlert(String ttl, String msg);
    }

    interface I_Presenter{

        void getStepOrigin(int mTkPrefix, int mTkCode);

    }
}
