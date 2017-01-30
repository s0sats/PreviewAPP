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
    public void getMenuItens(HMAux hmAux_Trans) {
        List<HMAux> menuList = new ArrayList<>();
        HMAux Aux = new HMAux();
        String icon = String.valueOf(R.drawable.cloud_upload);

        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,hmAux_Trans.get("lbl_checklist"));
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,hmAux_Trans.get("lbl_pending_data"));
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,hmAux_Trans.get("lbl_send_data"));
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,hmAux_Trans.get("lbl_sync_data"));
        menuList.add(Aux);

        Aux = new HMAux();
        Aux.put(Act005_Main.MENU_ICON, icon);
        Aux.put(Act005_Main.MENU_DESC,hmAux_Trans.get("lbl_close_app"));
        menuList.add(Aux);

        mView.loadMenu(menuList);
    }
}
