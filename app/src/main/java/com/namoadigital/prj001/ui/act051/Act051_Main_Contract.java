package com.namoadigital.prj001.ui.act051;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

public interface Act051_Main_Contract {

    interface I_Frag_Favorite{
    }

    interface I_Presenter{
        void getMD_Products();

        void executeSerialProcessSearch(String product_id, String serial_id, String tracking);

        MD_Product searchProduct(String product_id);

        void defineSearchResultFlow(ArrayList<IO_Serial_Process_Record> serial_list, long record_count, long record_page, boolean isOnline);

        void processSearchResult(String result);

        void onBackPressedClicked();

        void syncMovements();

        void syncBlindItem();

        void syncOutobundItem();

        void syncInboundItem();

        boolean hasWaitingSyncMovePendency();

        boolean hasWaitingSyncBlindPendency();

        boolean hasWaitingSyncPickingPendency();

        boolean hasWaitingSyncPutAwayPendency();

        void processIOItemSaveReturn(String jsonRet, String itemLabel);
    }

    interface I_View{

        void setProduto(ArrayList<MD_Product> productList);

        void showPD(String title, String msg);

        void showMsg(String title, String msg);

        void callAct052(Context context, Bundle bundle);

        void setWsProcess(String process);

        void callAct005(Context context);

        void showResult(ArrayList<HMAux> resultList);

        void handleNoConnection();

    }
}
