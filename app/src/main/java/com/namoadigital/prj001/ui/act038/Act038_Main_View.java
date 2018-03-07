package com.namoadigital.prj001.ui.act038;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act038_Main_View {

    void loadAP(GE_Custom_Form_Ap ap);

    void loadSSStatus(ArrayList<HMAux> statusList);

    void loadSSUsers(ArrayList<HMAux> statusList);

    void loadSSDepartment(ArrayList<HMAux> statusList);

    void showBtnSave(boolean visible);

    void showPD(String ttl, String msg);

    void showAlertDialog(String title, String msg);


}
