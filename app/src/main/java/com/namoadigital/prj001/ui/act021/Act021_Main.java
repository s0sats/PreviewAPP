package com.namoadigital.prj001.ui.act021;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.receiver.WBR_SO_Approval;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.sql.SM_SO_Sql_019;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.ui.act025.Act025_Main;
import com.namoadigital.prj001.ui.act026.Act026_Main;
import com.namoadigital.prj001.ui.act040.Act040_Main;
import com.namoadigital.prj001.ui.act047.Act047_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.Frg_Serial_Search.PRODUCT_ID;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main extends Base_Activity_Frag_NFC_Geral implements Act021_Main_View {

    public static final String NEW_OPT_ID = "new_opt_id";
    public static final String NEW_OPT_LABEL = "new_opt_label";

    public static final String NEW_OPT_TP_PRODUCT = "new_opt_tp_product";
    public static final String NEW_OPT_TP_SERIAL = "new_opt_tp_serial";
    public static final String NEW_OPT_TP_LOCATION = "new_opt_tp_location";

    public static final String WS_PROCESS_SO_SAVE = "WS_PROCESS_SO_SAVE";
    public static final String WS_PROCESS_SO_SAVE_APPROVAL = "WS_PROCESS_SO_SAVE_APPROVAL";
    public static final String WS_PROCESS_SO_SYNC = "WS_PROCESS_SO_SYNC";

    private Act021_Main_Presenter mPresenter;

    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;

    private HMAux hmAux_Trans_frg_serial_search;
    protected String mResource_CodeSS = "0";

    private int pendencies_qty;
    private int syncs_qty;

    private int search_pressed;
    private View.OnClickListener searchListner;

    private String ws_process = "";
    private ArrayList<HMAux> wsResults = new ArrayList<>();

    private SM_SODao soDao;

    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    private String fragProduct_ID;
    private String fragProduct_CODE;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act021_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT021
        );
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
        transList.add("act021_title");
        transList.add("btn_load_so");
        transList.add("btn_pendencies_so");
        transList.add("btn_so_express");
        transList.add("btn_sync_so");

        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("alert_new_opt_location_lbl");
        transList.add("alert_so_to_send_ttl");
        transList.add("alert_so_to_send_msg");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("mket_serial_hint");
        transList.add("mket_tracking_hint");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("alert_sync_success_ttl");
        transList.add("alert_sync_success_msg");
        transList.add("progress_so_save_approval_ttl");
        transList.add("progress_so_save_approval_msg");
        transList.add("progress_so_sync_ttl");
        transList.add("progress_so_sync_msg");
        transList.add("progress_so_save_ttl");
        transList.add("progress_so_save_msg");
        transList.add("alert_results_ttl");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("btn_so_next_orders");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
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

        soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(
                        ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        fm = getSupportFragmentManager();

        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act021_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {

                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        processLoadSO(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_02:
                        processPendencies(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_03:
                        processExpress(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_04:
                        processSyncro(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_05:
                        processServiceList(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });

        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_load_so"));
        mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_2_states);
        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_pendencies_so"));
        mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_so_express"));
        mFrgSerialSearch.setBtn_Option_04_Label(hmAux_Trans.get("btn_sync_so"));
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.VISIBLE);
        mFrgSerialSearch.setBtn_Option_05_Label(hmAux_Trans.get("btn_so_next_orders"));

        mPresenter = new Act021_Main_Presenter_Impl(
                context,
                this,
                soDao,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans
        );

        hideSoftKeyboard();

        mPresenter.getMD_Products();
        mPresenter.getPendencies();
        mPresenter.checkSOExpressProfile();
        mPresenter.getSync();

        if (!fragProduct_ID.isEmpty()) {
            mFrgSerialSearch.setProductIdText(fragProduct_ID);

            if (fragIsOnlyOne){
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

    private void processLoadSO(HMAux optionsInfo) {
        if (mPresenter.checkForSoToSend()) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_to_send_ttl"),
                    hmAux_Trans.get("alert_so_to_send_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callAct005(context);
                        }
                    },
                    0
            );
        } else {
            processSerialSearch(optionsInfo);
        }
    }

    private void processSerialSearch(HMAux optionsInfo) {
        if (optionsInfo.get(Frg_Serial_Search.PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0

                ) {
            mPresenter.executeSerialSearch(
                    optionsInfo.get(Frg_Serial_Search.PRODUCT_ID),
                    optionsInfo.get(Frg_Serial_Search.SERIAL),
                    optionsInfo.get(Frg_Serial_Search.TRACKING)
            );
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_value_filled_ttl"),
                    hmAux_Trans.get("alert_no_value_filled_msg"),
                    null,
                    0
            );
        }
    }

    private void processPendencies(HMAux optionsInfo) {
        if (pendencies_qty > 0) {
            callAct026(context);
        } else {
            showMsg();
        }
    }

    private void processExpress(HMAux optionsInfo) {
        callAct040(context);
    }

    private void processSyncro(HMAux optionsInfo) {
        if (ToolBox_Con.isOnline(context)) {
            executeSoSave();
        } else {
            ToolBox_Inf.showNoConnectionDialog(Act021_Main.this);
        }
    }

    private void processServiceList(HMAux optionsInfo) {
        if (ToolBox_Con.isOnline(context)) {
            callAct047(context);
        } else {
            ToolBox_Inf.showNoConnectionDialog(Act021_Main.this);
        }
    }

    private void initActions() {
    }

    public void showMsg() {
        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_no_pendencies_title"),
                hmAux_Trans.get("alert_no_pendencies_msg"),
                null,
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

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showNoCoPendencies() {
        if (pendencies_qty > 0) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("no_connection_ttl"),
                    hmAux_Trans.get("go_so_pendencies_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            processPendencies(null);
                        }
                    },
                    1
            );
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("no_connection_ttl"),
                    hmAux_Trans.get("no_so_pendencies_msg"),
                    null,
                    0
            );
        }
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void setPendencies(int qty, String qtyMyPendencies) {
        pendencies_qty = qty;
        String btn_text =
                hmAux_Trans.get("btn_pendencies_so") + " (" +
                        (qtyMyPendencies.equalsIgnoreCase("0") ? "" : qtyMyPendencies + "/") +
                        pendencies_qty + ")";

        mFrgSerialSearch.setBtn_Option_02_Label(btn_text);
    }

    @Override
    public void setProduto(ArrayList<MD_Product> list) {
        if (list.size() > 1) {
            mFrgSerialSearch.setProductIdText(hmAux_Trans_frg_serial_search.get("product_all_lbl"));
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(true);
            fragIsOnlyOne = false;
        } else if (list.size() == 1) {
            mFrgSerialSearch.setProductIdText(list.get(0).getProduct_id());
            mFrgSerialSearch.setShowTree(false);
            mFrgSerialSearch.setShowAll(false);
            fragIsOnlyOne = true;
        } else {
            mFrgSerialSearch.setProductIdText("");
        }
    }

    @Override
    public void setSync(int qty) {
        if (qty > 0) {
            syncs_qty = qty;
            String btn_text = hmAux_Trans.get("btn_sync_so") + " (" + syncs_qty + ")";

            mFrgSerialSearch.setBtn_Option_04_Label(btn_text);
            mFrgSerialSearch.setBtn_Option_04_Visibility(View.VISIBLE);
        } else {
            mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        }
    }

    @Override
    public void showNewOptDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act006_dialog_new_opt, null);

        /**
         * Ini Vars
         */
        ListView lv_opt = (ListView) view.findViewById(R.id.act006_dialog_opt_lv_opt);

        String[] from = {NEW_OPT_LABEL};
        //int[] to = {android.R.id.text1};
        int[] to = {R.id.namoa_custom_cell_3_tv_item};


        lv_opt.setAdapter(
                new SimpleAdapter(
                        context,
                        getNewOpts(),
                        //android.R.layout.simple_list_item_1,
                        R.layout.namoa_custom_cell_3,
                        from,
                        to
                )
        );

        /**
         * Ini Action
         */
        lv_opt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.defineFlow(item);

            }
        });

        builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }

    private List<HMAux> getNewOpts() {
        List<HMAux> opts = new ArrayList<>();

        HMAux aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_PRODUCT);
        aux.put(NEW_OPT_LABEL, hmAux_Trans.get("alert_new_opt_product_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_SERIAL);
        aux.put(NEW_OPT_LABEL, hmAux_Trans.get("alert_new_opt_serial_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_LOCATION);
        aux.put(NEW_OPT_LABEL, hmAux_Trans.get("alert_new_opt_location_lbl"));
        //opts.add(aux);

        return opts;
    }

    @Override
    public void setSoExpressVisibility(boolean isVisible) {
        mFrgSerialSearch.setBtn_Option_03_Visibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        //
        progressDialog.dismiss();
        //
        mPresenter.extractSearchResult(result);
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct022(Context context) {
        Intent mIntent = new Intent(context, Act022_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct023(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act023_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct025(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act025_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct026(Context context) {
        Intent mIntent = new Intent(context, Act026_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT021);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct040(Context context) {
        Intent mIntent = new Intent(context, Act040_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT021);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private void callAct047(Context context) {
        Intent mIntent = new Intent(context, Act047_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT021);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //
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

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT021;
        mAct_Title = Constant.ACT021 + "_" + "title";
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

    /**
     * Processamento do Botão Sync
     */


    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (ws_process.equals(WS_PROCESS_SO_SAVE)) {

            setWs_process("");
            processSoSave(hmAux);

        } else if (ws_process.equalsIgnoreCase(WS_PROCESS_SO_SAVE_APPROVAL)) {

            setWs_process("");
            processSoApproval(hmAux);

        } else if (ws_process.equals(WS_PROCESS_SO_SYNC)) {

            setWs_process("");
            processSoDownloadResult(hmAux);

        } else {
            progressDialog.dismiss();
        }

    }

    private void processSoSave(HMAux hmAux) {
        String so[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

        if (so.length > 0 && !so[0].isEmpty()) {
            for (int i = 0; i < so.length; i++) {
                String fields[] = so[i].split(Constant.MAIN_CONCAT_STRING_2);
                //
                HMAux mHmAux = new HMAux();
                mHmAux.put("label", "" + fields[0]);
                mHmAux.put("status", fields[1]);
                mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                //
                if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                    wsResults.add(mHmAux);
                }
            }
        } else {
        }

        executeSoSaveApproval();
    }

    private void processSoApproval(HMAux hmAux) {
        String approval[] = hmAux.get(WS_SO_Save.SO_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);

        if (approval.length > 0 && !approval[0].isEmpty()) {
            for (int i = 0; i < approval.length; i++) {
                String fields[] = approval[i].split(Constant.MAIN_CONCAT_STRING_2);
                //
                HMAux mHmAux = new HMAux();
                mHmAux.put("label", fields[0]);
                mHmAux.put("status", fields[1]);
                mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                //
                if (!mHmAux.get("status").equalsIgnoreCase("OK")) {
                    wsResults.add(mHmAux);
                }
            }
        } else {
        }

        executeSoSync();
    }

    private void processSoDownloadResult(HMAux hmAux) {

        progressDialog.dismiss();

        ToolBox.alertMSG(
                Act021_Main.this,
                hmAux_Trans.get("alert_sync_success_ttl"),
                hmAux_Trans.get("alert_sync_success_msg"),
                null,
                0
        );

        mPresenter.getSync();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);

        progressDialog.dismiss();
    }

    private void setRes(String label, String status, String final_status) {
        HMAux res = new HMAux();

        res.put("label", label);
        res.put("status", status);
        res.put("final_status", final_status);

        wsResults.add(res);
    }

    public void executeSoSave() {
        setWs_process(WS_PROCESS_SO_SAVE);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        enableProgressDialog(
                hmAux_Trans.get("progress_so_save_ttl"),
                hmAux_Trans.get("progress_so_save_msg"),
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );

        Intent mIntent = new Intent(context, WBR_SO_Save.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);

        mIntent.putExtras(bundle);

        context.sendBroadcast(mIntent);
    }

    public void executeSoSaveApproval() {
        setWs_process(WS_PROCESS_SO_SAVE_APPROVAL);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setTitle(hmAux_Trans.get("progress_so_save_approval_ttl"));
            progressDialog.setMessage(hmAux_Trans.get("progress_so_save_approval_msg"));
        }

        Intent mIntent = new Intent(context, WBR_SO_Approval.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);

        context.sendBroadcast(mIntent);
    }

    public void executeSoSync() {
        setWs_process(WS_PROCESS_SO_SYNC);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setTitle(hmAux_Trans.get("progress_so_sync_ttl"));
            progressDialog.setMessage(hmAux_Trans.get("progress_so_sync_msg"));
        }

        ArrayList<HMAux> sos = (ArrayList<HMAux>) soDao.query_HM(
                new SM_SO_Sql_019(
                        ToolBox_Con.getPreference_Customer_Code(context)

                ).toSqlQuery()
        );

        StringBuilder mSos = new StringBuilder();
        boolean mFirst = true;

        for (HMAux aux : sos) {
            if (mFirst) {
                mFirst = false;
            } else {
                mSos.append("|");
            }

            mSos.append(aux.get("so"));
        }

        Intent mIntent = new Intent(context, WBR_SO_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, mSos.toString());

        mIntent.putExtras(bundle);

        context.sendBroadcast(mIntent);
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

    // NFC Processing Data
    @Override
    protected void nfcData(boolean status, int id, String... value) {
        super.nfcData(status, id, value);

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
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
                    product_id = mPresenter.searchProductInfo(value[2], "");
                    mFrgSerialSearch.setSerialIdText("");
                    mFrgSerialSearch.setTrackingText("");
                    //
                    if (!product_id.equals("")) {
                        mFrgSerialSearch.setProductIdText(product_id);
                        mPresenter.executeSerialSearch(product_id, "", "");
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
                                hmAux_Trans.get("alert_local_product_not_found_msg"),
                                null,
                                0
                        );
                    }
                    break;
                case SERIAL:
                    //HMAux hmAux = mFrgSerialSearch.getHMAuxValues();
                    product_id = mFrgSerialSearch.searchProductInfo(value[2], "");

                    if (!product_id.equals("") || value[2].equalsIgnoreCase("")) {

                        if (!product_id.equals("")) {
                            mFrgSerialSearch.setProductIdText(product_id);
                        }
                        mFrgSerialSearch.setSerialIdText(value[3]);
                        mFrgSerialSearch.setTrackingText("");
                        //
                        HMAux hmAux = mFrgSerialSearch.getHMAuxValues();
                        mPresenter.executeSerialSearch(hmAux.get(PRODUCT_ID), value[3], "");
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_local_product_not_found_ttl"),
                                hmAux_Trans.get("alert_local_product_not_found_msg"),
                                null,
                                0
                        );
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
