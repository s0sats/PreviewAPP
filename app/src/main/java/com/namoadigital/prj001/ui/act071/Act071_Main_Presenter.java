package com.namoadigital.prj001.ui.act071;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act071_Main_Presenter implements Act071_Main_Contract.I_Presenter {

    private Context context;
    private Act071_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_Ticket_CtrlDao ticketCtrlDao;
    private MD_PartnerDao mdPartnerDao;

    public Act071_Main_Presenter(Context context, Act071_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        //
        this.ticketCtrlDao = new TK_Ticket_CtrlDao(
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
    public boolean validateBundleParams(int mTkActionPrefix, int mTkActionCode, int mTkActionSeq) {
        return mTkActionPrefix > 0 && mTkActionCode > 0 && mTkActionSeq > 0 ;
    }

    @Override
    public TK_Ticket_Ctrl getTicketCtrlObj(int mActionPrefix, int mActionCode, int mActionSeq) {
        return ticketCtrlDao.getByString(
            new TK_Ticket_Ctrl_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mActionPrefix,
                mActionCode,
                mActionSeq
            ).toSqlQuery()
        );
    }

    @Override
    public String getFormattedDoneInfo(String ctrl_end_date, String ctrl_end_user_name) {
        String sFormatted = ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(ctrl_end_date),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );
        //
        sFormatted += " (" + ctrl_end_user_name + ")";
        //
        return sFormatted;
    }

    @Override
    public boolean getReadOnlyDefinition(TK_Ticket_Ctrl mTicketCtrl) {
        return isReadOnlyStatus(mTicketCtrl.getCtrl_status())
                || !hasActionExecProfile()
                || !hasPartnerProfile(mTicketCtrl.getPartner_code());
    }

    @Override
    public boolean hasPartnerProfile(Integer partner_code) {
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

    @Override
    public boolean hasActionExecProfile() {
        return ToolBox_Inf.profileExists(
            context,
            ConstantBaseApp.PROFILE_MENU_TICKET,
            ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_ACTION_EXEC
        );
    }

    @Override
    public boolean isReadOnlyStatus(String ticketStatus) {
        return
            ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(ticketStatus)
                || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equalsIgnoreCase(ticketStatus);
    }

    @Override
    public void onBackPressedClicked(String requestingAct) {
        switch (requestingAct){
            case ConstantBaseApp.ACT070:
            default:
                mView.callAct070();
                break;
        }
    }
}
