package com.namoadigital.prj001.ui.act027;

import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by neomatrix on 12/07/17.
 */

public interface Act027_Serial_Presenter {

    void getProductInfo();

    void validadeSerialFlow(String serial , int required , int allow_new);

    boolean hasSerial(String serial);

    void defineForwardFlow(Object param);

    void defineBackFlow();

    void onBackPressedClicked();

    void executeSerialSearch(Long product_code, String serial_id);

    void executeSoSearch(Long product_code, String serial_id, boolean save_serial);

    void getSerialInfo(Long product_code, String serial_id);

    void updateSerialInfo(MD_Product_Serial productSerial);

}
