package com.namoadigital.prj001.ui.act070;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Action_V;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Super;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act070_Main_Presenter implements Act070_Main_Contract.I_Presenter {
    private Context context;
    private Act070_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;

    public Act070_Main_Presenter(Context context, Act070_Main_Contract.I_View mView, HMAux hmAux_Trans) {
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
    @Nullable
    public TK_Ticket getTicketObj(int mTkPrefix, int mTkCode) {
        return ticketDao.getByString(
            new TK_Ticket_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mTkPrefix,
                mTkCode
            ).toSqlQuery()
        );
    }

    @Override
    public boolean validateBundleParams(int mTkPrefix, int mTkCode) {
        return mTkPrefix > 0 && mTkCode > 0;
    }

    @Override
    public boolean getReadOnlyDefinition(TK_Ticket mTicket) {
        return isReadOnlyStatus(mTicket.getTicket_status()) || missingProfile();
    }

    private boolean missingProfile() {
        return !ToolBox_Inf.profileExists(
                                context,
                                ConstantBaseApp.PROFILE_MENU_TICKET,
                                ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_ACTION_EXEC
                            );
    }

    private boolean isReadOnlyStatus(String ticketStatus) {
        return
            ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(ticketStatus)
            || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(ticketStatus);
    }

    @Override
    public String getFormattedCheckinInfo(String checkin_date, String checkin_user_name) {
        String sFormatted = ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(checkin_date),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );
        //
        sFormatted += " (" + checkin_user_name + ")";
        return sFormatted;
    }

    @Override
    public String getFormattedDoneInfo(String close_date, String close_user_name) {
        return getFormattedCheckinInfo(close_date, close_user_name);
    }

    @Override
    public ArrayList<TK_Ticket_Ctrl_Super> generateCtrlActions(TK_Ticket mTicket, LinearLayout llActions) {
        ArrayList<TK_Ticket_Ctrl_Super> ctrlSupers = new ArrayList<>();
        if (mTicket != null && mTicket.getCtrl() != null && mTicket.getCtrl().size() > 0) {
            for (TK_Ticket_Ctrl ctrl : mTicket.getCtrl()) {
                TK_Ticket_Ctrl_Super auxCtrl = null;
                switch (ctrl.getCtrl_type()) {
                    case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                        auxCtrl = configActionView(mTicket,ctrl);
                        break;
                    default:
                        auxCtrl = configSuperView(mTicket,ctrl);
                }
                ctrlSupers.add(auxCtrl);
                llActions.addView(auxCtrl);
            }
        }
        //
        return ctrlSupers;
    }

    private TK_Ticket_Ctrl_Super configSuperView(TK_Ticket mTicket, TK_Ticket_Ctrl ctrl) {
        TK_Ticket_Ctrl_Super ctrlSuper = new TK_Ticket_Ctrl_Super(
            context,
            mTicket.getCurrent_product_code(),
            mTicket.getCurrent_serial_code(),
            ctrl,
            hmAux_Trans,
            null
        );
        //
        return ctrlSuper;
    }

    private TK_Ticket_Ctrl_Action_V configActionView(TK_Ticket mTicket, final TK_Ticket_Ctrl ctrl) {
        TK_Ticket_Ctrl_Action_V actionCtrlView = new TK_Ticket_Ctrl_Action_V(
            context,
            mTicket.getCurrent_product_code(),
            mTicket.getCurrent_serial_code(),
            ctrl,
            hmAux_Trans,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = getAct071Bundle(ctrl);
                    mView.callAct071(bundle);

                }
            }
        );
        //
        return actionCtrlView;
    }

    private Bundle getAct071Bundle(TK_Ticket_Ctrl ctrl) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ctrl.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, ctrl.getTicket_code());
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ, ctrl.getTicket_seq());
        return bundle;
    }

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct) {
            case ConstantBaseApp.ACT069:
            default:
                mView.callAct069();
        }
    }
}
