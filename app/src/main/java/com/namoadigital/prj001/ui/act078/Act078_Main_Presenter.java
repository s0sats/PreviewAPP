package com.namoadigital.prj001.ui.act078;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Form;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.namoadigital.prj001.util.ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE;

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
        if(TK_Ticket.isValidTkTicket(ticket) && ticket.getStep() != null && ticket.getStep().size() > 0){
            mView.loadTicketOrigin(ticket);
        }else{
            mView.ticketParameterError(
                hmAux_Trans.get("alert_origin_step_not_found_error_ttl"),
                hmAux_Trans.get("alert_origin_step_not_found_error_msg")
            );
        }

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

    @Override
    public String getOriginTypeLbl(TK_Ticket tkTicket) {
        switch (tkTicket.getOrigin_type()){
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE:
                return hmAux_Trans.get("barcode_origin_type_lbl");
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL:
                return hmAux_Trans.get("manual_origin_type_lbl");
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MEASURE:
                return hmAux_Trans.get("measure_origin_type_lbl");
            case TK_TICKET_ORIGIN_TYPE_SCHEDULE:
                return getScheduleOriginTypeLbl(tkTicket);
            default:
                return hmAux_Trans.get("new_origin_type_lbl");
        }
    }

    private String getScheduleOriginTypeLbl(TK_Ticket tkTicket) {
        try {
            //Quando tipo schedule, pode ser o agendamento de action ou ticket automatico.
            //No caso do ticket automatico não existe o ctrl, então isso que define o lbl
            TK_Ticket_Step originStep = tkTicket.getStep().get(0);
            return  originStep.getCtrl() != null && originStep.getCtrl().size() > 0
                ? hmAux_Trans.get("schedule_action_origin_type_lbl")
                : hmAux_Trans.get("schedule_ticket_origin_type_lbl");
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            return hmAux_Trans.get("new_origin_type_lbl");
        }
    }

    @Override
    public boolean isScheduleAction(TK_Ticket tkTicket){
        try {
            //Quando tipo schedule, pode ser o agendamento de action ou ticket automatico.
            //No caso do ticket automatico não existe o ctrl.
            TK_Ticket_Step originStep = tkTicket.getStep().get(0);
            return originStep.getCtrl() != null && originStep.getCtrl().size() > 0;
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            return false;
        }
    }

    @Override
    public void defineOriginLayoutConfig(TK_Ticket ticket) {
        switch (ticket.getOrigin_type()){
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MEASURE:
                mView.setMeasureLayout(ticket,true);
                mView.setScheduleLayout(ticket,false,isScheduleAction(ticket));
                break;
            case TK_TICKET_ORIGIN_TYPE_SCHEDULE:
                mView.setMeasureLayout(ticket,false);
                mView.setScheduleLayout(ticket,true,isScheduleAction(ticket));
                break;
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL:
            default:
                mView.setMeasureLayout(ticket,false);
                mView.setScheduleLayout(ticket,false,isScheduleAction(ticket));
                break;
        }
    }

    @NotNull
    @Override
    public String getFormatedScorePerc(TK_Ticket_Form form) {
        return form.getScore_perc().replace(".", ",") + "%";
    }

    @Override
    public void tryOpenFormPDF(TK_Ticket_Form form) {
        if(form.getPdf_url() == null || form.getPdf_url().isEmpty()){
            mView.showMsg(
                hmAux_Trans.get("alert_form_pdf_not_generated_ttl"),
                hmAux_Trans.get("alert_form_pdf_not_generated_msg")
            );
        }else{
            if(form.getPdf_url_local() == null || form.getPdf_url().isEmpty()){
                mView.showMsg(
                    hmAux_Trans.get("alert_form_pdf_not_downloaded_ttl"),
                    hmAux_Trans.get("alert_form_pdf_not_downloaded_msg")
                );
            }else{
                openFormPDF(form.getPdf_url_local());
            }
        }
    }

    private void openFormPDF(String pdf_url_local) {
        File pdfFile = new File(ConstantBaseApp.CACHE_PATH + "/" + pdf_url_local);
        copyPdfToPdfFolder(pdfFile);
        Intent pdfIntent = ToolBox_Inf.getOpenPdfIntent(context,ConstantBaseApp.CACHE_PDF + "/" + pdf_url_local);
        //
        try {
            context.startActivity(pdfIntent);
        }catch (ActivityNotFoundException e){
            ToolBox_Inf.registerException(getClass().getName(), e);
            //
            ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_starting_pdf_not_supported_ttl"),
                hmAux_Trans.get("alert_starting_pdf_not_supported_msg"),
                null,
                0
            );
        }
    }

    private void copyPdfToPdfFolder(File pdfFile) {
        try {
            ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);
            ToolBox_Inf.copyFile(
                pdfFile,
                new File(Constant.CACHE_PDF)
            );
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
    }
}
