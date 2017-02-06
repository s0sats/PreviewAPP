package com.namoadigital.prj001.ui.act010;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act010_Main_Presenter {

    void setAdapterData(long product_code, String filter);

    void onFormClicked(String product_code);

    void onBackPressedClicked();


}
