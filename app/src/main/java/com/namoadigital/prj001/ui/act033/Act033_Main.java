package com.namoadigital.prj001.ui.act033;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act033_Main extends Base_Activity implements Act033_Main_View {

    private TextView tv_customer_lbl;
    private TextView tv_customer_val;
    private TextView tv_site_lbl;
    private TextView tv_site_val;
    private TextView tv_no_site;
    private MKEditTextNM mk_search_zones;
    private ListView lv_zone;
    private Act033_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;
    private Bundle bundle;
    private int backAction;
    private String requestingAct;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act033_main);

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
                Constant.ACT033
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act033_title");
        transList.add("alert_no_zone_title");
        transList.add("alert_no_zone_msg");
        transList.add("lbl_customer");
        transList.add("lbl_site");
        transList.add("alert_no_zone_found");
        transList.add("lbl_search_zones_hint");
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
                new Act033_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        new MD_Site_ZoneDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        ),
                        requestingAct
                );
        //
        tv_customer_lbl = (TextView) findViewById(R.id.act033_tv_customer_lbl);
        tv_customer_val = (TextView) findViewById(R.id.act033_tv_customer_val);
        tv_site_lbl = (TextView) findViewById(R.id.act033_tv_site_lbl);
        tv_site_val = (TextView) findViewById(R.id.act033_tv_site_val);
        tv_no_site = (TextView) findViewById(R.id.act033_tv_no_zone);
        //
        mk_search_zones = (MKEditTextNM) findViewById(R.id.act033_mket_search_zones);
        mk_search_zones.setHint(hmAux_Trans.get("lbl_search_zones_hint"));
        mk_search_zones.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                mAdapter.getFilter().filter(mk_search_zones.getText().toString().trim());
            }
        });
        //
        lv_zone = (ListView) findViewById(R.id.act033_lv_zone);
        //
        mPresenter.accessToSoModule(backAction);
        //mPresenter.getZones();
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            backAction = bundle.getInt(Constant.BACK_ACTION,0);
            requestingAct = bundle.getString(Constant.MAIN_REQUESTING_ACT,"");
        }else{
            backAction = 0;
            requestingAct = "";
        }
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT033 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();

    }

    private void initActions() {
        tv_customer_lbl.setText(hmAux_Trans.get("lbl_customer"));
        tv_customer_val.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));
        //
        tv_site_lbl.setText(hmAux_Trans.get("lbl_site"));
        //
        MD_Site site =
                new MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).getByString(
                        new MD_Site_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context)
                        ).toSqlQuery()
                );
        //
        String siteDesc = hmAux_Trans.get("lbl_external_site");
        //
        if(site != null){
            siteDesc = site.getSite_id() + " - "  + site.getSite_desc()  ;
        }
        //
        tv_site_val.setText(siteDesc);
        //
        lv_zone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux zone = (HMAux) parent.getItemAtPosition(position);
                //
                mPresenter.setZonePreference(zone);
            }
        });

    }

    @Override
    public void loadZones(List<HMAux> zones) {
        mAdapter = new Lib_Custom_Cell_Adapter(
                context,
                R.layout.lib_custom_cell,
                zones,
                Lib_Custom_Cell_Adapter.CFG_ID_CODE_DESC,
                MD_Site_ZoneDao.ZONE_CODE,
                MD_Site_ZoneDao.ZONE_ID,
                MD_Site_ZoneDao.ZONE_DESC
        );
        //
        lv_zone.setAdapter(mAdapter);
    }

    @Override
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showNoZoneMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_no_zone_title"),
                hmAux_Trans.get("alert_no_zone_msg"),
                null,
                0
        );
        //
        tv_no_site.setText(hmAux_Trans.get("alert_no_zone_found"));
        tv_no_site.setVisibility(View.VISIBLE);
        //
        lv_zone.setVisibility(View.GONE);
        mk_search_zones.setVisibility(View.GONE);
    }

    @Override
    public void callAct003(Context context,Bundle bundle) {
        //Zera preferencia de site
        ToolBox_Con.setPreference_Site_Code(context,"-1");
        //
        Intent mIntent = new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct004(Context context) {
        Intent mIntent = new Intent(context, Act004_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void sendResultOkToRequestingAct(Context context) {
        setResult(RESULT_OK);
        //
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if ( requestingAct.equals(Constant.ACT017) || requestingAct.equals(Constant.ACT013)) {
            finish();
        } else {
            mPresenter.onBackPressedClicked();
        }
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
