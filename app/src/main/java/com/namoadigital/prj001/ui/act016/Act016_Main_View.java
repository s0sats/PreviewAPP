package com.namoadigital.prj001.ui.act016;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 13/04/2017.
 */

public interface Act016_Main_View {

    void loadSchedule(List<HMAux> scheduleData);

    void callAct017(Bundle bundle);

    void callAct046();
}
