package com.namoadigital.prj001.ui.act040;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act041.Act041_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main extends Base_Activity implements Act040_Main_View {

    private static final int PROCESSO_PRODUCT_CODE = 100;

    private Bundle bundle;
    private Act040_Main_Presenter_Impl mPresenter;
    private SO_Pack_Express mSo_pack_express;

    private MD_Product mdProduct;

    private MKEditTextNM mket_produto;
    private ImageView iv_search_produto;

    private MKEditTextNM mket_serial;
    private ImageView iv_search_serial;

    private MKEditTextNM mket_barcode;
    private ImageView iv_search_barcode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act040_main);

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
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT040
        );

        loadTranslation();
    }

    private void loadTranslation() {

        List<String> transList = new ArrayList<String>();
        transList.add("act040_title");
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
        mPresenter = new Act040_Main_Presenter_Impl(
                context,
                this,
                hmAux_Trans,
                new SO_Pack_ExpressDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        mket_produto = (MKEditTextNM) findViewById(R.id.act040_mket_product);
        iv_search_produto = (ImageView) findViewById(R.id.act040_iv_search_product);
        //
        mket_serial = (MKEditTextNM) findViewById(R.id.act040_mket_serial);
        iv_search_serial = (ImageView) findViewById(R.id.act040_iv_search_serial);
        //
        mket_barcode = (MKEditTextNM) findViewById(R.id.act040_mket_barcode);
        iv_search_barcode = (ImageView) findViewById(R.id.act040_iv_search_barcode);
        //
        //Add controles no array list.
        controls_sta.add(mket_produto);
        controls_sta.add(mket_serial);
        controls_sta.add(mket_barcode);
        //
        mPresenter.checkJump(ToolBox_Con.getPreference_Customer_Code(context));
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
        } else {
        }
    }

    @Override
    public void loadSO_Pack_Express(SO_Pack_Express so_pack_express) {
        mSo_pack_express = so_pack_express;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT040;
        mAct_Title = Constant.ACT040 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {

        iv_search_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAct041(context);
            }
        });

    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void jumpToOne() {
        iv_search_produto.setVisibility(View.GONE);
        callAct041(context);
    }

    @Override
    public void callAct041(Context context) {
        Intent mIntent = new Intent(context, Act041_Main.class);
        //mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            bundle = new Bundle();
        }

        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_PACK_EXPRESS);
        mIntent.putExtras(bundle);

        startActivityForResult(mIntent, PROCESSO_PRODUCT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PROCESSO_PRODUCT_CODE:
                processProduct_Code(resultCode, data);
                break;
            default:
                break;
        }
    }

    private void processProduct_Code(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MD_ProductDao mdProductDao = new MD_ProductDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            mdProduct = mdProductDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            Long.parseLong(data.getStringExtra("product_code"))
                    ).toSqlQuery()
            );
            //
            if (mdProduct != null) {
                mket_produto.setText(mdProduct.getProduct_desc());
                mket_serial.setText("");
                mket_barcode.setText("");
                mket_serial.requestFocus();
            } else {
                mket_produto.setText("");
            }

        } else {
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
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


}
