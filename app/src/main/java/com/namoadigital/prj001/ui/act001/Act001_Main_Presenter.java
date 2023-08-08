package com.namoadigital.prj001.ui.act001;

import com.namoadigital.prj001.core.data.domain.model.EnvironmentType;

/**
 * Created by neomatrix on 29/12/16.
 */

public interface Act001_Main_Presenter {

    void executeLoginProcess(String email, String password, String nfc, int status_jump);

    void executeLoginProcess(String email, String password, String nfc, int status_jump, boolean userValidadtion);

    void validateLogin(String login, String password, String nfc_code);

    void checkLogin();

    EnvironmentType getEnvironmentDevelopment();

    //checar se o antigo app esta instalado no device.
    boolean isPackageInstalled();

    void checkEnvironmentDevelopment();

    void setEnvironmentSelected(String environmentSelected);

    void processEnvironmentSelected(String environmentSelected);

}
