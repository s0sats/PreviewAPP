package com.namoadigital.prj001.ui.act024;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class Act024_Main extends Base_Activity implements Act024_Main_View {

    private Act024_Main_Presenter mPresenter;
    private ListView lv_so_headers;
    private Button btn_download;
    private Button btn_new;
    private String serialiazed_so_list;
    private SO_Header_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act024_main);

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
                Constant.ACT024
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act024_title");
        transList.add("btn_new");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void initVars() {
        //
        recoverIntentsInfo();
        //
        mPresenter = new Act024_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans
        );
        //
        lv_so_headers = (ListView) findViewById(R.id.act024_lv_so_headers);
        //
        btn_download = (Button) findViewById(R.id.act024_btn_download);
        btn_download.setTag("btn_download");
        //
        btn_new = (Button) findViewById(R.id.act024_btn_new);
        btn_new.setTag("btn_new");
        //
        views.add(btn_download);
        views.add(btn_new);
        //
        mPresenter.getSoHeaderList(serialiazed_so_list);

    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            if(bundle.containsKey(Constant.ACT023_SO_HEADER_LIST)){
                serialiazed_so_list = bundle.getString(Constant.ACT023_SO_HEADER_LIST);

            }else{
                //Tratar quando lista de s.o não for enviado.
                //Caixa de alerta e volta para menu?!?
            }

        }else{
            //Tratar caso não exista bundle
        }


    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT024;
        mAct_Title = Constant.ACT024 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value =  hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl =  hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value =  hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value =Constant.PRJ001_VERSION;

        //Aplica informações do rodapé -fim
    }

    private void initActions() {

    }

    @Override
    public void loadSoHeaders(ArrayList<SM_SO> so_list) {
        mAdapter = new SO_Header_Adapter(
                context,
                R.layout.act024_content_cell,
                so_list
        );
        //
        lv_so_headers.setAdapter(mAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
