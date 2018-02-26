package com.namoadigital.prj001.ui.act037;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act037_Main_View {


    void loadAPs(ArrayList<HMAux> aps);

    void callAct005(Context context);

    void callAct038(Context context, HMAux hmAux);


}
