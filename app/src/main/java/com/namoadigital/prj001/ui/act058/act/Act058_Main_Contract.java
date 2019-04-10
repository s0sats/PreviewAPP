package com.namoadigital.prj001.ui.act058.act;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act058_Main_Contract {

    interface I_Frag_Move{

    }

    interface I_Presenter{

        IO_Move getMoveInfo(int movePrefix, int moveCode);

        ArrayList<HMAux>  getClassList();

        MD_Product_Serial getSerialInfo(long product_code, int serial_code);

        ArrayList<HMAux> getMoveReasonList();

        void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

        int getViewMode(IO_Move moveInfo);

        void executeMovePersistence(long customer_code, int move_prefix, int move_code, Integer to_zone_code, Integer to_local_code, Integer to_class_code, Integer reason_code, String done_date, MD_Product_Serial serial);
    }

    interface I_View{

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void callAct054();

        void setWs_process(String name);
    }
}
