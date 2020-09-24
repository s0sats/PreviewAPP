package com.namoadigital.prj001.ui.act077;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act077_Main_Presenter implements Act077_Main_Contract.I_Presenter {
    Context context;
    Act077_Main_Contract.I_View mView;
    HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    public Act077_Main_Presenter(Context context, Act077_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }


    @Override
    public void getStepOrigin(int ticketPrefix, int ticketCode) {
        TK_Ticket ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticketPrefix,
                        ticketCode
                ).toSqlQuery()
        );

        mView.loadTicketOrigin(ticket);
    }

}
