package com.namoadigital.prj001.ui.act078;

import static com.namoadigital.prj001.ui.act075.Act075_Main.PRODUCT_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.VIEW_PROFILE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.databinding.Act078MainBinding;
import com.namoadigital.prj001.databinding.Act078MainContentBinding;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Act078_Main extends Base_Activity_Frag implements Act078_Main_Contract.I_View {

    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private boolean hasFABActive = false;
    private Act078_Main_Presenter mPresenter;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    private String actionPhotoLocalPath;
    //
    private boolean is_from_edit_header;
    private boolean is_from_edit_workgroup;
    private Act078MainContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Act078MainBinding mainBinding = Act078MainBinding.inflate(getLayoutInflater());
        binding = mainBinding.act078MainContent;
        //
        setContentView(mainBinding.getRoot());
        setSupportActionBar(mainBinding.toolbar);
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
        mPresenter = new Act078_Main_Presenter(context, this, hmAux_Trans);
        //
        recoverIntentsInfo();
        //
        setLabels();
        //
        if (mTkPrefix <= 0 || mTkCode <= 0) {
            ticketParameterError(
                hmAux_Trans.get("alert_ticket_parameter_error_ttl"),
                hmAux_Trans.get("alert_ticket_parameter_error_msg")

            );
        }else {
            mPresenter.getStepOrigin(mTkPrefix, mTkCode);
        }
    }

    @Override
    public void ticketParameterError(String ttl,String msg) {
        ToolBox.alertMSG(
                context,
                ttl,
                msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );
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

    private void setFabMenu(TK_Ticket mTicket) {
        //LUCHE - 16/12/2020
        //Quando em edição, o fab não deve ser exibido
        if(!isInEditionMode()) {
            ToolBox_Inf.setPipelineFabMenu(context, binding.act078FabMenuAnchor, hmAux_Trans,
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
                                    showMsg(
                                        hmAux_Trans.get("alert_wg_edit_need_connection_ttl"),
                                        hmAux_Trans.get("alert_wg_edit_need_connection_msg")
                                    );
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFabStatusChanged(boolean b) {
                        hasFABActive = b;
                    }
                });
        }else{
            binding.act078FabMenuAnchor.setVisibility(View.GONE);
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

    private void setLabels() {
        binding.act078TvOpenPhotoLbl.setText(hmAux_Trans.get("open_photo_lbl"));
        binding.act078TvOpenCommentLbl.setText(hmAux_Trans.get("open_comment_lbl"));
        binding.act078TvOpenUsernameLbl.setText(hmAux_Trans.get("open_username_lbl"));
        binding.act078TvOpenEmailLbl.setText(hmAux_Trans.get("open_email_lbl"));
        binding.act078TvOpenPhoneLbl.setText(hmAux_Trans.get("open_phone_lbl"));
        //
        binding.act078TvClientAddressTtl.setText(hmAux_Trans.get("client_address_ttl"));
        binding.act078TvClientAddressStreetLbl.setText(hmAux_Trans.get("client_address_street_lbl"));
        binding.act078TvClientAddressNumLbl.setText(hmAux_Trans.get("client_address_num_lbl"));
        binding.act078TvClientAddressDistrictLbl.setText(hmAux_Trans.get("client_address_district_lbl"));
        binding.act078TvClientAddressCityLbl.setText(hmAux_Trans.get("client_address_city_lbl"));
        binding.act078TvClientAddressStateLbl.setText(hmAux_Trans.get("client_address_state_lbl"));
        binding.act078TvClientAddressCountryLbl.setText(hmAux_Trans.get("client_address_country_lbl"));
        binding.act078TvClientAddressComplementLbl.setText(hmAux_Trans.get("client_address_complement_lbl"));
        binding.act078TvClientAddressZipcodeLbl.setText(hmAux_Trans.get("client_address_zipcode_lbl"));
        //
        binding.act078TvFormDownloadPdf.setText(hmAux_Trans.get("download_form_pdf_lbl"));
        binding.act078TvScheduleOpenPhotoLbl.setText(hmAux_Trans.get("action_photo_lbl"));
        binding.act078TvScheduleOpenCommentLbl.setText(hmAux_Trans.get("action_comment_lbl"));
    }


    private void setHeaderFragment(TK_Ticket tkTicket) {
        fm = getSupportFragmentManager();

        TK_Ticket_Step originStep = tkTicket.getStep().get(0);
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
                mPresenter.getOriginTypeLbl(tkTicket),
                context.getResources().getColor(R.color.grid_header_normal),
                getPathInfo(tkTicket),
                ToolBox_Inf.getFormattedTicketOriginDesc(tkTicket.getOrigin_type(),tkTicket.getOrigin_desc(),tkTicket.getType_desc()),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(originStep.getStep_end_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_user_name()
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act078_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    private String getPathInfo(TK_Ticket tkTicket) {
        switch (tkTicket.getOrigin_type()){
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE:
                return tkTicket.getType_path();
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MEASURE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_SCORE:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_FORM_NC:
            case ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_TRANSFER:
            default:
                return "";
        }
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
            binding.act078FabMenuAnchor.animateFAB();
        } else {
            if(is_from_edit_header){
                callAct082();
            }else {
                callAct070(false);
            }
        }
    }

    private void initActions() {

        binding.act078TvOpenPhoneVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +binding.act078TvOpenPhoneVal.getText().toString()));
                //LUCHE - 18/03/2021 - Add trativa que verifica se existe app para resolver a ação
                //se não houve, exibe msg
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else{
                    showMsg(
                        hmAux_Trans.get("alert_no_caller_app_found_ttl"),
                        hmAux_Trans.get("alert_no_caller_app_found_msg")
                    );
                }
            }
        });

        binding.act078TvOpenEmailVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    String[] email = new String[]{binding.act078TvOpenEmailVal.getText().toString()};
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    //LUCHE - 18/03/2021 - Add trativa que verifica se existe app para resolver a ação
                    //se não houve, exibe msg
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }else{
                        showMsg(
                            hmAux_Trans.get("alert_no_email_app_found_ttl"),
                            hmAux_Trans.get("alert_no_email_app_found_msg")
                        );
                    }
                }catch (Exception e){
                    ToolBox.registerException(getClass().getName(), e);
                }
            }
        });

        binding.act078IvOpenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCameraAct();
            }
        });
        //
        binding.act078IvScheduleOpenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCameraAct();
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
                Constant.ACT078
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act078_title");
        transList.add("alert_ticket_parameter_error_ttl");
        transList.add("alert_ticket_parameter_error_msg");
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        transList.add("to_origin_lbl");
        transList.add("to_work_group_edit_lbl");
        transList.add("to_header_edit_lbl");
        transList.add("manual_origin_type_lbl");
        transList.add("barcode_origin_type_lbl");
        transList.add("open_photo_lbl");
        transList.add("open_comment_lbl");
        transList.add("open_username_lbl");
        transList.add("open_email_lbl");
        transList.add("open_phone_lbl");
        //
        transList.add("client_address_ttl");
        transList.add("client_address_street_lbl");
        transList.add("client_address_num_lbl");
        transList.add("client_address_district_lbl");
        transList.add("client_address_city_lbl");
        transList.add("client_address_state_lbl");
        transList.add("client_address_country_lbl");
        transList.add("client_address_complement_lbl");
        transList.add("client_address_zipcode_lbl");
        transList.add("alert_no_navegation_app_found_ttl");
        transList.add("alert_no_navegation_app_found_msg");
        //
        transList.add("alert_wg_edit_need_connection_ttl");
        transList.add("alert_wg_edit_need_connection_msg");
        //
        transList.add("alert_no_email_app_found_ttl");
        transList.add("alert_no_email_app_found_msg");
        transList.add("alert_no_caller_app_found_ttl");
        transList.add("alert_no_caller_app_found_msg");
        //
        transList.add("alert_origin_step_not_found_error_ttl");
        transList.add("alert_origin_step_not_found_error_msg");
        transList.add("measure_origin_type_lbl");
        transList.add("schedule_action_origin_type_lbl");
        transList.add("schedule_ticket_origin_type_lbl");
        transList.add("new_origin_type_lbl");
        transList.add("alert_form_pdf_not_generated_ttl");
        transList.add("alert_form_pdf_not_generated_msg");
        transList.add("alert_form_pdf_not_downloaded_ttl");
        transList.add("alert_form_pdf_not_downloaded_msg");
        transList.add("alert_starting_pdf_not_supported_ttl");
        transList.add("alert_starting_pdf_not_supported_msg");
        transList.add("download_form_pdf_lbl");
        transList.add("action_photo_lbl");
        transList.add("action_comment_lbl");
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
        mAct_Info = Constant.ACT078;
        mAct_Title = Constant.ACT078 + "_title";
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

        setFabMenu(ticket);

        setHeaderFragment(ticket);

        setOpenFields(ticket);

        setContractAndClientFields(ticket);

        setFieldsByOrigin(ticket);
    }

    private void setFieldsByOrigin(TK_Ticket ticket) {
        mPresenter.defineOriginLayoutConfig(ticket);
    }

    private void setContractAndClientFields(final TK_Ticket ticket) {
        //
        if(ticket.getAddress_country() != null && !ticket.getAddress_country().isEmpty()) {
            binding.act078TvClientAddressStreetVal.setText(ticket.getAddress_street());
            binding.act078TvClientAddressNumVal.setText(ticket.getAddress_num());
            binding.act078TvClientAddressDistrictVal.setText(ticket.getAddress_district());
            binding.act078TvClientAddressCityVal.setText(ticket.getAddress_city());
            binding.act078TvClientAddressStateVal.setText(ticket.getAddress_state());
            binding.act078TvClientAddressCountryVal.setText(ticket.getAddress_country());
            binding.act078TvClientAddressComplementVal.setText(ticket.getAddress_complement());
            binding.act078TvClientAddressZipcodeVal.setText(ticket.getAddress_zipcode());
            //
            if (ticket.getAddress_complement() == null || ticket.getAddress_complement().isEmpty()) {
                binding.act078TvClientAddressComplementLbl.setVisibility(View.GONE);
                binding.act078TvClientAddressComplementVal.setVisibility(View.GONE);
            }
            //
            if (ticket.getAddress_zipcode() == null || ticket.getAddress_zipcode().isEmpty()) {
                binding.act078TvClientAddressZipcodeLbl.setVisibility(View.GONE);
                binding.act078TvClientAddressZipcodeVal.setVisibility(View.GONE);
            }
            //
            binding.act078BtnNavegation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callNavegationIntent(
                        mPresenter.getNavegationIntentData(ticket)
                    );
                }
            });
        }else{
            binding.act078ClClientAddress.setVisibility(View.GONE);
        }
    }

    private void callNavegationIntent(String navegationInfo){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Uri geoLocation = Uri.parse("geo:0,0?q=Alameda+São+Bernardo+268+09210725+Santo+André");
        Uri geoLocation = Uri.parse(navegationInfo);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            showMsg(
                hmAux_Trans.get("alert_no_navegation_app_found_ttl"),
                hmAux_Trans.get("alert_no_navegation_app_found_msg")
            );
        }
    }

    private void setOpenFields(TK_Ticket ticket) {
        binding.act078LlPrivacyFields.setVisibility(View.GONE);
        //
        if (ticket.getApp_personal_data() == 1){
            binding.act078LlPrivacyFields.setVisibility(View.VISIBLE);
            if(ticket.getOpen_name()==null || ticket.getOpen_name().isEmpty()){
                binding.act078LlOpenUsername.setVisibility(View.GONE);
            }else{
                binding.act078TvOpenUsernameVal.setText(ticket.getOpen_name());
            }
            //
            if(ticket.getOpen_email()==null || ticket.getOpen_email().isEmpty()){
                binding.act078LlOpenEmail.setVisibility(View.GONE);
            }else{
                binding.act078TvOpenEmailVal.setText(ticket.getOpen_email());
            }
            //
            if(ticket.getOpen_phone()==null || ticket.getOpen_phone().isEmpty()){
                binding.act078LlOpenPhone.setVisibility(View.GONE);
            }else{
                binding.act078TvOpenPhoneVal.setText(ticket.getOpen_phone());
            }
        }
        //
        if(ticket.getOpen_comments()==null || ticket.getOpen_comments().isEmpty()){
            binding.act078LlOpenComment.setVisibility(View.GONE);
        }else{
            binding.act078TvOpenCommentVal.setText(ticket.getOpen_comments());
        }
        //

        actionPhotoLocalPath = ticket.getOpen_photo_local();
            if (actionPhotoLocalPath == null && (ticket.getOpen_photo() == null || ticket.getOpen_photo().isEmpty())) {
                binding.act078LlOpenPhoto.setVisibility(View.GONE);
            } else {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                    if (bitmap == null) {
                        setImagePlaceholder(binding.act078IvOpenPhoto);
                    } else {
                        binding.act078IvOpenPhoto.setImageBitmap(bitmap);
                    }
                } catch (NullPointerException e) {
                    setImagePlaceholder(binding.act078IvOpenPhoto);
                    ToolBox_Inf.registerException(getClass().getName(), e);
                    e.printStackTrace();
                }
            }
    }

    private void setImagePlaceholder(ImageView imageView) {
        Drawable dPlaceholder = getResources().getDrawable(R.drawable.sand_watch_transp);
        dPlaceholder.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
        imageView.setImageDrawable(dPlaceholder);
    }


    private void callAct082() {
        Intent intent = new Intent(context, Act082_Main.class);
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

    private void callCameraAct() {
        File sFile;
        sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);

        if (!sFile.exists()) {
            return;
        }
        //
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantBase.PID, binding.act078IvOpenPhoto.getId());
        bundle.putInt(ConstantBase.PTYPE, 1);
        bundle.putString(ConstantBase.PPATH, actionPhotoLocalPath);
        bundle.putBoolean(ConstantBase.PEDIT, false);
        bundle.putBoolean(ConstantBase.PENABLED, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_GALLERY, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false);
        bundle.putString(ConstantBase.FILE_AUTHORITIES, ConstantBase.AUTHORITIES_FOR_PROVIDER);
        //
        Intent mIntent = new Intent(context, Camera_Activity.class);
        mIntent.putExtras(bundle);
        //
        context.startActivity(mIntent);
    }

    public void setMeasureLayout(TK_Ticket tkTicket, boolean isTicketOrigin){
        if(isTicketOrigin){
            setFormInfos(tkTicket);
        }else{
            binding.act078LlMeasureInfo.setVisibility(View.GONE);
        }
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
                            binding.act078TvFormScore.setText(mPresenter.getFormatedScorePerc(form));
                            int color = context.getResources().getColor(ToolBox_Inf.getScoreFormColor(form.getScore_status()));
                            binding.act078TvFormScore.setTextColor(color);
                            binding.act078IvFormScore.setImageTintList(ColorStateList.valueOf(color));
                        } else {
                            binding.act078TvFormScore.setVisibility(View.GONE);
                            binding.act078IvFormScore.setVisibility(View.GONE);
                        }
                        if(form.getNc() > 0 ) {
                            binding.act078TvFormNcCount.setText(String.format("%s", form.getNc()));
                        }else{
                            binding.act078TvFormNcCount.setVisibility(View.GONE);
                            binding.act078IvFormNcCount.setVisibility(View.GONE);
                        }

                    } catch (NullPointerException e) {
                        ToolBox_Inf.registerException(getClass().getName(), e);
                        hideFormInfo();
                    }
                    binding.act078TvFormDownloadPdf.setOnClickListener(new View.OnClickListener() {
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
        binding.act078TvFormScore.setVisibility(View.GONE);
        binding.act078IvFormScore.setVisibility(View.GONE);
        binding.act078TvFormNcCount.setVisibility(View.GONE);
        binding.act078IvFormNcCount.setVisibility(View.GONE);
        binding.act078TvFormDownloadPdf.setVisibility(View.GONE);
        binding.act078IvFormDownloadPdf.setVisibility(View.GONE);
    }

    public void setScheduleLayout(TK_Ticket ticket, boolean isTicketOrigin, boolean isScheduleAction){
        if(isTicketOrigin && isScheduleAction){
            TK_Ticket_Step originStep = ticket.getStep().get(0);
            if (originStep != null && originStep.getCtrl() != null && originStep.getCtrl().size() > 0) {
                TK_Ticket_Ctrl originCtrl = originStep.getCtrl().get(0);
                if (originCtrl != null) {
                    TK_Ticket_Action action = originCtrl.getAction();
                    if (action != null) {
                        if (action.getAction_comments() == null || action.getAction_comments().isEmpty()) {
                            binding.act078LlScheduleOpenComment.setVisibility(View.GONE);
                        } else {
                            binding.act078TvScheduleOpenCommentVal.setText(action.getAction_comments());
                        }
                        actionPhotoLocalPath = action.getAction_photo_local();
                        if ((actionPhotoLocalPath == null || actionPhotoLocalPath.isEmpty())
                            && (action.getAction_photo_url() == null || action.getAction_photo_url().isEmpty())
                            && (action.getAction_photo_name() == null || action.getAction_photo_name().isEmpty())
                        ) {
                            binding.act078LlScheduleOpenPhoto.setVisibility(View.GONE);
                        } else {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeFile(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                                if (bitmap == null) {
                                   setImagePlaceholder(binding.act078IvScheduleOpenPhoto);
                                } else {
                                    binding.act078IvScheduleOpenPhoto.setImageBitmap(bitmap);
                                }
                            } catch (NullPointerException e) {
                                setImagePlaceholder(binding.act078IvScheduleOpenPhoto);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }else{
            binding.act078LlScheduleInfo.setVisibility(View.GONE);
        }
    }

}