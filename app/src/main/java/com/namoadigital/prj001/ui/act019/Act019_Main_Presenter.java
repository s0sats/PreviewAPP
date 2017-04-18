package com.namoadigital.prj001.ui.act019;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public interface Act019_Main_Presenter {

    void setAdapterData();

    List<HMAux> getFcmMessageList();

    void addFormInfoToBundle(HMAux item);

    void onBackPressedClicked();
}
