package com.namoadigital.prj001.ui.act079;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.namoa_digital.namoa_library.ctls.FabMenu;
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
import com.namoadigital.prj001.ui.act082.Act082_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act075.Act075_Main.PRODUCT_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.VIEW_PROFILE;

public class Act079_Main extends Base_Activity_Frag implements Act079_Main_Contract.I_View {
    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private FabMenu fabMenu;
    private boolean hasFABActive=false;
    private Act079_Main_Presenter mPresenter;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    ImageView iv_form_score;
    TextView tv_form_score;
    ImageView iv_form_nc_count;
    TextView tv_form_nc_count;
    ImageView iv_form_download_pdf;
    TextView tv_form_download_pdf;
    private boolean is_from_edit_header;
    private boolean is_from_edit_workgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act079_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        fabMenu = (FabMenu) findViewById(R.id.act079_fabMenu_anchor);
        iv_form_score = findViewById(R.id.act079_iv_form_score);
        tv_form_score = findViewById(R.id.act079_tv_form_score);
        iv_form_nc_count =  findViewById(R.id.act079_iv_form_nc_count);
        tv_form_nc_count =  findViewById(R.id.act079_tv_form_nc_count);
        iv_form_download_pdf =  findViewById(R.id.act079_iv_form_download_pdf);
        tv_form_download_pdf =  findViewById(R.id.act079_tv_form_download_pdf);
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
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT079
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act079_title");
        transList.add("alert_ticket_parameter_error_ttl");
        transList.add("alert_ticket_parameter_error_msg");
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        transList.add("to_origin_lbl");
        transList.add("to_work_group_edit_lbl");
        transList.add("to_header_edit_lbl");
        //
        transList.add("form_origin_type_lbl");
        transList.add("form_nc_origin_type_lbl");
        transList.add("form_score_origin_type_lbl");
        transList.add("download_form_pdf_lbl");
        //
        transList.add("alert_form_pdf_not_generated_ttl");
        transList.add("alert_form_pdf_not_generated_msg");
        transList.add("alert_form_pdf_not_downloaded_ttl");
        transList.add("alert_form_pdf_not_downloaded_msg");
        transList.add("alert_starting_pdf_not_supported_ttl");
        transList.add("alert_starting_pdf_not_supported_msg");
        //
        transList.add("alert_wg_edit_need_connection_ttl");
        transList.add("alert_wg_edit_need_connection_msg");
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
        mPresenter = new Act079_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        //
        if(mTkPrefix <= 0 || mTkCode <= 0){
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
            //
            mPresenter.getStepOrigin(mTkPrefix, mTkCode);
        }
    }

    private void setFabMenu(TK_Ticket mTicket) {
        if(!isInEditionMode()) {
            ToolBox_Inf.setPipelineFabMenu(context, fabMenu, hmAux_Trans,
                    mTicket, new FabMenu.IFabMenu() {
                    @Override
                    public void onFabClick(View view) {
                        String tag = (String) view.getTag();
                        switch (tag) {
                            case ConstantBaseApp.FAB_TO_PRODUCT_LBL:
                                callAct075();
                                break;
                            case ConstantBaseApp.FAB_TO_STEP_LBL:
                                callAct070(false);
                                break;
                            case ConstantBaseApp.FAB_TO_HEADER_EDIT_LBL:
                                callAct082();
                                break;
                            case ConstantBaseApp.FAB_TO_WORK_GROUP_EDIT_LBL:
                                if(ToolBox_Con.isOnline(context)) {
                                    callAct070(true);
                                }else{
                                    ToolBox.alertMSG(
                                        context,
                                        hmAux_Trans.get("alert_wg_edit_need_connection_ttl"),
                                        hmAux_Trans.get("alert_wg_edit_need_connection_msg"),
                                        null,
                                        0
                                    );
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFabStatusChanged(boolean b) {
                        hasFABActive = b;
                    }
                }
            );
        }else{
            fabMenu.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 16/12/2020
     * Metodo que retorna se esta em algum modo de edição.
     * @return Verdadeiro se ao menos um flag de edição ativa.
     */
    private boolean isInEditionMode() {
        return is_from_edit_header || is_from_edit_workgroup;
    }


   //todo remover o campo tag_operational_desc, antigo custom_form_type_desc do layout
    private void setHeaderFragment(TK_Ticket tkTicket, String tag_operational_desc, String custom_form_desc, String step_date, String step_end_user_nick) {
        fm = getSupportFragmentManager();

        String origin_type = "";
        if(ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM.equalsIgnoreCase(tkTicket.getOrigin_type())) {
            origin_type = hmAux_Trans.get("form_origin_type_lbl");
        }else if(ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_NC.equalsIgnoreCase(tkTicket.getOrigin_type())) {
            origin_type = hmAux_Trans.get("form_nc_origin_type_lbl");
        }else{
            origin_type = hmAux_Trans.get("form_score_origin_type_lbl");
        }

        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForOrigin(
                tkTicket,
                tkTicket.getTicket_id(),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_site_code(),
                tkTicket.getOpen_site_desc(),
                tkTicket.getOpen_serial_id(),
                tkTicket.getOpen_product_desc(),
                origin_type,
                context.getResources().getColor(R.color.grid_header_normal),
                tag_operational_desc,
                custom_form_desc,
                step_date,
                step_end_user_nick
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act079_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            is_from_edit_header = requestingBundle.getBoolean(Act082_Main.FROM_EDIT_HEADER, false);
            is_from_edit_workgroup = requestingBundle.getBoolean(Act070_Main.PARAM_WORKGROUP_EDIT_MODE,false);
        }else{
            mTkPrefix = -1;
            mTkCode = -1;
            is_from_edit_header = false;
            is_from_edit_workgroup = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (hasFABActive) {
            fabMenu.animateFAB();
        } else {
            if(is_from_edit_header){
                callAct082();
            }else {
                callAct070(false);
            }
        }
    }

    private void callAct082() {
        Intent intent = new Intent(context, Act082_Main.class);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void initActions() {

    }

    private void callAct075() {
        Intent intent = new Intent(context, Act075_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.putInt(VIEW_PROFILE, PRODUCT_VIEW_ID);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }


    private void callAct070(boolean forceEditMode) {
        Intent intent = new Intent(context, Act070_Main.class);
        if(forceEditMode){
            requestingBundle.putBoolean(Act070_Main.PARAM_FORCE_WORKGROUP_EDIT_MODE,true);
        }
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
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
        mAct_Info = Constant.ACT079;
        mAct_Title = Constant.ACT079 + "_title";
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
    public void loadTicketOrigin(TK_Ticket ticket) {
        String custom_form_desc ="-";
        String step_date ="-";
        String step_end_user_nick ="-";
        //
        TK_Ticket_Step originStep = ticket.getStep().get(0);
        if(originStep != null ){
            //
            String dateStart = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(originStep.getStep_start_date()),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );
            //
            String dateEnd = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(originStep.getStep_end_date()),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );
            //
            step_date = ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd);
            step_end_user_nick = originStep.getStep_end_user_nick();
            TK_Ticket_Ctrl originCtrl = originStep.getCtrl().get(0);
            if(originCtrl != null){
                final TK_Ticket_Form form = originCtrl.getForm();
                try {
                    custom_form_desc = form.getCustom_form_desc();
                    if (form.getScore_perc() != null) {
                        tv_form_score.setText(form.getScore_perc().replace(".", ",") + "%");
                        int color = context.getResources().getColor(ToolBox_Inf.getScoreFormColor(form.getScore_status()));
                        tv_form_score.setTextColor(color);
                        iv_form_score.setImageTintList(ColorStateList.valueOf(color));
                    } else {
                        tv_form_score.setVisibility(View.GONE);
                        iv_form_score.setVisibility(View.GONE);
                    }
                    if(form.getNc() > 0 ) {
                        tv_form_nc_count.setText(String.format("%s", form.getNc()));
                    }else{
                        tv_form_nc_count.setVisibility(View.GONE);
                        iv_form_nc_count.setVisibility(View.GONE);
                    }

                }catch (NullPointerException e){
                    ToolBox_Inf.registerException(getClass().getName(), e);
                    tv_form_score.setVisibility(View.GONE);
                    iv_form_score.setVisibility(View.GONE);
                    tv_form_nc_count.setVisibility(View.GONE);
                    iv_form_nc_count.setVisibility(View.GONE);
                    tv_form_download_pdf.setVisibility(View.GONE);
                    iv_form_download_pdf.setVisibility(View.GONE);
                }
                tv_form_download_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.tryOpenFormPDF(form);
                    }
                });
            }
        }
        //
        setHeaderFragment(ticket, ticket.getTag_operational_desc(), custom_form_desc, step_date, step_end_user_nick);
        //
        setFabMenu(ticket);
        //
    }

    @Override
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                null,
                0
        );
    }
}