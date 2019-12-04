package com.namoadigital.prj001.ui.act070;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Super;

import java.util.ArrayList;

public interface Act070_Main_Contract {

    interface I_View{

        void callAct069();

        void callAct071(Bundle bundle);
    }

    interface I_Presenter{

        TK_Ticket getTicketObj(int mTkPrefix, int mTkCode);

        boolean validateBundleParams(int mTkPrefix, int mTkCode);

        String getFormattedCheckinInfo(String checkin_date, String checkin_user_name);

        String getFormattedDoneInfo(String close_date, String close_user_name);

        void onBackPressedClicked(String requestingAct);

        ArrayList<TK_Ticket_Ctrl_Super> generateCtrlActions(TK_Ticket mTicket, LinearLayout llActions);

        boolean getReadOnlyDefinition(TK_Ticket mTicket);
    }
}
