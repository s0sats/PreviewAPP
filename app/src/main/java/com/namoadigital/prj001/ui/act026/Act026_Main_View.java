package com.namoadigital.prj001.ui.act026;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act026_Main_View {

    void loadSOList(List<HMAux> soList);

    void callAct012(Context context);

    void callAct021(Context context);

    void callAct027(Context context, Bundle bundle);
}
