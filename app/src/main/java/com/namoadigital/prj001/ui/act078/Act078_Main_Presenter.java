package com.namoadigital.prj001.ui.act078;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Act078_Main_Presenter implements Act078_Main_Contract.I_Presenter{
    Context context;
    Act078_Main_Contract.I_View mView;
    HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;

    public Act078_Main_Presenter(Context context, Act078_Main_Contract.I_View mView, HMAux hmAux_trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void getStepOrigin(int mTkPrefix, int mTkCode) {
        TK_Ticket ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mTkPrefix,
                        mTkCode
                ).toSqlQuery()
        );

        mView.loadTicketOrigin(ticket);
    }

    @Override
    public String getNavegationIntentData(TK_Ticket ticket) {
        StringBuilder sb = new StringBuilder();
        sb
            .append("geo:")
            .append(ticket.getAddress_lat())
            .append(ticket.getAddress_lng())
            .append("?q=")
            .append(ticket.getAddress_street() != null ? ticket.getAddress_street().trim().replace(" ","+") + "+" : "")
            .append(ticket.getAddress_num() != null ? ticket.getAddress_num().trim().replace(" ","+") + "+" : "")
            .append(ticket.getAddress_zipcode() != null ? ticket.getAddress_zipcode().trim().replace(" ","+") + "+" : "")
            .append(ticket.getAddress_city() != null ? ticket.getAddress_city().trim().replace(" ","+") + "+" : "")
            ;
        //
        return sb.toString();
    }
}
