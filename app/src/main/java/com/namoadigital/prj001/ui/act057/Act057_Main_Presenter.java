package com.namoadigital.prj001.ui.act057;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound_Search_Record;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Download;
import com.namoadigital.prj001.service.WS_IO_Inbound_Download;
import com.namoadigital.prj001.sql.Sql_Act057_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act057_Main_Presenter implements Act057_Main_Contract.I_Presenter{

    private Context context;
    private Act057_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_InboundDao inboundDao;

    public Act057_Main_Presenter(Context context, Act057_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.inboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void processListInfo(long record_count, long record_page, ArrayList<IO_Inbound_Search_Record> records) {
        mView.setRecordInfo(records == null ? 0 : records.size());
        //
        if(records != null || records.size() > 0){
            mView.loadInboundList(records);
            //
//            if(records.size() == 1){
//               defineFlow();
//            }
        }
    }

    @Override
    public void getPendenciesList() {
        ArrayList<IO_Inbound_Search_Record> searchRecords = new ArrayList<>();
        //
        mView.setOnline(false);
        //
        ArrayList<HMAux> inbounds = (ArrayList<HMAux>) inboundDao.query_HM(
            new Sql_Act057_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                true,
                true
            ).toSqlQuery()
        );
        //
        for(HMAux inbound: inbounds){
            IO_Inbound_Search_Record aux = getHmAuxToInboundSearchRecord(inbound);
            if(aux != null){
                searchRecords.add(aux);
            }
        }
        //
        mView.setRecordInfo(searchRecords.size());
        //
        mView.loadInboundList(searchRecords);
    }

    @Override
    public void executeInboundDownload(String inboundList) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Inbound_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_inbound_download_ttl"),
                    hmAux_Trans.get("dialog_inbound_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Inbound_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(IO_InboundDao.INBOUND_CODE, inboundList);
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processDownloadReturn(HMAux hmAux) {
        if(hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_PROCESS_KEY)){
            if(hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_PREFIX_KEY)
               && hmAux.hasConsistentValue(ConstantBaseApp.HMAUX_CODE_KEY)
            ){
                Bundle bundle = new Bundle();
                bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY,hmAux.get(ConstantBaseApp.HMAUX_PROCESS_KEY));
                bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY,hmAux.get(ConstantBaseApp.HMAUX_PREFIX_KEY));
                bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY,hmAux.get(ConstantBaseApp.HMAUX_CODE_KEY));

                mView.callAct061(bundle);
            }else{
                mView.callAct062();
            }
        }else{
            mView.showAlert(
                    hmAux.get("alert_download_return_ttl"),
                    hmAux.get("alert_download_return_error_msg")
            );
        }
    }

    private IO_Inbound_Search_Record getHmAuxToInboundSearchRecord(HMAux hmAux){
        IO_Inbound_Search_Record record = new IO_Inbound_Search_Record();
        try {
            //
            record.setCustomer_code(Integer.parseInt(hmAux.get(IO_InboundDao.CUSTOMER_CODE)));
            record.setInbound_prefix(Integer.parseInt(hmAux.get(IO_InboundDao.INBOUND_PREFIX)));
            record.setInbound_code(Integer.parseInt(hmAux.get(IO_InboundDao.INBOUND_CODE)));
            record.setInbound_id(hmAux.get(IO_InboundDao.INBOUND_ID));
            record.setInbound_desc(hmAux.get(IO_InboundDao.INBOUND_DESC));
            record.setCreate_date(null);
            record.setEta_date(hmAux.get(IO_InboundDao.ETA_DATE));
            record.setInvoice_number(hmAux.get(IO_InboundDao.INVOICE_NUMBER));
            record.setStatus(hmAux.get(IO_InboundDao.STATUS));
            record.setComments(hmAux.get(IO_InboundDao.COMMENTS));
            record.setPerc_done(Float.valueOf(hmAux.get(Sql_Act057_001.PERC_DONE)));
            if(hmAux.get(IO_InboundDao.FROM_TYPE).equals(ConstantBaseApp.IO_FROM_TYPE_PARTNER)) {
                record.setFrom(hmAux.get(IO_InboundDao.FROM_PARTNER_DESC));
            }else{
                record.setFrom(hmAux.get(IO_InboundDao.FROM_SITE_CODE));
            }
            record.setModal(hmAux.get(IO_InboundDao.MODAL_DESC));
            //
            if( hmAux.hasConsistentValue(IO_InboundDao.TO_SITE_CODE)
                && hmAux.get(IO_InboundDao.TO_SITE_CODE).equals(ToolBox_Con.getPreference_Site_Code(context)))
            {
                record.setSameSiteAsLoggedOrFree(true);
            }else{
                record.setSameSiteAsLoggedOrFree(false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //
        return record;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct056();
    }
}
