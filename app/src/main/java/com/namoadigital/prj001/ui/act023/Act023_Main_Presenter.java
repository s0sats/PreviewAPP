package com.namoadigital.prj001.ui.act023;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act023_Main_Presenter {

    void defineForwardFlow(Object param);

    void defineBackFlow();

    void onBackPressedClicked();

    void executeSoSearch(int product_code, String serial_id, String so_mult);

}
