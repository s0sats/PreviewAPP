package com.namoadigital.prj001.ui.act057;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.IO_Inbound_Search_Record;

import java.util.ArrayList;

public interface Act057_Main_Contract {

    interface I_View{

        void showAlert(String ttl,String msg);

        void setRecordInfo(int record_size);

        void showQtyExceededMsg();

        void loadInboundList(ArrayList<IO_Inbound_Search_Record> records);

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void callAct056();

        void callAct061(Bundle bundle);

        void callAct062();

        void setOnline(boolean online);

    }

    interface I_Presenter{

        void executeInboundDownload(String inboundList);

        void processListInfo(long record_count, long record_page, ArrayList<IO_Inbound_Search_Record> records);

        void onBackPressedClicked();

        void processDownloadReturn(HMAux hmAux);

        void getPendenciesList();
    }
}
