package com.namoadigital.prj001.ui.act019;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public interface Act019_Main_View {

    void loadMessages(List<HMAux> messages);

    void callAct019(Context context, Bundle bundle);

    void callAct014(Context context);

}
