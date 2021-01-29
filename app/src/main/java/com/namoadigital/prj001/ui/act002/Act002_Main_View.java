package com.namoadigital.prj001.ui.act002;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.SiteLicense;

import java.util.List;

/**
 * Created by neomatrix on 13/01/17.
 */

public interface Act002_Main_View {

    void loadCustomers(List<HMAux> customers);
    //Chama activity com lista de operations
    void callAct003(Context context);
    //Abri progress Dialog
    void showPD(String title,String msg,String labelCancel, String labelOk);

    void callAct001();

    void prepareExecSessionProcess(HMAux item, int forced_login, int jump_validation, int jump_od);

    void prepareExecSessionProcess(HMAux item, int forced_login, int jump_validation, int jump_od, SiteLicense selectedSiteLicense);

    void setWsProcess(String wsProcess);

    void setSelectedCustomerInfo(HMAux item);

    HMAux getSelectedCustomerInfo();
}
