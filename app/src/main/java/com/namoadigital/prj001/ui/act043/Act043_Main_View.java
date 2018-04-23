package com.namoadigital.prj001.ui.act043;

import android.content.Context;

public interface Act043_Main_View {

    void showPD(String ttl, String msg);

    void callAct027(Context context);

    String getCurrentFrag();

    void setFragByTag(String tag);

    boolean hasItemAdded();
}
