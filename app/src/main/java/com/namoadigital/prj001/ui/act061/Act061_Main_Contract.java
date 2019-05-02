package com.namoadigital.prj001.ui.act061;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.*;

import java.util.ArrayList;

public interface Act061_Main_Contract {

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void callAct056();

        void setMDList(ArrayList<MD_Site> sites, ArrayList<MD_Partner> partners, ArrayList<T_IO_Master_Data_Rec.ModalObj> modals);

        void setFromOutboundList(ArrayList<IO_Outbound_Search_Record> outbound);

        void updateHeaderData(int inbound_prefix, int inbound_code, boolean newProcess);

        void callAct058(Bundle bundle);

        void callAct053(Bundle bundle);

        void callAct062();

        void showResult(ArrayList<HMAux> resultList, boolean inboundResult);

        void prepareFullRefresh();
    }

    interface I_Presenter{

        IO_Inbound getInbound(int prefix,int code);

        void executeWSMasterData(String type, boolean action);

        void onBackPressedClicked();

        void processIOMasterDataRet(String wsReturn);

        void executeWsSearchOutbound(String from_site);

        void processFromOutboundRet(String wsReturn);

        void executeWsSaveInboundHeader(IO_Inbound mInbound, boolean newProcess);

        void processHeaderSave(int mPrefix, int mCode, String actReturnJson);

        void processPutAwayMove(HMAux item);

        void processSerialEdition(HMAux item);

        void checkForUpdateRequired(int mPrefix, int mCode);

        void executeWsSaveItem();

        void processItemSaveReturn(int mPrefix, int mCode, String jsonRet);

        void checkSyncProcess(IO_Inbound mInbound);

        void processDownloadReturn(int mPrefix, int mCode, HMAux hmAux);
    }
}
