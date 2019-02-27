package com.namoadigital.prj001.ui.act023;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act023_Main_Presenter {

    void getProductInfo();

    void defineBackFlow();

    void onBackPressedClicked();

    void executeSerialSearch(Long product_code, String serial_id);

    void executeSerialSave();

    void executeSoDownload(Long product_code, String serial_id);

    void updateSerialData(MD_Product_Serial mdProductSerial);

    void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

    void processSoDownloadResult(HMAux so_download_result);

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

    void saveSerialInfo(MD_Product_Serial md_product_serial);

    void extractSearchResult(String result);


}
