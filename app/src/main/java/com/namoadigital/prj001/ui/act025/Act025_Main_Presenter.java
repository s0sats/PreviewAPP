package com.namoadigital.prj001.ui.act025;

import com.namoadigital.prj001.model.TProduct_Serial;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act025_Main_Presenter {

    void getProductSerialList(String ws_result);

    void onBackPressedClicked();

    void executeSerialSearch(String product_id, String serial_id,String tracking);

    void defineFlow(TProduct_Serial productSerial);

    String searchProductInfo(String product_code,String product_id);

    void checkSingleProduct();

   /* void updateSyncChecklist();

    void prepareAct009();

    void startDownloadServices();

    boolean checkFormXOperationExists();*/
}
