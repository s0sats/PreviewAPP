package com.namoadigital.prj001.ui.act005;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act005_Main_Presenter_Impl implements Act005_Main_Presenter {

    private Context context;
    private Act005_Main_View mView;
    private HMAux item;

    public Act005_Main_Presenter_Impl(Context context, Act005_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void getMenuItens() {
        List<HMAux> menuList = new ArrayList<>();
        HMAux Aux = new HMAux();
        String icon = String.valueOf(R.drawable.cloud_upload);

        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,"Checklist");
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,"Pending");
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,"Send Data");
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,"Sync");
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,"Sair");
        menuList.add(Aux);

        mView.loadMenu(menuList);
    }
}
