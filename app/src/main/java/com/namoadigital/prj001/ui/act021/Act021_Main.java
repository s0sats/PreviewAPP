package com.namoadigital.prj001.ui.act021;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main extends Base_Activity implements Act021_Main_View {

    private Act021_Main_Presenter mPresenter;
    private Button btn_load;
    private Button btn_express;
    private Button btn_pendencies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act020_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        iniUIFooter();
        //
        initVars();
        //
        initActions();




    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT021
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act021_title");
        transList.add("btn_load_so");
        transList.add("btn_express_so");
        transList.add("btn_pendencies_so");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT021;
        mAct_Title = Constant.ACT021 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }

    private void initVars() {

        mPresenter = new Act021_Main_Presenter_Impl();
        //
        btn_load = (Button) findViewById(R.id.act021_btn_load);
        btn_load.setTag("btn_load_so");
        views.add(btn_load);
        //
        btn_express = (Button) findViewById(R.id.act021_btn_express);
        btn_express.setTag("btn_express_so");
        views.add(btn_express);
        //
        btn_pendencies = (Button) findViewById(R.id.act021_btn_pendencies);
        btn_express.setTag("btn_pendencies_so");
        views.add(btn_pendencies);

    }

    private void initActions() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
