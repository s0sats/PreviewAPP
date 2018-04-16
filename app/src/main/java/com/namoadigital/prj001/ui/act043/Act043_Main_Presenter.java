package com.namoadigital.prj001.ui.act043;

import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public interface Act043_Main_Presenter {

    ArrayList<TSO_Service_Search_Obj> processServiceList(String service_list);

    void onBackPressedClicked();
}
