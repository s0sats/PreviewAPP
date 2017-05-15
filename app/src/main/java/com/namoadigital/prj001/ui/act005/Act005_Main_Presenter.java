package com.namoadigital.prj001.ui.act005;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act005_Main_Presenter {

    void getMenuItens(HMAux hmAux_Trans);

    void executeSyncProcess(int jump_validation_UR);

    void accessMenuItem(String menu_id, int jump_validation_UR);

    void showLogoutDialog();

    void executeEnableNFC();

    void executeCancelNFC();

    void executeSupport(String support_msg);

    void showSupportDialog();
}
