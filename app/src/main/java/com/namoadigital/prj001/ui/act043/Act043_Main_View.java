package com.namoadigital.prj001.ui.act043;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

public interface Act043_Main_View {

    void showServiceInfoDialog(HMAux item);

    void showPD(String ttl, String msg);

    void callAct027(Context context);
}
