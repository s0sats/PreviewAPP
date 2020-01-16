package com.namoadigital.prj001.ui.act047;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
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

        void showAlert(String ttl, String msg);

        void showAlert(String ttl, String msg,@Nullable DialogInterface.OnClickListener listener);

        void callAct027(Bundle bundle);

        void cleanWsTmpItem();

    }

    interface I_Presenter {

        void executeNextOrdersSearch();

        void processNextOrderList(String nextOrderList);

        void onBackPressedClicked();

        void executeSoDownload(String soPrefix, String soCode);

        void processSoDownloadResult(HMAux hmAux, String so_prefix, String so_code);

        void executeSerialDownload(String productId, String serialCode);

        void extractSearchResult(String mLink, SO_Next_Orders_Obj wsTmpItem);

        boolean checkSoExits(String soPrefix, String soCode);

        Bundle getAct027Bundle(String soPrefix, String soCode);
    }

}
