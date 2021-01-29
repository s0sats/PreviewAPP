package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_View {

    void setProductValues(MD_Product md_product);

    void setWsProcess(String wsProcess);

    void showPD(String title, String msg);

    void showAlertDialog(String title, String msg);

    void continueOfflineV2(boolean serial_offline);

    void callAct009(Context context);

    //void callAct007(Context context);

    void callAct011(Context context);

    void callAct017(Context context);

    void callAct006(Context context);

    void showSingleResultMsg(String ttl, String msg);

    void showSerialResults(ArrayList<HMAux> returnList);

    void refreshUI();

    void reApplySerialIdToFrag();

    void applyReceivedSerialToFrag(MD_Product_Serial serial_returned);

    void updateProductSerialValues(MD_Product_Serial mdProductSerial);

    void setMdProductSerial(MD_Product_Serial mdProductSerial);
    //17/08/18
    void callAct013(Context context);

    void callAct081(Context context);

    boolean isOffHandForm();

    void callAct071(Context context, Bundle bundle);

    boolean isHas_tk_ticket_is_form_off_hand();

    String getmdProductSerialSiteCode();
}
