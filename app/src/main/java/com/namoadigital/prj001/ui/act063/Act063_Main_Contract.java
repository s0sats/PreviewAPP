package com.namoadigital.prj001.ui.act063;

import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act063_Main_Contract {

    interface I_View{

        void setRecordInfo(int seriaListSize, long record_page);

        void showMsg(String title, String msg);

        void loadProductSerialList(ArrayList<MD_Product_Serial> serial_list);

        void showQtyExceededMsg(long record_count, long record_page);

        void setBtnCreateVisibility(boolean visible);

        void callAct053(Bundle bundle);

        void callAct062();
    }

    interface I_Presenter{

        MD_Product getProductInfo(String productID);

        void prepareDataToScreen(long record_count, long record_page, ArrayList<MD_Product_Serial> serial_list, boolean mJump);

        void defineFlow(MD_Product_Serial mdProductSerial, boolean serialCreation);

        void onBackPressedClicked();

        void processItemClick(MD_Product_Serial productSerial);
    }
}
