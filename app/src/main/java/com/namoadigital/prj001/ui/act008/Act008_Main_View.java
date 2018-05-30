package com.namoadigital.prj001.ui.act008;

import android.content.Context;

import com.namoadigital.prj001.model.MD_Product;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_View {

    void setProductValues(MD_Product md_product);

//    void fieldFocus();

    void setWsProcess(String wsProcess);

    void showPD(String title, String msg);

    void showAlertDialog(String title, String msg);

    void continueOfflineV2(boolean serial_offline);

    void callAct009(Context context);

    void callAct007(Context context);

    void callAct011(Context context);

    void callAct017(Context context);

}
