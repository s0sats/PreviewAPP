package com.namoadigital.prj001.ui.act009;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act009_Main_View {

    void loadForm_Types(List<HMAux> form_types);

    void callAct008(Context context);

    void callAct010(Context context);

}
