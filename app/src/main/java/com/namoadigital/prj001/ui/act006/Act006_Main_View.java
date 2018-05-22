package com.namoadigital.prj001.ui.act006;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act006_Main_View {

    void callAct005(Context context);

    void callAct007(Context context);

    void callAct013(Context context);

    void callAct020(Context context, Bundle bundle);

    void setPendenciesQty(int qty);

    void showMsg(String title, String msg);

    void showPD(String title, String msg);
}
