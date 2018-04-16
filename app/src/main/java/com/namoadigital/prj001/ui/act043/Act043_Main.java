package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_SO_Service_Search;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act043_Main extends Base_Activity_Frag_NFC_Geral implements Act043_Main_View {

    public static final String SELECTION_FRAG_PREVIEW = "FRAG_PREVIEW";
    public static final String SELECTION_FRAG_SERVICE_LIST = "FRAG_SERVICE_LIST";


    private Bundle bundle;
    private Act043_Main_Presenter_Impl mPresenter;
    private android.support.v4.app.FragmentManager fm;
    private String ws_process;
    private SM_SO mSm_so;
    private SM_SODao sm_soDao;
    //FRAGMENTS
    private Act043_Frag_Preview act043_frag_preview;
    private Act043_Frag_Service_List act043_frag_service_list;

    private String currentFrag = "";

    private Act043_Frag_Service_List mServiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act043_main);

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
        hideKeyBoard();

        fm = getSupportFragmentManager();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT043
        );
        //
        sm_soDao = new SM_SODao(context);
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act043_title");
        //
        //FragPreview
        transList.add("btn_search_service");
        transList.add("services_tll");
        transList.add("total_lbl");
        transList.add("total_val");
        transList.add("dialog_service_search_ttl");
        transList.add("dialog_service_search_msg");
        //
        //Frag_Service_List
        transList.add("alert_service_desc");
        transList.add("alert_service_id");
        transList.add("alert_service_qtd");
        transList.add("alert_service_price");
        transList.add("alert_service_comments");
        transList.add("alert_service_remove");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(SM_SODao.SO_PREFIX) && bundle.containsKey(SM_SODao.SO_CODE)) {
                mSm_so = loadSM_So(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, "0")),
                        Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, "0"))
                );
            } else {
                //
            }
        } else {
        }
    }

    private SM_SO loadSM_So(long customer_code, int so_prefix, int so_code) {
        SM_SO sm_so = sm_soDao.getByString(
                new SM_SO_Sql_001(
                        customer_code,
                        so_prefix,
                        so_code
                ).toSqlQuery()
        );
        //
        return sm_so;
    }

    private void initVars() {
        //
        recoverIntentsInfo();
        //
        mPresenter = new Act043_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                sm_soDao
        );
        //
        initFrags();
        //
        setFrag(act043_frag_preview, SELECTION_FRAG_PREVIEW);
        //mServiceList = new Act043_Frag_Service_List();
        //
        //setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
    }

    private void initFrags() {
        act043_frag_preview = new Act043_Frag_Preview();
        act043_frag_preview.setBaInfra(this);
        act043_frag_preview.setHmAux_Trans(hmAux_Trans);
        act043_frag_preview.setmSm_so(mSm_so);
        //
        act043_frag_service_list = new Act043_Frag_Service_List();
        act043_frag_service_list.setProgressAction(new Act043_Frag_Service_List.IAct043_Frag_Service_List() {
            @Override
            public void progressAction(String title, String message, String action) {
                switch (action.toUpperCase()) {
                    case "SHOW":
                        showPD(title, message);
                        break;
                    case "HIDE":
                        disableProgressDialog();
                        break;
                    default:
                        break;
                }
            }
        });
        act043_frag_service_list.setBaInfra(this);
        act043_frag_service_list.setHmAux_Trans(hmAux_Trans);
        act043_frag_service_list.setmService(mSm_so);
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act043_main_ll, type, sTag);
            ft.commit();
            setCurrentFrag(sTag);
        }
    }

    public void setCurrentFrag(String currentFrag) {
        this.currentFrag = currentFrag;
    }

    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT043;
        mAct_Title = Constant.ACT043 + "_" + "title";
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
    public void callAct027(Context context) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Bundle bundle = new Bundle();
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    //region WS_Return
    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (ws_process.equalsIgnoreCase(WS_SO_Service_Search.class.getName())) {
            //
            if (hmAux.containsKey(Constant.PARAM_KEY_WS_RETURN)) {
                act043_frag_service_list.setmService(mSm_so);
                act043_frag_service_list.setDataReturn(
                        mPresenter.processServiceList(hmAux.get(Constant.PARAM_KEY_WS_RETURN))
                );
                setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
            } else {
                //DEFINIR MSG DE ERRO
            }
        }
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        disableProgressDialog();
        //REMOVER A LINHA ABAIXO APOS WS FUNCIONAR DIREITO
        setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        disableProgressDialog();
        //REMOVER A LINHA ABAIXO APOS WS FUNCIONAR DIREITO
        setFrag(act043_frag_service_list, SELECTION_FRAG_SERVICE_LIST);
    }

    //Metodo chamado ao finalizar o download da atualização.
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    //Tratativa SESSION NOT FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }
    //endregion
}
