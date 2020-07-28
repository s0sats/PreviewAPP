package com.namoadigital.prj001.ui.act070;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Super;

import java.util.ArrayList;

public interface Act070_Main_Contract {

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String ttl,String msg);

        void callAct069();

        void callAct071(Bundle bundle);

        void callAct035();

        void showAlert(String ttl, String msg);

        void showResult(ArrayList<HMAux> resultList, boolean ticketResult);

        void callRefreshUi();

        void updateSyncRequiredByFCM();

        void callAct017();
        //LUCHE - 22/07/2020 - REINICIANDO O TICKET
        void setStepperSource(ArrayList<BaseStep> baseSteps);

        void informAdapterInsertRange(int mainPosition, int rangeLength);

        void informAdapterRemoveRange(int mainPosition, int rangeLength);

        void callAct076();

        void callAct068();
    }

    interface I_Presenter{

        TK_Ticket getTicketObj(int mTkPrefix, int mTkCode);

        boolean validateBundleParams(int mTkPrefix, int mTkCode);

        String getFormattedCheckinInfo(String checkin_date, String checkin_user_name);

        String getFormattedDoneInfo(String close_date, String close_user_name);

        void onBackPressedClicked(String requestingAct);

        ArrayList<TK_Ticket_Ctrl_Super> generateCtrlActions(TK_Ticket mTicket, LinearLayout llActions, boolean filterOn);

        boolean getReadOnlyDefinition(TK_Ticket mTicket);

        boolean checkFilterDisable(ArrayList<TK_Ticket_Ctrl> ctrl);

        void executeCheckin(TK_Ticket tkTicket, boolean checkIn);

        void processCheckinReturn(int mPrefix, int mCode, String jsonRet);

        void processSaveReturn(int mPrefix, int mCode, String jsonRet);

        boolean setCheckInData(TK_Ticket tkTicket);

        boolean hideCancelCheckin(TK_Ticket mTicket);

        void prepareSyncProcess(TK_Ticket mTicket);

        boolean checkOnlySyncNeeds(TK_Ticket mTicket);

        public boolean checkSyncRequireNeedsChange(int ticket_prefix, int ticket_code);

        boolean isTicketInTokenFile(int ticket_prefix, int ticket_code);

        boolean isReadOnlyStatus(String ticketStatus);
        //LUCHE - 22/07/2020 - REINICIANDO O TICKET
        void getStepsList(TK_Ticket mTicket);

        void generateStepCtrlsContent(TK_Ticket mTicket, ArrayList<BaseStep> source , int mainPosition );

        void removeStepCtrlsContent(ArrayList<BaseStep> sources, int mainPosition);

        void updateStepOpenStates(ArrayList<BaseStep> sources, int mainPosition, boolean isShown);
    }
}
