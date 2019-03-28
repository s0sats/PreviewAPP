package com.namoadigital.prj001.ui.act056;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.T_IO_Inbound_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Search;
import com.namoadigital.prj001.service.WS_IO_Inbound_Search;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act056_Main_Presenter implements Act056_Main_Contract.I_Presenter {

    private Context context;
    private Act056_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private MD_Site_ZoneDao siteZoneDao;
    private MD_Site_Zone_LocalDao siteZoneLocalDao;
    private IO_InboundDao inboundDao;

    public Act056_Main_Presenter(Context context, Act056_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.inboundDao = new IO_InboundDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val) {
        //
        if (default_val) {
            setDefaultZone(ss_zone);
        }
        //
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_zone, null, null, null, false, true);
        }
        //
        ArrayList<HMAux> zoneList = (ArrayList<HMAux>) siteZoneDao.query_HM(
                new MD_Site_Zone_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        ss_zone.setmOption(zoneList);
    }

    @Override
    public void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_local, null, null, null, false, true);
        }
        //
        ArrayList<HMAux> localList = (ArrayList<HMAux>) siteZoneLocalDao.query_HM(
                new MD_Site_Zone_Local_Sql_SS_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ToolBox_Con.getPreference_Site_Code(context),
                        ss_zone.getmValue().get(SearchableSpinner.CODE)
                ).toSqlQuery()
        );
        //
        ss_local.setmOption(localList);
    }

    @Override
    public void setDefaultZone(SearchableSpinner ss_zone) {
        MD_Site_Zone zone = siteZoneDao.getByString(
                new MD_Site_Zone_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
                        ToolBox_Con.getPreference_Zone_Code(context)
                ).toSqlQuery()
        );
        //
        if (zone != null) {
            ToolBox_Inf.setSSmValue(
                    ss_zone,
                    String.valueOf(zone.getZone_code()),
                    zone.getZone_id(),
                    zone.getZone_desc(),
                    true
            );
        } else {
            //MSG DE ERRO ?

        }

    }

    @Override
    public String getInboundPendencies() {
        HMAux hmAux = inboundDao.getByStringHM(
                new IO_Inbound_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        if(hmAux != null && hmAux.hasConsistentValue(IO_Inbound_Sql_001.PENDENCY_QTY)){
            return hmAux.get(IO_Inbound_Sql_001.PENDENCY_QTY);
        }else{
            //Msg informando erro?

        }
        //
        return "0";
    }

    @Override
    public void executeInboundSearch(String zone_code, String local_code, String inbound_id, String invoince) {
        mView.setWsProcess(WS_IO_Inbound_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_inbound_search_ttl"),
                hmAux_Trans.get("dialog_inbound_search_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_IO_Inbound_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
        bundle.putString(MD_Site_Zone_LocalDao.ZONE_CODE,zone_code);
        bundle.putString(MD_Site_Zone_LocalDao.LOCAL_CODE,local_code);
        bundle.putString(WS_IO_Inbound_Search.KEY_CODE_ID,inbound_id);
        bundle.putString(IO_InboundDao.INVOICE_NUMBER,invoince);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public boolean checkSearchParamFilled(SearchableSpinner ss_zone, SearchableSpinner ss_local, MKEditTextNM mket_inbound, MKEditTextNM mket_invoice) {
        //
        if(ss_zone.getmValue() != null && ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)){
            return true;
        }

        if(ss_local.getmValue() != null && ss_local.getmValue().hasConsistentValue(SearchableSpinner.CODE)){
            return true;
        }

        if(!mket_inbound.getText().toString().trim().isEmpty()){
            return true;
        }

        if(!mket_invoice.getText().toString().trim().isEmpty()){
            return true;
        }
        //
        return false;
    }

    @Override
    public void processSearchReturn(String searchRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            T_IO_Inbound_Search_Rec rec = gson.fromJson(
                    searchRet,
                    T_IO_Inbound_Search_Rec.class
            );
            //
            if (rec.getRecord() != null && rec.getRecord().size() > 0) {
                Bundle bundle = new Bundle();

                bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, rec.getRecord_count());
                bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, rec.getRecord_page());
                bundle.putSerializable(Constant.MAIN_WS_LIST_VALUES, rec.getRecord());
                //
                mView.callAct057(bundle);

            }else{
                mView.showAlert(
                        hmAux_Trans.get("alert_no_inbound_found_ttl"),
                        hmAux_Trans.get("alert_no_inbound_found_msg")
                );
            }

        }catch (Exception e){
            mView.showAlert(
                    hmAux_Trans.get("alert_error_on_processing_return_ttl"),
                    hmAux_Trans.get("alert_error_on_processing_return_msg")
            );
            //Gerar Exception ?!
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct051();
    }
}
