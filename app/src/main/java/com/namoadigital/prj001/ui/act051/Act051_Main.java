package com.namoadigital.prj001.ui.act051;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Blind_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Search;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act052.Act052_Main;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.ui.act056.Act056_Main;
import com.namoadigital.prj001.ui.act065.Act065_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search.PRODUCT_ID;

public class Act051_Main extends Base_Activity_Frag_NFC_Geral implements Act051_Main_Contract.I_View, On_Frg_Serial_Search {

    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;
    private HMAux hmAux_Trans_frg_serial_search;

    protected String mResource_CodeSS = "0";

    private String fragProduct_ID;
    private String fragProduct_CODE;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;
    private Act051_Main_Presenter mPresenter;
    private String wsProcess;
    private ArrayList<HMAux> wsResults = new ArrayList<>();
    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act051_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSetup();
        initVars();
        initFooter();
        initAction();
    }

    private void initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT051
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
        transList.add("act051_title");
        transList.add("btn_check_exists");
        transList.add("btn_move_order");
        transList.add("btn_inbound");
        transList.add("btn_outbound");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("planned_move_lbl");
        transList.add("blind_move_lbl");
        transList.add("inbound_move_lbl");
        transList.add("outbound_move_lbl");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("alert_move_results_ttl");
        transList.add("dialog_save_move_ttl");
        transList.add("dialog_save_move_msg");
        transList.add("progress_save_outbound_item_ttl");
        transList.add("progress_save_outbound_item_msg");
        transList.add("progress_save_inbound_item_ttl");
        transList.add("progress_save_inbound_item_msg");



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

        fm = getSupportFragmentManager();

        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act051_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        mFrgSerialSearch.setClickListener(actionBTN);

        controls_sta.addAll(mFrgSerialSearch.getControlsSta());

        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {

                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        processLoadIOAssets(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_02:
                        processIOMove(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_03:
                        processIOInbound(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_04:
                        processIOOutbound(optionsInfo);
                        break;
                    default:
                        break;
                }
            }
        });

        mPresenter = new Act051_Main_Presenter(
                context,
                this,
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans
        );

        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_check_exists"));
        mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_2_states);
        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_move_order"));
        mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_inbound"));
        mFrgSerialSearch.setBtn_Option_04_Label(hmAux_Trans.get("btn_outbound"));
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);

        applyProfile();

        hideSoftKeyboard();

        mPresenter.getMD_Products();
        mPresenter.getPendencies();

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
    }

    /**
     * Aplica profiles aos botões
     */
    private void applyProfile() {
        //Seta todos btn como invisiveis
        mFrgSerialSearch.setBtn_Option_02_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_03_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.GONE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
        //Se profile de movimentação, exibi btn
        if (ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_IO, ConstantBaseApp.PROFILE_MENU_IO_PARAM_MOVE_REQUEST)) {
            mFrgSerialSearch.setBtn_Option_02_Visibility(View.VISIBLE);
        }
        //Se profile de inbound, exibi btn
        if (ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_IO, ConstantBaseApp.PROFILE_MENU_IO_PARAM_INBOUND)) {
            mFrgSerialSearch.setBtn_Option_03_Visibility(View.VISIBLE);
        }
        //Se profile de outbound, exibi btn
        if (ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_IO, ConstantBaseApp.PROFILE_MENU_IO_PARAM_OUTBOUND)) {
            mFrgSerialSearch.setBtn_Option_04_Visibility(View.VISIBLE);
        }
    }

    @Override
    public void setWsProcess(String process) {
        this.wsProcess = process;
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void processLoadIOAssets(HMAux optionsInfo) {
        mProduct_id = optionsInfo.get(PRODUCT_ID);
        mSerial_id = optionsInfo.get(Frg_Serial_Search.SERIAL);
        mTracking = optionsInfo.get(Frg_Serial_Search.TRACKING);


        if (optionsInfo.get(PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0

        ) {
            if (mPresenter.hasWaitingSyncMovePendency()) {
                mPresenter.syncMovements();
            } else if (mPresenter.hasWaitingSyncPutAwayPendency()) {
                mPresenter.syncInboundItem();
            } else if (mPresenter.hasWaitingSyncPickingPendency()) {
                mPresenter.syncOutobundItem();
            } else if (mPresenter.hasWaitingSyncBlindPendency()) {
                mPresenter.syncBlindItem();
            } else {
                mPresenter.executeSerialProcessSearch(
                        mProduct_id,
                        mSerial_id,
                        mTracking
                );
            }
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

    private void processIOMove(HMAux optionsInfo) {
        Intent mIntent = new Intent(context, Act054_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    private void processIOInbound(HMAux optionsInfo) {

        Intent mIntent = new Intent(context, Act056_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();


    }

    private void processIOOutbound(HMAux optionsInfo) {

        Intent mIntent = new Intent(context, Act065_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

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

    private void initAction() {

    }

    private void initFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT051;
        mAct_Title = Constant.ACT051 + Constant.title_lbl;
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
//        super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
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
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void showMsg(String title, String msg) {
        ToolBox.alertMSG(
                Act051_Main.this,
                title,
                msg,
                null,
                0
        );
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
            String product_id = "";
            //
            switch (value[0]) {
                case PRODUCT:
                    product_id = mFrgSerialSearch.searchProductInfo(value[2], "");
                    //
                    if (!product_id.equals("")) {
                        mFrgSerialSearch.setProductIdText(product_id);
                        mFrgSerialSearch.setSerialIdText("");
                        mFrgSerialSearch.setTrackingText("");
                        mPresenter.executeSerialProcessSearch(product_id, "", "");
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
                    product_id = mFrgSerialSearch.searchProductInfo(value[2], "");

                    if (!product_id.equals("") || value[2].equalsIgnoreCase("")) {

                        if (!product_id.equals("")) {
                            mFrgSerialSearch.setProductIdText(product_id);
                        }
                        mFrgSerialSearch.setSerialIdText(value[3]);
                        mFrgSerialSearch.setTrackingText("");
                        //
                        HMAux hmAux = mFrgSerialSearch.getHMAuxValues();
                        mPresenter.executeSerialProcessSearch(hmAux.get(PRODUCT_ID), value[3], "");
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

    @Override
    public void callAct052(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act052_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList) {
        if (resultList.size() > 0) {
            wsResults.addAll(resultList);
            if (!wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())
                    && mPresenter.hasWaitingSyncPickingPendency()) {
                mPresenter.syncOutobundItem();
            } else if (mPresenter.hasWaitingSyncBlindPendency()) {
                mPresenter.syncBlindItem();
            } else {
                showNewOptDialog(wsResults);
            }
        } else {
            mPresenter.executeSerialProcessSearch(
                    mProduct_id,
                    mSerial_id,
                    mTracking
            );
        }
    }

    @Override
    public void handleNoConnection() {
        mPresenter.executeSerialProcessSearch(
                mProduct_id,
                mSerial_id,
                mTracking
        );
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_IO_Serial_Process_Search.class.getName())) {
            progressDialog.dismiss();
            //
            mPresenter.processSearchResult(mLink);
        } else if (wsProcess.equals(WS_IO_Move_Save.class.getName())) {
            String moves[] = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
            progressDialog.dismiss();
            if (!moves[0].isEmpty()) {
                showResults(moves, true);
            } else if (mPresenter.hasWaitingSyncPutAwayPendency()) {
                progressDialog.dismiss();
                mPresenter.syncInboundItem();
            } else if (mPresenter.hasWaitingSyncPickingPendency()) {
                progressDialog.dismiss();
                mPresenter.syncOutobundItem();
            } else if (mPresenter.hasWaitingSyncBlindPendency()) {
                progressDialog.dismiss();
                mPresenter.syncBlindItem();
            }
        } else if (wsProcess.equals(WS_IO_Inbound_Item_Save.class.getName())) {
            progressDialog.dismiss();
            mPresenter.processIOItemSaveReturn(mLink, "inbound_move_lbl");
        } else if (wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())) {
            progressDialog.dismiss();
            mPresenter.processIOItemSaveReturn(mLink, "outbound_move_lbl");
        } else if (wsProcess.equals(WS_IO_Blind_Move_Save.class.getName())) {
            progressDialog.dismiss();
            String moves[] = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
            try {
                if (!moves[0].isEmpty()) {
                    showResults(moves, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //ronaldo
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        disableProgressDialog();
    }

    //TRATA MSG SESSION NOT FOUND
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

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private void showResults(String[] moveArray, boolean isMovePlanned) {
        ArrayList<HMAux> moveList = new ArrayList<>();
        for (int i = 0; i < moveArray.length; i++) {
            String fields[] = moveArray[i].split(Constant.MAIN_CONCAT_STRING_2);
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("status", fields[1]);
            if (isMovePlanned) {
                mHmAux.put("title", hmAux_Trans.get("planned_move_lbl"));
            } else {
                mHmAux.put("title", hmAux_Trans.get("blind_move_lbl"));
            }
            //
            moveList.add(mHmAux);
            //
        }
        if (moveList.size() > 0) {
            wsResults.addAll(moveList);
            if (isMovePlanned && mPresenter.hasWaitingSyncPutAwayPendency()) {
                mPresenter.syncInboundItem();
            } else if (isMovePlanned && mPresenter.hasWaitingSyncPickingPendency()) {
                mPresenter.syncOutobundItem();
            } else if (isMovePlanned && mPresenter.hasWaitingSyncBlindPendency()) {
                mPresenter.syncBlindItem();
            } else {
                showNewOptDialog(wsResults);
            }
        }
    }

    private void showNewOptDialog(ArrayList<HMAux> resultList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

        //trad
        tv_title.setText(hmAux_Trans.get("alert_move_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        List<HMAux> moveList = new ArrayList<>();

        for (int i = 0; i < resultList.size(); i++) {
            HMAux hmAux = new HMAux();
            hmAux.put(Generic_Results_Adapter.LABEL_TTL, resultList.get(i).get("title"));
            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, resultList.get(i).get("label"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, resultList.get(i).get("status"));
            moveList.add(hmAux);
        }
        //
        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        moveList,
                        Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                        hmAux_Trans
                )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);
        //LUCHE - 15/01/2020
        //Movido fechamento da janela para antes da abertura do dialog.
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        //
        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                //
                mPresenter.executeSerialProcessSearch(
                        mProduct_id,
                        mSerial_id,
                        mTracking
                );
            }
        });
    }

    @Override
    public boolean hasHideSerialInfoChk() {
        return false;
    }
}
