package com.namoadigital.prj001.ui.act027;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;

import java.util.ArrayList;

/**
 * Created by neomatrix on 12/07/17.
 */

public interface Act027_Serial_Presenter {

    void getProductInfo();

    void executeSerialSave();

    void getSerialInfo(Long product_code, int serial_code);

    void updateSerialInfo(MD_Product_Serial productSerial);

    void processSerialSaveResult(String product_code, int serial_code, HMAux hmSaveResult);
    /*
    * Review S.O - Adição do tracking
    */
    void updateTrackingReference(ArrayList<MD_Product_Serial_Tracking> tracking_list);

    void processTrackingResult(HMAux auxResult, MD_Product_Serial serialObj);

    boolean isTrackingListed(String tracking);

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);


}
