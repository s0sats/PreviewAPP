package com.namoadigital.prj001.ui.act002;

/**
 * Created by neomatrix on 13/01/17.
 */

public interface Act002_Main_Presenter {

    void getAllCustomers();

    void executeSyncProcess(String email, String password, String nfc, long customer_code, int status);

}
