package com.namoadigital.prj001.view.frag.frg_ticket_search;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Frg_Ticket_Search_Presenter implements Frg_Ticket_Search_Contract.Presenter {


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

    @Override
    public ArrayList<HMAux> getSitesAvaibles() {
        ArrayList<HMAux> siteList = new ArrayList<>();
        List<MD_Site> allSite = new MD_SiteDao(context).getAllSite(true);

        for (MD_Site mdSite : allSite) {
            HMAux hmSite = new HMAux();
            hmSite.put(SearchableSpinner.ID, mdSite.getSite_id());
            hmSite.put(SearchableSpinner.CODE, mdSite.getSite_code());
            hmSite.put(SearchableSpinner.DESCRIPTION, mdSite.getSite_desc());
            siteList.add(hmSite);
        }

        return siteList;
    }
}
