package com.namoadigital.prj001.ui.act073;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act073_Main extends Base_Activity implements Act073_Main_Contract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act068_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();

        initAction();

    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.ACT068
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act068_title");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        );
    }

    private void initVars() {
        /**
         *
         *
         * POR HORA ESSA TELA NÃO TERÁ IMPLEMENTAÇÃO ENTÃO ELÁ SÓ CHAMARA A PROXIMA TELA
         *
         *
         */
        Intent intent = new Intent(context, Act069_Main.class);
        startActivity(intent);
        finish();

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT068;
        mAct_Title = Constant.ACT068 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }


    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initAction() {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //mPresenter.onBackPressedClicked();
        Intent intent = new Intent(context, Act005_Main.class);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
