package com.namoadigital.prj001.ui.act058.frag;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Class;

import java.util.ArrayList;
import java.util.List;

public interface Frag_Move_Create_Contract {

    interface I_Presenter{

        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val);

        void setLocalValue(SearchableSpinner ss_local);

        void setLocalValue(SearchableSpinner ss_local, Integer zone_code, Integer local_code);

        MD_Class getClassFromMove(Integer classCode);

        ArrayList<HMAux> getClassList();

        ArrayList<HMAux> getMoveReasonList();

        List<IO_Move_Tracking> getTrackingFromMove();

        void setDefaultZone(SearchableSpinner ss_zone);

        boolean removeTrackingFromMove(IO_Move_Tracking io_move_tracking);

        String getZoneDesc(int zone_code);

        String getLocalId(Integer local_code,Integer zone_code);

        boolean hasSerialBypass();
    }

    interface I_View{

    }
}
