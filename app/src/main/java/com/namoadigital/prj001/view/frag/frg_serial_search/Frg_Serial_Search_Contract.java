package com.namoadigital.prj001.view.frag.frg_serial_search;

import com.namoa_digital.namoa_library.util.HMAux;

public interface Frg_Serial_Search_Contract {

    interface Presenter{
        void setChkForHideSerialInfoPreference(boolean status);

        boolean getChkForForceNotShowSerialInfo();

        boolean getProfileForHideSerialInfo();

        boolean getProfileForceNotShowSerialInfo();

        String getFormattedRuleHelper(HMAux hmAux_Trans, String serial_rule, Integer min, Integer max);
    }
}
