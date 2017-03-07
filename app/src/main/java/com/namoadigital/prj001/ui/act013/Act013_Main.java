package com.namoadigital.prj001.ui.act013;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Local_Data_List_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main extends Base_Activity implements Act013_Main_View {

    private Context context;
    private Act013_Main_Presenter mPresenter;
    private Local_Data_List_Adapter mAdapter;

    private ListView lv_pendencies;
    private TextView tv_filter;
    private CheckBox chk_processing;
    private CheckBox chk_scheduled;
    private CheckBox chk_finalized;
    private ImageView iv_help;
    private List<CheckBox> checkBoxList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act013_main);

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
                Constant.ACT013
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("alert_form_not_ready_title");
        translateList.add("alert_form_not_ready_msg");
        translateList.add("alert_helper_dialog_ttl");
        translateList.add("alert_helper_dialog_msg");
        translateList.add("lbl_chk_in_processing");
        translateList.add("lbl_chk_scheduled");
        translateList.add("lbl_chk_finalized");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    private void initVars() {
        mPresenter =
                new Act013_Main_Presenter_Impl(
                        context,
                        this,
                        new GE_Custom_Form_LocalDao(
                                context,
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                Constant.DB_VERSION_CUSTOM
                        )
                );
        //
        lv_pendencies = (ListView) findViewById(R.id.act013_lv_pendencies);
        //
        tv_filter = (TextView) findViewById(R.id.act013_tv_filter);
        tv_filter.setTag("lbl_filter");
        views.add(tv_filter);
        //
        checkBoxList = new ArrayList<>();
        //
        chk_processing = (CheckBox) findViewById(R.id.act013_chk_in_process);
        chk_processing.setChecked(true);
        //
        chk_scheduled = (CheckBox) findViewById(R.id.act013_chk_scheduled);
        chk_scheduled.setVisibility(View.GONE);
        //
        chk_finalized = (CheckBox) findViewById(R.id.act013_chk_finalized);
        //Add checkbox na lista
        checkBoxList.add(chk_processing);
        checkBoxList.add(chk_scheduled);
        checkBoxList.add(chk_finalized);
        //
        iv_help = (ImageView) findViewById(R.id.act013_iv_help);
        //
        //mPresenter.getPendencies(true,false);
        filterApply();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT013;
        mAct_Title = Constant.ACT013 + "_" + "title";
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

        lv_pendencies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.validateOpenForm(item);
            }
        });

        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelperDialog();
            }
        });

        chk_processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply();
            }
        });

        chk_finalized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply();
            }
        });

        chk_scheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply();
            }
        });



    }

    /**
     *   Analisa todos checklist
     * e chama função que monsta lista ja passando os
     * filtros selecionados.
     */
    private void filterApply() {
        boolean filterInProcessing = false;
        boolean filterFinalized = false;
        boolean filterScheduled = false;

        for ( CheckBox checkBox :checkBoxList ) {
            switch (checkBox.getId() ){
                case R.id.act013_chk_in_process:

                    if( chk_processing.getVisibility() == View.VISIBLE
                        && chk_processing.isChecked() ){
                        filterInProcessing = true;
                    }
                    break;
                case R.id.act013_chk_finalized:

                    if( chk_finalized.getVisibility() == View.VISIBLE
                        && chk_finalized.isChecked() ){
                        filterFinalized = true;
                    }

                    break;
                case R.id.act013_chk_scheduled:
                    if( chk_scheduled.getVisibility() == View.VISIBLE
                        && chk_scheduled.isChecked() ){
                        filterScheduled = true;
                    }
                    break;
                default:
                    break;
            }
        }

        mPresenter.getPendencies(filterInProcessing,filterFinalized,filterScheduled);

    }

    private void showHelperDialog() {
        AlertDialog.Builder alert =  new AlertDialog.Builder(Act013_Main.this);

        LayoutInflater inflater =  this.getLayoutInflater();
        View view = inflater.inflate(R.layout.act013_helper_dialog,null);
        //
        TextView tv_title = (TextView) view.findViewById(R.id.act013_helper_dialog_tv_title);
        tv_title.setText(hmAux_Trans.get("alert_helper_dialog_msg"));

        CheckBox chk_processing = (CheckBox) view.findViewById(R.id.act013_helper_dialog_chk_processing);
        chk_processing.setText(hmAux_Trans.get("lbl_chk_in_processing"));
        //
        CheckBox chk_scheduled = (CheckBox) view.findViewById(R.id.act013_helper_dialog_chk_scheduled);
        chk_scheduled.setText(hmAux_Trans.get("lbl_chk_scheduled"));
        chk_scheduled.setVisibility(View.GONE);
        //
        CheckBox chk_finalized = (CheckBox) view.findViewById(R.id.act013_helper_dialog_chk_finalized);
        chk_finalized.setText(hmAux_Trans.get("lbl_chk_finalized"));

        alert
            .setView(view)
            .setCancelable(true)
            ;

        alert.show();
    }

    @Override
    public void loadPendencies(List<HMAux> pendencies) {

        mAdapter =
                new Local_Data_List_Adapter(
                        context,
                        R.layout.local_data_list_cell,
                        pendencies
                );
        lv_pendencies.setAdapter(mAdapter);

    }

    @Override
    public void callAct011(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void alertFormNotReady() {

        ToolBox.alertMSG(
                Act013_Main.this,
                hmAux_Trans.get("alert_form_not_ready_title"),
                hmAux_Trans.get("alert_form_not_ready_msg"),
                null,
                0
        );
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
