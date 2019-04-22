package com.namoadigital.prj001.ui.act058.act;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;
import java.util.List;

public interface Act058_Main_Contract {

    interface I_Frag_Move{

    }

    interface I_Presenter{

        IO_Move getMoveInfo(int movePrefix, int moveCode);

        MD_Product_Serial getSerialInfo(long product_code, int serial_code);

        void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

        int getViewMode(String move_type);

        void executeMovePlannedPersistence(long customer_code, int move_prefix, int move_code, Integer to_zone_code, Integer to_local_code, Integer to_class_code, Integer reason_code, String done_date, MD_Product_Serial serial, IO_Move io_move, List<IO_Move_Tracking> trackingFromMove);

        void onBackPressed(String actRequest);

        IO_Blind_Move getMoveInfo(int blind_tmp, long product_code, String serial_id);

        void executeMovePersistence(long customer_code, int blind_tmp, Integer zone_code, Integer local_code, Integer classCode, Integer reasonCode, String date_confirm, MD_Product_Serial serial, IO_Move movePlanned, List<IO_Move_Tracking> trackingFromMove);

        int getBlindTmp();
    }

    interface I_View{

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void callAct054();

        void setWs_process(String name);

        void callAct051();
    }
}
