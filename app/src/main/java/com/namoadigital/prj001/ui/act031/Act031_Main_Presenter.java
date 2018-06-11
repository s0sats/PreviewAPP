package com.namoadigital.prj001.ui.act031;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act031_Main_Presenter {

    void serialFlow(String serial);

    void getProductInfo();

    //boolean hasSerial(String serial);

    void onBackPressedClicked(boolean jump_ask);

    void executeSerialSearch(Long product_code, String product_id, String serial_id);

    void executeSerialSave();

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

    void updateSerialData(MD_Product_Serial productSerial);

    void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

    void searchLocalSerial(long product_code, String serial_id);

    void extractSearchResult(String result);
}

