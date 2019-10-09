package com.namoadigital.prj001.ui.act043;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Params_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public interface Act043_I_Add_Service_Interaction {

    ArrayList<TSO_Service_Search_Obj> getPackServiceAdapterList(ArrayList<TSO_Service_Search_Obj> data);
    ArrayList<MD_Partner> getPartnerList();
    ArrayList<HMAux> generateSiteOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone);
    ArrayList<HMAux> generateSiteZoneOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone);
    void calculateTotalPrice(TSO_Service_Search_Obj packService);
    void resetPackService(TSO_Service_Search_Obj packService);
}
