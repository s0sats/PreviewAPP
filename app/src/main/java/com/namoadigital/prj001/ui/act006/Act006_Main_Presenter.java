package com.namoadigital.prj001.ui.act006;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.TProduct_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act006_Main_Presenter {

    void getPendencies();

    void onBackPressedClicked();

    void checkPendenciesFlow(int pendencies_qty);

    void defineFlow(HMAux item);

    void executeSerialSearch(String serial_id);

    /*void defineSearchResultFlow(String result);*/

    void extractSearchResult(String result);

    void defineSearchResultFlowV2(ArrayList<TProduct_Serial> serial_list);


}
