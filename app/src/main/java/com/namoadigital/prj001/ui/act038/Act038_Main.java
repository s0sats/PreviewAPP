package com.namoadigital.prj001.ui.act038;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act038_Main extends Base_Activity implements Act038_Main_View {

    private Act038_Main_Presenter_Impl mPresenter;

    private Bundle bundle;

    private int backAction;
    private String requestingAct;

    private GE_Custom_Form_Ap mGe_custom_form_ap;
    private GE_Custom_Form_ApDao mGe_custom_form_apDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act037_main);

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
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT038
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act038_title");
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
        //
        mPresenter =
                new Act038_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        new GE_Custom_Form_ApDao(
                                context
                        )
                );
        mPresenter.getloadAP();
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mGe_custom_form_apDao = new GE_Custom_Form_ApDao(context);
            //
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT, "");
            String mCustomer_Code = bundle.getString(GE_Custom_Form_ApDao.CUSTOMER_CODE);
            String mCustom_Form_Type = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE);
            String mCustom_Form_Code = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE);
            String mCustom_Form_Version = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION);
            String mCustom_Form_Data = bundle.getString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA);
            String mAp_Code = bundle.getString(GE_Custom_Form_ApDao.AP_CODE);
            //
            mGe_custom_form_ap = mGe_custom_form_apDao.getByString(
                    new GE_Custom_Form_Ap_Sql_005(
                            mCustomer_Code,
                            mCustom_Form_Type,
                            mCustom_Form_Code,
                            mCustom_Form_Version,
                            mCustom_Form_Data,
                            mAp_Code,
                            GE_Custom_Form_Ap_Sql_005.RETURN_SQL_OBJ
                    ).toSqlQuery()
            );

            String tt = "Hugo";


        } else {
        }
    }

    @Override
    public void loadAP(HMAux aps) {
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT038;
        mAct_Title = Constant.ACT038 + "_" + "title";
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

    private void initActions() {
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();

        Log.d("DDDD", "Passei por aqui!!!");

        callAct005(context);
    }

    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
