package com.namoadigital.prj001.ui.act077;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.ctls.FabMenuItem;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Form;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.ui.act075.Act075_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act075.Act075_Main.PRODUCT_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.VIEW_PROFILE;

public class Act077_Main extends Base_Activity_Frag implements Act077_Main_Contract.I_View {
    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private ArrayList<FabMenuItem> fabMenuItems = new ArrayList<>();
    private FabMenu fabMenu;
    private FabMenuItem fabStep;
    private FabMenuItem fabProduct;
    private FabMenuItem fabOrigin;
    private boolean hasFABActive = false;
    private Act077_Main_Presenter mPresenter;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    ImageView iv_form_score;
    TextView tv_form_score;
    ImageView iv_form_nc_count;
    TextView tv_form_nc_count;
    ImageView iv_form_download_pdf;
    TextView tv_form_download_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act077_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        fabMenu = findViewById(R.id.act077_fabMenu_anchor);
        iv_form_score = findViewById(R.id.act077_iv_form_score);
        tv_form_score = findViewById(R.id.act077_tv_form_score);
        iv_form_nc_count =  findViewById(R.id.act077_iv_form_nc_count);
        tv_form_nc_count =  findViewById(R.id.act077_tv_form_nc_count);
        iv_form_download_pdf =  findViewById(R.id.act077_iv_form_download_pdf);
        tv_form_download_pdf =  findViewById(R.id.act077_tv_form_download_pdf);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void initVars() {
        mPresenter = new Act077_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        //
        if (mTkPrefix <= 0 || mTkCode <= 0) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_ticket_parameter_error_ttl"),
                    hmAux_Trans.get("alert_ticket_parameter_error_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    },
                    0
            );
        }else {
            //
            tv_form_download_pdf.setText(hmAux_Trans.get("download_form_pdf_lbl"));
            initFabMenuItens();
            mPresenter.getStepOrigin(mTkPrefix, mTkCode);
        }
    }

    private void initFabMenuItens() {
        int lblBgColor = getResources().getColor(R.color.namoa_pipeline_background_icon);
        int lblColor = getResources().getColor(R.color.padrao_WHITE);
        int btnBgColor = getResources().getColor(R.color.namoa_sync_pipeline_background_btn);
        int iconColor = getResources().getColor(R.color.colorPrimary);
        //atalaho para origin.
        fabOrigin = new FabMenuItem(context);
        fabOrigin.setTag("to_origin_lbl");
        fabOrigin.setmLabel(hmAux_Trans.get("to_origin_lbl"));
        fabOrigin.setmLabel_Back_Color(lblBgColor);
        fabOrigin.setmLabel_Text_Color(lblColor);
        fabOrigin.setmButton_Back_Color(btnBgColor);
        fabOrigin.setmButton_Resource_Color(iconColor);
        fabOrigin.setmButton_Resource(R.drawable.ic_baseline_error_outline_24dp_black);
        fabMenuItems.add(fabOrigin);
        //atalho para step
        fabStep = new FabMenuItem(context);
        fabStep.setTag("to_step_lbl");
        fabStep.setmLabel(hmAux_Trans.get("to_step_lbl"));
        fabStep.setmLabel_Back_Color(lblBgColor);
        fabStep.setmLabel_Text_Color(lblColor);
        fabStep.setmButton_Back_Color(btnBgColor);
        fabStep.setmButton_Resource_Color(iconColor);
        fabStep.setmButton_Resource(R.drawable.ic_baseline_assignment_24);
        fabMenuItems.add(fabStep);
        //atalaho para produto.
        fabProduct = new FabMenuItem(context);
        fabProduct.setTag("to_product_lbl");
        fabProduct.setmLabel(hmAux_Trans.get("to_product_lbl"));
        fabProduct.setmLabel_Back_Color(lblBgColor);
        fabProduct.setmLabel_Text_Color(lblColor);
        fabProduct.setmButton_Back_Color(btnBgColor);
        fabProduct.setmButton_Resource_Color(iconColor);
        fabProduct.setmButton_Resource(R.drawable.ic_baseline_build_24);
        fabMenuItems.add(fabProduct);
        fabMenu.setFabMenuItens(fabMenuItems);
        //
        //Seta tradução e itens no FabMenu
        for (FabMenuItem item : fabMenuItems) {
            if (item != null && hmAux_Trans.get((String) item.getTag()) != null) {
                item.setmLabel(hmAux_Trans.get((String) item.getTag()));
            } else {
                item.setmLabel(ToolBox.setNoTrans(mModule_Code, mResource_Code, (String) item.getTag()));
            }
        }
        //
        fabMenu.setFabMenuItens(fabMenuItems);
        fabMenu.setmIcons_Enabled(true);
    }


    private void setHeaderFragment(TK_Ticket tkTicket) {
        fm = getSupportFragmentManager();
        TK_Ticket_Step originStep = tkTicket.getStep().get(0);
        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForOrigin(
                tkTicket.getTicket_id(),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_site_code(),
                tkTicket.getOpen_site_desc(),
                tkTicket.getOpen_serial_id(),
                tkTicket.getOpen_product_desc(),
                hmAux_Trans.get("measure_origin_type_lbl"),
                context.getResources().getColor(R.color.grid_header_normal),
                tkTicket.getType_path(),
                tkTicket.getOrigin_desc(),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(originStep.getStep_end_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                originStep.getStep_end_user_nick()
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act077_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
        }
    }

    @Override
    public void onBackPressed() {
        if (hasFABActive) {
            fabMenu.animateFAB();
        } else {
            callAct070();
        }
    }

    private void initActions() {
        fabMenu.setOnFabClickListener(new FabMenu.IFabMenu() {
            @Override
            public void onFabClick(View view) {
                int id = view.getId();
                if ((id == fabProduct.getId())) {
                    callAct075();
                } else if (id == fabStep.getId()) {
                    callAct070();
                } else if (id == fabOrigin.getId()) {

                }
            }

            @Override
            public void onFabStatusChanged(boolean b) {
                hasFABActive = b;
            }
        });
    }

    private void callAct075() {
        Intent intent = new Intent(context, Act075_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.putInt(VIEW_PROFILE, PRODUCT_VIEW_ID);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT077
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act077_title");
        transList.add("alert_ticket_parameter_error_ttl");
        transList.add("alert_ticket_parameter_error_msg");
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        transList.add("to_origin_lbl");
        transList.add("measure_origin_type_lbl");
        transList.add("download_form_pdf_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT077;
        mAct_Title = Constant.ACT077 + "_title";
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
        ToolBox_Inf.buildFooterDialog(context, false);
    }

    @Override
    public void loadTicketOrigin(TK_Ticket tkTicket) {
        setHeaderFragment(tkTicket);

        setFormInfos(tkTicket);

    }

    private void setFormInfos(TK_Ticket tkTicket) {
        boolean hasFormCtrl = false;
        TK_Ticket_Step originStep = tkTicket.getStep().get(0);
        if (originStep != null) {
            for (TK_Ticket_Ctrl originCtrl : originStep.getCtrl()) {
                if (originCtrl != null && Constant.TK_TICKET_CRTL_TYPE_FORM.equalsIgnoreCase(originCtrl.getCtrl_type())) {
                    hasFormCtrl = true;
                    final TK_Ticket_Form form = originCtrl.getForm();
                    try {
                        if (form.getScore_perc() != null) {
                            tv_form_score.setText(form.getScore_perc().replace(".", ","));
                            tv_form_score.setTextColor(context.getResources().getColor(ToolBox_Inf.getScoreFormColor(form.getScore_status())));
                        } else {
                            tv_form_score.setVisibility(View.GONE);
                            iv_form_score.setVisibility(View.GONE);
                        }
                        tv_form_nc_count.setText(String.format("%s", form.getNc()));

                    } catch (NullPointerException e) {
                        ToolBox_Inf.registerException(e);
                        hideFormInfo();
                    }
                    tv_form_download_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.tryOpenFormPDF(form);
                        }
                    });
                }
            }
            if(!hasFormCtrl){
                hideFormInfo();
            }
        }else{
            hideFormInfo();
        }
    }

    private void hideFormInfo() {
        tv_form_score.setVisibility(View.GONE);
        iv_form_score.setVisibility(View.GONE);
        tv_form_nc_count.setVisibility(View.GONE);
        iv_form_nc_count.setVisibility(View.GONE);
        tv_form_download_pdf.setVisibility(View.GONE);
        iv_form_download_pdf.setVisibility(View.GONE);
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

    private void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }
}