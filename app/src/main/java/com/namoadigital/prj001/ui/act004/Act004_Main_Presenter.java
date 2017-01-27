package com.namoadigital.prj001.ui.act004;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act004_Main_Presenter {

    void getOperations();

    void setOperationCode(HMAux item);

    /**
     * Metodo que verifica se as preferencias dessa activity ja estão setadas
     * @return
     */
    boolean checkPreferenceIsSet();


}
