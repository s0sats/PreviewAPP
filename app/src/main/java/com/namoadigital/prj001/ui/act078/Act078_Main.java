package com.namoadigital.prj001.ui.act078;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
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

import static com.namoadigital.prj001.ui.act075.Act075_Main.PRODUCT_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.VIEW_PROFILE;

public class Act078_Main extends Base_Activity_Frag implements Act078_Main_Contract.I_View {

    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private FabMenu fabMenu;
    private boolean hasFABActive = false;
    private Act078_Main_Presenter mPresenter;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    private TextView tv_open_photo_lbl;
    private ImageView iv_open_photo;
    private TextView tv_open_comment_lbl;
    private TextView tv_open_comment_val;
    private LinearLayout ll_privacy_fields;
    private TextView tv_open_username_lbl;
    private TextView tv_open_username_val;
    private TextView tv_open_email_lbl;
    private TextView tv_open_email_val;
    private TextView tv_open_phone_lbl;
    private TextView tv_open_phone_val;

    private LinearLayout ll_open_phone;
    private LinearLayout ll_open_email;
    private LinearLayout ll_open_username;
    private LinearLayout ll_open_comment;
    private LinearLayout ll_open_photo;
    private String actionPhotoLocalPath;
    //Implementação contrato e cliente
    private ConstraintLayout cl_client_address;
    private TextView tv_client_address_ttl;
    private TextView tv_client_address_street_lbl;
    private TextView tv_client_address_street_val;
    private TextView tv_client_address_num_lbl;
    private TextView tv_client_address_num_val;
    private TextView tv_client_address_district_lbl;
    private TextView tv_client_address_district_val;
    private ImageButton btn_navegation;
    private TextView tv_client_address_city_lbl;
    private TextView tv_client_address_city_val;
    private TextView tv_client_address_state_lbl;
    private TextView tv_client_address_state_val;
    private TextView tv_client_address_country_lbl;
    private TextView tv_client_address_country_val;
    private TextView tv_client_address_complement_lbl;
    private TextView tv_client_address_complement_val;
    private TextView tv_client_address_zipcode_lbl;
    private TextView tv_client_address_zipcode_val;
    private boolean is_from_edit_header;
    private boolean is_from_edit_workgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act078_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        bindViews();
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void bindViews() {
        fabMenu = (FabMenu) findViewById(R.id.act078_fabMenu_anchor);

        ll_open_phone = findViewById(R.id.act078_ll_open_phone);
        ll_open_email = findViewById(R.id.act078_ll_open_email);
        ll_open_username = findViewById(R.id.act078_ll_open_username);
        ll_open_comment = findViewById(R.id.act078_ll_open_comment);
        ll_open_photo = findViewById(R.id.act078_ll_open_photo);

        tv_open_photo_lbl = findViewById(R.id.act078_tv_open_photo_lbl);
        iv_open_photo = findViewById(R.id.act078_iv_open_photo);
        tv_open_comment_lbl = findViewById(R.id.act078_tv_open_comment_lbl);
        tv_open_comment_val = findViewById(R.id.act078_tv_open_comment_val);
        ll_privacy_fields = findViewById(R.id.act078_ll_privacy_fields);
        tv_open_username_lbl = findViewById(R.id.act078_tv_open_username_lbl);
        tv_open_username_val = findViewById(R.id.act078_tv_open_username_val);
        tv_open_email_lbl = findViewById(R.id.act078_tv_open_email_lbl);
        tv_open_email_val = findViewById(R.id.act078_tv_open_email_val);
        tv_open_phone_lbl = findViewById(R.id.act078_tv_open_phone_lbl);
        tv_open_phone_val = findViewById(R.id.act078_tv_open_phone_val);
        //Campos contract e client
        cl_client_address = findViewById(R.id.act078_cl_client_address);
        tv_client_address_ttl = findViewById(R.id.act078_tv_client_address_ttl);
        tv_client_address_street_lbl = findViewById(R.id.act078_tv_client_address_street_lbl);
        tv_client_address_street_val = findViewById(R.id.act078_tv_client_address_street_val);
        tv_client_address_num_lbl = findViewById(R.id.act078_tv_client_address_num_lbl);
        tv_client_address_num_val = findViewById(R.id.act078_tv_client_address_num_val);
        tv_client_address_district_lbl = findViewById(R.id.act078_tv_client_address_district_lbl);
        tv_client_address_district_val = findViewById(R.id.act078_tv_client_address_district_val);
        btn_navegation = findViewById(R.id.act078_btn_navegation);
        tv_client_address_city_lbl = findViewById(R.id.act078_tv_client_address_city_lbl);
        tv_client_address_city_val = findViewById(R.id.act078_tv_client_address_city_val);
        tv_client_address_state_lbl = findViewById(R.id.act078_tv_client_address_state_lbl);
        tv_client_address_state_val = findViewById(R.id.act078_tv_client_address_state_val);
        tv_client_address_country_lbl = findViewById(R.id.act078_tv_client_address_country_lbl);
        tv_client_address_country_val = findViewById(R.id.act078_tv_client_address_country_val);
        tv_client_address_complement_lbl = findViewById(R.id.act078_tv_client_address_complement_lbl);
        tv_client_address_complement_val = findViewById(R.id.act078_tv_client_address_complement_val);
        tv_client_address_zipcode_lbl = findViewById(R.id.act078_tv_client_address_zipcode_lbl);
        tv_client_address_zipcode_val = findViewById(R.id.act078_tv_client_address_zipcode_val);
    }

    private void initVars() {
        mPresenter = new Act078_Main_Presenter(context, this, hmAux_Trans);
        //
        recoverIntentsInfo();
        //
        setLabels();
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
            mPresenter.getStepOrigin(mTkPrefix, mTkCode);
            //
        }
    }

    private void setFabMenu(TK_Ticket mTicket) {
        //LUCHE - 16/12/2020
        //Quando em edição, o fab não deve ser exibido
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
                });
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

    private void setLabels() {
        tv_open_photo_lbl.setText(hmAux_Trans.get("open_photo_lbl"));
        tv_open_comment_lbl.setText(hmAux_Trans.get("open_comment_lbl"));
        tv_open_username_lbl.setText(hmAux_Trans.get("open_username_lbl"));
        tv_open_email_lbl.setText(hmAux_Trans.get("open_email_lbl"));
        tv_open_phone_lbl.setText(hmAux_Trans.get("open_phone_lbl"));
        //
        tv_client_address_ttl.setText(hmAux_Trans.get("client_address_ttl"));
        tv_client_address_street_lbl.setText(hmAux_Trans.get("client_address_street_lbl"));
        tv_client_address_num_lbl.setText(hmAux_Trans.get("client_address_num_lbl"));
        tv_client_address_district_lbl.setText(hmAux_Trans.get("client_address_district_lbl"));
        tv_client_address_city_lbl.setText(hmAux_Trans.get("client_address_city_lbl"));
        tv_client_address_state_lbl.setText(hmAux_Trans.get("client_address_state_lbl"));
        tv_client_address_country_lbl.setText(hmAux_Trans.get("client_address_country_lbl"));
        tv_client_address_complement_lbl.setText(hmAux_Trans.get("client_address_complement_lbl"));
        tv_client_address_zipcode_lbl.setText(hmAux_Trans.get("client_address_zipcode_lbl"));
    }


    private void setHeaderFragment(TK_Ticket tkTicket) {
        fm = getSupportFragmentManager();
        String origin_type = "";
        if(ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE.equalsIgnoreCase(tkTicket.getOrigin_type())) {
            origin_type = hmAux_Trans.get("barcode_origin_type_lbl");
        }else{
            origin_type = hmAux_Trans.get("manual_origin_type_lbl");
        }

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
                origin_type,
                context.getResources().getColor(R.color.grid_header_normal),
                tkTicket.getType_path(),
                tkTicket.getType_desc(),
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

    private void initActions() {

        tv_open_phone_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +tv_open_phone_val.getText().toString()));
                //LUCHE - 18/03/2021 - Add trativa que verifica se existe app para resolver a ação
                //se não houve, exibe msg
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else{
                    ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_no_caller_app_found_ttl"),
                        hmAux_Trans.get("alert_no_caller_app_found_msg"),
                        null,
                        0
                    );
                }
            }
        });

        tv_open_email_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    String[] email = new String[]{tv_open_email_val.getText().toString()};
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    //LUCHE - 18/03/2021 - Add trativa que verifica se existe app para resolver a ação
                    //se não houve, exibe msg
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }else{
                        ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_email_app_found_ttl"),
                            hmAux_Trans.get("alert_no_email_app_found_msg"),
                            null,
                            0
                        );
                    }
                }catch (Exception e){
                    ToolBox.registerException(getClass().getName(), e);
                }
            }
        });

        iv_open_photo.setOnClickListener(new View.OnClickListener() {
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
    }

    private void setContractAndClientFields(final TK_Ticket ticket) {
        //
        if(ticket.getAddress_country() != null && !ticket.getAddress_country().isEmpty()) {
            tv_client_address_street_val.setText(ticket.getAddress_street());
            tv_client_address_num_val.setText(ticket.getAddress_num());
            tv_client_address_district_val.setText(ticket.getAddress_district());
            tv_client_address_city_val.setText(ticket.getAddress_city());
            tv_client_address_state_val.setText(ticket.getAddress_state());
            tv_client_address_country_val.setText(ticket.getAddress_country());
            tv_client_address_complement_val.setText(ticket.getAddress_complement());
            tv_client_address_zipcode_val.setText(ticket.getAddress_zipcode());
            //
            if (ticket.getAddress_complement() == null || ticket.getAddress_complement().isEmpty()) {
                tv_client_address_complement_lbl.setVisibility(View.GONE);
                tv_client_address_complement_val.setVisibility(View.GONE);
            }
            //
            if (ticket.getAddress_zipcode() == null || ticket.getAddress_zipcode().isEmpty()) {
                tv_client_address_zipcode_lbl.setVisibility(View.GONE);
                tv_client_address_zipcode_val.setVisibility(View.GONE);
            }
            //
            btn_navegation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callNavegationIntent(
                        mPresenter.getNavegationIntentData(ticket)
                    );
                }
            });
        }else{
            cl_client_address.setVisibility(View.GONE);
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
            ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_no_navegation_app_found_ttl"),
                hmAux_Trans.get("alert_no_navegation_app_found_msg"),
                null,
                0
            );
        }
    }

    private void setOpenFields(TK_Ticket ticket) {
        ll_privacy_fields.setVisibility(View.GONE);
        //
        if (ticket.getApp_personal_data() == 1){
            ll_privacy_fields.setVisibility(View.VISIBLE);
            if(ticket.getOpen_name()==null || ticket.getOpen_name().isEmpty()){
                ll_open_username.setVisibility(View.GONE);
            }else{
                tv_open_username_val.setText(ticket.getOpen_name());
            }
            //
            if(ticket.getOpen_email()==null || ticket.getOpen_email().isEmpty()){
                ll_open_email.setVisibility(View.GONE);
            }else{
                tv_open_email_val.setText(ticket.getOpen_email());
            }
            //
            if(ticket.getOpen_phone()==null || ticket.getOpen_phone().isEmpty()){
                ll_open_phone.setVisibility(View.GONE);
            }else{
                tv_open_phone_val.setText(ticket.getOpen_phone());
            }
        }
        //
        if(ticket.getOpen_comments()==null || ticket.getOpen_comments().isEmpty()){
            ll_open_comment.setVisibility(View.GONE);
        }else{
            tv_open_comment_val.setText(ticket.getOpen_comments());
        }
        //

        actionPhotoLocalPath = ticket.getOpen_photo_local();
        if (actionPhotoLocalPath == null && (ticket.getOpen_photo() == null || ticket.getOpen_photo().isEmpty()) ) {
            ll_open_photo.setVisibility(View.GONE);
        }else {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                if (bitmap == null) {
                    Drawable dPlaceholder = getResources().getDrawable(R.drawable.sand_watch_transp);
                    dPlaceholder.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
                    iv_open_photo.setImageDrawable(dPlaceholder);
                } else {
                    iv_open_photo.setImageBitmap(bitmap);
                }
            } catch (NullPointerException e) {
                ToolBox_Inf.registerException(e);
                e.printStackTrace();
            }
        }
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
        bundle.putInt(ConstantBase.PID, iv_open_photo.getId());
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


}