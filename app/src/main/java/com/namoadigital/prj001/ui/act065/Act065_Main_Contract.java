package com.namoadigital.prj001.ui.act065;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;

public interface Act065_Main_Contract {

    interface I_View{

        void showAlert(String ttl, String msg);
    }

    interface I_Presenter{

        String getOutboundPendencies();

        void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val);

        void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val);

        boolean checkSearchParamFilled(SearchableSpinner ss_zone, SearchableSpinner ss_local, MKEditTextNM mket_outbound, MKEditTextNM mket_invoice);

        void checkSearchFlow();
    }
}
