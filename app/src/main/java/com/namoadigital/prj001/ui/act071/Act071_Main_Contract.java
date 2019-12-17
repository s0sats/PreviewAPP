package com.namoadigital.prj001.ui.act071;

import android.content.DialogInterface;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;

import java.util.ArrayList;

public interface Act071_Main_Contract {

    interface I_View{

        TK_Ticket_Action getAction();

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg, DialogInterface.OnClickListener onClickListener);

        void postTicketSave();

        void callAct069();

        void callAct070();

        void showResult(ArrayList<HMAux> resultList, boolean ticketResult);

        boolean hasUnsavedData();

        void restoreActionImage();
    }

    interface I_Presenter{

        void onBackPressedClicked(String requestingAct);

        boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq);

        TK_Ticket_Ctrl getTicketCtrlObj(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq);

        String getFormattedDoneInfo(String ctrl_end_date, String ctrl_end_user_name);

        boolean getReadOnlyDefinition(TK_Ticket_Ctrl mTicketCtrl);

        String generateActionPhotoLocalPath(TK_Ticket_Action action);

        boolean newActionPhotoExists(TK_Ticket_Action action);

        boolean fileExists(String path);

        boolean updateTicketAction(TK_Ticket_Ctrl mTicketCtrl);

        void execTicketSave();

        void definePostTicketSaveFlow(int ticket_prefix, int ticket_code);

        void processSaveReturn(int ticket_prefix, int ticket_code, String mLink);

        /*boolean hasPartnerProfile(Integer partner_code);

        boolean hasActionExecProfile();

        boolean isReadOnlyStatus(String ticketStatus);*/
    }

}
