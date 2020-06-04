package com.namoadigital.prj001.ui.act035;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.Chat_Room_Obj_SO;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act035_Main_Presenter {

    void setData(String mRoom_code, String offSet);

    void sendMessage(String mRoom_code, String message, String image, String offSet);

    void sendRead(ArrayList<HMAux> hmAuxs);

    void onOnItemClicked(HMAux item);

    void executeSerialDownload(String productId, String serialId);

    void extractSearchResult(String result, Chat_Room_Obj_SO roomObjSo);

    void executeSoDownload(String soPrefix, String soCode);

    void processSoDownloadResult(HMAux soDownloadResult, String soPrefix, String soCode);

    void updateReadStatus(ArrayList<HMAux> hmAuxs);

    void updateReadStatus(ArrayList<HMAux> hmAuxs, String type);

    void sendHistoricalScrollUp(String mRoom_code, String msg_prefix, String msg_code);

    void checkFormApFlow(HMAux hmAux);

    //region Ticket
    void validateTicketDownload(String pk, String site_code, String operation_code, String product_code);

    boolean checkTicketMdProfile(String s_site_code, String s_operation_code, String s_product_code);

    String[] getSplitedPk(String pk, String splitter);

    boolean validateTicketPk(String ticketPk);

    void onBackPressedClicked(String act_request);
    //endregion
}
