package com.namoadigital.prj001.ui.act006;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act006_Main_View {

    void loadCheckListOpcs(List<HMAux> opcs);

    void callAct005(Context context);

    void callAct007(Context context);

    void callAct012(Context context);

}
