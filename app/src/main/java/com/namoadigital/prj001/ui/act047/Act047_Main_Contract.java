package com.namoadigital.prj001.ui.act047;

import android.content.Context;

import com.namoadigital.prj001.model.SO_Next_Orders_Obj;

import java.util.ArrayList;

public interface Act047_Main_Contract {

    interface I_View {

        void loadNextOrders(ArrayList<SO_Next_Orders_Obj> nextOrdersObjs);

        void setWsProcess(String wsProcess);

        void showPD(String title, String msg);

        void showNoConnecionMsg();

        void showEmptyLogMsg();

        void callAct021(Context context);

        void callAct005(Context context);
    }

    interface I_Presenter {

        void executeNextOrdersSearch();

        void processNextOrderList(String nextOrderList);

        void onBackPressedClicked();

    }

}
