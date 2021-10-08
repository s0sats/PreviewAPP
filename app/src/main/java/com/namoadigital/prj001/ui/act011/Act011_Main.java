package com.namoadigital.prj001.ui.act011;

import static com.namoa_digital.namoa_library.util.ConstantBase.CACHE_PATH_PHOTO;
import static com.namoadigital.prj001.util.ConstantBaseApp.DEVICE_BUNDLE;
import static com.namoadigital.prj001.util.ConstantBaseApp.DEVICE_ITEM_LIST_ACTION;
import static com.namoadigital.prj001.util.ConstantBaseApp.DEVICE_ITEM_LIST_FILTER;
import static com.namoadigital.prj001.util.ConstantBaseApp.DEVICE_ITEM_LIST_INDEX;
import static com.namoadigital.prj001.util.ConstantBaseApp.DEVICE_ITEM_PK;
import static com.namoadigital.prj001.util.ConstantBaseApp.DEVICE_ITEM_TAB_INDEX;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.namoa_digital.namoa_library.ctls.CheckBoxFF;
import com.namoa_digital.namoa_library.ctls.ComboBoxFF;
import com.namoa_digital.namoa_library.ctls.CustomFF;
import com.namoa_digital.namoa_library.ctls.LabelFF;
import com.namoa_digital.namoa_library.ctls.MKEditTextNMFF;
import com.namoa_digital.namoa_library.ctls.PhotoFF;
import com.namoa_digital.namoa_library.ctls.PictureFF;
import com.namoa_digital.namoa_library.ctls.RatingBarFF;
import com.namoa_digital.namoa_library.ctls.RatingImageFF;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.NamoaPermissionRequest;
import com.namoa_digital.namoa_library.view.SignaTure_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.GeOsDao;
import com.namoadigital.prj001.dao.GeOsDeviceDao;
import com.namoadigital.prj001.dao.GeOsDeviceItemDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MdTagDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.databinding.Act011CheckDialogBinding;
import com.namoadigital.prj001.model.AcessoryFormView;
import com.namoadigital.prj001.model.Act011FfOptionsViewObject;
import com.namoadigital.prj001.model.Act011FormTab;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.GeOs;
import com.namoadigital.prj001.model.InspectionCellActions;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MyActionFilterParam;
import com.namoadigital.prj001.model.MyActions;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_016;
import com.namoadigital.prj001.sql.GE_File_Sql_003;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrg;
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrgInteractionNavegation;
import com.namoadigital.prj001.ui.act011.frags.Act011FrgFF;
import com.namoadigital.prj001.ui.act011.frags.Act011FrgFFInteraction;
import com.namoadigital.prj001.ui.act011.frags.Act011FrgInspection;
import com.namoadigital.prj001.ui.act011.frags.InspectionListFragmentInteraction;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.ui.act083.Act083_Main;
import com.namoadigital.prj001.ui.act084.Act084Main;
import com.namoadigital.prj001.ui.act086.Act086Main;
import com.namoadigital.prj001.ui.act087.FormOsHeaderFrg;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main extends Base_Activity
    implements
    Act011_Main_View,
    Act011BaseFrgInteractionNavegation,
    Act011FrgFFInteraction,
    InspectionListFragmentInteraction
{

    public static final int SHOW_MSG_TYPE_FORM_LOCAL_INSERT_ERROR = 4;
    public static final int SHOW_MSG_TYPE_SCHEDULE_EXEC_UPDATE_ERROR = 5;
    public static final int SHOW_MSG_TYPE_SCHEDULE_EXEC_CANCEL_ERROR = 6;
    public static final int SHOW_MSG_TYPE_TICKET_STEP_OR_CTRL_ERROR = 7;
    public static final int SHOW_MSG_TYPE_TICKET_FORM_FINALIZED = 8;
    public static final String PNG_EXTENSION = ".png";
    public static final String JPG_EXTENSION = ".jpg";
    public static final String PAGE = "page";
    public static final String CONTENT = "CONTENT";
    public static final String DECIMAL = "DECIMAL";
    public static final String BLOB_ICON = "blob_icon";

    private Act011_Main_Presenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ViewPager pager;

    private FragmentManager fm;

    private Act011FfOption act011FfOption;

    private ArrayList<Act011BaseFrg> screens;
    private transient ArrayList<CustomFF> customFFs;
    private ArrayList<GE_File> geFiles;

    private ArrayList<HMAux> pdfs_local;

    private String sDate;

    private Toolbar toolbar;

    private String dtCustomer_Format;

    private Bundle bundle;

    private String product_code;
    private String product_desc;
    private String product_id;
    private String serial_id;
    private String type;
    private String form;
    private String form_version;
    private String form_desc;
    private String prefix;
    private String form_data;
    private String mSignature;
    private int signature;
    private int require_serial_done;
    private String require_serial_done_ok;

    private String so_prefix;
    private String so_code;

    private boolean gpsCanceled = false;

    private boolean ignoreUpdate = false;

    private boolean bNew = false;

    private GE_Custom_Form_Local formLocal;
    private GE_Custom_Form_Data formData;

    private boolean includeField;

    private int oldPageIndex = 0;
    private int currentPageIndex = 1;

    private int index_old = 0;
    private int index = 1;

    private transient Dialog infoDialog;

    private boolean hasNFCSupport = false;

    private Integer mSo_Prefix;
    private Integer mSo_Code;
    private String mSite_Code;
    private Integer mOperation_Code;

    private String wsSoProcess = "";
    private ArrayList<HMAux> wsResults = new ArrayList<>();
    private boolean finalizeNewFlow = false;
    private boolean canSave;
    private String filter_search;
    private int form_selected_index;
    //
    private Integer mTicket_prefix;
    private Integer mTicket_code;
    private Integer mTicket_seq;
    private Integer mTicket_seq_tmp;
    private Integer mStep_code;
    private String requestingAct;
    private boolean isOffHandForm=false;
    private Bundle act081Bundle;
    private String room_code;
    private CustomFF.ICustomFFDotsDialogDismiss onBackFocusEvent;
    private Bundle act083Bundle;
    private boolean isFormOs = false;

    private int device_item_tab_index =-1;
    private int device_item_list_index = -1;
    private String device_item_list_filter;


    public void setWsSoProcess(String wsSoProcess) {
        this.wsSoProcess = wsSoProcess;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act011_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        /**
         * LUCHE - 30/01/2020
         *
         * Implementado metodo para recuperar CUSTOM_FORM_DATA quando o app se recupera do fechamento
         * por falta de memoria evitando a msg de form aberto a cada evento de "fechamento por falta de memoria"
         */
         if(savedInstanceState != null) {
            //Se tiver os savedInstanceState e a chave CUSTOM_FORM_DATA,
            //seta o CUSTOM_FORM_DATA no bundle da intent que por sua vez será resgatado no metodo
            //recoverGetIntents
            if(savedInstanceState.containsKey(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA)) {
                getIntent().putExtra(
                    GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                    savedInstanceState.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA)
                );
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
//        String dataRecorded = "\nonDestroy ACT011: " + ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT);
//        recordProcess(dataRecorded);
        if (!ToolBox_Con.getBooleanPreferencesByKey(getApplicationContext(), Constant.HAS_PENDING_LOCATION, false)) {
            ToolBox_Inf.stop_Location_Tracker(context);
        } else {
            ToolBox_Inf.call_Location_Tracker_On_Background(context, SV_LocationTracker.LOCATION_BACKGROUND);
        }
    }

//    private void recordProcess(String data) {
//        try {
//            String filePath = getApplicationContext().getFilesDir().getPath() + "/GPS_Histo.txt";
//            ToolBox_Inf.writeIn(data, new File(filePath));
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void iniSetup() {
        //hasNFCSupport = ToolBox_Inf.hasNFC(context);

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT011
        );

        geFiles = new ArrayList<>();

        hideKeyBoard();

        loadTranslation();

        ToolBox_Inf.libTranslation(context);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("exit_alert_ttl");
        transList.add("exit_alert_msg");
        transList.add("dialog_info_title_lbl");
        transList.add("dialog_info_product_lbl");
        transList.add("dialog_info_serial_lbl");
        transList.add("dialog_info_form_type_lbl");
        transList.add("dialog_info_form_code_lbl");
        transList.add("dialog_info_form_version_lbl");
        transList.add("dialog_info_title_pdf_lbl");
        transList.add("alert_error_on_finalize_title");
        transList.add("alert_error_on_finalize_msg");
        transList.add("alert_save_title");
        transList.add("alert_save_msg");

        transList.add("alert_finalize_title");
        transList.add("alert_finalize_msg");

        transList.add("alert_nform_expired_ttl");
        transList.add("alert_nform_expired_msg");

        transList.add("alert_question_finalize_title");
        transList.add("alert_question_finalize_msg");

        transList.add("alert_require_signature_msg");
        transList.add("alert_optional_signature_msg");
        transList.add("dialog_signature_title_lbl");
        transList.add("drawer_automatic_lbl");
        transList.add("qty_automatic_answer_msg");
        transList.add("dialog_info_product_code_lbl");
        transList.add("dialog_info_product_id_lbl");
        transList.add("dialog_info_data_serv_lbl");
        transList.add("dialog_info_dt_schedule_start_lbl");
        transList.add("dialog_info_dt_schedule_end_lbl");

        transList.add("dialog_info_class_lbl");
        transList.add("dialog_info_category_lbl");
        transList.add("dialog_info_segment_lbl");
        transList.add("dialog_info_site_lbl");
        transList.add("dialog_info_zone_lbl");
        transList.add("dialog_info_position_lbl");
        transList.add("dialog_info_add_info_1_lbl");
        transList.add("dialog_info_add_info_2_lbl");
        transList.add("dialog_info_add_info_3_lbl");

        transList.add("dialog_info_so_prefix_lbl");
        transList.add("dialog_info_so_code_lbl");
        transList.add("dialog_info_ticket_code_lbl");

        transList.add("alert_location_info_title");
        transList.add("alert_location_info_required");
        transList.add("alert_location_gps_info");
        transList.add("alert_location_info_aquired_succesfully");
        transList.add("alert_location_info_aquired_unsuccesfully");
        transList.add("alert_schedule_comment_ttl");
        transList.add("lbl_no_serial_placeholder");

        transList.add("dialog_finalize_option_ttl");
        transList.add("dialog_finalize_option_finalize_lbl");
        transList.add("dialog_finalize_option_finalize_new_lbl");
        transList.add("btn_check_new");
        transList.add("btn_history");
        transList.add("btn_home");
        transList.add("alert_results_ttl");
        transList.add("lbl_serial_data");
        transList.add("alert_error_on_create_form_ttl");
        transList.add("alert_error_on_create_form_msg");
        transList.add("alert_data_not_sent_ttl");
        transList.add("alert_resend_data_by_menu_msg");

        transList.add("dialog_confirm_delete_ttl");
        transList.add("dialog_confirm_delete_msg");
        transList.add("dialog_confirm_delete_comfirmation");
        transList.add("dialog_confirm_delete_abort");

        transList.add("alert_starting_pdf_not_supported_ttl");
        transList.add("alert_starting_pdf_not_supported_msg");

        transList.add("alert_nform_already_started_ttl");
        transList.add("alert_nform_already_started_msg");
        //
        transList.add("alert_exit_confirmation_ttl");
        transList.add("alert_exit_confirmation_msg");
        transList.add("lbl_schedule");
        transList.add("alert_error_on_update_schedule_status_ttl");
        transList.add("alert_error_on_update_schedule_status_msg");
        //
        transList.add("dialog_has_gps_pendency_ttl");
        transList.add("dialog_has_gps_pendency_msg");
        //
        transList.add("alert_schedule_cancelled_by_server_ttl");
        transList.add("alert_schedule_cancelled_by_server_msg");
        transList.add("alert_schedule_warning_new_status_lbl");
        transList.add("alert_warning_user_nick_lbl");
        transList.add("alert_erro_on_cancel_schedule_form_ttl");
        transList.add("alert_erro_on_cancel_schedule_form_msg");
        //
        transList.add("alert_form_turn_gps_on_title");
        transList.add("alert_form_turn_gps_on_msg");
        //
        transList.add("alert_ticket_step_or_ctrl_not_found_ttl");
        transList.add("alert_ticket_step_or_ctrl_not_found_msg");
        transList.add("lbl_ticket");
        //
        transList.add("alert_error_on_create_ctrl_ttl");
        transList.add("alert_error_on_create_ctrl_msg");
        //
        transList.add("alert_gps_rationale_permission_ttl");
        transList.add("alert_gps_rationale_permission_msg");
        transList.add("alert_gps_denied_permission_ttl");
        transList.add("alert_gps_denied_permission_msg");
        transList.add("alert_gps_never_ask_again_permission_ttl");
        transList.add("alert_gps_never_ask_again_permission_msg");
        //
        transList.add("dialog_finalize_os_form_lbl");
        transList.add("dialog_finalize_form_lbl");
        transList.add("dialog_finalize_os_form_missing_answer_count_lbl");
        transList.add("dialog_finalize_os_form_elapsed_time_lbl");
        transList.add("dialog_finalize_os_form_justify_missing_answer_lbl");
        //
        transList.addAll(Act011FrgInspection.Companion.getFragTranslationsVars());
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        //LUCHE - 05/10/2021
        HMAux formOsFragTransient = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                FormOsHeaderFrg.Companion.getMResource_Name()
            ),
            ToolBox_Con.getPreference_Translate_Code(context),
            FormOsHeaderFrg.Companion.getFragTranslationsVars()
        );
        //
        hmAux_Trans.putAll(formOsFragTransient);
    }

    /**
     * LUCHE - 17/01/2019 - RotateBugFixed
     *
     * Correção bug que crash app no Tablet Mosolf e devices Android 8.1.0 com Android puro ou quase.
     * O crash acontecia quando o usr entrava na camera segurando o celular na horizontal e depois voltava
     * sem tirar a foto.
     *
     * Problema:
     *  - Identificamos nesse caso, que o apensar da Activity original ser destruida e reconstruida
     *  pelo S.O, o FragmentManager(fm), guardava internamente as referencias dos Fragmentos antigo
     *  não usandoos fragmentos recem criados. Esses fragmentos antigos ao serem recriados, tentavam
     *  acessar propriedades null, HmAuxTrans nesse caso, o que ocasionava o crash.
     *
     * Solução:
     *  - Para solucionar o problema, foram criados metodos get nessa Act011 para que o fragmento,
     *  no momento de sua reconstrução, resgate da Activity as informações necessarias para sua
     *  reconstrução.
     *  - No Fragmento ,Act011_FF, foi necessario implementar o save de algumas propriedades para que
     *  o fragmento conseguisse se reconstruir.

     */
    //region RotateBugFixed
    /**
     * Retorna a tradução contida na act.
     * @return
     */
    @Deprecated
    public HMAux getHmAuxTrans(){
        return hmAux_Trans;
    }

    /**
     * Retorna a implementação das interfaces,delegate, do fragmento Act011_FF
     * @return
     */
    @Deprecated
    public Act011_FF.ICustom_Form_FF_ll getFFInterface(){
        return  new Act011_FF.ICustom_Form_FF_ll() {
                    @Override
                    public void openDrawer() {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }

                    @Override
                    public void check() {
                        checkAction(false);
                    }

                    @Override
                    public void previosTab() {
                        if ((index - 1) >= 1) {
                            tabSelectedAction(index - 1);
                        }
                    }

                    @Override
                    public void nextTab() {
                        if ((index + 1) <= pager.getAdapter().getCount()) {
                            tabSelectedAction(index + 1);
                        }
                    }

                    @Override
                    public void checkWithNew() {
                        finalizeNewFlow = true;
                        //
                        checkAction(false);
                    }
                };
    }

    /**
     * Retorna a lista de componente do formulário.
     * @return
     */
    @Deprecated
    public ArrayList<CustomFF> getFf(){
        return customFFs;
    }

    //endregion

    //region Interfaces Novas

    @Override
    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void check() {
        checkAction(true);
    }

    @Override
    public void previosTab() {
        if ((index - 1) >= 1) {
            tabSelectedAction(index - 1);
        }
    }

    @Override
    public void nextTab() {
        if ((index + 1) <= pager.getAdapter().getCount()) {
            tabSelectedAction(index + 1);
        }
    }

    @Override
    public Bitmap getProductIconBmp(){
        return mPresenter.getProductIconBitmap(formLocal.getCustom_product_icon_name());
    }

    @NonNull
    @Override
    public ArrayList<CustomFF> getCustomFF() {
        return customFFs;
    }

    //endregion

    private void initVars() {
        fm = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.ge_cf_form_drawer);
        //
        pager = (ViewPager) findViewById(R.id.act011_pager);
        //
        mDrawerToggle = new ActionBarDrawerToggle(
                Act011_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideSoftKeyboard(Act011_Main.this);
                //Valida tabs e informa resultado ao drawer
                updateTabStatusIntoDrawer(
                    returnValidateTabObj(index_old)
                );
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        act011FfOption = (Act011FfOption)
                fm.findFragmentById(R.id.act011_ff_options);

        mPresenter = new Act011_Main_Presenter_Impl(
                context,
                this,
                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_DataDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_Data_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_Field_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_BlobDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_Blob_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MD_Product_SerialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                hmAux_Trans,
                new MD_Schedule_ExecDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new TK_Ticket_StepDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MdTagDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GeOsDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GeOsDeviceDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GeOsDeviceItemDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        );

        recoverGetIntents();
        //
        /**
         * LUCHE - 29/04/2021
         * Implmentado interface aciona após o dismiss do dialog.
         * Nesse caso,faz o scroll para a view aberta.
         */
        onBackFocusEvent = new CustomFF.ICustomFFDotsDialogDismiss() {
            @Override
            public void OnDotsDialogDismiss(CustomFF customFF) {
                //Remove o foco da view atual
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
                customFF.requestFocus();
                //Inicia processo de scroll, buscando o fragment atual e depois chamando o metodo que
                //faz o scroll
                int currentItem = pager.getCurrentItem();
                Act011BaseFrg fragment = screens.get(currentItem);
                if (fragment instanceof Act011FrgFF) {
                    ((Act011FrgFF) fragment).scrollToSelectedView(customFF);
                }
            }
        };

        //
        mPresenter.setData(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                type,
                form,
                form_version,
                product_code,
                form_data,
                product_desc,
                product_id,
                form_desc,
                serial_id,
                mSo_Prefix,
                mSo_Code,
                mSite_Code,
                mOperation_Code,
                mTicket_prefix,
                mTicket_code,
                mTicket_seq,
                mTicket_seq_tmp,
                mStep_code
        );
    }

    /**
     * Metodo que chama atualização de lista ou item da tab no drawer
     * Atualizado metodo com verificação de lsita vazia , pois na criação de form, como o oldIndex
     * é 0, não roda nenhum validação e retornava lista vazia, zerando lista de tabs
     * no drawer
     * @param tabs
     */
    private void updateTabStatusIntoDrawer(ArrayList<Act011FormTab> tabs) {
        if(tabs != null && tabs.size() > 0) {
            int selectecTab = isFormOs && index >= 1 ? index -1 : index;
            if (tabs.size() == 1) {
                act011FfOption.updateTabList(tabs.get(0), selectecTab);
            } else {
                act011FfOption.updateTabList(tabs, selectecTab);
            }
        }
    }

    /**
     * Metodo que chama atualização de item da lista
     * @param tab
     */
    private void updateTabStatusIntoDrawer(Act011FormTab tab) {
        act011FfOption.updateTabList(tab,index);
    }

    //TODO averiguar pq mudou de onResume para onStart
    @Override
    protected void onStart() {
        super.onStart();
        if (formLocal != null
                && formLocal.getRequire_location() == 1
                && ConstantBase.SYS_STATUS_IN_PROCESSING.equals(formLocal.getCustom_form_status())
                && !SV_LocationTracker.status) {
            getLocation();
        }
    }

    @Override
    public String getRequestingAct() {
        return requestingAct;
    }

    private void showConfirmDeleteDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act011_dialog_delete_warning, null);

        Drawable drawable_ic = null;
        TextView tv_title = view.findViewById(R.id.act011_dialog_tv_title);
        TextView tv_msg = view.findViewById(R.id.act011_dialog_tv_msg );
        TextView btn_ok = view.findViewById(R.id.act011_dialog_btn_ok);
        TextView btn_cancel = view.findViewById(R.id.act011_dialog_btn_cancel);
        ImageView iv_error = view.findViewById(R.id.act011_dialog_iv_error);


        drawable_ic = context.getResources().getDrawable(R.drawable.ic_error_black_24dp);
        drawable_ic.setColorFilter(context.getResources().getColor(R.color.namoa_color_danger_red), PorterDuff.Mode.SRC_ATOP);
        iv_error.setImageDrawable(drawable_ic);
        tv_title.setText(hmAux_Trans.get("dialog_confirm_delete_ttl"));
        tv_msg.setText(hmAux_Trans.get("dialog_confirm_delete_msg"));
        btn_ok.setText(hmAux_Trans.get("dialog_confirm_delete_comfirmation"));
        btn_cancel.setText(hmAux_Trans.get("dialog_confirm_delete_abort"));

        builder.setView(view);
        builder.setCancelable(false);
        final androidx.appcompat.app.AlertDialog show = builder.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                deleteFormLocal();
                canSave = false;
            }
        });
    }

    //Implments PhotoInterface
    private void saveV2(boolean fieldsValidation) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
//        LUCHE - 22/10/2019
//        Comentado reset dos dados de GPS que agora serão feitos
//        somente nos cancelamentos de GPS, Assinatura e serial.
//        formData.setLocation_type("");
//        formData.setLocation_lat("");
//        formData.setLocation_lng("");
        //
        if(fieldsValidation) {
            returnValidCheck(String.valueOf(-1));
        }

        loadCustomFFValueIntoFormData();    

        mPresenter.saveData(formData, fieldsValidation);

        bNew = false;
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Metodo que faz o loop nos fields do form data e seta os valor do customFF no field.
     */
    private void loadCustomFFValueIntoFormData() {
        for (GE_Custom_Form_Data_Field df : formData.getDataFields()) {
              setCustomFFValueIntoFormDataField(df);
        }
    }

    private void tabSelectedAction(int idtab) {
        //
        pager.setCurrentItem(idtab - 1);
    }

    private void checkAction(boolean showFinalizeOpt) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        //
        ArrayList<Act011FormTab> tabs = returnValidateTabObj(-1);
        //
        updateTabStatusIntoDrawer(
            tabs
        );
        //
        formData.setLocation_type("");
        formData.setLocation_lat("");
        formData.setLocation_lng("");
        //
        if(canSave) {
            saveV2(false);
        }
        //
        if (mPresenter.hasAnyInvalidField(tabs)) {
            //Luche - 28/02/2019
            //Reseta var de fluxo finaliza + novo
            finalizeNewFlow = false;
            //
            ToolBox.alertMSG(
                Act011_Main.this,
                hmAux_Trans.get("alert_error_on_finalize_title"),
                hmAux_Trans.get("alert_error_on_finalize_msg"),
                null,
                0
            );
        } else {
            if(showFinalizeOpt && allowFinalizeWithNewBtn()
            || isFormOs){
                showFinalizeDialogOpt();
            }else {
                // Mudar Aqui
                ToolBox.alertMSG(
                    Act011_Main.this,
                    hmAux_Trans.get("alert_question_finalize_title"), //"Finalizar Formalário"
                    hmAux_Trans.get("alert_question_finalize_msg"), //"Deseja Finalizar o Formulário?"
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startCheckIN();
//                                checkGpsFlow();
                        }
                    },
                    1
                );
            }
        }
    }

    /**
     * Valida se botão de finaliza + novo deve aparecer no fluxo.
     * <p>
     * LUCHE - 17/02/2020
     * Subistutuido a validação de agedamento via custom_form_data_serv pelo metodo isScheduleForm
     * <p>
     * Regras:
     * - O Usr deve ter o perfil de acesso ao botão.
     * - O N-Form não pode ter sido gerado pela O.S
     * - O N-Form não pode ter sido gerado pelo Agendamento
     * - 0 Serial deve ser informdo
     * LUCHE - 28/08/2020
     *  Modificado metodo, adicionando condição de form NÃO TER SIDO ORIDINADO por um ticket
     * @return
     */
    @Override
    public boolean allowFinalizeWithNewBtn() {
        if( ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,ConstantBaseApp.PROFILE_PRJ001_CHECKLIST_PARAM_DONE_NEW)
            && mSo_Prefix == null
            && mSo_Code == null
            && !ToolBox_Inf.isScheduleForm(formLocal)
            && serial_id != null
            && !serial_id.isEmpty()
            //LUCHE - 28/08/2020
            && !mPresenter.isFormCreateByTicket(formLocal)
        ){
            return true;
        }
        return false;
    }

    @Override
    public void nservCall() {
        Bundle bundle = new Bundle();

        bundle.putString(SM_SODao.SO_PREFIX, String.valueOf(mSo_Prefix));
        bundle.putString(SM_SODao.SO_CODE, String.valueOf(mSo_Code));
        //
        callAct027(context, bundle);
    }

    private void startCheckIN() {
        loadCustomFFValueIntoFormData();

        sDate = ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT);

        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        HMAux aux = geFileDao.getByStringHM(
                new GE_File_Sql_003().toSqlQuery()
        );

        geFiles.clear();

        for (int i = 0; i < customFFs.size(); i++) {
            String sFile_v = customFFs.get(i).getmValue();
            String sFile_e_1 = customFFs.get(i).getmDots_photo1();
            String sFile_e_2 = customFFs.get(i).getmDots_photo2();
            String sFile_e_3 = customFFs.get(i).getmDots_photo3();
            String sFile_e_4 = customFFs.get(i).getmDots_photo4();

            if (sFile_v.endsWith(PNG_EXTENSION) || sFile_v.endsWith(JPG_EXTENSION)) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_v);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_v.replace(PNG_EXTENSION, "").replace(JPG_EXTENSION, ""));
                    geFile.setFile_path(sFile_v);
                    geFile.setFile_status(GE_File.OPENED);
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_1.endsWith(PNG_EXTENSION) || sFile_e_1.endsWith(JPG_EXTENSION)) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_1);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_1.replace(PNG_EXTENSION, "").replace(JPG_EXTENSION, ""));
                    geFile.setFile_path(sFile_e_1);
                    geFile.setFile_status(GE_File.OPENED);
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_2.endsWith(PNG_EXTENSION) || sFile_e_2.endsWith(JPG_EXTENSION)) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_2);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_2.replace(PNG_EXTENSION, "").replace(JPG_EXTENSION, ""));
                    geFile.setFile_path(sFile_e_2);
                    geFile.setFile_status(GE_File.OPENED);
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_3.endsWith(PNG_EXTENSION) || sFile_e_3.endsWith(JPG_EXTENSION)) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_3);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_3.replace(PNG_EXTENSION, "").replace(JPG_EXTENSION, ""));
                    geFile.setFile_path(sFile_e_3);
                    geFile.setFile_status(GE_File.OPENED);
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_4.endsWith(PNG_EXTENSION) || sFile_e_4.endsWith(JPG_EXTENSION)) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_4);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_4.replace(PNG_EXTENSION, "").replace(JPG_EXTENSION, ""));
                    geFile.setFile_path(sFile_e_4);
                    geFile.setFile_status(GE_File.OPENED);
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
        }

        formData.setSignature(mSignature);

        mPresenter.checkSignature(formData, signature, 0, geFiles, require_serial_done, require_serial_done_ok, formLocal.getRequire_location());
    }

    private void deleteFormLocal() {

        GE_Custom_Form_LocalDao formLocalDao =
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

        GE_Custom_Form_DataDao formDataDao =
                new GE_Custom_Form_DataDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
        //BARRIONUEVO 08/08/2019
        //Query modificada para alterar status de form em vez de excluir fisicamente.
        //
        formLocalDao.addUpdate(
            new GE_Custom_Form_Local_Sql_016(
                String.valueOf(formData.getCustomer_code()),
                String.valueOf(formData.getCustom_form_type()),
                String.valueOf(formData.getCustom_form_code()),
                String.valueOf(formData.getCustom_form_version()),
                String.valueOf(formData.getCustom_form_data()),
                    ConstantBase.SYS_STATUS_DELETED,
                    ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
            ).toSqlQuery()
        );
        //
        formDataDao.addUpdate(
                new GE_Custom_Form_Data_Sql_005(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data()),
                        ConstantBase.SYS_STATUS_DELETED,
                        ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
                ).toSqlQuery()
        );
        //
        mPresenter.resetTicketCtrlFormDataIfNeeds(formLocal);
        //LUCHE - 17/09/2021 - Deleção de agendamento
        mPresenter.resetScheduleExecIfNeeds(formLocal);
        //
        mPresenter.deleteGeOsFormIfNeeds(formLocal);
        //
        mPresenter.checkAppExecutionDecrementUpdateNeeds(mSo_Prefix,mSo_Code, formData);
        //
        if(mPresenter.isaTicketFlowForm()){
            callAct070();
        }else if(ConstantBaseApp.ACT084.equals(requestingAct)){
            callAct084();
        }else if( ConstantBaseApp.ACT027.equals(requestingAct) || ConstantBaseApp.ACT028.equals(requestingAct)){
            nservCall();
        }else{
            if(serial_id != null && !serial_id.isEmpty()){
                callAct083();
            }else{
                //LUCHE - 22/06/2021
                //Modificado fluxo para voltar act006
               // callAct005(context);
                callAct006(context,false);
            }
        }
    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = bundle.getString(MD_ProductDao.PRODUCT_CODE, "");
            product_desc = bundle.getString(MD_ProductDao.PRODUCT_DESC, "");
            product_id = bundle.getString(MD_ProductDao.PRODUCT_ID, "Sem Product ID");
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            type = bundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, "");
            form = bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_CODE, "");
            form_version = bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, "");
            form_desc = bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, "");
            form_data = bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, "0");
            //
            mSo_Prefix = bundle.getString(SM_SODao.SO_PREFIX, null) == null ? null : Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, null));
            mSo_Code = bundle.getString(SM_SODao.SO_CODE, null) == null ? null : Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, null));
            mSite_Code = bundle.getString(SM_SODao.SITE_CODE, null);
            mOperation_Code = bundle.getString(SM_SODao.OPERATION_CODE, null) == null ? null : Integer.parseInt(bundle.getString(SM_SODao.OPERATION_CODE, null));
            //LUCHE - 24/08/2020 - PK Ticket
            mTicket_prefix = bundle.containsKey(TK_Ticket_CtrlDao.TICKET_PREFIX) ? bundle.getInt(TK_Ticket_CtrlDao.TICKET_PREFIX) : null;
            mTicket_code = bundle.containsKey(TK_Ticket_CtrlDao.TICKET_CODE)? bundle.getInt(TK_Ticket_CtrlDao.TICKET_CODE) : null;
            mTicket_seq = bundle.containsKey(TK_Ticket_CtrlDao.TICKET_SEQ) ? bundle.getInt(TK_Ticket_CtrlDao.TICKET_SEQ) : null;
            mTicket_seq_tmp = bundle.containsKey(TK_Ticket_CtrlDao.TICKET_SEQ_TMP) ? bundle.getInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP) : null;
            mStep_code = bundle.containsKey(TK_Ticket_CtrlDao.STEP_CODE) ? bundle.getInt(TK_Ticket_CtrlDao.STEP_CODE) : null;
            requestingAct = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT005);
            isOffHandForm = bundle.containsKey(ConstantBaseApp.TK_TICKET_IS_FORM_OFF_HAND);

            if(isOffHandForm){
                mTicket_seq = 0;
                mTicket_seq_tmp = mPresenter.getSeqTmpForFormOffHand(context, mTicket_prefix, mTicket_code, mStep_code);
                act081Bundle = new Bundle();
                act081Bundle.putString(MD_ProductDao.PRODUCT_CODE, bundle.getString(MD_ProductDao.PRODUCT_CODE, ""));
                act081Bundle.putString(MD_ProductDao.PRODUCT_DESC, bundle.getString(MD_ProductDao.PRODUCT_DESC, ""));
                act081Bundle.putString(MD_ProductDao.PRODUCT_ID, bundle.getString(MD_ProductDao.PRODUCT_ID, ""));
                room_code = bundle.getString(CH_RoomDao.ROOM_CODE);
                act081Bundle.putBoolean(ConstantBaseApp.TK_TICKET_IS_FORM_OFF_HAND, bundle.getBoolean(ConstantBaseApp.TK_TICKET_IS_FORM_OFF_HAND));
                act081Bundle.putInt(TK_TicketDao.TICKET_PREFIX, mTicket_prefix);
                act081Bundle.putInt(TK_TicketDao.TICKET_CODE, mTicket_code);

                act081Bundle.putString(TK_TicketDao.TICKET_ID, bundle.getString(TK_TicketDao.TICKET_ID, ""));
                act081Bundle.putInt(TK_Ticket_StepDao.STEP_CODE, mStep_code);
                act081Bundle.putString(TK_Ticket_StepDao.STEP_DESC, bundle.getString(TK_Ticket_StepDao.STEP_DESC, ""));

                act081Bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, ""));
                act081Bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, ""));
                act081Bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, ""));

            }
            //
            if( bundle.containsKey(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW)
                || bundle.containsKey(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
            ){
                act083Bundle = new Bundle();
                act083Bundle.putString(
                        ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                        bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,ConstantBaseApp.ACT005)
                );
                act083Bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM,ToolBox_Inf.getMyActionFilterParam(bundle));
            }
            //
            if(bundle.containsKey(DEVICE_BUNDLE)) {
                Bundle deviceBundle = bundle.getBundle(DEVICE_BUNDLE);
                this.device_item_tab_index = deviceBundle.getInt(DEVICE_ITEM_TAB_INDEX);
                this.device_item_list_index = deviceBundle.getInt(DEVICE_ITEM_LIST_INDEX);
                this.device_item_list_filter = deviceBundle.getString(DEVICE_ITEM_LIST_FILTER);
                bundle.remove(DEVICE_BUNDLE);
            }

        } else {
            mSo_Prefix = null;
            mSo_Code = null;
            //
            mSite_Code = null;
            mOperation_Code = null;
            //LUCHE - 24/08/2020 - TICKET
            mTicket_prefix = null;
            mTicket_code = null;
            mTicket_seq = null;
            mTicket_seq_tmp = null;
            mStep_code = null;
            requestingAct = ConstantBaseApp.ACT005;
        }
    }

    /**
     * LUCHE - 30/01/2020
     * <p>
     * Implmentado metodo para salvar o form_data no bundle
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,form_data);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT011;
        mAct_Title = Constant.ACT011 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        //LUCHE - 14/03/2018
        //Tratativa necessaria quando o carregamento dos itens é abortado pelo erro de insert na ge_custom_form_local
        //pois nesse fluxo, o adapter do pager não é setado.
        if(pager != null && pager.getAdapter() != null) {
            setTitleLanguage("          (" + String.valueOf(index) + "/" + String.valueOf(pager.getAdapter().getCount()) + ")");
        }else{
            setTitleLanguage();
        }
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {

    }

    @Override
    public void loadFragment_CF_Fields(List<HMAux> cf_fields, boolean bNew, GE_Custom_Form_Local formLocal, GE_Custom_Form_Data formData, String prefix, List<HMAux> pdfs, int indexF, int signature, int require_serial_done, ArrayList<AcessoryFormView> acessoryFormViews, GeOs geOs) {
        this.prefix = prefix;
        this.bNew = bNew;
        this.formLocal = formLocal;
        this.formData = formData;
        this.index_old = indexF;
        this.index = 1;
        this.signature = signature;
        this.mSignature = "s_" + prefix + "1.png";
        this.pdfs_local = (ArrayList<HMAux>) pdfs;
        this.form_data = String.valueOf(formLocal.getCustom_form_data());
        this.mSo_Prefix = formData.getSo_prefix();
        this.mSo_Code = formData.getSo_code();
        this.require_serial_done = require_serial_done;
        this.require_serial_done_ok = "";
        this.isFormOs = geOs != null;

        if (!formData.getSerial_id().equalsIgnoreCase(serial_id) && !serial_id.isEmpty()) {
            formData.setSerial_id(serial_id);
        } else {
            serial_id = formData.getSerial_id();
        }
        /**
         * BARRIONUEVO - 23-10-2019 - Autosave no onPause
         * Verifica existencia previa de assinatura do n-form e apaga o arquivo caso esteja
         * IN_PROCESSING
         */
        String signaturePath = Constant.CACHE_PATH_PHOTO + "/" + mSignature;
        if(ToolBox.validationCheckFile(signaturePath)
        && mPresenter.isInProcessing(formLocal)){
            cleanSignatureFile(signaturePath);
        }

        includeField = formData.getDataFields().size() == 0 ? true : false;

        int pages = 0;
        boolean automatic=false;
        canSave = mPresenter.isInProcessing(formLocal);

        if (cf_fields != null && cf_fields.size() > 0) {
            pages = Integer.parseInt(cf_fields.get(cf_fields.size() - 1).get(PAGE));
            //
            customFFs = new ArrayList<>();
            screens = new ArrayList<>();
            //
            for (HMAux cf : cf_fields) {

                if (includeField
                    && !cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE).equalsIgnoreCase(CustomFF.LABEL)
                    && !cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE).equalsIgnoreCase(CustomFF.TAB)
                ) {
                    GE_Custom_Form_Data_Field form_data_field = new GE_Custom_Form_Data_Field();
                    form_data_field.setCustomer_code(formData.getCustomer_code());
                    form_data_field.setCustom_form_type(formData.getCustom_form_type());
                    form_data_field.setCustom_form_code(formData.getCustom_form_code());
                    form_data_field.setCustom_form_version(formData.getCustom_form_version());
                    form_data_field.setCustom_form_data(formData.getCustom_form_data());
                    form_data_field.setCustom_form_seq(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
                    //
                    formData.getDataFields().add(form_data_field);
                }

                if(cf.hasConsistentValue(GE_Custom_Form_Field_LocalDao.AUTOMATIC)){
                    automatic = !cf.get(GE_Custom_Form_Field_LocalDao.AUTOMATIC).isEmpty();
                }

                /*
                 * LUCHE - 14/09/2021
                 * Movido os sets do loop que existia posterior a esse para dentro do loop.
                 * Criado var customField que será recebida dentro do switch
                 * Caso passe em algum case, terá os set feito após o switch.
                 * Se for null, não faz nada.
                 */

                CustomFF customField = null;
                switch (cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE)) {
                    case CustomFF.CHAR:
                        customField = cfg_Char(cf);
                        break;
                    case CustomFF.TAB:
                    case CustomFF.LABEL:
                        customField = cfg_Label(cf);
                        break;
                    case CustomFF.COMBOBOX:
                        customField = cfg_ComboBox(cf);
                        break;
                    case CustomFF.NUMBER:
                        customField = cfg_Number(cf);
                        break;
                    case CustomFF.DATE:
                        customField = cfg_Date(cf);
                        break;
                    case CustomFF.HOUR:
                        customField = cfg_Hour(cf);
                        break;
                    case CustomFF.CHECKBOX:
                        customField = cfg_CheckBox(cf);
                        break;
                    case CustomFF.RATINGIMAGE:
                        customField = cfg_RatingImage(cf);
                        break;
                    case CustomFF.RATINGBAR:
                        customField = cfg_RatingBar(cf);
                        break;
                    case CustomFF.PICTURE:
                        customField = cfg_Picture(cf);
                        break;
                    case CustomFF.PHOTO:
                        customField = cfg_Photo(cf);
                        break;
                    default:
                        break;
                }
                //Se != de null, seta o listener e insere nas listas customFFs e controls_dyn
                if(customField != null) {
                    //Implments da interface que faz o scroll ao rodar o dismiss do dialog dos dots
                    customField.setOnDotsDialogDismiss(onBackFocusEvent);
                    //Add na lista de customFF
                    customFFs.add(customField);
                    //Add nos controles dinamicos
                    controls_dyn.add(customField);
                }
            }
            //Tenta resgatar dados do agendamento caso exista.
            MD_Schedule_Exec mdScheduleExec = null;
            if(formLocal.getSchedule_prefix() != null
                && formLocal.getSchedule_code() != null
                && formLocal.getSchedule_exec() != null)
            {
                mdScheduleExec = mPresenter.getMdScheduleExec(formLocal.getSchedule_prefix(), formLocal.getSchedule_code(), formLocal.getSchedule_exec());
            }
           //
            ArrayList<Act011FormTab> tabs = new ArrayList<>();
            //Loop de criação das tabs do form utilizando o novo fragment.
            if(formLocal.getIs_so() == 1){
                //Qtd total de tabs 1(header) + pages(customFF) + acessoryFormViews.size(Devices)
                int fullTabQty = pages + acessoryFormViews.size();
                addOsHeaderFrag(geOs,formData.getCustom_form_status(),tabs,fullTabQty,mdScheduleExec);
                //
                for (int i = 1; i <= pages; i++) {
                    Act011FrgFF custom_form_ff = Act011FrgFF.Companion.newInstance(
                            hmAux_Trans,
                            i,
                            fullTabQty,
                            formData.getCustom_form_status(),
                            mdScheduleExec != null ? mdScheduleExec.getSchedule_desc() : null,
                            mdScheduleExec != null ? mdScheduleExec.getComments() : null,
                        formLocal.getIs_so() == 1

                    );
                    custom_form_ff.setCustomFF(customFFs);
                    //Substituido o param de bNew para includeField, pois ele identifica  aprimeira abertura.
                    //Ajuste necessario pois no caso do agendamento, o bNew era false na primera abertura,
                    //os campos estavam sendo validados e marcados como erro
                    tabs.add(custom_form_ff.getTabObj(includeField));
                    screens.add(custom_form_ff);
                }
                //
                int acessoryIndex = pages + 1;
                for(AcessoryFormView acessoryFormView: acessoryFormViews){
                    Act011FrgInspection act011FrgInspection = Act011FrgInspection.Companion
                            .newInstance(
                                    hmAux_Trans,
                                    acessoryIndex ,
                                    fullTabQty,
                                    formLocal.getCustom_form_status(),
                                    "",
                                    ""
                                    );
                    act011FrgInspection.setViewObject(acessoryFormView);
                    tabs.add(act011FrgInspection.getTabObj(includeField));
                    screens.add(act011FrgInspection);
                    acessoryIndex ++;
                }
            }else {
                for (int i = 1; i <= pages; i++) {
                    Act011FrgFF custom_form_ff = Act011FrgFF.Companion.newInstance(
                            hmAux_Trans,
                            i,
                            pages,
                            formData.getCustom_form_status(),
                            mdScheduleExec != null ? mdScheduleExec.getSchedule_desc() : null,
                            mdScheduleExec != null ? mdScheduleExec.getComments() : null,
                        formLocal.getIs_so() == 1
                    );
                    custom_form_ff.setCustomFF(customFFs);
                    //Substituido o param de bNew para includeField, pois ele identifica  aprimeira abertura.
                    //Ajuste necessario pois no caso do agendamento, o bNew era false na primera abertura,
                    //os campos estavam sendo validados e marcados como erro
                    tabs.add(custom_form_ff.getTabObj(includeField));
                    screens.add(custom_form_ff);
                }
            }
            //
            pager.setOffscreenPageLimit(screens.size());
            pager.setAdapter(
                    new ScreenAdapter(
                            getSupportFragmentManager(),
                            screens
                    )
            );

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    saveV2(false);
//                    act011FfOption.setFOpc(position + 1);
                    //
                    index_old = index;
                    index = position + 1;
                    //
                    setTitleLanguage("          (" + String.valueOf(index) + "/" + String.valueOf(pager.getAdapter().getCount()) + ")");
                    //
                    updateTabStatusIntoDrawer(
                            returnValidateTabObj(index_old)
                        );
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //Seta dados no drawer
            act011FfOption.setFragmentsArgs(
                    new Act011FfOptionsViewObject(
                            form_desc,
                            tabs,
                            isFormOs ? 0 : 1,
                            formData.getCustom_form_status(),
                            mSignature,
                            automatic,
                            (formData.getTicket_prefix() != null && formData.getTicket_prefix() > 0)
                                    && (formData.getTicket_code() != null && formData.getTicket_code() > 0 ),
                            mSo_Prefix != null && mSo_Code != null,
                            isFormOs
                    ),
                    hmAux_Trans,
                    new Act011FfOption.ICustom_Form_FF_Options_ll() {
                        @Override
                        public void info() {
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            showCustomDialog();
                        }

                        @Override
                        public void delete() {
                            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showConfirmDeleteDialog();
                                }
                            };
                            //
                            ToolBox.alertMSG(
                                    Act011_Main.this,
                                    hmAux_Trans.get("dialog_delete_title"),
                                    hmAux_Trans.get("dialog_delete_msg"),
                                    listener,
                                    1
                            );
                        }

                        @Override
                        public void save() {
                            saveV2(true);
                        }

                        @Override
                        public void check() {
                            checkAction(true);
                        }

                        @Override
                        public void autograph() {
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            showCustomDialogSignature();
                        }

                        @Override
                        public void auto() {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            int quantidade = 0;
                            //
                            for (Act011BaseFrg baseFrg : screens) {
                                if(baseFrg instanceof Act011FrgFF) {
                                    int itensAuto = baseFrg.applyAutoAnswer();
                                    if (itensAuto > 0) {
                                        updateTabStatusIntoDrawer(baseFrg.getTabObj(false));
                                        quantidade += itensAuto;
                                    }
                                }
                            }
                            //
                            Toast.makeText(
                                    context,
                                    hmAux_Trans.get("qty_automatic_answer_msg") + ": " + String.valueOf(quantidade),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        @Override
                        public void nserv() {
                            mDrawerLayout.closeDrawer(GravityCompat.START);

                            if (formData != null && formData.getCustom_form_status().equals(Constant.SYS_STATUS_IN_PROCESSING)) {
                                exitAlertNServ();
                            } else {
                                nservCall();
                            }
                        }

                        @Override
                        public void onTabSelected(int page) {
                            //LUCHE - 05/10/2021 - ajuste para quando for form o.s funcione
                            //corretamente.
                            tabSelectedAction(isFormOs ? page + 1 : page);
                            //
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        }
                    });

            returnValidCheck(String.valueOf(index_old));
            if(bNew){
                saveV2(false);
            }
        }
        //LUCHE - 31/03/2020
        //Caso GPS desligado e form dependa de GPS exibe dialog
        //
        mPresenter.validateGPSResource(formLocal);
    }

    private void addOsHeaderFrag(GeOs geOs, String custom_form_status, ArrayList<Act011FormTab> tabs, int tabQty, MD_Schedule_Exec mdScheduleExec) {
        FormOsHeaderFrg formOsHeaderFrg = FormOsHeaderFrg.newInstance(
            hmAux_Trans,
            0,
            tabQty,
            custom_form_status,
            mdScheduleExec != null ? mdScheduleExec.getSchedule_desc() : null,
            mdScheduleExec != null ? mdScheduleExec.getComments() : null,
            geOs,
            false
        );
        //
        tabs.add(formOsHeaderFrg.getTabObj(includeField));
        screens.add(formOsHeaderFrg);
    }

    @Override
    public void alertActiveGPSResource() {
        ToolBox.alertMSG(
            this,
            hmAux_Trans.get("alert_form_turn_gps_on_title"),
            hmAux_Trans.get("alert_form_turn_gps_on_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.validateGPSResource(formLocal);
                }
            },
            2,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //LUCHE - 31/03/2020 - Gambi para evitar msg de deseja sair ao forçar volta
                    checkBackFlow();
                }
            }
        );

    }

    private void cleanSignatureFile(String signaturePath) {
        File file = new File(signaturePath);
        try {
            if (file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            ToolBox.registerException(getClass().getName(), e);
        }
    }

    private CustomFF cfg_Label(HMAux cf) {
        LabelFF labelFF = new LabelFF(Act011_Main.this);

        labelFF.setId(View.generateViewId());
        labelFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        labelFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        labelFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        labelFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        labelFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        return labelFF;
    }
    /**
     * BARRIONUEVO - 23-10-2019 - Autosave no onPause
     * Função que visa diminuir a perda de dados no N-Form como metodo paliativo para o crash de
     * unmarshalling.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(canSave) {
            saveV2(false);
        }
    }

    private CustomFF cfg_Char(HMAux cf) {
        MKEditTextNMFF mkEditTextNMFF = new MKEditTextNMFF(Act011_Main.this);

        mkEditTextNMFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        mkEditTextNMFF.setId(View.generateViewId());
        mkEditTextNMFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        mkEditTextNMFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        mkEditTextNMFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        mkEditTextNMFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        mkEditTextNMFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        mkEditTextNMFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

//        Remover a configuraçao automatica do campo customizado de edicao mesmo que o equipamento suporte NFC.
//        if (hasNFCSupport) {
//            mkEditTextNMFF.setmNFC(true);
//        } else {
//            mkEditTextNMFF.setmNFC(false);
//        }

        if (cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE).equalsIgnoreCase(CustomFF.DATE)) {
            mkEditTextNMFF.setmBARCODE(false);
            mkEditTextNMFF.setmNFC(false);
            mkEditTextNMFF.setmOCR(false);
            //mkEditTextNMFF.setmMask("{\"MASK\":[{\"TYPE\":\"DATE\",\"VALUE\":\"$$/$$/$$$$\"}]}");
        } else if (cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE).equalsIgnoreCase(CustomFF.HOUR)) {
            mkEditTextNMFF.setmBARCODE(false);
            mkEditTextNMFF.setmNFC(false);
            mkEditTextNMFF.setmOCR(false);
            //mkEditTextNMFF.setmMask("{\"MASK\":[{\"TYPE\":\"HOUR\",\"VALUE\":\"$$:$$\"}]}");
        } else {
            mkEditTextNMFF.setmMask(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_MASK));
            //mkEditTextNMFF.setmMask("{\"MASK\":[{\"TYPE\":\"CPF\",\"VALUE\":\"$$$.$$$.$$$-$$\"}]}");
        }

        mkEditTextNMFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));
        mkEditTextNMFF.setmMaxSize(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_SIZE)));

        if (mkEditTextNMFF.getmMaxSize() == 0 && cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE).equalsIgnoreCase(CustomFF.NUMBER)) {

            String[] opcs = null;

            try {
                JSONObject jsonObject = new JSONObject(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));
                JSONArray jsonArray = jsonObject.getJSONArray(CONTENT);
                //
                opcs = new String[jsonArray.length()];
                //
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    opcs[i] = jo.getString(DECIMAL);
                }
                //
                mkEditTextNMFF.setmDecimal(Integer.parseInt(opcs[0]));
            } catch (JSONException e) {
                mkEditTextNMFF.setmDecimal(0);
            }
        }

        mkEditTextNMFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        mkEditTextNMFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        mkEditTextNMFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        mkEditTextNMFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//              ||  formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                ) {
            mkEditTextNMFF.setmEnabled(false);
        } else {
            mkEditTextNMFF.setmEnabled(true);
        }

        return mkEditTextNMFF;
    }

    private CustomFF cfg_ComboBox(HMAux cf) {
        ComboBoxFF comboBoxFF = new ComboBoxFF(Act011_Main.this);

        comboBoxFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        comboBoxFF.setId(View.generateViewId());
        comboBoxFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        comboBoxFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        comboBoxFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        comboBoxFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        comboBoxFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        comboBoxFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        comboBoxFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));

        comboBoxFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        comboBoxFF.setmPre(prefix);


        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        comboBoxFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        comboBoxFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//               || formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                ) {
            comboBoxFF.setmEnabled(false);
        } else {
            comboBoxFF.setmEnabled(true);
        }

        return comboBoxFF;
    }

    private CustomFF cfg_Number(HMAux cf) {
        return cfg_Char(cf);
    }

    private CustomFF cfg_Date(HMAux cf) {
        return cfg_Char(cf);
    }

    private CustomFF cfg_Hour(HMAux cf) {
        return cfg_Char(cf);
    }

    private CustomFF cfg_CheckBox(HMAux cf) {
        CheckBoxFF checkBoxFF = new CheckBoxFF(Act011_Main.this);

        checkBoxFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        checkBoxFF.setId(View.generateViewId());
        checkBoxFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        checkBoxFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        checkBoxFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        checkBoxFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        checkBoxFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        checkBoxFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        checkBoxFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));

        checkBoxFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        checkBoxFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        checkBoxFF.setmAutomatic(cf.get(GE_Custom_Form_Field_LocalDao.AUTOMATIC));

        checkBoxFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        checkBoxFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//               || formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
        ) {
            checkBoxFF.setmEnabled(false);
        } else {
            checkBoxFF.setmEnabled(true);
        }

        return checkBoxFF;
    }

    private CustomFF cfg_RatingImage(HMAux cf) {
        RatingImageFF ratingImageFF = new RatingImageFF(Act011_Main.this);

        ratingImageFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        ratingImageFF.setId(View.generateViewId());
        ratingImageFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        ratingImageFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        ratingImageFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        ratingImageFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        ratingImageFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        ratingImageFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        ratingImageFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));

        ratingImageFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        ratingImageFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        ratingImageFF.setmAutomatic(cf.get(GE_Custom_Form_Field_LocalDao.AUTOMATIC));

        ratingImageFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        ratingImageFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//               || formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                ) {
            ratingImageFF.setmEnabled(false);
        } else {
            ratingImageFF.setmEnabled(true);
        }

        return ratingImageFF;
    }

    private CustomFF cfg_RatingBar(HMAux cf) {
        RatingBarFF ratingBarFF = new RatingBarFF(Act011_Main.this);

        ratingBarFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        ratingBarFF.setId(View.generateViewId());
        ratingBarFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        ratingBarFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        ratingBarFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        ratingBarFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        ratingBarFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        ratingBarFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        ratingBarFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));

        ratingBarFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        ratingBarFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        ratingBarFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        ratingBarFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//              ||  formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                ) {
            ratingBarFF.setmEnabled(false);
        } else {
            ratingBarFF.setmEnabled(true);
        }

        return ratingBarFF;
    }

    private CustomFF cfg_Picture(HMAux cf) {
        PictureFF pictureFF = new PictureFF(Act011_Main.this);

        pictureFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        pictureFF.setId(View.generateViewId());
        pictureFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        pictureFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        pictureFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        pictureFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        pictureFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        pictureFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        pictureFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));
        pictureFF.setmFName(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_LOCAL_LINK));

        pictureFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        pictureFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        pictureFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        pictureFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//               || formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                ) {
            pictureFF.setmEnabled(false);
        } else {
            pictureFF.setmEnabled(true);
        }

        return pictureFF;
    }

    private CustomFF cfg_Photo(HMAux cf) {
        PhotoFF photoFF = new PhotoFF(Act011_Main.this);

        photoFF.setmDots_txt_app(cf.get(GE_Custom_Form_Field_LocalDao.COMMENT));

        photoFF.setId(View.generateViewId());
        photoFF.setmRequire_photo_on_nc(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRE_PHOTO_ON_NC).equals("1") ? true : false);
        photoFF.setmLabel(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_FIELD_DESC));
        photoFF.setmOrder(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_ORDER)));
        photoFF.setmSequence(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));
        photoFF.setmPage(Integer.parseInt(cf.get(PAGE)));
        photoFF.setmType(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_TYPE));

        photoFF.setmOption(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_DATA_CONTENT));

        photoFF.setmRequired(cf.get(GE_Custom_Form_Field_LocalDao.REQUIRED).equalsIgnoreCase("1") ? true : false);
        photoFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ)));

        if (itemDB.hasConsistentValue(HMAux.TEXTO_01)
        && itemDB.get(HMAux.TEXTO_01).length() > 0) {
            photoFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        }else{
            photoFF.setmValue("p_" + prefix + cf.get(GE_Custom_Form_Field_LocalDao.CUSTOM_FORM_SEQ) + JPG_EXTENSION);
        }
        photoFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));
        //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
        //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DONE)
//              ||  formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_DELETED)
                ) {
            photoFF.setmEnabled(false);
        } else {
            photoFF.setmEnabled(true);
        }

        return photoFF;
    }

    private HMAux retornDBValue(int seq) {
        HMAux hmAux = new HMAux();
        //
        if (formData != null) {
            for (GE_Custom_Form_Data_Field form_data_field : formData.getDataFields()) {
                if (form_data_field.getCustom_form_seq() == seq) {
                    hmAux.put(HMAux.TEXTO_01, form_data_field.getValue());
                    hmAux.put(HMAux.TEXTO_02, form_data_field.getValue_extra());
                }
            }
        }
        //
        return hmAux;
    }

    /**
     * Metodo que pega os valores do customFF e seta do form_data_fields
     * Faz o loop nos customFF buscano a view da sequencia do form_data_fields e quando encontra,
     * copia os valores de getmValue e getmValue_Extra para form_data_fields.value e valuewsExtra
     * @param df
     */
    private void setCustomFFValueIntoFormDataField(GE_Custom_Form_Data_Field df) {
        for (int i = 0; i < customFFs.size(); i++) {
            if (customFFs.get(i).getmSequence() == df.getCustom_form_seq()) {
                df.setValue(customFFs.get(i).getmValue());
                df.setValue_extra(customFFs.get(i).getmValue_Extra());
                break;
            }
        }
    }

    /**
     * LUCHE - 14/09/2021
     * Iniciado a nova implementação de codigo.
     * Quando sPage = -1 , faz loop na lista de screen chamando a validação em cada uma.
     * Se sPage > 0 , "ajusta" indice da lista e chama o metodo de validação daquela aba especifica
     * Se sPage 0, não faz nada
     * @param sPage
     * @return
     */
    private int returnValidCheck(String sPage) {
        int numberOfErrors = 0;
        int ipage = Integer.parseInt(sPage);
        try {
            if(ipage == -1) {
                for (Act011BaseFrg act011BaseFrg : screens) {
                    numberOfErrors += ((Act011FrgFF) act011BaseFrg).getTabErrorCount();
                }
            }else if(ipage > 0) {
                ipage = ipage - 1;
                numberOfErrors = screens.get(ipage).getTabErrorCount();
            }
        }catch (Exception e) {
            //
            for (int i = 0; i < customFFs.size(); i++) {
                //Projeto delecao logica de formulario visava a consulta do nform deletado via menu Historico
                //mas a vida eh uma caixinha de surpresas e teve que ser removido t0d0 acesso aos nform deletados
                if (ipage == -1) {
                    if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                        numberOfErrors += 1;
                    }
    //                if(formData.getCustom_form_status() != null && !formData.getCustom_form_status().equals(ConstantBase.SYS_STATUS_DELETED)) {
                        customFFs.get(i).setValidationBackGroundDots();
    //                }
                } else {
                    if (customFFs.get(i).getmPage() == ipage) {
                        if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                            numberOfErrors += 1;
                        }
    //                    if(formData.getCustom_form_status() != null && !formData.getCustom_form_status().equals(ConstantBase.SYS_STATUS_DELETED)) {
                            customFFs.get(i).setValidationBackGroundDots();
    //                    }
                    } else {
                    }
                }
            }
        }

        return numberOfErrors;
    }

    /**
     * Metodo que faz validação de 1 o todas as tabs e retorna lista de
     * Act011FormTab atualizado
     *
     * @param page -1  se deve validar todos ou >0 se especifico
     * @return
     */
    private ArrayList<Act011FormTab> returnValidateTabObj(int page){
        ArrayList<Act011FormTab> tabs = new ArrayList<>();
        if(page == -1){
            for (Act011BaseFrg baseFrg : screens) {
                tabs.add(baseFrg.getTabObj(false));
            }
        }else if(page > 0){
            //Ajuste para pega o indice correto da tab no array de frags.
            page--;
            tabs.add(screens.get(page).getTabObj(false));
        }
        return tabs;
    }

    @Override
    public void onInspectionSelected(@NotNull AcessoryFormView acessoryFormView, @NotNull InspectionCellActions action, int position, @NotNull String textFilter) {
        String device_item_pk = acessoryFormView.getDevicePkPrefix();
        if(!action.equals(InspectionCellActions.ADD_NEW_ITEM)){
            device_item_pk = acessoryFormView.getDevicePkPrefix() + acessoryFormView.getInspections().get(position).getItemCodeAndSeq();
        }
        Intent mIntent = new Intent(context, Act086Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle deviceBundle = new Bundle();
        deviceBundle.putString(GeOsDeviceDao.DEVICE_TP_DESC, acessoryFormView.getAcessoryName());
        deviceBundle.putString(GeOsDeviceDao.TRACKING_NUMBER, acessoryFormView.getAcessoryTracking());
        deviceBundle.putString(DEVICE_ITEM_PK,device_item_pk);
        deviceBundle.putInt(DEVICE_ITEM_TAB_INDEX,acessoryFormView.getTabIndex());
        deviceBundle.putInt(DEVICE_ITEM_LIST_INDEX,position);
        deviceBundle.putString(DEVICE_ITEM_LIST_FILTER,textFilter);
        deviceBundle.putString(DEVICE_ITEM_LIST_ACTION,action.getAction());
        bundle.putBundle(DEVICE_BUNDLE, deviceBundle);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    //TODO APAGAR APPOS TESTES FINAL
//    private HMAux returnValidCheckTabs(String sPage) {
//
//        ArrayList<HMAux> itens = new ArrayList<>();
//
//        HMAux item = new HMAux();
//
//        HMAux ipages = new HMAux();
//        int ipage = Integer.parseInt(sPage);
//        //
//        for (int i = 0; i < customFFs.size(); i++) {
//            HMAux aux = new HMAux();
//
//            if (ipage == -1) {
//
//                if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
//                    aux.put(PAGE, String.valueOf(customFFs.get(i).getmPage()));
//                    aux.put("value", "ERROR");
//                } else {
//                    aux.put(PAGE, String.valueOf(customFFs.get(i).getmPage()));
//                    aux.put("value", ConstantBaseApp.MAIN_RESULT_OK);
//                }
//
//                ipages.put(String.valueOf(customFFs.get(i).getmPage()), "");
//
//                itens.add(aux);
//
//
//            } else {
//                if (ipage == customFFs.get(i).getmPage()) {
//                    if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
//                        aux.put(PAGE, String.valueOf(customFFs.get(i).getmPage()));
//                        aux.put("value", "ERROR");
//                    } else {
//                        aux.put(PAGE, String.valueOf(customFFs.get(i).getmPage()));
//                        aux.put("value", ConstantBaseApp.MAIN_RESULT_OK);
//                    }
//
//                    ipages.put(String.valueOf(customFFs.get(i).getmPage()), "");
//
//                    itens.add(aux);
//                }
//            }
//
//        }
//
//        Set keys = ipages.keySet();
//
//        for (Iterator i = keys.iterator(); i.hasNext(); ) {
//            String key = (String) i.next();
//            String value = (String) ipages.get(key);
//
//            item.put(key, pageStatus(key, itens));
//
//        }
//
//        return item;
//    }
//
//    private String pageStatus(String page, ArrayList<HMAux> itens) {
//        int total = 0;
//        int ok = 0;
//        int error = 0;
//        int pending = 0;
//
//        for (HMAux aux : itens) {
//
//            if (aux.get(PAGE).equals(page)) {
//                switch (aux.get("value").toUpperCase()) {
//                    case "PENDING":
//                        pending++;
//                        break;
//                    case "ERROR":
//                        error++;
//                        break;
//                    case ConstantBaseApp.MAIN_RESULT_OK:
//                        ok++;
//                        break;
//                    default:
//                        break;
//                }
//
//                total++;
//            }
//        }
//
//        if (error != 0) {
//            return "ERROR";
//        }
//
//        if (total == pending) {
//            return "PENDING";
//        }
//
//        if (total == ok) {
//            return ConstantBaseApp.MAIN_RESULT_OK;
//        }
//
//        return "EXEC";
//    }


    private class ScreenAdapter extends FragmentPagerAdapter {

        //private ArrayList<Fragment> data;
        private ArrayList<Act011BaseFrg> data;

        public ScreenAdapter(FragmentManager fm, ArrayList<Act011BaseFrg> dados) {
            super(fm);
            this.data = dados;
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        //return super.onPrepareOptionsMenu(menu);
//        //Pega os settings do menu e esconde
//        MenuItem item = menu.findItem(R.id.act11_action_settings);
//        item.setVisible(false);
//
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.act011_main_menu, menu);

        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));

        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.act11_action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMsg(String title, String msg, int type) {
        switch (type) {
            case 0:

                ToolBox.alertMSG(
                        Act011_Main.this,
                        title,
                        msg,
                        null,
                        0
                );

                break;
            case 1:
                ToolBox.alertMSG(
                        Act011_Main.this,
                        title,
                        msg,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callSignature();
                            }
                        },
                        0
                );

                break;
            case 2:
                ToolBox.alertMSG(
                        Act011_Main.this,
                        title,
                        msg,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                defineFinalizeFlow();
                            }
                        },
                        0,
                        false
                );
                break;

            case 3:
                ToolBox.alertMSG(
                        Act011_Main.this,
                        title,
                        msg,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callSignature();
                            }
                        },
                        2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // hugo assinatura
                                formData.setSignature("");
                                formData.setSignature_name("");

                                mPresenter.checkData(formData, geFiles, require_serial_done, require_serial_done_ok, formLocal.getRequire_location());
                                bNew = false;
                            }
                        }
                );

                break;
            //Msg quando ocorre erro ao criar registro na ge_custom_form_local
            case SHOW_MSG_TYPE_FORM_LOCAL_INSERT_ERROR:
            //Msg quando ocorrer erro ao atualizar dados do novo agendamento.
            case SHOW_MSG_TYPE_SCHEDULE_EXEC_UPDATE_ERROR:
                ToolBox.alertMSG(
                        Act011_Main.this,
                        title,
                        msg,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flowControl();
                            }
                        },
                        0
                );
                break;
            case SHOW_MSG_TYPE_SCHEDULE_EXEC_CANCEL_ERROR:
            case SHOW_MSG_TYPE_TICKET_STEP_OR_CTRL_ERROR:
                //TODO O QUE FAZER NO CASO DO TICKET
                ToolBox.alertMSG(
                    context,
                    title,
                    msg,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    },
                    0
                );
                break;
            case SHOW_MSG_TYPE_TICKET_FORM_FINALIZED:
                ToolBox.alertMSG(
                    Act011_Main.this,
                    title,
                    msg,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flowControl();
                        }
                    },
                    0,
                    false
                );
                break;

        }
    }

    @Override
    public void defineFinalizeFlow() {
        if (ToolBox_Con.isOnline(context)) {
            enableProgressDialog(
                    hmAux_Trans.get("alert_send_finish_ttl"),
                    hmAux_Trans.get("alert_send_finish_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );

            executeSerialSave();
        } else {
            flowControl();
        }
    }

    @Override
    public void showFormCancelledMsg(final GE_Custom_Form_Local customFormLocal, final MD_Schedule_Exec scheduleExec) {
        android.app.AlertDialog.Builder dialogScheduleWarning = new android.app.AlertDialog.Builder(context);
        dialogScheduleWarning.setTitle(hmAux_Trans.get("alert_schedule_cancelled_by_server_ttl"));
        dialogScheduleWarning.setMessage(
                ToolBox_Inf.getFormattedScheduleWarningInfo(
                    hmAux_Trans.get("alert_schedule_warning_new_status_lbl"),
                    hmAux_Trans.get(scheduleExec.getFcm_new_status()),
                    hmAux_Trans.get("alert_warning_user_nick_lbl"),
                    scheduleExec.getFcm_user_nick(),
                    null,
                    null,
                    hmAux_Trans.get("alert_schedule_cancelled_by_server_msg")+"\n"
                )
        );
        dialogScheduleWarning.setCancelable(true);
        dialogScheduleWarning.setPositiveButton(
            hmAux_Trans.get("sys_alert_btn_ok"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.cancelScheduleAndForm(customFormLocal,scheduleExec);
                }
            }
        );
        //
        AlertDialog dialog = dialogScheduleWarning.create();
        dialog.show();
        //
        dialog.getButton(
            DialogInterface.BUTTON_POSITIVE
        ).setTextColor(getResources().getColor(R.color.namoa_lime_green));
    }

    /**
     * LUCHE - 31/08/2020
     * <P></P>
     * Modificado o if else que existia por if return e adicionado tratativa para o caso da act070(ticket)
     */

    public void flowControl() {
        //ToolBox_Inf.showNoConnectionDialog(Act011_Main.this);
        /*
            30-08-2019 Barrionuevo
            Garante que o onPause nao salvarah nada em cima do ultimo save enviado para o servico
            23-10-2019
            Tratativa para não alterar os dados apos a execucao do servico de envio do n-form
         */
        canSave = false;
        //LUCHE - 31/08/2020
        if(mPresenter.isaTicketFlowForm()){
            callAct070();
            return;
        }
        if(mSo_Prefix != null && mSo_Code != null){
            nservCall();
            return;
        }
        if (finalizeNewFlow) {
            if (mPresenter.checkNFormExists(formLocal)) {
                callAct006(context,finalizeNewFlow);
            } else {
                finalizeNewFlow = false;
                //
                ToolBox.alertMSG(
                    Act011_Main.this,
                    hmAux_Trans.get("alert_nform_expired_ttl"),
                    hmAux_Trans.get("alert_nform_expired_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callAct006(context,finalizeNewFlow);
                        }
                    },
                    0
                );
            }
        } else {
            mPresenter.checkOriginDoneFlow(act083Bundle);
        }
    }

    public void exitAlert() {
             /*
                30-08-2019  BARRIONUEVO
                Agora ao sair o usuario nao perdera os dados, logo a mensagem de aviso mudou
             */
        String alertTitle = hmAux_Trans.get("alert_exit_confirmation_ttl");
        String alertMsg = hmAux_Trans.get("alert_exit_confirmation_msg");
        //
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
                //callAct005(Act011_Main.this);
                mPresenter.onBackPressedClicked();
            }
        };

        ToolBox.alertMSG(
                Act011_Main.this,
                alertTitle,
                alertMsg,
                listener,
                1
        );
    }

    private boolean hasAnyValueChanged() {
        for (CustomFF customFF : customFFs) {
            if(customFF != null && !customFF.getmValue().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public void exitAlertNServ() {

        String alertTitle = hmAux_Trans.get("exit_alert_ttl");
        String alertMsg = hmAux_Trans.get("exit_alert_msg");
        //
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // modificar para incluir a remossao do custom_form_local.
                //mPresenter.saveData(formData, false);
                if (bNew) {
                    mPresenter.deleteFormLocal(formLocal);
                }
                //
                nservCall();
            }
        };

        ToolBox.alertMSG(
                Act011_Main.this,
                alertTitle,
                alertMsg,
                listener,
                1
        );
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct006(Context context, boolean finalizeNewFlow) {
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        if(finalizeNewFlow) {
            bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(formLocal.getCustom_product_code()));
            bundle.putString(MD_ProductDao.PRODUCT_DESC, formLocal.getCustom_product_desc());
            bundle.putString(MD_ProductDao.PRODUCT_ID, formLocal.getCustom_product_id());
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, formLocal.getSerial_id());
            bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, String.valueOf(formLocal.getCustom_form_type()));
            bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, String.valueOf(formLocal.getCustom_form_code()));
            bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, String.valueOf(formLocal.getCustom_form_version()));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, formLocal.getCustom_form_desc());
        }
        //LUCHE - 18/06/2021 comentado else de finalizeNewFlow  que passava via bundle as infos de produto e serial para serem usadas na act006. Segundo jhon isso foi definido erroneamente na documentação
        // else{
//            bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, formLocal.getCustom_product_id());
//            bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, formLocal.getSerial_id());
//        }
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        //Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct070() {
        Intent mIntent = new Intent(context, Act070_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, mTicket_prefix != null ? mTicket_prefix : -1 );
        bundle.putInt(TK_TicketDao.TICKET_CODE, mTicket_code != null ? mTicket_code : -1 );
        //
        bundle.putInt(TK_Ticket_CtrlDao.STEP_CODE, formData.getStep_code());
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, formData.getTicket_seq());
        bundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ_TMP, formData.getTicket_seq_tmp());
        //
        if(isOffHandForm){
             bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestingAct);
        }
        bundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        //LUCHE - 08/09/2020
        //Se é finalização do form e esta voltando pra act070, seta flag para forçar o envio ao chegar na act
        if(mPresenter.setForceSentByForm(formData.getCustomer_code(),formData.getCustom_form_type(),formData.getCustom_form_code(),formData.getCustom_form_version(), (int) formData.getCustom_form_data())){
            bundle.putBoolean(Act070_Main.PARAM_FORCE_SEND_BY_FORM_EXEC,true);
        }
        if(act083Bundle != null) {
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083);
            bundle.putAll(act083Bundle);
        }
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct083() {
        Intent intent = new Intent(context, Act083_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle mBundle = new Bundle();
        getMyActionsParam(mBundle);
        intent.putExtras(mBundle);
        //
        startActivity(intent);
        finish();
    }

    private void getMyActionsParam(Bundle mBundle) {
        if(act083Bundle != null) {
            mBundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083);
            MyActionFilterParam myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(act083Bundle);
            //Se não tinha dados é pq é um novo form, seta então a nova pk
            if(myActionFilterParam.getParamItemSelectedPk() == null || myActionFilterParam.getParamItemSelectedPk().isEmpty() ){
                myActionFilterParam.setParamItemSelectedPk(formLocal.getFormatedPk());
                myActionFilterParam.setParamItemSelectedType(MyActions.MY_ACTION_TYPE_FORM);
                act083Bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM,myActionFilterParam);
            }
            mBundle.putAll(act083Bundle);
        }
    }

    @Override
    public void callAct084() {
        Intent intent = new Intent(context, Act084Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle mBundle = new Bundle();
        mBundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, Constant.ACT005);
        mBundle.putSerializable(
            MyActionFilterParam.MY_ACTION_FILTER_PARAM,
            bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
        );
        intent.putExtras(mBundle);
        //
        startActivity(intent);
        finish();
    }

    @Override
    public boolean isOffHandForm() {
        return isOffHandForm;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //mPresenter.onBackPressedClicked();
        if (formData != null){
            if(formData.getCustom_form_status().equals(Constant.SYS_STATUS_IN_PROCESSING)) {
                exitAlert();
            }else {
                checkBackFlow();
            }
        } else {
           callAct005(Act011_Main.this);
        }
    }

    /**
     * LUCHE - 31/03/2020
     * Extraido metodo que verifica o fluxo de voltar
     * Feito isso para poder executar o fluxo de voltar sem exibir a msg de confirmação no fluxo
     * do bvoltar por GPS desligado.
     *
     */
    private void checkBackFlow() {
        if(mPresenter.isaTicketFlowForm()) {
            callAct070();
        }else if(ConstantBaseApp.ACT084.equals(requestingAct)
            || ConstantBaseApp.SYS_STATUS_DONE.equals(formData.getCustom_form_status())){
            callAct084();
        }else {
            callAct083();
        }
    }

    @Override
    public void showSignature() {

    }

    @Override
    protected void getSignatueF(String mValue) {
        String sName = mValue;
        canSave = mPresenter.isInProcessing(formLocal);
        if (sName.trim().length() != 0 && !sName.equals(Constant.CACHE_PATH_PHOTO + "/" + mSignature) ) {

            File sFile = new File(Constant.CACHE_PATH_PHOTO + "/" + mSignature);
            if (sFile.exists()) {
                formData.setSignature(mSignature);
                formData.setSignature_name(sName);
                //
                GE_File geFile = new GE_File();
                geFile.setFile_code(mSignature.replace(PNG_EXTENSION, ""));
                geFile.setFile_path(mSignature);
                geFile.setFile_status(GE_File.OPENED);
                geFile.setFile_date(sDate);
                //
                geFiles.add(geFile);
                //

                mPresenter.checkData(formData, geFiles, require_serial_done, require_serial_done_ok, formLocal.getRequire_location());
                bNew = false;
            } else {
                formData.setSignature_name("");
                //Luche - 28/02/2019
                //Reseta var de fluxo finaliza + novo
                finalizeNewFlow = false;
                //LUCHE - 22/10/2019
                //Após bug de dados do GPS sendo limpos pelo save no onpause,
                //O reseta das informações de GPS serão feitos apenas nos cancelamentos
                //da assinatura, do gps e do serial.
                formData.setLocation_lat("");
                formData.setLocation_lng("");
                formData.setLocation_type("");
            }
        } else {
            //LUCHE - 22/10/2019
            //Após bug de dados do GPS sendo limpos pelo save no onpause,
            //O reseta das informações de GPS serão feitos apenas nos cancelamentos
            //da assinatura, do gps e do serial.
            formData.setLocation_lat("");
            formData.setLocation_lng("");
            formData.setLocation_type("");
        }
    }

    @Override
    public void callSignature() {
        canSave = false;
        try {
            Bundle bundleN = new Bundle();
            bundleN.putInt(ConstantBase.PID, -1);
            bundleN.putInt(ConstantBase.PTYPE, 0);
            bundleN.putString(ConstantBase.PPATH, CACHE_PATH_PHOTO + "/" + mSignature);

            Intent mIntent = new Intent(context, SignaTure_Activity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundleN);

            context.startActivity(mIntent);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            canSave = mPresenter.isInProcessing(formLocal);
        }
    }

    /**
     * Resultado da leitura de NFC do serial
     * @param mValue
     */
    @Override
    protected void getNFCResults(String mValue) {
        String sResults = mValue;
        canSave = mPresenter.isInProcessing(formLocal);
        //Se resultado
        if (sResults.trim().length() != 0 && sResults.equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_OK)) {
            require_serial_done_ok = ConstantBaseApp.MAIN_RESULT_OK;
            formData.setDate_end(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));
            mPresenter.checkData(formData, geFiles, require_serial_done, require_serial_done_ok, formLocal.getRequire_location());
            //
            bNew = false;
        } else {
            //Se cancelou
            File sFile = new File(Constant.CACHE_PATH_PHOTO + "/" + mSignature);
            if (sFile.exists()) {
                sFile.delete();
            }
            //
            formData.setSignature("");
            formData.setSignature_name("");
            formData.setLocation_lat("");
            formData.setLocation_lng("");
            formData.setLocation_type("");
            formData.setDate_end("1900-01-01 00:00:00 +00:00");
            //Luche - 28/02/2019
            //Reseta var de finaliza + novo
            finalizeNewFlow = false;

        }
    }

    @Override
    public void callNFCResults() {
        require_serial_done_ok = "";
        canSave = false;
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantBase.PID, -1);
            bundle.putInt(ConstantBase.PTYPE, 2);

            bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, product_code);
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial_id);

            Intent mIntent = new Intent(context, Act022_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtras(bundle);

            context.startActivity(mIntent);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            canSave = mPresenter.isInProcessing(formLocal);
        }
    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        infoDialog = new Dialog(Act011_Main.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.act011_dialog_form_info, null);

        TextView tv_title = (TextView) view.findViewById(R.id.act_011_dialog_tv_title);

        TextView tv_product_title_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_product_title_lbl);
//        TextView tv_product_code_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_product_code_lbl);
//        TextView tv_product_code_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_product_code_val);
//        TextView tv_product_id_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_product_id_lbl);
//        TextView tv_product_id_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_product_id_val);

        TextView tv_product_desc = (TextView) view.findViewById(R.id.act_011_dialog_tv_product_desc);
        //
        TextView tv_serial_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_serial_code_lbl);
        TextView tv_serial_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_serial_code_val);
        TextView tv_serial_desc = (TextView) view.findViewById(R.id.act_011_dialog_tv_serial_id);
        //
        TextView tv_form_title = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_title);
        TextView tv_form_type_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_type_lbl);
        TextView tv_form_type_desc = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_type_desc);
        //
        TextView tv_form_code_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_code_lbl);
        TextView tv_form_code_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_code_val);
        TextView tv_form_code_desc = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_code_desc);
        //
        TextView tv_form_version_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_version_lbl);
        TextView tv_form_version_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_form_version_val);
        //
        LinearLayout ll_schedule_info = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_scheduel_info);
        //
        TextView tv_so_code_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_so_code_lbl);
        TextView tv_so_code_desc = (TextView) view.findViewById(R.id.act_011_dialog_tv_so_code_desc);
        //
        TextView tv_ticket_info_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_ticket_info_lbl);
        TextView tv_ticket_info_desc = (TextView) view.findViewById(R.id.act_011_dialog_tv_ticket_info_desc);
        //
        TextView tv_data_serv_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_data_serv_lbl);
        TextView tv_data_serv_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_data_serv_val);
        //
        TextView tv_dt_schedule_start_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_schedule_dt_start_lbl);
        TextView tv_dt_schedule_start_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_schedule_dt_start_val);
        //
        TextView tv_dt_schedule_end_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_schedule_dt_end_lbl);
        TextView tv_dt_schedule_end_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_schedule_dt_end_val);
        //
        TextView tv_title_pdf = (TextView) view.findViewById(R.id.act_011_dialog_tv_title_pdf);
        ListView lv_pdfs = (ListView) view.findViewById(R.id.act_011_dialog_lv_pdfs);


//      Para o controle de visibilidade de elementos com dados
        LinearLayout ll_serial_info = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_serial_info);
        LinearLayout ll_serial = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_serial);
        LinearLayout ll_class = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_class);
        LinearLayout ll_category = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_category);
        LinearLayout ll_segment = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_segment);
        LinearLayout ll_site = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_site);
        LinearLayout ll_zone = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_zone);
        LinearLayout ll_position = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_position);
        LinearLayout ll_tracking = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_tracking);
        LinearLayout ll_tracking_val = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_tracking_val);
        LinearLayout ll_info_1 = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_info_1);
        LinearLayout ll_info_2 = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_info_2);
        LinearLayout ll_info_3 = (LinearLayout) view.findViewById(R.id.act_011_dialog_ll_info_3);

        TextView tv_descriptions = (TextView) view.findViewById(R.id.act_011_dialog_tv_description);
        TextView tv_serial_code_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_serial_code_val);
        TextView tv_classe_id_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_class_id_val);
        TextView tv_categoria_code_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_category_code_val);
        TextView tv_segmento_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_segment_val);
        TextView tv_site_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_site_val);
        TextView tv_zona_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_zone_val);
        TextView tv_posicao_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_position_val);
        TextView tv_info_1_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_info_1_val);
        TextView tv_info_2_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_info_2_val);
        TextView tv_info_3_val = (TextView) view.findViewById(R.id.act_011_dialog_tv_info_3_val);

        TextView tv_serial_code_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_serial_code_lbl);
        TextView tv_class_id_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_class_id_lbl);
        TextView tv_category_code_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_category_code_lbl);
        TextView tv_segment_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_segment_lbl);
        TextView tv_site_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_site_lbl);
        TextView tv_zone_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_zone_lbl);
        TextView tv_position_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_position_lbl);
        TextView tv_info_1_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_info_1_lbl);
        TextView tv_info_2_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_info_2_lbl);
        TextView tv_info_3_lbl = (TextView) view.findViewById(R.id.act_011_dialog_tv_info_3_lbl);

        //Campos que sempre existem
        tv_title.setText(hmAux_Trans.get("dialog_info_title_lbl"));
        tv_product_title_lbl.setText(hmAux_Trans.get("dialog_info_product_lbl"));
        tv_form_title.setText(hmAux_Trans.get("dialog_info_form_ttl"));
        tv_form_type_lbl.setText(hmAux_Trans.get("dialog_info_form_type_lbl"));
        tv_form_code_lbl.setText(hmAux_Trans.get("dialog_info_form_code_lbl"));
        tv_form_version_lbl.setText(hmAux_Trans.get("dialog_info_form_version_lbl"));
        tv_so_code_lbl.setText(hmAux_Trans.get("dialog_info_so_code_lbl"));
        tv_ticket_info_lbl.setText(hmAux_Trans.get("dialog_info_ticket_code_lbl"));
        tv_product_desc.setText(product_desc);
        tv_serial_val.setText(serial_id);
        tv_form_type_desc.setText(type);
        tv_form_code_val.setText(form);
        tv_form_code_desc.setText(form_desc);
        tv_form_version_val.setText(form_version);

        //Campos do Serial podem ou não existir
        MD_Product_Serial serial = null;

        if(serial_id == null || serial_id.isEmpty()) {
            //Pegar info do serial
            ll_serial_info.setVisibility(View.GONE);
        }else{
             serial = getSerialInfo();
             //
            if(serial != null) {
                setDescriptions(tv_descriptions, serial);
                setSerialInfo(ll_serial, tv_serial_code_val, serial.getSerial_id());
                setSerialInfo(ll_class, tv_classe_id_val, serial.getClass_id());
                setSerialInfo(ll_category, tv_categoria_code_val, serial.getCategory_price_id(), serial.getCategory_price_desc());
                setSerialInfo(ll_segment, tv_segmento_val, serial.getSegment_id(), serial.getSegment_desc());
                setSerialInfo(ll_site, tv_site_val, serial.getSite_id(), serial.getSite_desc());
                setSerialInfo(ll_zone, tv_zona_val, serial.getZone_id(), serial.getZone_desc());
                setSerialInfo(ll_position, tv_posicao_val, serial.getLocal_id());
                setTrackingListForm(ll_tracking, ll_tracking_val, serial);
                setSerialInfo(ll_info_1, tv_info_1_val, serial.getAdd_inf1());
                setSerialInfo(ll_info_2, tv_info_2_val, serial.getAdd_inf2());
                setSerialInfo(ll_info_3, tv_info_3_val, serial.getAdd_inf3());
            }
            //
            tv_serial_lbl.setText(hmAux_Trans.get("dialog_info_serial_lbl"));
            tv_class_id_lbl.setText(hmAux_Trans.get("dialog_info_class_lbl"));
            tv_category_code_lbl.setText(hmAux_Trans.get("dialog_info_category_lbl"));
            tv_segment_lbl.setText(hmAux_Trans.get("dialog_info_segment_lbl"));
            tv_site_lbl.setText(hmAux_Trans.get("dialog_info_site_lbl"));
            tv_zone_lbl.setText(hmAux_Trans.get("dialog_info_zone_lbl"));
            tv_position_lbl.setText(hmAux_Trans.get("dialog_info_position_lbl"));
            tv_info_1_lbl.setText(hmAux_Trans.get("dialog_info_add_info_1_lbl"));
            tv_info_2_lbl.setText(hmAux_Trans.get("dialog_info_add_info_2_lbl"));
            tv_info_3_lbl.setText(hmAux_Trans.get("dialog_info_add_info_3_lbl"));
        }
        //Campos da S.O que podem ou não existir
        if (mSo_Code != null) {
            tv_so_code_desc.setText(String.valueOf(mSo_Prefix) + "." + String.valueOf(mSo_Code));
        } else {
            tv_so_code_desc.setText("");
            //
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_desc.setVisibility(View.GONE);
        }

        if (formData != null
            && formData.getTicket_prefix() != null
            && formData.getTicket_code() != null
        ) {
            String ticketDesc = formData.getTicket_prefix() + "." + formData.getTicket_code();
            //
            if(formData.getStep_code() != null) {
                ticketDesc = mPresenter.getDialogTicketInfo(formData.getTicket_prefix(), formData.getTicket_code(), formData.getStep_code());
            }
            tv_ticket_info_desc.setText(ticketDesc);
        } else {
            tv_ticket_info_desc.setText("");
            //
            tv_ticket_info_lbl.setVisibility(View.GONE);
            tv_ticket_info_desc.setVisibility(View.GONE);
        }



         if(ToolBox_Inf.isScheduleForm(formLocal)){
            tv_data_serv_lbl.setText(hmAux_Trans.get("dialog_info_data_serv_lbl"));
            tv_dt_schedule_start_lbl.setText(hmAux_Trans.get("dialog_info_dt_schedule_start_lbl"));
            tv_dt_schedule_end_lbl.setText(hmAux_Trans.get("dialog_info_dt_schedule_end_lbl"));
            //
            ll_schedule_info.setVisibility(View.VISIBLE);
            tv_data_serv_val.setText(
                ToolBox_Inf.formatSchedulePk(
                    formLocal.getSchedule_prefix(),
                    formLocal.getSchedule_code(),
                    formLocal.getSchedule_exec()
                )
            );
            //
            tv_dt_schedule_start_val.setText(
                ToolBox_Inf.formatScheduleDate(context, formLocal.getSchedule_date_start_format())
            );
            tv_dt_schedule_end_val.setText(
                ToolBox_Inf.formatScheduleDate(context, formLocal.getSchedule_date_end_format())
            );
        }else{
            ll_schedule_info.setVisibility(View.GONE);
        }

        tv_title_pdf.setText(hmAux_Trans.get("dialog_info_title_pdf_lbl"));

//      Incluir os vazios
//        int repeat = 4 - pdfs_local.size();
//
//        if (pdfs_local.size() < 4) {
//            for (int i = 0; i < repeat; i++) {
//                HMAux aux = new HMAux();
//                aux.put("blob_icon", String.valueOf(R.drawable.ic_picture_as_pdf_black_disabled_24px));
//                aux.put(GE_Custom_Form_Blob_LocalDao.BLOB_NAME, "");
//                aux.put(GE_Custom_Form_Blob_LocalDao.BLOB_URL_LOCAL, "");
//                //
//                pdfs_local.add(aux);
//            }
//        }


        if(pdfs_local.size()>0) {
            String[] from = {BLOB_ICON, GE_Custom_Form_Blob_LocalDao.BLOB_NAME};
            int[] to = {R.id.act011_dialog_form_info_cell_iv_logo, R.id.act011_dialog_form_info_cell_tv_name};
            lv_pdfs.setAdapter(
                    new SimpleAdapter(
                            Act011_Main.this,
                            pdfs_local,
                            R.layout.act011_dialog_form_info_cell,
                            from,
                            to
                    )
            );

            // Tirar Divisao
            lv_pdfs.setDivider(null);

            lv_pdfs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HMAux aux = (HMAux) parent.getItemAtPosition(position);

                    if (aux.get(GE_Custom_Form_Blob_LocalDao.BLOB_NAME).trim().length() != 0) {

                        File file = new File(Constant.CACHE_PATH + "/" + aux.get(GE_Custom_Form_Blob_LocalDao.BLOB_URL_LOCAL));

                        try {

                            ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);

                            ToolBox_Inf.copyFile(
                                    file,
                                    new File(Constant.CACHE_PDF)
                            );
                        } catch (Exception e) {
                            ToolBox_Inf.registerException(getClass().getName(), e);
                        }
                        //LUCHE - 03/10/2020
                        //Modificado metodo de abertura do PDF para que seja compativel com Android 10
                        Intent intent = ToolBox_Inf.getOpenPdfIntent(context,Constant.CACHE_PDF + "/" + aux.get(GE_Custom_Form_Blob_LocalDao.BLOB_URL_LOCAL));
                        /*
                            23/08/2019 - BARRIONUEVO
                            Trata devices sem suporte a pdf
                        */
                        try {
                            startActivity(intent);
                        }catch (ActivityNotFoundException e){
                            ToolBox_Inf.registerException(getClass().getName(), e);
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_starting_pdf_not_supported_ttl"),
                                    hmAux_Trans.get("alert_starting_pdf_not_supported_msg"),
                                    null,
                                    0
                            );
                        }
                    }
                }
            });
        }else{
            lv_pdfs.setVisibility(View.GONE);
        }

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dmW = (float) dm.widthPixels * 0.98F;
        float dmH = (float) dm.heightPixels * 0.98F;

        //infoDialog.setTitle(hmAux_Trans.get("dialog_info_title_lbl"));
        infoDialog.setContentView(view);
        infoDialog.setCancelable(true);
        infoDialog.getWindow().setLayout((int) dmW, (int) dmH);

        infoDialog.show();

    }

    private void setTrackingListForm(LinearLayout ll_tracking, LinearLayout ll_tracking_val, MD_Product_Serial serial) {
        if(serial.getTracking_list() == null || serial.getTracking_list().isEmpty()) {
            ll_tracking.setVisibility(View.GONE);
        }else{
            for (MD_Product_Serial_Tracking tracking : serial.getTracking_list()) {
                TextView tvTracking = new TextView(Act011_Main.this);
                tvTracking.setText(tracking.getTracking());
                tvTracking.setTextAppearance(Act011_Main.this, R.style.TextViewForm);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                tvTracking.setLayoutParams(layoutParams);
                ll_tracking_val.addView(tvTracking);
            }

        }
    }

    private void setDescriptions(TextView tv_descriptions, MD_Product_Serial serial) {
        String description = ( serial.getBrand_desc() == null ? "" :  serial.getBrand_desc() );
        description = description + ( serial.getModel_desc() == null ? "" :  " - " + serial.getModel_desc() );
        description = description + ( serial.getColor_desc() == null ? "" :  " - " + serial.getColor_desc() );
        if(description.isEmpty()){
            tv_descriptions.setVisibility(View.GONE);
        }else{
            tv_descriptions.setText(description);
        }
    }

    private void setSerialInfo(LinearLayout layout, TextView tvValor, String conteudo_id, String conteudo_desc) {
        if (conteudo_id==null || conteudo_id.isEmpty()){
            hideView(layout);
        } else{
            tvValor.setText(conteudo_id + " - " + conteudo_desc);
        }
    }

    private void setSerialInfo(LinearLayout layout, TextView tvValor, String conteudo) {
        if (conteudo==null || conteudo.isEmpty()){
            hideView(layout);
        } else{
            tvValor.setText(conteudo);
        }
    }

    private void hideView(LinearLayout ll_info_1) {
        ll_info_1.setVisibility(View.GONE);
    }

    private void showCustomDialogSignature() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        infoDialog = new Dialog(Act011_Main.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.act011_dialog_form_signature, null);

        TextView tv_title = (TextView) view.findViewById(R.id.act_011_dialog_tv_siganture_title);
        TextView tv_name = (TextView) view.findViewById(R.id.act_011_dialog_tv_siganture_name);
        ImageView iv_signature = (ImageView) view.findViewById(R.id.act_011_dialog_iv_siganture);

        tv_title.setText(hmAux_Trans.get("dialog_signature_title_lbl"));
        tv_name.setText(formData.getSignature_name());

        iv_signature.setImageBitmap(
                BitmapFactory.decodeFile(
                        CACHE_PATH_PHOTO + "/" + mSignature
                )
        );


        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dmW = (float) dm.widthPixels * 0.95F;
        float dmH = (float) dm.heightPixels * 0.60F;

        infoDialog.setContentView(view);
        infoDialog.setCancelable(true);
        infoDialog.getWindow().setLayout((int) dmW, (int) dmH);

        infoDialog.show();

    }

    private void showFinalizeDialogOpt(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        Act011CheckDialogBinding binding = Act011CheckDialogBinding.inflate(getLayoutInflater());
        //
        setDialogVisibilityAndLabels(binding);
        //
        setDialogAction(binding);
        //
        builder
//                .setTitle(hmAux_Trans.get("dialog_finalize_option_ttl"))
                .setView(binding.getRoot())
                .setCancelable(false)
                .setPositiveButton(
                        hmAux_Trans.get("sys_alert_btn_ok"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Seta valor var que controla se fluxo é finaliza ou finaliza mais novo.
                                finalizeNewFlow = binding.act011DialogCheckOptionRg.getCheckedRadioButtonId() == R.id.act011_dialog_finalize_option_rdo_finalize_new;
                                //
                                startCheckIN();
//                                checkGpsFlow();
                            }
                        }

                )
                .setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"),null)
        ;
        //
        builder.create().show();

    }

    private void setDialogAction(Act011CheckDialogBinding binding) {

    }

    private void setDialogVisibilityAndLabels(Act011CheckDialogBinding binding) {
        binding.act011DialogCheckTtl.setText(hmAux_Trans.get("dialog_finalize_option_ttl"));
        //
        if(isFormOs){
            setFormOsViewVisibility(binding, View.VISIBLE);
            int missingAnswersAmount = missingAnswersCounter();
            if(missingAnswersAmount == 0){
                binding.act011DialogCheckClMissingAnswers.setVisibility(View.GONE);
            }
            binding.act011DialogCheckTvMissingAnswerVal.setText(String.valueOf(missingAnswersAmount));
            binding.act011DialogCheckTvElapsedTimeVal.setText(getFormElapsedTimeFormatted(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")));
            binding.act011DialogCheckMkdateFormStart.setmCanClean(false);
            binding.act011DialogCheckMkdateFormStart.setmLabel("");
            binding.act011DialogCheckMkdateFormEnd.setmCanClean(false);
            binding.act011DialogCheckMkdateFormEnd.setmLabel("");
            binding.act011DialogCheckMkdateFormStart.setmValue(formData.getDate_start());
            binding.act011DialogCheckMkdateFormEnd.setmValue(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            binding.act011DialogFinalizeLbl.setText(hmAux_Trans.get("dialog_finalize_os_form_lbl"));
            binding.act011DialogCheckTvMissingAnswerLbl.setText(hmAux_Trans.get("dialog_finalize_os_form_missing_answer_count_lbl"));
            binding.act011DialogCheckTvElapsedTimeLbl.setText(hmAux_Trans.get("dialog_finalize_os_form_missing_answer_count_lbl"));
            binding.act011DialogCheckTvJustifyMissingAnswerLbl.setText(hmAux_Trans.get("dialog_finalize_os_form_justify_missing_answer_lbl"));
        }else{
            setFormOsViewVisibility(binding, View.GONE);
            binding.act011DialogFinalizeLbl.setText(hmAux_Trans.get("dialog_finalize_form_lbl"));
        }
        //
        binding.act011DialogCheckOptionRg.setVisibility(View.GONE);
        //
        if (allowFinalizeWithNewBtn()) {
            binding.act011DialogCheckOptionRg.setVisibility(View.VISIBLE);
            binding.act011DialogCheckOptionRdoFinalize.setText(hmAux_Trans.get("dialog_finalize_option_finalize_lbl"));
            binding.act011DialogCheckOptionRdoFinalizeNew.setText(hmAux_Trans.get("dialog_finalize_option_finalize_new_lbl"));
        }
    }

    private String getFormElapsedTimeFormatted(String endDate) {
        return ToolBox_Inf.getDateDiferenceInHHMM(endDate, formData.getDate_start());
    }

    private int missingAnswersCounter() {
        return 0;
    }

    private void setFormOsViewVisibility(Act011CheckDialogBinding binding, int visibility) {
        binding.act011DialogCheckClMissingAnswers.setVisibility(visibility);
        binding.act011DialogCheckTvElapsedTimeLbl.setVisibility(visibility);
        binding.act011DialogCheckTvElapsedTimeVal.setVisibility(visibility);
        binding.act011DialogCheckMkdateFormStart.setVisibility(visibility);
        binding.act011DialogCheckMkdateFormEnd.setVisibility(visibility);
    }

    private void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

//    @Override
//    protected void processGPS_ENABLED() {
//
//        SV_LocationTracker.msg_ok = hmAux_Trans.get("alert_location_info_aquired_succesfully");
//        SV_LocationTracker.msg_nok = hmAux_Trans.get("alert_location_info_aquired_unsuccesfully");
//
//        ToolBox_Inf.sendBCStatus(getApplicationContext(), "GPS_GO", hmAux_Trans.get("alert_location_gps_info"), "", "0");
//        ToolBox_Inf.call_Location_Tracker(context);
//    }
//
//    @Override
//    protected void processGPS_GO() {
//        super.processGPS_GO();
//        //
//        gpsCanceled = false;
//    }

    /**
     * LUCHE - 22/10/2019
     * Alterado tratativa do metodo para somente recuperar e setar os dados
     * do GPS caso o usuario NÃO TENHA cancelado a ação.
     * Em caso de cancelamento, por segurança, os valores estão sendo resetados.
     *
     * @param mLink - String concatenada com as informações:
     *                  * Tipo de Provider
     *                  * Latitude
     *                  * Longitude
     * @param mRequired - Não analisado
     */
//    @Override
//    protected void processGPS_OK(String mLink, String mRequired) {
//        progressDialog.dismiss();
//        //processa as coordenadas
//        if (!gpsCanceled) {
//            String parts[] = mLink.split("#");
//            formData.setLocation_type(parts[0]);
//            formData.setLocation_lat(parts[1]);
//            formData.setLocation_lng(parts[2]);
//            startCheckIN();
//        } else {
//            gpsCanceled = false;
//            //Luche - 28/02/2019
//            //reseta var de fluxo finaliza  + novo
//            finalizeNewFlow = false;
//            formData.setLocation_type("");
//            formData.setLocation_lat("");
//            formData.setLocation_lng("");
//        }
//    }


//    @Override
//    protected void processGPS_STOP() {
//        ToolBox_Inf.stop_Location_Tracker(context);
//
//        gpsCanceled = true;
//        //Luche - 28/02/2019
//        //reseta var de finaliza + novo
//        finalizeNewFlow = false;
//
//        progressDialog.dismiss();
//    }
    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        progressDialog.dismiss();
        //
        if( wsSoProcess.equalsIgnoreCase(WS_Serial_Save.class.getSimpleName())
           || wsSoProcess.equalsIgnoreCase(WS_Save.class.getSimpleName())
        ){
            ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_data_not_sent_ttl"),
                hmAux_Trans.get("alert_resend_data_by_menu_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flowControl();
                    }
                },
                0
            );
        }else {
            formData.setLocation_type("");
            formData.setLocation_lat("");
            formData.setLocation_lng("");
        }
    }

    /**
     * LUCHE
     * @param mLink
     * @param mRequired
     */
    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        ToolBox.alertMSG(
            context,
            hmAux_Trans.get("alert_data_not_sent_ttl"),
            hmAux_Trans.get("alert_resend_data_by_menu_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flowControl();
                }
            },
            0
        );
    }

    private void executeSerialSave() {
        setWsSoProcess(WS_Serial_Save.class.getSimpleName());
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (wsSoProcess.equalsIgnoreCase(WS_Serial_Save.class.getSimpleName())) {
            setWsSoProcess("");
            //
            if (!hmAux.isEmpty() && hmAux.size() > 0) {
                for (Map.Entry<String, String> item : hmAux.entrySet()) {
                    HMAux aux = new HMAux();
                    String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                    String status = item.getValue();
                    String productInfo = getProductInfo(Long.parseLong(pk[0]));
                    //
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", "" + productInfo + " - " + pk[1]);
                    mHmAux.put("type", "SERIAL");
                    mHmAux.put("status", status);
                    mHmAux.put("final_status", productInfo + " - " + pk[1] + " / " + status);
                    //
                    if (!mHmAux.get("status").equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_OK)) {
                        wsResults.add(mHmAux);
                    }
                }
            }

            executeSaveProcess();
        }
    }

    private String getProductInfo(Long product_code) {
        MD_ProductDao mdProductDao = new MD_ProductDao(context);
        MD_Product mdProduct = mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
        //
        if (mdProduct != null) {
            return mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc();
        } else {
            return "";
        }
    }

    private void executeSaveProcess() {
        setWsSoProcess(WS_Save.class.getSimpleName());
        //
        Intent mIntent = new Intent(context, WBR_Save.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
        bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device
        bundle.putString(Act005_Main.WS_PROCESS_SO_STATUS, "SEND");

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_preparing_to_send_data"), "", "0");
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);

        if (wsSoProcess.equalsIgnoreCase(WS_Save.class.getSimpleName())) {
            setWsSoProcess("");
            mPresenter.processWS_SaveReturn(mLink);
        }
    }

    @Override
    public void afterSaveFlow() {
        if (wsResults.size() > 0) {
            showResults(wsResults);
        } else {
            progressDialog.dismiss();
            if(mPresenter.hasGpsPendecy(
                    formLocal.getCustomer_code(),
                    formLocal.getCustom_form_type(),
                    formLocal.getCustom_form_code(),
                    formLocal.getCustom_form_version(),
                    formLocal.getCustom_form_data()
            )){
                ToolBox.alertMSG(
                        Act011_Main.this,
                        hmAux_Trans.get("dialog_has_gps_pendency_ttl"),
                        hmAux_Trans.get("dialog_has_gps_pendency_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flowControl();
                            }
                        },
                        0
                );
            }else {
                flowControl();
            }
        }
    }

    @Override
    public void addWsResults(ArrayList<HMAux> formDataErroAux) {
        wsResults.addAll(
            formDataErroAux
        );
        if (wsResults.size() > 0) {
                showResults(wsResults);
        } else {
            progressDialog.dismiss();
            if(mPresenter.hasGpsPendecy(
                    formLocal.getCustomer_code(),
                    formLocal.getCustom_form_type(),
                    formLocal.getCustom_form_code(),
                    formLocal.getCustom_form_version(),
                    formLocal.getCustom_form_data()
            )){
                ToolBox.alertMSG(
                        Act011_Main.this,
                        hmAux_Trans.get("dialog_has_gps_pendency_ttl"),
                        hmAux_Trans.get("dialog_has_gps_pendency_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flowControl();
                            }
                        },
                        0
                );
            }else {
                flowControl();
            }
        }
    }

    public void showResults(List<HMAux> res) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        List<HMAux> gAdapterRes = new ArrayList<>();
        for (HMAux item : res) {
            HMAux hmAux = new HMAux();
            //
            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.get("label"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.get("status"));
            //
            switch (item.get("type")) {
                case "SERIAL":
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_serial_data"));
                    break;
                case ConstantBaseApp.SYS_STATUS_SCHEDULE:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_schedule"));
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.get("final_status")+"\n"+item.get("status"));
                    break;
                case TSave_Rec.Error_Process.ERROR_TYPE_TICKET:
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_ticket"));
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.get("final_status")+"\n"+item.get("status"));
                    break;
            }
            //
            gAdapterRes.add(hmAux);
        }
        //
        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        gAdapterRes,
                        Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                        hmAux_Trans
                )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);

        final androidx.appcompat.app.AlertDialog show = builder.show();

        /**
         * Ini Action
         */
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    //
                    flowControl();
                }
            }
        });
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

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

    public MD_Product_Serial getSerialInfo(){
        return mPresenter.getSerialInfo(
                ToolBox_Con.getPreference_Customer_Code(context),
                Integer.parseInt(product_code),
                serial_id,
                formLocal
        );
    }

    public String getProduct_icon(){
        //return mPresenter.getProductIcon(Long.parseLong(product_code));
        return formLocal != null ? formLocal.getCustom_product_icon_name(): "";
    }

    private void getLocation() {
        requestPermissions(
                Act011_Main.this,
                NamoaPermissionRequest.MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                new NamoaPermissionRequest() {
                    @Override
                    public void accessGranted() {

                        ToolBox_Inf.call_Location_Tracker_On_Background(context, SV_LocationTracker.LOCATION_NFORM_ON);

                    }

                    @Override
                    public void accessDenied(final String[] permissions) {
                        showPermissionRationaleDialog(
                                Act011_Main.this,
                                com.namoa_digital.namoa_library.R.drawable.ic_alert_n,
                                hmAux_Trans.get("alert_gps_denied_permission_ttl"),
                                hmAux_Trans.get("alert_gps_denied_permission_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        callRequestPermission(MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,permissions);
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callAct083();
                                    }
                                }
                        );
                    }

                    @Override
                    public void requestPermissionRationale(final String[] permissions) {

                        showPermissionRationaleDialog(
                                Act011_Main.this,
                                com.namoa_digital.namoa_library.R.drawable.ic_alert_n,
                                hmAux_Trans.get("alert_gps_rationale_permission_ttl"),
                                hmAux_Trans.get("alert_gps_rationale_permission_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        callRequestPermission(MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,permissions);
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callAct083();
                                    }
                                }
                        );
                    }

                    @Override
                    public void accessDeniedNeverAskAgain(String[] permissions) {

                        showPermissionNeverAskAgainDialog(
                                R.drawable.ic_location_on_24,
                                hmAux_Trans.get("alert_gps_never_ask_again_permission_ttl"),
                                hmAux_Trans.get("alert_gps_never_ask_again_permission_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        callAct083();
                                    }
                                }
                        );
                    }

                    @Override
                    public void informAppDetailSettingsReturn() {
                        callRequestPermission(MULTIIPLE_PERMISSION_REQUEST_WITHOUT_RATIONALE,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
                    }
                }
        );
    }

}
