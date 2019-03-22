package com.namoadigital.prj001.view.frag.frg_serial_edit;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Frg_Serial_Edit_View {

    /**
     * Metodo para exibição de apenas um serial
     * @param ttl
     * @param msg
     */
    void showSingleResultMsg(String ttl, String msg);

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
}
