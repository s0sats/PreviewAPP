package com.namoadigital.prj001.ui.act006;

import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act006_Main_Presenter {

    void getPendencies();

    void onBackPressedClicked();

    void checkPendenciesFlow(int pendencies_qty);

    void executeSerialSearch(String product_id, String serial_id, String tracking);

    void extractSearchResult(String result);

    void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list);

    String searchProductInfo(String product_code,String product_id);

}
