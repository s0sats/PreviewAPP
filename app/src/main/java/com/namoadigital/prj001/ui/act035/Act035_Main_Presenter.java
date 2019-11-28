package com.namoadigital.prj001.ui.act035;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act035_Main_Presenter {

    void setData(String mRoom_code, String offSet);

    void sendMessage(String mRoom_code, String message, String image, String offSet);

    void sendRead(ArrayList<HMAux> hmAuxs);

    void onOnItemClicked(HMAux item);

    void onBackPressedClicked();

    void updateReadStatus(ArrayList<HMAux> hmAuxs);

    void updateReadStatus(ArrayList<HMAux> hmAuxs, String type);

    void sendHistoricalScrollUp(String mRoom_code, String msg_prefix, String msg_code);

    void checkFormApFlow(HMAux hmAux);

    //region Ticket
    void validateTicketDownload(String pk, String site_code, String operation_code, String product_code);

    boolean checkTicketMdProfile(String s_customer_code, String s_site_code, String s_operation_code, String s_product_code);

    String[] getSplitedPk(String pk, String splitter);

    boolean validateTicketPk(String ticketPk);

    String getTicketProductDesc(String s_customer_code, String s_product_code);
    //endregion
}
