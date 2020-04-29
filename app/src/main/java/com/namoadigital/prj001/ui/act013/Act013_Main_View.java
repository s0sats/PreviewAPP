package com.namoadigital.prj001.ui.act013;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act013_Main_View {

    void loadPendencies(List<HMAux> pendencies);

    void callAct011(Context context , Bundle bundle);

    void callAct012(Context context);

    void callAct006(Context context);

    void showMsg(String type, final HMAux item);

    void alertFormNotReady();
    //17/08/2018
    void callAct008(Context context, Bundle bundle);

    void setWsProcess(String wsProcess);

    void showPD(String ttl, String msg);

    void addControlToActivity(MKEditTextNM mketSerial);

    void removeControlFromActivity(MKEditTextNM mketSerial);

    void callAct020(Context context, Bundle bundle);

    void alertActiveGPSResource(HMAux item);

    void callAct033(Context context);
}
