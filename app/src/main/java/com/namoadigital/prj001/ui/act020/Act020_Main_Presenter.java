package com.namoadigital.prj001.ui.act020;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by d.luche on 17/05/2017.
 */

public interface Act020_Main_Presenter {

    void getProductSerialList(String ws_result);

    boolean getChkForHideSerialInfoPreference();

    void onBackPressedClicked();

    //void executeSerialSearch(String product_id, String serial, String tracking);

    void defineFlow(MD_Product_Serial productSerial,boolean no_serial);

    void updateSyncChecklist();

    void createNewSerialFlow(MD_Product mdProduct, String serial_id);

    void prepareAct009();

    void prepareAct011();

    void startDownloadServices();

    String searchProductInfo(String product_code,String product_id);

    boolean hasSyncRegister();

    void prepareAct008();
}
