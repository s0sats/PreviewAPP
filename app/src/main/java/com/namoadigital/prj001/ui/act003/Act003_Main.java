package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main;
import com.namoadigital.prj001.ui.act033.Act033_Main;
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
    private TextView tv_customer_val;
    private MKEditTextNM mk_search_sites;
    private TextInputLayout mk_search_layout;
    private ListView lv_sites;
    private TextView lv_sites_empty_list;
    private Act003_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act003_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniSetup() {
        //Mudado o context para activity evitar problemas com layout
        context = Act003_Main.this;
        //
        mResource_Code = ToolBox_Inf.getResourceCode(context, mModule_Code, Constant.ACT003);
        //
        loadTranslation();
    }

    public void callAct033(Context context) {
        Intent mIntent = new Intent(context, Act033_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mIntent);
        finish();
    }

    public void callAct004(Context context) {
        Intent mIntent = new Intent(context, Act004_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void initVars() {
        //Inicia Download do logo do customer
        //LUCHE - 29/06/2020
        //Substituido conjunto WBR + WS pelo Workmanager
        ToolBox_Inf.scheduleDownloadCustomerLogoWork(context);

        mPresenter = new Act003_Main_Presenter_Impl(context, this);
        //Chama start do serviço do Chat.
        mPresenter.startChatService(hmAux_Trans);
        //
        tv_customer_val = (TextView) findViewById(R.id.act003_tv_customer_val);
        //
        mk_search_sites = (MKEditTextNM) findViewById(R.id.filter_edit_text);
        mk_search_layout = findViewById(R.id.filter_edit_text_llayout);
        mk_search_layout.setHint(hmAux_Trans.get("lbl_search_sites_hint"));
        mk_search_sites.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                mAdapter.getFilter().filter(mk_search_sites.getText().toString().trim());
            }
        });
        //
        lv_sites = (ListView) findViewById(R.id.act003_lv_sites);
        lv_sites_empty_list = findViewById(R.id.empty_list_textview);
        lv_sites_empty_list.setText(
                getAuxTranslation("empty_list_lbl", R.string.act002_empty_list)
        );
        //
        if (mPresenter.checkPreferenceIsSet()) {
            //callAct004(context);
            callAct033(context);
        } else {
            mPresenter.getSites(hmAux_Trans);
        }
    }

    @Override
    public void callAct002(Context context, boolean force_get_customer) {
        ToolBox_Con.setPreference_Customer_Code(context, -1L);
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);

        if (force_get_customer) {
            bundle.putInt(Constant.EXECUTE_WS_GET_CUSTOMER, 1);
        }
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }

    private void initActions() {

        tv_customer_val.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));

        lv_sites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.setSiteCode(item);
                //
                ToolBox_Inf.hideSoftKeyboard(Act003_Main.this);
            }
        });
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT003 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
    }

    private String getAuxTranslation(String var, int textOffline) {
        return !hmAux_Trans.containsKey(var) || hmAux_Trans.get(var).contains(Constant.APP_MODULE + "/") ? getString(textOffline) : hmAux_Trans.get(var);
    }

    @Override
    public void loadSites(List<HMAux> sites) {
        if (sites.isEmpty()) {
            String title = !hmAux_Trans.containsKey("alert_no_site_title") || hmAux_Trans.get("alert_no_site_title").contains(Constant.APP_MODULE + "/") ? getString(R.string.generic_alert_no_site_ttl) : hmAux_Trans.get("alert_no_site_title");
            String msg = !hmAux_Trans.containsKey("alert_no_site_msg") || hmAux_Trans.get("alert_no_site_msg").contains(Constant.APP_MODULE + "/") ? getString(R.string.generic_alert_no_site_msg) : hmAux_Trans.get("alert_no_site_msg");

            mk_search_layout.setVisibility(View.GONE);

            ToolBox.alertMSG(
                    Act003_Main.this,
                    title,
                    msg,
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

        } else if (sites.size() == 1) {
            mk_search_layout.setVisibility(View.VISIBLE);
            Bundle bundle = getIntent().getExtras();
            //Bundle é passado quando o btn voltar da act 004 foi clicado.
            if (bundle != null && bundle.getInt(Constant.BACK_ACTION) == 1) {
                callAct002(context, false);
            } else {
                mPresenter.setSiteCode(sites.get(0));
            }
        } else {
            mk_search_layout.setVisibility(View.VISIBLE);
            if(ToolBox_Inf.isConcurrentBySiteLicense(context)) {
                sites = ToolBox_Inf.getSiteLicenseAvailability(sites, Lib_Custom_Cell_Adapter.IMV_002);
            }
            mAdapter = new Lib_Custom_Cell_Adapter(
                    context,
                    R.layout.lib_custom_cell,
                    sites,
                    Lib_Custom_Cell_Adapter.CFG_ID_CODE_DESC,
                    MD_SiteDao.SITE_CODE,
                    MD_SiteDao.SITE_ID,
                    MD_SiteDao.SITE_DESC,
                    Lib_Custom_Cell_Adapter.IMV_002,
                    list -> {
                        lv_sites.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
                        lv_sites_empty_list.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                    }
            );

            lv_sites.setAdapter(mAdapter);
        }
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_customer");
        transList.add("empty_list_lbl");
        transList.add("alert_no_site_title");
        transList.add("alert_no_site_msg");
        transList.add("alert_logout_ttl");
        transList.add("alert_logout_msg");
        transList.add("lbl_search_sites_hint");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void killCurSession(Context context) {

        String title = !hmAux_Trans.containsKey("alert_logout_ttl") || hmAux_Trans.get("alert_logout_ttl").contains(Constant.APP_MODULE + "/") ? getString(R.string.generic_dialog_logout_ttl) : hmAux_Trans.get("alert_logout_ttl");
        String msg = !hmAux_Trans.containsKey("alert_logout_msg") || hmAux_Trans.get("alert_logout_msg").contains(Constant.APP_MODULE + "/") ? getString(R.string.generic_dialog_logout_msg) : hmAux_Trans.get("alert_logout_msg");

        if (title == null || msg == null) {
            title = getString(R.string.generic_dialog_logout_ttl);
            msg = getString(R.string.generic_dialog_logout_msg);
        }

        enableProgressDialog(
                title,
                msg,
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

        callAct002(context, true);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
