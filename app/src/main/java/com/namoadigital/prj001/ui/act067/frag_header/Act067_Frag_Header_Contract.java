package com.namoadigital.prj001.ui.act067.frag_header;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

public interface Act067_Frag_Header_Contract {
    interface I_View{

    }

    interface I_Presenter{

        HMAux getZoneDbValue(ArrayList<HMAux> zoneOptions, int zone_code);

        HMAux getLocalDbValue(ArrayList<HMAux> zoneOptions, int zone_code, int local_code);

        ArrayList<HMAux> getZonesOptions();

        ArrayList<HMAux> getLocalsOptions(String zone_code);
    }
}
