package com.namoadigital.prj001.ui.act059;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act059_Main extends Base_Activity_Frag implements Act059_Main_Contract.I_View, Frag_Move_Create.OnFragmentInteractionListener {
    public static final String FRAGMENT_MOVE = "FRAGMENT_MOVE";
    private FragmentManager fm;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private String ws_process;
    private String actRequest;
    private Act059_Main_Presenter mPresenter;
    private Integer io_prefix;
    private Integer io_code;
    private Integer io_item;
    private IO_Inbound_Item io_inbound_item;
    private String move_type;
    private Frag_Move_Create frag_move_create;
    private int has_put_away;
    private Integer zone_code_conf;
    private Integer local_code_conf;
    private MD_Product_Serial serialInfo;
    private Integer to_local_code;
    private Integer to_zone_code;
    private int move_prefix;
    private int move_code;
    private Integer reason_code;
    private Integer planned_zone_code;
    private Integer outbound_prefix;
    private Integer inbound_prefix;
    private Integer outbound_code;
    private Integer inbound_code;
    private Integer planned_local_code;
    private String status;
    private Integer to_class_code;
    int viewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act059_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        if (savedInstanceState != null) {
            restoreSavedIntance(savedInstanceState);
        }
        //
        initVars();
        //
        iniUIFooter();
        //

    }

    private void iniSetup() {
        recoverIntentsInfo();
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT059
        );
        //
        mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_MOVE_CREATE
        );

        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act059_title");
        transList.add("dialog_save_move_ttl");
        transList.add("dialog_save_move_msg");
        transList.add("alert_move_results_ttl");
        transList.add("alert_move_list_title");

        transList.add("alert_offline_save_msg");
        transList.add("alert_offline_save_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_result_in_conf");
        transList.add("alert_result_movement");

        transList.addAll(Frag_Move_Create.getFragTranslationsVars());

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code_Frag,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            io_prefix = Integer.valueOf(bundle.getString(IO_Inbound_ItemDao.INBOUND_PREFIX));
            io_code = Integer.valueOf(bundle.getString(IO_Inbound_ItemDao.INBOUND_CODE));
            has_put_away = bundle.getInt(IO_InboundDao.PUT_AWAY_PROCESS);
            zone_code_conf = bundle.getInt(IO_InboundDao.ZONE_CODE_CONF, -1);
            local_code_conf = bundle.getInt(IO_InboundDao.LOCAL_CODE_CONF, -1);
            try {
                io_item = Integer.valueOf(bundle.getString(IO_Inbound_ItemDao.INBOUND_ITEM));
            } catch (Exception e) {
                e.printStackTrace();
                io_item = null;
            }
            actRequest = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT005);
        } else {
            io_prefix = null;
            io_code = null;
            io_item = null;
            actRequest = Constant.ACT005;
        }

    }

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void initVars() {

        mPresenter = new Act059_Main_Presenter(context, this, hmAux_Trans);

        io_inbound_item = mPresenter.getInboudItem(io_prefix, io_code, io_item);
        to_local_code = io_inbound_item.getLocal_code();
        to_zone_code = io_inbound_item.getZone_code();
        move_prefix = -1;
        move_code = -1;
        reason_code = null;
        outbound_prefix = null;
        inbound_prefix = io_inbound_item.getInbound_prefix();
        outbound_code = null;
        inbound_code = io_inbound_item.getInbound_code();
        status = ConstantBaseApp.SYS_STATUS_PENDING;
        //has_put_away == 1 trava spinners
        if (has_put_away == 0) {
            planned_zone_code = io_inbound_item.getPlanned_zone_code();
            planned_local_code = io_inbound_item.getPlanned_local_code();
        } else {
            planned_zone_code = zone_code_conf;
            planned_local_code = local_code_conf;
        }
        to_class_code = null;
        move_type = ConstantBaseApp.IO_PROCESS_IN_CONF;
        viewMode = mPresenter.getViewMode(move_type, has_put_away);
        serialInfo = mPresenter.getSerialInfo(io_inbound_item.getProduct_code(), (int) io_inbound_item.getSerial_code());

        frag_move_create = Frag_Move_Create.newInstance(
                serialInfo,
                viewMode,
                true,
                hmAux_Trans_Frag,
                to_local_code,
                to_zone_code,
                move_prefix,
                move_code,
                reason_code,
                move_type,
                planned_zone_code,
                outbound_prefix,
                inbound_prefix,
                outbound_code,
                inbound_code,
                planned_local_code,
                status,
                to_class_code
        );

        setFrag(frag_move_create, FRAGMENT_MOVE);
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act059_frg_placeholder, type, sTag);
            ft.addToBackStack(null);
            ft.commit();
        } else {

        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT059;
        mAct_Title = Constant.ACT059 + "_" + "title";
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
    protected void processCloseACT(String mLink, String mRequired) {
//        super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        disableProgressDialog();
        if (ws_process.equals(WS_Serial_Tracking_Search.class.getName())) {
            frag_move_create.processTrackingResult(hmAux);
        } else if (ws_process.equals(WS_IO_Inbound_Item_Save.class.getName())) {
            Gson gsonParser = new GsonBuilder().serializeNulls().create();
            ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn> actReturnList
                    = gsonParser.fromJson(mLink, new TypeToken<ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn>>() {
            }.getType());
            showResults(actReturnList);
        }
    }

    private void showResults(ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn> actReturnList) {
        ArrayList<HMAux> resultList = new ArrayList<>();
        for (WS_IO_Inbound_Item_Save.InboundItemSaveActReturn result : actReturnList) {
            HMAux aux = new HMAux();
            String itemPk;

            if (result.isMove()) {
                itemPk = result.getPrefix() + "." + result.getCode();
                aux.put("title", hmAux_Trans.get("alert_result_movement"));
            } else {
                aux.put("title", hmAux_Trans.get("alert_result_in_conf"));
                itemPk = result.getPrefix() + "." + result.getCode() + "." + result.getItem();
            }
            aux.put("item", itemPk);

            if (result.getMsg() == null) {
                aux.put("status", "Ok");
            } else {
                aux.put("status", result.getMsg());
            }


            resultList.add(aux);
        }
        showNewOptDialog(resultList);
    }

    private void showNewOptDialog(ArrayList<HMAux> resultList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = view.findViewById(R.id.act028_dialog_btn_ok);

        //trad
        tv_title.setText(hmAux_Trans.get("alert_move_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        List<HMAux> formattedList = new ArrayList<>();
        HMAux auxMove = new HMAux();
        for (int i = 0; i < resultList.size(); i++) {
            HMAux hmAux = new HMAux();
            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, resultList.get(i).get("item"));
            if (resultList.get(i).hasConsistentValue("status")) {
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, resultList.get(i).get("status"));
            } else {
                hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, "OK");
            }

            hmAux.put(Generic_Results_Adapter.LABEL_TTL, resultList.get(i).get("title"));


            if (resultList.get(i).hasConsistentValue("item") && resultList.get(i).get("item").equals(
                    io_inbound_item.getInbound_prefix() + "."
                            + io_inbound_item.getInbound_code() + "."
                            + io_inbound_item.getInbound_item())) {
                auxMove = hmAux;
                formattedList.add(0, auxMove);
            } else {
                formattedList.add(hmAux);
            }
        }
        //
        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        formattedList,
                        Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                        hmAux_Trans
                )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        final HMAux finalAuxMove = auxMove;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (finalAuxMove.get(Generic_Results_Adapter.VALUE_ITEM_1).equalsIgnoreCase("OK")) {
                    onBackPressed();
                } else {
                    frag_move_create.restoreUIFields(serialInfo, viewMode, true, hmAux_Trans_Frag, to_local_code, to_zone_code, move_prefix, move_code, reason_code, move_type, planned_zone_code, outbound_prefix, inbound_prefix, outbound_code, inbound_code, planned_local_code, status, to_class_code);
                }
            }
        });
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
    public void persistIoMovePlanned(long customer_code, Integer to_zone_code, Integer to_local_code, Integer to_class_code, Integer reason_code, String comments, String done_date, MD_Product_Serial serial, List<IO_Move_Tracking> trackingFromMove) {
        HMAux zoneInfo = frag_move_create.getZoneInfo();
        HMAux localInfo = frag_move_create.getLocalInfo();

        if (!zoneInfo.hasConsistentValue(SearchableSpinner.ID)) {
            zoneInfo.put(SearchableSpinner.ID, "");
        }

        if (!zoneInfo.hasConsistentValue(SearchableSpinner.DESCRIPTION)) {
            zoneInfo.put(SearchableSpinner.DESCRIPTION, "");
        }

        if (!localInfo.hasConsistentValue(SearchableSpinner.ID)) {
            localInfo.put(SearchableSpinner.ID, "");
        }

        if (!localInfo.hasConsistentValue(SearchableSpinner.DESCRIPTION)) {
            localInfo.put(SearchableSpinner.DESCRIPTION, "");
        }

        mPresenter.executeInConfPersistence(customer_code,
                io_prefix,
                io_code,
                to_zone_code,
                zoneInfo.get(SearchableSpinner.ID),
                zoneInfo.get(SearchableSpinner.DESCRIPTION),
                to_local_code,
                localInfo.get(SearchableSpinner.ID),
                localInfo.get(SearchableSpinner.DESCRIPTION),
                to_class_code,
                reason_code,
                comments,
                done_date,
                serial,
                io_inbound_item,
                trackingFromMove);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showAlert(String ttl, String msg) {
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
    public boolean isOnline() {
        return ToolBox_Con.isOnline(context);
    }

    @Override
    public void onAddOrRemoveControl(MKEditTextNM mket_tracking, boolean add) {
        if (add) {
            controls_sta.add(mket_tracking);
        } else {
            controls_sta.remove(mket_tracking);
        }
    }

    @Override
    public void onAddOrRemoveControlSS(SearchableSpinner searchableSpinner, boolean add) {
        if (add) {
            controls_ss.add(searchableSpinner);
        } else {
            controls_ss.remove(searchableSpinner);
        }
    }

    @Override
    public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
        mPresenter.executeTrackingSearch(product_code, serial_code, tracking, site_code);
    }

    @Override
    public void callLogAct(Intent logIntent) {
        startActivityForResult(logIntent, Constant.REQUEST_CODE_SERIAL_LOG);
    }

    @Override
    public void callAct054() {
        Intent mIntent = new Intent(context, Act054_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void setWs_process(String ws_process) {
        this.ws_process = ws_process;
    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct061() {
        Intent mIntent = new Intent(context, Act061_Main.class);
        Bundle bundle = new Bundle();
        bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD, Act061_Main.INBOUND_FRAG_ITEM);
        bundle.putString(ConstantBaseApp.HMAUX_PROCESS_KEY, Constant.IO_INBOUND);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(io_prefix));
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(io_code));
        //Caso haja a necessidade de destacar o item na lista de inbound
//        bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(serialInfo.getProduct_code()));
//        bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(serialInfo.getSerial_code()));
//        bundle.putString(MD_Product_SerialDao.SERIAL_ID, String.valueOf(serialInfo.getSerial_id()));
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT059);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
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
        mPresenter.onBackPressed(actRequest);
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
