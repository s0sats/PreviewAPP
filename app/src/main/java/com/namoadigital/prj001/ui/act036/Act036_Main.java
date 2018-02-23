package com.namoadigital.prj001.ui.act036;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act037.Act037_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act036_Main extends Base_Activity implements Act036_Main_View {

    private Act036_Main_Presenter_Impl mPresenter;

    private ArrayList<HMAux> dados;

    private Bundle bundle;

    private Button btn_pendencies;
    private Button btn_sync;

    private int pendencies_qty;
    private int syncs_qty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act036_main);

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
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT036
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act036_title");
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
                new Act036_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans
                );
        //
        btn_pendencies = (Button) findViewById(R.id.act036_btn_pendencies);
        btn_pendencies.setText(hmAux_Trans.get("btn_pendencies_so"));
        //
        btn_sync = (Button) findViewById(R.id.act036_btn_sync);
        btn_sync.setText(hmAux_Trans.get("btn_sync_so"));

        mPresenter.getPendencies();
        mPresenter.getSync();

        if (syncs_qty == 0) {
            callAct037(context);
        }
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
        } else {
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT036;
        mAct_Title = Constant.ACT036 + "_" + "title";
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
        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pendencies_qty > 0) {
                    callAct037(context);
                } else {
                    showMsg();
                }
            }
        });

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ToolBox_Con.isOnline(context)) {

                    //Call AP Sync

                } else {
                    ToolBox_Inf.showNoConnectionDialog(Act036_Main.this);
                }

            }
        });
    }

    private void showMsg() {

    }


    @Override
    public void setPendencies(int qty, String qtyMyPendencies) {
        if (qty > 0) {
            pendencies_qty = qty;
            String btn_text = hmAux_Trans.get("btn_pendencies_ap") + " (" + qtyMyPendencies + ")";

            btn_pendencies.setVisibility(View.VISIBLE);
            btn_pendencies.setText(btn_text);
        } else {
            btn_pendencies.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSync(int qty) {
        if (qty > 0) {
            syncs_qty = qty;
            String btn_text = hmAux_Trans.get("btn_sync_ap") + " (" + syncs_qty + ")";

            btn_sync.setVisibility(View.VISIBLE);
            btn_sync.setText(btn_text);
        } else {
            btn_sync.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();

        Log.d("DDDD", "Passei por aqui!!!");

        callAct005(context);
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct037(Context context) {
        Intent mIntent = new Intent(context, Act037_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
