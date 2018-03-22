package com.namoadigital.prj001.ui.act040;

import android.content.Context;

import com.namoadigital.prj001.model.SO_Pack_Express;

/**
 * Created by d.luche on 09/03/2018.
 */

public interface Act040_Main_View {

    void loadSO_Pack_Express(SO_Pack_Express so_pack_express);

    void callAct005(Context context);

    void callAct041(Context context);

    void jumpToOne();
}
