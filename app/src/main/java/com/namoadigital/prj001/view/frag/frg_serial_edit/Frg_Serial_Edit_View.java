package com.namoadigital.prj001.view.frag.frg_serial_edit;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Frg_Serial_Edit_View {

    /**
     * Metodo para exibição de apenas um serial
     * @param ttl
     * @param msg
     * @param saveOk
     */
    void showSingleResultMsg(String ttl, String msg, boolean saveOk);

    /**
     * Metodo para exibição de lista de seriais retornados
     * @param returnList
     */
    void showSerialResults(ArrayList<HMAux> returnList);

    //void setProductValues

    /**
     * Metodo que dispara o refresh da UI do fragment
     */
    void refreshUI();

    /**
     * Metodo chamado quando o serial retornado não exsite.
     */
    void reApplySerialIdToFrag();

    /**
     * Metodo chamado quando o retorno da verificação identifica que o serial ja existe
     * @param serial_returned
     */
    void applyReceivedSerialToFrag(MD_Product_Serial serial_returned);

    /**
     * MEtodo que atualizará no Fragment os dados do serial
     * @param mdProductSerial
     */
    void updateProductSerialValues(MD_Product_Serial mdProductSerial);

    /**
     * MEtodo que seta os dados do serial no Fragmento
     * @param mdProductSerial
     */
    void setMdProductSerial(MD_Product_Serial mdProductSerial);

    /**
     * Metodo que informa dados do endereço sugerido após processamento do ws de suggestão.
     * @param zone_code
     * @param zone_id
     * @param zone_desc
     * @param local_code
     * @param local_id
     */
    void reportAddressSuggestion(Integer zone_code, String zone_id, String zone_desc, Integer local_code, String local_id);
}
