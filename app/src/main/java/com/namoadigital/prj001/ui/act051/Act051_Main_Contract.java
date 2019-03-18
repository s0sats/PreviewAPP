package com.namoadigital.prj001.ui.act051;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act051_Main_Contract {

    interface I_Frag_Favorite{
    }

    interface I_Presenter{
        void getMD_Products();

        void executeSerialSearch(String product_id, String serial_id, String tracking);

        MD_Product searchProduct(String product_id);

        void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page);
    }

    interface I_View{

        void setProduto(ArrayList<MD_Product> productList);

        void showPD(String title, String msg);

        void showMsg(String title, String msg);

        void callAct020(Context context, Bundle bundle);
    }
}
