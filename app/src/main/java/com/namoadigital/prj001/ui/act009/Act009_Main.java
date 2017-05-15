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
import com.namoadigital.prj001.ui.act008.Act008_Main;
import com.namoadigital.prj001.ui.act010.Act010_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act009_Main extends Base_Activity implements Act009_Main_View {

    private Context context;
    private Act009_Main_Presenter mPresenter;
    private ListView lv_form_types;
    private long product_code;
    private String serial_id;
    private Bundle bundle;
    private Lib_Custom_Cell_Adapter mAdapter;

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
        context = getBaseContext();

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
                new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        );

        lv_form_types = (ListView) findViewById(R.id.act009_lv_form_types);
        //
        mPresenter.setAdapterData(product_code, "");

    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
            serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");
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
        mAct_Info = Constant.ACT009;
        mAct_Title = Constant.ACT009 + "_" + "title";
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
        mVersion_Value = Constant.PRJ001_VERSION;
        //Aplica informações do rodapé - fim
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

    private void addFormTypeInfoToBundle(HMAux item) {
        bundle.putString(
                Constant.ACT009_CUSTOM_FORM_TYPE,
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE)
        );
        //
        bundle.putString(
                Constant.ACT009_CUSTOM_FORM_TYPE_DESC,
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC)
        );
    }

    @Override
    public void loadForm_Types(List<HMAux> form_types) {

        if(form_types.size() > 0) {
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

        }else{
            //Se lista vazia exibe alert e volta pra tela anterior
            List<String> transList = new ArrayList<>();
            transList.add("alert_ttl_no_form_found");
            transList.add("alert_msg_no_form_found");

            HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans,mModule_Code,mResource_Code, transList);
            ToolBox.alertMSG(
                    Act009_Main.this,
                    alertTrans.get("alert_ttl_no_form_found"),
                    alertTrans.get("alert_msg_no_form_found"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            callAct008(context);
                        }
                    },
                    0
            );
        }
    }


//    private HMAux getTranslationList(List<String> translate_list) {
//        HMAux hmAux = new HMAux();
//        for (String txt:translate_list) {
//
//            if (hmAux_Trans.get(txt) != null) {
//                hmAux.put(txt,hmAux_Trans.get(txt));
//            } else {
//                hmAux.put(txt, ToolBox.setNoTrans(mModule_Code, mResource_Code, txt));
//            }
//        }
//        return hmAux;
//    }

    public void callAct008(Context context) {
        Intent mIntent = new Intent(context, Act008_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(Constant.ACT008_SERIAL_ID);
        bundle.remove(Constant.ACT008_PRODUCT_DESC);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    public void callAct010(Context context) {
        Intent mIntent = new Intent(context, Act010_Main.class);
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
}
