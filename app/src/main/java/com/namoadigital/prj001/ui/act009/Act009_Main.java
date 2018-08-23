package com.namoadigital.prj001.ui.act009;

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

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act010.Act010_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act028.Act028_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act009_Main extends Base_Activity implements Act009_Main_View {

    private Act009_Main_Presenter mPresenter;
    private ListView lv_form_types;
    private long product_code;
    private String serial_id;
    private Bundle bundle;
    private Lib_Custom_Cell_Adapter mAdapter;
    //private boolean back_act020 = false;
    private int back_action;
    private Integer mSo_Prefix;
    private Integer mSo_Code;
    private String actResqueting="";
    //Revisão novo fluxo n-form 06/06/2018
    private String site_code_form_param;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act009_main);

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
                Constant.ACT009
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_type");

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
        mPresenter = new Act009_Main_Presenter_Impl(
                context,
                this,
                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                actResqueting,
                back_action,
                site_code_form_param
        );

        lv_form_types = (ListView) findViewById(R.id.act009_lv_form_types);
        //
        mPresenter.setAdapterData(product_code, "", serial_id );

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(MD_ProductDao.PRODUCT_CODE));
            //product_code = Long.parseLong(bundle.getString(Constant.ACT020_PRODUCT_CODE));
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            //serial_id = bundle.getString(Constant.ACT020_SERIAL_ID, "");
            //back_act020 = bundle.getBoolean(Constant.ACT020_BACK_FLOW, false);
            actResqueting = bundle.getString(Constant.MAIN_REQUESTING_ACT,"");
            back_action = bundle.getInt(Constant.BACK_ACTION, 0);
            //Novo fluxo N-Form 06/06/2018
            site_code_form_param = bundle.getString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            //site_code_form_param = bundle.getString(Constant.ACT008_SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
        } else {
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT009;
        mAct_Title = Constant.ACT009 + "_" + "title";
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
        //
        lv_form_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                addFormTypeInfoToBundle(item);
                //
                callAct010(context);

            }
        });
    }

    @Override
    public void addFormTypeInfoToBundle(HMAux item) {
        bundle.putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
                //Constant.ACT009_CUSTOM_FORM_TYPE,
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE)
        );
        //
        bundle.putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC,
                //Constant.ACT009_CUSTOM_FORM_TYPE_DESC,
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC)
        );
    }

    @Override
    public void loadForm_Types(List<HMAux> form_types) {

        if (form_types.size() > 0) {
            //
            mAdapter = new Lib_Custom_Cell_Adapter(
                    context,
                    R.layout.lib_custom_cell,
                    form_types,
                    Lib_Custom_Cell_Adapter.CFG_ID_CODE_DESC,
                    GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
                    "",
                    GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC,
                    hmAux_Trans.get("lbl_type"),
                    "",
                    ""
            );
            //
            lv_form_types.setAdapter(mAdapter);

        } else {
            //Se lista vazia exibe alert e volta pra tela anterior
            List<String> transList = new ArrayList<>();
            transList.add("alert_ttl_no_form_found");
            transList.add("alert_msg_no_form_found");

            HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans, mModule_Code, mResource_Code, transList);
            ToolBox.alertMSG(
                    Act009_Main.this,
                    alertTrans.get("alert_ttl_no_form_found"),
                    alertTrans.get("alert_msg_no_form_found"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //callAct008(context);
                            mPresenter.onBackPressedClicked(actResqueting);
                        }
                    },
                    0
            );
        }
    }

    public void callAct008(Context context) {
//        Intent mIntent = new Intent(context, Act008_Main.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        bundle.remove(MD_Product_SerialDao.SERIAL_ID);
//        //bundle.remove(Constant.ACT008_SERIAL_ID);
//        bundle.remove(MD_ProductDao.PRODUCT_DESC);
//        //bundle.remove(Constant.ACT008_PRODUCT_DESC);
//        bundle.remove(Constant.BACK_ACTION);
//        //
//        // VERIFICAR
//        bundle.putString(MD_ProductDao.PRODUCT_CODE,bundle.getString(MD_ProductDao.PRODUCT_CODE));
//        //bundle.putString(Constant.MAIN_PRODUCT_CODE,bundle.getString(Constant.ACT007_PRODUCT_CODE));
//        bundle.putString(MD_Product_SerialDao.SERIAL_ID,bundle.getString(MD_Product_SerialDao.SERIAL_ID));
//        //bundle.putString(Constant.MAIN_SERIAL_ID,bundle.getString(Constant.ACT008_SERIAL_ID));
//
//        mIntent.putExtras(bundle);
//        startActivity(mIntent);
//        finish();
    }

    public void callAct010(Context context) {
        Intent mIntent = new Intent(context, Act010_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

//    @Override
//    public void callAct020(Context context) {
//
//        Intent mIntent = new Intent(context, Act020_Main.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        bundle.remove(Constant.ACT007_PRODUCT_CODE);
//        bundle.remove(Constant.ACT008_SERIAL_ID);
//        bundle.remove(Constant.ACT008_PRODUCT_DESC);
//        bundle.remove(Constant.BACK_ACTION);
//        mIntent.putExtras(bundle);
//        startActivity(mIntent);
//        finish();
//    }

    @Override
    public void callAct006(Context context) {

        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        bundle.remove(Constant.ACT007_PRODUCT_CODE);
//        bundle.remove(Constant.ACT008_SERIAL_ID);
//        bundle.remove(Constant.ACT008_PRODUCT_DESC);
//        bundle.remove(Constant.BACK_ACTION);
//        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Context context) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(MD_ProductDao.PRODUCT_CODE);
        //bundle.remove(Constant.ACT007_PRODUCT_CODE);
        bundle.remove(MD_Product_SerialDao.SERIAL_ID);
        //bundle.remove(Constant.ACT008_SERIAL_ID);
        bundle.remove(MD_ProductDao.PRODUCT_DESC);
        //bundle.remove(Constant.ACT008_PRODUCT_DESC);

        bundle.remove(Constant.BACK_ACTION);
        bundle.remove(Constant.MAIN_REQUESTING_ACT);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct028(Context context) {
        Intent mIntent = new Intent(context, Act028_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(MD_ProductDao.PRODUCT_CODE);
        //bundle.remove(Constant.ACT007_PRODUCT_CODE);
        bundle.remove(MD_Product_SerialDao.SERIAL_ID);
        //bundle.remove(Constant.ACT008_SERIAL_ID);
        bundle.remove(MD_ProductDao.PRODUCT_DESC);
        //bundle.remove(Constant.ACT008_PRODUCT_DESC);

        bundle.remove(Constant.BACK_ACTION);
        bundle.remove(Constant.MAIN_REQUESTING_ACT);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked(actResqueting);
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
