package com.namoadigital.prj001.ui.act045;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act045_Main_Contract {

    interface I_View {

        void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list);

        void setRecordInfo(long record_size, long record_page);

        void callAct031(Context context, Bundle bundle);
    }

    interface I_Presenter {

        void getProductSerialList(String ws_result);

        void defineFlow(MD_Product_Serial productSerial, boolean new_serial);

        void onBackPressedClicked();

    }

}
