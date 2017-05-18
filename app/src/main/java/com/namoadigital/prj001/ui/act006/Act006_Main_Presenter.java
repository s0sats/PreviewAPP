package com.namoadigital.prj001.ui.act006;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act006_Main_Presenter {

    void getPendencies();

    void onBackPressedClicked();

    void checkPendenciesFlow(int pendencies_qty);

    void defineFlow(HMAux item);

}
