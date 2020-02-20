package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act011_Main_View {

    void loadFragment_CF_Fields(List<HMAux> cf_fields, boolean bNew, GE_Custom_Form_Local formLocal, GE_Custom_Form_Data formData, String prefix, List<HMAux> pdfs, int indexF, int signature, int Require_serial_done);

    void showMsg(String title, String msg, int type);

    void callAct005(Context context);

    void callAct027(Context context, Bundle bundle);

    void callSignature();

    void callNFCResults();

    void showSignature();

    void callAct006(Context context);

    boolean allowFinalizeWithNewBtn();

    void addWsResults(ArrayList<HMAux> formDataErroAux);

    void afterSaveFlow();
}
