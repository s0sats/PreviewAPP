package com.namoadigital.prj001.ui.act023;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act023_Main_Presenter {

    void getProductInfo();

    void validadeSerialFlow(String serial , int required , int allow_new);

    boolean hasSerial(String serial);

    void defineForwardFlow(Object param);

    void defineBackFlow();

    void onBackPressedClicked();

    void executeSoSearch(Long product_code, String serial_id);

}
