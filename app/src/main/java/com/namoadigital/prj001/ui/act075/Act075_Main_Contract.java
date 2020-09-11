package com.namoadigital.prj001.ui.act075;

import android.content.DialogInterface;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;

import java.util.ArrayList;
import java.util.List;

public interface Act075_Main_Contract {

    interface I_View {
        void showMsg(String ttl, String msg);
        //
        void showAlert(String ttl, String msg,  DialogInterface.OnClickListener listenerOk, boolean showNegative);
        //
        void setWsProcess(String wsProcess);
        //
        void resetHasUpdate();
        //
        void showPD(String ttl, String msg);
        //
        void showResult(ArrayList<HMAux> resultList, boolean ticketResult);
        //
        void callMoveOn();
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
        TK_Ticket_Ctrl getSelectedCtrl(int mTkPrefix, int mTkCode, int mTkSeq, int mStepCode);

        String getSelectedCtrlStatus(int mTkPrefix, int mTkCode, int mTkSeq, int mStepCode);

        //
        int getStepColor(TK_Ticket_Step ticketStep, boolean IsCurrentStep);
        //
        String getStepNumFormatted(TK_Ticket_Step ticketStep);
        //
        String getStepDesc(TK_Ticket_Step ticketStep);
        //
        void saveApproval(TK_Ticket_Approval ticketApproval, boolean isApproved, String approveComments);
        //
        void processSaveReturn(int ticket_prefix, int ticket_code, String mLink);
        //
        boolean hasApproveProfile(int mTkPrefix, int mTkCode, int mTkSeq, int stepCode);

        List<TK_Ticket_Product> getTicketProductListForApproval(int mTkPrefix, int mTkCode);

        void saveAppliedProduct(TK_Ticket ticket, ArrayList<TK_Ticket_Product> getmValues);

        List<TK_Ticket_Product> getTicketProductList(TK_Ticket tkTicket);
        //
        void setStartCtrl();

        String defineActTitle(int act_profile, int mTkPrefix, int mTkCode, int mTkSeq, int mStepCode);
    }
}
