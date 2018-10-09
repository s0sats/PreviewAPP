package com.namoadigital.prj001.ui.act049;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act049_Main_Contract {

    interface I_View{

        void showPD(String title , String msg);

        void showAlertDialog(String title, String msg);

        void setWs_process(String ws_process);

        void setProductValues(MD_Product md_product);

        void callAct040(Context context);

        void showSingleResultMsg(String ttl, String msg, boolean backToExpressOS);

        void showSerialResults(ArrayList<HMAux> returnList);

        void refreshUI();

        void reApplySerialIdToFrag();

        void applyReceivedSerialToFrag(MD_Product_Serial serial_returned);

        void updateProductSerialValues(MD_Product_Serial mdProductSerial);

    }

    interface I_Presenter{

        void getProductInfo();

        //boolean hasSerial(String serial);

        void onBackPressedClicked();

        void executeSerialSearch(Long product_code, String product_id, String serial_id);

        void executeSerialSave();

        void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

        void updateSerialData(MD_Product_Serial productSerial);

        void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult);

        void searchLocalSerial(long product_code, String serial_id);

        void extractSearchResult(String result);

    }

}
