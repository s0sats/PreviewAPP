package com.namoadigital.prj001.ui.act042;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act042SOExpressAdapter;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.ui.act040.Act040_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act042_Main extends Base_Activity {

//    private Act042_Main_Presenter_Impl mPresenter;
    private RecyclerView rv_sos;
    private Act042SOExpressAdapter mAdapter;
    private MKEditTextNM mket_filter;
    private Bundle bundle;
    private TextView tv_placeholder;
    private ProgressBar pb_list;
    private ViewFlipper vf_main;


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
        transList.add("alert_get_express_so_error");

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
        Act042MainViewModelFactory viewModelFactory = new Act042MainViewModelFactory(
                ToolBox_Con.getPreference_Customer_Code(context),
                new SO_Pack_Express_LocalDao(context)
        );

        Act042MainViewModel viewModel = new ViewModelProvider(this, viewModelFactory)
                .get(Act042MainViewModel.class);
        //
        rv_sos = findViewById(R.id.act042_rv_sos);
        pb_list = findViewById(R.id.act042_pb_list);
        tv_placeholder = findViewById(R.id.act042_tv_placeholder);
        vf_main = findViewById(R.id.act042_vf_main);
        //
        vf_main.setDisplayedChild(0);
        //
        mket_filter = findViewById(R.id.act042_mket_filter_desc);
        mket_filter.setHint(hmAux_Trans.get("filter_hint"));
        tv_placeholder.setText(hmAux_Trans.get("alert_get_express_so_error"));
        //
        viewModel.getSo_express_list().observe(this, new Observer<ArrayList<SO_Pack_Express_Local>>() {
            @Override
            public void onChanged(ArrayList<SO_Pack_Express_Local> so_pack_express_locals) {

                if(so_pack_express_locals != null) {
                    vf_main.setDisplayedChild(1);
                    loadSoExpress(so_pack_express_locals);
                }else{
                    vf_main.setDisplayedChild(2);
                }
            }
        });
        //
        viewModel.getData();
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
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
                applySearchFilter(s.trim());
            }
        });
    }

    private void applySearchFilter(String textFilter) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(textFilter);
        }
    }

    public void loadSoExpress(ArrayList<SO_Pack_Express_Local> so_express_list) {
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rv_sos.setLayoutManager(linearLayoutManager);
        //
        mAdapter = new Act042SOExpressAdapter(
                so_express_list
        );
        //
        rv_sos.setAdapter(mAdapter);
        //
    }

    public void callAct040() {
        Intent mIntent = new Intent(context, Act040_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        String requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT);

        if(ConstantBaseApp.ACT014.equals(requestingAct)) {
            callAct014();
        }else{
            callAct040();
        }
    }

    private void callAct014() {
        Intent mIntent = new Intent(context, Act014_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }
}
