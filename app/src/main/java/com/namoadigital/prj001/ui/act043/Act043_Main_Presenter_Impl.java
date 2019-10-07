package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Params_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Rec;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

public class Act043_Main_Presenter_Impl implements Act043_Main_Presenter {

    private Context context;
    private Act043_Main_View mView;
    private HMAux hmAux_Trans;
    private SM_SODao smSoDao;
    private String fileName = "";

    public Act043_Main_Presenter_Impl(Context context, Act043_Main mView, HMAux hmAux_Trans, SM_SODao smSoDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.smSoDao = smSoDao;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean jsonFileExists() {
        File file = new File(Constant.TOKEN_PATH, fileName);
        return file.exists();
    }

    @Override
    public ArrayList<TSO_Service_Search_Obj> processServiceList() {
        File file = new File(Constant.TOKEN_PATH, fileName);
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSO_Service_Search_Rec rec = null;
        ArrayList<TSO_Service_Search_Obj> serviceList = new ArrayList<>();
        //
        if(file.exists()) {
            try {
                rec = gson.fromJson(
                    ToolBox_Inf.getContents(file),
                    TSO_Service_Search_Rec.class
                );
                //
                if (rec != null) {
                    serviceList = rec.getData();
                }

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
        //
        return serviceList;
    }

    @Override
    public ArrayList<MD_Partner> getPackServicePartnerList() {
        File file = new File(Constant.TOKEN_PATH, fileName);
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSO_Service_Search_Rec rec = null;
        ArrayList<MD_Partner> partner_list = new ArrayList<>();
        //
        if(file.exists()) {
            try {
                rec = gson.fromJson(
                    ToolBox_Inf.getContents(file),
                    TSO_Service_Search_Rec.class
                );
                //
                if (rec != null) {
                    partner_list = rec.getPartner_list();
                }
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
        }
        //
        return partner_list;
    }

    @Override
    public ArrayList<TSO_Service_Search_Obj> prepareListToAdapter(ArrayList<TSO_Service_Search_Obj> packServiceList) {
        for (TSO_Service_Search_Obj obj : packServiceList) {
            Double totPrice = null;
            boolean reportMissingValue = false;
            //
            if (Act043_Main.TYPE_PS_PACK.equals(obj.getType_ps())) {
                for (TSO_Service_Search_Detail_Obj innerService : obj.getService_list()) {
                    if (innerService.getPrice() != null) {
                        totPrice = totPrice == null ? innerService.getPrice() : totPrice + innerService.getPrice();
                    } else {
                        reportMissingValue = true;
                    }
                }
                obj.setPrice(totPrice);
                obj.setNullPrice(reportMissingValue);
            } else {
                obj.setNullPrice(obj.getPrice() == null);
            }
        }
        //
        return packServiceList;
    }

    /**
     * Metodo que gera lista de sites formatada para o Option do spinner de detalhes do serviço.
     * @param rawSiteZone - Lista de Site Zone no formato enviado pelo server
     * @return - Lista de site com code, desc e partner default
     */
    @Override
    public ArrayList<HMAux> generateSiteOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone){
        ArrayList<HMAux> siteList = new ArrayList<>();
        if(rawSiteZone != null){
            for (TSO_Service_Search_Detail_Params_Obj siteZone : rawSiteZone) {
                HMAux hmAux = new HMAux();
                if(!isSiteInList(siteList,siteZone.getSite_code())){
                    hmAux.put(SearchableSpinner.CODE, String.valueOf(siteZone.getSite_code()));
                    hmAux.put(SearchableSpinner.DESCRIPTION, siteZone.getSite_desc());
                    hmAux.put(MD_PartnerDao.PARTNER_CODE, String.valueOf(siteZone.getPartner_code()));
                    siteList.add(hmAux);
                }
            }
        }
        //
        return siteList;
    }

    /**
     * Metodo que verifica se o site ja foi adicionado a lista de hmAux
     * @param siteList
     * @param site_code
     * @return
     */
    private boolean isSiteInList(ArrayList<HMAux> siteList, Integer site_code) {
        for (HMAux hmAux : siteList) {
            if( hmAux != null
                && hmAux.hasConsistentValue(SearchableSpinner.CODE)
                && hmAux.get(SearchableSpinner.CODE).equals(String.valueOf(site_code))
            ){
                return true;
            }
        }
        //
        return false;
    }

    /**
     * Metodo que gera lista de zone formatada para o Option do spinner de detalhes do serviço.
     * @param rawSiteZone - Lista de Site Zone no formato enviado pelo server
     * @return - Lista de zonas com code, desc e site code
     */
    @Override
    public ArrayList<HMAux> generateSiteZoneOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone){
        ArrayList<HMAux> siteZoneList = new ArrayList<>();
        if(rawSiteZone != null){
            for (TSO_Service_Search_Detail_Params_Obj siteZone : rawSiteZone) {
                HMAux hmAux = new HMAux();
                if(!isSiteZoneInList(siteZoneList,siteZone.getSite_code(),siteZone.getZone_code())){
                    hmAux.put(SearchableSpinner.CODE, String.valueOf(siteZone.getZone_code()));
                    hmAux.put(SearchableSpinner.DESCRIPTION, siteZone.getZone_desc());
                    hmAux.put(MD_Site_ZoneDao.SITE_CODE,String.valueOf(siteZone.getSite_code()));
                    siteZoneList.add(hmAux);
                }
            }
        }
        //
        return siteZoneList;
    }

    private boolean isSiteZoneInList(ArrayList<HMAux> siteList, Integer site_code,Integer zone_code) {
        for (HMAux hmAux : siteList) {
            if( hmAux != null
                && hmAux.hasConsistentValue(MD_Site_ZoneDao.SITE_CODE)
                && hmAux.hasConsistentValue(SearchableSpinner.CODE)
                && hmAux.get(MD_Site_ZoneDao.SITE_CODE).equals(String.valueOf(site_code))
                && hmAux.get(SearchableSpinner.CODE).equals(String.valueOf(zone_code))
            ){
                return true;
            }
        }
        //
        return false;
    }

    @Override
    public void onBackPressedClicked() {
        switch (mView.getCurrentFrag()){
            case Act043_Main.SELECTION_FRAG_SERVICE_LIST:
                if(mView.hasItemAdded()){
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_discard_services_ttl"),
                            hmAux_Trans.get("alert_discard_services_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.setFragByTag(Act043_Main.SELECTION_FRAG_PREVIEW);
                                }
                            },
                            1
                    );
                }else{
                    mView.setFragByTag(Act043_Main.SELECTION_FRAG_PREVIEW);
                }
                //
                break;
            case Act043_Main.SELECTION_FRAG_PREVIEW:
            default:
                mView.callAct027(context);
                break;
        }
    }

    public void deleteJsonFile() {
        File file = new File(Constant.TOKEN_PATH,fileName);
        file.delete();
    }
}
