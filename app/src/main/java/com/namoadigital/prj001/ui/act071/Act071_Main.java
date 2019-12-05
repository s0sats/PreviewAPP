package com.namoadigital.prj001.ui.act071;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Act071_Main extends Base_Activity implements Act071_Main_Contract.I_View {

    private Act071_Main_Presenter mPresenter;
    private TextView tvTicketId;
    private TextView tvStatus;
    private TextView tvTypePath;
    private TextView tvTypeDesc;
    private TextView tvSeq;
    private ImageView ivExec;
    private TextView tvPartnerLbl;
    private MKEditTextNM mketPartner;
    private TextView tvPhotoLbl;
    private ImageView ivActionPhoto;
    private TextView tvCommentsLbl;
    private MKEditTextNM mketComments;
    private TextView tvDoneInfoLbl;
    private TextView tvDoneInfoVal;
    private Group grDone;
    private Bundle requestingBundle;
    private int mActionPrefix;
    private int mActionCode;
    private int mActionSeq;
    private String mTicketID;
    private String mTypePath;
    private String mTypeDesc;
    private TK_Ticket_Ctrl mTicketCtrl;
    private String requestingAct;
    private boolean bReadOnly;
    private boolean bDisableByCheckin;
    private View.OnClickListener execClickListener;
    private View.OnClickListener photoClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act071_main);

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
            Constant.ACT071
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act071_title");
        transList.add("partner_lbl");
        transList.add("photo_lbl");
        transList.add("done_info_lbl");
        transList.add("comments_lbl");
        transList.add("alert_action_parameter_error_ttl");
        transList.add("alert_action_parameter_error_msg");
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
        recoverIntentsInfo();
        //
        mPresenter = new Act071_Main_Presenter(
            context,
            this,
            hmAux_Trans
        );
        //
        //
        if (mPresenter.validateBundleParams(mActionPrefix, mActionCode, mActionSeq)) {
            updateActionData();
        } else {
            paramErrorFlow();
        }


    }


    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            requestingAct = requestingBundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT068);
            mActionPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mActionCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            mActionSeq = requestingBundle.getInt(TK_Ticket_CtrlDao.TICKET_SEQ, -1);
            mTicketID = requestingBundle.getString(TK_TicketDao.TICKET_ID, "");
            mTypePath = requestingBundle.getString(TK_TicketDao.TYPE_PATH, "");
            mTypeDesc = requestingBundle.getString(TK_TicketDao.TYPE_DESC, "");
            bDisableByCheckin = requestingBundle.getBoolean(Act070_Main.PARAM_DENIED_BY_CHECKIN,false);
            //
        } else {
            requestingAct = ConstantBaseApp.ACT070;
            mActionPrefix = -1;
            mActionCode = -1;
            mActionSeq = -1;
            mTicketID = "";
            mTypePath = "";
            mTypeDesc = "";
            bDisableByCheckin = false;
        }
    }

    private void bindViews() {
        tvTicketId = findViewById(R.id.act071_tv_ticket_id);
        tvStatus = findViewById(R.id.act071_tv_status);
        tvTypePath = findViewById(R.id.act071_tv_type_path);
        tvTypeDesc = findViewById(R.id.act071_tv_type_desc);
        tvSeq = findViewById(R.id.act071_tv_seq);
        ivExec = findViewById(R.id.act071_iv_exec);
        tvPartnerLbl = findViewById(R.id.act071_tv_partner_lbl);
        mketPartner = findViewById(R.id.act071_mket_partner);
        tvPhotoLbl = findViewById(R.id.act071_tv_photo_lbl);
        ivActionPhoto = findViewById(R.id.act071_iv_action_photo);
        tvCommentsLbl = findViewById(R.id.act071_tv_comment_lbl);
        mketComments = findViewById(R.id.act071_mket_comment_val);
        tvDoneInfoLbl = findViewById(R.id.act071_tv_done_info_lbl);
        tvDoneInfoVal = findViewById(R.id.act071_tv_done_info_val);
        grDone = findViewById(R.id.act071_gr_done);
        //
        setLabels();
    }

    private void setLabels() {
        tvPartnerLbl.setText(hmAux_Trans.get("partner_lbl"));
        tvPhotoLbl.setText(hmAux_Trans.get("photo_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        tvDoneInfoLbl.setText(hmAux_Trans.get("done_info_lbl"));
    }

    private void updateActionData() {
        mTicketCtrl = mPresenter.getTicketCtrlObj(mActionPrefix, mActionCode, mActionSeq);
        //
        if (mTicketCtrl != null) {
            setReadOnly();
            setDataToViews();
        } else {
            paramErrorFlow();
        }
    }

    private void setReadOnly() {
        bReadOnly = bDisableByCheckin ? bDisableByCheckin: mPresenter.getReadOnlyDefinition(mTicketCtrl);
        //
        if(bReadOnly) {
            applyReadOnly();
        }
        //
        defineExecListener();
        applyReadOnlyInPhoto();
    }

    private void applyReadOnly() {
        ivExec.setVisibility(View.INVISIBLE);
        mketPartner.setEnabled(false);
        mketComments.setEnabled(false);
    }

    private void defineExecListener() {
        if(bReadOnly){
            execClickListener = null;
        }else{
            execClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Executar",Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    private void applyReadOnlyInPhoto() {
        //boolean photoReadOnly = isPhotoReadOnly();
        //
        if(bReadOnly && (mTicketCtrl.getAction().getAction_photo() == null || mTicketCtrl.getAction().getAction_photo_local() == null)){
            if(mTicketCtrl.getAction().getAction_photo() == null){
                ivActionPhoto.setEnabled(false);
                defineActionPhotoListener(false);
            } else{
                defineActionPhotoListener(false);
            }
        }else{
            defineActionPhotoListener(true);
        }
    }

//    private boolean isPhotoReadOnly() {
//        return  mPresenter.isReadOnlyStatus(mTicketCtrl.getCtrl_status())
//                ||!mPresenter.hasActionExecProfile()
//                || !mPresenter.checkPartnerProfile(mTicketCtrl.getPartner_code()
//        );
//    }

    private void setDataToViews() {
        tvTicketId.setText(mTicketID);
        tvStatus.setText(hmAux_Trans.get(mTicketCtrl.getCtrl_status()));
        tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mTicketCtrl.getCtrl_status())));
        definePathVisibility();
        tvTypeDesc.setText(mTypeDesc);
        tvSeq.setText(String.valueOf(mTicketCtrl.getTicket_seq()));
        mketPartner.setText(mTicketCtrl.getPartner_desc());
        mketComments.setText(mTicketCtrl.getAction().getAction_comments());
        defineActionPhoto();
        defineDoneInfo();

    }

    private void definePathVisibility() {
        tvTypePath.setVisibility(View.GONE);
        //
        if (mTypePath != null && !mTypePath.isEmpty()) {
            tvTypePath.setVisibility(View.VISIBLE);
            tvTypePath.setText(mTypePath);
        }
    }

    private void defineActionPhoto() {
        if (mTicketCtrl.getAction().getAction_photo() == null && mTicketCtrl.getAction().getAction_photo_local() == null) {
            ivActionPhoto.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
        } else {
            if (mTicketCtrl.getAction().getAction_photo_local() == null) {
                //FOTO NÃO FOI BAIXADA, COMO FAZER?
                //INICIAR SERVICE DOWNLOAD E CRIAR HANDLER PARA DE X em X SEGUNDOS VERIFICAR SE SERVIÇO PAROU DE RODAR E SE PAROU TENTA RESETAR IMAGE?
                //CRIAR ASYNC_TAKS PARA DOWNLOAD?
                //USAR GLIDE PARA BAIXAR A IMAGEM SETANDO ELA NO PATH DEFINITIVO? NECESSARIO ATUALIZAR O BANCO DEPOIS...
                Glide.with(context)
                    .load(mTicketCtrl.getAction().getAction_photo())
                    .placeholder(R.drawable.sand_watch_transp)
                    .into(ivActionPhoto);
            } else {
                String path = ConstantBaseApp.CACHE_PATH_PHOTO + "/" + mTicketCtrl.getAction().getAction_photo_local();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ivActionPhoto.setImageBitmap(bitmap);
            }
        }
    }

    private void defineDoneInfo() {
        grDone.setVisibility(View.GONE);
        //
        if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(mTicketCtrl.getCtrl_status())) {
            grDone.setVisibility(View.VISIBLE);
            tvDoneInfoVal.setText(mPresenter.getFormattedDoneInfo(mTicketCtrl.getCtrl_end_date(),mTicketCtrl.getCtrl_end_user_name()));
        }
    }


    private void paramErrorFlow() {
        ToolBox.alertMSG(
            context,
            hmAux_Trans.get("alert_action_parameter_error_ttl"),
            hmAux_Trans.get("alert_action_parameter_error_msg"),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callAct070();
                }
            },
            0
        );

    }

    @Override
    public void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.remove(TK_Ticket_CtrlDao.TICKET_SEQ);
        requestingBundle.remove(TK_TicketDao.TICKET_ID);
        requestingBundle.remove(TK_TicketDao.TYPE_PATH);
        requestingBundle.remove(TK_TicketDao.TYPE_DESC);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void initAction() {
        ivActionPhoto.setOnClickListener(photoClickListener);
        //
        ivExec.setOnClickListener(execClickListener);
    }

    private void defineActionPhotoListener(boolean enableAction) {
        if(enableAction) {
            photoClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callCameraAct();
                }
            }
            ;
        }else{
            photoClickListener = null;
        }

    }

    private void callCameraAct() {
        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + mTicketCtrl.getAction().getAction_photo_local());
        if (!sFile.exists()) {
            return;
        }
        //
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantBase.PID, ivActionPhoto.getId());
        bundle.putInt(ConstantBase.PTYPE, 1);
        bundle.putString(ConstantBase.PPATH, mTicketCtrl.getAction().getAction_photo_local());
        bundle.putBoolean(ConstantBase.PEDIT, !bReadOnly);
        bundle.putBoolean(ConstantBase.PENABLED, !bReadOnly);
        bundle.putBoolean(ConstantBase.P_ALLOW_GALLERY, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false);
        //
        Intent mIntent = new Intent(context, Camera_Activity.class);
        mIntent.putExtras(bundle);
        //
        context.startActivity(mIntent);
    }

    //region UiFooter
    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT071;
        mAct_Title = Constant.ACT071 + "_" + "title";
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
