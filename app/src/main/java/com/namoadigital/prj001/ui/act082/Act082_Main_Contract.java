package com.namoadigital.prj001.ui.act082;

import android.content.Context;
import android.os.Bundle;

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
    }

    interface I_Presenter {



        boolean getDateEditionProfile();

        boolean getHeaderEditionProfile();

        boolean getStepEditTimeProfile();

        void onBackPressedClicked(String mainRequestingAct);

        TK_Ticket getTicketData(int ticketPrefix, int ticketCode);

    }
}
