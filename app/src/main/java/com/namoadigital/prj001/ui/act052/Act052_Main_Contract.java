package com.namoadigital.prj001.ui.act052;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;

public interface Act052_Main_Contract {

    interface I_Presenter{

        void onBackPressedClicked();

        void defineIOSerialFlow(HMAux hmAuxRet);

        void executeWsProcessDownload(IO_Serial_Process_Record data);
    }

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String title, String msg);

        void callAct051();
    }
}
