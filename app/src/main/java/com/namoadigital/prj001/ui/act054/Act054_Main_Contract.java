package com.namoadigital.prj001.ui.act054;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Move_Search_Record;

import java.util.ArrayList;
import java.util.List;

public interface Act054_Main_Contract {

    interface I_Presenter{

        void onBackPressedClicked(String requesting_act);

        void getMovements(boolean inboundStatus, boolean outboundStatus, boolean movePlannedStatus, String zone, boolean originStatus, boolean destinyStatus);

        void processIOMoveSearch(String resultado);

        List<HMAux> getZoneList();
    }

    interface I_View{

        void showPD(String dialog_serial_search_ttl, String dialog_serial_search_start);

        void setWsProcess(String name);

        void callAct055(Bundle bundle);

        void callAct051();
    }
}
