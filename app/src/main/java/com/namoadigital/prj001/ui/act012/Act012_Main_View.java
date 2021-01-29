package com.namoadigital.prj001.ui.act012;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act012_Main_View {

    void loadPendencies(List<HMAux> pendencies);

    void callAct005(Context context);

    void callAct013(Context context);

    void callAct026(Context context);

    void callAct037(Context context);

    void callAct042(Context context);

    void callAct055(Context context);

    void showMsg();

    void callAct057(Context context);

    void callAct066(Context context);

    void callAct069(Context context);

    void callAct076();
}
