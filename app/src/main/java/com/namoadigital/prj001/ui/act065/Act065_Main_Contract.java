package com.namoadigital.prj001.ui.act065;

import android.os.Bundle;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;

public interface Act065_Main_Contract {

    interface I_View{

        void showAlert(String ttl, String msg);

        void setWsProcess(String wsProcess);

        void callSearchOutbound();

        void showPD(String dialog_outbound_search_ttl, String dialog_outbound_search_start);

        void callAct051();

        void callAct066(Bundle bundle);
    }

    interface I_Presenter{

        String getOutboundPendencies();

        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val);

        boolean checkSearchParamFilled(SearchableSpinner ss_zone, SearchableSpinner ss_local, MKEditTextNM mket_outbound, MKEditTextNM mket_invoice);

        void checkSearchFlow();

        void executeOutboundSearch(String zone_id, String local_id, String outbound_id, String invoice_code);

        void onBackPressedClicked();

        void processSearchReturn(String mLink);
    }
}
