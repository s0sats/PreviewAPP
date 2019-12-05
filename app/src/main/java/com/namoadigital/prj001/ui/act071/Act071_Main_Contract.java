package com.namoadigital.prj001.ui.act071;

import com.namoadigital.prj001.model.TK_Ticket_Ctrl;

public interface Act071_Main_Contract {

    interface I_View{

        void callAct070();
    }

    interface I_Presenter{

        void onBackPressedClicked(String requestingAct);

        boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq);

        TK_Ticket_Ctrl getTicketCtrlObj(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq);

        String getFormattedDoneInfo(String ctrl_end_date, String ctrl_end_user_name);

        boolean getReadOnlyDefinition(TK_Ticket_Ctrl mTicketCtrl);

        boolean hasPartnerProfile(Integer partner_code);

        boolean hasActionExecProfile();

        boolean isReadOnlyStatus(String ticketStatus);
    }

}
