package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act023_Main_View {

    void setProductValues(MD_Product md_product);

    void setWs_process(String ws_process);

    //void fieldFocus();

    void showPD(String title , String msg);

    void showAlertDialog(String title, String msg);

    void callAct021(Context context);

    void callAct022(Context context);

    void callAct025(Context context);

    void showSingleResultMsg(String ttl, String msg);

    void showSerialResults(ArrayList<HMAux> returnList);

    void callAct026(Context context);

    void callAct027(Context context,Bundle bundle);

    void refreshUI();

}
