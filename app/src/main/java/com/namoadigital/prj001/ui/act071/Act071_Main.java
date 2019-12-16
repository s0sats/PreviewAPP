package com.namoadigital.prj001.ui.act071;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Act071_Main extends Base_Activity implements Act071_Main_Contract.I_View {

    public static final String TEMP_SUFIX_FILE = "temp-";
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
    private String actionPhotoLocalPath ="";
    private String wsProcess ="";
    private long previousLenght =0;
    private TextInputLayout tilComment;
    //flag criada para controle do metodo que coloca imagem na tela
    private boolean fromCamera = false;
    private boolean hasImageFileChanged=false;

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
        transList.add("alert_unsaved_data_will_be_lost_ttl");
        transList.add("alert_unsaved_data_will_be_lost_msg");
        transList.add("alert_ticket_ttl");
        transList.add("alert_ticket_save_ttl");
        transList.add("alert_ticket_save_success_msg");
        transList.add("dialog_ticket_save_ttl");
        transList.add("dialog_ticket_save_start");
        transList.add("alert_offline_save_ttl");
        transList.add("alert_offline_save_msg");
        transList.add("ticket_lbl");
        transList.add("alert_none_ticket_returned_ttl");
        transList.add("alert_none_ticket_returned_msg");
        transList.add("alert_finalize_action_ttl");
        transList.add("alert_finalize_action_msg");
        //
        transList.add("alert_error_on_offline_done_ttl");
        transList.add("alert_error_on_offline_done_msg");
        transList.add("alert_image_too_large_to_open_ttl");
        transList.add("alert_image_too_large_to_open_msg");
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
        tilComment = findViewById(R.id.act071_til_comment);
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
        mketComments.setEnabled(false);
    }

    private void defineExecListener() {
        if(bReadOnly){
            execClickListener = null;
        }else{
            execClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToolBox.alertMSG_YES_NO(
                        context,
                        hmAux_Trans.get("alert_finalize_action_ttl"),
                        hmAux_Trans.get("alert_finalize_action_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                    BARRIONUEVO - 11.12.2019
                                    Passa alterações da imagem para arquivo oficial.
                                 */
                                try {
                                    File photo = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
                                    long currentLength = photo.length();
                                    if (currentLength != previousLenght) {
                                        hasImageFileChanged = true;
                                    }
                                    if(photo.exists() && hasImageFileChanged) {
                                        copyFiles(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath,
                                                ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                                        photo.delete();
                                    }else{
                                        if(mPresenter.fileExists(actionPhotoLocalPath)) {
                                            File originalPhoto = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                                            originalPhoto.delete();
                                        }
                                    }
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    ToolBox_Inf.registerException(e);
                                }

                                setDataToObj();
                                if(mPresenter.updateTicketAction(mTicketCtrl)){
                                    mPresenter.execTicketSave();
                                }
                            }
                        },
                        1
                    );
                }
            };
        }
    }

    private void copyFiles(String fromFile, String toFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        File tempImageFile = new File(toFile);
        saveBitmapToFile(bitmap, tempImageFile);
    }

    private void saveBitmapToFile(Bitmap bitmap, File tempImageFile) {
        try (FileOutputStream out = new FileOutputStream(tempImageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restoreActionImage() {
        try{
            Bitmap bitmap = BitmapFactory.decodeFile(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
            ivActionPhoto.setImageBitmap(bitmap);
        } catch (NullPointerException e ){
            e.printStackTrace();
        }
    }

    private void setDataToObj() {
        mTicketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
        mTicketCtrl.getAction().setAction_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
        mTicketCtrl.setCtrl_end_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
        mTicketCtrl.setCtrl_end_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
        mTicketCtrl.getAction().setAction_comments(
            mketComments.getText().toString()
        );
        //
        if(mPresenter.fileExists(actionPhotoLocalPath)) {
            mTicketCtrl.getAction().setAction_photo_local(actionPhotoLocalPath);
        }else{
            mTicketCtrl.getAction().setAction_photo_local(null);
        }
        //
        mTicketCtrl.setCtrl_end_date(
            ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        );
        //
        if (hasImageFileChanged) {
            mTicketCtrl.getAction().setAction_photo_changed(1);
            //Se teve alteração e photo_local null, significa q apagou a
            //foto que veio do server.Nesse caso reseta photo e photo_name
            //assim impede que a rotina de download baixe a imagem
            //e ferre o envio
            if(mTicketCtrl.getAction().getAction_photo_local() == null){
                mTicketCtrl.getAction().setAction_photo(null);
                mTicketCtrl.getAction().setAction_photo_name(null);
                deletePhotoFile(actionPhotoLocalPath);
            }
        }else {
            mTicketCtrl.getAction().setAction_photo_changed(0);
        }
    }

    private void deletePhotoFile(String filename) {
        if(mPresenter.fileExists(filename)){
            try {
                File file = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + filename);
                if(file != null && file.exists()) {
                    file.delete();
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
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
    public void showAlert(String ttl, String msg, DialogInterface.OnClickListener onClickListener) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            onClickListener,
            0
        );
    }

    @Override
    public void showResult(ArrayList<HMAux> resultList, final boolean ticketResult) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        TextView tv_title = view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = view.findViewById(R.id.act028_dialog_btn_ok);
        //trad
        tv_title.setText(hmAux_Trans.get("alert_ticket_ttl"));
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
                if(ticketResult){
                    mPresenter.definePostTicketSaveFlow(
                        mTicketCtrl.getTicket_prefix(),
                        mTicketCtrl.getTicket_code()
                    );
                }else{
                    updateActionData();
                }
                //
                show.dismiss();
            }
        });
    }

    @Override
    public void postTicketSave() {
        updateActionData();
        //
        mPresenter.definePostTicketSaveFlow(
            mTicketCtrl.getTicket_prefix(),
            mTicketCtrl.getTicket_code()
        );
//        //
//        showAlert(
//            hmAux_Trans.get("alert_ticket_save_ttl"),
//            hmAux_Trans.get("alert_ticket_save_success_msg"),
//            new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //updateActionData();
//                    mPresenter.definePostTicketSaveFlow(
//                        mTicketCtrl.getTicket_prefix(),
//                        mTicketCtrl.getTicket_code()
//                    );
//                }
//            }
//        );
    }

    private void applyReadOnlyInPhoto() {
        //boolean photoReadOnly = isPhotoReadOnly();
        //
        if(bReadOnly){
            if(mTicketCtrl.getAction().getAction_photo() == null && mTicketCtrl.getAction().getAction_photo_local() == null){
                ivActionPhoto.setEnabled(false);
                defineActionPhotoListener(false);
            } else{
                defineActionPhotoListener(true);
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
        definePartner();
        defineComments();
        defineActionPhotoMetrics();
        defineActionPhoto();
        defineDoneInfo();

    }

    private void definePartner() {
        mketPartner.setEnabled(false);
        //
        if(mTicketCtrl.getPartner_desc() != null && !mTicketCtrl.getPartner_desc().isEmpty()) {
            mketPartner.setText(mTicketCtrl.getPartner_desc());
            tvPartnerLbl.setVisibility(View.VISIBLE);
            mketPartner.setVisibility(View.VISIBLE);
        }else{
            tvPartnerLbl.setVisibility(View.GONE);
            mketPartner.setVisibility(View.GONE);
        }
    }

    private void defineComments() {
        //Remove counter
        tilComment.setCounterEnabled(false);
        mketComments.setText(mTicketCtrl.getAction().getAction_comments());
        mketComments.setTag(mTicketCtrl.getAction().getAction_comments());
        //Reabilita counter para atualizar contador
        tilComment.setCounterEnabled(true);
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
        actionPhotoLocalPath = mPresenter.generateActionPhotoLocalPath(mTicketCtrl.getAction());
        //
        if (mTicketCtrl.getAction().getAction_photo() == null && mTicketCtrl.getAction().getAction_photo_local() == null) {
            if(!bReadOnly) {
                deleteTempFile(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
            }else{
                ivActionPhoto.setEnabled(false);
            }
            ivActionPhoto.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
        } else {
            String path = mTicketCtrl.getAction().getAction_photo();
            boolean saveBitmap = false;
            //
            if (mTicketCtrl.getAction().getAction_photo_local() != null) {
                path = ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath;
                ivActionPhoto.setEnabled(true);
                //
                if(!bReadOnly) {
                    path = ConstantBaseApp.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath;
                    //Passa dados para arquivo temporario, mudanca feita para restauracao de info original
                    final File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
                    copyFiles(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath, path);
                    previousLenght = sFile.length();
                }

            }else{
                saveBitmap = !bReadOnly;
            }
            //
            final boolean finalSaveBitmap = saveBitmap;
            final String finalPath = path;
            //
            Glide.with(context).asBitmap()
                .placeholder(R.drawable.sand_watch_transp)
                .load(path)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ivActionPhoto.setEnabled(true);
                        //ivActionPhoto.setImageBitmap(resource);
                        //
                        if(finalSaveBitmap){
                            final File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
                            saveBitmapToFile(resource, sFile);
                            previousLenght = sFile.length();
                        }
                        //
                        Glide.with(context).load(resource).into(ivActionPhoto);
                        if(photoClickListener != null && !ToolBox_Inf.isImageUnder4kLimit(finalPath)){
                            ivActionPhoto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showAlert(
                                        hmAux_Trans.get("alert_image_too_large_to_open_ttl"),
                                        hmAux_Trans.get("alert_image_too_large_to_open_msg"),
                                        null
                                    );
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        ivActionPhoto.setImageDrawable(placeholder);
                        ivActionPhoto.setEnabled(false);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });

//            if (mTicketCtrl.getAction().getAction_photo_local() == null) {
//                //FOTO NÃO FOI BAIXADA, COMO FAZER?
//                //INICIAR SERVICE DOWNLOAD E CRIAR HANDLER PARA DE X em X SEGUNDOS VERIFICAR SE SERVIÇO PAROU DE RODAR E SE PAROU TENTA RESETAR IMAGE?
//                //CRIAR ASYNC_TAKS PARA DOWNLOAD?
//                //USAR GLIDE PARA BAIXAR A IMAGEM SETANDO ELA NO PATH DEFINITIVO? NECESSARIO ATUALIZAR O BANCO DEPOIS...
////                ivActionPhoto.setImageDrawable(getResources().getDrawable(R.drawable.sand_watch_transp));
//                Glide.with(context).asBitmap()
//                    .placeholder(R.drawable.sand_watch_transp)
//                    .load(mTicketCtrl.getAction().getAction_photo())
//                    .into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            ivActionPhoto.setEnabled(true);
//                            ivActionPhoto.setImageBitmap(resource);
//                            if(!bReadOnly) {
//                                final File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
//                                saveBitmapToFile(resource, sFile);
//                                previousLenght = sFile.length();
//                            }
//                        }
//
//                        @Override
//                        public void onLoadStarted(@Nullable Drawable placeholder) {
//                            super.onLoadStarted(placeholder);
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//                            ivActionPhoto.setImageDrawable(placeholder);
//                            ivActionPhoto.setEnabled(false);
//                        }
//
//                        @Override
//                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                            super.onLoadFailed(errorDrawable);
//                        }
//                    });
//
//            } else {
//                ivActionPhoto.setEnabled(true);
//                if(!bReadOnly) {
//                    //Passa dados para arquivo temporario, mudanca feita para restauracao de info original
//                    final File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
//                    String tempPath = ConstantBaseApp.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath;
//                    copyFiles(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath, tempPath);
//                    previousLenght = sFile.length();
//                    Bitmap bitmap = BitmapFactory.decodeFile(tempPath);
//                    ivActionPhoto.setImageBitmap(bitmap);
//                }else{
//                    String path = ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath;
//                    Bitmap bitmap = BitmapFactory.decodeFile(path);
//                    ivActionPhoto.setImageBitmap(bitmap);
//                }
//            }
        }
    }

    private void deleteTempFile(String path) {
        final File sFile = new File(path);
        if (sFile != null && sFile.exists()) {
            sFile.delete();
        }
    }

    private void defineActionPhotoMetrics() {
        int[] percentMetrics = ToolBox_Inf.getPercentageWidthAndHeight(context,0.8,0.3);
        ivActionPhoto.getLayoutParams().width = percentMetrics[0];
        ivActionPhoto.getLayoutParams().height = percentMetrics[1];
    }

    private void setActinPhotoToView(String pathSufix) {
        if(mPresenter.fileExists(pathSufix)) {
            String path = ConstantBaseApp.CACHE_PATH_PHOTO + "/" + pathSufix;
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivActionPhoto.setImageBitmap(bitmap);
        }else {

            ivActionPhoto.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
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
                    fromCamera = true;
                    callCameraAct();
                }
            }
            ;
        }else{
            photoClickListener = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        if(fromCamera) {
            fromCamera = false;
            updateActionPhotoReference();
        }
    }

    private void updateActionPhotoReference() {
        if(bReadOnly) {
            setActinPhotoToView(actionPhotoLocalPath);
        }else{
            setActinPhotoToView(TEMP_SUFIX_FILE + actionPhotoLocalPath);
        }
    }

    @Override
    public TK_Ticket_Action getAction() {
        return mTicketCtrl.getAction();
    }

    private void callCameraAct() {
        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + mTicketCtrl.getAction().getAction_photo_local());
        if (!sFile.exists() && bReadOnly) {
            return;
        }
        //
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantBase.PID, ivActionPhoto.getId());
        bundle.putInt(ConstantBase.PTYPE, 1);
        //bundle.putString(ConstantBase.PPATH, mTicketCtrl.getAction().getAction_photo_local());
        /*
            BARRIONUEVO -  11.12.2019
            Criacao de arquivo temporario para restaurar imagem original caso user saia da
            activity sem confirmar as alteracoes.
        */
        if(bReadOnly) {
            bundle.putString(ConstantBase.PPATH, actionPhotoLocalPath);
        }else {
            bundle.putString(ConstantBase.PPATH, TEMP_SUFIX_FILE + actionPhotoLocalPath);
        }
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

    @Override
    public boolean hasUnsavedData() {
        //Comentario vindo do banco
        String commentTag =  mketComments.getTag() == null ? "":String.valueOf(mketComments.getTag());
        //Comentario capturado da tela
        String commentText = mketComments.getText().toString();
        //
        File photo = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE +actionPhotoLocalPath);
        try {
            long currentLength = photo.length();
            if (currentLength != previousLenght) {
                return true;
            }
        }catch (NullPointerException e){
            if (previousLenght > 0){
               return true;
            }
        }
        if(!commentText.equalsIgnoreCase(commentTag)){
            return true;
        }
        return false;
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
    public void callAct069() {
        Intent intent = new Intent(context, Act069_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
        if(wsProcess.equalsIgnoreCase(WS_TK_Ticket_Save.class.getName())){
            wsProcess = "";
            mPresenter.processSaveReturn(mTicketCtrl.getTicket_prefix(), mTicketCtrl.getTicket_code(), mLink);
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
        //Atualiza UI
        updateActionData();
        //
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
        deletePhotoFile(TEMP_SUFIX_FILE + actionPhotoLocalPath);
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
