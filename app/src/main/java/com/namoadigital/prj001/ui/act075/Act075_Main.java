package com.namoadigital.prj001.ui.act075;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.ctls.FabMenuItem;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act075_Product_List_Adapter;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act070.Act070_Main.IS_OPERATIONAL_PROCESS;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_PIPELINE_PRODUCT_STATUS_NO_CONTROL;
import static com.namoadigital.prj001.util.ConstantBaseApp.TK_PIPELINE_PRODUCT_STATUS_PENDING;

public class Act075_Main extends Base_Activity_Frag implements Act075_Main_Contract.I_View, Act075_Product_List_Adapter.OnProductInteract, Act075_Product_List_Adapter.OnApproveInteract {
    public static final String PRODUCT_LIST = "PRODUCT_LIST";
    public static final String IS_ADD_PRODUCT_LIST = "IS_ADD_PRODUCT_LIST";
    public static final String DECIMAL_PRODUCT_QTY_PATTERN = "###0.####";
    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private String mResource_CodeFrg = "0";
    private HMAux hmAux_Trans_frg_pipeline_header;
    public static final String VIEW_PROFILE = "VIEW_PROFILE";
    public static final int PRODUCT_VIEW_ID = 1;
    public static final int APPROVAL_VIEW_ID = 2;
    /*
        BARRIONUEVO - 22.07.2020
        act_profile - 1 Produto
                      2 Aprovacao
     */
    private TK_Ticket tkTicket;
    private int act_profile;
    RecyclerView rvProduct;
    Button btnSave;
    FabMenu fabMenu;
    FabMenuItem fabStep;
    FabMenuItem fabProduct;
    Act075_Main_Presenter mPresenter;
    private ArrayList<FabMenuItem> fabMenuItems = new ArrayList<>();
    private List<TK_Ticket_Product> tk_ticket_products = new ArrayList<>();
    private Act075_Product_List_Adapter mAdapter;
    private int mTkPrefix;
    private int mTkCode;
    private int mTkSeq;
    private int mStepCode;
    private boolean hasFABActive = false;
    private boolean hasUpdated = false;
    private String wsProcess;
    private TK_Ticket_Step ticketStep;
    private boolean mPipelineHeaderIsCurrentStepOrder;
    private boolean isApproved;
    private boolean isOperationalProcess= false;
    private Bundle requestingBundle;
    private ArrayList<HMAux> wsResult = new ArrayList<>();
    private boolean sync_ticket_form=false;
    private boolean hasPendency = false;
    private String save_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act075_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        rvProduct = findViewById(R.id.act075_rv_product);
        btnSave = findViewById(R.id.act075_save_product);
        fabMenu = (FabMenu) findViewById(R.id.act075_fabMenu_anchor);
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
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT075
        );
        //
        loadTranslation();
        loadTranslationFrg_Pipeline_Header();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void loadTranslationFrg_Pipeline_Header() {

    }

    private void initVars() {
        mPresenter = new Act075_Main_Presenter(context, this, hmAux_Trans);
        recoverIntentsInfo();
        //
        btnSave.setText(hmAux_Trans.get("save_product_lbl"));
        //
        wsResult.clear();
        //
        tkTicket = mPresenter.getTicket(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix,mTkCode);
        setHeaderFragment();
        //
        boolean hasWithdrawnApproved = mPresenter.getWithdrawStatus(tkTicket);
        boolean hasAppliedApproved = mPresenter.getAppliedStatus(tkTicket);
        //
        if(act_profile == 1) {
            //
            if(!hasWithdrawnApproved && tkTicket.getInventory_control() ==1) {
                hasPendency = mPresenter.hasUpdatePendency(tkTicket);
                if (hasPendency) {
                    if (ToolBox_Inf.hasFormGpsPendencyWithinTicket(context, mTkPrefix, mTkCode)) {
                        showAlert(
                                hmAux_Trans.get("alert_form_location_pendency_ttl"),
                                hmAux_Trans.get("alert_form_location_pendency_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callMoveOn();
                                    }
                                },
                                false
                        );
                    } else {
                        if (ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTkPrefix, mTkCode)) {
                            sync_ticket_form = false;
                            mPresenter.callWsSave();
                        } else {
                            mPresenter.executeTicketSaveSyncFormProcess();
                        }
                    }
                }
            }
            setProductList(hasWithdrawnApproved, hasAppliedApproved);
            if(tkTicket.getUpdate_required_product() == 1
                    || !hasUpdated) {
                btnSave.setEnabled(false);
            }else{
                btnSave.setEnabled(true);
            }
            if(hasWithdrawnApproved && hasAppliedApproved){
                btnSave.setVisibility(View.GONE);
            }
            if(hasWithdrawnApproved && hasAppliedApproved
                    || !isEditable(Constant.SYS_STATUS_PENDING, tkTicket.getTicket_status(), Constant.SYS_STATUS_PROCESS, tkTicket.getTicket_status())){
                btnSave.setVisibility(View.GONE);
            }
        }else{
            String ticketCtrlStatus = mPresenter.getSelectedCtrlStatus(mTkPrefix,mTkCode, mTkSeq, mStepCode);
            mPresenter.setStartCtrl();
            if( mPresenter.hasApproveProfile(mTkPrefix, mTkCode, mTkSeq, mStepCode)
                    && tkTicket.getUpdate_required_product() == 1
                    && (isEditable(Constant.SYS_STATUS_PENDING, ticketCtrlStatus, Constant.SYS_STATUS_PROCESS, ticketCtrlStatus))
                    && hasUpdated){
                btnSave.setEnabled(true);
            }else{
                hasUpdated = false;
                btnSave.setEnabled(false);
            }
            setProductForApproveList(ticketCtrlStatus, hasWithdrawnApproved, hasAppliedApproved);
            fabMenu.setVisibility(View.GONE);
        }
        //
        initFabMenuItens();
    }

    @Override
    public boolean hasWithdrawnDataChange(TK_Ticket_Product product) {
        TK_Ticket_Product mTicketProduct = mPresenter.getTicketProduct(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix, mTkCode, product.getProduct_code());
        if(mTicketProduct == null){
            return true;
        }
        if(product.getQty() == null){
            product.setQty(0.0);
        }
        if(mTicketProduct.getQty() == null){
            mTicketProduct.setQty(0.0);
        }
        //
        if(!product.getQty().equals(mTicketProduct.getQty())){
            hasUpdated = true;
            Log.d("PRODUCT", "qty mudou: " + true);
            updateSaveButton(true);
            return true;
        }
        Log.d("PRODUCT", "qty mudou: " + false);
        return false;
    }

    @Override
    public boolean hasAppliedDataChange(TK_Ticket_Product product) {
        //
        TK_Ticket_Product mTicketProduct = mPresenter.getTicketProduct(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix, mTkCode, product.getProduct_code());
        if(mTicketProduct == null){
            return true;
        }
        //
        if(product.getQty_used() == null){
            product.setQty_used(0.0);
        }
        if(mTicketProduct.getQty_used() == null){
            mTicketProduct.setQty_used(0.0);
        }
        //
        if(!product.getQty_used().equals(mTicketProduct.getQty_used())){
            hasUpdated = true;
            Log.d("PRODUCT", "qty_used mudou: " + true);
            updateSaveButton(true);
            return true;
        }
        Log.d("PRODUCT", "qty_used mudou: " + false);
        return false;
    }

    private void setProductList(boolean hasWithdrawnApproved, boolean hasAppliedApproved) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvProduct.setLayoutManager(layoutManager);
        tk_ticket_products = mPresenter.getTicketProductList(tkTicket);
        //
        mAdapter = new Act075_Product_List_Adapter(
                context,
                hmAux_Trans,
                tk_ticket_products,
                act_profile,
                tkTicket.getInventory_control(),
                isEditable(tkTicket.getTicket_status(), Constant.SYS_STATUS_PENDING, tkTicket.getTicket_status(), Constant.SYS_STATUS_PROCESS),
                tkTicket.getMain_user(),
                hasWithdrawnApproved,
                hasAppliedApproved,
                hasPendency,
                this
        );
        //
        rvProduct.setAdapter(mAdapter);
    }

    private boolean isEditable(String ticket_status, String sysStatusPending, String ticket_status2, String sysStatusProcess) {
        return ticket_status.equalsIgnoreCase(sysStatusPending)
                || ticket_status2.equalsIgnoreCase(sysStatusProcess);
    }

    private void setProductForApproveList(String ticketCtrlStatus, boolean hasWithdrawnApproved, boolean hasAppliedApproved) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvProduct.setLayoutManager(layoutManager);
        //
        TK_Ticket_Approval ticketApproval = mPresenter.getTicketApproval(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix, mTkCode, mTkSeq, mStepCode);
        if(!isOperationalProcess) {
            tk_ticket_products = mPresenter.getTicketProductListForApproval(ticketApproval);
        }
        //
        mAdapter = new Act075_Product_List_Adapter(
                context,
                hmAux_Trans,
                tk_ticket_products,
                ticketApproval,
                act_profile,
                tkTicket.getInventory_control(),
                isEditable(Constant.SYS_STATUS_PENDING, ticketCtrlStatus, Constant.SYS_STATUS_PROCESS, ticketCtrlStatus),
                tkTicket.getMain_user(),
                hasWithdrawnApproved,
                hasAppliedApproved,
                this
        );
        //
        rvProduct.setAdapter(mAdapter);
        rvProduct.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        smoothMoveToItemAndScrollItToTop(0);
                    }
                },100
        );
    }

    private void smoothMoveToItemAndScrollItToTop(int position) {
        //Gera smooth scroller
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        //seta a posição
        smoothScroller.setTargetPosition(position);
        //Seta smooth scroller no layoutmaager do recycle o.O
        try {
            ((LinearLayoutManager) rvProduct.getLayoutManager()).startSmoothScroll(smoothScroller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setHeaderFragment() {
        fm = getSupportFragmentManager();
        //
        if(act_profile == 1) {
            mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForProduct(
                    tkTicket.getTicket_id(),
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    ),
                    tkTicket.getOpen_site_code(),
                    tkTicket.getOpen_site_desc(),
                    tkTicket.getOpen_serial_id(),
                    tkTicket.getOpen_product_desc(),
                    tkTicket.getOrigin_desc()
            );
        }else{
            ticketStep = mPresenter.getSelectedStep(mTkPrefix,mTkCode, mStepCode);
            //
            mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForApprovalOrAction(
                    tkTicket.getTicket_id(),
                    ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    ),
                    tkTicket.getOpen_site_code(),
                    tkTicket.getOpen_site_desc(),
                    tkTicket.getOpen_serial_id(),
                    tkTicket.getOpen_product_desc(),
                    tkTicket.getOrigin_desc(),
                    mPresenter.getStepColor(ticketStep,mPipelineHeaderIsCurrentStepOrder),
                    mPresenter.getStepNumFormatted(ticketStep),
                    mPresenter.getStepDesc(ticketStep)
            );
        }
        //
        handleSoftInputFocus();
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.header_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *  BARRIONUEVO - 27-08-2020
     *  Metodo que
     */
    private void handleSoftInputFocus() {
        final FrameLayout header = findViewById(R.id.header_frg_pipeline_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(header.getWindowToken(), 0);
            }
        });
    }

    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            act_profile = requestingBundle.getInt(VIEW_PROFILE, -1);
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            mStepCode = requestingBundle.getInt(TK_Ticket_CtrlDao.STEP_CODE, -1);
            mTkSeq = requestingBundle.getInt(TK_Ticket_CtrlDao.TICKET_SEQ, -1);
            mPipelineHeaderIsCurrentStepOrder = requestingBundle.getBoolean(TK_TicketDao.CURRENT_STEP_ORDER, false);
            isOperationalProcess = requestingBundle.getBoolean(IS_OPERATIONAL_PROCESS, false);
        } else {
            act_profile = -1;
        }
    }

    private void initFabMenuItens() {
        int lblBgColor = getResources().getColor(R.color.namoa_pipeline_background_icon);
        int lblColor = getResources().getColor(R.color.padrao_WHITE);
        int btnBgColor = getResources().getColor(R.color.namoa_sync_pipeline_background_btn);
        int iconColor = getResources().getColor(R.color.colorPrimary);
        //atalho para step
        fabStep = new FabMenuItem(context);
        fabStep.setTag("to_step_lbl");
        fabStep.setmLabel(hmAux_Trans.get("to_step_lbl"));
        fabStep.setmLabel_Back_Color(lblBgColor);
        fabStep.setmLabel_Text_Color(lblColor);
        fabStep.setmButton_Back_Color(btnBgColor);
        fabStep.setmButton_Resource_Color(iconColor);
        fabStep.setmButton_Resource(R.drawable.ic_baseline_assignment_24);
        fabMenuItems.add(fabStep);
        //atalaho para produto.
        fabProduct = new FabMenuItem(context);
        fabProduct.setTag("to_product_lbl");
        fabProduct.setmLabel(hmAux_Trans.get("to_product_lbl"));
        fabProduct.setmLabel_Back_Color(lblBgColor);
        fabProduct.setmLabel_Text_Color(lblColor);
        fabProduct.setmButton_Back_Color(btnBgColor);
        fabProduct.setmButton_Resource_Color(iconColor);
        fabProduct.setmButton_Resource(R.drawable.ic_baseline_build_24);
        fabMenuItems.add(fabProduct);
        fabMenu.setFabMenuItens(fabMenuItems);
        //
        //Seta tradução e itens no FabMenu
        for (FabMenuItem item : fabMenuItems) {
            if (item != null && hmAux_Trans.get((String) item.getTag()) != null) {
                item.setmLabel(hmAux_Trans.get((String) item.getTag()));
            } else {
                item.setmLabel(ToolBox.setNoTrans(mModule_Code, mResource_Code, (String) item.getTag()));
            }
        }
        //
        fabMenu.setFabMenuItens(fabMenuItems);
        fabMenu.setmIcons_Enabled(true);
    }


    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT075;

        //Define o nome baseado no profile e tipo de aprovação
        if(mPresenter != null) {
            mAct_Title = mPresenter.defineActTitle(act_profile, mTkPrefix, mTkCode, mTkSeq, mStepCode);
        }else{
            mAct_Title = Constant.ACT075 + "_" + "title";
        }
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
        ToolBox_Inf.buildFooterDialog(context, false);
    }

    private void initActions() {
        fabMenu.setOnFabClickListener(new FabMenu.IFabMenu() {
            @Override
            public void onFabClick(View view) {
                int id = view.getId();

                if ((id == fabProduct.getId())) {


                }else if (id == fabStep.getId()){
                    verifyChangesBeforeExit();
                }
            }

            @Override
            public void onFabStatusChanged(boolean b) {
                hasFABActive = b;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(act_profile == 1) {
                    ToolBox.alertMSG_YES_NO(context,
                            hmAux_Trans.get("alert_confirm_product_ttl"),
                            hmAux_Trans.get("alert_confirm_product_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean saveSuccess = mPresenter.saveAppliedProduct(tkTicket, (ArrayList<TK_Ticket_Product>) mAdapter.getmValues());
                                    if(saveSuccess) {
                                        if (ToolBox_Con.isOnline(context)) {
                                            if (ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTkPrefix, mTkCode)) {
                                                if (ToolBox_Inf.hasFormGpsPendencyWithinTicket(context, mTkPrefix, mTkCode)) {
                                                    showAlert(
                                                            hmAux_Trans.get("alert_form_location_pendency_ttl"),
                                                            hmAux_Trans.get("alert_form_location_pendency_msg"),
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    callMoveOn();
                                                                }
                                                            },
                                                            false
                                                    );
                                                } else {
                                                    sync_ticket_form = true;
                                                    mPresenter.callWsSave();
                                                }
                                            } else {
                                                mPresenter.executeTicketSaveProcess();
                                            }
                                        } else {

                                            showAlert(
                                                    hmAux_Trans.get("alert_offline_save_ttl"),
                                                    hmAux_Trans.get("alert_offline_save_msg"),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            hasUpdated = false;
                                                            refreshUI();
                                                        }
                                                    },
                                                    false
                                            );
                                        }
                                    }else{
                                        showAlert(
                                                hmAux_Trans.get("alert_error_on_save_product_ttl"),
                                                hmAux_Trans.get("alert_error_on_save_product_msg"),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                },
                                                false
                                        );
                                    }
                                }
                            },
                            1);
                }else if(act_profile == 2){
                    if(mPresenter.hasApproveProfile(mTkPrefix, mTkCode, mTkSeq, mStepCode)) {
                        ToolBox.alertMSG_YES_NO(context,
                                hmAux_Trans.get("alert_confirm_approval_ttl"),
                                hmAux_Trans.get("alert_confirm_approval_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(ToolBox_Inf.hasFormWaitingSyncWithinTicket(context, mTkPrefix, mTkCode)) {
                                            if (ToolBox_Inf.hasFormGpsPendencyWithinTicket(context, mTkPrefix, mTkPrefix)) {
                                                showAlert(
                                                        hmAux_Trans.get("alert_form_location_pendency_ttl"),
                                                        hmAux_Trans.get("alert_form_location_pendency_msg"),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                callMoveOn();
                                                            }
                                                        },
                                                        false
                                                );
                                            } else {
                                                sync_ticket_form = true;
                                                mPresenter.callWsSave();
                                            }
                                        }else {
                                            saveApprovalFlow();
                                        }
                                    }
                                },
                                1
                        );
                    }else{
                        showMsg(
                                hmAux_Trans.get("alert_approval_access_denied_ttl"),
                                hmAux_Trans.get("alert_approval_access_denied_msg")
                        );
                    }
                }
            }
        });
    }

    private void saveApprovalFlow() {
        TK_Ticket_Approval ticketApproval = mPresenter.getTicketApproval(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix, mTkCode, mTkSeq, mStepCode);
        mPresenter.saveApproval(ticketApproval, isApproved, mAdapter.getApprovalCommments());
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(ttl,
                msg,
                context.getString(R.string.generic_msg_cancel),
                context.getString(R.string.generic_msg_ok));
    }

    @Override
    public void showResult(boolean ticketResult) {
        if(wsResult != null && wsResult.isEmpty() && ticketResult){
            Toast.makeText(context,  hmAux_Trans.get("alert_ticket_results_ok"), Toast.LENGTH_SHORT).show();
            //SE SUCESSO NA APROVACAO/REJEICAO VOLTA AUTOMATICAMENTE PARA TELA DE TICKET
            if (act_profile == 2) {
                hasUpdated = false;
                hasFABActive = false;
                onBackPressed();
            }
            refreshUI();
            wsResult.clear();
        }else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act028_dialog_results, null);

            TextView tv_title = view.findViewById(R.id.act028_dialog_tv_title);
            ListView lv_results = view.findViewById(R.id.act028_dialog_lv_results);
            Button btn_ok = view.findViewById(R.id.act028_dialog_btn_ok);
            //trad
            tv_title.setText(hmAux_Trans.get("alert_checkin_results_ttl"));
            btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
            //
            lv_results.setAdapter(
                    new Generic_Results_Adapter(
                            context,
                            wsResult,
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
                    //
                    if (act_profile == 2) {
                        hasUpdated = false;
                        hasFABActive = false;
                        onBackPressed();
                    } else {
                        refreshUI();
                    }
                    //
                    show.dismiss();
                }
            });
        }
    }

    @Override
    public void callMoveOn() {
        resetHasUpdate();
        onBackPressed();
    }

    private void verifyChangesBeforeExit() {
        if(hasUpdated) {
            ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans.get("exit_without_save_ttl"),
                    hmAux_Trans.get("exit_without_save_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callAct070();
                        }
                    },
                    1
            );
        }else{
            callAct070();
        }
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        //super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())) {
            progressDialog.dismiss();
            wsProcess = "";
            if (act_profile == 1) {
                hasPendency = mPresenter.hasUpdatePendency(tkTicket);
            }
            if(mPresenter.verifyProductForForm(tkTicket.getTicket_prefix(), tkTicket.getTicket_code())){
                save_return = mLink;
            }else {
                mPresenter.processSaveReturn(tkTicket.getTicket_prefix(), tkTicket.getTicket_code(), mLink);
            }
        } else if (wsProcess.equalsIgnoreCase(WS_Save.class.getName())) {
            progressDialog.dismiss();
            wsProcess = "";
            mPresenter.processWS_SaveReturn(mLink);
            //
            if (act_profile == 1) {
                if (!mPresenter.getWithdrawStatus(tkTicket)
                        && tkTicket.getInventory_control() == 1) {
                    if (ToolBox_Con.isOnline(context)) {
                        mPresenter.saveproduct(tkTicket.getScn(), (ArrayList<TK_Ticket_Product>) mAdapter.getmValues());
                    } else {
                        ToolBox_Inf.showNoConnectionDialog(context);
                    }
                } else if (!mPresenter.getAppliedStatus(tkTicket) || tkTicket.getInventory_control() == 0) {
                    mPresenter.executeTicketSaveProcess();
                }
            } else {
                if (mPresenter.hasApproveProfile(mTkPrefix, mTkCode, mTkSeq, mStepCode)) {
                    saveApprovalFlow();
                }
            }
        } else if (wsProcess.equalsIgnoreCase(WS_Sync.class.getName())) {
            progressDialog.dismiss();
            mPresenter.processSaveReturn(tkTicket.getTicket_prefix(), tkTicket.getTicket_code(), save_return);
        } else{
            progressDialog.dismiss();
        }
    }

    private void refreshUI() {
        tkTicket = mPresenter.getTicket(ToolBox_Con.getPreference_Customer_Code(context), mTkPrefix, mTkCode);
        setHeaderFragment();
        boolean hasWithdrawnApproved = mPresenter.getWithdrawStatus(tkTicket);
        boolean hasAppliedApproved = mPresenter.getAppliedStatus(tkTicket);
        if (act_profile == 1) {
            tk_ticket_products = tkTicket.getProduct();
            setProductList(hasWithdrawnApproved, hasAppliedApproved);
            mAdapter.setmValues(tk_ticket_products);
            if (tkTicket.getUpdate_required_product() == 1
                    && hasUpdated
                    && (isEditable(Constant.SYS_STATUS_PENDING, tkTicket.getTicket_status(), Constant.SYS_STATUS_PROCESS, tkTicket.getTicket_status()))) {
                btnSave.setEnabled(true);
            } else {
                hasUpdated = false;
                btnSave.setEnabled(false);
            }
            if (hasWithdrawnApproved && hasAppliedApproved
                    || !isEditable(Constant.SYS_STATUS_PENDING, tkTicket.getTicket_status(), Constant.SYS_STATUS_PROCESS, tkTicket.getTicket_status())) {
                btnSave.setVisibility(View.GONE);
            }
        } else {
            String ticketCtrlStatus = mPresenter.getSelectedCtrlStatus(mTkPrefix, mTkCode, mTkSeq, mStepCode);
            if (mPresenter.hasApproveProfile(mTkPrefix, mTkCode, mTkSeq, mStepCode)
                    && tkTicket.getUpdate_required_product() == 1
                    && (isEditable(Constant.SYS_STATUS_PENDING, ticketCtrlStatus, Constant.SYS_STATUS_PROCESS, ticketCtrlStatus))
            ) {
                btnSave.setEnabled(true);
            } else {
                hasUpdated = false;
                btnSave.setEnabled(false);
            }
            setProductForApproveList(ticketCtrlStatus, hasWithdrawnApproved, hasAppliedApproved);
        }
    }

    private void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act075_title");
        transList.add("save_product_lbl");
        transList.add("set_product_qty_lbl");
        transList.add("set_product_qty_used_lbl");
        transList.add("exit_without_save_ttl");
        transList.add("exit_without_save_msg");
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        //
        transList.add("alert_product_add_sucess_ttl");
        transList.add("alert_product_add_sucess_msg");
        transList.add("alert_ticket_updated_ttl");
        transList.add("alert_ticket_updated_msg");
        //
        transList.add("alert_error_on_reject_ttl");
        transList.add("alert_error_on_reject_msg");
        transList.add("alert_error_on_approve_ttl");
        transList.add("alert_error_on_approve_msg");
        transList.add("alert_approval_access_denied_ttl");
        transList.add("alert_approval_access_denied_msg");
        transList.add("ticket_lbl");
        //
        transList.add("dialog_product_save_ticket_ttl");
        transList.add("dialog_product_save_ticket_start");
        transList.add("dialog_ticket_save_ttl");
        transList.add("dialog_ticket_save_start");
        transList.add("alert_offline_save_ttl");
        transList.add("alert_offline_save_msg");
        transList.add("alert_none_ticket_returned_ttl");
        transList.add("alert_none_ticket_returned_msg");
        //
        transList.add("alert_form_location_pendency_ttl");
        transList.add("alert_form_location_pendency_msg");
        //
        transList.add("dialog_ticket_form_save_ttl");
        transList.add("dialog_ticket_form_save_start");
        //
        transList.add("dialog_ticket_update_ttl");
        transList.add("dialog_ticket_update_start");
        //
        transList.add("alert_ticket_results_ok");
        //
        transList.add("alert_has_update_required_ttl");
        transList.add("alert_has_update_required_msg");
        transList.add("alert_form_location_pendency_ttl");
        transList.add("alert_form_location_pendency_msg");
        transList.add("alert_confirm_product_ttl");
        transList.add("alert_confirm_product_msg");
        transList.add("alert_confirm_approval_ttl");
        transList.add("alert_confirm_approval_msg");
        transList.add("alert_form_pendency_please_sync_ttl");
        transList.add("alert_form_pendency_please_sync_msg");
        //
        transList.add("act_ticket_product_ttl");
        transList.add("act_ticket_approval_ttl");
        transList.add("act_ticket_get_material_approval_ttl");
        transList.add("act_ticket_return_material_approval_ttl");
        transList.add("act_ticket_operational_approval_ttl");
        //
        transList.add("progress_sync_ttl");
        transList.add("progress_sync_msg");
        //
        transList.add("alert_error_on_save_product_ttl");
        transList.add("alert_error_on_save_product_msg");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        if (act_profile == 1 && !mPresenter.getWithdrawStatus(tkTicket)) {
            hasPendency = mPresenter.hasUpdatePendency(tkTicket);
        }
        if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())) {
            //caso haja algo no extrato referente ao formulario forca a execucao do extrato.
            if (!wsResult.isEmpty()) {
                showResult(false);
            }
        } else {
            wsResult.clear();
        }
        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        if (act_profile == 1 && !mPresenter.getWithdrawStatus(tkTicket)) {
            hasPendency = mPresenter.hasUpdatePendency(tkTicket);
        }
        if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())) {
            //caso haja algo no extrato referente ao formulario forca a execucao do extrato.
            if (sync_ticket_form) {
                sync_ticket_form = false;
                if (!wsResult.isEmpty()) {
                    showResult(false);
                }
            }
        }else if (wsProcess.equalsIgnoreCase(WS_Sync.class.getName())) {
            progressDialog.dismiss();
            mPresenter.processSaveReturn(tkTicket.getTicket_prefix(), tkTicket.getTicket_code(), save_return);
        } else {
            wsResult.clear();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onAddProduct(boolean hasUpdatedRequired) {
        if (hasUpdatedRequired) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_has_update_required_ttl"),
                    hmAux_Trans.get("alert_has_update_required_msg"),
                    null,
                    0
            );
        } else {
            callAct_Product_Selection(context, (ArrayList<TK_Ticket_Product>) mAdapter.getmValues());
        }
    }

    @Override
    public void callQtyDialog(final int position, TK_Ticket_Product tk_ticket_product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mDialogVIew = inflater.inflate(R.layout.act075_set_qties_dialog, null);
        TextView dialog_set_qty_lbl = mDialogVIew.findViewById(R.id.act075_dialog_set_qty_lbl);
        final MKEditTextNM dialog_set_mkedt_qty = mDialogVIew.findViewById(R.id.act075_dialog_set_mkedt_qty);

        dialog_set_mkedt_qty.setText((new DecimalFormat(DECIMAL_PRODUCT_QTY_PATTERN).format(tk_ticket_product.getQty())).replace(".", ","));
        dialog_set_qty_lbl.setText(hmAux_Trans.get("set_product_qty_lbl"));

        builder.setView(mDialogVIew)
                // Add action buttons
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String qty = dialog_set_mkedt_qty.getText().toString().replace(",", ".");
                        //tk_ticket_products.get(position).setQty(Double.valueOf(qty));

                        if (qty.isEmpty()
                                || qty.contains("-")) {
                            qty = "0.0";
                        }
                        Double inputValue = Double.valueOf(qty);
                        Double qty_value = mAdapter.getmValues().get(position).getQty();
                        if (!inputValue.equals(qty_value)) {
                            mAdapter.getmValues().get(position).setQty(Double.valueOf(qty));
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                })
                .setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }

    @Override
    public void callQtyUsedDialog(final int position, TK_Ticket_Product tk_ticket_product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mDialogVIew = inflater.inflate(R.layout.act075_set_qties_dialog, null);
        TextView dialog_set_qty_lbl = mDialogVIew.findViewById(R.id.act075_dialog_set_qty_lbl);
        final MKEditTextNM dialog_set_mkedt_qty = mDialogVIew.findViewById(R.id.act075_dialog_set_mkedt_qty);

        dialog_set_mkedt_qty.setText((new DecimalFormat(DECIMAL_PRODUCT_QTY_PATTERN).format(tk_ticket_product.getQty_used())).replace(".", ","));
        dialog_set_qty_lbl.setText(hmAux_Trans.get("set_product_qty_used_lbl"));

        builder.setView(mDialogVIew)
                // Add action buttons
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String qty_used = dialog_set_mkedt_qty.getText().toString().replace(",", ".");
//                        tk_ticket_products.get(position).setQty_used(Double.valueOf(qty_used));
                        if (qty_used.isEmpty()
                                || qty_used.contains("-")) {
                            qty_used = "0.0";
                        }
                        Double inputValue = Double.valueOf(qty_used);
                        Double qty_used_value = mAdapter.getmValues().get(position).getQty_used();
                        Double qty_value = mAdapter.getmValues().get(position).getQty();
                        if (qty_value == null) {
                            qty_value = 0.0;
                        }
                        if ((!inputValue.equals(qty_used_value)
                                && inputValue <= qty_value) || tkTicket.getInventory_control() == 0) {
                            mAdapter.getmValues().get(position).setQty_used(Double.valueOf(qty_used));
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                })
                .setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }


    public void callAct_Product_Selection(Context context, ArrayList<TK_Ticket_Product> tk_ticket_products) {
        Intent mIntent = new Intent(context, Act_Product_Selection.class);
        //
        Bundle bundle = new Bundle();
        //
        bundle.putBoolean(IS_ADD_PRODUCT_LIST, true);
        bundle.putSerializable(PRODUCT_LIST, tk_ticket_products);
        mIntent.putExtras(bundle);
        //
        startActivityForResult(mIntent, 20);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        closeFabMenu();
        switch (requestCode) {
            case 20:
                processResult(resultCode, data);
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void closeFabMenu() {
        if (hasFABActive) {
            fabMenu.animateFAB();
        }
    }

    private void processResult(int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            updateSaveButton(true);
            MD_Product pAux = (MD_Product) data.getSerializableExtra(MD_Product.class.getName());
            String pickup_status = tkTicket.getInventory_control() == 0 ? TK_PIPELINE_PRODUCT_STATUS_NO_CONTROL : TK_PIPELINE_PRODUCT_STATUS_PENDING;
            mAdapter.getmValues().add(
                    new TK_Ticket_Product(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            tkTicket.getTicket_prefix(),
                            tkTicket.getTicket_code(),
                            (int) pAux.getProduct_code(),
                            pAux.getProduct_id(),
                            pAux.getProduct_desc(),
                            pAux.getUn(),
                            0.0,
                            0.0,
                            pickup_status,
                            0.0,
                            pickup_status
                    )
            );
            mAdapter.notifyDataSetChanged();
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        if (hasFABActive) {
            fabMenu.animateFAB();
        } else {
            verifyChangesBeforeExit();
        }
    }

    @Override
    public void showMsg(String ttl, String msg) {
        ToolBox.alertMSG(context, ttl, msg, null, 0);
    }

    @Override
    public void showAlert(String ttl, String msg, DialogInterface.OnClickListener listenerOk, boolean showNegative) {
        ToolBox.alertMSG(context, ttl, msg, listenerOk, 0);
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void resetHasUpdate() {
        updateSaveButton(false);
    }

    private void updateSaveButton(boolean b) {
        hasUpdated = b;
        btnSave.setEnabled(hasUpdated);
    }

    @Override
    public void setSaveEnable(boolean isEnable) {
        String ticketCtrlStatus = mPresenter.getSelectedCtrlStatus(mTkPrefix, mTkCode, mTkSeq, mStepCode);
        if (!isEditable(Constant.SYS_STATUS_PENDING, ticketCtrlStatus, Constant.SYS_STATUS_PROCESS, ticketCtrlStatus)) {
            updateSaveButton(false);
        } else {
            updateSaveButton(isEnable);
        }
    }

    @Override
    public void onSelectOption(boolean isApproved) {
        this.isApproved = isApproved;
        String ticketCtrlStatus = mPresenter.getSelectedCtrlStatus(mTkPrefix, mTkCode, mTkSeq, mStepCode);
        if ((act_profile == 2
                && !isEditable(Constant.SYS_STATUS_PENDING, ticketCtrlStatus, Constant.SYS_STATUS_PROCESS, ticketCtrlStatus))) {
            updateSaveButton(false);
            btnSave.setVisibility(View.GONE);
        } else {
            updateSaveButton(true);
        }
    }

    @Override
    public void addResultList(ArrayList<HMAux> resultList) {
        wsResult.addAll(resultList);
    }
}