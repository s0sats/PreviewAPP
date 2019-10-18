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
            calculateTotalPrice(obj,true);
        }
        //
        return packServiceList;
    }

    /**
     * Metodo publico que será chamado para recalcular o preço do pacote
     * @param packService - obj PackService
     */@Override
    public void calculateTotalPrice(TSO_Service_Search_Obj packService){
        calculateTotalPrice(packService,false);
    }

    /**
     * Metodo que calcula o preço total do pacote e seta preço referencia caso definePriceRef
     * for true.
     * Só dever se chamado como definePriceRef na chamada do close act
     * @param packService - obj PackSerice
     * @param definePriceRef - Se true, além de calcular o preço total, seta valores de referencias
     */
    private void calculateTotalPrice(TSO_Service_Search_Obj packService, boolean definePriceRef){
        Double totPrice = null;
        boolean reportMissingValue = false;
        //Independente do type,
        //Seta qtd 0 para 1
        packService.setQty(packService.getQty() == 0 ? 1 : packService.getQty());
        //
        if (Act043_Main.TYPE_PS_PACK.equals(packService.getType_ps())) {
            for (TSO_Service_Search_Detail_Obj innerService : packService.getService_list()) {
                //Se item não foi selecionado,ou seja esta inalterado pelo usr
                //seta preço de referencia.
                //Esse metodo deveria dar apenas uma vez, porem, caso não seja, essa
                //validalção evita que um preço alterado pelo usr passe por cima do original.
                if(definePriceRef) {
                    //Seta preço "original" no atributo price_ref
                    innerService.setPrice_ref(innerService.getPrice());
                }
                //Seta qtd 0 para 1
                innerService.setQty(packService.getQty() == 0 ? 1 : packService.getQty());
                //Calcula preço total, soma dos valores
                if (innerService.getPrice() != null) {
                    totPrice = totPrice == null ? innerService.getPrice() : totPrice + innerService.getPrice();
                } else {
                    reportMissingValue = true;
                }
            }
            //
            if(definePriceRef) {
                packService.setPrice_ref(totPrice);
            }
            packService.setPrice(totPrice);
            packService.setNullPrice(reportMissingValue);
        } else {
            //Se item não foi selecionado,ou seja esta inalterado pelo usr
            //seta preço de referencia.
            //Esse metodo deveria dar apenas uma vez, porem, caso não seja, essa
            //validalção evita que um preço alterado pelo usr passe por cima do original.
            packService.setPrice_ref( definePriceRef ? packService.getPrice() : packService.getPrice_ref());
            //Calcula preço total,pois price é sempre UNITÁRIO (price * qty)
            if(packService.getPrice() != null && packService.getQty() >= 1){
                //totPrice = packService.getPrice() * (packService.getQty() >= 1 ? packService.getQty() : 1);
                totPrice = packService.getPrice() * packService.getQty();
                packService.setPrice(totPrice);
            }
            packService.setPrice(packService.getPrice());
            //Seta flag que indica se valor do preço não esta definido.
            packService.setNullPrice(packService.getPrice() == null);
        }
    }

    @Override
    public void resetPackService(TSO_Service_Search_Obj packService) {
        if(packService != null){
            //Propriedades do "obj" pack, comum a ambos os types
            packService.setQty(1);
            packService.setPrice(packService.getPrice_ref());
            packService.setSite_code_selected(null);
            packService.setZone_code_selected(null);
            packService.setPartner_code_selected(null);
            packService.setComment(null);
            packService.setDetailed(false);
            packService.setSelected(false);
            packService.setNullPrice(false);
            //
            if(Act043_Main.TYPE_PS_PACK.equals(packService.getType_ps())){
                //Propriedades do service
                for (TSO_Service_Search_Detail_Obj innerService : packService.getService_list()) {
                    innerService.setPrice(innerService.getPrice_ref());
                    innerService.setSite_code_selected(null);
                    innerService.setSite_desc_selected(null);
                    innerService.setZone_code_selected(null);
                    innerService.setZone_desc_selected(null);
                    innerService.setPartner_code_selected(null);
                    innerService.setPartner_desc_selected(null);
                    innerService.setComment(null);
                }
            }
            //
            calculateTotalPrice(packService);
        }
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
                    hmAux.put(SearchableSpinner.ID, siteZone.getSite_id());
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
                    hmAux.put(SearchableSpinner.ID, siteZone.getZone_id());
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
            case Act043_Main.SELECTION_FRAG_PACKAGE_DETAIL_LIST:
                final TSO_Service_Search_Obj packDetailObj = mView.getPackDetailObj();
                //
                if(!packDetailObj.isSelected()){
                    mView.alertPackDetailRemoveConfirm(packDetailObj);
                } else{
                    mView.setFragByTag(Act043_Main.SELECTION_FRAG_SERVICE_LIST);
                }

                break;
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
        if(fileName != null && !fileName.isEmpty()) {
            File file = new File(Constant.TOKEN_PATH, fileName);
            //
            if(file.exists() && file.isFile()){
                file.delete();
            }
        }
    }
}
