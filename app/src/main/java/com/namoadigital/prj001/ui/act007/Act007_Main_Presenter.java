package com.namoadigital.prj001.ui.act007;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act007_Main_Presenter {

    void setAdapterData(long product_code, String filter);

    void onCategoryProductClicked(String product_code);

    void onBtnHomeClicked();

    void onBackPressedClicked();

}
