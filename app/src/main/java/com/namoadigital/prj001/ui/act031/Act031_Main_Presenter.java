package com.namoadigital.prj001.ui.act031;

import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act031_Main_Presenter {

    void serialFlow(String serial);

    void getProductInfo();

    boolean hasSerial(String serial);

    void onBackPressedClicked();

    void executeSerialSearch(Long product_code, String serial_id);

    void executeSoSearch(Long product_code, String serial_id, boolean save_serial);

    void getSerialInfo(Long product_code, String serial_id);

    void updateSerialInfo(MD_Product_Serial productSerial);

}

