package com.namoadigital.prj001.ui.act010;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.ui.act009.Act009_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main extends Base_Activity implements Act010_Main_View {

    private Context context;
    private Act010_Main_Presenter mPresenter;
    private ListView lv_forms;
    private TextView tv_form_ttl;
    private TextView tv_form_type_label;
    private TextView tv_form_type_val;
    private TextView tv_form_type_desc_label;
    private TextView tv_form_type_desc_val;

    private Lib_Custom_Cell_Adapter mAdapter;
    private Bundle bundle;
    private long product_code;
    private String serial_id;
    private int custom_form_type;
    private String custom_form_type_desc;

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
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_version");

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

        tv_form_ttl = (TextView) findViewById(R.id.act010_tv_form_lbl);
        tv_form_ttl.setTag("lbl_form_ttl");
        views.add(tv_form_ttl);
        //
        tv_form_type_label = (TextView) findViewById(R.id.act010_tv_form_type_lbl);
        tv_form_type_label.setTag("lbl_form_type_label");
        views.add(tv_form_type_label);
        //
        tv_form_type_val = (TextView) findViewById(R.id.act010_tv_form_type_val);
        //
        tv_form_type_desc_label = (TextView) findViewById(R.id.act010_tv_form_desc_lbl);
        tv_form_type_desc_label.setTag("lbl_form_type_desc_label");
        views.add(tv_form_type_desc_label);
        //
        tv_form_type_desc_val = (TextView) findViewById(R.id.act010_tv_form_desc_val);
        //
        lv_forms = (ListView) findViewById(R.id.act010_lv_form);
        //
        mPresenter.setAdapterData(product_code, custom_form_type, "");

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
            serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");
            custom_form_type = Integer.parseInt(bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE));
            custom_form_type_desc = bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);

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
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }

    private void initActions() {
        tv_form_type_val.setText(String.valueOf(custom_form_type));
        tv_form_type_desc_val.setText(custom_form_type_desc);
        //
        lv_forms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HMAux item = (HMAux) parent.getItemAtPosition(position);
            //
            mPresenter.validateOpenForm(item);
            }
        });

    }

    @Override
    public void loadForms(List<HMAux> forms) {
        mAdapter =
                new Lib_Custom_Cell_Adapter(
                        context,
                        R.layout.lib_custom_cell,
                        forms,
                        Lib_Custom_Cell_Adapter.CFG_ID_CODE_DESC,
                        GE_Custom_FormDao.CUSTOM_FORM_CODE,
                        GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                        GE_Custom_FormDao.CUSTOM_FORM_DESC,
                        "",
                        hmAux_Trans.get("lbl_version"),
                        ""
                        );
        lv_forms.setAdapter(mAdapter);

    }
    @Override
    public void addFormInfoToBundle(HMAux item) {
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
    public void alertFormNotReady() {
        List<String> translist = new ArrayList<>();
        translist.add("alert_form_not_ready_title");
        translist.add("alert_form_not_ready_msg");

        HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans,mModule_Code,mResource_Code,translist);

        ToolBox.alertMSG(
                Act010_Main.this,
                alertTrans.get("alert_form_not_ready_title"),
                alertTrans.get("alert_form_not_ready_msg"),
                null,
                0
        );
    }

    @Override
    public void callAct009(Context context) {
        Intent mIntent = new Intent(context, Act009_Main.class);
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
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }
}
