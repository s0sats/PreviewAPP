package com.namoadigital.prj001.ui.act043;

import android.content.Context;

import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

public interface Act043_Main_View {

    void showPD(String ttl, String msg);

    void callAct027(Context context);

    String getCurrentFrag();

    void setFragByTag(String tag);

    boolean hasItemAdded();

    TSO_Service_Search_Obj getPackDetailObj();

    void alertPackDetailRemoveConfirm(TSO_Service_Search_Obj packDetailObj);
}
