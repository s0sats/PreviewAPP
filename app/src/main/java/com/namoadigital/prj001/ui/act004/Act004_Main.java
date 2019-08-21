package com.namoadigital.prj001.ui.act004;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.sql.MD_Site_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act033.Act033_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act004_Main extends Base_Activity implements Act004_Main_View {

    private Context context;
    private TextView tv_customer_lbl;
    private TextView tv_customer_val;
    private TextView tv_site_lbl;
    private TextView tv_site_val;
    private TextView tv_zone_val;
    private MKEditTextNM mk_search_operations;
    private ListView lv_operations;
    private Act004_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act004_main);

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
        mResource_Code = ToolBox_Inf.getResourceCode(context, mModule_Code, Constant.ACT004);
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act004_title");
        transList.add("alert_no_operation_title");
        transList.add("alert_no_operation_msg");
        transList.add("lbl_customer");
        transList.add("lbl_site");
        transList.add("lbl_search_operations_hint");
        //transList.add("lbl_external_site");
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
        mPresenter = new Act004_Main_Presenter_Impl(context, this);
        //
        tv_customer_lbl = (TextView) findViewById(R.id.act004_tv_customer_lbl);
        tv_customer_val = (TextView) findViewById(R.id.act004_tv_customer_val);
        tv_site_lbl = (TextView) findViewById(R.id.act004_tv_site_lbl);
        tv_site_val = (TextView) findViewById(R.id.act004_tv_site_val);
        tv_zone_val = (TextView) findViewById(R.id.act004_tv_zone_val);
        //
        mk_search_operations = (MKEditTextNM) findViewById(R.id.act004_mket_search_operations);
        mk_search_operations.setHint(hmAux_Trans.get("lbl_search_operations_hint"));
        mk_search_operations.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                mAdapter.getFilter().filter(mk_search_operations.getText().toString().trim());
            }
        });
        //
        lv_operations = (ListView) findViewById(R.id.act004_lv_operations);
        //
        if (mPresenter.checkPreferenceIsSet()) {
            callAct005(context);
        } else {
            mPresenter.getOperations();
        }
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT004 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
    }

    private void initActions() {

        tv_customer_lbl.setText(hmAux_Trans.get("lbl_customer"));
        tv_customer_val.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));

        tv_site_lbl.setText(hmAux_Trans.get("lbl_site"));

        MD_Site site =
                new MD_SiteDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ).getByString(
                        new MD_Site_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context)
                        ).toSqlQuery()
                );

        String siteDesc = hmAux_Trans.get("lbl_external_site");

        if(site != null){
            siteDesc = site.getSite_desc();
        }
        //
        tv_site_val.setText(siteDesc);
        //
        tv_zone_val.setVisibility(View.GONE);
        //Se customer tem acesso ao modulo de serviço, busca qual a zona selecionada e a exibe
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
            || ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)) {
            MD_Site_Zone zone =
                    new MD_Site_ZoneDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    ).getByString(
                            new MD_Site_Zone_Sql_003(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)),
                                    ToolBox_Con.getPreference_Zone_Code(context)
                            ).toSqlQuery()
                    );
            if (zone != null) {
                tv_zone_val.setText(zone.getZone_desc());
            }
            tv_zone_val.setVisibility(View.VISIBLE);
        }
        //
        lv_operations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.setOperationCode(item);
            }
        });
    }

    @Override
    public void loadOperations(List<HMAux> operations) {
        //Se não existe operação
        //Exibe msg e fecha aplicação
        if (operations.size() == 0) {
            ToolBox.alertMSG(
                    Act004_Main.this,
                    hmAux_Trans.get("alert_no_operation_title"),
                    hmAux_Trans.get("alert_no_operation_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            killCurSession(context);
                        }
                    },
                    0
            );
            //
            //No futuro, deixar a caixa cancelable false ou capturar a caixa
            // e no evento setOnDismissListener , rodar o metodo que chamada
            // callAct002
        }
        //Se so existe uma operação, seleciona ela e pula para proxima tela
        else if (operations.size() == 1) {
            Bundle bundle = getIntent().getExtras();
            //Bundle é passado quando o btn voltar da act 004 foi clicado.
            if(bundle != null && bundle.getInt(Constant.BACK_ACTION) == 1){
                //callAct003(context);
                callAct033(context);
            }else{
                mPresenter.setOperationCode(operations.get(0));
            }
        } else {
            mAdapter = new Lib_Custom_Cell_Adapter(
                    context,
                    R.layout.lib_custom_cell,
                    operations,
                    Lib_Custom_Cell_Adapter.CFG_ID_CODE_DESC,
                    MD_OperationDao.OPERATION_CODE,
                    MD_OperationDao.OPERATION_ID,
                    MD_OperationDao.OPERATION_DESC
            );

            lv_operations.setAdapter(mAdapter);
        }

    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mIntent);
        finish();
    }

    private void callAct002(Context context){
        ToolBox_Con.resetCustomerSiteOperationPreferences(context);
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXECUTE_WS_GET_CUSTOMER, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();

    }

    @Override
    public void callAct003(Context context) {
        ToolBox_Con.setPreference_Site_Code(context,"-1");
        Intent mIntent = new Intent(context, Act003_Main.class);
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

    @Override
    public void callAct033(Context context) {
        ToolBox_Con.setPreference_Zone_Code(context,-1);
        Intent mIntent = new Intent(context, Act033_Main.class);
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

    private void killCurSession(Context context){

        enableProgressDialog(
                hmAux_Trans.get("alert_logout_ttl"),
                hmAux_Trans.get("alert_logout_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );


        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);

        callAct002(context);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
