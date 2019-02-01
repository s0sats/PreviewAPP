package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act050_Main_Contract {

    interface I_Frag_Favorite{
    }

    interface I_Presenter{
        boolean getProductSerial(long productCode, long serialCode);
        void getFavoriteList(long productCode, long serialCode, int categoryPriceCode, int segmentCode);
        void executeWsSoClient();
        void onBackPressedClicked(FragmentManager fm, MD_Product_Serial mdProductSerial);
    }

    interface I_View{
        void setProductSerial(MD_Product_Serial mdProductSerial);
        void setWsProcess(String wsProcess);
        void showPD(String title, String msg);
        void clearOSCreationData();
        void callAct005(Context context);
        void callAct023(Context context, Bundle bundle);
    }

}
