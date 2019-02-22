package com.namoadigital.prj001.ui.act026;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act026_Main_Presenter {

    void getSOList(String product_code, String serial_id, boolean onlyAvaliables);

    void defineForwardFlow(HMAux so);

    void onBackPressedClicked();

    void setRequesting_act(String requesting_act);
}
