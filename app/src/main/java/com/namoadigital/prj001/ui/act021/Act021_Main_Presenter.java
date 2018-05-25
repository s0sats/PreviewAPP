package com.namoadigital.prj001.ui.act021;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;

/**
 * Created by d.luche on 21/06/2017.
 */

public interface Act021_Main_Presenter {

    boolean checkForSoToSend();

    void getPendencies();

    void getMD_Products();

    void getSync();

    void defineFlow(HMAux hmAux);

    void executeSerialTracking(String serial, String tracking);

    void defineSearchResultFlow(String result, String tracking);

    void onBackPressedClicked();

    void checkSOExpressProfile();

    String searchProductInfo(String product_code, String product_id);

    MD_Product searchProduct(String product_id);
}
