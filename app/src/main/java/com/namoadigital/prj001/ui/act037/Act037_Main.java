package com.namoadigital.prj001.ui.act037;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
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
    private MKEditTextNM mket_filter;
    private ImageView iv_filter;
    //
    private boolean filter_edit;
    private boolean filter_process;
    private boolean filter_waiting_action;
    private boolean filter_done;
    private boolean filter_cancelled;
    //
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
        transList.add("lbl_chk_edit");
        transList.add("lbl_chk_process");
        transList.add("lbl_chk_waiting_action");
        transList.add("lbl_chk_done");
        transList.add("lbl_chk_cancelled");
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
        mket_filter = (MKEditTextNM) findViewById(R.id.act037_mket_filter_desc);
        mket_filter.setHint("lbl_filter");
        //
        iv_filter = (ImageView) findViewById(R.id.act037_iv_filter);
        //
        initFilters();
        //
        lv_aps = (ListView) findViewById(R.id.act037_lv_aps);
        //
        applyFilter();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initFilters() {
        filter_edit = filter_process = filter_waiting_action = true;
        filter_done = filter_cancelled = false;
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
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }

    private void applyFilter() {
        mPresenter.getloadAPs(
                filter_edit,
                filter_process,
                filter_waiting_action,
                filter_done,
                filter_cancelled);
        //
        if(filter_edit||filter_process||filter_waiting_action||filter_done||filter_cancelled){
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
        }else{
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
        }
    }

    private void showFilterDialog() {
        AlertDialog.Builder alert =  new AlertDialog.Builder(context);

        LayoutInflater inflater =  this.getLayoutInflater();
        View view = inflater.inflate(R.layout.act037_helper_dialog,null);
        //
        TextView tv_title = (TextView) view.findViewById(R.id.act037_helper_dialog_tv_title);
        tv_title.setText(hmAux_Trans.get("alert_helper_dialog_msg"));
        //
        CheckBox chk_edit = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_edit);
        chk_edit.setText(hmAux_Trans.get("lbl_chk_edit"));
        chk_edit.setChecked(filter_edit);
        //
        CheckBox chk_process = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_process);
        chk_process.setText(hmAux_Trans.get("lbl_chk_process"));
        chk_process.setChecked(filter_process);
        //
        CheckBox chk_waiting_action = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_waiting_action);
        chk_waiting_action.setText(hmAux_Trans.get("lbl_chk_waiting_action"));
        chk_waiting_action.setChecked(filter_waiting_action);
        //
        CheckBox chk_done = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_done);
        chk_done.setText(hmAux_Trans.get("lbl_chk_done"));
        chk_done.setChecked(filter_done);
        //
        CheckBox chk_cancelled = (CheckBox) view.findViewById(R.id.act037_helper_dialog_chk_cancelled);
        chk_cancelled.setText(hmAux_Trans.get("lbl_chk_cancelled"));
        chk_cancelled.setChecked(filter_cancelled);
        //
        alert
            .setView(view)
            .setCancelable(true)
            .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    applyFilter();
                }
            });
        CompoundButton.OnCheckedChangeListener chkListner = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()){
                    case R.id.act037_helper_dialog_chk_edit:
                        filter_edit = isChecked;
                        break;
                    case R.id.act037_helper_dialog_chk_process:
                        filter_process = isChecked;
                        break;
                    case R.id.act037_helper_dialog_chk_waiting_action:
                        filter_waiting_action = isChecked;
                        break;
                    case R.id.act037_helper_dialog_chk_done:
                        filter_done = isChecked;
                        break;
                    case R.id.act037_helper_dialog_chk_cancelled:
                        filter_cancelled = isChecked;
                        break;
                }
            }
        };
        //
        chk_edit.setOnCheckedChangeListener(chkListner);
        chk_process.setOnCheckedChangeListener(chkListner);
        chk_waiting_action.setOnCheckedChangeListener(chkListner);
        chk_done.setOnCheckedChangeListener(chkListner);
        chk_cancelled.setOnCheckedChangeListener(chkListner);
        //
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
