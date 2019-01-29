package com.namoadigital.prj001.ui.act050;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.model.SO_Favorite_Response;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act050_Main extends Base_Activity_Frag implements Act050_Favorite_Fragment.OnListFragmentInteractionListener {

    public static final String FAVORITE_LIST_FRAGMENT = "Favorite_List_Fragment";
    private Bundle bundle;
    private Act050_Main_Contract.I_Presenter mPresenter;
    private FragmentManager fm;
    private HMAux hmAux_Trans_Frag;
    private String mResource_Code_Frag;
    private long mSerialCode;
    private long mProductCode;


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


    }

    private void initFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.act050_frg_placeholder, Act050_Favorite_Fragment.newInstance(1, mProductCode, mSerialCode), FAVORITE_LIST_FRAGMENT);
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

        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                "",
                ToolBox_Con.getPreference_Translate_Code(context),
                //transListFrag
                Act050_Favorite_Fragment.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        initFragment();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT050;
        mAct_Title = Constant.ACT050 + "_" + "title";
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
    public void onListFragmentInteraction(SO_Favorite_Item item) {

    }

    @Override
    public void onProgressDialogRequest(String title, String message, String labelCancel, String labelOk) {
        enableProgressDialog(
                title,
                message,
                labelCancel,
                labelOk
        );
    }
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        Gson gson = new GsonBuilder().serializeNulls().create();
        SO_Favorite_Response response = gson.fromJson(
                mLink,
                SO_Favorite_Response.class
        );
        Act050_Favorite_Fragment.populatedFavoritesList(response.getFavorite());
    }

    private void recoverIntentsInfo() {

        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(MD_Product_SerialDao.SERIAL_CODE)) {
                mProductCode = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE, 0);
                mSerialCode = bundle.getLong(MD_Product_SerialDao.SERIAL_CODE, 0);
            } else {
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }
        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }
}
