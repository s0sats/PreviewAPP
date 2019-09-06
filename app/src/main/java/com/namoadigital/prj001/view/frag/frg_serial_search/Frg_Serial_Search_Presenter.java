package com.namoadigital.prj001.view.frag.frg_serial_search;

import android.content.Context;

import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Frg_Serial_Search_Presenter implements Frg_Serial_Search_Contract.Presenter {

    private Context context;

    public Frg_Serial_Search_Presenter(Context context) {
        this.context = context;
    }

    @Override
    public void setChkForHideSerialInfoPreference(boolean status) {
        ToolBox_Con.setBooleanPreference(context, Frg_Serial_Search.HIDE_SERIAL_INFO, status);
    }

    @Override
    public boolean getChkForHideSerialInfoPreference() {
        return ToolBox_Con.getBooleanPreferencesByKey(context, Frg_Serial_Search.HIDE_SERIAL_INFO, false);
    }

    @Override
    public boolean getProfileForHideSerialInfo() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL, Frg_Serial_Search.HIDE_SERIAL_INFO);
    }
}
