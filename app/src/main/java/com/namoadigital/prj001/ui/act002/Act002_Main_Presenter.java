package com.namoadigital.prj001.ui.act002;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 13/01/17.
 */

public interface Act002_Main_Presenter {

    void getAllCustomers(boolean offline_update);

    void executeSessionProcess(String email, String password, String nfc, HMAux customer,int forced_login, int jump_validation, int jump_od);

    void executeSyncProcess();

    void executeGetCustomerProcess();
    /**
     * Metodo que verifica se as preferencias dessa activity ja estão setadas
     * @return
     */
    boolean checkPreferenceIsSet();

}
