package com.namoadigital.prj001.ui.act078;

import com.namoadigital.prj001.model.TK_Ticket;

public interface Act078_Main_Contract {
    interface I_View {

        void loadTicketOrigin(TK_Ticket ticket);
    }

    interface I_Presenter{

        void getStepOrigin(int mTkPrefix, int mTkCode);
    }
}
