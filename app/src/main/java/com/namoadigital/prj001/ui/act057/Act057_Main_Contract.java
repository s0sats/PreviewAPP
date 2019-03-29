package com.namoadigital.prj001.ui.act057;

import com.namoadigital.prj001.model.IO_Inbound_Search_Record;

import java.util.ArrayList;

public interface Act057_Main_Contract {

    interface I_View{

        //void showAlert(String ttl,String msg);

        void setRecordInfo();

        void showQtyExceededMsg();

        void loadInboundList();

        void callAct056();
    }

    interface I_Presenter{

        void processListInfo(long record_count, long record_page, ArrayList<IO_Inbound_Search_Record> records);

        void onBackPressedClicked();
    }
}
