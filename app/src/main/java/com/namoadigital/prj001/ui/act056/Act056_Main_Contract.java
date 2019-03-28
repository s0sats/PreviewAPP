package com.namoadigital.prj001.ui.act056;

import android.os.Bundle;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;

public interface Act056_Main_Contract {

    interface I_View{

        void setWsProcess(String wsProcess);

        void showPD(String ttl, String msg);

        void showAlert(String ttl, String msg);

        void callAct051();

        void callAct057(Bundle bundle);
    }

    interface I_Presenter{
        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone,SearchableSpinner ss_local, boolean reset_val);

        void setDefaultZone(SearchableSpinner ss_zone);

        String getInboundPendencies();

        void executeInboundSearch(String zone_code, String local_code,String inbound_id, String invoince);

        void onBackPressedClicked();

        boolean checkSearchParamFilled(SearchableSpinner ss_zone, SearchableSpinner ss_local, MKEditTextNM mket_inbound, MKEditTextNM mket_invoice);

        void processSearchReturn(String searchRet);
    }
}
