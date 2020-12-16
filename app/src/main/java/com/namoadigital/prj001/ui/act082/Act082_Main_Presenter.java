package com.namoadigital.prj001.ui.act082;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Main_User_Rec;
import com.namoadigital.prj001.receiver.WBR_TK_Header_N_Group_Save;
import com.namoadigital.prj001.receiver.WBR_TK_Main_User_List;
import com.namoadigital.prj001.service.WS_TK_Header_N_Group_Save;
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

    @Override
    public String getDateFromForm(String date, String time) {
        String dateTime = date + " " + time;

        return dateTime;
    }

    @Override
    public String getTimeFromForm(String day, String hour, String minutes) {
        String time = day + " " + hour + ":" + minutes;
        return time;

    }

    @Override
    public void callEditHeaderService(int ticket_prefix, int ticket_code, int scn, Integer main_user_code, String main_user_name, String main_user_nick, String forecast_time, String start_date, String forecast_date, String timeAction, String internalComments, int move_other_date, int move_steps) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Header_N_Group_Save.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_edit_header_date_ttl"),
                    hmAux_trans.get("dialog_edit_header_date_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Header_N_Group_Save.class);
            Bundle bundle = new Bundle();
            //
            bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticket_prefix);
            bundle.putInt(TK_TicketDao.TICKET_CODE, ticket_code);
            bundle.putInt(TK_TicketDao.SCN, scn);
            if(main_user_code != -1) {
                bundle.putInt(TK_TicketDao.MAIN_USER, main_user_code);
                bundle.putString(TK_TicketDao.MAIN_USER_NAME, main_user_name);
                bundle.putString(TK_TicketDao.MAIN_USER_NICK, main_user_nick);
            }
            if(start_date != null) {
                bundle.putString(TK_TicketDao.START_DATE, start_date);
            }
            //
            if(forecast_date != null) {
                bundle.putString(TK_TicketDao.FORECAST_DATE, forecast_date);
            }
            //
            if(forecast_time != null) {
                bundle.putString(TK_TicketDao.FORECAST_TIME, forecast_time);
            }
            //
            if(internalComments != null) {
                bundle.putString(TK_TicketDao.INTERNAL_COMMENTS, internalComments);
            }
            bundle.putString(WS_TK_Header_N_Group_Save.TIME_ACTION, timeAction);
            bundle.putInt(WS_TK_Header_N_Group_Save.MOVE_OTHER_DATE, move_other_date);
            bundle.putInt(WS_TK_Header_N_Group_Save.MOVE_STEPS,  move_steps);
            bundle.putBoolean(WS_TK_Header_N_Group_Save.IS_HEADER_DATETIME_CHANGES, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            //
        } else {
            mView.showMsg(
                    hmAux_trans.get("dialog_main_user_search_ttl"),
                    hmAux_trans.get("dialog_main_user_search_start")
            );
        }
    }

    @Override
    public String getElapsedTime(TK_Ticket mTk_ticket) {
        long start_date = ToolBox_Inf.dateToMilliseconds(mTk_ticket.getStart_date());
        long current_date = ToolBox_Inf.dateToMilliseconds(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        long elapsed_time = current_date - start_date;
        String day;
        String hour;
        String minute;
        day = String.valueOf( (int) elapsed_time/86400000);
        return day;
    }

    @Override
    public Long getRemainingTime(TK_Ticket mTk_ticket) {
        long forecast_date =0;
        if(mTk_ticket.getForecast_date() != null) {
            forecast_date = ToolBox_Inf.dateToMilliseconds(mTk_ticket.getForecast_date());
        }else{
            return null;
        }
        long current_date = ToolBox_Inf.dateToMilliseconds(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        long remaining_time =  forecast_date - current_date;
        return remaining_time;
    }
}
