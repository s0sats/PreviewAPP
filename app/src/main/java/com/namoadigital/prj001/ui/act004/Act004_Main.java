package com.namoadigital.prj001.ui.act004;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
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
    private ListView lv_operations;
    private Act004_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;

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
        //
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act004_title");
        transList.add("alert_no_operation_title");
        transList.add("alert_no_operation_msg");
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
                            ToolBox_Con.cleanPreferences(Act004_Main.this);
                            ToolBox_Inf.call_Act001_Main(Act004_Main.this);
                            finish();
                        }
                    },
                    0
            );

        }
        //Se so existe uma operação, seleciona ela e pula para proxima tela
        else if (operations.size() == 1) {
            mPresenter.setOperationCode(operations.get(0));
        } else {
            mAdapter = new Lib_Custom_Cell_Adapter(
                    context,
                    R.layout.lib_custom_cell,
                    operations,
                    Lib_Custom_Cell_Adapter.CFG_ID_DESC,
                    MD_OperationDao.OPERATION_DESC,
                    MD_OperationDao.OPERATION_ID
            );

            lv_operations.setAdapter(mAdapter);
        }

    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
