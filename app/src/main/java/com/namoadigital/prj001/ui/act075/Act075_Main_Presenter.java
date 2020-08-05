package com.namoadigital.prj001.ui.act075;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao;
import com.namoadigital.prj001.dao.TK_Ticket_ProductDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Product_Save;
import com.namoadigital.prj001.service.WS_TK_Ticket_Product_Save;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_GET_MATERIAL;
import static com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao.APPROVAL_RETURN_MATERIAL;

public class Act075_Main_Presenter implements Act075_Main_Contract.I_Presenter {
    private Context context;
    private Act075_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;
    private TK_Ticket_ProductDao ticketProductDao;
    private TK_Ticket_ApprovalDao ticketApprovalDao;

    public Act075_Main_Presenter(Context context, Act075_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.ticketDao = new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        this.ticketApprovalDao = new TK_Ticket_ApprovalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        this.ticketProductDao = new TK_Ticket_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    public TK_Ticket getTicket(long customer_code, int ticket_prefix, int ticket_code){
        //
        return ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        customer_code,
                        ticket_prefix,
                        ticket_code
                ).toSqlQuery()
        );
    }

    @Override
    public void saveproduct(int scn, ArrayList<TK_Ticket_Product> tk_ticket_products) {
        mView.setWsProcess(WS_TK_Ticket_Product_Save.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_product_save_ticket_ttl"),
                hmAux_Trans.get("dialog_product_save_ticket_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_TK_Ticket_Product_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.SCN, scn);
        bundle.putSerializable(TK_Ticket_ProductDao.TABLE, tk_ticket_products);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public boolean getWithdrawStatus(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = ticketApprovalDao.query(
                new TK_Ticket_Approval_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticket.getTicket_prefix(),
                        ticket.getTicket_code()
                ).toSqlQuery());

        if(approvals != null){
            for (TK_Ticket_Approval approval:
                 approvals) {
                if (APPROVAL_GET_MATERIAL.equalsIgnoreCase(approval.getApproval_type())) {
                    if(ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(approval.getApproval_status())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean getAppliedStatus(TK_Ticket ticket) {
        List<TK_Ticket_Approval> approvals = ticketApprovalDao.query(
                new TK_Ticket_Approval_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ticket.getTicket_prefix(),
                        ticket.getTicket_code()
                ).toSqlQuery());

        if(approvals != null){
            for (TK_Ticket_Approval approval:
                    approvals) {
                if (APPROVAL_RETURN_MATERIAL.equalsIgnoreCase(approval.getApproval_type())) {
                    if(ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(approval.getApproval_status())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
