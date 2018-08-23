package com.namoadigital.prj001.ui.act022;

import com.namoadigital.prj001.model.MD_Product;

public interface Act022_Main_Contract {

    interface I_View {
        void showMSG();
        void sendReturn(String status);
    }

    interface I_Presenter {
        void processValidation(String product_code_org, String serial_id_org, String product_code_inf, String serial_id_inf);

        MD_Product getMD_Produt(String product_code);

        void onBackPressedClicked();
    }
}
