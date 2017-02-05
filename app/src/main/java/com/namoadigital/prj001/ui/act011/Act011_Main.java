package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.ui.act004.Act004_Main_View;
import com.namoadigital.prj001.ui.act009.Act009_Main_Presenter;
import com.namoadigital.prj001.ui.act009.Act009_Main_Presenter_Impl;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main extends Base_Activity implements Act011_Main_View {

    private Context context;
    private Act011_Main_Presenter mPresenter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act011_main);

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

    private void iniSetup() {
        context = getBaseContext();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT009
        );

        loadTranslation();
    }

    private void loadTranslation() {
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context)
        );
    }

    private void initVars() {
//        mPresenter = new Act009_Main_Presenter_Impl(
//                context,
//                this,
//                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
//                new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
//        );

//        lv_form_types = (ListView) findViewById(R.id.act009_lv_form_types);

//        btn_back = (BootstrapButton) findViewById(R.id.act007_btn_back);

        recuperaGetIntents();

//        mPresenter.setAdapterData(0L, "");

    }

    private void recuperaGetIntents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//            currentIndex = Long.parseLong(bundle.getString(Constant.ACT007_CURRENTINDEX));
//            mket_product_search.setText(bundle.getString(Constant.ACT007_PRODUCT_SEARCH));
//            //
//            reloadStack(bundle.getString(Constant.ACT007_MSTACKVALUES));
        } else {
//            currentIndex = 0;
//            mket_product_search.setText("");
//            //
//            mStack.clear();
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT009;
        mAct_Title = Constant.ACT009 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    private void initActions() {

    }

}
