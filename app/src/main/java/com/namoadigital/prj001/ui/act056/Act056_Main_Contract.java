package com.namoadigital.prj001.ui.act056;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;

public interface Act056_Main_Contract {

    interface I_View{
        void callAct051();

    }

    interface I_Presenter{
        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone,SearchableSpinner ss_local, boolean reset_val);

        void setDefaultZone(SearchableSpinner ss_zone);

        String getInboundPendencies();

        void onBackPressedClicked();
    }
}
