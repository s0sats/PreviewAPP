package com.namoadigital.prj001.ui.act007;

import com.namoadigital.prj001.model.MD_Product;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act007_Main_Presenter {

    void setAdapterData(long product_code, String filter);

    void onCategoryProductClicked(String product_code);

    List<MD_Product> getProductList();

    void onBtnHomeClicked();

    void onBackPressedClicked();

}
