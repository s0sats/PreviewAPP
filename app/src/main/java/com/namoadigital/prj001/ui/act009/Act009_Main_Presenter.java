package com.namoadigital.prj001.ui.act009;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act009_Main_Presenter {

    void setAdapterData(long product_code, String serial_id, Integer blockSpontaneous);

    void onBackPressedClicked(String actResqueting);
}
