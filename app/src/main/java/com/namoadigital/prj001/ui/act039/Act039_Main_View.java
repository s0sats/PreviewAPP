package com.namoadigital.prj001.ui.act039;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act039_Main_View{

    void loadAPs(ArrayList<HMAux> aps);

    void callAct038(Context context, HMAux item);

}
