package com.namoadigital.prj001.view.frag.frg_ticket_search;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

public interface I_Frg_Ticket_Search {
    void onSearchClick(String btn_Action, HMAux optionsInfo);
    void onControlStaListReady(ArrayList<MKEditTextNM> control_sta_list);
}
