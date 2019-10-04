package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Rec;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act043_Main_Presenter_Impl implements Act043_Main_Presenter {

    private Context context;
    private Act043_Main_View mView;
    private HMAux hmAux_Trans;
    private SM_SODao smSoDao;

    public Act043_Main_Presenter_Impl(Context context, Act043_Main mView, HMAux hmAux_Trans, SM_SODao smSoDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.smSoDao = smSoDao;
    }

    @Override
    public ArrayList<TSO_Service_Search_Obj> processServiceList(String serviceSearchResult) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSO_Service_Search_Rec rec = null;
        ArrayList<TSO_Service_Search_Obj> serviceList = new ArrayList<>();
        try {
            rec = gson.fromJson(
                serviceSearchResult,
                TSO_Service_Search_Rec.class
            );
            //
            if(rec != null) {
                serviceList = rec.getData();
            }

        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
        //
        return serviceList;
    }

    @Override
    public ArrayList<MD_Partner> getPackServicePartnerList(String serviceSearchResult) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSO_Service_Search_Rec rec = null;
        ArrayList<MD_Partner> partner_list = new ArrayList<>();
        try{
            rec = gson.fromJson(
                serviceSearchResult,
                TSO_Service_Search_Rec.class
            );
            //
            if(rec != null){
                partner_list = rec.getPartner_list();
            }
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
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
}
