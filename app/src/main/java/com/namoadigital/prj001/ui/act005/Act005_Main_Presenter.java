package com.namoadigital.prj001.ui.act005;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.MainTagMenu;
import com.namoadigital.prj001.model.MenuMainNamoa;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act005_Main_Presenter {

    List<HMAux> getMenuItensV3(@NotNull String periodFilter, @NotNull String sitesFilter, @NotNull String focusFilter);

    boolean hasSOProfile();

    boolean hasUpdateRequired();

    boolean hasTicketSyncRequired();

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

    int countInboundItemSaveReturnTotal(String mLink, String io_item_lbl);

    int countOutboundItemSaveReturnTotal(String mLink, String io_item_lbl);

    void executeTicketSave();

    ArrayList<HMAux> processTicketSaveReturn(String mLink, String ticket_lbl);

    void callWsSyncForTicketsForm();

    int processFakeMenus(ArrayList<MenuMainNamoa> menus, int columnsQty);

    @Nullable
    ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn> getTicketSaveActReturns(String jsonRet, ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn> checkinReturns);

    void processWS_SaveReturn(String wsRet);

    /**
     * Metodo que retorna se deve ser exibido ou não a opção de habilitar NFC
     * @return
     */
    boolean showEnableNfcOption();

    /**
     * Metodo que retorna se deve ser exibido ou não a opção de desabilitar NFC
     * @return
     */
    boolean showDisableNfcOption();

    /**
     * Metodo que retorna se deve ser exibido ou não a opção de changeCustomer
     * @return
     */
    boolean showChangeCustomerOption();

    /**
     * Retorna o Bitmap do logo do customer
     *  - Null se existe link pra donwload e não existe logo local
     *  - Logo da namoa, caso não tenha logo definido pro customer
     *  - Logo do customer quando houver ja baixado.
     * @return
     */
    @Nullable
    Bitmap getLogoBitmap();

    EV_User getEv_user();

    Bundle getAct083BundleParams(MainTagMenu mainTagMenu);

    void executeWSTicketDownload();

    boolean hasMasterDataSyncRequired();
}
