package com.namoadigital.prj001.ui.act030;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act030_Main_Presenter {

    void getProductSerialList(String ws_result);

    void onBackPressedClicked();

    void executeSerialSearch(String product_id, String serial_id, String tracking );

    void defineFlow(MD_Product_Serial productSerial, boolean new_serial);

    void checkSingleProduct();

    boolean checkProductExists(String product_id, String serial);

    boolean productAllowNewSerial(MD_Product md_product);

    boolean productAllowNewSerial(String product_code, String product_id);

    String searchProductInfo(String product_code, String product_id );
}
