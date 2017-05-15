package com.namoadigital.prj001.ui.act008;

import android.os.Bundle;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_Presenter {

    void getProductInfo(Bundle bundle);

    void validateSerial(String serial , int required , int allow_new);

    void checkSyncChecklist(String serial, int required, int allow_new);

    void updateSyncChecklist();

    void proceedToSerialProcess(String serial, int serial_required);

    void executeSerialProcess(String serial);

    void startDownloadServices();

    void onBackPressedClicked(String scheduled_date);

    boolean checkFormXOperationExists();

    void defineFlow();

}
