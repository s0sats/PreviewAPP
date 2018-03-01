package com.namoadigital.prj001.ui.act037;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act037_Adapter_AP;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act038.Act038_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act037_Main extends Base_Activity implements Act037_Main_View {

    private Act037_Main_Presenter_Impl mPresenter;

    private ListView lv_aps;
    private Act037_Adapter_AP act037_adapter_ap;
    private ArrayList<HMAux> dados;
    private TextView tv_filter;
    private CheckBox chk_pending;
    private CheckBox chk_done;
    private ImageView iv_help;

    private Bundle bundle;
    private int backAction;
    private String requestingAct;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act037_main);

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
                Constant.ACT037
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act037_title");
        transList.add("lbl_filter");
        transList.add("alert_helper_dialog_msg");
        transList.add("lbl_chk_pendings");
        transList.add("lbl_chk_done");
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
        mPresenter =
                new Act037_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        new GE_Custom_Form_ApDao(
                                context
                        )
                );
        tv_filter = (TextView) findViewById(R.id.act037_tv_filter);
        tv_filter.setTag("lbl_filter");
        views.add(tv_filter);
        //
        chk_pending = (CheckBox) findViewById(R.id.act037_chk_pending);
        //
        chk_done = (CheckBox) findViewById(R.id.act037_chk_done);
        //
        iv_help = (ImageView) findViewById(R.id.act037_iv_help);
        //
        lv_aps = (ListView) findViewById(R.id.act037_lv_aps);
        //
        mPresenter.getloadAPs(
                chk_pending.isChecked(),
                chk_done.isChecked(),
                true ,
                false,
                false);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
        } else {
        }
    }

    @Override
    public void loadAPs(ArrayList<HMAux> aps) {
        act037_adapter_ap = new Act037_Adapter_AP(
                context,
                //R.layout.act037_main_content_cell_ap_normal,
                R.layout.namoa_ap_cell,
                aps
        );

        lv_aps.setAdapter(act037_adapter_ap);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT037;
        mAct_Title = Constant.ACT037 + "_" + "title";
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
        lv_aps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                callAct038(context, item);
            }
        });
        //
        chk_pending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               applyFilter();
            }
        });
        //
        chk_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                applyFilter();
            }
        });
        //
        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelperDialog();
            }
        });
    }

    private void applyFilter() {
        mPresenter.getloadAPs(
                chk_pending.isChecked(),
                chk_done.isChecked(),
                true,
                false,
                false);
    }

    private void showHelperDialog() {
        AlertDialog.Builder alert =  new AlertDialog.Builder(context);

        LayoutInflater inflater =  this.getLayoutInflater();
        View view = inflater.inflate(R.layout.act037_helper_dialog,null);
        //
        TextView tv_title = (TextView) view.findViewById(R.id.act037_helper_dialog_tv_title);
        tv_title.setText(hmAux_Trans.get("alert_helper_dialog_msg"));

        CheckBox chk_processing = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_pendings);
        chk_processing.setText(hmAux_Trans.get("lbl_chk_pendings"));
        //
        /*CheckBox chk_scheduled = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_scheduled);
        chk_scheduled.setText(hmAux_Trans.get("lbl_chk_scheduled"));
        chk_scheduled.setVisibility(View.GONE);*/
        //
        CheckBox chk_finalized = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_done);
        chk_finalized.setText(hmAux_Trans.get("lbl_chk_done"));

        alert
                .setView(view)
                .setCancelable(true)
        ;

        alert.show();
    }

    @Override
    public void callAct038(Context context, HMAux hmAux) {
        Intent mIntent = new Intent(context, Act038_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT037);
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();

        Log.d("DDDD", "Passei por aqui!!!");

        callAct005(context);
    }

    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

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
