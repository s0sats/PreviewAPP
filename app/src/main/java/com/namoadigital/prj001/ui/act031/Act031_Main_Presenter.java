package com.namoadigital.prj001.ui.act031;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;

import java.util.ArrayList;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act031_Main_Presenter {

    void serialFlow(String serial);

    void getProductInfo();

    boolean hasSerial(String serial);

    void onBackPressedClicked();

    void executeSerialSearch(Long product_code, String serial_id);

    void executeSaveSerial(Long product_code, String serial_id, boolean save_serial);

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

    void getSerialInfo(Long product_code, String serial_id);

    void updateSerialInfo(MD_Product_Serial productSerial);

    void saveNewSerialInfo(Long product_code, String serial_id);

    void saveNewSerialInfo(MD_Product_Serial md_product_serial);

    void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

    void processTrackingResult(HMAux auxResult, MD_Product_Serial serialObj);

    boolean isTrackingListed(String tracking);

    void updateTrackingReference(ArrayList<MD_Product_Serial_Tracking> tracking_list);

}

