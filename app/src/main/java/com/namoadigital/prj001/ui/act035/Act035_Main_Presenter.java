package com.namoadigital.prj001.ui.act035;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act035_Main_Presenter {

    void setData(String mRoom_code);

    void sendMessage(String mRoom_code, String message, String image);

    void onOnItemClicked(HMAux item);

    void onBackPressedClicked();

}
