package com.namoadigital.prj001.ui.act021;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by d.luche on 21/06/2017.
 */

public interface Act021_Main_View {

    void setPendencies(int qty);

    void callAct005(Context context);

    void callAct022(Context context);

    void callAct025(Context context, Bundle bundle);

    void callAct023(Context context, Bundle bundle);

    void callAct026(Context context);

    void showNewOptDialog();

    void showMsg(String ttl,String msg);

    void showPD(String ttl, String msg);

}
