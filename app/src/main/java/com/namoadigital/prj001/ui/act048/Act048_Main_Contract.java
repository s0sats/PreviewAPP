package com.namoadigital.prj001.ui.act048;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act048_Main_Contract {

    interface I_View{

        void setProductObj(MD_Product mdProduct);

        void showMsg(String title, String msg);

        void loadSerialList(ArrayList<MD_Product_Serial> serialList);

        void setRecordInfo();

        void callAct040(Context context);

        void callAct049(Context context, Bundle bundle);

    }

    interface I_Presenter{

        void getProductInfo(long product_code);

        void checkSerialListJump(ArrayList<MD_Product_Serial> serialList);

        void prepareEditionParams(MD_Product_Serial productSerial, boolean new_serial);

        void onBackPressedClicked();
    }

}
