package com.namoadigital.prj001.ui.act055;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act055_IO_Move_Order_List_Adapter;
import com.namoadigital.prj001.model.IO_Move_Search_Record;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act054.Act054_Main.IO_MOVE_RECORDS;

public class Act055_Main extends Base_Activity implements Act055_Main_Contract.I_View, Act055_IO_Move_Order_List_Adapter.Act055ListListener {

    private Act055_Main_Presenter mPresenter;
    private RecyclerView rvMoveOrderList;
    private MKEditTextNM mketFilterDesc;
    private Act055_IO_Move_Order_List_Adapter mAdapter;
//    private boolean serial_jump;
    private List<IO_Move_Search_Record> mMoveSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act055_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT054
        );

        loadTranslation();
        //
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act055_title");
        transList.add("act055_lbl_new");
        transList.add("move_order_filter_hint");
        //Traducao para itens da lista


        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        mPresenter = new Act055_Main_Presenter(context,this, hmAux_Trans);
        recoverIntentsInfo();
        bindViews();
        setInitialView();
    }

    private void setInitialView() {
        mAdapter = new Act055_IO_Move_Order_List_Adapter(context, mMoveSearchList, this, hmAux_Trans);
        rvMoveOrderList.setLayoutManager(new LinearLayoutManager(context));
        rvMoveOrderList.setAdapter(mAdapter);
        mketFilterDesc.setHint(hmAux_Trans.get("move_order_filter_hint"));
    }

    private void bindViews() {
        rvMoveOrderList = findViewById(R.id.act055_rv_move_order_list);
        mketFilterDesc = findViewById(R.id.act050_mket_filter_desc);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mMoveSearchList = (List<IO_Move_Search_Record>) bundle.getSerializable(IO_MOVE_RECORDS);
        }else{
            mMoveSearchList = new ArrayList<>();
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT055;
        mAct_Title = Constant.ACT055 + Constant.title_lbl;
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

    private void initActions() {

        mketFilterDesc.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(s.trim());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mPresenter.onBackPressedClicked(Constant.ACT054);
    }

    @Override
    public void showAlertSerialOut(String alert_serial_out_site_title, String alert_serial_out_site_msg) {

    }

    @Override
    public void onClickListItem(IO_Move_Search_Record record) {
        Toast.makeText(context, String.valueOf(record.getMove_code()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPD(String dialog_serial_search_ttl, String dialog_serial_search_start) {

    }

    @Override
    public void setWsProcess(String name) {

    }

    @Override
    public void callAct054() {
        Intent mIntent = new Intent(context, Act054_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
}
