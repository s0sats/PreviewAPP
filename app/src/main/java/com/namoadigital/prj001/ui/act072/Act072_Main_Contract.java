package com.namoadigital.prj001.ui.act072;

import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act072_Main_Contract {

    interface I_View{
        void showAlert(String ttl, String msg);

        void callAct068();

        void callAct073(Bundle bundle);
    }

    interface I_Presenter{

        void defineFlow(MD_Product_Serial productSerial);

        void onBackPressedClicked();
    }
}
