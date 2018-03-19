package com.namoadigital.prj001.ui.act017;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public interface Act017_Main_Presenter {

    void getSchedules(String selected_date, boolean filter_form, boolean filter_form_ap);

    void checkScheduleFlow(HMAux item);

    void prepareOpenForm(HMAux item,  boolean hasSerial);

    void onBackPressedClicked();
}
