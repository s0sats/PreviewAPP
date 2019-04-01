package com.namoadigital.prj001.ui.act058;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act058_Main extends Base_Activity_Frag implements Act058_Main_Contract.I_View, Act058_Frag_Move.OnFragmentInteractionListener {

    public static final String FRAGMENT_MOVE = "FRAGMENT_MOVE";
    private FragmentManager fm;
    private Act058_Frag_Move act058_frag_move;
    Act058_Main_Presenter mPresenter;
    private int moveCode;
    private int movePrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act058_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        if (savedInstanceState != null) {
            restoreSavedIntance(savedInstanceState);
        }
        //
        initVars();
        //
        iniUIFooter();
        //
    }

    private void iniSetup() {
        //movido para utilizar o objeto na criação da
        recoverIntentsInfo();
        fm = getSupportFragmentManager();
        IO_Move moveInfo = mPresenter.getMoveInfo(movePrefix, moveCode);
        int viewMode = getViewMode(moveInfo);
        act058_frag_move = Act058_Frag_Move.newInstance(moveInfo, viewMode, true);
        setFrag(act058_frag_move, FRAGMENT_MOVE);
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT050
        );
        //

        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private int getViewMode(IO_Move moveInfo) {

        switch (moveInfo.getMove_type()) {
            case ConstantBaseApp.IO_PROCESS_IN_PUT_AWAY:
            case ConstantBaseApp.IO_PROCESS_OUT_PICKING:
                return 1;
            case ConstantBaseApp.IO_PROCESS_IN_CONF:
            case ConstantBaseApp.IO_PROCESS_OUT_CONF:
            case ConstantBaseApp.IO_PROCESS_MOVE:
            case ConstantBaseApp.IO_PROCESS_MOVE_PLANNED:
            default:
                return 0;
        }

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act058_title");

        transList.addAll(act058_frag_move.getFragTranslationsVars());

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void initVars() {

        //

    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movePrefix = Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_PREFIX));
            moveCode = Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_CODE));
        } else {
            movePrefix = 0;
            moveCode = 0;
        }

    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act058_frg_placeholder, type, sTag);
            ft.addToBackStack(null);
            ft.commit();
        } else {

        }
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setWsProcess(String wsProcess) {

    }

    @Override
    public void showPD(String ttl, String msg) {

    }

    @Override
    public void showAlert(String ttl, String msg) {

    }

    @Override
    public void callAct055() {

    }
}
