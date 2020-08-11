package com.namoadigital.prj001.ui.act070;

import android.content.DialogInterface;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;

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

        void showAlert(String ttl, String msg, DialogInterface.OnClickListener listenerOk, boolean showNegative);

        public String getRequestingAct();

        void setCurrentStepFirstPosition(int currentStepFirstPosition);

        int getCurrentStepFirstPosition();

        void callAct076();

        void callAct068();

        void callact075ForMaterialApproval(int step_code, int ticket_seq, boolean currentStep);
    }

    interface I_Presenter{

        TK_Ticket getTicketObj(int mTkPrefix, int mTkCode);

        boolean validateBundleParams(int mTkPrefix, int mTkCode);

        void onBackPressedClicked(String requestingAct);

        boolean getReadOnlyDefinition(TK_Ticket mTicket);

        void processCheckinReturn(int mPrefix, int mCode, String jsonRet);

        void processSaveReturn(int mPrefix, int mCode, String jsonRet);

        void prepareSyncProcess(TK_Ticket mTicket);

        boolean checkOnlySyncNeeds(TK_Ticket mTicket);

        boolean checkSyncRequireNeedsChange(int ticket_prefix, int ticket_code);

        boolean isTicketInTokenFile(int ticket_prefix, int ticket_code);

        boolean isReadOnlyStatus(String ticketStatus);
        //LUCHE - 22/07/2020 - REINICIANDO O TICKET
        void getStepsList(TK_Ticket mTicket);

        void generateStepCtrlsContent(TK_Ticket mTicket, ArrayList<BaseStep> source , int mainPosition );

        void removeStepCtrlsContent(ArrayList<BaseStep> sources, int mainPosition);

        void updateStepOpenStates(ArrayList<BaseStep> sources, int mainPosition, boolean isShown);

        Bundle getAct071Bundle(TK_Ticket mTicket, int stepCode, int processTkSeq, int processTkSeqTmp, boolean currentStep, boolean ctrlCreation, boolean actionCreation);

        void defineActionFlow(TK_Ticket mTicket, StepAction stepAction);

        void defineProcessBtnFlow(TK_Ticket mTicket, StepProcessBtn stepProcessBtn);

        void defineApprovalFlow(TK_Ticket mTicket, StepApproval stepApproval);
    }
}
