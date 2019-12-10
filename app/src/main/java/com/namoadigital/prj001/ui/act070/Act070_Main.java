package com.namoadigital.prj001.ui.act070;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.service.WS_TK_Ticket_Checkin;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act035.Act035_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act070.view.TK_Ticket_Ctrl_Super;
import com.namoadigital.prj001.ui.act071.Act071_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Act070_Main extends Base_Activity implements Act070_Main_Contract.I_View {

    public static final String PARAM_DENIED_BY_CHECKIN = "PARAM_DENIED_BY_CHECKIN";

    private Act070_Main_Presenter mPresenter;
    private ScrollView svMain;
    private TextView tvTicketId;
    private TextView tvStatus;
    private TextView tvTypePath;
    private TextView tvTypeDesc;
    private TextView tvOpenComment;
    private TextView tvOpenDate;
    private TextView tvOpenDate_val;
    private TextView tvForecastDate;
    private TextView tvForecastDate_val;
    private TextView tvProduct;
    private TextView tvSerial;
    private ImageView ivInnerComment;
    private ImageView ivOpenPhoto;
    private Button btnCheckIn;
    private ConstraintLayout clCheckinInfo;
    private ImageView ivCheckinCancel;
    private TextView tvCheckinInfoLbl;
    private TextView tvCheckinInfoVal;
    private TextView tvDoneInfoLbl;
    private TextView tvDoneInfoVal;
    private Group grDone;
    private TextView tvFilterLbl;
    private Switch swFilter;
    private Group grFilter;
    private LinearLayout llActions;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    private TK_Ticket mTicket;
    private String requestingAct;
    private String wsProcess = "";
    private ArrayList<TK_Ticket_Ctrl_Super> actionList = new ArrayList<>();
    private boolean bReadOnly = false;
    private String room_code;
    private FCMReceiver fcmReceiver;

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
        transList.add("open_date_lbl");
        transList.add("forecast_date_lbl");
        transList.add("btn_checkin");
        transList.add("checkin_info_lbl");
        transList.add("done_info_lbl");
        transList.add("filter_lbl");
        transList.add("partner_lbl");
        transList.add("inner_comment_lbl");
        transList.add("alert_ticket_parameter_error_ttl");
        transList.add("alert_ticket_parameter_error_msg");
        //
        transList.add("ticket_lbl");
        transList.add("alert_cancel_checkin_ttl");
        transList.add("alert_cancel_checkin_confirm");
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
        transList.add("alert_ticket_checkin_offline_ttl");
        transList.add("alert_ticket_checkin_offline_msg");
        //
        transList.add("alert_sync_data_ttl");
        transList.add("alert_sync_data_msg");
        transList.add("dialog_download_ticket_ttl");
        transList.add("dialog_download_ticket_start");
        //
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
        if (mPresenter.validateBundleParams(mTkPrefix, mTkCode)) {
            updateTicketData();
        } else {
            paramErrorFlow();
        }
    }

    private void refreshUi() {
        resetActionList();
        //
        updateTicketData();
    }

    @Override
    public void callRefreshUi() {
        refreshUi();
    }

    private void resetActionList() {
        llActions.removeAllViews();
        actionList = new ArrayList<>();
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
            //
        } else {
            requestingAct = ConstantBaseApp.ACT069;
            mTkPrefix = -1;
            mTkCode = -1;
            room_code = null;
        }
    }

    private void bindViews() {
        svMain = findViewById(R.id.act070_sv_main);
        tvTicketId = findViewById(R.id.act070_tv_ticket_id);
        tvStatus = findViewById(R.id.act070_tv_status);
        tvTypePath = findViewById(R.id.act070_tv_type_path);
        tvTypeDesc = findViewById(R.id.act070_tv_type_desc);
        tvOpenComment = findViewById(R.id.act070_tv_open_comment);
        tvOpenDate = findViewById(R.id.act070_tv_open_date);
        tvOpenDate_val = findViewById(R.id.act070_tv_open_date_val);
        tvForecastDate = findViewById(R.id.act070_tv_forecast_date);
        tvForecastDate_val = findViewById(R.id.act070_tv_forecast_date_val);
        tvProduct = findViewById(R.id.act070_tv_product);
        tvSerial = findViewById(R.id.act070_tv_serial);
        ivInnerComment = findViewById(R.id.act070_iv_inner_comment);
        ivOpenPhoto = findViewById(R.id.act070_iv_open_photo);
        btnCheckIn = findViewById(R.id.act070_btn_check_in);
        clCheckinInfo = findViewById(R.id.act070_cl_checkin_info);
        ivCheckinCancel = findViewById(R.id.act070_iv_checkin_cancel);
        tvCheckinInfoLbl = findViewById(R.id.act070_tv_checkin_info_lbl);
        tvCheckinInfoVal = findViewById(R.id.act070_tv_checkin_info_val);
        tvDoneInfoLbl = findViewById(R.id.act070_tv_done_info_lbl);
        tvDoneInfoVal = findViewById(R.id.act070_tv_done_info_val);
        grDone = findViewById(R.id.act070_gr_done);
        tvFilterLbl = findViewById(R.id.act070_tv_filter_lbl);
        swFilter = findViewById(R.id.act070_sw_filter);
        grFilter = findViewById(R.id.act070_gr_filter);
        llActions = findViewById(R.id.act070_ll_actions);
        //
        setTranslation();
    }

    private void setTranslation() {
        tvOpenDate.setText(hmAux_Trans.get("open_date_lbl"));
        tvForecastDate.setText(hmAux_Trans.get("forecast_date_lbl"));
        btnCheckIn.setText(hmAux_Trans.get("btn_checkin"));
        tvCheckinInfoLbl.setText(hmAux_Trans.get("checkin_info_lbl"));
        tvDoneInfoLbl.setText(hmAux_Trans.get("done_info_lbl"));
        tvFilterLbl.setText(hmAux_Trans.get("filter_lbl"));
    }

    private void updateTicketData() {
        mTicket = mPresenter.getTicketObj(mTkPrefix, mTkCode);
        //
        if (mTicket != null) {
            setReadOnly();
            initFCMReceiver();
            setDataToViews();
            checkSyncNeeds();
        } else {
            paramErrorFlow();
        }
    }

    private void checkSyncNeeds() {
         if(mPresenter.checkOnlySyncNeeds(mTicket) && ToolBox_Con.isOnline(context)){
            mPresenter.prepareSyncProcess(mTicket);
        }
    }

    @Override
    public void updateSyncRequiredByFCM() {
        mTicket.setSync_required(1);
        //
        setTicketSync();
    }

    private void initFCMReceiver() {
        fcmReceiver = new FCMReceiver();
        //
        startStopFCMReceiver(true);
    }

    private void startStopFCMReceiver(boolean start) {
        if(start){
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConstantBaseApp.WS_FCM);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            LocalBroadcastManager.getInstance(this).registerReceiver(fcmReceiver, filter);
        }else{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver);
        }
    }
    private void setReadOnly() {
        bReadOnly = mPresenter.getReadOnlyDefinition(mTicket);
    }

    private void setDataToViews() {
        tvTicketId.setText(mTicket.getTicket_id());
        setTicketSync();
        //
        tvStatus.setText(hmAux_Trans.get(mTicket.getTicket_status()));
        tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mTicket.getTicket_status())));
        //
        tvTypePath.setText(mTicket.getType_path());
        tvTypeDesc.setText(mTicket.getType_desc());
        defineOpenComment();
        tvOpenDate_val.setText(
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(mTicket.getOpen_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            )
        );
        tvForecastDate_val.setText(
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(mTicket.getForecast_date()),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            )
        );
        //
        tvProduct.setText(mTicket.getCurrent_product_desc());
        tvSerial.setText(mTicket.getCurrent_serial_id());
        defineInnerCommentIcon();
        defineOpenPhotoImage();
        configCheckinInfos();
        configDoneInfo();
        defineFilterVisility();
        loadActionList();
    }

    private void defineOpenComment() {
        if(mTicket.getOpen_comments() != null && !mTicket.getOpen_comments().isEmpty()){
            tvOpenComment.setVisibility(View.VISIBLE);
            tvOpenComment.setText(mTicket.getOpen_comments());
        }else{
            tvOpenComment.setVisibility(View.GONE);
        }

    }

    private void setTicketSync() {
        if(mTicket != null){
            Drawable rightDraw = null;
            Drawable background = getResources().getDrawable(R.drawable.stroke_blue2_states);
            if(mTicket.getUpdate_required() == 1 || mTicket.getCheckin_required() == 1 || mTicket.getSync_required() == 1){
                rightDraw = getResources().getDrawable(R.drawable.ic_sync_black_24dp);
                rightDraw.setColorFilter(getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
                background = getResources().getDrawable(R.drawable.stroke_yellow_states);
            }
            tvTicketId.setCompoundDrawablesWithIntrinsicBounds(null,null,rightDraw,null);
            tvTicketId.setBackground(background);
        }
    }


    private void loadActionList() {
        actionList = mPresenter.generateCtrlActions(
            mTicket,
            llActions,
            swFilter.isChecked()
        );
    }

    private void defineFilterVisility() {
        if (
            ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(mTicket.getTicket_status())
                || mPresenter.checkFilterDisable(mTicket.getCtrl())
        ) {
            swFilter.setChecked(false);
            grFilter.setVisibility(View.GONE);
        } else {
            grFilter.setVisibility(View.VISIBLE);
        }
    }

    private void configDoneInfo() {
        if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(mTicket.getTicket_status())
            && mTicket.getClose_date() != null && mTicket.getClose_user() != null) {
            grDone.setVisibility(View.VISIBLE);
            tvDoneInfoVal.setText(mPresenter.getFormattedDoneInfo(mTicket.getClose_date(), mTicket.getClose_user_name()));
        } else {
            grDone.setVisibility(View.GONE);
        }
    }

    private void configCheckinInfos() {
        if (mTicket.getCheckin_date() != null && mTicket.getCheckin_user() != null) {
            btnCheckIn.setVisibility(View.GONE);
            clCheckinInfo.setVisibility(View.VISIBLE);
            tvCheckinInfoVal.setText(mPresenter.getFormattedCheckinInfo(mTicket.getCheckin_date(), mTicket.getCheckin_user_name()));
        } else {
            btnCheckIn.setVisibility(bReadOnly ? View.GONE : View.VISIBLE);
            clCheckinInfo.setVisibility(View.GONE);
        }
        //
        if (mPresenter.hideCancelCheckin(mTicket)) {
            ivCheckinCancel.setVisibility(View.GONE);
        } else {
            ivCheckinCancel.setVisibility(View.VISIBLE);
        }
    }

    private void defineOpenPhotoImage() {
        //Se status do ticket diferente de pending, reduz o tamanho da imagem
        if (!ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(mTicket.getTicket_status())) {
            ViewGroup.LayoutParams layoutParams = ivOpenPhoto.getLayoutParams();
            //
            layoutParams.width = 250;
            layoutParams.height = 250;
            //
            ivOpenPhoto.setLayoutParams(layoutParams);
        }else{
            ViewGroup.LayoutParams layoutParams = ivOpenPhoto.getLayoutParams();
            //
            int[] percentMetrics = getPercentageWidthAndHeight(context,0.8,0.3);
            layoutParams.width = percentMetrics[0];
            layoutParams.height = percentMetrics[1];
            //
            ivOpenPhoto.setLayoutParams(layoutParams);
            //ivOpenPhoto.requestLayout();
        }
        //
        if (mTicket.getOpen_photo() == null && mTicket.getOpen_photo_local() == null) {
            ivOpenPhoto.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
            //VERIFICAR REGRA - Segundo excel, se não tem imagem, escode view.
            ivOpenPhoto.setVisibility(View.GONE);
        } else {
            if (mTicket.getOpen_photo_local() == null) {
                //FOTO NÃO FOI BAIXADA, COMO FAZER?
                //INICIAR SERVICE DOWNLOAD E CRIAR HANDLER PARA DE X em X SEGUNDOS VERIFICAR SE SERVIÇO PAROU DE RODAR E SE PAROU TENTA RESETAR IMAGE?
                //CRIAR ASYNC_TAKS PARA DOWNLOAD?
                //USAR GLIDE PARA BAIXAR A IMAGEM SETANDO ELA NO PATH DEFINITIVO? NECESSARIO ATUALIZAR O BANCO DEPOIS...
                Glide.with(context)
                    .load(mTicket.getOpen_photo())
                    .placeholder(R.drawable.sand_watch_transp)
                    .into(ivOpenPhoto);
            } else {
                String path = ConstantBaseApp.CACHE_PATH_PHOTO + "/" + mTicket.getOpen_photo_local();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ivOpenPhoto.setImageBitmap(bitmap);
            }
        }
    }

    /**
     *
     * MOVER METODOS PARA O TOOLBOX?
     */
    private int[] getPercentageWidthAndHeight(Context context, double wPercent, double hPercent) {
        int[] percentMetrics = getScreenMetrics(context);
        percentMetrics[0] = (int) (percentMetrics[0] * wPercent);
        percentMetrics[1] = (int) (percentMetrics[1] * hPercent);
        return percentMetrics;
    }

    public int[] getScreenMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] metrics = new int[2];
        metrics[0] = displayMetrics.widthPixels;
        metrics[1] = displayMetrics.heightPixels;
        return metrics;
    }

    private void defineInnerCommentIcon() {
        if (mTicket.getInternal_comments() != null && mTicket.getInternal_comments().trim().length() > 0) {
            ivInnerComment.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_msg_on));
        } else {
            ivInnerComment.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_msg_off));
        }
    }

    private void paramErrorFlow() {
        ToolBox.alertMSG(
            context,
            hmAux_Trans.get("alert_ticket_parameter_error_ttl"),
            hmAux_Trans.get("alert_ticket_parameter_error_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callAct069();
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

    private void showAlert(String ttl, String msg, DialogInterface.OnClickListener listenerOk, boolean showNegative) {
        ToolBox.alertMSG_YES_NO(
            context,
            ttl,
            msg,
            listenerOk,
            showNegative ? 1 : 0
        );
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
        if(ConstantBaseApp.ACT012.equals(requestingAct)
        || ConstantBaseApp.ACT014.equals(requestingAct)
        || ConstantBaseApp.ACT035.equals(requestingAct)) {
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestingAct);
            if(ConstantBaseApp.ACT035.equals(requestingAct)){
                bundle.putString(CH_RoomDao.ROOM_CODE, room_code);
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
        tvTicketId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans.get("alert_sync_data_ttl"),
                    hmAux_Trans.get("alert_sync_data_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.prepareSyncProcess(mTicket);
                        }
                    },
                    1
                );
            }
        });
        //
        ivInnerComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTicket.getInternal_comments() != null && mTicket.getInternal_comments().trim().length() > 0) {
                    showInnerCommetDialog();
                }
            }
        });
        //
        ivOpenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCameraAct();
            }
        });
        //
        swFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                applyActionFilter(isChecked);
            }
        });
        //
        ivCheckinCancel.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mPresenter.hideCancelCheckin(mTicket)) {
                        showAlert(
                            hmAux_Trans.get("alert_cancel_checkin_ttl"),
                            hmAux_Trans.get("alert_cancel_checkin_confirm"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.executeCheckin(mTicket, false);
                                }
                            },
                            true
                        );
                    }
                }
            }
        );
        //
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bReadOnly) {
                    showAlert(
                        hmAux_Trans.get("alert_start_checkin_ttl"),
                        hmAux_Trans.get("alert_start_checkin_confirm"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Seta dados do checkin
                                setCheckinToObj();
                                //Tenta update no banco, se ok, vai pra WS, se não limpa os dados do checkin
                                if(mPresenter.setCheckInData(mTicket)) {
                                    mPresenter.executeCheckin(mTicket, true);
                                }else{
                                    resetCheckinInObj();
                                    //
                                    showAlert(
                                        hmAux_Trans.get("alert_error_on_checkin_ttl"),
                                        hmAux_Trans.get("alert_error_on_checkin_msg")
                                    );
                                }
                            }
                        },
                        true
                    );

                }
            }
        });

    }

    private void resetCheckinInObj() {
        mTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_PENDING);
        mTicket.setCheckin_required(0);
        mTicket.setCheckin_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        mTicket.setCheckin_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
        mTicket.setCheckin_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
    }

    private void setCheckinToObj() {
        mTicket.setTicket_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
        mTicket.setCheckin_required(1);
        mTicket.setCheckin_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        mTicket.setCheckin_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
        mTicket.setCheckin_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
    }

    private void applyActionFilter(boolean isChecked) {
        for (TK_Ticket_Ctrl_Super ctrlSuper : actionList) {
            //
            ctrlSuper.setVisible(isChecked);
            if (isChecked) {
                ctrlSuper.applyFilterVisibility();
            } else {
                ctrlSuper.setVisible(true);
            }
        }
        //Faz scroll para o fim do scroll
        new Handler().postDelayed(
            new Runnable() {
                @Override
                public void run() {
                    svMain.fullScroll(View.FOCUS_DOWN);
                }
            }, 100
        );
    }

    private void callCameraAct() {
        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + mTicket.getOpen_photo_local());
        if (!sFile.exists()) {
            return;
        }
        //
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantBase.PID, ivOpenPhoto.getId());
        bundle.putInt(ConstantBase.PTYPE, 1);
        bundle.putString(ConstantBase.PPATH, mTicket.getOpen_photo_local());
        bundle.putBoolean(ConstantBase.PEDIT, false);
        bundle.putBoolean(ConstantBase.PENABLED, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_GALLERY, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false);
        //
        Intent mIntent = new Intent(context, Camera_Activity.class);
        mIntent.putExtras(bundle);
        //
        context.startActivity(mIntent);
    }

    private void showInnerCommetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, com.namoa_digital.namoa_library.R.style.AlertDialogTheme);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View cView = (View) layoutInflater.inflate(com.namoa_digital.namoa_library.R.layout.dots_dialog_comments, null);
        final EditText etComments = cView.findViewById(com.namoa_digital.namoa_library.R.id.dots_dialog_comments_tv_comments);
        etComments.setText(mTicket.getInternal_comments());
        etComments.setEnabled(false);
        etComments.setFocusable(false);
        builder
            .setTitle(hmAux_Trans.get("inner_comment_lbl"))
            .setView(cView)
            .setCancelable(false)
            .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), null);
        //
        builder.create().show();
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
            if( bundle != null
                && bundle.containsKey(ConstantBaseApp.SW_TYPE)
                && bundle.getString(ConstantBaseApp.SW_TYPE).equals(ConstantBaseApp.FCM_ACTION_TK_TICKET_UPDATE)
            ){
                //
                if(mPresenter.checkSyncRequireNeedsChange(mTicket.getTicket_prefix(),mTicket.getTicket_code())) {
                    updateSyncRequiredByFCM();
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
            mPresenter.processCheckinReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), mLink);

        }else if(wsProcess.equalsIgnoreCase(WS_TK_Ticket_Download.class.getName())){
            wsProcess = "";
            refreshUi();

        }else if(wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())){
            wsProcess = "";
            mPresenter.processSaveReturn(mTicket.getTicket_prefix(), mTicket.getTicket_code(), mLink);
        }
        //
        progressDialog.dismiss();
    }


    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        progressDialog.dismiss();
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
        mPresenter.onBackPressedClicked(requestingAct);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
