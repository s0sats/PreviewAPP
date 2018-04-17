package com.namoadigital.prj001.ui.act043;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

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
    public ArrayList<TSO_Service_Search_Obj> processServiceList(String ws_list_return) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<TSO_Service_Search_Obj> serviceList = new ArrayList<>();
        //
        serviceList = gson.fromJson(
                ws_list_return,
                new TypeToken<ArrayList<TSO_Service_Search_Obj>>() {
                }.getType()
        );
        //
        return serviceList;
    }

    @Override
    public void onBackPressedClicked() {
        switch (mView.getCurrentFrag()){
            case Act043_Main.SELECTION_FRAG_SERVICE_LIST:
                mView.setFragByTag(Act043_Main.SELECTION_FRAG_PREVIEW);
                break;
            case Act043_Main.SELECTION_FRAG_PREVIEW:
            default:
                mView.callAct027(context);
                break;
        }
    }
}
