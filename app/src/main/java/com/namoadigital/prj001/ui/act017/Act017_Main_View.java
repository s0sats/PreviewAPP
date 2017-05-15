package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public interface Act017_Main_View {

    void loadSchedules(List<HMAux> schedules);

    void showMsg(String type, HMAux item);

    void callAct008(Context context, Bundle bundle);

    void callAct016(Context context);

    void callAct011(Context context, Bundle bundle);
}
