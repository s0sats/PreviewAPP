package com.namoadigital.prj001.ui.act061;

import com.namoadigital.prj001.model.IO_Inbound;

public interface Act061_Main_Contract {

    interface I_View{

        void callAct056();
    }

    interface I_Presenter{

        IO_Inbound getInbound(int prefix,int code);

        void onBackPressedClicked();

    }
}
