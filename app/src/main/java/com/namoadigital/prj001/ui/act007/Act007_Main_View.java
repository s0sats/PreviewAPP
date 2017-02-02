package com.namoadigital.prj001.ui.act007;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act007_Main_View {

    void loadGroups_Products(List<HMAux> groups_products);

    void callAct006(Context context);

    void callAct008(Context context, String product_code);

}
