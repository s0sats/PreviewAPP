package com.namoadigital.prj001.ui.act046;

import android.content.Context;

public interface Act046_Main_Contract {

    interface I_View {

        void callAct005(Context context);

        void callAct016(Context context);

        void callAct017(Context context, String serial_id, boolean late);

        void callAct068();
    }

    interface I_Presenter {

        int getTotalDelay(boolean filter_form, boolean filter_form_ap);

        void saveCheckBoxStatusIntoPreference(String checkboxConstant, boolean isChecked);

        boolean loadCheckboxStatusFromPreferencie(String checkboxConstant, boolean defaultValue);

        void onBackPressedClicked(String requestingAct);
    }

}
