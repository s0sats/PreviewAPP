package com.namoadigital.prj001.ui.act021;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by d.luche on 21/06/2017.
 */

public interface Act021_Main_Presenter {

    boolean checkForSoToSend();

    void getPendencies();

    void getMD_Products();

    void getSync();

    void executeSerialTracking(String serial, String tracking);

    void executeSerialSearch(String product_id, String serial_id, String tracking);

    void extractSearchResult(String result);

    void defineSearchResultFlow(String result, String tracking);

    void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page);

    void onBackPressedClicked();

    String searchProductInfo(String product_code, String product_id);

    MD_Product searchProduct(String product_id);
}
