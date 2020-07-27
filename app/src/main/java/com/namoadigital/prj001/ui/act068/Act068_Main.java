package com.namoadigital.prj001.ui.act068;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act046.Act046_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act072.Act072_Main;
import com.namoadigital.prj001.ui.act075.Act075_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;
import com.namoadigital.prj001.view.frag.frg_serial_search.On_Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search.PRODUCT_ID;

public class Act068_Main extends Base_Activity_Frag_NFC_Geral implements Act068_Main_Contract.I_View, On_Frg_Serial_Search {

    private Act068_Main_Presenter mPresenter;
    private int pendencies_qty;
    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;
    private HMAux hmAux_Trans_frg_serial_search;
    protected String mResource_CodeSS = "0";
    private String fragProduct_ID;
    private String fragSerial_ID;
    private String fragTracking;
    private boolean fragIsOnlyOne;
    private String wsProcess ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act068_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();

        initAction();

    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.ACT068
        );
        //
        loadTranslation();
        //
        mResource_CodeSS = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Serial_Search();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act068_title");
        transList.add("btn_pendencies");
        transList.add("btn_check_exists");
        transList.add("btn_graphics");
        transList.add("btn_scheduled_tickets");
        transList.add("btn_next_tickets");
        transList.add("alert_no_pendencies_ttl");
        transList.add("alert_no_pendencies_msg");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_no_connection_try_pendencies_msg");
        transList.add("dialog_ticket_save_ttl");
        transList.add("dialog_ticket_save_start");
        transList.add("ticket_lbl");
        transList.add("alert_none_ticket_returned_ttl");
        transList.add("alert_none_ticket_returned_msg");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("alert_ticket_results_ttl");
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
        //
        fm = getSupportFragmentManager();
        //
        mFrgSerialSearch = (Frg_Serial_Search) fm.findFragmentById(R.id.act068_frg_serial_search);
        mFrgSerialSearch.setHmAux_Trans(hmAux_Trans_frg_serial_search);
        mFrgSerialSearch.setSupportNFC(supportNFC);
        controls_sta.addAll(mFrgSerialSearch.getControlsSta());
        mFrgSerialSearch.setClickListener(actionBTN);
        mFrgSerialSearch.setOnSearchClickListener(new Frg_Serial_Search.I_Frg_Serial_Search() {
            @Override
            public void onSearchClick(String btn_Action, HMAux optionsInfo) {
                switch (btn_Action) {
                    case Frg_Serial_Search.BTN_OPTION_01:
                        checkFlow(optionsInfo);
                        break;
                    case Frg_Serial_Search.BTN_OPTION_02:
                        processPendencies();
                        break;
                    case Frg_Serial_Search.BTN_OPTION_03:
                        processNextTickets();
                        break;
                    case Frg_Serial_Search.BTN_OPTION_04:
                        processScheduledTickets();
                        break;
                    default:
                        break;
                }
            }
        });
        //
        mFrgSerialSearch.setShowHideTracking(ToolBox_Con.getPreference_Customer_Uses_Tracking(context) == 1 ? true : false);
        mFrgSerialSearch.setBtn_Option_01_BackGround(R.drawable.namoa_cell_3_states);
        mFrgSerialSearch.setBtn_Option_01_Label(hmAux_Trans.get("btn_check_exists"));
        mFrgSerialSearch.setBtn_Option_02_BackGround(R.drawable.namoa_cell_2_states);
        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_pendencies"));
        mFrgSerialSearch.setBtn_Option_03_BackGround(R.drawable.namoa_cell_2_states);
        mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_next_tickets"));
        mFrgSerialSearch.setBtn_Option_03_Visibility(View.VISIBLE);
        mFrgSerialSearch.setBtn_Option_04_BackGround(R.drawable.namoa_cell_2_states);
        mFrgSerialSearch.setBtn_Option_04_Label(hmAux_Trans.get("btn_scheduled_tickets"));
        mFrgSerialSearch.setBtn_Option_04_Visibility(View.VISIBLE);
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);
        //
        mPresenter = new Act068_Main_Presenter(
            context,this,hmAux_Trans
        );
        //
        //
        mPresenter.getPendencies();
        mPresenter.getMD_Products();
        //
        applyBundleSearchParams();

    }

    private void processScheduledTickets() {
        Intent mIntent = new Intent(context, Act046_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void processNextTickets() {
        Intent intent = new Intent(context, Act075_Main.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Act075_Main.VIEW_PROFILE, 1);
        bundle.putSerializable("TK_Ticket_Product", new ArrayList<>());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void applyBundleSearchParams() {
        if (!fragProduct_ID.isEmpty()) {
            mFrgSerialSearch.setProductIdText(fragProduct_ID);

            if (fragIsOnlyOne){
                mFrgSerialSearch.setShowTree(false);
                mFrgSerialSearch.setShowAll(false);
            } else {
                mFrgSerialSearch.setShowTree(true);
            }
        }
        //
        if (!fragSerial_ID.isEmpty() || !fragTracking.isEmpty()) {
            mFrgSerialSearch.setSerialIdText(fragSerial_ID);
            mFrgSerialSearch.setTrackingText(fragTracking);
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    private void checkFlow(HMAux optionsInfo) {
        if(mPresenter.hasItensToSend()){
            mPresenter.executeWSTicketSave();
        }else{
            processSerialSearch(optionsInfo);
        }
    }


    private void processSerialSearch(HMAux optionsInfo) {
        if (optionsInfo.get(PRODUCT_ID).trim().length() > 0
            || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
            || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0
        ) {
            mPresenter.executeSerialSearch(
                optionsInfo.get(PRODUCT_ID),
                optionsInfo.get(Frg_Serial_Search.SERIAL),
                optionsInfo.get(Frg_Serial_Search.TRACKING)
            );
        } else {
          showMsg(
                hmAux_Trans.get("alert_no_value_filled_ttl"),
                hmAux_Trans.get("alert_no_value_filled_msg")
            );
        }
    }

    private void processPendencies() {
        mPresenter.checkPendenciesFlow(pendencies_qty);
    }

    private void processGraphics() {

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

    @Override
    public void setPendenciesQty(int qty) {
        pendencies_qty = qty;
        String btn_dependency_qty_text = hmAux_Trans.get("btn_pendencies") + " (" + pendencies_qty + ")";
        //
        mFrgSerialSearch.setBtn_Option_02_Label(btn_dependency_qty_text);
    }

    @Override
    public void setProduct(ArrayList<MD_Product> list) {
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
    public void showMsg(String title, String msg) {
        ToolBox.alertMSG(
            context,
            title,
            msg,
            null,
            0
        );
    }
    /**
     * LUCHE - 16/01/2020
     *
     * Alterado metodo para verificar se o progressDialog ja esta instanciado e, caso esteja, atualiza
     * title, msg e exibe o dialog ao inves de criar uma nova instancia.
     *
     * Teste feito para tentar resolver problemas que acontecem em algumas telas que tem chamadas de
     * ws encadeadas. Como cada chamada do enableProgressDialog, gera uma nova instancia do progressDialog,
     * era possivel empilhar dialogs e não conseguir fechar los ja que o se houvessem 2 aberto,
     * não existe mais referencia do primeiro tornando impossivel fechar o dialog.     *
     *
     * O teste mostrou ser efetivo e talvez fosse interessando aplicar esse conceito direto na BaseACt
     *
     * @param title - Titulo
     * @param msg - Msg
     */
    @Override
    public void showPD(String title, String msg) {
        if(progressDialog == null) {
            enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
            );
        }else{
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            //
            if(!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT068;
        mAct_Title = Constant.ACT068 + "_" + "title";
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

    private void initAction() {

    }


    @Override
    public boolean hasHideSerialInfoChk() {
        return true;
    }

    @Override
    public void callAct069(Bundle bundle){
        Intent intent = new Intent(context, Act069_Main.class);
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct072(Bundle bundle) {
        Intent intent = new Intent(context, Act072_Main.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct005() {
        Intent intent = new Intent(context, Act005_Main.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String result, String mRequired, HMAux hmAux) {
        super.processCloseACT(result, mRequired, hmAux);

        if (wsProcess.equalsIgnoreCase(WS_Serial_Search.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.extractSearchResult(result);
        } else if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())) {
            progressDialog.dismiss();
            mPresenter.processSaveReturn(result);
        } else{
            //
            progressDialog.dismiss();
        }
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = view.findViewById(R.id.act028_dialog_btn_ok);
        //trad
        tv_title.setText(hmAux_Trans.get("alert_ticket_results_ttl"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        lv_results.setAdapter(
            new Generic_Results_Adapter(
                context,
                resultList,
                Generic_Results_Adapter.CONFIG_MENU_SEND_RET,
                hmAux_Trans
            )
        );
        //
        builder.setView(view);
        builder.setCancelable(false);
        //
        final AlertDialog show = builder.show();
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                //
                checkFlow(mFrgSerialSearch.getHMAuxValues());
            }
        });
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
    /*
       BARRIONUEVO 17-04-2020
            Atualiza info do footer e info da lista
   */
    @Override
    protected void processRefreshMessage(String mType, String mValue, String mActivity) {
        super.processRefreshMessage(mType, mValue, mActivity);
        iniUIFooter();
    }
}
