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

    void executeSerialSave(Long product_code, String serial_id, boolean save_serial);

    void getSerialInfo(Long product_code, String serial_id);

    void updateSerialInfo(MD_Product_Serial productSerial);

    void processSerialSaveResult(String product_code, String serial_id, HMAux hmSaveResult);
    /*
    * Review S.O - Adição do tracking
    */
    void updateTrackingReference(ArrayList<MD_Product_Serial_Tracking> tracking_list);

    void processTrackingResult(HMAux auxResult, MD_Product_Serial serialObj);

    boolean isTrackingListed(String tracking);

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);


}
