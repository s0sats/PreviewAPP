package com.namoadigital.prj001.ui.act003;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 17/01/17.
 */

public interface Act003_Main_Presenter {

    void getSites(HMAux hmAux_Trans);

    void setSiteCode(HMAux item);

    /**
     * Metodo que verifica se as preferencias dessa activity ja estão setadas
     * @return
     */
    boolean checkPreferenceIsSet();

    void startChatService();

    void onBackPressedClicked();

}
