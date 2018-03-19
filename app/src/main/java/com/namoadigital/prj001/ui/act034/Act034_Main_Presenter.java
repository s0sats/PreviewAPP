package com.namoadigital.prj001.ui.act034;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public interface Act034_Main_Presenter {

    ArrayList<HMAux> getCustomerNameList();

    ArrayList<HMAux> getCustomerMessageList(ArrayList<HMAux> customerNameList);

    void tryToRestartChatService();

    void onBackPressedClicked();
}
