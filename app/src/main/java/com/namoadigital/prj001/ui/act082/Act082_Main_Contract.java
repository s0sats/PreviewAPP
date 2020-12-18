package com.namoadigital.prj001.ui.act082;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;

import java.util.ArrayList;

public interface Act082_Main_Contract {
    interface I_View {
        void callAct070();

        void setWsProcess(String wsProcess);

        void showPD(String progress_ttl, String progress_start);

        void showMsg(String show_ttl, String show_msg);

        void setProduct(ArrayList<MD_Product> productList);

        void callAct020(Context context, Bundle bundle);

        void handleReadOnly(boolean offlineMode);

        void setMainUserSSList(ArrayList<HMAux> mainUserListSS);
    }

    interface I_Presenter {

        boolean getDateEditionProfile();

        boolean getHeaderEditionProfile();

        boolean getStepEditTimeProfile();

        void onBackPressedClicked(String mainRequestingAct);

        TK_Ticket getTicketData(int ticketPrefix, int ticketCode);

        void callMainUserService(TK_Ticket ticket);

        void setMainUserList(String mLink);

        String getDateFromForm(String date, String time);

        String getTimeFromForm(String day, String hour, String minutes);

        void callEditHeaderService(int ticket_prefix, int ticket_code, int scn, Integer main_user_code, String main_user_name, String main_user_nick, String forecast_time, String start_date, String forecast_date, String timeAction, String internalComments, int move_other_date, int move_steps);

        Long getElapsedTime(TK_Ticket mTk_ticket);

        String getFormattedDate(long time);

        Long getRemainingTime(TK_Ticket mTk_ticket);

        boolean hasAnyOnlinePendency(Context context, TK_Ticket tkTicket);
    }
}
