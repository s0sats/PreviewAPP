package com.namoadigital.prj001.ui.act062;

import android.os.Bundle;
import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

public interface Act062_Main_Contract {

    interface I_View{

        void setWs_process(String wsProcess);

        void showPD(String ttl, String msg);

        void setProduct(ArrayList<MD_Product> productList);

        void showMsg(String ttl, String msg);

        void callAct063(Bundle bundle);

        void callAct061();

        void callAct051();
    }

    interface I_Presenter{

        void executeSerialSearch(String product_id, String serial_id, String tracking);

        void getMD_Products();

        void extractSearchResult(String result);

        void onBackPressedClicked(String requestingAct);
    }
}
