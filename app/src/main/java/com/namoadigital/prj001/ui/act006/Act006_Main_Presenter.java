package com.namoadigital.prj001.ui.act006;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act006_Main_Presenter {

    void getPendencies();

    void getMD_Products();

    void onBackPressedClicked();

    void checkPendenciesFlow(int pendencies_qty);

    void executeSerialSearch(String product_id, String serial_id, String tracking, boolean forceExactSearch);
    //LUCHE - 17/03/2021 - Aplicado busca exata na leitura de barcode offline
    void offlineSerialSearch(boolean forceExactSearch);

    void extractSearchResult(String result);

    void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page, boolean from_offline_source);

    //String searchProductInfo(String product_code, String product_id);

    MD_Product searchProduct(String product_id);

}
