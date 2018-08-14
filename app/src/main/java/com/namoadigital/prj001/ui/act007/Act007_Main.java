package com.namoadigital.prj001.ui.act007;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Serial_Log_Adapter;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.service.WS_Serial_Log;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act007_Main extends Base_Activity implements Act007_Main_View {

    private Act007_Main_Presenter mPresenter;
    private TextView tv_product_lbl;
    private TextView tv_product_val;
    private TextView tv_serial_lbl;
    private TextView tv_serial_val;
    private ListView lv_logs;
    private Serial_Log_Adapter mAdapter;
    private Bundle bundle;
    private MD_Product_Serial mdProductSerial;
    private String file_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act007_main);

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
                Constant.ACT007
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_code");
        transList.add("lbl_id");
        transList.add("lbl_desc");
        transList.add("mket_hint_msg");

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
        mPresenter = new Act007_Main_Presenter_Impl(
                context,
                this,
                file_name,
                new MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans

        );
        //
        //tv_product_lbl = (TextView) findViewById(R.id.act007_tv_product_lbl);
        //tv_product_lbl.setTag("product_lbl");
        //
        tv_product_val = (TextView) findViewById(R.id.act007_tv_product_val);
        //
        tv_serial_lbl = (TextView) findViewById(R.id.act007_tv_serial_lbl);
        tv_serial_lbl.setTag("serial_lbl");
        //
        tv_serial_val = (TextView) findViewById(R.id.act007_tv_serial_val);
        //
        lv_logs = (ListView) findViewById(R.id.act007_lv_log);
        //
        //views.add(tv_product_lbl);
        views.add(tv_serial_lbl);
        //Chama carregamento dos dados do produto.
        setProductInfo();
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mdProductSerial = (MD_Product_Serial) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            file_name = bundle.getString(WS_Serial_Log.SERIAL_LOG_FILE, "");
        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }


    public void setProductInfo() {
        tv_product_val.setText(mdProductSerial.getProduct_id() + " - " + mdProductSerial.getProduct_desc());
        //Chama metodo que carrega lista de log do arquivo json
        tv_serial_val.setText(mdProductSerial.getSerial_id());
        //
        mPresenter.getLog();

    }

    @Override
    public void loadLogList(ArrayList<Serial_Log_Obj> logList) {
        mAdapter = new Serial_Log_Adapter(
                context,
                logList,
                R.layout.serial_log_cell
        );
        //
        lv_logs.setAdapter(mAdapter);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT007;
        mAct_Title = Constant.ACT007 + "_" + "title";
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

    }

    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
