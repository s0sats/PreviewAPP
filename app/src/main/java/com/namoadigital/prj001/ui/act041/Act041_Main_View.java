package com.namoadigital.prj001.ui.act041;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by d.luche on 22/06/2017.
 */

public interface Act041_Main_View {

    void loadGroups_Products(List<HMAux> groups_products);

    void callAct040(Context context, String product_code);
}
