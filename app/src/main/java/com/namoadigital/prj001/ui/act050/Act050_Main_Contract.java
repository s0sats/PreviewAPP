package com.namoadigital.prj001.ui.act050;

import android.content.Context;

public interface Act050_Main_Contract {

    interface I_Frag_Favorite{
        void setWsProcess(String wsProcess);
        void showPD(String title, String msg);
        void showNoConnecionMsg();
    }

    interface I_Presenter{
        void getFavoriteList(Context context, long productCode, long serialCode, int categoryPriceCode, int segmentCode);
    }

}
