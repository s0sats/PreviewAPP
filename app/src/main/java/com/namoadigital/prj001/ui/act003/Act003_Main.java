package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 17/01/17.
 */

public class Act003_Main extends Base_Activity implements Act003_Main_View {

    private Context context;
    private TextView tv_customer_lbl;
    private TextView tv_customer_val;
    private ListView lv_sites;
    private Act003_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act003_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    private void iniSetup() {
        context = getBaseContext();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(context, mModule_Code, Constant.ACT003);
        //
        loadTranslation();
        //
    }

    public void callAct004(Context context) {
        Intent mIntent =  new Intent(context, Act004_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void initVars() {
        mPresenter = new Act003_Main_Presenter_Impl(context, this);
        //
        tv_customer_lbl = (TextView) findViewById(R.id.act003_tv_customer_lbl);
        tv_customer_val = (TextView) findViewById(R.id.act003_tv_customer_val);
        //
        lv_sites = (ListView) findViewById(R.id.act003_lv_sites);
        //
        if(mPresenter.checkPreferenceIsSet()){
                callAct004(context);
        }else{
            mPresenter.getSites(hmAux_Trans);
        }
    }

    private void callAct002(Context context) {
        ToolBox_Con.setPreference_Customer_Code(context,-1L);
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    private void initActions() {

        tv_customer_lbl.setText(hmAux_Trans.get("lbl_customer"));
        tv_customer_val.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));

        lv_sites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.setSiteCode(item);
            }
        });
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT003 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
    }

    @Override
    public void loadSites(List<HMAux> sites) {

        if(sites.size() == 1 ){
            Bundle bundle = getIntent().getExtras();
            //Bundle é passado quando o btn voltar da act 004 foi clicado.
            if(bundle != null && bundle.getInt(Constant.BACK_ACTION) == 1){
                callAct002(context);
            }else {
                mPresenter.setSiteCode(sites.get(0));
            }
        }else {
            mAdapter = new Lib_Custom_Cell_Adapter(
                    context,
                    R.layout.lib_custom_cell,
                    sites,
                    Lib_Custom_Cell_Adapter.CFG_ID_CODE_DESC,
                    MD_SiteDao.SITE_CODE,
                    MD_SiteDao.SITE_ID,
                    MD_SiteDao.SITE_DESC
            );

            lv_sites.setAdapter(mAdapter);
        }
    }

    private void loadTranslation(){
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_customer");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
