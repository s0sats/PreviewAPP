package com.namoadigital.prj001.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class SendResumeDialog extends AlertDialog {

    private Context context;
    private HMAux hmAux_trans;
    private String mResource_Code;
    private String mResourceName = "send_resume_dialog";
    private View nFormItem;
    private View serialItem;
    private View soItem;
    private View assetsItem;
    private View formApItem;
    private View expressSoItem;
    private View ticketItem;
    private TextView  tv_module_nform;

    protected SendResumeDialog(Context context, HMAux hmAux_trans) {
        super(context);
        this.hmAux_trans = hmAux_trans;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_send_resume);
        //
        loadTranslation();
        //
        setViewsById();
        //
        setViewVisibility();
    }

    private void setViewVisibility() {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_CHECKLIST, null)) {
            nFormItem.setVisibility(View.VISIBLE);
            nFormItem.findViewById(R.id.send_resume_pb);
            nFormItem.findViewById(R.id.send_resume_iv_ready);
            tv_module_nform = nFormItem.findViewById(R.id.send_resume_tv_module);
            nFormItem.findViewById(R.id.send_resume_amount);

        } else {
            nFormItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, null)) {
            serialItem.setVisibility(View.VISIBLE);
            serialItem.findViewById(R.id.send_resume_pb);
            serialItem.findViewById(R.id.send_resume_iv_ready);
            serialItem.findViewById(R.id.send_resume_tv_module);
            serialItem.findViewById(R.id.send_resume_amount);
        } else {
            serialItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            soItem.setVisibility(View.VISIBLE);
            soItem.findViewById(R.id.send_resume_pb);
            soItem.findViewById(R.id.send_resume_iv_ready);
            soItem.findViewById(R.id.send_resume_tv_module);
            soItem.findViewById(R.id.send_resume_amount);
        } else {
            soItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_IO, null)) {
            assetsItem.setVisibility(View.VISIBLE);
            assetsItem.findViewById(R.id.send_resume_pb);
            assetsItem.findViewById(R.id.send_resume_iv_ready);
            assetsItem.findViewById(R.id.send_resume_tv_module);
            assetsItem.findViewById(R.id.send_resume_amount);
        } else {
            assetsItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_AP, null)) {
            formApItem.setVisibility(View.VISIBLE);
            formApItem.findViewById(R.id.send_resume_pb);
            formApItem.findViewById(R.id.send_resume_iv_ready);
            formApItem.findViewById(R.id.send_resume_tv_module);
            formApItem.findViewById(R.id.send_resume_amount);
        } else {
            formApItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, ConstantBaseApp.PROFILE_MENU_SO_PARAM_DIRECT_EXPRESS_ORDER)) {
            expressSoItem.setVisibility(View.VISIBLE);
            expressSoItem.findViewById(R.id.send_resume_pb);
            expressSoItem.findViewById(R.id.send_resume_iv_ready);
            expressSoItem.findViewById(R.id.send_resume_tv_module);
            expressSoItem.findViewById(R.id.send_resume_amount);
        } else {
            expressSoItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET, null)) {
            ticketItem.setVisibility(View.VISIBLE);
            ticketItem.findViewById(R.id.send_resume_pb);
            ticketItem.findViewById(R.id.send_resume_iv_ready);
            ticketItem.findViewById(R.id.send_resume_tv_module);
            ticketItem.findViewById(R.id.send_resume_amount);
        } else {
            ticketItem.setVisibility(View.GONE);
        }

    }

    private void setViewsById() {
        nFormItem = findViewById(R.id.act005_send_resume_nform);
        serialItem = findViewById(R.id.act005_send_resume_serial);
        soItem = findViewById(R.id.act005_send_resume_so);
        assetsItem = findViewById(R.id.act005_send_resume_assets);
        formApItem = findViewById(R.id.act005_send_resume_form_ap);
        expressSoItem = findViewById(R.id.act005_send_resume_express_so);
        ticketItem = findViewById(R.id.act005_send_resume_ticket);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        //
        transList.add("alert_resume_nform");
        transList.add("alert_resume_serial");
        transList.add("alert_resume_so");
        transList.add("alert_resume_assets");
        transList.add("alert_resume_form_ap");
        transList.add("alert_resume_express_so");
        transList.add("alert_resume_ticket");
        transList.add("alert_package_details");
        transList.add("alert_site_lbl");
        transList.add("alert_zone_lbl");
        transList.add("alert_partner_lbl");
        transList.add("alert_multiple_service_added_ttl");
        transList.add("alert_multiple_service_added_msg");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                getContext(),
                ConstantBaseApp.APP_MODULE,
                mResourceName
        );
        //
        hmAux_trans = ToolBox_Inf.setLanguage(
                getContext(),
                ConstantBaseApp.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getContext()),
                transList
        );
    }
}
