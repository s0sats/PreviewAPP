package com.namoadigital.prj001.ui.act031;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act031_Main_View {

    void showPD(String title , String msg);

    void showAlertDialog(String title, String msg);

    void setWs_process(String ws_process);

    void setProductValues(MD_Product md_product);

    void callAct030(Context context);

    void showSingleResultMsg(String ttl, String msg);

    void showSerialResults(ArrayList<HMAux> returnList);

    void refreshUI();

    void reApplySerialIdToFrag();

    void applyReceivedSerialToFrag(MD_Product_Serial serial_returned);

    void updateProductSerialValues(MD_Product_Serial mdProductSerial);


}
