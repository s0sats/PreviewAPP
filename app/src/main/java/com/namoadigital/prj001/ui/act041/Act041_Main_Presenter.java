package com.namoadigital.prj001.ui.act041;

import com.namoadigital.prj001.model.MD_Product;

import java.util.List;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act041_Main_Presenter {

    void setAdapterData(long group_code, Long recursive_code, String filter);

    void onCategoryProductClicked(String product_code);

    List<MD_Product> getProductList();

    void onBtnHomeClicked();

    void onBackPressedClicked();

    void setRequesting_process(String requesting_process);
}
