package com.namoadigital.prj001.ui.act053;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit_Presenter;
import com.namoadigital.prj001.view.frag.frg_serial_edit.Frg_Serial_Edit_View;

public interface Act053_Main_Contract {

    interface I_Presenter extends Frg_Serial_Edit_Presenter {

        void onBackPressedClicked(String requesting_act);

        void defineFlow(String requesting_act);

        void checkFlow();
    }

    interface I_View extends Frg_Serial_Edit_View {

        void setWsProcess(String wsProcess);

        void callAct051();

        void setProductValues(MD_Product md_product);

        void showAlertDialog(String title, String msg);

        void showPD(String title, String msg);

    }
}
