package com.namoadigital.prj001.ui.act010;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SM_SODao;
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
    //
    private String so_prefix;
    private String so_code;
    //Revisão novo fluxo n-form 06/06/2018
    private String site_code_form_param;

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
        transList.add("alert_more_than_one_form_ttl");
        transList.add("alert_more_than_one_form_msg");
        transList.add("alert_so_form_exits_no_so_ttl");
        transList.add("alert_so_form_exits_no_so_msg");
        transList.add("alert_so_form_exits_with_so_ttl");
        transList.add("alert_so_form_exits_with_so_msg");
        transList.add("alert_form_exits_with_so_ttl");
        transList.add("alert_form_exits_with_so_msg");
        transList.add("alert_so_lbl");


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
                new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_DataDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                product_code,
                serial_id,
                so_prefix,
                so_code,
                site_code_form_param,
                hmAux_Trans
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
            product_code = Long.parseLong(bundle.getString(MD_ProductDao.PRODUCT_CODE));
            //product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            //serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");
            custom_form_type = Integer.parseInt(bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_TYPE));
            //custom_form_type = Integer.parseInt(bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE));
            custom_form_type_desc = bundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC);
            //custom_form_type_desc = bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);
            so_prefix = bundle.getString(SM_SODao.SO_PREFIX, "");
            so_code = bundle.getString(SM_SODao.SO_CODE, "");
            //Novo fluxo N-Form 06/06/2018
            site_code_form_param = bundle.getString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            //site_code_form_param = bundle.getString(Constant.ACT008_SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
        } else {
            so_prefix = "";
            so_code = "";
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT010;
        mAct_Title = Constant.ACT010 + "_" + "title";
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
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
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
        //
//        if(forms.size() == 1 ){
//            new Handler().post(new Runnable(){
//                @Override
//                public void run() {
//                    lv_forms.performItemClick(
//                            lv_forms.getAdapter().getView(0, null, null),
//                            0,
//                            lv_forms.getAdapter().getItemId(0)
//                    );
//                }
//            });
//        }

    }

    @Override
    public void addFormInfoToBundle(HMAux item) {
        bundle.putString(
                //Constant.ACT010_CUSTOM_FORM_CODE,
                GE_Custom_FormDao.CUSTOM_FORM_CODE,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE)
        );
        //
        bundle.putString(
                //Constant.ACT010_CUSTOM_FORM_VERSION,
                GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION)
        );
        //
        // DIFERENTE VERIFICAR
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

        HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans, mModule_Code, mResource_Code, translist);

        ToolBox.alertMSG(
                Act010_Main.this,
                alertTrans.get("alert_form_not_ready_title"),
                alertTrans.get("alert_form_not_ready_msg"),
                null,
                0
        );
    }

    @Override
    public void showAlertMsg(String title, String msg) {

        ToolBox.alertMSG(
                Act010_Main.this,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void alertActiveGPSResource(final HMAux item) {
        List<String> translist = new ArrayList<>();
        translist.add("alert_form_turn_gps_on_title");
        translist.add("alert_form_turn_gps_on_msg");

        HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans, mModule_Code, mResource_Code, translist);

        ToolBox.alertMSG(
                Act010_Main.this,
                alertTrans.get("alert_form_turn_gps_on_title"),
                alertTrans.get("alert_form_turn_gps_on_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.validateGPSResource(item);
                    }
                },
                1
        );

    }

    @Override
    public void callAct009(Context context) {
        Intent mIntent = new Intent(context, Act009_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(GE_Custom_FormDao.CUSTOM_FORM_TYPE);
        //bundle.removeFull(Constant.ACT009_CUSTOM_FORM_TYPE);
        bundle.remove(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC);
        //bundle.removeFull(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);
        bundle.putInt(Constant.BACK_ACTION, 1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

}
