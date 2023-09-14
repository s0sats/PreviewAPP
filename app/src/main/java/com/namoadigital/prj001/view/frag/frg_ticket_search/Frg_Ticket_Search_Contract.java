package com.namoadigital.prj001.view.frag.frg_ticket_search;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

public interface Frg_Ticket_Search_Contract {

    interface Presenter {

        boolean getProfileForSearchContractId();

        boolean getProfileForSearchClientId();

        ArrayList<HMAux> getSitesAvaibles();
    }
}
