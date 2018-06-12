package com.namoadigital.prj001.ui.act008;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_Presenter {

    void getProductInfo(Bundle bundle);

    void validateSerial(String serial , int required , int allow_new);

    void checkSyncChecklist(String serial, int required, int allow_new);

    void updateSyncChecklist();

    void proceedToSerialProcess(String serial, int serial_required);

    //void executeSerialProcess(String serial);

    void startDownloadServices();

    void onBackPressedClicked();

    void defineFlow();

    void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

    void executeSerialSearch(String product_id, String serial_id);

    void executeSerialSave();

    void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

    void updateSerialData(MD_Product_Serial mdProductSerial);

    void checkFlow();

    void extractSearchResult(String result);

    void searchLocalSerial(long product_code, String serial_id);

}
