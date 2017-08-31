package com.namoadigital.prj001.ui.act033;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act033_Main_Presenter {

    void getZones();

    boolean checkPreferenceIsSet();

    void setZonePreference(HMAux zone);

    void defineForwardFlow();

    void onBackPressedClicked();

}
