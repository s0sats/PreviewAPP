package com.namoadigital.prj001.ui.act003;


import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 17/01/17.
 */

public interface Act003_Main_View {

    void loadSites(List<HMAux> sites);

    void callAct004(Context context);

    void callAct002(Context context);
}
