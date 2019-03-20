package com.namoadigital.prj001.ui.act052;

import com.namoadigital.prj001.model.IO_Serial_Process_Record;

public interface Act052_Main_Contract {

    interface I_Presenter{

        void onBackPressedClicked();

        void defineIOSerialFlow(IO_Serial_Process_Record data);
    }

    interface I_View{

        void callAct051();
    }
}
