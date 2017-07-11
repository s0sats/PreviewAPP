package com.namoadigital.prj001.ui.act026;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.SM_SO;

import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act026_Main_View {

    void loadSOList(List<SM_SO> soList);

    void callAct012(Context context);

    void callAct021(Context context);

    void callAct027(Context context, Bundle bundle);
}
