package com.namoadigital.prj001.ui.act032;

import java.util.HashMap;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act032_Main_Presenter {

    void onBackPressedAction();

    void onProcessApproval(HashMap<String, String> data);

    void onProcessSignature(HashMap<String, String> data, String sFile);

    void onProcessNFCPassWord(HashMap<String, String> data, String nfc, String password);

}
