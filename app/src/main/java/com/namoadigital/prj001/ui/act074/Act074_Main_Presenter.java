package com.namoadigital.prj001.ui.act074;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.VH_models.Act069_TicketVH;
import com.namoadigital.prj001.sql.Sql_Act069_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act074_Main_Presenter implements Act074_Main_Contract.I_Presenter {
    private Context context;
    private Act074_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;

    public Act074_Main_Presenter(Context context, Act074_Main_Contract.I_View mView, HMAux hmAux_Trans) {
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
    public void getTicketList(boolean isHistoricalShown, boolean bStatusPending, boolean bStatusProcess, boolean bStatusWaitingSync, boolean bStatusDone, boolean bParterEmpty, boolean bParterProfile, long ticketProductCode, long ticketSerialCode, boolean bStatusNotExecuted, boolean bStatusIgnored, boolean bStatusCanceled, boolean bStatusRejected, boolean bParterNoProfile) {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
                new Sql_Act069_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        isHistoricalShown,
                        bStatusPending,
                        bStatusProcess,
                        bStatusWaitingSync,
                        bStatusDone,
                        ticketProductCode,
                        ticketSerialCode,
                        bStatusNotExecuted,
                        bStatusIgnored,
                        bStatusCanceled,
                        bStatusRejected,
                        bParterEmpty,
                        bParterProfile,
                        bParterNoProfile
                ).toSqlQuery()
        );
        //
//        mView.loadTicketList(generateTicketVhList(auxTickets));
    }

    @Override
    public int checkTicketToSync() {
        return 0;
    }

    @Override
    public String getBtnSyncText(int qtyToSync) {
        return null;
    }

    @Override
    public boolean hasTicketInUpdateRequired() {
        return false;
    }

    @Override
    public void executeTicketSync() {

    }

    @Override
    public void onBackPressedClicked(String requestingAct) {

    }

    @Override
    public void checkTicketFlow(Act069_TicketVH item) {

    }
}
