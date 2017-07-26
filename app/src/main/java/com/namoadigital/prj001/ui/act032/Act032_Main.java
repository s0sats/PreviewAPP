package com.namoadigital.prj001.ui.act032;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.SignaTure_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoa_digital.namoa_library.util.ConstantBase.CACHE_PATH_PHOTO;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act032_Main extends Base_Activity implements Act032_Main_View {

    private Bundle bundle;
    private String mSignature;
    private String mStatus;
    private String mClientType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act032_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();

    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT032
        );

        loadTranslation();

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act032_title");
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
        recoverIntentsInfo();
        //Teste Ini
        mSignature = "sm_so_666.jpg";
        callSignature();
        //Teste Fim


    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(Constant.SO_PARAM_STATUS)) {
                mStatus = bundle.getString(Constant.SO_PARAM_STATUS, "");
                mClientType = bundle.getString(Constant.SO_PARAM_CLIENT_TYPE, "");

            } else {
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }

        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }

    public void callSignature() {
        try {
            Bundle bundleN = new Bundle();
            bundleN.putInt(ConstantBase.PID, -1);
            bundleN.putInt(ConstantBase.PTYPE, 0);
            bundleN.putString(ConstantBase.PPATH, CACHE_PATH_PHOTO + "/" + mSignature);

            Intent mIntent = new Intent(context, SignaTure_Activity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundleN);

            context.startActivity(mIntent);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        }

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT032;
        mAct_Title = Constant.ACT032 + "_" + "title";
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

        //Aplica informações do rodapé -fim

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
