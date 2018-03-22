package com.namoadigital.prj001.ui.act040;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_Presenter {

    void setSO_Pack_Express(long customer_code, long site_code, long operation_code, String express_code);

    void checkJump(long customer_code);

    void onBackPressedClicked();

}
