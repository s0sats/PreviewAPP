package com.namoadigital.prj001.ui.act005;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MenuMainNamoa;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act005_Main_Presenter {

    void getMenuItensV2(HMAux hmAux_Trans);

    void executeSyncProcess(int jump_validation_UR);

    void accessMenuItem(String menu_id, int jump_validation_UR);

    void showLogoutDialog();

    void executeEnableNFC();

    void executeCancelNFC();

    void executeSupport(String support_msg, String support_contact);

    void showSupportDialog();

    void executeSaveProcess();

    void executeSoSave();

    void executeSoSaveApproval();

    void executeSOPackExpress();

    void stopChatServices();

    void executeApSave();

    void executeMoveSave();

    void executeBlindMoveSave();

    void executeItemInboundSave();

    void executeItemOutboundSave();

    void executeSerialSave();

    String getProductInfo(Long product_code);

    void syncFlow(int to_send_qty);

    boolean existOthersSession();

    void clearLocalSession();

    int getChatBadgeQty();

    ArrayList<HMAux> processOutboundItemSaveReturn(String mLink, String io_item_lbl);

    ArrayList<HMAux> processInboundItemSaveReturn(String mLink, String io_item_lbl);

    int processFakeMenus(ArrayList<MenuMainNamoa> menus, int columnsQty);
}
