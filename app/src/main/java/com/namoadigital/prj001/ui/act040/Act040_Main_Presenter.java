package com.namoadigital.prj001.ui.act040;

import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.SO_Pack_Express;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_Presenter {

    void setSO_Pack_Express(long customer_code, long site_code, long operation_code, long product_code, String express_code);

    void setMD_Partner(long customer_code, long partner_code);

    void checkJump(long customer_code);

    void setPartners();

    void onBackPressedClicked();

    void onCreateSo_Pack_Express(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial, MD_Site md_site, MD_Operation md_operation, boolean connectionStatusAlter);

    boolean processValidation(SO_Pack_Express mSo_pack_express, long partner_code, long product_code, String serial);

    void executeSO_Pack_Express_Local(boolean connectionStatusAlter);

}
