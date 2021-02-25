package com.namoadigital.prj001.ui.act042;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act042_Main extends Base_Activity implements Act042_Main_View{

    private Act042_Main_Presenter_Impl mPresenter;
    private ListView lv_sos;
    private SO_Header_Adapter mAdapter;
    private MKEditTextNM mket_filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act042_main);

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
                Constant.ACT042
        );

        loadTranslation();

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act042_title");
        transList.add("filter_hint");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void initVars() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //
        recoverIntentsInfo();
        //
        mPresenter = new Act042_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans
        );
        //
        lv_sos = (ListView) findViewById(R.id.act042_lv_sos);
        //
        mket_filter = (MKEditTextNM) findViewById(R.id.act042_mket_filter_desc);
        mket_filter.setHint(hmAux_Trans.get("filter_hint"));
        //
        mPresenter.getSoExpressList();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            //requesting_act = bundle.getString(Constant.MAIN_REQUESTING_ACT,Constant.ACT036);
        } else {
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT042;
        mAct_Title = Constant.ACT042 + "_" + "title";
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


    private void initActions() {
        //
        mket_filter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                applySearchFilter();
            }
        });
    }

    private void applySearchFilter() {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(mket_filter.getText().toString().trim());
        }
    }

    @Override
    public void loadSoExpress(ArrayList<HMAux> so_express_list) {

        mAdapter = new SO_Header_Adapter(
                context,
                so_express_list,
                SO_Header_Adapter.CONFIG_TYPE_EXIBITION_FULL,
                R.layout.so_header_cell,
                R.layout.so_header_cell
        );
        //
        lv_sos.setAdapter(mAdapter);
        //
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
