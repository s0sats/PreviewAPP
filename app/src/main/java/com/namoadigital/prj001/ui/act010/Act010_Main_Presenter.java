package com.namoadigital.prj001.ui.act010;

import android.text.SpannableString;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act010_Main_Presenter {

    void setAdapterData(
            long product_code,
            int tagCode,
            Integer ticketPrefix,
            Integer ticketCode,
            Integer blockSpontaneous,
            boolean has_tk_ticket_is_form_off_hand
    );

    void validateOpenForm(HMAux item);

    void onBackPressedClicked();

    void validateGPSResource(HMAux item);

    SpannableString getTagLblText(String tag_lbl, String tag_desc);

    void callTicketCreationService(long customer_code, int type_code, String site_code, long operation_code, long product_code, long serial_code, String comments);

    void executeSerialSave();

    boolean verifyProductForForm(HMAux hmAux);

    long getSerialCode(long product_code, String serial_id);

    String getSerialSiteDescription(String site_code_form_param);
}
