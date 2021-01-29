package com.namoadigital.prj001.ui.act079;

import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Form;

public interface Act079_Main_Contract {
    interface I_View {

        void loadTicketOrigin(TK_Ticket ticket);

        void showAlert(String ttl, String msg);
    }

    interface I_Presenter{

        void getStepOrigin(int mTkPrefix, int mTkCode);
        void tryOpenFormPDF(TK_Ticket_Form form);
    }
}
