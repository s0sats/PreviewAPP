package com.namoadigital.prj001.view.act.product_selection;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.model.MD_Product;

import java.util.List;

public interface Act_Product_Selection_Contract {


    interface I_View {

        void loadGroups_Products(List<HMAux> groups_products);

        void sendResult(MD_Product md_product);
    }

    interface I_Presenter {
        void setAdapterData(long group_code, Long recursive_code, String filter);

        MD_Product getProduct(String customer_code, String product_code);

        void onBtnHomeClicked();

        void setAdapterDataForProductInsert(long group_code, Long recursive_code, String filter);

        MD_All_Product getProductFromAll(String customer_code, String product_code);
    }

}
