package com.namoadigital.prj001.ui.act052;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act052_Main_Contract {

    interface I_Presenter{

        void onBackPressedClicked();

        void defineIOSerialFlow(HMAux hmAuxRet);

        void executeWsProcessDownload(IO_Serial_Process_Record data);

        MD_Product getMd_product(String mProduct_id);

        void createNewSerialFlow(MD_Product_Serial productSerial);

        boolean isSiteInboundAutoCreation();
    }

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String title, String msg);

        void callAct051();

        void callAct053(Bundle bundle);

        void callAct058(Bundle bundle);

        void callAct059(Bundle bundle);
    }
}
