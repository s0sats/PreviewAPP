package com.namoadigital.prj001.ui.act043;

import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public interface Act043_I_Add_Service_Interaction {

    ArrayList<TSO_Service_Search_Obj> getPackServiceAdapterList(ArrayList<TSO_Service_Search_Obj> data);
    ArrayList<MD_Partner> getPartnerList();
}
