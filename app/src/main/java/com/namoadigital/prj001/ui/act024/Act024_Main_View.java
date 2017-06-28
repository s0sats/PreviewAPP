package com.namoadigital.prj001.ui.act024;

import android.content.Context;

import com.namoadigital.prj001.model.SM_SO;

import java.util.ArrayList;

/**
 * Created by d.luche on 28/06/2017.
 */

public interface Act024_Main_View {

    void loadSoHeaders(ArrayList<SM_SO> so_list);

    void callAct005(Context context);

}
