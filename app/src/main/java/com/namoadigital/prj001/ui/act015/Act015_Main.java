package com.namoadigital.prj001.ui.act015;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Local_Data_List_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.StatusFilterDialog;

import java.util.ArrayList;
import java.util.List;

public class Act015_Main extends Base_Activity implements Act015_Main_View {


    public static final String FILTER_CHK_IS_DONE = "FILTER_CHK_IS_DONE";
    public static final String FILTER_CHK_IS_NOT_EXEC = "FILTER_CHK_IS_NOT_EXEC";
    public static final String FILTER_CHK_IS_CANCELLED = "FILTER_CHK_IS_CANCELLED";
    public static final String FILTER_CHK_IS_IGNORED = "FILTER_CHK_IS_IGNORED";
    private Act015_Main_Presenter mPresenter;
    private ListView lv_sent;
    private ImageView iv_filter;
    private MKEditTextNM mket_filter;
    private Local_Data_List_Adapter mAdapter;
    public static final String FILTER_SEARCH_KEY = "filter_search_key";
    public static final String FORM_SELECTED_INDEX_KEY = "form_selected_index";
    private int form_selected_index = -1;
    private boolean is_done;
    private boolean is_not_exec;
    private boolean is_cancelled;
    private boolean is_ignored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act015_main);

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

    @Override
    protected void onStart() {
        super.onStart();
        if(form_selected_index > 0) {
            lv_sent.setSelection(form_selected_index);
        }
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT015
        );

        loadTranslation();

    }

    private void loadTranslation() {
        //
        List<String> translateList = new ArrayList<>();
        translateList.add("alert_schedule_comment_ttl");
        translateList.add("lbl_filter");
        translateList.add("dialog_schedule_warning_ttl");
        translateList.add("dialog_schedule_warning_new_status_lbl");
        translateList.add("dialog_schedule_warning_user_nick_lbl");
        translateList.add("dialog_schedule_warning_error_msg_lbl");
        //
        translateList.add("alert_form_status_prevents_to_open_ttl");
        translateList.add("alert_form_status_prevents_to_open_msg");
        //
        translateList.add("alert_filter_status_dialog_msg");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    private void initVars() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //
        String filter_search = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            filter_search = bundle.getString(FILTER_SEARCH_KEY);
            form_selected_index = bundle.getInt(FORM_SELECTED_INDEX_KEY, -1);
            is_done =  bundle.getBoolean(FILTER_CHK_IS_DONE, true);
            is_not_exec =  bundle.getBoolean(FILTER_CHK_IS_NOT_EXEC, true);
            is_cancelled = bundle.getBoolean(FILTER_CHK_IS_CANCELLED, false);
            is_ignored =  bundle.getBoolean(FILTER_CHK_IS_IGNORED, false);
        }else{
            is_done = true ;
            is_not_exec = true;
            is_cancelled = false;
            is_ignored = false;
        }
        //
        mPresenter =
                new Act015_Main_Presenter_Impl(
                        context,
                        this,
                        new GE_Custom_Form_LocalDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        ),
                        hmAux_Trans
                );

        iv_filter = findViewById(R.id.act015_iv_filter);
        lv_sent = (ListView) findViewById(R.id.act015_lv_sent_data);
        mket_filter = (MKEditTextNM) findViewById(R.id.act015_mket_filter);
        mket_filter.setHint(hmAux_Trans.get("lbl_filter"));
        if(filter_search != null && filter_search.length() >0){
            mket_filter.setText(filter_search);
        }
        mket_filter.clearFocus();
        /**
         * BARRIONUEVO - 30-03-2020
         * Default da tela sao os filtros done e not_exec como true e cancelled e ignored como falso
         */

        mPresenter.getSentData(is_done, is_not_exec, is_cancelled, is_ignored);
        updateIvFilterState();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT015;
        mAct_Title = Constant.ACT015 + "_" + "title";
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
        lv_sent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                form_selected_index = position;
                //LUCHE - 27/03/2020
                mPresenter.processClickAction(item);
            }
        });

        mket_filter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                applySearchFilter();
            }
        });


        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }

    private void showFilterDialog() {
        final StatusFilterDialog alert = new StatusFilterDialog(
                context,
                hmAux_Trans,
                is_done,
                is_not_exec,
                is_cancelled,
                is_ignored,
                new StatusFilterDialog.OnApplyFilterListener() {
                    @Override
                    public void onApply(boolean isDone, boolean isNotExec, boolean isCancelled, boolean isIgnored) {
                        mPresenter.getSentData(isDone, isNotExec, isCancelled, isIgnored);
                        is_done = isDone ;
                        is_not_exec = isNotExec;
                        is_cancelled = isCancelled;
                        is_ignored = isIgnored;
                        updateIvFilterState();
                    }
                });
        //
        alert.show();
    }

    @Override
    public void loadSentData(List<HMAux> sentData) {
        mAdapter = new Local_Data_List_Adapter(
                context,
                R.layout.local_data_list_cell,
                sentData,
                mket_filter.getText().toString().trim()
        );
        //
        mAdapter.setOnIvScheduleWarningClickListner(new Local_Data_List_Adapter.OnIvScheduleWarningClickListner() {
            @Override
            public void OnIvScheduleWarningClick(String fcmNewStatus, String fcmUserNick, String errorMsg) {
                ToolBox_Inf.showScheduleWarningDialog(
                    context,
                    hmAux_Trans.get("dialog_schedule_warning_ttl"),
                    hmAux_Trans.get("dialog_schedule_warning_new_status_lbl"),
                    hmAux_Trans.get(fcmNewStatus),
                    hmAux_Trans.get("dialog_schedule_warning_user_nick_lbl"),
                    fcmUserNick,
                    hmAux_Trans.get("dialog_schedule_warning_error_msg_lbl"),
                    errorMsg
                );
            }
        });
        //
        lv_sent.setAdapter(mAdapter);
    }

    @Override
    public void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            null,
            0
        );
    }

    @Override
    public void callAct011(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(!"".equals(mket_filter.getText().toString())){
            bundle.putString(FILTER_SEARCH_KEY,mket_filter.getText().toString());
        }
        bundle.putInt(FORM_SELECTED_INDEX_KEY, form_selected_index);
        bundle.putBoolean(FILTER_CHK_IS_DONE, is_done);
        bundle.putBoolean(FILTER_CHK_IS_NOT_EXEC, is_not_exec);
        bundle.putBoolean(FILTER_CHK_IS_CANCELLED, is_cancelled);
        bundle.putBoolean(FILTER_CHK_IS_IGNORED, is_ignored);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct014(Context context) {
        Intent mIntent = new Intent(context, Act014_Main.class);
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

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

    private void applySearchFilter() {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(mket_filter.getText().toString().trim());
        }
    }

    private void updateIvFilterState() {
        if (is_done || is_cancelled || is_ignored ||is_not_exec) {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
        } else {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
        }
    }
}
