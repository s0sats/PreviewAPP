package com.namoadigital.prj001.ui.act059;

import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.MD_Product_Serial;

public interface Act059_Main_Contract {

    interface I_View{

    }

    interface I_Presenter{
        IO_Inbound_Item getInboudItem(Integer inbound_prefix, Integer inbound_code, Integer inbound_item);

        MD_Product_Serial getSerialInfo(long product_code, int serial_code);

        public int getViewMode(String move_type);

        void onBackPressed(String actRequest);
    }
}
