package com.namoadigital.prj001.ui.act015;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public interface Act015_Main_View {

    void loadSentData(List<HMAux> sentData);

    void callAct011(Context context , Bundle bundle);

    void callAct014(Context context);

    void showMsg(String ttl, String msg);
}
