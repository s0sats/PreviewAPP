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

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Results_Adapter;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.model.Chat_Room_Obj_SO;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_IO_Move_Save;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.ui.act054.Act054_Main;
import com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create;
import com.namoadigital.prj001.util.Constant;
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
    private IO_Move moveInfo;

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
        transList.add("alert_results_ttl");
        transList.add("sys_alert_btn_ok");
        transList.add("alert_move_list_title");
        transList.add("alert_move_ttl");
        transList.add("msg_move_save_ok");

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

        moveInfo = mPresenter.getMoveInfo(movePrefix, moveCode);
        int viewMode = mPresenter.getViewMode(moveInfo);

        MD_Product_Serial serialInfo = mPresenter.getSerialInfo(moveInfo.getProduct_code(), moveInfo.getSerial_code());

        frag_move_create = Frag_Move_Create.newInstance(moveInfo,
                serialInfo,
                viewMode,
                true,
                hmAux_Trans_Frag);

        setFrag(frag_move_create, FRAGMENT_MOVE);
    }

    private void recoverIntentsInfo() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movePrefix = Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_PREFIX));
            moveCode = Integer.valueOf(bundle.getString(IO_MoveDao.MOVE_CODE));
        } else {
            movePrefix = 0;
            moveCode = 0;
        }

    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act058_frg_placeholder, type, sTag);
            ft.addToBackStack(null);
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

        disableProgressDialog();

        if (ws_process.equals(WS_Serial_Tracking_Search.class.getName())) {
            frag_move_create.processTrackingResult(hmAux);
        } else {
            if (ws_process.equals(WS_IO_Move_Save.class.getName())) {
                String moves[] = hmAux.get(WS_IO_Move_Save.MOVE_RETURN_LIST).split(Constant.MAIN_CONCAT_STRING);
                showResults(moves);
            }
        }
    }

    private void showResults(String[] moveArray) {

        ArrayList<HMAux> resultList = new ArrayList<>();

        for (int i = 0; i < moveArray.length; i++) {
            try {
                String fields[] = moveArray[i].split(Constant.MAIN_CONCAT_STRING_2);
                //
                HMAux mHmAux = new HMAux();
                mHmAux.put("label", fields[0]);
                mHmAux.put("status", fields[1]);
                mHmAux.put("final_status", fields[0] + " / " + fields[1]);
                resultList.add(mHmAux);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (resultList.size() == 1) {
            if (resultList.get(0).get("label").equals(moveInfo.getMove_prefix() + "." + moveInfo.getMove_code())) {
                if (resultList.get(0).get("status").equalsIgnoreCase("Ok")) {
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    //
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_move_list_title"),
                            resultList.get(0).get("status"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    refreshUI();
                                }
                            },
                            0
                    );
                }
            }
        } else {
            showNewOptDialog(resultList);
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
        final HMAux auxMove = new HMAux();
        List<HMAux> moveList = new ArrayList<>();

        for (int i = 0; i < resultList.size(); i++) {
            HMAux hmAux = new HMAux();
            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, resultList.get(i).get("label"));
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, resultList.get(i).get("status"));
            moveList.add(hmAux);
            if (resultList.get(i).get("label").equals(moveInfo.getMove_prefix() + "." + moveInfo.getMove_code())) {
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
                if (auxMove.get("status").equalsIgnoreCase("Ok")) {
                    //
                    onBackPressed();
                    //atualizar a tela com os dados do move
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
//        super.onBackPressed();
        callAct054();
    }

    @Override
    public boolean isOnline() {
        return ToolBox_Con.isOnline(context);
    }

    @Override
    public ArrayList<HMAux> getReasonOption() {
        return mPresenter.getMoveReasonList();
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
    public void onTrackingSearchClick(long product_code, long serial_code, String tracking, String site_code) {
        mPresenter.executeTrackingSearch(product_code, serial_code, tracking, site_code);
    }

    @Override
    public ArrayList<HMAux> getClassList() {
        return mPresenter.getClassList();
    }

    @Override
    public void callLogAct(Intent logIntent) {
        startActivityForResult(logIntent, Constant.REQUEST_CODE_SERIAL_LOG);
    }

    @Override
    public void persistIoMove(long customer_code,
                              int move_prefix,
                              int move_code,
                              Integer to_zone_code,
                              Integer to_local_code,
                              Integer to_class_code,
                              Integer reason_code,
                              String done_date,
                              MD_Product_Serial serial) {

        mPresenter.executeMovePersistence(customer_code,
                move_prefix,
                move_code,
                to_zone_code,
                to_local_code,
                to_class_code,
                reason_code,
                done_date,
                serial,
                moveInfo);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
