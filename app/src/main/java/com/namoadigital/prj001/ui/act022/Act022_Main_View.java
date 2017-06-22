package com.namoadigital.prj001.ui.act022;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act022_Main_View {

    void loadGroups_Products(List<HMAux> groups_products);

    void callAct006(Context context);

    void callAct008(Context context, String product_code);

    void callAct021(Context context);

    void callAct023(Context context, String product_code);
}
