package com.namoadigital.prj001.ui.act024;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.ArrayList;

/**
 * Created by d.luche on 28/06/2017.
 */

public interface Act024_Main_View {

    void loadSoHeaders(ArrayList<HMAux> so_list);

    void callAct005(Context context);

    void callAct026(Context context);

    void callAct027(Context context, HMAux so);

    void showPD();

    //void showMsg(String process);

}
