package com.namoadigital.prj001.ui.act058.frag;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;

public interface Frag_Move_Create_Contract {

    interface I_Presenter{

        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val);

        void setDefaultZone(SearchableSpinner ss_zone);
    }

    interface I_View{

    }
}
