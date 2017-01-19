package com.namoadigital.prj001.ui.act002;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 13/01/17.
 */

public interface Act002_Main_Presenter {

    void getAllCustomers();

    void getToken(HMAux item);

    void executeSessionProcess(String email, String password, String nfc, long customer_code, int status);

}
