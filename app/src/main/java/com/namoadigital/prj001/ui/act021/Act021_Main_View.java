package com.namoadigital.prj001.ui.act021;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

/**
 * Created by d.luche on 21/06/2017.
 */

public interface Act021_Main_View {

    void setPendencies(int qty, String qtyMyPendencies);

    void setProduto(ArrayList<MD_Product> list);

    void setSync(int qty);

    void callAct005(Context context);

    void callAct022(Context context);

    void callAct025(Context context, Bundle bundle);

    void callAct023(Context context, Bundle bundle);

    void callAct026(Context context);

    void callAct040(Context context);

    void showNewOptDialog();

    void showMsg(String ttl,String msg);

    void showPD(String ttl, String msg);

    void showNoCoPendencies();

    void setSoExpressVisibility(boolean isVisible);

}
