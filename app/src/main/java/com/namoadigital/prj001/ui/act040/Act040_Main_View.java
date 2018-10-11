package com.namoadigital.prj001.ui.act040;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.SO_Pack_Express;

import java.util.ArrayList;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_View {

    void loadSO_Pack_Express(SO_Pack_Express so_pack_express, String express_code);

    void setPartner(HMAux partner);

    void disablePartnerSelector();

    //void loadMD_Product(MD_Product md_product);

    void callAct021(Context context);

    //void callAct041(Context context);

    //void jumpToOne();

    void automationCleanForm();

    void setPartnerList(ArrayList<HMAux> partnerList);

    void showPD(String ttl, String msg);

    void showMsg(String ttl, String msg);

    void showMsgToast(String msg);

    void setConnectionStatusAlter(boolean connectionStatusAlter);
    //08/10/2018
    void setWsProcess(String wsProcess);
    //
    void callAct048(Context context, Bundle bundle);

    void addWsAuxResult(HMAux auxResult);

    boolean isExitProcess();

    void setExitProcess(boolean exitProcess);

    boolean isConnectionStatusAlter();
}
