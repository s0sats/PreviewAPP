package com.namoadigital.prj001.ui.act043;

import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public interface Act043_Main_Presenter {

    ArrayList<TSO_Service_Search_Obj> processServiceList(String service_list);

    void onBackPressedClicked();

    ArrayList<TSO_Service_Search_Obj> prepareListToAdapter(ArrayList<TSO_Service_Search_Obj> packServiceList);

    ArrayList<MD_Partner> getPackServicePartnerList(String mLink);
}
