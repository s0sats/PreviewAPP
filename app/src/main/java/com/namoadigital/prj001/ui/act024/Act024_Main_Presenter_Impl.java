package com.namoadigital.prj001.ui.act024;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.SM_SO;

import java.util.ArrayList;

/**
 * Created by d.luche on 28/06/2017.
 */

public class Act024_Main_Presenter_Impl implements Act024_Main_Presenter {


    private Context context;
    private Act024_Main_View mView;
    private HMAux hmAux_Trans;


    public Act024_Main_Presenter_Impl(Context context, Act024_Main_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void getSoHeaderList(String so_list) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ArrayList<SM_SO> sos = gson.fromJson(
                so_list,
                new TypeToken<ArrayList<SM_SO>>() {
                }.getType());
        //
        if(sos.size() == 0){

        }
        //
        mView.loadSoHeaders(sos);

    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
