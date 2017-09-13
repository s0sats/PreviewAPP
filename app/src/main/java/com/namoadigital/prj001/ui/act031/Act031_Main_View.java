package com.namoadigital.prj001.ui.act031;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act031_Main_View {
    void fieldFocus();

    void showPD(String title , String msg);

    void showAlertDialog(String title, String msg);

    String getSearched_tracking();

    void cleanSearched_tracking();

    void setWs_process(String ws_process);

    void setProductValues(MD_Product md_product);

    void setSerialValues(HMAux md_product_serial);

    void setSerialValuesV2(HMAux md_product_serial);

    void callAct030(Context context);

    void showSingleResultMsg(String ttl, String msg);

    void showSerialResults(ArrayList<HMAux> returnList);

    void appendTracking(String tracking);

    void scrollToTracking();

    void setTrackingListChanged(boolean trackingListChanged);

}
