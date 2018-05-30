package com.namoadigital.prj001.ui.act020;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by d.luche on 17/05/2017.
 */

public interface Act020_Main_Presenter {

    void getProductSerialList(String ws_result);

    void onBackPressedClicked();

    //void executeSerialSearch(String product_id, String serial, String tracking);

    void defineFlow(MD_Product_Serial productSerial);

    //void updateSyncChecklist();

    void createNewSerialFlow(MD_Product mdProduct, String serial_id);

    void prepareAct009();

    void startDownloadServices();

    boolean checkFormXOperationExists();

    String searchProductInfo(String product_code,String product_id);
}
