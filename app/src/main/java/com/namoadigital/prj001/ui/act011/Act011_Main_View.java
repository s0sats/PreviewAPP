package com.namoadigital.prj001.ui.act011;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act011_Main_View {

    public void loadFragment_CF_Fields(List<HMAux> cf_fields, GE_Custom_Form_Data formData, String prefix);

    void callAct005(Context context);


}
