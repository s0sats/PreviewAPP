package com.namoadigital.prj001.ui.act032;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.R.layout.act032_main;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act032_Main extends Base_Activity_Frag implements Act032_Main_View {

    public static final String WS_PROCESS_SERIAL = "WS_PROCESS_SERIAL";
    public static final String WS_PROCESS_SO_SYNC = "WS_PROCESS_SO_SYNC";
    public static final String WS_PROCESS_SO_SAVE = "WS_PROCESS_SO_SAVE";

    private Act032_Main_Presenter mPresenter;
    private ListView lv_so;
    private SO_Header_Adapter mAdapter;
    private String requesting_act;
    private String product_code;
    private String serial_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(act032_main);

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
                Constant.ACT032
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act032_title");
        transList.add("btn_new");
        transList.add("btn_download");
        transList.add("alert_download_mult_so_ttl");
        transList.add("alert_download_mult_so_msg");
        transList.add("alert_download_so_ttl");
        transList.add("alert_download_so_msg");
        transList.add("alert_no_so_selected");
        transList.add("progress_downloading_so_ttl");
        transList.add("progress_downloading_so_msg");
        transList.add("alert_no_so_founded_ttl");
        transList.add("alert_no_so_founded_msg");
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
        //
        recoverIntentsInfo();
        //
        mPresenter =
                new Act032_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        requesting_act,
                        new SM_SODao(
                                context,
                                ToolBox_Con.customDBPath(
                                        ToolBox_Con.getPreference_Customer_Code(context)
                                ),
                                Constant.DB_VERSION_CUSTOM
                        )
                );
        //
        lv_so = (ListView) findViewById(R.id.act032_lv_so);
        //
        mPresenter.getSOList(product_code, serial_id);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(Constant.MAIN_REQUESTING_ACT)) {
                requesting_act = bundle.getString(Constant.MAIN_REQUESTING_ACT, Constant.ACT014);
                product_code = bundle.getString(Constant.MAIN_PRODUCT_CODE, null);
                serial_id = bundle.getString(Constant.MAIN_SERIAL_ID, null);

            } else {
                //Tratar quando lista de s.o não for enviado.
                //Caixa de alerta e volta para menu?!?
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }
        } else {
            //Tratar caso não exista bundle
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT032;
        mAct_Title = Constant.ACT032 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
        //
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {

        lv_so.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux so = (HMAux) parent.getItemAtPosition(position);
                //
                mPresenter.defineForwardFlow(so);

            }
        });


    }

    @Override
    public void loadSOList(List<HMAux> soList) {
        String configType = product_code == null || serial_id == null ? SO_Header_Adapter.CONFIG_TYPE_EXIBITION_FULL : SO_Header_Adapter.CONFIG_TYPE_EXIBITION_SO;
        //
        mAdapter = new SO_Header_Adapter(
                context,
                R.layout.so_header_cell,
                soList,
                configType
        );
        //
        lv_so.setAdapter(mAdapter);
    }

    @Override
    public void callAct014(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void callAct027(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        //Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();

    }
}
