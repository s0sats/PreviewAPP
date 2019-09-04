package com.namoadigital.prj001.ui.act060;

import com.namoadigital.prj001.model.IO_Conf_Tracking;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.IO_Outbound_Item;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.List;

public interface Act060_Main_Contract {

    interface I_View{
        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void setWs_process(String name);

        void callAct051();

        void callAct067();
    }

    interface I_Presenter{
        IO_Outbound_Item getOutboudItem(Integer outbound_prefix, Integer outbound_code, Integer outbound_item);

        MD_Product_Serial getSerialInfo(long product_code, int serial_code);

        int getViewMode(String move_type, int has_put_away);

        void onBackPressed(String actRequest);

        void executeTrackingSearch(long product_code, long serial_code, String tracking, String site_code);

        void executeOutConfPersistence(long customer_code, Integer io_prefix, Integer io_code, Integer to_zone_code, String to_zone_id, String to_zone_desc, Integer to_local_code, String to_local_id, String to_local_desc, Integer to_class_code, String classId, Integer reason_code, String comments, String done_date, MD_Product_Serial serial, IO_Outbound_Item item, List<IO_Conf_Tracking> trackingFromMove);

        IO_Outbound getOutbound(Integer io_prefix, Integer io_code);
    }
}
