package com.namoadigital.prj001.ui.act058;

import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act058_Main_Contract {

    interface I_Frag_Move{

    }

    interface I_Presenter{

        IO_Move getMoveInfo(int movePrefix, int moveCode);

        void getSerialHistoric();

        MD_Product_Serial getSerialInfo(long product_code, int serial_code);
    }

    interface I_View{
        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void callAct055();
    }
}
