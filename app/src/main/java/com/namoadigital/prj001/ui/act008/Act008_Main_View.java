package com.namoadigital.prj001.ui.act008;

import android.content.Context;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_View {

    void setProductValues(MD_Product md_product);

    void fieldFocus();

    void showPD(String wsProcess);

    void showAlertDialog(String title, String msg);

    void continueOffline();

    void callAct009(Context context);

    void callAct007(Context context);

    /**
     * Outro Fluxo para obtencao do numero de serie para o agendamento
     * @param context
     */
    void callAct017(Context context);

    void callAct011(Context context);



}
