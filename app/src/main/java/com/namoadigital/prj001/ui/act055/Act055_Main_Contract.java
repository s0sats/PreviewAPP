package com.namoadigital.prj001.ui.act055;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Move_Search_Record;

import java.util.ArrayList;
import java.util.List;

public interface Act055_Main_Contract {

    interface I_Presenter{
        void onBackPressedClicked(String requesting_act);

        void getDownloadedMove(String moveCode);

        void processSearchReturn(HMAux searchRet);

        void getOfflineMove(String moveKey);
    }

    interface I_View{

        void showPD(String title, String desc);

        void setWsProcess(String name);

        void callAct054();

        void showAlert(String title, String msg);

        void callAct058(Bundle bundle);
    }
}
