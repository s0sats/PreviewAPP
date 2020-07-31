package com.namoadigital.prj001.ui.act075;

import com.namoadigital.prj001.model.TK_Ticket;

public interface Act075_Main_Contract {

    interface I_View {
        void showMsg(String ttl, String msg);
        //
        void setWsProcess(String wsProcess);
        //
    }

    interface I_Presenter{
        TK_Ticket getTicket(long customer_code, int ticket_prefix, int ticket_code);
        //
        void saveproduct();

        boolean getWithdrawStatus(TK_Ticket ticket);

        boolean getAppliedStatus(TK_Ticket ticket);
        //
    }
}
