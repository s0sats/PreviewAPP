package com.namoadigital.prj001.ui.act070;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Action_V;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Generic;
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
    private MD_PartnerDao mdPartnerDao;

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
        //
        this.mdPartnerDao = new MD_PartnerDao(
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
        return  isOtherCheckIn(mTicket.getCheckin_user())
                || isReadOnlyStatus(mTicket.getTicket_status())
                || missingProfile();
    }

    private boolean isOtherCheckIn(Integer checkin_user) {
        if(checkin_user == null){
            return false;
        }else{
            return !ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(checkin_user));
        }
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
    public boolean checkFilterDisable(ArrayList<TK_Ticket_Ctrl> ctrls) {
        //Se vazio ou apénas um controle, esconde filtro
        if(ctrls == null || ctrls.size() <= 1 ){
            return true;
        }
        //Se algum ctrl status diferente de DONE, NAO esconde filtro
        for (TK_Ticket_Ctrl ctrl : ctrls) {
            if(!ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(ctrl.getCtrl_status())){
                return false;
            }
        }
        //Se chegou aqui, esconde filtro
        return true;
    }

    @Override
    public ArrayList<TK_Ticket_Ctrl_Super> generateCtrlActions(TK_Ticket mTicket, LinearLayout llActions, boolean filterOn) {
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
                //Aplica filtro se ativo.
                if(filterOn){
                    auxCtrl.applyFilterVisibility();
                }
                //
                ctrlSupers.add(auxCtrl);
                llActions.addView(auxCtrl);
            }
        }
        //
        return ctrlSupers;
    }

    private TK_Ticket_Ctrl_Generic configSuperView(TK_Ticket mTicket, TK_Ticket_Ctrl ctrl) {
        TK_Ticket_Ctrl_Generic ctrlGeneric = new TK_Ticket_Ctrl_Generic(
            context,
            mTicket.getCurrent_product_code(),
            mTicket.getCurrent_serial_code(),
            ctrl,
            hmAux_Trans,
            null
        );
        //
        return ctrlGeneric;
    }

    private TK_Ticket_Ctrl_Action_V configActionView(final TK_Ticket mTicket, final TK_Ticket_Ctrl ctrl) {
        TK_Ticket_Ctrl_Action_V actionCtrlView = new TK_Ticket_Ctrl_Action_V(
            context,
            mTicket.getCurrent_product_code(),
            mTicket.getCurrent_serial_code(),
            ctrl,
            hmAux_Trans,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = getAct071CtrlBundleInfo(mTicket,ctrl);
                    mView.callAct071(bundle);
                }
            },
            new TK_Ticket_Ctrl_Action_V.TK_Ticket_Ctrl_Action_I() {
                @Override
                public boolean checkPartnerProfile(Integer partnerCode) {
                    return hasPartnerProfile(partnerCode);
                }
            }
        );
        //
        return actionCtrlView;
    }


    private boolean hasPartnerProfile(Integer partner_code) {
        if(partner_code == null){
            return true;
        }
        //
        MD_Partner partner = mdPartnerDao.getByString(
            new MD_Partner_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                partner_code
            ).toSqlQuery()
        );
        //
        if(partner != null && partner.getCustomer_code() > 0){
            return true;
        }
        //
        return false;
    }

    private Bundle getAct071CtrlBundleInfo(TK_Ticket mTicket , TK_Ticket_Ctrl ctrl) {
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ctrl.getTicket_prefix());
        bundle.putInt(TK_TicketDao.TICKET_CODE, ctrl.getTicket_code());
        bundle.putInt(TK_Ticket_ActionDao.TICKET_SEQ, ctrl.getTicket_seq());
        bundle.putString(TK_TicketDao.TICKET_ID, mTicket.getTicket_id());
        bundle.putString(TK_TicketDao.TYPE_PATH,  mTicket.getType_path());
        bundle.putString(TK_TicketDao.TYPE_DESC, mTicket.getType_desc());
        bundle.putBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN, mTicket.getCheckin_user() == null || !ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(mTicket.getCheckin_user())));
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
