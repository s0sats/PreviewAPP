package com.namoadigital.prj001.ui.act054;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Blind_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Search;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Save;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act055.Act055_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act054_Main extends Base_Activity implements Act054_Main_Contract.I_View {


    public static final String ZERO_PENDENCY = "(0)";
    public static final String IS_LOCAL_PROCESS = "isLocalProcess";
    private CheckBox chkInbound;
    private CheckBox chkOutbound;
    private CheckBox chkPlannedMove;
    private CheckBox chkIoOrigins;
    private CheckBox chkIoDestiny;
    private LinearLayout llIoZone;
    private SearchableSpinner ssIoZone;
    private TextView tvIoOrientationLbl;
    private Button btnSearchMoveOrder;
    private Button btnMoveOrderPendency;
    boolean isLocalProcess;
    private Act054_Main_Presenter mPresenter;
    private String wsProcess;
    private String pendeciesCount;
    String zoneDesc;
    private ArrayList<HMAux> wsResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act054_main);


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
                Constant.ACT054
        );

        loadTranslation();
        //
        hideSoftKeyboard();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act054_title");
        transList.add("btn_check_exists");
        //
        transList.add("user_zone_lbl");
        transList.add("user_zone_hint");
        transList.add("search_move_order_lbl");
        transList.add("pendencies_lbl");
        transList.add("destiny_lbl");
        transList.add("origin_lbl");
        transList.add("planned_move_lbl");
        transList.add("blind_move_lbl");
        transList.add("inbound_move_lbl");
        transList.add("outbound_move_lbl");
        transList.add("outbound_lbl");
        transList.add("inbound_lbl");
        transList.add("orientation_lbl");
        //
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        //
        transList.add("alert_must_fill_orientation_ttl");
        transList.add("alert_must_fill_orientation_msg");
        //
        transList.add("alert_must_classify_order_ttl");
        transList.add("alert_must_classify_order_msg");
        //
        transList.add("alert_move_order_not_found_ttl");
        transList.add("alert_move_order_not_found_msg");
        //
        transList.add("dialog_move_order_search_ttl");
        transList.add("dialog_move_order_search_start");
        //
        transList.add("dialog_save_move_ttl");
        transList.add("dialog_save_move_msg");
        transList.add("alert_move_results_ttl");
        transList.add("alert_offline_search_title");
        transList.add("alert_offline_search_msg");
        transList.add("alert_result_movement");
        transList.add("progress_save_outbound_item_ttl");
        transList.add("progress_save_outbound_item_msg");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initVars() {
        wsResults.clear();
        mPresenter = new Act054_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        bindViews();
        setInitialView();
    }

    private void setInitialView() {
        ArrayList<HMAux> zoneList = new ArrayList<>();
        zoneList.addAll(mPresenter.getZoneList());
        ssIoZone.setmOption(zoneList);
        ssIoZone.setmShowLabel(true);
        ssIoZone.setmShowBarcode(true);
        ssIoZone.setmStyle(1);

        chkPlannedMove.setChecked(mPresenter.getSearchFilterPreferences(Act054_Main_Contract.MOVE_PLANNED_TYPE_SEARCH, true));
        chkInbound.setChecked(mPresenter.getSearchFilterPreferences(Act054_Main_Contract.INBOUND_TYPE_SEARCH, true));
        chkOutbound.setChecked(mPresenter.getSearchFilterPreferences(Act054_Main_Contract.OUTBOUND_TYPE_SEARCH, true));
        chkIoDestiny.setChecked(mPresenter.getSearchFilterPreferences(Act054_Main_Contract.DESTINY_ORIENTATION_SEARCH, false));
        chkIoOrigins.setChecked(mPresenter.getSearchFilterPreferences(Act054_Main_Contract.ORIGIN_ORIENTATION_SEARCH, false));

        chkIoDestiny.setEnabled(false);
        chkIoOrigins.setEnabled(false);

        for (HMAux hmAux : zoneList) {
            if (hmAux.hasConsistentValue(SearchableSpinner.CODE)) {
                String s = hmAux.get(SearchableSpinner.CODE);
                String preference_zone_code = String.valueOf(ToolBox_Con.getPreference_Zone_Code(this));
                if (hmAux.get(SearchableSpinner.CODE).equals(preference_zone_code)) {
                    ssIoZone.setmValue(hmAux);
                    chkIoDestiny.setEnabled(true);
                    chkIoOrigins.setEnabled(true);
                    break;
                }
            }
        }
        controls_ss.add(ssIoZone);

        setViewsTranslation();


    }

    private void setViewsTranslation() {
        pendeciesCount = mPresenter.getPendecies();
        ssIoZone.setmTitle(hmAux_Trans.get("user_zone_lbl"));
        btnSearchMoveOrder.setText(hmAux_Trans.get("search_move_order_lbl"));
        btnMoveOrderPendency.setText(hmAux_Trans.get("pendencies_lbl") + pendeciesCount);
        chkIoDestiny.setText(hmAux_Trans.get("destiny_lbl"));
        chkIoOrigins.setText(hmAux_Trans.get("origin_lbl"));
        chkPlannedMove.setText(hmAux_Trans.get("planned_move_lbl"));
        chkOutbound.setText(hmAux_Trans.get("outbound_lbl"));
        chkInbound.setText(hmAux_Trans.get("inbound_lbl"));
        tvIoOrientationLbl.setText(hmAux_Trans.get("orientation_lbl"));
    }

    private void recoverIntentsInfo() {

    }

    private void bindViews() {
        chkInbound = findViewById(R.id.act054_cb_inbound);
        chkOutbound = findViewById(R.id.act054_cb_outbound);
        chkPlannedMove = findViewById(R.id.act054_cb_planned_move);
        llIoZone = findViewById(R.id.act054_ll_io_zone);
        ssIoZone = findViewById(R.id.act054_ss_io_zone);
        tvIoOrientationLbl = findViewById(R.id.act054_tv_io_orientation_lbl);
        chkIoOrigins = findViewById(R.id.act054_cb_io_origins);
        chkIoDestiny = findViewById(R.id.act054_cb_io_destiny);
        btnSearchMoveOrder = findViewById(R.id.act054_search_btn_move_order);
        btnMoveOrderPendency = findViewById(R.id.act054_btn_move_order_pendency);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT054;
        mAct_Title = Constant.ACT054 + Constant.title_lbl;
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


    private void initActions() {
        ssIoZone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux.hasConsistentValue(SearchableSpinner.ID)) {
                    chkIoDestiny.setEnabled(true);
                    chkIoOrigins.setEnabled(true);
                } else {
                    ssIoZone.setmHint(hmAux_Trans.get("user_zone_hint"));
                    chkIoDestiny.setEnabled(false);
                    chkIoDestiny.setChecked(false);
                    chkIoOrigins.setEnabled(false);
                    chkIoOrigins.setChecked(false);
                }
            }
        });

        btnSearchMoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocalProcess = false;
                if (!ssIoZone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    zoneDesc = "";
                } else {
                    zoneDesc = ssIoZone.getmValue().get(SearchableSpinner.CODE);
                }
                if (validateOrderCategory()) {
                    if (validateOrientation(zoneDesc))
                        if (mPresenter.hasWaitingSyncMovePendency()) {
                            mPresenter.syncMovements();
                        } else if (mPresenter.hasWaitingSyncPutAwayPendency()) {
                            mPresenter.executeWsSaveInboundItem();
                        }  else if (mPresenter.hasWaitingSyncPickingPendency()) {
                            mPresenter.executeWsSaveOutobundItem();
                        } else if (mPresenter.hasWaitingSyncBlindPendency()) {
                            mPresenter.executeWsSaveBlindItem();
                        } else {
                            callMovementList();
                        }
                    else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_must_fill_orientation_ttl"),
                                hmAux_Trans.get("alert_must_fill_orientation_msg"),
                                null,
                                0
                        );
                    }
                } else {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_must_classify_order_ttl"),
                            hmAux_Trans.get("alert_must_classify_order_msg"),
                            null,
                            0
                    );
                }
            }
        });

        btnMoveOrderPendency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocalProcess = true;
                if (pendeciesCount.equals(ZERO_PENDENCY)) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_pendencies_title"),
                            hmAux_Trans.get("alert_no_pendencies_msg"),
                            null,
                            0
                    );
                } else {
                    mPresenter.getPendenciesList();
                }
            }
        });
    }

    private void callMovementList() {
        if (ToolBox_Con.isOnline(context)) {
            mPresenter.getMovements(
                    chkInbound.isChecked(),
                    chkOutbound.isChecked(),
                    chkPlannedMove.isChecked(),
                    zoneDesc,
                    chkIoOrigins.isChecked(),
                    chkIoDestiny.isChecked()
            );
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_offline_search_title"),
                    hmAux_Trans.get("alert_offline_search_msg"),
                    null,
                    0
            );
        }
    }

    private boolean validateOrientation(String zoneDesc) {
        return ((!zoneDesc.isEmpty() && (chkIoOrigins.isChecked() || chkIoDestiny.isChecked())) || zoneDesc.isEmpty());
    }

    @Override
    public void callAct055(Bundle bundle) {
        Intent mIntent = new Intent(context, Act055_Main.class);
        if (bundle != null) {
            bundle.putBoolean(IS_LOCAL_PROCESS, isLocalProcess);
            mIntent.putExtras(bundle);
        }
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void setWs_process(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void refreshPendencyCount() {
        pendeciesCount = mPresenter.getPendecies();
        btnMoveOrderPendency.setText(hmAux_Trans.get("pendencies_lbl") + pendeciesCount);
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList) {

        if (resultList.size() > 0) {
            wsResults.addAll(resultList);
            if(!wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())
            && mPresenter.hasWaitingSyncPickingPendency()){
                mPresenter.executeWsSaveOutobundItem();
            }else if (mPresenter.hasWaitingSyncBlindPendency()){
                mPresenter.executeWsSaveBlindItem();
            }else {
                showNewOptDialog(wsResults);
            }
        } else {
            callMovementList();
        }
    }

    /**
     * Alguns WS mais antigos executam a chamada dessa assinatura do metodo
     * processCloseACT e aqui serão "encaminhados" para a segunda assinatura,
     * consolidando as tratativas em um unico metodo.
     * <p>
     * No caso dessa act, o WS_Serial_Search retorna os dados aqui.
     *
     * @param mLink
     * @param mRequired
     */
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_IO_Move_Search.class.getName())) {
            mPresenter.processIOMoveSearch(mLink);
            progressDialog.dismiss();
        } else if (wsProcess.equals(WS_IO_Move_Save.class.getName())) {
            String moves[] = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
            progressDialog.dismiss();
            if (!moves[0].isEmpty()) {
                showResults(moves,true);
            } else if (mPresenter.hasWaitingSyncPutAwayPendency()) {
                mPresenter.executeWsSaveInboundItem();
            } else if (mPresenter.hasWaitingSyncPickingPendency()) {
                mPresenter.executeWsSaveOutobundItem();
            } else if (mPresenter.hasWaitingSyncBlindPendency()){
                mPresenter.executeWsSaveBlindItem();
            }
        } else if (wsProcess.equals(WS_IO_Inbound_Item_Save.class.getName())) {
            mPresenter.processInboundItemSaveReturn(0, 0, mLink);
            progressDialog.dismiss();
        } else if (wsProcess.equals(WS_IO_Outbound_Item_Save.class.getName())) {
            mPresenter.processOutboundItemSaveReturn(0, 0, mLink);
            progressDialog.dismiss();
        } else if (wsProcess.equals(WS_IO_Blind_Move_Save.class.getName())) {
            String moves[] = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
            try{
                if(!moves[0].isEmpty()) {
                    showResults(moves, false);
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
            disableProgressDialog();
        }

        //
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.saveSearchPreferences(chkPlannedMove.isChecked(),
                chkInbound.isChecked(),
                chkOutbound.isChecked(),
                chkIoOrigins.isChecked(),
                chkIoDestiny.isChecked())
        ;
    }

    private boolean validateOrderCategory() {
        return (chkInbound.isChecked() || chkOutbound.isChecked() || chkPlannedMove.isChecked());
    }

    private void showResults(String[] moveArray, boolean isMovePlanned) {
        ArrayList<HMAux> moveList = new ArrayList<>();
        for (int i = 0; i < moveArray.length; i++) {
            String fields[] = moveArray[i].split(Constant.MAIN_CONCAT_STRING_2);
            //
            HMAux mHmAux = new HMAux();
            mHmAux.put("label", fields[0]);
            mHmAux.put("status", fields[1]);
            if(isMovePlanned) {
                mHmAux.put("title", hmAux_Trans.get("planned_move_lbl"));
            }else{
                mHmAux.put("title", hmAux_Trans.get("blind_move_lbl"));
            }
            //
            moveList.add(mHmAux);
            //
        }

        if (moveList.size() > 0) {
            wsResults.addAll(moveList);
            if (mPresenter.hasWaitingSyncPutAwayPendency()) {
                mPresenter.executeWsSaveInboundItem();
            } else if (mPresenter.hasWaitingSyncPickingPendency()){
                    mPresenter.executeWsSaveOutobundItem();
                } else if (mPresenter.hasWaitingSyncBlindPendency()){
                    mPresenter.executeWsSaveBlindItem();
                }else {
                    showNewOptDialog(moveList);
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
                callMovementList();
            }
        });
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
                context,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mPresenter.onBackPressedClicked(Constant.ACT051);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
