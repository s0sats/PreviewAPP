package com.namoadigital.prj001.ui.act081;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

public interface Act081_Main_Contract {
    interface I_View {
        void callAct070();

        void setWsProcess(String wsProcess);

        void showPD(String progress_ttl, String progress_start);

        void showMsg(String show_ttl, String show_msg);

        void setProduct(ArrayList<MD_Product> productList);

        void callAct020(Context context, Bundle bundle);
    }

    interface I_Presenter {

        void executeSerialSearch(String product_id, String serial_id, String tracking, boolean forceExactSearch);

        void onBackPressedClicked(String mainRequestingAct);

        void extractSearchResult(String result);

        void getMD_Products();

        void offlineSerialSearch();


    }
}
