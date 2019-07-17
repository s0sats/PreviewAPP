package com.namoadigital.prj001.ui.act058.act;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Blind_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Inbound_Item_Save;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_IO_Outbound_Item_Save;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.ui.act055.Act055_Main;
import com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act058_Main extends Base_Activity_Frag implements Act058_Main_Contract.I_View, Frag_Move_Create.OnFragmentInteractionListener {

    public static final String FRAGMENT_MOVE = "FRAGMENT_MOVE";
    private FragmentManager fm;
    private Frag_Move_Create frag_move_create;
    private Act058_Main_Presenter mPresenter;
    private int moveCode;
    private int movePrefix;
    private String mResource_Code_Frag;
    private HMAux hmAux_Trans_Frag;
    private String ws_process;
    private String actRequest;
    private int zone_code;
    private int local_code;
    private Integer class_code;
    private String move_type;
    private IO_Move movePlanned;
    private IO_Blind_Move blind_move;
    private int product_code;
    private int serial_code;
    private int blind_tmp;
    private String serial_id;

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
    private MD_Product_Serial serialInfo;
    private int viewMode;
    private boolean isLocalProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act058_main);
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
        //movido para utilizar o objeto na criação da
        recoverIntentsInfo();
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT058
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
        transList.add("act058_title");
        transList.add("dialog_save_move_ttl");
        transList.add("dialog_save_move_msg");
        //transList.add("alert_results_ttl");
        transList.add("alert_move_results_ttl");
        transList.add("alert_move_list_title");
        transList.add("alert_move_ttl");
        transList.add("msg_move_save_ok");
        transList.add("msg_serial_error");

        transList.add("alert_offline_save_msg");
        transList.add("alert_offline_save_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_offline_save_error_ttl");
        transList.add("alert_offline_save_error_msg");
        transList.add("alert_result_movement");
        transList.add("alert_result_in_conf");


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

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void initVars() {

        mPresenter = new Act058_Main_Presenter(context, this, hmAux_Trans);

        if (movePrefix > 0) {
            getMoveInfoFromBD();
        } else {
//            blind_move = mPresenter.getMoveInfo(blind_tmp, product_code, serial_code);
            to_local_code = local_code;
            to_zone_code = zone_code;
            move_prefix = -1;
            move_code = -1;
            reason_code = mPresenter.getReasonCodeDefault(ToolBox_Con.getPreference_Site_Code(context));
            outbound_prefix = null;
            inbound_prefix = null;
            outbound_code = null;
            inbound_code = null;
            status = ConstantBaseApp.SYS_STATUS_PENDING;
            planned_zone_code = zone_code;
            planned_local_code = local_code;
            to_class_code = class_code;
            move_type = ConstantBaseApp.IO_PROCESS_MOVE;
            viewMode = mPresenter.getViewMode(move_type);
            serialInfo = mPresenter.getSerialInfo(product_code, serial_code);
        }


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

    private void getMoveInfoFromBD() {
        movePlanned = mPresenter.getMoveInfo(movePrefix, moveCode);
        move_type = movePlanned.getMove_type();
        viewMode = mPresenter.getViewMode(move_type);
        serialInfo = mPresenter.getSerialInfo(movePlanned.getProduct_code(), movePlanned.getSerial_code());
        try{
            serial_id = serialInfo.getSerial_id();
        }catch (NullPointerException e ){
            e.printStackTrace();
           Toast.makeText(context, hmAux_Trans.get("msg_serial_error"), Toast.LENGTH_SHORT).show();
           onBackPressed();
        }
        to_local_code = movePlanned.getTo_local_code();
        to_zone_code = movePlanned.getTo_zone_code();
        move_prefix = movePlanned.getMove_prefix();
        move_code = movePlanned.getMove_code();
        reason_code = movePlanned.getReason_code();
        planned_zone_code = movePlanned.getPlanned_zone_code();
        outbound_prefix = movePlanned.getOutbound_prefix();
        inbound_prefix = movePlanned.getInbound_prefix();
        outbound_code = movePlanned.getOutbound_code();
        inbound_code = movePlanned.getInbound_code();
        planned_local_code = movePlanned.getPlanned_local_code();
        status = movePlanned.getStatus();
        to_class_code = movePlanned.getTo_class_code();
    }

    private void recoverIntentsInfo() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movePrefix = bundle.getString(IO_MoveDao.MOVE_PREFIX) != null ? Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_PREFIX)) : -1;
            moveCode = bundle.getString(IO_MoveDao.MOVE_CODE) != null ? Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_CODE)) : -1;
            zone_code = bundle.getInt(IO_Blind_MoveDao.ZONE_CODE);
            local_code = bundle.getInt(IO_Blind_MoveDao.LOCAL_CODE);
            class_code = bundle.getInt(IO_Blind_MoveDao.CLASS_CODE);
            product_code = bundle.getInt(MD_Product_SerialDao.PRODUCT_CODE);
            serial_code = bundle.getInt(MD_Product_SerialDao.SERIAL_CODE);
            actRequest = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT005);
            isLocalProcess = bundle.getBoolean(ConstantBaseApp.IS_LOCAL_PROCESS, false);

        } else {
            movePrefix = -1;
            moveCode = -1;
            zone_code = -1;
            local_code = -1;
            class_code = -1;
            product_code = -1;
            serial_code = -1;
            actRequest = Constant.ACT005;
            isLocalProcess =false;
        }
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act058_frg_placeholder, type, sTag);
            //ft.addToBackStack(null);
            ft.commit();
        } else {

        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT058;
        mAct_Title = Constant.ACT058 + "_" + "title";
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


        if (ws_process.equals(WS_Serial_Tracking_Search.class.getName())) {
            frag_move_create.processTrackingResult(hmAux);
            disableProgressDialog();
        } else {
            if (ws_process.equals(WS_IO_Move_Save.class.getName())
                    || ws_process.equals(WS_IO_Blind_Move_Save.class.getName())) {
                String moves[] = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
                try{
                    if(!moves[0].isEmpty()) {
                        showResults(moves);
                    }
                }catch (Exception e ){
                    e.printStackTrace();
                }
                disableProgressDialog();

            }
            if (ws_process.equals(WS_IO_Inbound_Item_Save.class.getName())) {
                Gson gsonParser = new GsonBuilder().serializeNulls().create();
                ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn> actReturnList
                        = gsonParser.fromJson(mLink, new TypeToken<ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn>>() {
                }.getType());
                showResults(actReturnList);
                disableProgressDialog();
            }

            if (ws_process.equals(WS_IO_Outbound_Item_Save.class.getName())) {
                Gson gsonParser = new GsonBuilder().serializeNulls().create();
                ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn> outboundReturnList
                        = gsonParser.fromJson(mLink, new TypeToken<ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn>>() {
                }.getType());
                showOutboundResults(outboundReturnList);
                disableProgressDialog();
            }
        }
    }

    private void showOutboundResults(ArrayList<WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn> outboundReturnList) {
        ArrayList<HMAux> resultList = new ArrayList<>();
        for(WS_IO_Outbound_Item_Save.OutboundItemSaveActReturn result : outboundReturnList){
            HMAux aux = new HMAux();
            String itemPk;

            if(result.isMove()) {
                itemPk= result.getPrefix() + "." + result.getCode();
                aux.put("title", hmAux_Trans.get("alert_result_movement"));
            }else{
                aux.put("title", hmAux_Trans.get("alert_result_in_conf"));
                itemPk= result.getPrefix() + "." + result.getCode() +"."+ result.getItem();
            }
            aux.put("item", itemPk);

            if(result.getMsg() == null){
                aux.put("status","Ok");
            }else{
                aux.put("status",result.getMsg());
            }

            resultList.add(aux);
        }
        showNewOptDialog(resultList);
    }

    private void showResults(ArrayList<WS_IO_Inbound_Item_Save.InboundItemSaveActReturn> actReturnList) {
        ArrayList<HMAux> resultList = new ArrayList<>();
        for(WS_IO_Inbound_Item_Save.InboundItemSaveActReturn result : actReturnList){
            HMAux aux = new HMAux();
            String itemPk;

            if(result.isMove()) {
                itemPk= result.getPrefix() + "." + result.getCode();
                aux.put("title", hmAux_Trans.get("alert_result_movement"));
            }else{
                aux.put("title", hmAux_Trans.get("alert_result_in_conf"));
                itemPk= result.getPrefix() + "." + result.getCode() +"."+ result.getItem();
            }
            aux.put("item", itemPk);

            if(result.getMsg() == null){
                aux.put("status","Ok");
            }else{
                aux.put("status",result.getMsg());
            }

            resultList.add(aux);
        }
        showNewOptDialog(resultList);
    }

    private void showResults(String[] moveArray) {

        ArrayList<HMAux> resultList = new ArrayList<>();

        for (String aMoveArray : moveArray) {
            try {
                String fields[] = aMoveArray.split(Constant.MAIN_CONCAT_STRING_2);
                //
                HMAux mHmAux = new HMAux();
                mHmAux.put("item", fields[0]); // item
                mHmAux.put("status", fields[1]);// status
                mHmAux.put("title", hmAux_Trans.get("alert_result_movement"));
                resultList.add(mHmAux);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (resultList.size() == 1) {
            if (resultList.get(0).get("item").equals(movePrefix + "." + moveCode)
            ||  resultList.get(0).get("item").equals(String.valueOf(blind_tmp)) ) {
                if (resultList.get(0).get("status").equalsIgnoreCase("Ok")) {

                    //
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_move_ttl"),
                            hmAux_Trans.get("msg_move_save_ok"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            },
                            0
                    );
                } else {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_move_list_title"),
                            resultList.get(0).get("status"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    frag_move_create.restoreUIFields(serialInfo, viewMode, true, hmAux_Trans_Frag, to_local_code, to_zone_code, move_prefix, move_code, reason_code, move_type, planned_zone_code, outbound_prefix, inbound_prefix, outbound_code, inbound_code, planned_local_code, status, to_class_code);
                                }
                            },
                            0
                    );
                }
                progressDialog.dismiss();
            }
        } else {
            showNewOptDialog(resultList);
        }
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
        final HMAux auxMove = new HMAux();
        List<HMAux> moveList = new ArrayList<>();

        for (int i = 0; i < resultList.size(); i++) {
            HMAux hmAux = new HMAux();
            hmAux.put(Generic_Results_Adapter.LABEL_TTL, resultList.get(i).get("title"));
            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, resultList.get(i).get("item"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, resultList.get(i).get("status"));
            moveList.add(hmAux);
            if (resultList.get(i).get("item").equals(movePrefix + "." + moveCode)) {
                auxMove.putAll(resultList.get(i));
                break;
            }
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

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //

                if (auxMove.hasConsistentValue("status")
                && auxMove.get("status").equalsIgnoreCase("Ok")) {
                    //
                    onBackPressed();
                    //atualizar a tela com os dados do move
                }else{
                    getMoveInfoFromBD();
                    frag_move_create.restoreUIFields(serialInfo,
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
                            to_class_code);
                }

            }
        });
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
    public boolean isOnline() {
        return ToolBox_Con.isOnline(context);
    }

    @Override
    public void onAddOrRemoveControl(MKEditTextNM mket_text, boolean add) {
        if (add) {
            controls_sta.add(mket_text);
        } else {
            controls_sta.remove(mket_text);
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
    public void persistIoMovePlanned(long customer_code,
                                     Integer to_zone_code,
                                     Integer to_local_code,
                                     Integer to_class_code,
                                     Integer reason_code,
                                     String comments,
                                     String done_date,
                                     MD_Product_Serial serial,
                                     List<IO_Move_Tracking> trackingFromMove) {

        if (move_type.equals(ConstantBaseApp.IO_PROCESS_MOVE)) {
            blind_tmp = mPresenter.getBlindTmp();
            mPresenter.executeMovePersistence(customer_code,
                    blind_tmp,
                    to_zone_code,
                    to_local_code,
                    to_class_code,
                    reason_code,
                    done_date,
                    serial,
                    movePlanned,
                    trackingFromMove);
        } else {
            mPresenter.executeMovePlannedPersistence(customer_code,
                    movePrefix,
                    moveCode,
                    to_zone_code,
                    to_local_code,
                    to_class_code,
                    reason_code,
                    comments,
                    done_date,
                    serial,
                    movePlanned,
                    trackingFromMove);
        }
    }

    @Override
    protected void footerCreateDialog() {
//        super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(movePlanned.getInbound_prefix()));
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(movePlanned.getInbound_code()));
        //Caso haja a necessidade de destacar o item na lista de inbound
//        bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(product_code));
//        bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(serial_code));
//        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial_id);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT058);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct067() {
        Intent mIntent = new Intent(context, Act067_Main.class);
        Bundle bundle = new Bundle();
        bundle.putString(Act061_Main.FIRST_FRAG_TO_LOAD, Act061_Main.INBOUND_FRAG_ITEM);
        bundle.putString(ConstantBaseApp.HMAUX_PREFIX_KEY, String.valueOf(movePlanned.getOutbound_prefix()));
        bundle.putString(ConstantBaseApp.HMAUX_CODE_KEY, String.valueOf(movePlanned.getOutbound_code()));
        bundle.putString(MD_Product_SerialDao.PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(MD_Product_SerialDao.SERIAL_CODE, String.valueOf(serial_code));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, serial_id);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT058);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct055() {
        Intent mIntent = new Intent(context, Act055_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, actRequest);
        bundle.putBoolean(ConstantBaseApp.IS_LOCAL_PROCESS, isLocalProcess);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }
}
