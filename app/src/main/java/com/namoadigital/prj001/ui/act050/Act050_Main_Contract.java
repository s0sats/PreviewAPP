package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SO_Creation_Obj;

public interface Act050_Main_Contract {

    interface I_Frag_Favorite{
    }

    interface I_Presenter{
        boolean getProductSerial(long productCode, long serialCode);
        void getFavoriteList(long productCode, long serialCode, int categoryPriceCode, int segmentCode, MD_Product_Serial mdProductSerial);
        void executeWsSoClient();
        void executeWsSoCreation(SO_Creation_Obj mSoCreation);
        void processSoCreationRet(HMAux hmAuxRet);
        void onBackPressedClicked(FragmentManager fm, MD_Product_Serial mdProductSerial, boolean isEmptyList);
    }

    interface I_View{
        void setProductSerial(MD_Product_Serial mdProductSerial);
        void setWsProcess(String wsProcess);
        void showPD(String title, String msg);
        void clearOSCreationData();
        void callAct005(Context context);
        void callAct026(Context context, Bundle bundle);
        void callAct027(Context context, Bundle bundle);
    }

}
