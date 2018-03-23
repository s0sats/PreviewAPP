package com.namoadigital.prj001.ui.act040;

import com.namoadigital.prj001.model.SO_Pack_Express;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_Presenter {

    void setSO_Pack_Express(long customer_code, long site_code, long operation_code, String express_code, long product_code);

    void setMD_Partner(long customer_code, long partner_code);

    void checkJump(long customer_code);

    void setPartners();

    void onBackPressedClicked();

    boolean processValidation(SO_Pack_Express mSo_pack_express, long partner_code, long product_code, String serial);

}
