package com.namoadigital.prj001.ui.act067;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.T_IO_Master_Data_Rec;

import java.util.ArrayList;

public interface Act067_Main_Contract {

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void setMDList(ArrayList<MD_Site> sites, ArrayList<MD_Partner> partners, ArrayList<T_IO_Master_Data_Rec.ModalObj> modals);

        void updateHeaderData(int inbound_prefix, int inbound_code, boolean newProcess);

        void callAct058(Bundle bundle);

        void callAct053(Bundle bundle);

        void callAct062();

        void showResult(ArrayList<HMAux> resultList, boolean inboundResult);

        void prepareFullRefresh();

        String getRequestingAct();

        void callAct065();

        void callAct014();

        void callAct066();

        void callAct012();
    }

    interface I_Presenter{

        IO_Outbound getOutbound(int prefix, int code);

        void executeWSMasterData(String type, boolean action);

        void processIOMasterDataRet(String wsReturn);

        void executeWsSaveOutboundHeader(IO_Outbound mOutbound, boolean newProcess);

        void processHeaderSave(int mPrefix, int mCode, String actReturnJson);

        void processPickingMove(HMAux item);

        void processSerialEdition(HMAux item);

        void checkForUpdateRequired(int mPrefix, int mCode);

        void executeWsSaveItem();

        void processItemSaveReturn(int mPrefix, int mCode, String jsonRet);

        void checkSyncProcess(IO_Outbound mOutbound);

        void processDownloadReturn(int mPrefix, int mCode, HMAux hmAux);

        boolean hasUpdateRequiredDbOrToken(int mPrefix, int mCode);

        void onBackPressedClicked(String requestAct, boolean headerInfoChanged);
    }
}
