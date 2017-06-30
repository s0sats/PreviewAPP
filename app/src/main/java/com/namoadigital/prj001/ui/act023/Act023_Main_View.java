package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act023_Main_View {

    void setProductValues(MD_Product md_product);

    void fieldFocus();

    void showPD(String title , String msg);

    void showAlertDialog(String title, String msg);

    void continueOffline();

    void callAct022(Context context);

    void callAct024(Context context, Bundle bundle);
}
