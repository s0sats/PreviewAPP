package com.namoadigital.prj001.ui.act080;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.namoa_digital.namoa_library.ctls.FabMenu;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.ui.act075.Act075_Main;
import com.namoadigital.prj001.ui.act082.Act082_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act075.Act075_Main.PRODUCT_VIEW_ID;
import static com.namoadigital.prj001.ui.act075.Act075_Main.VIEW_PROFILE;

public class Act080_Main extends Base_Activity_Frag implements Act080_Main_Contract.I_View {

    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private FabMenu fabMenu;
    private boolean hasFABActive = false;
    private Act080_Main_Presenter mPresenter;
    private Bundle requestingBundle;
    private int mTkPrefix;
    private int mTkCode;
    private LinearLayout ll_open_photo;
    private LinearLayout ll_open_comments;
    private TextView tv_action_photo_lbl;
    private ImageView iv_action_photo;
    private TextView tv_action_comment_lbl;
    private TextView tv_action_comment_val;
    private String actionPhotoLocalPath;
    private boolean is_from_edit_header=false;
    private boolean is_from_edit_workgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act080_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        bindViews();
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void initActions() {
        iv_action_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCameraAct();
            }
        });
    }

    private void callCameraAct() {
        File sFile;
        sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);

        if (!sFile.exists()) {
            return;
        }
        //
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantBase.PID, iv_action_photo.getId());
        bundle.putInt(ConstantBase.PTYPE, 1);
        bundle.putString(ConstantBase.PPATH, actionPhotoLocalPath);
        bundle.putBoolean(ConstantBase.PEDIT, false);
        bundle.putBoolean(ConstantBase.PENABLED, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_GALLERY, false);
        bundle.putBoolean(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false);
        bundle.putString(ConstantBase.FILE_AUTHORITIES, ConstantBase.AUTHORITIES_FOR_PROVIDER);
        //
        Intent mIntent = new Intent(context, Camera_Activity.class);
        mIntent.putExtras(bundle);
        //
        context.startActivity(mIntent);
    }

    private void bindViews() {
        fabMenu = (FabMenu) findViewById(R.id.act080_fabMenu_anchor);
        ll_open_photo = findViewById(R.id.act080_ll_open_photo);
        ll_open_comments = findViewById(R.id.act080_ll_open_comment);
        tv_action_photo_lbl = findViewById(R.id.act080_tv_open_photo_lbl);
        iv_action_photo = findViewById(R.id.act080_iv_open_photo);
        tv_action_comment_lbl = findViewById(R.id.act080_tv_open_comment_lbl);
        tv_action_comment_val = findViewById(R.id.act080_tv_open_comment_val);
    }

    private void initVars() {
        mPresenter = new Act080_Main_Presenter(context, this, hmAux_Trans);
        //
        recoverIntentsInfo();
        //
        setLabels();
        //
        if (mTkPrefix <= 0 || mTkCode <= 0) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_ticket_parameter_error_ttl"),
                    hmAux_Trans.get("alert_ticket_parameter_error_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    },
                    0
            );
        }else {
            //
            mPresenter.getStepOrigin(mTkPrefix, mTkCode);
        }
        //
    }
    private void setLabels() {
        tv_action_photo_lbl.setText(hmAux_Trans.get("action_photo_lbl"));
        tv_action_comment_lbl.setText(hmAux_Trans.get("action_comment_lbl"));

    }

    /**
     * LUCHE - 16/12/2020
     * Metodo que retorna se esta em algum modo de edição.
     * @return Verdadeiro se ao menos um flag de edição ativa.
     */
    private boolean isInEditionMode() {
        return is_from_edit_header || is_from_edit_workgroup;
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT080
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setHeaderFragment(TK_Ticket tkTicket) {
        fm = getSupportFragmentManager();
        String step_end_date ="-";
        String step_end_user_nick ="-";

        TK_Ticket_Step originStep = tkTicket.getStep().get(0);
        step_end_date = originStep.getStep_end_date();
        step_end_user_nick = originStep.getStep_end_user_nick();
        //LUCHE - 22/06/2021

        String originLabel = originStep.getCtrl() != null && originStep.getCtrl().size() > 0
                                ? hmAux_Trans.get("schedule_action_origin_type_lbl")
                                : hmAux_Trans.get("schedule_ticket_origin_type_lbl") ;

        mFrgPipelineHeader = Frg_Pipeline_Header.newInstanceForOrigin(
                tkTicket,
                tkTicket.getTicket_id(),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(tkTicket.getOpen_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_site_code(),
                tkTicket.getOpen_site_desc(),
                tkTicket.getOpen_serial_id(),
                tkTicket.getOpen_product_desc(),
                originLabel,
                context.getResources().getColor(R.color.grid_header_normal),
                "",
                ToolBox_Inf.getFormattedTicketOriginDesc(tkTicket.getOrigin_type(), tkTicket.getOrigin_desc(),tkTicket.getType_desc()),
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(originStep.getStep_end_date()),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                ),
                tkTicket.getOpen_user_name()
        );
        //
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.act080_frg_pipeline_header, mFrgPipelineHeader, mFrgPipelineHeader.getTag());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void recoverIntentsInfo() {
        requestingBundle = getIntent().getExtras();
        //
        if (requestingBundle != null) {
            mTkPrefix = requestingBundle.getInt(TK_TicketDao.TICKET_PREFIX, -1);
            mTkCode = requestingBundle.getInt(TK_TicketDao.TICKET_CODE, -1);
            is_from_edit_header = requestingBundle.getBoolean(Act082_Main.FROM_EDIT_HEADER, false);
            is_from_edit_workgroup = requestingBundle.getBoolean(Act070_Main.PARAM_WORKGROUP_EDIT_MODE,false);
        }else{
            mTkPrefix = -1;
            mTkCode = -1;
            is_from_edit_header = false;
            is_from_edit_workgroup = false;
        }
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act080_title");
        transList.add("alert_ticket_parameter_error_ttl");
        transList.add("alert_ticket_parameter_error_msg");
        transList.add("to_product_lbl");
        transList.add("to_step_lbl");
        transList.add("to_origin_lbl");
        transList.add("to_work_group_edit_lbl");
        transList.add("to_header_edit_lbl");
        transList.add("schedule_action_origin_type_lbl");
        transList.add("action_photo_lbl");
        transList.add("action_comment_lbl");
        //
        transList.add("alert_wg_edit_need_connection_ttl");
        transList.add("alert_wg_edit_need_connection_msg");
        //
        transList.add("schedule_ticket_origin_type_lbl");
        transList.add("alert_origin_info_not_found_ttl");
        transList.add("alert_origin_info_not_found_msg");
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

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT080;
        mAct_Title = Constant.ACT080 + "_title";
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


    @Override
    public void loadTicketOrigin(TK_Ticket ticket) {
        setFabMenu(ticket);

        setHeaderFragment(ticket);

        setOpenFields(ticket);
    }

    private void setFabMenu(TK_Ticket mTicket) {
        if (!isInEditionMode()) {
            fabMenu.setVisibility(View.VISIBLE);
            ToolBox_Inf.setPipelineFabMenu(context, fabMenu, hmAux_Trans,
                    mTicket, new FabMenu.IFabMenu() {
                        @Override
                        public void onFabClick(View view) {

                            String tag = (String) view.getTag();
                            switch (tag){
                                case ConstantBaseApp.FAB_TO_PRODUCT_LBL:
                                    callAct075();
                                    break;
                                case ConstantBaseApp.FAB_TO_STEP_LBL:
                                    callAct070(false);
                                    break;
                                case ConstantBaseApp.FAB_TO_HEADER_EDIT_LBL:
                                    callAct082();
                                    break;
                                case ConstantBaseApp.FAB_TO_WORK_GROUP_EDIT_LBL:
                                    if(ToolBox_Con.isOnline(context)) {
                                        callAct070(true);
                                    }else{
                                        ToolBox.alertMSG(
                                                context,
                                                hmAux_Trans.get("alert_wg_edit_need_connection_ttl"),
                                                hmAux_Trans.get("alert_wg_edit_need_connection_msg"),
                                                null,
                                                0
                                        );
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onFabStatusChanged(boolean b) {
                            hasFABActive = b;
                        }
                    }
            );
        }else{
            fabMenu.setVisibility(View.GONE);
        }
    }

    private void setOpenFields(TK_Ticket ticket) {
        //LUCHE - 22/06/2021
        if(ticket.getStep() != null && ticket.getStep().size() > 0) {
            TK_Ticket_Step originStep = ticket.getStep().get(0);
            ////LUCHE - 22/06/2021
            if (originStep != null && originStep.getCtrl() != null && originStep.getCtrl().size() > 0) {
                TK_Ticket_Ctrl originCtrl = originStep.getCtrl().get(0);
                if (originCtrl != null) {
                    TK_Ticket_Action action = originCtrl.getAction();
                    if (action != null) {
                        if (action.getAction_comments() == null || action.getAction_comments().isEmpty()) {
                            ll_open_comments.setVisibility(View.GONE);
                        } else {
                            tv_action_comment_val.setText(action.getAction_comments());
                        }
                        actionPhotoLocalPath = action.getAction_photo_local();
                        if ((actionPhotoLocalPath == null || actionPhotoLocalPath.isEmpty())
                            && (action.getAction_photo_url() == null || action.getAction_photo_url().isEmpty())
                            && (action.getAction_photo_name() == null || action.getAction_photo_name().isEmpty())

                        ) {
                            ll_open_photo.setVisibility(View.GONE);
                        } else {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeFile(ConstantBase.CACHE_PATH_PHOTO + "/" + actionPhotoLocalPath);
                                if (bitmap == null) {
                                    setImagePlaceholder();
                                } else {
                                    iv_action_photo.setImageBitmap(bitmap);
                                }
                            } catch (NullPointerException e) {
                                setImagePlaceholder();
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }else{
                //LUCHE - 22/06/2021
                //Tratativa para o ticket "agendado" via automação.....
                ll_open_comments.setVisibility(View.GONE);
                ll_open_photo.setVisibility(View.GONE);
            }
        }else{
            //LUCHE - 22/06/2021
            //Esse cenario nunca deveria acontecer....
            //assim como agendamendo sem action até a versão 4.0.0.....
            ll_open_comments.setVisibility(View.GONE);
            ll_open_photo.setVisibility(View.GONE);
            //
            showAlert(
                hmAux_Trans.get("alert_origin_info_not_found_ttl"),
                hmAux_Trans.get("alert_origin_info_not_found_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }
            );
        }
    }

    private void setImagePlaceholder() {
        Drawable dPlaceholder = getResources().getDrawable(R.drawable.sand_watch_transp);
        iv_action_photo.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
        iv_action_photo.setImageDrawable(dPlaceholder);
    }


    private void showAlert(String ttl, String msg,@Nullable DialogInterface.OnClickListener listner) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            listner,
            0
        );
    }
    //
    @Override
    public void onBackPressed() {
        if (hasFABActive) {
            fabMenu.animateFAB();
        } else {
            if(is_from_edit_header){
                callAct082();
            }else {
                callAct070(false);
            }
        }
    }
    //
    private void callAct070(boolean forceEditMode) {
        Intent intent = new Intent(context, Act070_Main.class);
        if(forceEditMode){
            requestingBundle.putBoolean(Act070_Main.PARAM_FORCE_WORKGROUP_EDIT_MODE,true);
        }
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void callAct082() {
        Intent intent = new Intent(context, Act082_Main.class);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

    private void callAct075() {
        Intent intent = new Intent(context, Act075_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requestingBundle.putInt(VIEW_PROFILE, PRODUCT_VIEW_ID);
        intent.putExtras(requestingBundle);
        startActivity(intent);
        finish();
    }

}