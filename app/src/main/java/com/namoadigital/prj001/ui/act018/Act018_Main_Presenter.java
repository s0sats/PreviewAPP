package com.namoadigital.prj001.ui.act018;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.model.MD_Product;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public interface Act018_Main_Presenter {

    void setAdapterData();

    List<HMAux> getFcmMessageList();

    void modifyDBRead(long fcmmessage_code);

    void addFormInfoToBundle(HMAux item);

    void onBackPressedClicked();
}
