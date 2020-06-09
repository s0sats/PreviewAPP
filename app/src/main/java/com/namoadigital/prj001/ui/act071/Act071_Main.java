package com.namoadigital.prj001.ui.act071;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Act071_Main extends Base_Activity implements Act071_Main_Contract.I_View {

    public static final String TEMP_SUFIX_FILE = "temp-";
    private final double IV_PHOTO_EXISTS_WIDTH_PERCENT = 0.8;
    private final double IV_PHOTO_EXISTS_HEIGHT_PERCENT = 0.3;
    private final double IV_PHOTO_NOT_EXISTS_WIDTH_PERCENT = 0.8;
    private final double IV_PHOTO_NOT_EXISTS_HEIGHT_PERCENT = 0.13;

    private Act071_Main_Presenter mPresenter;
    private ScrollView svMain;
    private TextView tvTicketId;
    private TextView tvStatus;
    private TextView tvSerialId;
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
    private TextView tvCheckinNeeded;
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
    private String actionPhotoLocalPath = "";
    private String wsProcess = "";
    private long previousLenght = 0;
    private TextInputLayout tilComment;
    //flag criada para controle do metodo que coloca imagem na tela
    private boolean fromCamera = false;
    private boolean hasImageFileChanged = false;
    //LUCHE - 12/03/2020
    private int mSchedulePrefix;
    private int mScheduleCode;
    private int mScheduleExec;
    private boolean mIsSchedule;

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
        transList.add("checkin_info_lbl");
        transList.add("checkin_needed_alert_lbl");
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
        transList.add("alert_error_on_save_photo_ttl");
        transList.add("alert_error_on_save_photo_msg");
        transList.add("alert_image_too_large_to_open_ttl");
        transList.add("alert_image_too_large_to_open_msg");
        //
        transList.add("seq_lbl");
        transList.add("alert_schedule_cancelled_by_server_ttl");
        transList.add("alert_schedule_cancelled_by_server_msg");
        transList.add("alert_schedule_warning_new_status_lbl");
        transList.add("alert_warning_user_nick_lbl");
        transList.add("alert_error_on_cancel_schedule_ttl");
        transList.add("alert_error_on_cancel_schedule_msg");
        transList.add("alert_error_ticket_not_found_msg");
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
        if (mPresenter.validateBundleParams(mActionPrefix, mActionCode, mActionSeq, mSchedulePrefix, mScheduleCode, mScheduleExec)) {
            updateActionData();
            //
            if(mIsSchedule && mPresenter.isScheduleAbortProcess(mSchedulePrefix, mScheduleCode, mScheduleExec)){
                svMain.setVisibility(View.INVISIBLE);
                mPresenter.showScheduleCancelMsg(mSchedulePrefix,mScheduleCode,mScheduleExec);
            }
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
            //LUCHE - 12/03/2020
            mSchedulePrefix = requestingBundle.getInt(TK_TicketDao.SCHEDULE_PREFIX, -1);
            mScheduleCode = requestingBundle.getInt(TK_TicketDao.SCHEDULE_CODE, -1);
            mScheduleExec = requestingBundle.getInt(TK_TicketDao.SCHEDULE_EXEC, -1);
            mIsSchedule = defineIsScheduleAttr();
        } else {
            requestingAct = ConstantBaseApp.ACT070;
            mActionPrefix = -1;
            mActionCode = -1;
            mActionSeq = -1;
            mTicketID = "";
            mTypePath = "";
            mTypeDesc = "";
            bDisableByCheckin = false;
            mSchedulePrefix = -1;
            mScheduleCode = -1;
            mScheduleExec = -1;
            mIsSchedule = false;
        }
    }

    private void bindViews() {
        svMain = findViewById(R.id.act071_sv_main);
        tvTicketId = findViewById(R.id.act071_tv_ticket_id);
        tvStatus = findViewById(R.id.act071_tv_status);
        tvSerialId = findViewById(R.id.act071_tv_serial);
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
        tvCheckinNeeded = findViewById(R.id.act071_tv_checkin_needed);
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
                                    if (hasImageFileChanged) {
                                        if(photo.exists()) {
                                            copyFiles(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath,
                                                    ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);

                                            photo.delete();
                                        }else{
                                            if(mPresenter.fileExists(actionPhotoLocalPath)) {
                                                File originalPhoto = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                                                originalPhoto.delete();
                                            }
                                        }
                                    }
                                    setDataToObj();
                                    if(mPresenter.updateTicketAction(mTicketCtrl)){
                                        deletePhotoFile(TEMP_SUFIX_FILE + actionPhotoLocalPath);
                                        mPresenter.execTicketSave();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    ToolBox_Inf.registerException(e);
                                    showAlert(hmAux_Trans.get("alert_error_on_save_photo_ttl"),
                                            hmAux_Trans.get("alert_error_on_save_photo_msg"),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                    ;

                                }
                            }
                        },
                        1
                    );
                }
            };
        }
    }

    private void copyFiles(String fromFile, String toFile) throws IOException {

        File src = new File(fromFile);
        File dst = new File(toFile);

        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }

//        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
//        File tempImageFile = new File(toFile);
//        saveBitmapToFile(bitmap, tempImageFile);
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
                //Se apagou a foto, limpa photo_code
                mTicketCtrl.getAction().setAction_photo_code(null);
            } else {
                //LUCHE - 27/12/2019
                //Após a implementação do getAction_photo_code para identificar se houve mudança na foto,
                //quando o usr adicionar ou alterar a foto, seta campo como 0 para saber que no retorno
                //do save, a foto deve ser mantida e apenas o photo_code deve ser atualizado
                //
                mTicketCtrl.getAction().setAction_photo_code(0);
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
                    checkPostTicketSaveFlow();
                }else{
                    updateActionData();
                }
                //
                show.dismiss();
            }
        });
    }
    @Override
    public void checkPostTicketSaveFlow() {
        if(isScheduledTicket()){
            mPresenter.definePostTicketSaveFlow(
                mSchedulePrefix,
                mScheduleCode,
                mScheduleExec
            );
        }else {
            mPresenter.definePostTicketSaveFlow(
                mTicketCtrl.getTicket_prefix(),
                mTicketCtrl.getTicket_code()
            );
        }
    }

    @Override
    public void updateTicketPk(int mPrefix, int mCode) {
        this.mActionPrefix = mPrefix;
        this.mActionCode = mCode;
        updateActionData();
    }

    @Override
    public String getRequestingAct() {
        return requestingAct;
    }

    /**
     * LUCHE - 17/03/2020
     * Metodo que define o atributo mIsSchedule
     * @return - Verdadeiro se ticket prefix == 0 e ticket_code, ticket_seq e pk do agendamento
     */
    private boolean defineIsScheduleAttr(){
        return mActionPrefix == 0
               && mActionCode > 0
               && mActionSeq > 0
               && mSchedulePrefix > 0
               && mScheduleCode > 0
               && mScheduleExec > 0;
    }

    @Override
    public boolean isScheduledTicket() {
        return mIsSchedule;
    }

    @Override
    public int getmSchedulePrefix() {
        return mSchedulePrefix;
    }

    @Override
    public int getmScheduleCode() {
        return mScheduleCode;
    }

    @Override
    public int getmScheduleExec() {
        return mScheduleExec;
    }

    @Override
    public void postTicketSave() {
        updateActionData();
        //
        checkPostTicketSaveFlow();
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
        tvStatus.setTextColor(ToolBox_Inf.getStatusColorV2(context, mTicketCtrl.getCtrl_status()));
        defineSerialVisibility();
        definePathVisibility();
        tvTypeDesc.setText(mTypeDesc);
        tvSeq.setText(mPresenter.getFormattedSeqText(String.valueOf(mTicketCtrl.getTicket_seq())));
        definePartner();
        defineComments();
        //Define tamanho imageView., considerand que img já existe.
        defineActionPhotoMetrics(IV_PHOTO_EXISTS_WIDTH_PERCENT,IV_PHOTO_EXISTS_HEIGHT_PERCENT);
        defineActionPhoto();
        defineDoneInfo();
        defineCheckinAlert();

    }

    private void defineSerialVisibility() {
        tvSerialId.setVisibility(View.GONE);
        if(isScheduledTicket()){
            tvSerialId.setVisibility(View.VISIBLE);
            tvSerialId.setText(mTicketCtrl.getSerial_id());
        }
    }

    /*
        BARRIONUEVO 05-02-2020
        Adicao de informacao referentes ao checkin caso seja feito por outro usuario
     */
    private void defineCheckinAlert() {
        if(bDisableByCheckin && !mPresenter.hasCheckinAlertByStatus(mTicketCtrl.getCtrl_status())) {
            tvCheckinNeeded.setVisibility(View.VISIBLE);
            String checkin_user = mPresenter.hasCheckinBlockBy(mActionPrefix, mActionCode);
            if (checkin_user != null && !checkin_user.isEmpty()) {
                tvCheckinNeeded.setText(hmAux_Trans.get("checkin_info_lbl") + " " + checkin_user);
            } else {
                tvCheckinNeeded.setText(hmAux_Trans.get("checkin_needed_alert_lbl"));
            }
        }else{
            tvCheckinNeeded.setVisibility(View.GONE);
        }
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
            //Redefine tamanho imageView. Neste caso menor pois img não existe
            defineActionPhotoMetrics(IV_PHOTO_NOT_EXISTS_WIDTH_PERCENT,IV_PHOTO_NOT_EXISTS_HEIGHT_PERCENT);
            if(!bReadOnly) {
                deletePhotoFile(TEMP_SUFIX_FILE + actionPhotoLocalPath);
            }else{
                ivActionPhoto.setEnabled(false);
            }
            Drawable placeHolder;
            //Se photo name null, de fato não há foto, mas se photo name != null
            //significa q a imagem foi inserida por outro user
            if(mTicketCtrl.getAction().getAction_photo_name() == null) {
                placeHolder = getNoPhotoDrawable();
            }else{
                placeHolder = getResources().getDrawable(R.drawable.sand_watch_transp);
            }
            //
            ivActionPhoto.setImageDrawable(placeHolder);
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
                    try {
                        copyFiles(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath, path);
                    } catch (IOException e) {
                        ToolBox_Inf.registerException(e);
                    }
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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
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
                        //
                        applyTooLargeImgListener(finalPath);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        ivActionPhoto.setEnabled(false);
                        ivActionPhoto.setImageDrawable(getResources().getDrawable(R.drawable.sand_watch_transp));
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
        }
    }

    /**
     * LUCHE - 22/04/2020
     * Metodo retorna o drawable referente a sem foto.
     * Usa icone do resource e aplica filtro para cor azul.
     * @return Drawable do icone com a cor de filtro aplicada.
     */
    @NonNull
    private Drawable getNoPhotoDrawable() {
        Drawable placeHolder;
        placeHolder = getResources().getDrawable(R.drawable.ic_foto_ns_black);
        placeHolder.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
        return placeHolder;
    }

    private void applyTooLargeImgListener(String path) {
        if(photoClickListener != null && !ToolBox_Inf.isImageUnder4kLimit(path)){
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

    /**
     * LUCHE
     * <p></p>
     * Metodo que define o tamanho do imageView para usa 80% da largura e 30% da altura da tela do device
     * <p></p>
     * LUCHE - 23/04/2020
     * Modificado metodo para receber via parametro a porcentagem de largura e altura a serem usados,
     * pois, agora haverá uma diferença de tamanho entre foto tirada e sem foto.
     *
     * @param widthPercent Porcentagem da largura
     * @param heightPercent Porcentagem da altura
     */
    private void defineActionPhotoMetrics(double widthPercent, double heightPercent) {
        int[] percentMetrics = ToolBox_Inf.getPercentageWidthAndHeight(context,widthPercent,heightPercent);
        ivActionPhoto.getLayoutParams().width = percentMetrics[0];
        ivActionPhoto.getLayoutParams().height = percentMetrics[1];
    }

    private void setActinPhotoToView(String pathSufix) {
        if(mPresenter.fileExists(pathSufix)) {
            String path = ConstantBaseApp.CACHE_PATH_PHOTO + "/" + pathSufix;
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //Redefine tamanho imageView. Neste caso MAIOR pois img existe
            defineActionPhotoMetrics(IV_PHOTO_EXISTS_WIDTH_PERCENT,IV_PHOTO_EXISTS_HEIGHT_PERCENT);
            ivActionPhoto.setImageBitmap(bitmap);
        }else {
            //Redefine tamanho imageView. Neste caso MENOR pois img não existe
            defineActionPhotoMetrics(IV_PHOTO_NOT_EXISTS_WIDTH_PERCENT,IV_PHOTO_NOT_EXISTS_HEIGHT_PERCENT);
            ivActionPhoto.setImageDrawable(getNoPhotoDrawable());
        }
    }

    private void defineDoneInfo() {
        grDone.setVisibility(View.GONE);
        //
        if (ConstantBaseApp.SYS_STATUS_DONE.equalsIgnoreCase(mTicketCtrl.getCtrl_status())) {
            grDone.setVisibility(View.VISIBLE);
            tvDoneInfoVal.setText(mPresenter.getFormattedInfo(mTicketCtrl.getCtrl_end_date(),mTicketCtrl.getCtrl_end_user_name()));
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
        File sFile;

        if (bReadOnly) {
            sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
        }else{
            sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + TEMP_SUFIX_FILE + actionPhotoLocalPath);
        }

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
        bundle.putString(ConstantBase.FILE_AUTHORITIES, ConstantBase.AUTHORITIES_FOR_PROVIDER);
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
            deletePhotoFile(TEMP_SUFIX_FILE +actionPhotoLocalPath);
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
    public void callAct069(boolean useRequestingBundle) {
        Intent intent = new Intent(context, Act069_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Flag que indica que se deve usar vars do requestingBundle como bundle na chamada da act069
        //Usado somente quando o usuario veio da act069 para "executar uma ação" de agendamento e
        //desistiu
        if(useRequestingBundle){
            requestingBundle.remove(TK_TicketDao.TICKET_PREFIX);
            requestingBundle.remove(TK_TicketDao.TICKET_CODE);
            requestingBundle.remove(MD_Schedule_ExecDao.SCHEDULE_PREFIX);
            requestingBundle.remove(MD_Schedule_ExecDao.SCHEDULE_CODE);
            requestingBundle.remove(MD_Schedule_ExecDao.SCHEDULE_EXEC);
            requestingBundle.remove(TK_Ticket_CtrlDao.TICKET_SEQ);
            requestingBundle.remove(TK_TicketDao.TICKET_ID);
            requestingBundle.remove(TK_TicketDao.TYPE_DESC);
            requestingBundle.remove(Act070_Main.PARAM_DENIED_BY_CHECKIN);
            requestingBundle.remove(MD_Schedule_ExecDao.SCHEDULE_PK);
            //LUCHE - 27/03/2020 - Se status do historico, força requesting 14 pra reconfigurar act 069 o.O
            if(mPresenter.isClosedStatus(mTicketCtrl.getCtrl_status())){
                requestingBundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT014);
            }
            //
            intent.putExtras(requestingBundle);
        }
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

    @Override
    public void callAct017() {
        Intent intent = new Intent(context, Act017_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantBaseApp.ACT_SELECTED_DATE, requestingBundle.getString(ConstantBaseApp.ACT_SELECTED_DATE, null));
        bundle.putString(MD_Schedule_ExecDao.SCHEDULE_PK, requestingBundle.getString(MD_Schedule_ExecDao.SCHEDULE_PK, null));
        intent.putExtras(bundle);
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
