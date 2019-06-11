package com.namoadigital.prj001.ui.act064;

import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

public interface Act064_Main_Contract {

    interface I_View{


        void showSaveSucessfully();

        void callAct051();
    }

    interface I_Presenter{
        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val);

        void setDefaultZone(SearchableSpinner ss_zone);

        void saveBlindMove(int zone_code, int local_code, int reason_code);

        ArrayList<HMAux> getMoveReasonList();

        void loadSerialInfo(TextView tv_product_cod_val, TextView tv_serial_val);

        void onBackPressed();
    }
}
