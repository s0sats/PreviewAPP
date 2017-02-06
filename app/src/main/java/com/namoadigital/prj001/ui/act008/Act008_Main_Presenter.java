package com.namoadigital.prj001.ui.act008;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_Presenter {

    void getProductInfo();

    void validadeSerial(String serial);

    void checkSyncChecklist(String serial);

    void updateSyncChecklist(String serial, int executeSerial);


}
