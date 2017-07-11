package com.namoadigital.prj001.ui.act026;

import com.namoadigital.prj001.model.SM_SO;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act026_Main_Presenter {

    void getSOList();

    void defineForwardFlow(SM_SO so);

    void onBackPressedClicked();
}
