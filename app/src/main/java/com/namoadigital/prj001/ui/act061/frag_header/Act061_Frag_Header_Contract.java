package com.namoadigital.prj001.ui.act061.frag_header;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Inbound;

import java.util.ArrayList;

public interface Act061_Frag_Header_Contract {
    interface I_View{

    }

    interface I_Presenter{

        HMAux getZoneDbValue(ArrayList<HMAux> zoneOptions, int zone_code);

        HMAux getLocalDbValue(ArrayList<HMAux> zoneOptions,int zone_code, int local_code);

        ArrayList<HMAux> getZonesOptions();

        ArrayList<HMAux> getLocalsOptions(String zone_code);

        ArrayList<HMAux> generateStatusList(ArrayList<String> status);

        boolean hasConfirmedItem(IO_Inbound mInbound);

        boolean allItemsDone(IO_Inbound mInbound);

        boolean hasSyncRequired(int inbound_prefix, int inbound_code);
    }
}
