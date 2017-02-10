package com.namoadigital.prj001.ui.act013;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act013_Main_View {

    void loadPendencies(List<HMAux> pendencies);

    void callAct011(Context context);

    void callAct012(Context context);


}
