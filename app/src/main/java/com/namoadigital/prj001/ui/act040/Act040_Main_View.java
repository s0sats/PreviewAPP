package com.namoadigital.prj001.ui.act040;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SO_Pack_Express;

import java.util.ArrayList;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_View {

    void loadSO_Pack_Express(SO_Pack_Express so_pack_express);

    void loadMD_Partner(MD_Partner md_partner);

    void loadMD_Product(MD_Product md_product);

    void callAct005(Context context);

    void callAct041(Context context);

    void jumpToOne();

    void automationCleanForm();

    void setPartnerList(ArrayList<HMAux> partnerList);

    void showPD(String ttl, String msg);
}
