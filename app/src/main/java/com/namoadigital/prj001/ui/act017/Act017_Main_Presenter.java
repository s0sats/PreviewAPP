package com.namoadigital.prj001.ui.act017;

import android.text.SpannableString;

import com.namoa_digital.namoa_library.util.HMAux;

/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public interface Act017_Main_Presenter {

    void getSchedules(String selected_date, boolean filter_form, boolean filter_form_ap, boolean filter_ticket, String serial_id, boolean late, boolean filter_site_logged);

    void checkScheduleFlow(HMAux item);

    void onBackPressedClicked();

    String getDateDesc(String scheduled_date);

    void saveCheckBoxStatusIntoPreference(String checkboxConstant, boolean isChecked);

    boolean loadCheckboxStatusFromPreferencie(String checkboxConstant, boolean defaultValue);

    void extractSearchResult(String wsResult);

    void checkFormFlow(HMAux item);

    SpannableString getCommentMessage(HMAux item);

    void checkTicketFlow(HMAux item);
}
