package com.namoadigital.prj001.ui.act023;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;

import java.util.ArrayList;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act023_Main_Presenter {

    void getProductInfo();

    void validadeSerialFlow(String serial , int required , int allow_new);

    boolean hasSerial(String serial);

    void defineForwardFlow(Object param);

    void defineBackFlow();

    void onBackPressedClicked();

    void executeSerialSearch(Long product_code, String serial_id);

    void executeSerialSave();

    void executeSoDownload(Long product_code, String serial_id);

    void getSerialInfo(Long product_code, String serial_id);

    void updateSerialInfo(MD_Product_Serial productSerial);

    void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

    void processSoDownloadResult(HMAux so_download_result);

    void saveSerialInfo(MD_Product_Serial md_product_serial);

    /*
    * Review S.O - Adição do tracking
    */
    void updateTrackingReference(ArrayList<MD_Product_Serial_Tracking> tracking_list);

    void processTrackingResult(HMAux auxResult, MD_Product_Serial serialObj);

    boolean isTrackingListed(String tracking);

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

}
