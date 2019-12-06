package com.namoadigital.prj001.ui.act070;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.Toolbar;
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
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
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

    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            requestingAct = requestingBundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            //
        } else {
            requestingAct = ConstantBaseApp.ACT069;
            mTkPrefix = -1;
            mTkCode = -1;
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
            setDataToViews();
        } else {
            paramErrorFlow();
        }
    }

    private void setReadOnly() {
        bReadOnly = mPresenter.getReadOnlyDefinition(mTicket);
    }

    private void setDataToViews() {
        tvTicketId.setText(mTicket.getTicket_id());
        //
        tvStatus.setText(hmAux_Trans.get(mTicket.getTicket_status()));
        tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mTicket.getTicket_status())));
        //
        tvTypePath.setText(mTicket.getType_path());
        tvTypeDesc.setText(mTicket.getType_desc());
        tvOpenComment.setText(mTicket.getOpen_comments());
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
            btnCheckIn.setVisibility( bReadOnly ? View.GONE: View.VISIBLE);
            clCheckinInfo.setVisibility(View.GONE);
        }
        //
        if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(mTicket.getTicket_status()) || bReadOnly) {
            ivCheckinCancel.setVisibility(View.GONE);
        } else {
            ivCheckinCancel.setVisibility(View.VISIBLE);
        }
    }

    private void defineOpenPhotoImage() {
        //Se status do ticket diferente de pending, reduz o tamanho da imagem
        if(!ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(mTicket.getTicket_status())){
            ViewGroup.LayoutParams layoutParams = ivOpenPhoto.getLayoutParams();
            //
            layoutParams.width = 250 ;
            layoutParams.height = 250;
            //
            ivOpenPhoto.setLayoutParams(layoutParams);
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
        if(ConstantBaseApp.ACT014.equals(requestingAct)) {
            bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, requestingAct);
        }
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

    }

    private void applyActionFilter(boolean isChecked) {
        for (TK_Ticket_Ctrl_Super ctrlSuper : actionList) {
            //
            ctrlSuper.setVisible(isChecked);
            if(isChecked){
                ctrlSuper.applyFilterVisibility();
            }else{
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
            },100
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
