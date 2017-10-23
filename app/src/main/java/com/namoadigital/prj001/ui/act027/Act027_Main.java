package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoa_digital.namoa_library.view.SignaTure_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Results_Adapter;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_SO_Approval;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.receiver.WBR_UserAuthor;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.sql.SM_SO_File_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_File_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_File_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_012;
import com.namoadigital.prj001.sql.SM_SO_Sql_014;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act009.Act009_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act028.Act028_Main_New;
import com.namoadigital.prj001.ui.act032.Act032_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.namoa_digital.namoa_library.util.ConstantBase.CACHE_PATH_PHOTO;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Main extends Base_Activity_Frag_NFC_Geral implements Act027_Main_View, Act027_Opc.IAct027_Opc, Act027_Services.IAct027_Services {

    public static final String SELECTION_SERVICES = "SERVICES";
    public static final String SELECTION_SERIAL = "SERIAL";
    public static final String SELECTION_HEADER = "HEADER";
    public static final String SELECTION_APPROVAL = "APPROVAL";

    public static final String SELECTION_EXPRESS = "EXPRESS";
    public static final String SELECTION_NORMAL = "NORMAL";

    public static final String WS_PROCESS_USER_AUTHOR = "WS_PROCESS_USER_AUTHOR";
    public static final String WS_PROCESS_SO_SAVE_APPROVAL = "WS_PROCESS_SO_SAVE_APPROVAL";
    public static final String WS_SEARCH_TRACKING = "WS_SEARCH_TRACKING";
    public static final String WS_PROCESS_SERIAL = "WS_PROCESS_SERIAL";
    public static final String WS_PROCESS_SO_SYNC = "WS_PROCESS_SO_SYNC";
    public static final String WS_PROCESS_SO_SAVE = "WS_PROCESS_SO_SAVE";
    public static final String WS_PROCESS_N_FORM_SYNC = "WS_PROCESS_N_FORM_SYNC";

    public static final int WS_PROCESS_APPROVAL_NOT = 0;
    public static final int WS_PROCESS_APPROVAL_ON_SIGNATURE = 1;
    public static final int WS_PROCESS_APPROVAL_ON_LINE = 2;

    private Context context;
    private Bundle bundle;

    private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager fm;

    private Act027_Opc act027_opc_;
    private Act027_Approval act027_approval_;
    private Act027_Services act027_services_;
    private Act027_Serial act027_serial_;
    private Act027_Header act027_header_;

    private SM_SODao sm_soDao;
    private SM_SO mSm_so;

    private String ws_process = "";
    private String ws_process_approval_status = "";

    private boolean only_save = false;
    private boolean only_approval = false;

    private String lastServiceReturned = "";
    private boolean ws_call_next_ctrl = true;

    private String sFileNameSignature = "";

    private Sync_ChecklistDao syncChecklistDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act027_main_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniSetup();
        initVars();
        iniUIFooter();
        initActions();
    }

    @Override
    protected void onDestroy() {
        ToolBox_Con.setApproval_Type(context, "");

        super.onDestroy();
    }

    private void iniSetup() {
        context = Act027_Main.this;

        fm = getSupportFragmentManager();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT027
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act027_title");
        transList.add("alert_so_exit_title");
        transList.add("alert_so_exit_msg");
        transList.add("alert_so_ttl");
        transList.add("msg_so_save_ok");
        transList.add("alert_author_validation");
        transList.add("alert_no_profile_ttl");
        transList.add("alert_no_profile_msg");

        // ACT027_Opc Fragment
        transList.add("so_lbl");
        transList.add("so_id_lbl");
        transList.add("so_code_lbl");
        transList.add("product_lbl");
        transList.add("product_id_lbl");
        transList.add("product_header_lbl");
        transList.add("product_id_header_lbl");

        transList.add("product_description_lbl");
        transList.add("serial_lbl");
        transList.add("deadline_lbl");
        transList.add("status_lbl");
        transList.add("priority_lbl");

        transList.add("approval_ll_lbl");
        transList.add("services_ll_lbl");
        transList.add("serial_ll_lbl");
        transList.add("header_ll_lbl");

        // ACT027_Approval Fragment
        transList.add("user_name_lbl");
        transList.add("user_lbl");
        transList.add("other_lbl");
        transList.add("approval_nfc_lbl");
        transList.add("approval_user_password_lbl");
        transList.add("approval_signature_lbl");
        transList.add("alert_no_name_ttl");
        transList.add("alert_no_name_msg");
        transList.add("alert_so_signature_ttl");
        transList.add("alert_so_signature_msg");
        transList.add("dialog_credentials_ttl");
        transList.add("dialog_credentials_msg");

        transList.add("so_client_approval_quality_type_lbl");
        transList.add("client_approval_quality_user_nick_lbl");
        transList.add("client_approval_quality_date_lbl");

        transList.add("dialog_user_author_ttl");
        transList.add("dialog_user_author_lbl");
        transList.add("dialog_user_author_pwd_lbl");
        transList.add("dialog_user_author_btn");

        transList.add("quality_approval_lbl");
        transList.add("quality_approval_user_nick_lbl");
        transList.add("quality_approval_date_lbl");

        // ACT027_Serial Fragment
        transList.add("alert_no_connection_title");
        transList.add("alert_no_connection_msg");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_start_sync_title");
        transList.add("alert_start_sync_msg");
        transList.add("alert_start_serial_title");
        transList.add("alert_start_serial_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_no_serial_typed_title");
        transList.add("alert_no_serial_typed_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("product_ttl");
        transList.add("mket_search_hint");
        transList.add("product_label");
        transList.add("product_id_label");
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("btn_create");
        transList.add("serial_ttl");
        transList.add("serial_location_ttl");
        transList.add("site_lbl");
        transList.add("site_zone_lbl");
        transList.add("site_zone_local_lbl");
        transList.add("serial_add_info_ttl");
        transList.add("add_info1_lbl");
        transList.add("add_info2_lbl");
        transList.add("add_info3_lbl");
        transList.add("serial_properties_ttl");
        transList.add("brand_lbl");
        transList.add("brand_model_lbl");
        transList.add("brand_color_lbl");
        transList.add("segment_lbl");
        transList.add("category_price_lbl");
        transList.add("site_owner_lbl");
        transList.add("btn_serial_search");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("btn_serial_save");
        //
        transList.add("spinner_site_lbl");
        transList.add("spinner_zone_lbl");
        transList.add("spinner_local_lbl");
        //ACT027_Serial Tracking
        transList.add("tracking_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_tracking_unavailable_ttl");
        transList.add("alert_tracking_unavailable_msg");
        transList.add("alert_no_site_selected_ttl");
        transList.add("alert_no_site_selected_msg");
        transList.add("dialog_tracking_ttl");
        transList.add("alert_tracking_already_listed_ttl");
        transList.add("alert_tracking_already_listed_msg");
        transList.add("alert_clear_tracking_list_ttl");
        transList.add("alert_clear_tracking_list_msg");
        transList.add("alert_keep_tracking_list_ttl");
        transList.add("alert_keep_tracking_list_msg");
        transList.add("alert_invalid_serial_local_ttl");
        transList.add("alert_invalid_serial_local_msg");

        // ACT027_Header Fragment
        transList.add("so_id");
        transList.add("so_desc");
        transList.add("prefix_code");
        transList.add("serial");
        transList.add("category_price_id");
        transList.add("category_price_desc");
        transList.add("segment_id");
        transList.add("segment_desc");
        transList.add("site_id");
        transList.add("site_desc");
        transList.add("operation_id");
        transList.add("operation_desc");
        transList.add("deadline");
        transList.add("status");
        transList.add("priority_desc");
        transList.add("contract_desc");
        transList.add("contract_po_erp");
        transList.add("contract_po_client1");
        transList.add("contract_po_client2");
        transList.add("quality_approval_user");
        transList.add("quality_approval_user_nick");
        transList.add("quality_approval_date");
        transList.add("comments");
        transList.add("client_type");
        transList.add("client_user");
        transList.add("client_code");
        transList.add("client_id");
        transList.add("client_name");
        transList.add("client_email");
        transList.add("client_phone");
        transList.add("client_approval_date");
        transList.add("client_approval_user");
        transList.add("client_approval_user_nick");
        transList.add("total_qty_service");
        transList.add("total_price");
        transList.add("alert_no_data_changes_ttl");
        transList.add("alert_no_data_changes_msg");
        transList.add("progress_save_serial_ttl");
        transList.add("progress_save_serial_msg");
        transList.add("searchable_spinner_lbl");
        transList.add("add_info1_ttl");
        transList.add("add_info2_ttl");
        transList.add("add_info3_ttl");
        //Service Fragment
        transList.add("filter_lbl");
        transList.add("select_partner_lbl");
        transList.add("alert_start_task_confirm_ttl");
        transList.add("alert_start_task_confirm_msg");
        transList.add("alert_partner_selection_ttl");
        transList.add("alert_no_partner_selected_msg");
        transList.add("alert_no_partner_found_msg");
        transList.add("partner_selection_ttl");
        transList.add("partner_search_lbl");
        transList.add("tracking_num_lbl");
        transList.add("alert_goto_service_menu_ttl");
        transList.add("alert_goto_service_menu_msg");
        //N-Form Creation
        transList.add("toolbar_n_form_lbl");
        transList.add("alert_open_n_form_ttl");
        transList.add("alert_open_n_form_msg");
        transList.add("progress_n_form_sync_ttl");
        transList.add("progress_n_form_sync_msg");

        //
        sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        ToolBox_Inf.libTranslation(context);
    }

    private void initVars() {
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.act027_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                Act027_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                act027_opc_.loadDataToScreen();

                ActivityCompat.invalidateOptionsMenu(Act027_Main.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                ActivityCompat.invalidateOptionsMenu(Act027_Main.this);

            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recoverGetIntents();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Drawer Opc
        act027_opc_ = (Act027_Opc) fm.findFragmentById(R.id.act027_opc);
        // Dialog Acess
        act027_opc_.setBaInfra(this);
        // Translation Access
        act027_opc_.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_opc_.setmSm_so(mSm_so);
        //
        act027_opc_.setOnMenuOptionsSelected(this);

        // Approval
        act027_approval_ = new Act027_Approval();
        // Dialog Acess
        act027_approval_.setBaInfra(this);
        // Translation Access
        act027_approval_.setHmAux_Trans(hmAux_Trans);
        //
        act027_approval_.setListener(actionBTN);
        //
        act027_approval_.setmSm_so(mSm_so);

        // Services
        act027_services_ = new Act027_Services();
        // Dialog Acess
        act027_services_.setBaInfra(this);
        // Translation Access
        act027_services_.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_services_.setmSm_so(mSm_so);
        //
        act027_services_.setOnServiceSelectedListener(this);
        //Se retorno da task, seta qual serviço deve ser mostrado
        act027_services_.setLastServiceUpdated(lastServiceReturned);
        // Serial
        act027_serial_ = new Act027_Serial();
        // Dialog Acess
        act027_serial_.setBaInfra(this);
        // Translation Access
        act027_serial_.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_serial_.setmSm_so(mSm_so);

        // header
        act027_header_ = new Act027_Header();
        // Dialog Acess
        act027_header_.setBaInfra(this);
        // Translation Access
        act027_header_.setHmAux_Trans(hmAux_Trans);
        // SO Acess
        act027_header_.setmSm_so(mSm_so);
        //
        controls_frags.add(act027_serial_);
        //
        setFrag(act027_services_, "SERVICES");
        //
        syncChecklistDao = new Sync_ChecklistDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        checkSOAttachExists();
    }

    private void checkSOAttachExists() {
        if(!WBR_DownLoad_PDF.IS_RUNNING) {
            SM_SO_FileDao soFileDao =
                    new SM_SO_FileDao(
                            getApplicationContext(),
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                            Constant.DB_VERSION_CUSTOM
                    );

            ArrayList<HMAux> so_file_list = new ArrayList<>();
            //Carrega lista de files do cabealho da SO
            so_file_list.addAll(
                    soFileDao.query_HM(
                            new SM_SO_File_Sql_005(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    mSm_so.getSo_prefix(),
                                    mSm_so.getSo_code()
                            ).toSqlQuery()
                    )
            );
            //
            String splitKey = "@#My#@Key#@";
            for (HMAux hmAux : so_file_list) {
                String fileName = hmAux.get(SM_SO_FileDao.FILE_NAME).replace(".", splitKey);
                String[] nameSplited = fileName.split(splitKey);
                String ext = "." + nameSplited[nameSplited.length - 1];
                //
                if (ToolBox_Inf.verifyDownloadFileInf(hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME).toLowerCase() + ext)) {
                    soFileDao.addUpdate(
                            new SM_SO_File_Sql_004(
                                    hmAux.get(SM_SO_FileDao.CUSTOMER_CODE),
                                    hmAux.get(SM_SO_FileDao.SO_PREFIX),
                                    hmAux.get(SM_SO_FileDao.SO_CODE),
                                    hmAux.get(SM_SO_FileDao.FILE_CODE),
                                    hmAux.get(SM_SO_File_Sql_003.FILE_LOCAL_NAME) + ext
                            ).toSqlQuery().toLowerCase()
                    );
                }
            }
        }
    }

    //region Recover Intent Parameters
    private void recoverGetIntents() {
        bundle = getIntent().getExtras();

        if (bundle != null) {
            mSm_so = loadSM_SO(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX)),
                    Integer.parseInt(bundle.getString(SM_SODao.SO_CODE))
            );
            //
            if (mSm_so != null && mSm_so.getClient_approval_image_name() != null && !mSm_so.getClient_approval_image_name().isEmpty()) {
                new DownloadSignature().execute(mSm_so.getClient_approval_image_url());
            }

            if (mSm_so != null) {
                if (mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_QUALITY) ||
                        mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_CLIENT)) {
                    ToolBox_Con.setApproval_Type(context, mSm_so.getStatus());
                } else {
                    ToolBox_Con.setApproval_Type(context, "");
                }
            }
            //
            lastServiceReturned = bundle.getString(Constant.ACT028_SERVICE_UPDATED, "");
        } else {
            mSm_so = null;
        }
    }

    private SM_SO loadSM_SO(long customer_code, int so_prefix, int so_code) {
        sm_soDao.remove(
                new SM_SO_Sql_014(ToolBox_Con.getApproval_Type(context)).toSqlQuery()
        );

        SM_SO mSm_so = sm_soDao.getByString(
                new SM_SO_Sql_001(
                        customer_code,
                        so_prefix,
                        so_code
                ).toSqlQuery()
        );

        // Recarregar o Status da SO;
        if (mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_QUALITY) ||
                mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_CLIENT)) {
            ToolBox_Con.setApproval_Type(context, mSm_so.getStatus());
        } else {
            ToolBox_Con.setApproval_Type(context, "");
        }

        return mSm_so;
    }
    //endregion

    private void iniUIFooter() {
        iniFooter();

        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT027;
        mAct_Title = Constant.ACT027 + "_" + "title";

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

    //region SERVICE RETURN HANDLING
    // TRATAVIA DE ERROS ESPECIFICOS
    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        if (ws_process_approval_status.equalsIgnoreCase(WS_PROCESS_SO_SAVE_APPROVAL)) {
            setWs_process("");
            only_save = false;

            if (mSm_so.getApproval_required() == WS_PROCESS_APPROVAL_ON_LINE) {
                recoverApprovalState();
            }
        }

        progressDialog.dismiss();
    }


    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);

        if (mSm_so.getApproval_required() == WS_PROCESS_APPROVAL_ON_LINE) {
            recoverApprovalState();
        }
    }

    // TRATAVIA DE ENCERRAMENTO SEM PROBLEMAS DO SERVICO
    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (ws_process.equals(WS_PROCESS_SO_SYNC)) {

            progressDialog.dismiss();

            setWs_process("");
            processSoDownloadResult(hmAux);

        } else if (ws_process.equals(WS_PROCESS_SO_SAVE)) {

            progressDialog.dismiss();

            setWs_process("");
            processSoSave(hmAux);

        } else if (ws_process.equalsIgnoreCase(WS_PROCESS_SO_SAVE_APPROVAL)) {

            progressDialog.dismiss();

            setWs_process("");
            processSoApproval(hmAux);

        } else if (ws_process.equalsIgnoreCase(WS_PROCESS_USER_AUTHOR)) {

            progressDialog.dismiss();
            //
            setWs_process("");
            processUserAuthorCheck(hmAux);

        } else if (ws_process.equalsIgnoreCase(WS_SEARCH_TRACKING)) {
            progressDialog.dismiss();
            //
            setWs_process("");
            act027_serial_.callProcessTrackingResult(hmAux);
        } else {
            act027_serial_.callProcessSerialSaveResult(String.valueOf(mSm_so.getProduct_code()), mSm_so.getSerial_id(), hmAux);
            progressDialog.dismiss();
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        progressDialog.dismiss();
        //
        if(ws_process.equals(WS_PROCESS_N_FORM_SYNC)){
            setWs_process("");
            //
            updateSyncChecklist();
            //
            callAct009();
        }
    }

    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }
//    public void addControlToList(MKEditTextNM mket_tracking){
//        controls_sta.add(mket_tracking);
//    }
//
//    public void removeControlToList(MKEditTextNM mket_tracking){
//        controls_sta.remove(mket_tracking);
//    }


    //Variavel que determina se salva so e sincroniza ou só salva.
    //Após implementação do atalho, foi necessario criar essa var pra pular o syncronismo.
    public void setOnly_save(boolean only_save) {
        this.only_save = only_save;
    }

    private void processSoDownloadResult(HMAux so_download_result) {
        if (so_download_result.containsKey(WS_SO_Search.SO_PREFIX_CODE) && so_download_result.containsKey(WS_SO_Search.SO_LIST_QTY)) {
            if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_invalid_status_ttl"),
                        hmAux_Trans.get("alert_so_invalid_status_msg"),
                        null,
                        0
                );

            } else if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 1) {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_sync_ok_ttl"),
                        hmAux_Trans.get("alert_so_sync_ok_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refreshUI();
                            }
                        },
                        0
                );

            } else {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("alert_so_sync_ok_ttl"),
                        hmAux_Trans.get("alert_so_sync_ok_msg"),
                        null,
                        0
                );
            }
        } else {
            // ToolBox_Inf.alertBundleNotFound(this,hmAux_Trans);
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_sync_param_error_ttl"),
                    hmAux_Trans.get("alert_so_sync_param_error_msg"),
                    null,
                    0
            );
        }

    }

    public void refreshUI() {

        mSm_so = loadSM_SO(
                mSm_so.getCustomer_code(),
                mSm_so.getSo_prefix(),
                mSm_so.getSo_code()
        );

        act027_opc_.setmSm_so(mSm_so);
        act027_opc_.loadDataToScreen();

        act027_approval_.setmSm_so(mSm_so);
        act027_approval_.loadDataToScreen();

        act027_services_.setmSm_so(mSm_so);
        act027_services_.loadDataToScreen();

        act027_serial_.setmSm_so(mSm_so);
        act027_serial_.loadDataToScreen();

        act027_header_.setmSm_so(mSm_so);
        act027_header_.loadDataToScreen();
        //
        startDownloadServices();
    }

    //region Ciclo Normal
    private void processSoSave(HMAux hmAux) {
        //Tratativa para quando WS chamado e sem nenhuma s.o para atualizar.
        if (hmAux.containsKey(WS_SO_Save.SO_NO_EMPTY_LIST)) {
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_starting_sync"), "", "0");
            //
            executeSoSaveApproval();
            //executeSoSync(mSm_so.getSo_prefix(), mSm_so.getSo_code());
        } else {
            String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            showResults(so);
        }
    }

    private void showResults(String[] so) {
        ArrayList<HMAux> sos = new ArrayList<>();
        for (int i = 0; i < so.length; i++) {
            String fields[] = so[i].split(Constant.MAIN_CONCAT_STRING_2);
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("status", fields[1]);
            mHmAux.put("final_status", fields[0] + " / " + fields[1]);
            //
            sos.add(mHmAux);
        }

        if (sos.size() == 1) {
            //Verifica se S.O atualizada, foi esta S.O
            if (sos.get(0).get("label").equals(mSm_so.getSo_prefix() + "." + mSm_so.getSo_code())) {
                if (sos.get(0).get("status").equalsIgnoreCase("Ok")) {
                    progressDialog.dismiss();
                    only_save = false;
                    //
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_ttl"),
                            hmAux_Trans.get("msg_so_save_ok"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refreshUI();
                                }
                            },
                            0
                    );
                    //refreshUI();
                } else {
                    progressDialog.dismiss();
                    //
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_list_title"),
                            sos.get(0).get("status"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refreshUI();
                                }
                            },
                            0
                    );
                }
            } else {
                showNewOptDialog(sos);
            }

        } else {
            showNewOptDialog(sos);
        }
    }

    public void showNewOptDialog(List<HMAux> sos) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        final HMAux auxSo = new HMAux();
        for (int i = 0; i < sos.size(); i++) {
            if (sos.get(i).get("label").equals(mSm_so.getSo_prefix() + "." + mSm_so.getSo_code())) {
                auxSo.putAll(sos.get(i));
                break;
            }
        }
        //
        lv_results.setAdapter(
                new Act028_Results_Adapter(
                        context,
                        R.layout.act028_results_adapter_cell,
                        sos
                )
        );


        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //
                if (auxSo.containsKey("status")) {
                    if (auxSo.get("status").equalsIgnoreCase("Ok")) {
                        //
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_so_sync_ok_ttl"),
                                hmAux_Trans.get("alert_so_sync_ok_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        refreshUI();
                                    }
                                },
                                0
                        );
                        //refreshUI();
                    }
                } else {
                    executeSoSaveApproval();
                    //executeSoSync(mSm_so.getSo_prefix(), mSm_so.getSo_code());
                }
            }
        });
    }
    //endregion

    //region Ciclo Approval
    private void processSoApproval(HMAux hmAux) {
        //Tratativa para quando WS chamado e sem nenhuma s.o para atualizar.
        if (hmAux.containsKey(WS_SO_Save.SO_NO_EMPTY_LIST)) {
            if (!only_approval) {
                ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_starting_sync"), "", "0");
                //
                executeSoSync(mSm_so.getSo_prefix(), mSm_so.getSo_code());
            } else {
                only_approval = false;
            }
        } else {
            String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

            showResultsApproval(so);
        }
    }

    private void showResultsApproval(String[] so) {
        ArrayList<HMAux> sos = new ArrayList<>();
        for (int i = 0; i < so.length; i++) {
            String fields[] = so[i].split(Constant.MAIN_CONCAT_STRING_2);
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("status", fields[1]);
            mHmAux.put("final_status", fields[0] + " / " + fields[1]);
            //
            sos.add(mHmAux);
        }

        if (sos.size() == 1) {
            //Verifica se S.O atualizada, foi esta S.O
            if (sos.get(0).get("label").equals(mSm_so.getSo_prefix() + "." + mSm_so.getSo_code())) {
                if (sos.get(0).get("status").equalsIgnoreCase("Ok")) {
                    progressDialog.dismiss();
                    only_save = false;
                    //
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_ttl"),
                            hmAux_Trans.get("msg_so_save_ok"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refreshUI();
                                }
                            },
                            0
                    );
                    //refreshUI();
                } else {
                    progressDialog.dismiss();
                    //
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_so_list_title"),
                            sos.get(0).get("status"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refreshUI();
                                }
                            },
                            0
                    );
                }
            } else {
                showNewOptDialogApproval(sos);
            }

        } else {
            showNewOptDialogApproval(sos);
        }
    }

    public void showNewOptDialogApproval(List<HMAux> sos) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        final HMAux auxSo = new HMAux();
        for (int i = 0; i < sos.size(); i++) {
            if (sos.get(i).get("label").equals(mSm_so.getSo_prefix() + "." + mSm_so.getSo_code())) {
                auxSo.putAll(sos.get(i));
                break;
            }
        }
        //
        lv_results.setAdapter(
                new Act028_Results_Adapter(
                        context,
                        R.layout.act028_results_adapter_cell,
                        sos
                )
        );


        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //
                if (auxSo.containsKey("status")) {
                    if (auxSo.get("status").equalsIgnoreCase("Ok")) {
                        //
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_so_sync_ok_ttl"),
                                hmAux_Trans.get("alert_so_sync_ok_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        refreshUI();
                                    }
                                },
                                0
                        );
                        //refreshUI();
                    }
                } else {
                    if (!only_approval) {
                        executeSoSync(mSm_so.getSo_prefix(), mSm_so.getSo_code());
                    } else {
                        only_approval = false;
                    }
                }
            }
        });
    }
    //endregion

    private void processUserAuthorCheck(HMAux hmAux) {

        try {

            act027_approval_.setOnLineApproval(Integer.parseInt(hmAux.get("so_param_return_status")));
            only_approval = true;
            executeSoSaveApproval();

        } catch (Exception e) {

            only_approval = false;

            recoverApprovalState();

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_author_validation"),
                    ((hmAux.get("so_param_return_msg") == null || hmAux.get("so_param_return_msg").isEmpty()) ? e.toString() : hmAux.get("so_param_return_msg")),
                    null,
                    0
            );
        }
    }

    public void executeSoSaveApproval() {
        setWs_process(WS_PROCESS_SO_SAVE_APPROVAL);
        //
        enableProgressDialog(
                hmAux_Trans.get("progress_so_save_ttl"),
                hmAux_Trans.get("progress_so_save_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Approval.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    public void executeSoSave() {
        setWs_process(WS_PROCESS_SO_SAVE);
        //
        enableProgressDialog(
                hmAux_Trans.get("progress_so_save_ttl"),
                hmAux_Trans.get("progress_so_save_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Save.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    public void executeUserAuthorCheck(
            long customer_code,
            long so_prefix,
            long so_code,
            String auth_type,
            String auth_nick_mail,
            String auth_password,
            String auth_nfc

    ) {
        if (ToolBox_Con.isOnline(context)) {
            setWs_process(WS_PROCESS_USER_AUTHOR);
            //
            enableProgressDialog(
                    hmAux_Trans.get("dialog_credentials_ttl"),
                    hmAux_Trans.get("dialog_credentials_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            Intent mIntent = new Intent(context, WBR_UserAuthor.class);
            Bundle bundle = new Bundle();
            //
            bundle.putLong(Constant.SO_PARAM_CUSTOMER_CODE, customer_code);
            bundle.putLong(Constant.SO_PARAM_SO_PREFIX, so_prefix);
            bundle.putLong(Constant.SO_PARAM_SO_CODE, so_code);
            bundle.putString(Constant.SO_PARAM_AUTH_TYPE, auth_type);
            bundle.putString(Constant.SO_PARAM_AUTH_NICK_MAIL, auth_nick_mail);
            bundle.putString(Constant.SO_PARAM_AUTH_PASSWORD, auth_password);
            bundle.putString(Constant.SO_PARAM_AUTH_NFC, auth_nfc);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            setWs_process("");
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    public void executeSoSync(int so_prefix, int so_code) {
        if (ws_call_next_ctrl) {
            setWs_process(WS_PROCESS_SO_SYNC);
            //
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //
            enableProgressDialog(
                    hmAux_Trans.get("progress_so_sync_ttl"),
                    hmAux_Trans.get("progress_so_sync_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Search.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, so_prefix + "." + so_code);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }
    }

    //Metodo chamado ao finalizar o download da atualização.
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    //Tratativa SESSION NOT FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }
    //endregion

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act027_main_ll, type, sTag);
            ft.commit();
        } else {
            //type.loadDataToScreen();
        }
    }

    @Override
    public void onBackPressed() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_so_exit_title"),
                hmAux_Trans.get("alert_so_exit_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent(context, Act005_Main.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //
                        startActivity(mIntent);
                        finish();

                    }
                },
                1,
                false
        );
    }

    @Override
    public void menuOptionsSelected(String type) {
        switch (type.toUpperCase()) {
            case Act027_Main.SELECTION_SERVICES:
                setFrag(act027_services_, Act027_Main.SELECTION_SERVICES);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case Act027_Main.SELECTION_SERIAL:
                setFrag(act027_serial_, Act027_Main.SELECTION_SERIAL);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case Act027_Main.SELECTION_HEADER:
                setFrag(act027_header_, Act027_Main.SELECTION_HEADER);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case Act027_Main.SELECTION_APPROVAL:

                if (mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_CLIENT)) {

                    if (mSm_so.getClient_type().equalsIgnoreCase(Constant.CLIENT_TYPE_CLIENT)) {
                        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_APPROVE_CLIENT)) {
                            setFrag(act027_approval_, Act027_Main.SELECTION_APPROVAL);
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_no_profile_ttl"),
                                    hmAux_Trans.get("alert_no_profile_msg"),
                                    null,
                                    0
                            );
                        }
                    } else {
                        setFrag(act027_approval_, Act027_Main.SELECTION_APPROVAL);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }

                } else if (mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_QUALITY)) {
                    setFrag(act027_approval_, Act027_Main.SELECTION_APPROVAL);
                    mDrawerLayout.closeDrawer(GravityCompat.START);

//                    if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_APPROVE_QUALITY)) {
//                        setFrag(act027_approval_, Act027_Main.SELECTION_APPROVAL);
//                        mDrawerLayout.closeDrawer(GravityCompat.START);
//                    } else {
//                        ToolBox.alertMSG(
//                                context,
//                                hmAux_Trans.get("alert_no_profile_ttl"),
//                                hmAux_Trans.get("alert_no_profile_msg"),
//                                null,
//                                0
//                        );
//                    }
                } else {
                    setFrag(act027_approval_, Act027_Main.SELECTION_APPROVAL);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }

                break;
            default:
                setFrag(act027_header_, Act027_Main.SELECTION_HEADER);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        //mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void soSyncClick() {

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_so_sync_ttl"),
                hmAux_Trans.get("alert_so_sync_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ToolBox_Con.isOnline(context)) {
                            //Seta S.O como update required.
                            /*sm_soDao.addUpdate(
                                    new SM_SO_Sql_009(
                                            ToolBox_Con.getPreference_Customer_Code(context),
                                            mSm_so.getSo_prefix(),
                                            mSm_so.getSo_code()
                                    ).toSqlQuery()
                            );*/
                            //
                            setWs_process(WS_PROCESS_SO_SAVE);
                            //
                            executeSoSave();
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    }
                },
                1
        );

        // refreshUI();
    }

    private void recoverApprovalState() {
        //mSm_so.setStatus(Constant.SO_STATUS_WAITING_CLIENT);
        mSm_so.setStatus(ToolBox_Con.getApproval_Type(context));
        mSm_so.setApproval_required(WS_PROCESS_APPROVAL_NOT);
        mSm_so.setClient_approval_user(null);

        mSm_so.setClient_approval_date(null);
        mSm_so.setClient_name(null);
        mSm_so.setClient_approval_image_name(null);
        mSm_so.setClient_approval_type_sig(null);

        mSm_so.setQuality_approval_user(null);
        mSm_so.setQuality_approval_user_nick(null);
        mSm_so.setQuality_approval_date(null);
        //
        sm_soDao.addUpdate(
                new SM_SO_Sql_012(
                        0,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        mSm_so.getStatus(),
                        null,
                        null,
                        null
                ).toSqlQuery()
        );

        //ToolBox_Con.setApproval_Type(context, "");
    }

//    @Override
//    public void ivNServiceClick() {
//        ToolBox.alertMSG(
//                context,
//                hmAux_Trans.get("alert_goto_service_menu_ttl"),
//                hmAux_Trans.get("alert_goto_service_menu_msg"),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        callAct021(context);
//                    }
//                },
//                1
//        );
//    }

    @Override
    public void onServiceSelected(HMAux sService) {
        bundle.putString(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE, sService.get(SM_SO_Service_Exec_TaskDao.PRICE_LIST_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PACK_CODE, sService.get(SM_SO_Service_Exec_TaskDao.PACK_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.PACK_SEQ, sService.get(SM_SO_Service_Exec_TaskDao.PACK_SEQ));
        bundle.putString(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE, sService.get(SM_SO_Service_Exec_TaskDao.CATEGORY_PRICE_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.SERVICE_CODE, sService.get(SM_SO_Service_Exec_TaskDao.SERVICE_CODE));
        bundle.putString(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ, sService.get(SM_SO_Service_Exec_TaskDao.SERVICE_SEQ));
        bundle.putString(SM_SO_Service_Exec_TaskDao.EXEC_TMP, sService.get(SM_SO_Service_Exec_TaskDao.EXEC_TMP));
        bundle.putString(SM_SO_Service_Exec_TaskDao.TASK_TMP, sService.get(SM_SO_Service_Exec_TaskDao.TASK_TMP));

        bundle.putSerializable("data", sService);
        bundle.putBoolean(Constant.ACT027_IS_SHORTCUT, false);

        callAct028(context, bundle);
    }

    private void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct028(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act028_Main_New.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);

        finish();
    }

    private void callAct032(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act032_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);

        finish();
    }

    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);
        if (!status) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Toast.makeText(
                    context,
                    "Erro",
                    Toast.LENGTH_SHORT
            ).show();

        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            String Autor_Type = ToolBox_Con.getApproval_Type(context).equalsIgnoreCase(Constant.SO_STATUS_WAITING_QUALITY) ? Constant.SO_PARAM_AUTH_TYPE_QUALITY : Constant.SO_PARAM_AUTH_TYPE_CLIENT;

            executeUserAuthorCheck(
                    mSm_so.getCustomer_code(),
                    mSm_so.getSo_prefix(),
                    mSm_so.getSo_code(),
                    Autor_Type,
                    "",
                    "",
                    value[1]
            );
        }
    }

    @Override
    protected void nfcDataError(boolean status, int id, String... value) {
        int i = 10;
    }

    @Override
    protected void getSignatueF(String mValue) {
        super.getSignatueF(mValue);

        File sFile = new File(Constant.CACHE_PATH_PHOTO + "/" + sFileNameSignature);
        if (sFile.exists()) {

            act027_approval_.setOnLineApprovalSig(sFileNameSignature);
            //
            only_approval = true;
            //
            callAddSignature(sFileNameSignature);
            //
            if (ToolBox_Con.isOnline(context)) {
                executeSoSaveApproval();
            }

        } else {
            only_approval = false;

            recoverApprovalState();

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_signature_ttl"),
                    hmAux_Trans.get("alert_so_signature_msg"),
                    null,
                    0
            );
        }
    }

    public void callSignature(String name) {
        try {
            sFileNameSignature = "s_" +
                    ToolBox_Con.getPreference_Customer_Code(context) + "_" +
                    String.valueOf(mSm_so.getSo_prefix()) + "_" +
                    String.valueOf(mSm_so.getSo_code()) + "_" + UUID.randomUUID().toString() +
                    ".png";

            Bundle bundleN = new Bundle();
            bundleN.putInt(ConstantBase.PID, -1);
            bundleN.putInt(ConstantBase.PTYPE, 0);
            bundleN.putString(ConstantBase.MNAME, name);
            bundleN.putBoolean(ConstantBase.BLOCK_NAME, true);
            bundleN.putString(ConstantBase.PPATH, CACHE_PATH_PHOTO + "/" + sFileNameSignature);

            Intent mIntent = new Intent(context, SignaTure_Activity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundleN);

            context.startActivity(mIntent);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
    }

    private void callAddSignature(String signature) {
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        ArrayList<GE_File> geFiles = new ArrayList<>();

        GE_File geFile = new GE_File();
        geFile.setFile_code(signature.replace(".png", ""));
        geFile.setFile_path(signature);
        geFile.setFile_status("OPENED");
        geFile.setFile_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

        geFiles.add(geFile);

        geFileDao.addUpdate(geFiles, false);

        activateUpload(context);
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private class DownloadSignature extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String sFileName = null;

            try {

                sFileName = params[0].replace(".png", "");

                if (!ToolBox_Inf.verifyDownloadFileInf(sFileName.toLowerCase() + ".png")) {

                    ToolBox_Inf.deleteDownloadFileInf(sFileName.toLowerCase() + ".tmps");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            mSm_so.getClient_approval_image_url(),
                            Constant.CACHE_PATH_PHOTO + "/" + sFileName.toLowerCase() + ".tmps"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInfSig(sFileName.toLowerCase(), ".png");
                }
            } catch (Exception e) {
            }

            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(ToolBox_Inf.parameterExists(context,Constant.PARAM_CHECKLIST)) {
            menu.add(0, 3, Menu.FIRST + 4, hmAux_Trans.get("toolbar_n_form_lbl"));
            menu.findItem(3).setIcon(getResources().getDrawable(R.drawable.ic_n_form));
            menu.findItem(3).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.findItem(3).setTitle(hmAux_Trans.get("toolbar_n_form_lbl"));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ToolBox_Inf.parameterExists(context,Constant.PARAM_CHECKLIST)) {
            //
            int id = item.getItemId();
            //
            switch (id) {
                case 3:
                    callNFormMsg();
                    return true;

            }
            //
        }
        return super.onOptionsItemSelected(item);
    }

    private void callNFormMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_open_n_form_ttl"),
                hmAux_Trans.get("alert_open_n_form_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        processNFormFlow();
                    }
                },
                1
        );
    }

    private void processNFormFlow() {
                //
        HMAux syncProduct = syncChecklistDao
                .getByStringHM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                mSm_so.getProduct_code()
                        ).toSqlQuery()
                );
        //Se Forms do produto ainda não foram sincronizados,
        //Chama Ws para baixar os Forms.
        //Caso contrario, verifica se ja existe form aberto antes de
        //direcionar par act009
        if(syncProduct == null || syncProduct.size() == 0){
            //Chamar WS
            executeSyncProcess();
        }else{
            callAct009();
        }

    }

    private void executeSyncProcess() {
        if(ToolBox_Con.isOnline(context)) {
            setWs_process(WS_PROCESS_N_FORM_SYNC);
            //
            //
            enableProgressDialog(
                    hmAux_Trans.get("progress_n_form_sync_ttl"),
                    hmAux_Trans.get("progress_n_form_sync_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            ArrayList<String> data_package = new ArrayList<>();
            data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
            //
            Intent mIntent = new Intent(context, WBR_Sync.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
            bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
            bundle.putLong(Constant.GS_PRODUCT_CODE, mSm_so.getProduct_code());
            bundle.putInt(Constant.GC_STATUS_JUMP, 1);
            bundle.putInt(Constant.GC_STATUS, 1);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    public void updateSyncChecklist() {
        //Pega data atual
        Calendar cDate =  Calendar.getInstance();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist syncChecklist =  new Sync_Checklist();

        syncChecklist.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        syncChecklist.setProduct_code(mSm_so.getProduct_code());
        syncChecklist.setLast_update(last_update);

        syncChecklistDao.addUpdate(syncChecklist);
        //
        startDownloadServices();
    }

    private void startDownloadServices() {
        Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Intent mIntentLogo =  new Intent(context,WBR_DownLoad_Customer_Logo.class);
        Bundle bundle = new Bundle();
        mIntentPDF.putExtras(bundle);
        mIntentPIC.putExtras(bundle);
        mIntentLogo.putExtras(bundle);
        //
        context.sendBroadcast(mIntentPDF);
        context.sendBroadcast(mIntentPIC);
        context.sendBroadcast(mIntentLogo);
    }


    private void callAct009() {
        Intent mIntent = new Intent(context, Act009_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(SM_SODao.SO_PREFIX, String.valueOf(mSm_so.getSo_prefix()));
        bundle.putString(SM_SODao.SO_CODE, String.valueOf(mSm_so.getSo_code()));
        bundle.putString(SM_SODao.SITE_CODE, String.valueOf(mSm_so.getSite_code()));
        bundle.putString(SM_SODao.OPERATION_CODE, String.valueOf(mSm_so.getOperation_code()));
        bundle.putString(Constant.ACT007_PRODUCT_CODE, String.valueOf(mSm_so.getProduct_code()));
        bundle.putString(Constant.ACT008_SERIAL_ID, mSm_so.getSerial_id());
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT027);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }
}
