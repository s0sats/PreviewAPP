package com.namoadigital.prj001.ui.act082;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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

        return ticket;
    }
}
