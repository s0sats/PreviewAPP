package com.namoadigital.prj001.ui.act055;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Move_Search_Record;

import java.util.ArrayList;
import java.util.List;

public interface Act055_Main_Contract {

    interface I_Presenter{
        void onBackPressedClicked(String requesting_act);
    }

    interface I_View{

        void showPD(String dialog_serial_search_ttl, String dialog_serial_search_start);

        void setWsProcess(String name);

        void callAct054();
    }
}
