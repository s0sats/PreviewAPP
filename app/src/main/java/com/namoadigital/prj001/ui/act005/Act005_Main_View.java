package com.namoadigital.prj001.ui.act005;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MenuMainNamoa;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act005_Main_View {

    void loadMenuV2(ArrayList<MenuMainNamoa> menus, int columnsQty);

    void showPD();

    void callAct006(Context context);

    void callAct036(Context context);

    void callAct012(Context context);

    void callAct014(Context context);

    void callAct016(Context context);

    void callAct046(Context context);

    void callAct018(Context context);

    void callAct021(Context context);

    void callAct030(Context context);

    void callAct034(Context context);

    void showNoConnectionDialog();

    void closeApp();

    void setWsProcess(String ws_called);

    void setWsSoProcess(String ws_called);

    void callLoginProcess();

    void callChangeCustomerProcess();

    void cleanUpResults();

    void setWsProcessList(ArrayList<HMAux> wsProcessList);

    void setSyncAfterSave(boolean syncAfterSave);

    int getSendBadgeQty();

    int getImagesToUpload();

    int getOpenForms(HMAux customers);

    boolean getPendingForms();

    void callAct051(Context context);

    void callAct040(Context context);

    void callAct068(Context context);

    void callAct069(Context context);

    void addWsResults(ArrayList<HMAux> auxResults);
}
