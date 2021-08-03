package com.namoadigital.prj001.ui.act079;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Form;
import com.namoadigital.prj001.model.TkTicketOriginNc;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

public class Act079_Main_Presenter implements Act079_Main_Contract.I_Presenter{
    Context context;
    Act079_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private TK_TicketDao ticketDao;

    public Act079_Main_Presenter(Context context, Act079_Main_Contract.I_View mView, HMAux hmAux_trans) {
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
        TK_Ticket ticket = getTkTicket(mTkPrefix, mTkCode);
        mView.loadTicketOrigin(ticket);
    }

    private TK_Ticket getTkTicket(int mTkPrefix, int mTkCode) {
        TK_Ticket ticket = ticketDao.getByString(
                new TK_Ticket_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                    mTkPrefix,
                    mTkCode
                ).toSqlQuery()
        );
        return ticket;
    }

    @Override
    public void checkOriginType(int mTkPrefix, int mTkCode) {
        TK_Ticket ticket = getTkTicket(mTkPrefix, mTkCode);
        if(ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_NC.equals(ticket.getOrigin_type())){
            mView.loadTicketNcs(
                generateNcViews(ticket.getNc())
            );
        }
    }

    private ArrayList<TextView> generateNcViews(ArrayList<TkTicketOriginNc> ncList) {
        ArrayList<TextView> ncViews = new ArrayList<>();
        for (TkTicketOriginNc tkTicketOriginNc : ncList) {
            TextView auxTv = new TextView(context);
            auxTv.setText(tkTicketOriginNc.toString());
            auxTv.setBackground(ContextCompat.getDrawable(context, R.drawable.namoa_cell_8_states));
            ncViews.add(auxTv);
        }
        return ncViews;
    }

    //
    @Override
    public void tryOpenFormPDF(TK_Ticket_Form form) {
        if(form.getPdf_url() == null || form.getPdf_url().isEmpty()){
            mView.showAlert(
                    hmAux_Trans.get("alert_form_pdf_not_generated_ttl"),
                    hmAux_Trans.get("alert_form_pdf_not_generated_msg")
            );
        }else{
            if(form.getPdf_url_local() == null || form.getPdf_url().isEmpty()){
                mView.showAlert(
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
