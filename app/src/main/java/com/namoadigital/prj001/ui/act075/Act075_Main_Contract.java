package com.namoadigital.prj001.ui.act075;

import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;

import java.util.ArrayList;

public interface Act075_Main_Contract {

    interface I_View {
        void showMsg(String ttl, String msg);
        //
        void setWsProcess(String wsProcess);

        void resetHasUpdate();

        void showPD(String ttl, String msg);
        //
    }

    interface I_Presenter{
        TK_Ticket getTicket(long customer_code, int ticket_prefix, int ticket_code);
        //
        TK_Ticket_Approval getTicketApproval(long customer_code, int ticket_prefix, int ticket_code, int ticket_seq, int step_code);
        //
        TK_Ticket_Product getTicketProduct(long customer_code, int ticket_prefix, int ticket_code, int product_code);
        //
        void saveproduct(int scn, ArrayList<TK_Ticket_Product> tk_ticket_products);
        //
        boolean getWithdrawStatus(TK_Ticket ticket);
        //
        boolean getAppliedStatus(TK_Ticket ticket);
        //
        TK_Ticket_Step getSelectedStep(int mTkPrefix, int mTkCode, int mStepCode);
        //
        int getStepColor(TK_Ticket_Step ticketStep, boolean IsCurrentStep);
        //
        String getStepNumFormatted(TK_Ticket_Step ticketStep);
        //
        String getStepDesc(TK_Ticket_Step ticketStep);

        void saveApproval(TK_Ticket_Approval ticketApproval);
        //
    }
}
