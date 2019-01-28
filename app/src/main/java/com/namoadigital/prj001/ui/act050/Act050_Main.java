package com.namoadigital.prj001.ui.act050;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act050_Main extends Base_Activity_Frag {

    public static final String FAVORITE_LIST_FRAGMENT = "Favorite_List_Fragment";
    private Bundle bundle;
    private Act050_Main_Contract.I_Presenter mPresenter;
    private FragmentManager fm;
    private HMAux hmAux_Trans_Frag;
    private String mResource_Code_Frag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act050_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();


    }

    private void initFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.act050_frg_placeholder,FavoriteFragment.newInstance(1), FAVORITE_LIST_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT050
        );
        //
        mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_FAVORITE_LIST
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initFragment();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act050_title");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        List<String> dummie = new ArrayList<>();
        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                "",
                ToolBox_Con.getPreference_Translate_Code(context),
                //transListFrag
                FavoriteFragment.getFragTranslationsVars()
        );
    }

    private void initVars() {

    }

    private void iniUIFooter() {

    }

    private void initActions() {

    }
}
