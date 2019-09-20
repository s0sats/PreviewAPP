package com.namoadigital.prj001.view.frag.frg_serial_search;

public interface Frg_Serial_Search_Contract {

    interface Presenter{
        void setChkForHideSerialInfoPreference(boolean status);

        boolean getChkForForceNotShowSerialInfo();

        boolean getProfileForHideSerialInfo();

        boolean getProfileForceNotShowSerialInfo();
    }
}
