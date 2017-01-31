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

    String[] menuDesc = {
            "lbl_checklist",
            "lbl_pending_data",
            "lbl_send_data",
            "lbl_sync_data",
            "lbl_close_app"
    };

    String[] icon ={
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload),
            String.valueOf(R.drawable.cloud_upload)
    };

    @Override
    public void getMenuItens(HMAux hmAux_Trans) {
        List<HMAux> menuList = new ArrayList<>();

        for (int i = 0; i < menuDesc.length;i++ ){
            HMAux Aux = new HMAux();
            Aux.put(Act005_Main.MENU_ICON, icon[i]);
            Aux.put(Act005_Main.MENU_DESC,menuDesc[i]);
            menuList.add(Aux);

        }
        mView.loadMenu(menuList);
    }



}
