package com.namoadigital.prj001.ui.act046;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act016.Act016_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT046;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_FILTER_LATE;
import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;

public class Act046_Main extends Base_Activity_Frag_NFC_Geral implements Act046_Main_Contract.I_View, On_Frg_Serial_Search {

    private Act046_Main_Contract.I_Presenter mPresenter;
    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;

    private HMAux hmAux_Trans_frg_serial_search;
    protected String mResource_CodeSS = "0";

    private String ws_process = "";
    private ArrayList<HMAux> wsResults = new ArrayList<>();

    private MD_Product_Serial serial;

    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;
    private HMAux hmAux_Trans_Extra = new HMAux();

    private MKEditTextNM mket_date;
    private ImageView iv_remove;
    private View mOptions;
    private CheckBox cbk_nform;
    private CheckBox cbk_nform_ap;
    private CheckBox cbk_site_logado;
    private CheckBox cbk_ticket;
    private CheckBox.OnCheckedChangeListener chkListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act046_main);

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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT046
        );
        //
        fm = getSupportFragmentManager();
        //
        loadTranslation();

        mResource_CodeSS = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Serial_Search();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("search_date_hint");
        transList.add("chk_n_form");
        transList.add("chk_n_form_ap");
        transList.add("alert_nfc_type_not_supported_ttl");
        transList.add("alert_nfc_type_not_supported_msg");

//        transList.add("search_prod_hint");
//        transList.add("search_serial_hint");
//        transList.add("drawer_product_lbl");
//        transList.add("drawer_product_id_lbl");
//        transList.add("drawer_serial_lbl");
//        transList.add("progress_serial_search_ttl");
//        transList.add("progress_serial_search_msg");
        transList.add("alert_no_search_parameter_ttl");
        transList.add("alert_no_search_parameter_msg");
//        transList.add("progress_nfc_ttl");
//        transList.add("progress_nfc_msg");
//        transList.add("showing_lbl");
//        transList.add("records_lbl");
//        transList.add("no_record_found_lbl");
//        transList.add("alert_nfc_return");
//        transList.add("alert_qty_records_exceeded_ttl");
//        transList.add("alert_qty_records_exceeded_msg");
//        transList.add("alert_qty_records_founded");
//        transList.add("msg_start_search");
//        transList.add("alert_nfc_timeout");
//        transList.add("no_search_realized");
//        transList.add("records_display_limit_lbl");
//        transList.add("records_found_lbl");
//        transList.add("progress_sync_title");
//        transList.add("progress_sync_msg");
//        transList.add("alert_product_not_found_ttl");
//        transList.add("alert_product_not_found_msg");
//        transList.add("alert_new_serial_ttl");
//        transList.add("alert_new_serial_msg");
//        transList.add("alert_new_serial_not_allow_ttl");
//        transList.add("alert_new_serial_not_allow_msg");
//        //
//        transList.add("drawer_tracking_lbl");
//        transList.add("alert_local_product_not_found_ttl");
//        transList.add("alert_local_product_not_found_msg");
//        transList.add("alert_tracking_not_found_ttl");
//        transList.add("alert_tracking_not_found_msg");
        //
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        //
        transList.add("btn_check_exists");
        transList.add("btn_calendar");
        transList.add("btn_lates");
        //
        transList.add("lbl_site");
//
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        /*
         * ENQUANTO NÃO FOR DEFINIDO MODULO NÃO TRAUDZIVEL PARA O TEXTO
         * DO NOME DOS MODULOS, SERÁ USADO ESSE METODO ABAIXO QUE BUSCA DIRETAMENTE
         * DO RECURSO DA ACT005
         * */
        List<String> transList_Extra = new ArrayList<String>();
        transList_Extra.add("lbl_checklist");
        transList_Extra.add("lbl_form_ap");
        transList_Extra.add("lbl_ticket");
        //
        hmAux_Trans_Extra = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                        context,
                        mModule_Code,
                        Constant.ACT005
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transList_Extra
        );
    }

    private void loadTranslationFrg_Serial_Search() {
        hmAux_Trans_frg_serial_search = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_CodeSS,
                ToolBox_Con.getPreference_Translate_Code(context),
                mFrgSerialSearch.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();

        fm = getSupportFragmentManager();

        mket_date = (MKEditTextNM) findViewById(R.id.act046_mket_date);
        mket_date.setHint(hmAux_Trans.get("search_date_hint"));
        mket_date.setText(
                ToolBox.reverseS(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"))
        );

        iv_remove = (ImageView) findViewById(R.id.act046_iv_remove);
        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mket_date.setText("");
            }
        });

        mOptions = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.schedule_filter_layout, null);
        cbk_nform = (CheckBox) mOptions.findViewById(R.id.schedule_filter_chk_n_form);
        cbk_nform.setText(hmAux_Trans_Extra.get("lbl_checklist"));
        cbk_nform.setTag(ConstantBaseApp.SCHEDULE_N_FORM_FILTER_PREFERENCE);
        //
        cbk_nform_ap = (CheckBox) mOptions.findViewById(R.id.schedule_filter_chk_n_form_ap);
        cbk_nform_ap.setText(hmAux_Trans_Extra.get("lbl_form_ap"));
        cbk_nform_ap.setTag(ConstantBaseApp.SCHEDULE_N_FORM_AP_FILTER_PREFERENCE);
        //
        cbk_site_logado = (CheckBox) mOptions.findViewById(R.id.schedule_filter_chk_site_logged);
        cbk_site_logado.setText(hmAux_Trans.get("lbl_site"));
        cbk_site_logado.setTag(ConstantBaseApp.SCHEDULE_SITE_LOGGED_FILTER_PREFERENCE);
        //
        cbk_ticket = (CheckBox) mOptions.findViewById(R.id.schedule_filter_chk_n_ticket);
        cbk_ticket.setText(hmAux_Trans_Extra.get("lbl_ticket"));
        cbk_ticket.setTag(ConstantBaseApp.SCHEDULE_N_TICKET_FILTER_PREFERENCE);
        //
        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act046_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setLl_options(mOptions);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {

                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        processLoadForm(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_02:
                        processCalendario(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_03:
                        processLates(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });

        //mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);

        mFrgSerialSearch.setShowHideProduct(false);
        mFrgSerialSearch.setShowHideTracking(false);
        mFrgSerialSearch.setbTokenPendenciesCheck(false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_check_exists"));
        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_calendar"));
        mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_lates"));
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
        //
        mPresenter = new Act046_Main_Presenter(
                context,
                this,
                hmAux_Trans,
                new MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                )
        );
        //
        hideSoftKeyboard();

        int mDelays = mPresenter.getTotalDelay(true, true);

        if (mDelays > 0) {
            mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_lates") + " (" + String.valueOf(mDelays) + ")");
        } else {
            mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        }

        if (!fragProduct_ID.isEmpty()) {
            mFrgSerialSearch.setProductIdText(fragProduct_ID);

            if (fragIsOnlyOne) {
                mFrgSerialSearch.setShowTree(false);
                mFrgSerialSearch.setShowAll(false);
            } else {
                mFrgSerialSearch.setShowTree(true);
            }
        }

        if (!fragSerial_ID.isEmpty() || !fragTracking.isEmpty()) {
            mFrgSerialSearch.setSerialIdText(fragSerial_ID);
            mFrgSerialSearch.setTrackingText(fragTracking);
        }
        //LUCHE - 10/03/2020
        applyCbkModuleProfile();
        //LUCHE - 21/02/2020
        loadCheckboxValueFromPreferencies();
    }

    /**
     * LUCHE - 10/03/2020
     * Metodo que verifica o profile de acesso aos modulos.
     * Caso não possua acesso,esconde checkbox e reseta preferencia para false
     */
    private void applyCbkModuleProfile() {
        if(!ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_AP,null)){
            cbk_nform_ap.setVisibility(View.GONE);
            mPresenter.saveCheckBoxStatusIntoPreference(String.valueOf(cbk_nform_ap.getTag()),false);
        }
        //
        if(!ToolBox_Inf.profileExists(context,ConstantBaseApp.PROFILE_MENU_TICKET,null)){
            cbk_ticket.setVisibility(View.GONE);
            mPresenter.saveCheckBoxStatusIntoPreference(String.valueOf(cbk_ticket.getTag()),false);
        }
    }
    /**
     * LUCHE
     * Metodo que seta valor da preferencia nos checkbox.
     */
    private void loadCheckboxValueFromPreferencies() {
        cbk_nform.setChecked(
            mPresenter.loadCheckboxStatusFromPreferencie(String.valueOf(cbk_nform.getTag()), true)
        );
        cbk_nform_ap.setChecked(
            mPresenter.loadCheckboxStatusFromPreferencie(String.valueOf(cbk_nform_ap.getTag()), true)
        );
        cbk_ticket.setChecked(
            mPresenter.loadCheckboxStatusFromPreferencie(String.valueOf(cbk_ticket.getTag()), true)
        );
        cbk_site_logado.setChecked(
            mPresenter.loadCheckboxStatusFromPreferencie(String.valueOf(cbk_site_logado.getTag()), false)
        );
    }


    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            fragProduct_ID = bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, "");
            fragSerial_ID = bundle.getString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
            fragTracking = bundle.getString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
        } else {
            fragProduct_ID = "";
            fragSerial_ID = "";
            fragTracking = "";
        }
    }

    private void processLoadForm(HMAux optionsInfo) {
        if (optionsInfo.get(Frg_Serial_Search.PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0
                || !mket_date.getText().toString().equalsIgnoreCase("")
                ) {

            callAct017(context, optionsInfo.get(Frg_Serial_Search.SERIAL).trim(), false);

        } else {

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_search_parameter_ttl"),
                    hmAux_Trans.get("alert_no_search_parameter_msg"),
                    null,
                    0
            );
        }
    }

    private void processCalendario(HMAux optionsInfo) {
        callAct016(context);
    }

    private void processLates(HMAux optionsInfo) {
        callAct017(context, "", true);
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT046;
        mAct_Title = Constant.ACT046 + "_" + "title";
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
        ToolBox_Inf.buildFooterDialog(context, true);
    }

    private void initActions() {
        //LUCHE - 21/02/2020
        //Criado listener para salvar valor dos checkbox na preferencia.
        chkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.saveCheckBoxStatusIntoPreference(
                    String.valueOf(buttonView.getTag()),
                    isChecked
                );
            }
        };
        //
        applyListenersToChk();
    }

    private void applyListenersToChk() {
        cbk_nform.setOnCheckedChangeListener(chkListener);
        cbk_nform_ap.setOnCheckedChangeListener(chkListener);
        cbk_ticket.setOnCheckedChangeListener(chkListener);
        cbk_site_logado.setOnCheckedChangeListener(chkListener);
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct016(Context context) {
        Intent mIntent = new Intent(context, Act016_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ACT_SELECTED_DATE, null);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct017(Context context, String serial_id, boolean late) {
        Intent mIntent = new Intent(context, Act017_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, ACT046);
        bundle.putString(ACT_SELECTED_DATE, ToolBox.reverseB(mket_date.getText().toString()).length() != 0 ? ToolBox.reverseB(mket_date.getText().toString()) : null);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial_id);
        bundle.putBoolean(ACT_FILTER_LATE, late);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        callAct005(context);
    }

    // NFC Processing Data
    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);

        Log.d("NFC", value[0]);

        if (!status) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ToolBox.alertMSG(
                    context,
                    "Erro:",
                    value[0],
                    null,
                    0
            );

        } else {
            switch (value[0]) {
                case PRODUCT:

                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_nfc_type_not_supported_ttl"),
                            hmAux_Trans.get("alert_nfc_type_not_supported_msg"),
                            null,
                            0
                    );

                    break;
                case SERIAL:

                    mFrgSerialSearch.setSerialIdText(value[3]);

                    processLoadForm(mFrgSerialSearch.getHMAuxValues());

                    break;

                default:
                    break;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean hasHideSerialInfoChk() {
        return true;
    }
}
