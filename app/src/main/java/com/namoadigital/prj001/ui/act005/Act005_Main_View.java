package com.namoadigital.prj001.ui.act005;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act005_Main_View {

    void loadMenu(List<HMAux> menus);

    void showPD();

    void callAct006(Context context);

    void callAct012(Context context);

    void callAct014(Context context);

    void callAct016(Context context);

    void callAct018(Context context);

    void showNoConnectionDialog();

    void closeApp();

    void setWsProcess(String ws_called);

    void callLoginProcess();

}
