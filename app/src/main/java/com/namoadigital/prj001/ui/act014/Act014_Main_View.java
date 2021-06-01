package com.namoadigital.prj001.ui.act014;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public interface Act014_Main_View {

    void loadSentData(List<HMAux> sent_datas);

    void callAct005(Context context);

    void callAct015(Context context);

    void callAct032(Context context);

    void callAct039(Context context);

    void callAct055(Context context, Bundle bundle);

    void showMsg();

    void callAct057(Context context);

    void callAct066(Context context);

    void callAct069(Context context);

    void callAct084(Context context);
}
