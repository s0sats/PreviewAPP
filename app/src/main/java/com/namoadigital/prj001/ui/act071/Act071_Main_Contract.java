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

        void callAct069(boolean useRequestingBundle);

        void callAct070();

        void showResult(ArrayList<HMAux> resultList, boolean ticketResult);

        boolean hasUnsavedData();

        void restoreActionImage();

        void callAct017();

        boolean isScheduledTicket();

        int getmSchedulePrefix();

        int getmScheduleCode();

        int getmScheduleExec();

        void updateTicketPk(int mPrefix, int mCode);

        void checkPostTicketSaveFlow();

        String getRequestingAct();
    }

    interface I_Presenter{

        void onBackPressedClicked(String requestingAct);

        boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq, int mSchedulePrefix, int mScheduleCode, int mScheduleExec);

        TK_Ticket_Ctrl getTicketCtrlObj(int mActionPrefix, int mActionCode, int mActionSeq, int mStepCode);

        String getFormattedInfo(String ctrl_end_date, String ctrl_end_user_name);

        boolean getReadOnlyDefinition(TK_Ticket_Ctrl mTicketCtrl);

        String generateActionPhotoLocalPath(TK_Ticket_Action action);

        boolean newActionPhotoExists(TK_Ticket_Action action);

        boolean fileExists(String path);

        boolean updateTicketAction(TK_Ticket_Ctrl mTicketCtrl);

        void execTicketSave();

        void processSaveReturn(int ticket_prefix, int ticket_code, String mLink);

        String hasCheckinBlockBy(int ticket_prefix, int ticket_code);

        boolean hasCheckinAlertByStatus(String ticketStatus);
        //Metodo chamado quando o ticket ja existe
        void definePostTicketSaveFlow(int ticket_prefix, int ticket_code);
        //Metodo chamado quando o ticket foi criado via agendamento
        void definePostTicketSaveFlow(int mSchedulePrefix, int mScheduleCode, int mScheduleExec);

        String getFormattedSeqText(String seq);

        boolean isScheduleAbortProcess(int mSchedulePrefix, int mScheduleCode, int mScheduleExec);

        void showScheduleCancelMsg(int mSchedulePrefix, int mScheduleCode, int mScheduleExec);

        boolean isClosedStatus(String ctrl_status);

        /*boolean hasPartnerProfile(Integer partner_code);

        boolean hasActionExecProfile();

        boolean isReadOnlyStatus(String ticketStatus);*/
    }

}
