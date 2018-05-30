package com.namoadigital.prj001.ui.act045;

import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act045_Main_Contract {

    interface I_View {

        void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list);

        void setRecordInfo(long record_size, long record_page);
    }

    interface I_Presenter {

        void getProductSerialList(String ws_result);

        void onBackPressedClicked();

    }

}
