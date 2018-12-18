package com.namoadigital.prj001.ui.act011;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import com.namoa_digital.namoa_library.view.SignaTure_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
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
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_007;
import com.namoadigital.prj001.sql.GE_File_Sql_003;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act011_003;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.namoa_digital.namoa_library.util.ConstantBase.CACHE_PATH_PHOTO;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main extends Base_Activity implements Act011_Main_View {
    private Act011_Main_Presenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ViewPager pager;

    private FragmentManager fm;

    private Act011_FF_Options act011_ff_options;

    private List<HMAux> tabsAndFields;
    private ArrayList<Fragment> screens;
    private ArrayList<CustomFF> customFFs;
    private ArrayList<GE_File> geFiles;

    private ArrayList<HMAux> pdfs_local;

    private String sDate;

    private HMAux resTabs;

    private Toolbar toolbar;

    private String dtCustomer_Format;

    private Bundle bundle;

    private HMAux hmPages = new HMAux();

    private String product_code;
    private String product_desc;
    private String product_id;
    private String serial_id;
    private String type;
    private String type_desc;
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

    private String wsSoProcess;
    private ArrayList<HMAux> wsResults = new ArrayList<>();
    //Implments PhotoInterface
    private CustomFF.ICustomFFPhoto onPhotoClick;


    public void setWsSoProcess(String wsSoProcess) {
        this.wsSoProcess = wsSoProcess;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act011_main);

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

        transList.add("alert_location_info_title");
        transList.add("alert_location_info_required");
        transList.add("alert_location_gps_info");
        transList.add("alert_location_info_aquired_succesfully");
        transList.add("alert_location_info_aquired_unsuccesfully");
        transList.add("alert_schedule_comment_ttl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

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

                if (index_old == -1 || index_old == 0) {
                    resTabs = returnValidCheckTabs(String.valueOf(index_old));

                    act011_ff_options.tabsS(resTabs);
                } else {
                    resTabs = returnValidCheckTabs(String.valueOf(index_old));

                    // Hugo
                    Set keys = resTabs.keySet();

                    hmPages.clear();

                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        String value = (String) resTabs.get(key);

                        hmPages.put(key, value);
                    }

                    act011_ff_options.tabsS(hmPages);
                }
                //
                //resTabs = returnValidCheckTabs(String.valueOf(index_old));
                //act011_ff_options.tabsS(resTabs);
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        act011_ff_options = (Act011_FF_Options)
                fm.findFragmentById(R.id.act011_ff_options);

        act011_ff_options.setOnTabSelectedListener(new Act011_FF_Options.ICustom_Form_FF_Options() {
            @Override
            public void tabSelected(int idtab, String link) {
                tabSelectedAction(idtab);
                //
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        act011_ff_options.setOnSaveCheckListener(new Act011_FF_Options.ICustom_Form_FF_Options_ll() {

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
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        deleteFormLocal();
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
                /*mDrawerLayout.closeDrawer(GravityCompat.START);

                formData.setLocation_type("");
                formData.setLocation_lat("");
                formData.setLocation_lng("");

                returnValidCheck(String.valueOf(-1));

                for (GE_Custom_Form_Data_Field df : formData.getDataFields()) {
                    df.setValue(returnFieldValue(df.getCustom_form_seq(), 0));
                    df.setValue_extra(returnFieldValue(df.getCustom_form_seq(), 1));
                }

                mPresenter.saveData(formData, true);

                bNew = false;

                mDrawerLayout.closeDrawer(GravityCompat.START);*/
                //Implments PhotoInterface
                saveV2(true);
            }

            @Override
            public void check() {
                checkAction();
            }

            @Override
            public void autograph() {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                showCustomDialogSignature();
            }

            @Override
            public void auto() {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                HMAux hmP = new HMAux();

                int quantidade = 0;

                for (CustomFF customFF : customFFs) {
                    if (customFF.activateAutoReplay() != 0) {
                        quantidade += 1;

                        hmP.put(String.valueOf(customFF.getmPage()), String.valueOf(customFF.getmPage()));
                    }
                }
                //
                Set keysAuto = hmP.keySet();

                for (Iterator iAuto = keysAuto.iterator(); iAuto.hasNext(); ) {

                    String keyAutoHM = (String) iAuto.next();

                    returnValidCheck(keyAutoHM);

                    resTabs = returnValidCheckTabs(keyAutoHM);
                    //
                    Set keys = resTabs.keySet();

                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        String value = (String) resTabs.get(key);

                        hmPages.put(key, value);
                    }
                }
                //
                act011_ff_options.tabsS(hmPages);


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
        });

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
                hmAux_Trans
        );

        recoverGetIntents();
        //
        onPhotoClick = new CustomFF.ICustomFFPhoto() {
            @Override
            public void OnPhotoClick(String s) {
                saveV2(false);
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
                type_desc,
                form_desc,
                serial_id,
                mSo_Prefix,
                mSo_Code,
                mSite_Code,
                mOperation_Code
        );

    }
    //Implments PhotoInterface
    private void saveV2(boolean fieldsValidation) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        formData.setLocation_type("");
        formData.setLocation_lat("");
        formData.setLocation_lng("");
        //
        if(fieldsValidation) {
            returnValidCheck(String.valueOf(-1));
        }

        for (GE_Custom_Form_Data_Field df : formData.getDataFields()) {
            df.setValue(returnFieldValue(df.getCustom_form_seq(), 0));
            df.setValue_extra(returnFieldValue(df.getCustom_form_seq(), 1));
        }

        mPresenter.saveData(formData, fieldsValidation);

        bNew = false;

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void tabSelectedAction(int idtab) {
        ignoreUpdate = true;

        index_old = index;
        index = idtab;

        resTabs = returnValidCheckTabs(String.valueOf(index_old));

        act011_ff_options.tabsS(resTabs);

        returnValidCheck(String.valueOf(index_old));

        pager.setCurrentItem(idtab - 1);
    }

    private void checkAction() {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        /**
         * Atualizar tabs caso o usuário tente fazer um check().
         */
        resTabs = returnValidCheckTabs("-1");
        returnValidCheck("-1");
        act011_ff_options.tabsS(resTabs);
        //Fim

        formData.setLocation_type("");
        formData.setLocation_lat("");
        formData.setLocation_lng("");

        int sum = returnValidCheck(String.valueOf(-1));

        if (sum == 0) {

            // Mudar Aqui
            ToolBox.alertMSG(
                    Act011_Main.this,
                    hmAux_Trans.get("alert_question_finalize_title"), //"Finalizar Formalário"
                    hmAux_Trans.get("alert_question_finalize_msg"), //"Deseja Finalizar o Formulário?"
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (formLocal.getRequire_location() == 1) {
                                enableProgressDialog(
                                        hmAux_Trans.get("alert_location_info_title"),
                                        hmAux_Trans.get("alert_location_info_required"),
                                        hmAux_Trans.get("sys_alert_btn_cancel"),
                                        hmAux_Trans.get("sys_alert_btn_ok")
                                );
                                //
                                ToolBox_Inf.sendBCStatus(getApplicationContext(), "GPS_ENABLED", hmAux_Trans.get("alert_location_info_required"), "", "0");

                            } else {
                                startCheckIN();
                            }
                        }
                    },
                    1
            );

//                    if (formLocal.getRequire_location() == 1) {
//                        enableProgressDialog(
//                                hmAux_Trans.get("alert_location_info_title"),
//                                hmAux_Trans.get("alert_location_info_required"),
//                                hmAux_Trans.get("sys_alert_btn_cancel"),
//                                hmAux_Trans.get("sys_alert_btn_ok")
//                        );
//                        //
//                        ToolBox_Inf.sendBCStatus(getApplicationContext(), "GPS_ENABLED", hmAux_Trans.get("alert_location_info_required"), "", "0");
//
//                    } else {
//                        startCheckIN();
//                    }

        } else {

            ToolBox.alertMSG(
                    Act011_Main.this,
                    hmAux_Trans.get("alert_error_on_finalize_title"),
                    hmAux_Trans.get("alert_error_on_finalize_msg"),
                    null,
                    0
            );
        }
    }

    private void nservCall() {
        Bundle bundle = new Bundle();

        bundle.putString(SM_SODao.SO_PREFIX, String.valueOf(mSo_Prefix));
        bundle.putString(SM_SODao.SO_CODE, String.valueOf(mSo_Code));
        //
        callAct027(context, bundle);
    }

    private void startCheckIN() {
        for (GE_Custom_Form_Data_Field df : formData.getDataFields()) {
            df.setValue(returnFieldValue(df.getCustom_form_seq(), 0));
            df.setValue_extra(returnFieldValue(df.getCustom_form_seq(), 1));
        }

        sDate = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z");

        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        HMAux aux = geFileDao.getByStringHM(
                new GE_File_Sql_003().toSqlQuery()
        );

        int index = Integer.parseInt(aux.get("next_code"));

        geFiles.clear();

        for (int i = 0; i < customFFs.size(); i++) {
            String sFile_v = customFFs.get(i).getmValue();
            String sFile_e_1 = customFFs.get(i).getmDots_photo1();
            String sFile_e_2 = customFFs.get(i).getmDots_photo2();
            String sFile_e_3 = customFFs.get(i).getmDots_photo3();
            String sFile_e_4 = customFFs.get(i).getmDots_photo4();

            if (sFile_v.endsWith(".png") || sFile_v.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_v);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_v.replace(".png", "").replace(".jpg", ""));
                    geFile.setFile_path(sFile_v);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_1.endsWith(".png") || sFile_e_1.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_1);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_1.replace(".png", "").replace(".jpg", ""));
                    geFile.setFile_path(sFile_e_1);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_2.endsWith(".png") || sFile_e_2.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_2);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_2.replace(".png", "").replace(".jpg", ""));
                    geFile.setFile_path(sFile_e_2);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_3.endsWith(".png") || sFile_e_3.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_3);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_3.replace(".png", "").replace(".jpg", ""));
                    geFile.setFile_path(sFile_e_3);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
            //
            if (sFile_e_4.endsWith(".png") || sFile_e_4.endsWith(".jpg")) {
                File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + sFile_e_4);
                if (sFile.exists()) {
                    GE_File geFile = new GE_File();
                    geFile.setFile_code(sFile_e_4.replace(".png", "").replace(".jpg", ""));
                    geFile.setFile_path(sFile_e_4);
                    geFile.setFile_status("OPENED");
                    geFile.setFile_date(sDate);

                    geFiles.add(geFile);
                }
            }
        }

        formData.setSignature(mSignature);

        mPresenter.checkSignature(formData, signature, 0, geFiles, require_serial_done, require_serial_done_ok);
    }

    private void deleteFormLocal() {

        GE_Custom_Form_LocalDao formLocalDao =
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

        GE_Custom_Form_Field_LocalDao formFieldLocalDao =
                new GE_Custom_Form_Field_LocalDao(
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
        //
        GE_Custom_Form_Data_FieldDao formDataFieldDao =
                new GE_Custom_Form_Data_FieldDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

        formLocalDao.remove(
                new GE_Custom_Form_Local_Sql_007(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data())
                ).toSqlQuery()
        );
        //
        formFieldLocalDao.remove(
                new GE_Custom_Form_Field_Local_Sql_004(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data())
                ).toSqlQuery()
        );
        //
        //
        formDataDao.remove(
                new GE_Custom_Form_Data_Sql_002(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data())
                ).toSqlQuery()
        );
        //
        //
        formDataFieldDao.remove(
                new GE_Custom_Form_Data_Field_Sql_002(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data())
                ).toSqlQuery()
        );

        callAct005(context);

    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = bundle.getString(MD_ProductDao.PRODUCT_CODE, "");
            //product_code = bundle.getString(Constant.ACT007_PRODUCT_CODE, "");
            product_desc = bundle.getString(MD_ProductDao.PRODUCT_DESC, "");
            //product_desc = bundle.getString(Constant.ACT008_PRODUCT_DESC, "");
            product_id = bundle.getString(MD_ProductDao.PRODUCT_ID, "Sem Product ID");
            //product_id = bundle.getString(Constant.ACT008_PRODUCT_ID, "Sem Product ID");
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            //serial_id = bundle.getString(Constant.ACT008_SERIAL_ID, "");
            type = bundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, "");
            //type = bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE, "");
            type_desc = bundle.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, "");
            //type_desc = bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC, "");
            form = bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_CODE, "");
            //form = bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE, "");
            form_version = bundle.getString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, "");
            //form_version = bundle.getString(Constant.ACT010_CUSTOM_FORM_VERSION, "");
            // VERIFICAR
            form_desc = bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, "");
            form_data = bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, "0");
            //form_data = bundle.getString(Constant.ACT013_CUSTOM_FORM_DATA, "0");
            //
            mSo_Prefix = bundle.getString(SM_SODao.SO_PREFIX, null) == null ? null : Integer.parseInt(bundle.getString(SM_SODao.SO_PREFIX, null));
            mSo_Code = bundle.getString(SM_SODao.SO_CODE, null) == null ? null : Integer.parseInt(bundle.getString(SM_SODao.SO_CODE, null));
            mSite_Code = bundle.getString(SM_SODao.SITE_CODE, null);
            mOperation_Code = bundle.getString(SM_SODao.OPERATION_CODE, null) == null ? null : Integer.parseInt(bundle.getString(SM_SODao.OPERATION_CODE, null));

        } else {
            mSo_Prefix = null;
            mSo_Code = null;
            //
            mSite_Code = null;
            mOperation_Code = null;
        }
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
        setTitleLanguage("          (" + String.valueOf(index) + "/" + String.valueOf(pager.getAdapter().getCount()) + ")");
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
    public void loadFragment_CF_Fields(List<HMAux> cf_fields, boolean bNew, GE_Custom_Form_Local formLocal, GE_Custom_Form_Data formData, String prefix, List<HMAux> pdfs, int indexF, int signature, int require_serial_done) {

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

        if (!formData.getSerial_id().equalsIgnoreCase(serial_id) && !serial_id.isEmpty()) {
            formData.setSerial_id(serial_id);
        } else {
            serial_id = formData.getSerial_id();
        }

        includeField = formData.getDataFields().size() == 0 ? true : false;

        int pages = 0;

        if (cf_fields != null && cf_fields.size() > 0) {
            pages = Integer.parseInt(cf_fields.get(cf_fields.size() - 1).get("page"));
            //
            customFFs = new ArrayList<>();
            screens = new ArrayList<>();
            //
            for (HMAux cf : cf_fields) {

                if (includeField && !cf.get("custom_form_data_type").equalsIgnoreCase("label") && !cf.get("custom_form_data_type").equalsIgnoreCase("tab")) {
                    GE_Custom_Form_Data_Field form_data_field = new GE_Custom_Form_Data_Field();
                    form_data_field.setCustomer_code(formData.getCustomer_code());
                    form_data_field.setCustom_form_type(formData.getCustom_form_type());
                    form_data_field.setCustom_form_code(formData.getCustom_form_code());
                    form_data_field.setCustom_form_version(formData.getCustom_form_version());
                    form_data_field.setCustom_form_data(formData.getCustom_form_data());
                    form_data_field.setCustom_form_data_serv(formData.getCustom_form_data_serv());
                    form_data_field.setCustom_form_seq(Integer.parseInt(cf.get("custom_form_seq")));
                    //
                    formData.getDataFields().add(form_data_field);
                }

                switch (cf.get("custom_form_data_type").toLowerCase()) {
                    case "char":
                        customFFs.add(cfg_Char(cf));
                        break;
                    case "label":
                        customFFs.add(cfg_Label(cf));
                        break;
                    case "combobox":
                        customFFs.add(cfg_ComboBox(cf));
                        break;
                    case "number":
                        customFFs.add(cfg_Number(cf));
                        break;
                    case "date":
                        customFFs.add(cfg_Date(cf));
                        break;
                    case "hour":
                        customFFs.add(cfg_Hour(cf));
                        break;
                    case "checkbox":
                        customFFs.add(cfg_CheckBox(cf));
                        break;
                    case "ratingimage":
                        customFFs.add(cfg_RatingImage(cf));
                        break;
                    case "ratingbar":
                        customFFs.add(cfg_RatingBar(cf));
                        break;
                    case "picture":
                        customFFs.add(cfg_Picture(cf));
                        break;
                    case "photo":
                        customFFs.add(cfg_Photo(cf));
                        break;
                    default:
                        break;
                }

            }

            for (CustomFF customFF : customFFs) {
                controls_dyn.add(customFF);
                //Implments PhotoInterface
                customFF.setOnPhotoClickListener(onPhotoClick);
            }

            for (int i = 1; i <= pages; i++) {
                Act011_FF custom_form_ff = new Act011_FF();
                //
                if (i == 1) {
                    custom_form_ff.setComments(formLocal.getSchedule_comments() != null ? formLocal.getSchedule_comments() : "");
                } else {
                    custom_form_ff.setComments("");
                }
                //
                custom_form_ff.setCustomFFs(customFFs, i);
                custom_form_ff.setHmAux_Trans(hmAux_Trans);
                custom_form_ff.setOnDrawerCheckListener(new Act011_FF.ICustom_Form_FF_ll() {
                    @Override
                    public void openDrawer() {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }

                    @Override
                    public void check() {
                        checkAction();
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
                });
                //
                custom_form_ff.setFormStatus(formData.getCustom_form_status());
                //
                screens.add(custom_form_ff);
            }

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
                    act011_ff_options.setFOpc(position + 1);
                    //
                    index_old = index;
                    index = position + 1;
                    //
                    setTitleLanguage("          (" + String.valueOf(index) + "/" + String.valueOf(pager.getAdapter().getCount()) + ")");
                    //
                    if (ignoreUpdate) {
                        ignoreUpdate = false;
                    } else {
                        //
                        returnValidCheck(String.valueOf(index_old));
                        //
                        resTabs = returnValidCheckTabs(String.valueOf(index_old));
                        //
                        Set keys = resTabs.keySet();

                        for (Iterator i = keys.iterator(); i.hasNext(); ) {
                            String key = (String) i.next();
                            String value = (String) resTabs.get(key);

                            hmPages.put(key, value);
                        }
                        //
                        act011_ff_options.tabsS(hmPages);
                    }

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            resTabs = returnValidCheckTabs(String.valueOf(index_old));

            act011_ff_options.loadCF_Fields(cf_fields, resTabs, pdfs, mSignature, form_desc);
            act011_ff_options.enableScheduled(formData.getCustom_form_data_serv());

            if (mSo_Prefix == null || mSo_Code == null) {
                act011_ff_options.enableTab(formData.getCustom_form_status(), 0);
            } else {
                act011_ff_options.enableTab(formData.getCustom_form_status(), 1);
            }

            //act011_ff_options.enableTab(formData.getCustom_form_status());
            act011_ff_options.translaTab(hmAux_Trans);

            returnValidCheck(String.valueOf(index_old));
        }
    }

    private CustomFF cfg_Label(HMAux cf) {
        LabelFF labelFF = new LabelFF(Act011_Main.this);

        labelFF.setId(View.generateViewId());
        labelFF.setmLabel(cf.get("custom_form_field_desc"));
        labelFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        labelFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        labelFF.setmPage(Integer.parseInt(cf.get("page")));
        labelFF.setmType(cf.get("custom_form_data_type"));

        return labelFF;
    }

    private CustomFF cfg_Char(HMAux cf) {
        MKEditTextNMFF mkEditTextNMFF = new MKEditTextNMFF(Act011_Main.this);

        mkEditTextNMFF.setmDots_txt_app(cf.get("comment"));

        mkEditTextNMFF.setId(View.generateViewId());
        mkEditTextNMFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        mkEditTextNMFF.setmLabel(cf.get("custom_form_field_desc"));
        mkEditTextNMFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        mkEditTextNMFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        mkEditTextNMFF.setmPage(Integer.parseInt(cf.get("page")));
        mkEditTextNMFF.setmType(cf.get("custom_form_data_type"));

//        Remover a configuraçao automatica do campo customizado de edicao mesmo que o equipamento suporte NFC.
//        if (hasNFCSupport) {
//            mkEditTextNMFF.setmNFC(true);
//        } else {
//            mkEditTextNMFF.setmNFC(false);
//        }

        if (cf.get("custom_form_data_type").equalsIgnoreCase("DATE")) {
            mkEditTextNMFF.setmBARCODE(false);
            mkEditTextNMFF.setmNFC(false);
            mkEditTextNMFF.setmOCR(false);
            //mkEditTextNMFF.setmMask("{\"MASK\":[{\"TYPE\":\"DATE\",\"VALUE\":\"$$/$$/$$$$\"}]}");
        } else if (cf.get("custom_form_data_type").equalsIgnoreCase("HOUR")) {
            mkEditTextNMFF.setmBARCODE(false);
            mkEditTextNMFF.setmNFC(false);
            mkEditTextNMFF.setmOCR(false);
            //mkEditTextNMFF.setmMask("{\"MASK\":[{\"TYPE\":\"HOUR\",\"VALUE\":\"$$:$$\"}]}");
        } else {
            mkEditTextNMFF.setmMask(cf.get("custom_form_data_mask"));
            //mkEditTextNMFF.setmMask("{\"MASK\":[{\"TYPE\":\"CPF\",\"VALUE\":\"$$$.$$$.$$$-$$\"}]}");
        }

        mkEditTextNMFF.setmOption(cf.get("custom_form_data_content"));
        mkEditTextNMFF.setmMaxSize(Integer.parseInt(cf.get("custom_form_data_size")));

        if (mkEditTextNMFF.getmMaxSize() == 0 && cf.get("custom_form_data_type").equalsIgnoreCase("NUMBER")) {

            String[] opcs = null;

            try {
                JSONObject jsonObject = new JSONObject(cf.get("custom_form_data_content"));
                JSONArray jsonArray = jsonObject.getJSONArray("CONTENT");
                //
                opcs = new String[jsonArray.length()];
                //
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    opcs[i] = jo.getString("DECIMAL");
                }
                //
                mkEditTextNMFF.setmDecimal(Integer.parseInt(opcs[0]));
            } catch (JSONException e) {
                mkEditTextNMFF.setmDecimal(0);
            }
        }

        mkEditTextNMFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        mkEditTextNMFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        mkEditTextNMFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        mkEditTextNMFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
                ) {
            mkEditTextNMFF.setmEnabled(false);
        } else {
            mkEditTextNMFF.setmEnabled(true);
        }

        return mkEditTextNMFF;
    }

    private CustomFF cfg_ComboBox(HMAux cf) {
        ComboBoxFF comboBoxFF = new ComboBoxFF(Act011_Main.this);

        comboBoxFF.setmDots_txt_app(cf.get("comment"));

        comboBoxFF.setId(View.generateViewId());
        comboBoxFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        comboBoxFF.setmLabel(cf.get("custom_form_field_desc"));
        comboBoxFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        comboBoxFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        comboBoxFF.setmPage(Integer.parseInt(cf.get("page")));
        comboBoxFF.setmType(cf.get("custom_form_data_type"));

        comboBoxFF.setmOption(cf.get("custom_form_data_content"));

        comboBoxFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        comboBoxFF.setmPre(prefix);


        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        comboBoxFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        comboBoxFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
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

        checkBoxFF.setmDots_txt_app(cf.get("comment"));

        checkBoxFF.setId(View.generateViewId());
        checkBoxFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        checkBoxFF.setmLabel(cf.get("custom_form_field_desc"));
        checkBoxFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        checkBoxFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        checkBoxFF.setmPage(Integer.parseInt(cf.get("page")));
        checkBoxFF.setmType(cf.get("custom_form_data_type"));

        checkBoxFF.setmOption(cf.get("custom_form_data_content"));

        checkBoxFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        checkBoxFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        checkBoxFF.setmAutomatic(cf.get("automatic"));

        checkBoxFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        checkBoxFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
                ) {
            checkBoxFF.setmEnabled(false);
        } else {
            checkBoxFF.setmEnabled(true);
        }

        return checkBoxFF;
    }

    private CustomFF cfg_RatingImage(HMAux cf) {
        RatingImageFF ratingImageFF = new RatingImageFF(Act011_Main.this);

        ratingImageFF.setmDots_txt_app(cf.get("comment"));

        ratingImageFF.setId(View.generateViewId());
        ratingImageFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        ratingImageFF.setmLabel(cf.get("custom_form_field_desc"));
        ratingImageFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        ratingImageFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        ratingImageFF.setmPage(Integer.parseInt(cf.get("page")));
        ratingImageFF.setmType(cf.get("custom_form_data_type"));

        ratingImageFF.setmOption(cf.get("custom_form_data_content"));

        ratingImageFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        ratingImageFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        ratingImageFF.setmAutomatic(cf.get("automatic"));

        ratingImageFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        ratingImageFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
                ) {
            ratingImageFF.setmEnabled(false);
        } else {
            ratingImageFF.setmEnabled(true);
        }

        return ratingImageFF;
    }

    private CustomFF cfg_RatingBar(HMAux cf) {
        RatingBarFF ratingBarFF = new RatingBarFF(Act011_Main.this);

        ratingBarFF.setmDots_txt_app(cf.get("comment"));

        ratingBarFF.setId(View.generateViewId());
        ratingBarFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        ratingBarFF.setmLabel(cf.get("custom_form_field_desc"));
        ratingBarFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        ratingBarFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        ratingBarFF.setmPage(Integer.parseInt(cf.get("page")));
        ratingBarFF.setmType(cf.get("custom_form_data_type"));

        ratingBarFF.setmOption(cf.get("custom_form_data_content"));

        ratingBarFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        ratingBarFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        ratingBarFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        ratingBarFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
                ) {
            ratingBarFF.setmEnabled(false);
        } else {
            ratingBarFF.setmEnabled(true);
        }

        return ratingBarFF;
    }

    private CustomFF cfg_Picture(HMAux cf) {
        PictureFF pictureFF = new PictureFF(Act011_Main.this);

        pictureFF.setmDots_txt_app(cf.get("comment"));

        pictureFF.setId(View.generateViewId());
        pictureFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        pictureFF.setmLabel(cf.get("custom_form_field_desc"));
        pictureFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        pictureFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        pictureFF.setmPage(Integer.parseInt(cf.get("page")));
        pictureFF.setmType(cf.get("custom_form_data_type"));

        pictureFF.setmOption(cf.get("custom_form_data_content"));
        pictureFF.setmFName(cf.get("custom_form_local_link"));

        pictureFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        pictureFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        pictureFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        pictureFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
                ) {
            pictureFF.setmEnabled(false);
        } else {
            pictureFF.setmEnabled(true);
        }

        return pictureFF;
    }

    private CustomFF cfg_Photo(HMAux cf) {
        PhotoFF photoFF = new PhotoFF(Act011_Main.this);

        photoFF.setmDots_txt_app(cf.get("comment"));

        photoFF.setId(View.generateViewId());
        photoFF.setmRequire_photo_on_nc(cf.get("require_photo_on_nc").equals("1") ? true : false);
        photoFF.setmLabel(cf.get("custom_form_field_desc"));
        photoFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        photoFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        photoFF.setmPage(Integer.parseInt(cf.get("page")));
        photoFF.setmType(cf.get("custom_form_data_type"));

        photoFF.setmOption(cf.get("custom_form_data_content"));

        photoFF.setmRequired(cf.get("required").equalsIgnoreCase("1") ? true : false);
        photoFF.setmPre(prefix);

        HMAux itemDB = retornDBValue(Integer.parseInt(cf.get("custom_form_seq")));

        photoFF.setmValue(itemDB.get(HMAux.TEXTO_01));
        photoFF.setmValue_Extra(itemDB.get(HMAux.TEXTO_02));

        if (formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                formData.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)
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

    private String returnFieldValue(int seq, int type) {
        String result = "";
        //
        for (int i = 0; i < customFFs.size(); i++) {
            if (customFFs.get(i).getmSequence() == seq) {
                if (type == 0) {
                    result = customFFs.get(i).getmValue();
                } else {
                    result = customFFs.get(i).getmValue_Extra();
                }
                //
                break;
            }
        }
        //
        return result;
    }


    private int returnValidCheck(String sPage) {

        int numberOfErrors = 0;
        int ipage = Integer.parseInt(sPage);
        //
        for (int i = 0; i < customFFs.size(); i++) {

            if (ipage == -1) {
                if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                    numberOfErrors += 1;
                }

                customFFs.get(i).setValidationBackGroundDots();
            } else {
                if (customFFs.get(i).getmPage() == ipage) {
                    if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                        numberOfErrors += 1;
                    }

                    customFFs.get(i).setValidationBackGroundDots();
                } else {
                }
            }
        }

        return numberOfErrors;
    }

    private int returnValidCheck2(String sPage) {

        int numberOfErrors = 0;
        int ipage = Integer.parseInt(sPage);
        //
        for (int i = 0; i < customFFs.size(); i++) {

            if (ipage == -1) {
                if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                    numberOfErrors += 1;
                }

                customFFs.get(i).setValidationBackGroundDots();
            } else {
                if (customFFs.get(i).getmPage() == ipage) {
                    if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                        numberOfErrors += 1;
                    }

                    customFFs.get(i).setValidationBackGroundDots();
                } else {
                }
            }
        }

        return numberOfErrors;
    }

    private HMAux returnValidCheckTabs(String sPage) {

        ArrayList<HMAux> itens = new ArrayList<>();

        HMAux item = new HMAux();

        HMAux ipages = new HMAux();
        int ipage = Integer.parseInt(sPage);
        //
        for (int i = 0; i < customFFs.size(); i++) {
            HMAux aux = new HMAux();

            if (ipage == -1) {

                if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                    aux.put("page", String.valueOf(customFFs.get(i).getmPage()));
                    aux.put("value", "ERROR");
                } else {
                    aux.put("page", String.valueOf(customFFs.get(i).getmPage()));
                    aux.put("value", "OK");
                }

                ipages.put(String.valueOf(customFFs.get(i).getmPage()), "");

                itens.add(aux);


            } else {
                if (ipage == customFFs.get(i).getmPage()) {
                    if (!customFFs.get(i).isValid() || !customFFs.get(i).isValidDots()) {
                        aux.put("page", String.valueOf(customFFs.get(i).getmPage()));
                        aux.put("value", "ERROR");
                    } else {
                        aux.put("page", String.valueOf(customFFs.get(i).getmPage()));
                        aux.put("value", "OK");
                    }

                    ipages.put(String.valueOf(customFFs.get(i).getmPage()), "");

                    itens.add(aux);
                }
            }

        }

        Set keys = ipages.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) ipages.get(key);

            item.put(key, pageStatus(key, itens));

        }

        return item;
    }

    private String pageStatus(String page, ArrayList<HMAux> itens) {
        int total = 0;
        int ok = 0;
        int error = 0;
        int pending = 0;

        for (HMAux aux : itens) {

            if (aux.get("page").equals(page)) {
                switch (aux.get("value").toUpperCase()) {
                    case "PENDING":
                        pending++;
                        break;
                    case "ERROR":
                        error++;
                        break;
                    case "OK":
                        ok++;
                        break;
                    default:
                        break;
                }

                total++;
            }
        }

        if (error != 0) {
            return "ERROR";
        }

        if (total == pending) {
            return "PENDING";
        }

        if (total == ok) {
            return "OK";
        }

        return "EXEC";
    }

    private void prepareFormSave() {
//
//        for (GE_Custom_Form_Data_Field df : formData.getDataFields()) {
//            df.setValue(returnFieldValue(df.getCustom_form_seq(), 0));
//            df.setValue_extra(returnFieldValue(df.getCustom_form_seq(), 1));
//        }
//
//        int quantidade = returnValidCheck();
//
//        String sF = formData.getToken();
    }

    private class ScreenAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> data;

        public ScreenAdapter(FragmentManager fm, ArrayList<Fragment> dados) {
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

                               /* GE_FileDao geFileDao = new GE_FileDao(
                                        context,
                                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
                                );

                                geFileDao.addUpdate(geFiles, false);

                                activateUpload(context);*/

                                // Hugo is ok

//                                if (mSo_Prefix == null || mSo_Code == null) {
//                                    callAct005(context);
//                                } else {
//                                    nservCall();
//                                }

                                if (ToolBox_Con.isOnline(context)) {
                                    enableProgressDialog(
                                            hmAux_Trans.get("alert_send_finish_ttl"),
                                            hmAux_Trans.get("alert_send_finish_msg"),
                                            hmAux_Trans.get("sys_alert_btn_cancel"),
                                            hmAux_Trans.get("sys_alert_btn_ok")
                                    );

                                    executeSerialSave();
                                    //executeSaveProcess();

                                } else {
                                    flowControl();
                                }

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

                                mPresenter.checkData(formData, geFiles, require_serial_done, require_serial_done_ok);
                                bNew = false;
                            }
                        }
                );

                break;

        }
    }

    private void flowControl() {
        //ToolBox_Inf.showNoConnectionDialog(Act011_Main.this);
        if (mSo_Prefix == null || mSo_Code == null) {
            callAct005(context);
        } else {
            nservCall();
        }
    }

    public void exitAlert() {

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
                callAct005(Act011_Main.this);
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
    public void callAct027(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        //Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //mPresenter.onBackPressedClicked();

        if (formData != null && formData.getCustom_form_status().equals(Constant.SYS_STATUS_IN_PROCESSING)) {
            exitAlert();
        } else {
            callAct005(Act011_Main.this);
        }
    }

    @Override
    public void showSignature() {

    }

    @Override
    protected void getSignatueF(String mValue) {
        String sName = mValue;

        if (sName.trim().length() != 0) {

            File sFile = new File(Constant.CACHE_PATH_PHOTO + "/" + mSignature);
            if (sFile.exists()) {
                formData.setSignature(mSignature);
                formData.setSignature_name(sName);
                //
                GE_File geFile = new GE_File();
                geFile.setFile_code(mSignature.replace(".png", ""));
                geFile.setFile_path(mSignature);
                geFile.setFile_status("OPENED");
                geFile.setFile_date(sDate);
                //
                geFiles.add(geFile);
                //

                mPresenter.checkData(formData, geFiles, require_serial_done, require_serial_done_ok);
                bNew = false;
            } else {
                formData.setSignature_name("");
                if (signature == 0) {
                    //mPresenter.checkData(formData);
                }
            }
        } else {
            String sRes = "";
        }
    }

    @Override
    public void callSignature() {
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
        }
    }

    @Override
    protected void getNFCResults(String mValue) {
        String sResults = mValue;

        if (sResults.trim().length() != 0 && sResults.equalsIgnoreCase("OK")) {
            require_serial_done_ok = "OK";
            formData.setDate_end(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            mPresenter.checkData(formData, geFiles, require_serial_done, require_serial_done_ok);
            //
            bNew = false;
        } else {
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
        }
    }

    @Override
    public void callNFCResults() {
        require_serial_done_ok = "";
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
        }
    }

    private void activateUpload(Context context) {
        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
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

        MD_Product_Serial serial = null;

        if(serial_id == null || serial_id.isEmpty()) {
            //Pegar info do serial
            ll_serial_info.setVisibility(View.GONE);
        }else{
             serial = mPresenter.getSerialInfo(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    Integer.parseInt(product_code),
                    serial_id
            );
        }

        if(serial!= null) {
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

        tv_class_id_lbl.setText(hmAux_Trans.get("dialog_info_class_lbl"));
        tv_category_code_lbl.setText(hmAux_Trans.get("dialog_info_category_lbl"));
        tv_segment_lbl.setText(hmAux_Trans.get("dialog_info_segment_lbl"));
        tv_site_lbl.setText(hmAux_Trans.get("dialog_info_site_lbl"));
        tv_zone_lbl.setText(hmAux_Trans.get("dialog_info_zone_lbl"));
        tv_position_lbl.setText(hmAux_Trans.get("dialog_info_position_lbl"));
        tv_info_1_lbl.setText(hmAux_Trans.get("dialog_info_add_info_1_lbl"));
        tv_info_2_lbl.setText(hmAux_Trans.get("dialog_info_add_info_2_lbl"));
        tv_info_3_lbl.setText(hmAux_Trans.get("dialog_info_add_info_3_lbl"));

        tv_title.setText(hmAux_Trans.get("dialog_info_title_lbl"));
//        tv_product_id_lbl.setText(hmAux_Trans.get("dialog_info_product_id_lbl"));
        tv_product_title_lbl.setText(hmAux_Trans.get("dialog_info_product_lbl"));
//        tv_product_code_lbl.setText(hmAux_Trans.get("dialog_info_product_code_lbl"));
        tv_serial_lbl.setText(hmAux_Trans.get("dialog_info_serial_lbl"));

        tv_form_title.setText(hmAux_Trans.get("dialog_info_form_ttl"));
        tv_form_type_lbl.setText(hmAux_Trans.get("dialog_info_form_type_lbl"));
        tv_form_code_lbl.setText(hmAux_Trans.get("dialog_info_form_code_lbl"));
        tv_form_version_lbl.setText(hmAux_Trans.get("dialog_info_form_version_lbl"));

        tv_so_code_lbl.setText(hmAux_Trans.get("dialog_info_so_code_lbl"));

//        tv_product_code_val.setText(product_code);
        tv_product_desc.setText(product_desc);

//        tv_product_id_val.setText(product_id);

        tv_serial_val.setText(serial_id);


        tv_form_type_desc.setText(type + " - " + type_desc);

        tv_form_code_val.setText(form);
        tv_form_code_desc.setText(form_desc);

        tv_form_version_val.setText(form_version);

        if (mSo_Code != null) {
            tv_so_code_desc.setText(String.valueOf(mSo_Prefix) + "." + String.valueOf(mSo_Code));
        } else {
            tv_so_code_desc.setText("");
            //
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_desc.setVisibility(View.GONE);
        }

        GE_Custom_Form_LocalDao formLocalDao =
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

        HMAux dialogFormLocal
                = formLocalDao.getByStringHM(
                new Sql_Act011_003(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        type,
                        form,
                        form_version,
                        form_data
                ).toSqlQuery()

        );
        if (dialogFormLocal != null && dialogFormLocal.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA_SERV).length() > 0) {

            tv_data_serv_lbl.setText(hmAux_Trans.get("dialog_info_data_serv_lbl"));
            tv_dt_schedule_start_lbl.setText(hmAux_Trans.get("dialog_info_dt_schedule_start_lbl"));
            tv_dt_schedule_end_lbl.setText(hmAux_Trans.get("dialog_info_dt_schedule_end_lbl"));

            //
            ll_schedule_info.setVisibility(View.VISIBLE);
            tv_data_serv_val.setText(dialogFormLocal.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA_SERV));
            tv_dt_schedule_start_val.setText(dialogFormLocal.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));
            tv_dt_schedule_end_val.setText(dialogFormLocal.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_END_FORMAT));
        } else {
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
//                aux.put("blob_name", "");
//                aux.put("blob_url_local", "");
//                //
//                pdfs_local.add(aux);
//            }
//        }


        if(pdfs_local.size()>0) {
            String[] from = {"blob_icon", "blob_name"};
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

                    if (aux.get("blob_name").trim().length() != 0) {

                        File file = new File(Constant.CACHE_PATH + "/" + aux.get("blob_url_local"));

                        try {

                            ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);

                            ToolBox_Inf.copyFile(
                                    file,
                                    new File(Constant.CACHE_PDF)
                            );
                        } catch (Exception e) {
                            ToolBox_Inf.registerException(getClass().getName(), e);
                        }


                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(Constant.CACHE_PDF + "/" + aux.get("blob_url_local"))), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        startActivity(intent);
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

    private void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void processGPS_ENABLED() {
        SV_LocationTracker.msg_ok = hmAux_Trans.get("alert_location_info_aquired_succesfully");
        SV_LocationTracker.msg_nok = hmAux_Trans.get("alert_location_info_aquired_unsuccesfully");

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "GPS_GO", hmAux_Trans.get("alert_location_gps_info"), "", "0");
        ToolBox_Inf.call_Location_Tracker(context);
    }

    @Override
    protected void processGPS_GO() {
        super.processGPS_GO();
        //
        gpsCanceled = false;
    }

    @Override
    protected void processGPS_OK(String mLink, String mRequired) {
        progressDialog.dismiss();
        //
        String parts[] = mLink.split("#");
        formData.setLocation_type(parts[0]);
        formData.setLocation_lat(parts[1]);
        formData.setLocation_lng(parts[2]);

        //processa as coordenadas
        if (!gpsCanceled) {
            startCheckIN();
        } else {
            gpsCanceled = false;
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        progressDialog.dismiss();
        //
        formData.setLocation_type("");
        formData.setLocation_lat("");
        formData.setLocation_lng("");
    }

    @Override
    protected void processGPS_STOP() {
        ToolBox_Inf.stop_Location_Tracker(context);

        gpsCanceled = true;

        progressDialog.dismiss();
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
                    if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
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
            if (wsResults.size() > 0) {
                showResults(wsResults);
            } else {
                flowControl();
            }
        }
    }

    public void showResults(List<HMAux> res) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

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
                case "A.P.":
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_form_ap"));
                    break;
                case "S.O.":
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_so"));
                    break;
                case "SO_EXPRESS":
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("lbl_so_express"));
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

        final android.support.v7.app.AlertDialog show = builder.show();

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

}
