package com.namoadigital.prj001.ui.act066;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Outbound_Search_Record;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Download;
import com.namoadigital.prj001.receiver.WBR_IO_Outbound_Search;
import com.namoadigital.prj001.service.WS_IO_Outbound_Download;
import com.namoadigital.prj001.service.WS_IO_Outbound_Search;
import com.namoadigital.prj001.sql.Sql_Act057_001;
import com.namoadigital.prj001.sql.Sql_Act066_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act066_Main_Presenter implements Act066_Main_Contract.I_Presenter{

    private Context context;
    private Act066_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private IO_OutboundDao outboundDao;
    private String zone_code;
    private String local_code;
    private String outbound_id;
    private String invoince;

    public Act066_Main_Presenter(Context context, Act066_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.outboundDao = new IO_OutboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void processListInfo(long record_count, long record_page, ArrayList<IO_Outbound_Search_Record> records) {
        mView.setRecordInfo(records == null ? 0 : records.size());
        //
        if(records != null || records.size() > 0){
            mView.loadOutboundList(records);
            //
//            if(records.size() == 1){
//               defineFlow();
//            }
        }
    }

    @Override
    public void getPendenciesList() {
        ArrayList<IO_Outbound_Search_Record> searchRecords = new ArrayList<>();
        //
        mView.setOnline(false);
        //
        ArrayList<HMAux> outbounds = (ArrayList<HMAux>) outboundDao.query_HM(
            new Sql_Act066_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                true,
                true
            ).toSqlQuery()
        );
        //
        for(HMAux outbound: outbounds){
            IO_Outbound_Search_Record aux = getHmAuxToOutboundSearchRecord(outbound);
            if(aux != null){
                searchRecords.add(aux);
            }
        }
        //
        mView.setRecordInfo(searchRecords.size());
        //
        mView.loadOutboundList(searchRecords);
    }

    @Override
    public void executeOutboundDownload(String outboundList) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_IO_Outbound_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_outbound_download_ttl"),
                    hmAux_Trans.get("dialog_outbound_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Outbound_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putString(MD_Site_Zone_LocalDao.ZONE_CODE, zone_code);
            bundle.putString(MD_Site_Zone_LocalDao.LOCAL_CODE, local_code);
            bundle.putString(WS_IO_Outbound_Search.KEY_CODE_ID, outbound_id);
            bundle.putString(IO_OutboundDao.INVOICE_NUMBER, invoince);
            bundle.putString(IO_OutboundDao.OUTBOUND_CODE, outboundList);
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

//                mView.callAct061(bundle);
            }else{
                mView.callAct065();
            }
        }else{
            mView.showAlert(
                hmAux_Trans.get("alert_download_return_ttl"),
                hmAux_Trans.get("alert_download_return_error_msg")
            );
        }
    }

    private IO_Outbound_Search_Record getHmAuxToOutboundSearchRecord(HMAux hmAux){
        IO_Outbound_Search_Record record = new IO_Outbound_Search_Record();
        try {
            //
            record.setCustomer_code(Integer.parseInt(hmAux.get(IO_OutboundDao.CUSTOMER_CODE)));
            record.setOutbound_prefix(Integer.parseInt(hmAux.get(IO_OutboundDao.OUTBOUND_PREFIX)));
            record.setOutbound_code(Integer.parseInt(hmAux.get(IO_OutboundDao.OUTBOUND_CODE)));
            record.setOutbound_id(hmAux.get(IO_OutboundDao.OUTBOUND_ID));
            record.setOutbound_desc(hmAux.get(IO_OutboundDao.OUTBOUND_DESC));
            record.setCreate_date(null);
            record.setEta_date(hmAux.get(IO_OutboundDao.ETA_DATE));
            record.setInvoice_number(hmAux.get(IO_OutboundDao.INVOICE_NUMBER));
            record.setStatus(hmAux.get(IO_OutboundDao.STATUS));
            record.setComments(hmAux.get(IO_OutboundDao.COMMENTS));
            record.setPerc_done(Float.valueOf(hmAux.get(Sql_Act057_001.PERC_DONE)));

            record.setModal(hmAux.get(IO_OutboundDao.MODAL_CODE));
            //
            if( hmAux.hasConsistentValue(IO_OutboundDao.TO_SITE_CODE)
                && hmAux.get(IO_OutboundDao.TO_SITE_CODE).equals(ToolBox_Con.getPreference_Site_Code(context)))
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
        mView.callAct065();
    }
}
