package com.namoadigital.prj001.ui.act020;

/**
 * Created by d.luche on 17/05/2017.
 */

public interface Act020_Main_Presenter {

    void getProductSerialList(String ws_result);

    void onBackPressedClicked();

    void executeSerialSearch(String product_code, String product_id,String serial_code, String serial_id);
}
