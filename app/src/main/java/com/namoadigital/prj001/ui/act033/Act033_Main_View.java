package com.namoadigital.prj001.ui.act033;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act033_Main_View {

    void loadZones(List<HMAux> zones);

    void showPD(String title, String msg);

    void showNoZoneMsg();

    void callAct003(Context context,Bundle bundle);

    void callAct004(Context context);

    void callAct005(Context context);

    void callAct017(Context context);
}
