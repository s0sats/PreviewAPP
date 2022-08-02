package com.namoadigital.prj001.ui.act040;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.model.SoPackExpressPacksLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_Presenter {

    void searchSO_Pack_Express(long customer_code, String express_code);

    //void setMD_Partner(long customer_code, long partner_code);

    //void checkJump(long customer_code);

    MD_Product getProdutctInfo(long product_code);

    void loadPartners(String partner_code);

    void onBackPressedClicked(SO_Pack_Express mSoPackExpress, String serialID, boolean skipConfirm);

    void onCreateSo_Pack_Express(SO_Pack_Express mSo_pack_express,
                                 MD_Partner md_partner,
                                 MD_Product md_product,
                                 String serial,
                                 String billingInfo1,
                                 String billingInfo2,
                                 String billingInfo3
                                );

    void onCreateSo_Pack_Express_Structure(SO_Pack_Express mSo_pack_express,
                                 MD_Partner md_partner,
                                 MD_Product md_product,
                                 String serial,
                                 String billingInfo1,
                                 String billingInfo2,
                                 String billingInfo3
                                );

    void executeSO_Pack_Express_Local();

    SO_Pack_Express_Local checkOrderAlreadyExists(long customer_code, String site_code, long operation_code, long product_code, String express_code, String serial_id);
    //08/10/2018
    void executeSerialSearch(MD_Product mdProduct, String serial_id);
    //
    void extractSearchResult(MD_Product mdProduct, String serial_id, String result);

    void executeSerialSave();

    void processSerialSaveResult(HMAux hmSaveResult);

    int getExpressSoPendency(HMAux hmAux_Trans);

    String getFormattedRuleHelper(HMAux hmAux_trans, String serial_rule, Integer min, Integer max);

    void getLastExpressInfoInSiteOper();

    void handleHistClick();

    boolean hasSerialOrExpressOsPendency();

    boolean hasSerialUpdateRequired();

    boolean hasNoTrackingDuplicated(ArrayList<MKEditTextNM> trackingFields);

    String getFormattedTrackingDuplicated(String tracking_duplicated_msg, ArrayList<MKEditTextNM> trackingFields);

    //void checkSerialUpdateRequired(long product_code, String serial_id);
    void executeWS_SO_Service_Search(SO_Pack_Express mSo_pack_express, String serialId);

    List<SoPackExpressPacksLocal> getExpressPacks(SO_Pack_Express mSo_pack_express);

    boolean hasPackServiceFile(int contract_code, long product_code, int category_price_code, long site_code, long operation_code);

    SO_Pack_Express_Local getExpressPackLocal(long customer_code, long product_code, long site_code, long operation_code, int bundle_express_tmp);
}
