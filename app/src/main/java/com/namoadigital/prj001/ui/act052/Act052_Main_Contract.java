package com.namoadigital.prj001.ui.act052;

import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act052_Main_Contract {

    interface I_Presenter{

        void onBackPressedClicked();

        void defineIOSerialFlow(IO_Serial_Process_Record data);

        void defineFlow(MD_Product_Serial newSerialForThisProduct, boolean b);
    }

    interface I_View{

        void callAct051();

        void showPD(String progress_sync_title, String progress_sync_msg);

        void setWs_process(String ws_process);
    }
}
