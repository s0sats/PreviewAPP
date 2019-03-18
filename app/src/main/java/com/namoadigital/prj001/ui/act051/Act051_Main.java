package com.namoadigital.prj001.ui.act051;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act052.Act052_Main;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.view.frag.Frg_Serial_Search.PRODUCT_ID;

public class Act051_Main extends Base_Activity_Frag_NFC_Geral implements Act051_Main_Contract.I_View {

    public static final String title_lbl = "_title";
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
        transList.add("act051_title");
        transList.add("btn_check_exists");
        transList.add("btn_move_order");
        transList.add("btn_inbound");
        transList.add("btn_outbound");


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
//        mFrgSerialSearch.setBtn_Option_02_Label(hmAux_Trans.get("btn_move_order"));
        mFrgSerialSearch.setBtn_Option_02_Label("btn_move_order - trad");

//        mFrgSerialSearch.setBtn_Option_03_Label(hmAux_Trans.get("btn_inbound"));
        mFrgSerialSearch.setBtn_Option_03_Label("btn_inbound - trad");
//        mFrgSerialSearch.setBtn_Option_04_Label(hmAux_Trans.get("btn_outbound"));
        mFrgSerialSearch.setBtn_Option_04_Label("btn_outbound - trad");
        mFrgSerialSearch.setBtn_Option_05_Visibility(View.GONE);

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

    @Override
    public void setWsProcess(String process) {
        this.wsProcess = process;
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void processLoadIOAssets(HMAux optionsInfo) {
        if (optionsInfo.get(PRODUCT_ID).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.SERIAL).trim().length() > 0
                || optionsInfo.get(Frg_Serial_Search.TRACKING).trim().length() > 0

        ) {
            mPresenter.executeSerialProcessSearch(
                    optionsInfo.get(PRODUCT_ID),
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

    private void processIOMove(HMAux optionsInfo) {

    }

    private void processIOInbound(HMAux optionsInfo) {

    }

    private void processIOOutbound(HMAux optionsInfo) {

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
        mAct_Title = Constant.ACT051 + title_lbl;
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

    @Override
    public void callAct052(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act052_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        if(bundle != null){
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
    protected void processCloseACT(String mLink, String mRequired) {
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if(wsProcess.equals(WS_IO_Serial_Process_Search.class.getName())){
            progressDialog.dismiss();
            //
            mPresenter.processSearchResult(mLink);
        }
    }
}
