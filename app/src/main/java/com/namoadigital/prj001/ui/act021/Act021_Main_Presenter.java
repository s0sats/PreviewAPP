package com.namoadigital.prj001.ui.act021;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by d.luche on 21/06/2017.
 */

public interface Act021_Main_Presenter {

    boolean checkForSoToSend();

    void getPendencies();

    void getSync();

    void defineFlow(HMAux hmAux);

    void executeSerialTracking(String serial, String tracking);

    void defineSearchResultFlow(String result, String tracking);

    void onBackPressedClicked();

    void checkSOExpressProfile();
}
