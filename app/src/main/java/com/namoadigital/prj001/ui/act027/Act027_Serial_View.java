package com.namoadigital.prj001.ui.act027;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 12/07/17.
 */

public interface Act027_Serial_View {

    void setProductValues(MD_Product md_product);

    void showPD(String title, String msg);

    void showAlertDialog(String title, String msg);

    void showSingleResultMsg(String ttl, String msg);

    void showSerialResults(ArrayList<HMAux> returnList);
    /*
    * Review S.O - Adição do tracking
    */
    void setSerialValuesV2(HMAux md_product_serial, MD_Product_Serial serialObjDb);

    void appendTracking(String tracking);

    void scrollToTracking();

    void setTrackingListChanged(boolean trackingListChanged);

    String getSearched_tracking();

    void cleanSearched_tracking();

    void setWsProcess(String ws_process);

}
