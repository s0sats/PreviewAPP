package com.namoadigital.prj001.ui.act010;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act010_Main_View {

    void loadForms(List<HMAux> forms);

    void addFormInfoToBundle(HMAux item);

    void callAct009(Context context);

    void callAct011(Context context);

    void alertFormNotReady();

    void showAlertMsg(String title, String msg);

}
