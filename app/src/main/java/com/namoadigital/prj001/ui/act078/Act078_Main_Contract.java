package com.namoadigital.prj001.ui.act078;

import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Form;

public interface Act078_Main_Contract {
    interface I_View {

        void ticketParameterError(String ttl,String msg);

        void loadTicketOrigin(TK_Ticket ticket);

        void showMsg(String ttl, String msg);

        void setMeasureLayout(TK_Ticket tkTicket, boolean isTicketOrigin);

        void setScheduleLayout(TK_Ticket ticket, boolean isTicketOrigin, boolean isScheduleAction);
    }

    interface I_Presenter{

        void getStepOrigin(int mTkPrefix, int mTkCode);

        String getNavegationIntentData(TK_Ticket ticket);

        String getOriginTypeLbl(TK_Ticket tkTicket);

        boolean isScheduleAction(TK_Ticket tkTicket);

        void defineOriginLayoutConfig(TK_Ticket ticket);

        void tryOpenFormPDF(TK_Ticket_Form form);

        String getFormatedScorePerc(TK_Ticket_Form form);
    }
}
