package com.namoadigital.prj001.ui.act010;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main extends Base_Activity implements Act010_Main_View {

    private Context context;
    private Act010_Main_Presenter mPresenter;
    private ListView lv_forms;
    private BootstrapButton btn_back;
    private long product_code;
    private String serial_id;
    private int custom_form_type;
    private Lib_Custom_Cell_Adapter mAdapter;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act010_main);

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
                Constant.ACT010
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

        recoverIntentsInfo();

        mPresenter = new Act010_Main_Presenter_Impl(
                context,
                this,
                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        );

        lv_forms = (ListView) findViewById(R.id.act010_lv_form);

        btn_back = (BootstrapButton) findViewById(R.id.act010_btn_back);
        btn_back.setTag("btn_back");
        views.add(btn_back);

        mPresenter.setAdapterData(product_code, custom_form_type, "");

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
            serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");
            custom_form_type = Integer.parseInt(bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE));

        } else {
//
//
//            Tratar o Bundle null ?
//
//
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT010;
        mAct_Title = Constant.ACT010 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    private void initActions() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBackPressedClicked();
            }
        });

        lv_forms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                addFormInfoToBundle(item);
                //
                callAct011(context);
            }
        });

    }

    @Override
    public void callAct009(Context context) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(Constant.ACT009_CUSTOM_FORM_TYPE);
        bundle.remove(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct011(Context context) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void loadForms(List<HMAux> forms) {
        mAdapter =
                new Lib_Custom_Cell_Adapter(
                        context,
                        R.layout.lib_custom_cell,
                        forms,
                        Lib_Custom_Cell_Adapter.CFG_ID_DESC_DESC2,
                        GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                        GE_Custom_FormDao.CUSTOM_FORM_CODE,
                        GE_Custom_FormDao.CUSTOM_FORM_DESC
                        );
        lv_forms.setAdapter(mAdapter);

    }

    private void addFormInfoToBundle(HMAux item) {
        bundle.putString(
                Constant.ACT010_CUSTOM_FORM_CODE,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE)
        );
        //
        bundle.putString(
                Constant.ACT010_CUSTOM_FORM_VERSION,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION)
        );
        //
        bundle.putString(
                Constant.ACT010_CUSTOM_FORM_CODE_DESC,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_DESC)
        );
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }
}
