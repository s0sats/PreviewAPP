package com.namoadigital.prj001.view.frag.frg_serial_edit;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Frg_Serial_Edit_Presenter {

    /**
     * Metodo que deverá busca os dados do produto recebido pela act
     *
     * @param product_code
     */
    void getProductInfo(long product_code);

    /**
     * Metodo que deve retorna obj Produto para o metodo getProductInfo
     * @return
     */
    MD_Product getMDProduct();

    /**
     * Metodo que realizará a chamada de WS verificando se a existencia do serial
     * @param product_code
     * @param serial_id
     */
    void executeSerialSearch(long product_code, String serial_id);

    /**
     * Metodo que processará o retorno do save do serial
     * @param product_code
     * @param serial_id
     * @param hmSaveResult
     */
    void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

    /**
     * Metodo que realizará a chamada de WS verificando se a existencia do tracking
     * @param product_code
     * @param serial_code
     * @param tracking
     * @param site_code
     */
    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

    /**
     * Metodo qu atualizará os dados do serial no banco de dados.
     * @param mdProductSerial
     */
    void updateSerialData(MD_Product_Serial mdProductSerial);

    /**
     * Metodo que chamará o WS de save do serial
     */
    void executeSerialSave();

    /**
     * Metodo que processará o retorno da verificação de existencia do serial
     * @param result
     */
    void extractSearchResult(String result);

    /**
     * Metodo que realizará a busca do serial offline
     * Não é necessario para todos os fluxos
     * @param product_code
     * @param serial_id
     */
    void searchLocalSerial(long product_code, String serial_id);
}
