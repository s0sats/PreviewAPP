package com.namoadigital.prj001.ui.act070;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.ctls.FabMenuItem;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Checkin;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_MainVH;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.ui.act070.model.StepForm;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.ui.act070.model.StepNone;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;
import com.namoadigital.prj001.ui.act071.Act071_Main;
import com.namoadigital.prj001.ui.act075.Act075_Main;
import com.namoadigital.prj001.ui.act076.Act076_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act075.Act075_Main.APPROVAL_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.PRODUCT_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.VIEW_PROFILE;

public class Act070_Main extends Base_Activity_Frag implements Act070_Main_Contract.I_View, Frg_Pipeline_Header.OnPipelineFragmentInteractionListener {

    public static final String PARAM_DENIED_BY_CHECKIN = "PARAM_DENIED_BY_CHECKIN";
    public static final String PARAM_CTRL_CREATION = "PARAM_CTRL_CREATION";
    public static final String PARAM_ACTION_CREATION = "PARAM_ACTION_CREATION";
    public static final String IS_OPERATIONAL_PROCESS = "IS_OPERATIONAL_PROCESS";
    public static final String PARAM_FORCE_SEND_BY_FORM_EXEC = "PARAM_FORCE_SEND_BY_FORM_EXEC";

    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private Act070_Main_Presenter mPresenter;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    private TK_Ticket mTicket;
    private String requestingAct;
    private String wsProcess = "";
    private boolean bReadOnly = false;
    private String room_code;
    private FCMReceiver fcmReceiver;
    private int currentStepFirstPosition = -1;

    /**
     * Iniciando terraplanagem e reinicio da tela.
     * @param savedInstanceState
     */
    private RecyclerView rvTicketPipeline;
    private Act070_Steps_Adapter mAdapter;
    private ArrayList<BaseStep> sources = new ArrayList<>();
    private FabMenu fabMenu;
    private FabMenuItem fabStep;
    private FabMenuItem fabProduct;
    private ArrayList<FabMenuItem> fabMenuItems = new ArrayList<>();
    private boolean hasFABActive=false;
    private String save_return = "";
    private int lastPositionClicked =-1;
    private boolean preventSyncLoop = false;
    //LUCHE - 08/09/2020 - Var que define se deve forçar ou não envio ao chegar na act.
    private boolean forceSendByFormExecution = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act070_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initAction();
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            Constant.ACT070
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act070_title");
        transList.add("alert_ticket_parameter_error_ttl");
        transList.add("alert_ticket_parameter_error_msg");
        transList.add("ticket_lbl");
        transList.add("alert_start_checkin_ttl");
        transList.add("alert_start_checkin_confirm");
        transList.add("alert_checkin_not_returned_ttl");
        transList.add("alert_checkin_not_returned_msg");
        transList.add("alert_checkin_results_ttl");
        transList.add("dialog_ticket_checkin_ttl");
        transList.add("dialog_ticket_checkin_start");
        transList.add("dialog_ticket_checkin_cancel_ttl");
        transList.add("dialog_ticket_checkin_cancel_start");
        transList.add("result_checkin_lbl");
        transList.add("result_checkin_cancel_lbl");
        transList.add("alert_sync_data_ttl");
        transList.add("alert_sync_data_msg");
        transList.add("dialog_download_ticket_ttl");
        transList.add("dialog_download_ticket_start");
        transList.add("dialog_ticket_save_ttl");
        transList.add("dialog_ticket_save_start");
        //NOVO_TICKET
        transList.add("process_action_tll");
        transList.add("process_add_new_btn");
        transList.add("process_check_in_btn");
        transList.add("process_check_out_btn");
        transList.add("please_sync_lbl");
        transList.add("alert_start_action_ttl");
        transList.add("alert_start_action_confirm");
        transList.add("alert_step_or_ctrl_not_found_ttl");
        transList.add("alert_step_or_ctrl_not_found_msg");
        //
        transList.add("alert_checkin_confirm_ttl");
        transList.add("alert_checkin_confirm_msg");
        transList.add("alert_error_on_set_checkin_ttl");
        transList.add("alert_error_on_set_checkin_msg");
        //
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        //
        transList.add("alert_checkout_confirm_ttl");
        transList.add("alert_checkout_confirm_msg");
        transList.add("alert_error_on_set_checkout_ttl");
        transList.add("alert_error_on_set_checkout_msg");
        transList.add("alert_offline_save_ttl");
        transList.add("alert_offline_save_msg");
        //DIALOG DE EXPONTANEOS
        transList.add("dialog_pipeline_main_title");
        transList.add("dialog_pipeline_main_msg");
        transList.add("dialog_pipeline_btn_process_action");
        transList.add("dialog_pipeline_btn_cancel");
        //
        transList.add("process_none_tll");
        transList.add("alert_start_none_process_ttl");
        transList.add("alert_start_none_process_msg");
        transList.add("alert_process_access_denied_ttl");
        transList.add("alert_process_started_in_server_msg");
        //
        transList.add("process_checklist_tll");
        transList.add("alert_form_pdf_not_generated_ttl");
        transList.add("alert_form_pdf_not_generated_msg");
        transList.add("alert_form_pdf_not_downloaded_ttl");
        transList.add("alert_form_pdf_not_downloaded_ttl");
        transList.add("alert_form_master_data_not_found_ttl");
        transList.add("alert_form_master_data_not_found_msg");
        //
        transList.add("alert_start_approval_ttl");
        transList.add("alert_start_approval_confirm");
        //
        transList.add("progress_sync_ttl");
        transList.add("progress_sync_msg");
        //
        transList.add("dialog_ticket_form_save_ttl");
        transList.add("dialog_ticket_form_save_start");

        hmAux_Trans = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        );
    }

    private void initVars() {
        bindViews();
        //
        mPresenter = new Act070_Main_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
        recoverIntentsInfo();
        //
        initFabMenuItens();
        //
        if (mPresenter.validateBundleParams(mTkPrefix, mTkCode)) {
            updateTicketData();
        } else {
            paramErrorFlow();
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
//        fabMenu.setFabMenuItens(fabMenuItems);
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

    private void iniHeaderFrag() {
        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForPipeline(
            mTicket.getTicket_id(),
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(mTicket.getOpen_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            ),
            mTicket.getOpen_site_code(),
            mTicket.getOpen_site_desc(),
            mTicket.getOpen_serial_id(),
            mTicket.getOpen_product_desc(),
            hmAux_Trans.get(mTicket.getTicket_status()),
            ToolBox_Inf.getStatusColorV2(context,mTicket.getTicket_status()),
            "\\" + mTicket.getOrigin_desc(),
            hmAux_Trans.get("please_sync_lbl"),
            mPresenter.checkOnlySyncNeeds(mTicket) || mPresenter.checkUpdateRequiredNeeds(mTicket)
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act070_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void syncPipeline() {
        mPresenter.prepareSyncProcess(mTicket);
    }

    private void refreshUi() {
        updateTicketData();
    }

    @Override
    public void callRefreshUi() {
        refreshUi();
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            requestingAct = requestingBundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            room_code = requestingBundle.getString(CH_RoomDao.ROOM_CODE, null);
            forceSendByFormExecution = requestingBundle.getBoolean(PARAM_FORCE_SEND_BY_FORM_EXEC, false);
            //
        } else {
            requestingAct = ConstantBaseApp.ACT069;
            mTkPrefix = -1;
            mTkCode = -1;
            room_code = null;
            forceSendByFormExecution = false;
        }
    }

    private void bindViews() {
        rvTicketPipeline = findViewById(R.id.act070_rv_pipeline);
        fabMenu = findViewById(R.id.act070_fabMenu_anchor);
        //
        setTranslation();
    }

    private void initRecycle() {
        rvTicketPipeline.setLayoutManager(new LinearLayoutManager(context));
        rvTicketPipeline.setAdapter(mAdapter);
        rvTicketPipeline.postDelayed(
            new Runnable() {
                @Override
                public void run() {
                    openCurrentSteps();
                    moveToCurrentStep(currentStepFirstPosition);
                }
            },100
        );
        //
    }

    private void openCurrentSteps() {
        try {
            for (int i = 0; i < sources.size(); i++) {
                if(sources.get(i) instanceof StepMain){
                    if(((StepMain) sources.get(i)).isCurrentStep() && !ConstantBaseApp.SYS_STATUS_DONE.equals(((StepMain) sources.get(i)).getStepStatus())) {
                        Act070_Step_MainVH stepMainVH = (Act070_Step_MainVH) rvTicketPipeline.findViewHolderForAdapterPosition(i);
                        if (stepMainVH != null) {
                            stepMainVH.itemView.performClick();
                        }
                    }
                }
            }
        }catch (Exception e){
            ToolBox.toastMSG(context, e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //openCurrentSteps();
    }

    private void iniAdapter() {
        //
        mAdapter = new Act070_Steps_Adapter(
            context,
            sources,
            new Act070_Steps_Adapter.OnMainClickListener() {
                @Override
                public void onMainClick(boolean isShown, int mainPosition) {
                    mPresenter.updateStepOpenStates(sources, mainPosition, isShown);
                    if (isShown) {
                        mPresenter.removeStepCtrlsContent(sources, mainPosition);
                    } else {
                        mPresenter.generateStepCtrlsContent(
                            mTicket,
                            sources,
                            mainPosition
                        );
                    }
                }
            },
            new Act070_Steps_Adapter.OnActionClickListener() {
                @Override
                public void onActionClick(int actionPosition) {
                    StepAction stepAction = (StepAction) sources.get(actionPosition);
                    mPresenter.defineActionFlow(mTicket, stepAction);
//                    callAct071(
//                        mPresenter.getAct071Bundle(mTicket, stepAction.getStepCode(), stepAction.getProcessTkSeq())
//                    );
                }
            },
            new Act070_Steps_Adapter.OnChecklistClickListener() {
                @Override
                public void onChecklistClick(int checklistPosition) {
                    lastPositionClicked = checklistPosition;
                    StepForm stepForm = (StepForm) sources.get(checklistPosition);
                    mPresenter.defineFormFlow(mTicket,stepForm);
//                    if(ConstantBaseApp.SYS_STATUS_PENDING.equals(stepForm.getProcessStatus())){
//                        showAlert(
//                            hmAux_Trans.get("alert_start_form_process_ttl"),
//                            hmAux_Trans.get("alert_start_form_process_ttl"),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    mPresenter.defineNoneFlow(mTicket, stepNone);
//                                }
//                            },
//                            true
//                        );
//                    }
                }
            },
            new Act070_Steps_Adapter.OnApprovalClickListener() {
                @Override
                public void onApprovalClick(int approvalPosition) {
                    StepApproval stepApproval = (StepApproval) sources.get(approvalPosition);
                    mPresenter.defineApprovalFlow(mTicket, stepApproval);
                }

                @Override
                public void onShowRejectionClick(int approvalPosition) {
                    StepApproval stepApproval = (StepApproval) sources.get(approvalPosition);
                    mPresenter.prepareRejectionDialog(mTicket, stepApproval);
                }
            },
            new Act070_Steps_Adapter.OnProcessBtnClickListener() {
                @Override
                public void onProcessBtnClick(int processBtnPosition) {
                    StepProcessBtn stepProcessBtn = (StepProcessBtn) sources.get(processBtnPosition);
                    mPresenter.defineProcessBtnFlow(mTicket, stepProcessBtn);
                }
            },
            new Act070_Steps_Adapter.OnNoneClickListener() {
                @Override
                public void onNoneClick(int nonePosition) {
                    final StepNone stepNone = (StepNone) sources.get(nonePosition);
                    mPresenter.defineNoneFlow(mTicket, stepNone);
                }
            });
        //
        initRecycle();
    }

    //region NOVO_TICKET
    @Override
    public void setStepperSource(ArrayList<BaseStep> baseSteps) {
        sources = baseSteps;
        iniAdapter();
    }

    @Override
    public void informAdapterInsertRange(int mainPosition, int rangeLength) {
        if(mAdapter != null) {
            mAdapter.notifyItemRangeInserted(mainPosition, rangeLength);
        }
    }

    @Override
    public void informAdapterRemoveRange(int mainPosition, int rangeLength) {
        if(mAdapter != null) {
            mAdapter.notifyItemRangeRemoved(mainPosition, rangeLength);
        }
    }
    //endregion

    @Override
    public void callAct076() {
        Intent intent = new Intent(context, Act076_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.remove(TK_TicketDao.TICKET_PREFIX);
        requestingBundle.remove(TK_TicketDao.TICKET_CODE);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct068() {
        Intent intent = new Intent(context, Act068_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }


    private void setTranslation() {
    }

    private void updateTicketData() {
        mTicket = mPresenter.getTicketObj(mTkPrefix, mTkCode);
        //
        if (mTicket != null) {
            iniHeaderFrag();
            mPresenter.getStepsList(mTicket);
            setReadOnly();
            initFCMReceiver();
            checkSyncNeeds();
        } else {
            paramErrorFlow();
        }
    }

    @Override
    public int getCurrentStepFirstPosition() {
        return currentStepFirstPosition;
    }

    @Override
    public void setCurrentStepFirstPosition(int currentStepFirstPosition) {
        this.currentStepFirstPosition = currentStepFirstPosition;
    }

    /**
     * LUCHE - 03/09/2020
     * Alterado metodo adicionando verificação da flag preventSyncLoop que impede que a tela entre
     * em loop de chamada de WS caso durante alguma chamada seja retornado processCustom_error ou processError_1
     * LUCHE - 08/09/2020
     * Modificado logica da chama de  chamada automatica de sincronismo, para além do checkOnlySyncNeeds,
     * verificar a flag forceSendByFormExecution, que pe enviada via bundle.
     */
    private void checkSyncNeeds() {
        //Se a atualização da tela foi chamada pelas callbacks de erro do Ws, não az chamada do WS
        //e reseta var que controla o loop
        if(preventSyncLoop) {
            preventSyncLoop = false;
        }else {
            if ( (mPresenter.checkOnlySyncNeeds(mTicket) || forceSendByFormExecution) && ToolBox_Con.isOnline(context)) {
                resetForceSendByform();
                mPresenter.prepareSyncProcess(mTicket);
            }else{
                resetForceSendByform();
            }
        }
    }

    private void resetForceSendByform() {
        forceSendByFormExecution = false;
    }

    @Override
    public void updateSyncRequiredByFCM() {
        mTicket.setSync_required(1);
        //
        if(mFrgPipelineHeader != null) {
            mFrgPipelineHeader.updateSyncRequired(true);
        }
    }

    private void updateSyncRequiredByGpsService() {
        mTicket.setUpdate_required(1);
        //
        if(mFrgPipelineHeader != null) {
            mFrgPipelineHeader.updateSyncRequired(true);
        }
    }

    private void initFCMReceiver() {
        fcmReceiver = new FCMReceiver();
        //
        startStopFCMReceiver(true);
    }

    private void startStopFCMReceiver(boolean start) {
        if (start) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConstantBaseApp.WS_FCM);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            LocalBroadcastManager.getInstance(this).registerReceiver(fcmReceiver, filter);
        } else {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver);
        }
    }

    private void setReadOnly() {
        bReadOnly = mPresenter.getReadOnlyDefinition(mTicket);
    }

    private void paramErrorFlow() {
        ToolBox.alertMSG(
            context,
            hmAux_Trans.get("alert_ticket_parameter_error_ttl"),
            hmAux_Trans.get("alert_ticket_parameter_error_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callAct076();
                }
            },
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
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            null,
            0
        );
    }

    @Override
    public void showAlert(String ttl, String msg, DialogInterface.OnClickListener listenerOk, boolean showNegative) {
        ToolBox.alertMSG_YES_NO(
            context,
            ttl,
            msg,
            listenerOk,
            showNegative ? 1 : 0
        );
    }

    @Override
    public String getRequestingAct() {
        return requestingAct;
    }

    @Override
    public void callAct069() {
        Intent intent = new Intent(context, Act069_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.remove(TK_TicketDao.TICKET_PREFIX);
        requestingBundle.remove(TK_TicketDao.TICKET_CODE);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct071(Bundle bundle) {
        Intent intent = new Intent(context, Act071_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Poderia barra ja aqui
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (ConstantBaseApp.ACT012.equals(requestingAct)
            || ConstantBaseApp.ACT014.equals(requestingAct)
            || ConstantBaseApp.ACT035.equals(requestingAct)
            || ConstantBaseApp.ACT017.equals(requestingAct))
        {
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestingAct);
            if (ConstantBaseApp.ACT035.equals(requestingAct)) {
                bundle.putString(CH_RoomDao.ROOM_CODE, room_code);
            }
            //LUCHE - 18/03/2020 - Tratativa especifica do agendamento
            if(ConstantBaseApp.ACT017.equals(requestingAct)){
                bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, requestingBundle.getString(MD_Schedule_ExecDao.SCHEDULE_PK, null));
                bundle.putString(ConstantBaseApp.ACT_SELECTED_DATE, requestingBundle.getString(ConstantBaseApp.ACT_SELECTED_DATE, null));
            }
        }
        //
        intent.putExtras(bundle);
        //
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct035() {
        Intent intent = new Intent(context, Act035_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        //
        intent.putExtras(bundle);
        //
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct017() {
        Intent intent = new Intent(context, Act017_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.ACT_SELECTED_DATE, requestingBundle.getString(ConstantBaseApp.ACT_SELECTED_DATE, null));
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, requestingBundle.getString(MD_Schedule_ExecDao.SCHEDULE_PK, null));
        intent.putExtras(bundle);
        //
        startActivity(intent);
        finish();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT070;
        mAct_Title = Constant.ACT070 + "_" + "title";
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

    private void initAction() {
        fabMenu.setOnFabClickListener(new FabMenu.IFabMenu() {
            @Override
            public void onFabClick(View view) {
                int id = view.getId();

                if ((id == fabProduct.getId())) {
                    callAct075(PRODUCT_VIEW_ID);
                }else if (id == fabStep.getId()){

                }
            }

            @Override
            public void onFabStatusChanged(boolean b) {
                hasFABActive = b;
            }
        });
    }

    @Override
    public void callact075ForApproval(int step_code, int ticket_seq, boolean currentStep, boolean isOperational) {
        Intent intent = new Intent(context, Act075_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.putInt(VIEW_PROFILE, APPROVAL_VIEW_ID);
        requestingBundle.putInt(TK_Ticket_CtrlDao.STEP_CODE, step_code);
        requestingBundle.putInt(TK_Ticket_CtrlDao.TICKET_SEQ, ticket_seq);
        requestingBundle.putBoolean(TK_TicketDao.CURRENT_STEP_ORDER, currentStep);
        requestingBundle.putBoolean(IS_OPERATIONAL_PROCESS, isOperational);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void callAct075(int act_profile) {
        Intent intent = new Intent(context, Act075_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.putInt(VIEW_PROFILE, act_profile);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void callAct011(Bundle act011Bundle) {
        Intent intent = new Intent(context, Act011_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(act011Bundle);
        startActivity(intent);
        finish();
    }

    private void moveToCurrentStep(final int position) {
        if(position > -1) {
            //Faz scroll para o fim do scroll
            new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        smoothMoveToItemAndScrollItToTop(position);
                    }
                }, 400
            );
        }
    }

    /**
     * LUCHE - 28/07/2020
     * Como as tentativas de smoothScroll do recycle e scrollToPositionWithOffset falharam, ja que
     * uma,smoothScroll, naõ rodava item pra primeiro na lista e a outra,scrollToPositionWithOffset,
     * setava no topo, mas abruptamente, foi necessario criar esse semi-demonio
     * @param position
     */
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
            ((LinearLayoutManager) rvTicketPipeline.getLayoutManager()).startSmoothScroll(smoothScroller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList, boolean ticketResult) {
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
                //
                refreshUi();
                //
                show.dismiss();
            }
        });
    }

    class FCMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null
                && bundle.containsKey(ConstantBaseApp.SW_TYPE)
            ) {
                //
                if(bundle.getString(ConstantBaseApp.SW_TYPE).equals(ConstantBaseApp.FCM_ACTION_TK_TICKET_UPDATE)) {
                    if (mPresenter.checkSyncRequireNeedsChange(mTicket.getTicket_prefix(), mTicket.getTicket_code())) {
                        updateSyncRequiredByFCM();
                    }
                }
                //
                if(bundle.getString(ConstantBaseApp.SW_TYPE).equals(ConstantBaseApp.TK_TICKET_FORM_GPS_LOCATION_UPDATE)) {
                    updateSyncRequiredByGpsService();
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        //Para receiver que ouve o FCM
        startStopFCMReceiver(false);
        //
        super.onDestroy();

    }

    //region WS Callbacks
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        //super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired);
        //
        if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Checkin.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.processCheckinReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), mLink);
        } else if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Download.class.getName())) {
            wsProcess = "";
            save_return = "";
            progressDialog.dismiss();
            if(!mPresenter.verifyProductForForm()){
                refreshUi();
            }
        } else if (wsProcess.equalsIgnoreCase(WS_Sync.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            if(save_return == null
            || save_return.isEmpty()) {
                refreshUi();
                StepForm stepForm = (StepForm) sources.get(lastPositionClicked);
                mPresenter.defineFormFlow(mTicket,stepForm);
                lastPositionClicked =-1;
            }else{
                mPresenter.processSaveReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), save_return);
                save_return = "";
            }
        } else if (wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            if(mPresenter.verifyProductForForm()){
                save_return = mLink;
            }else {
                mPresenter.processSaveReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), mLink);
            }
        } else if (wsProcess.equalsIgnoreCase(WS_Save.class.getName())) {
            wsProcess = "";
            progressDialog.dismiss();
            mPresenter.prepareSyncProcess(mTicket);
        }
        //
    }


    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
        //
        if (wsProcess.equalsIgnoreCase(WS_Sync.class.getName())) {
            if(save_return != null
                    && !save_return.isEmpty()) {
                mPresenter.processSaveReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), save_return);
                save_return = "";
            }
        }
        //LUCHE - 03/09/2020
        //Ao chamar o updateTicketData, é verificado se há necessidade sincronizar os dados com server
        //porem, se houve um erro, esse processo faz com que a tela entre em loop. Essa flag abaixo,
        //previne o loop
        preventSyncLoop = true;
        //LUCHE - 01/09/2020
        updateTicketData();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        progressDialog.dismiss();
        if (wsProcess.equalsIgnoreCase(WS_Sync.class.getName())) {
            if(save_return != null
                    && !save_return.isEmpty()) {
                mPresenter.processSaveReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), save_return);
                save_return = "";
            }
        }
        //LUCHE - 03/09/2020
        //Ao chamar o updateTicketData, é verificado se há necessidade sincronizar os dados com server
        //porem, se houve um erro, esse processo faz com que a tela entre em loop. Essa flag abaixo,
        //previne o loop
        preventSyncLoop = true;
        //LUCHE - 01/09/2020
        updateTicketData();
    }

    //TRATA SESSION_NOT_FOUND
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
    //endregion

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(hasFABActive){
            fabMenu.animateFAB();
        }else {
            mPresenter.onBackPressedClicked(requestingAct);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
