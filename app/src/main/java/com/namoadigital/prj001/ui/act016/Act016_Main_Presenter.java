package com.namoadigital.prj001.ui.act016;

import java.util.Date;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public interface Act016_Main_Presenter {

    void getSchedule(boolean filter_form, boolean filter_form_ap, boolean filter_site);

    void formatDate(Date date);

    void saveCheckBoxStatusIntoPreference(String checkboxConstant, boolean isChecked);

    boolean loadCheckboxStatusFromPreferencie(String checkboxConstant, boolean defaultValue);

    void onBackPressedClicked();

}
