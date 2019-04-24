package com.namoadigital.prj001.ui.act059;

import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act059_Main_Contract {

    interface I_View{

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void callAct054();

        void setWs_process(String name);

        void callAct051();
    }

    interface I_Presenter{
        IO_Inbound_Item getInboudItem(Integer inbound_prefix, Integer inbound_code, Integer inbound_item);

        MD_Product_Serial getSerialInfo(long product_code, int serial_code);

        int getViewMode(String move_type, int has_put_away);

        void onBackPressed(String actRequest);
    }
}
