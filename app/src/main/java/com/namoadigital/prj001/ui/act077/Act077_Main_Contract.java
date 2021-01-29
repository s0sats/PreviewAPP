package com.namoadigital.prj001.ui.act077;

import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Form;

public interface Act077_Main_Contract {
    interface I_View {
        void loadTicketOrigin(TK_Ticket tkTicket);

        void showMsg(String ttl, String msg);
    }

    interface I_Presenter{

        void getStepOrigin(int ticketPrefix, int ticketCode);

        void tryOpenFormPDF(TK_Ticket_Form form);
    }
}
