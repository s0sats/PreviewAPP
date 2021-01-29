package com.namoadigital.prj001.view.frag.frg_ticket_search;

import android.content.Context;

import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Frg_Ticket_Search_Presenter implements Frg_Ticket_Search_Contract.Presenter{


    private Context context;

    public Frg_Ticket_Search_Presenter(Context context) {
        this.context = context;
    }

    @Override
    public boolean getProfileForSearchContractId() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_SEARCH_CONTRACT_ID);
    }

    @Override
    public boolean getProfileForSearchClientId() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_SEARCH_CLIENT_ID);
    }
}
