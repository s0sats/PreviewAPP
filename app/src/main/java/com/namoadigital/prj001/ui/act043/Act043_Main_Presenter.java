package com.namoadigital.prj001.ui.act043;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Params_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public interface Act043_Main_Presenter {

    ArrayList<TSO_Service_Search_Obj> processServiceList();

    void onBackPressedClicked();

    void setFileName(String fileName);

    boolean jsonFileExists();

    ArrayList<TSO_Service_Search_Obj> prepareListToAdapter(ArrayList<TSO_Service_Search_Obj> packServiceList);

    ArrayList<MD_Partner> getPackServicePartnerList();

    ArrayList<HMAux> generateSiteOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone);

    ArrayList<HMAux> generateSiteZoneOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone);
}
