package com.namoadigital.prj001.ui.act013;

import android.content.Context;
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
import com.namoadigital.prj001.adapter.Act013_Adapter_Pendencies;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main extends Base_Activity implements Act013_Main_View {

    private Context context;
    private ListView lv_pendencies;
    private Act013_Main_Presenter mPresenter;
    private Act013_Adapter_Pendencies mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act013_main);

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
        context = getBaseContext();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT013
        );

        loadTranslation();
    }

    private void loadTranslation() {
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context)
        );
    }

    private void initVars() {
        mPresenter =
                new Act013_Main_Presenter_Impl(
                        context,
                        this,
                        new GE_Custom_Form_LocalDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        )
                );
        //
        lv_pendencies = (ListView) findViewById(R.id.act013_lv_pendencies);
        //
        mPresenter.getPendencies();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT013;
        mAct_Title = Constant.ACT013 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value =  hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl =  hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value =  hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);

        //Aplica informações do rodapé - fim

    }

    private void initActions() {

        lv_pendencies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.validateOpenForm(item);
            }
        });
    }

    @Override
    public void loadPendencies(List<HMAux> pendencies) {
        mAdapter =
                new Act013_Adapter_Pendencies(
                        context,
                        R.layout.act013_main_content_cell_01,
                        pendencies
                );

        lv_pendencies.setAdapter(mAdapter);
    }

    @Override
    public void callAct011(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void alertFormNotReady() {
        List<String> translist = new ArrayList<>();
        translist.add("alert_form_not_ready_title");
        translist.add("alert_form_not_ready_msg");

        HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans,mModule_Code,mResource_Code,translist);

        ToolBox.alertMSG(
                Act013_Main.this,
                alertTrans.get("alert_form_not_ready_title"),
                alertTrans.get("alert_form_not_ready_msg"),
                null,
                0
        );
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
