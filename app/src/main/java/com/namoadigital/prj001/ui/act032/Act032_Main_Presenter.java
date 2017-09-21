package com.namoadigital.prj001.ui.act032;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act032_Main_Presenter {

    void getSOList(String product_code, String serial_id);

    void defineForwardFlow(HMAux so);

    void onBackPressedClicked();

}
