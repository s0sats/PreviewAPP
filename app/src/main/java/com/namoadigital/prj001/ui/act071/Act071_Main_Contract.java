package com.namoadigital.prj001.ui.act071;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;

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

        void showResult(boolean ticketResult);

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

        boolean isCreationCtrl();

        void addResultList(ArrayList<HMAux> auxResults);

        boolean has_tk_ticket_is_form_off_hand();

        void callAct081();
    }

    interface I_Presenter{

        void onBackPressedClicked(String requestingAct);

        //boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq, int mSchedulePrefix, int mScheduleCode, int mScheduleExec, boolean isCreationCtrl);

        boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeqTmp, int mSchedulePrefix, int mScheduleCode, int mScheduleExec, boolean isCreationCtrl);

        TK_Ticket_Ctrl getTicketCtrlObj(int mActionPrefix, int mActionCode, int mActionSeqTmp, int mStepCode);

        String getFormattedInfo(String ctrl_end_date, String ctrl_end_user_name);

        boolean getReadOnlyDefinition(TK_Ticket_Ctrl mTicketCtrl);

        String generateActionPhotoLocalPath(TK_Ticket_Action action);

        boolean newActionPhotoExists(TK_Ticket_Action action);

        boolean fileExists(String path);

        boolean updateTicketAction(TK_Ticket_Ctrl mTicketCtrl);

        void execTicketSave(boolean forceOfflineProcess);

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

        TK_Ticket_Step getStepInfo(int mTicketPrefix, int mTicketCode, int mStepCode);

        int getStepColor(TK_Ticket_Step ticketStep, boolean mPipelineHeaderIsCurrentStepOrder);

        String getStepNumFormatted(TK_Ticket_Step ticketStep);

        String getStepDesc(TK_Ticket_Step ticketStep);

        void createActionIfNeed(TK_Ticket_Ctrl mTicketCtrl, boolean isCreationAction);

        void setStartInfoIfNeed(TK_Ticket_Ctrl mTicketCtrl);

        TK_Ticket_Ctrl createTicketCtrlObj(int mActionPrefix, int mActionCode, int mStepCode, Bundle act081Bundle);

        MD_Schedule_Exec getScheduleExec(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec);

        boolean verifyProductForForm();

        void callWsSave();

        void processWS_SaveReturn(String mLink);

        void defineFormWaitingSyncFlow(int mActionPrefix, int mActionCode);

        /*boolean hasPartnerProfile(Integer partner_code);

        boolean hasActionExecProfile();

        boolean isReadOnlyStatus(String ticketStatus);*/

        TK_Ticket getTicketbyPk(int ticket_prefix, int ticket_code);

        void defineProductSerialViews(int mActionPrefix, int mActionCode, TK_Ticket_Ctrl mTicketCtrl, TextView tvProduct, TextView tvSerial);

        void defineNextSaveFlow(int mActionPrefix, int mActionCode);

        void processWsSerialSavelReturn(HMAux hmAux);

        void executeSerialSave();
    }

}
