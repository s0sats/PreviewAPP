package com.namoadigital.prj001.ui.act067.frag_header;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Outbound;

import java.util.ArrayList;

public interface Act067_Frag_Header_Contract {
    interface I_View{

    }

    interface I_Presenter{

        HMAux getZoneDbValue(ArrayList<HMAux> zoneOptions, int zone_code);

        HMAux getLocalDbValue(ArrayList<HMAux> zoneOptions, int zone_code, int local_code);

        ArrayList<HMAux> getZonesOptions();

        ArrayList<HMAux> getLocalsOptions(String zone_code);

        boolean hasConfirmedItem(IO_Outbound mOutbound);

        boolean allItemsDone(IO_Outbound mOutbound);

        ArrayList<HMAux> generateStatusList(ArrayList<String> status);
    }
}
