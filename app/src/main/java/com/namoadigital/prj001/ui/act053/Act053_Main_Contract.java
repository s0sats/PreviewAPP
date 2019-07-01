package com.namoadigital.prj001.ui.act053;

import android.os.Bundle;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit_Presenter;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit_View;

import java.util.ArrayList;

public interface Act053_Main_Contract {

    interface I_Presenter extends Frg_Serial_Edit_Presenter {

        void onBackPressedClicked(String requesting_act);

        void defineFlow(String requesting_act);

        void checkFlow();

        void defineWsRetFlow(String ioProcess, String requesting_act);

        void processInboundItemAdd(String wsJsonReturn);

        void processOutboundItemAdd(String wsJsonReturn);
    }

    interface I_View extends Frg_Serial_Edit_View {

        void setWsProcess(String wsProcess);

        void callAct051();

        void setProductValues(MD_Product md_product);

        void showAlertDialog(String title, String msg);

        void showPD(String title, String msg);

        String getIoProcess();

        void callAct061(Bundle bundle);

        MD_Product_Serial getProductSerial();

        String getIoPrefix();

        String getIoCode();

        void showResultDialog(ArrayList<HMAux> resultList, boolean itemAdd);

        boolean isItemSavedOk();

        void setItemSavedOk(boolean itemSavedOk);

        void callAct067(Bundle bundle);
    }
}
