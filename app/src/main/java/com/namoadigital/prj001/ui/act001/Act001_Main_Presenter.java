package com.namoadigital.prj001.ui.act001;

/**
 * Created by neomatrix on 29/12/16.
 */

public interface Act001_Main_Presenter {

    void executeLoginProcess(String email, String password, String nfc, int status_jump);

    void validateLogin(String login, String password, String nfc_code);

    void checkLogin();


}
