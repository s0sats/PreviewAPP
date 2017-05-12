package com.namoadigital.prj001.ui.act006;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.ui.act013.Act013_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main extends Base_Activity implements Act006_Main_View {

    public static final String LIST_ID = "list_id";
    public static final String LIST_LABEL = "list_label";
    public static final String LIST_OPT = "list_opt";


    private Context context;
    private Act006_Main_Presenter mPresenter;

    private Button btn_new;
    private Button btn_pendencies;
    private int pendencies_qty;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act006_main);

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
                Constant.ACT006
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act006_title");
        transList.add("act006_lbl_new");
        transList.add("act006_lbl_barcode");
        transList.add("act006_lbl_checklist");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
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
        mPresenter = new Act006_Main_Presenter_Impl(
                context,
                this,
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )

        );

        btn_new = (Button) findViewById(R.id.act006_btn_new);
        btn_new.setText(hmAux_Trans.get("act006_lbl_new"));

        btn_pendencies = (Button) findViewById(R.id.act006_btn_pendencies);
        btn_pendencies.setText(hmAux_Trans.get("act006_lbl_checklist"));

        mPresenter.getPendencies();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT006;
        mAct_Title = Constant.ACT006 + "_" + "title";
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

        //Aplica informações do rodapé - fim
    }

    private void initActions() {

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAct007(context);
            }
        });

        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkPendenciesFlow(pendencies_qty);
            }
        });

    }

    @Override
    public void setPendenciesQty(int qty) {
        pendencies_qty = qty;
        String btn_text = btn_pendencies.getText().toString().trim() +" (" +pendencies_qty+")";
        btn_pendencies.setText(btn_text);
    }

    @Override
    public void callAct007(Context context) {
        Intent mIntent = new Intent(context, Act007_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    //Metodo que será chamado quando houver btn barcode.
    private void callBarCode(HMAux item) {
        try {
            Intent mIntent = new Intent(
                    context,
                    Class.forName(
                            "com.namoa_digital.namoa_library.view.BarCode_Activity"
                    )
            );

            mIntent.putExtra(ConstantBase.B_C_O_N_ID, Integer.parseInt(item.get(HMAux.ID)));
            mIntent.putExtra(ConstantBase.PREFERENCES_UI_TYPE, 4);

            context.startActivity(mIntent);

        } catch (Exception e) {
        }
    }


    @Override
    protected void barCodeShortCut(int id, String value) {
        super.barCodeShortCut(id, value);
        //
        /*Toast.makeText(
                context,
                String.valueOf(id) + " - " + value,
                Toast.LENGTH_SHORT
        ).show();        */
    }

    @Override
    public void showMsg() {

        ToolBox.alertMSG(
                Act006_Main.this,
                hmAux_Trans.get("alert_no_pendencies_title"),
                hmAux_Trans.get("alert_no_pendencies_msg"),
                null,
                0
        );

    }

    @Override
    public void callAct013(Context context) {
        Intent mIntent = new Intent(context, Act013_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle =  new Bundle();
        bundle.putInt(Constant.ACT006,1);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent =  new Intent(context, Act005_Main.class);
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
