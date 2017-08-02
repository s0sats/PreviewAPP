package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;

/**
 * Created by neomatrix on 12/07/17.
 */

public interface Act027_Serial_View {

    void setProductValues(MD_Product md_product);

    void setSerialValues(HMAux md_product_serial);

    void setWs_process(String ws_process);

    void fieldFocus();

    void showPD(String title, String msg);

    void showAlertDialog(String title, String msg);

    void continueOffline();

    void callAct022(Context context);

    void callAct024(Context context, Bundle bundle);

    void callAct025(Context context);

}
