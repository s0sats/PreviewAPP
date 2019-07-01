package com.namoadigital.prj001.ui.act054;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;
import java.util.List;

public interface Act054_Main_Contract {
    String MOVE_PLANNED_TYPE_SEARCH = "move_planned_type_search";
    String INBOUND_TYPE_SEARCH = "inbound_type_search";
    String OUTBOUND_TYPE_SEARCH = "outbound_type_search";
    String ORIGIN_ORIENTATION_SEARCH = "origin_orientation_search";
    String DESTINY_ORIENTATION_SEARCH = "destiny_orientation_search";

    interface I_Presenter{

        void onBackPressedClicked(String requesting_act);

        void getMovements(boolean inboundStatus, boolean outboundStatus, boolean movePlannedStatus, String zone, boolean originStatus, boolean destinyStatus);

        void processIOMoveSearch(String resultado);

        List<HMAux> getZoneList();

        String getPendecies();

        void syncMovements();

        void getPendenciesList();

        void executeWsSaveItem();

        void saveSearchPreferences(boolean move_planned_status, boolean inbound_status, boolean outbound_status, boolean origin_status, boolean destiny_status);

        boolean getSearchFilterPreferences(String key_prefs, boolean default_value);

        void processItemSaveReturn(int mPrefix, int mCode, String jsonRet);

        boolean hasWaitingSyncMovePendency();

        boolean hasWaitingSyncBlindPendency();

        boolean hasWaitingSyncPutAwayPendency();

        void executeWsSaveBlindItem();
    }

    interface I_View{

        void showPD(String dialog_serial_search_ttl, String dialog_serial_search_start);

        void showMsg(String title, String msg);

        void setWsProcess(String name);

        void callAct055(Bundle bundle);

        void callAct051();

        void setWs_process(String name);

        void refreshPendencyCount();

        void showResult(ArrayList<HMAux> resultList);
    }
}
