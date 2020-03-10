package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class ModuleFilterDialog extends AlertDialog {
    private final Context context;
    private HMAux hmAux_Trans_Extra;
    private final OnPositiveClickListener onPositiveClickListener ;
    private final HMAux hmAux_Trans;
    private TextView tv_title;
    private CheckBox chk_site;
    private CheckBox chk_form;
    private CheckBox chk_form_ap;
    private CheckBox chk_ticket;
    private Button btn_positive;

    public ModuleFilterDialog(@NonNull Context context, HMAux hmAux_Trans, HMAux hmAux_Trans_Extra, OnPositiveClickListener onPositiveClickListener) {
        super(context);
        this.context = context;
        this.hmAux_Trans = hmAux_Trans;
        this.hmAux_Trans_Extra = hmAux_Trans_Extra;
        this.onPositiveClickListener = onPositiveClickListener;
    }

    public interface OnPositiveClickListener{
        void onPositiveClick(
            CheckBox chk_site,
            CheckBox chk_form,
            CheckBox chk_form_ap,
            CheckBox chk_ticket
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_filter_dialog);
        //
        bindViews();
        //
        setLabels();
        //
        setConfig();
        //
        initValues();
        //
        iniActions();
    }

    private void bindViews() {
        tv_title = findViewById(R.id.module_filter_dialog_tv_title);
        chk_site = findViewById(R.id.schedule_filter_chk_site_logged);
        chk_form = findViewById(R.id.schedule_filter_chk_n_form);
        chk_form_ap = findViewById(R.id.schedule_filter_chk_n_form_ap);
        chk_ticket = findViewById(R.id.schedule_filter_chk_n_ticket);
        btn_positive = findViewById(R.id.module_filter_dialog_btn_ok);
    }

    private void setLabels() {
        tv_title.setText(hmAux_Trans.get("alert_filter_dialog_msg"));
        chk_site.setText(hmAux_Trans.get("lbl_site"));
        //
        chk_form.setText(hmAux_Trans_Extra.get("lbl_checklist"));
        chk_form_ap.setText(hmAux_Trans_Extra.get("lbl_form_ap"));
        chk_ticket.setText(hmAux_Trans_Extra.get("lbl_ticket"));
        btn_positive.setText(hmAux_Trans.get("sys_alert_btn_ok"));
    }

    private void setConfig() {
        chk_site.setTag(ConstantBaseApp.SCHEDULE_SITE_LOGGED_FILTER_PREFERENCE);
        chk_form.setTag(ConstantBaseApp.SCHEDULE_N_FORM_FILTER_PREFERENCE);
        chk_form_ap.setTag(ConstantBaseApp.SCHEDULE_N_FORM_AP_FILTER_PREFERENCE);
        chk_ticket.setTag(ConstantBaseApp.SCHEDULE_N_TICKET_FILTER_PREFERENCE);
        //
        setCancelable(true);
    }

    private void initValues() {
        chk_site.setChecked(ToolBox_Con.getBooleanPreferencesByKey(context, String.valueOf(chk_site.getTag()), false));
        if(ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_CHECKLIST,null)){
            chk_form.setChecked(
                ToolBox_Con.getBooleanPreferencesByKey(
                    context,
                    String.valueOf(chk_form.getTag()),
                    true
                )
            );
        }else{
            chk_form.setChecked(false);
            chk_form.setVisibility(View.GONE);
        }
        if(ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_AP,null)){
            chk_form_ap.setChecked(
                ToolBox_Con.getBooleanPreferencesByKey(
                    context,
                    String.valueOf(chk_form_ap.getTag()),
                    true
                )
            );
        }else{
            chk_form_ap.setChecked(false);
            chk_form_ap.setVisibility(View.GONE);
        }
        if(ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_MENU_TICKET,null)){
            chk_ticket.setChecked(
                ToolBox_Con.getBooleanPreferencesByKey(
                    context,
                    String.valueOf(chk_ticket.getTag()),
                    true
                )
            );
        }else{
            chk_ticket.setChecked(false);
            chk_ticket.setVisibility(View.GONE);
        }
    }

    private void iniActions() {
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPositiveClickListener != null){
                    onPositiveClickListener.onPositiveClick(
                        chk_site,
                        chk_form,
                        chk_form_ap,
                        chk_ticket
                    );
                }
                //
                dismiss();
            }
        });
        //
    }
}


