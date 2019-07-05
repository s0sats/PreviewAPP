package com.namoadigital.prj001.ui.act061.frag_header;

import android.content.Context;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act061_Frag_Header_Presenter implements Act061_Frag_Header_Contract.I_Presenter  {

    private Context context;
    private Act061_Frag_Header_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private MD_Site_ZoneDao zoneDao;
    private MD_Site_Zone_LocalDao localDao;

    public Act061_Frag_Header_Presenter(Context context, Act061_Frag_Header_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.zoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.localDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);

    }


    @Override
    public HMAux getZoneDbValue(ArrayList<HMAux> zoneOptions, int zone_code) {
        HMAux zoneDb = new HMAux();
        //
        for(HMAux aux :zoneOptions) {
            if(aux.hasConsistentValue(SearchableSpinner.CODE)
               && aux.get(SearchableSpinner.CODE).equals(String.valueOf(zone_code))
            ){
                zoneDb = aux;
                break;
            }
        }
        //
        if(zoneDb.size() == 0){
            zoneDb.put(SearchableSpinner.CODE, String.valueOf(zone_code));
            zoneDb.put(SearchableSpinner.ID, String.valueOf(zone_code));
            zoneDb.put(SearchableSpinner.DESCRIPTION, String.valueOf(zone_code));
        }

        return zoneDb;
    }

    @Override
    public HMAux getLocalDbValue(ArrayList<HMAux> zoneOptions,int zone_code, int local_code) {
        HMAux localDb = new HMAux();
        //
        for(HMAux aux :zoneOptions) {
            if(
                aux.hasConsistentValue(SearchableSpinner.CODE)
                && aux.hasConsistentValue(MD_Site_Zone_LocalDao.ZONE_CODE)
                && aux.get(MD_Site_Zone_LocalDao.ZONE_CODE).equals(String.valueOf(zone_code))
                && aux.get(SearchableSpinner.CODE).equals(String.valueOf(local_code))
            ){
                localDb = aux;
                break;
            }
        }
        //
        if(localDb.size() == 0){
            localDb.put(SearchableSpinner.CODE, String.valueOf(zone_code));
            localDb.put(SearchableSpinner.ID, String.valueOf(zone_code));
            localDb.put(SearchableSpinner.DESCRIPTION, String.valueOf(zone_code));
            localDb.put(MD_Site_Zone_LocalDao.ZONE_CODE, String.valueOf(zone_code));
            localDb.put(MD_Site_Zone_LocalDao.SITE_CODE, String.valueOf(zone_code));

        }

        return localDb;
    }

    @Override
    public ArrayList<HMAux> getZonesOptions() {
        //
        ArrayList<HMAux> zoneList = (ArrayList<HMAux>) zoneDao.query_HM(
            new MD_Site_Zone_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                ToolBox_Con.getPreference_Site_Code(context)
            ).toSqlQuery()
        );
        //
        return zoneList;
    }

    @Override
    public ArrayList<HMAux> getLocalsOptions(String zone_code) {
        //
        ArrayList<HMAux> localList = (ArrayList<HMAux>) localDao.query_HM(
            new MD_Site_Zone_Local_Sql_SS_002(
                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                    ToolBox_Con.getPreference_Site_Code(context),
                    String.valueOf(zone_code)
            ).toSqlQuery()
        );
        //
        return localList;
    }

    @Override
    public boolean hasConfirmedItem(IO_Inbound mInbound) {
        if(mInbound.getItems() != null && mInbound.getItems().size() > 0 ){
            for(IO_Inbound_Item inboundItem : mInbound.getItems()){
                if( inboundItem.getStatus().equals(ConstantBaseApp.SYS_STATUS_PUT_AWAY)
                    || inboundItem.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)
                    || inboundItem.getStatus().equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
                ){
                    return true;
                }
            }
        }
        //
        return false;
    }

    /**
     * Verifica se todos os item foram finalizados.
     * @param mInbound - Obj inbound carregado.
     * @return - True se todos os itens estiverem finalizados.
     */
    @Override
    public boolean allItemsDone(IO_Inbound mInbound) {
        int doneItems = 0;
        if(mInbound.getItems() != null && mInbound.getItems().size() > 0 ){
            //
            for(IO_Inbound_Item inboundItem : mInbound.getItems()){
                if( inboundItem.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE) ){
                    doneItems++;
                }
            }
            //
            return doneItems == mInbound.getItems().size();
        }
        //
        return false;
    }

    /**
     * Gera lista de hmAux baseado nos status passados.
     *
     * @param status - Lista com status que devem compor o mOption
     * @return - Lista de status para spinner
     */
    @Override
    public ArrayList<HMAux> generateStatusList(ArrayList<String> status) {
        ArrayList<HMAux> statusList = new ArrayList<>();
        for (String s : status ){
            HMAux hmAux = new HMAux();
            hmAux.put(SearchableSpinner.CODE, s);
            hmAux.put(SearchableSpinner.ID, s);
            hmAux.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(s));
            //
            statusList.add(hmAux);
        }
        return statusList;
    }
}
