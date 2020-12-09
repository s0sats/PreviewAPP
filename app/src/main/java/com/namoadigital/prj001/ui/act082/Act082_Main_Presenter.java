package com.namoadigital.prj001.ui.act082;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Main_User_Rec;
import com.namoadigital.prj001.receiver.WBR_TK_Main_User_List;
import com.namoadigital.prj001.service.WS_TK_Main_User_List;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act082_Main_Presenter implements Act082_Main_Contract.I_Presenter {
    Context context;
    Act082_Main_Contract.I_View mView;
    HMAux hmAux_trans;
    private TK_TicketDao ticketDao;

    public Act082_Main_Presenter(Context context, Act082_Main_Contract.I_View mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_trans = hmAux_trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public boolean getDateEditionProfile() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_EDIT_FORECAST);
    }

    @Override
    public boolean getHeaderEditionProfile() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_EDIT_HEADER);
    }

    @Override
    public boolean getStepEditTimeProfile() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET, ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_STEP_EDIT_TIME);
    }

    @Override
    public void onBackPressedClicked(String mainRequestingAct) {

    }

    @Override
    public TK_Ticket getTicketData(int ticketPrefix, int ticketCode) {
        TK_Ticket ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticketPrefix,
                        ticketCode
                ).toSqlQuery()
        );
        //
        return ticket;
    }

    @Override
    public void callMainUserService(TK_Ticket ticket) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Main_User_List.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_main_user_search_ttl"),
                    hmAux_trans.get("dialog_main_user_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Main_User_List.class);
            Bundle bundle = new Bundle();
            //
            int edit_header = getHeaderEditionProfile()? 1 : 0;
            //
            bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket.getTicket_prefix());
            bundle.putInt(TK_TicketDao.TICKET_CODE, ticket.getTicket_code());
            bundle.putInt(TK_TicketDao.SCN, ticket.getScn());
            bundle.putInt(WS_TK_Main_User_List.EDIT_HEADER, edit_header);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        } else {
            mView.handleReadOnly(true);
        }
    }

    @Override
    public void setMainUserList(String mLink) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        List<T_TK_Main_User_Rec> main_user_list = gson.fromJson(mLink, new TypeToken<ArrayList<T_TK_Main_User_Rec>>() {}.getType());
        ArrayList<HMAux> hmAuxMainUse = new ArrayList<>();
        for(T_TK_Main_User_Rec item: main_user_list){
            HMAux hmAux = new HMAux();
            //
            hmAux.put(SearchableSpinner.CODE, String.valueOf(item.getUser_code()));
            hmAux.put(SearchableSpinner.ID, item.getUser_nick());
            hmAux.put(SearchableSpinner.DESCRIPTION, item.getUser_name());
            //
            hmAuxMainUse.add(hmAux);
        }
        mView.setMainUserSSList(hmAuxMainUse);

    }
}
