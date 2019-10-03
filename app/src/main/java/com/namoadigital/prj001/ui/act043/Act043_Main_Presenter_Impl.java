package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
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
    public ArrayList<TSO_Service_Search_Obj> processServiceList(String ws_list_return) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<TSO_Service_Search_Obj> serviceList = new ArrayList<>();
        try {
            //
            serviceList = gson.fromJson(
                ws_list_return,
                new TypeToken<ArrayList<TSO_Service_Search_Obj>>() {
                }.getType()
            );
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            e.printStackTrace();
        }
        //
        return serviceList;
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
