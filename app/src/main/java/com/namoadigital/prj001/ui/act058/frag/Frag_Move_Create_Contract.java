package com.namoadigital.prj001.ui.act058.frag;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.model.MD_Class;

public interface Frag_Move_Create_Contract {

    interface I_Presenter{

        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val);

        void setLocalValue(SearchableSpinner ss_local);

        MD_Class getClassFromMove(int classCode);

        void setDefaultZone(SearchableSpinner ss_zone);
    }

    interface I_View{

    }
}
