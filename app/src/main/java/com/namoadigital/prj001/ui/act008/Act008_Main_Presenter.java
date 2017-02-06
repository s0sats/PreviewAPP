package com.namoadigital.prj001.ui.act008;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act008_Main_Presenter {

    void getProductInfo();

    void validateSerial(String serial , int required , int allow_new);

    void checkSyncChecklist(String serial, int required);

    void updateSyncChecklist(String serial, int executeSerial);


}
