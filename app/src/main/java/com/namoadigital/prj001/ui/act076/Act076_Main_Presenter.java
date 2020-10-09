package com.namoadigital.prj001.ui.act076;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.VH_models.Act074_TicketVH;
import com.namoadigital.prj001.sql.Sql_Act076_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act076_Main_Presenter implements Act076_Main_Contract.I_Presenter {
    Context context;
    Act076_Main_Contract.I_View mView;
    HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    public Act076_Main_Presenter(Context context, Act076_Main_Contract.I_View mView, HMAux hmAux_Trans) {
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

    private ArrayList<Act074_TicketVH> generateTicketVhList(ArrayList<HMAux> auxTickets) {
        ArrayList<Act074_TicketVH> tickets = new ArrayList<>();
        if (auxTickets != null && auxTickets.size() > 0) {
            try {
                for (HMAux aux : auxTickets) {
//                    getCtrlsSerialsList(aux);
                    //
                    tickets.add(
                            Act074_TicketVH.getTicketVHObj(aux,true)
                    );
                }
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(),e);
                tickets = new ArrayList<>();
                mView.showMsg(
                        hmAux_Trans.get("alert_error_on_generate_list_ttl"),
                        hmAux_Trans.get("alert_error_on_generate_list_msg")
                );
            }
        }
        //
        return tickets;
    }

    @Override
    public void getTicketListBySerial(long ticketProductCode, long ticketSerialCode) {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
                new Sql_Act076_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        ticketProductCode,
                        ticketSerialCode
                ).toSqlQuery()
        );
        //
        mView.loadTicketList(generateTicketVhList(auxTickets));
    }

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct){
            case ConstantBaseApp.ACT068:
            case ConstantBaseApp.ACT070:
                mView.callAct068();
                break;
            case ConstantBaseApp.ACT012:
                mView.callAct012();
                break;
            case ConstantBaseApp.ACT005:
            default:
                mView.callAct005();
        }
    }

    @Override
    public void checkTicketFlow(Act074_TicketVH item) {
        mView.callAct070(generateAct070Bundle(item));
    }

    /**
     * LUCHE - 18/03/2020
     * <P></P>
     * Criad bundle com dado do ticket para seusado na chamada da act070
     * @param item Ticket clicado
     * @return - Bundle com pk do ticket
     */
    private Bundle generateAct070Bundle(Act074_TicketVH item) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX,item.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE,item.getTicket_code());
        return bundle;
    }
}
